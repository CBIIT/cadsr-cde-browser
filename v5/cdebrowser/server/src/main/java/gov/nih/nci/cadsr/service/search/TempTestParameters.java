package gov.nih.nci.cadsr.service.search;

import java.util.Arrays;
import java.util.HashMap;

public class TempTestParameters
{
    private HashMap<String, String> searchParameters = new HashMap<String, String>();
    private HashMap<String, String> searchParameterList = new HashMap<String, String>();

    public TempTestParameters()
    {


        /*
        STDOUT] MHL treeParamType: null
2015-02-16 11:02:26,686 INFO  [STDOUT]  strArray:
2015-02-16 11:02:26,686 INFO  [STDOUT] 1
2015-02-16 11:02:26,686 INFO  [STDOUT] 1
2015-02-16 11:02:26,686 INFO  [STDOUT]  txtValueDomain: null
2015-02-16 11:02:26,686 INFO  [STDOUT]  txtDataElementConcept: null
2015-02-16 11:02:26,686 INFO  [STDOUT]  txtClassSchemeItem: null
2015-02-16 11:02:26,686 INFO  [STDOUT]  contextUse: both


MHL treeParamType: null
2015-02-16 11:47:22,977 INFO  [STDOUT]  strArray: "SEARCH"
2015-02-16 11:47:22,977 INFO  [STDOUT] 1
2015-02-16 11:47:22,977 INFO  [STDOUT] 1
2015-02-16 11:47:22,977 INFO  [STDOUT]  txtValueDomain: null
2015-02-16 11:47:22,977 INFO  [STDOUT]  txtDataElementConcept: null
2015-02-16 11:47:22,977 INFO  [STDOUT]  txtClassSchemeItem: null
2015-02-16 11:47:22,977 INFO  [STDOUT]  contextUse: both
2015-02-16 11:47:22,977 INFO  [STDOUT] jspKeyword: tissue
2015-02-16 11:47:22,977 INFO  [STDOUT] jspStatus: [ALL]
2015-02-16 11:47:22,977 INFO  [STDOUT] regStatus: [ALL]
2015-02-16 11:47:22,977 INFO  [STDOUT] altName: null
2015-02-16 11:47:22,977 INFO  [STDOUT] jspSearchIn: [ALL]
2015-02-16 11:47:22,977 INFO  [STDOUT] jspValidValue:
2015-02-16 11:47:22,977 INFO  [STDOUT] jspObjectClass:
2015-02-16 11:47:22,977 INFO  [STDOUT] jspProperty:
2015-02-16 11:47:22,977 INFO  [STDOUT] jspValueDomain:
2015-02-16 11:47:22,977 INFO  [STDOUT] jspCdeId:
2015-02-16 11:47:22,977 INFO  [STDOUT] jspDataElementConcept:
2015-02-16 11:47:22,977 INFO  [STDOUT] jspClassification:
2015-02-16 11:47:22,977 INFO  [STDOUT] jspLatestVersion:
2015-02-16 11:47:22,977 INFO  [STDOUT] jspAltName:
2015-02-16 11:47:22,977 INFO  [STDOUT] jspConceptName:
2015-02-16 11:47:22,977 INFO  [STDOUT] jspConceptCode:
2015-02-16 11:47:22,977 INFO  [STDOUT] jspVDType:
2015-02-16 11:47:22,977 INFO  [STDOUT] jspCDEType:

         */


        //searchParameters.put( "jspKeyword", "tissue" );

        // upper (pv.value) LIKE upper ('jspValidValue') )
        // Empty searchParameters.put( "jspValidValue", "jspValidValue" );

        // upper(oc.long_name) LIKE upper('jspObjectClass')
        // Empty searchParameters.put( "jspObjectClass", "jspObjectClass" );

        // upper(pc.long_name) LIKE upper('jspProperty')
        // Empty searchParameters.put( "jspProperty", "jspProperty" );

        // vd.vd_idseq = 'jspValueDomain'
        // Empty searchParameters.put( "jspValueDomain", "jspValueDomain" );

        // to_char(de.cde_id) LIKE 'jspCdeId'
        // Empty searchParameters.put( "jspCdeId", "jspCdeId" );

        // dec.dec_idseq = 'jspDataElementConcept'
        // Empty searchParameters.put( "jspDataElementConcept", "jspDataElementConcept" );

        // acs.cs_csi_idseq = 'jspClassification'
        // Empty searchParameters.put( "jspClassification", "jspClassification" );

        //This may turn on Basic search?
        // Empty searchParameters.put( "jspLatestVersion", "no" );

        // upper (nvl(dsn.NAME, '%')) LIKE upper ('jspAltName') )
        // Empty searchParameters.put( "jspAltName", "jspAltName" );

        // WHERE  upper(long_name) LIKE upper('jspConceptName')
        // Empty searchParameters.put( "jspConceptName", "jspConceptName" );

        // upper(preferred_name) LIKE upper('jspConceptCode')
        // Empty searchParameters.put( "jspConceptCode", "jspConceptCode" );

        // Empty searchParameters.put( "jspVDType", "jspVDType" );
        // Empty searchParameters.put( "jspCDEType", "jspCDEType" );

        searchParameters.put( "contextUse", "both" );

        /***************************************************/
        searchParameterList.put( "jspStatus", "ALL" );
        searchParameterList.put( "regStatus", "ALL" );
        // null searchParameterList.put("altName", "altName");

    }

    public String getParameter( String key )
    {
        if( !searchParameters.containsKey( key ) )
        {
            //System.out.println( "KEY NOT FOUND: " + key );
            //return ( "KEY NOT FOUND" );
            return null;
        }
        return searchParameters.get( key );
    }

    public String getParameterListValue( String key )
    {
        if( !searchParameterList.containsKey( key ) )
        {
            return ( "ListValue KEY NOT FOUND" );
        }
        return searchParameterList.get( key );
    }

    /*
    de.asl_name
    acr.registration_status
    dsn.detl_name
     */
    public String[] getParameterValues( String key )
    {
        String[] values = new String[20];
        if( key.compareTo( "jspStatus" ) == 0 )
        {
            String[] jspStatus = { "ALL" };
            return jspStatus;
        }
        else if( key.compareTo( "regStatus" ) == 0 )
        {
            String[] regStatus = { "ALL" };
            return regStatus;
        }
        else if( key.compareTo( "jspSearchIn" ) == 0 )
        {
            String[] jspSearchIn = { "ALL" };
            return jspSearchIn;
        }
        else if( key.compareTo( "SEARCH" ) == 0 )
        {
            String[] SEARCH = { "1", "1" };
            return SEARCH;
        }


        return null;
    }

    public static void main( String a[] )
    {
        TempTestParameters fakeHttpServletRequest = new TempTestParameters();
        System.out.println( fakeHttpServletRequest.getParameter( "jspCdeId" ) );
        String[] vals = fakeHttpServletRequest.getParameterValues( "altName" );
        System.out.println( Arrays.toString( vals ) );


    }

}
