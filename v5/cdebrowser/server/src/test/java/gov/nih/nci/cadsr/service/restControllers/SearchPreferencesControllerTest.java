/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import static org.junit.Assert.*;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.common.RegistrationStatusEnum;
import gov.nih.nci.cadsr.common.WorkflowStatusEnum;
import gov.nih.nci.cadsr.dao.RegistrationStatusDAO;
import gov.nih.nci.cadsr.dao.WorkflowStatusDAO;
import gov.nih.nci.cadsr.model.SearchPreferences;
import gov.nih.nci.cadsr.model.SearchPreferencesServer;

@WebAppConfiguration
@ContextConfiguration("classpath:test-application-context.xml")
@TestExecutionListeners( {DependencyInjectionTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class SearchPreferencesControllerTest {
	@Autowired
    private RegistrationStatusDAO registrationStatusDAO;
	@Autowired
    private WorkflowStatusDAO workflowStatusDAO;
	@Before
	public void setUp() throws Exception
	{		
		Mockito.when(registrationStatusDAO.getRegnStatusesAsList()).thenReturn(RegistrationStatusEnum.getAsList());
		Mockito.when(workflowStatusDAO.getWorkflowStatusesAsList()).thenReturn(WorkflowStatusEnum.getAsList());
	}
	@Test
	public void testSaveGivenSearchPreferences() {
		HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
		HttpSession mockSession = Mockito.mock(HttpSession.class);
		SearchPreferencesController searchPreferencesController = new SearchPreferencesController();
		searchPreferencesController.setRegistrationStatusDAO(registrationStatusDAO);
		searchPreferencesController.setWorkflowStatusDAO(workflowStatusDAO);
		
		SearchPreferences searchPreferencesExpected = new SearchPreferences();

		SearchPreferencesServer searchPreferencesServer = new SearchPreferencesServer();
		searchPreferencesServer.addServerExcluded();
		
		Mockito.when(mockRequest.getSession(true)).thenReturn(mockSession);
		Mockito.doNothing().when(mockSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SEARCH_PREFERENCES), Mockito.eq(searchPreferencesServer));
		Mockito.when(mockSession.getAttribute(CaDSRConstants.USER_SESSION_WORKFLOW_STATUS_LIST)).thenReturn(WorkflowStatusEnum.getAsList());
		Mockito.when(mockSession.getAttribute(CaDSRConstants.USER_SESSION_REGISTRATION_STATUS_LIST)).thenReturn(RegistrationStatusEnum.getAsList());
		Mockito.doNothing().when(mockSession).removeAttribute(Mockito.eq(CaDSRConstants.USER_SESSION_WORKFLOW_STATUS_LIST));
		Mockito.doNothing().when(mockSession).removeAttribute(Mockito.eq(CaDSRConstants.USER_SESSION_REGISTRATION_STATUS_LIST));
		Mockito.doNothing().when(mockSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SESSION_WORKFLOW_STATUS_LIST), Mockito.eq(WorkflowStatusEnum.getAsList()));
		Mockito.doNothing().when(mockSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SESSION_REGISTRATION_STATUS_LIST), Mockito.eq(RegistrationStatusEnum.getAsList()));
		
		//MUT
		SearchPreferences received = searchPreferencesController.saveSearchPreferences(mockRequest, searchPreferencesExpected);
		
		//check
		assertEquals(searchPreferencesExpected, received);
		Mockito.verify(mockRequest).getSession(true);
		Mockito.verify(mockSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SEARCH_PREFERENCES), Mockito.eq(searchPreferencesServer));
	}
	@Test
	public void testResetSearchPreferences() {
		HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
		HttpSession mockSession = Mockito.mock(HttpSession.class);
		SearchPreferencesController searchPreferencesController = new SearchPreferencesController();
		SearchPreferences searchPreferencesExpected = new SearchPreferences();
		searchPreferencesExpected.initPreferences();
		
		SearchPreferencesServer searchPreferencesServerExpected = new SearchPreferencesServer();
		searchPreferencesServerExpected.initPreferences();
		System.out.println("searchPreferencesServerExpected: " + searchPreferencesServerExpected.toString());
		
		Mockito.when(mockRequest.getSession(true)).thenReturn(mockSession);
		Mockito.doNothing().when(mockSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SEARCH_PREFERENCES), Mockito.eq(searchPreferencesServerExpected));
		//MUT
		SearchPreferences received = searchPreferencesController.saveSearchPreferences(mockRequest, null);
		//check
		assertEquals(searchPreferencesExpected, received);
		Mockito.verify(mockRequest).getSession(true);
		Mockito.verify(mockSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SEARCH_PREFERENCES), Mockito.eq(searchPreferencesServerExpected));
	}
	@Test
	public void testRetrieveSearchPreferencesSession() {
		SearchPreferencesController searchPreferencesController = new SearchPreferencesController();
		SearchPreferences searchPreferencesExpected = new SearchPreferences();		
		searchPreferencesExpected.initPreferences();
		List<String> regList = searchPreferencesExpected.getRegistrationStatusExcluded();
		regList.add(RegistrationStatusEnum.CANDIDATE.getRegStatus());
		
		SearchPreferencesServer searchPreferencesServerExpected = new SearchPreferencesServer(searchPreferencesExpected, WorkflowStatusEnum.getAsList(), RegistrationStatusEnum.getAsList());
		//stub
		HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
		HttpSession mockSession = Mockito.mock(HttpSession.class);
		Mockito.when(mockRequest.getSession(false)).thenReturn(mockSession);
		Mockito.when(mockSession.getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES)).thenReturn(searchPreferencesServerExpected);
		Mockito.doNothing().when(mockSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SEARCH_PREFERENCES), Mockito.eq(searchPreferencesServerExpected));
		Mockito.doNothing().when(mockSession).removeAttribute(Mockito.eq(CaDSRConstants.USER_SESSION_WORKFLOW_STATUS_LIST));
		Mockito.doNothing().when(mockSession).removeAttribute(Mockito.eq(CaDSRConstants.USER_SESSION_REGISTRATION_STATUS_LIST));
		Mockito.when(mockSession.getAttribute(CaDSRConstants.USER_SESSION_WORKFLOW_STATUS_LIST)).thenReturn(WorkflowStatusEnum.getAsList());
		Mockito.when(mockSession.getAttribute(CaDSRConstants.USER_SESSION_REGISTRATION_STATUS_LIST)).thenReturn(RegistrationStatusEnum.getAsList());
		Mockito.doNothing().when(mockSession).removeAttribute(Mockito.eq(CaDSRConstants.USER_SESSION_REGISTRATION_STATUS_LIST));
		Mockito.doNothing().when(mockSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SESSION_WORKFLOW_STATUS_LIST), Mockito.eq(WorkflowStatusEnum.getAsList()));
		Mockito.doNothing().when(mockSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SESSION_REGISTRATION_STATUS_LIST), Mockito.eq(RegistrationStatusEnum.getAsList()));
		//MUT
		SearchPreferences received = searchPreferencesController.retrieveSearchPreferences(mockRequest);
		//check
		assertEquals(searchPreferencesExpected, received);
		Mockito.verify(mockRequest).getSession(false);
		Mockito.verify(mockSession).getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES);
	}
	@Test
	public void testRetrieveSearchPreferencesNew() {
		SearchPreferencesController searchPreferencesController = new SearchPreferencesController();
		SearchPreferences searchPreferencesExpected = new SearchPreferences();
		searchPreferencesExpected.initPreferences();
		SearchPreferencesServer searchPreferencesServerExpected = new SearchPreferencesServer(searchPreferencesExpected, WorkflowStatusEnum.getAsList(), RegistrationStatusEnum.getAsList());
		//stub
		HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
		HttpSession mockSession = Mockito.mock(HttpSession.class);
		Mockito.when(mockRequest.getSession(true)).thenReturn(mockSession);
		Mockito.when(mockSession.getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES)).thenReturn(null);
		Mockito.doNothing().when(mockSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SEARCH_PREFERENCES), Mockito.eq(searchPreferencesServerExpected));
		Mockito.when(mockSession.getAttribute(CaDSRConstants.USER_SESSION_WORKFLOW_STATUS_LIST)).thenReturn(WorkflowStatusEnum.getAsList());
		Mockito.when(mockSession.getAttribute(CaDSRConstants.USER_SESSION_REGISTRATION_STATUS_LIST)).thenReturn(RegistrationStatusEnum.getAsList());
		Mockito.doNothing().when(mockSession).removeAttribute(Mockito.eq(CaDSRConstants.USER_SESSION_WORKFLOW_STATUS_LIST));
		Mockito.doNothing().when(mockSession).removeAttribute(Mockito.eq(CaDSRConstants.USER_SESSION_REGISTRATION_STATUS_LIST));
		Mockito.doNothing().when(mockSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SESSION_WORKFLOW_STATUS_LIST), Mockito.eq(WorkflowStatusEnum.getAsList()));
		Mockito.doNothing().when(mockSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SESSION_REGISTRATION_STATUS_LIST), Mockito.eq(RegistrationStatusEnum.getAsList()));

		//MUT
		SearchPreferences received = searchPreferencesController.retrieveSearchPreferences(mockRequest);
		//check
		List <String> arr = received.getWorkflowStatusExcluded();
		for (int i = 0; i < SearchPreferencesServer.excludedWorkflowStatusHiddenFromView.length; i++) {
			assertFalse(arr.contains(SearchPreferencesServer.excludedWorkflowStatusHiddenFromView[i]));//always excluded in server side
		}
		List <String> arrExpected = searchPreferencesServerExpected.getWorkflowStatusExcluded();
		for (int i = 0; i < SearchPreferencesServer.excludedWorkflowStatusHiddenFromView.length; i++) {
			assertTrue(arrExpected.contains(SearchPreferencesServer.excludedWorkflowStatusHiddenFromView[i]));
		}
		
		assertEquals(searchPreferencesExpected, received);
		Mockito.verify(mockRequest).getSession(true);
		Mockito.verify(mockSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SEARCH_PREFERENCES), Mockito.eq(searchPreferencesServerExpected));
	}
	@Test
	public void testRetrieveSearchPreferencesDefault() {
		SearchPreferencesController searchPreferencesController = new SearchPreferencesController();
		SearchPreferences expected = new SearchPreferences();
		expected.initPreferences();
		//MUT
		SearchPreferences received = searchPreferencesController.retrieveSearchPreferencesDefault();
		assertEquals(expected, received);
	}
	@Test
	public void testResetSearchPreferencesToDefault() {
		SearchPreferencesController searchPreferencesController = new SearchPreferencesController();
		SearchPreferences expected = new SearchPreferences();
		expected.initPreferences();
		SearchPreferencesServer searchPreferencesServerExpected = new SearchPreferencesServer(expected, WorkflowStatusEnum.getAsList(), RegistrationStatusEnum.getAsList());
		//stub
		HttpSession mockSession = Mockito.mock(HttpSession.class);
		Mockito.doNothing().when(mockSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SEARCH_PREFERENCES), Mockito.eq(searchPreferencesServerExpected));
		Mockito.when(mockSession.getAttribute(CaDSRConstants.USER_SESSION_WORKFLOW_STATUS_LIST)).thenReturn(WorkflowStatusEnum.getAsList());
		Mockito.when(mockSession.getAttribute(CaDSRConstants.USER_SESSION_REGISTRATION_STATUS_LIST)).thenReturn(RegistrationStatusEnum.getAsList());
		Mockito.doNothing().when(mockSession).removeAttribute(Mockito.eq(CaDSRConstants.USER_SESSION_WORKFLOW_STATUS_LIST));
		Mockito.doNothing().when(mockSession).removeAttribute(Mockito.eq(CaDSRConstants.USER_SESSION_REGISTRATION_STATUS_LIST));
		Mockito.doNothing().when(mockSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SESSION_WORKFLOW_STATUS_LIST), Mockito.eq(WorkflowStatusEnum.getAsList()));
		Mockito.doNothing().when(mockSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SESSION_REGISTRATION_STATUS_LIST), Mockito.eq(RegistrationStatusEnum.getAsList()));

		//MUT
		SearchPreferences received = searchPreferencesController.resetSearchPreferencesToDefault(mockSession);
		assertEquals(expected, received);
		Mockito.verify(mockSession).setAttribute(Mockito.eq(CaDSRConstants.USER_SEARCH_PREFERENCES), Mockito.eq(searchPreferencesServerExpected));
	}
}
