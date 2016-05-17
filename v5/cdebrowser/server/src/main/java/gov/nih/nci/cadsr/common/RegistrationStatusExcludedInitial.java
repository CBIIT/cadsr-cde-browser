package gov.nih.nci.cadsr.common;

import java.util.ArrayList;
import java.util.List;

public enum RegistrationStatusExcludedInitial
{
	RETIRED("Retired");
	private String regStatus;
	
	private RegistrationStatusExcludedInitial(String regStatus) {
		this.regStatus = regStatus;
	}

	public String getRegStatus() {
		return regStatus;
	}

	public void setRegStatus(String regStatus) {
		this.regStatus = regStatus;
	}
	
	public static List<String> getInitialExcludedList()
	{
		List<String> regStatusList = new ArrayList<String>();
		
		for (RegistrationStatusExcludedInitial rs: RegistrationStatusExcludedInitial.values())
		{
			regStatusList.add(rs.getRegStatus());
		}
		
		return regStatusList;
	}	

}
