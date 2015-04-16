package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ReferenceDocModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by lavezzojl on 4/15/15.
 */
public class ReferenceDocDAOImpl extends AbstractDAOOperations implements ReferenceDocDAO {
    private Logger logger = LogManager.getLogger(DataElementDAOImpl.class.getName());

    private JdbcTemplate jdbcTemplate;

    private ContextDAO contextDAO;

    @Autowired
    ReferenceDocDAOImpl(DataSource dataSource ) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }


    @Override
    public List<ReferenceDocModel> getRefDocsByRdIdseq(String rdIdseq) {
        String sql = "SELECT * FROM REFERENCE_DOCUMENTS WHERE RD_IDSEQ = ?";
        List<ReferenceDocModel> referenceDocModel = jdbcTemplate.query(sql, new Object[]{rdIdseq}, new ReferenceDocMapper());
        return referenceDocModel;
    }



    @Override
    public List<ReferenceDocModel> getRefDocsByAcIdseq(String acIdseq) {

        String sql = "SELECT * FROM REFERENCE_DOCUMENTS WHERE AC_IDSEQ = ?";
        List<ReferenceDocModel> referenceDocModel = jdbcTemplate.query(sql, new Object[]{acIdseq}, new ReferenceDocMapper());
        return referenceDocModel;
    }


    public ContextDAO getContextDAO() {
        return contextDAO;
    }

    public void setContextDAO(ContextDAO contextDAO) {
        this.contextDAO = contextDAO;
    }

    public final class ReferenceDocMapper extends BeanPropertyRowMapper<ReferenceDocModel> {

        public ReferenceDocModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ReferenceDocModel referenceDocModel = new ReferenceDocModel();
            referenceDocModel.setContext(getContextDAO().getContextByIdseq(rs.getString("CONTE_IDSEQ")));
            return referenceDocModel;
        }
    }
}
