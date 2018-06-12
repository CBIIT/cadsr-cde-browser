package gov.nih.nci.cadsr.service.model.cdeData.dataElement;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

public class DataElementDetails
{
    private int publicId;
    private float version;
    private String formattedVersion;
    private String longName;
    private String shortName;
    private String preferredQuestionText;
    private String definition;
    private String valueDomain;
    private String dataElementConcept;
    private String context;
    private String workflowStatus;
    private String origin;
    private String registrationStatus;
    private String directLink;
    private String id; //CDEBROWSER-868 CDE IDSEQ is required to resolve this error

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

    public String getPreferredQuestionText()
    {
        return preferredQuestionText;
    }

    public void setPreferredQuestionText( String preferredQuestionText )
    {
        this.preferredQuestionText = preferredQuestionText;
    }

    public String getDefinition()
    {
        return definition;
    }

    public void setDefinition( String definition )
    {
        this.definition = definition;
    }

    public String getValueDomain()
    {
        return valueDomain;
    }

    public void setValueDomain( String valueDomain )
    {
        this.valueDomain = valueDomain;
    }

    public String getDataElementConcept()
    {
        return dataElementConcept;
    }

    public void setDataElementConcept( String dataElementConcept )
    {
        this.dataElementConcept = dataElementConcept;
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

    public String getOrigin()
    {
        return origin;
    }

    public void setOrigin( String origin )
    {
        this.origin = origin;
    }

    public String getRegistrationStatus()
    {
        return registrationStatus;
    }

    public void setRegistrationStatus( String registrationStatus )
    {
        this.registrationStatus = registrationStatus;
    }

    public String getDirectLink()
    {
        return directLink;
    }

    public void setDirectLink( String directLink )
    {
        this.directLink = directLink;
    }

	/**
	 * @return the id which is is CDE IDSEQ
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id is CDE IDSEQ
	 */
	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DataElementDetails [publicId=" + publicId + ", version=" + version + ", formattedVersion="
				+ formattedVersion + ", longName=" + longName + ", shortName=" + shortName + ", preferredQuestionText="
				+ preferredQuestionText + ", definition=" + definition + ", valueDomain=" + valueDomain
				+ ", dataElementConcept=" + dataElementConcept + ", context=" + context + ", workflowStatus="
				+ workflowStatus + ", origin=" + origin + ", registrationStatus=" + registrationStatus + ", directLink="
				+ directLink + ", id=" + id + "]";
	}

}
