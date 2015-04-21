package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ConceptDerivationRuleModel;
import gov.nih.nci.cadsr.dao.model.ValueDomainModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by lavezzojl on 4/15/15.
 */
public class ValueDomainDAOImpl extends AbstractDAOOperations implements ValueDomainDAO {

    private Logger logger = LogManager.getLogger(ValueDomainDAOImpl.class.getName());

    private JdbcTemplate jdbcTemplate;

    private RepresentationDAO representationDAO;
    private ConceptDerivationRuleDAO conceptDerivationRuleDAO;

    @Autowired
    ValueDomainDAOImpl(DataSource dataSource) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public ValueDomainModel getValueDomainByIdseq(String vdIdseq) throws EmptyResultDataAccessException {
        //String sql = "SELECT * FROM SBR.VALUE_DOMAINS WHERE VD_IDSEQ = ?";
        String sql = "SELECT * FROM VALUE_DOMAINS_VIEW WHERE VD_IDSEQ = ?";
        ValueDomainModel valueDomainModel = jdbcTemplate.queryForObject(sql, new Object[]{vdIdseq}, new ValueDomainMapper());
        return valueDomainModel;
    }

    public RepresentationDAO getRepresentationDAO() {
        return representationDAO;
    }

    public void setRepresentationDAO(RepresentationDAO representationDAO) {
        this.representationDAO = representationDAO;
    }

    public ConceptDerivationRuleDAO getConceptDerivationRuleDAO() {
        return conceptDerivationRuleDAO;
    }

    public void setConceptDerivationRuleDAO(ConceptDerivationRuleDAO conceptDerivationRuleDAO) {
        this.conceptDerivationRuleDAO = conceptDerivationRuleDAO;
    }

    public final class ValueDomainMapper extends BeanPropertyRowMapper<ValueDomainModel> {

        public ValueDomainModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ValueDomainModel valueDomainModel = new ValueDomainModel();
            try {
                ConceptDerivationRuleModel conceptDerivationRuleModel = getConceptDerivationRuleDAO().getCDRByIdseq(rs.getString("CONDR_IDSEQ"));
                valueDomainModel.setConceptDerivationRuleModel(conceptDerivationRuleModel);
            } catch (EmptyResultDataAccessException ex) {
                // this isn't a problem, just means there's no associated ConceptDerivationRule
                // valueDomainModel.setConceptDerivationRuleModel(new ConceptDerivationRuleModel());
            }
            valueDomainModel.setRepresentationModel(getRepresentationDAO().getRepresentationByIdseq(rs.getString("REP_IDSEQ")));
            return valueDomainModel;
        }
    }
}
