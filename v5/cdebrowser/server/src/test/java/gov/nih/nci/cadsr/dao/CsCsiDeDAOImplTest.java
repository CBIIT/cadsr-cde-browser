/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
//would be used in the case of next
//import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import gov.nih.nci.cadsr.dao.CsCsiDeDAOImpl.CsCsiDeRowMapper;
import gov.nih.nci.cadsr.dao.model.CsCsiDeModel;

public class CsCsiDeDAOImplTest {
	/*
		cscsi.cs_csi_idseq, "+
		"ClassificationSchemes.LONG_NAME cs_long_name, "+ 
		"ClassificationSchemes.PREFERRED_DEFINITION cs_definition, "+
		"ClassSchemeItems.LONG_NAME csi_name, "+
		"ClassSchemeItems.csitl_name "+
	 */
	static List<String> columnNamesAltNames = Arrays.asList("cs_csi_idseq", "cs_long_name", "cs_definition", "csi_name", "csitl_name");
	static String[][] resultArray = {
			{"cs_csi_idseq11", "cs_long_name12", "cs_definition13", "csi_name14", "csitl_name15"}, 
			{"cs_csi_idseq21", "cs_long_name22", "cs_definition23", "csi_name24", "csitl_name25"}, 
			{"cs_csi_idseq31", "cs_long_name32", "cs_definition33", "csi_name34", "csitl_name35"}
	};
	@Test
	public void test() throws SQLException {
		CsCsiDeRowMapper csCsiDeRowMapper = new CsCsiDeRowMapper();
		ResultSetMock resultSetMock = new ResultSetMock();
		ResultSet resultSet = resultSetMock.getResultSet();

		for (int i = 0; i < resultArray.length; i++) {
			//MUT
			resultSetMock.setCurrentRowData(resultArray[i]);
			CsCsiDeModel csCsiModel = csCsiDeRowMapper.mapRow(resultSet, i);
			//check
			assertEquals(resultArray[i][0], csCsiModel.getCsCsiIdseq());
			assertEquals(resultArray[i][1], csCsiModel.getCsLongName());
			assertEquals(resultArray[i][2], csCsiModel.getCsDefinition());
			assertEquals(resultArray[i][3], csCsiModel.getCsiName());
			assertEquals(resultArray[i][4], csCsiModel.getCsitlName());
		}

		
	}
	static class ResultSetMock  {
        final MockRow row = new MockRow(columnNamesAltNames);
        
		public void setCurrentRowData(String[] dataArr) {
			row.setCurrentRowData(dataArr);
		}
	    public ResultSet getResultSet() throws SQLException{
	        ResultSet resultSet = mock(ResultSet.class);
	        //we do not have next in the current DAO implementation
	//        doAnswer(new Answer<Boolean>() {
	//            @Override
	//            public Boolean answer(InvocationOnMock invocation) throws Throwable {
	//                int index = idx.getAndIncrement();
	//                if (index < resultArray.length) {
	//                    row.setCurrentRowData(resultArray[index]);
	//                    return true;
	//                } else
	//                    return false;
	//            }
	//        }).when(resultSet).next();
	
	        doAnswer(new Answer<String>() {
	            @Override
	            public String answer(InvocationOnMock invocation) throws Throwable {
	                Object[] args = invocation.getArguments();
	                int idx = (Integer) args[0];
	                return row.getString(idx);
	            }
	        }).when(resultSet).getString(anyInt());
	
	        doAnswer(new Answer<String>() {
	            @Override
	            public String answer(InvocationOnMock invocation) throws Throwable {
	                Object[] args = invocation.getArguments();
	                String name = (String) args[0];
	                return row.getString(name);
	            }
	        }).when(resultSet).getString(anyString());
	
	        return resultSet;
	    }
	}
    static class MockRow {
        Object[] rowData;
        List<String> columnNames;

        public MockRow(List<String> columnNames) {

            this.columnNames = columnNames;
        }

        public void setCurrentRowData(Object[] rowData) {
            this.rowData = rowData;
        }

        public String getString(int idx) {
            return (String)rowData[idx - 1];
        }

        public String getString(String name) {
        	int idx = -1;
        	for (int i = 1; i <= columnNames.size(); i++) {
        		if (columnNames.get(i - 1).equals(name)) {
        			idx = i - 1;
        			break;
        		}
        	}
        	if (idx >= 0) {
        		return (String)rowData[idx];
        	}
        	else 
        		return null;
        }

    }
}
