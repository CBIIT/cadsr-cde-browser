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

/**
 * Created by lernermh on 7/14/15.
 */
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
        //SELECT CON_DERIVATION_RULES_EXT.* FROM SBR.VALUE_DOMAINS,CON_DERIVATION_RULES_EXT  WHERE SBR.VALUE_DOMAINS.VD_ID = '4768431' AND CON_DERIVATION_RULES_EXT.condr_idseq = SBR.VALUE_DOMAINS.CONDR_IDSEQ;
        String sql = "SELECT CON_DERIVATION_RULES_EXT.* FROM SBR.VALUE_DOMAINS,CON_DERIVATION_RULES_EXT WHERE vd_idseq = ? AND CON_DERIVATION_RULES_EXT.condr_idseq = SBR.VALUE_DOMAINS.CONDR_IDSEQ";
        logger.debug( ">>>>>>> " + sql.replace( "?", vdIdseq ) );
        ConceptDerivationRuleModel conceptDerivationRuleModel = query( sql, vdIdseq, ConceptDerivationRuleModel.class );

        if( conceptDerivationRuleModel == null )
        {
            return null; // no results
        }
        logger.debug("conceptDerivationRuleModel.getName: " + conceptDerivationRuleModel.getName());

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
