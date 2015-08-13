package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ConceptModel;
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
    private ConceptDerivationRuleDAOImpl conceptDerivationRuleDAO;
    private ConceptDAOImpl conceptDAO;

    private Logger logger = LogManager.getLogger( RepresentationConceptsDAOImpl.class.getName() );
    private JdbcTemplate jdbcTemplate;

    @Autowired
    RepresentationConceptsDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }


    @Override
    public List<ConceptModel> getRepresentationConceptByRepresentationId( String representationId )
    {
        logger.debug( "representationModel(" + representationId + ")" );

        //select CON_DERIVATION_RULES_EXT.NAME   from sbrext.REPRESENTATIONS_EXT,CON_DERIVATION_RULES_EXT  where sbrext.REPRESENTATIONS_EXT.REP_ID = '3125303' and CON_DERIVATION_RULES_EXT.condr_idseq = sbrext.REPRESENTATIONS_EXT.CONDR_IDSEQ;

        String conceptCodeStr = conceptDerivationRuleDAO.getCDRByRepId( representationId ).getName();
        return conceptDAO.getConceptByConceptCode( conceptCodeStr );


    }

    @Override
    public List<ConceptModel> getRepresentationConceptByRepresentationId( int representationId )
    {
        return getRepresentationConceptByRepresentationId( new Integer( representationId ).toString() );
    }

    public ConceptDAOImpl getConceptDAO()
    {
        return conceptDAO;
    }

    public void setConceptDAO( ConceptDAOImpl conceptDAO )
    {
        this.conceptDAO = conceptDAO;
    }

    public ConceptDerivationRuleDAOImpl getConceptDerivationRuleDAO()
    {
        return conceptDerivationRuleDAO;
    }

    public void setConceptDerivationRuleDAO( ConceptDerivationRuleDAOImpl conceptDerivationRuleDAO )
    {
        this.conceptDerivationRuleDAO = conceptDerivationRuleDAO;
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
