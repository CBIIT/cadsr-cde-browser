/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.mockito.Mockito;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.model.SearchPreferences;

public class SearchPreferencesControllerTest {

	@Test
	public void testSaveGivenSearchPreferences() {
		HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
		HttpSession mockSession = Mockito.mock(HttpSession.class);
		SearchPreferencesController searchPreferencesController = new SearchPreferencesController();
		SearchPreferences searchPreferencesExpected = new SearchPreferences();
		SearchPreferences searchPreferences = new SearchPreferences();
		Mockito.when(mockRequest.getSession(true)).thenReturn(mockSession);
		Mockito.doNothing().when(mockSession).setAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES, searchPreferences);
		//MUT
		SearchPreferences received = searchPreferencesController.saveSearchPreferences(mockRequest, searchPreferences);
		//check
		assertEquals(searchPreferencesExpected, received);
		Mockito.verify(mockRequest).getSession(true);
		Mockito.verify(mockSession).setAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES, searchPreferences);
	}
	@Test
	public void testResetSearchPreferences() {
		HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
		HttpSession mockSession = Mockito.mock(HttpSession.class);
		SearchPreferencesController searchPreferencesController = new SearchPreferencesController();
		SearchPreferences searchPreferencesExpected = new SearchPreferences();
		searchPreferencesExpected.initPreferences();
		Mockito.when(mockRequest.getSession(true)).thenReturn(mockSession);
		Mockito.doNothing().when(mockSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SEARCH_PREFERENCES), Mockito.eq(searchPreferencesExpected));
		//MUT
		SearchPreferences received = searchPreferencesController.saveSearchPreferences(mockRequest, null);
		//check
		assertEquals(searchPreferencesExpected, received);
		Mockito.verify(mockRequest).getSession(true);
		Mockito.verify(mockSession);
	}
}
