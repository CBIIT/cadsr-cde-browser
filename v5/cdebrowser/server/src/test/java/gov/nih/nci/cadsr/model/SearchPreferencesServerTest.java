/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.model;

import static org.junit.Assert.*;

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
		System.out.println("clientReceived:" + clientReceived);
		assertEquals(clientExpected, clientReceived);
	}
}
