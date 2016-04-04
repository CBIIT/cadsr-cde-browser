package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.PermissibleValuesModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

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
    /*
        This is the best I have so far -
        does not always have match for Concept Code, so that part is commented out,
        if it where left in we would not get back the records which we failed to get Concept Code
    */
        String sql =
                "SELECT DISTINCT  sbr.permissible_values.*, " +
                        "    sbr.value_meanings.description AS VM_DESCRIPTION," +
                        "    sbr.value_meanings.vm_id, " +
                        "    sbr.value_meanings.version AS vm_version, " +
                        "    sbr.vd_pvs.con_idseq " +
                        "FROM sbr.permissible_values, " +
                        "    sbr.vd_pvs," +
                        "    sbr.value_meanings " +
                        "WHERE " +
                        "   sbr.permissible_values.pv_idseq = sbr.vd_pvs.pv_idseq " +
                        "AND sbr.vd_pvs.vd_idseq = ? " +
                        "AND sbr.value_meanings.vm_idseq = sbr.permissible_values.vm_idseq ORDER BY UPPER(sbr.permissible_values.value)";


        String sqlOLD =
                "SELECT DISTINCT " +
                        " sbr.permissible_values.*," +
                        // " sbrext.up_semantic_metadata_mvw.concept_code," +

                        " sbr.value_meanings.description AS VM_DESCRIPTION," +
                        " sbr.value_meanings.vm_id," +
                        " sbr.value_meanings.version AS vm_version," +
                        " sbr.vd_pvs.con_idseq " +
                        "FROM" +
                        " sbr.permissible_values," +
                        " sbr.vd_pvs," +
                        " sbr.value_meanings," +
                        " sbrext.up_semantic_metadata_mvw " +
                        "WHERE" +
                        " sbr.permissible_values.pv_idseq = sbr.vd_pvs.pv_idseq " +
                        " AND sbr.vd_pvs.vd_idseq = ? " +
                        " AND sbr.value_meanings.vm_idseq = sbr.permissible_values.vm_idseq " +
                        // " and sbrext.up_semantic_metadata_mvw.public_id = sbr.value_meanings.vm_id " +
                        "ORDER BY" +
                        " UPPER(sbr.permissible_values.value)";


        logger.debug( ">>>>>" + sql.replace( "?", vdIdseq ) + " <<<<<<<" );

        return getAll( sql, vdIdseq, PermissibleValuesModel.class );
    }

}
