package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.CSRefDocModel;
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
public class CSRefDocDAOImpl extends AbstractDAOOperations implements CSRefDocDAO
{
    private Logger logger = LogManager.getLogger(CSRefDocDAOImpl.class.getName());

    private JdbcTemplate jdbcTemplate;

    @Autowired
    CSRefDocDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }



    @Override
    public List<CSRefDocModel> getCSRefDocsByDEIdseq(String deIdseq) {
        /*
    public String csLongName;
    public Float csVersion;
    public String documentName;
    public String documentType;
    public String DocumentText;
    public String url;
    public String attachments;*/
// todo attachements!
        String sql = "SELECT cs.long_name cs_long_name, cs.version cs_version, rd.name document_name, rd.dctl_name document_type, rd.doc_text document_text, rd.url " +
                "FROM sbr.reference_documents rd, sbr.classification_schemes cs " +
                "WHERE rd.ac_idseq in ( " +
                "SELECT cs_csi.cs_idseq FROM sbr.ac_csi, sbr.cs_csi " +
                "WHERE ac_csi.ac_idseq = ? " +
                "AND ac_csi.cs_csi_idseq = cs_csi.cs_csi_idseq " +
                ") " +
                "AND cs.cs_idseq = rd.ac_idseq";
        List<CSRefDocModel> csRefDocModels = jdbcTemplate.query(sql, new Object[]{deIdseq}, new BeanPropertyRowMapper(CSRefDocModel.class));
        return csRefDocModels;
    }
}
