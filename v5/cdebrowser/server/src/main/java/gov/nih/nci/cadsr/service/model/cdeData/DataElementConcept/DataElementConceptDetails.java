package gov.nih.nci.cadsr.service.model.cdeData.DataElementConcept;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

public class DataElementConceptDetails
{
    private int publicId;
    private float version;
    private String formattedVersion;
    private String longName;
    private String shortName;
    private String definition;
    private String context;
    private String workflowStatus;
    private Integer conceptualDomainPublicId;
    private String conceptualDomainShortName;
    private String conceptualDomainLongName;//CDEBROWSER-816 Use CD Long name on CDE View
    private String conceptualDomainContextName;
    private String conceptualDomainVersion;
    private String formattedConceptualDomainVersion;
    private String origin;

    public int getPublicId()
    {
        return publicId;
    }

    public void setPublicId( int publicId )
    {
        this.publicId = publicId;
    }

    public float getVersion()
    {
        return version;
    }

    public void setVersion( float version )
    {
        this.version = version;
        //this will give us at least one digit to the right of the decimal place
        this.formattedVersion = Float.toString( Float.valueOf( version ) );
    }

    public String getFormattedVersion()
    {
        return formattedVersion;
    }

    public void setFormattedVersion( String formattedVersion )
    {
        this.formattedVersion = formattedVersion;
    }

    public String getLongName()
    {
        return longName;
    }

    public void setLongName( String longName )
    {
        this.longName = longName;
    }

    public String getShortName()
    {
        return shortName;
    }

    public void setShortName( String shortName )
    {
        this.shortName = shortName;
    }

    public String getDefinition()
    {
        return definition;
    }

    public void setDefinition( String definition )
    {
        this.definition = definition;
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

    public Integer getConceptualDomainPublicId()
    {
        return conceptualDomainPublicId;
    }

    public void setConceptualDomainPublicId( Integer conceptualDomainPublicId )
    {
        this.conceptualDomainPublicId = conceptualDomainPublicId;
    }

    public String getConceptualDomainShortName()
    {
        return conceptualDomainShortName;
    }

    public void setConceptualDomainShortName( String conceptualDomainShortName )
    {
        this.conceptualDomainShortName = conceptualDomainShortName;
    }

    public String getConceptualDomainLongName() {
		return conceptualDomainLongName;
	}

	public void setConceptualDomainLongName(String conceptualDomainLongName) {
		this.conceptualDomainLongName = conceptualDomainLongName;
	}

	public String getConceptualDomainContextName()
    {
        return conceptualDomainContextName;
    }

    public void setConceptualDomainContextName( String conceptualDomainContextName )
    {
        this.conceptualDomainContextName = conceptualDomainContextName;
    }

    public String getConceptualDomainVersion()
    {
        return conceptualDomainVersion;
    }

    public void setConceptualDomainVersion( String conceptualDomainVersion )
    {
        this.conceptualDomainVersion = conceptualDomainVersion;
        this.formattedConceptualDomainVersion = Float.toString( Float.valueOf( conceptualDomainVersion ) );
    }

    public String getFormattedConceptualDomainVersion()
    {
        return formattedConceptualDomainVersion;
    }

    public void setFormattedConceptualDomainVersion( String formattedConceptualDomainVersion )
    {
        this.formattedConceptualDomainVersion = formattedConceptualDomainVersion;
    }

    public String getOrigin()
    {
        return origin;
    }

    public void setOrigin( String origin )
    {
        this.origin = origin;
    }
}
