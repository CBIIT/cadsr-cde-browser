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
     public BasicSearchNode[] basicSearch( @RequestParam("query") String query, @RequestParam("field") String field, @RequestParam("queryType") String queryType)
    //public String basicSearch( @RequestParam("query") String query, @RequestParam("field") String field, @RequestParam("queryType") String queryType)
    {


        TempTestParameters request = new TempTestParameters();
        //        String treeParamType = "REGCSI";
        String treeParamType = null;
        String treeParamIdSeq = null; //"99BA9DC8-2094-4E69-E034-080020C9C0E0,Standard";
        String treeConteIdSeq = null; //"99BA9DC8-2094-4E69-E034-080020C9C0E0";
        DataElementSearchBean searchBean = new DataElementSearchBean();

        //FIXME
int intMode = Integer.parseInt(queryType);
String searchMode = CaDSRConstants.SEARCH_MODE[intMode];
System.out.println("searchMode: " + queryType);

        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request, treeParamType, treeParamIdSeq, treeConteIdSeq, searchBean,query, searchMode );
        String sql = dESearchQueryBuilder.getQueryStmt();
        sql = sql.replaceAll( "  *", " " );
        System.out.println("SQL: " + sql +"\n");

        basicSearchDAO.setBasicSearchSql( sql );
        List<BasicSearchModel> results =  basicSearchDAO.getAllContexts();

        //System.out.println("\n\nBasic search: " + results.toString() + "\n\n");

        int rowCount = results.size();
        int i = 0;
        System.out.println("rowCount: " + rowCount );
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

            //FIXME - this a bit hacky. This is so in the client side display table there will be spaces to allow good line wrapping.
            if( model.getDeUsedby() != null )
            {
                basicSearchNodes[i].setUsedByContext( model.getDeUsedby().replace( ",", ", " ));
            }

            basicSearchNodes[i].setRegistrationStatus( model.getRegistrationStatus() );

            i++;
        }

        //return "{\"results\":\"" + query + " " + field + " " + queryType +"\"}";
        return basicSearchNodes;
    }
}
