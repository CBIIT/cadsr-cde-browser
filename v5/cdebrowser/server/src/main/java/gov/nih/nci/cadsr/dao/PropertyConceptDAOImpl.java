package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ConceptModel;
import gov.nih.nci.cadsr.dao.model.PropertyConceptModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by lernermh on 7/8/15.
 */
public class PropertyConceptDAOImpl extends AbstractDAOOperations implements PropertyConceptDAO
{

    private Logger logger = LogManager.getLogger( PropertyConceptDAOImpl.class.getName() );
    private JdbcTemplate jdbcTemplate;
    private ConceptDAOImpl conceptDAO;


    @Autowired
    PropertyConceptDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }


    @Override
    public List<ConceptModel> getPropertyConceptByDecIdseq( String decIdseq )
    {

        String sql = "SELECT PROP_IDSEQ from SBR.DATA_ELEMENT_CONCEPTS where DEC_IDSEQ = ?";
        logger.debug( ">>>>>>> " + sql.replace( "?", decIdseq ) );
        PropertyConceptModel propertyConceptModel = query( sql, decIdseq, PropertyConceptModel.class );
        logger.debug( ">>>>>>> propIdseq: " + propertyConceptModel.getPropIdseq() );

        sql = "SELECT CONDR_IDSEQ from PROPERTIES_EXT where PROP_IDSEQ = ?";
        logger.debug( ">>>>>>> " + sql.replace( "?", propertyConceptModel.getPropIdseq() ) );
        propertyConceptModel = query( sql, propertyConceptModel.getPropIdseq(), PropertyConceptModel.class );
        logger.debug( ">>>>>>> condrIdseq: " + propertyConceptModel.getCondrIdseq());

        sql = "SELECT name from CON_DERIVATION_RULES_EXT where CONDR_IDSEQ = ?";
        logger.debug( ">>>>>>> " + sql.replace( "?", propertyConceptModel.getCondrIdseq() ) );
        propertyConceptModel = query( sql, propertyConceptModel.getCondrIdseq(), PropertyConceptModel.class );
        logger.debug( ">>>>>>> Codes: " + propertyConceptModel.getName());


        return conceptDAO.getConceptByConceptCode( propertyConceptModel.getName() );

    }

    public ConceptDAOImpl getConceptDAO()
    {
        return conceptDAO;
    }

    public void setConceptDAO( ConceptDAOImpl conceptDAO )
    {
        this.conceptDAO = conceptDAO;
    }

}
