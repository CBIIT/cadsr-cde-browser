/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.dao.model;

import gov.nih.nci.cadsr.common.Audit;
import gov.nih.nci.cadsr.common.CaDSRConstants;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This class contains all the things that are common to all database data models.
 */
public abstract class BaseModel implements Serializable, Audit
{
    protected String createdBy;
    protected Timestamp createdDate;
    protected String modifiedBy;
    protected Timestamp modifiedDate;
    protected String formattedVersion;

    public BaseModel()
    {
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy( String p0 )
    {
        this.createdBy = p0;
    }

    public Timestamp getDateCreated()
    {
        return createdDate;
    }

    public void setDateCreated( Timestamp p0 )
    {
        this.createdDate = p0;
    }

    public String getModifiedBy()
    {
        return modifiedBy;
    }

    public void setModifiedBy( String p0 )
    {
        this.modifiedBy = p0;
    }

    public Timestamp getDateModified()
    {
        return modifiedDate;
    }

    public void setDateModified( Timestamp p0 )
    {
        this.modifiedDate = p0;
    }

    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    public String getFormattedVersion()
    {
        return formattedVersion;
    }

    public void setFormattedVersion( String formattedVersion )
    {
        this.formattedVersion = formattedVersion;
        setFormattedVersion( Float.valueOf( formattedVersion ) );
    }

    public void setFormattedVersion( float formattedVersion )
    {
        this.formattedVersion = Float.toString( formattedVersion );
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append( CaDSRConstants.ATTR_SEPARATOR + "createdBy=" + getCreatedBy() );
        if( this.getDateCreated() == null )
        {
            sb.append( CaDSRConstants.ATTR_SEPARATOR + "dateCreated=null" );
        }
        else
        {
            sb.append( CaDSRConstants.ATTR_SEPARATOR + "dateCreated=" + getDateCreated().toString() );
        }

        sb.append( CaDSRConstants.ATTR_SEPARATOR + "modifiedBy=" + getModifiedBy() );
        if( this.getDateModified() == null )
        {
            sb.append( CaDSRConstants.ATTR_SEPARATOR + "dateModified=null" );
        }
        else
        {
            sb.append( CaDSRConstants.ATTR_SEPARATOR + "dateModified=" + getDateModified().toString() );
        }

        return sb.toString();

    }
}
