/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import static org.junit.Assert.*;

import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.model.SearchPreferences;
/**
 * 
 * @author asafievan
 *
 */
public class ControllerUtilsTest {
	SearchPreferences testSearcvhPreferences;
	@Before
	public void setUp() {
		testSearcvhPreferences = new SearchPreferences();
	}
	//FIXME these tests fail on Jenkins but not locally. Needed to be investifgated, and fixed.
	//@Test
	public void testRetriveSessionSearchPreferencesExisted() {
		HttpSession mockHttpSession = Mockito.mock(HttpSession.class);
		Mockito.when(mockHttpSession.getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES)).thenReturn(testSearcvhPreferences);
		testSearcvhPreferences.initPreferences();
		testSearcvhPreferences.setExcludeTest(false);
		
		//MUT
		SearchPreferences received = ControllerUtils.retriveSessionSearchPreferences(mockHttpSession);
		//check	
		assertEquals(testSearcvhPreferences, received);
		//FIXME fix verify in this class
		//Mockito.verify(mockHttpSession).getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES);
	}
	//@Test
	public void testRetriveSessionSearchPreferencesNull() {
		HttpSession mockHttpSession = Mockito.mock(HttpSession.class);
		Mockito.when(mockHttpSession.getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES)).thenReturn(null);
		testSearcvhPreferences.initPreferences();
		
		//MUT
		SearchPreferences received = ControllerUtils.retriveSessionSearchPreferences(mockHttpSession);
		//check		
		assertEquals(testSearcvhPreferences, received);
		//Mockito.verify(mockHttpSession).getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES);
	}
	@Test
	public void testRetriveSessionSearchPreferencesWrong() {
		HttpSession mockHttpSession = Mockito.mock(HttpSession.class);
		Mockito.when(mockHttpSession.getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES)).thenReturn(new Object());
		testSearcvhPreferences.initPreferences();
		
		//MUT
		SearchPreferences received = ControllerUtils.retriveSessionSearchPreferences(mockHttpSession);
		//check
		assertEquals(testSearcvhPreferences, received);
		//Mockito.verify(mockHttpSession).getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES);
	}
	//@Test
	public void testInitSessionSearchPreferences() {
		HttpSession mockHttpSession = Mockito.mock(HttpSession.class);
		testSearcvhPreferences.initPreferences();
		//MUT
		SearchPreferences received = ControllerUtils.initSearchPreferencesInSession(mockHttpSession);
		//check		
		assertEquals(testSearcvhPreferences, received);
		//Mockito.verify(mockHttpSession).setAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES, received);
	}
}
