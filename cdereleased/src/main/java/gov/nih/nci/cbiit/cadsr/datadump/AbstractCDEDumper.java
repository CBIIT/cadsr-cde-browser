package gov.nih.nci.cbiit.cadsr.datadump;
import java.io.File;
/*
 * Copyright (C) 2018 Leidos Biomedical Research, Inc. - All rights reserved.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class AbstractCDEDumper implements CDEDump {
	private String dbURL;
	private String username;
	private String password;
	private String fileNamePrefix;

	public String getDbURL() {
		return this.dbURL;
	}

	public void setDbURL(String dbURL) {
		this.dbURL = dbURL;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFileNamePrefix() {
		return this.fileNamePrefix;
	}

	public void setFileNamePrefix(String fileNamePrefix) {
		this.fileNamePrefix = fileNamePrefix;
	}

	//This code was for ojdbc4
//	protected Connection createConnectionJdbc4() throws Exception {
//		OracleDataSource ds = new OracleDataSource();
//		ds.setURL(getDbURL());
//		ds.setUser(getUsername());
//		ds.setPassword(getPassword());
//
//		return ds.getConnection();
//	}

	protected Connection createConnection() throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection connection = null;
		connection = DriverManager.getConnection(getDbURL(), getUsername(), getPassword());

		if (connection != null) 
		{
			System.out.println("DB Connection is created by AbstractCDEDumper.");
		} else 
		{
			throw new Exception("AbstractCDEDumper: Failed to make connection " + getDbURL() + " for " + getUsername());
		}
		return connection;
	}
	//this one gives springframework error No supported DataSource type found.
//	protected Connection createConnection() throws Exception {
//        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
//        dataSourceBuilder.driverClassName("oracle.jdbc.driver.OracleDriver");
//        dataSourceBuilder.url(getDbURL());
//        dataSourceBuilder.username(getUsername());
//        dataSourceBuilder.password(getPassword());
//        return dataSourceBuilder.build().getConnection();
//	}
	protected String getTimeStr() {
		Calendar cal = Calendar.getInstance();
		int date = cal.get(5);
		int month = cal.get(2) + 1;
		int year = cal.get(1);
		int sec = cal.get(13);
		int min = cal.get(12);
		int hour = cal.get(10);

		return "" + year + month + date + hour + min + sec;
	}
	/**
	 * Make just one time call for this program run.
	 * 
	 * @param typeDirectoryName not null
	 * @return String Directory to put data end with a path separator.
	 */
	public String getDirectoryFull(String typeDirectoryName) {
		String directoryName;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd_M_yyyy");
		Date now = new Date();
		String steUploadCurr = System.getProperty("user.dir");//User working directory
		String parentDir =  steUploadCurr + File.separator + dateFormat.format(now);
		File uploadDirDate = new File(parentDir);

		if (! uploadDirDate.exists())	{
			uploadDirDate.mkdirs();//date related directory
		}
		String strUploadDirType = parentDir + File.separator + typeDirectoryName;
		File uploadDirType = new File(strUploadDirType);
		if (! uploadDirType.exists())	{
			uploadDirType.mkdirs();//date related xml data directory
		}
		String strUploadDirData = strUploadDirType + File.separator + "data";
		File uploadDirData = new File(strUploadDirData);
		if (! uploadDirData.exists())	{
			uploadDirData.mkdirs();//date related xml data directory
		}
		else {
			System.out.println("Data directory exists: " + strUploadDirData);
		}
		directoryName = strUploadDirData + File.separator;
		return directoryName;
	}
	public String getDirectory(String typeDirectoryName) {
		String directoryName;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd_M_yyyy");
		Date now = new Date();
		String parentDir =  dateFormat.format(now);
		File uploadDirDate = new File(parentDir);

		if (! uploadDirDate.exists())	{
			uploadDirDate.mkdirs();//date related directory
		}
		String strUploadDirType = parentDir + File.separator + typeDirectoryName;
		File uploadDirType = new File(strUploadDirType);
		if (! uploadDirType.exists())	{
			uploadDirType.mkdirs();//date related xml data directory
		}
		String strUploadDirData = strUploadDirType + File.separator + "data";
		File uploadDirData = new File(strUploadDirData);
		if (! uploadDirData.exists())	{
			uploadDirData.mkdirs();//date related xml data directory
		}
		else {
			System.out.println("Data directory exists: " + strUploadDirData);
		}
		directoryName = strUploadDirData + File.separator;
		return directoryName;
	}
}