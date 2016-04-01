package gov.nih.nci.cadsr.service.restControllers;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.dao.BasicSearchDAOImpl;
import gov.nih.nci.cadsr.dao.DataElementDAO;
import gov.nih.nci.cadsr.dao.model.BasicSearchModel;
import gov.nih.nci.cadsr.dao.model.ProgramAreaModel;
import gov.nih.nci.cadsr.service.model.search.BasicSearchNode;
import gov.nih.nci.cadsr.service.search.AbstractSearchQueryBuilder;
import gov.nih.nci.cadsr.service.search.DESearchQueryBuilder;
import gov.nih.nci.objectCart.domain.CartObject;

@RestController
@RequestMapping("/cdeCart")
public class CdeCartController
{

    private Logger logger = LogManager.getLogger(CdeCartController.class.getName());
    private BasicSearchDAOImpl basicSearchDAO;
    private DataElementDAO dataElementDAO;
    private RestControllerCommon restControllerCommon;
    private List<ProgramAreaModel> programAreaModelList = null;

    @Value("${cdeDataRestService}")
    String cdeDataRestServiceName;

    public CdeCartController()
    {
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public BasicSearchNode[] retrieveObjectCart()
    {
        String query = "3813132 3121734";
    	logger.debug("retrieveObjectCart query: " + query);

        BasicSearchNode[] results = null;
        try
        {
        	CartObject cartObject = new CartObject();
            //results = basicSearch(query, field, queryType, getProgramAreaPalNameByIndex(programArea));
            results = basicSearch(query, AbstractSearchQueryBuilder.PUBLIC_ID_FIELD, 2, "");
        } catch(Exception e)
        {

            return createErrorNode("Server Error:\nretrieveObjectCart: " + query + " failed ", e);
        }
        return results;


    }
    
	@RequestMapping(produces = "text/plain", consumes = "application/json", method = RequestMethod.POST)
	public ResponseEntity<String> downloadExcel(
			RequestEntity<List<String>> request) {
		logger.debug("Received rest call save Object Cart, IDs: " + request);
		return new ResponseEntity<String>("Done", HttpStatus.OK);
	}
	
    /**
     * @param query       The text of the users search input.
     * @param field       0=Name 1=PublicId
     * @param queryType   0="Exact phrase" 1="All of the words" 2="At least one of the words" defined in CaDSRConstants.SEARCH_MODE
     * @param programArea Constrain a search to the specified Program Area, if programArea is empty, search all.
     * @return an array of BasicSearchNodes.
     */
    private BasicSearchNode[] basicSearch(String query, int field, int queryType, String programArea)
    {
        String searchMode = CaDSRConstants.SEARCH_MODE[queryType];

        //If programArea an empty string, all program areas will be searched
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder(query, searchMode, field, programArea);

        String sql = dESearchQueryBuilder.getQueryStmt();
        logger.debug("sql: " + sql);

        //If we could not build sql from the parameters return a empty search results
        if(sql == null)
        {
            return new BasicSearchNode[0];
        }
        List<BasicSearchModel> results = runQuery(sql);
        return buildSearchResultsNodes(results);
    }

    /**
     * If index is 0, or too high, return "All"
     *
     * @param index
     * @return
     */
    protected String getProgramAreaPalNameByIndex(int index)
    {
        // Zero is "All
        if(index < 1)
        {
            return ""; //All
        }

        // If the index is too high, the client has sent us an incorrect program area, log a warning, and return "All"
        if(index > programAreaModelList.size())
        {
            logger.debug("Client has requested in invalid Program area index [" + index + "] using All.");
            return ""; //All
        }
        return programAreaModelList.get(index - 1).getPalName();// -1 because the client uses 0 for all.
    }


    /**
     * Take search results from database, and build array of search results nodes to send to the client.
     *
     * @param results
     * @return
     */
    protected BasicSearchNode[] buildSearchResultsNodes(List<BasicSearchModel> results)
    {
        int rowCount = results.size();
        int i = 0;
        BasicSearchNode[] basicSearchNodes = new BasicSearchNode[rowCount];
        for(BasicSearchModel model : results)
        {
            basicSearchNodes[i] = new BasicSearchNode();
            basicSearchNodes[i].setLongName(model.getLongName());
            basicSearchNodes[i].setOwnedBy(model.getName());
            basicSearchNodes[i].setPreferredQuestionText(model.getDocText());
            basicSearchNodes[i].setPublicId(model.getDeCdeid());
            basicSearchNodes[i].setWorkflowStatus(model.getAslName());
            basicSearchNodes[i].setVersion(model.getDeVersion());
            basicSearchNodes[i].setDeIdseq(model.getDeIdseq());

            basicSearchNodes[i].setHref(cdeDataRestServiceName);

            //This is so in the client side display table, there will be spaces to allow good line wrapping.
            if(model.getDeUsedby() != null)
            {
                basicSearchNodes[i].setUsedByContext(model.getDeUsedby().replace(",", ", "));
            }

            basicSearchNodes[i].setRegistrationStatus(model.getRegistrationStatus());
            i++;
        }
        return basicSearchNodes;
    }

    protected List<BasicSearchModel> runQuery(String sql)
    {
        basicSearchDAO.setBasicSearchSql(sql);
        return basicSearchDAO.getAllContexts();
    }


    public BasicSearchNode[] createErrorNode(String text, Exception e)
    {
        return createErrorNode(text, e.getMessage());
    }

    public BasicSearchNode[] createErrorNode(String text)
    {
        return createErrorNode(text, "");
    }

    public BasicSearchNode[] createErrorNode(String text, String logString)
    {
        logger.error("createErrorNode: " + text + "  " + logString);
        BasicSearchNode[] errorNode = new BasicSearchNode[1];
        errorNode[0] = new BasicSearchNode();
        errorNode[0].setStatus(CaDSRConstants.ERROR);
        errorNode[0].setLongName(text);
        return errorNode;
    }


    ///////////////////////////////////
    // Setters & Getters
    public void setBasicSearchDAO(BasicSearchDAOImpl basicSearchDAO)
    {
        this.basicSearchDAO = basicSearchDAO;
    }

    public void setRestControllerCommon(RestControllerCommon restControllerCommon)
    {
        this.restControllerCommon = restControllerCommon;
    }

    public List<ProgramAreaModel> getProgramAreaModelList()
    {
        return programAreaModelList;
    }

    public void setDataElementDAO(DataElementDAO dataElementDAO)
    {
        this.dataElementDAO = dataElementDAO;
    }

    public void setProgramAreaModelList(List<ProgramAreaModel> programAreaModelList)
    {
        this.programAreaModelList = programAreaModelList;
    }


}
