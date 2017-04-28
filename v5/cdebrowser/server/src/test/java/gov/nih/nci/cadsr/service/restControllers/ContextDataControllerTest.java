package gov.nih.nci.cadsr.service.restControllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import gov.nih.nci.cadsr.common.AppConfig;
import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.common.util.DBUtil;
import gov.nih.nci.cadsr.dao.model.*;
import gov.nih.nci.cadsr.common.util.UnitTestCommon;
import gov.nih.nci.cadsr.service.model.context.*;
import junit.framework.TestCase;

import java.io.IOException;
import java.util.*;

import org.mockito.Mockito;
import static org.mockito.Mockito.*;
public class ContextDataControllerTest extends TestCase
{
    private ContextDataController contextDataController;

    private List<CsCsiModel> csCsiNodelList = null;
    private ProtocolFormModel protocolFormModel = null;
    private ProtocolModel protocolModel = null;
    private UnitTestCommon unitTestCommon = null;
    private int programArea = 1;
    private String protoIdseq = "X3";
    private String testPreferredDefinition;
    private String testLongName;
    private String testHoverText;
    private int testMaxHoverTextLen;
    RestControllerCommon mockRestControllerCommon = Mockito.mock(RestControllerCommon.class);
    
    public void setUp()
    {
        unitTestCommon = new UnitTestCommon();
        List<ProgramAreaModel> programAreaModelList = new ArrayList<ProgramAreaModel> ();
        programAreaModelList.add(new ProgramAreaModel());
        Mockito.when(mockRestControllerCommon.getProgramAreaList()).thenReturn(programAreaModelList);
        contextDataController = new ContextDataController(mockRestControllerCommon);
        contextDataController.setAppConfig(new AppConfig());
        // TODO set up Spring test @Configuration class with PropertyPlaceholderConfigurer so
        // Spring can resolve the @Value("${maxHoverTextLen}") etc. during Unit tests
        contextDataController.getAppConfig().setMaxHoverTextLenStr( "50" );
        testMaxHoverTextLen = Integer.parseInt( contextDataController.getAppConfig().getMaxHoverTextLenStr() );
        contextDataController.setProgramAreaModelList( unitTestCommon.initSampleProgramAreas() );
        initTestPreferredDefinition();
    }
    protected void tearDown() throws Exception {
    	Mockito.reset(mockRestControllerCommon);
    }

    public void testAddBreadCrumbs()
    {
        // Initialize tree with test data.
        BaseNode nodeTree = unitTestCommon.initContextTree();
        contextDataController.addBreadCrumbs( nodeTree, null );

        // This array matches the data used by unitTestCommon.initContextTree() above
        ArrayList<String> tree = new ArrayList<>();
        tree.add( "Alpha" );
        tree.add( "Bravo" );
        tree.add( "Charlie" );
        tree.add( "Delta" );
        tree.add( "Echo" );
        tree.add( "Foxtrot" );
        tree.add( "Golf" );
        tree.add( "Hotel" );

        // Check top node to be sure it was set to null in the initial call to this recursive function.
        assertEquals( tree.get( 0 ), nodeTree.getTreePath().get( 0 ) );
        // Check the children
        while( nodeTree.isIsParent() )
        {
            nodeTree = nodeTree.getChildren().get( 0 );
            List<String> nodeTreePath = nodeTree.getTreePath();
            for( int f = 0; f < nodeTreePath.size(); f++ )
            {
                assertEquals( tree.get( f ), nodeTreePath.get( f ) );
            }
        }
    }

    public void testAddBreadCrumbsAll0()
    {
        BaseNode contextNodeTree = unitTestCommon.initContextTree();
        // Sets first node in recursive call to "All", would have used the nodes text field if 2nd parameter was null.
        contextDataController.addBreadCrumbsAll( contextNodeTree, CaDSRConstants.ALL_CONTEXTS_STRING );

        // This array matches the data used by unitTestCommon.initContextTree() above
        ArrayList<String> tree = new ArrayList<>();
        tree.add( CaDSRConstants.ALL_CONTEXTS_STRING ); //First "Crumb" should always be all
        tree.add( "Bravo" );
        tree.add( "Charlie" );
        tree.add( "Delta" );
        tree.add( "Echo" );
        tree.add( "Foxtrot" );
        tree.add( "Golf" );
        tree.add( "Hotel" );

        //Check the top level
        assertEquals( tree.get( 0 ), contextNodeTree.getTreePath().get( 0 ) );
        //Check the children
        while( contextNodeTree.isIsParent() )
        {
            contextNodeTree = contextNodeTree.getChildren().get( 0 );
            List<String> nodeTreePath = contextNodeTree.getTreePath();
            for( int f = 0; f < nodeTreePath.size(); f++ )
            {
                assertEquals( tree.get( f ), nodeTreePath.get( f ) );
            }
        }
    }

    public void testAddBreadCrumbsAll1()
    {
        BaseNode contextNodeTree = unitTestCommon.initContextTree();
        // Sets first node in recursive call to "Abcd1234", would have used the nodes text field if 2nd parameter was null.
        contextDataController.addBreadCrumbsAll( contextNodeTree, "Abcd1234" );

        // This array matches the data used by unitTestCommon.initContextTree() above
        ArrayList<String> tree = new ArrayList<>();
        tree.add( "Abcd1234" ); //First "Crumb" should always be all
        tree.add( "Bravo" );
        tree.add( "Charlie" );
        tree.add( "Delta" );
        tree.add( "Echo" );
        tree.add( "Foxtrot" );
        tree.add( "Golf" );
        tree.add( "Hotel" );

        //Check the top level
        assertEquals( tree.get( 0 ), contextNodeTree.getTreePath().get( 0 ) );
        //Check the children
        while( contextNodeTree.isIsParent() )
        {
            contextNodeTree = contextNodeTree.getChildren().get( 0 );
            List<String> nodeTreePath = contextNodeTree.getTreePath();
            for( int f = 0; f < nodeTreePath.size(); f++ )
            {
                assertEquals( tree.get( f ), nodeTreePath.get( f ) );
            }
        }
    }


    // Update the Program Area for rest call url included int the top level "Classifications" and "Protocol Forms"
    // in the href and the nodes program area.
    public void testSetHrefProgramArea()
    {
        BaseNode contextNodeTree = unitTestCommon.initContextTree();
        int local_testProgramArea = 5; //5Matches results of initContextTree()
        //Set a bogus program area, so we can check that has ben changed
        contextNodeTree.getChildren().get( 0 ).setProgramArea( 66 );
        int origProgramArea = contextNodeTree.getChildren().get( 0 ).getProgramArea();
        contextDataController.setHrefProgramArea( contextNodeTree, programArea );

        //Make sure program area changed
        assertFalse( "Program area should have changed, and no longer == " + origProgramArea, origProgramArea == contextNodeTree.getChildren().get( 0 ).getProgramArea() );

        //Make sure it changed to the correct program area
        assertEquals( programArea, contextNodeTree.getChildren().get( 0 ).getProgramArea() );

        //Make sure the program area has been set in the rest call string given to the client
        String restCall = contextNodeTree.getHref();
        assertTrue( restCall.contains( "&programArea=" + local_testProgramArea ) );
    }


    // Test the initializing of a new ProtocolForms node - the parent ProtocolForms folder within each Context
    public void testInitProtocolsFormsParentNode()
    {
        boolean collapsed = true;
        String text = "ProtocolForms";
        ParentNode protocolsParentNode = new ParentNode();
        contextDataController.initProtocolsFormsParentNode( protocolsParentNode, programArea, collapsed );

        assertEquals( text, protocolsParentNode.getText() );
        assertEquals( programArea, protocolsParentNode.getProgramArea() );
        assertEquals( CaDSRConstants.FOLDER, protocolsParentNode.getType() );
        assertEquals( CaDSRConstants.PROTOCOL_FORMS_FOLDER, protocolsParentNode.getChildType() );
        assertFalse( "Should not be set as parent.", protocolsParentNode.isIsParent() );
        assertNotNull( protocolsParentNode.getChildren() );
    }

    // Test the initializing of a new Classifications node - the parent ProtocolForms folder within each Context
    public void testInitClassificationsParentNode()
    {
        boolean collapsed = true;
        String text = "Classifications";
        ParentNode classificationsParentNode = new ParentNode();
        contextDataController.initClassificationsParentNode( classificationsParentNode, programArea, collapsed );//ParentNode, programArea, isCollapsed

        assertEquals( text, classificationsParentNode.getText() );
        assertEquals( programArea, classificationsParentNode.getProgramArea() );
        assertEquals( CaDSRConstants.FOLDER, classificationsParentNode.getType() );
        assertEquals( CaDSRConstants.FOLDER, classificationsParentNode.getChildType() );
        assertFalse( "Should not be set as parent.", classificationsParentNode.isIsParent() );
        assertNotNull( classificationsParentNode.getChildren() );
    }

    // Place holders are the children of the Classifications and Protocol Forms, before they are populated with the real data tree
    // Their purpose is to make sure their parent folder knows if it has children, so it can show the correct icon (clickable folder, or greyed out disabled)
    public void testInsertPlaceHolderNode()
    {
        ParentNode parentNode = new ParentNode();
        contextDataController.insertPlaceHolderNode( parentNode );

        System.out.println();
        //Get the place holder node, it will be the first and only child.
        ParentNode placeHolderNode = (ParentNode) parentNode.getChildren().get( 0 );

        assertTrue( "Place holder node should be collapsed.", placeHolderNode.isCollapsed() );
        assertTrue( "Place holder node should be set as a Parent.", placeHolderNode.isIsParent() );
    }

    // Test initProtocolModels
    // Populates each Protocol (within the paren ProtocolForms folder) with it's Protocol forms and
    // add to the Parent Protocol node (the "Protocol Forms" folder)
    public void testInitProtocolModels()
    {
        // List of Protocols for one Context.
        List<ProtocolModel> protocolModelList = new ArrayList<>();
        // Create/init a Protocol Model.
        initProtocolModel();
        // Add this Protocol Model to the list.
        protocolModelList.add( protocolModel );

        // List of all the Protocol Forms for this one Context.
        List<ProtocolFormModel> protocolFormModelList = new ArrayList<>();
        // Create/init a Protocol Form Model.
        initProtocolFormModel();
        // Add this Protocol Form to the list.
        protocolFormModelList.add( protocolFormModel );

        // Create a Parent node to add the Protocol Model list to.
        ParentNode protocolsParentNode = new ParentNode();
        protocolsParentNode.setIsParent( true );
        contextDataController.initProtocolModels( protocolModelList, protocolFormModelList, protocolsParentNode, programArea );

        //Does the parent have the Protocol node
        BaseNode testProtocolModel = protocolsParentNode.getChildren().get( 0 );
        assertNotNull( testProtocolModel );

        //Does the Protocol node have the Protocol Form
        BaseNode testProtocolFormModel = testProtocolModel.getChildren().get( 0 );
        assertNotNull( testProtocolFormModel );

        //Does the Protocol Form have the init test values
        assertEquals( testHoverText, testProtocolFormModel.getHover() );
        assertEquals( testLongName, testProtocolFormModel.getText() );


    }

    // The top level nodes should have only the Program areas
    // initTopLevelContextNodes will be the first call when constructing the Context nodes
    public void testInitTopLevelContextNodes()
    {
        String name[] = { CaDSRConstants.ALL_CONTEXTS_STRING, "CancerCenters", "NCI", "NCIConsortium", "NCTN/eNCTN", "NIHInstitutes", "SDOs", "UNASSIGNED" };
        String description[] = { "", "CancerCenters/CancerInstitutes", "NCIDivisions/Centers/Programs", "NCIConsortrium", "NCIClinicalTrialsNetwork", "NIHInstitutes", "StandardsDevelopmentOrganizations", "null" };

        ContextNode[] contextNode = contextDataController.initTopLevelContextNodes();
        //Make sure "All Contexts" was add to front of the array
        assertEquals( CaDSRConstants.ALL_CONTEXTS_STRING, contextNode[0].getText() );

        //Test data has 7 program areas, with "All" is 8
        assertEquals( 8, contextNode.length );

        //Did they all get populated correctly
        for( int f = 0; f < contextNode.length; f++ )
        {
            assertEquals( name[f], contextNode[f].getText() );
            assertEquals( description[f], contextNode[f].getPalNameDescription() );
            assertEquals(f, contextNode[f].getProgramArea());
        }
    }
    
    //tests of getProgramAreaByName
    
    public void testFindProgramAreaByNameSuccess() {
    	int paIndex = 0;
    	ProgramAreaModel expected = contextDataController.getProgramAreaModelList().get(paIndex);
    	int receivedNumber = contextDataController.findProgramAreaByName(expected.getPalName());
    	assertEquals(paIndex+1, receivedNumber);
    }
    
    public void testFindProgramAreaByNameNotExisted() {
    	int paIndex = 0;
    	int receivedNumber = contextDataController.findProgramAreaByName("test Non Existed");
    	assertEquals(paIndex, receivedNumber);
    }
    
    public void testGetProgramAreaByName()
    {
    	int paIndex = 0;
    	ProgramAreaModel expected = contextDataController.getProgramAreaModelList().get(paIndex);
    	int receivedNumber = contextDataController.getProgramAreaByName(expected.getPalName());
    	assertEquals(paIndex+1, receivedNumber);
    }
    
    public void testGetProgramAreaByNameNotExisted() {
    	int paIndexExpected = 0;
    	reset(mockRestControllerCommon);
    	List<ProgramAreaModel> emptyPaList = new ArrayList<ProgramAreaModel>();
    	when(mockRestControllerCommon.getProgramAreaList()).thenReturn(emptyPaList);
    	int receivedNumber = contextDataController.getProgramAreaByName("test Non Existed");
    	assertEquals(paIndexExpected, receivedNumber);
    	verify(mockRestControllerCommon, times(1)).getProgramAreaList();
    }

    public void testGetProgramAreaByNameError() {
    	reset(mockRestControllerCommon);
    	when(mockRestControllerCommon.getProgramAreaList()).thenThrow(new RuntimeException("test Exception"));
    	try {
    		contextDataController.getProgramAreaByName("test Non Existed");
    	}
    	catch(RuntimeException e) {
    		assertEquals("test Exception", e.getMessage());
    	}
    	verify(mockRestControllerCommon, times(1)).getProgramAreaList();
    }
    public void testGetProgramAreaByNameReload() {
    	int paIndexExpected = 1;
    	reset(mockRestControllerCommon);
    	String newProgramArea = "test Non Existed";
    	List<ProgramAreaModel> newPaList = new ArrayList<ProgramAreaModel>();
    	ProgramAreaModel programAreaModel = new ProgramAreaModel();
    	programAreaModel.setPalName(newProgramArea);
    	newPaList.add(programAreaModel);
    	when(mockRestControllerCommon.getProgramAreaList()).thenReturn(newPaList);
    	int receivedNumber = contextDataController.getProgramAreaByName(newProgramArea);
    	assertEquals(paIndexExpected, receivedNumber);
    	verify(mockRestControllerCommon, times(1)).getProgramAreaList();
    }
    
    // Test - ContextDataController.getCsCsisByParentCsCsi

    // Create three children, two match the parent ID, one does not, make sure we find two
    public void testGetCsCsisByParentCsCsi0()
    {
        String parentIdSeq = "F7BA6033-BAEA-C5EF-E040-BB89AD437201";
        initCsCsisNodeList();

        List<CsCsiModel> csCsiChildModelList = contextDataController.getCsCsisByParentCsCsi( parentIdSeq, csCsiNodelList);
        for( CsCsiModel csCsiModel : csCsiChildModelList )
        {
            if( ( csCsiModel.getCsLongName().compareTo( "CRF CDEs" ) != 0 ) && ( csCsiModel.getCsLongName().compareTo( "MDR" ) != 0 ) )
            {
                assertFalse( "Did not find child Classification folder: \"" + csCsiModel.getCsLongName() + "\"  .", true );
            }
        }
        assertTrue( true );
    }

    // Create three children, two match the parent ID, one does not, make sure we don't match the non-child
    public void testGetCsCsisByParentCsCsi1()
    {
        String parentIdSeq = "F7BA6033-BAEA-C5EF-E040-BB89AD437201";
        initCsCsisNodeList();

        List<CsCsiModel> csCsiChildModelList = contextDataController.getCsCsisByParentCsCsi( parentIdSeq, csCsiNodelList );
        for( CsCsiModel csCsiModel : csCsiChildModelList )
        {
            if( csCsiModel.getCsLongName().compareTo( "Not a child" ) == 0 )
            {
                assertFalse( "Matched child Classification folder: \"" + csCsiModel.getCsLongName() + "\", SHOULD NOT HAVE", true );
            }
        }
        assertTrue( true );
    }

    /* ************************************************************
         Test - ContextDataController.addChildrenToCsi
    ************************************************************ */
    public void testAddChildrenToCsi0()
    {
        initCsCsisNodeList();

        ClassificationItemNode classificationItemNodeParent = new ClassificationItemNode();
        classificationItemNodeParent.setIdSeq( "F7BA6033-BAEA-C5EF-E040-BB89AD437201" );
        contextDataController.addChildrenToCsi( classificationItemNodeParent, csCsiNodelList );

        //Look at the parent, do we have two children?
        assertEquals( 2, classificationItemNodeParent.getChildren().size() );
        for( BaseNode child : classificationItemNodeParent.getChildren() )
        {
            if( ( child.getText().compareTo( "CRF CDEs" ) != 0 ) &&
                    ( child.getText().compareTo( "MDR" ) != 0 ) )
            {
                assertFalse( "Did not successfully add child to ClassificationItemNode.", true );
            }
        }
        assertTrue( true );
    }


    // ContextDataController.initProtocolNode
    private void initTestPreferredDefinition()
    {
        testLongName = "Test long name";
        // This will test the truncating of very long Descriptions when they are used for hover text
        testPreferredDefinition = "Test preferred preferred definition" + new String( new char[testMaxHoverTextLen] ).replace( '\0', 'X' );
        testHoverText = testPreferredDefinition.substring( 0, testMaxHoverTextLen ) + "...";

    }

    private void initProtocolModel()
    {
        //Set the test ProtocolModel with only fields needed for initializing a ProtocolNode.
        protocolModel = new ProtocolModel();
        protocolModel.setLongName( testLongName );
        protocolModel.setPreferredDefinition( testPreferredDefinition );
        protocolModel.setProtoIdseq( protoIdseq );
    }

    //FIXME - add a good description
    public void testInitProtocolNode0()
    {
        initProtocolModel();
        ProtocolNode protocolNode = contextDataController.initProtocolNode( protocolModel, programArea );
        assertEquals( testLongName, protocolNode.getText() );
    }

    public void testInitProtocolNode1()
    {
        initProtocolModel();
        ProtocolNode protocolNode = contextDataController.initProtocolNode( protocolModel, programArea );
        assertEquals( testHoverText, protocolNode.getHover() );
    }

    public void testInitProtocolNode2()
    {
        initProtocolModel();
        ProtocolNode protocolNode = contextDataController.initProtocolNode( protocolModel, programArea );
        assertEquals( programArea, protocolNode.getProgramArea() );
    }

    public void testInitProtocolNode3()
    {
        initProtocolModel();
        ProtocolNode protocolNode = contextDataController.initProtocolNode( protocolModel, programArea );
        assertEquals( CaDSRConstants.PROTOCOL_FORMS_FOLDER, protocolNode.getType() );
    }

    public void testInitProtocolNode4()
    {
        initProtocolModel();
        ProtocolNode protocolNode = contextDataController.initProtocolNode( protocolModel, programArea );
        assertEquals( CaDSRConstants.PROTOCOL, protocolNode.getChildType() );
    }

    public void testInitProtocolNode5()
    {
        initProtocolModel();
        ProtocolNode protocolNode = contextDataController.initProtocolNode( protocolModel, programArea );
        assertTrue( "This ProtocolNode should be collapsed.", protocolNode.isCollapsed() );
    }

    public void testInitProtocolNode6()
    {
        initProtocolModel();
        ProtocolNode protocolNode = contextDataController.initProtocolNode( protocolModel, programArea );
        assertFalse( "This ProtocolNode should not be set as a Parent.", protocolNode.isIsParent() );
    }


    // Test - ContextDataController.initProtocolFormNode
    public void testInitProtocolFormNode0()
    {
        initProtocolFormModel();
        ProtocolFormNode protocolFormNode = contextDataController.initProtocolFormNode( protocolFormModel, programArea );
        assertEquals( testLongName, protocolFormNode.getText() );
    }

    public void testInitProtocolFormNode1()
    {
        initProtocolFormModel();
        ProtocolFormNode protocolFormNode = contextDataController.initProtocolFormNode( protocolFormModel, programArea );
        assertEquals( testHoverText, protocolFormNode.getHover() );
    }

    public void testInitProtocolFormNode2()
    {
        initProtocolFormModel();
        ProtocolFormNode protocolFormNode = contextDataController.initProtocolFormNode( protocolFormModel, programArea );
        assertEquals( CaDSRConstants.PROTOCOL, protocolFormNode.getType() );
    }

    public void testInitProtocolFormNode3()
    {
        initProtocolFormModel();
        ProtocolFormNode protocolFormNode = contextDataController.initProtocolFormNode( protocolFormModel, programArea );
        assertEquals( CaDSRConstants.EMPTY, protocolFormNode.getChildType() );
    }


    public void testInitProtocolFormNode4()
    {
        initProtocolFormModel();
        ProtocolFormNode protocolFormNode = contextDataController.initProtocolFormNode( protocolFormModel, programArea );
        assertFalse( protocolFormNode.isIsChild() );
    }

    public void testInitProtocolFormNode5()
    {
        initProtocolFormModel();
        ProtocolFormNode protocolFormNode = contextDataController.initProtocolFormNode( protocolFormModel, programArea );
        assertFalse( protocolFormNode.isIsParent() );
    }

    // Do we get the correct number of children
/*    public void testInsertClassifications0()
    {

        ContextModel contextModel = new ContextModel();

        //For the context model, we only need the ConteIdseq
        contextModel.setConteIdseq( "D9344734-8CAF-4378-E034-0003BA12F5E7" );

        //List of Classification Schemes
        List<ClassificationSchemeModel> csModelList = initCsModelList();

        //CS (Classification Scheme) folder
        ParentNode classificationsParentNode = new ParentNode();
        contextDataController.initClassificationsParentNode( classificationsParentNode, programArea, true );

        contextDataController.insertClassifications( classificationsParentNode, csModelList, contextModel, programArea, initCsCsiModelList()  );

        //Should be five children for this context
        assertEquals( 5, classificationsParentNode.getChildren().size() );
    }

    public void testInsertClassifications1()
    {

        ContextModel contextModel = new ContextModel();

        //For the context model, we only need the ConteIdseq
        contextModel.setConteIdseq( "F6141DD3-5081-EC4B-E040-BB89AD435D8B" );

        //List of Classification Schemes
        List<ClassificationSchemeModel> csModelList = initCsModelList();

        //CS (Classification Scheme) folder - A single parent node
        ParentNode classificationsParentNode = new ParentNode();
        contextDataController.initClassificationsParentNode( classificationsParentNode, programArea, true );
        contextDataController.insertClassifications( classificationsParentNode, csModelList, contextModel, programArea, initCsCsiModelList()  );

        //Should be two children for this context
        assertEquals( 2, classificationsParentNode.getChildren().size() );
    }*/


    //A local test, dev time only
/*
    public void testCsModelListInit()
    {
        initCsModelList();
        initCsCsiModelList();
        assertTrue( true );
    }
*/

    // Initialize Context Scheme Items list with sample data
    private List<CsCsiModel> initCsCsiModelList()
    {
        Gson gson = new GsonBuilder().create();
        String json = null;
        try
        {
            json = DBUtil.readFile( unitTestCommon.getTestDataDir() + "/src/test/java/gov/nih/nci/cadsr/service/restControllers/csCsiModelTest.data" );
        }
        catch( IOException e )
        {
            assertTrue( e.getMessage(), false );
        }
        return gson.fromJson( json, new TypeToken<List<CsCsiModel>>()
        {
        }.getType() );
    }


    public void testGetProgramAreaByName0()
    {
        //SDOs is index 6 in the test data.
        assertEquals( 6, contextDataController.getProgramAreaByName( "SDOs" ) );
    }

    public void testGetProgramAreaByName1()
    {
        //A bad value should return 0, which is "All"
        assertEquals( 0, contextDataController.getProgramAreaByName( "XXXXXXXXXX" ) );
    }

    public void testGetProgramAreaDescriptionByIndex0()
    {
        assertEquals( "StandardsDevelopmentOrganizations", contextDataController.getProgramAreaDescriptionByIndex( 6 ) );
    }

    public void testGetProgramAreaDescriptionByIndex1()
    {
        // 0 is "all", which is the default if the Program Area is returned as an empty String.
        assertEquals( "", contextDataController.getProgramAreaDescriptionByIndex( 0 ) );
    }

    // Test data initialization
    private void initProtocolFormModel()
    {
        protocolFormModel = new ProtocolFormModel();
        protocolFormModel.setLongName( testLongName );
        protocolFormModel.setProtoPreferredDefinition( testPreferredDefinition );
        protocolFormModel.setProtoIdseq( protoIdseq );
    }
    
    // Creates and initializes a list of ClassificationSchemeModels for insertClassifications tests
    private List<ClassificationSchemeModel> initCsModelList()
    {
        Gson gson = new GsonBuilder().create();
        String json = null;
        try
        {
            json = DBUtil.readFile(  unitTestCommon.getTestDataDir() + "/src/test/java/gov/nih/nci/cadsr/service/restControllers/classificationSchemeModelTest.data" );
        }
        catch( IOException e )
        {
            assertTrue( e.getMessage(), false );
        }
        return gson.fromJson( json, new TypeToken<List<ClassificationSchemeModel>>()
        {
        }.getType() );
    }


    // Initilize test data
    private void initCsCsisNodeList()
    {
        CsCsiModel childeClassificationFolder0 = new CsCsiModel();
        childeClassificationFolder0.setParentCsiIdseq( "F7BA6033-BAEA-C5EF-E040-BB89AD437201" );
        childeClassificationFolder0.setCsLongName( "CRF CDEs" );
        childeClassificationFolder0.setCsiName( "CRF CDEs" );
        childeClassificationFolder0.setCsiIdseq( "F7BA5589-4423-0BF5-E040-BB89AD435EB8" );

        CsCsiModel childeClassificationFolder1 = new CsCsiModel();
        childeClassificationFolder1.setParentCsiIdseq( "A6BA6233-DESE-C5EF-G040-RB89AD437010" );
        childeClassificationFolder1.setCsLongName( "Not a child" );
        childeClassificationFolder1.setCsiName( "Not a child" );
        childeClassificationFolder1.setCsiIdseq( "F7BA665F-255C-1487-E040-BB89AD433C74" );

        CsCsiModel childeClassificationFolder2 = new CsCsiModel();
        childeClassificationFolder2.setParentCsiIdseq( "F7BA6033-BAEA-C5EF-E040-BB89AD437201" );
        childeClassificationFolder2.setCsLongName( "MDR" );
        childeClassificationFolder2.setCsiName( "MDR" );
        childeClassificationFolder2.setCsiIdseq( "5675D35E-ED0C-22F8-E044-0003BA3F9857" );

        csCsiNodelList = new ArrayList<>();
        csCsiNodelList.add( childeClassificationFolder0 );
        csCsiNodelList.add( childeClassificationFolder1 );
        csCsiNodelList.add( childeClassificationFolder2 );
    }

}
