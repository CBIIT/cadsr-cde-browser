package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ContextModel;
import gov.nih.nci.cadsr.dao.model.PropertyModel;
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
public class PropertyDAOImpl extends AbstractDAOOperations implements PropertyDAO {

    private Logger logger = LogManager.getLogger(PropertyDAOImpl.class.getName());

    private JdbcTemplate jdbcTemplate;

    private ContextDAO contextDAO;


    @Autowired
    PropertyDAOImpl( DataSource dataSource ) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }

    public PropertyModel getPropertyByIdseq(String propIdseq) {
        String sql = "SELECT * FROM sbrext.properties_ext WHERE prop_idseq = ?";
        PropertyModel propertyModel = jdbcTemplate.queryForObject(sql, new Object[] { propIdseq }, new PropertyMapper(PropertyModel.class));
        return propertyModel;
    }


    public ContextDAO getContextDAO() {
        return contextDAO;
    }

    public void setContextDAO(ContextDAO contextDAO) {
        this.contextDAO = contextDAO;
    }


    public final class PropertyMapper extends BeanPropertyRowMapper<PropertyModel> {
        public PropertyMapper(Class<PropertyModel> mappedClass) {
            super(mappedClass);
        }

        public PropertyModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            PropertyModel propertyModel = super.mapRow(rs, rowNum);

//            propertyModel.setPreferredName(rs.getString("PREFERRED_NAME"));
//            propertyModel.setLongName(rs.getString("LONG_NAME"));
//            propertyModel.setVersion(rs.getFloat("VERSION"));
            propertyModel.setPublicId(rs.getInt("PROP_ID"));
//            propertyModel.setName(rs.getString(""));
//            propertyModel.setQualifier(rs.getString(""));

            propertyModel.setContext(getContextDAO().getContextByIdseq(rs.getString("CONTE_IDSEQ")));
            return propertyModel;
        }
    }
}
