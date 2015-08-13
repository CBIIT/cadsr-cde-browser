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
public class UsageDAOImpl extends AbstractDAOOperations implements UsageDAO
{

    private Logger logger = LogManager.getLogger( UsageDAOImpl.class.getName() );

    private JdbcTemplate jdbcTemplate;

    @Autowired
    UsageDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public List<UsageModel> getUsagesByDeIdseq( String deIdseq )
    {
        String sql = "SELECT " +
                " NVL(proto.long_name,'N/A') protocol_number " +
                ", NVL(proto.lead_org,'N/A') lead_org" +
                ", crf.long_name form_name" +
                ", crf.qtl_name form_usage_type" +
 ", crf.qc_idseq form_idseq " +
                //      ", proto.proto_idseq " +
                //      ", que.de_idseq " +
                ", que.long_name question_name " +
                //      ", que.de_idseq que_de_idseq " +
                ", crf.qc_id public_id " +
                ", crf.version version " +

                //For usage.publicId in usage-view.html when we have a service to answer.
                //    ", (SELECT (SELECT VALUE FROM tool_options_ext WHERE tool_name ='FormBuilder' AND property='URL') FROM dual) FRM_URL" +
                //    ", (SELECT (SELECT VALUE FROM tool_options_ext WHERE tool_name ='FormBuilder' AND property='FRM_DET_URL') FROM dual) FRM_DET_URL " +

                " FROM " +
                " sbrext.protocols_ext proto" +
                ", sbrext.quest_contents_ext crf" +
                ", sbrext.quest_contents_ext que" +
                ", sbrext.protocol_qc_ext proto_qc " +

                " WHERE " +
                "crf.qc_idseq = proto_qc.qc_idseq(+) " +
                "AND proto.proto_idseq(+) = proto_qc.proto_idseq " +
                "AND crf.qc_idseq = que.dn_crf_idseq " +
                "AND que.de_idseq = ? " +
                "AND crf.qtl_name IN ('CRF','TEMPLATE') " +
                "AND que.qtl_name = 'QUESTION' " +
                "AND crf.deleted_ind = 'No' " +
                " ORDER BY " +
                " protocol_number" +
                ", form_name" +
                ", question_name";
        logger.debug( sql.replace( "?", deIdseq ) );
        List<UsageModel> usageModels = jdbcTemplate.query( sql, new Object[]{ deIdseq }, new BeanPropertyRowMapper( UsageModel.class ) );
        return usageModels;
    }
}
