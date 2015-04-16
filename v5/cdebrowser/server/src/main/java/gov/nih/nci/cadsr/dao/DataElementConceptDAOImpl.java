package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.DataElementConceptModel;
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
 * Created by lavezzojl on 4/15/15.
 */
public class DataElementConceptDAOImpl extends AbstractDAOOperations implements DataElementConceptDAO {

    private Logger logger = LogManager.getLogger(DataElementConceptDAOImpl.class.getName());

    private JdbcTemplate jdbcTemplate;


    @Autowired
    DataElementConceptDAOImpl( DataSource dataSource ) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public DataElementConceptModel getDecByDecIdseq(String decIdseq) {
        String sql = "SELECT * FROM DATA_ELEMENT_CONCEPTS WHERE DEC_IDSEQ = ?";
        DataElementConceptModel dataElementConceptModel = jdbcTemplate.queryForObject(sql, new Object[] { decIdseq }, new DataElementConceptMapper());
        return dataElementConceptModel;
    }




    public final class DataElementConceptMapper extends BeanPropertyRowMapper<DataElementConceptModel> {

        public DataElementConceptModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            DataElementConceptModel dataElementConceptModel = new DataElementConceptModel();
            /* TODO: need to map these members:
            private PropertyModel property;
            private ObjectClassModel objectClassModel;
            */
            return dataElementConceptModel;
        }
    }

}
