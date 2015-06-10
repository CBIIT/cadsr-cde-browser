package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ConceptDerivationRuleModel;
import gov.nih.nci.cadsr.dao.model.ConceptualDomainModel;
import gov.nih.nci.cadsr.dao.model.ValueDomainModel;
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
public class ValueDomainDAOImpl extends AbstractDAOOperations implements ValueDomainDAO {

    private Logger logger = LogManager.getLogger( ValueDomainDAOImpl.class.getName() );

    private JdbcTemplate jdbcTemplate;

    private RepresentationDAO representationDAO;
    private ConceptDerivationRuleDAO conceptDerivationRuleDAO;
    private ConceptualDomainDAO conceptualDomainDAO;

    @Autowired
    ValueDomainDAOImpl(DataSource dataSource) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public ValueDomainModel getValueDomainByIdseq(String vdIdseq) throws EmptyResultDataAccessException {
        String sql = "SELECT * FROM SBR.VALUE_DOMAINS WHERE VD_IDSEQ = ?";
        logger.debug( sql.replace( "?", vdIdseq ) + " <<<<<<<" );
        //String sql = "SELECT * FROM VALUE_DOMAINS_VIEW WHERE VD_IDSEQ = ?";
        ValueDomainModel valueDomainModel = jdbcTemplate.queryForObject(sql, new Object[]{vdIdseq}, new ValueDomainMapper());
        return valueDomainModel;
    }

    public RepresentationDAO getRepresentationDAO() {
        return representationDAO;
    }

    public void setRepresentationDAO(RepresentationDAO representationDAO) {
        this.representationDAO = representationDAO;
    }

    public ConceptDerivationRuleDAO getConceptDerivationRuleDAO() {
        return conceptDerivationRuleDAO;
    }

    public void setConceptDerivationRuleDAO(ConceptDerivationRuleDAO conceptDerivationRuleDAO) {
        this.conceptDerivationRuleDAO = conceptDerivationRuleDAO;
    }

    public ConceptualDomainDAO getConceptualDomainDAO() {
        return conceptualDomainDAO;
    }

    public void setConceptualDomainDAO(ConceptualDomainDAO conceptualDomainDAO) {
        this.conceptualDomainDAO = conceptualDomainDAO;
    }

    public final class ValueDomainMapper extends BeanPropertyRowMapper<ValueDomainModel> {
        private Logger logger = LogManager.getLogger( ValueDomainMapper.class.getName() );

        public ValueDomainModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ValueDomainModel valueDomainModel = new ValueDomainModel();
            valueDomainModel.setPreferredName(rs.getString("PREFERRED_NAME"));
            valueDomainModel.setPreferredDefinition(rs.getString("PREFERRED_DEFINITION"));
            valueDomainModel.setLongName(rs.getString("LONG_NAME"));
            valueDomainModel.setAslName(rs.getString("ASL_NAME"));
            valueDomainModel.setVersion(rs.getFloat("VERSION"));
            valueDomainModel.setDeletedInd(rs.getString("DELETED_IND"));
            valueDomainModel.setLatestVerInd(rs.getString("LATEST_VERSION_IND"));
            valueDomainModel.setPublicId(rs.getInt("VD_ID"));
            valueDomainModel.setOrigin(rs.getString("ORIGIN"));
            valueDomainModel.setIdseq(rs.getString("VD_IDSEQ"));
            valueDomainModel.setVdIdseq(rs.getString("VD_IDSEQ"));// duplicate
            valueDomainModel.setDatatype(rs.getString("DTL_NAME")); // according to old code (BC4JValueDomainTransferObject)
            valueDomainModel.setUom(rs.getString("UOML_NAME")); // according to old code (BC4JValueDomainTransferObject)
            valueDomainModel.setDispFormat(rs.getString("FORML_NAME"));
            valueDomainModel.setMaxLength(rs.getInt("MAX_LENGTH_NUM"));
            valueDomainModel.setMinLength(rs.getInt("MIN_LENGTH_NUM"));
            valueDomainModel.setHighVal(rs.getString("HIGH_VALUE_NUM"));
            valueDomainModel.setLowVal(rs.getString("LOW_VALUE_NUM"));
            valueDomainModel.setCharSet(rs.getString("CHAR_SET_NAME"));
            valueDomainModel.setDecimalPlace(rs.getInt("DECIMAL_PLACE"));
            valueDomainModel.setVdType(rs.getString("VD_TYPE_FLAG"));

            try {
                ConceptDerivationRuleModel conceptDerivationRuleModel = getConceptDerivationRuleDAO().getCDRByIdseq(rs.getString("CONDR_IDSEQ"));
                if (conceptDerivationRuleModel != null) {
                    valueDomainModel.setConceptDerivationRuleModel(conceptDerivationRuleModel);
                }
            } catch (EmptyResultDataAccessException ex) {
                // this isn't a problem, just means there's no associated ConceptDerivationRule
                // valueDomainModel.setConceptDerivationRuleModel(new ConceptDerivationRuleModel());
            }


            try {
                logger.debug( "rs.getString(\"REP_IDSEQ\"): " + rs.getString("REP_IDSEQ") );
                valueDomainModel.setRepresentationModel(getRepresentationDAO().getRepresentationByIdseq(rs.getString("REP_IDSEQ")));

            } catch (EmptyResultDataAccessException ex) {
                // this isn't a problem, just means there's no associated RepresentationModel
            }




            try {
                ConceptualDomainModel conceptualDomainModel = getConceptualDomainDAO().getConceptualDomainByIdseq(rs.getString("CD_IDSEQ"));
                if (conceptualDomainModel != null) {
                    valueDomainModel.setCdPublicId(conceptualDomainModel.getCdId());
                    if (conceptualDomainModel.getPreferredName() != null) {
                        valueDomainModel.setCdPrefName(conceptualDomainModel.getPreferredName());
                    }
                    if (conceptualDomainModel.getVersion() != null) {
                        valueDomainModel.setCdVersion(conceptualDomainModel.getVersion());
                    }
                    if (conceptualDomainModel.getContextModel() != null && conceptualDomainModel.getContextModel().getName() != null) {
                        valueDomainModel.setCdContextName(conceptualDomainModel.getContextModel().getName());
                    }
                }
            } catch (EmptyResultDataAccessException ex) {
                // this isn't a problem, just means there's no associated ConceptualDomainModel
            }
            logger.debug( "valueDomainModel.getRepresentationModel: " + valueDomainModel.getRepresentationModel().toString() );

            return valueDomainModel;
        }
    }
}
