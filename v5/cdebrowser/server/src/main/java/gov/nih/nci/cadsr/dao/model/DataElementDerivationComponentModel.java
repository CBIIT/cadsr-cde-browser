package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

public class DataElementDerivationComponentModel extends BaseModel
{
    private String displayOrder;
    private String longName;
    private String context;
    private String workflowStatus;
    private String publicId;
    private String version;
    private String deIdseq;

    public String getDisplayOrder()
    {
        return displayOrder;
    }

    public void setDisplayOrder( String displayOrder )
    {
        this.displayOrder = displayOrder;
    }

    public String getLongName()
    {
        return longName;
    }

    public void setLongName( String longName )
    {
        this.longName = longName;
    }

    public String getContext()
    {
        return context;
    }

    public void setContext( String context )
    {
        this.context = context;
    }

    public String getWorkflowStatus()
    {
        return workflowStatus;
    }

    public void setWorkflowStatus( String workflowStatus )
    {
        this.workflowStatus = workflowStatus;
    }

    public String getPublicId()
    {
        return publicId;
    }

    public void setPublicId( String publicId )
    {
        this.publicId = publicId;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
        setFormattedVersion( version );
    }

    public String getDeIdseq()
    {
        return deIdseq;
    }

    public void setDeIdseq( String deIdseq )
    {
        this.deIdseq = deIdseq;
    }
}
