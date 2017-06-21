package gov.nih.nci.cadsr.dao.operation;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public abstract class AbstractDAOOperations extends JdbcDaoSupport
{
    private static Logger logger = LogManager.getLogger( AbstractDAOOperations.class.getName() );
    
    protected static final int oracleIn1000 = 1000;
    
    @Autowired
    AbstractDAOOperations( DataSource dataSource )
    {
        setDataSource( dataSource );
    }

    protected AbstractDAOOperations()
    {
    }

    /**
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
                            new BeanPropertyRowMapper<T>( type ) ) );

            return results;
        } catch( DataAccessException e )
        {
            //logger.debug( "Error: [" + e.getMessage() + "]" );
            //logger.debug( "Error: [" + e.toString() + "]" );
            if( e.getMessage().compareTo( "Incorrect result size: expected 1, actual 0" ) == 0 )
            {
                //logger.debug( "No results" );
            }
            // FIXME - this is a WORK AROUND FOR SOME BAD DATA - MAKE SURE
            else if( e.getMessage().startsWith( "Incorrect result size: expected 1, actual" ) )
            {
                logger.debug( e.getMessage() );
                logger.debug( sql + " where " + where );
                return getAll( sql, where, type ).get( 0 );
            }
            else
            {
                e.printStackTrace();
            }
        } catch( Exception e )
        {
            e.printStackTrace();
        }
        //logger.debug( "Error: returning null." );
        return null;
    }

    public <T> T query( String sql, int where, Class<T> type )
    {
        return query( sql, Integer.toString( where ), type );
    }

    /**
     * @param sql
     * @param where array of SQL parameters
     * @param type
     * @param <T>
     * @return List
     */
    public <T> List<T> getAll(String sql, Object[] where, Class<T> type)
    {
        List<T> allColumns = getJdbcTemplate().query(
                sql, where, new BeanPropertyRowMapper( type )
        );

        return allColumns;
    }
    /**
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
    public Integer getOneInt( String sql, String where )
    {
        Integer n = ( Integer ) getJdbcTemplate().queryForObject(
                sql, new Object[]{ where }, Integer.class );
        return n;
    }

    /**
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
     * @param table
     * @param type
     * @param <T>
     * @return
     */
    public <T> List<T> getAllByTable( String table, Class<T> type )
    {

        List<T> allRowsAndColumns = getJdbcTemplate().query(
                "SELECT * FROM " + table,
                new BeanPropertyRowMapper( type )
        );
        
        return allRowsAndColumns;
    }
    
    public int getInt(String sql)
    {
    	Integer i = (Integer) getJdbcTemplate().queryForObject(sql, Integer.class);
    	return i.intValue();
    }
	
    protected static <T> List<T> cleanUpIdDuplicates(List<T> acIdseqList) {
    	Set<T> acIdseqSet = new HashSet<>();
    	List<T> resultList = new ArrayList<>();
        for (T currIdseq : acIdseqList) {
        	if ((currIdseq != null) && (! acIdseqSet.contains(currIdseq)))  {
        		resultList.add(currIdseq);
        		acIdseqSet.add(currIdseq);
        	}
        }
        return resultList;
    }
    
    protected static <T> List<T> retrieve1000Ids(List<T> deIdseqList, String sqlStmt, String paramName, 
    		NamedParameterJdbcTemplate namedParameterJdbcTemplate, 
    		Class<T> clazz,
    		RowMapper<T> rowMapper) {
    	List<T> idseqList;
    	if (((deIdseqList != null ) && (! deIdseqList.isEmpty())) && (StringUtils.isNotBlank(paramName)) && (namedParameterJdbcTemplate != null)
    			&& (rowMapper != null)) {
            //MapSqlParameterSource parameters = new MapSqlParameterSource();
            Map<String, List<T>> param = Collections.singletonMap(paramName, deIdseqList);
            idseqList = namedParameterJdbcTemplate.query(sqlStmt, param, rowMapper);
    	}
    	else {
    		logger.info("Parameter problems in retrieve1000Ids, returning an empty list");
    		idseqList = new ArrayList<>();
    	}
        return idseqList;
    }
	public <T> List<T> getObjectList(List<T> acIdseqList, String sqlStmt, String paramName, 
			NamedParameterJdbcTemplate namedParameterJdbcTemplate, 
			Class<T> clazz, 
			RowMapper<T> beanPropertyRowMapper) {
		return getObjectList(acIdseqList, sqlStmt, paramName,
				namedParameterJdbcTemplate, clazz, beanPropertyRowMapper, true);
	}
    /**
     * Search for list of objects of type T.
     * T expected to have equals function implemented to return a unique List.
     * 
     * @param acIdseqList - List of SQL Parameters for IN Statement
     * @param sqlStmt - SQL Statement with named parameter
     * @return List<T>
     */	
	public <T> List<T> getObjectList(List<T> acIdseqList, String sqlStmt, String paramName, 
			NamedParameterJdbcTemplate namedParameterJdbcTemplate, 
			Class<T> clazz, 
			RowMapper<T> rowMapper, 
			boolean doFinalDuplicateCleanup) {
        List<T> arrResult = new ArrayList<>();
        if ((acIdseqList != null) && (!(acIdseqList.isEmpty()))) {
        	List<T> deIdseqList = cleanUpIdDuplicates(acIdseqList);
        	List<T> portionOf1000;
        	List<T> arrOf1000;
        	Iterator<T> iter = deIdseqList.iterator();
        	while (iter.hasNext()) {
        		portionOf1000 = new ArrayList<>();
        		for (int j = 0; ((j < oracleIn1000) & iter.hasNext()); j++) {
        			portionOf1000.add(iter.next());
        		}
        		arrOf1000 = retrieve1000Ids(portionOf1000, sqlStmt, paramName, namedParameterJdbcTemplate, clazz,
        	    	rowMapper);
        		arrResult.addAll(arrOf1000);
        	}
        }
        if (doFinalDuplicateCleanup) {
        	arrResult = cleanUpIdDuplicates(arrResult);
        }
        return arrResult;
	}
}
