package gov.nih.nci.cadsr.service.model.cdeData.adminInfo;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.Date;

public class AdminInfo
{
    private String createdBy;
    private String dateCreated;
    private String modifiedBy;
    private String dateModified;

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy( String createdBy )
    {
        this.createdBy = createdBy;
    }

    public String getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated( String dateCreated )
    {
        this.dateCreated = dateCreated;
    }

    public String getModifiedBy()
    {
        return modifiedBy;
    }

    public void setModifiedBy( String modifiedBy )
    {
        this.modifiedBy = modifiedBy;
    }

    public String getDateModified()
    {
        return dateModified;
    }

    public void setDateModified( String dateModified )
    {
        this.dateModified = dateModified;
    }
}
