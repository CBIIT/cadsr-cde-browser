package gov.nih.nci.cadsr.service.model.cdeData.adminInfo;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.service.model.cdeData.SelectedDataElement;

public class AdminInfo
{
    private String createdBy;
    private String dateCreated;
    private String modifiedBy;
    private String dateModified;
    private String vdCreatedBy;
    private String vdOwnedBy;   
    private String vdDateCreated; // CDEBROWSER-833 UI Edits and fixes - Admin View Details - Backend
    private String vdDateModified;   // CDEBROWSER-833 UI Edits and fixes - Admin View Details - Backend
    private String decCreatedBy;
    private String decOwnedBy;
    private String decDateCreated; // CDEBROWSER-833 UI Edits and fixes - Admin View Details - Backend
    private String decDateModified;   // CDEBROWSER-833 UI Edits and fixes - Admin View Details - Backend    
    private String organization;
    private SelectedDataElement selectedDataElement = null;
    private String changeNote;

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
    
    public String getVdDateCreated()
    {
        return vdDateCreated;
    }

    public void setVdDateCreated( String vdDateCreated )
    {
        this.vdDateCreated = vdDateCreated;
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
    
    public String getVdDateModified()
    {
        return vdDateModified;
    }

    public void setVdDateModified( String vdDateModified )
    {
        this.vdDateModified = vdDateModified;
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

    public String getDecDateCreated()
    {
        return decDateCreated;
    }

    public void setDecDateCreated( String decDateCreated )
    {
        this.decDateCreated = decDateCreated;
    } 
    
    public String getDecDateModified()
    {
        return decDateModified;
    }

    public void setDecDateModified( String decDateModified )
    {
        this.decDateModified = decDateModified;
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
	
    public SelectedDataElement getSelectedDataElement()
    {
        return selectedDataElement;
    }

    public void setSelectedDataElement( SelectedDataElement selectedDataElement )
    {
        this.selectedDataElement = selectedDataElement;
    }	

	public String getChangeNote() {
		return changeNote;
	}

	public void setChangeNote(String changeNote) {
		this.changeNote = changeNote;
	}

	@Override
	public String toString() {
		return "AdminInfo [createdBy=" + createdBy + ", dateCreated=" + dateCreated + ", modifiedBy=" + modifiedBy
				+ ", dateModified=" + dateModified + ", vdCreatedBy=" + vdCreatedBy + ", vdOwnedBy=" + vdOwnedBy
				+ ", decCreatedBy=" + decCreatedBy + ", decOwnedBy=" + decOwnedBy + ", organization=" + organization
				+ "]";
	}
	
}
