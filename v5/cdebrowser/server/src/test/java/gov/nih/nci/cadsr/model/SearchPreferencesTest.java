/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class SearchPreferencesTest {
	@Test
	public void buildExcludedRegistrationSql3() {
		SearchPreferences searchPreferences = new SearchPreferences();
		List<String> statusRegistrationExcluded = new ArrayList<String> ();
		searchPreferences.setRegistrationStatusExcluded(statusRegistrationExcluded);
		statusRegistrationExcluded.add("element1");
		statusRegistrationExcluded.add("element2");
		statusRegistrationExcluded.add("element3");
		String expected = " ('element1', 'element2', 'element3') ";
		//MUT
		String received = searchPreferences.buildfExcludedRegistrationSql();
		assertEquals(expected, received);
	}
	@Test
	public void buildExcludedWorkflowSql2() {
		SearchPreferences searchPreferences = new SearchPreferences();
		List<String> workflowStatusExcluded = new ArrayList<String> ();
		searchPreferences.setWorkflowStatusExcluded(workflowStatusExcluded);
		workflowStatusExcluded.add("element1");
		workflowStatusExcluded.add("element2");
		String expected = " ('element1', 'element2') ";
		//MUT
		String received = searchPreferences.buildfExcludedWorkflowSql();
		assertEquals(expected, received);
	}
	@Test
	public void buildExcludedWorkflowSql3() {
		SearchPreferences searchPreferences = new SearchPreferences();
		List<String> workflowStatusExcluded = new ArrayList<String> ();
		searchPreferences.setWorkflowStatusExcluded(workflowStatusExcluded);
		workflowStatusExcluded.add("element1");
		workflowStatusExcluded.add("element2");
		workflowStatusExcluded.add("element3");
		String expected = " ('element1', 'element2', 'element3') ";
		//MUT
		String received = searchPreferences.buildfExcludedWorkflowSql();
		assertEquals(expected, received);
	}
	@Test
	public void buildExcludedWorkflowSqlOne() {
		SearchPreferences searchPreferences = new SearchPreferences();
		List<String> workflowStatusExcluded = new ArrayList<String> ();
		searchPreferences.setWorkflowStatusExcluded(workflowStatusExcluded);
		workflowStatusExcluded.add("element1");
		String expected = " ('element1') ";
		//MUT
		String received = searchPreferences.buildfExcludedWorkflowSql();
		assertEquals(expected, received);
	}
	@Test
	public void buildExcludedWorkflowSqlNull() {
		SearchPreferences searchPreferences = new SearchPreferences();
		List<String> workflowStatusExcluded = new ArrayList<String> ();
		searchPreferences.setWorkflowStatusExcluded(workflowStatusExcluded);
		//MUT
		String received = searchPreferences.buildfExcludedWorkflowSql();
		assertNull(received);
	}
	@Test
	public void buildExcludedWorkflowSqlNullNull() {
		SearchPreferences searchPreferences = new SearchPreferences();
		searchPreferences.setWorkflowStatusExcluded(null);
		//MUT
		String received = searchPreferences.buildfExcludedWorkflowSql();
		assertNull(received);
	}
	@Test
	public void buildExcludedRegistrationSqlNull() {
		SearchPreferences searchPreferences = new SearchPreferences();
		List<String> registrationStatusExcluded = new ArrayList<String> ();
		searchPreferences.setWorkflowStatusExcluded(registrationStatusExcluded);
		//MUT
		String received = searchPreferences.buildfExcludedRegistrationSql();
		assertNull(received);
	}
	@Test
	public void buildExcludedContextSqlDefault() {
		SearchPreferences searchPreferences = new SearchPreferences();
		//MUT
		assertEquals("'TEST', 'Training'", searchPreferences.buildContextExclided());
	}
	
	@Test
	public void buildExcludedContextSqlNone() {
		SearchPreferences searchPreferences = new SearchPreferences();
		searchPreferences.setExcludeTest(false);
		searchPreferences.setExcludeTraining(false);
		//MUT
		assertNull(searchPreferences.buildContextExclided());
	}
	
	@Test
	public void buildExcludedContextSqlTest() {
		SearchPreferences searchPreferences = new SearchPreferences();
		searchPreferences.setExcludeTraining(false);
		//MUT
		assertEquals("'TEST'", searchPreferences.buildContextExclided());
	}
	@Test
	public void buildExcludedContextSqlTraining() {
		SearchPreferences searchPreferences = new SearchPreferences();
		searchPreferences.setExcludeTest(false);
		//MUT
		assertEquals("'Training'", searchPreferences.buildContextExclided());
	}	
}
