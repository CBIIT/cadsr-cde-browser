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

public abstract class BaseNode implements Serializable
{
    private Logger logger = LogManager.getLogger( BaseNode.class.getName() );
    private String text;
    private String hover = "";
    private int childType;
    private String href = "";
    private int type;
    private int programArea;
    private String idSeq = "";

    // At this time these are only used as UI hints.
    private boolean isParent;
    private boolean isChild;
    private boolean collapsed;
    private List<String> treePath;
    private ArrayList<BaseNode> children;


    public BaseNode()
    {
        this.children = new ArrayList<BaseNode>();
        this.treePath = new ArrayList<String>();
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

    public void addNode( BaseNode contextNode )
    {
        children.add( contextNode );
        this.isParent = true;
        this.childType = contextNode.type;
        //logger.debug( "A [" + contextNode.getText() +"] Adding child: parent is type: " +  this.type + "  child is type: " + this.childType);

        //If I am a folder with csi child, make me a csi folder
        if( this.type == 3 && this.childType == 2 )
        {
            this.type = 5;
        }
        //logger.debug( "B [" + contextNode.getText() + "] Adding child: parent is type: " +  this.type + "  child is type: " + this.childType);

    }



    protected String trimName( int len, String text )
    {
        if( !text.isEmpty() && text.length() > len )
        {
            text = text.substring( 0, len ) + "...";
        }
        return text;
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

    public String getTrimText()
    {
        //FIXME - just test text for now
        //this.setHref( "test.html?context=" + this.text );
        //logger.debug( "this.Trimtext: " + trimName( CaDSRConstants.MAX_TITLE_WITH_DESCRIPTION_LEN, text ) );
        return trimName( CaDSRConstants.MAX_TITLE_WITH_DESCRIPTION_LEN, text );
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
        StringBuffer sb = new StringBuffer();

        //sb.append( "name=" + this.getText() + "\n" );
        sb.append( "name=" + this.getText() + "\n" );

        sb.append( "ConteIdSeq=" + this.getIdSeq() + "\n" );
        sb.append( "href=" + this.getHref() + "\n" );
        sb.append( "hover=" + this.getHover() + "\n" );
        sb.append( "type=" + this.getType() + "\n" );
        sb.append( "childType=" + this.getChildType() + "\n" );
        sb.append( "isParent=" + this.isIsParent() + "\n" );
        sb.append( "isChild=" + this.isIsChild() + "\n" );
        sb.append( "programArea=" + this.getProgramArea() + "\n" );
        sb.append( "treePath=" + this.getTreePath() + "\n" );
        return sb.toString();
    }

}
