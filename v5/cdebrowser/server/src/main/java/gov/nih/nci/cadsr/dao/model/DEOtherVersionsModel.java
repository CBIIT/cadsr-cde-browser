package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;


public class DEOtherVersionsModel extends BaseModel
{
    public Float version;
    public String longName;
    public String workflowStatus; // als name
    public String registrationStatus; // not in table. Filled from SBR.AC_RESISTRATIONS.REGISTRATION_STATUS see DAO row mapper
    public String contextName;
    public List<CsCsiModel> csCsiModelList;

    public DEOtherVersionsModel()
    {
    }

    public Float getVersion()
    {
        return version;
    }

    public void setVersion( Float version )
    {
        setFormattedVersion( version );
        this.version = version;
    }

    public String getLongName()
    {
        return longName;
    }

    public void setLongName( String longName )
    {
        this.longName = longName;
    }

    public String getWorkflowStatus()
    {
        return workflowStatus;
    }

    public void setWorkflowStatus( String workflowStatus )
    {
        this.workflowStatus = workflowStatus;
    }

    public String getRegistrationStatus()
    {
        return registrationStatus;
    }

    public void setRegistrationStatus( String registrationStatus )
    {
        this.registrationStatus = registrationStatus;
    }

    public String getContextName()
    {
        return contextName;
    }

    public void setContextName( String contextName )
    {
        this.contextName = contextName;
    }

    public List<CsCsiModel> getCsCsiModelList()
    {
        return csCsiModelList;
    }

    public void setCsCsiModelList( List<CsCsiModel> csCsiModelList )
    {
        this.csCsiModelList = csCsiModelList;
    }

    @Override
    public boolean equals( Object o )
    {
        if( this == o ) return true;
        if( !( o instanceof DEOtherVersionsModel ) ) return false;

        DEOtherVersionsModel that = ( DEOtherVersionsModel ) o;

        if( getVersion() != null ? !getVersion().equals( that.getVersion() ) : that.getVersion() != null ) return false;
        if( getLongName() != null ? !getLongName().equals( that.getLongName() ) : that.getLongName() != null )
            return false;
        if( getWorkflowStatus() != null ? !getWorkflowStatus().equals( that.getWorkflowStatus() ) : that.getWorkflowStatus() != null )
            return false;
        if( getRegistrationStatus() != null ? !getRegistrationStatus().equals( that.getRegistrationStatus() ) : that.getRegistrationStatus() != null )
            return false;
        if( getContextName() != null ? !getContextName().equals( that.getContextName() ) : that.getContextName() != null )
            return false;
        return !( getCsCsiModelList() != null ? !getCsCsiModelList().equals( that.getCsCsiModelList() ) : that.getCsCsiModelList() != null );

    }

    @Override
    public int hashCode()
    {
        int result = getVersion() != null ? getVersion().hashCode() : 0;
        result = 31 * result + ( getLongName() != null ? getLongName().hashCode() : 0 );
        result = 31 * result + ( getWorkflowStatus() != null ? getWorkflowStatus().hashCode() : 0 );
        result = 31 * result + ( getRegistrationStatus() != null ? getRegistrationStatus().hashCode() : 0 );
        result = 31 * result + ( getContextName() != null ? getContextName().hashCode() : 0 );
        result = 31 * result + ( getCsCsiModelList() != null ? getCsCsiModelList().hashCode() : 0 );
        return result;
    }

    @Override
    public String toString()
    {
        return "DEOtherVersionsModel{" +
                "version=" + version +
                ", longName='" + longName + '\'' +
                ", workflowStatus='" + workflowStatus + '\'' +
                ", registrationStatus='" + registrationStatus + '\'' +
                ", contextName='" + contextName + '\'' +
                ", csCsiModelList=" + csCsiModelList +
                '}';
    }
}
