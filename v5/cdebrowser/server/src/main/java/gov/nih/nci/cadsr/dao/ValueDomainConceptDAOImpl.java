package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ConceptDerivationRuleModel;
import gov.nih.nci.cadsr.dao.model.ConceptModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class ValueDomainConceptDAOImpl extends AbstractDAOOperations implements ValueDomainConceptDAO
{

    private Logger logger = LogManager.getLogger( ValueDomainConceptDAOImpl.class.getName() );
    private JdbcTemplate jdbcTemplate;
    private ConceptDAOImpl conceptDAO;


    @Autowired
    ValueDomainConceptDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public List<ConceptModel> getValueDomainConceptByVdIdseq( String vdIdseq )
    {
        String sql = "SELECT con_derivation_rules_ext.* FROM sbr.value_domains,con_derivation_rules_ext WHERE vd_idseq = ? AND con_derivation_rules_ext.condr_idseq = sbr.value_domains.condr_idseq";
        logger.debug( ">>>>>>> " + sql.replace( "?", vdIdseq ) );
        ConceptDerivationRuleModel conceptDerivationRuleModel = query( sql, vdIdseq, ConceptDerivationRuleModel.class );

        if( conceptDerivationRuleModel == null )
        {
            return null; // no results
        }
        logger.debug( "conceptDerivationRuleModel.getName: " + conceptDerivationRuleModel.getName() );

        String conceptCodeStr = conceptDerivationRuleModel.getName();
        return conceptDAO.getConceptByConceptCode( conceptCodeStr );
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
