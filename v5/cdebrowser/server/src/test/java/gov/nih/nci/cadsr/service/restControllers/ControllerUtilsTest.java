/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

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
	@Test
	public void testvalidateAndRemoveIdDupNoChange() {
		String curr1 = "99BA9DC8-2CCB-4E69-E034-080020C9C0E0";
		String curr2 = "99BA9DC8-2CCB-4E69-E034-080020C9C0E1";
		String curr3 = "99BA9DC8-2CCB-4E69-E034-080020C9C0E3";
		List<String> idseqList = new ArrayList<>();
		idseqList.add(curr1);
		idseqList.add(curr2);
		idseqList.add(curr3);
		//MUT
		List<String> idseqListReceived = ControllerUtils.validateAndRemoveIdDuplicates(idseqList);
		assertEquals(idseqList, idseqListReceived);
	}
	@Test
	public void testvalidateAndRemoveIdDupSent() {
		String curr1 = "99BA9DC8-2CCB-4E69-E034-080020C9C0E0";
		String curr11 = "99BA9DC8-2CCB-4E69-E034-080020C9C0E0";
		String curr2 = "99BA9DC8-2CCB-4E69-E034-080020C9C0E1";
		String curr21 = "99BA9DC8-2CCB-4E69-E034-080020C9C0E1";
		String curr3 = "99BA9DC8-2CCB-4E69-E034-080020C9C0E3";
		List<String> idseqList = new ArrayList<>();
		idseqList.add(curr1);
		idseqList.add(curr2);
		idseqList.add(curr3);
		List<String> idseqSent = new ArrayList<>();
		idseqSent.add(curr1);
		idseqSent.add(curr11);
		idseqSent.add(curr2);
		idseqSent.add(curr21);
		idseqSent.add(curr3);
		//MUT
		List<String> idseqListReceived = ControllerUtils.validateAndRemoveIdDuplicates(idseqList);
		assertEquals(idseqList, idseqListReceived);
	}
	public void testvalidateAndRemoveIdDupBadSent() {
		String curr10 = "99BA9DC8-2CCB-4E69-E034-080020C9C0E";//bad
		String curr1 = "99BA9DC8-2CCB-4E69-E034-080020C9C0E0";
		String curr11 = "99BA9DC8-2CCB-4E69-E034-080020C9C0E";//bad
		String curr2 = "99BA9DC8-2CCB-4E69-E034-080020C9C0E1";
		String curr21 = "99BA9DC8-2CCB-4E69-E034-080020C9C0E1";//duplicate
		String curr3 = "99BA9DC8-2CCB-4E69-E034-080020C9C0E3";
		String curr31 = "99BA9DC8-2CCB-4E69-E034080020C9C0E3";//bad
		List<String> idseqList = new ArrayList<>();
		idseqList.add(curr1);
		idseqList.add(curr2);
		idseqList.add(curr3);
		List<String> idseqSent = new ArrayList<>();
		idseqSent.add(curr10);
		idseqSent.add(curr1);
		idseqSent.add(curr11);
		idseqSent.add(curr2);
		idseqSent.add(curr21);
		idseqSent.add(curr3);
		idseqSent.add(curr31);
		//MUT
		List<String> idseqListReceived = ControllerUtils.validateAndRemoveIdDuplicates(idseqList);
		assertEquals(idseqList, idseqListReceived);
	}
}
