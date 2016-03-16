/*
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */

package gov.nih.nci.cadsr.download;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
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
//import oracle.xml.sql.dataset.OracleXMLDataSetExtJdbc;
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
	private static final String cdeXmlElement="<DataElement num=\"";
	private static final String closingXmlElement="</DataElementsList>";
	private static final String rowsetTag = "DataElementsList";
	private static final String rowTag = "DataElement";
	private static final int maxRecords = 1000;
	private static String stmtFormat = " SELECT '%s' as \"RAI\"" +
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
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.nih.nci.cadsr.download.GetXMLDownloadInterface#persist(java.util.Collection, java.lang.String, java.lang.String)
	 */
	@Override
	public String persist(final Collection<String> itemIds, String RAI, String source) throws Exception {
		String xmlString = null;
		String fileSuffix = "";
		String filename = "";
		Connection cn = null;
		
		checkInCondition(itemIds);
		
		BufferedWriter bw = null;
		
		try {
			String fromStmt = String.format(stmtFormat, RAI);
			
			// Get Oracle Native Connection
			cn = getConnection();// we either get a connection, or an Exception is thrown; no null is returned
			
			Connection oracleConn = cn.getMetaData().getConnection();//get underlying Oracle connection
			
			filename = buildDownloadAbsoluteFileName(fileSuffix = generateXmlFileId());
			
			int lastGroupNumber = calcNumberOfGroups(itemIds.size()) - 1 ;
			
			Iterator<String> iter = itemIds.iterator();
			
			String groupWhereInCond;
			String stmt;
			
			bw = new BufferedWriter(new FileWriter(filename));
			
			for (int groupId = 0; groupId <= lastGroupNumber; groupId++) {
				groupWhereInCond = buildSqlInCondition(iter, maxRecords); //"where DE_IDSEQ IN ('1','2','3',   , '1000')";
				
				stmt = fromStmt + groupWhereInCond;
				
				xmlString = getXMLString(oracleConn, stmt, true);
				
				if (groupId != lastGroupNumber) {
					xmlString = trimClosingXmlElement(xmlString);
				}
				
				if (groupId == 0) {
					bw.write(StringUtils.updateXMLDataForSpecialCharacters(xmlString));
					bw.flush();
				}
				else {
					writeGroupChildElements(xmlString, groupId, bw);
				}
			}

			return fileSuffix;
		} 
		catch (Exception ex) {
			logger.error("Exception caught in Generate XML File", ex);
			throw ex;
		} 
		finally {
			if (bw != null) {
				bw.flush();
				bw.close();
			}
			if (cn != null) {
				releaseConnection(cn);
			}
		}
	}
	protected String trimClosingXmlElement(String xmlString) {
		int lastPos = xmlString.indexOf(closingXmlElement);
		
		xmlString = xmlString.substring(0, lastPos);
		return xmlString;
	}
	
	protected void writeGroupChildElements(String xmlString, final int groupId, BufferedWriter bw) throws IOException {
		int firstReplacementPos = xmlString.indexOf("   " + cdeXmlElement);//Oracle uses 3 spaces for a child element identation
		if (firstReplacementPos < 0) {
			firstReplacementPos = xmlString.indexOf(cdeXmlElement);
		}
		xmlString = xmlString.substring(firstReplacementPos);
		
		writeGroupToFile(xmlString, groupId, bw);
	}
	
	protected void writeGroupToFile(String xmlToUpdate, final int groupId, BufferedWriter bw) throws IOException {
		int replaceNum = maxRecords*groupId;
		int curNum = 0;
		String line;
		BufferedReader bufReader = new BufferedReader(new StringReader(xmlToUpdate));
		int endPos;
		while( (line=bufReader.readLine()) != null )
		{
			if ((endPos = line.indexOf("num=\"")) < 0) {
				bw.write(StringUtils.updateXMLDataForSpecialCharacters(line));
				bw.newLine();
			}
			else {
				curNum++;
				bw.write(line, 0, endPos);
				bw.write("num=\"");
				bw.write("" + (replaceNum+curNum));
				bw.write("\">");
				bw.newLine();
			}
		}
		bw.flush();
	}
	//FIXME remove this method
	protected String updateCdeNumAttributes(String xmlToUpdate, final int groupId) {
		String targetPattern;
		String replacementPattern;
		int replaceNum;
		
		int increaseNum = maxRecords*groupId;
		String groupStart = cdeXmlElement;
		//fix
		for (int curNum = 1; curNum <= maxRecords; curNum++) {
			targetPattern = groupStart + curNum +"\"";//searching for <DataElement num="1"
			replaceNum = curNum + increaseNum;
			replacementPattern = groupStart + replaceNum + "\"";
			xmlToUpdate = xmlToUpdate.replace(targetPattern, replacementPattern);
		}
		return xmlToUpdate;
	}
	
	protected int calcNumberOfGroups(final int lengthOfCollection) {
		return (lengthOfCollection / maxRecords) + 
			(((lengthOfCollection % maxRecords) == 0)? 0 : 1);
	}
	
	public String getXMLString(Connection oracleConn, String sqlQuery, boolean showNull) throws Exception {

		String xmlString = "";
		//OracleXMLDataSetExtJdbc dset = null;
		OracleXMLQuery xmlQuery = null;
		try {
			if (logger.isTraceEnabled()) {
				logger.trace("Sql Stmt: " + sqlQuery);
			}
			//This is another way of creating OracleXMLQuery object; I keep it here for our information
			//dset = new OracleXMLDataSetExtJdbc(oracleConn, sqlQuery);
			//xmlQuery = new OracleXMLQuery(dset);
			
			xmlQuery = new OracleXMLQuery(oracleConn, sqlQuery);
			
			//We still decide if we want to use default Date format, or to make it custom
			//This is a format whioch we have in CDE Browser v4.0.5
			//xmlQuery.setDateFormat("M/d/yyyy H:m:s");
			
			//this is a new CDE Browser v5.1 format; we drop time
			xmlQuery.setDateFormat("yyyy-MM-dd");//java class SimpleDateFormat

			xmlQuery.setEncoding("UTF-8");
			xmlQuery.useNullAttributeIndicator(showNull);

			xmlQuery.setRowsetTag(rowsetTag);

			xmlQuery.setRowTag(rowTag);
			
			//we do not use this restriction
			//xmlQuery.setMaxRows(maxRecords);

			xmlString = xmlQuery.getXMLString();
			
			logger.trace(xmlString);

			return xmlString;
		} 
		catch (Exception e) {
			logger.error("getXMLString() error: ", e);
			throw e;
		} 
		finally {
//			try {
//				if (dset != null) {
//					dset.close();
//				}				
//			}
//			catch (Exception e) {
//				logger.debug("Error when closing OracleXMLDataSetExtJdbc: " + e.getMessage());
//			}
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
		FileWriter newFos = null;
		BufferedWriter bos = null;

		try {
			newFos = new FileWriter(fn, true);
			bos = new BufferedWriter(newFos);
			bos.write(xmlStr);
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

	protected String generateXmlFileId() throws Exception {
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		String fileId = jdbcTemplate.queryForObject("SELECT SBREXT.XML_FILE_SEQ.NEXTVAL from dual", new Object[]{}, String.class);

		if ((fileId == null) || (fileId.isEmpty())) {
			throw new Exception("Error generating file ID");
		}
		
		return fileId;
	}
	
	protected String buildSqlInCondition(Iterator <String> iter, int max) {

			StringBuilder sb = new StringBuilder("where DE_IDSEQ IN (");
			String currVal;
			for (int indx = 0; indx < max; indx++) {
				if (iter.hasNext()) {
					currVal = iter.next();
					sb.append("'" + currVal + "', ");
				}
				else break;
			}
			int len = sb.length();
			sb.setCharAt(len - 2, ')');
			return sb.toString().substring(0, len - 1);		
	}
	
	protected void checkInCondition(final Collection<String> itemIds) throws Exception {
		if ((itemIds == null) || (itemIds.isEmpty())) {
			throw new ClientException("Expected Download CDE IDs are not provided");
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
}
