/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
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

import gov.nih.nci.cadsr.common.AppConfig;
import gov.nih.nci.cadsr.common.UsageLog;
import gov.nih.nci.cadsr.dao.SearchDAO;
import gov.nih.nci.cadsr.dao.model.SearchModel;
import gov.nih.nci.cadsr.model.SearchPreferencesServer;
import gov.nih.nci.cadsr.service.ServiceTestUtils;
import gov.nih.nci.cadsr.service.model.search.SearchCriteria;
import gov.nih.nci.cadsr.service.model.search.SearchNode;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-application-context.xml")
@WebAppConfiguration
public class SearchControllerTest2 {
	SearchController searchController;
	@Mock
	SearchDAO mockSearchDAO = mock(SearchDAO.class);
	@Mock
	HttpSession mockHttpSession = mock(HttpSession.class);
	@Mock
	BindingResult mockBindingResult = mock(BindingResult.class);
	@Mock
	AppConfig mockAppConfig = mock(AppConfig.class);

	UsageLog usageLog = new UsageLog();
	
	@Before
	public void setUp() throws Exception {
		searchController = new SearchController();
		searchController.setSearchDAO(mockSearchDAO);
		searchController.setAppConfig(mockAppConfig);
		searchController.setUsageLog(usageLog);
	}

	@After
	public void tearDown() throws Exception {
		Mockito.reset(mockSearchDAO);
		Mockito.verifyZeroInteractions(mockSearchDAO);
		Mockito.reset(mockBindingResult);
		Mockito.verifyZeroInteractions(mockBindingResult);
		Mockito.reset(mockAppConfig);
		Mockito.verifyZeroInteractions(mockAppConfig);
	}

	@Test
	public void testRetrieveTypeaheadSearchFilteredNull() {
    	SearchCriteria searchCriteria = new SearchCriteria();
    	searchCriteria.setName("nameTest9");
		Mockito.when(mockBindingResult.hasErrors()).thenReturn(false);

		SearchNode[] received = searchController.search(searchCriteria, mockBindingResult, mockHttpSession);
		assertNotNull(received);
		assertEquals(1, received.length);
		assertEquals(SearchController.clientErrorEmptyFilteredInput, received[0].getLongName());
		Mockito.verify(mockBindingResult).hasErrors();
	}
	
	@Test
	public void testRetrieveTypeaheadSearchFilteredEmpty() {
    	SearchCriteria searchCriteria = new SearchCriteria();
    	searchCriteria.setName("nameTest9");
    	searchCriteria.setFilteredinput("");
		Mockito.when(mockBindingResult.hasErrors()).thenReturn(false);

		SearchNode[] received = searchController.search(searchCriteria, mockBindingResult, mockHttpSession);
		assertNotNull(received);
		assertEquals(1, received.length);
		assertEquals(SearchController.clientErrorEmptyFilteredInput, received[0].getLongName());
		Mockito.verify(mockBindingResult).hasErrors();
	}
	
	@Test
	public void testRetrieveTypeaheadSearchFilteredEmptyNoName() {
    	SearchCriteria searchCriteriaGiven = new SearchCriteria();
    	searchCriteriaGiven.setName("nameTest9");
    	searchCriteriaGiven.setFilteredinput("ALL+Fields");
		Mockito.when(mockBindingResult.hasErrors()).thenReturn(false);
		SearchModel searchModelGiven = ServiceTestUtils.buildTestSearchModel("123", 1234);
		SearchNode searchNodeExpected = ServiceTestUtils.buildTestSearchNode(searchModelGiven);
		SearchModel[] searchModelArrGiven = new SearchModel[1];
		searchModelArrGiven[0] = searchModelGiven;
		List<SearchModel> searchModelListGiven = Arrays.asList(searchModelArrGiven);

		Mockito.when(mockSearchDAO.getAllContexts(Mockito.eq(searchCriteriaGiven), Mockito.any(SearchPreferencesServer.class))).thenReturn(searchModelListGiven);
		Mockito.when(mockAppConfig.getCdeDataRestServiceName()).thenReturn(searchNodeExpected.getHref());
		
		//MUT
		SearchNode[] received = searchController.search(searchCriteriaGiven, mockBindingResult, mockHttpSession);
		assertNotNull(received);
		assertEquals(1, received.length);
		assertEquals(searchNodeExpected, received[0]);
		Mockito.verify(mockBindingResult).hasErrors();
		Mockito.verify(mockSearchDAO).getAllContexts(Mockito.eq(searchCriteriaGiven), Mockito.any(SearchPreferencesServer.class));
		Mockito.verify(mockAppConfig).getCdeDataRestServiceName();
	}
}
