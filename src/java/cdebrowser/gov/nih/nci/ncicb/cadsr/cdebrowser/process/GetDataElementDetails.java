package gov.nih.nci.ncicb.cadsr.cdebrowser.process;


// java imports
import gov.nih.nci.ncicb.cadsr.base.process.BasePersistingProcess;
import gov.nih.nci.ncicb.cadsr.cdebrowser.userexception.DataElementNotFoundException;
import gov.nih.nci.ncicb.cadsr.cdebrowser.userexception.IllegalURLParametersException;
import gov.nih.nci.ncicb.cadsr.resource.DataElement;
import gov.nih.nci.ncicb.cadsr.resource.handler.DataElementHandler;
import gov.nih.nci.ncicb.cadsr.util.TabInfoBean;
import gov.nih.nci.ncicb.cadsr.util.UserErrorMessage;

import oracle.cle.persistence.HandlerFactory;

import oracle.cle.process.PersistingProcess;
import oracle.cle.process.ProcessConstants;
import oracle.cle.process.ProcessInfo;
import oracle.cle.process.ProcessInfoException;
import oracle.cle.process.ProcessParameter;
import oracle.cle.process.ProcessResult;
import oracle.cle.process.Service;

import oracle.cle.util.statemachine.TransitionCondition;
import oracle.cle.util.statemachine.TransitionConditionException;

import javax.servlet.http.HttpServletRequest;


public class GetDataElementDetails extends BasePersistingProcess {

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
      }
      else {
        de = (DataElement) getInfoObject("de");
      }

      tib = new TabInfoBean("cdebrowser_details_tabs");
      myRequest = (HttpServletRequest) getInfoObject("HTTPRequest");
      tib.processRequest(myRequest);

      if (tib.getMainTabNum() != 0) {
        tib.setMainTabNum(0);
      }

      setResult("tib", tib);
      setResult("de", de);
      setResult("queryDE", "no");
      setResult("cdeId", null);
      setResult("version", null);
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
        uem.setMsgTechnical(
          "<b>System administrator:</b> Here is the stack " +
          "trace from the Exception.<BR><BR>" + iex.toString() + "<BR><BR>");
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
        uem.setMsgTechnical(
          "<b>System administrator:</b> Here is the stack " +
          "trace from the Exception.<BR><BR>" + ex.toString() + "<BR><BR>");
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
