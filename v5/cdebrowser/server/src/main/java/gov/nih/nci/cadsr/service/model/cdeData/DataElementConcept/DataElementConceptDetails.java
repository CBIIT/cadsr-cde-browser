package gov.nih.nci.cadsr.service.model.cdeData.DataElementConcept;

public class DataElementConceptDetails
{
    private int publicId;
    private float version;
    private String longName;
    private String shortName;
    private String definition;
    private String context;
    private String workflowStatus;
    private Integer conceptualDomainPublicId;
    private String conceptualDomainShortName;
    private String conceptualDomainContextName;
    private String getConceptualDomainVersion;
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

    public String getConceptualDomainContextName()
    {
        return conceptualDomainContextName;
    }

    public void setConceptualDomainContextName( String conceptualDomainContextName )
    {
        this.conceptualDomainContextName = conceptualDomainContextName;
    }

    public String getGetConceptualDomainVersion()
    {
        return getConceptualDomainVersion;
    }

    public void setGetConceptualDomainVersion( String getConceptualDomainVersion )
    {
        this.getConceptualDomainVersion = getConceptualDomainVersion;
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
