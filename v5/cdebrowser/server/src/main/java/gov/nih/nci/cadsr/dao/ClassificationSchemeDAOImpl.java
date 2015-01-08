/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.*;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class ClassificationSchemeDAOImpl extends AbstractDAOOperations implements ClassificationSchemeDAO
{
    private Logger logger = LogManager.getLogger( ClassificationSchemeDAOImpl.class.getName() );

    private JdbcTemplate jdbcTemplate;
    private String sql;


    @Autowired
    ClassificationSchemeDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }

    public ClassificationSchemeDAOImpl()
    {
    }

    /**
     *
     * @param csId
     * @return
     */
    @Override
    public List<ClassificationSchemeModel> getChildrenClassificationSchemesByCsId( String csId )
    {
        sql = "SELECT distinct CS_IDSEQ , preferred_name, long_name, " +
                "preferred_definition, cstl_name,asl_name,conte_idseq " +
                " FROM sbr.classification_Schemes_view, sbr.cs_recs_view c  " +
                " WHERE  cs_idseq = c_cs_idseq " +
                " AND rl_name = 'HAS_A' " +
                " AND p_cs_idseq = ?" +
                " order by long_name  ";

        logger.debug( "SELECT distinct CS_IDSEQ , preferred_name, long_name, " +
                "preferred_definition, cstl_name,asl_name,conte_idseq " +
                " FROM sbr.classification_Schemes_view, sbr.cs_recs_view c  " +
                " WHERE  cs_idseq = c_cs_idseq " +
                " AND rl_name = 'HAS_A' " +
                " AND p_cs_idseq = '" + csId +
                "' order by long_name  " );


        //sql = "select * from SBREXT.CABIO_CLASS_SCHEMES_VIEW WHERE CONTE_IDSEQ=? order by PREFERRED_DEFINITION";
        //logger.debug( "SQL:  " + sql);
        return getAll( sql, csId, ClassificationSchemeModel.class );
    }


    /**
     *
     * @return
     */
    @Override
    public List<ClassificationSchemeModel> getAllClassificationSchemes()
    {
        List<ClassificationSchemeModel> results;

        sql = "select * from sbr.classification_Schemes_view WHERE ASL_NAME='RELEASED' order by UPPER(long_name)";

        logger.debug( "getAllClassificationSchemes" );
        //logger.debug( ">>>>>>> " + sql );
        results = getAll( sql, ClassificationSchemeModel.class );
        //logger.debug( sql + " <<<<<<<" );
        logger.debug( "Done getAllClassificationSchemes\n" );

        return results;
    }

    /**
     *
     * @param conteId
     * @return
     */
    @Override
    public List<ClassificationSchemeModel> getClassificationSchemes( String conteId )
    {
        List<ClassificationSchemeModel> results;
/*


        sql= "SELECT distinct CS_IDSEQ , preferred_name, long_name, " +
                "preferred_definition, cstl_name,asl_name,conte_idseq " +
                " FROM SBREXT.CABIO_CLASS_SCHEMES_VIEW" +
                " WHERE CONTE_IDSEQ = ? " +
                " and ASL_NAME = 'RELEASED' " +
                " and CSTL_NAME != 'Publishing' " +
                "  order by UPPER(long_name)  ";

        logger.info( "SELECT distinct CS_IDSEQ , preferred_name, long_name, " +
                "preferred_definition, cstl_name,asl_name,conte_idseq " +
                " FROM sbr.classification_Schemes_view" +
                " WHERE CONTE_IDSEQ = '" + conteId +
                "' and ASL_NAME = 'RELEASED' " +
                " and CSTL_NAME != 'Publishing' " +
                "  order by UPPER(long_name)  " );

*/


/*


        sql = "select cs_idseq, cs_preffered_name, cs_long_name, cstl_name, "
                + "CS_PREFFRED_DEFINITION, "
                + "csi_idseq, csi_name, csitl_name, csi_description, "
                + "cs_csi_idseq, csi_level, parent_csi_idseq, cs_conte_idseq "
                + " from SBREXT.BR_CS_CSI_HIER_VIEW_EXT "
                + " where CS_ASL_NAME = 'RELEASED' "
                + " and CSTL_NAME != 'Publishing' "
                + " and PARENT_CSI_IDSEQ IS NULL "
                + " and cs_idseq = ?"
                + " order by CSI_LEVEL, upper(csi_name)";

        logger.debug( "select cs_idseq, cs_preffered_name, cs_long_name, cstl_name, "
                + "CS_PREFFRED_DEFINITION, "
                + "csi_idseq, csi_name, csitl_name, csi_description, "
                + "cs_csi_idseq, csi_level, parent_csi_idseq, cs_conte_idseq "
                + " from SBREXT.BR_CS_CSI_HIER_VIEW_EXT "
                + " where CS_ASL_NAME = 'RELEASED' "
                + " and CSTL_NAME != 'Publishing' "
                + " and PARENT_CSI_IDSEQ IS NULL "
                + " and cs_idseq = '" + conteId +"' "
                + " order by CSI_LEVEL, upper(csi_name)" );
*/


/*

        sql= "SELECT distinct CS_IDSEQ , preferred_name, long_name, " +
                "preferred_definition, cstl_name,asl_name,conte_idseq " +
                " FROM sbr.classification_Schemes_view" +
                " WHERE CONTE_IDSEQ = ? " +
                " and ASL_NAME = 'RELEASED' " +
                " and CSTL_NAME != 'Publishing' " +
                "  order by UPPER(long_name)  ";

*/
        //sql = "select * from SBREXT.CABIO_CLASS_SCHEMES_VIEW WHERE CONTE_IDSEQ=? AND ASL_NAME='RELEASED' order by UPPER(long_name)";
        //logger.debug( ">>>>>>> select * from SBREXT.CABIO_CLASS_SCHEMES_VIEW WHERE CONTE_IDSEQ='" + conteId + "'order by UPPER(long_name)\n" );

        sql = "select * from SBR.CLASSIFICATION_SCHEMES_VIEW where CONTE_IDSEQ=? and ASL_NAME='RELEASED' order by UPPER(LONG_NAME)";

        logger.debug( "getClassificationSchemes" );
        //logger.debug( ">>>>>>> " + sql.replace( "?", conteId ) );
        results = getAll( sql, conteId, ClassificationSchemeModel.class );
        //logger.debug( sql.replace( "?", conteId ) + " <<<<<<<" );
        logger.debug( "Done getClassificationSchemes\n" );

        return results;
    }


    /**
     *
     * @param contextId
     * @return
     */
    @Override
    public ClassificationSchemeModel getClassificationSchemeById( String contextId )
    {

        sql = "select * from SBREXT.CABIO_CLASS_SCHEMES_VIEW WHERE CONTE_IDSEQ=?";

        sql = "SELECT distinct CS_IDSEQ , preferred_name, long_name, " +
                "preferred_definition, cstl_name,asl_name,conte_idseq " +
                " FROM sbr.classification_Schemes_view" +
                " WHERE CONTE_IDSEQ = ? " +
                " and ASL_NAME = 'RELEASED' " +
                " and CSTL_NAME != 'Publishing' " +
                "  order by UPPER(long_name)  ";

        //logger.warn( "SQL ["+ contextId +"]: " + sql );
        ClassificationSchemeModel results = query( sql, contextId, ClassificationSchemeModel.class );

        return results;
    }

}
/*
"SELECT distinct CS_IDSEQ , preferred_name, long_name, " +
                         "preferred_definition, cstl_name,asl_name,conte_idseq " +
                         " FROM sbr.classification_Schemes_view" +
                         " WHERE CONTE_IDSEQ = ? " +
                         " and ASL_NAME = 'RELEASED' " +
                         " and CSTL_NAME != 'Publishing' " +
                         "  order by UPPER(long_name)  "

*/