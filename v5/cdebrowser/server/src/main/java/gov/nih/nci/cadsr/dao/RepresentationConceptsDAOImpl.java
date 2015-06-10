package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.RepresentationConceptModel;
import gov.nih.nci.cadsr.dao.model.RepresentationModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by lernermh on 6/9/15.
 */
public class RepresentationConceptsDAOImpl extends AbstractDAOOperations implements RepresentationConceptsDAO
{
    private RepresentationDAO representationDAO;

    private Logger logger = LogManager.getLogger( RepresentationConceptsDAOImpl.class.getName() );
    private JdbcTemplate jdbcTemplate;

    @Autowired
    RepresentationConceptsDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }


    @Override
    public List<RepresentationConceptModel> getRepresentationConceptByRepresentationId( String representationId )
    {
        // First get the list of Representation Concepts.Concept Codes
        RepresentationModel representationModel = representationDAO.getRepresentationById( representationId );
logger.debug( "representationModel.getPreferredName(): " + representationModel.getPreferredName() );
        StringBuilder whereClause = new StringBuilder( " where " );
        String[] conceptCode = representationModel.getPreferredName().split( ":" );
        for( int f = 0; f < conceptCode.length; f++ )
        {
            if( f > 0 )
            {
                whereClause.append( " or " );
            }
            whereClause.append( " preferred_name = '" + conceptCode[f] + "'" );
        }

        String sql = "select long_name as concept_name, preferred_name as concept_code, con_id as public_id, definition_source, evs_source from CONCEPTS_EXT " +
                whereClause.toString();

logger.debug( "representationModel(" + representationId + "): \n" + sql );

        return getAll( sql, RepresentationConceptModel.class );
    }

    @Override
    public List<RepresentationConceptModel> getRepresentationConceptByRepresentationId( int representationId )
    {
        return getRepresentationConceptByRepresentationId( new Integer( representationId ).toString() );
    }

    public RepresentationDAO getRepresentationDAO()
    {
        return representationDAO;
    }

    public void setRepresentationDAO( RepresentationDAO representationDAO )
    {
        this.representationDAO = representationDAO;
    }


}
