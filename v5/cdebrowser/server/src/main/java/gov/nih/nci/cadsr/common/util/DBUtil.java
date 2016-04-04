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


    public DBUtil() {
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

}
