package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.CSIRefDocModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by lavezzojl on 5/27/15.
 */
public class CSIRefDocDAOImpl extends AbstractDAOOperations implements CSIRefDocDAO
{
    private Logger logger = LogManager.getLogger(CSIRefDocDAOImpl.class.getName());

    private JdbcTemplate jdbcTemplate;

    @Autowired
    CSIRefDocDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public List<CSIRefDocModel> getCSIRefDocsByDEIdseq(String deIdseq) {
        /*
    public String csiName;
    public String documentName;
    public String documentType;
    public String documentText;
    public String url;
    public String attachments;*/
        // TODO attachments!
        String sql = "SELECT csi.csi_name csi_name, rd.name document_name, rd.dctl_name document_type, rd.doc_text document_text, rd.url " +
                "FROM sbr.reference_documents rd, sbr.cs_items csi " +
                "WHERE rd.ac_idseq in ( " +
                "SELECT cs_csi.csi_idseq FROM sbr.ac_csi, sbr.cs_csi " +
                "WHERE ac_csi.ac_idseq = ? " +
                "AND ac_csi.cs_csi_idseq = cs_csi.cs_csi_idseq " +
                ") " +
                "AND csi.csi_idseq = rd.ac_idseq";
        List<CSIRefDocModel> csiRefDocModels = jdbcTemplate.query(sql, new Object[]{deIdseq}, new BeanPropertyRowMapper(CSIRefDocModel.class));
        return csiRefDocModels;
    }
}
