package gov.nih.nci.cadsr.common;

import java.util.ArrayList;
import java.util.List;

import gov.nih.nci.cadsr.service.model.search.SearchCriteria;

public enum RegistrationStatusEnum
{
	PREFSTD("Preferred Standard"),	
	STANDARD("Standard"),
	QUALIFIED("Qualified"),
	RECORDED("Recorded"),		
	CANDIDATE("Candidate"),
	INCOMPLETE("Incomplete"),
	PROPOSED("Proposed"),
	SUPERCEDED("Superceded"),
	STDELSEWHERE("Standardized Elsewhere"),
	APPLICATION("Application"),
	HISTORICAL("Historical"),
	RETIRED("Retired"),	
	SUSPENDED("Suspended"),
	BLANK("");

	private String regStatus;
	
	private RegistrationStatusEnum(String regStatus) {
		this.regStatus = regStatus;
	}

	public String getRegStatus() {
		return regStatus;
	}

	public void setRegStatus(String regStatus) {
		this.regStatus = regStatus;
	}
	
	public static List<String> getAsList()
	{
		List<String> regStatusList = new ArrayList<String>();
		
		for (RegistrationStatusEnum rs: RegistrationStatusEnum.values())
		{
			regStatusList.add(rs.getRegStatus());
		}
		return regStatusList;
	}
	public static boolean isValidStatus(String registrationStatus) {
		for (RegistrationStatusEnum rs : RegistrationStatusEnum.values()) {
			if(rs.getRegStatus().equals(registrationStatus)) {
					return true;
			}
		}
		if (SearchCriteria.ALL_REGISTRATION_STATUSES.equals(registrationStatus)) {
			return true;
		}
		else {
			return false;
		}
	}
}
