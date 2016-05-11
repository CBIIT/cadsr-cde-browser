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
    private static final Logger logger = LogManager.getLogger( PermissibleValuesDAOImpl.class.getName() );
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
    	//conceptCode comes from sbrext.CON_DERIVATION_RULES_EXT. See comment CDEBROWSER-460 explanations by Rui
        String sql = buildPermissibleValuesSql();
        
        //This SQL is not used, and is kept here for comparison purposes
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


        logger.debug( ">>>>>" + sql.replace( "?", ((vdIdseq != null) ? vdIdseq : "null")) + " <<<<<<<" );

        return getAll( sql, vdIdseq, PermissibleValuesModel.class );
    }
    protected String buildPermissibleValuesSql() {
        String sql =
                "SELECT DISTINCT  sbr.permissible_values.pv_idseq, " +
                        "sbr.permissible_values.value, " +
                        "sbr.permissible_values.short_meaning, " +
                        "sbr.permissible_values.meaning_description, " + 
                        "sbr.permissible_values.high_value_num, " +
                        "sbr.permissible_values.low_value_num, " +
                        "sbr.permissible_values.vm_idseq, " + 
                        "sbr.vd_pvs.begin_date begin_date, " +
                        "sbr.vd_pvs.end_date end_date, " +
                        "sbr.value_meanings.description AS VM_DESCRIPTION, " +
                        "sbr.value_meanings.vm_id, " +
                        "sbr.value_meanings.version AS vm_version, " +
                        "sbr.vd_pvs.con_idseq, " +
                        "sbr.vd_pvs.created_by, " +
                        "sbr.vd_pvs.date_created, " +
                        "sbr.vd_pvs.modified_by, " +
                        "sbr.vd_pvs.date_modified, " +
                        "sbrext.CON_DERIVATION_RULES_EXT.name as CONCEPT_CODE " + 
                        "FROM sbr.permissible_values, " +
                        "sbr.vd_pvs," +
                        "sbr.value_meanings, " +
                        "sbrext.CON_DERIVATION_RULES_EXT " +
                        "WHERE " +
                        "sbr.permissible_values.pv_idseq = sbr.vd_pvs.pv_idseq " +
                        "AND sbr.vd_pvs.vd_idseq = ? " +
                        "AND sbr.value_meanings.vm_idseq = sbr.permissible_values.vm_idseq " +
                        "AND sbr.value_meanings.condr_idseq = sbrext.CON_DERIVATION_RULES_EXT.CONDR_IDSEQ(+) "
                        + "ORDER BY UPPER(sbr.permissible_values.value)";
        return sql;
    }
}
