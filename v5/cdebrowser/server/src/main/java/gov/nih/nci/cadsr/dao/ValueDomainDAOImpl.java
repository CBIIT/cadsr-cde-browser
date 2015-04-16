package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ValueDomainModel;
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
 * Created by lavezzojl on 4/15/15.
 */
public class ValueDomainDAOImpl extends AbstractDAOOperations implements ValueDomainDAO {

    private Logger logger = LogManager.getLogger(ValueDomainDAOImpl.class.getName());

    private JdbcTemplate jdbcTemplate;

    @Autowired
    ValueDomainDAOImpl( DataSource dataSource ) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public ValueDomainModel getValueDomainByIdseq(String vdIdseq) {
        String sql = "SELECT * FROM VALUE_DOMAINS WHERE VD_IDSEQ = ?";
        ValueDomainModel valueDomainModel = jdbcTemplate.queryForObject(sql, new Object[] { vdIdseq }, new ValueDomainMapper());
        return valueDomainModel;
    }



    public final class ValueDomainMapper extends BeanPropertyRowMapper<ValueDomainModel> {

        public ValueDomainModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ValueDomainModel valueDomainModel = new ValueDomainModel();
            /* TODO:  need to map these members:
            private RepresentationModel representationModel;
            private ConceptDerivationRuleModel conceptDerivationRuleModel;
            * */
            return valueDomainModel;
        }
    }
}
