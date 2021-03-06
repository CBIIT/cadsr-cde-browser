package gov.nih.nci.cadsr.service.restControllers;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.common.AppConfig;
import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.common.RegistrationStatusEnum;
import gov.nih.nci.cadsr.common.SearchCriteriaValidator;
import gov.nih.nci.cadsr.common.UsageLog;
import gov.nih.nci.cadsr.common.WorkflowStatusEnum;
import gov.nih.nci.cadsr.common.util.StringUtilities;
import gov.nih.nci.cadsr.dao.RegistrationStatusDAO;
import gov.nih.nci.cadsr.dao.SearchDAO;
import gov.nih.nci.cadsr.dao.WorkflowStatusDAO;
import gov.nih.nci.cadsr.dao.model.ProgramAreaModel;
import gov.nih.nci.cadsr.dao.model.SearchModel;
import gov.nih.nci.cadsr.service.model.search.SearchCriteria;
import gov.nih.nci.cadsr.service.model.search.SearchNode;

@RestController
public class SearchController
{

    private Logger logger = LogManager.getLogger( SearchController.class.getName() );
    protected static final String clientErrorEmptyFilteredInput = "Client Error: all search fields cannot be unselected when using Search Term field";
    
    @Autowired
    AppConfig appConfig;

    @Autowired
    private SearchDAO searchDAO;

    @Autowired
    private RestControllerCommon restControllerCommon;

    @Autowired
    private UsageLog usageLog;

    private List<ProgramAreaModel> programAreaModelList = null;
    
    private SearchCriteriaValidator searchCriteriaValidator = new SearchCriteriaValidator();
    
	@Autowired
    private RegistrationStatusDAO registrationStatusDAO;
	@Autowired
    private WorkflowStatusDAO workflowStatusDAO;
	
    /**
	 * @param registrationStatusDAO the registrationStatusDAO to set
	 */
	public void setRegistrationStatusDAO(RegistrationStatusDAO registrationStatusDAO) {
		this.registrationStatusDAO = registrationStatusDAO;
	}

	/**
	 * @param workflowStatusDAO the workflowStatusDAO to set
	 */
	public void setWorkflowStatusDAO(WorkflowStatusDAO workflowStatusDAO) {
		this.workflowStatusDAO = workflowStatusDAO;
	}


    @RequestMapping( value = "/testSearch" )
    @ResponseBody
    public SearchNode[] testSearch(@ModelAttribute SearchCriteria searchCriteria, HttpSession httpSession)
    {
        SearchNode[] results = null;
        try
        {
            String searchMode = CaDSRConstants.SEARCH_MODE[searchCriteria.getQueryType()];
            searchCriteria.setSearchMode(searchMode);
            results = buildSearchResultsNodes( searchDAO.getAllContexts(searchCriteria, ControllerUtils.retriveSessionSearchPreferencesServer(httpSession), 
            	WorkflowStatusEnum.getAsList(), RegistrationStatusEnum.getAsList()) );
        } catch( Exception e )
        {
            return createErrorNode( "Server Error:\ntestSearch: " + searchCriteria.getName() + ", " + searchCriteria.getQueryType() + ", " + searchCriteria.getPublicId() + ", " + searchCriteria.getProgramArea() + " failed ", e );
        }

        return results;
    }

    /**
     *
     * @param searchCriteria
     * @param result
     * @param httpSession
     * @return an array of SearchNode
     */
    @SuppressWarnings("unchecked")
	@RequestMapping( value = "/search" )
    @ResponseBody
    public SearchNode[] search(@ModelAttribute SearchCriteria searchCriteria, BindingResult result, HttpSession httpSession)
    {
        logger.debug("Received a search request with the following search criteria: " + searchCriteria);
        searchCriteriaValidator.validate(searchCriteria, result);//SECURITYTEAM-1417
        if (result.hasErrors())
        {
        	logger.error("Error in binding search criteria to the SearchCriteria bean.");
        	return createErrorNode( "Server Error in binding search criteria to a bean. Error count:" +  result.getErrorCount() + "  " + result.getAllErrors().get(0));
        }
        
        SearchNode[] results;

        // AppScan will try to inject %Hex strings to test our parameter sanitizing.
        if( StringUtilities.checkForBadParameters(searchCriteria.getName(), searchCriteria.getPublicId(), searchCriteria.getProgramArea(), searchCriteria.getContext(),
        										searchCriteria.getClassification(), searchCriteria.getCsCsiIdSeq(), searchCriteria.getProtocol(), searchCriteria.getFormIdSeq(),
        										searchCriteria.getWorkFlowStatus(), searchCriteria.getRegistrationStatus(), searchCriteria.getConceptName(), searchCriteria.getConceptCode(),
                                                searchCriteria.getDataElementConcept(), searchCriteria.getPermissibleValue(), searchCriteria.getObjectClass(), searchCriteria.getFilteredinput()))
        {
            logger.warn( "Suspect parameter from client." );
            return null;
        }
        
        //CDEBROWSER-814 We made a client change that all fields cannot be unselected. This change is to make sure on the server.
        if ((StringUtils.isNotBlank(searchCriteria.getName())) &&
        		((searchCriteria.getFilteredinput() == null) || (searchCriteria.getFilteredinput().isEmpty()))) {
        	logger.warn( "Server received filteredinput empty parameter from the client when Search Term is not blank, returning Error");
        	return createErrorNode(clientErrorEmptyFilteredInput);
        }
        
        try
        {
            String searchMode = CaDSRConstants.SEARCH_MODE[searchCriteria.getQueryType()];
            searchCriteria.setSearchMode(searchMode);

            searchCriteria.preprocessCriteria();
            List<String> allowedWorkflowStatuses = ControllerUtils.retriveSessionWorkflowStatusList(httpSession, workflowStatusDAO);
            List<String> allowedRegStatuses = ControllerUtils.retriveSessionRegistrationStatusList(httpSession, registrationStatusDAO);
            results = buildSearchResultsNodes( searchDAO.getAllContexts(searchCriteria, 
            	ControllerUtils.retriveSessionSearchPreferencesServer(httpSession), 
            	allowedWorkflowStatuses, allowedRegStatuses));
        } catch( Exception e )
        {
        	logger.error("Error in searching: ", e);
            return createErrorNode( "Server Error: " + e.getMessage() + "\n" + searchCriteria + " failed ", e );
        }
        usageLog.log( "search", searchCriteria.toLogString() + " [" + results.length + " results returned]" );

        return results;
    }

    @RequestMapping( value = "/cdesByContext" )
    @ResponseBody
    public SearchNode[] getCDEsByContext( @RequestParam( "contextId" ) String contextId, HttpSession httpSession )
    {
        // Sample contextId   DCC52A25-A107-42D4-E040-BB89AD4346A7
        // Check for bad values of contextId - defend against SQL Injection
        if( contextId.matches( "^[0-9A-Za-z-]+$" ) )
        {
            logger.debug( "Good contextId: " + contextId );
        }
        else
        {
            // Log this, as it may be an attempt to hack the SQL server.  Return null to the client
            logger.warn( "getCDEsByContext failed Bad contex ID [" + contextId + "]" );
            return null;
            //return createErrorNode( "Server Error:\ngetCDEsByContext failed: Bad contex ID [" + contextId + "]", contextId );
        }

        SearchNode[] results = null;
        try
        {
            results = getCdeByContext( contextId, httpSession );
        } catch( Exception e )
        {
        	logger.error("Error in Searching by context id: " + contextId, e);
            return createErrorNode( "Server Error:\ngetCDEsByContext failed ", e );
        }

        usageLog.log( "cdesByContext",  "contextId=" + contextId + " [" + results.length + " results returned]" );

        return results;
    }

    @RequestMapping( value = "/cdesByClassificationScheme" )
    @ResponseBody
    public SearchNode[] getCDEsByClassificationScheme( @RequestParam( "classificationSchemeId" ) String classificationSchemeId, HttpSession httpSession )
    {
        SearchNode[] results = null;
        try
        {
            results = cdeByContextClassificationScheme( classificationSchemeId, httpSession );
        } catch( Exception e )
        {
            return createErrorNode( "Server Error:\ngetCDEsByClassificationScheme failed ", e );
        }
        usageLog.log( "cdesByClassificationScheme",  "classificationSchemeId=" + classificationSchemeId + " [" + results.length + " results returned]" );
        return results;
    }

    @RequestMapping( value = "/cdesByClassificationSchemeItem" )
    @ResponseBody
    public SearchNode[] getCDEsByClassificationSchemeItem( @RequestParam( "classificationSchemeItemId" ) String classificationSchemeItemId, HttpSession httpSession )
    {
        SearchNode[] results = null;
        try
        {
            results = cdeByContextClassificationSchemeItem( classificationSchemeItemId, httpSession );
        } catch( Exception e )
        {

            return createErrorNode( "Server Error:\ngetCDEsByClassificationSchemeItem failed ", e );
        }
        usageLog.log( "cdesByClassificationSchemeItem",  "classificationSchemeItemId=" + classificationSchemeItemId + " [" + results.length + " results returned]" );
        return results;
    }

    @RequestMapping( value = "/cdesByProtocol" )
    @ResponseBody
    public SearchNode[] getCDEsByProtocol( @RequestParam( "protocolId" ) String protocolId, HttpSession httpSession )
    {
        SearchNode[] results = null;
        try
        {
            results = cdeByProtocol( protocolId, httpSession );
        } catch( Exception e )
        {

            return createErrorNode( "Server Error:\ngetCDEsByProtocol failed ", e );
        }
        usageLog.log( "cdesByProtocol",  "protocolId=" + protocolId + " [" + results.length + " results returned]" );

        return results;
    }

    @RequestMapping( value = "/cdesByProtocolForm" )
    @ResponseBody
    public SearchNode[] getCDEsByProtocolForm( @RequestParam( "id" ) String id )
    {
        SearchNode[] results = null;
        try
        {
            results = cdeByProtocolForm( id );
        } catch( Exception e )
        {

            return createErrorNode( "Server Error:\ngetCDEsByProtocol failed ", e );
        }
        usageLog.log( "cdesByProtocolForm",  "id=" + id + " [" + results.length + " results returned]" );
        return results;
    }


    /**
     * If index is 0, or too high, return "All"
     *
     * @param index
     * @return
     */
    protected String getProgramAreaPalNameByIndex( int index )
    {
        // We use 0 or -1 for "All"
        if( index < 1 )
        {
            return ""; //All
        }

        if( programAreaModelList == null )
        {
            programAreaModelList = restControllerCommon.getProgramAreaList();
        }
        // If the index is too high, the client has sent us an incorrect program area, log a warning, and return "All"
        if( index > programAreaModelList.size() )
        {
            logger.debug( "Client has requested in invalid Program area index [" + index + "] using All." );
            return ""; //All
        }
        return programAreaModelList.get( index - 1 ).getPalName();// -1 because the client uses 0 for all.
    }


    /**
     * Take search results from database, and build array of search results nodes to send to the client.
     *
     * @param results
     * @return
     */
    protected SearchNode[] buildSearchResultsNodes( List<SearchModel> results )
    {
        int rowCount = results.size();
        int i = 0;
        SearchNode[] searchNodes = new SearchNode[rowCount];
        for( SearchModel model : results )
        {
            searchNodes[i] = new SearchNode();
            searchNodes[i].setLongName( model.getLongName() );
            searchNodes[i].setOwnedBy( model.getName() );
            searchNodes[i].setPreferredQuestionText( model.getDocText() );
            searchNodes[i].setPublicId( model.getDeCdeid() );
            searchNodes[i].setWorkflowStatus( model.getAslName() );
            searchNodes[i].setVersion( model.getDeVersion() );
            searchNodes[i].setDeIdseq( model.getDeIdseq() );

            searchNodes[i].setHref(appConfig.getCdeDataRestServiceName());

            //This is so in the client side display table, there will be spaces to allow good line wrapping.
            if( model.getDeUsedby() != null )
            {
                searchNodes[i].setUsedByContext( model.getDeUsedby().replace( ",", ", " ) );
            }

            searchNodes[i].setRegistrationStatus( model.getRegistrationStatus() );
            i++;
        }
        return searchNodes;
    }

    protected SearchNode[] getCdeByContext( String contexId, HttpSession httpSession )
    {
        List<SearchModel> results = searchDAO.cdeOwnedAndUsedByContext( contexId, ControllerUtils.retriveSessionSearchPreferencesServer(httpSession));
        return buildSearchResultsNodes( results );
    }

    protected SearchNode[] cdeByContextClassificationScheme( String classificationSchemeId, HttpSession httpSession )
    {
        List<SearchModel> results = searchDAO.cdeByContextClassificationScheme( classificationSchemeId, ControllerUtils.retriveSessionSearchPreferencesServer(httpSession) );
        return buildSearchResultsNodes( results );
    }

    protected SearchNode[] cdeByContextClassificationSchemeItem( String classificationSchemeItemId, HttpSession httpSession )
    {
        List<SearchModel> results = searchDAO.cdeByContextClassificationSchemeItem( classificationSchemeItemId, ControllerUtils.retriveSessionSearchPreferencesServer(httpSession) );
        return buildSearchResultsNodes( results );
    }

    protected SearchNode[] cdeByProtocol( String protocolId, HttpSession httpSession )
    {
        List<SearchModel> results = searchDAO.cdeByProtocol( protocolId, ControllerUtils.retriveSessionSearchPreferencesServer(httpSession) );

        return buildSearchResultsNodes( results );

    }

    protected SearchNode[] cdeByProtocolForm( String id )
    {
        List<SearchModel> results = searchDAO.cdeByProtocolForm( id );
        return buildSearchResultsNodes( results );
    }


    public SearchNode[] createErrorNode( String text, Exception e )
    {
        return createErrorNode( text, e.getMessage() );
    }

    public SearchNode[] createErrorNode( String text )
    {
        return createErrorNode( text, "" );
    }


    public SearchNode[] createErrorNode( String text, String logString )
    {
        logger.error( "createErrorNode: " + text + "  " + logString );
        SearchNode[] errorNode = new SearchNode[1];
        errorNode[0] = new SearchNode();
        errorNode[0].setStatus( CaDSRConstants.ERROR );
        errorNode[0].setLongName( text );
        return errorNode;
    }


    ///////////////////////////////////
    // Setters & Getters
    public void setSearchDAO( SearchDAO searchDAO )
    {
        this.searchDAO = searchDAO;
    }

    public void setRestControllerCommon( RestControllerCommon restControllerCommon )
    {
        this.restControllerCommon = restControllerCommon;
    }

    public List<ProgramAreaModel> getProgramAreaModelList()
    {
        return programAreaModelList;
    }

    public void setProgramAreaModelList( List<ProgramAreaModel> programAreaModelList )
    {
        this.programAreaModelList = programAreaModelList;
    }

	public AppConfig getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(AppConfig appConfig) {
		this.appConfig = appConfig;
	}

    public UsageLog getUsageLog()
    {
        return usageLog;
    }

    public void setUsageLog( UsageLog usageLog )
    {
        this.usageLog = usageLog;
    }
//    @InitBinder removed this method - it is called on every request
//    protected void initBinder(WebDataBinder binder) {
//        binder.addValidators(searchCriteriaValidator);
//    }
}
