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
            /* need to map these members:
            List<ReferenceDocModel> refDocs;
            List<DesignationModel> designationModels;
            ValueDomainModel valueDomainModel;
            DataElementConceptModel dec;
            ContextModel context; */
            String deIdseq = rs.getString("DE_IDSEQ");
            dataElementModel.setRefDocs(getReferenceDocDAO().getRefDocsByAcIdseq(deIdseq));
            dataElementModel.fillPreferredQuestionText();
            dataElementModel.setDesignationModels(getDesignationDAO().getDesignationModelsByAcIdseq(deIdseq));
            dataElementModel.fillUsingContexts();
            try {
                dataElementModel.setValueDomainModel(getValueDomainDAO().getValueDomainByIdseq(rs.getString("VD_IDSEQ")));
            } catch (EmptyResultDataAccessException ex) {
                logger.warn("No Value Domain found for Data Element with idseq: " + deIdseq + "  the vdIdseq is " + rs.getString("VD_IDSEQ"));
            }
            try {
                dataElementModel.setDec(getDataElementConceptDAO().getDecByDecIdseq(deIdseq));
            } catch (EmptyResultDataAccessException ex) {
                logger.warn("No DataElementConcept found for Data Element with idseq: " + deIdseq);
            }
            dataElementModel.setContext(getContextDAO().getContextByIdseq(rs.getString("CONTE_IDSEQ")));
            dataElementModel.setContextName(dataElementModel.getContext().getName());
            dataElementModel.setPublicId(dataElementModel.getCdeId());
            AcRegistrationsModel acRegistrationsModel = getAcRegistrationsDAO().getAcRegistrationByAcIdseq(deIdseq);
            if (acRegistrationsModel != null && acRegistrationsModel.getRegistrationStatus() != null) {
                dataElementModel.setRegistrationStatus(acRegistrationsModel.getRegistrationStatus());
            }
            return dataElementModel;
        }
    }

}
