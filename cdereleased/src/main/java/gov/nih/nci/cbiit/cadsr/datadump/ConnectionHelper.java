package gov.nih.nci.cbiit.cadsr.datadump;
/*
 * Copyright (C) 2018 Leidos Biomedical Research, Inc. - All rights reserved.
 */
import java.sql.Connection;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import oracle.jdbc.pool.OracleDataSource;

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
	protected Connection createConnection() throws Exception {
		OracleDataSource ds = new OracleDataSource();
		ds.setURL(System.getenv("db_url"));
		ds.setUser(System.getenv("db_user"));
		ds.setPassword(System.getenv("db_credential"));

		return ds.getConnection();
	}
}