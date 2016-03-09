package gov.nih.nci.cadsr.service.restControllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.common.RegistrationStatusEnum;
import gov.nih.nci.cadsr.common.WorkflowStatusEnum;

@RestController
@RequestMapping("/lookupdata")
public class LookupDataController
{	
	private Logger logger = LogManager.getLogger(LookupDataController.class.getName());
	
	@RequestMapping(value="/workflowstatus")
	public List<String> getWorkflowStatus()
	{
		logger.debug("Received request for Workflow Status information.");
		return WorkflowStatusEnum.getAsList();
	}
	
	@RequestMapping(value="/registrationstatus")
	public List<String> getRegistrationStatus()
	{
		logger.debug("Received request for Registration Status information.");
		return RegistrationStatusEnum.getAsList();
	}

}
