/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.download;

import static org.junit.Assert.*;

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
}
