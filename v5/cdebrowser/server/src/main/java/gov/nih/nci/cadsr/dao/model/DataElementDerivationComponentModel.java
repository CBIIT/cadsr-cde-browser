package gov.nih.nci.cadsr.dao.model;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

public class DataElementDerivationComponentModel extends BaseModel
{
    private String cdrIdseq;
	private String displayOrder;
    private String longName;
    private String context;
    private String workflowStatus;
    private String publicId;
    private String version;
    private String deIdseq;

    
    public String getCdrIdseq() {
		return cdrIdseq;
	}

	public void setCdrIdseq(String cdrIdseq) {
		this.cdrIdseq = cdrIdseq;
	}

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

	@Override
	public String toString() {
		return "DataElementDerivationComponentModel [cdrIdseq=" + cdrIdseq + ", displayOrder=" + displayOrder
				+ ", longName=" + longName + ", context=" + context + ", workflowStatus=" + workflowStatus
				+ ", publicId=" + publicId + ", version=" + version + ", deIdseq=" + deIdseq + ", getCreatedBy()="
				+ getCreatedBy() + ", getDateCreated()=" + getDateCreated() + ", getModifiedBy()=" + getModifiedBy()
				+ ", getDateModified()=" + getDateModified() + ", getFormattedVersion()=" + getFormattedVersion() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cdrIdseq == null) ? 0 : cdrIdseq.hashCode());
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result + ((deIdseq == null) ? 0 : deIdseq.hashCode());
		result = prime * result + ((displayOrder == null) ? 0 : displayOrder.hashCode());
		result = prime * result + ((longName == null) ? 0 : longName.hashCode());
		result = prime * result + ((publicId == null) ? 0 : publicId.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		result = prime * result + ((workflowStatus == null) ? 0 : workflowStatus.hashCode());
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
		DataElementDerivationComponentModel other = (DataElementDerivationComponentModel) obj;
		if (cdrIdseq == null) {
			if (other.cdrIdseq != null)
				return false;
		} else if (!cdrIdseq.equals(other.cdrIdseq))
			return false;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		if (deIdseq == null) {
			if (other.deIdseq != null)
				return false;
		} else if (!deIdseq.equals(other.deIdseq))
			return false;
		if (displayOrder == null) {
			if (other.displayOrder != null)
				return false;
		} else if (!displayOrder.equals(other.displayOrder))
			return false;
		if (longName == null) {
			if (other.longName != null)
				return false;
		} else if (!longName.equals(other.longName))
			return false;
		if (publicId == null) {
			if (other.publicId != null)
				return false;
		} else if (!publicId.equals(other.publicId))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		if (workflowStatus == null) {
			if (other.workflowStatus != null)
				return false;
		} else if (!workflowStatus.equals(other.workflowStatus))
			return false;
		return true;
	}

}
