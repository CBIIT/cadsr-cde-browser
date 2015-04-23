package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ContextModel;
import gov.nih.nci.cadsr.dao.model.ObjectClassModel;
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
public class ObjectClassDAOImpl extends AbstractDAOOperations implements ObjectClassDAO {

    private Logger logger = LogManager.getLogger(ObjectClassDAOImpl.class.getName());

    private JdbcTemplate jdbcTemplate;

    private ContextDAO contextDAO;


    @Autowired
    ObjectClassDAOImpl( DataSource dataSource ) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public ObjectClassModel getObjectClassByIdseq(String ocIdseq) {
        String sql = "SELECT * FROM SBREXT.OBJECT_CLASSES_EXT WHERE OC_IDSEQ = ?";
        ObjectClassModel objectClassModel = jdbcTemplate.queryForObject(sql, new Object[] { ocIdseq }, new ObjectClassMapper(ObjectClassModel.class));
        return objectClassModel;
    }


    public ContextDAO getContextDAO() {
        return contextDAO;
    }

    public void setContextDAO(ContextDAO contextDAO) {
        this.contextDAO = contextDAO;
    }


    public final class ObjectClassMapper extends BeanPropertyRowMapper<ObjectClassModel> {
        public ObjectClassMapper(Class<ObjectClassModel> mappedClass) {
            super(mappedClass);
        }

        public ObjectClassModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ObjectClassModel objectClassModel = super.mapRow(rs, rowNum);

//            objectClassModel.setPreferredName(rs.getString("PREFERRED_NAME"));
//            objectClassModel.setLongName(rs.getString("LONG_NAME"));
//            objectClassModel.setVersion(rs.getFloat("VERSION"));
            objectClassModel.setPublicId(rs.getInt("OC_ID"));
            objectClassModel.setIdseq(rs.getString("OC_IDSEQ"));

            objectClassModel.setContext(getContextDAO().getContextByIdseq(rs.getString("CONTE_IDSEQ")));
            return objectClassModel;
        }
    }
}
