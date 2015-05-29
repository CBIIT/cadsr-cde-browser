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
import java.util.ArrayList;
import java.util.List;


public class DataElementDAOImpl extends AbstractDAOOperations implements DataElementDAO {
    private Logger logger = LogManager.getLogger(DataElementDAOImpl.class.getName());

    private JdbcTemplate jdbcTemplate;

    private ContextDAO contextDAO;
    private DataElementConceptDAO dataElementConceptDAO;
    private ValueDomainDAO valueDomainDAO;
    private DesignationDAO designationDAO;
    private DefinitionDAO definitionDAO;
    private ReferenceDocDAO referenceDocDAO;
    private AcRegistrationsDAO acRegistrationsDAO;
    private CsCsiDAO csCsiDAO;
    private UsageDAO usageDAO;
    private DEOtherVersionsDAO deOtherVersionsDAO;
    private CSRefDocDAO csRefDocDAO;
    private CSIRefDocDAO csiRefDocDAO;

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
        DataElementModel dataElementModel = jdbcTemplate.queryForObject(sql, new Object[]{deIdseq}, new DataElementMapper(DataElementModel.class));
        return dataElementModel;
    }

    @Override
    public List<DataElementModel> getAllCdeByCdeId(Integer cdeId) {
        String sql = "SELECT * FROM DATA_ELEMENTS WHERE cde_id = ?";
        List<DataElementModel> dataElementModel = jdbcTemplate.query(sql, new Object[]{cdeId}, new DataElementMapper(DataElementModel.class));
        return dataElementModel;
    }

    @Override
    public DataElementModel geCdeByCdeIdAndVersion(Integer cdeId, Integer version) {
        String sql = "SELECT * FROM DATA_ELEMENTS WHERE cde_id = ? and version = ?";
        DataElementModel dataElementModel = jdbcTemplate.queryForObject(sql, new Object[]{cdeId, version}, new DataElementMapper(DataElementModel.class));
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

    public DefinitionDAO getDefinitionDAO() {
        return definitionDAO;
    }

    public void setDefinitionDAO(DefinitionDAO definitionDAO) {
        this.definitionDAO = definitionDAO;
    }

    public CsCsiDAO getCsCsiDAO() {
        return csCsiDAO;
    }

    public void setCsCsiDAO(CsCsiDAO csCsiDAO) {
        this.csCsiDAO = csCsiDAO;
    }

    public UsageDAO getUsageDAO() {
        return usageDAO;
    }

    public void setUsageDAO(UsageDAO usageDAO) {
        this.usageDAO = usageDAO;
    }

    public DEOtherVersionsDAO getDeOtherVersionsDAO() {
        return deOtherVersionsDAO;
    }

    public void setDeOtherVersionsDAO(DEOtherVersionsDAO deOtherVersionsDAO) {
        this.deOtherVersionsDAO = deOtherVersionsDAO;
    }

    public CSIRefDocDAO getCsiRefDocDAO() {
        return csiRefDocDAO;
    }

    public void setCsiRefDocDAO(CSIRefDocDAO csiRefDocDAO) {
        this.csiRefDocDAO = csiRefDocDAO;
    }

    public CSRefDocDAO getCsRefDocDAO() {
        return csRefDocDAO;
    }

    public void setCsRefDocDAO(CSRefDocDAO csRefDocDAO) {
        this.csRefDocDAO = csRefDocDAO;
    }

    public final class DataElementMapper extends BeanPropertyRowMapper<DataElementModel> {
        private Logger logger = LogManager.getLogger(DataElementMapper.class.getName());

        public DataElementMapper(Class<DataElementModel> mappedClass) {
            super(mappedClass);
        }

        public DataElementModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            DataElementModel dataElementModel = super.mapRow(rs, rowNum);

            String deIdseq = rs.getString("DE_IDSEQ");
            dataElementModel.setDeIdseq(deIdseq);
            dataElementModel.setLatestVerInd(rs.getString("LATEST_VERSION_IND"));
            dataElementModel.fillPreferredQuestionText();
            dataElementModel.fillUsingContexts();

            dataElementModel.setRefDocs(getReferenceDocDAO().getRefDocsByAcIdseq(deIdseq));

            try {
                dataElementModel.setDesignationModels(getDesignationDAO().getDesignationModelsByAcIdseq(deIdseq));
                dataElementModel.fillCsCsiDesignations();
            } catch (EmptyResultDataAccessException ex) {
                logger.warn("No Designation Models found for Data Element with idseq: " + deIdseq);
            }
            try {
                dataElementModel.setDefinitionModels(getDefinitionDAO().getAllDefinitionsByAcIdseq(deIdseq));
                dataElementModel.fillCsCsiDefinitions();
            } catch (EmptyResultDataAccessException ex) {
                logger.warn("No Definition Models found for Data Element with idseq: " + deIdseq);
            }

            try {
                dataElementModel.setValueDomainModel(getValueDomainDAO().getValueDomainByIdseq(rs.getString("VD_IDSEQ")));
            } catch (EmptyResultDataAccessException ex) {
                logger.warn("No Value Domain found for Data Element with idseq: " + deIdseq + "  the vdIdseq is " + rs.getString("VD_IDSEQ"));
            }

            try {
            dataElementModel.setDec(getDataElementConceptDAO().getDecByDecIdseq(rs.getString("DEC_IDSEQ")));
            } catch (EmptyResultDataAccessException ex) {
                logger.warn("No DataElementConcept found for Data Element with idseq: " + deIdseq + "  the DEC_IDSEQ is " + rs.getString("DEC_IDSEQ"));
            }
            dataElementModel.setContext(getContextDAO().getContextByIdseq(rs.getString("CONTE_IDSEQ")));
            if (dataElementModel.getContext() != null && dataElementModel.getContext().getName()!= null) {
                dataElementModel.setContextName(dataElementModel.getContext().getName());
            }
            if (dataElementModel.getContext() != null && dataElementModel.getContext().getConteIdseq()!= null) {
                dataElementModel.setConteIdseq(dataElementModel.getContext().getConteIdseq()); // rudely redundant
            }
            dataElementModel.setPublicId(dataElementModel.getCdeId());

            try {
                AcRegistrationsModel acRegistrationsModel = getAcRegistrationsDAO().getAcRegistrationByAcIdseq(deIdseq);

                if (acRegistrationsModel != null && acRegistrationsModel.getRegistrationStatus() != null) {
                    dataElementModel.setRegistrationStatus(acRegistrationsModel.getRegistrationStatus());
                }
            } catch (EmptyResultDataAccessException ex) {
                logger.warn("No AcRegistrationsModel found for Data Element with idseq: " + deIdseq);
            }

            try {
                List<CsCsiModel> csCsiModels = getCsCsiDAO().getAltNamesAndDefsByDataElement(deIdseq);
                if (csCsiModels != null && csCsiModels.size() > 0) {
                    dataElementModel.fillCsCsiData(csCsiModels);
                } else {
                    // none found. Call fillCsCsiData() to initalize the "Unclassified" record
                    dataElementModel.fillCsCsiData(new ArrayList<CsCsiModel>(0));
                }
            } catch (EmptyResultDataAccessException ex) {
                logger.warn("No CsCsiModels found for Data Element with idseq: " + deIdseq);
                // none found. Call fillCsCsiData() to initalize the "Unclassified" record
                dataElementModel.fillCsCsiData(new ArrayList<CsCsiModel>(0));
            }
            try {
                dataElementModel.setUsageModels(getUsageDAO().getUsagesByDeIdseq(deIdseq));
            } catch (EmptyResultDataAccessException ex) {
                logger.warn("No UsageModels found for Data Element with idseq: " + deIdseq);
            }
            try {
                dataElementModel.setDeOtherVersionsModels(getDeOtherVersionsDAO().getOtherVersions(dataElementModel.getCdeId(), deIdseq));
            } catch (EmptyResultDataAccessException ex) {
                logger.warn("No Other Versions found for Data Element with idseq: " + deIdseq);
            }
            try {
                dataElementModel.setClassifications(getCsCsiDAO().getCsCsisByAcIdseq(deIdseq));
            } catch (EmptyResultDataAccessException ex) {
                logger.warn("No Other Versions found for Data Element with idseq: " + deIdseq);
            }
            try {
                dataElementModel.setCsRefDocModels(getCsRefDocDAO().getCSRefDocsByDEIdseq(deIdseq));
            } catch (EmptyResultDataAccessException ex) {
                logger.info("No Classif scheme found for Reference Docs for Data Element with idseq: " + deIdseq);
            }
            try {
                dataElementModel.setCsiRefDocModels(getCsiRefDocDAO().getCSIRefDocsByDEIdseq(deIdseq));
            } catch (EmptyResultDataAccessException ex) {
                logger.warn("No Classif scheme items found for Reference Docs for Data Element with idseq: " + deIdseq);
            }


            return dataElementModel;
        }
    }

}
