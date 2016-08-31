package gov.nih.nci.cadsr.common;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UsageLog
{
    private static Logger logger = LogManager.getLogger( UsageLog.class.getName() );

    // For easy to parse prefix
    private String prefix = "USAGE_LOG";
    private String dateTimeFormat = "YYYY:MM:dd:HH:mm";

    public void log(  String action, String entry )
    {
        logger.info( flatten( buildLogEntry( action, entry, getFormattedDate() ) ) );
    }

    protected String buildLogEntry( String action, String entry, String date )
    {
        String logEntry = "[" +
                prefix + "][" +
                date + "][" +
                action + "] " +
                entry;

        return logEntry;
    }

    private String getFormattedDate()
    {
        LocalDateTime currentDate = LocalDateTime.now();
        String formattedCurrentDate = currentDate.format( DateTimeFormatter.ofPattern( dateTimeFormat ) );
        return formattedCurrentDate;
    }


    // Only for testing
    protected String getFormattedDate( String dateTime )
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm" );
        LocalDateTime currentDate = LocalDateTime.parse( dateTime, formatter );
        String formattedCurrentDate = currentDate.format( DateTimeFormatter.ofPattern( dateTimeFormat ) );
        return formattedCurrentDate;
    }


    /**
     * To make sure the entries are on one line.
     *
     * @param logEntry
     * @return
     */
    protected String flatten( String logEntry )
    {
        String flattened = logEntry
                .replaceAll( "(\\n|\\r|\\t)", " " )
                .replaceAll( "  *", " " )
                .replaceAll( " $", "" );
        return flattened;
    }
}
