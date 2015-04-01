package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.DataElementModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;


public class DataElementDAOImpl  extends AbstractDAOOperations implements DataElementDAO
{
    private Logger logger = LogManager.getLogger( DataElementDAOImpl.class.getName() );

    private JdbcTemplate jdbcTemplate;

    private String DataElementSql;


    @Autowired
    DataElementDAOImpl( DataSource dataSource ) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public List<DataElementModel> getCdeByContextId()
    {
        List<DataElementModel>  results;

        logger.debug( "basicSearch");
        //logger.debug( ">>>>>>> "+ sql );
        results = getAll( DataElementSql, DataElementModel.class );
        //logger.debug( sql + " <<<<<<<" );
        logger.debug( "Done basicSearch");

        return results;
    }

    public String getDataElementSql()
    {
        return DataElementSql;
    }

    @Override
    public void setDataElementSql( String dataElementSql )
    {
        this.DataElementSql = dataElementSql;
    }
}
