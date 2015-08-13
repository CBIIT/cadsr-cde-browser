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

        String sql = "SELECT oc_idseq FROM sbr.data_element_concepts WHERE dec_idseq = ?";
        logger.debug( ">>>>>>> " + sql.replace( "?", decIdseq ) );
        ObjectClassConceptModel objectClassConceptModel = query( sql, decIdseq, ObjectClassConceptModel.class );
        logger.debug( ">>>>>>> ocIdseq: " + objectClassConceptModel.getOcIdseq() );

        sql = "SELECT condr_idseq FROM object_classes_ext WHERE oc_idseq = ?";
        logger.debug( ">>>>>>> " + sql.replace( "?", objectClassConceptModel.getOcIdseq() ) );
        objectClassConceptModel = query( sql, objectClassConceptModel.getOcIdseq(), ObjectClassConceptModel.class );
        logger.debug( ">>>>>>> condrIdseq: " + objectClassConceptModel .getCondrIdseq());

        sql = "SELECT name FROM con_derivation_rules_ext WHERE condr_idseq = ?";
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
