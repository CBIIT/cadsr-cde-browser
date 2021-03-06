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

//java imports
import java.util.*;
import java.io.*;
import java.util.zip.*;
import javax.servlet.http.*;

// Framework imports
import oracle.cle.persistence.*;
import oracle.cle.util.statemachine.TransitionCondition;
import oracle.cle.process.ProcessInfo;
import oracle.cle.process.ProcessResult;
import oracle.cle.process.ProcessInfoException;
import oracle.cle.process.Service;
//import oracle.cle.process.ProcessConstants;
import oracle.clex.process.*;
import oracle.cle.process.ProcessParameter;

//Application imports
import gov.nih.nci.ncicb.cadsr.common.ProcessConstants;

public class DownloadExcel extends CreateGenericBinaryPage  {
  
  public DownloadExcel(){
    super();
    DEBUG = false;
  }

  protected void registerInfo(){
    super.registerInfo();
    try{
      registerStringParameter("EXCEL_FILE_NAME");
    }
    catch(ProcessInfoException pie){
      reportException(pie, DEBUG);
    }
  }

  protected String getContentType(){
    HttpServletResponse myResponse = null;
    ProcessInfo info = (ProcessInfo)getInfo("HTTPResponse");
    if (info!=null && info.isReady()) {
      myResponse = (HttpServletResponse)info.getValue();
    }
    myResponse.setHeader("Content-disposition",
                  "attachment; filename=" +
                  "CDEBrowser_SearchResults.xls" );
    return "application/vnd.ms-excel";
  }
  protected InputStream getBinaryInputStream(){
    return getExcelFileAsInputStream();
  }
  
  protected BufferedInputStream getExcelFileAsInputStream(){
    String excelFilename = getStringInfo("EXCEL_FILE_NAME");
    BufferedInputStream bis = null;
    try {
      FileInputStream fis = new FileInputStream(excelFilename);
      bis = new BufferedInputStream(fis);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return bis;
  }
  
}