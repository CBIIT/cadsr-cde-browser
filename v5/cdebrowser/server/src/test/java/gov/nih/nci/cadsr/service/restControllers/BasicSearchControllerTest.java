package gov.nih.nci.cadsr.service.restControllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gov.nih.nci.cadsr.common.util.DBUtil;
import gov.nih.nci.cadsr.dao.model.BasicSearchModel;
import gov.nih.nci.cadsr.dao.model.CsCsiModel;
import gov.nih.nci.cadsr.dao.model.ProgramAreaModel;
import gov.nih.nci.cadsr.service.UnitTestCommon;
import gov.nih.nci.cadsr.service.model.search.BasicSearchNode;
import junit.framework.TestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the BasicSearchController.
 *
 * The BasicSearchController contains the rest service/entry point
 */

public class BasicSearchControllerTest extends TestCase
{
    BasicSearchController basicSearchController;
    private BasicSearchNode[] searchNodes;
    private List<ProgramAreaModel> programAreaModelList;

    public void setUp()
    {
        UnitTestCommon unitTestCommon;
        unitTestCommon = new UnitTestCommon();
        basicSearchController = new BasicSearchController();
        List<BasicSearchModel> sampleRawQueryResults = initSampleSearchResults();
        searchNodes = basicSearchController.buildSearchResultsNodes( sampleRawQueryResults );
        programAreaModelList = unitTestCommon.initSampleProgramAreas();
        basicSearchController.setProgramAreaModelList( programAreaModelList );
    }


    public void testProgramAreaPalNameLookUp0()
    {
        // Zero is Program Area "All" which should return an empty (not null) String
        assertEquals( "",  basicSearchController.getProgramAreaPalNameByIndex( 0 ) );
    }

    public void testProgramAreaPalNameLookUp1()
    {
        // One should return the first ProgramArea name, they start at one rather than zero because the client will us zero to indicate all.
        assertEquals( "CancerCenters",  basicSearchController.getProgramAreaPalNameByIndex( 1 ) );
        //Last one
        assertEquals( "UNASSIGNED",  basicSearchController.getProgramAreaPalNameByIndex( programAreaModelList.size() ) );
    }

    public void testProgramAreaPalNameLookUp2()
    {
        // If index is too high, should warn, and return empty String (All)
        assertEquals( "",  basicSearchController.getProgramAreaPalNameByIndex( programAreaModelList.size() + 1 ) );
    }


    /**
     * searchNodes is sample query data generated with:
     * cdebrowserServer/basicSearchWithProgramAreaTest?query=diastolic&field=0&queryType=1&programArea=2
     */
    public void testBuildSearchResultsNodes0()
    {
        assertEquals( "Results node count ", 14, searchNodes.length );
    }

    public void testBuildSearchResultsNodes1()
    {
        String longName[] = { "Common Toxicity Criteria Adverse Event Left Ventricular Diastolic Dysfunction Grade", "Hypertension Treatment Received Ind-2", "Assessment Diastolic Blood Pressure java.lang.Integer", "Diastolic Blood Pressure Value", "Alpha DVG Blood Pressure, Diastolic", "Person Peak Diastolic Blood Pressure Measurement Value", "Blood Pressure, Diastolic", "Diastolic Blood Pressure Evaluation Measurement", "Uncontrolled Hypertension Present Ind-3", "Diastolic Blood Pressure Standing Evaluation Measurement", "Patient Inadequate Control Hypertension Exclusion Criteria Ind-2", "Diastolic Blood Pressure Supine Position Evaluation Measurement", "Person Resting Diastolic Blood Pressure Assessment Value", "Person Blood Pressure Assessment Value" };
        for( int f = 0; f < searchNodes.length; f++ )
        {
            assertEquals( longName[f], searchNodes[f].getLongName() );
        }
    }

    public void testBuildSearchResultsNodes2()
    {
        String deIdseq[] = { "C4C5E899-8698-1624-E034-0003BA12F5E7", "09046428-4D37-6C1D-E044-0003BA3F9857", "4DDEFDE6-8587-59AC-E044-0003BA3F9857", "A97329E8-B6FC-2CAB-E034-0003BA12F5E7", "FAA03445-BB9E-0301-E034-0003BA3F9857", "D07CA6C0-6AB4-811C-E040-BB89AD4307F7", "D751EFE2-20DE-1EAF-E034-0003BA12F5E7", "E7D299AF-BEC8-18C6-E034-0003BA3F9857", "DE3C51AF-10E7-3D4B-E034-0003BA12F5E7", "7FBEB13B-F595-A014-E040-BB89AD437F6F", "9299B94C-40D9-BA08-E040-BB89AD430CAF", "7FBEB13B-F552-A014-E040-BB89AD437F6F", "D0743C26-E6F1-A341-E040-BB89AD432778", "C5ABD214-14E1-39DC-E040-BB89AD4338BE" };
        for( int f = 0; f < searchNodes.length; f++ )
        {
            assertEquals( deIdseq[f], searchNodes[f].getDeIdseq() );
        }
    }

    public void testBuildSearchResultsNodes3()
    {
        String preferredQuestionText[] = { "CTC Adverse Event Left ventricular diastolic dysfunction Grade", "Is patient on anti-hypertensive therapy", null, "Diastolic BP", "Alpha DVG Blood Pressure, Diastolic", "Exercise Peak diastolic blood pressure", "Blood Pressure, Diastolic", "Blood pressure, Diastolic", "Does the patient have uncontrolled hypertension?", "Blood pressure, Standing at 1 minute, Diastolic", "Inadequately controlled hypertension (systolic blood pressure of >150 mmHg or diastolic pressure >100 mmHg on anti-hypertensive medication)", "Blood pressure, Supine, Diastolic", "Resting Diastolic BP", "What it is the combined diastolic and systolic blood pressure measurements?" };
        for( int f = 0; f < searchNodes.length; f++ )
        {
            assertEquals( preferredQuestionText[f], searchNodes[f].getPreferredQuestionText() );
        }
    }

    public void testBuildSearchResultsNodes5()
    {
        String ownedBy[] = { "CTEP", "CTEP", "NCIP", "CCR", "DCP", "NCIP", "DCP", "CTEP", "CTEP", "CTEP", "CTEP", "CTEP", "NCIP", "NCIP" };
        for( int f = 0; f < searchNodes.length; f++ )
        {
            assertEquals( ownedBy[f], searchNodes[f].getOwnedBy() );
        }
    }

    public void testBuildSearchResultsNodes6()
    {
        int publicId[] = { 2005470, 2442229, 2752828, 2004291, 2183222, 3639077, 2182885, 2002242, 2001887, 3010824, 3151348, 3010819, 3638868, 3534951 };
        for( int f = 0; f < searchNodes.length; f++ )
        {
            assertTrue( publicId[f] == searchNodes[f].getPublicId() );
        }
    }

    public void testBuildSearchResultsNodes7()
    {
        String registrationStatus[] = { "Qualified", "Qualified", null, "Standard", "Qualified", null, "Qualified", "Qualified", "Qualified", "Qualified", "Qualified", "Qualified", null, null };
        for( int f = 0; f < searchNodes.length; f++ )
        {
            assertEquals( registrationStatus[f], searchNodes[f].getRegistrationStatus() );
        }
    }

    public void testBuildSearchResultsNodes8()
    {
        String usedByContext[] = { null, "COG, CTEP", null, "AECC, CITN, CTEP, DCP, LCC, NCIP, NICHD, NINDS, SPOREs", null, null, null, null, "NRG", null, null, null, "LCC", null };
        for( int f = 0; f < searchNodes.length; f++ )
        {
            assertEquals( usedByContext[f], searchNodes[f].getUsedByContext() );
        }
    }

    public void testBuildSearchResultsNodes9()
    {
        String version[] = { "4", "1", "1", "1", "2", "1", "1", "4", "4", "1", "1", "1", "1", "1" };
        for( int f = 0; f < searchNodes.length; f++ )
        {
            assertEquals( version[f], searchNodes[f].getVersion() );
        }
    }

    public void testBuildSearchResultsNodes10()
    {
        String workflowStatus[] = { "RELEASED", "RELEASED", "RELEASED", "RELEASED", "RELEASED", "RELEASED", "RELEASED", "RELEASED", "RELEASED", "RELEASED", "RELEASED", "RELEASED", "RELEASED", "RELEASED" };
        for( int f = 0; f < searchNodes.length; f++ )
        {
            assertEquals( workflowStatus[f], searchNodes[f].getWorkflowStatus() );
        }
    }

    /**
     * Initialize sample search results.
     *
     * Raw query data generated with:
     * cdebrowserServer/basicSearchWithProgramAreaTest?query=diastolic&field=0&queryType=1&programArea=2
     */
    private ArrayList<BasicSearchModel> initSampleSearchResults()
    {
        Gson gson = new GsonBuilder().create();
        String json = null;
        try
        {
            json = DBUtil.readFile( "src/test/java/gov/nih/nci/cadsr/service/restControllers/basicSearchModelTest1.data" );
        }
        catch( IOException e )
        {
            assertTrue( e.getMessage(), false );
        }
        return gson.fromJson( json, new TypeToken<ArrayList<BasicSearchModel>>()
        {
        }.getType() );
    }

}
