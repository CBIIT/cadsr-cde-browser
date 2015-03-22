package gov.nih.nci.cadsr.service.restControllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.common.util.DBUtil;
import gov.nih.nci.cadsr.dao.model.*;
import gov.nih.nci.cadsr.service.UnitTestCommon;
import gov.nih.nci.cadsr.service.model.context.*;
import junit.framework.TestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContextDataControllerTest extends TestCase
{
    private ContextDataController contextDataController;

    private List<CsCsiModel> csCsiNodelList = null;
    private ProtocolFormModel protocolFormModel = null;
    private ProtocolModel protocolModel  = null;
    private UnitTestCommon unitTestCommon = null;
    private int programArea = 1;
    private String testPreferredDefinition;
    private String testLongName;
    private String testHoverText;
    private int testMaxHoverTextLen;

    public void setUp()
    {
        unitTestCommon = new UnitTestCommon();
        contextDataController = new ContextDataController();
        contextDataController.setMaxHoverTextLenStr( "50" );
        testMaxHoverTextLen = Integer.parseInt( contextDataController.getMaxHoverTextLenStr() );
        contextDataController.setProgramAreaModelList( unitTestCommon.initSampleProgramAreas() );
        contextDataController.setContextPalNameCount(contextDataController.getProgramAreaModelList().size());
        initTestPreferredDefinition();
    }


    public void testAddBreadCrumbs()
    {
        BaseNode contextNodeTree = unitTestCommon.initContextTree();
        contextDataController.addBreadCrumbs( contextNodeTree, null );

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

    public void testAddBreadCrumbsAll()
    {
        BaseNode contextNodeTree = unitTestCommon.initContextTree();
        contextDataController.addBreadCrumbsAll( contextNodeTree, "All" );

        // This array matches the data used by unitTestCommon.initContextTree() above
        ArrayList<String> tree = new ArrayList<>();
        tree.add( "All" ); //First "Crumb" should always be all
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

    //Sets the program area for the top level "Classifications" and "Protocol Forms" in the href and the nodes program area.
    public void testSetHrefProgramArea()
    {
        BaseNode contextNodeTree = unitTestCommon.initContextTree();

        //Set a bogus program area, so we can check that has ben changed
        contextNodeTree.getChildren().get( 0 ).setProgramArea( 66 );
        int origProgramArea = contextNodeTree.getChildren().get( 0 ).getProgramArea();
        contextDataController.setHrefProgramArea( contextNodeTree, programArea );

        //Make sure program area changed
        assertFalse( "Program area should have changed, and no longer == " + origProgramArea, origProgramArea == contextNodeTree.getChildren().get( 0 ).getProgramArea() );

        //Make sure it changed to the correct program area
        assertEquals( programArea, contextNodeTree.getChildren().get( 0 ).getProgramArea() );

        //Make sure the program area has been set in the rest call string given to the client
        String restCall = contextNodeTree.getChildren().get( 0 ).getHref();
        assertTrue( restCall.contains( "&programArea=" + programArea ) );
    }


    public void testInitProtocolsFormsParentNode()
    {
        boolean collapsed = true;
        String text = "ProtocolForms";
        ParentNode protocolsParentNode = new ParentNode();
        contextDataController.initProtocolsFormsParentNode( protocolsParentNode, programArea, collapsed );//ParentNode, programArea, isCollapsed

        assertEquals( text, protocolsParentNode.getText() );
        assertEquals( programArea, protocolsParentNode.getProgramArea() );
        assertEquals( CaDSRConstants.FOLDER, protocolsParentNode.getType() );
        assertEquals( CaDSRConstants.PROTOCOL_FORMS_FOLDER, protocolsParentNode.getChildType() );
        assertFalse( "Should not be set as parent.", protocolsParentNode.isIsParent() );
        assertNotNull( protocolsParentNode.getChildren() );
    }

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

    public void testInsertPlaceHolderNode()
    {
        ParentNode parentNode = new ParentNode();
        contextDataController.insertPlaceHolderNode( parentNode );

        //Get the place holder node, it will be the first and only child.
        ParentNode placeHolderNode = (ParentNode)parentNode.getChildren().get( 0 );
        assertEquals( "Place Holder", placeHolderNode.getText());
        assertTrue( "Place holder node should be collapsed.", placeHolderNode.isCollapsed() );
        assertFalse( "Place holder node should not be set as a Parent.", placeHolderNode.isIsParent() );
    }

    // The top level nodes should have only the Program areas
    public void testInitTopLevelContextNodes()
    {
        String name[] = { "All", "CancerCenters", "NCI", "NCIConsortium", "NCTN/eNCTN", "NIHInstitutes", "SDOs", "UNASSIGNED" };
        String description[] = { "", "CancerCenters/CancerInstitutes", "NCIDivisions/Centers/Programs", "NCIConsortrium", "NCIClinicalTrialsNetwork", "NIHInstitutes", "StandardsDevelopmentOrganizations", "null" };

        ContextNode[] contextNode = contextDataController.initTopLevelContextNodes();
        //Make sure "All" was add to front of the array
        assertEquals( "All", contextNode[0].getText() );

        //Test data has 7 program areas, with "All" is 8
        assertEquals( 8, contextNode.length );

        //Did they all get populated correctly
        for( int f = 0; f < contextNode.length; f++ )
        {
            assertEquals( name[f], contextNode[f].getText() );
            assertEquals( description[f], contextNode[f].getPalNameDescription() );
        }
    }

    /* ************************************************************
         Test - ContextDataController.getCsCsisByParentCsCsi
    ************************************************************ */
    /*
         Create three children, two match the parent ID, one does not, make sure we find two
    */
    public void testGetCsCsisByParentCsCsi0()
    {
        String parentIdSeq = "F7BA6033-BAEA-C5EF-E040-BB89AD437201";
        initCsCsisNodeList();

        List<CsCsiModel> csCsiChildModelList = contextDataController.getCsCsisByParentCsCsi( parentIdSeq );
        for( CsCsiModel csCsiModel : csCsiChildModelList )
        {
            if( ( csCsiModel.getCsLongName().compareTo( "CRF CDEs" ) != 0 ) && ( csCsiModel.getCsLongName().compareTo( "MDR" ) != 0 ) )
            {
                assertFalse( "Did not find child Classification folder: \"" + csCsiModel.getCsLongName() + "\"  .", true );
            }
        }
        assertTrue( true );
    }

    /*
        Create three children, two match the parent ID, one does not, make sure we don't match the non-child
    */
    public void testGetCsCsisByParentCsCsi1()
    {
        String parentIdSeq = "F7BA6033-BAEA-C5EF-E040-BB89AD437201";
        initCsCsisNodeList();

        List<CsCsiModel> csCsiChildModelList = contextDataController.getCsCsisByParentCsCsi( parentIdSeq );
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
        contextDataController.addChildrenToCsi( classificationItemNodeParent );

        //Look at the parent, do we have two children?
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


    /* ************************************************************
        Test - ContextDataController.initProtocolNode
   ************************************************************ */
    private void initTestPreferredDefinition()
    {
        testLongName = "Test long name";
        // This will test the truncating of very long Descriptions when they are used for hover text
        testPreferredDefinition =  "Test preferred preferred definition" + new String( new char[testMaxHoverTextLen]).replace('\0', 'X');
        testHoverText = testPreferredDefinition.substring( 0, testMaxHoverTextLen) + "...";

    }

    private void initProtocolModel()
    {
        //Set the test ProtocolModel with only fields needed for initializing a ProtocolNode.
        protocolModel = new ProtocolModel();
        protocolModel.setLongName( testLongName );
        protocolModel.setPreferredDefinition( testPreferredDefinition );

    }
    //FIXME - add a good description
    public void testInitProtocolNode0()
    {
        initProtocolModel();
        ProtocolNode protocolNode = contextDataController.initProtocolNode(  protocolModel,  programArea );
        assertEquals( testLongName, protocolNode.getText()  );
    }

    public void testInitProtocolNode1()
    {
        initProtocolModel();
        ProtocolNode protocolNode = contextDataController.initProtocolNode(  protocolModel,  programArea );
        assertEquals( testHoverText, protocolNode.getHover() );
    }

    public void testInitProtocolNode2()
    {
        initProtocolModel();
        ProtocolNode protocolNode = contextDataController.initProtocolNode(  protocolModel,  programArea );
        assertEquals( programArea, protocolNode.getProgramArea() );
    }

    public void testInitProtocolNode3()
    {
        initProtocolModel();
        ProtocolNode protocolNode = contextDataController.initProtocolNode(  protocolModel,  programArea );
        assertEquals( CaDSRConstants.PROTOCOL_FORMS_FOLDER, protocolNode.getType() );
    }

    public void testInitProtocolNode4()
    {
        initProtocolModel();
        ProtocolNode protocolNode = contextDataController.initProtocolNode(  protocolModel,  programArea );
        assertEquals( CaDSRConstants.PROTOCOL, protocolNode.getChildType() );
    }

    public void testInitProtocolNode5()
    {
        initProtocolModel();
        ProtocolNode protocolNode = contextDataController.initProtocolNode(  protocolModel,  programArea );
        assertTrue( "This ProtocolNode should be collapsed.", protocolNode.isCollapsed() );
    }

    public void testInitProtocolNode6()
    {
        initProtocolModel();
        ProtocolNode protocolNode = contextDataController.initProtocolNode(  protocolModel,  programArea );
        assertFalse( "This ProtocolNode should not be set as a Parent.", protocolNode.isIsParent() );
    }



    /* ************************************************************
         Test - ContextDataController.initProtocolFormNode
    ************************************************************ */
    private void initProtocolFormModel()
    {
        protocolFormModel = new ProtocolFormModel();
        protocolFormModel.setLongName( testLongName );
        protocolFormModel.setProtoPreferredDefinition( testPreferredDefinition );
    }

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
        assertEquals(  CaDSRConstants.EMPTY, protocolFormNode.getChildType() );
    }


    public void testInitProtocolFormNode4()
    {
        initProtocolFormModel();
        ProtocolFormNode protocolFormNode = contextDataController.initProtocolFormNode( protocolFormModel, programArea );
        assertEquals( "", protocolFormNode.getHref() );
    }

    public void testInitProtocolFormNode5()
    {
        initProtocolFormModel();
        ProtocolFormNode protocolFormNode = contextDataController.initProtocolFormNode( protocolFormModel, programArea );
        assertFalse( protocolFormNode.isIsChild() );
    }

    public void testInitProtocolFormNode6()
    {
        initProtocolFormModel();
        ProtocolFormNode protocolFormNode = contextDataController.initProtocolFormNode( protocolFormModel, programArea );
        assertFalse( protocolFormNode.isIsParent() );
    }

    /**
     * Test - Do we get the correct number of children
     */
    public void testInsertClassifications0()
    {

        contextDataController.setCsCsiNodelList( csCsiModelListInit() );
        ContextModel contextModel = new ContextModel();

        //For the context model, we only need the ConteIdseq
        contextModel.setConteIdseq( "D9344734-8CAF-4378-E034-0003BA12F5E7" );

        //List of Classification Schemes
        List<ClassificationSchemeModel> csModelList = csModelListInit();

        //CS (Classification Scheme) folder
        ParentNode classificationsParentNode = new ParentNode();
        contextDataController.initClassificationsParentNode( classificationsParentNode, programArea, true );

        contextDataController.insertClassifications( classificationsParentNode, csModelList, contextModel, programArea );

        //Should be five children for this context
        assertEquals( 5, classificationsParentNode.getChildren().size() );
    }

    public void testInsertClassifications1()
    {

        contextDataController.setCsCsiNodelList( csCsiModelListInit() );
        ContextModel contextModel = new ContextModel();

        //For the context model, we only need the ConteIdseq
        contextModel.setConteIdseq( "F6141DD3-5081-EC4B-E040-BB89AD435D8B" );
        //contextModel.setConteIdseq( "D9344734-8CAF-4378-E034-0003BA12F5E7" );

        //List of Classification Schemes
        List<ClassificationSchemeModel> csModelList = csModelListInit();

        //CS (Classification Scheme) folder - A single parent node
        ParentNode classificationsParentNode = new ParentNode();
        contextDataController.initClassificationsParentNode( classificationsParentNode, programArea, true );

        //Look at the Classification Schemes
/*
        for( ClassificationSchemeModel classificationSchemeModel : csModelList )
        {
            System.out.println( "------------------\nclassificationSchemeModel:\n" + classificationSchemeModel );
        }
*/

        contextDataController.insertClassifications( classificationsParentNode, csModelList, contextModel, programArea );

        //Should be two children fo this context
        assertEquals( 2, classificationsParentNode.getChildren().size() );
    }

    /*
    This one still needs some work...
     */
    public void testInsertClassifications2()
    {

        contextDataController.setCsCsiNodelList( csCsiModelListInit() );
        ContextModel contextModel = new ContextModel();

        //For the context model, we only need the ConteIdseq
        contextModel.setConteIdseq( "D9344734-8CAF-4378-E034-0003BA12F5E7" );

        //List of Classification Schemes
        List<ClassificationSchemeModel> csModelList = csModelListInit();

        //CS (Classification Scheme) folder - A single parent node
        ParentNode classificationsParentNode = new ParentNode();
        contextDataController.initClassificationsParentNode( classificationsParentNode, programArea, true );
        contextDataController.insertClassifications( classificationsParentNode, csModelList, contextModel, programArea );

        //Should be two children fo this context
        assertEquals( 5, classificationsParentNode.getChildren().size() );
    }

    //A local test, dev time only
    public void testCsModelListInit()
    {
        csModelListInit();
        csCsiModelListInit();
        assertTrue( true );
    }

    private List<CsCsiModel> csCsiModelListInit()
    {
        Gson gson = new GsonBuilder().create();
        String json = null;
        try
        {
            json = DBUtil.readFile( "src/test/java/gov/nih/nci/cadsr/service/restControllers/csCsiModelTest.data" );
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
        assertEquals(6, contextDataController.getProgramAreaByName( "SDOs" ));
    }

    public void testGetProgramAreaByName1()
    {
        //A bad value should return 0, which is "All"
        assertEquals( 0, contextDataController.getProgramAreaByName( "XXXXXXXXXX" ) );
    }

    public void testGetProgramAreaDescriptionByIndex()
    {
       assertEquals( "StandardsDevelopmentOrganizations", contextDataController.getProgramAreaDescriptionByIndex( 6 ) );
    }

    // Creates and initilizes a list of ClassificationSchemeModels for insertClassifications tests
    private List<ClassificationSchemeModel> csModelListInit()
    {
        Gson gson = new GsonBuilder().create();
        String json = null;
        try
        {
            json = DBUtil.readFile( "src/test/java/gov/nih/nci/cadsr/service/restControllers/classificationSchemeModelTest.data" );
        }
        catch( IOException e )
        {
            assertTrue( e.getMessage(), false );
        }
        return gson.fromJson( json, new TypeToken<List<ClassificationSchemeModel>>()
        {
        }.getType() );
    }


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
        contextDataController.setCsCsiNodelList( csCsiNodelList );
    }

}