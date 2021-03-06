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
// java imports
import gov.nih.nci.ncicb.cadsr.common.base.process.*;
import gov.nih.nci.ncicb.cadsr.common.CaDSRConstants;
import gov.nih.nci.ncicb.cadsr.common.ProcessConstants;
import gov.nih.nci.ncicb.cadsr.common.persistence.dao.AbstractDAOFactory;
import gov.nih.nci.ncicb.cadsr.common.persistence.dao.ConceptDAO;
import gov.nih.nci.ncicb.cadsr.common.resource.*;
import gov.nih.nci.ncicb.cadsr.common.servicelocator.ServiceLocator;
import gov.nih.nci.ncicb.cadsr.common.servicelocator.ServiceLocatorFactory;
import gov.nih.nci.ncicb.cadsr.common.util.*;
import gov.nih.nci.ncicb.cadsr.common.util.TabInfoBean;
import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import oracle.cle.persistence.HandlerFactory;
import oracle.cle.process.PersistingProcess;
import oracle.cle.process.ProcessInfo;
import oracle.cle.process.ProcessInfoException;
import oracle.cle.process.ProcessParameter;
import oracle.cle.process.ProcessResult;
import oracle.cle.process.Service;
import oracle.cle.util.statemachine.TransitionCondition;
import oracle.cle.util.statemachine.TransitionConditionException;


//Constants Imports




/**
 *
 * @author Oracle Corporation 
 */
public class GetValidValueDetails extends BasePersistingProcess
{
    TabInfoBean tib = null;
    private HttpServletRequest myRequest = null;
	public GetValidValueDetails()
	{
		this(null);

		DEBUG = false;
	} // end default constructor


	public GetValidValueDetails(Service aService)
	{
		super(aService);

		DEBUG = false;
	} // end Constructor 2



	/**
	* Registers all the parameters and results 
	* (<code>ProcessInfo</code>) for this process
	* during construction.
	*
	* @author Oracle Corporation
	*/
	public void registerInfo()
	{
	    try {
	      registerResultObject("tib");
	      //registerParameterObject("de");
	    }
	    catch (ProcessInfoException pie) {
	      reportException(pie, true);
	    }  
	} // end registerInfo



	/**
	* persist: called by start to do all persisting
	*   work for this process.  If this method throws
	*   an exception, then the condition returned by
	*   the <code>getPersistFailureCondition()</code>
	*   is set.  Otherwise, the condition returned by
	*   <code>getPersistSuccessCondition()</code> is
	*   set.
	*
	* @author Oracle Corporation
	*/
	public void persist() throws Exception
	
	{
		try
		{
			 tib = new TabInfoBean("cdebrowser_details_tabs");
			 myRequest = (HttpServletRequest) getInfoObject("HTTPRequest");
			 tib.processRequest(myRequest);

			 int tabNum = tib.getMainTabNum();

			 if (tabNum != 2) {
			   tib.setMainTabNum(2);
			 }
			 DataElement de = (DataElement) getInfoObject("de");
			 ServiceLocator locator = 
			 ServiceLocatorFactory.getLocator(CaDSRConstants.CDEBROWSER_SERVICE_LOCATOR_CLASSNAME);
			 AbstractDAOFactory daoFactory = AbstractDAOFactory.getDAOFactory(locator);
			 ConceptDAO conDAO = daoFactory.getConceptDAO();
			 Representation rep = de.getValueDomain().getRepresentation();
			 if(rep!=null)
			 {
			   ConceptDerivationRule repRule = conDAO.getRepresentationDerivationRuleForVD(de.getValueDomain().getVdIdseq());
			   de.getValueDomain().getRepresentation().setConceptDerivationRule(repRule);
			 }

			     
			 }
			 catch (Exception e) {
			 e.printStackTrace();
			 }
			 setResult("tib", tib);

			setCondition(SUCCESS);
			
		
	} // end persist



	protected TransitionCondition getPersistSuccessCondition()
	{
		return getCondition(SUCCESS);
	} // end getPersistSuccessCondition



	protected TransitionCondition getPersistFailureCondition()
	{
		return getCondition(FAILURE);
	} // end getPersistFailureCondition
}
