package gov.nih.nci.cadsr.service.model.cdeData.dataElement;
/*
 * Copyright 2017 Leidos Biomedical Research, Inc.
 */

import java.util.List;
/**
 * 
 */
//CDEBROWSER-809
public class CsCsiForCdeDetails
{
    private Boolean hide;
    
    private List<AlternateName> usedByAlternateNames;//CDEBROWSER-809 "Separate out the Alternate names of type = "Used_By" into their own sub-table"
    private List<AlternateNameCsCsi> alternateNames;
    private List<AlternateDefinitionCsCsi> alternateDefinitions;

    public CsCsiForCdeDetails()
    {
    }
    public CsCsiForCdeDetails(CsCsi csCsi)
    {
    	this.usedByAlternateNames = csCsi.getUsedByAlternateNames();
    	this.hide = csCsi.getHide();
    }
	public Boolean getHide() {
		return hide;
	}

	public void setHide(Boolean hide) {
		this.hide = hide;
	}

	public List<AlternateName> getUsedByAlternateNames() {
		return usedByAlternateNames;
	}

	public void setUsedByAlternateNames(List<AlternateName> usedByAlternateNames) {
		this.usedByAlternateNames = usedByAlternateNames;
	}

	public List<AlternateNameCsCsi> getAlternateNames() {
		return alternateNames;
	}

	public void setAlternateNames(List<AlternateNameCsCsi> alternateNames) {
		this.alternateNames = alternateNames;
	}

	public List<AlternateDefinitionCsCsi> getAlternateDefinitions() {
		return alternateDefinitions;
	}

	public void setAlternateDefinitions(List<AlternateDefinitionCsCsi> alternateDefinitions) {
		this.alternateDefinitions = alternateDefinitions;
	}
}
