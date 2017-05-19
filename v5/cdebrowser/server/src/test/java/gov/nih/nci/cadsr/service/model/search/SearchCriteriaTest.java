/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.model.search;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gov.nih.nci.cadsr.service.ServiceTestUtils;

public class SearchCriteriaTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPreprocessCriteriaMultiple() {
		SearchCriteria testCriteria = ServiceTestUtils.buildTestSearchCriteriaAll("test1");
		//MUT
		testCriteria.preprocessSearchContext();
		assertNull(testCriteria.getContext());
	}
	@Test
	public void testPreprocessCriteriaProtocol() {
		SearchCriteria testCriteria = ServiceTestUtils.buildTestSearchCriteriaAll("test1");
		testCriteria.setClassification(null);
		testCriteria.setCsCsiIdSeq(null);
		testCriteria.setFormIdSeq(null);
		//MUT
		testCriteria.preprocessSearchContext();
		assertNull(testCriteria.getContext());
	}
	@Test
	public void testPreprocessCriteriaClassif() {
		SearchCriteria testCriteria = ServiceTestUtils.buildTestSearchCriteriaAll("test2");
		testCriteria.setProtocol(null);
		testCriteria.setCsCsiIdSeq(null);
		testCriteria.setFormIdSeq(null);
		//MUT
		testCriteria.preprocessSearchContext();
		assertNull(testCriteria.getContext());
	}
	@Test
	public void testPreprocessCriteriaCsCsi() {
		SearchCriteria testCriteria = ServiceTestUtils.buildTestSearchCriteriaAll("test3");
		testCriteria.setClassification(null);
		testCriteria.setProtocol(null);
		testCriteria.setFormIdSeq(null);
		//MUT
		testCriteria.preprocessSearchContext();
		assertNull(testCriteria.getContext());
	}
	@Test
	public void testPreprocessCriteriaFormId() {
		SearchCriteria testCriteria = ServiceTestUtils.buildTestSearchCriteriaAll("test3");
		testCriteria.setClassification(null);
		testCriteria.setProtocol(null);
		testCriteria.setCsCsiIdSeq(null);
		//MUT
		testCriteria.preprocessSearchContext();
		assertNull(testCriteria.getContext());
	}
	@Test
	public void testPreprocessCriteriaNone() {
		SearchCriteria testCriteria = ServiceTestUtils.buildTestSearchCriteriaAll("test3");
		String contextExpected = testCriteria.getContext();
		testCriteria.setClassification(null);
		testCriteria.setProtocol(null);
		testCriteria.setCsCsiIdSeq(null);
		testCriteria.setFormIdSeq(null);
		//MUT
		testCriteria.preprocessSearchContext();
		assertEquals(contextExpected, testCriteria.getContext());
	}
	//preprocessCriteria
	@Test
	public void testPreprocessCriteriaAll() {
		SearchCriteria testCriteria = ServiceTestUtils.buildTestSearchCriteriaAll("test1");
		//MUT
		testCriteria.preprocessCriteria();
		//check
		assertAllSubstitute(testCriteria);
	}
	@Test
	public void testPreprocessCriteriaCleanContext() {
		SearchCriteria testCriteria = ServiceTestUtils.buildTestSearchCriteriaAll("test4");
		//MUT
		testCriteria.preprocessCriteria();
		assertNull(testCriteria.getContext());
		assertAllSubstitute(testCriteria);
	}
	@Test
	public void testPreprocessCriteriaKeepContext() {
		SearchCriteria testCriteria = ServiceTestUtils.buildTestSearchCriteriaAll("test5");
		String contextExpected = testCriteria.getContext();
		testCriteria.setClassification(null);
		testCriteria.setProtocol(null);
		testCriteria.setCsCsiIdSeq(null);
		testCriteria.setFormIdSeq(null);
		//MUT
		testCriteria.preprocessCriteria();
		assertEquals(contextExpected, testCriteria.getContext());
		assertAllSubstitute(testCriteria);
	}
	@Test
	public void testPreprocessCriteriaContextWasNull() {
		SearchCriteria testCriteria = ServiceTestUtils.buildTestSearchCriteriaAll("test5");
		testCriteria.setContext(null);
		testCriteria.setClassification(null);
		testCriteria.setProtocol(null);
		testCriteria.setCsCsiIdSeq(null);
		testCriteria.setFormIdSeq(null);
		//MUT
		testCriteria.preprocessCriteria();
		assertNull(testCriteria.getContext());
		assertAllSubstitute(testCriteria);
	}
	protected void assertAllSubstitute(SearchCriteria testCriteria) {
		String allExpected = "ALL";
		assertEquals(allExpected, testCriteria.getAltNameType());
		assertEquals(allExpected, testCriteria.getWorkFlowStatus());
		assertEquals(allExpected, testCriteria.getRegistrationStatus());
		assertEquals(allExpected, testCriteria.getFilteredinput());
	}
}
