package gov.nih.nci.cadsr.service.model.cdeData.valueDomain;

/**
 * Created by lernermh on 4/22/15.
 */
public class ValueDomainDetails
{
    private int publicId;
    private float version;
    private String longName;
    private String shortName;
    private String context;
    private String definition;
    private String workflowStatus;
    private String dataType;
    private String unitOfMeasure;
    private String displayFormat;
    private int maximumLength;
    private int minimumLength;
    private int decimalPlace;
    private String highValue;
    private String lowValue;
    private String valueDomainType;
    private int conceptualDomainPublicId;
    private String conceptualDomainShortName;
    private String conceptualDomainContextName;
    private Float getConceptualDomainVersion;
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

    public String getContext()
    {
        return context;
    }

    public void setContext( String context )
    {
        this.context = context;
    }

    public String getDefinition()
    {
        return definition;
    }

    public void setDefinition( String definition )
    {
        this.definition = definition;
    }

    public String getWorkflowStatus()
    {
        return workflowStatus;
    }

    public void setWorkflowStatus( String workflowStatus )
    {
        this.workflowStatus = workflowStatus;
    }

    public String getDataType()
    {
        return dataType;
    }

    public void setDataType( String dataType )
    {
        this.dataType = dataType;
    }

    public String getUnitOfMeasure()
    {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure( String unitOfMeasure )
    {
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getDisplayFormat()
    {
        return displayFormat;
    }

    public void setDisplayFormat( String displayFormat )
    {
        this.displayFormat = displayFormat;
    }

    public int getMaximumLength()
    {
        return maximumLength;
    }

    public void setMaximumLength( int maximumLength )
    {
        this.maximumLength = maximumLength;
    }

    public int getMinimumLength()
    {
        return minimumLength;
    }

    public void setMinimumLength( int minimumLength )
    {
        this.minimumLength = minimumLength;
    }

    public int getDecimalPlace()
    {
        return decimalPlace;
    }

    public void setDecimalPlace( int decimalPlace )
    {
        this.decimalPlace = decimalPlace;
    }

    public String getHighValue()
    {
        return highValue;
    }

    public void setHighValue( String highValue )
    {
        this.highValue = highValue;
    }

    public String getLowValue()
    {
        return lowValue;
    }

    public void setLowValue( String lowValue )
    {
        this.lowValue = lowValue;
    }

    public String getValueDomainType()
    {
        return valueDomainType;
    }

    public void setValueDomainType( String valueDomainType )
    {
        this.valueDomainType = valueDomainType;
    }

    public int getConceptualDomainPublicId()
    {
        return conceptualDomainPublicId;
    }

    public void setConceptualDomainPublicId( int conceptualDomainPublicId )
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

    public Float getGetConceptualDomainVersion()
    {
        return getConceptualDomainVersion;
    }

    public void setConceptualDomainVersion( Float getConceptualDomainVersion )
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
