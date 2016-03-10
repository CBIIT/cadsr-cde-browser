/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

import gov.nih.nci.cadsr.download.GetXmlDownloadInterface;
import gov.nih.nci.cadsr.service.ClientException;
import gov.nih.nci.cadsr.service.ServerException;
/**
 * This is a MVC RESTful controller Unit Test to download data to XML file based.
 * 
 * @author asafievan
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-application-context.xml")
@WebAppConfiguration
public class DownloadXmlControllerTest {
	
	@Autowired DownloadXmlController downloadXmlController;
	
	GetXmlDownloadInterface getXmlDownloadMock = Mockito.mock(GetXmlDownloadInterface.class);;
	
	String fileName;
	
	String localDownloadDirectory = System.getProperty("user.dir");
	String fileNamePrefix = "TestXmlFile";
	
	@Before
	public void resetXmlDownload() {
		Mockito.reset(getXmlDownloadMock);
		downloadXmlController.setGetXmlDownload(getXmlDownloadMock);
		assignDownloadServiceProperties(downloadXmlController);
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
	
	@Test(expected=ServerException.class)
	public void testDownloadXmlServer() throws Exception {
		Mockito.when(getXmlDownloadMock.persist(Mockito.anyCollectionOf(String.class), 
			Mockito.anyString(), Mockito.eq("deSearch")))
			.thenThrow(new ServerException("test exception"));
		List<String> idList = new ArrayList<>();
		idList.add("testId1");
		String testUriStr = "http://localhost:8080/downloadXml";
		URI uri = buildTestUri(testUriStr);
		RequestEntity<List<String>> request = new RequestEntity<>(idList, HttpMethod.POST, uri);
		//MUT
		downloadXmlController.downloadXml("deSearch", request);
	}
	
	@Test(expected=ClientException.class)
	public void testDownloadXmlEmptyList() throws Exception {
		List<String> idList = new ArrayList<>();
		String testUriStr = "http://localhost:8080/downloadXml";
		URI uri = buildTestUri(testUriStr);
		RequestEntity<List<String>> request = new RequestEntity<>(idList, HttpMethod.POST, uri);
		//MUT
		downloadXmlController.downloadXml("deSearch", request);	
	}
	
	@Test(expected=ClientException.class)
	public void testDownloadXmlNullList() throws Exception {
		String testUriStr = "http://localhost:8080/downloadXml";
		URI uri = buildTestUri(testUriStr);
		RequestEntity<List<String>> request = new RequestEntity<>(null, HttpMethod.POST, uri);
	
		//MUT
		downloadXmlController.downloadXml("deSearch", request);	
	}
	
	@Test(expected=ClientException.class)
	public void testDownloadXmlNullSource() throws Exception {
		List<String> idList = new ArrayList<>();
		String testUriStr = "http://localhost:8080/downloadXml";
		URI uri = buildTestUri(testUriStr);
		RequestEntity<List<String>> request = new RequestEntity<>(idList, HttpMethod.POST, uri);
		//MUT
		downloadXmlController.downloadXml(null, request);	
	}
	
	@Test(expected=ClientException.class)
	public void testDownloadXmlWrongSource() throws Exception {
		List<String> idList = new ArrayList<>();
		idList.add("testId1");	
		String testUriStr = "http://localhost:8080/downloadXml";
		URI uri = buildTestUri(testUriStr);
		RequestEntity<List<String>> request = new RequestEntity<>(idList, HttpMethod.POST, uri);

		//MUT
		downloadXmlController.downloadXml("deSearchWrong", request);	
	}
	
	@Test(expected=ClientException.class)
	public void testDownloadXmlWrongFileId() throws Exception {
		//MUT
		downloadXmlController.retrieveXmlFile("009");	
	}
	
	@Test
	public void testCreateDownloadXml() throws Exception {
		String fileIdExpected = "123";
	
		List<String> idList = new ArrayList<>();
		idList.add("testId1");
		String testUriStr = "http://localhost:8080/downloadXml";
		URI uri = buildTestUri(testUriStr);
		RequestEntity<List<String>> request = new RequestEntity<>(idList, HttpMethod.POST, uri);
		
		Mockito.when(getXmlDownloadMock.persist(Mockito.eq(idList), 
				Mockito.anyString(), Mockito.eq("deSearch")))
				.thenReturn(fileIdExpected);

		
		//MUT
		ResponseEntity<byte[]> responseEntity = downloadXmlController.downloadXml("deSearch", request);
		
		//check results
		assertNotNull(responseEntity);
		Object resObj = responseEntity.getBody();
		assertNotNull(resObj);
		assertEquals(byte[].class, resObj.getClass());
		String fileIdReceivedStr = new String((byte[])resObj);
		
		System.out.println(new String((byte[])resObj));
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
	}
	
	@Test
	public void testRetrieveDownloadXml() throws Exception {
		String fileIdExpected = "12345";
		//create a file
		byte[] expectedBytes = buildFileReturnedByDownload(fileIdExpected);
		
		//MUT
		ResponseEntity<InputStreamResource> responseEntity = downloadXmlController.retrieveXmlFile(fileIdExpected);
		
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

		assertTrue(byteArraysEqual(expectedBytes, receivedBytes));
		
		HttpHeaders headersReceived = responseEntity.getHeaders();
		assertNotNull(headersReceived);
		List<String> headerReceivedDisp = headersReceived.get("Content-Disposition");
		assertNotNull(headerReceivedDisp);
		assertEquals(1, headerReceivedDisp.size());
		assertEquals("attachment; filename=CDEBrowser_SearchResults.xml", headerReceivedDisp.get(0));
		
	}
	
	protected byte[] buildFileReturnedByDownload(String fileId) throws Exception {
		String strTestExistedFileName = "CDEBrowser_SearchResult_test.xml";
		String testExistedFileName = ClassLoader.getSystemResource(strTestExistedFileName).getFile();
		FileInputStream fis = new FileInputStream(testExistedFileName);
		String preparedFileName = buildDownloadAbsoluteFileName(fileId);
		//System.out.println("preparedFileName: " + preparedFileName);
		byte[] arr = streamCollector(fis);
		FileOutputStream fos = new FileOutputStream(preparedFileName);
		fos.write(arr);
		fos.flush();
		fos.close();
		return arr;
	}
	
	protected String buildDownloadAbsoluteFileName(String fileId) {
		fileName = localDownloadDirectory + "/" +  fileNamePrefix + fileId+ ".xml";
		return fileName;
	}

	protected void assignDownloadServiceProperties(DownloadXmlController downloadXmlController) {
		downloadXmlController.setDownloadDirectory(localDownloadDirectory + "/");
		downloadXmlController.setFileNamePrefix(fileNamePrefix);		
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
	
	public static boolean byteArraysEqual(byte[] source, byte[] result) {
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
