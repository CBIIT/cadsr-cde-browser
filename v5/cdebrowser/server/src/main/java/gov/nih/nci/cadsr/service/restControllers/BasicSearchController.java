package gov.nih.nci.cadsr.service.restControllers;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.dao.BasicSearchDAOImpl;
import gov.nih.nci.cadsr.dao.model.BasicSearchModel;
import gov.nih.nci.cadsr.service.model.search.BasicSearchNode;
import gov.nih.nci.cadsr.service.search.DESearchQueryBuilder;
import gov.nih.nci.cadsr.service.search.DataElementSearchBean;
import gov.nih.nci.cadsr.service.search.TempTestParameters;
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


    public void setBasicSearchDAO( BasicSearchDAOImpl basicSearchDAO )
    {
        this.basicSearchDAO = basicSearchDAO;
    }

    @RequestMapping(value = "/basicSearch")
    @ResponseBody
     public BasicSearchNode[] basicSearch( @RequestParam("query") String query, @RequestParam("field") int field, @RequestParam("queryType") String queryType)
    {


        TempTestParameters request = new TempTestParameters();
        String treeParamType = null;
        String treeParamIdSeq = null;
        String treeConteIdSeq = null;
        DataElementSearchBean searchBean = new DataElementSearchBean();

        //FIXME - Martin add documentation for String queryType
int intMode = Integer.parseInt(queryType);
String searchMode = CaDSRConstants.SEARCH_MODE[intMode];

        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request,   searchBean,query, searchMode, field );
        String sql = dESearchQueryBuilder.getQueryStmt();
        sql = sql.replaceAll( "  *", " " );

        basicSearchDAO.setBasicSearchSql( sql );
        List<BasicSearchModel> results =  basicSearchDAO.getAllContexts();

        int rowCount = results.size();
        int i = 0;
        BasicSearchNode[] basicSearchNodes = new BasicSearchNode[rowCount];
        for( BasicSearchModel model: results)
        {
            basicSearchNodes[i] = new BasicSearchNode();
            basicSearchNodes[i].setLongName( model.getLongName() );
            basicSearchNodes[i].setOwnedBy( model.getName() );
            basicSearchNodes[i].setPreferredQuestionText( model.getDocText() );
            basicSearchNodes[i].setPublicId( model.getDeCdeid() );
            basicSearchNodes[i].setWorkflowStatus( model.getAslName() );
            basicSearchNodes[i].setVersion( model.getDeVersion() );

            //TODO here we add the URL for the search results
            basicSearchNodes[i].setHref( "cdebrowserServer/searchResultsData" );


            //FIXME - this a bit hacky. This is so in the client side display table there will be spaces to allow good line wrapping.
            if( model.getDeUsedby() != null )
            {
                basicSearchNodes[i].setUsedByContext( model.getDeUsedby().replace( ",", ", " ));
            }

            basicSearchNodes[i].setRegistrationStatus( model.getRegistrationStatus() );

            i++;
        }
        return basicSearchNodes;
    }
}
