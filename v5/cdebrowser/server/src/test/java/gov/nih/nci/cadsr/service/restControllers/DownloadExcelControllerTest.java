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
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import gov.nih.nci.cadsr.download.GetExcelDownloadTestImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-application-context.xml")
@WebAppConfiguration
public class DownloadExcelControllerTest {
//	@Autowired MockHttpServletRequest request;
//	
//	@Autowired MockHttpServletResponse response;
	
	@Autowired DownloadExcelController downloadExcelController;
	
	@Autowired GetExcelDownloadTestImpl getExcelDownload;
	
	@Test
	public void testDownloadExcel() throws Exception {
//		request.setQueryString("src=deSearch");
//		request.setContentType("applicatopn/json");
//		request.setMethod("POST");
		List<String> idList = new ArrayList<>();
		idList.add("testId1");
		//MUT
		ResponseEntity<InputStreamResource> responseEntity = downloadExcelController.downloadExcel("deSearch", idList);
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
