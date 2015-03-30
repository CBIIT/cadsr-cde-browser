package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.BasicSearchModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;


public class BasicSearchDAOImpl extends AbstractDAOOperations implements BasicSearchDAO
{
    private Logger logger = LogManager.getLogger( BasicSearchDAOImpl.class.getName() );

    private JdbcTemplate jdbcTemplate;

    private String basicSearchSql;

    public BasicSearchDAOImpl()
    {
    }

    @Autowired
    BasicSearchDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }

    public List<BasicSearchModel> getAllContexts()
    {
        List<BasicSearchModel>  results;

        logger.debug( "basicSearch");
        //logger.debug( ">>>>>>> "+ basicSearchSql );
        results = getAll( basicSearchSql, BasicSearchModel.class );
        //logger.debug( sql + " <<<<<<<" );
        logger.debug( "Done basicSearch\n");

        return results;
    }


    public String getBasicSearchSql()
    {
        return basicSearchSql;
    }

    public void setBasicSearchSql( String basicSearchSql )
    {
        this.basicSearchSql = basicSearchSql;
    }

}
