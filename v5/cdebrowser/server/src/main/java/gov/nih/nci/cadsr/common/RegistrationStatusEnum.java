package gov.nih.nci.cadsr.common;

import java.util.ArrayList;
import java.util.List;

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

}
