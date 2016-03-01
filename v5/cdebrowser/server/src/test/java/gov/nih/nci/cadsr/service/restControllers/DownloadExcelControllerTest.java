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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpMethod;
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
	@Before
	public void resetGetExcelDownload() {
		downloadExcelController.setGetExcelDownload(getExcelDownload);		
	}
	
	@Test(expected=ServerException.class)
	public void testDownloadExcelServer() throws Exception {
		GetExcelDownloadInterface getExcelDownloadMock = Mockito.mock(GetExcelDownloadInterface.class);
		downloadExcelController.setGetExcelDownload(getExcelDownloadMock);
		Mockito.when(getExcelDownloadMock.persist(Mockito.anyCollectionOf(String.class), 
			Mockito.anyString(), Mockito.eq("deSearch")))
			.thenThrow(new ServerException("test exception"));
		List<String> idList = new ArrayList<>();
		idList.add("testId1");
		URI uri = new URI("http://localhost/downloadExcel");
		RequestEntity<List<String>> request = new RequestEntity<>(idList, HttpMethod.POST, uri);
		//MUT
		downloadExcelController.downloadExcel("deSearch", request);
	}
	@Test(expected=ClientException.class)
	public void testDownloadExcelEmptyList() throws Exception {
		List<String> idList = new ArrayList<>();
		URI uri = new URI("http://localhost/downloadExcel");
		RequestEntity<List<String>> request = new RequestEntity<>(idList, HttpMethod.POST, uri);
		//MUT
		downloadExcelController.downloadExcel("deSearch", request);	
	}
	@Test(expected=ClientException.class)
	public void testDownloadExcelNullList() throws Exception {
		URI uri = new URI("http://localhost/downloadExcel");
		RequestEntity<List<String>> request = new RequestEntity<>(null, HttpMethod.POST, uri);
	
		//MUT
		downloadExcelController.downloadExcel("deSearch", request);	
	}
	@Test(expected=ClientException.class)
	public void testDownloadExcelNullSource() throws Exception {
		List<String> idList = new ArrayList<>();
		idList.add("testId1");	
		URI uri = new URI("http://localhost/downloadExcel");
		RequestEntity<List<String>> request = new RequestEntity<>(idList, HttpMethod.POST, uri);
		//MUT
		downloadExcelController.downloadExcel(null, request);	
	}
	@Test(expected=ClientException.class)
	public void testDownloadExcelWrongSource() throws Exception {
		List<String> idList = new ArrayList<>();
		idList.add("testId1");	
		URI uri = new URI("http://localhost/downloadExcel");
		RequestEntity<List<String>> request = new RequestEntity<>(idList, HttpMethod.POST, uri);

		//MUT
		downloadExcelController.downloadExcel("deSearchWrong", request);	
	}
	//FIXME fix this test after changes done to controllers
	//@Test
	public void testDownloadExcel() throws Exception {
//		request.setQueryString("src=deSearch");
//		request.setContentType("applicatopn/json");
//		request.setMethod("POST");
		List<String> idList = new ArrayList<>();
		idList.add("testId1");
		URI uri = new URI("http://localhost/downloadExcel");
		RequestEntity<List<String>> request = new RequestEntity<>(idList, HttpMethod.POST, uri);

		//MUT
		ResponseEntity<byte[]> responseEntity = downloadExcelController.downloadExcel("deSearch", request);
		//check results
		assertNotNull(responseEntity);
		Object resObj = responseEntity.getBody();
		assertNotNull(resObj);
		assertEquals(InputStreamResource.class, resObj.getClass());
		InputStreamResource resStream = (InputStreamResource)resObj;
		
		InputStream receivedInput = resStream.getInputStream();
		byte[] receivedBytes = streamCollector(receivedInput);
		InputStream expectedInput = new FileInputStream(getExcelDownload.getExcelFileName());
		byte[] expectedBytes = streamCollector(expectedInput);
		assertTrue(isByteArraysEqual(expectedBytes, receivedBytes));
		
		//clean up
		File file = new File(getExcelDownload.getExcelFileName());
		boolean isDeleted = file.delete();
		assertTrue(isDeleted);
		
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
