package gov.nih.nci.cadsr.service.restControllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.common.util.ParameterValidator;
import gov.nih.nci.cadsr.dao.DesignationDAO;
import gov.nih.nci.cadsr.dao.RegistrationStatusDAO;
import gov.nih.nci.cadsr.dao.WorkflowStatusDAO;
import gov.nih.nci.cadsr.error.RestControllerException;
import gov.nih.nci.cadsr.service.ClassificationSchemeService;
import gov.nih.nci.cadsr.service.ProtocolService;
import gov.nih.nci.cadsr.service.model.cdeData.Protocol;
import gov.nih.nci.cadsr.service.model.cdeData.classifications.ClassificationScheme;
import gov.nih.nci.cadsr.service.model.search.SearchCriteria;

@RestController
@RequestMapping("/lookupdata")
public class LookupDataController
{
	private Logger logger = LogManager.getLogger(LookupDataController.class.getName());
	//DesignationDAO designationDAO = new DesignationDAOImpl();

	@Autowired
	ClassificationSchemeService classificationSchemeService;

	@Autowired
	ProtocolService protocolService;

    @Autowired
    private DesignationDAO designationDAO;
    
    @Autowired
    private RegistrationStatusDAO registrationStatusDAO;
    
    @Autowired
    private WorkflowStatusDAO workflowStatusDAO;


	@RequestMapping(value="/workflowstatus", produces = "application/json")
	public List<String> getWorkflowStatus(HttpSession mySession)
	{
		//logger.debug("Received request for Workflow Status information.");
		List<String> resList = workflowStatusDAO.getWorkflowStatusesAsList();
		//CDEBROWSER-703 keep in session not to go to DB every time to check parameter validity when we receive search request 
		mySession.setAttribute(CaDSRConstants.USER_SESSION_WORKFLOW_STATUS_LIST, resList);
		resList.add(0, SearchCriteria.ALL_WORKFLOW_STATUSES);
		return resList;
	}

	@RequestMapping(value="/registrationstatus", produces = "application/json")
	public List<String> getRegistrationStatus(HttpSession mySession)
	{
		//logger.debug("Received request for Registration Status information.");
		List<String> resList = registrationStatusDAO.getRegnStatusesAsList();
		//CDEBROWSER-703 keep in session not to go to DB every time to check parameter validity when we receive search request
		mySession.setAttribute(CaDSRConstants.USER_SESSION_REGISTRATION_STATUS_LIST, resList);
		resList.add(0,SearchCriteria.ALL_REGISTRATION_STATUSES);
		return resList;
	}

	@RequestMapping(value="/alternateType", produces = "application/json")
	public List<String> getAlternateTypes()
	{

		//logger.debug("Received request for Alternate Type(s) information.");
		return designationDAO.getAllDesignationModelTypes();
	}

	@RequestMapping(value="/classificationscheme", produces = "application/json")
	public List<ClassificationScheme> getClassificationScheme(@RequestParam(value="contextIdSeq", required=false) String contexIdSeq,
															  @RequestParam(value="csOrCsCsi", required=false) String csOrCsCsi) throws RestControllerException
	{
		logger.debug("Received request for Classification Scheme information for context = " + contexIdSeq + ", csOrCsCsi = " + csOrCsCsi );

		List<ClassificationScheme> csList = new ArrayList<ClassificationScheme>();
		try {
			if (StringUtils.isBlank(contexIdSeq) && StringUtils.isBlank(csOrCsCsi)) {
				throw new RestControllerException("Either one of context id seq or CS or CSI name should be provided. ");
			}
			else if ((StringUtils.isEmpty(contexIdSeq)) || (ParameterValidator.validateIdSeq(contexIdSeq))) {
				csList = classificationSchemeService.getClassificationSchemesWithProgramAreaAndContext(contexIdSeq, csOrCsCsi);
			}
			else {
				throw new RestControllerException("Context id seq unexpected value provided: " + contexIdSeq);
			}
		}
		catch (RestControllerException re)
		{
			logger.error(re.getMessage(), re);
			throw re;
		}
		catch (Exception e)
		{
			String errMsg = "Error in fetching Classification Scheme with Program Area and Context. ";
			logger.error(errMsg, e);
			throw new RestControllerException(errMsg + e.getMessage());
		}
		return csList;
	}

	@RequestMapping(value="/protocol", produces = "application/json")
	public List<Protocol> getProtocol(@RequestParam(value="contextIdSeq", required=false) String contexIdSeq,
									  @RequestParam(value="protocolOrForm", required=false) String protocolOrForm) throws RestControllerException
	{
		logger.debug("Received request for Protocol information for context = " + contexIdSeq + ", protocolOrForm = " + protocolOrForm);

		List<Protocol> protocolList = new ArrayList<Protocol>();
		try {
			if (StringUtils.isBlank(contexIdSeq) && StringUtils.isBlank(protocolOrForm)) {
				throw new RestControllerException("Either one of context id seq or protocol or form name should be provided. ");
			}		
			else if ((StringUtils.isEmpty(contexIdSeq)) || (ParameterValidator.validateIdSeq(contexIdSeq)))  {
				protocolList = protocolService.getProtocolsWithProgramAreaAndContext(contexIdSeq, protocolOrForm);
			}
			else {
				throw new RestControllerException("Context id seq unexpected value provided: " + contexIdSeq);
			}
		}
		catch (RestControllerException re)
		{
			logger.error(re.getMessage(), re);
			throw re;
		}
		catch (Exception e)
		{
			String errMsg = "Error in fetching Protocols with Program Area and Context. ";
			logger.error(errMsg, e);
			throw new RestControllerException(errMsg + e.getMessage());
		}
		return protocolList;
	}

}
