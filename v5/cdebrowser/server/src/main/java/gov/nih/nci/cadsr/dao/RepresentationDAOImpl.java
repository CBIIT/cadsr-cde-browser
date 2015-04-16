package gov.nih.nci.cadsr.dao;

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

    private Logger logger = LogManager.getLogger(DataElementConceptDAOImpl.class.getName());

    private JdbcTemplate jdbcTemplate;

    private PropertyDAO propertyDAO;
    private ObjectClassDAO objectClassDAO;


    @Autowired
    RepresentationDAOImpl( DataSource dataSource ) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public RepresentationModel getRepresentationByIdseq(String representationIdseq) {
        String sql = "SELECT * FROM REPRESENTATIONS_EXT WHERE REP_IDSEQ = ?";
        RepresentationModel representationModel = jdbcTemplate.queryForObject(sql, new Object[] { representationIdseq }, new RepresentationMapper());
        return representationModel;
    }




    public final class RepresentationMapper extends BeanPropertyRowMapper<RepresentationModel> {

        public RepresentationModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            RepresentationModel representationModel = new RepresentationModel();
            return representationModel;
        }
    }
}
