package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.*;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;


import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class DataElementDAOImpl extends AbstractDAOOperations implements DataElementDAO {
    private Logger logger = LogManager.getLogger(DataElementDAOImpl.class.getName());

    private JdbcTemplate jdbcTemplate;

    private ContextDAO contextDAO;
    private DataElementConceptDAO dataElementConceptDAO;
    private ValueDomainDAO valueDomainDAO;
    private DesignationDAO designationDAO;
    private ReferenceDocDAO referenceDocDAO;
    private AcRegistrationsDAO acRegistrationsDAO;

//private String DataElementSql; //Spring DAOs are singletons.  Doing this whole passing sql around as member variables is totally not safe...


    @Autowired
    DataElementDAOImpl(DataSource dataSource) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public List<DataElementModel> getCdeBySearchString(String dataElementSql) {
        List<DataElementModel> results;

        logger.debug("basicSearch");
        //logger.debug( ">>>>>>> "+ sql );
        results = getAll(dataElementSql, DataElementModel.class);
        //logger.debug( sql + " <<<<<<<" );
        logger.debug("Done basicSearch");

        return results;
    }

    @Override
    public DataElementModel getCdeByDeIdseq(String deIdseq) throws EmptyResultDataAccessException {
        String sql = "SELECT * FROM DATA_ELEMENTS WHERE de_idseq = ?";
        DataElementModel dataElementModel = jdbcTemplate.queryForObject(sql, new Object[]{deIdseq}, new DataElementMapper());
        return dataElementModel;
    }

    @Override
    public List<DataElementModel> getAllCdeByCdeId(Integer cdeId) {
        String sql = "SELECT * FROM DATA_ELEMENTS WHERE cde_id = ?";
        List<DataElementModel> dataElementModel = jdbcTemplate.query(sql, new Object[]{cdeId}, new DataElementMapper());
        return dataElementModel;
    }

    @Override
    public DataElementModel geCdeByCdeIdAndVersion(Integer cdeId, Integer version) {
        String sql = "SELECT * FROM DATA_ELEMENTS WHERE cde_id = ? and version = ?";
        DataElementModel dataElementModel = jdbcTemplate.queryForObject(sql, new Object[]{cdeId, version}, new DataElementMapper());
        return dataElementModel;
    }

    /*public String getDataElementSql()
    {
        return DataElementSql;
    }

    //@Override
    public void setDataElementSql( String dataElementSql )
    {
        this.DataElementSql = dataElementSql;
    }*/


    public ContextDAO getContextDAO() {
        return contextDAO;
    }

    public void setContextDAO(ContextDAO contextDAO) {
        this.contextDAO = contextDAO;
    }

    public DataElementConceptDAO getDataElementConceptDAO() {
        return dataElementConceptDAO;
    }

    public void setDataElementConceptDAO(DataElementConceptDAO dataElementConceptDAO) {
        this.dataElementConceptDAO = dataElementConceptDAO;
    }

    public ValueDomainDAO getValueDomainDAO() {
        return valueDomainDAO;
    }

    public void setValueDomainDAO(ValueDomainDAO valueDomainDAO) {
        this.valueDomainDAO = valueDomainDAO;
    }

    public DesignationDAO getDesignationDAO() {
        return designationDAO;
    }

    public void setDesignationDAO(DesignationDAO designationDAO) {
        this.designationDAO = designationDAO;
    }

    public ReferenceDocDAO getReferenceDocDAO() {
        return referenceDocDAO;
    }

    public void setReferenceDocDAO(ReferenceDocDAO referenceDocDAO) {
        this.referenceDocDAO = referenceDocDAO;
    }

    public AcRegistrationsDAO getAcRegistrationsDAO() {
        return acRegistrationsDAO;
    }

    public void setAcRegistrationsDAO(AcRegistrationsDAO acRegistrationsDAO) {
        this.acRegistrationsDAO = acRegistrationsDAO;
    }

    public final class DataElementMapper extends BeanPropertyRowMapper<DataElementModel> {
        private Logger logger = LogManager.getLogger(DataElementMapper.class.getName());

        public DataElementModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            DataElementModel dataElementModel = new DataElementModel();

            String deIdseq = rs.getString("DE_IDSEQ");
            dataElementModel.setVersion(rs.getFloat("VERSION"));
            dataElementModel.setIdseq(deIdseq);
            dataElementModel.setDeIdseq(deIdseq);
            dataElementModel.setPreferredName(rs.getString("PREFERRED_NAME"));
            dataElementModel.setVdIdseq(rs.getString("VD_IDSEQ"));
            dataElementModel.setDecIdseq(rs.getString("DEC_IDSEQ"));
            dataElementModel.setPreferredDefinition(rs.getString("PREFERRED_DEFINITION"));
            dataElementModel.setAslName(rs.getString("ASL_NAME"));
            dataElementModel.setLongName(rs.getString("LONG_NAME"));
            dataElementModel.setLatestVerInd(rs.getString("LATEST_VERSION_IND"));
            dataElementModel.setDeletedInd(rs.getString("DELETED_IND"));
            dataElementModel.setBeginDate(rs.getTimestamp("BEGIN_DATE"));
            dataElementModel.setEndDate(rs.getTimestamp("END_DATE"));
            dataElementModel.setOrigin(rs.getString("ORIGIN"));
            dataElementModel.setCdeId(rs.getInt("CDE_ID"));
            dataElementModel.setQuestion(rs.getString("QUESTION"));
            dataElementModel.setModifiedBy(rs.getString("MODIFIED_BY"));
            dataElementModel.setCreatedBy(rs.getString("CREATED_BY"));
            dataElementModel.setDateCreated(rs.getTimestamp("DATE_CREATED"));
            dataElementModel.setDateModified(rs.getTimestamp("DATE_MODIFIED"));
            dataElementModel.fillPreferredQuestionText();
            dataElementModel.setDesignationModels(getDesignationDAO().getDesignationModelsByAcIdseq(deIdseq));
            dataElementModel.fillUsingContexts();

            dataElementModel.setRefDocs(getReferenceDocDAO().getRefDocsByAcIdseq(deIdseq));

            try {
                dataElementModel.setValueDomainModel(getValueDomainDAO().getValueDomainByIdseq(rs.getString("VD_IDSEQ")));
            } catch (EmptyResultDataAccessException ex) {
                logger.warn("No Value Domain found for Data Element with idseq: " + deIdseq + "  the vdIdseq is " + rs.getString("VD_IDSEQ"));
            }

            try {
                dataElementModel.setDec(getDataElementConceptDAO().getDecByDecIdseq(rs.getString("DEC_IDSEQ")));
            } catch (EmptyResultDataAccessException ex) {
                logger.warn("No DataElementConcept found for Data Element with idseq: " + deIdseq);
            }
            dataElementModel.setContext(getContextDAO().getContextByIdseq(rs.getString("CONTE_IDSEQ")));
            if (dataElementModel.getContext() != null && dataElementModel.getContext().getName()!= null) {
                dataElementModel.setContextName(dataElementModel.getContext().getName());
            }
            if (dataElementModel.getContext() != null && dataElementModel.getContext().getConteIdseq()!= null) {
                dataElementModel.setConteIdseq(dataElementModel.getContext().getConteIdseq()); // rudely redundant
            }
            dataElementModel.setPublicId(dataElementModel.getCdeId());
            AcRegistrationsModel acRegistrationsModel = getAcRegistrationsDAO().getAcRegistrationByAcIdseq(deIdseq);
            if (acRegistrationsModel != null && acRegistrationsModel.getRegistrationStatus() != null) {
                dataElementModel.setRegistrationStatus(acRegistrationsModel.getRegistrationStatus());
            }
            return dataElementModel;
        }
    }

}
