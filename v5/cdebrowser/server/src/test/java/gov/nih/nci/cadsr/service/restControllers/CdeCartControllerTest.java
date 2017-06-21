/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import gov.nih.nci.cadsr.cdecart.CdeCartUtilInterface;
import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.dao.DataElementDerivationDAO;
import gov.nih.nci.cadsr.error.AutheticationFailureException;
import gov.nih.nci.cadsr.service.model.search.SearchNode;
import gov.nih.nci.objectCart.client.ObjectCartException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-application-context.xml")
@WebAppConfiguration
public class CdeCartControllerTest {
	@Autowired MockHttpServletRequest mockRequest;
	@Autowired
	CdeCartController cdeCartController;
	@Autowired
	CdeCartUtilInterface cdeCartUtil;//this is a mock object
	@Autowired
	DataElementDerivationDAO mockDataElementDerivationDAO;

	@Before
	public void setUp() throws Exception {
		Mockito.when(mockDataElementDerivationDAO.getDataElementDerivationIdseqList(Mockito.anyList())).thenReturn(new ArrayList<>());
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test(expected=AutheticationFailureException.class)
	public void testRetrieveObjectCartNoUserName() throws AutheticationFailureException {
		HttpSession mockSession = Mockito.mock(HttpSession.class);
		Mockito.when(mockSession.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME)).thenReturn(null);
		//MUT
		cdeCartController.retrieveObjectCartWithException(mockSession);
		Mockito.verifyZeroInteractions(cdeCartUtil);
	}
	
	@Test(expected=AutheticationFailureException.class)
	public void testRetrieveObjectCartNoSession() throws AutheticationFailureException {
		//MUT
		cdeCartController.retrieveObjectCartWithException(null);
		
		Mockito.verifyZeroInteractions(cdeCartUtil);
	}
	protected SearchNode[] helperCallRetrieveMUT(HttpSession mockSession) throws Exception{
		ResponseEntity<?> received = cdeCartController.retrieveObjectCartWithException(mockSession);
		Object objReceived = received.getBody();
		assertNotNull(objReceived);
		assertTrue(objReceived instanceof SearchNode[]);
		SearchNode[] arrOfNodesReceived = (SearchNode[])objReceived;
		return arrOfNodesReceived;
	}
	@Test
	public void testRetrieveObjectCartSuccessEmptyResult() throws Exception {
		HttpSession mockSession = Mockito.mock(HttpSession.class);
		Mockito.when(mockSession.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME)).thenReturn("testUser");
		List<SearchNode> searchNodeList =  new ArrayList<SearchNode>();
		Mockito.when(cdeCartUtil.findCartNodes(mockSession, "testUser")).thenReturn(searchNodeList);
		//MUT
		SearchNode[] arrOfNodesReceived = helperCallRetrieveMUT(mockSession);
		assertEquals(0, arrOfNodesReceived.length);
		Mockito.verify(cdeCartUtil).findCartNodes(mockSession, "testUser");
		Mockito.reset(cdeCartUtil);
		Mockito.verifyZeroInteractions(cdeCartUtil);
	}
	@Test
	public void testRetrieveObjectCartSuccess() throws Exception {
		HttpSession mockSession = Mockito.mock(HttpSession.class);
		Mockito.when(mockSession.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME)).thenReturn("testUser");
		List<SearchNode> searchNodeList =  new ArrayList<SearchNode>();
		SearchNode searchNodeExpected1 = new SearchNode();
		searchNodeExpected1.setDeIdseq("testSeqId1");
		searchNodeExpected1.setLongName("testlongName1");
		searchNodeList.add(searchNodeExpected1);
		SearchNode searchNodeExpected2 = new SearchNode();
		searchNodeExpected1.setDeIdseq("testSeqId2");
		searchNodeExpected1.setLongName("testlongName2");
		searchNodeList.add(searchNodeExpected2);
		Mockito.when(cdeCartUtil.findCartNodes(mockSession, "testUser")).thenReturn(searchNodeList);
		//MUT
		SearchNode[] received = helperCallRetrieveMUT(mockSession);
		
		assertNotNull(received);
		assertEquals(2, received.length);
		assertEquals(searchNodeExpected1, received[0]);
		assertEquals(searchNodeExpected2, received[1]);
		Mockito.verify(cdeCartUtil).findCartNodes(mockSession, "testUser");
		Mockito.reset(cdeCartUtil);
		Mockito.verifyZeroInteractions(cdeCartUtil);
	}
	
	@Test
	public void testRetrieveObjectCartError() throws Exception {
		HttpSession mockSession = Mockito.mock(HttpSession.class);
		Mockito.when(mockSession.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME)).thenReturn("testUser3");
		
		ObjectCartException expectedException = new ObjectCartException("test exception3");
		
		Mockito.doThrow(expectedException).when(cdeCartUtil).findCartNodes(mockSession, "testUser3");

		String expectedMessage = CdeCartController.buildGetCartErrorMessage(expectedException, "testUser3");
		//MUT
		ResponseEntity<?> received = cdeCartController.retrieveObjectCartWithException(mockSession);
		
		//check
		Object objReceived = received.getBody();
		assertNotNull(objReceived);
		assertTrue(objReceived instanceof String);
		String messageReceived = (String)objReceived;
		
		assertNotNull(received);
		assertEquals(expectedMessage, messageReceived);
		Mockito.verify(cdeCartUtil).findCartNodes(mockSession, "testUser3");
		Mockito.reset(cdeCartUtil);
		Mockito.verifyZeroInteractions(cdeCartUtil);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=AutheticationFailureException.class)
	public void testSaveObjectCartNoUserName() throws AutheticationFailureException {
		HttpSession mockSession = Mockito.mock(HttpSession.class);
		Mockito.when(mockSession.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME)).thenReturn(null);
		RequestEntity<List<String>> request = Mockito.mock(RequestEntity.class);
		//MUT
		cdeCartController.saveCart(mockSession, request);
		
		Mockito.reset(cdeCartUtil);
		Mockito.verifyZeroInteractions(cdeCartUtil);
	}
	@SuppressWarnings("unchecked")
	@Test(expected=AutheticationFailureException.class)
	public void testSaveObjectCartNoSession() throws AutheticationFailureException {
		RequestEntity<List<String>> request = Mockito.mock(RequestEntity.class);
		//MUT
		cdeCartController.saveCart(null, request);
		
		Mockito.verifyZeroInteractions(cdeCartUtil);
	}	
	@Test(expected=AutheticationFailureException.class)
	public void testDeleteObjectCartNoUserName() throws AutheticationFailureException {
		HttpSession mockSession = Mockito.mock(HttpSession.class);
		Mockito.when(mockSession.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME)).thenReturn(null);
		
		cdeCartController.deleteFromCart(mockSession, null);
		
		Mockito.reset(cdeCartUtil);
		Mockito.verifyZeroInteractions(cdeCartUtil);
	}
	
	@Test(expected=AutheticationFailureException.class)
	public void testDeleteObjectCartNoSession() throws AutheticationFailureException {
		//MUT
		cdeCartController.deleteFromCart(null, null);
		
		Mockito.reset(cdeCartUtil);
		Mockito.verifyZeroInteractions(cdeCartUtil);
	}
	@Test 
	public void testDeleteObjectCartNoIDs() throws Exception {
		HttpSession mockSession = Mockito.mock(HttpSession.class);
		Mockito.when(mockSession.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME)).thenReturn("testUser1");
		List<String> expected1 =  new ArrayList<String>();

		RequestEntity<List<String>> request = Mockito.mock(RequestEntity.class);
		Mockito.when(request.getBody()).thenReturn(expected1);
		//MUT
		ResponseEntity<String> received = cdeCartController.deleteFromCart(mockSession, request);
		
		assertNotNull(received);
		assertEquals("Done", received.getBody());
		assertEquals(HttpStatus.OK, received.getStatusCode());
		
		Mockito.reset(cdeCartUtil);
		Mockito.verifyZeroInteractions(cdeCartUtil);
	}
	
	@Test
	public void testDeleteObjectCartSuccess() throws Exception {
		HttpSession mockSession = Mockito.mock(HttpSession.class);
		Mockito.when(mockSession.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME)).thenReturn("testUser1");
		
		String[] expected = {"1", "2"};
		List<String> expected1 =  new ArrayList<String>();
		expected1.add("1");
		expected1.add("2");
		ObjectCartException expectedException = new ObjectCartException("test exception1");
		
		Mockito.doNothing().when(cdeCartUtil).deleteCartNodes(mockSession, "testUser1", expected);
		RequestEntity<List<String>> request = Mockito.mock(RequestEntity.class);
		Mockito.when(request.getBody()).thenReturn(expected1);

		//MUT
		ResponseEntity<String> received = cdeCartController.deleteFromCart(mockSession, request);
		
		assertNotNull(received);
		assertEquals("Done", received.getBody());
		assertEquals(HttpStatus.OK, received.getStatusCode());
		Mockito.verify(cdeCartUtil).deleteCartNodes(mockSession, "testUser1", expected);
		Mockito.reset(cdeCartUtil);
		Mockito.verifyZeroInteractions(cdeCartUtil);
	}
	
	@Test
	public void testDeleteObjectCartError() throws Exception {
		HttpSession mockSession = Mockito.mock(HttpSession.class);
		Mockito.when(mockSession.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME)).thenReturn("testUser1");
		String[] expected = {"1", "2"};
		List<String> expected1 =  new ArrayList<String>();
		expected1.add("1");
		expected1.add("2");
		ObjectCartException expectedException = new ObjectCartException("test exception1");
		String expectedMessage = CdeCartController.buildGetCartErrorMessage(expectedException, "testUser1");
		
		Mockito.doThrow(expectedException).when(cdeCartUtil).deleteCartNodes(mockSession, "testUser1", expected);
		RequestEntity<List<String>> request = Mockito.mock(RequestEntity.class);
		Mockito.when(request.getBody()).thenReturn(expected1);
		//MUT
		ResponseEntity<String> received = cdeCartController.deleteFromCart(mockSession, request);
		
		assertNotNull(received);
		assertEquals(expectedMessage, received.getBody());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, received.getStatusCode());
		Mockito.verify(cdeCartUtil).deleteCartNodes(mockSession, "testUser1", expected);
		Mockito.reset(cdeCartUtil);
		Mockito.verifyZeroInteractions(cdeCartUtil);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSaveObjectCartError() throws Exception {
		HttpSession mockSession = Mockito.mock(HttpSession.class);
		Mockito.when(mockSession.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME)).thenReturn("testUser1");
		List<String> expected =  new ArrayList<String>();
		expected.add("1");
		expected.add("2");

		ObjectCartException expectedException = new ObjectCartException("test exception2");
		String expectedMessage = CdeCartController.buildGetCartErrorMessage(expectedException, "testUser1");
		
		Mockito.doThrow(expectedException).when(cdeCartUtil).addToCart(mockSession, "testUser1", expected);
		RequestEntity<List<String>> request = Mockito.mock(RequestEntity.class);
		Mockito.when(request.getBody()).thenReturn(expected);
		
		//MUT
		ResponseEntity<String> received = cdeCartController.saveCart(mockSession, request);
		
		assertNotNull(received);
		assertEquals(expectedMessage, received.getBody());
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, received.getStatusCode());
		Mockito.verify(cdeCartUtil).addToCart(mockSession, "testUser1", expected);
		Mockito.reset(cdeCartUtil);
		Mockito.verifyZeroInteractions(cdeCartUtil);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSaveObjectCartSuccess() throws Exception {
		HttpSession mockSession = Mockito.mock(HttpSession.class);
		Mockito.when(mockSession.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME)).thenReturn("testUser1");
		List<String> expected =  new ArrayList<String>();
		expected.add("1");
		expected.add("2");
	
		Mockito.doNothing().when(cdeCartUtil).addToCart(mockSession, "testUser1", expected);
		RequestEntity<List<String>> request = Mockito.mock(RequestEntity.class);
		Mockito.when(request.getBody()).thenReturn(expected);
		
		//MUT
		ResponseEntity<String> received = cdeCartController.saveCart(mockSession, request);
		
		assertNotNull(received);
		assertEquals("Done", received.getBody());
		assertEquals(HttpStatus.OK, received.getStatusCode());
		Mockito.verify(cdeCartUtil).addToCart(mockSession, "testUser1", expected);
		Mockito.reset(cdeCartUtil);
		Mockito.verifyZeroInteractions(cdeCartUtil);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSaveObjectCartWithDerived() throws Exception {
		HttpSession mockSession = Mockito.mock(HttpSession.class);
		Mockito.when(mockSession.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME)).thenReturn("testUser1");
		List<String> given =  new ArrayList<String>();
		given.add("1");
		given.add("2");
		
		List<String> derivedFromIds = new ArrayList<>();
		derivedFromIds.add("272D9093-DD6D-2315-E044-0003BA3F9857");
		derivedFromIds.add("272DB2A0-355C-2D49-E044-0003BA3F9857");
		derivedFromIds.add("FC7048E5-CBD2-101F-E034-0003BA3F9857");
		
		List<String> expected =  new ArrayList<String>();
		expected.addAll(given);
		expected.addAll(derivedFromIds);
		
		Mockito.reset(mockDataElementDerivationDAO);
		Mockito.when(mockDataElementDerivationDAO.getDataElementDerivationIdseqList(Mockito.eq(given))).thenReturn(derivedFromIds);
		
		Mockito.doNothing().when(cdeCartUtil).addToCart(mockSession, "testUser1", expected);
		RequestEntity<List<String>> mockRequest = Mockito.mock(RequestEntity.class);
		Mockito.when(mockRequest.getBody()).thenReturn(given);
		
		//MUT
		ResponseEntity<String> received = cdeCartController.saveCart(mockSession, mockRequest);
		
		assertNotNull(received);
		assertEquals("Done", received.getBody());
		assertEquals(HttpStatus.OK, received.getStatusCode());
		Mockito.verify(cdeCartUtil).addToCart(mockSession, "testUser1", expected);
		Mockito.reset(cdeCartUtil);
		Mockito.verify(mockDataElementDerivationDAO).getDataElementDerivationIdseqList(given);
		Mockito.reset(mockDataElementDerivationDAO);
		Mockito.verifyZeroInteractions(cdeCartUtil);
		Mockito.verifyZeroInteractions(mockDataElementDerivationDAO);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testSaveObjectCartNoIds() throws Exception {
		HttpSession mockSession = Mockito.mock(HttpSession.class);
		Mockito.when(mockSession.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME)).thenReturn("testUser1");
		List<String> expected =  new ArrayList<String>();
	
		RequestEntity<List<String>> request = Mockito.mock(RequestEntity.class);
		Mockito.when(request.getBody()).thenReturn(expected);
		
		//MUT
		ResponseEntity<String> received = cdeCartController.saveCart(mockSession, request);
		
		assertNotNull(received);
		assertEquals("Done", received.getBody());
		assertEquals(HttpStatus.OK, received.getStatusCode());
	}
}
