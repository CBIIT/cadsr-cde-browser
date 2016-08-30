package gov.nih.nci.cadsr.common.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;


public class StringUtilities
{
    private static Logger logger = LogManager.getLogger( StringUtilities.class.getName() );


    public static boolean toBoolean( String inString )
    {
        boolean retVal = false;
        if(
                inString.toUpperCase().equals( "Y" ) ||
                        inString.toUpperCase().equals( "TRUE" ) ||
                        inString.equalsIgnoreCase( "Yes" ) )
        {
            retVal = true;
        }
        else if(
                inString.toUpperCase().equals( "N" ) ||
                        inString.toUpperCase().equals( "FALSE" ) ||
                        inString.equalsIgnoreCase( "No" ) )
        {
            retVal = false;
        }
        return retVal;
    }

    public static String booleanToStr( boolean bool )
    {
        if( bool )
        {
            return "Yes";
        }
        else
        {
            return "No";
        }
    }

    public static String booleanToStrYN( boolean bool )
    {
        if( bool )
        {
            return "Y";
        }
        else
        {
            return "N";
        }
    }

    public static String[] tokenizeCSVList( String values )
    {
        String[] retVal = null;

        try
        {
            java.util.StringTokenizer st = new java.util.StringTokenizer( values, "," );
            int numberOfTokens = st.countTokens();
            retVal = new String[numberOfTokens];

            for( int i = 0; i < numberOfTokens; i++ )
            {
                retVal[i] = st.nextToken();
            }
        }
        catch( Exception e )
        {
            return new String[0];
        }

        return retVal;
    }

    public static boolean isArrayWithEmptyStrings( String[] values )
    {

        if( values == null )
        {
            return true;
        }
        boolean result = true;
        for( int i = 0; i < values.length; ++i )
        {
            if( values[i] != null && !values[i].equalsIgnoreCase( "" ) )
            {
                result = false;
            }
        }
        return result;
    }

    public static String getValidJSString( String sourceStr )
    {
        String newStr = strReplace( sourceStr, "'", "\\'" );
        newStr = strReplace( newStr, "\"", "&quot;" );

        return newStr;
    }

    public static String replaceNull( Object obj )
    {
        if( obj == null )
        {
            return "";
        }
        else
        {
            return obj.toString().trim();
        }
    }

    /**
     * This method is used to replace null or empty string with  "&nbsp;";
     * Used in jsp pages
     */
    public static String ifNullReplaceWithnbsp( String obj )
    {
        if( obj == null )
        {
            return "&nbsp;";
        }
        else if( obj.equals( "" ) )
        {
            return "&nbsp;";
        }
        else
        {
            return obj;
        }
    }

    public static String strReplace(
            String sourceStr,
            String patternStr,
            String newStr )
    {
        int bePos = 0;
        int pos = 0;
        int patternStrLen = patternStr.length();
        ;

        int sourceStrLen = sourceStr.length();
        ;

        boolean found = false;
        String outputStr = new String();
        String endStr = new String();
        int endStrIdx = 0;

        while( bePos < sourceStrLen )
        {
            pos = sourceStr.indexOf( patternStr, bePos );

            if( pos != -1 )
            {
                outputStr += ( sourceStr.substring( bePos, pos ) + newStr );
                bePos = pos + patternStrLen;
                endStrIdx = pos + patternStrLen;
                found = true;
            }
            else
            {
                bePos = sourceStrLen;
            }
        }

        if( found )
        {
            return outputStr + sourceStr.substring( endStrIdx );
        }
        else
        {
            return sourceStr;
        }
    }

    public static boolean containsKey(
            String[] keys,
            String key )
    {
        if( keys == null )
        {
            return false;
        }
        /**
         for(int i=0; i<keys.length;i++)
         {
         String val = keys[i];
         if(val!=null)
         {
         if(val.equals(key))
         {
         return true;
         }
         }
         }
         return false;
         **/

        Arrays.sort( keys );

        int retValue = Arrays.binarySearch( keys, key );

        ArrayList list = new ArrayList();


        if( retValue >= 0 )
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    public static Date getCurDateDbFormat()
    {
        Date curDate = null;

        try
        {
            String DEFAULT_DB_ZERO_TIME_DATE_FORMAT = "yyyy-MM-dd";
            java.util.Date theDate = new java.util.Date();
            java.text.SimpleDateFormat simpleDate = new java.text.SimpleDateFormat();
            simpleDate.applyPattern( DEFAULT_DB_ZERO_TIME_DATE_FORMAT );

            String currDateStr = simpleDate.format( theDate );
            curDate = Date.valueOf( currDateStr );
        }
        catch( Exception e )
        {
            logger.error( e.getMessage(), e );
        }

        return curDate;
    }

    public static boolean isInteger( String inStr )
    {
        if( !doesValueExist( inStr ) )
        {
            return false;
        }

        try
        {
            if( Integer.parseInt( inStr.trim() ) < 0 )
            {
                return false;
            }
        }
        catch( NumberFormatException nfe )
        {
            return false;
        }

        return true;
    }

    public static boolean isDecimal( String inStr )
    {
        if( !doesValueExist( inStr ) )
        {
            return false;
        }

        try
        {
            Double.parseDouble( inStr.trim() );
        }
        catch( NumberFormatException nfe )
        {
            return false;
        }

        return true;
    }

    public static boolean doesValueExist( String val )
    {
        boolean valExist = false;

        if( ( val != null ) && !val.trim().equals( "" ) )
        {
            valExist = true;
        }

        return valExist;
    }

    public static boolean doesNumValueExist( Number val )
    {
        if( val == null )
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean doesDateValueExist( Date val )
    {
        if( val == null )
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Reformate data for Excell download to support special characters
     *
     * @param fieldValue Orginal field value using ASCII codes
     * @return filedValue using unicode
     */
    public static String updateDataForSpecialCharacters( String fieldValue )
    {
        if( fieldValue == null )
        {
            return fieldValue;
        }

        fieldValue = fieldValue.replace( "&#8322;", "\u2082" );  //Subscript 2
        fieldValue = fieldValue.replace( "&#945;", "\u03B1" ); // Alpha
        fieldValue = fieldValue.replace( "&#946;", "\u03B2" ); // Beta
        fieldValue = fieldValue.replace( "&#947;", "\u03B3" ); // Gamma
        fieldValue = fieldValue.replace( "&#948;", "\u03B4" ); // Delta
        fieldValue = fieldValue.replace( "&#178;", "\u00B2" ); // Superscript 2
        fieldValue = fieldValue.replace( "&#176;", "\u00B0" ); // Degree
        fieldValue = fieldValue.replace( "&#9702;", "\u00B0" ); // Degree
        fieldValue = fieldValue.replace( "&#181;", "\u00B5" ); // Micro
        fieldValue = fieldValue.replace( "&#955;", "\u03BB" ); // lambda
        fieldValue = fieldValue.replace( "&#411;", "\u03BB" ); // lambda
        fieldValue = fieldValue.replace( "&#8805;", "\u2265" ); // Greater than or equal to
        fieldValue = fieldValue.replace( "&#8804;", "\u2264" ); // Less than or equal to
        fieldValue = fieldValue.replace( "&#177;", "\u00B1" ); // Plus-Minus sign
        fieldValue = fieldValue.replace( "&#954;", "\u03BA" ); // Kappa Small
        fieldValue = fieldValue.replace( "&#8495;", "\u212F" ); // Small Exponent
        fieldValue = fieldValue.replace( "&#922;", "\u03BA" ); // Kappa Big

        return fieldValue;
    }

    /**
     * Reformate data for XML download to support special characters
     *
     * @param fieldValue Orginal field value using ASCII codes
     * @return filedValue using unicode
     */
    public static String updateXMLDataForSpecialCharacters( String fieldValue )
    {
        if( fieldValue == null )
        {
            return fieldValue;
        }

        fieldValue = fieldValue.replace( "&amp;#8322;", "\u2082" );  //Subscript 2
        fieldValue = fieldValue.replace( "&amp;#945;", "\u03B1" ); // Alpha
        fieldValue = fieldValue.replace( "&amp;#946;", "\u03B2" ); // Beta
        fieldValue = fieldValue.replace( "&amp;#947;", "\u03B3" ); // Gamma
        fieldValue = fieldValue.replace( "&amp;#948;", "\u03B4" ); // Delta
        fieldValue = fieldValue.replace( "&amp;#178;", "\u00B2" ); // Superscript 2
        fieldValue = fieldValue.replace( "&amp;#176;", "\u00B0" ); // Degree
        fieldValue = fieldValue.replace( "&amp;#9702;", "\u00B0" ); // Degree
        fieldValue = fieldValue.replace( "&amp;#181;", "\u00B5" ); // Micro
        fieldValue = fieldValue.replace( "&amp;#955;", "\u03BB" ); // lambda
        fieldValue = fieldValue.replace( "&amp;#411;", "\u03BB" ); // lambda
        fieldValue = fieldValue.replace( "&amp;#8805;", "\u2265" ); // Greater than or equal to
        fieldValue = fieldValue.replace( "&amp;#8804;", "\u2264" ); // Less than or equal to
        fieldValue = fieldValue.replace( "&amp;#177;", "\u00B1" ); // Plus-Minus sign
        fieldValue = fieldValue.replace( "&amp;#954;", "\u03BA" ); // Kappa Small
        fieldValue = fieldValue.replace( "&amp;#8495;", "\u212F" ); // Small Exponent
        fieldValue = fieldValue.replace( "&amp;#922;", "\u03BA" ); // Kappa Big
//      fieldValue = fieldValue.replace("&lt;", "<"); // less than
        fieldValue = fieldValue.replace( "&lt;", "&#60;" ); // less than
//      fieldValue = fieldValue.replace("&gt;", ">"); // greater than
        fieldValue = fieldValue.replace( "&gt;", "&#62;" ); // greater than
        fieldValue = fieldValue.replace( "&amp;#8225;", "\u2021" ); // double dagger


        // decimel value &#8225
        //double dagger is \U2021


        return fieldValue;
    }


    public static String stringArrayToString( String[] arr)
    {
        if( arr == null )
        {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for(String s : arr) {
            if( first )
            {
                first = false;
            }
            else
            {
                builder.append(", ");
            }
            builder.append(s);
        }
        return builder.toString();
    }

    public static String readFile(String path)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes( Paths.get( path ) );
        return new String(encoded, Charset.defaultCharset());
    }

    /**
     * Checks for escaped (with %) hexadecimal
     * @param args
     * @return
     */
    public static boolean checkForBadParameters( String... args)
    {
        for ( String arg : args )
        {
            // It's okay if it's null.
            if( arg != null)
            {
                if( arg.matches( ".*%[0-9a-fA-F]{2}.*" ) )
                {
                    return true;
                }
            }
        }
        return false;
    }


    public static String sanitizeForSql( String text)
    {
        String cleanString = text;

        // Anything escaped
        cleanString = cleanString.replaceAll( "\\\\.?", " " );

        // Possibly dangerous characters. or ASCII format hex
        // " ' | & ; $ @ % ) ( > < + 0x..
        cleanString = cleanString.replaceAll( "(?i)[\"\'\\|&;$@%)(><+]|0x..", " " );

        // Cleanup possible extra spaces
        cleanString = cleanString.replaceAll( " +", " " );
        return cleanString;
    }


	public static String[] buildArrayFromParameter(String paramValue) {
		if (paramValue == null) {
			return null;
		}

		return paramValue.split(":::");
	}
	public static String[] buildArrayFromParameter(String paramValue, String separator) {
		if (paramValue == null) {
			return null;
		}

		return paramValue.split(separator);
	}
	public static boolean containsKeyLoop(String[] arr, String targetValue) {
		if (arr == null)
			return false;
		for (String s: arr) {
			if ((s != null) && (s.equals(targetValue)))
				return true;
			else if ((s == null) && (targetValue == null))
				return true;
		}
		return false;
	}

    public static void main( String[] args )
    {
        String str = "\"test xxx\\Q\' |AAA & BBB; (CCC) + III <stuff> +  $ddd EEE$ %FFFF% ggg@HHHH zzz 0x0a 0X0a 0x0A JJJJJ \\Q' \"\\";
        String newStr = "\\\"";
        System.out.println( str );
        String result = StringUtilities.sanitizeForSql( str );
        System.out.println( result );
    }
}
