/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ContextModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;

@Repository
public class ContextDAOImpl  extends AbstractDAOOperations implements ContextDAO
{
    private Logger logger = LogManager.getLogger( ContextDAOImpl.class.getName() );

    private JdbcTemplate jdbcTemplate;

    public ContextDAOImpl()
    {
    }



    @Autowired
    ContextDAOImpl(DataSource dataSource) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }


    @Override
    public List<ContextModel> getAllContexts()
    {
        List<ContextModel>  results;

        String sql = "select * from SBR.CONTEXTS_VIEW order by NAME";
        //logger.debug( ">>>>>>> "+ sql );
        results = getAll( sql, ContextModel.class );
        //logger.debug( sql + " <<<<<<<" );

        return results;
    }


 @Override
    public List<ContextModel> getContextsByName( String name )
    {
        List<ContextModel>  results;

        String sql = "select * from SBR.CONTEXTS_VIEW where NAME=?";
        //logger.debug( ">>>>>>> "+ sql.replace( "?", name ) );
        results = getAll( sql, name , ContextModel.class );
        //logger.debug( sql.replace( "?", name ) + " <<<<<<<" );
        return results;
    }


    @Override
    public ContextModel getContextById( String contextId )
    {
        String sql = "select * from SBR.CONTEXTS_VIEW where CONTE_IDSEQ=?";
        ContextModel results = query( sql, contextId, ContextModel.class );

        return results;
    }


    @Override
    public Collection getContexts( String username, String businessRole )
    {
        throw new UnsupportedOperationException("Not yet implemented.");
        //return null;
    }

    @Override
    public List getAllContexts( String excludeList )
    {
        throw new UnsupportedOperationException("Not yet implemented.");
        //return null;
    }
}
