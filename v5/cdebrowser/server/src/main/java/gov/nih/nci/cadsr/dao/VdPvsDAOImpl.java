package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.VdPvsModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by lernermh on 6/3/15.
 */
public class VdPvsDAOImpl extends AbstractDAOOperations implements VdPvsDAO
{
    private Logger logger = LogManager.getLogger( VdPvsDAOImpl.class.getName() );
    private JdbcTemplate jdbcTemplate;

    @Autowired
    VdPvsDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public List<VdPvsModel> getVdPvs( String vdIdseq )
    {
        String sql = " SELECT DISTINCT * FROM sbr.vd_pvs WHERE vd_idseq = ?";
        return getAll( sql, vdIdseq, VdPvsModel.class );
    }
}
