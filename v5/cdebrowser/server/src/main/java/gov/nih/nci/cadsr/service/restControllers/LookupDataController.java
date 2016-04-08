package gov.nih.nci.cadsr.service.restControllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.common.RegistrationStatusEnum;
import gov.nih.nci.cadsr.common.WorkflowStatusEnum;
import gov.nih.nci.cadsr.error.RestControllerException;
import gov.nih.nci.cadsr.service.ClassificationSchemeService;
import gov.nih.nci.cadsr.service.ProtocolService;
import gov.nih.nci.cadsr.service.model.cdeData.Protocol;
import gov.nih.nci.cadsr.service.model.cdeData.classifications.ClassificationScheme;

@RestController
@RequestMapping("/lookupdata")
public class LookupDataController
{	
	private Logger logger = LogManager.getLogger(LookupDataController.class.getName());
	
	@Autowired
	ClassificationSchemeService classificationSchemeService;
	
	@Autowired
	ProtocolService protocolService;
	
	@RequestMapping(value="/workflowstatus", produces = "application/json")
	public List<String> getWorkflowStatus()
	{
		logger.debug("Received request for Workflow Status information.");
		return WorkflowStatusEnum.getAsList();
	}
	
	@RequestMapping(value="/registrationstatus", produces = "application/json")
	public List<String> getRegistrationStatus()
	{
		logger.debug("Received request for Registration Status information.");
		return RegistrationStatusEnum.getAsList();
	}
	
	@RequestMapping(value="/classificationscheme", produces = "application/json")
	public List<ClassificationScheme> getClassificationScheme() throws RestControllerException
	{
		logger.debug("Received request for Classification Scheme information.");
		List<ClassificationScheme> csList = new ArrayList<ClassificationScheme>();
		try {
			csList = classificationSchemeService.getClassificationSchemesWithProgramAreaAndContext();
		} catch (Exception e) {
			String errMsg = "Error in fetching Classification Scheme with Program Area and Context: ";
			logger.error(errMsg, e);
			throw new RestControllerException(errMsg + e.getMessage());
		}
		return csList;
	}
	
	@RequestMapping(value="/protocolform", produces = "application/json")
	public List<Protocol> getProtocolForm() throws RestControllerException
	{
		logger.debug("Received request for Protocol Form information.");
		List<Protocol> protocolList = new ArrayList<Protocol>();
		try {
			protocolList = protocolService.getProtocolsWithProgramAreaAndContext();
		} catch (Exception e) {
			String errMsg = "Error in fetching Protocols with Program Area and Context: ";
			logger.error(errMsg, e);
			throw new RestControllerException(errMsg + e.getMessage());
		}
		return protocolList;
	}

}
