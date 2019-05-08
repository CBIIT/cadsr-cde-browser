package gov.nih.nci.cbiit.cadsr.datadump;
/*
 * Copyright (C) 2018 Leidos Biomedical Research, Inc. - All rights reserved.
 */
import java.sql.Connection;
import java.sql.DriverManager;

import org.springframework.stereotype.Repository;

//import oracle.jdbc.pool.OracleDataSource;
@Repository
public class ConnectionHelper {
	//This code is currently not used
//	@Autowired
//	private JdbcTemplate jdbcTemplate;
//	public Connection getConnection() {
//		try {
//			return jdbcTemplate.getDataSource().getConnection();
//		} 
//		catch (SQLException e) {
//			e.printStackTrace();
//			throw new RuntimeException("DB Connection problem", e);
//		}
//	}
//	protected Connection createConnectionOjdbc4() throws Exception {
//		OracleDataSource ds = new OracleDataSource();
//		ds.setURL(System.getenv("db_url"));
//		ds.setUser(System.getenv("db_user"));
//		ds.setPassword(System.getenv("db_credential"));
//		return ds.getConnection();
//	}
	protected Connection createConnection() throws Exception {
		Connection connection = null;
		String dbUrl = System.getenv("db_url");
		connection = DriverManager.getConnection(dbUrl, System.getenv("db_user"), System.getenv("db_credential"));

		if (connection != null) 
		{
			System.out.println("DB Connection created by ConnectionHelper: " + dbUrl);
		} else 
		{
			System.out.println("ConnectionHelper: Failed to make DB connection! " + dbUrl);
		}
		return connection;
	}
}