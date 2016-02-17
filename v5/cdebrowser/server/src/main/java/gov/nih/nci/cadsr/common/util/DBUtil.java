package gov.nih.nci.cadsr.common.util;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;

public class DBUtil
{
    //private static Log log = LogFactory.getLog(gov.nih.nci.cadsr.common.util.DBUtil.class.getName());
    private static Logger logger = LogManager.getLogger( DBUtil.class.getName() );

    public static final String CDEBROWSER_PROVIDER = "cdebrowser_bc4j";
    private Connection conn;
    private boolean isConnected = false;
    private boolean isOracleConnection = false;
    public DBUtil() {
        conn = null;
    }

    /**
     * get the property from the property file
     * @return String
     */
    public String getJNDIProperty()
    {
        String jndiName = "";
        try {
            InputStream ins = getClass().getResourceAsStream("/gov/nih/nci/ncicb/cadsr/common/jndi.properties");
            Properties props = new Properties();
            props.load(ins);
            jndiName = props.getProperty("datasource.jndi.name");
        } catch (IOException e) {
            logger.error( "unable to get property file", e );
        }
        return (jndiName == null) ? "" : jndiName;
    }

    /**
     * gets the datasource
     * @return DataSource
     */
    public DataSource getDataSource() {
        DataSource ds = null;



/*
	    if (getServiceLocator() != null) {
	      ds = getServiceLocator().getDataSource(getJNDIProperty());
	    }

	    if (logger.isDebugEnabled()) {
	      logger.debug("Return DataSource =  " + ds);
	    }
*/

        return ds;
    }

    /**
     *  This method returns a Connection obtained from the container using the
     *  datasource name specified as a parameter
     */
    public boolean getConnectionFromContainer()
            throws Exception {
        if (!isConnected) {
            try {
                //DataSource ds = getServiceLocator().getDataSource(PersistenceConstants.DATASOURCE_LOCATION_KEY);
                DataSource ds = getDataSource();
                //logger.info(PersistenceConstants.DATASOURCE_LOCATION_KEY + " dssource " + ds.toString());
                //Extract Oracle Native Connection
                conn = ds.getConnection();
                isConnected = true;
                isOracleConnection = false;
                logger.info
                        ( "Connected to the database successfully using datasource " + ds.toString() );
            }
            catch (Exception e) {
                logger.error( "Exception occurred in getConnectionFromContainer", e );
                throw new Exception("Exception in getConnectionFromContainer() ");
            }
        }
        return isConnected;
    }

    /**
     *  This method returns a Oracle Connection obtained by createing a Datasource using the
     *  connection information in cle-providers.xml
     *  This is a temporary fix till we move to 9i libs
     *
     */
 /* public boolean getOracleConnectionFromContainer()
                    throws Exception {
   if(isConnected&&!isOracleConnection)
   {
     this.returnConnection();
   }
    if (!isConnected) {
      try {
        ConnectionManager manager = ConnectionManager.getInstance();
        ConnectionProvider provider = manager.getProvider(CDEBROWSER_PROVIDER);

        DataSource ds = DataSourceUtil.getOracleDataSource(provider.getConnectionString(),provider.getUserName(),provider.getPassword());
        conn = ds.getConnection();

        isConnected = true;
        isOracleConnection = true;
        logger.info
                ( "Connected to the database successfully using datasource " );
      }
      catch (Exception e) {
        logger.error( "Exception occurred in getConnectionFromContainer", e );
        throw new Exception("Exception in getConnectionFromContainer() ");
      }
    }
    return isConnected;
  }
*/
    /**
     *  This method returns a Vector containing multiple DB records after executing
     *  the sql statement specified as the parameter
     */
    public Vector retrieveMultipleRecordsDB (String sqlStmt) throws SQLException {
        Vector rowData = null;
        Vector dataToReturn = null;
        int columnCount;
        Statement stmt = null;
        ResultSet	rs = null;
        boolean isThereResult =false;
        try {
            dataToReturn = new Vector();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlStmt);
            columnCount = rs.getMetaData().getColumnCount();
            isThereResult = rs.next();
            while (isThereResult) {
                rowData = new Vector();
                for (int i = 0; i < columnCount; i++) {
                    rowData.addElement(rs.getString(i + 1));
                }
                dataToReturn.addElement(rowData);
                isThereResult = rs.next();
            }
        }
        catch (SQLException sqle) {
            logger.error( "Exception in gov.nih.nci.cadsr.common.util.DBUtil.retrieveMultipleRecordsDB(String )" );
            logger.error( "The statement executed : " + sqlStmt, sqle );

            throw sqle;
        }
        finally {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        }
        return dataToReturn;
    }

    /**
     *   This method returns a Vector containing multiple DB records after executing
     *    the sql statement with the params specified
     */
    public Vector retrieveMultipleRecordsDB
    (String tableName, String[] tableFields, String whereClause)
            throws SQLException {

        Vector rowData = null;
        Vector dataToReturn = null;
        int columnCount;
        Statement stmt = null;
        ResultSet	rs = null;
        boolean isThereResult =false;
        String stmt_str = null;
        try {
            dataToReturn = new Vector();
            stmt = conn.createStatement();
            stmt_str = "SELECT ";
            for (int i = 0;i < tableFields.length; i++){
                if (i < tableFields.length - 1){
                    stmt_str += tableFields[i] + ", ";
                }
                else{
                    stmt_str += (String) tableFields[i] + " FROM " + tableName + " " + whereClause;
                }
            }

            logger.debug( "statement" + stmt_str );
            rs = stmt.executeQuery(stmt_str);
            isThereResult = rs.next();
            while (isThereResult) {
                rowData = new Vector();
                for (int i = 0; i < tableFields.length; i++) {
                    rowData.addElement(rs.getString(i + 1));
                }
                dataToReturn.addElement(rowData);
                isThereResult = rs.next();
            }

        }

        catch (SQLException sqle) {
            logger.error( "  Exception in gov.nih.nci.cadsr.common.util.DBUtil.retrieveMultipleRecordsDB()" );
            logger.error( "  The statement executed : " + stmt_str, sqle );
            throw sqle;
        }
        finally {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        }
        return dataToReturn;
    }

    /**
     *  This method returns a Vector containing one DB record
     *  after executing the sql statement specified as a parameter
     */
    public Vector retrieveRecordDB( String sqlStmt) throws SQLException{

        Vector rowData = null;
        Vector dataToReturn = null;
        int columnCount;
        Statement stmt = null;
        ResultSet	rs = null;
        boolean isThereResult =false;
        try {
            dataToReturn = new Vector();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sqlStmt);
            columnCount = rs.getMetaData().getColumnCount();
            isThereResult = rs.next();
            if (isThereResult== true) {
                for (int i = 0;i < columnCount; i++) {
                    dataToReturn.addElement(rs.getString(i+1));
                }
            }
        }
        catch (SQLException sqle) {
            logger.error( "  Exception in gov.nih.nci.cadsr.common.util.DBUtil.retrieveRecordDB(String)" );
            logger.error( "  The statement executed : " + sqlStmt, sqle );
            throw sqle;
        }
        finally {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        }
        return dataToReturn;
    }

    /**
     *   This method returns a Vector after executing the sql with
     *   the params specified
     */
    public Vector retrieveRecordDB( String tableName,
                                    String[] tableFields,
                                    String whereClause) throws SQLException {
        Vector rowData = null;
        Vector dataToReturn = null;
        int columnCount;
        Statement stmt = null;
        ResultSet	rs = null;
        boolean isThereResult =false;
        String stmt_str = null;
        try {
            dataToReturn = new Vector();
            stmt = conn.createStatement();
            stmt_str = "SELECT ";
            for (int i = 0;i < tableFields.length; i++) {
                if (i < tableFields.length - 1) {
                    stmt_str += tableFields[i] + ", ";
                } else {
                    stmt_str += (String) tableFields[i] + " FROM " + tableName + " " + whereClause;
                }
            }

            rs = stmt.executeQuery(stmt_str);
            isThereResult = rs.next();

            if (isThereResult== true) {
                for (int i = 0;i < tableFields.length; i++) {
                    dataToReturn.addElement(rs.getObject(i+1));
                }
            }

        }
        catch (SQLException sqle) {
            logger.error( "  Exception in gov.nih.nci.cadsr.common.util.DBUtil.retrieveRecordDB()" );
            logger.error( "  The statement executed : " + stmt_str, sqle );
            throw sqle;
        }
        finally {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        }
        return dataToReturn;
    }

    /**
     *  Get unique id from the database
     *  idGenerator - an Oracle sequence number or store proc
     *
     */
    public String getUniqueId(String idGenerator) throws SQLException {
        String id=null;
        Statement stmt = null;
        ResultSet	rs = null;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery
                    ("SELECT " + idGenerator + " FROM DUAL");
            rs.next();
            id = rs.getString(1);
        }
        catch(SQLException sqle) {
            logger.error( "Exception in getUniqueId()", sqle );
            throw sqle;
        }
        finally {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        }
        return id;
    }

    /**
     *  Get unique id from the database
     *  idGenerator - an Oracle sequence number or store proc
     *
     */
    public static String getUniqueId(Connection con, String idGenerator) throws SQLException {
        String id=null;
        Statement stmt = null;
        ResultSet	rs = null;

        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery
                    ("SELECT " + idGenerator + " FROM DUAL");
            rs.next();
            id = rs.getString(1);
        }
        catch(SQLException sqle) {
            logger.error( "Exception in getUniqueId()", sqle );
            throw sqle;
        }
        finally {
            if (rs != null) {
                rs.close();
                rs = null;
            }
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        }
        return id;
    }

    public void returnConnection() throws SQLException {
        try {
            if (conn != null) {
                conn.close();
                isConnected = false;
            }
        }
        catch (SQLException sqle) {
            //logger.error("Error occured in returing DB connection to the container", sqle);
            //throw sqle;
        }
    }

    public Connection getConnection(){
        return conn;
    }

    public ResultSet executeQuery(String sqlStmt) throws SQLException {
        Statement stmt = null;
        ResultSet	rs = null;
        try {
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sqlStmt);
        }
        catch (SQLException ex) {
            logger.error( "Exception occurred in executeQuery " + sqlStmt, ex );
            throw ex;
        }
        return rs;
    }

    public static String readFile( String file ) throws IOException {
        BufferedReader reader = new BufferedReader( new FileReader(file));
        String         line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");

        while( ( line = reader.readLine() ) != null ) {
            stringBuilder.append( line );
            stringBuilder.append( ls );
        }

        return stringBuilder.toString();
    }
/*

  private  OracleConnection createOracleConnection(
    String dbURL,
    String username,
    String password) throws Exception {
    OracleDataSource ds = DataSourceUtil.getOracleDataSource(dbURL,username,password);
    return (OracleConnection)ds.getConnection();
    }

  */
/*
   * Need to recode after the class loading problem is fixed
   *@return Oracle native Connection from jboss wrapper connection
   */

    /*

  public static OracleConnection extractOracleConnection(
    Connection conn
    ) throws Exception {
      OracleJBossNativeJdbcExtractor extractor = new OracleJBossNativeJdbcExtractor();
      return extractor.doGetOracleConnection(conn);
    }


*/

    public static void main(String[] args) {
        DBUtil dBUtil = new DBUtil();
    }
/*

  public ServiceLocator getServiceLocator()
  {
    ObjectLocator locator  = new SpringObjectLocatorImpl();
    return (ServiceLocator)locator.findObject("serviceLocator");
  }

*/
}
