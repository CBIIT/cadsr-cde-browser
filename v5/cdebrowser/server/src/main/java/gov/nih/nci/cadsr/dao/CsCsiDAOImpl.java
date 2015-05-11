/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ContextModel;
import gov.nih.nci.cadsr.dao.model.CsCsiModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * DAO for the SBREXT.BR_CS_CSI_HIER_VIEW_EXT which is actually a real view and not just a copy of a table
 */
public class CsCsiDAOImpl extends AbstractDAOOperations implements CsCsiDAO
{
    private Logger logger = LogManager.getLogger( CsCsiDAOImpl.class.getName() );

    private JdbcTemplate jdbcTemplate;

    public CsCsiDAOImpl()
    {
    }

    @Autowired
    CsCsiDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }


    @Override
    public List<CsCsiModel> getCsCsisByParentCsCsi( String parentCsCsi )
    {
        String sql;
        //sql = "select * from SBR.CSCSI_VIEW WHERE CS_IDSEQ=? order by lower(CSI_DESCRIPTION)";
        sql = "select CS_IDSEQ, CS_PREFFERED_NAME, CS_LONG_NAME, CSTL_NAME, "
                + "CS_PREFFRED_DEFINITION, "
                + "CSI_IDSEQ, CSI_NAME, CSITL_NAME, CSI_DESCRIPTION, "
                + "CS_CSI_IDSEQ, CSI_LEVEL, PARENT_CSI_IDSEQ, CS_CONTE_IDSEQ "
                + " from SBREXT.BR_CS_CSI_HIER_VIEW_EXT "
                + " where CS_ASL_NAME = 'RELEASED' "
                + " and CSTL_NAME != 'Publishing' "
                + " and PARENT_CSI_IDSEQ = ?"
                + " order by CSI_LEVEL, upper(csi_name)";
        //logger.debug( "("+ parentCsCsi +")  " + sql );

        return getAll( sql, parentCsCsi, CsCsiModel.class );
    }

    @Override
    public List<CsCsiModel> getCsCsisById( String csId )
    {
        List<CsCsiModel> result;
        String sql;

        //sql = "select * from SBR.CSCSI_VIEW WHERE CS_IDSEQ=? order by lower(CSI_DESCRIPTION)";
        sql = "select CS_IDSEQ, CS_PREFFERED_NAME, CS_LONG_NAME, CSTL_NAME, "
                + "CS_PREFFRED_DEFINITION, "
                + "CSI_IDSEQ, CSI_NAME, CSITL_NAME, CSI_DESCRIPTION, "
                + "CS_CSI_IDSEQ, CSI_LEVEL, PARENT_CSI_IDSEQ, CS_CONTE_IDSEQ "
                + " FROM SBREXT.BR_CS_CSI_HIER_VIEW_EXT "
                + " where CS_ASL_NAME = 'RELEASED' "
                + " and CSTL_NAME != 'Publishing' "
                + " and CS_IDSEQ = ?"
                + " order by CSI_LEVEL, upper(CSI_NAME)";
        //logger.debug( "getCsCsisById" );
        //logger.debug( ">>>>>>> " + sql.replace( "?", csId ) );
        result = getAll( sql, csId, CsCsiModel.class );
        //logger.debug( sql.replace( "?", csId ) + " <<<<<<<" );
        //logger.debug( "Done getCsCsisById\n" );

        return result;
    }

    public List<CsCsiModel> getCsCsisByConteId( String conteId )
    {
        List<CsCsiModel> result;
        String sql;

        //sql = "select * from SBR.CSCSI_VIEW WHERE CS_IDSEQ=? order by lower(CSI_DESCRIPTION)";
        sql = "select CS_IDSEQ, CS_PREFFERED_NAME, CS_LONG_NAME, CSTL_NAME, "
                + "CS_PREFFRED_DEFINITION, "
                + "CSI_IDSEQ, CSI_NAME, CSITL_NAME, CSI_DESCRIPTION, "
                + "CS_CSI_IDSEQ, CSI_LEVEL, PARENT_CSI_IDSEQ, CS_CONTE_IDSEQ "
                + " FROM SBREXT.BR_CS_CSI_HIER_VIEW_EXT "
                + " where CS_ASL_NAME = 'RELEASED' "
                + " and CSTL_NAME != 'Publishing' "
                + " and CS_CONTE_IDSEQ = ?"
                + " order by CSI_LEVEL, upper(CSI_NAME)";
        //logger.debug( "getCsCsisById" );
        //logger.debug( ">>>>>>> " + sql.replace( "?", csId ) );
        result = getAll(sql, conteId, CsCsiModel.class);
        //logger.debug( sql.replace( "?", csId ) + " <<<<<<<" );
        //logger.debug( "Done getCsCsisById\n" );

        return result;
    }

    @Override
    public List<CsCsiModel> getAllCsCsis()
    {
        List<CsCsiModel> result;
        String sql;

        //sql = "select * from SBR.CSCSI_VIEW WHERE CS_IDSEQ=? order by lower(CSI_DESCRIPTION)";
        sql = "select CS_IDSEQ, CS_PREFFERED_NAME, CS_LONG_NAME, CSTL_NAME, "
                + "CS_PREFFRED_DEFINITION, "
                + "CSI_IDSEQ, CSI_NAME, CSITL_NAME, CSI_DESCRIPTION, "
                + "CS_CSI_IDSEQ, CSI_LEVEL, PARENT_CSI_IDSEQ, CS_CONTE_IDSEQ "
                + " from SBREXT.BR_CS_CSI_HIER_VIEW_EXT "
                + " where CS_ASL_NAME = 'RELEASED' "
                + " and CSTL_NAME != 'Publishing' "
                + " order by CSI_LEVEL, upper(csi_name)";
        logger.debug( "getAllCsCsis" );
        //logger.debug( ">>>>>>> " + sql);
        result = getAll( sql, CsCsiModel.class );
        //logger.debug( sql + " <<<<<<<" );
        logger.debug( "Done getAllCsCsis\n" );

        return result;
    }

    /**
     * This method takes the Data Element's idseq to find the CS and CSI data associated
     * with the DE's Definitions and Designations (alt names)
     * @param deIdseq
     * @return
     */
    @Override
    public List<CsCsiModel> getAllCsCsisByDataElement(String deIdseq) {
        List<CsCsiModel> csCsiModels;

        String definitionCsCsiSql = "SELECT cs_csi.cs_idseq, cs_csi.cs_preffered_name AS cs_pref_name, cs_csi.cs_long_name, cs_csi.cstl_name, " +
                "cs_csi.cs_preffred_definition, cs_csi.csi_idseq, cs_csi.csi_name, cs_csi.csitl_name, cs_csi.csi_description, " +
                "cs_csi.cs_csi_idseq, csi_level, cs_csi.parent_csi_idseq, cs_csi.cs_conte_idseq " +
                "FROM sbrext.br_cs_csi_hier_view_ext cs_csi, sbrext.ac_att_cscsi_ext att, sbr.definitions def " +
                "WHERE def.ac_idseq = ? " +
                "AND att.att_idseq = def.defin_idseq " +
                "AND cs_csi.cs_csi_idseq = att.cs_csi_idseq";
        //csCsiModels = jdbcTemplate.queryForList(definitionCsCsiSql, CsCsiModel.class, deIdseq);
        csCsiModels = jdbcTemplate.query(definitionCsCsiSql, new Object[]{deIdseq}, new BeanPropertyRowMapper(CsCsiModel.class));


        String designationCsCsiSql = "SELECT cs_csi.cs_idseq, cs_csi.cs_preffered_name AS cs_pref_name, cs_csi.cs_long_name, cs_csi.cstl_name, " +
                "cs_csi.cs_preffred_definition, cs_csi.csi_idseq, cs_csi.csi_name, cs_csi.csitl_name, cs_csi.csi_description, " +
                "cs_csi.cs_csi_idseq, csi_level, cs_csi.parent_csi_idseq, cs_csi.cs_conte_idseq " +
                "FROM sbrext.br_cs_csi_hier_view_ext cs_csi, sbrext.ac_att_cscsi_ext att, sbr.designations desig" +
                "WHERE desig.ac_idseq = ?" +
                "AND att.att_idseq = desig.desig_idseq" +
                "AND cs_csi.cs_csi_idseq = att.cs_csi_idseq";
//        csCsiModels.addAll(jdbcTemplate.queryForList(definitionCsCsiSql, CsCsiModel.class, deIdseq));
        csCsiModels.addAll(jdbcTemplate.query(definitionCsCsiSql, new Object[]{deIdseq}, new BeanPropertyRowMapper(CsCsiModel.class)));

        return csCsiModels;
    }

}
