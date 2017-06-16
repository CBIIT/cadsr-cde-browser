package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

public class DataElementConceptModel extends BaseDesignationDefinitionModel
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
    private String latestVerInd;//LATEST_VERSION_IND
    private int publicId;
    private String origin;//ORIGIN
    private String idseq;
    private String decIdseq;
    private String cdIdseq;
    private String proplName; //PROPL_NAME
    private String oclName; // Object Class
    private String objClassQualifier; // Object Class
    private String propertyQualifier; //PROPERTY_QUALIFIER
    private String changeNote; //CHANGE_NOTE
    private String objClassPrefName; // Object Class
    private String objClassContextName; // Object Class
    private String propertyPrefName;
    private String propertyContextName;
    private Float propertyVersion;
    private Float objClassVersion; // Object Class
    private String conteName;
    private String cdPrefName;
    private String cdLongName;//CDEBROWSER-816 Use CD Long Name in CDE View
    private String cdRegistrationStatus;//CDEBROWSER-816 Add Registration Status Remove if we do not need it
    private String cdContextName;
    private Float cdVersion;
    private int cdPublicId;
    private int objClassPublicId; // Object Class
    private PropertyModel property;
    private ObjectClassModel objectClassModel; // Object Class
    private String createdBy;

    public DataElementConceptModel()
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
        setFormattedVersion( version );
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

    public String getIdseq()
    {
        return idseq;
    }

    public void setIdseq( String idseq )
    {
        this.idseq = idseq;
    }

    public String getDecIdseq()
    {
        return decIdseq;
    }

    public void setDecIdseq( String decIdseq )
    {
        this.decIdseq = decIdseq;
    }

    public String getCdIdseq()
    {
        return cdIdseq;
    }

    public void setCdIdseq( String cdIdseq )
    {
        this.cdIdseq = cdIdseq;
    }

    public String getProplName()
    {
        return proplName;
    }

    public void setProplName( String proplName )
    {
        this.proplName = proplName;
    }

    public String getOclName()
    {
        return oclName;
    }

    public void setOclName( String oclName )
    {
        this.oclName = oclName;
    }

    public String getObjClassQualifier()
    {
        return objClassQualifier;
    }

    public void setObjClassQualifier( String objClassQualifier )
    {
        this.objClassQualifier = objClassQualifier;
    }

    public String getPropertyQualifier()
    {
        return propertyQualifier;
    }

    public void setPropertyQualifier( String propertyQualifier )
    {
        this.propertyQualifier = propertyQualifier;
    }

    public String getChangeNote()
    {
        return changeNote;
    }

    public void setChangeNote( String changeNote )
    {
        this.changeNote = changeNote;
    }

    public String getObjClassPrefName()
    {
        return objClassPrefName;
    }

    public void setObjClassPrefName( String objClassPrefName )
    {
        this.objClassPrefName = objClassPrefName;
    }

    public String getObjClassContextName()
    {
        return objClassContextName;
    }

    public void setObjClassContextName( String objClassContextName )
    {
        this.objClassContextName = objClassContextName;
    }

    public String getPropertyPrefName()
    {
        return propertyPrefName;
    }

    public void setPropertyPrefName( String propertyPrefName )
    {
        this.propertyPrefName = propertyPrefName;
    }

    public String getPropertyContextName()
    {
        return propertyContextName;
    }

    public void setPropertyContextName( String propertyContextName )
    {
        this.propertyContextName = propertyContextName;
    }

    public Float getPropertyVersion()
    {
        return propertyVersion;
    }

    public void setPropertyVersion( Float propertyVersion )
    {
        this.propertyVersion = propertyVersion;
    }

    public Float getObjClassVersion()
    {
        return objClassVersion;
    }

    public void setObjClassVersion( Float objClassVersion )
    {
        this.objClassVersion = objClassVersion;
    }

    public String getConteName()
    {
        return conteName;
    }

    public void setConteName( String conteName )
    {
        this.conteName = conteName;
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

    public int getObjClassPublicId()
    {
        return objClassPublicId;
    }

    public void setObjClassPublicId( int objClassPublicId )
    {
        this.objClassPublicId = objClassPublicId;
    }

    public PropertyModel getProperty()
    {
        return property;
    }

    public void setProperty( PropertyModel property )
    {
        this.property = property;
    }

    public ObjectClassModel getObjectClassModel()
    {
        return objectClassModel;
    }

    public void setObjectClassModel( ObjectClassModel objectClassModel )
    {
        this.objectClassModel = objectClassModel;
    }

    public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCdLongName() {
		return cdLongName;
	}

	public void setCdLongName(String cdLongName) {
		this.cdLongName = cdLongName;
	}
	
	public String getCdRegistrationStatus() {
		return cdRegistrationStatus;
	}

	public void setCdRegistrationStatus(String cdRegistrationStatus) {
		this.cdRegistrationStatus = cdRegistrationStatus;
	}

	@Override
	public String toString() {
		return "DataElementConceptModel [preferredName=" + preferredName + ", preferredDefinition="
				+ preferredDefinition + ", longName=" + longName + ", aslName=" + aslName + ", version=" + version
				+ ", deletedInd=" + deletedInd + ", latestVerInd=" + latestVerInd + ", publicId=" + publicId
				+ ", origin=" + origin + ", idseq=" + idseq + ", decIdseq=" + decIdseq + ", cdIdseq=" + cdIdseq
				+ ", proplName=" + proplName + ", oclName=" + oclName + ", objClassQualifier=" + objClassQualifier
				+ ", propertyQualifier=" + propertyQualifier + ", changeNote=" + changeNote + ", objClassPrefName="
				+ objClassPrefName + ", objClassContextName=" + objClassContextName + ", propertyPrefName="
				+ propertyPrefName + ", propertyContextName=" + propertyContextName + ", propertyVersion="
				+ propertyVersion + ", objClassVersion=" + objClassVersion + ", conteName=" + conteName
				+ ", cdPrefName=" + cdPrefName + ", cdLongName=" + cdLongName + ", cdRegistrationStatus="
				+ cdRegistrationStatus + ", cdContextName=" + cdContextName + ", cdVersion=" + cdVersion
				+ ", cdPublicId=" + cdPublicId + ", objClassPublicId=" + objClassPublicId + ", property=" + property
				+ ", objectClassModel=" + objectClassModel + ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate + ", formattedVersion="
				+ formattedVersion + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aslName == null) ? 0 : aslName.hashCode());
		result = prime * result + ((cdContextName == null) ? 0 : cdContextName.hashCode());
		result = prime * result + ((cdIdseq == null) ? 0 : cdIdseq.hashCode());
		result = prime * result + ((cdLongName == null) ? 0 : cdLongName.hashCode());
		result = prime * result + ((cdPrefName == null) ? 0 : cdPrefName.hashCode());
		result = prime * result + cdPublicId;
		result = prime * result + ((cdRegistrationStatus == null) ? 0 : cdRegistrationStatus.hashCode());
		result = prime * result + ((cdVersion == null) ? 0 : cdVersion.hashCode());
		result = prime * result + ((changeNote == null) ? 0 : changeNote.hashCode());
		result = prime * result + ((conteName == null) ? 0 : conteName.hashCode());
		result = prime * result + ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result + ((decIdseq == null) ? 0 : decIdseq.hashCode());
		result = prime * result + ((deletedInd == null) ? 0 : deletedInd.hashCode());
		result = prime * result + ((idseq == null) ? 0 : idseq.hashCode());
		result = prime * result + ((latestVerInd == null) ? 0 : latestVerInd.hashCode());
		result = prime * result + ((longName == null) ? 0 : longName.hashCode());
		result = prime * result + ((objClassContextName == null) ? 0 : objClassContextName.hashCode());
		result = prime * result + ((objClassPrefName == null) ? 0 : objClassPrefName.hashCode());
		result = prime * result + objClassPublicId;
		result = prime * result + ((objClassQualifier == null) ? 0 : objClassQualifier.hashCode());
		result = prime * result + ((objClassVersion == null) ? 0 : objClassVersion.hashCode());
		result = prime * result + ((objectClassModel == null) ? 0 : objectClassModel.hashCode());
		result = prime * result + ((oclName == null) ? 0 : oclName.hashCode());
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
		result = prime * result + ((preferredDefinition == null) ? 0 : preferredDefinition.hashCode());
		result = prime * result + ((preferredName == null) ? 0 : preferredName.hashCode());
		result = prime * result + ((property == null) ? 0 : property.hashCode());
		result = prime * result + ((propertyContextName == null) ? 0 : propertyContextName.hashCode());
		result = prime * result + ((propertyPrefName == null) ? 0 : propertyPrefName.hashCode());
		result = prime * result + ((propertyQualifier == null) ? 0 : propertyQualifier.hashCode());
		result = prime * result + ((propertyVersion == null) ? 0 : propertyVersion.hashCode());
		result = prime * result + ((proplName == null) ? 0 : proplName.hashCode());
		result = prime * result + publicId;
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
		DataElementConceptModel other = (DataElementConceptModel) obj;
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
		if (cdIdseq == null) {
			if (other.cdIdseq != null)
				return false;
		} else if (!cdIdseq.equals(other.cdIdseq))
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
		if (cdRegistrationStatus == null) {
			if (other.cdRegistrationStatus != null)
				return false;
		} else if (!cdRegistrationStatus.equals(other.cdRegistrationStatus))
			return false;
		if (cdVersion == null) {
			if (other.cdVersion != null)
				return false;
		} else if (!cdVersion.equals(other.cdVersion))
			return false;
		if (changeNote == null) {
			if (other.changeNote != null)
				return false;
		} else if (!changeNote.equals(other.changeNote))
			return false;
		if (conteName == null) {
			if (other.conteName != null)
				return false;
		} else if (!conteName.equals(other.conteName))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (decIdseq == null) {
			if (other.decIdseq != null)
				return false;
		} else if (!decIdseq.equals(other.decIdseq))
			return false;
		if (deletedInd == null) {
			if (other.deletedInd != null)
				return false;
		} else if (!deletedInd.equals(other.deletedInd))
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
		if (objClassContextName == null) {
			if (other.objClassContextName != null)
				return false;
		} else if (!objClassContextName.equals(other.objClassContextName))
			return false;
		if (objClassPrefName == null) {
			if (other.objClassPrefName != null)
				return false;
		} else if (!objClassPrefName.equals(other.objClassPrefName))
			return false;
		if (objClassPublicId != other.objClassPublicId)
			return false;
		if (objClassQualifier == null) {
			if (other.objClassQualifier != null)
				return false;
		} else if (!objClassQualifier.equals(other.objClassQualifier))
			return false;
		if (objClassVersion == null) {
			if (other.objClassVersion != null)
				return false;
		} else if (!objClassVersion.equals(other.objClassVersion))
			return false;
		if (objectClassModel == null) {
			if (other.objectClassModel != null)
				return false;
		} else if (!objectClassModel.equals(other.objectClassModel))
			return false;
		if (oclName == null) {
			if (other.oclName != null)
				return false;
		} else if (!oclName.equals(other.oclName))
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
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		if (propertyContextName == null) {
			if (other.propertyContextName != null)
				return false;
		} else if (!propertyContextName.equals(other.propertyContextName))
			return false;
		if (propertyPrefName == null) {
			if (other.propertyPrefName != null)
				return false;
		} else if (!propertyPrefName.equals(other.propertyPrefName))
			return false;
		if (propertyQualifier == null) {
			if (other.propertyQualifier != null)
				return false;
		} else if (!propertyQualifier.equals(other.propertyQualifier))
			return false;
		if (propertyVersion == null) {
			if (other.propertyVersion != null)
				return false;
		} else if (!propertyVersion.equals(other.propertyVersion))
			return false;
		if (proplName == null) {
			if (other.proplName != null)
				return false;
		} else if (!proplName.equals(other.proplName))
			return false;
		if (publicId != other.publicId)
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
	
}
