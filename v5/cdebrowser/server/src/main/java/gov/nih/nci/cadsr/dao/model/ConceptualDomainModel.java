package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

public class ConceptualDomainModel extends BaseModel
{
	private static final long serialVersionUID = 1L;
	// these are all the fields used for now
    private String preferredName;
    private String longName;//CDEBROWSER-816 Adding CD Long name to use in CDE View
    private Float version;
    private int cdId; //aka public id
    private ContextModel contextModel;
    private String conteIdseq;

    public ConceptualDomainModel()
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

    public Float getVersion()
    {
        return version;
    }

    public void setVersion( Float version )
    {
        this.version = version;
        setFormattedVersion( this.version );
    }

    public int getCdId()
    {
        return cdId;
    }

    public void setCdId( int cdId )
    {
        this.cdId = cdId;
    }

    public ContextModel getContextModel()
    {
        return contextModel;
    }

    public void setContextModel( ContextModel contextModel )
    {
        this.contextModel = contextModel;
    }

    public String getConteIdseq()
    {
        return conteIdseq;
    }

    public void setConteIdseq( String conteIdseq )
    {
        this.conteIdseq = conteIdseq;
    }

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	@Override
	public String toString() {
		return "ConceptualDomainModel [preferredName=" + preferredName + ", longName=" + longName + ", version="
				+ version + ", cdId=" + cdId + ", contextModel=" + contextModel + ", conteIdseq=" + conteIdseq
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", modifiedBy=" + modifiedBy
				+ ", modifiedDate=" + modifiedDate + ", formattedVersion=" + formattedVersion + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cdId;
		result = prime * result + ((conteIdseq == null) ? 0 : conteIdseq.hashCode());
		result = prime * result + ((contextModel == null) ? 0 : contextModel.hashCode());
		result = prime * result + ((longName == null) ? 0 : longName.hashCode());
		result = prime * result + ((preferredName == null) ? 0 : preferredName.hashCode());
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
		ConceptualDomainModel other = (ConceptualDomainModel) obj;
		if (cdId != other.cdId)
			return false;
		if (conteIdseq == null) {
			if (other.conteIdseq != null)
				return false;
		} else if (!conteIdseq.equals(other.conteIdseq))
			return false;
		if (contextModel == null) {
			if (other.contextModel != null)
				return false;
		} else if (!contextModel.equals(other.contextModel))
			return false;
		if (longName == null) {
			if (other.longName != null)
				return false;
		} else if (!longName.equals(other.longName))
			return false;
		if (preferredName == null) {
			if (other.preferredName != null)
				return false;
		} else if (!preferredName.equals(other.preferredName))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}
}
