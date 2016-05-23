/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import javax.sql.DataSource;

import org.junit.Test;
import org.mockito.Mockito;
//import gov.nih.nci.cadsr.dao.model.PermissibleValuesModel;

public class PermissibleValuesDAOImplTest {
	DataSource mockDataSource = Mockito.mock(DataSource.class);
	String[] expectedFields = {"value",   "end_date",  "high_value_num",  "low_value_num",  "vm_idseq", "concept_code",
	"vm_version", "vm_description", "vm_id", "pv_idseq", "short_meaning", "meaning_description", "begin_date",
	"created_by", 
	"date_created", 
	"modified_by", 
	"date_modified"};
	
	@Test
	public void testBuildPermissibleValuesSql() {
		PermissibleValuesDAOImpl permissibleValuesDAOImpl = new PermissibleValuesDAOImpl(mockDataSource);
		String sqlReceived = permissibleValuesDAOImpl.buildPermissibleValuesSql();
		sqlReceived = sqlReceived.toLowerCase();
		//System.out.println(sqlReceived);
		//check that all PermissibleValuesModel fields are covered by this SQL
		for (int i = 0; i < expectedFields.length; i++) {
			assertTrue(expectedFields[i] + " not found in SQL", sqlReceived.contains(expectedFields[i]));
		}
		
	}
}
