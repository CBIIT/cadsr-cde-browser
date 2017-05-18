package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

public class ValueDomainModel extends BaseModel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String preferredName;
    private String preferredDefinition;
    private String longName;
    private String aslName;
    private Float version;
    private String deletedInd;
    private String latestVerInd;
    private int publicId;
    private String origin;
    private String idseq;
    private String vdIdseq;
    private String datatype;
    private String uom;
    private String dispFormat;
    private String formlName;
    private Integer maxLength;
    private Integer minLength;
    private String highVal;
    private String lowVal;
    private String charSet;
    private Integer decimalPlace;
    private String cdPrefName;
    private String cdContextName;
    private String cdLongName;    
    private Float cdVersion;
    private int cdPublicId;
    private String vdType;
    private RepresentationModel representationModel;
    private ConceptDerivationRuleModel conceptDerivationRuleModel;
    private String createdBy;
    private String vdContextName;//CDEBROWSER-760 We need VD Context name on DE Details VD Tab; added in v.5.3

	public ValueDomainModel()
    {
    }

    public String getPreferredName()
    {
        return preferredName;
    }

    public void setPreferredName( String preferredName )
    {
        this.preferredName = preferredName;
    }

    public String getPreferredDefinition()
    {
        return preferredDefinition;
    }

    public void setPreferredDefinition( String preferredDefinition )
    {
        this.preferredDefinition = preferredDefinition;
    }

    public String getLongName()
    {
        return longName;
    }

    public void setLongName( String longName )
    {
        this.longName = longName;
    }

    public String getAslName()
    {
        return aslName;
    }

    public void setAslName( String aslName )
    {
        this.aslName = aslName;
    }

    public Float getVersion()
    {
        return version;
    }

    public void setVersion( Float version )
    {
        this.version = version;
    }

    public String getDeletedInd()
    {
        return deletedInd;
    }

    public void setDeletedInd( String deletedInd )
    {
        this.deletedInd = deletedInd;
    }

    public String getLatestVerInd()
    {
        return latestVerInd;
    }

    public void setLatestVerInd( String latestVerInd )
    {
        this.latestVerInd = latestVerInd;
    }

    public int getPublicId()
    {
        return publicId;
    }

    public void setPublicId( int publicId )
    {
        this.publicId = publicId;
    }

    public String getOrigin()
    {
        return origin;
    }

    public void setOrigin( String origin )
    {
        this.origin = origin;
    }

    public String getFormlName()
    {
        return formlName;
    }

    public void setFormlName( String formlName )
    {
        this.formlName = formlName;
    }

    public String getIdseq()
    {
        return idseq;
    }

    public void setIdseq( String idseq )
    {
        this.idseq = idseq;
    }

    public String getVdIdseq()
    {
        return vdIdseq;
    }

    public void setVdIdseq( String vdIdseq )
    {
        this.vdIdseq = vdIdseq;
    }

    public String getDatatype()
    {
        return datatype;
    }

    public void setDatatype( String datatype )
    {
        this.datatype = datatype;
    }

    public String getUom()
    {
        return uom;
    }

    public void setUom( String uom )
    {
        this.uom = uom;
    }

    public String getDispFormat()
    {
        return dispFormat;
    }

    public void setDispFormat( String dispFormat )
    {
        this.dispFormat = dispFormat;
    }

    public String getHighVal()
    {
        return highVal;
    }

    public void setHighVal( String highVal )
    {
        this.highVal = highVal;
    }

    public String getLowVal()
    {
        return lowVal;
    }

    public void setLowVal( String lowVal )
    {
        this.lowVal = lowVal;
    }

    public String getCharSet()
    {
        return charSet;
    }

    public void setCharSet( String charSet )
    {
        this.charSet = charSet;
    }

    public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public Integer getMinLength() {
		return minLength;
	}

	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}

	public Integer getDecimalPlace() {
		return decimalPlace;
	}

	public void setDecimalPlace(Integer decimalPlace) {
		this.decimalPlace = decimalPlace;
	}

	public String getCdPrefName()
    {
        return cdPrefName;
    }

    public void setCdPrefName( String cdPrefName )
    {
        this.cdPrefName = cdPrefName;
    }

    public String getCdContextName()
    {
        return cdContextName;
    }

    public void setCdContextName( String cdContextName )
    {
        this.cdContextName = cdContextName;
    }
    
    public String getCdLongName()
    {
        return cdLongName;
    }

    public void setCdLongName( String cdLongName )
    {
        this.cdLongName = cdLongName;
    }    

    public Float getCdVersion()
    {
        return cdVersion;
    }

    public void setCdVersion( Float cdVersion )
    {
        this.cdVersion = cdVersion;
    }

    public int getCdPublicId()
    {
        return cdPublicId;
    }

    public void setCdPublicId( int cdPublicId )
    {
        this.cdPublicId = cdPublicId;
    }

    public String getVdType()
    {
        return vdType;
    }

    public void setVdType( String vdType )
    {
        this.vdType = vdType;
    }

    public RepresentationModel getRepresentationModel()
    {
        return representationModel;
    }

    public void setRepresentationModel( RepresentationModel representationModel )
    {
        this.representationModel = representationModel;
    }

    public ConceptDerivationRuleModel getConceptDerivationRuleModel()
    {
        return conceptDerivationRuleModel;
    }

    public void setConceptDerivationRuleModel( ConceptDerivationRuleModel conceptDerivationRuleModel )
    {
        this.conceptDerivationRuleModel = conceptDerivationRuleModel;
    }

    public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getVdContextName() {
		return vdContextName;
	}

	public void setVdContextName(String vdContextName) {
		this.vdContextName = vdContextName;
	}

	@Override
    public String toString()
    {
        return "ValueDomainModel{" +
                "preferredName='" + preferredName + '\'' +
                ", preferredDefinition='" + preferredDefinition + '\'' +
                ", longName='" + longName + '\'' +
                ", aslName='" + aslName + '\'' +
                ", version=" + version +
                ", deletedInd='" + deletedInd + '\'' +
                ", latestVerInd='" + latestVerInd + '\'' +
                ", publicId=" + publicId +
                ", origin='" + origin + '\'' +
                ", idseq='" + idseq + '\'' +
                ", vdIdseq='" + vdIdseq + '\'' +
                ", datatype='" + datatype + '\'' +
                ", uom='" + uom + '\'' +
                ", dispFormat='" + dispFormat + '\'' +
                ", formlName='" + formlName + '\'' +
                ", maxLength='" + maxLength + '\'' +
                ", minLength='" + minLength + '\'' +
                ", highVal='" + highVal + '\'' +
                ", lowVal='" + lowVal + '\'' +
                ", charSet='" + charSet + '\'' +
                ", decimalPlace='" + decimalPlace + '\'' +
                ", cdPrefName='" + cdPrefName + '\'' +
                ", cdContextName='" + cdContextName + '\'' +
                ", cdVersion=" + cdVersion +
                ", cdPublicId=" + cdPublicId +
                ", vdType='" + vdType + '\'' +
                ", vdContextName='" + vdContextName + '\'' +
                ", representationModel=" + representationModel +
                ", conceptDerivationRuleModel=" + conceptDerivationRuleModel +
                '}';
    }

    @Override
    public boolean equals( Object o )
    {
        if( this == o ) return true;
        if( !( o instanceof ValueDomainModel ) ) return false;

        ValueDomainModel that = ( ValueDomainModel ) o;

        if( getPublicId() != that.getPublicId() ) return false;
        if( getMaxLength() != that.getMaxLength() ) return false;
        if( getMinLength() != that.getMinLength() ) return false;
        if( getDecimalPlace() != that.getDecimalPlace() ) return false;
        if( getCdPublicId() != that.getCdPublicId() ) return false;
        if( getPreferredName() != null ? !getPreferredName().equals( that.getPreferredName() ) : that.getPreferredName() != null )
            return false;
        if( getPreferredDefinition() != null ? !getPreferredDefinition().equals( that.getPreferredDefinition() ) : that.getPreferredDefinition() != null )
            return false;
        if( getLongName() != null ? !getLongName().equals( that.getLongName() ) : that.getLongName() != null )
            return false;
        if( getAslName() != null ? !getAslName().equals( that.getAslName() ) : that.getAslName() != null ) return false;
        if( getVersion() != null ? !getVersion().equals( that.getVersion() ) : that.getVersion() != null ) return false;
        if( getDeletedInd() != null ? !getDeletedInd().equals( that.getDeletedInd() ) : that.getDeletedInd() != null )
            return false;
        if( getLatestVerInd() != null ? !getLatestVerInd().equals( that.getLatestVerInd() ) : that.getLatestVerInd() != null )
            return false;
        if( getOrigin() != null ? !getOrigin().equals( that.getOrigin() ) : that.getOrigin() != null ) return false;
        if( getIdseq() != null ? !getIdseq().equals( that.getIdseq() ) : that.getIdseq() != null ) return false;
        if( getVdIdseq() != null ? !getVdIdseq().equals( that.getVdIdseq() ) : that.getVdIdseq() != null ) return false;
        if( getDatatype() != null ? !getDatatype().equals( that.getDatatype() ) : that.getDatatype() != null )
            return false;
        if( getUom() != null ? !getUom().equals( that.getUom() ) : that.getUom() != null ) return false;
        if( getDispFormat() != null ? !getDispFormat().equals( that.getDispFormat() ) : that.getDispFormat() != null )
            return false;
        if( getHighVal() != null ? !getHighVal().equals( that.getHighVal() ) : that.getHighVal() != null ) return false;
        if( getLowVal() != null ? !getLowVal().equals( that.getLowVal() ) : that.getLowVal() != null ) return false;
        if( getCharSet() != null ? !getCharSet().equals( that.getCharSet() ) : that.getCharSet() != null ) return false;
        if( getCdPrefName() != null ? !getCdPrefName().equals( that.getCdPrefName() ) : that.getCdPrefName() != null )
            return false;
        if( getCdContextName() != null ? !getCdContextName().equals( that.getCdContextName() ) : that.getCdContextName() != null )
            return false;
        if( getCdVersion() != null ? !getCdVersion().equals( that.getCdVersion() ) : that.getCdVersion() != null )
            return false;
        if( getVdType() != null ? !getVdType().equals( that.getVdType() ) : that.getVdType() != null ) return false;
        if( getVdContextName() != null ? !getVdContextName().equals( that.getVdContextName() ) : that.getVdContextName() != null )
            return false;
        if( getRepresentationModel() != null ? !getRepresentationModel().equals( that.getRepresentationModel() ) : that.getRepresentationModel() != null )
            return false;
        return !( getConceptDerivationRuleModel() != null ? !getConceptDerivationRuleModel().equals( that.getConceptDerivationRuleModel() ) : that.getConceptDerivationRuleModel() != null );

    }

    @Override
    public int hashCode()
    {
        int result = getPreferredName() != null ? getPreferredName().hashCode() : 0;
        result = 31 * result + ( getPreferredDefinition() != null ? getPreferredDefinition().hashCode() : 0 );
        result = 31 * result + ( getLongName() != null ? getLongName().hashCode() : 0 );
        result = 31 * result + ( getAslName() != null ? getAslName().hashCode() : 0 );
        result = 31 * result + ( getVersion() != null ? getVersion().hashCode() : 0 );
        result = 31 * result + ( getDeletedInd() != null ? getDeletedInd().hashCode() : 0 );
        result = 31 * result + ( getLatestVerInd() != null ? getLatestVerInd().hashCode() : 0 );
        result = 31 * result + getPublicId();
        result = 31 * result + ( getOrigin() != null ? getOrigin().hashCode() : 0 );
        result = 31 * result + ( getIdseq() != null ? getIdseq().hashCode() : 0 );
        result = 31 * result + ( getVdIdseq() != null ? getVdIdseq().hashCode() : 0 );
        result = 31 * result + ( getDatatype() != null ? getDatatype().hashCode() : 0 );
        result = 31 * result + ( getUom() != null ? getUom().hashCode() : 0 );
        result = 31 * result + ( getDispFormat() != null ? getDispFormat().hashCode() : 0 );
        result = 31 * result + getMaxLength();
        result = 31 * result + getMinLength();
        result = 31 * result + ( getHighVal() != null ? getHighVal().hashCode() : 0 );
        result = 31 * result + ( getLowVal() != null ? getLowVal().hashCode() : 0 );
        result = 31 * result + ( getCharSet() != null ? getCharSet().hashCode() : 0 );
        result = 31 * result + getDecimalPlace();
        result = 31 * result + ( getCdPrefName() != null ? getCdPrefName().hashCode() : 0 );
        result = 31 * result + ( getCdContextName() != null ? getCdContextName().hashCode() : 0 );
        result = 31 * result + ( getCdVersion() != null ? getCdVersion().hashCode() : 0 );
        result = 31 * result + getCdPublicId();
        result = 31 * result + ( getVdType() != null ? getVdType().hashCode() : 0 );
        result = 31 * result + ( getVdContextName() != null ? getVdContextName().hashCode() : 0 );
        result = 31 * result + ( getRepresentationModel() != null ? getRepresentationModel().hashCode() : 0 );
        result = 31 * result + ( getConceptDerivationRuleModel() != null ? getConceptDerivationRuleModel().hashCode() : 0 );
        return result;
    }
}
