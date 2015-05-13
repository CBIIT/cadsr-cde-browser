package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.UsageModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by lavezzojl on 5/13/15.
 */
public class UsageDAOImpl extends AbstractDAOOperations implements UsageDAO {

    private Logger logger = LogManager.getLogger(UsageDAOImpl.class.getName());

    private JdbcTemplate jdbcTemplate;

    @Autowired
    UsageDAOImpl( DataSource dataSource ) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }

// todo: this needs left join on protocols_ext to allow for null protocols!
    @Override
    public List<UsageModel> getUsagesByDeIdseq(String deIdseq) {
        String sql = "select qc_basic.long_name question_name\n" +
                ", qc_basic.DN_CRF_IDSEQ\n" +
                ", qc_crf.version\n" +
                ", qc_crf.long_name form_name\n" +
                ", qc_crf.qc_id public_id\n" +
                ", qc_crf.qtl_name form_usage_type\n" +
                ", qc_crf.qc_idseq form_idseq\n" +
                ", p.lead_org\n" +
                ", p.long_name protocol_number\n" +
                "FROM quest_contents_ext qc_basic, quest_contents_ext qc_crf, protocols_ext p, protocol_qc_ext pq\n" +
                "WHERE qc_basic.de_idseq = ?\n" +
                "AND qc_basic.DN_CRF_IDSEQ = qc_crf.qc_idseq\n" +
                "AND p.PROTO_IDSEQ = pq.PROTO_IDSEQ\n" +
                "AND qc_crf.qc_idseq = pq.qc_idseq";
        List<UsageModel> usageModels = jdbcTemplate.query(sql, new Object[]{deIdseq}, new BeanPropertyRowMapper(UsageModel.class));
        return usageModels;
    }
}
