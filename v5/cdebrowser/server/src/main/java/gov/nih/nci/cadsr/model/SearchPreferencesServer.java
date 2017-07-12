/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gov.nih.nci.cadsr.common.RegistrationStatusExcludedInitial;
import gov.nih.nci.cadsr.common.WorkflowStatusExcludedInitial;
/**
 * This is a class to represent user session search preferences entity.
 * The excluded Workflow statuses shown to the client, and used on the server are different.
 * The server has always excluded lists of statuses, which are not used in client search preferences.
 * 
 * @author asafievan
 *
 */
public class SearchPreferencesServer implements Serializable {
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger( SearchPreferencesServer.class.getName() );
	private boolean excludeTest = true;
	private boolean excludeTraining = true;
	private List<String> workflowStatusExcluded = new ArrayList<String>();
	private List<String> registrationStatusExcluded = new ArrayList<String>();
	public static final String CONTEXT_EXCLUDES = "'TEST', 'Training'";
	public static final String CONTEXT_EXCLUDES_TEST = "'TEST'";
	public static final String CONTEXT_EXCLUDES_TRAINING = "'Training'";
	
	//this is workflow status list of statuses which shall never be presented to the client.
	public static final String[] excludedWorkflowStatusHiddenFromView = {WorkflowStatusExcludedInitial.RetiredDeleted.getWorkflowStatus()};
	/**
	 * This will add all WorkflowStatusExcludedInitial and RegistrationStatusExcludedInitial statuses.
	 * 
	 */
	public void initPreferences() {
		excludeTest = true;
		excludeTraining = true;
		this.workflowStatusExcluded = WorkflowStatusExcludedInitial.getInitialExcludedList();
		this.registrationStatusExcluded = RegistrationStatusExcludedInitial.getInitialExcludedList();
		this.addServerExcluded();
	}
	
	public SearchPreferencesServer() {
		addServerExcluded(workflowStatusExcluded);
	}
	/**
	 * 	 * This constructor creates new excluded lists to use on the server.
	 * 
	 * @param searchPreferencesClient not null
	 * @param workflowAllowed not null to check for allowed values
	 * @param regAllowed not null to check for allowed values
	 * @return SearchPreferences with allowed values of WorkflowStatus and RegistrationStatus.
	 */
	protected SearchPreferences buildValidatedClientSearchPreferences(final SearchPreferences searchPreferencesClient,
			List<String> workflowAllowed, List<String> regAllowed) {
		SearchPreferences searchPreferencesClientValid = new SearchPreferences();
		searchPreferencesClientValid.setExcludeTest(searchPreferencesClient.isExcludeTest());
		searchPreferencesClientValid.setExcludeTraining(searchPreferencesClient.isExcludeTraining());//we have empty excluded here
		//these are empty after c'tor
		List<String> wfs = searchPreferencesClientValid.getWorkflowStatusExcluded();
		List<String> regs = searchPreferencesClientValid.getRegistrationStatusExcluded();
		for (String curr : searchPreferencesClient.getWorkflowStatusExcluded()) {
			if (workflowAllowed.contains(curr)) {
				wfs.add(curr);
			}
			else {
				logger.info("Received not allowed WorkflowStatusExcluded: " + curr);
			}
		}
		for (String curr : searchPreferencesClient.getRegistrationStatusExcluded()) {
			if (regAllowed.contains(curr)) {
				regs.add(curr);
			}
			else {
				logger.info("Received not allowed RegistrationStatusExcluded: " + curr);
			}
		}
		//SearchPreferences contain only allowed values now
		return searchPreferencesClientValid;
	}

	/**
	 * This constructor creates new excluded lists to use on the server.
	 * 
	 * @param clientPreferences SearchPreferences received from the client.
	 */
	public SearchPreferencesServer(final SearchPreferences clientPreferences, List<String> workflowStatusAllowed, List<String> registrationStatusAllowed) {
		//CDEBROWSER-703 we validate received values before Save and use validated instead of received
		SearchPreferences validatedClientPreferences = buildValidatedClientSearchPreferences(clientPreferences, workflowStatusAllowed, registrationStatusAllowed);
		this.excludeTest = validatedClientPreferences.isExcludeTest();
		this.excludeTraining = validatedClientPreferences.isExcludeTraining();
		
		List<String> workflowListclientPreferences = validatedClientPreferences.getWorkflowStatusExcluded();
		List<String> workflowListSelf = new ArrayList<>();
		for (String str : workflowListclientPreferences) {
			workflowListSelf.add(str);
		}
		
		this.workflowStatusExcluded = workflowListSelf;
		
		//none of the status in excluded shall be in client status list
		this.addServerExcluded();

		List<String> registrationListclientPreferences = validatedClientPreferences.getRegistrationStatusExcluded();
		List<String> registrationListSelf = new ArrayList<>();
		for (String str : registrationListclientPreferences) {
			registrationListSelf.add(str);
		}
		this.registrationStatusExcluded = registrationListSelf;

	}
	
	public static void addServerExcluded(List<String> workflowStatusList) {
		for (int i = 0; i < excludedWorkflowStatusHiddenFromView.length; i++) {
			if (! workflowStatusList.contains(excludedWorkflowStatusHiddenFromView[i])) {
				workflowStatusList.add(excludedWorkflowStatusHiddenFromView[i]);
			}
		}
	}
	
	public void addServerExcluded() {
		addServerExcluded(this.workflowStatusExcluded);
	}
	
	public SearchPreferences buildClientSearchPreferences() {
		SearchPreferences clientPreferences = new SearchPreferences();
		
		clientPreferences.setExcludeTest(this.excludeTest);
		clientPreferences.setExcludeTraining(this.excludeTraining);
		
		List<String> workflowListServerPreferences = getWorkflowStatusExcluded();
		List<String> workflowListToClient = new ArrayList<>();
		for (String strStatus : workflowListServerPreferences) {
			if (isValueAllowedToClient(strStatus)) {
				workflowListToClient.add(strStatus);
			}
		}
		
		clientPreferences.setWorkflowStatusExcluded(workflowListToClient);

		List<String> registrationListServerPreferences = getRegistrationStatusExcluded();
		List<String> registrationListToClient = new ArrayList<>();
		for (String str : registrationListServerPreferences) {
			registrationListToClient.add(str);
		}
		clientPreferences.setRegistrationStatusExcluded(registrationListToClient);
		return clientPreferences;
	}
	
	public static boolean isValueAllowedToClient(String workflowStatus) {	
		if (workflowStatus != null ) {
			for (int i = 0; i < excludedWorkflowStatusHiddenFromView.length; i++) {
				if (excludedWorkflowStatusHiddenFromView[i].equals(workflowStatus)) {
						return false;
				}
			}//end of for
			return true;
		}
		else return false;
	}
	public static String buildSqlAlwaysExcluded() {
		StringBuilder sb = new StringBuilder( "('" );
		for (int i = 0; i < excludedWorkflowStatusHiddenFromView.length; i++) {
			sb.append (excludedWorkflowStatusHiddenFromView[i]).append("' , '");		
		}
		sb.delete(sb.length() - 4, sb.length());
		sb.append(")");
		return sb.toString();
	}
	public boolean isExcludeTest() {
		return excludeTest;
	}
	public void setExcludeTest(boolean excludeTest) {
		this.excludeTest = excludeTest;
	}
	public boolean isExcludeTraining() {
		return excludeTraining;
	}
	public void setExcludeTraining(boolean excludeTraining) {
		this.excludeTraining = excludeTraining;
	}

	public List<String> getWorkflowStatusExcluded() {
		return workflowStatusExcluded;
	}
	/**
	 * This method does not check validity of the list values.
	 * 
	 * @param workflowStatusExcluded
	 */
	public void setWorkflowStatusExcluded(List<String> workflowStatusExcluded) {
		if (workflowStatusExcluded != null)
			this.workflowStatusExcluded = workflowStatusExcluded;
		else 
			this.workflowStatusExcluded = new ArrayList<String>();;
	}
	
	public List<String> getRegistrationStatusExcluded() {
		return registrationStatusExcluded;
	}
	public void setRegistrationStatusExcluded(List<String> registrationStatusExcluded) {
		if (registrationStatusExcluded != null)
			this.registrationStatusExcluded = registrationStatusExcluded;
		else 
			this.registrationStatusExcluded = new ArrayList<String>();

	}
	/**
	 * 
	 * @return SQL-ready String which has a list of string values of excluded statuses as " ('Aa', 'Ba',...,'Xa') ", or null if the list is empty
	 */
	public String buildExcludedWorkflowSql() {
		return buildExcludedStatusSql(workflowStatusExcluded);
	}
	
	/**
	 * 
	 * @return SQL-ready String which has a list of string values of excluded statuses as " ('Aa', 'Ba',...,'Xa') ", or null if the list is empty
	 */
	public String buildExcludedRegistrationSql() {
		return buildExcludedStatusSql(registrationStatusExcluded);
	}
	
	/**
	 * 
	 * @return SQL-ready String which has a list of string values of excluded statuses as " ('Aa', 'Ba',...,'Xa') ", or null if the list is empty
	 */
	protected static String buildExcludedStatusSql(List<String> statusExcluded) {
		if ((statusExcluded == null) || (statusExcluded.isEmpty()))
			return null;

		StringBuilder sb = new StringBuilder(" ('");
		String tmp;
		for (String curr : statusExcluded) {
			tmp = curr.replace("'", "''");//CDEBROWSER-703 status comes from DB
			sb.append(tmp + "', '");
		}
		sb.replace(sb.length() - 3, sb.length(), ") ");

		return sb.toString();
	}
	/**
	 * 
	 * @return string comma separated excluded contexts names in quotes as "'TEST', 'Training'"
	 */
	public String buildContextExclided() {
		String result = null;
		if (excludeTest && excludeTraining)
			result = CONTEXT_EXCLUDES;
		else if (excludeTest)
			result = CONTEXT_EXCLUDES_TEST;
		else if (excludeTraining)
			result = CONTEXT_EXCLUDES_TRAINING;
		return result;
	}
	
	@Override
	public String toString() {
		return "SearchPreferencesServer [excludeTest=" + excludeTest + ", excludeTraining=" + excludeTraining
				+ ", workflowStatusExcluded=" + workflowStatusExcluded + ", registrationStatusExcluded="
				+ registrationStatusExcluded + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (excludeTest ? 1231 : 1237);
		result = prime * result + (excludeTraining ? 1231 : 1237);
		result = prime * result + ((registrationStatusExcluded == null) ? 0 : registrationStatusExcluded.hashCode());
		result = prime * result + ((workflowStatusExcluded == null) ? 0 : workflowStatusExcluded.hashCode());
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
		SearchPreferencesServer other = (SearchPreferencesServer) obj;
		if (excludeTest != other.excludeTest)
			return false;
		if (excludeTraining != other.excludeTraining)
			return false;
		if (registrationStatusExcluded == null) {
			if (other.registrationStatusExcluded != null)
				return false;
		} else if (!registrationStatusExcluded.equals(other.registrationStatusExcluded))
			return false;
		if (workflowStatusExcluded == null) {
			if (other.workflowStatusExcluded != null)
				return false;
		} else if (!workflowStatusExcluded.equals(other.workflowStatusExcluded))
			return false;
		return true;
	}

}
