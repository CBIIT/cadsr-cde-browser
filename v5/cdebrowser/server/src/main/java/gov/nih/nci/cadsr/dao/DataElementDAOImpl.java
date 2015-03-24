package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Created with IntelliJ IDEA.
 * User: lerner
 * Date: 3/23/15
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataElementDAOImpl  extends AbstractDAOOperations implements DataElementDAO
{
    private JdbcTemplate jdbcTemplate;


    @Autowired
    DataElementDAOImpl( DataSource dataSource ) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }


}
