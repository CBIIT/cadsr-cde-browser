/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.dao.TypeaheadSearchDAO;
import gov.nih.nci.cadsr.model.SearchPreferencesServer;
import gov.nih.nci.cadsr.service.ServiceTestUtils;
import gov.nih.nci.cadsr.service.model.search.SearchCriteria;


@WebAppConfiguration
@ContextConfiguration("classpath:test-application-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TypeaheadSearchControllerTest {
	TypeaheadSearchController typeaheadSearchController;
	@Mock
	TypeaheadSearchDAO mockTypeaheadSearchDAO = mock(TypeaheadSearchDAO.class);
	@Mock
	HttpSession mockHttpSession = mock(HttpSession.class);
	@Mock
	BindingResult mockBindingResult = mock(BindingResult.class);
	
	@Before
	public void setUp() throws Exception {
		typeaheadSearchController = new TypeaheadSearchController();
		typeaheadSearchController.typeaheadSearchDAO = mockTypeaheadSearchDAO;
	}

	@After
	public void tearDown() throws Exception {
		Mockito.reset(mockBindingResult);
		Mockito.verifyZeroInteractions(mockBindingResult);
	}

	@Test
	public void testRetrieveTypeaheadSearchFullEmpty() {
		SearchCriteria searchCriteria = ServiceTestUtils.buildTestSearchCriteriaAll("test8");
		Mockito.when(mockBindingResult.hasErrors()).thenReturn(false);
		Mockito.when(mockHttpSession.getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES)).thenReturn(null);
		Mockito.when(mockTypeaheadSearchDAO.buildSearchTypeaheadByNameAndDomain(Mockito.eq(searchCriteria), Mockito.any(SearchPreferencesServer.class))).thenReturn(new ArrayList<String>());
		List<String> received = typeaheadSearchController.retrieveTypeaheadSearchFull(searchCriteria, mockBindingResult, mockHttpSession);
		assertNotNull(received);
		assertEquals(0, received.size());
		Mockito.verify(mockBindingResult).hasErrors();
		Mockito.verify(mockHttpSession).getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES);
		Mockito.verify(mockTypeaheadSearchDAO).buildSearchTypeaheadByNameAndDomain(Mockito.eq(searchCriteria), Mockito.any(SearchPreferencesServer.class));
	}
	@Test
	public void testRetrieveTypeaheadSearchBindingError() {
		SearchCriteria searchCriteria = ServiceTestUtils.buildTestSearchCriteriaAll("test7");
		Mockito.when(mockBindingResult.hasErrors()).thenReturn(true);
		Mockito.when(mockBindingResult.getErrorCount()).thenReturn(1);
		List<ObjectError> errorList = new ArrayList<>();
		ObjectError mockError = mock(ObjectError.class);
		errorList.add(mockError);
		Mockito.when(mockBindingResult.getAllErrors()).thenReturn(errorList);
		List<String> received = typeaheadSearchController.retrieveTypeaheadSearchFull(searchCriteria, mockBindingResult, mockHttpSession);
		assertNotNull(received);
		assertEquals(0, received.size());
		Mockito.verify(mockBindingResult).hasErrors();
		//bindingResult.getErrorCount() + bindingResult.getAllErrors().get(0)
		Mockito.verify(mockBindingResult).getErrorCount();
		Mockito.verify(mockBindingResult).getAllErrors();
		Mockito.verifyZeroInteractions(mockHttpSession);
		Mockito.verifyZeroInteractions(mockTypeaheadSearchDAO);   
	}
	@Test
	public void testRetrieveTypeaheadSearchOneRes() {
		SearchCriteria searchCriteria = ServiceTestUtils.buildTestSearchCriteriaAll("test7");
		Mockito.when(mockBindingResult.hasErrors()).thenReturn(false);
		List<String> resListExpected = new ArrayList<>();
		String resStrExpected = "testRes1";
		resListExpected.add(resStrExpected);
		Mockito.when(mockHttpSession.getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES)).thenReturn(null);
		Mockito.when(mockTypeaheadSearchDAO.buildSearchTypeaheadByNameAndDomain(Mockito.eq(searchCriteria), Mockito.any(SearchPreferencesServer.class))).thenReturn(resListExpected);
		List<String> received = typeaheadSearchController.retrieveTypeaheadSearchFull(searchCriteria, mockBindingResult, mockHttpSession);
		assertNotNull(received);
		assertEquals(1, received.size());
		assertEquals(resStrExpected, received.get(0));		
		Mockito.verify(mockBindingResult).hasErrors();
		Mockito.verify(mockHttpSession).getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES);
		Mockito.verify(mockTypeaheadSearchDAO).buildSearchTypeaheadByNameAndDomain(Mockito.eq(searchCriteria), Mockito.any(SearchPreferencesServer.class));
	}
}
