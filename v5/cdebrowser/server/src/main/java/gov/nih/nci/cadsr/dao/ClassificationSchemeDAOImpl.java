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
     * @return
     */
    @Override
    public List<ClassificationSchemeModel> getAllClassificationSchemes()
    {
        List<ClassificationSchemeModel> results;
        sql = "select * from sbr.classification_Schemes_view WHERE ASL_NAME='RELEASED' order by lower(long_name)";
        results = getAll( sql, ClassificationSchemeModel.class );
        return results;
    }

    /**
     * @param conteId
     * @return
     */
    @Override
    public List<ClassificationSchemeModel> getClassificationSchemes( String conteId )
    {
        List<ClassificationSchemeModel> results;

        sql = "select * from SBR.CLASSIFICATION_SCHEMES_VIEW where CONTE_IDSEQ=? and ASL_NAME='RELEASED' order by lower(LONG_NAME)";

        //logger.debug( "getClassificationSchemes" );
        logger.debug( ">>>>>>> " + sql.replace( "?", conteId ) );
        results = getAll( sql, conteId, ClassificationSchemeModel.class );
        //logger.debug( sql.replace( "?", conteId ) + " <<<<<<<" );
        //logger.debug( "Done getClassificationSchemes\n" );

        return results;
    }


    public boolean haveClassificationSchemes( String conteId )
    {
        Integer results;

        sql = "select count(CS_IDSEQ) from SBR.CLASSIFICATION_SCHEMES_VIEW where CONTE_IDSEQ=? and ASL_NAME='RELEASED'";
        results = getOneInt( sql, conteId );
        return (  results > 0 );
    }


    /**
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
