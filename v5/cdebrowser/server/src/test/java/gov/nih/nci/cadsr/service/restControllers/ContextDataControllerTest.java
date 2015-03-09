package gov.nih.nci.cadsr.service.restControllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gov.nih.nci.cadsr.common.util.DBUtil;
import gov.nih.nci.cadsr.common.util.StringUtils;
import gov.nih.nci.cadsr.dao.model.ClassificationSchemeModel;
import gov.nih.nci.cadsr.dao.model.ContextModel;
import gov.nih.nci.cadsr.dao.model.CsCsiModel;
import gov.nih.nci.cadsr.dao.model.ProtocolFormModel;
import gov.nih.nci.cadsr.service.model.context.*;
import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ContextDataControllerTest extends TestCase
{
    private ContextDataController contextDataController;

    private List<CsCsiModel> csCsiNodelList = null;
    private ProtocolFormModel protocolFormModel = null;

    public void setUp()
    {
        contextDataController = new ContextDataController();
        contextDataController.setMaxHoverTextLenStr( "128" );
    }

    private void initCsCsiNodelList()
    {
        //Parent Classification folder
        CsCsiModel parentClassificationFolder = new CsCsiModel();
        parentClassificationFolder.setCsiIdseq( "7A12C53B-BD94-0A34-E040-BB89AD4349EF" );
        parentClassificationFolder.setCsCsiIdseq( "4E5E07B8-A7FF-1EB0-E044-0003BA3F9857" );
        parentClassificationFolder.setCsiName( "2000r1: Recipient Baseline Data" );

        csCsiNodelList.add( parentClassificationFolder );
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

        csCsiNodelList = new ArrayList<CsCsiModel>();
        csCsiNodelList.add( childeClassificationFolder0 );
        csCsiNodelList.add( childeClassificationFolder1 );
        csCsiNodelList.add( childeClassificationFolder2 );
        contextDataController.setCsCsiNodelList( csCsiNodelList );
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
         Test - ContextDataController.initProtocolFormNode
    ************************************************************ */
    private void initProtocolFormModel()
    {
        String testProtocolLongName = "Test Protocol LongName";
        String testProtocolPreferredDefinition = "Test Protocol Preferred Definition";
        protocolFormModel = new ProtocolFormModel();
        protocolFormModel.setLongName( testProtocolLongName );
        protocolFormModel.setProtoPreferredDefinition( testProtocolPreferredDefinition );
    }

    public void testInitProtocolFormNode0()
    {
        initProtocolFormModel();
        ProtocolFormNode protocolFormNode = contextDataController.initProtocolFormNode( protocolFormModel );
        assertEquals( "Test Protocol LongName", protocolFormNode.getText() );
    }

    public void testInitProtocolFormNode1()
    {
        initProtocolFormModel();
        ProtocolFormNode protocolFormNode = contextDataController.initProtocolFormNode( protocolFormModel );
        assertEquals( "Test Protocol Preferred Definition", protocolFormNode.getHover() );
    }

    public void testInitProtocolFormNode2()
    {
        initProtocolFormModel();
        ProtocolFormNode protocolFormNode = contextDataController.initProtocolFormNode( protocolFormModel );
        assertEquals( 6, protocolFormNode.getType() );
    }


    public void testInitProtocolFormNode3()
    {
        initProtocolFormModel();
        ProtocolFormNode protocolFormNode = contextDataController.initProtocolFormNode( protocolFormModel );
        assertEquals( "Default action", protocolFormNode.getHref() );
    }

    public void testInitProtocolFormNode4()
    {
        initProtocolFormModel();
        ProtocolFormNode protocolFormNode = contextDataController.initProtocolFormNode( protocolFormModel );
        assertFalse( protocolFormNode.isIsChild() );
    }

    public void testInitProtocolFormNode5()
    {
        initProtocolFormModel();
        ProtocolFormNode protocolFormNode = contextDataController.initProtocolFormNode( protocolFormModel );
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
        contextDataController.initClassificationsParentNode( classificationsParentNode );

        contextDataController.insertClassifications( classificationsParentNode, csModelList, contextModel );

        //Should be five children fo this context
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
        contextDataController.initClassificationsParentNode( classificationsParentNode );

        //Look at the Classification Schemes
/*
        for( ClassificationSchemeModel classificationSchemeModel : csModelList )
        {
            System.out.println( "------------------\nclassificationSchemeModel:\n" + classificationSchemeModel );
        }
*/


        System.out.println( "A IN testInsertClassifications: " + classificationsParentNode.toString() );
        System.out.println( "A IN contextModel.getConteIdseq: " + contextModel.getConteIdseq() );

        System.out.println( "A1");
        contextDataController.insertClassifications( classificationsParentNode, csModelList, contextModel );
        System.out.println( "A2");
               //System.out.println( ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\nB IN testInsertClassifications: " + classificationsParentNode.toString() );

        int parentCount = 0;
        //Get the children( the Classifications for this one context) for this Context
        ArrayList<BaseNode> children = classificationsParentNode.getChildren();
        System.out.println( "\nChildren[" + children.size() +"]\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" );
        for( BaseNode childNode : children )
        {
            System.out.println("Parent[" + parentCount +"] : " + childNode.toString() + "\n");
            ArrayList<BaseNode> cs = childNode.getChildren();
            System.out.println( "Grandchildren count: " + cs.size() );
            for( BaseNode classificationScheme : cs )
            {
                System.out.println( ">>=================================\nParent[" + parentCount + "] Grandchild (classificationScheme): " + classificationScheme );
                // ArrayList<BaseNode> cs = childNode.getChildren();
            }
            parentCount++;
            System.out.println( "\n=================================<<" );


        }
        System.out.println( "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n" );
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
        contextDataController.initClassificationsParentNode( classificationsParentNode );

        System.out.println( "A IN testInsertClassifications: " + classificationsParentNode.toString() );
        System.out.println( "A IN contextModel.getConteIdseq: " + contextModel.getConteIdseq() );

        contextDataController.insertClassifications( classificationsParentNode, csModelList, contextModel );
        //System.out.println( ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\nB IN testInsertClassifications: " + classificationsParentNode.toString() );

        int parentCount = 0;
        //Get the children( the Classifications for this one context) for this Context
        ArrayList<BaseNode> children = classificationsParentNode.getChildren();
        System.out.println( "\nChildren[" + children.size() +"]\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" );
        for( BaseNode childNode : children )
        {
            System.out.println("Parent[" + parentCount +"] : " + childNode.toString() + "\n");
            ArrayList<BaseNode> cs = childNode.getChildren();
            System.out.println( "Grandchildren count: " + cs.size() );
            for( BaseNode classificationScheme : cs )
            {
                System.out.println( ">>=================================\nParent[" + parentCount + "] *********** Grandchild (classificationScheme): " + classificationScheme );
                // ArrayList<BaseNode> cs = childNode.getChildren();
            }
            parentCount++;
            System.out.println( "\n=================================<<" );


        }
        System.out.println( "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n" );
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
        System.out.println( "csCsiModelListInit");

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

        List<CsCsiModel> csCsiNodelList = gson.fromJson( json, new TypeToken<List<CsCsiModel>>()
        {
        }.getType() );
/*

        for( CsCsiModel csCsiModel : csCsiNodelList )
        {
            System.out.println( "------------------\ncsCsiModel:\n" + csCsiModel );
        }
*/
        return csCsiNodelList;
    }

    // Creates and initilizes a list of ClassificationSchemeModels for insertClassifications tests
    private List<ClassificationSchemeModel> csModelListInit()
    {
System.out.println( "csModelListInit");
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

        List<ClassificationSchemeModel> csModelList = gson.fromJson( json, new TypeToken<List<ClassificationSchemeModel>>()
        {
        }.getType() );
/*

        for( ClassificationSchemeModel classificationSchemeModel : csModelList )
        {
            System.out.println( "------------------\nclassificationSchemeModel:\n" + classificationSchemeModel );
            System.out.println( "------------------\nclassificationSchemeModel.getCsIdseq():\n" + classificationSchemeModel.getCsIdseq() );
        }

*/
        return csModelList;
    }

}