package gov.nih.nci.cadsr.service.restControllers;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;

import gov.nih.nci.cadsr.dao.SearchDAOImpl;
import gov.nih.nci.cadsr.dao.operation.AbstractSearchQueryBuilder;
import gov.nih.nci.cadsr.service.model.search.SearchNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.dao.DataElementDAO;
import gov.nih.nci.cadsr.dao.model.SearchModel;
import gov.nih.nci.cadsr.dao.model.ProgramAreaModel;
import gov.nih.nci.objectCart.domain.CartObject;

@RestController
@RequestMapping( "/cdeCart" )
public class CdeCartController
{

    private Logger logger = LogManager.getLogger( CdeCartController.class.getName() );
    private SearchDAOImpl searchDAO;
    private DataElementDAO dataElementDAO;
    private RestControllerCommon restControllerCommon;
    private List<ProgramAreaModel> programAreaModelList = null;

    @Value( "${cdeDataRestService}" )
    String cdeDataRestServiceName;

    public CdeCartController()
    {
    }

    @RequestMapping( method = RequestMethod.GET )
    @ResponseBody
    public SearchNode[] retrieveObjectCart()
    {
        String query = "3813132 3121734";
        logger.debug( "retrieveObjectCart query: " + query );

        SearchNode[] results = null;
        try
        {
            CartObject cartObject = new CartObject();
            //results = basicSearch(query, field, queryType, getProgramAreaPalNameByIndex(programArea));
            results = basicSearch( query, AbstractSearchQueryBuilder.PUBLIC_ID_FIELD, 2, "" );
        } catch( Exception e )
        {

            return createErrorNode( "Server Error:\nretrieveObjectCart: " + query + " failed ", e );
        }
        return results;


    }

    @RequestMapping( produces = "text/plain", consumes = "application/json", method = RequestMethod.POST )
    public ResponseEntity<String> downloadExcel(
            RequestEntity<List<String>> request )
    {
        logger.debug( "Received rest call save Object Cart, IDs: " + request );
        return new ResponseEntity<String>( "Done", HttpStatus.OK );
    }

    /**
     * @param query       The text of the users search input.
     * @param field       0=Name 1=PublicId
     * @param queryType   0="Exact phrase" 1="All of the words" 2="At least one of the words" defined in CaDSRConstants.SEARCH_MODE
     * @param programArea Constrain a search to the specified Program Area, if programArea is empty, search all.
     * @return an array of BasicSearchNodes.
     */
    private SearchNode[] basicSearch( String query, int field, int queryType, String programArea )
    {
        String searchMode = CaDSRConstants.SEARCH_MODE[queryType];
        List<SearchModel> results = searchDAO.getAllContexts( query, searchMode, field, programArea );
        return buildSearchResultsNodes( results );
    }

    /**
     * If index is 0, or too high, return "All"
     *
     * @param index
     * @return
     */
    protected String getProgramAreaPalNameByIndex( int index )
    {
        // Zero is "All
        if( index < 1 )
        {
            return ""; //All
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

            searchNodes[i].setHref( cdeDataRestServiceName );

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
    public void setSearchDAO( SearchDAOImpl searchDAO )
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

    public void setDataElementDAO( DataElementDAO dataElementDAO )
    {
        this.dataElementDAO = dataElementDAO;
    }

    public void setProgramAreaModelList( List<ProgramAreaModel> programAreaModelList )
    {
        this.programAreaModelList = programAreaModelList;
    }


}
