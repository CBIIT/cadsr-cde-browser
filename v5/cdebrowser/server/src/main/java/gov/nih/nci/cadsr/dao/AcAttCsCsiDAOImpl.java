package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.common.util.StringReplace;
import gov.nih.nci.cadsr.dao.model.AcAttCsCsiModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;


import javax.sql.DataSource;
import java.util.List;

/**
 * DAO for SBREXT.AC_ATT_CSCSI_EXT
 */
public class AcAttCsCsiDAOImpl extends AbstractDAOOperations implements AcAttCsCsiDAO {

    private Logger logger = LogManager.getLogger(AcAttCsCsiDAOImpl.class.getName());

    private JdbcTemplate jdbcTemplate;
    private ContextDAO contextDAO;

    @Autowired
    AcAttCsCsiDAOImpl(DataSource dataSource) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public List<AcAttCsCsiModel> getAllAcAttCsCsiByAttIdseq(String attIdseq) {
        String sql = "SELECT * FROM sbrext.ac_att_cscsi_ext WHERE att_idseq = ?";
        logger.debug(sql + " " + attIdseq);
        List<AcAttCsCsiModel> acAttCsCsiModels = jdbcTemplate.query(sql, new Object[]{attIdseq}, new BeanPropertyRowMapper(AcAttCsCsiModel.class));
        return acAttCsCsiModels;
    }

}
