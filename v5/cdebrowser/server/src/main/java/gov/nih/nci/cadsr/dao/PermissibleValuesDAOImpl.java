package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.PermissibleValuesModel;
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
public class PermissibleValuesDAOImpl extends AbstractDAOOperations implements PermissibleValuesDAO
{
    private Logger logger = LogManager.getLogger( PermissibleValuesDAOImpl.class.getName() );
    private JdbcTemplate jdbcTemplate;

    @Autowired
    PermissibleValuesDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }


    @Override
    public List<PermissibleValuesModel> getPermissibleValuesByPvIdseq( String pvIdseq )
    {
        String sql = " select distinct * from SBR.PERMISSIBLE_VALUES where PV_IDSEQ = ?";
        return getAll( sql, pvIdseq, PermissibleValuesModel.class );
    }

    @Override
    public List<PermissibleValuesModel> getPermissibleValuesByVdIdseq( String vdIdseq )
    {
        //String sql = "select distinct SBR.PERMISSIBLE_VALUES.* from SBR.PERMISSIBLE_VALUES , SBR.VD_PVS where SBR.PERMISSIBLE_VALUES.pv_idseq = SBR.VD_PVS.pv_idseq and SBR.VD_PVS.vd_idseq = ?  order by upper(SBR.PERMISSIBLE_VALUES.value)";

        String sql =
                "select distinct " +
                        " SBR.PERMISSIBLE_VALUES.*," +
                       // " SBREXT.UP_SEMANTIC_METADATA_MVW.CONCEPT_CODE," +
                        " SBR.VALUE_MEANINGS.description as vm_description," +
                        " SBR.VALUE_MEANINGS.vm_id," +
                        " SBR.VALUE_MEANINGS.version as vm_version," +
                        " SBR.VD_PVS.con_idseq " +
                "from" +
                        " SBR.PERMISSIBLE_VALUES," +
                        " SBR.VD_PVS," +
                        " SBR.VALUE_MEANINGS," +
                        " SBREXT.UP_SEMANTIC_METADATA_MVW " +
                "where" +
                        " SBR.PERMISSIBLE_VALUES.pv_idseq = SBR.VD_PVS.pv_idseq " +
                        " and SBR.VD_PVS.vd_idseq = ? " +
                        " and SBR.VALUE_MEANINGS.vm_idseq = SBR.PERMISSIBLE_VALUES.vm_idseq " +
                     //   " and SBREXT.UP_SEMANTIC_METADATA_MVW.public_id = SBR.VALUE_MEANINGS.vm_id " +
                "order by" +
                        " upper(SBR.PERMISSIBLE_VALUES.value)";
/*
       String sql = "select distinct SBR.PERMISSIBLE_VALUES.*, SBREXT.UP_SEMANTIC_METADATA_MVW.CONCEPT_CODE, SBR.VALUE_MEANINGS.description as vm_description, SBR.VALUE_MEANINGS.vm_id , SBR.VALUE_MEANINGS.version as vm_version, SBR.VD_PVS.con_idseq " +
                "from SBR.PERMISSIBLE_VALUES , SBR.VD_PVS, SBR.VALUE_MEANINGS, SBREXT.UP_SEMANTIC_METADATA_MVW " +
                "where SBR.PERMISSIBLE_VALUES.pv_idseq = SBR.VD_PVS.pv_idseq " +
                "and SBR.VD_PVS.vd_idseq = ? " +
                "and SBR.VALUE_MEANINGS.vm_idseq = SBR.PERMISSIBLE_VALUES.vm_idseq " +
                "and SBREXT.UP_SEMANTIC_METADATA_MVW.public_id = SBR.VALUE_MEANINGS.vm_id " +
                "order by upper(SBR.PERMISSIBLE_VALUES.value)";
*/

        logger.debug( sql.replace( "?", vdIdseq ) + " <<<<<<<" );

        return getAll( sql, vdIdseq, PermissibleValuesModel.class );
    }

}
