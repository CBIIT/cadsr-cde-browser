/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import gov.nih.nci.cadsr.download.DownloadUtilsTest;
import gov.nih.nci.cadsr.download.GetExcelDownloadInterface;
import gov.nih.nci.cadsr.download.GetExcelDownloadTestImpl;
import gov.nih.nci.cadsr.service.ServerException;

@WebAppConfiguration
@ContextConfiguration("classpath:test-application-context.xml")
@TestExecutionListeners( {DependencyInjectionTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class DownloadExcelControllerTest {
//	@Autowired MockHttpServletRequest request;
//	
//	@Autowired MockHttpServletResponse response;
	
	@Autowired DownloadExcelController downloadExcelController;
	
	@Autowired GetExcelDownloadTestImpl getExcelDownload;
	
	String fileName;
	
	@Before
	public void resetGetExcelDownload() {
		downloadExcelController.setGetExcelDownload(getExcelDownload);
		assignDownloadServiceProperties(downloadExcelController);
		assignDownloadBeadProperties(getExcelDownload);
	}
	

	@After
	public void cleanUpAfterTest() {
		if (fileName != null) {
			File fileReceived = new File(fileName);
			if (fileReceived.exists());{
			//clean up the created test file	
				boolean isDeleted = fileReceived.delete();
				assertTrue(isDeleted);	
			}
			fileName = null;
		}
	}

	@Test //client error
	public void testDownloadExcelServerError() throws Exception {
		GetExcelDownloadInterface getExcelDownloadMock = Mockito.mock(GetExcelDownloadInterface.class);
		downloadExcelController.setGetExcelDownload(getExcelDownloadMock);
		String exceptionMessage = "test exception";
		Mockito.when(getExcelDownloadMock.persist(Mockito.anyCollectionOf(String.class), 
			Mockito.anyString(), Mockito.eq("deSearch")))
			.thenThrow(new ServerException(exceptionMessage));
		List<String> idList = new ArrayList<>();
		idList.add("testId1");
		String testUriStr = "http://localhost:8080/downloadExcel";
		URI uri = DownloadUtilsTest.buildTestUri(testUriStr);
		RequestEntity<List<String>> request = new RequestEntity<>(idList, HttpMethod.POST, uri);
		//MUT
		ResponseEntity <String> resp = downloadExcelController.downloadExcel("deSearch", request);
		//check
		String receivedObj = resp.getBody();
		assertNotNull(receivedObj);
		String expectedError = String.format(DownloadExcelController.serverErrorMessage, exceptionMessage);
		assertEquals(expectedError, receivedObj);
	}
	
	@Test //client error
	public void testDownloadExcelEmptyList() throws Exception {
		List<String> idList = new ArrayList<>();
		String testUriStr = "http://localhost:8080/downloadExcel";
		URI uri = DownloadUtilsTest.buildTestUri(testUriStr);
		RequestEntity<List<String>> request = new RequestEntity<>(idList, HttpMethod.POST, uri);
		//MUT
		ResponseEntity <String> resp = downloadExcelController.downloadExcel("deSearch", request);
		//check
		String receivedObj = resp.getBody();
		assertNotNull(receivedObj);
		String expectedError = DownloadExcelController.clientErrorMessageNoIDs;
		assertEquals(expectedError, receivedObj);
	}
	@Test //client error
	public void testDownloadExcelNullList() throws Exception {
		String testUriStr = "http://localhost:8080/downloadExcel";
		URI uri = DownloadUtilsTest.buildTestUri(testUriStr);
		RequestEntity<List<String>> request = new RequestEntity<>(null, HttpMethod.POST, uri);
	
		//MUT
		ResponseEntity <String> resp =  downloadExcelController.downloadExcel("deSearch", request);
		//check
		String receivedObj = resp.getBody();
		assertNotNull(receivedObj);
		String expectedError = DownloadExcelController.clientErrorMessageNoIDs;
		assertEquals(expectedError, receivedObj);		
	}
	@Test //client error
	public void testDownloadExcelNullSource() throws Exception {
		List<String> idList = new ArrayList<>();
		String testUriStr = "http://localhost:8080/downloadExcel";
		URI uri = DownloadUtilsTest.buildTestUri(testUriStr);
		RequestEntity<List<String>> request = new RequestEntity<>(idList, HttpMethod.POST, uri);
		
		//MUT
		ResponseEntity <String> resp =  downloadExcelController.downloadExcel(null, request);
		//check
		String receivedObj = resp.getBody();
		assertNotNull(receivedObj);
		String srcStr = null;
		String expectedError = String.format(DownloadExcelController.clientErrorMessageWrongParam, srcStr);
		
		assertEquals(expectedError, receivedObj);
	}
	@Test //client error
	public void testDownloadExcelWrongSource() throws Exception {
		List<String> idList = new ArrayList<>();
		idList.add("testId1");	
		String testUriStr = "http://localhost:8080/downloadExcel?src=deSearc";
		URI uri = DownloadUtilsTest.buildTestUri(testUriStr);
		RequestEntity<List<String>> request = new RequestEntity<>(idList, HttpMethod.POST, uri);

		//MUT
		ResponseEntity <String> resp = downloadExcelController.downloadExcel("deSearchWrong", request);
		//check
		String receivedObj = resp.getBody();
		assertNotNull(receivedObj);
		String expectedError = String.format(DownloadExcelController.clientErrorMessageWrongParam, "deSearchWrong");
		
		assertEquals(expectedError, receivedObj);
	}
	@Test //client error
	public void testDownloadExcelWrongFileId() throws Exception {
		
		//MUT
		ResponseEntity<InputStreamResource> resp = downloadExcelController.retrieveExcelFile("009");
		
		//check result
		String expectedMessage = (String.format(DownloadExcelController.clientErrorMessageFileNotFound, "009"));
		InputStreamResource receivedObj = resp.getBody();
		assertNotNull(receivedObj);
		byte[] arr = DownloadUtilsTest.streamCollector(receivedObj.getInputStream());
		
		assertEquals(expectedMessage, (new String(arr)));
	}
	@Test
	public void testDownloadExcel() throws Exception {
//		request.setQueryString("src=deSearch");
//		request.setContentType("applicatopn/json");
//		request.setMethod("POST");
		List<String> idList = new ArrayList<>();
		idList.add("testId1");
		String testUriStr = "http://localhost:8080/downloadExcel";
		URI uri = DownloadUtilsTest.buildTestUri(testUriStr);
		RequestEntity<List<String>> request = new RequestEntity<>(idList, HttpMethod.POST, uri);

		
		//MUT
		ResponseEntity<String> responseEntity = downloadExcelController.downloadExcel("deSearch", request);
		
		//check results
		assertNotNull(responseEntity);
		Object resObj = responseEntity.getBody();
		assertNotNull(resObj);
		assertEquals(String.class, resObj.getClass());
		String fileIdExpected = getExcelDownload.getFileId();
		String fileIdReceivedStr = (String)resObj;
		
		//System.out.println(fileIdReceivedStr);
		assertEquals(fileIdExpected, fileIdReceivedStr);
		
		HttpStatus httpStatusReceived = responseEntity.getStatusCode();
		assertNotNull(httpStatusReceived);
		assertEquals(HttpStatus.CREATED, httpStatusReceived);
		
		HttpHeaders headersReceived = responseEntity.getHeaders();
		assertNotNull(headersReceived);
		List<String> locationReceived = headersReceived.get("Location");
		assertNotNull(locationReceived);
		assertEquals(1, locationReceived.size());
		String locationUrlReceived = locationReceived.get(0);
		assertTrue(locationUrlReceived.contains(fileIdExpected));
		assertEquals(testUriStr + '/' + fileIdExpected, locationUrlReceived);
		
		fileName = getExcelDownload.buildDownloadAbsoluteFileName(fileIdExpected);
		File fileReceived = new File(fileName);
		assertTrue(fileReceived.exists());

	}
	@Test
	public void testRetrieveDownloadExcel() throws Exception {
		
		List<String> idList = new ArrayList<>();
		idList.add("testId1");
		String testUriStr = "http://localhost:8080/downloadExcel";
		URI uri = DownloadUtilsTest.buildTestUri(testUriStr);
		RequestEntity<List<String>> request = new RequestEntity<>(idList, HttpMethod.POST, uri);

		//run creating file with fileName step one of Excel download
		downloadExcelController.downloadExcel("deSearch", request);
		fileName = getExcelDownload.buildDownloadAbsoluteFileName(getExcelDownload.getFileId());
		//read the created file
		InputStream expectedInput = new FileInputStream(fileName);
		byte[] expectedBytes = DownloadUtilsTest.streamCollector(expectedInput);
		
		//MUT
		ResponseEntity<InputStreamResource> responseEntity = downloadExcelController.retrieveExcelFile(getExcelDownload.getFileId());
		
		//check
		Object resObj = responseEntity.getBody();
		assertNotNull(resObj);
		assertEquals(InputStreamResource.class, resObj.getClass());
		InputStreamResource receivedObj = (InputStreamResource)resObj;
		InputStream receivedInput = receivedObj.getInputStream();
		byte[] receivedBytes = DownloadUtilsTest.streamCollector(receivedInput);
		
		HttpStatus httpStatusReceived = responseEntity.getStatusCode();
		assertNotNull(httpStatusReceived);
		assertEquals(HttpStatus.OK, httpStatusReceived);

		assertTrue(DownloadUtilsTest.isByteArraysEqual(expectedBytes, receivedBytes));
		
		HttpHeaders headersReceived = responseEntity.getHeaders();
		assertNotNull(headersReceived);
		List<String> headerReceivedDisp = headersReceived.get("Content-Disposition");
		assertNotNull(headerReceivedDisp);
		assertEquals(1, headerReceivedDisp.size());
		assertEquals("attachment; filename=CDEBrowser_SearchResults" + DownloadExcelController.fileExtension, headerReceivedDisp.get(0));
	}
	
	public void assignDownloadBeadProperties(GetExcelDownloadTestImpl getExcelDownload) {
		String dir = System.getProperty("user.dir");
		getExcelDownload.setLocalDownloadDirectory(dir + "/src/test/resources/");
		getExcelDownload.setFileNamePrefix("TestExcelFile");		
	}
	public void assignDownloadServiceProperties(DownloadExcelController downloadExcelController) {
		String dir = System.getProperty("user.dir");
		downloadExcelController.getAppConfig().setDownloadDirectory(dir + "/src/test/resources/");
		downloadExcelController.getAppConfig().setFileNamePrefix("TestExcelFile");		
	}

}
