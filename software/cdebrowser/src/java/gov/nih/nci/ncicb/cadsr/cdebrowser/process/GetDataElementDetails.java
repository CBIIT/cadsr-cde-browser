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

import gov.nih.nci.ncicb.cadsr.common.base.process.BasePersistingProcess;
import gov.nih.nci.ncicb.cadsr.common.cdebrowser.service.CDEBrowserService;
import gov.nih.nci.ncicb.cadsr.common.cdebrowser.userexception.DataElementNotFoundException;
import gov.nih.nci.ncicb.cadsr.common.cdebrowser.userexception.IllegalURLParametersException;
import gov.nih.nci.ncicb.cadsr.common.resource.DataElement;
import gov.nih.nci.ncicb.cadsr.common.resource.handler.DataElementHandler;
import gov.nih.nci.ncicb.cadsr.common.servicelocator.ApplicationServiceLocator;
import gov.nih.nci.ncicb.cadsr.common.util.AppScanValidator;
import gov.nih.nci.ncicb.cadsr.common.util.TabInfoBean;
import gov.nih.nci.ncicb.cadsr.common.util.UserErrorMessage;

import javax.servlet.http.HttpServletRequest;

import oracle.cle.persistence.HandlerFactory;
import oracle.cle.process.ProcessInfoException;
import oracle.cle.process.Service;
import oracle.cle.util.statemachine.TransitionCondition;
import oracle.cle.util.statemachine.TransitionConditionException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.log4j.Logger;


// java imports


public class GetDataElementDetails extends BasePersistingProcess {
  private Logger logger = Logger.getLogger(GetDataElementDetails.class);

  private String getFormattedDate() {
    Calendar cal = Calendar.getInstance();
    cal.getTime();
    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
    return  sdf.format(cal.getTime());
  }

  public GetDataElementDetails() {
    this(null);

    DEBUG = false;
  }

  public GetDataElementDetails(Service aService) {
    super(aService);

    DEBUG = false;
  }

  /**
   * Registers all the parameters and results  (<code>ProcessInfo</code>) for
   * this process during construction.
   */
  public void registerInfo() {
    try {
      registerStringParameter("p_de_idseq");
      registerStringResult("p_de_idseq");
      registerParameterObject("de");
      registerResultObject("de");
      registerResultObject("tib");
      registerStringParameter("queryDE");
      registerStringResult("queryDE");
      registerStringParameter("cdeId");
      registerStringParameter("version");
      registerStringResult("cdeId");
      registerStringResult("version");
      registerResultObject("uem");
      
      // TT #257 added support for derived data element
      registerParameterObject("derivedDe");
      registerResultObject("derivedDe");
    }
    catch (ProcessInfoException pie) {
      reportException(pie, true);
    }
  }

  /**
   * persist: called by start to do all persisting work for this process.  If
   * this method throws an exception, then the condition returned by the
   * <code>getPersistFailureCondition()</code> is set.  Otherwise, the
   * condition returned by <code>getPersistSuccessCondition()</code> is set.
   */
  public void persist() throws Exception {
    TabInfoBean tib = null;
    DataElement de = null;
    DataElementHandler dh = null;
    HttpServletRequest myRequest = null;

    String queryDE = getStringInfo("queryDE");
    String deIdseq = getStringInfo("p_de_idseq");   
    String cdeId = getStringInfo("cdeId");
    String version = getStringInfo("version");
    Object sessionId = getSessionId();
    myRequest = (HttpServletRequest) getInfoObject("HTTPRequest");
    boolean validElementId=false;
    if (deIdseq!=null)
    	validElementId=AppScanValidator.validateElementIdSequence(deIdseq);
    else if (cdeId!=null)
    	validElementId=AppScanValidator.validateElementPublicId(cdeId);

    if (!validElementId)
    {
    	Exception validationExp= new Exception("Invalid input parameters: p_de_idseq or cdeId ");
        try {
            UserErrorMessage uem;
            tib = new TabInfoBean("cdebrowser_error_tabs");
            myRequest = (HttpServletRequest) getInfoObject("HTTPRequest");
            tib.processRequest(myRequest);

            if (tib.getMainTabNum() != 0) {
              tib.setMainTabNum(0);
            }

            uem = new UserErrorMessage();
            uem.setMsgOverview("Input validation Error");
            uem.setMsgText(
              "Input validation error has occurred. Please re-try your request");

          logger.error( validationExp.toString());
          uem.setMsgTechnical(
                  "System Error. <b>System administrator:</b> The stack trace from the Exception was logged " +
                          "at this time: " + getFormattedDate() + ".<BR><BR>Please contact support for help.<BR><BR>");
//            uem.setMsgTechnical(
//              "<b>System Error:</b> Here is the stack " +
//              "trace from the Exception.<BR><BR>" + validationExp.toString() + "<BR><BR>");
            setResult("tib", tib);
            setResult("uem", uem);
            setCondition(FAILURE);
          }
          catch (TransitionConditionException tce) {
            reportException(tce, DEBUG);
          }

          reportException(validationExp, DEBUG);
          throw validationExp;
    }
    
    try {
      if (queryDE == null) {
        throw new IllegalURLParametersException("Incorrect URL parameters");
      }

      if (queryDE.equals("yes")) {
        dh = (DataElementHandler) HandlerFactory.getHandler(DataElement.class);

        if (deIdseq != null) {
          de = (DataElement) dh.findObject(deIdseq, sessionId);
        }
        else if ((cdeId != null) && (version != null)) {
          int icdeId = Integer.parseInt(getStringInfo("cdeId"));
          float fversion = Float.parseFloat(getStringInfo("version"));
          de = dh.findDataElementsByPublicId(icdeId, fversion, sessionId);
        }
        else {
          throw new IllegalURLParametersException("Incorrect URL parameters");
        }

      ApplicationServiceLocator  appServiceLocator =(ApplicationServiceLocator)
      myRequest.getSession().getServletContext().getAttribute(ApplicationServiceLocator.APPLICATION_SERVICE_LOCATOR_CLASS_KEY);
      
      CDEBrowserService cdeBrowserService = appServiceLocator.findCDEBrowserService();
      // add service to cdeBrowserService to retrieve the cs of alt names and alt def
      cdeBrowserService.populateDataElementAltNameDef(de);

      }
      else {
        de = (DataElement) getInfoObject("de");
      }

      tib = new TabInfoBean("cdebrowser_details_tabs");
      tib.processRequest(myRequest);

      if (tib.getMainTabNum() != 0) {
        tib.setMainTabNum(0);
      }

      setResult("tib", tib);
      setResult("de", de);
      setResult("queryDE", "no");
//      setResult("derivedDe", null);
      setResult("p_de_idseq", deIdseq);
      setResult("cdeId", cdeId);
      setResult("version", version);
      setCondition(SUCCESS);
    }
    catch (IllegalURLParametersException iex) {
      UserErrorMessage uem;

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
          "An application error occurred due to incorrect URL parameters. " +
          "Please specify correct URL parameters.");

        logger.error( iex.toString());
        uem.setMsgTechnical(
                "<b>System administrator:</b> The stack trace from the Exception was logged " +
                        "at this time: " + getFormattedDate() + ".<BR><BR>Please contact support for help.<BR><BR>");
//        uem.setMsgTechnical(
//          "<b>System administrator:</b> Here is the stack " +
//          "trace from the Exception.<BR><BR>" + iex.toString() + "<BR><BR>");
        setResult("tib", tib);
        setResult("uem", uem);
        setCondition(FAILURE);
      }
      catch (TransitionConditionException tce) {
        reportException(tce, DEBUG);
      }

      reportException(iex, DEBUG);
      throw new Exception("Incorrect URL parameters");
    }
    catch (DataElementNotFoundException dex) {
      UserErrorMessage uem;

      try {
        tib = new TabInfoBean("cdebrowser_error_tabs");
        myRequest = (HttpServletRequest) getInfoObject("HTTPRequest");
        tib.processRequest(myRequest);

        if (tib.getMainTabNum() != 0) {
          tib.setMainTabNum(0);
        }

        uem = new UserErrorMessage();
        uem.setMsgOverview("Database Error");
        uem.setMsgText(dex.getMessage());
        setResult("tib", tib);
        setResult("uem", uem);
        setCondition(FAILURE);
      }
      catch (TransitionConditionException tce) {
        reportException(tce, DEBUG);
      }

      reportException(dex, DEBUG);
      throw new Exception(dex.getMessage());
    }
    catch (Exception ex) {
      UserErrorMessage uem;

      try {
        tib = new TabInfoBean("cdebrowser_error_tabs");
        myRequest = (HttpServletRequest) getInfoObject("HTTPRequest");
        tib.processRequest(myRequest);

        if (tib.getMainTabNum() != 0) {
          tib.setMainTabNum(0);
        }

        uem = new UserErrorMessage();
        uem.setMsgOverview("Unexpected Application Error");
        uem.setMsgText(
                "An unexpected application error has occurred. Please re-try your search");
        // Log stack trace instead of blindly printing it in the html to avoid hacking
//        uem.setMsgTechnical(
//                "<b>System administrator:</b> Here is the stack " +
//                        "trace from the Exception.<BR><BR>" + ex.toString() + "<BR><BR>");
        logger.error( ex.toString());
        uem.setMsgTechnical(
                "<b>System administrator:</b> The stack trace from the Exception was logged " +
                        "at this time: " + getFormattedDate() + ".<BR><BR>Please contact support for help.<BR><BR>");
        setResult("tib", tib);
        setResult("uem", uem);
        setCondition(FAILURE);
      }
      catch (TransitionConditionException tce) {
        reportException(tce, DEBUG);
      }

      reportException(ex, DEBUG);
      throw ex;
    }
  }

  protected TransitionCondition getPersistSuccessCondition() {
    return getCondition(SUCCESS);
  }

  protected TransitionCondition getPersistFailureCondition() {
    return getCondition(FAILURE);
  }
  
  
}
