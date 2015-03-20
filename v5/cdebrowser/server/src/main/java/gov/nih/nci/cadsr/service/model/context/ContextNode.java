/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.service.model.context;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.dao.model.ContextModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class ContextNode extends BaseNode
{

    private Logger logger = LogManager.getLogger( ContextNode.class.getName() );
    private String palName ="";
    private String palNameDescription ="";

    public ContextNode()
    {
        super();
    }

    public String getPalName()
    {
        return palName;
    }

    public void setPalName( String palName )
    {
        this.palName = palName;
    }

    public String getPalNameDescription()
    {
        return palNameDescription;
    }

    public void setPalNameDescription( String palNameDescription )
    {
        this.palNameDescription = palNameDescription;
    }

    /**
     * Hover text is set to empty String
     *
     * @param type
     * @param isCollapsed
     * @param name
     * @param programArea
     */
    public ContextNode( int type, boolean isCollapsed, String name, int programArea )
    {
        this( type, isCollapsed, name, programArea, "" );
    }


    /**
     *
     * @param type
     * @param isCollapsed
     * @param name
     * @param programArea
     * @param hoverText
     */
    public ContextNode( int type, boolean isCollapsed, String name, int programArea, String hoverText )
    {
        super();
        this.setChildren( new ArrayList<BaseNode>() );
        this.setIsParent( false );
        this.setType( type );
        this.setProgramArea( programArea );
        this.setText( name );
        this.setHover( hoverText );
        // Contexts don't need tooltips
        //this.setHover( hoverText );
        this.setCollapsed( isCollapsed );
    }

    /**
     * Copy constructor
     */
    public ContextNode( ContextNode aContextNode)
    {
        super();
        this.setPalNameDescription( aContextNode.getPalNameDescription() );
        this.setPalName( aContextNode.getPalName() );
        this.setHref( aContextNode.getHref() );
        this.setHover( aContextNode.getHover() );
        this.setIsParent( aContextNode.isIsParent() );
        this.setProgramArea( aContextNode.getProgramArea() );
        this.setChildType( aContextNode.getChildType() );
        this.setCollapsed( aContextNode.isCollapsed() );
        this.setIdSeq( aContextNode.getIdSeq() );
        this.setIsChild( aContextNode.isIsChild() );
        this.setText( aContextNode.getText() );
        this.setType( aContextNode.getType() );
        this.setTreePath( aContextNode.getTreePath() );

        this.setChildren( copyChildren(aContextNode.getChildren()) );

    }

    /**
     * Recursively copy a nodes children
     * @param sourceChildren
     * @return
     */
    public ArrayList<BaseNode> copyChildren( ArrayList<BaseNode> sourceChildren)
    {
        ArrayList<BaseNode> newChildren = new ArrayList<>();
        for( BaseNode node : sourceChildren )
        {
            BaseNode newChildNode = new ContextNode();
            newChildNode.setText( node.getText() );
            newChildNode.setHover( node.getHover() );
            newChildNode.setChildType( node.getChildType() );
            newChildNode.setHref( node.getHref() );
            newChildNode.setIdSeq( node.getIdSeq() );
            newChildNode.setIsParent( node.isIsParent() );
            newChildNode.setProgramArea( node.getProgramArea() );
            newChildNode.setIsChild( node.isIsChild() );
            newChildNode.setCollapsed( node.isCollapsed() );
            newChildNode.setTreePath( node.getTreePath() );
            newChildNode.setType( node.getType() );

            if( ! node.getChildren().isEmpty())
            {
                newChildNode.setChildren( copyChildren( node.getChildren() ) );
            }
            newChildren.add( newChildNode );
        }
        return newChildren;
    }
    /**
     * Create a new Context Node with the values of a Context Model from the database
     *
     * @param contextModel database model returned by SQL query
     */
    public ContextNode( ContextModel contextModel, int programArea)
    {
        super();
        this.setText( contextModel.getName() + " (" + contextModel.getDescription() + ") " );
        this.setPalName( contextModel.getPalName() );
        this.setType( CaDSRConstants.FOLDER );
        this.setProgramArea( programArea );
        this.setCollapsed( true );
        this.setIsParent( false );
        // Contexts don't need tooltips
        //this.setHover( this.getText() );

        //FIXME - just test text for now
        //this.setHref( "test.html?context=" + this.getText() );
        this.setChildType( 0 );
        this.setIdSeq( contextModel.getConteIdseq() );
        this.setChildren( new ArrayList<BaseNode>() );
    }


    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append( super.toString() );
        sb.append( "palName=" + this.palName + "\n" );
        sb.append( "palNameDescription=" + this.palNameDescription + "\n" );
        return sb.toString();
    }
}
