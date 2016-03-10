/*
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */

package gov.nih.nci.cadsr.download;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Collection;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import gov.nih.nci.cadsr.common.util.StringUtils;
import gov.nih.nci.cadsr.service.ClientException;
import oracle.xml.sql.dataset.OracleXMLDataSetExtJdbc;
import oracle.xml.sql.query.OracleXMLQuery;

public class GetXmlDownload extends JdbcDaoSupport implements GetXmlDownloadInterface  {
	private Logger logger = LogManager.getLogger(GetXmlDownload.class.getName());

	//these are parameterized from cdeBrowser.server.properties
	@Value("${downloadDirectory}")
	private String localDownloadDirectory;  //"/local/content/cdebrowser/output/"
	@Value("${downloadFileNamePrefix}")
	private String fileNamePrefix;	
	
	public void setLocalDownloadDirectory(String localDownloadDirectory) {
		this.localDownloadDirectory = localDownloadDirectory;
	}

	public void setFileNamePrefix(String fileNamePrefix) {
		this.fileNamePrefix = fileNamePrefix;
	}
  
	@Autowired
	public GetXmlDownload(DataSource dataSource) {
		setDataSource(dataSource);
	}
	private static final String rowsetTag = "DataElementsList";
	private static final String rowTag = "DataElement";
	private static final int maxRecords = 1000;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.nih.nci.cadsr.download.GetXMLDownloadInterface#persist(java.util.Collection, java.lang.String, java.lang.String)
	 */
	@Override
	public String persist(Collection<String> itemIds, String RAI, String source) throws Exception {
		String xmlString = null;
		String fileSuffix = "";
		String filename = "";
		String where = "";
		Connection cn = null;

		try {

			where = "where DE_IDSEQ IN (" + buildSqlInCondition(itemIds) + ")";

			String stmt = " SELECT '" + RAI + "' as \"RAI\"" +
					// "," + RAI + " as \"Object_Class_RAI\"" +
					// "," + RAI + " as \"Property_RAI\"" +
					// "," + RAI + " as \"Value_Domain_RAI\"" +
					// "," + RAI + " as \"Representation_RAI\"" +
			", PublicId " + 
			", LongName " + 
			",  PreferredName  " + 
			",  PreferredDefinition  " + 
			",  Version  "+ 
			",  WorkflowStatus  " + 
			",  ContextName  " + 
			",  ContextVersion  " + 
			",  Origin  " + 
			",  RegistrationStatus  " + 
			",  DataElementConcept  " + 
			",  ValueDomain  " + 
			",  ReferenceDocumentsList  " + 
			",  ClassificationsList  " + 
			",  AlternateNameList  " + 
			",  DataElementDerivation  " + 
			" FROM sbrext.DE_CDE1_XML_GENERATOR_VIEW ";

			// Get Oracle Native Connection
			cn = getConnection();// we either get a connection, or an Exception is thrown; no null is returned
			
			Connection oracleConn = cn.getMetaData().getConnection();//get underlying Oracle connection
			
			xmlString = getXMLString(oracleConn, stmt, where, true);
			
			fileSuffix = generateXmlFileId();

			if (fileSuffix == null) {
				throw new Exception("Error generating file suffix");
			}

			filename = buildDownloadAbsoluteFileName(fileSuffix);

			writeToFile(StringUtils.updateXMLDataForSpecialCharacters(xmlString), filename);

			return fileSuffix;
		} 
		catch (Exception ex) {
			logger.error("Exception caught in Generate XML File", ex);
			throw ex;
		} 
		finally {
			if (cn != null) {
				releaseConnection(cn);
			}
		}
	}

	public String getXMLString(Connection oracleConn, String stmt, String where, boolean showNull) throws Exception {

		String xmlString = "";
		OracleXMLDataSetExtJdbc dset = null;
		OracleXMLQuery xmlQuery = null;
		try {
			String sqlQuery = stmt + where;

			if (logger.isTraceEnabled()) {
				logger.trace("Sql Stmt: " + sqlQuery);
			}
			dset = new OracleXMLDataSetExtJdbc(oracleConn, stmt);
			xmlQuery = new OracleXMLQuery(dset);
			
			//We still decide if we want to use default Date format, or to make it custom
			//xmlQuery.setDateFormat(arg0); https://docs.oracle.com/cd/A87860_01/doc/appdev.817/a83730/arx09xsj.htm
			
			xmlQuery.setEncoding("UTF-8");
			xmlQuery.useNullAttributeIndicator(showNull);

			xmlQuery.setRowsetTag(rowsetTag);

			xmlQuery.setRowTag(rowTag);

			xmlQuery.setMaxRows(maxRecords);

			xmlString = xmlQuery.getXMLString();
			logger.trace(xmlString);

			return xmlString;
		} 
		catch (Exception e) {
			logger.error("getXMLString() error: ", e);
			throw e;
		} 
		finally {
			try {
				if (dset != null) {
					dset.close();
				}				
			}
			catch (Exception e) {
				logger.debug("Error when closing OracleXMLDataSetExtJdbc: " + e.getMessage());
			}
			try {
				if (xmlQuery != null) {
					xmlQuery.close();
				}				
			}
			catch (Exception e) {
				logger.debug("Error when closing OracleXMLQuery: " + e.getMessage());
			}
		}
	}
	
	public String buildDownloadAbsoluteFileName(String fileSuffix) {
		String excelFilename = localDownloadDirectory + fileNamePrefix + fileSuffix + ".xml";
		return excelFilename;
	}
	
	protected void writeToFile(String xmlStr, String fn) throws Exception {
		FileOutputStream newFos = null;
		BufferedOutputStream bos = null;

		try {
			newFos = new FileOutputStream(fn);
			bos = new BufferedOutputStream(newFos);
			bos.write(xmlStr.getBytes("UTF-8"));
		} 
		finally {
			if (bos != null) {
				try {
					bos.flush();
					bos.close();
				} 
				catch (Exception e) {
					logger.debug("Unable to close underlying stream due to the following error ", e);
				}
			}
			if (newFos != null) {
				try {
					newFos.close();
				} 
				catch (Exception e) {
					logger.debug("Unable to close temporarily file due to the following error ", e);
				}
			}
		}
	}

	protected String generateXmlFileId() {
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		String fileId = jdbcTemplate.queryForObject("SELECT SBREXT.XML_FILE_SEQ.NEXTVAL from dual", new Object[]{}, String.class);
		return fileId;
	}
	
	protected String buildSqlInCondition(final Collection<String> itemIds) throws Exception {
		if ((itemIds != null) && (!(itemIds.isEmpty())) && (itemIds.size() <= 1000)) {
			
			StringBuilder sb = new StringBuilder();
			for (String item : itemIds) {
				sb.append("'" + item + "', ");
			}
			int len = sb.length();
			return sb.toString().substring(0, len - 2);
		}
		else if ((itemIds == null) || (!(itemIds.isEmpty()))) {
			throw new ClientException("No item ID to download");
		}
		else {
			throw new ClientException("XML download is restricted to 1,000 item IDs");
		}
	}
	/*
	 * ========Legacy code below==============================
	 * I do not see if there is a requirement to use zip files
	 * =======================================================
	 */
	static final int BUFFER = 2048;

	@SuppressWarnings("unused")
	  private void createZipFile(
	    @SuppressWarnings("rawtypes") 
	    Vector fileList,
	    String zipFilename) throws Exception {
	    BufferedInputStream origin = null;
	    FileOutputStream dest = null;
	    ZipOutputStream out = null;

	    try {
	      dest = new FileOutputStream(zipFilename);
	      out = new ZipOutputStream(new BufferedOutputStream(dest));

	      byte[] data = new byte[BUFFER];

	      for (int i = 0; i < fileList.size(); i++) {
	        FileInputStream fi =
	          new FileInputStream((String) fileList.elementAt(i));
	        origin = new BufferedInputStream(fi, BUFFER);

	        ZipEntry entry = new ZipEntry((String) fileList.elementAt(i));
	        out.putNextEntry(entry);

	        int count;

	        while ((count = origin.read(data, 0, BUFFER)) != -1) {
	          out.write(data, 0, count);
	        }

	        origin.close();
	      }

	      out.close();

	    }
	    catch (Exception ex) {
	      ex.printStackTrace();
	      throw ex;
	    }
	  }
	  @SuppressWarnings("unused")
	private void createZipFile(
	    String filename,
	    String zipFilename) throws Exception {
	    BufferedInputStream origin = null;
	    FileOutputStream dest = null;
	    ZipOutputStream out = null;

	    try {
	      dest = new FileOutputStream(zipFilename);
	      out = new ZipOutputStream(new BufferedOutputStream(dest));

	      byte[] data = new byte[BUFFER];

	      FileInputStream fi = new FileInputStream(filename);
	      origin = new BufferedInputStream(fi, BUFFER);

	      ZipEntry entry = new ZipEntry(filename);
	      out.putNextEntry(entry);

	      int count;

	      while ((count = origin.read(data, 0, BUFFER)) != -1) {
	        out.write(data, 0, count);
	      }

	      origin.close();

	      out.close();

	    }
	    catch (Exception ex) {
	      ex.printStackTrace();
	      throw ex;
	    }
	  }
	  @SuppressWarnings("unused")
	  private PrintWriter getPrintWriter(String newFilename)
	    throws Exception {
	    PrintWriter pw = null;

	    try {
	      pw = new PrintWriter(new BufferedWriter(new FileWriter(newFilename)));
	    }
	    catch (Exception ex) {
	      throw ex;
	    }

	    return pw;
	  }
}
