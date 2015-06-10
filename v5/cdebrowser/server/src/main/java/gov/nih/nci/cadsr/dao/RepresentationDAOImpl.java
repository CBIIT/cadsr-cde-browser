package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ConceptDerivationRuleModel;
import gov.nih.nci.cadsr.dao.model.ContextModel;
import gov.nih.nci.cadsr.dao.model.RepresentationModel;
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
public class RepresentationDAOImpl extends AbstractDAOOperations implements RepresentationDAO {

    private Logger logger = LogManager.getLogger(RepresentationDAOImpl.class.getName());

    private JdbcTemplate jdbcTemplate;

    private ConceptDerivationRuleDAO conceptDerivationRuleDAO;
    private ContextDAO contextDAO;


    @Autowired
    RepresentationDAOImpl( DataSource dataSource ) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public RepresentationModel getRepresentationByIdseq(String representationIdseq) {
        String sql = "SELECT * FROM SBREXT.REPRESENTATIONS_EXT WHERE REP_IDSEQ = ?";
        logger.debug( sql.replace( "?", representationIdseq ) + " << << << <<" );
        RepresentationModel representationModel = jdbcTemplate.queryForObject(sql, new Object[] { representationIdseq }, new RepresentationMapper());
        logger.debug("representationModel: " +  representationModel);
        return representationModel;
    }

    @Override
    public RepresentationModel getRepresentationById( String representationId )
    {
        String sql = "SELECT * FROM SBREXT.REPRESENTATIONS_EXT WHERE REP_ID = ?";
        logger.debug( sql.replace( "?", representationId ) + " << << << <<" );
        RepresentationModel representationModel = jdbcTemplate.queryForObject(sql, new Object[] { representationId }, new RepresentationMapper());
        logger.debug("representationModel: " +  representationModel);
        return representationModel;
    }


    public final class RepresentationMapper extends BeanPropertyRowMapper<RepresentationModel> {
        private Logger logger = LogManager.getLogger(RepresentationMapper.class.getName());

        public RepresentationModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            RepresentationModel representationModel = new RepresentationModel();

//            protected String preferredName;
//            protected String longName;
//            protected Float version;
//            protected ContextModel context;
//            protected int publicId;
//            protected String idseq;
//            private ConceptDerivationRuleModel conceptDerivationRuleModel;

            representationModel.setPreferredName(rs.getString("PREFERRED_NAME"));
            representationModel.setLongName(rs.getString("LONG_NAME"));
            representationModel.setVersion(rs.getFloat("VERSION"));
            representationModel.setPublicId(rs.getInt("REP_ID"));
            representationModel.setIdseq(rs.getString("REP_IDSEQ"));

            representationModel.setContext(getContextDAO().getContextByIdseq(rs.getString("CONTE_IDSEQ")));
            representationModel.setConceptDerivationRuleModel(getConceptDerivationRuleDAO().getCDRByIdseq(rs.getString("CONDR_IDSEQ")));


            return representationModel;
        }
    }

    public ConceptDerivationRuleDAO getConceptDerivationRuleDAO() {
        return conceptDerivationRuleDAO;
    }

    public void setConceptDerivationRuleDAO(ConceptDerivationRuleDAO conceptDerivationRuleDAO) {
        this.conceptDerivationRuleDAO = conceptDerivationRuleDAO;
    }

    public ContextDAO getContextDAO() {
        return contextDAO;
    }

    public void setContextDAO(ContextDAO contextDAO) {
        this.contextDAO = contextDAO;
    }
}
