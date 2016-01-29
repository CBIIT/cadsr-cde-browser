package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.common.CaDSRConstants;


public class ProgramAreaModel extends BaseModel
{
    private String comments;
    private String description;
    private String palName;

    public ProgramAreaModel()
    {
    }

    public String getComments()
    {
        return comments;
    }

    public void setComments( String comments )
    {
        this.comments = comments;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getPalName()
    {
        return palName;
    }

    public void setPalName( String palName )
    {
        this.palName = palName;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append( CaDSRConstants.OBJ_SEPARATOR_START );
        sb.append( "name=" + getPalName() );
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "description=" + getDescription() );
        sb.append( super.toString() );
        sb.append( CaDSRConstants.OBJ_SEPARATOR_END );
        sb.toString();

        return sb.toString();
    }

    @Override
    public boolean equals( Object o )
    {
        if( this == o ) return true;
        if( !( o instanceof ProgramAreaModel ) ) return false;

        ProgramAreaModel that = ( ProgramAreaModel ) o;

        if( getComments() != null ? !getComments().equals( that.getComments() ) : that.getComments() != null )
            return false;
        if( getDescription() != null ? !getDescription().equals( that.getDescription() ) : that.getDescription() != null )
            return false;
        return !( getPalName() != null ? !getPalName().equals( that.getPalName() ) : that.getPalName() != null );

    }

    @Override
    public int hashCode()
    {
        int result = getComments() != null ? getComments().hashCode() : 0;
        result = 31 * result + ( getDescription() != null ? getDescription().hashCode() : 0 );
        result = 31 * result + ( getPalName() != null ? getPalName().hashCode() : 0 );
        return result;
    }
}
