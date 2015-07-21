package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ProgramAreaModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class ProgramAreaDAOImpl extends AbstractDAOOperations implements ProgramAreaDAO
{
    private Logger logger = LogManager.getLogger( ProtocolDAOImpl.class.getName() );
    private JdbcTemplate jdbcTemplate;


    @Autowired
    ProgramAreaDAOImpl( DataSource dataSource ) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }


    @Override
    public List<ProgramAreaModel> getAllProgramAreas()
    {
        List<ProgramAreaModel> result;
        String sql = " select * from PROGRAM_AREAS_LOV_VIEW";
        result = getAll( sql , ProgramAreaModel.class );
        //logger.debug( "Done getAllProgramAreas" );
        return result;
    }
}
