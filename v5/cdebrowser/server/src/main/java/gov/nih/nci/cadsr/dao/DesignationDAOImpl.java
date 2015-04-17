package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.DesignationModel;
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
public class DesignationDAOImpl extends AbstractDAOOperations implements DesignationDAO {
    private Logger logger = LogManager.getLogger(DesignationDAOImpl.class.getName());

    private JdbcTemplate jdbcTemplate;
    private ContextDAO contextDAO;

    @Autowired
    DesignationDAOImpl(DataSource dataSource) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public List<DesignationModel> getDesignationModelsByAcIdseq(String acIdseq) {

        String sql = "SELECT * FROM DESIGNATIONS WHERE AC_IDSEQ = ?";
        List<DesignationModel> dataElementModel = jdbcTemplate.query(sql, new Object[]{acIdseq}, new DesignationMapper());
        return dataElementModel;
    }

    @Override
    public List<DesignationModel> getUsedByDesignationModels(String acIdseq) {

        String sql = "SELECT * FROM DESIGNATIONS WHERE AC_IDSEQ = ? and DETL_NAME = 'USED_BY'";
        List<DesignationModel> dataElementModel = jdbcTemplate.query(sql, new Object[]{acIdseq}, new DesignationMapper());
        return dataElementModel;
    }

    public ContextDAO getContextDAO() {
        return contextDAO;
    }

    public void setContextDAO(ContextDAO contextDAO) {
        this.contextDAO = contextDAO;
    }

    public final class DesignationMapper extends BeanPropertyRowMapper<DesignationModel> {

        public DesignationModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            DesignationModel designationModel = new DesignationModel();
            designationModel.setContex(getContextDAO().getContextByIdseq(rs.getString("CONTE_IDSEQ")));
            return designationModel;
        }
    }
}
