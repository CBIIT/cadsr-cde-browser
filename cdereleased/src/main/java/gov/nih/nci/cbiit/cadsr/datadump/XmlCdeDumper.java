package gov.nih.nci.cbiit.cadsr.datadump;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;

import oracle.xml.sql.OracleXMLSQLNoRowsException;
@Service
public class XmlCdeDumper extends AbstractCDEDumper {
	private static int BUFFER = 102400;
	Connection connection;
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	@Override
	public String getFileNamePrefix() {
		return "xml_cde_";
	}

	public void doDump() {
		try {
			Connection con = connection;      
			XMLGeneratorBean xmlBean = getXMLGeneratorBean(con);

			String xmlString = "";
			String strDataDirectory = getDirectory("xml_dir");//ends with a path separator
			List<String> zipFiles = new ArrayList<String>(32);
			String timeStr = getTimeStr();
			try {
				int i = 0;
				while ((xmlString = xmlBean.getNextPage()) != null) {
					++i;
					String currFilePath = strDataDirectory + getFileNamePrefix() + timeStr + "_" + i + ".xml";
					System.out.println("new file: " + currFilePath);
					writeToFile(xmlString, currFilePath);
					zipFiles.add(currFilePath);
					xmlString = null;
					System.gc();
				}
			} 
			catch (OracleXMLSQLNoRowsException e) {
				createZipFile(zipFiles, strDataDirectory + getFileNamePrefix() + timeStr + ".zip");
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private XMLGeneratorBean getXMLGeneratorBean(Connection con) throws Exception {
		XMLGeneratorBean xmlBean = new XMLGeneratorBean();
		xmlBean.setConnection(con);

//		String qry = " SELECT PublicId , LongName ,  PreferredName  ,  PreferredDefinition  ,  Version  ,  WorkflowStatus  ,  ContextName  ,  ContextVersion  ,  Origin  ,  RegistrationStatus  ,  DataElementConcept  ,  ValueDomain  ,  ReferenceDocumentsList  ,  ClassificationsList  ,  AlternateNameList  ,  DataElementDerivation   FROM sbrext.DE_XML_GENERATOR_VIEW where workflowstatus not like ('%RETIRED%') and upper(contextname) not in ('TEST', 'TRAINING')";
		String qry = " SELECT PublicId , LongName ,  PreferredName  ,  PreferredDefinition  ,  Version  ,  WorkflowStatus  ,  ContextName  ,  ContextVersion  ,  Origin  ,  RegistrationStatus  ,  DataElementConcept  ,  ValueDomain  ,  ReferenceDocumentsList  ,  ClassificationsList  ,  AlternateNameList  ,  DataElementDerivation   FROM sbrext.DE_CDE1_XML_GENERATOR_VIEW where workflowstatus not like ('%RETIRED%') and upper(contextname) not in ('TEST', 'TRAINING')";

		xmlBean.setQuery(qry);
		System.out.println("XML Query: " + qry);

		xmlBean.setRowsetTag("DataElementsList");
		xmlBean.setRowTag("DataElement");
		xmlBean.displayNulls(true);

		xmlBean.setMaxRowSize(5000);
		xmlBean.createOracleXMLQuery();

		return xmlBean;
	}

	private void writeToFile(String xmlStr, String fn) throws Exception {
		try {
			FileOutputStream newFos = new FileOutputStream(fn);
			DataOutputStream newDos = new DataOutputStream(newFos);
			newDos.writeBytes(xmlStr + "\n");
			newDos.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private void createZipFile(List<String> fileList, String zipFilename) throws Exception {
		BufferedInputStream origin = null;
		FileOutputStream dest = null;
		ZipOutputStream out = null;
		int count;
		try {
			dest = new FileOutputStream(zipFilename);
			out = new ZipOutputStream(new BufferedOutputStream(dest));

			byte[] data = new byte[BUFFER];

			for (int i = 0; i < fileList.size(); ++i) {
				FileInputStream fi = new FileInputStream(fileList.get(i));

				origin = new BufferedInputStream(fi, BUFFER);
				File file = new File(fileList.get(i));
				String fileName = file.getName();
				ZipEntry entry = new ZipEntry(fileName);
				out.putNextEntry(entry);

				while ((count = origin.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				}

				origin.close();
			}
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		finally {
			out.close();
		}
	}

	public static void main(String[] args) {
		new XmlCdeDumper().doDump();
	}
}