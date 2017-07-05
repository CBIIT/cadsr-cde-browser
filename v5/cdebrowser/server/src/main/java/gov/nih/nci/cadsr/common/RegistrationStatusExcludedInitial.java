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
	public static boolean isRegistrationStatusValid(final String param) {
		RegistrationStatusEnum[] all = RegistrationStatusEnum.values();
		for (RegistrationStatusEnum curr : all) {
			if (curr.getRegStatus().equals(param))
				return true;
		}
		return false;
	}
	public static List<String> buildValidStatusList(final List<String> statusList) {
		List<String> cleanedUp = new ArrayList<String>();
		if (statusList != null)
			for (String status : statusList) {
				// if (isRegistrationStatusValid(status)) { // CDEBROWSER-703 - no more required as the statuses come from the DB instead of ENUM
					cleanedUp.add(status);
				// }
			}
		return cleanedUp;
	}
}
