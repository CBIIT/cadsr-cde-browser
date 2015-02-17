package gov.nih.nci.cadsr.service.model.context;

import gov.nih.nci.cadsr.dao.model.ContextModel;
import junit.framework.Test;
import junit.framework.TestCase;

import java.sql.Timestamp;


public class ContextNodeTest extends TestCase
{
    private ContextModel contextModel1;

    public void setUp ()
    {
        //A top level parent node
        contextModel1 = new ContextModel(  );
        contextModel1.setName( "ABTC" );
        contextModel1.setDescription( "Adult Brain Tumor Consortium" );
        contextModel1.setConteIdseq("F6117C06-C689-F9FD-E040-BB89AD432E40");
        contextModel1.setCreatedBy( "SBR" );
        contextModel1.setDateCreated( Timestamp.valueOf("2007-09-23 10:10:10.0"));
    }

    public void testTopLevelContextNode0()
    {
        ContextNode contextNode = new ContextNode( contextModel1 );
        assertEquals( "ABTC (Adult Brain Tumor Consortium) ", contextNode.getText() );
    }

    public void testTopLevelContextNode1()
    {
        ContextNode contextNode = new ContextNode( contextModel1 );
        assertEquals( "F6117C06-C689-F9FD-E040-BB89AD432E40", contextNode.getIdSeq() );
    }

    public void testTopLevelContextNode2()
    {
        ContextNode contextNode = new ContextNode( contextModel1 );
        assertEquals( "ABTC (Adult Brain Tumor Consor...", contextNode.getTrimText());
    }

    public void testTopLevelContextNode3()
    {
        ContextNode contextNode = new ContextNode( contextModel1 );
        assertFalse( contextNode.isIsParent());
    }

    public void testTopLevelContextNod4()
    {
        ContextNode contextNode = new ContextNode( contextModel1 );
        assertFalse( contextNode.isIsChild());
    }

    public void testTopLevelContextNode5()
    {
        ContextNode contextNode = new ContextNode( contextModel1 );
        contextNode.addChildNode( new ContextNode( contextModel1 ) );
        assertTrue( contextNode.isIsParent());
    }

    public void testTopLevelContextNode6()
    {
        ContextNode contextNode = new ContextNode( contextModel1 );
        contextNode.addChildNode( new ContextNode( contextModel1 ) );
        assertTrue( contextNode.getChildren().get(0).isIsChild());
    }

    public void testTopLevelContextNode7()
    {
        ContextNode contextNode = new ContextNode( contextModel1 );
        ContextNode childContextNode = new ContextNode( contextModel1 );
        childContextNode.setType( 3 );
        contextNode.addChildNode( new ContextNode( contextModel1 ) );

        assertEquals( 3, contextNode.getChildType() );
    }

}
