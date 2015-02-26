package gov.nih.nci.cadsr.service.search;

import java.util.Arrays;
import java.util.HashMap;

public class TempTestParameters
{
    private HashMap<String, String> searchParameters = new HashMap<String, String>();
    private HashMap<String, String> searchParameterList = new HashMap<String, String>();

    public TempTestParameters()
    {


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
        //searchParameters.put( "jspCdeId", "jspCdeId" );

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


        return null;
    }

    //FIXME - a hasty hack!
    public void setJspCdeId( String id)
    {
        searchParameters.put( "jspCdeId", id );
    }
    public static void main( String a[] )
    {
        TempTestParameters fakeHttpServletRequest = new TempTestParameters();
        System.out.println( fakeHttpServletRequest.getParameter( "jspCdeId" ) );
        String[] vals = fakeHttpServletRequest.getParameterValues( "altName" );
        System.out.println( Arrays.toString( vals ) );


    }

}
