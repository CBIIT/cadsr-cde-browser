package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

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
        //  todo: I can't see there's any reason to use the view instead of SBR.CLASSIFICATION_SCHEMES do you?
        sql = "SELECT distinct CS_IDSEQ , preferred_name, long_name, " +
                "preferred_definition, cstl_name,asl_name,conte_idseq " +
                " FROM sbr.classification_Schemes_view, sbr.cs_recs_view c  " +
                " WHERE  cs_idseq = c_cs_idseq " +
                " AND rl_name = 'HAS_A' " +
                " AND p_cs_idseq = ?" +
                " ORDER BY long_name  ";

        logger.debug( "getChildrenClassificationSchemesByCsId( String csId ) executing query " + sql + " (p_cs_idseq is " + csId + ")" );


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
        sql = "SELECT * FROM sbr.classification_schemes WHERE asl_name='RELEASED' ORDER BY LOWER(long_name)";
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

        sql = "SELECT * FROM sbr.classification_schemes WHERE conte_idseq=? AND asl_name='RELEASED' ORDER BY LOWER(long_name)";

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

        sql = "SELECT COUNT(cs_idseq) FROM sbr.classification_schemes_view WHERE conte_idseq=? AND asl_name='RELEASED'";
        results = getOneInt( sql, conteId );
        return ( results > 0 );
    }


    /**
     * @param contextId
     * @return
     */
    @Override
    public ClassificationSchemeModel getClassificationSchemeById( String contextId )
    {

        //sql = "SELECT * FROM sbrext.cabio_class_schemes_view WHERE conte_idseq=?";

        sql = "SELECT DISTINCT cs_idseq , preferred_name, long_name, " +
                "preferred_definition, cstl_name,asl_name,conte_idseq " +
                " FROM sbr.classification_Schemes_view" +
                " WHERE conte_idseq = ? " +
                " AND asl_name = 'RELEASED' " +
                " AND cstl_name != 'Publishing' " +
                " ORDER BY UPPER(long_name)  ";

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
