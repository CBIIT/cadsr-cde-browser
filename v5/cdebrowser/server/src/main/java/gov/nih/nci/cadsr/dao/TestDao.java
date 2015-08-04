package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ContextModel;
import gov.nih.nci.cadsr.dao.model.TestModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lerner
 * Date: 12/11/14
 * Time: 1:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TestDao
{
    void getRoewsByTable( int rowCount, final String tableName ,final List<String> rowList );
}
