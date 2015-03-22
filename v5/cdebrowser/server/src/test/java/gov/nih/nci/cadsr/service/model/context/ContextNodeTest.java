package gov.nih.nci.cadsr.service.model.context;

import gov.nih.nci.cadsr.dao.model.ContextModel;
import gov.nih.nci.cadsr.service.UnitTestCommon;
import junit.framework.TestCase;

import java.sql.Timestamp;


public class ContextNodeTest extends TestCase
{
    private ContextModel contextModel1;
    private int programArea = 1;


    public void setUp ()
    {
        //A top level parent node
        contextModel1 = new ContextModel( "ABTC" );
        contextModel1.setDescription( "Adult Brain Tumor Consortium" );
        contextModel1.setConteIdseq( "F6117C06-C689-F9FD-E040-BB89AD432E40" );
        contextModel1.setCreatedBy( "SBR" );
        contextModel1.setDateCreated( Timestamp.valueOf( "2007-09-23 10:10:10.0" ) );
    }

    public void testContextNodeFromContextDataModel0()
    {
        ContextNode contextNode = new ContextNode( contextModel1, programArea );
        assertEquals( "ABTC (Adult Brain Tumor Consortium) ", contextNode.getText() );
        assertEquals( "F6117C06-C689-F9FD-E040-BB89AD432E40", contextNode.getIdSeq() );
        assertEquals( "ABTC (Adult Brain Tumor Consor...", contextNode.getTrimText());
    }

    //A new context node should not be a child or a parent
     public void testContextNodeFromContextDataModel1()
    {
        ContextNode contextNode = new ContextNode( contextModel1, programArea );
        assertFalse( contextNode.isIsParent());
    }

    public void testContextNodeFromContextDataModel2()
    {
        ContextNode contextNode = new ContextNode( contextModel1, programArea );
        contextNode.addChildNode( new ContextNode( contextModel1, programArea ) );
        assertTrue( contextNode.isIsParent());
    }

    public void testContextNodeFromContextDataModel3()
    {
        ContextNode contextNode = new ContextNode( contextModel1, programArea );
        assertFalse( contextNode.isIsChild());
    }

    public void testContextNodeFromContextDataModel4()
    {
        ContextNode contextNode = new ContextNode( contextModel1, programArea );
        contextNode.addChildNode( new ContextNode( contextModel1, programArea ) );
        assertTrue( contextNode.getChildren().get(0).isIsChild());
    }

    public void testContextNodeFromContextDataModel5()
    {
        ContextNode contextNode = new ContextNode( contextModel1, programArea );
        ContextNode childContextNode = new ContextNode( contextModel1, programArea );
        childContextNode.setType( 3 );
        contextNode.addChildNode( new ContextNode( contextModel1, programArea ) );

        assertEquals( 3, contextNode.getChildType() );
    }

    public void testContextNodeDeepCopy0()
    {
        UnitTestCommon unitTestCommon = new UnitTestCommon();

        ContextNode contextNodeTreeOrig= unitTestCommon.initContextTree();
        ContextNode contextNodeTreeCopy = new ContextNode( contextNodeTreeOrig );

        assertEquals( contextNodeTreeOrig.getText(), contextNodeTreeCopy.getText() );
        BaseNode contextNodeTempOrig = contextNodeTreeOrig;
        BaseNode contextNodeTempCopy = contextNodeTreeCopy;

        while( contextNodeTempOrig.isIsParent() )
        {
            contextNodeTempOrig = contextNodeTempOrig.getChildren().get( 0 );
            contextNodeTempCopy = contextNodeTempCopy.getChildren().get( 0 );

            //Make sure the values are correct.
            assertEquals( contextNodeTempOrig.getText( ), contextNodeTempCopy.getText());
            assertEquals( contextNodeTempOrig.getProgramArea(), contextNodeTempCopy.getProgramArea());
            assertEquals( contextNodeTempOrig.getHover(), contextNodeTempCopy.getHover());
            assertEquals( contextNodeTempOrig.toString(), contextNodeTempCopy.toString());

            //Make sure it's a copy, not a same reference.
            assertNotSame( contextNodeTempOrig, contextNodeTempCopy );
        }
    }

    public void testContextNodeInit()
    {
        String name = "Test Alpha";
        int type = 2;
        int programArea = 3;
        ContextNode contextNode = new ContextNode( type, false, name, programArea );
        assertEquals( type, contextNode.getType() );
        assertEquals( name, contextNode.getText() );
        assertEquals( false, contextNode.isCollapsed() );
        assertEquals( programArea, contextNode.getProgramArea() );
    }

    //Adds a node, with out setting it as a child node
    public void testAddTopNode()
    {
        String name = "Test Alpha";
        int type = 2;
        int programArea = 3;
        ContextNode contextNode0 = new ContextNode( type, false, name, programArea );
        ContextNode contextNode1 = new ContextNode( type, false, name, programArea );
        contextNode0.addTopNode( contextNode1 );
        assertFalse( "This node should not be considered a child node.", contextNode1.isIsChild() );
    }

}
