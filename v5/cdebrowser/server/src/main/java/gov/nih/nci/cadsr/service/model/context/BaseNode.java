/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.service.model.context;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is extended by service.model.context nodes. They are the models for data going to the client side.
 * The subclass of this class currently do not add much if anything to the object, but are individually subclassed to allow for
 * ease of anticipated future evolution of these subclasses.
 */
public abstract class BaseNode implements Serializable
{
    private Logger logger = LogManager.getLogger( BaseNode.class.getName() );
    private String text;
    private String hover = "";
    private int childType;
    private String href = "";
    private int type;
    // program area is used as the parent grouping in client UIs
    private int programArea;
    private String idSeq = "";
    private boolean isParent;
    private boolean isChild;
    private boolean collapsed;
    // treePath is used to display "Bread Crumbs"  in client UIs
    private List<String> treePath;
    // children can be nested to an arbitrary depth, in the client UI this is typically nested folders fo "Classifications"
    private ArrayList<BaseNode> children;


    public BaseNode()
    {
        this.children = new ArrayList<>();
        this.treePath = new ArrayList<>();
        this.isParent = false;
        this.isChild = false;
        this.childType = 0;
    }

    public void addChildNode( BaseNode contextNode )
    {
        contextNode.setIsChild( true );
        addNode( contextNode );

    }

    public void addTopNode( BaseNode contextNode )
    {
        contextNode.setIsChild( false );
        addNode( contextNode );
    }

    /**
     * Add a Child node.
     *
     * @param contextNode A node to be added as a child to this node.
     */
    public void addNode( BaseNode contextNode )
    {
        children.add( contextNode );
        this.isParent = true;
        this.childType = contextNode.type;

        // If I am a folder with csi child, make me a csi folder
        // Type 2="Csi", type 3="Folder" type 5="CIS Folder"
        if( this.type == 3 && this.childType == 2 )
        {
            this.type = 5;
        }
    }


    public String getHover()
    {
        return hover;
    }

    public void setHover( String hover )
    {
        this.hover = hover;
    }

    public int getType()
    {
        return type;
    }

    public void setType( int type )
    {
        this.type = type;
    }

    public boolean isIsParent()
    {
        return isParent;
    }

    public void setIsParent( boolean parent )
    {
        this.isParent = parent;
    }

    public boolean isIsChild()
    {
        return isChild;
    }

    public void setIsChild( boolean child )
    {
        this.isChild = child;
    }

    public boolean isCollapsed()
    {
        return collapsed;
    }

    public void setCollapsed( boolean collapsed )
    {
        this.collapsed = collapsed;
    }

    public ArrayList<BaseNode> getChildren()
    {
        return children;
    }

    public void setChildren( ArrayList<BaseNode> children )
    {
        this.children = children;
    }


    /**
     * Returns a truncated version of the nodes text.
     *
     * @return truncated node text.
     */
    public String getTrimText()
    {
        String str = text;
        if( ( !str.isEmpty() )&& (str.length() > CaDSRConstants.MAX_TITLE_WITH_DESCRIPTION_LEN) )
        {
            str = str.substring( 0, CaDSRConstants.MAX_TITLE_WITH_DESCRIPTION_LEN ) + "...";
        }
        return str;
    }

    public String getText()
    {
        return this.text;
    }

    public void setText( String text )
    {
        this.text = text;
    }

    public int getChildType()
    {
        return childType;
    }

    public void setChildType( int childType )
    {
        this.childType = childType;
    }

    public String getHref()
    {
        return href;
    }

    public void setHref( String href )
    {
        this.href = href;
    }

    public String getIdSeq()
    {
        return idSeq;
    }

    public void setIdSeq( String idSeq )
    {
        this.idSeq = idSeq;
    }

    public int getProgramArea()
    {
        return programArea;
    }

    public void setProgramArea( int programArea )
    {
        this.programArea = programArea;
    }

    public List<String> getTreePath()
    {
        return treePath;
    }

    public void setTreePath( List<String> treePath )
    {
        this.treePath = treePath;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append( "name=" + this.getText() + "\n" );
        sb.append( "ConteIdSeq=" + this.getIdSeq() + "\n" );
        sb.append( "href=" + this.getHref() + "\n" );
        sb.append( "hover=" + this.getHover() + "\n" );
        sb.append( "type[" +  this.getType()  + "]=" + CaDSRConstants.nodeType[this.getType()] + "\n" );
        sb.append( "childType[" +  this.getChildType()  + "]=" + CaDSRConstants.nodeType[this.getChildType()] + "\n" );
        sb.append( "isParent=" + this.isIsParent() + "\n" );
        sb.append( "isChild=" + this.isIsChild() + "\n" );
        sb.append( "programArea=" + this.getProgramArea() + "\n" );
        sb.append( "treePath=" + this.getTreePath() + "\n" );
        return sb.toString();
    }

}
