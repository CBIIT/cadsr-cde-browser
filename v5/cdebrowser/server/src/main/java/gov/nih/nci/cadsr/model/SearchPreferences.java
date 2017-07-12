/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gov.nih.nci.cadsr.common.RegistrationStatusExcludedInitial;
import gov.nih.nci.cadsr.common.WorkflowStatusExcludedInitial;
/**
 * This is a class to represent user session search preferences entity.
 * @author asafievan
 *
 */
public class SearchPreferences implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean excludeTest = true;
	private boolean excludeTraining = true;
	private List<String> workflowStatusExcluded = new ArrayList<String>();
	private List<String> registrationStatusExcluded = new ArrayList<String>();
	public static final String CONTEXT_EXCLUDES = "'TEST', 'Training'";
	public static final String CONTEXT_EXCLUDES_TEST = "'TEST'";
	public static final String CONTEXT_EXCLUDES_TRAINING = "'Training'";
	
	public void initPreferences() {
		excludeTest = true;
		excludeTraining = true;
		this.workflowStatusExcluded = WorkflowStatusExcludedInitial.getInitialExcludedList();
		this.registrationStatusExcluded = RegistrationStatusExcludedInitial.getInitialExcludedList();
		this.removeServerExcluded();
	}
	
	public SearchPreferences() {

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected SearchPreferences clone() {
		SearchPreferences searchPreferences = new SearchPreferences();
		searchPreferences.setExcludeTest(this.isExcludeTest());
		searchPreferences.setExcludeTraining(this.isExcludeTraining());
		searchPreferences.setRegistrationStatusExcluded(new ArrayList<>(this.getRegistrationStatusExcluded()));
		searchPreferences.setWorkflowStatusExcluded(new ArrayList<>(this.getWorkflowStatusExcluded()));
		Collections.copy(searchPreferences.getRegistrationStatusExcluded(), this.getRegistrationStatusExcluded());
		Collections.copy(searchPreferences.getWorkflowStatusExcluded(), this.getWorkflowStatusExcluded());
		return searchPreferences;
	}
	
	/**
	 * This is a deep copy constructor 
	 * @param other SearchPreferences
	 */
	public SearchPreferences(SearchPreferences other) {
		this.excludeTest = other.excludeTest;
		this.excludeTraining = other.excludeTraining;
		List<String> workflowListOther = other.workflowStatusExcluded;
		List<String> workflowListSelf = new ArrayList<>();
		for (String str : workflowListOther) {
			workflowListSelf.add(str);
		}
		this.workflowStatusExcluded = workflowListSelf;
		
		List<String> registrationListOther = other.registrationStatusExcluded;
		List<String> registrationListSelf = new ArrayList<>();
		for (String str : registrationListOther) {
			registrationListSelf.add(str);
		}
		this.registrationStatusExcluded = registrationListSelf;
		
		this.registrationStatusExcluded = other.registrationStatusExcluded;
	}
	
	public static void removeServerExcluded(List<String> workflowStatusList) {
		for (int i = 0; i < SearchPreferencesServer.excludedWorkflowStatusHiddenFromView.length; i++) {
			if (workflowStatusList.contains(SearchPreferencesServer.excludedWorkflowStatusHiddenFromView[i])) {
				workflowStatusList.remove(SearchPreferencesServer.excludedWorkflowStatusHiddenFromView[i]);
			}
		}
	}
	
	public void removeServerExcluded() {
		removeServerExcluded(this.workflowStatusExcluded);
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
	public String buildfExcludedWorkflowSql() {
		return buildfExcludedStatusSql(workflowStatusExcluded);
	}
	
	/**
	 * 
	 * @return SQL-ready String which has a list of string values of excluded statuses as " ('Aa', 'Ba',...,'Xa') ", or null if the list is empty
	 */
	public String buildfExcludedRegistrationSql() {
		return buildfExcludedStatusSql(registrationStatusExcluded);
	}
	
	/**
	 * 
	 * @return SQL-ready String which has a list of string values of excluded statuses as " ('Aa', 'Ba',...,'Xa') ", or null if the list is empty
	 */
	protected String buildfExcludedStatusSql(List<String> statusExcluded) {
		if ((statusExcluded == null) || (statusExcluded.isEmpty()))
			return null;

		StringBuilder sb = new StringBuilder(" ('");
		for (String curr : statusExcluded) {
			sb.append(curr + "', '");
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
		return "SearchPreferences [excludeTest=" + excludeTest + ", excludeTraining=" + excludeTraining
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
	//TODO change to make arrays equals even if the order of string is different, but the set of strings is the same
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SearchPreferences other = (SearchPreferences) obj;
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
