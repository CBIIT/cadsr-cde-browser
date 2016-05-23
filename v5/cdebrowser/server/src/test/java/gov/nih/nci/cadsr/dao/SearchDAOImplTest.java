/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import gov.nih.nci.cadsr.common.RegistrationStatusEnum;
import gov.nih.nci.cadsr.common.RegistrationStatusExcludedInitial;
import gov.nih.nci.cadsr.common.WorkflowStatusExcludedInitial;
import gov.nih.nci.cadsr.model.SearchPreferences;

public class SearchDAOImplTest {
	DataSource mockDataSource = Mockito.mock(DataSource.class);
	SearchPreferences searchPreferences = new SearchPreferences();
	
	String[] expectedFields = {
			"name", "de_idseq", "de_preferred_name", "long_name", "doc_text", "asl_name", "de_cdeid", "de_version", "de_usedby", "vd_idseq", "dec_idseq", 
			"conte_idseq", "preferred_definition", "registration_status", "display_order", 
			//"workflow_order", //TODO we have wkflow_order in SQL, and workflow_order in model class; why is that? I guess we do not use this field in the model
			"cdeid"
			//, "created_by", //this model attribute is not in SQL
			//"date_created", //this model attribute is not in SQL
			//"modified_by", //this model attribute is not in SQL
			//"date_modified", //this model attribute is not in SQL
	};
			
	@Before
	public void setUp() {
		searchPreferences = new SearchPreferences();
	}
	
	@Test 
	public void testCdeByProtocolFields() {
		SearchDAOImpl searchDAO = new SearchDAOImpl(mockDataSource);
		String protocolId = "testProt1";
		//MUT
		String sqlReceived = searchDAO.buildSqlCdeByProtocol(protocolId, searchPreferences);
		assertTrue (sqlReceived.indexOf(protocolId) > 0);
		int idxFrom = sqlReceived.toLowerCase().indexOf("from");
		assertTrue(idxFrom > 0);
		sqlReceived = sqlReceived.substring(0,  idxFrom);
		for (int i = 0; i < expectedFields.length; i++) {
			assertTrue(expectedFields[i] + " not found in SQL", sqlReceived.contains(expectedFields[i]));
		}
		

	}
	@Test 
	public void testCdeByContextClassificationSchemeFields() {
		SearchDAOImpl searchDAO = new SearchDAOImpl(mockDataSource);
		String classificationSchemeId = "testcl1";
		//MUT
		String sqlReceived = searchDAO.buildCdeByContextClassificationScheme(classificationSchemeId, searchPreferences);
		//check
		assertTrue (sqlReceived.indexOf(classificationSchemeId) > 0);
		int idxFrom = sqlReceived.toLowerCase().indexOf("from");
		assertTrue(idxFrom > 0);
		sqlReceived = sqlReceived.substring(0,  idxFrom);
		for (int i = 0; i < expectedFields.length; i++) {
			assertTrue(expectedFields[i] + " not found in SQL", sqlReceived.contains(expectedFields[i]));
		}
	}
	@Test 
	public void testCdeByContextClassificationSchemeItemFields() {
		SearchDAOImpl searchDAO = new SearchDAOImpl(mockDataSource);
		String classificationSchemeItemId = "testclitem1";
		//MUT
		String sqlReceived = searchDAO.buildCdeByContextClassificationSchemeItem(classificationSchemeItemId, searchPreferences);
		//check
		assertTrue (sqlReceived.indexOf(classificationSchemeItemId) > 0);
		int idxFrom = sqlReceived.toLowerCase().indexOf("from");
		assertTrue(idxFrom > 0);
		sqlReceived = sqlReceived.substring(0,  idxFrom);
		for (int i = 0; i < expectedFields.length; i++) {
			assertTrue(expectedFields[i] + " not found in SQL", sqlReceived.contains(expectedFields[i]));
		}
	}
	@Test 
	public void testCdeOwnedAndUsedByContextFields() {
		SearchDAOImpl searchDAO = new SearchDAOImpl(mockDataSource);
		String conteId = "testconteId1";
		//MUT
		String sqlReceived = searchDAO.buildCdeOwnedAndUsedByContext(conteId, searchPreferences);
		//check
		assertTrue (sqlReceived.indexOf(conteId) > 0);
		int idxFrom = sqlReceived.toLowerCase().indexOf("from");
		assertTrue(idxFrom > 0);
		sqlReceived = sqlReceived.substring(0,  idxFrom);
		for (int i = 0; i < expectedFields.length; i++) {
			assertTrue(expectedFields[i] + " not found in SQL", sqlReceived.contains(expectedFields[i]));
		}
	}
	@Test
	public void testBuildWorkflowStatusExcludedSql() {
		SearchDAOImpl searchDAO = new SearchDAOImpl(mockDataSource);
		String expected = "AND asl.asl_name NOT IN " + " ('" + WorkflowStatusExcludedInitial.CmteApproved.getWorkflowStatus() + "', '"+ WorkflowStatusExcludedInitial.RetiredWithdrawn.getWorkflowStatus() + "') " + "\n";
		List<String> workflowStatusExcluded = new ArrayList<>();
		workflowStatusExcluded.add(WorkflowStatusExcludedInitial.CmteApproved.getWorkflowStatus());
		workflowStatusExcluded.add(WorkflowStatusExcludedInitial.RetiredWithdrawn.getWorkflowStatus());
		searchPreferences.setWorkflowStatusExcluded(workflowStatusExcluded);
		//MUT
		String received = searchDAO.buildWorkflowStatusExcludedSql(searchPreferences);
		//check
		assertEquals(expected, received);
	}
	@Test
	public void testBuildWorkflowStatusExcludedSqlNone() {
		SearchDAOImpl searchDAO = new SearchDAOImpl(mockDataSource);
		String expected = "";
		//MUT
		String received = searchDAO.buildWorkflowStatusExcludedSql(searchPreferences);
		//check
		assertEquals(expected, received);
	}
	@Test
	public void testBuildRegistrationStatusExcludedSql() {
		SearchDAOImpl searchDAO = new SearchDAOImpl(mockDataSource);
		String expected = "AND nvl(acr.registration_status,'-1') NOT IN " + " ('" + RegistrationStatusExcludedInitial.RETIRED.getRegStatus() + "', '"+ RegistrationStatusEnum.SUSPENDED.getRegStatus() + "') " + "\n";
		List<String> workflowStatusExcluded = new ArrayList<>();
		workflowStatusExcluded.add(RegistrationStatusExcludedInitial.RETIRED.getRegStatus());
		workflowStatusExcluded.add(RegistrationStatusEnum.SUSPENDED.getRegStatus());
		searchPreferences.setRegistrationStatusExcluded(workflowStatusExcluded);
		//MUT
		String received = searchDAO.buildRegistrationStatusExcludedSql(searchPreferences);
		//check
		assertEquals(expected, received);
	}
	@Test
	public void testBuildRegistrationStatusExcludedSqlNone() {
		SearchDAOImpl searchDAO = new SearchDAOImpl(mockDataSource);
		String expected = "";
		//MUT
		String received = searchDAO.buildRegistrationStatusExcludedSql(searchPreferences);
		//check
		assertEquals(expected, received);
	}
	@Test
	public void testBuildContextExcludedSql() {
		SearchDAOImpl searchDAO = new SearchDAOImpl(mockDataSource);
		String expected = "AND conte.NAME NOT IN ( " + searchPreferences.buildContextExclided() + ")\n";
		searchPreferences.setExcludeTest(true);
		searchPreferences.setExcludeTraining(true);
		//MUT
		String received = searchDAO.buildContextExcludedSql(searchPreferences);
		//check
		assertEquals(expected, received);
	}
	@Test
	public void testBuildContextExcludedSqlNone() {
		SearchDAOImpl searchDAO = new SearchDAOImpl(mockDataSource);
		String expected = "";
		searchPreferences.setExcludeTest(false);
		searchPreferences.setExcludeTraining(false);
		//MUT
		String received = searchDAO.buildContextExcludedSql(searchPreferences);
		//check
		assertEquals(expected, received);
	}
}
