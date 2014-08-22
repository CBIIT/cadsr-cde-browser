/*L
 * Copyright SAIC-F Inc.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cde-browser/LICENSE.txt for details.
 *
 * Portions of this source file not modified since 2008 are covered by:
 *
 * Copyright 2000-2008 Oracle, Inc.
 *
 * Distributed under the caBIG Software License.  For details see
 * http://ncip.github.com/cadsr-cde-browser/LICENSE-caBIG.txt
 */

package gov.nih.nci.ncicb.cadsr.common.downloads.impl;

import gov.nih.nci.ncicb.cadsr.common.downloads.GetXMLDownload;
import gov.nih.nci.ncicb.cadsr.common.util.CDEBrowserParams;
import gov.nih.nci.ncicb.cadsr.common.util.ConnectionHelper;
import gov.nih.nci.ncicb.cadsr.common.util.DBUtil;
import gov.nih.nci.ncicb.cadsr.common.util.logging.Log;
import gov.nih.nci.ncicb.cadsr.common.util.logging.LogFactory;
import gov.nih.nci.ncicb.cadsr.common.xml.XMLGeneratorBean;
import gov.nih.nci.ncicb.cadsr.objectCart.CDECart;
import gov.nih.nci.ncicb.cadsr.objectCart.CDECartItem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/**
 * @author Ram Chilukuri
 */
public class GetXMLDownloadImpl implements GetXMLDownload{
	static final int BUFFER = 2048;
	private static Log log = LogFactory.getLog(GetXMLDownloadImpl.class .getName());
	private String jndiName = null;
	private String source;
	private String where;
	private String fileName = "";

  public GetXMLDownloadImpl() {
	  super();
  }
	public void generateXMLForCDECart(CDECart cart, String src, String _jndiName) throws Exception 
	{
		if (_jndiName == null) {
	    	throw new Exception("JNDI name cannot be null");  
	    }
		
		Collection items = cart.getDataElements();
		CDECartItem item = null;
		boolean firstOne = true;
		StringBuffer whereBuffer = new StringBuffer("");
		Iterator itemsIt = items.iterator();

		while (itemsIt.hasNext()) {
			item = (CDECartItem) itemsIt.next();

			if (firstOne) {
				whereBuffer.append("'" + item.getId() + "'");

				firstOne = false;
			}
			else
			{
				whereBuffer.append(",'" + item.getId() + "'");
			}
		}

		where = whereBuffer.toString();
		source = src;
		jndiName = _jndiName;
		generateXMLFile();
	}

	public void generateXMLForDESearch(String sWhere, String src, String _jndiName) throws Exception 
	{
		if (_jndiName == null) {
			throw new Exception("JNDI name cannot be null");  
	    }
		
		where = sWhere;
		source = src;
		jndiName = _jndiName;
		generateXMLFile();
	}

	private String getSQLStatement()
	{
	    String stmt = " SELECT PublicId " +
	          ", LongName "+
	          ",  PreferredName  "+
	          ",  PreferredDefinition  "+
	          ",  Version  "+
	          ",  WorkflowStatus  "+
	          ",  ContextName  "+
	          ",  ContextVersion  "+
	          ",  Origin  "+
	          ",  RegistrationStatus  "+
	          ",  DataElementConcept  "+
	          ",  ValueDomain  " +
	          ",  ReferenceDocumentsList  " +
	          ",  ClassificationsList  " +
	          ",  AlternateNameList  " +                    
	          ",  DataElementDerivation  " +
	          " FROM sbrext.DE_CDE1_XML_GENERATOR_VIEW ";

	    stmt +=  "WHERE DE_IDSEQ IN " +	" ( " + where + " )  ";
		return stmt;
	}
	
	@SuppressWarnings("static-access")
	public String getFileName(String prefix)
	{
		Connection cn = null;
		
		try {
			if (fileName.equals(""))
			{
				DBUtil dbutil = new DBUtil();
				
				ConnectionHelper connHelper = new ConnectionHelper(jndiName);
			    cn = connHelper.getConnection();
			    
			    if (cn == null) {
			    	throw new Exception("Cannot get the connection for the JNDI name ["+jndiName+"]");
			    }
			    
				String zipFileSuffix = dbutil.getUniqueId(cn, "SBREXT.XML_FILE_SEQ.NEXTVAL");
				String downLoadDir = CDEBrowserParams.getInstance().getXMLDownloadDir();
				if (prefix.equals(""))
					prefix = ".xml";
				fileName =	downLoadDir + "DataElements" + "_" + zipFileSuffix + "." + prefix;
			}
		} catch (Exception e) {
			log.error("Unable to determine the file name ", e);
		} finally {
			if (cn != null) {
				try { cn.close(); } catch(Exception e) {}
			}
		}
		return fileName;
	}
	
	public void setFileName(String sfile)
	{
		fileName = sfile;
	}
	
	//generate the workbook and format it and crate the header
	private void generateXMLFile() throws Exception
	{
    XMLGeneratorBean xmlBean = null;
    Connection cn = null;
    
    try {      
      String xmlString = null;
      
      xmlBean = new XMLGeneratorBean();
      xmlBean.setQuery(getSQLStatement());

      xmlBean.setRowsetTag("DataElementsList");
      xmlBean.setRowTag("DataElement");
      xmlBean.displayNulls(true);
      String mrec = CDEBrowserParams.getInstance().getXMLFileMaxRecords();
      int maxRecords = 10;
      if (mrec != null && !mrec.equals(""))
        maxRecords = Integer.parseInt(mrec);
      String paginate = CDEBrowserParams.getInstance().getXMLPaginationFlag();
      if (paginate == null)
    	  paginate = "no";
      
	  ConnectionHelper connHelper = new ConnectionHelper(jndiName);
      cn = connHelper.getConnection();
      
      if (cn == null) {
    	  throw new Exception("Cannot get the connection for the JNDI name ["+jndiName+"]");
      }

      fileName = getFileName("xml");
      if (paginate.equals("yes")) {
    	Vector zipFileVec = new Vector(10);
        xmlBean.setMaxRowSize(maxRecords);
        xmlBean.createOracleXMLQuery();

        while ((xmlString = xmlBean.getNextPage()) != null) {
          fileName = getFileName("xml");
          writeToFile(xmlString, fileName);
          zipFileVec.addElement(fileName);
          //get a new file
          fileName = "";
        }
        if (zipFileVec.size() > 0)
        	createZipFile(zipFileVec, getFileName("zip"));
      }
      else {
        xmlString = xmlBean.getXMLString(cn);
        writeToFile(xmlString, fileName);
      }

      xmlString = null;
    }
    catch (Exception ex) {
        log.error("An unknown application error has occured. Please re-start the "+
          "download operation", ex);
        throw ex;
    }
    finally {
      try {
    	if (cn != null) {
    		cn.close();
    	}
      }
      catch (Exception e) {
        log.error("Error while Closing connections", e);
      }
    }
  }

  private void writeToFile(
    String xmlStr,
    String fn) throws Exception {
    FileOutputStream newFos = null;
    DataOutputStream newDos = null;

    try {
      newFos = new FileOutputStream(fn);
      newDos = new DataOutputStream(newFos);
      newDos.writeBytes(xmlStr + "\n");
      //setResult("FILE_NAME", fn);
    }
    catch (Exception e) {
      log.error("Error while writing to file. ", e);
      throw e;
    }
    finally
    {
    	if (newDos != null)
    		newDos.close();
    }
  }

  private void createZipFile(
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
    }
    catch (Exception ex) {
    	log.error("Error while writing to xip file. ", ex);
    	throw ex;
    }
    finally
    {
    	if (out != null)
    		out.close();
    }
  }

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
    }
    catch (Exception ex) {
    	log.error("Error while writing to xip file. ", ex);
    	throw ex;
    }
    finally
    {
    	if (out != null)
    		out.close();
    }
  }

}
