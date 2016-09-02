package gov.nih.nci.cadsr.service.model.cdeData.adminInfo;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

public class AdminInfo
{
    private String createdBy;
    private String dateCreated;
    private String modifiedBy;
    private String dateModified;
    private String vdCreatedBy;
    private String vdOwnedBy;    
    private String decCreatedBy;
    private String decOwnedBy;
    private String organization;

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy( String createdBy )
    {
        this.createdBy = createdBy;
    }

    public String getDateCreated()
    {
        return dateCreated;
    }

    public void setDateCreated( String dateCreated )
    {
        this.dateCreated = dateCreated;
    }

    public String getModifiedBy()
    {
        return modifiedBy;
    }

    public void setModifiedBy( String modifiedBy )
    {
        this.modifiedBy = modifiedBy;
    }

    public String getDateModified()
    {
        return dateModified;
    }

    public void setDateModified( String dateModified )
    {
        this.dateModified = dateModified;
    }

	public String getVdCreatedBy() {
		return vdCreatedBy;
	}

	public void setVdCreatedBy(String vdCreatedBy) {
		this.vdCreatedBy = vdCreatedBy;
	}

	public String getVdOwnedBy() {
		return vdOwnedBy;
	}

	public void setVdOwnedBy(String vdOwnedBy) {
		this.vdOwnedBy = vdOwnedBy;
	}

	public String getDecCreatedBy() {
		return decCreatedBy;
	}

	public void setDecCreatedBy(String decCreatedBy) {
		this.decCreatedBy = decCreatedBy;
	}

	public String getDecOwnedBy() {
		return decOwnedBy;
	}

	public void setDecOwnedBy(String decOwnedBy) {
		this.decOwnedBy = decOwnedBy;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	@Override
	public String toString() {
		return "AdminInfo [createdBy=" + createdBy + ", dateCreated=" + dateCreated + ", modifiedBy=" + modifiedBy
				+ ", dateModified=" + dateModified + ", vdCreatedBy=" + vdCreatedBy + ", vdOwnedBy=" + vdOwnedBy
				+ ", decCreatedBy=" + decCreatedBy + ", decOwnedBy=" + decOwnedBy + ", organization=" + organization
				+ "]";
	}
	
}
