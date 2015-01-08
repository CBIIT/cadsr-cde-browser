/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.service.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class BaseNode implements Serializable
{
    private Logger logger = LogManager.getLogger( BaseNode.class.getName() );

    private String contextName;
    private String hover = "";
    private int childType;
    private String action;
    private int type;
    private ArrayList<BaseNode> children;
    private String idSeq = "";

    // At this time these are only used as UI hints.
    private boolean isParent;
    private boolean collapsed;


    public BaseNode()
    {
        this.children = new ArrayList<BaseNode>();
        this.isParent = false;
        this.childType = 0;
    }


    public void addChild( BaseNode contextNode )
    {
        children.add( contextNode );
        this.isParent = true;
        this.childType = contextNode.type;
        //logger.debug( "A [" + contextNode.getContextName() +"] Adding child: parent is type: " +  this.type + "  child is type: " + this.childType);

        //If I am a folder with csi child, make me a csi folder
        if( this.type == 3 && this.childType == 2 )
        {
            this.type = 5;
        }
        //logger.debug( "B [" + contextNode.getContextName() + "] Adding child: parent is type: " +  this.type + "  child is type: " + this.childType);

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

    public String getContextName()
    {
        return contextName;
    }

    public void setContextName( String contextName )
    {
        this.contextName = contextName;
    }

    public int getChildType()
    {
        return childType;
    }

    public void setChildType( int childType )
    {
        this.childType = childType;
    }

    public String getAction()
    {
        return action;
    }

    public void setAction( String action )
    {
        this.action = action;
    }

    public String getIdSeq()
    {
        return idSeq;
    }

    public void setIdSeq( String idSeq )
    {
        this.idSeq = idSeq;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append( "name=" + this.getContextName() + "\n" );
        sb.append( "ConteIdSeq=" + this.getIdSeq() + "\n" );
        sb.append( "action=" + this.getAction() + "\n" );
        sb.append( "hover=" + this.getHover() + "\n" );
        sb.append( "type=" + this.getType() + "\n" );
        sb.append( "childType=" + this.getChildType() + "\n" );
        sb.append( "isParent=" + this.isIsParent() + "\n" );
        return sb.toString();
    }

}
