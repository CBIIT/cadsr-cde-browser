package gov.nih.nci.cadsr.service.restControllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gov.nih.nci.cadsr.common.util.StringUtils;
import gov.nih.nci.cadsr.dao.model.CsCsiModel;
import gov.nih.nci.cadsr.dao.model.ProtocolFormModel;
import gov.nih.nci.cadsr.service.model.context.BaseNode;
import gov.nih.nci.cadsr.service.model.context.ClassificationItemNode;
import gov.nih.nci.cadsr.service.model.context.ContextNode;
import gov.nih.nci.cadsr.service.model.context.ProtocolFormNode;
import junit.framework.TestCase;

import java.io.IOException;
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
        contextDataController.setMaxHoverTextLenStr("128");
    }

    private void initCsCsiNodelList()
    {
        //Parent Classification folder - FIXME-THis is not typical parent data
        CsCsiModel parentClassificationFolder = new CsCsiModel();
        parentClassificationFolder.setCsiIdseq( "7A12C53B-BD94-0A34-E040-BB89AD4349EF" );
        parentClassificationFolder.setCsCsiIdseq( "4E5E07B8-A7FF-1EB0-E044-0003BA3F9857" );
        parentClassificationFolder.setCsiName( "2000r1: Recipient Baseline Data" );


        csCsiNodelList.add( parentClassificationFolder );
    }
    //private List<CsCsiModel> csCsiNodelList = null;
//private List<CsCsiModel> getCsCsisByParentCsCsi( String csCsiIdseq )


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
   */

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
            System.out.println( csCsiModel.getCsLongName() );
        }
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

        // List<BaseNode> childrenList = classificationItemNodeParent.getChildren();

        //Look at the parent, do we have two children?
        //System.out.println( classificationItemNodeParent.getChildren().size() + " children" );

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



/* *************************************************
    Test - ContextDataController.initProtocolFormNode
 */
    private void initProtocolFormModel()
    {
        String testProtocolLongName = "Test Protocol LongName";
        String testProtocolPreferredDefinition = "Test Protocol Preferred Definition";
        protocolFormModel = new ProtocolFormModel();
        protocolFormModel.setLongName(testProtocolLongName);
        protocolFormModel.setProtoPreferredDefinition(testProtocolPreferredDefinition);
    }

    public void testInitProtocolFormNode0()
    {
        initProtocolFormModel();
        ProtocolFormNode protocolFormNode = contextDataController.initProtocolFormNode(protocolFormModel);
        assertEquals( "Test Protocol LongName", protocolFormNode.getText() );
    }

    public void testInitProtocolFormNode1()
    {
        initProtocolFormModel();
        ProtocolFormNode protocolFormNode = contextDataController.initProtocolFormNode(protocolFormModel);
        assertEquals( "Test Protocol Preferred Definition", protocolFormNode.getHover() );
    }

    public void testInitProtocolFormNode2()
    {
        initProtocolFormModel();
        ProtocolFormNode protocolFormNode = contextDataController.initProtocolFormNode(protocolFormModel);
        assertEquals( 6, protocolFormNode.getType() );
    }


    public void testInitProtocolFormNode3()
    {
        initProtocolFormModel();
        ProtocolFormNode protocolFormNode = contextDataController.initProtocolFormNode(protocolFormModel);
        assertEquals( "Default action", protocolFormNode.getHref() );
    }

    public void testInitProtocolFormNode4()
    {
        initProtocolFormModel();
        ProtocolFormNode protocolFormNode = contextDataController.initProtocolFormNode(protocolFormModel);
        assertFalse( protocolFormNode.isIsChild() );
    }

    public void testInitProtocolFormNode5()
    {
        initProtocolFormModel();
        ProtocolFormNode protocolFormNode = contextDataController.initProtocolFormNode(protocolFormModel);
        assertFalse( protocolFormNode.isIsParent() );
    }

}