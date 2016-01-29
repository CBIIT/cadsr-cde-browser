package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lernermh on 7/29/15.
 */
public class TestDaoImpl extends AbstractDAOOperations implements TestDao
{
    private Logger logger = LogManager.getLogger( TestDaoImpl.class.getName() );
    private JdbcTemplate jdbcTemplate;

    private String basicSearchSql;

    public TestDaoImpl()
    {

    }

    @Autowired
    TestDaoImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();

    }


    public List<String> getRowsByTableCol( final String tableName, final List<String> rowList )
    {
        String[] args = {};
        final String[] tabCol = tableName.split( "," );
        final List<String> rowVals = new ArrayList<>();
        String sql = "SELECT * FROM " + tabCol[0] + " WHERE " + tabCol[1] + " LIKE '%C43234%' OR " + tabCol[1] + " LIKE '%C17459%' OR " +
                tabCol[1] + " LIKE '%C17998%' OR " + tabCol[1] + " LIKE '%C41222%' ";
        ;
        //String sql = "select " + tabCol[1] + " from " +  tabCol[0] + " WHERE " +  tabCol[1] +" LIKE '%C43234%'" ;
        logger.debug( sql + " <<<<<<<" );

        jdbcTemplate.query( sql, args, new RowCallbackHandler()
                {
                    @Override
                    public void processRow( ResultSet rs ) throws SQLException
                    {
                        ResultSetMetaData rsmd = rs.getMetaData();
                        String strResults = "";
                        int i = rsmd.getColumnCount();
                        for( int f = 1; f <= i; f++ )
                        {
                            strResults += "[" + tabCol[0] + "] [" + rsmd.getColumnName( f ) + "] [";
                            strResults += rs.getString( f ) + "] | ";
                            //logger.debug( "RS: " + rs.isLast() );
                            //logger.debug( "RS: " + rs.getString( 2 ) );
                            //rowVals.add( rs.getRow() );
                        }
                        //logger.debug( "RS: " + strResults );
                        System.out.println( strResults );
                        /*
                        if( tableName.compareTo( "SBR.AC_CI_BU,AC_IDSEQ" ) == 0)
                        {
                            System.out.println( strResults + " XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                        }
                        else
                        {
                            System.out.println( strResults );
                        }
*/
                        rowVals.add( strResults );
                    }
                }
        );
        return rowVals;
    }

    @Override
    public void getRoewsByTable( int rowCount, final String tableName, final List<String> rowList )
    {
        String strRowCount = Integer.toString( rowCount + 1 );
        String[] args = {};
        final List<String> rowVals = new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE ROWNUM < " + strRowCount;
        logger.debug( sql + " <<<<<<<" );

        jdbcTemplate.query( sql, args, new RowCallbackHandler()
                {
                    @Override
                    public void processRow( ResultSet rs ) throws SQLException
                    {
                        ResultSetMetaData rsmd = rs.getMetaData();
                        String strResults = "(" + tableName + ") ";
                        int i = rsmd.getColumnCount();
                        for( int f = 1; f <= i; f++ )
                        {
                            strResults += rsmd.getColumnName( f ) + "[";
                            strResults += rs.getString( f ) + "]";
                            if( f != i )
                            {
                                strResults += " | ";

                            }
                            //logger.debug( "RS: " + rs.isLast() );
                            //logger.debug( "RS: " + rs.getString( 2 ) );
                            //rowVals.add( rs.getRow() );
                        }
                        //logger.debug( "RS: " + strResults );
                        rowList.add( strResults + "\n" );
                    }
                }
        );

    }


}
