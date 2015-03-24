/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.dao.model;

import gov.nih.nci.cadsr.common.CaDSRConstants;

public class ContextModel extends BaseModel
{
    private String conteIdSeq = null;
    private String name = null;
    private String palName = "";
    private String description = null;
    private String preferredDefinition = null;

    public ContextModel()
    {
    }

    public ContextModel( String name )
    {
        setName( name );
    }

    public String getPreferredDefinition()
    {
        return preferredDefinition;
    }

    public void setPreferredDefinition( String preferredDefinition )
    {
        this.preferredDefinition = preferredDefinition;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getConteIdseq()
    {
        return conteIdSeq;
    }

    public void setConteIdseq( String newConteIdseq )
    {
        conteIdSeq = newConteIdseq;
    }

    public String getLlName()
    {
        return null;
    }

    public void setLlName( String p0 )
    {
    }

    public String getPalName()
    {
        return palName;
    }

    public void setPalName( String palName )
    {
        this.palName = palName;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String desc )
    {
        description = desc;
    }

    public String getLanguage()
    {
        return null;
    }

    public void setLanguage( String p0 )
    {
    }

    /**
     * This equals method only compares the Idseq to define equals
     *
     * @param obj
     * @return
     */
    public boolean equals( Object obj )
    {
        if( obj == null )
        {
            return false;
        }
        if( !( obj instanceof ContextModel ) )
        {
            return false;
        }
        ContextModel context = ( ContextModel ) obj;

        if( this.getConteIdseq().equals( context.getConteIdseq() ) )
        {
            return true;
        } else
        {
            return false;
        }
    }


    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append( CaDSRConstants.OBJ_SEPARATOR_START );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "name=" + getName() );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "conteIdSeq=" + getConteIdseq() );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "preferredDefinition=" + getPreferredDefinition() );
        sb.append( super.toString() );
        sb.append( CaDSRConstants.OBJ_SEPARATOR_END );
        sb.toString();

        return sb.toString();
    }
}
