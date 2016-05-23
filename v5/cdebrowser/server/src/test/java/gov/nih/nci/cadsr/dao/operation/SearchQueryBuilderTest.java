/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao.operation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import gov.nih.nci.cadsr.common.RegistrationStatusEnum;
import gov.nih.nci.cadsr.common.WorkflowStatusEnum;
import gov.nih.nci.cadsr.model.SearchPreferences;
import gov.nih.nci.cadsr.service.model.search.SearchCriteria;
/**
 * 
 * @author asafievan
 *
 */
public class SearchQueryBuilderTest {	
	//There is another test file located in test package gov.nih.nci.cadsr.service.search
	private SearchQueryBuilder searchQueryBuilder;
	private SearchPreferences searchPreferences;
	private SearchCriteria searchCriteria;
	@BeforeClass
	public static void setUpClass() throws Exception
	{

	}
	
	@Before
	public void setUp() throws Exception
	{
		searchQueryBuilder = new SearchQueryBuilder();
		searchPreferences = new SearchPreferences();
		searchCriteria = new SearchCriteria();
		searchCriteria.setName("");
		searchCriteria.setPublicId("62*");
		searchCriteria.setSearchMode("0");
		searchCriteria.setProgramArea("All");
		searchCriteria.setContext("Testcontext");
		searchCriteria.setClassification("99BA9DC8-84A5-4E69-E034-080020C9C0E0");
		searchCriteria.setProtocol("protocol");
	}
	//Context excluded tests
	@Test
	public void testInitBothContextsBothExcluded() {
		//MUT
		String sqlStmtReceived = searchQueryBuilder.initSearchQueryBuilder(searchCriteria, searchPreferences);
		//check
		String contextExludeWhere = " AND conte.name NOT IN (" + SearchPreferences.CONTEXT_EXCLUDES;
		assertTrue(sqlStmtReceived.indexOf(contextExludeWhere) > 0);
	}
	@Test
	public void testInitBothContextTestExcluded() {
		//MUT
		searchPreferences.setExcludeTraining(false);
		String sqlStmtReceived = searchQueryBuilder.initSearchQueryBuilder(searchCriteria, searchPreferences);
		//check
		String contextExludeWhere = " AND conte.name NOT IN (" + SearchPreferences.CONTEXT_EXCLUDES_TEST + " )";
		assertTrue(sqlStmtReceived.indexOf(contextExludeWhere) > 0);
	}
	@Test
	public void testInitBothContextTrainingExcluded() {
		searchPreferences.setExcludeTest(false);
		//MUT
		String sqlStmtReceived = searchQueryBuilder.initSearchQueryBuilder(searchCriteria, searchPreferences);
		//check
		String contextExludeWhere = " AND conte.name NOT IN (" + SearchPreferences.CONTEXT_EXCLUDES_TRAINING + " )";
		assertTrue(sqlStmtReceived.indexOf(contextExludeWhere) > 0);
	}
	//Workflow status tests
	@Test
	public void testInitWorkflowNoExcluded() {
		String workflowWhere = " AND asl.asl_name NOT IN ";

		//MUT
		String sqlStmtReceived = searchQueryBuilder.initSearchQueryBuilder(searchCriteria, searchPreferences);
		//check
		assertFalse(sqlStmtReceived.indexOf(workflowWhere) > 0);
	}
	@Test
	public void testInitWorkflowExcluded() {
		List <String> workflowStatusExcluded = new ArrayList<>();
		workflowStatusExcluded.add(WorkflowStatusEnum.DraftMod.getWorkflowStatus());		
		searchPreferences.setWorkflowStatusExcluded(workflowStatusExcluded);
		
		String workflowWhere = " AND asl.asl_name NOT IN " + searchPreferences.buildfExcludedWorkflowSql();

		//MUT
		String sqlStmtReceived = searchQueryBuilder.initSearchQueryBuilder(searchCriteria, searchPreferences);
		//check
		assertTrue(sqlStmtReceived.indexOf(workflowWhere) > 0);
	}	
	//Registration Status tests
	@Test
	public void testInitRegwNoExcluded() {
		String workflowWhere = " AND asl.asl_name NOT IN ";

		//MUT
		String sqlStmtReceived = searchQueryBuilder.initSearchQueryBuilder(searchCriteria, searchPreferences);
		//check
		assertFalse(sqlStmtReceived.indexOf(workflowWhere) > 0);
	}
	@Test
	public void testInitRegExcluded() {
		List <String> regStatusExcluded = new ArrayList<>();
		regStatusExcluded.add(RegistrationStatusEnum.PROPOSED.getRegStatus());		
		searchPreferences.setRegistrationStatusExcluded(regStatusExcluded);
		
		String[] arr = regStatusExcluded.toArray(new String[1]);
		String workflowWhere = searchQueryBuilder.getExcludeWhereClause( "nvl(acr.registration_status,'-1')", arr );;

		//MUT
		String sqlStmtReceived = searchQueryBuilder.initSearchQueryBuilder(searchCriteria, searchPreferences);
		
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
		String workflowWhere = searchQueryBuilder.getExcludeWhereClause( "nvl(acr.registration_status,'-1')", arr );;

		//MUT
		String sqlStmtReceived = searchQueryBuilder.initSearchQueryBuilder(searchCriteria, searchPreferences);
		//check
		assertTrue(sqlStmtReceived.indexOf(workflowWhere) > 0);
	}
}
