package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ContextModel;
import gov.nih.nci.cadsr.dao.model.DefinitionModel;
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
import java.util.List;

/**
 * Created by lavezzojl on 5/6/15.
 */
public class DefinitionDAOImpl extends AbstractDAOOperations implements DefinitionDAO {

    private Logger logger = LogManager.getLogger(DefinitionDAOImpl.class.getName());

    private JdbcTemplate jdbcTemplate;
    private ContextDAO contextDAO;

    @Autowired
    DefinitionDAOImpl(DataSource dataSource) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }


    public ContextDAO getContextDAO() {
        return contextDAO;
    }

    public void setContextDAO(ContextDAO contextDAO) {
        this.contextDAO = contextDAO;
    }

    @Override
    public DefinitionModel getDefinitionByDefinIdseq(String definIdseq) {
        String sql = "SELECT * FROM SBR.DEFINITIONS WHERE defin_idseq = ?";
        DefinitionModel definitionModel = jdbcTemplate.queryForObject(sql, new Object[]{ definIdseq }, new DefinitionMapper(DefinitionModel.class));
        return definitionModel;
    }

    @Override
    public List<DefinitionModel> getAllDefinitionsByAcIdseq(String acIdseq) {
        String sql = "SELECT * FROM SBR.DEFINITIONS WHERE ac_idseq = ?";
        List<DefinitionModel> definitionModels = jdbcTemplate.query(sql, new Object[]{acIdseq}, new DefinitionMapper(DefinitionModel.class));
        return definitionModels;
    }

    public final class DefinitionMapper extends BeanPropertyRowMapper<DefinitionModel> {
        private Logger logger = LogManager.getLogger(DefinitionMapper.class.getName());

        public DefinitionMapper(Class<DefinitionModel> mappedClass) {
            super(mappedClass);
        }

        public DefinitionModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            DefinitionModel definitionModel = super.mapRow(rs, rowNum);
            try {
                ContextModel contextModel = getContextDAO().getContextByIdseq(rs.getString("CONTE_IDSEQ"));
                if (contextModel != null) {
                    definitionModel.setContext(contextModel);
                }
            } catch (EmptyResultDataAccessException ex) {
                logger.warn("no contextModel found for CONTE_IDSEQ: " + rs.getString("CONTE_IDSEQ"));
            }

            return definitionModel;
        }
    }
}
