package gov.nih.nci.cadsr.service.restControllers;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.dao.BasicSearchDAOImpl;
import gov.nih.nci.cadsr.dao.model.BasicSearchModel;
import gov.nih.nci.cadsr.dao.model.ProgramAreaModel;
import gov.nih.nci.cadsr.service.model.search.BasicSearchNode;
import gov.nih.nci.cadsr.service.search.DESearchQueryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BasicSearchController
{

    private Logger logger = LogManager.getLogger( BasicSearchController.class.getName() );
    private BasicSearchDAOImpl basicSearchDAO;
    private RestControllerCommon restControllerCommon;
    private List<ProgramAreaModel> programAreaModelList = null;

    public BasicSearchController()
    {
    }

    public void setBasicSearchDAO( BasicSearchDAOImpl basicSearchDAO )
    {
        this.basicSearchDAO = basicSearchDAO;
    }

    public void setRestControllerCommon( RestControllerCommon restControllerCommon )
    {
        this.restControllerCommon = restControllerCommon;
    }

    @RequestMapping(value = "/basicSearch")
    @ResponseBody
    public BasicSearchNode[] basicSearch( @RequestParam("query") String query, @RequestParam("field") int field, @RequestParam("queryType") int queryType )
    {
        logger.debug( "basicSearch( " + query + ", " + field + ", " + queryType + " )" );
        return basicSearch( query, field, queryType, "" );
    }

    @RequestMapping(value = "/basicSearchWithProgramArea")
    @ResponseBody
    public BasicSearchNode[] basicSearchWithProgramArea( @RequestParam("query") String query, @RequestParam("field") int field, @RequestParam("queryType") int queryType, @RequestParam("programArea") int programArea )
    {
        programAreaModelList = restControllerCommon.getProgramAreaList();
        logger.debug( "basicSearchWithProgramArea: " + query + ", " + field + ", " + queryType + ", " + programArea + "[" + getProgramAreaPalNameByIndex( programArea ) + "]" );

/*
        for( ProgramAreaModel model : programAreaModelList )
        {
            logger.debug( ">>>>>>>>>>>>>\n" + model.toString() + "<<<<<<<<<<<<<" );
        }
*/
        return basicSearch( query, field, queryType, getProgramAreaPalNameByIndex( programArea ) );
    }

    /**
     * If index is 0, or too high, return "All"
     *
     * @param index
     * @return
     */
    private String getProgramAreaPalNameByIndex( int index )
    {
        if( ( index < 1 ) || ( index > programAreaModelList.size() ) )
        {
            return ""; //All
        }
        return programAreaModelList.get( index - 1 ).getPalName();// -1 because the client uses 0 for all.
    }

    private BasicSearchNode[] basicSearch( String query, int field, int queryType, String programArea )
    {

        //FIXME - Martin add documentation for String queryType
        String searchMode = CaDSRConstants.SEARCH_MODE[queryType];

        DESearchQueryBuilder dESearchQueryBuilder;

        if( programArea.isEmpty() )
        {
            dESearchQueryBuilder = new DESearchQueryBuilder( query, searchMode, field );
        }
        else
        {
            dESearchQueryBuilder = new DESearchQueryBuilder( query, searchMode, field, programArea );
        }


        String sql = dESearchQueryBuilder.getQueryStmt();

        logger.debug("SQL:\n" + sql +"\n");

        //If we could not build sql from parameters return a empty search results
        if( sql == null )
        {
            return new BasicSearchNode[0];
        }
        sql = sql.replaceAll( "  *", " " );

        basicSearchDAO.setBasicSearchSql( sql );
        List<BasicSearchModel> results = basicSearchDAO.getAllContexts();

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

            //TODO here we add the URL for the search results
            basicSearchNodes[i].setHref( "cdebrowserServer/CDEData" );

            //This is so in the client side display table there will be spaces to allow good line wrapping.
            if( model.getDeUsedby() != null )
            {
                basicSearchNodes[i].setUsedByContext( model.getDeUsedby().replace( ",", ", " ) );
            }

            basicSearchNodes[i].setRegistrationStatus( model.getRegistrationStatus() );
            i++;
        }
        return basicSearchNodes;
    }
}
