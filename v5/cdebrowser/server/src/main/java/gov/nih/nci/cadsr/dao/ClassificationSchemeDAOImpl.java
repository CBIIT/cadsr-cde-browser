package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.*;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import gov.nih.nci.cadsr.dao.operation.LookupDataQueryBuilder;
import gov.nih.nci.cadsr.service.model.cdeData.classifications.ClassificationScheme;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import java.util.List;

public class ClassificationSchemeDAOImpl extends AbstractDAOOperations implements ClassificationSchemeDAO
{
    private Logger logger = LogManager.getLogger( ClassificationSchemeDAOImpl.class.getName() );

    private JdbcTemplate jdbcTemplate;
    private String sql;
    private LookupDataQueryBuilder lookupDataQueryBuilder;


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
                " FROM sbr.classification_Schemes, sbr.cs_recs c  " +
                " WHERE  cs_idseq = c_cs_idseq " +
                " AND rl_name = 'HAS_A' " +
                " AND p_cs_idseq = ?" +
                " ORDER BY long_name  ";

        logger.debug( "getChildrenClassificationSchemesByCsId( String csId ) executing query " + sql + " (p_cs_idseq is " + csId + ")" );

        return getAll( sql, csId, ClassificationSchemeModel.class );
    }

    
    /**
     * @param csId
     * @return
     */
    @Override
    public List<ClassificationSchemeModel> getAllChildrenClassificationSchemes()
    {
        sql = "SELECT distinct CS_IDSEQ , preferred_name, long_name, " +
                "preferred_definition, cstl_name,asl_name,conte_idseq " +
                " FROM sbr.classification_Schemes, sbr.cs_recs c  " +
                " WHERE  cs_idseq = c_cs_idseq " +
                " AND rl_name = 'HAS_A' ";
        logger.debug( "getAllChildrenClassificationSchemes executing query " + sql);

        return getAll( sql, ClassificationSchemeModel.class );
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
    

    /**
     * Retrieving only Classification schemes that have children and preventing the corresponding child classification schemes 
     * from being part of the main Parent Classifications list as they would be recursively added to their parents
     * @param conteId
     * @return
     */
    @Override
    public List<ClassificationSchemeModel> getClassificationSchemesSansChildren( String conteId )
    {
        List<ClassificationSchemeModel> results;

        sql = "SELECT * FROM sbr.classification_schemes WHERE conte_idseq=? AND asl_name='RELEASED' " +
        		"and cs_idseq NOT IN "+
        		"(select CS_IDSEQ from (SELECT distinct CS_IDSEQ , preferred_name, long_name, " +
        		                "preferred_definition, cstl_name,asl_name, conte_idseq " +
        		                "FROM sbr.classification_Schemes, sbr.cs_recs c " +
        		                "WHERE  cs_idseq = c_cs_idseq " +
        		                "AND rl_name = 'HAS_A')) " +
        " ORDER BY LOWER(long_name)";

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
        sql = "SELECT DISTINCT cs_idseq , preferred_name, long_name, " +
                "preferred_definition, cstl_name,asl_name,conte_idseq " +
                " FROM sbr.classification_Schemes_view" +
                " WHERE conte_idseq = ? " +
                " AND asl_name = 'RELEASED' " +
                " AND cstl_name != 'Publishing' " +
                " ORDER BY UPPER(long_name)  ";

        ClassificationSchemeModel results = query( sql, contextId, ClassificationSchemeModel.class );

        return results;
    }
    
    @Override
	public List<ClassificationScheme> getAllClassificationSchemeWithProgramAreaAndContext(String contexIdSeq, String csOrCsCsi)
	{
    	String sql = lookupDataQueryBuilder.buildCSLookupQuery(contexIdSeq, csOrCsCsi);
		
        List<ClassificationScheme> results = null;
    	if (StringUtils.isNotEmpty(contexIdSeq)) {
    		results = jdbcTemplate.query(sql, new Object[]{ contexIdSeq}, new BeanPropertyRowMapper(ClassificationScheme.class));
    	}
    	else if (StringUtils.isNotEmpty(csOrCsCsi)) {
    		csOrCsCsi = '%' + csOrCsCsi + '%';
    		results = jdbcTemplate.query(sql, new Object[]{csOrCsCsi, csOrCsCsi}, new BeanPropertyRowMapper(ClassificationScheme.class));
    	}
        return results;
	}
    
	public LookupDataQueryBuilder getLookupDataQueryBuilder() {
		return lookupDataQueryBuilder;
	}

	public void setLookupDataQueryBuilder(LookupDataQueryBuilder lookupDataQueryBuilder) {
		this.lookupDataQueryBuilder = lookupDataQueryBuilder;
	}

}
