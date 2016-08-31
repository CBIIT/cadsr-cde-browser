package gov.nih.nci.cadsr.common;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by lernermh on 8/30/16.
 */
public class UsageLogTest
{
    private UsageLog usageLog = null;

    @Before
    public void setUp() throws Exception
    {
        usageLog = new UsageLog();
    }

    @Test
    public void flatten() throws Exception
    {
        String logEntry = "abc\n\rdef\n \t  ghi\njkl\r  \n  ";
        String expected = "abc def ghi jkl";
        //System.out.println( logEntry );
        //System.out.println( "[" + usageLog.flatten( logEntry ) + "]" );
        assertEquals( expected, usageLog.flatten( logEntry ) );
    }

    @Test
    public void dateFormatTest() throws Exception
    {
        String str = "2015-04-08 12:30";
        String expected = "2015:04:08:12:30";
        String results = usageLog.getFormattedDate(str);
        //System.out.println(results);
        assertEquals( expected, results );
    }

    @Test
    public void buildLogEntryTest() throws Exception
    {
        String date = "2015-04-08 12:30";
        String entryText = "This is a test message";
        String action = "SearchByMassAndDistanceFromTheSun";
        String expected = "[USAGE_LOG][2015:04:08:12:30][SearchByMassAndDistanceFromTheSun] This is a test message";
        String results = usageLog.buildLogEntry( action, entryText, usageLog.getFormattedDate(date));
        //System.out.println(results);
        assertEquals( expected, results );
    }
}
