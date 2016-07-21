package gov.nih.nci.cadsr.service.restControllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.common.RegistrationStatusEnum;
import gov.nih.nci.cadsr.common.WorkflowStatusEnum;
import gov.nih.nci.cadsr.error.RestControllerException;
import gov.nih.nci.cadsr.service.ClassificationSchemeService;
import gov.nih.nci.cadsr.service.ProtocolService;
import gov.nih.nci.cadsr.service.model.cdeData.Protocol;
import gov.nih.nci.cadsr.service.model.cdeData.classifications.ClassificationScheme;
import gov.nih.nci.cadsr.dao.DesignationDAO;

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
	
	
	@RequestMapping(value="/workflowstatus", produces = "application/json")
	public List<String> getWorkflowStatus()
	{
		logger.debug("Received request for Workflow Status information.");
		List<String> resList = WorkflowStatusEnum.getAsList();
		resList.add(0, "ALL");
		return resList;
	}
	
	@RequestMapping(value="/registrationstatus", produces = "application/json")
	public List<String> getRegistrationStatus()
	{
		logger.debug("Received request for Registration Status information.");
		List<String> resList = RegistrationStatusEnum.getAsList();;
		resList.add(0, "ALL");
		return resList;
	}
	
	@RequestMapping(value="/alternateType", produces = "application/json")	
	public List<String> getAlternateTypes()
	{
		
		logger.debug("Received request for Alternate Type(s) information.");
		return designationDAO.getAllDesignationModelTypes();
	}	
	
	@RequestMapping(value="/classificationscheme", produces = "application/json")
	public List<ClassificationScheme> getClassificationScheme(@RequestParam(value="contextIdSeq", required=false) String contexIdSeq,
															  @RequestParam(value="csOrCsCsi", required=false) String csOrCsCsi) throws RestControllerException
	{
		logger.debug("Received request for Classification Scheme information for context = " + contexIdSeq + ", csOrCsCsi = " + csOrCsCsi );
		
		List<ClassificationScheme> csList = new ArrayList<ClassificationScheme>();
		try {
			if (StringUtils.isBlank(contexIdSeq) && StringUtils.isBlank(csOrCsCsi))
				throw new RestControllerException("Either one of context id seq or CS or CSI name should be provided. ");
			else
				csList = classificationSchemeService.getClassificationSchemesWithProgramAreaAndContext(contexIdSeq, csOrCsCsi);
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
			if (StringUtils.isBlank(contexIdSeq) && StringUtils.isBlank(protocolOrForm))
				throw new RestControllerException("Either one of context id seq or protocol or form name should be provided. ");
			else
				protocolList = protocolService.getProtocolsWithProgramAreaAndContext(contexIdSeq, protocolOrForm);
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
