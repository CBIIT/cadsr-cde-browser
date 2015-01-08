/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.dao.operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.sql.DataSource;
import java.util.List;

public abstract class AbstractDAOOperations extends JdbcDaoSupport
{

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

        T results = type.cast(
                getJdbcTemplate().queryForObject(
                        sql, new Object[]{ where },
                        new BeanPropertyRowMapper( type ) ) );

        return results;
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
