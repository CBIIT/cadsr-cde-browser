package gov.nih.nci.cadsr.service.model.cdeData.valueDomain;

/**
 * Created by lernermh on 4/22/15.
 */
public class ValueDomainDetails
{
    private int publicId;
    private float version;
    private String formattedVersion;
    private String longName;
    private String shortName;
    private String context;
    private String definition;
    private String workflowStatus;
    private String registrationStatus; // CDEBROWSER-832 This is DE Registration Status
    private String dataType;
    private String unitOfMeasure;
    private String displayFormat;
    private Integer maximumLength;
    private Integer minimumLength;
    private Integer decimalPlace;
    private String highValue;
    private String lowValue;
    private String valueDomainType;
    private int conceptualDomainPublicId;
    private String conceptualDomainShortName;
    private String conceptualDomainContextName;
    private String conceptualDomainLongName;    //CDEBROWSER-798 UI Edits and Fixes - Compare Screen Matrix
    private Float conceptualDomainVersion;
    private String formattedConceptualDomainVersion;
    private String origin;
    private String vdRegistrationStatus; // CDEBROWSER-832 Value Domain Registration status

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
        this.formattedVersion = Float.toString( Float.valueOf(version) );
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
 // CDEBROWSER-832 UI Edits - Value Domain View Details - Backend
    public String getRegistrationStatus()
    {
        return registrationStatus;
    }
 // CDEBROWSER-832 UI Edits - Value Domain View Details - Backend
    public void setRegistrationStatus( String registrationStatus )
    {
        this.registrationStatus = registrationStatus;
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

    public Integer getMaximumLength() {
		return maximumLength;
	}

	public void setMaximumLength(Integer maximumLength) {
		this.maximumLength = maximumLength;
	}

	public Integer getMinimumLength() {
		return minimumLength;
	}

	public void setMinimumLength(Integer minimumLength) {
		this.minimumLength = minimumLength;
	}

	public Integer getDecimalPlace() {
		return decimalPlace;
	}

	public void setDecimalPlace(Integer decimalPlace) {
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
  // CDEBROWSER-798 UI Edits and Fixes - Compare Screen Matrix - begin  
    public String getConceptualDomainLongName()
    {
        return conceptualDomainLongName;
    }

    public void setConceptualDomainLongName( String conceptualDomainLongName )
    {
        this.conceptualDomainLongName = conceptualDomainLongName;
    }    
   // CDEBROWSER-798 UI Edits and Fixes - Compare Screen Matrix - end
    
    public Float getConceptualDomainVersion()
    {
        return conceptualDomainVersion;
    }

    public void setConceptualDomainVersion( Float conceptualDomainVersion )
    {
        this.conceptualDomainVersion = conceptualDomainVersion;
        this.formattedConceptualDomainVersion = Float.toString( Float.valueOf(conceptualDomainVersion) );

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

	public String getVdRegistrationStatus() {
		return vdRegistrationStatus;
	}

	public void setVdRegistrationStatus(String vdRegistrationStatus) {
		this.vdRegistrationStatus = vdRegistrationStatus;
	}

	@Override
	public String toString() {
		return "ValueDomainDetails [publicId=" + publicId + ", version=" + version + ", formattedVersion="
				+ formattedVersion + ", longName=" + longName + ", shortName=" + shortName + ", context=" + context
				+ ", definition=" + definition + ", workflowStatus=" + workflowStatus + ", registrationStatus="
				+ registrationStatus + ", dataType=" + dataType + ", unitOfMeasure=" + unitOfMeasure
				+ ", displayFormat=" + displayFormat + ", maximumLength=" + maximumLength + ", minimumLength="
				+ minimumLength + ", decimalPlace=" + decimalPlace + ", highValue=" + highValue + ", lowValue="
				+ lowValue + ", valueDomainType=" + valueDomainType + ", conceptualDomainPublicId="
				+ conceptualDomainPublicId + ", conceptualDomainShortName=" + conceptualDomainShortName
				+ ", conceptualDomainContextName=" + conceptualDomainContextName + ", conceptualDomainLongName="
				+ conceptualDomainLongName + ", conceptualDomainVersion=" + conceptualDomainVersion
				+ ", formattedConceptualDomainVersion=" + formattedConceptualDomainVersion + ", origin=" + origin
				+ ", vdRegistrationStatus=" + vdRegistrationStatus + "]";
	}

}
