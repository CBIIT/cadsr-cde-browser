package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.CsCsiModel;
import gov.nih.nci.cadsr.dao.model.DEOtherVersionsModel;
import gov.nih.nci.cadsr.dao.model.DataElementModel;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lavezzojl on 5/22/15.
 */
public class DEOtherVersionsDAOImpl extends AbstractDAOOperations implements DEOtherVersionsDAO {
    private Logger logger = LogManager.getLogger(DEOtherVersionsDAOImpl.class.getName());

    private JdbcTemplate jdbcTemplate;

    private CsCsiDAO csCsiDAO;

    @Autowired
    DEOtherVersionsDAOImpl(DataSource dataSource) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }

    public CsCsiDAO getCsCsiDAO() {
        return csCsiDAO;
    }

    public void setCsCsiDAO(CsCsiDAO csCsiDAO) {
        this.csCsiDAO = csCsiDAO;
    }

    @Override
    public List<DEOtherVersionsModel> getOtherVersions(Integer publicId, String deIdseq) {
        String sql = "SELECT de.version, de.long_name, de.asl_name workflow_status, reg.registration_status, c.name context_name, de.de_idseq " +
                "FROM sbr.contexts c, sbr.data_elements de " +
                "LEFT JOIN sbr.ac_registrations reg ON reg.ac_idseq = de.de_idseq " +
                "WHERE de.cde_id = ? " +
                "AND de.de_idseq != ? " +
                "AND de.conte_idseq = c.conte_idseq ";
        List<DEOtherVersionsModel> deOtherVersionsModels = jdbcTemplate.query(sql, new Object[]{publicId, deIdseq}, new DEOtherVersionsMapper(DEOtherVersionsModel.class));
        return deOtherVersionsModels;
    }


    public final class DEOtherVersionsMapper extends BeanPropertyRowMapper<DEOtherVersionsModel> {
        private Logger logger = LogManager.getLogger(DEOtherVersionsMapper.class.getName());

        public DEOtherVersionsMapper(Class<DEOtherVersionsModel> mappedClass) {
            super(mappedClass);
        }

        public DEOtherVersionsModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            DEOtherVersionsModel deOtherVersionsModel = super.mapRow(rs, rowNum);

            try {
                List<CsCsiModel> csCsiModels = getCsCsiDAO().getCsCsisByAcIdseq(rs.getString("de_idseq"));
                if (csCsiModels != null && csCsiModels.size() > 0) {
                    deOtherVersionsModel.setCsCsiModelList(csCsiModels);
                } else {
                    deOtherVersionsModel.setCsCsiModelList(new ArrayList<CsCsiModel>());
                }
            } catch (EmptyResultDataAccessException ex) {
                deOtherVersionsModel.setCsCsiModelList(new ArrayList<CsCsiModel>());
                logger.warn("No CsCsiModels found for Other Version Data Element with idseq: " + rs.getString("de_idseq"));
            }
            return deOtherVersionsModel;
        }
    }

}
