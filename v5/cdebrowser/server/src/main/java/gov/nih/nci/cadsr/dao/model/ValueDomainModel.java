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
    private String cdLongName;    // CDEBROWSER-798 UI Edits and Fixes - Compare Screen Matrix
    private Float cdVersion;
    private int cdPublicId;
    private String vdType;
    private RepresentationModel representationModel;
    private ConceptDerivationRuleModel conceptDerivationRuleModel;
    private String createdBy;
    private String vdContextName;//CDEBROWSER-760 We need VD Context name on DE Details VD Tab; added in v.5.3
    private AcRegistrationsModel vdRegistrationsModel;
    
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
    
    // CDEBROWSER-798 UI Edits and Fixes - Compare Screen Matrix - begin
    
    public String getCdLongName()
    {
        return cdLongName;
    }

    public void setCdLongName( String cdLongName )
    {
        this.cdLongName = cdLongName;
    }
    
    // CDEBROWSER-798 UI Edits and Fixes - Compare Screen Matrix - end
    
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

	public AcRegistrationsModel getVdRegistrationsModel() {
		return vdRegistrationsModel;
	}

	public void setVdRegistrationsModel(AcRegistrationsModel vdRegistrationsModel) {
		this.vdRegistrationsModel = vdRegistrationsModel;
	}

	@Override
	public String toString() {
		return "ValueDomainModel [preferredName=" + preferredName + ", preferredDefinition=" + preferredDefinition
				+ ", longName=" + longName + ", aslName=" + aslName + ", version=" + version + ", deletedInd="
				+ deletedInd + ", latestVerInd=" + latestVerInd + ", publicId=" + publicId + ", origin=" + origin
				+ ", idseq=" + idseq + ", vdIdseq=" + vdIdseq + ", datatype=" + datatype + ", uom=" + uom
				+ ", dispFormat=" + dispFormat + ", formlName=" + formlName + ", maxLength=" + maxLength
				+ ", minLength=" + minLength + ", highVal=" + highVal + ", lowVal=" + lowVal + ", charSet=" + charSet
				+ ", decimalPlace=" + decimalPlace + ", cdPrefName=" + cdPrefName + ", cdContextName=" + cdContextName
				+ ", cdLongName=" + cdLongName + ", cdVersion=" + cdVersion + ", cdPublicId=" + cdPublicId + ", vdType="
				+ vdType + ", representationModel=" + representationModel + ", conceptDerivationRuleModel="
				+ conceptDerivationRuleModel + ", createdBy=" + createdBy + ", vdContextName=" + vdContextName
				+ ", vdRegistrationsModel=" + vdRegistrationsModel + ", getDateCreated()=" + getDateCreated()
				+ ", getModifiedBy()=" + getModifiedBy() + ", getDateModified()=" + getDateModified()
				+ ", getFormattedVersion()=" + getFormattedVersion() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aslName == null) ? 0 : aslName.hashCode());
		result = prime * result + ((cdContextName == null) ? 0 : cdContextName.hashCode());
		result = prime * result + ((cdLongName == null) ? 0 : cdLongName.hashCode());
		result = prime * result + ((cdPrefName == null) ? 0 : cdPrefName.hashCode());
		result = prime * result + cdPublicId;
		result = prime * result + ((cdVersion == null) ? 0 : cdVersion.hashCode());
		result = prime * result + ((charSet == null) ? 0 : charSet.hashCode());
		result = prime * result + ((conceptDerivationRuleModel == null) ? 0 : conceptDerivationRuleModel.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((datatype == null) ? 0 : datatype.hashCode());
		result = prime * result + ((decimalPlace == null) ? 0 : decimalPlace.hashCode());
		result = prime * result + ((deletedInd == null) ? 0 : deletedInd.hashCode());
		result = prime * result + ((dispFormat == null) ? 0 : dispFormat.hashCode());
		result = prime * result + ((formlName == null) ? 0 : formlName.hashCode());
		result = prime * result + ((highVal == null) ? 0 : highVal.hashCode());
		result = prime * result + ((idseq == null) ? 0 : idseq.hashCode());
		result = prime * result + ((latestVerInd == null) ? 0 : latestVerInd.hashCode());
		result = prime * result + ((longName == null) ? 0 : longName.hashCode());
		result = prime * result + ((lowVal == null) ? 0 : lowVal.hashCode());
		result = prime * result + ((maxLength == null) ? 0 : maxLength.hashCode());
		result = prime * result + ((minLength == null) ? 0 : minLength.hashCode());
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
		result = prime * result + ((preferredDefinition == null) ? 0 : preferredDefinition.hashCode());
		result = prime * result + ((preferredName == null) ? 0 : preferredName.hashCode());
		result = prime * result + publicId;
		result = prime * result + ((representationModel == null) ? 0 : representationModel.hashCode());
		result = prime * result + ((uom == null) ? 0 : uom.hashCode());
		result = prime * result + ((vdContextName == null) ? 0 : vdContextName.hashCode());
		result = prime * result + ((vdIdseq == null) ? 0 : vdIdseq.hashCode());
		result = prime * result + ((vdRegistrationsModel == null) ? 0 : vdRegistrationsModel.hashCode());
		result = prime * result + ((vdType == null) ? 0 : vdType.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ValueDomainModel other = (ValueDomainModel) obj;
		if (aslName == null) {
			if (other.aslName != null)
				return false;
		} else if (!aslName.equals(other.aslName))
			return false;
		if (cdContextName == null) {
			if (other.cdContextName != null)
				return false;
		} else if (!cdContextName.equals(other.cdContextName))
			return false;
		if (cdLongName == null) {
			if (other.cdLongName != null)
				return false;
		} else if (!cdLongName.equals(other.cdLongName))
			return false;
		if (cdPrefName == null) {
			if (other.cdPrefName != null)
				return false;
		} else if (!cdPrefName.equals(other.cdPrefName))
			return false;
		if (cdPublicId != other.cdPublicId)
			return false;
		if (cdVersion == null) {
			if (other.cdVersion != null)
				return false;
		} else if (!cdVersion.equals(other.cdVersion))
			return false;
		if (charSet == null) {
			if (other.charSet != null)
				return false;
		} else if (!charSet.equals(other.charSet))
			return false;
		if (conceptDerivationRuleModel == null) {
			if (other.conceptDerivationRuleModel != null)
				return false;
		} else if (!conceptDerivationRuleModel.equals(other.conceptDerivationRuleModel))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (datatype == null) {
			if (other.datatype != null)
				return false;
		} else if (!datatype.equals(other.datatype))
			return false;
		if (decimalPlace == null) {
			if (other.decimalPlace != null)
				return false;
		} else if (!decimalPlace.equals(other.decimalPlace))
			return false;
		if (deletedInd == null) {
			if (other.deletedInd != null)
				return false;
		} else if (!deletedInd.equals(other.deletedInd))
			return false;
		if (dispFormat == null) {
			if (other.dispFormat != null)
				return false;
		} else if (!dispFormat.equals(other.dispFormat))
			return false;
		if (formlName == null) {
			if (other.formlName != null)
				return false;
		} else if (!formlName.equals(other.formlName))
			return false;
		if (highVal == null) {
			if (other.highVal != null)
				return false;
		} else if (!highVal.equals(other.highVal))
			return false;
		if (idseq == null) {
			if (other.idseq != null)
				return false;
		} else if (!idseq.equals(other.idseq))
			return false;
		if (latestVerInd == null) {
			if (other.latestVerInd != null)
				return false;
		} else if (!latestVerInd.equals(other.latestVerInd))
			return false;
		if (longName == null) {
			if (other.longName != null)
				return false;
		} else if (!longName.equals(other.longName))
			return false;
		if (lowVal == null) {
			if (other.lowVal != null)
				return false;
		} else if (!lowVal.equals(other.lowVal))
			return false;
		if (maxLength == null) {
			if (other.maxLength != null)
				return false;
		} else if (!maxLength.equals(other.maxLength))
			return false;
		if (minLength == null) {
			if (other.minLength != null)
				return false;
		} else if (!minLength.equals(other.minLength))
			return false;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		if (preferredDefinition == null) {
			if (other.preferredDefinition != null)
				return false;
		} else if (!preferredDefinition.equals(other.preferredDefinition))
			return false;
		if (preferredName == null) {
			if (other.preferredName != null)
				return false;
		} else if (!preferredName.equals(other.preferredName))
			return false;
		if (publicId != other.publicId)
			return false;
		if (representationModel == null) {
			if (other.representationModel != null)
				return false;
		} else if (!representationModel.equals(other.representationModel))
			return false;
		if (uom == null) {
			if (other.uom != null)
				return false;
		} else if (!uom.equals(other.uom))
			return false;
		if (vdContextName == null) {
			if (other.vdContextName != null)
				return false;
		} else if (!vdContextName.equals(other.vdContextName))
			return false;
		if (vdIdseq == null) {
			if (other.vdIdseq != null)
				return false;
		} else if (!vdIdseq.equals(other.vdIdseq))
			return false;
		if (vdRegistrationsModel == null) {
			if (other.vdRegistrationsModel != null)
				return false;
		} else if (!vdRegistrationsModel.equals(other.vdRegistrationsModel))
			return false;
		if (vdType == null) {
			if (other.vdType != null)
				return false;
		} else if (!vdType.equals(other.vdType))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
}
