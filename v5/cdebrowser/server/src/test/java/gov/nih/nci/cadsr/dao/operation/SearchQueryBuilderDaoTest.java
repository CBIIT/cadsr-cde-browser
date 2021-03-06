/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao.operation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;

import gov.nih.nci.cadsr.common.RegistrationStatusEnum;
import gov.nih.nci.cadsr.common.WorkflowStatusEnum;
import gov.nih.nci.cadsr.model.SearchPreferences;
import gov.nih.nci.cadsr.model.SearchPreferencesServer;
import gov.nih.nci.cadsr.service.model.search.SearchCriteria;
/**
 *
 * @author asafievan
 *
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebAppConfiguration
//@ContextConfiguration("classpath:test-application-context.xml")
public class SearchQueryBuilderDaoTest {
	//There is another test file SearchQueryBuilderTest located in test package gov.nih.nci.cadsr.service.search
	private SearchQueryBuilder searchQueryBuilder;
	private SearchPreferencesServer searchPreferences;
	private SearchCriteria searchCriteria;
	
	@BeforeClass
	public static void setUpClass() throws Exception
	{

	}

	@Before
	public void setUp() throws Exception
	{
		searchQueryBuilder = new SearchQueryBuilder();
		searchPreferences = new SearchPreferencesServer();
		searchCriteria = new SearchCriteria();
		searchCriteria.setName("");
		searchCriteria.setPublicId("62*");
		searchCriteria.setSearchMode("0");
		searchCriteria.setProgramArea("All");
		searchCriteria.setContext("Testcontext");
		searchCriteria.setClassification("99BA9DC8-84A5-4E69-E034-080020C9C0E0");
		searchCriteria.setProtocol("protocol");
		
	}
	@After
	public void tearDown() throws Exception
	{

	}
	//Context excluded tests
	@Test
	public void testInitBothContextsBothExcluded() {
		
		//MUT
		String sqlStmtReceived = searchQueryBuilder.initSearchQueryBuilder(searchCriteria, searchPreferences, WorkflowStatusEnum.getAsList(), RegistrationStatusEnum.getAsList());
		//check
		String contextExludeWhere = " AND conte.name NOT IN (" + SearchPreferences.CONTEXT_EXCLUDES;
		assertTrue(sqlStmtReceived.indexOf(contextExludeWhere) > 0);
	}
	@Test
	public void testInitBothContextTestExcluded() {
		//MUT
		searchPreferences.setExcludeTraining(false);
		String sqlStmtReceived = searchQueryBuilder.initSearchQueryBuilder(searchCriteria, searchPreferences, WorkflowStatusEnum.getAsList(), RegistrationStatusEnum.getAsList());
		//check
		String contextExludeWhere = " AND conte.name NOT IN (" + SearchPreferences.CONTEXT_EXCLUDES_TEST + " )";
		assertTrue(sqlStmtReceived.indexOf(contextExludeWhere) > 0);
	}
	@Test
	public void testInitBothContextTrainingExcluded() {
		searchPreferences.setExcludeTest(false);
		//MUT
		String sqlStmtReceived = searchQueryBuilder.initSearchQueryBuilder(searchCriteria, searchPreferences, WorkflowStatusEnum.getAsList(), RegistrationStatusEnum.getAsList());
		//check
		String contextExludeWhere = " AND conte.name NOT IN (" + SearchPreferences.CONTEXT_EXCLUDES_TRAINING + " )";
		assertTrue(sqlStmtReceived.indexOf(contextExludeWhere) > 0);
	}
	//Workflow status tests
	@Test
	public void testInitWorkflowNoClientExcluded() {
		String workflowWhere = " AND de.asl_name NOT IN  " + SearchPreferencesServer.buildSqlAlwaysExcluded();

		//MUT
		String sqlStmtReceived = searchQueryBuilder.initSearchQueryBuilder(searchCriteria, searchPreferences, WorkflowStatusEnum.getAsList(), RegistrationStatusEnum.getAsList());
		//check
		assertTrue(sqlStmtReceived.indexOf(workflowWhere) > 0);
	}
	@Test
	public void testInitWorkflowExcluded() {
		List <String> workflowStatusExcluded = new ArrayList<>();
		workflowStatusExcluded.add(WorkflowStatusEnum.DraftMod.getWorkflowStatus());
		searchPreferences.setWorkflowStatusExcluded(workflowStatusExcluded);

		String workflowWhere = " AND de.asl_name NOT IN " + searchPreferences.buildExcludedWorkflowSql();

		//MUT
		String sqlStmtReceived = searchQueryBuilder.initSearchQueryBuilder(searchCriteria, searchPreferences, WorkflowStatusEnum.getAsList(), RegistrationStatusEnum.getAsList());
		//check
		assertTrue(sqlStmtReceived.indexOf(workflowWhere) > 0);
	}
	//Registration Status tests
	@Test
	public void testInitRegNoExcluded() {
		String workflowWhere = "nvl(acr.registration_status";

		//MUT
		String sqlStmtReceived = searchQueryBuilder.initSearchQueryBuilder(searchCriteria, searchPreferences, WorkflowStatusEnum.getAsList(), RegistrationStatusEnum.getAsList());
		//check
		assertFalse(sqlStmtReceived.indexOf(workflowWhere) > 0);
	}
	@Test
	public void testInitRegExcluded() {
		List <String> regStatusExcluded = new ArrayList<>();
		regStatusExcluded.add(RegistrationStatusEnum.PROPOSED.getRegStatus());
		searchPreferences.setRegistrationStatusExcluded(regStatusExcluded);

		String[] arr = regStatusExcluded.toArray(new String[1]);
		String workflowWhere = searchQueryBuilder.getExcludeWhereClause( "nvl(acr.registration_status,'-1')", arr);

		//MUT
		String sqlStmtReceived = searchQueryBuilder.initSearchQueryBuilder(searchCriteria, searchPreferences, WorkflowStatusEnum.getAsList(), RegistrationStatusEnum.getAsList());

		//check
		assertTrue(sqlStmtReceived.indexOf(workflowWhere) > 0);
	}
	@Test
	public void testInitReg2Excluded() {
		List <String> regStatusExcluded = new ArrayList<>();
		regStatusExcluded.add(RegistrationStatusEnum.PROPOSED.getRegStatus());
		regStatusExcluded.add(RegistrationStatusEnum.STANDARD.getRegStatus());
		searchPreferences.setRegistrationStatusExcluded(regStatusExcluded);
		String[] arr = regStatusExcluded.toArray(new String[2]);
		String workflowWhere = searchQueryBuilder.getExcludeWhereClause( "nvl(acr.registration_status,'-1')", arr);

		//MUT
		String sqlStmtReceived = searchQueryBuilder.initSearchQueryBuilder(searchCriteria, searchPreferences, WorkflowStatusEnum.getAsList(), RegistrationStatusEnum.getAsList());
		//check
		assertTrue(sqlStmtReceived.indexOf(workflowWhere) > 0);
	}

	@Test
	public void testBuildRegStatusWhereClauseEmpty() {
		//MUT
		String received = searchQueryBuilder.buildRegStatusWhereClause(new String[0]);
		//check
		assertEquals("", received);
	}
	@Test
	public void testBuildRegStatusWhereClauseNull() {
		//MUT
		String received = searchQueryBuilder.buildRegStatusWhereClause(null);
		//check
		assertEquals("", received);
	}
	@Test
	public void testBuildRegStatusWhereClauseAll() {
		//MUT
		String received = searchQueryBuilder.buildRegStatusWhereClause(searchQueryBuilder.regStatusesWhere);
		//check
		assertEquals("", received);
	}
	@Test
	public void testGetExcludeWhereClauseNull() {
		//MUT
		String received = searchQueryBuilder.getExcludeWhereClause(null, null);
		//check
		assertNull(received);
	}
	@Test
	public void testGetExcludeWhereClauseEmpty() {
		//MUT
		String received = searchQueryBuilder.getExcludeWhereClause(null, new String[0]);
		//check
		assertNull(received);
	}
	@Test
	public void testGetExcludeWhereClause() {
		String[] arr = {RegistrationStatusEnum.APPLICATION.getRegStatus(), RegistrationStatusEnum.CANDIDATE.getRegStatus()};
		String expected = " TestCol1 NOT IN ('" + RegistrationStatusEnum.APPLICATION.getRegStatus() + "' , '" + RegistrationStatusEnum.CANDIDATE.getRegStatus() + "' ) ";
		//MUT
		String received = searchQueryBuilder.getExcludeWhereClause("TestCol1", arr);
		//check
		assertEquals(expected, received);
	}
	@Test
	public void testbuildRegStatusWhereClauseEmptyFirst() {
		String[] arr = {""};
		//MUT
		String received = searchQueryBuilder.buildRegStatusWhereClause(arr);
		//check
		assertEquals("", received);
	}
	//buildfExcludedStatusSql
	@Test
	public void testBuildfExcludedStatusSqlGeneric() {
		String[] regStatusList = {"Ones' test", "Two's test"};

		//MUT
		String received = searchQueryBuilder.buildRegStatusWhereClause(regStatusList);
		
		//check
		assertEquals(" AND acr.registration_status IN ('Ones'' test','Two''s test')", received);
	}
}
