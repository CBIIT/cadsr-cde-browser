/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.download;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.mockito.Mockito;

import gov.nih.nci.cadsr.service.ClientException;

public class DownloadUtilsTest {

	@Test
	public void testBuildSqlInConditionNull() throws Exception {
		//MUT
		assertNull(DownloadUtils.buildSqlInCondition(null));
	}
	@Test
	public void testBuildSqlInConditionEmpty() throws Exception {
		//MUT
		assertNull(DownloadUtils.buildSqlInCondition(new ArrayList<String>()));
	}
	@Test
	public void testBuildSqlInCondition() throws Exception {
		List<String> itemIds = new ArrayList<>();
		itemIds.add("testId1");
		itemIds.add("testId2");
		//MUT
		String received = DownloadUtils.buildSqlInCondition(itemIds);
		assertEquals("'testId1', 'testId2'", received);
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
