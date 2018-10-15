package gov.nih.nci.cbiit.cadsr.datadump;
/*
 * Copyright (C) 2018 Leidos Biomedical Research, Inc. - All rights reserved.
 */
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Service;

import oracle.xml.sql.query.OracleXMLQuery;

@Service
public class XMLGeneratorBean {
	private String targetView = "";
	private String whereClause = "";
	private String sqlQuery = "";
	private String orderBy = "";
	private String dataSource = "";
	private String rowset = "";
	private String row = "";
	private int maxRows = -1;
	private boolean showNull = false;
	private OracleXMLQuery xmlQuery;
	private int lastRow = 0;
	private Connection cn = null;
	private ResultSet rset = null;
	private Statement stmt = null;
	private boolean jndiDatasource = false;

	public String getXMLString(Connection con) throws SQLException {
		String xmlString = "";
		try {
			buildQuery();

			System.out.println("Sql Stmt: " + this.sqlQuery);

			this.xmlQuery = new OracleXMLQuery(con, this.sqlQuery);
			this.xmlQuery.setEncoding("UTF-8");
			this.xmlQuery.useNullAttributeIndicator(this.showNull);

			if (!(this.rowset.equals(""))) {
				this.xmlQuery.setRowsetTag(this.rowset);
			}

			if (!(this.row.equals(""))) {
				this.xmlQuery.setRowTag(this.row);
			}

			if (this.maxRows != -1) {
				this.xmlQuery.setMaxRows(this.maxRows);
			}

			try {
				if (con != null)
					con.close();
			} catch (SQLException sqle) {
				System.out.println("Exception getXMLString()");
				sqle.printStackTrace();
				throw sqle;
			}
		} catch (Exception sqle) {
			System.out.println("getXMLString()");
			try {
				if (con != null)
					con.close();
			} catch (SQLException sqle1) {
				System.out.println("Exception getXMLString()");
				sqle1.printStackTrace();
				throw sqle;
			}
		} finally {
			try {
				if (con != null)
					con.close();
			} catch (SQLException sqle) {
				System.out.println("Exception getXMLString()");
				sqle.printStackTrace();
				throw sqle;
			}
		}
		return xmlString;
	}

	public void createOracleXMLQuery() throws SQLException {
		try {
			buildQuery();

			this.stmt = this.cn.createStatement(1004, 1007);

			this.rset = this.stmt.executeQuery(this.sqlQuery);

			this.xmlQuery = new OracleXMLQuery(this.cn, this.rset);
			this.xmlQuery.keepCursorState(true);
			this.xmlQuery.setDateFormat("yyyy-MM-dd");//java class SimpleDateFormat
			this.xmlQuery.setRaiseNoRowsException(true);
			this.xmlQuery.setRaiseException(true);
			this.xmlQuery.setEncoding("UTF-8");
			if (!(this.rowset.equals(""))) {
				this.xmlQuery.setRowsetTag(this.rowset);
			}

			if (!(this.row.equals(""))) {
				this.xmlQuery.setRowTag(this.row);
			}
			if (this.maxRows != -1) {
				this.xmlQuery.setMaxRows(this.maxRows);
			}
			this.xmlQuery.useNullAttributeIndicator(this.showNull);
		} catch (SQLException sqle) {
			closeResources();
			System.out.println("Exception createOracleXMLQuery()");
			sqle.printStackTrace();
			throw sqle;
		}
	}

	public void setTargetDBObject(String target) {
		this.targetView = target;
	}

	public void setWhereClause(String wc) {
		this.whereClause = " WHERE " + wc;
	}

	public void buildQuery() {
		if (!(this.targetView.equals(""))) {
			this.sqlQuery = "SELECT * FROM " + this.targetView + this.whereClause + this.orderBy;
		} else
			this.sqlQuery = this.sqlQuery + this.whereClause + this.orderBy;
	}

	public void setQuery(String qry) {
		this.sqlQuery = qry;
	}

	public String getQuery(String qry) {
		return this.sqlQuery;
	}

	public void setOrderBy(String oby) {
		this.orderBy = " ORDER BY " + oby;
	}

	public void setDataSource(String ds) {
		this.dataSource = ds;
	}

	public void setRowsetTag(String rs) {
		this.rowset = rs;
	}

	public void setRowTag(String r) {
		this.row = r;
	}

	public void setMaxRowSize(int rs) {
		this.maxRows = rs;
	}

	public void displayNulls(boolean b) {
		this.showNull = b;
	}

	public String getRows(int startRow, int endRow) throws SQLException {
		this.xmlQuery.setMaxRows(endRow - startRow);
		return this.xmlQuery.getXMLString();
	}

	public String getNextPage() throws SQLException {
		String xmlStr = null;
		xmlStr = getRows(this.lastRow, this.lastRow + this.maxRows);
		this.lastRow += this.maxRows;
		return xmlStr;
	}

	public void closeResources() {
		try {
			if (this.rset != null)
				this.rset.close();
			if (this.stmt != null)
				this.stmt.close();
			if (this.cn != null)
				this.cn.close();
		} catch (SQLException sqle) {
		}
	}

	private void writeToFile(String xmlStr, String fn) throws Exception {
		try {
			FileOutputStream newFos = new FileOutputStream(fn);
			DataOutputStream newDos = new DataOutputStream(newFos);
			newDos.writeBytes(xmlStr + "\n");
			newDos.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			throw e;
		}
	}

	private void initializeDBConnection() {
		if (this.jndiDatasource) {
			throw new RuntimeException("No jndiDatasource in this configuration");
		}
	}

	public void setJndiDatasource(boolean jndiDatasource) {
		this.jndiDatasource = jndiDatasource;
	}

	public boolean isJndiDatasource() {
		return this.jndiDatasource;
	}

	public void setConnection(Connection conn) {
		this.cn = conn;
	}
	
}
