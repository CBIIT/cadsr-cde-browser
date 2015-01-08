package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ContextModel;
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
@Repository
public class TestDao // extends AbstractDAOOperations
{
    private String version;
    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;


    public void setDataSource( DataSource dataSource )
    {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);

    }

    public TestDao()
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
                "select * from SBR.CONTEXTS",
                new RowMapper<ContextModel>() {
                    public ContextModel mapRow(ResultSet rs, int rowNum) throws SQLException
                    {
                        ContextModel context = new ContextModel();
                        context.setConteIdseq( rs.getString("CONTE_IDSEQ" ));
                        context.setName( rs.getString( "NAME" ) );
                        context.setDescription( rs.getString( "DESCRIPTION" ) );
                        context.setDateCreated( rs.getTimestamp( "DATE_CREATED" ));
                        context.setCreatedBy( rs.getString("CREATED_BY" ) );
                        context.setDateModified( rs.getTimestamp( "DATE_MODIFIED" ));
                        context.setModifiedBy( rs.getString("MODIFIED_BY" ) );
                        return context;
                    }
                });

        return allContext;
    }

}
