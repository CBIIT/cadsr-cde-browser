package gov.nih.nci.cadsr.service.restControllers;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.dao.BasicSearchDAOImpl;
import gov.nih.nci.cadsr.dao.DataElementDAO;
import gov.nih.nci.cadsr.dao.model.BasicSearchModel;
import gov.nih.nci.cadsr.dao.model.ProgramAreaModel;
import gov.nih.nci.cadsr.service.model.search.BasicSearchNode;
import gov.nih.nci.cadsr.service.search.DESearchQueryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BasicSearchController
{

    private Logger logger = LogManager.getLogger( BasicSearchController.class.getName() );
    private BasicSearchDAOImpl basicSearchDAO;
    private DataElementDAO dataElementDAO;
    private RestControllerCommon restControllerCommon;
    private List<ProgramAreaModel> programAreaModelList = null;

    @Value( "${cdeDataRestService}" )
    String cdeDataRestServiceName;

    public BasicSearchController()
    {
    }

    @RequestMapping( value = "/basicSearch" )
    @ResponseBody
    public BasicSearchNode[] basicSearch( @RequestParam( "query" ) String query, @RequestParam( "field" ) int field, @RequestParam( "queryType" ) int queryType )
    {
        BasicSearchNode[] results = null;
        try
        {
            results = basicSearch( query, field, queryType, "" );
        } catch( Exception e )
        {
            return createErrorNode( "Server Error:\nbasicSearch( " + query + ", " + field + ", " + queryType + " ) failed ", e );
        }
        return results;
    }

    @RequestMapping( value = "/basicSearchWithProgramArea" )
    @ResponseBody
    public BasicSearchNode[] basicSearchWithProgramArea( @RequestParam( "query" ) String query, @RequestParam( "field" ) int field, @RequestParam( "queryType" ) int queryType, @RequestParam( "programArea" ) int programArea )
    {
        logger.debug( "basicSearchWithProgramArea   query: " + query + "   field: " + field + "    queryType: " + queryType + "     programArea: " + programArea );

        BasicSearchNode[] results = null;
        try
        {
            programAreaModelList = restControllerCommon.getProgramAreaList();
            results = basicSearch( query, field, queryType, getProgramAreaPalNameByIndex( programArea ) );
        } catch( Exception e )
        {

            return createErrorNode( "Server Error:\nbasicSearchWithProgramArea: " + query + ", " + field + ", " + queryType + ", " + programArea + "[" + getProgramAreaPalNameByIndex( programArea ) + "] failed ", e );
        }
        return results;


    }


 /*
     - - This service is only used for testing. It returns raw search results. - -

    @RequestMapping(value = "/basicSearchWithProgramAreaTest")
    @ResponseBody
    public List<BasicSearchModel> basicSearchWithProgramAreaTest( @RequestParam("query") String query, @RequestParam("field") int field, @RequestParam("queryType") int queryType, @RequestParam("programArea") int programArea )
    {
        programAreaModelList = restControllerCommon.getProgramAreaList();
        logger.debug( "basicSearchWithProgramArea: " + query + ", " + field + ", " + queryType + ", " + programArea + "[" + getProgramAreaPalNameByIndex( programArea ) + "]" );
        return basicSearchRawResults( query, field, queryType, getProgramAreaPalNameByIndex( programArea ) );
    }
*/


    @RequestMapping( value = "/cdesByContext" )
    @ResponseBody
    public BasicSearchNode[] getCDEsByContext( @RequestParam( "contextId" ) String contexId )
    {
        // Sample contextId   DCC52A25-A107-42D4-E040-BB89AD4346A7
        // Check for bad values of contextId - defend against SQL Injection
        if( contexId.matches( "^[0-9A-Za-z-]+$" ) )
        {
            logger.debug( "Good contexId: " + contexId );
        }
        else
        {
            // Log this, as it may be an attempt to hack the SQL server.  Return null to the client
            logger.warn( "getCDEsByContext failed Bad contex ID [" + contexId + "]" );
            return null;
            //return createErrorNode( "Server Error:\ngetCDEsByContext failed: Bad contex ID [" + contexId + "]", contexId );
        }

        BasicSearchNode[] results = null;
        try
        {
            results = runCdeByContextQuery( contexId );
            Exception e = new Exception();
        } catch( Exception e )
        {

            return createErrorNode( "Server Error:\ngetCDEsByContext failed ", e );
        }
        return results;
    }

    @RequestMapping( value = "/cdesByClassificationScheme" )
    @ResponseBody
    public BasicSearchNode[] getCDEsByClassificationScheme( @RequestParam( "classificationSchemeId" ) String classificationSchemeId )
    {
        BasicSearchNode[] results = null;
        try
        {
            results = runCdeByContextClassificationSchemeQuery( classificationSchemeId );
        } catch( Exception e )
        {

            return createErrorNode( "Server Error:\ngetCDEsByClassificationScheme failed ", e );
        }
        return results;
    }

    @RequestMapping( value = "/cdesByClassificationSchemeItem" )
    @ResponseBody
    public BasicSearchNode[] getCDEsByClassificationSchemeItem( @RequestParam( "classificationSchemeItemId" ) String classificationSchemeItemId )
    {
        BasicSearchNode[] results = null;
        try
        {
            results = runCdeByContextClassificationSchemeItemQuery( classificationSchemeItemId );
        } catch( Exception e )
        {

            return createErrorNode( "Server Error:\ngetCDEsByClassificationSchemeItem failed ", e );
        }
        return results;
    }

    @RequestMapping( value = "/cdesByProtocol" )
    @ResponseBody
    public BasicSearchNode[] getCDEsByProtocol( @RequestParam( "protocolId" ) String protocolId )
    {
        BasicSearchNode[] results = null;
        try
        {
            results = runCdeByProtocolQuery( protocolId );
        } catch( Exception e )
        {

            return createErrorNode( "Server Error:\ngetCDEsByProtocol failed ", e );
        }
        return results;
    }

    @RequestMapping( value = "/cdesByProtocolForm" )
    @ResponseBody
    public BasicSearchNode[] getCDEsByProtocolForm( @RequestParam( "id" ) String id )
    {
        BasicSearchNode[] results = null;
        try
        {
            results = runCdeByProtocolFormQuery( id );
        } catch( Exception e )
        {

            return createErrorNode( "Server Error:\ngetCDEsByProtocol failed ", e );
        }
        return results;
    }


    /**
     * @param query       The text of the users search input.
     * @param field       0=Name 1=PublicId
     * @param queryType   0="Exact phrase" 1="All of the words" 2="At least one of the words" defined in CaDSRConstants.SEARCH_MODE
     * @param programArea Constrain a search to the specified Program Area, if programArea is empty, search all.
     * @return an array of BasicSearchNodes.
     */
    private BasicSearchNode[] basicSearch( String query, int field, int queryType, String programArea )
    {
        String searchMode = CaDSRConstants.SEARCH_MODE[queryType];

        //If programArea an empty string, all program areas will be searched
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( query, searchMode, field, programArea );

        String sql = dESearchQueryBuilder.getQueryStmt();
        logger.debug( "sql: " + sql );

        //If we could not build sql from the parameters return a empty search results
        if( sql == null )
        {
            return new BasicSearchNode[0];
        }
        List<BasicSearchModel> results = runQuery( sql );
        return buildSearchResultsNodes( results );
    }

/*
    - - This method is only used for testing. It returns raw search results. - -

    protected List<BasicSearchModel> basicSearchRawResults( String query, int field, int queryType, String programArea )
    {
        String searchMode = CaDSRConstants.SEARCH_MODE[queryType];

        //If programArea an empty string, all program areas will be searched
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( query, searchMode, field, programArea );

        String sql = dESearchQueryBuilder.getQueryStmt();
        //If we could not build sql from the parameters return a empty search results
        if( sql == null )
        {
            return null;
        }
        return runQuery( sql );
    }

*/

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
    protected BasicSearchNode[] buildSearchResultsNodes( List<BasicSearchModel> results )
    {
        int rowCount = results.size();
        int i = 0;
        BasicSearchNode[] basicSearchNodes = new BasicSearchNode[rowCount];
        for( BasicSearchModel model : results )
        {
            basicSearchNodes[i] = new BasicSearchNode();
            basicSearchNodes[i].setLongName( model.getLongName() );
            basicSearchNodes[i].setOwnedBy( model.getName() );
            basicSearchNodes[i].setPreferredQuestionText( model.getDocText() );
            basicSearchNodes[i].setPublicId( model.getDeCdeid() );
            basicSearchNodes[i].setWorkflowStatus( model.getAslName() );
            basicSearchNodes[i].setVersion( model.getDeVersion() );
            basicSearchNodes[i].setDeIdseq( model.getDeIdseq() );

            basicSearchNodes[i].setHref( cdeDataRestServiceName );

            //This is so in the client side display table, there will be spaces to allow good line wrapping.
            if( model.getDeUsedby() != null )
            {
                basicSearchNodes[i].setUsedByContext( model.getDeUsedby().replace( ",", ", " ) );
            }

            basicSearchNodes[i].setRegistrationStatus( model.getRegistrationStatus() );
            i++;
        }
        return basicSearchNodes;
    }

    protected List<BasicSearchModel> runQuery( String sql )
    {
        basicSearchDAO.setBasicSearchSql( sql );
        return basicSearchDAO.getAllContexts();
    }

    protected BasicSearchNode[] runCdeByContextQuery( String contexId )
    {

        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder();

        String sql = dESearchQueryBuilder.getQueryCDEsOwnedAndUsedByContext( contexId );
        logger.debug( "runCdeByContextQuery: " + sql );
        basicSearchDAO.setBasicSearchSql( sql );
        List<BasicSearchModel> results = basicSearchDAO.getAllContexts();

        return buildSearchResultsNodes( results );
    }

    protected BasicSearchNode[] runCdeByContextClassificationSchemeQuery( String classificationSchemeId )
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder();

        String sql = dESearchQueryBuilder.getQueryCdeByContextClassificationScheme( classificationSchemeId );
        //logger.debug( "SQL: " + sql );
        basicSearchDAO.setBasicSearchSql( sql );
        List<BasicSearchModel> results = basicSearchDAO.getAllContexts();

        return buildSearchResultsNodes( results );
    }

    protected BasicSearchNode[] runCdeByContextClassificationSchemeItemQuery( String classificationSchemeItemId )
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder();

        String sql = dESearchQueryBuilder.getQueryCdeByContextClassificationSchemeItem( classificationSchemeItemId );
        //logger.debug( "SQL: " + sql );
        basicSearchDAO.setBasicSearchSql( sql );
        List<BasicSearchModel> results = basicSearchDAO.getAllContexts();

        return buildSearchResultsNodes( results );

    }

    protected BasicSearchNode[] runCdeByProtocolQuery( String protocolId )
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder();

        String sql = dESearchQueryBuilder.getQueryCdeByProtocol( protocolId );
        //logger.debug( "SQL: " + sql );
        basicSearchDAO.setBasicSearchSql( sql );
        List<BasicSearchModel> results = basicSearchDAO.getAllContexts();

        return buildSearchResultsNodes( results );

    }

    protected BasicSearchNode[] runCdeByProtocolFormQuery( String id )
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder();

        String sql = dESearchQueryBuilder.getQueryCdeByProtocolForm( id );
        //logger.debug( "SQL: " + sql );
        basicSearchDAO.setBasicSearchSql( sql );
        List<BasicSearchModel> results = basicSearchDAO.getAllContexts();

        return buildSearchResultsNodes( results );

    }

    public BasicSearchNode[] createErrorNode( String text, Exception e )
    {
        return createErrorNode( text, e.getMessage() );
    }

    public BasicSearchNode[] createErrorNode( String text )
    {
        return createErrorNode( text, "" );
    }

    public BasicSearchNode[] createErrorNode( String text, String logString )
    {
        logger.error( "createErrorNode: " + text + "  " + logString );
        BasicSearchNode[] errorNode = new BasicSearchNode[1];
        errorNode[0] = new BasicSearchNode();
        errorNode[0].setStatus( CaDSRConstants.ERROR );
        errorNode[0].setLongName( text );
        return errorNode;
    }


    ///////////////////////////////////
    // Setters & Getters
    public void setBasicSearchDAO( BasicSearchDAOImpl basicSearchDAO )
    {
        this.basicSearchDAO = basicSearchDAO;
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
