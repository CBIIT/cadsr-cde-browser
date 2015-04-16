package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ConceptDerivationRuleModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by lavezzojl on 4/16/15.
 */
public class ConceptDerivationRuleDAOImpl extends AbstractDAOOperations implements ConceptDerivationRuleDAO {

    private Logger logger = LogManager.getLogger(DataElementConceptDAOImpl.class.getName());

    private JdbcTemplate jdbcTemplate;

    private PropertyDAO propertyDAO;
    private ObjectClassDAO objectClassDAO;


    @Autowired
    ConceptDerivationRuleDAOImpl( DataSource dataSource ) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public ConceptDerivationRuleModel getCDRByIdseq(String condrIdseq) {
        String sql = "SELECT * FROM SBREXT.CON_DERIVATION_RULES_EXT WHERE CONDR_IDSEQ = ?";
        ConceptDerivationRuleModel conceptDerivationRuleModel = jdbcTemplate.queryForObject(sql, new Object[] { condrIdseq }, new ConceptDerivationRuleMapper());
        return conceptDerivationRuleModel;
    }




    public final class ConceptDerivationRuleMapper extends BeanPropertyRowMapper<ConceptDerivationRuleModel> {

        public ConceptDerivationRuleModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConceptDerivationRuleModel conceptDerivationRuleModel = new ConceptDerivationRuleModel();
            // don't know what needs to be mapped yet
            return conceptDerivationRuleModel;
        }
    }
}
