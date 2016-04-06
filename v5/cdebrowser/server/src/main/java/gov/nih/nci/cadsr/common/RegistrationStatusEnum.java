package gov.nih.nci.cadsr.common;

import java.util.ArrayList;
import java.util.List;

public enum RegistrationStatusEnum
{
	STANDARD("Standard"),
	CANDIDATE("Candidate"),
	PROPOSED("Proposed"),
	QUALIFIED("Qualified"),
	SUPERCEDED("Superceded"),
	STDELSEWHERE("Standardized Elsewhere"),
	RETIRED("Retired"),
	APPLICATION("Application"),
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

}
