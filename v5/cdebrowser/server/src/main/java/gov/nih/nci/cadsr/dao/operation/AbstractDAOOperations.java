/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.dao.operation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.sql.DataSource;
import java.util.List;

public abstract class AbstractDAOOperations extends JdbcDaoSupport
{
    private Logger logger = LogManager.getLogger( AbstractDAOOperations.class.getName() );

    @Autowired
    AbstractDAOOperations( DataSource dataSource )
    {
        setDataSource( dataSource );
    }

    protected AbstractDAOOperations()
    {
    }

    /**
     *
     * @param sql
     * @param where
     * @param type
     * @param <T>
     * @return
     */
    public <T> T query( String sql, String where, Class<T> type )
    {


        try
        {
            T results = type.cast(
                    getJdbcTemplate().queryForObject(
                            sql, new Object[]{ where },
                            new BeanPropertyRowMapper( type ) ) );

            return results;
        } catch( DataAccessException e )
        {
            logger.debug( "Error: [" + e.getMessage() + "]" );
            logger.debug( "Error: [" + e.toString() + "]" );
            if( e.getMessage().compareTo( "Incorrect result size: expected 1, actual 0" ) ==0)
            {
                logger.debug( "No results" );
            }
            // FIXME - this is a WORK AROUND FOR SOME BAD DATA - MAKE SURE
            else if(  e.getMessage().startsWith( "Incorrect result size: expected 1, actual" ))
            {
                logger.debug( e.getMessage() );
                logger.debug( sql + " where " + where );
                return  getAll(  sql,  where,  type ).get(0);
            }
            else
            {
                e.printStackTrace();
            }
        }
        catch( Exception e)
        {
            e.printStackTrace();
        }
        logger.debug( "Error: returning null." );
        return null;
    }

    public <T> T query( String sql, int where, Class<T> type )
    {
        return query( sql, Integer.toString( where ), type );
    }

        /**
         *
         * @param sql
         * @param where
         * @param type
         * @param <T>
         * @return
         */
    public <T> List<T> getAll( String sql, String where, Class<T> type )
    {

        List<T> allColumns = getJdbcTemplate().query(
                sql, new Object[]{ where },
                new BeanPropertyRowMapper( type )
        );

        return allColumns;
    }

    public Integer getOneInt(String sql, String where )
    {
        Integer n = (Integer)getJdbcTemplate().queryForObject(
                sql, new Object[] { where }, Integer.class);
        return n;
    }

    /**
     *
     * @param sql
     * @param type
     * @param <T>
     * @return
     */
    public <T> List<T> getAll( String sql, Class<T> type )
    {

        List<T> allColumns = getJdbcTemplate().query(
                sql, new BeanPropertyRowMapper( type )
        );

        return allColumns;
    }

    /**
     *
     * @param table
     * @param type
     * @param <T>
     * @return
     */
    public <T> List<T> getAllByTable( String table, Class<T> type )
    {

        List<T> allRowsAndColumns = getJdbcTemplate().query(
                "select * from " + table,
                new BeanPropertyRowMapper( type )
        );

        return allRowsAndColumns;
    }

}
