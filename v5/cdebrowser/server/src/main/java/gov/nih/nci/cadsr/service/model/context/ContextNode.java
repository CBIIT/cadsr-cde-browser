/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.service.model.context;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.dao.model.ContextModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * A Context node, which can contain an arbitrary number/depth of children, is used to deliver to a client
 * a hierarchy of Contexts, classifications and Protocol Forms.
 */
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
     * Initialize (as much as possible) this new Context node.
     * Hover text is set to empty String
     *
     * @param type Folder/node type. 0="Empty" 1="Container" 2="Csi" 3="Folder" 4="ProtocolFormsFolder" 4="CIS Folder" 5="Protocol"
     * @param isCollapsed This only used as a UI display hint for clients.
     * @param name Display text.
     * @param programArea The Program Area, which is used by the client to display top level Tabs/Categories, and to constrain searches when Program Area is selected.
     */
    public ContextNode( int type, boolean isCollapsed, String name, int programArea )
    {
        this( type, isCollapsed, name, programArea, "" );
    }


    /**
     * Initialize (as much as possible) this new Context node.
     *
     * @param type Folder/node type. 0="Empty" 1="Container" 2="Csi" 3="Folder" 4="ProtocolFormsFolder" 4="CIS Folder" 5="Protocol"
     * @param isCollapsed This only used as a UI display hint for clients.
     * @param name Display text.
     * @param programArea The Program Area, which is used by the client to display top level Tabs/Categories, and to constrain searches when Program Area is selected.
     * @param hoverText Hover text/tooltip
     */
    public ContextNode( int type, boolean isCollapsed, String name, int programArea, String hoverText )
    {
        super();
        this.setChildren( new ArrayList<BaseNode>() );
        this.setIsParent( false );
        this.setType( type );
        this.setProgramArea( programArea );
        this.setText( name );
        // Hover text/tooltip
        this.setHover( hoverText );
        this.setCollapsed( isCollapsed );
    }


    /**
     * Copy constructor
     * Create a new ContextNode, with the contents of an existing ContextNode, will deep copy all children.
     * @param aContextNode The node to be duplicated.
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
     * @param sourceChildren The children to be duplicated.
     * @return Duplicate of sourceChildren
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
        this.setHref( "cdebrowserServer/cdesByContext," + contextModel.getConteIdseq());

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
