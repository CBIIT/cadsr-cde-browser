package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ContextModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lerner
 * Date: 12/13/14
 * Time: 12:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestDao2 // extends AbstractDAOOperations
{
    private String version;
    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;


    public void setDataSource( DataSource dataSource )
    {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);

    }

    public TestDao2()
    {
        // setVersion("Version set in constructor");
    }






    public String getVersion()
    {
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
    }

    public String getResults()
    {
        String results = " Results";

        if( this.version == null )
        {
            return "version is null";
        }

        if( this.jdbcTemplate == null )
        {
            return "jdbcTemplate is null";
        }

        int rowCount = this.jdbcTemplate.queryForObject("select count(*) from SBR.CONTEXTS", Integer.class);
        results = "Got " + rowCount + " Contexts";
        return results;

    }
    public List<ContextModel> getAllContext()
    {

        if( this.jdbcTemplate == null )
        {
            return null;
        }
        List<ContextModel> allContext = this.jdbcTemplate.query(
                "select NAME, DESCRIPTION from SBR.CONTEXTS",
                new RowMapper<ContextModel>() {
                    public ContextModel mapRow(ResultSet rs, int rowNum) throws SQLException
                    {
                        ContextModel context = new ContextModel();
                        context.setName( rs.getString( "NAME" ) );
                        context.setDescription( rs.getString( "DESCRIPTION" ) );
                        return context;
                    }
                });

        return allContext;
    }

}
