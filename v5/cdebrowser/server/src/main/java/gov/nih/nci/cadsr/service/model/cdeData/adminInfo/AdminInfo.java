package gov.nih.nci.cadsr.service.model.cdeData.adminInfo;

import java.util.Date;

/**
 * Created by lernermh on 4/23/15.
 */
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
