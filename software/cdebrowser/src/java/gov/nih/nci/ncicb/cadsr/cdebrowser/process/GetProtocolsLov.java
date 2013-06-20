package gov.nih.nci.ncicb.cadsr.cdebrowser.process;

import gov.nih.nci.ncicb.cadsr.common.ProcessConstants;
import gov.nih.nci.ncicb.cadsr.common.base.process.BasePersistingProcess;
import gov.nih.nci.ncicb.cadsr.common.lov.ProtocolsLOVBean;
import gov.nih.nci.ncicb.cadsr.common.util.AppScanValidator;
import gov.nih.nci.ncicb.cadsr.common.util.DBUtil;
import gov.nih.nci.ncicb.cadsr.common.util.TabInfoBean;

import javax.servlet.http.HttpServletRequest;

import oracle.cle.process.ProcessInfoException;
import oracle.cle.process.Service;
import oracle.cle.util.statemachine.TransitionCondition;
import oracle.cle.util.statemachine.TransitionConditionException;


/**
 * @author Oracle
 */
public class GetProtocolsLov extends BasePersistingProcess {
  public GetProtocolsLov() {
    this(null);
    DEBUG = false;
  }

  public GetProtocolsLov(Service aService) {
    super(aService);
    DEBUG = false;
  }

  /**
   * Registers all the parameters and results  (<code>ProcessInfo</code>) for
   * this process during construction.
   */
  public void registerInfo() {
    try {
      registerResultObject(ProcessConstants.PROTO_LOV);
      registerResultObject("tib");
      registerParameterObject("SEARCH");
      registerStringParameter("P_PARAM_TYPE");
      registerStringParameter("P_CONTEXT");
      registerStringParameter("P_CONTE_IDSEQ");
      registerParameterObject(ProcessConstants.PROTO_LOV);
      registerStringParameter("performQuery");
      registerStringResult("performQuery");
      registerParameterObject("dbUtil");
      registerStringParameter("SBR_DSN");
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
    HttpServletRequest myRequest = null;
    TabInfoBean tib = null;
    String[] searchParam = null;
    String performQuery = null;
    ProtocolsLOVBean protolb = null;
    String additionalWhere = "";
    DBUtil dbUtil = null;

    try {
      tib = new TabInfoBean("cdebrowser_lov_tabs");
      myRequest = (HttpServletRequest) getInfoObject("HTTPRequest");
      tib.processRequest(myRequest);

      if (tib.getMainTabNum() != 0) {
        tib.setMainTabNum(0);
      }

      performQuery = getStringInfo("performQuery");

      if (performQuery == null) {
        dbUtil = (DBUtil) getInfoObject("dbUtil");

        //String dsName = getStringInfo("SBR_DSN");
        dbUtil.getConnectionFromContainer();

        String conteIdseq = getStringInfo("P_CONTE_IDSEQ");

        if (conteIdseq == null) {
          conteIdseq = "";
        }
        else
		{

			if (!AppScanValidator.validateElementIdSequence(conteIdseq))
				throw new Exception ("Invalidate ID sequence:"+conteIdseq);
		}
        additionalWhere =
          " and upper(nvl(proto_conte.conte_idseq,'%')) like upper ( '%" +
          conteIdseq + "%') ";
        protolb = new ProtocolsLOVBean(myRequest, dbUtil, additionalWhere);
        dbUtil.returnConnection();
      }
      else {
        protolb = (ProtocolsLOVBean) getInfoObject(ProcessConstants.PROTO_LOV);
        protolb.getCommonLOVBean().resetRequest(myRequest);
      }

      setResult(ProcessConstants.PROTO_LOV, protolb);
      setResult("performQuery", null);
      setResult("tib", tib);
      setCondition(SUCCESS);
    }
    catch (Exception ex) {
      try {
        setCondition(FAILURE);
        //dbUtil.returnConnection();
      }
      catch (TransitionConditionException tce) {
        reportException(tce, DEBUG);
      }
      catch (Exception e) {
        reportException(e, DEBUG);
      }

      reportException(ex, DEBUG);
    }finally{
    	if (dbUtil != null) {
			dbUtil.returnConnection();
		}
    }
  }

  protected TransitionCondition getPersistSuccessCondition() {
    return getCondition(SUCCESS);
  }

  protected TransitionCondition getPersistFailureCondition() {
    return getCondition(FAILURE);
  }
}
