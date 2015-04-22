package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ConceptualDomainModel;
import gov.nih.nci.cadsr.dao.model.ContextModel;
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
 * Created by lavezzojl on 4/22/15.
 */
public class ConceptualDomainDAOImpl extends AbstractDAOOperations implements ConceptualDomainDAO {

    private Logger logger = LogManager.getLogger(DataElementConceptDAOImpl.class.getName());

    private JdbcTemplate jdbcTemplate;

    private ContextDAO contextDAO;


    @Autowired
    ConceptualDomainDAOImpl(DataSource dataSource) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public ConceptualDomainModel getConceptualDomainByIdseq(String cdIdseq) {
        String sql = "SELECT * FROM SBR.CONCEPTUAL_DOMAINS WHERE CD_IDSEQ = ?";
        ConceptualDomainModel conceptualDomainModel = jdbcTemplate.queryForObject(sql, new Object[]{cdIdseq}, new ConceptualDomainMapper());
        return conceptualDomainModel;
    }

    public ContextDAO getContextDAO() {
        return contextDAO;
    }

    public void setContextDAO(ContextDAO contextDAO) {
        this.contextDAO = contextDAO;
    }

    public final class ConceptualDomainMapper extends BeanPropertyRowMapper<ConceptualDomainModel> {

        public ConceptualDomainModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConceptualDomainModel conceptualDomainModel = new ConceptualDomainModel();

            conceptualDomainModel.setPreferredName(rs.getString("PREFERRED_NAME"));
            conceptualDomainModel.setVersion(rs.getFloat("VERSION"));
            conceptualDomainModel.setCdId(rs.getInt("CD_ID"));
            conceptualDomainModel.setContextModel(getContextDAO().getContextByIdseq(rs.getString("CONTE_IDSEQ")));

            return conceptualDomainModel;
        }
    }
}
