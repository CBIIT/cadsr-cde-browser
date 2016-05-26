/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.model.SearchPreferencesServer;
/**
 * 
 * @author asafievan
 *
 */
public class ControllerUtilsTest {
	SearchPreferencesServer testSearchPreferences;
	@Before
	public void setUp() {
		testSearchPreferences = new SearchPreferencesServer();
	}
	@Test
	public void testRetriveSessionSearchPreferencesExisted() {
		HttpSession mockHttpSession = Mockito.mock(HttpSession.class);
		Mockito.when(mockHttpSession.getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES)).thenReturn(testSearchPreferences);
		testSearchPreferences.initPreferences();
		testSearchPreferences.setExcludeTest(false);
		
		//MUT
		SearchPreferencesServer received = ControllerUtils.retriveSessionSearchPreferencesServer(mockHttpSession);
		//check	
		assertEquals(testSearchPreferences, received);
		Mockito.verify(mockHttpSession).getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES);
	}
	@Test
	public void testRetriveSessionSearchPreferencesNull() {
		HttpSession mockHttpSession = Mockito.mock(HttpSession.class);
		testSearchPreferences.initPreferences();
		Mockito.when(mockHttpSession.getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES)).thenReturn(null);
		Mockito.doNothing().when(mockHttpSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SEARCH_PREFERENCES), Mockito.eq(testSearchPreferences));
		testSearchPreferences.initPreferences();
		
		//MUT
		SearchPreferencesServer received = ControllerUtils.retriveSessionSearchPreferencesServer(mockHttpSession);
		//check		
		assertEquals(testSearchPreferences, received);
		Mockito.verify(mockHttpSession).getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES);
		Mockito.verify(mockHttpSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SEARCH_PREFERENCES), Mockito.eq(testSearchPreferences));
	}
	@Test
	public void testRetriveSessionSearchPreferencesWrong() {
		testSearchPreferences.initPreferences();
		HttpSession mockHttpSession = Mockito.mock(HttpSession.class);
		Mockito.when(mockHttpSession.getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES)).thenReturn(new Object());
		Mockito.doNothing().when(mockHttpSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SEARCH_PREFERENCES), Mockito.eq(testSearchPreferences));
		
		//MUT
		SearchPreferencesServer received = ControllerUtils.retriveSessionSearchPreferencesServer(mockHttpSession);
		//check
		assertEquals(testSearchPreferences, received);
		Mockito.verify(mockHttpSession).getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES);
		Mockito.verify(mockHttpSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SEARCH_PREFERENCES), Mockito.eq(testSearchPreferences));
	}
	@Test
	public void testInitSessionSearchPreferences() {
		HttpSession mockHttpSession = Mockito.mock(HttpSession.class);
		testSearchPreferences.initPreferences();
		Mockito.doNothing().when(mockHttpSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SEARCH_PREFERENCES), Mockito.eq(testSearchPreferences));
		//MUT
		SearchPreferencesServer received = ControllerUtils.initSearchPreferencesServerInSession(mockHttpSession);
		//check		
		assertEquals(testSearchPreferences, received);
		Mockito.verify(mockHttpSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SEARCH_PREFERENCES), Mockito.eq(testSearchPreferences));
	}
}
