package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ConceptModel;
import gov.nih.nci.cadsr.dao.model.ObjectClassConceptModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import gov.nih.nci.cadsr.service.model.cdeData.DataElementConcept.ObjectClassConcept;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by lernermh on 7/6/15.
 */
public class ObjectClassConceptDAOImpl extends AbstractDAOOperations implements ObjectClassConceptDAO
{
    private Logger logger = LogManager.getLogger( ObjectClassConceptDAOImpl.class.getName() );
    private JdbcTemplate jdbcTemplate;
    private ConceptDAOImpl conceptDAO;


    @Autowired
    ObjectClassConceptDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public List<ConceptModel> getObjectClassConceptByDecIdseq( String decIdseq )
    {

        String sql = "SELECT OC_IDSEQ from sbr.DATA_ELEMENT_CONCEPTS where DEC_IDSEQ = ?";
        logger.debug( ">>>>>>> " + sql.replace( "?", decIdseq ) );
        ObjectClassConceptModel objectClassConceptModel = query( sql, decIdseq, ObjectClassConceptModel.class );
        logger.debug( ">>>>>>> ocIdseq: " + objectClassConceptModel.getOcIdseq() );

        sql = "SELECT CONDR_IDSEQ from OBJECT_CLASSES_EXT where OC_IDSEQ = ?";
        logger.debug( ">>>>>>> " + sql.replace( "?", objectClassConceptModel.getOcIdseq() ) );
        objectClassConceptModel = query( sql, objectClassConceptModel.getOcIdseq(), ObjectClassConceptModel.class );
        logger.debug( ">>>>>>> condrIdseq: " + objectClassConceptModel .getCondrIdseq());

        sql = "SELECT name from CON_DERIVATION_RULES_EXT where CONDR_IDSEQ = ?";
        logger.debug( ">>>>>>> " + sql.replace( "?", objectClassConceptModel.getCondrIdseq() ) );
        objectClassConceptModel = query( sql, objectClassConceptModel.getCondrIdseq(), ObjectClassConceptModel.class );
        logger.debug( ">>>>>>> Codes: " + objectClassConceptModel.getName());


        return conceptDAO.getConceptByConceptCode( objectClassConceptModel.getName() );

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
