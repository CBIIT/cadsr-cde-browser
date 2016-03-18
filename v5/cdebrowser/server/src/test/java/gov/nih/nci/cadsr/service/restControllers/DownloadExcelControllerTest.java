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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import gov.nih.nci.cadsr.download.GetExcelDownloadInterface;
import gov.nih.nci.cadsr.download.GetExcelDownloadTestImpl;
import gov.nih.nci.cadsr.service.ClientException;
import gov.nih.nci.cadsr.service.ServerException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-application-context.xml")
@WebAppConfiguration
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
	//FIXME fix the tests commented
	@Test
	public void testDownloadExcelServer() throws Exception {
		GetExcelDownloadInterface getExcelDownloadMock = Mockito.mock(GetExcelDownloadInterface.class);
		downloadExcelController.setGetExcelDownload(getExcelDownloadMock);
		Mockito.when(getExcelDownloadMock.persist(Mockito.anyCollectionOf(String.class), 
			Mockito.anyString(), Mockito.eq("deSearch")))
			.thenThrow(new ServerException("test exception"));
		List<String> idList = new ArrayList<>();
		idList.add("testId1");
		String testUriStr = "http://localhost:8080/downloadExcel";
		URI uri = buildTestUri(testUriStr);
		RequestEntity<List<String>> request = new RequestEntity<>(idList, HttpMethod.POST, uri);
		//MUT
		ResponseEntity <String> resp = downloadExcelController.downloadExcel("deSearch", request);
		String receivedObj = resp.getBody();
		assertNotNull(receivedObj);
		assertTrue(receivedObj.indexOf("test exception") > 0);
		assertTrue(receivedObj.startsWith(DownloadExcelController.serverErrorMessage));
	}
	
	//@Test(expected=ClientException.class)
	public void testDownloadExcelEmptyList() throws Exception {
		List<String> idList = new ArrayList<>();
		String testUriStr = "http://localhost:8080/downloadExcel";
		URI uri = buildTestUri(testUriStr);
		RequestEntity<List<String>> request = new RequestEntity<>(idList, HttpMethod.POST, uri);
		//MUT
		downloadExcelController.downloadExcel("deSearch", request);	
	}
	//@Test(expected=ClientException.class)
	public void testDownloadExcelNullList() throws Exception {
		String testUriStr = "http://localhost:8080/downloadExcel";
		URI uri = buildTestUri(testUriStr);
		RequestEntity<List<String>> request = new RequestEntity<>(null, HttpMethod.POST, uri);
	
		//MUT
		downloadExcelController.downloadExcel("deSearch", request);	
	}
	//@Test(expected=ClientException.class)
	public void testDownloadExcelNullSource() throws Exception {
		List<String> idList = new ArrayList<>();
		String testUriStr = "http://localhost:8080/downloadExcel";
		URI uri = buildTestUri(testUriStr);
		RequestEntity<List<String>> request = new RequestEntity<>(idList, HttpMethod.POST, uri);
		//MUT
		downloadExcelController.downloadExcel(null, request);	
	}
	//@Test(expected=ClientException.class)
	public void testDownloadExcelWrongSource() throws Exception {
		List<String> idList = new ArrayList<>();
		idList.add("testId1");	
		String testUriStr = "http://localhost:8080/downloadExcel";
		URI uri = buildTestUri(testUriStr);
		RequestEntity<List<String>> request = new RequestEntity<>(idList, HttpMethod.POST, uri);

		//MUT
		downloadExcelController.downloadExcel("deSearchWrong", request);	
	}
	//@Test(expected=ClientException.class)
	public void testDownloadExcelWrongFileId() throws Exception {
		
		//MUT
		downloadExcelController.retrieveExcelFile("009");	
	}
	//@Test
	public void testDownloadExcel() throws Exception {
//		request.setQueryString("src=deSearch");
//		request.setContentType("applicatopn/json");
//		request.setMethod("POST");
		List<String> idList = new ArrayList<>();
		idList.add("testId1");
		String testUriStr = "http://localhost:8080/downloadExcel";
		URI uri = buildTestUri(testUriStr);
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
	//@Test
	public void testRetrieveDownloadExcel() throws Exception {
		
		List<String> idList = new ArrayList<>();
		idList.add("testId1");
		String testUriStr = "http://localhost:8080/downloadExcel";
		URI uri = buildTestUri(testUriStr);
		RequestEntity<List<String>> request = new RequestEntity<>(idList, HttpMethod.POST, uri);

		//run creating file with fileName step one of Excel download
		downloadExcelController.downloadExcel("deSearch", request);
		fileName = getExcelDownload.buildDownloadAbsoluteFileName(getExcelDownload.getFileId());
		//read the created file
		InputStream expectedInput = new FileInputStream(fileName);
		byte[] expectedBytes = streamCollector(expectedInput);
		
		//MUT
		ResponseEntity<InputStreamResource> responseEntity = downloadExcelController.retrieveExcelFile(getExcelDownload.getFileId());
		
		//check
		Object resObj = responseEntity.getBody();
		assertNotNull(resObj);
		assertEquals(InputStreamResource.class, resObj.getClass());
		InputStreamResource receivedObj = (InputStreamResource)resObj;
		InputStream receivedInput = receivedObj.getInputStream();
		byte[] receivedBytes = streamCollector(receivedInput);
		
		HttpStatus httpStatusReceived = responseEntity.getStatusCode();
		assertNotNull(httpStatusReceived);
		assertEquals(HttpStatus.OK, httpStatusReceived);

		assertTrue(isByteArraysEqual(expectedBytes, receivedBytes));
		
		HttpHeaders headersReceived = responseEntity.getHeaders();
		assertNotNull(headersReceived);
		List<String> headerReceivedDisp = headersReceived.get("Content-Disposition");
		assertNotNull(headerReceivedDisp);
		assertEquals(1, headerReceivedDisp.size());
		assertEquals("attachment; filename=CDEBrowser_SearchResults.xls", headerReceivedDisp.get(0));
	}
	public void assignDownloadBeadProperties(GetExcelDownloadTestImpl getExcelDownload) {
		String dir = System.getProperty("user.dir");
		getExcelDownload.setLocalDownloadDirectory(dir + "/src/test/resources/");
		getExcelDownload.setFileNamePrefix("TestExcelFile");		
	}
	public void assignDownloadServiceProperties(DownloadExcelController downloadExcelController) {
		String dir = System.getProperty("user.dir");
		downloadExcelController.setDownloadDirectory(dir + "/src/test/resources/");
		downloadExcelController.setFileNamePrefix("TestExcelFile");		
	}
	public static URI buildTestUri(String absoluteUrlString) throws Exception{
		return (new URL(absoluteUrlString)).toURI();
	}
	public static byte[] streamCollector(InputStream istream) throws Exception {
		byte[] res;
		ArrayList<byte[]> storeArr = new ArrayList<>();
		ArrayList<Integer> storeLen = new ArrayList<>();
		byte[] currArr = new byte[4096];
		int currNum = 0;
		int readNum = 0;
		while ((currNum = istream.read(currArr,0, 4096)) > 0) {
			storeArr.add(currArr);
			storeLen.add(currNum);
			readNum += currNum;
			currArr = new byte[4096];
		}
		res = new byte[readNum];
		int startPos = 0;
		for (int i = 0; i < storeArr.size(); i++) {
			currArr = storeArr.get(i);
			currNum = storeLen.get(i);
			System.arraycopy(currArr, 0, res, startPos, currNum);
			startPos += currNum;
		}
		istream.close();
		return res;
	}
	public static boolean isByteArraysEqual(byte[] source, byte[] result) {
		if ((source == null) && (result == null)) return true;
		else if ((source == null) && (result != null)) return false;
		else if ((source != null) && (result == null)) return false;
		if (source.length != result.length) return false;
		for (int i = 0; i < source.length; i++) {
			if (source[i] != result[i]) return false;
		}
		return true;
	}
}
