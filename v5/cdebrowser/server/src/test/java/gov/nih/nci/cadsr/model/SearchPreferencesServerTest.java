/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import gov.nih.nci.cadsr.common.WorkflowStatusExcludedInitial;

public class SearchPreferencesServerTest {

	@Test
	public void testBuildSqlAlwaysExcluded() {
		String expected = "('" + WorkflowStatusExcludedInitial.RetiredDeleted.getWorkflowStatus() + "')";
		String strReceived = SearchPreferencesServer.buildSqlAlwaysExcluded();
		assertEquals(expected, strReceived);
	}
	@Test
	public void testIsValueAllowedToClient() {
		String notAllowed = WorkflowStatusExcludedInitial.RetiredDeleted.getWorkflowStatus();
		assertFalse(SearchPreferencesServer.isValueAllowedToClient(notAllowed));
		String allowed = WorkflowStatusExcludedInitial.CmteSubmtdUsed.getWorkflowStatus();
		assertTrue(SearchPreferencesServer.isValueAllowedToClient(allowed));
	}
	@Test
	public void testBuildClientSearchPreferences() {
		SearchPreferences clientExpected = new SearchPreferences();
		clientExpected.initPreferences();
		SearchPreferencesServer serverExpected = new SearchPreferencesServer();
		serverExpected.initPreferences();
		SearchPreferences clientReceived = serverExpected.buildClientSearchPreferences();
		assertEquals(clientExpected, clientReceived);
	}
	@Test
	public void testExcludedStatusSqlOne() {
		String expected = " ('" + WorkflowStatusExcludedInitial.CmteSubmtdUsed.getWorkflowStatus() + "') ";
		List<String> given = new ArrayList<>();
		given.add(WorkflowStatusExcludedInitial.CmteSubmtdUsed.getWorkflowStatus());
		String strReceived = SearchPreferencesServer.buildExcludedStatusSql(given);
		assertEquals(expected, strReceived);
	}
	@Test
	public void testExcludedStatusSqlQuote() {
		String expected = " ('Test''One') ";
		List<String> given = new ArrayList<>();
		given.add("Test'One");
		String strReceived = SearchPreferencesServer.buildExcludedStatusSql(given);
		assertEquals(expected, strReceived);
	}
	@Test
	public void testExcludedStatusSqlMix() {
		String expected = " ('Test''One', 'Test Two', 'Test'' Three') ";
		List<String> given = new ArrayList<>();
		given.add("Test'One");
		given.add("Test Two");
		given.add("Test' Three");
		String strReceived = SearchPreferencesServer.buildExcludedStatusSql(given);
		assertEquals(expected, strReceived);
	}
}
