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

package gov.nih.nci.ncicb.cadsr.cdebrowser.process;

import gov.nih.nci.ncicb.cadsr.common.CaDSRConstants;
import gov.nih.nci.ncicb.cadsr.common.CaDSRUtil;
import gov.nih.nci.ncicb.cadsr.common.ProcessConstants;
import gov.nih.nci.ncicb.cadsr.common.base.process.BasePersistingProcess;
import gov.nih.nci.ncicb.cadsr.common.cdebrowser.DESearchQueryBuilder;
import gov.nih.nci.ncicb.cadsr.common.cdebrowser.DataElementSearchBean;
import gov.nih.nci.ncicb.cadsr.common.util.DBUtil;
import gov.nih.nci.ncicb.cadsr.common.util.StringUtils;
import gov.nih.nci.ncicb.cadsr.common.util.TabInfoBean;
import gov.nih.nci.ncicb.cadsr.common.util.UserErrorMessage;
import gov.nih.nci.ncicb.cadsr.common.util.logging.Log;
import gov.nih.nci.ncicb.cadsr.common.util.logging.LogFactory;
import gov.nih.nci.ncicb.cadsr.common.xml.XMLGeneratorBean;
import gov.nih.nci.ncicb.cadsr.objectCart.CDECart;
import gov.nih.nci.ncicb.cadsr.objectCart.CDECartItem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import oracle.cle.process.ProcessInfoException;
import oracle.cle.process.Service;
import oracle.cle.util.statemachine.TransitionCondition;
import oracle.cle.util.statemachine.TransitionConditionException;
import oracle.xml.sql.OracleXMLSQLNoRowsException;


/**
 * @author Ram Chilukuri
 */
public class GetXMLDownload extends BasePersistingProcess {
  static final int BUFFER = 2048;
  private static Log log = LogFactory.getLog(GetXMLDownload.class .getName());

  public GetXMLDownload() {
    this(null);

    DEBUG = false;
  }

  public GetXMLDownload(Service aService) {
    super(aService);

    DEBUG = false;
  }

  /**
   * Registers all the parameters and results  (<code>ProcessInfo</code>) for
   * this process during construction.
   */
  public void registerInfo() {
    try {
      //registerParameterObject(ProcessConstants.ALL_DATA_ELEMENTS);
      registerParameterObject("desb");

      //registerResultObject(ProcessConstants.XML_DOCUMENT);
      registerStringParameter("SBREXT_DSN");
      registerStringParameter("src");
      registerStringParameter("XML_DOWNLOAD_DIR");
      registerStringResult("FILE_NAME");
      registerParameterObject("dbUtil");
      registerStringParameter("SBR_DSN");
      registerStringResult("ZIP_FILE_NAME");
      registerStringParameter("XML_PAGINATION_FLAG");
      registerStringParameter("XML_FILE_MAX_RECORDS");
      registerStringResult("FILE_TYPE");
      registerResultObject("tib");
      registerResultObject("uem");
      registerParameterObject(ProcessConstants.DE_SEARCH_QUERY_BUILDER);
    }
    catch (ProcessInfoException pie) {
      reportException(pie, true);
    }
    catch (Exception e) {
    }
  }

  /**
   * persist: called by start to do all persisting work for this process.  If
   * this method throws an exception, then the condition returned by the
   * <code>getPersistFailureCondition()</code> is set.  Otherwise, the
   * condition returned by <code>getPersistSuccessCondition()</code> is set.
   */
  public void persist() throws Exception {
    DBUtil dbUtil = null;
    XMLGeneratorBean xmlBean = null;
    Vector zipFileVec = new Vector(10);
    DataElementSearchBean desb = null;
    int rowcount;
    String xmlString = null;
    String paginate;
    int maxRecords;
    String zipFileSuffix = "";
    String zipFilename = "";
    String fileSuffix = "";
    String filename = "";
    DESearchQueryBuilder deSearch = null;
    String source = null;
    String where = "";
    Connection cn = null;
    
	String RAI = "";
	try
	{
		RAI = "'" + CaDSRUtil.getNciRegistryId() + "'";
	}
	catch ( IOException e) {
		RAI = GetExcelDownload.DEFAULT_RAI;
    }    

    try {
      source = getStringInfo("src");
      if ("deSearch".equals(source)) {
        desb = (DataElementSearchBean) getInfoObject("desb");

        deSearch =
          (DESearchQueryBuilder) getInfoObject(
            ProcessConstants.DE_SEARCH_QUERY_BUILDER);

        where = deSearch.getXMLQueryStmt();

        where = "DE_IDSEQ IN (" + where + ")";
      }else if ("cdeCart".equals(source)) {
        HttpServletRequest myRequest =
          (HttpServletRequest) getInfoObject("HTTPRequest");
        HttpSession userSession = myRequest.getSession(false);
        CDECart cart = (CDECart)userSession.getAttribute(CaDSRConstants.CDE_CART);
        Collection items = cart.getDataElements();
        CDECartItem item = null;
        boolean firstOne = true;
        StringBuffer whereBuffer = new StringBuffer("");
        Iterator itemsIt = items.iterator();
        while (itemsIt.hasNext()) {
        	item = (CDECartItem)itemsIt.next();
        	if (firstOne) {
        		whereBuffer.append("'" +item.getId()+"'");
        		firstOne = false;
        	}else{
        		whereBuffer.append(",'" +item.getId()+"'");
        	}
        }
        where = "DE_IDSEQ IN (" + whereBuffer.toString() + ")";

      }else {
        throw new Exception("No result set to download");
      }

      String stmt = " SELECT " + RAI + " as \"Data_Element_RAI\"" +
//    		            "," + RAI + " as \"Object_Class_RAI\"" +
//    		            "," + RAI + " as \"Property_RAI\"" +
//    		            "," + RAI + " as \"Value_Domain_RAI\"" +
//    		            "," + RAI + " as \"Representation_RAI\"" +
      					", PublicId " +  
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
                   " FROM sbrext.DE_XML_GENERATOR_VIEW ";

      xmlBean = new XMLGeneratorBean();
      
      //String sbrextJNDIName = getStringInfo("SBREXT_DSN");

      // Added for JBoss deployment

      //xmlBean.setDataSource(sbrextJNDIName);
      //Removed to use datasource
      //ApplicationParameters ap = ApplicationParameters.getInstance("cdebrowser");
      //cn = DBUtil.createOracleConnection(ap.getDBUrl(),ap.getUser(),ap.getPassword());
      dbUtil = new DBUtil();
      dbUtil.getOracleConnectionFromContainer();  //getConnectionFromContainer();  went back to original

      //Get Oracle Native Connection
      cn = dbUtil.getConnection();
      
      xmlBean.setConnection(cn);

      xmlBean.setQuery(stmt);
      xmlBean.setWhereClause(where);

      //xmlBean.setOrderBy("\"PreferredName\"");
      xmlBean.setRowsetTag("DataElementsList");
      xmlBean.setRowTag("DataElement");
      xmlBean.displayNulls(true);

      dbUtil = (DBUtil) getInfoObject("dbUtil");

      //String dsName = getStringInfo("SBR_DSN");
      dbUtil.getConnectionFromContainer();

      paginate = getStringInfo("XML_PAGINATION_FLAG");
      maxRecords = Integer.parseInt(getStringInfo("XML_FILE_MAX_RECORDS"));

      zipFileSuffix = dbUtil.getUniqueId("SBREXT.XML_FILE_SEQ.NEXTVAL");
      zipFilename =
        getStringInfo("XML_DOWNLOAD_DIR") + "DataElements" + "_" +
        zipFileSuffix + ".zip";

      if (paginate.equals("yes")) {
        xmlBean.setMaxRowSize(maxRecords);
        xmlBean.createOracleXMLQuery();

        while ((xmlString = xmlBean.getNextPage()) != null) {
          fileSuffix = dbUtil.getUniqueId("SBREXT.XML_FILE_SEQ.NEXTVAL");

          if (fileSuffix == null) {
            throw new Exception("Error generating file suffix");
          }

          filename =
            getStringInfo("XML_DOWNLOAD_DIR") + "DataElements" + "_" +
            fileSuffix + ".xml";
          writeToFile(StringUtils.updateXMLDataForSpecialCharacters(xmlString), filename);
          zipFileVec.addElement(filename);
        }
      }else {
        xmlString = xmlBean.getXMLString(cn);
        fileSuffix = dbUtil.getUniqueId("SBREXT.XML_FILE_SEQ.NEXTVAL");

        if (fileSuffix == null) {
          throw new Exception("Error generating file suffix");
        }

        filename =
          getStringInfo("XML_DOWNLOAD_DIR") + "DataElements" + "_" +
          fileSuffix + ".xml";
        writeToFile(StringUtils.updateXMLDataForSpecialCharacters(xmlString), filename);
        setResult("FILE_TYPE", "xml");

        //createZipFile(filename,zipFilename);
      }

      xmlString = null;
      setCondition(SUCCESS);
    }
    catch (OracleXMLSQLNoRowsException e) {
      createZipFile(zipFileVec, zipFilename);
    }
    catch (Exception ex) {
      TabInfoBean tib;
      UserErrorMessage uem;
      HttpServletRequest myRequest = null;

      try {
        tib = new TabInfoBean("cdebrowser_error_tabs");
        myRequest = (HttpServletRequest) getInfoObject("HTTPRequest");
        tib.processRequest(myRequest);

        if (tib.getMainTabNum() != 0) {
          tib.setMainTabNum(0);
        }

        uem = new UserErrorMessage();
        uem.setMsgOverview("Application Error");
        uem.setMsgText(
          "An unknown application error has occured. Please re-start the "+
          "download operation");
        setResult("tib", tib);
        setResult("uem", uem);
        setCondition(FAILURE);
        throw ex;
      }
      catch (TransitionConditionException tce) {
        reportException(tce, DEBUG);
      }
      catch (Exception e) {
        reportException(e, DEBUG);
      }

      reportException(ex, DEBUG);
    }
    finally {
      try {
        dbUtil.returnConnection();
        xmlBean.closeResources();

        //setCondition(FAILURE);
      }
      catch (Exception e) {
        log.debug("Error while Closing connections");
        //e.printStackTrace();
      }
    }
  }

  protected TransitionCondition getPersistSuccessCondition() {
    return getCondition(SUCCESS);
  }

  protected TransitionCondition getPersistFailureCondition() {
    return getCondition(FAILURE);
  }

  private void writeToFile(
    String xmlStr,
    String fn) throws Exception {
    FileOutputStream newFos;
    DataOutputStream newDos;
    BufferedOutputStream bos;
    
    try {
      newFos = new FileOutputStream(fn);
      //newDos = new DataOutputStream(newFos);
      //newDos.writeBytes(xmlStr + "\n");
      //newDos.close();
            
      bos = new BufferedOutputStream(newFos);
      bos.write(xmlStr.getBytes("UTF-8"));
      bos.close();
      setResult("FILE_NAME", fn);
    }
    catch (java.io.IOException e) {
      e.printStackTrace();
      throw e;
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

      out.close();
      setResult("FILE_NAME", zipFilename);
      setResult("FILE_TYPE", "zip");
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
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

      out.close();
      setResult("FILE_NAME", zipFilename);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
  }

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
