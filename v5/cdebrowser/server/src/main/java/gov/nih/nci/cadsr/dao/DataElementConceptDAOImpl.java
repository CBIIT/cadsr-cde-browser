package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ContextModel;
import gov.nih.nci.cadsr.dao.model.DataElementConceptModel;
import gov.nih.nci.cadsr.dao.model.ObjectClassModel;
import gov.nih.nci.cadsr.dao.model.PropertyModel;
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
public class DataElementConceptDAOImpl extends AbstractDAOOperations implements DataElementConceptDAO {

    private Logger logger = LogManager.getLogger(DataElementConceptDAOImpl.class.getName());

    private JdbcTemplate jdbcTemplate;

    private PropertyDAO propertyDAO;
    private ObjectClassDAO objectClassDAO;
    private ContextDAO contextDAO;


    @Autowired
    DataElementConceptDAOImpl(DataSource dataSource) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public DataElementConceptModel getDecByDecIdseq(String decIdseq) throws EmptyResultDataAccessException {
        String sql = "SELECT * FROM SBR.DATA_ELEMENT_CONCEPTS WHERE DEC_IDSEQ = ?";
        DataElementConceptModel dataElementConceptModel = jdbcTemplate.queryForObject(sql, new Object[]{decIdseq}, new DataElementConceptMapper());
        return dataElementConceptModel;
    }


    public PropertyDAO getPropertyDAO() {
        return propertyDAO;
    }

    public void setPropertyDAO(PropertyDAO propertyDAO) {
        this.propertyDAO = propertyDAO;
    }

    public ObjectClassDAO getObjectClassDAO() {
        return objectClassDAO;
    }

    public void setObjectClassDAO(ObjectClassDAO objectClassDAO) {
        this.objectClassDAO = objectClassDAO;
    }

    public ContextDAO getContextDAO() {
        return contextDAO;
    }

    public void setContextDAO(ContextDAO contextDAO) {
        this.contextDAO = contextDAO;
    }

    public final class DataElementConceptMapper extends BeanPropertyRowMapper<DataElementConceptModel> {

        public DataElementConceptModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            DataElementConceptModel dataElementConceptModel = new DataElementConceptModel();

            dataElementConceptModel.setPreferredName(rs.getString("PREFERRED_NAME"));
            dataElementConceptModel.setPreferredDefinition(rs.getString("PREFERRED_DEFINITION"));
            dataElementConceptModel.setLongName(rs.getString("LONG_NAME"));
            dataElementConceptModel.setAslName(rs.getString("ALS_NAME"));
            dataElementConceptModel.setVersion(rs.getFloat("VERSION"));
            dataElementConceptModel.setDeletedInd(rs.getString("DELETED_IND"));
            dataElementConceptModel.setLatestVerInd(rs.getString("LATEST_VERSION_IND"));
            dataElementConceptModel.setPublicId(rs.getInt("DEC_ID"));
            dataElementConceptModel.setDecIdseq(rs.getString("DEC_IDSEQ"));
            dataElementConceptModel.setCdIdseq(rs.getString("CD_IDSEQ"));
            dataElementConceptModel.setProplName(rs.getString("PROPL_NAME"));
            dataElementConceptModel.setOclName(rs.getString("OCL_NAME"));
            dataElementConceptModel.setObjClassQualifier(rs.getString("OBJ_CLASS_QUALIFIER"));
            dataElementConceptModel.setPropertyQualifier(rs.getString("PROPERTY_QUALIFIER"));
            dataElementConceptModel.setChangeNote(rs.getString("CHANGE_NOTE"));
//            dataElementConceptModel.


            PropertyModel propertyModel = getPropertyDAO().getPropertyByIdseq("PROP_IDSEQ");
            if (propertyModel != null) {
                dataElementConceptModel.setProperty(propertyModel);
                if (propertyModel.getPreferredName() != null) {
                    dataElementConceptModel.setPropertyPrefName(propertyModel.getPreferredName());
                }
                if (propertyModel.getContext() != null && propertyModel.getContext().getName() != null) {
                    dataElementConceptModel.setPropertyContextName(propertyModel.getContext().getName());
                }
                if (propertyModel.getVersion() != null) {
                    dataElementConceptModel.setPropertyVersion(propertyModel.getVersion());
                }
            }
            ObjectClassModel objectClassModel = getObjectClassDAO().getObjectClassByIdseq("OC_IDSEQ");
            if (objectClassModel != null) {
                dataElementConceptModel.setObjectClassModel(objectClassModel);
                if (objectClassModel.getPreferredName() != null) {
                    dataElementConceptModel.setObjClassPrefName(objectClassModel.getPreferredName());
                }
                if (objectClassModel.getContext() != null && objectClassModel.getContext().getName() != null) {
                    dataElementConceptModel.setObjClassContextName(objectClassModel.getContext().getName());
                }
                if (objectClassModel.getVersion() != null) {
                    dataElementConceptModel.setObjClassVersion(objectClassModel.getVersion());
                }
            }
            ContextModel contextModel = getContextDAO().getContextByIdseq(rs.getString("CONTE_IDSEQ"));
            if (contextModel != null && contextModel.getName() != null) {
                dataElementConceptModel.setConteName(contextModel.getName());
            }
            // todo implement Concept Domain and add it to this class to populate the cd* fields
            return dataElementConceptModel;
        }
    }

}
