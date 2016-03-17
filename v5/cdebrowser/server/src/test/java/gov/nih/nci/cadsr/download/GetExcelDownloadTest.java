/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.download;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import gov.nih.nci.cadsr.download.GetExcelDownload.ColumnInfo;
import gov.nih.nci.cadsr.service.ClientException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/test-application-context.xml")
public class GetExcelDownloadTest {

	@Test
	public void testPersistHeaders() throws Exception {
		DataSource dataSource = Mockito.mock(DataSource.class);
		GetExcelDownload getExcelDownload = new GetExcelDownload(dataSource);

		List<ColumnInfo> columnInfo = initColumnInfo("deSearch", getExcelDownload);
		//this test is specific for columnInfo list. If changed this test shall be changed
		HSSFWorkbook wbh = new HSSFWorkbook();
		HSSFSheet sheeth = wbh.createSheet();
		HSSFCellStyle boldCellStyle = wbh.createCellStyle();
		HSSFFont font = wbh.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		boldCellStyle.setFont(font);
		boldCellStyle.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
		
		//MUT
		getExcelDownload.generateExcelHeaders(sheeth, boldCellStyle, "deSearch", columnInfo);
		//for manual testing and looking into Excel use commented code
//		FileOutputStream fileOut1 = new FileOutputStream("testExcelHeaders.xls");
//		
//		wbh.write(fileOut1);
//		fileOut1.flush();
//		fileOut1.close();
		
		//check
		HSSFRow row = sheeth.getRow(0);

		HSSFCell receivedCell = row.getCell(0);
		assertEquals(columnInfo.get(0).displayName, receivedCell.getStringCellValue());
		receivedCell = row.getCell(0);
		assertEquals(boldCellStyle, receivedCell.getCellStyle());
		ColumnInfo currColumnInfo = columnInfo.get(1);
		receivedCell = row.getCell(1);
		assertEquals(boldCellStyle, receivedCell.getCellStyle());
		assertEquals(currColumnInfo.displayName + currColumnInfo.nestedColumns.get(0).displayName, receivedCell.getStringCellValue());
		receivedCell = row.getCell(2);
		assertEquals(boldCellStyle, receivedCell.getCellStyle());
		assertEquals(currColumnInfo.displayName + currColumnInfo.nestedColumns.get(1).displayName, receivedCell.getStringCellValue());
		receivedCell = row.getCell(3);
		assertEquals(boldCellStyle, receivedCell.getCellStyle());
		currColumnInfo = columnInfo.get(2);
		assertEquals(boldCellStyle, receivedCell.getCellStyle());
		assertEquals(currColumnInfo.displayName + currColumnInfo.nestedColumns.get(0).displayName, receivedCell.getStringCellValue());
		receivedCell = row.getCell(4);
		assertEquals(boldCellStyle, receivedCell.getCellStyle());
		assertEquals(currColumnInfo.displayName + currColumnInfo.nestedColumns.get(1).displayName, receivedCell.getStringCellValue());
		
		wbh.close();
	}
	@Test
	public void testPersistStruct() throws Exception {
		//mock objects
		ResultSet rs = Mockito.mock(ResultSet.class);
        Mockito.when(rs.next()).thenReturn(true, true, false);//two main rows
        DataSource dataSource = Mockito.mock(DataSource.class);
		GetExcelDownload getExcelDownload = new GetExcelDownload(dataSource);
		List<ColumnInfo> columnInfo = initColumnInfoStruct("deSearch", getExcelDownload);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
				
		Mockito.when(rs.getString("LONG_NAME")).thenReturn(dataMatrixStruct[0][0]).thenReturn(dataMatrixStruct[2][0]);
		//Mock Struct
		Struct structSqlDe0 = Mockito.mock(Struct.class);
		Object[] valueStructDe0 = new Object[4];
		valueStructDe0[0] = dataMatrixStruct[0][1];
		valueStructDe0[2] = dataMatrixStruct[0][2];
		valueStructDe0[3] = dataMatrixStruct[0][3];
		Mockito.when(structSqlDe0.getAttributes()).thenReturn(valueStructDe0).thenReturn(valueStructDe0).thenReturn(valueStructDe0);
		Struct structSqlDe2 = Mockito.mock(Struct.class);
		Object[] valueStructDe2 = new Object[4];
		valueStructDe2[0] = dataMatrixStruct[2][1];
		valueStructDe2[2] = dataMatrixStruct[2][2];
		valueStructDe2[3] = dataMatrixStruct[2][3];
		Mockito.when(structSqlDe2.getAttributes()).thenReturn(valueStructDe2).thenReturn(valueStructDe2).thenReturn(valueStructDe2);
		Mockito.when(rs.getObject("DE_DERIVATION")).thenReturn(structSqlDe0).thenReturn(structSqlDe0).thenReturn(structSqlDe0)
			.thenReturn(structSqlDe2).thenReturn(structSqlDe2).thenReturn(structSqlDe2);
		
		//MUT
		getExcelDownload.generateWorkbook(rs, sheet, 0, "deSearch", columnInfo);
		wb.close();
		//for manual testing and looking into Excel use commented code
//		FileOutputStream fileOut = new FileOutputStream("testExcel.xls");
//		wb.write(fileOut);
//		fileOut.flush();
//		fileOut.close();
		
		//check
		HSSFSheet receivedSheet = wb.getSheetAt(0);
		//row # 0
		checkFullRow(receivedSheet, 0, 3, dataMatrixStruct);

		//row # 1 empty between groups
		assertNull(receivedSheet.getRow(1));

		//row # 2
		checkFullRow(receivedSheet, 2, 3, dataMatrixStruct);
	}
	@Test
	public void testPersistRsSubIdx() throws Exception {
		//mock objects
		ResultSet rs = Mockito.mock(ResultSet.class);
        Mockito.when(rs.next()).thenReturn(true, true, false);//two main rows
        DataSource dataSource = Mockito.mock(DataSource.class);
		GetExcelDownload getExcelDownload = new GetExcelDownload(dataSource);
		List<ColumnInfo> columnInfo = initColumnInfoRsSubIdx("deSearch", getExcelDownload);

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
				
		Mockito.when(rs.getString("OBJ_CLASS")).thenReturn(dataMatrixWithRsSubIdxExpected[0][0]).thenReturn(dataMatrixWithRsSubIdxExpected[2][0]);
		//Mock Array
		ResultSet nestedRs0 = Mockito.mock(ResultSet.class);
		ResultSet nestedRs1 = Mockito.mock(ResultSet.class);

        Mockito.when(nestedRs0.next()).thenReturn(true, false);
        Mockito.when(nestedRs1.next()).thenReturn(true, false);
		java.sql.Array sqlArray0 = Mockito.mock(java.sql.Array.class);
		java.sql.Array sqlArray1 = Mockito.mock(java.sql.Array.class);
		Struct valueStructSql0 = Mockito.mock(Struct.class);
		Struct valueStructSql1 = Mockito.mock(Struct.class);
		Mockito.when(sqlArray0.getResultSet()).thenReturn(nestedRs0);
		Mockito.when(sqlArray1.getResultSet()).thenReturn(nestedRs1);
		Mockito.when(nestedRs0.getObject(2)).thenReturn(valueStructSql0);
		Mockito.when(nestedRs1.getObject(2)).thenReturn(valueStructSql1);
		Struct nestedStructSql0 = Mockito.mock(Struct.class);
		Struct nestedStructSql1 = Mockito.mock(Struct.class);

		//data
		Struct[] structArray0 = new Struct[1];
		structArray0[0] = nestedStructSql0;
		
		Struct[] structArray1 = new Struct[1];
		structArray1[0] = nestedStructSql1;	
		
		Mockito.when(valueStructSql0.getAttributes()).thenReturn(structArray0);
		Mockito.when(valueStructSql1.getAttributes()).thenReturn(structArray1);

		Object[] nestedDatum0 = new Object[5];
		nestedDatum0[1] = dataMatrixWithRsSubIdx[0][1];
		nestedDatum0[2] = dataMatrixWithRsSubIdx[0][2];	
		nestedDatum0[3] = dataMatrixWithRsSubIdx[0][3];
		nestedDatum0[4] = dataMatrixWithRsSubIdx[0][4];
		
		Object[] nestedDatum2 = new Object[5];
		nestedDatum2[1] = dataMatrixWithRsSubIdx[2][1];
		nestedDatum2[2] = dataMatrixWithRsSubIdx[2][2];
		nestedDatum2[3] = dataMatrixWithRsSubIdx[2][3];
		nestedDatum2[4] = dataMatrixWithRsSubIdx[2][4];
		
		Mockito.when(nestedStructSql0.getAttributes()).thenReturn(nestedDatum0);
		Mockito.when(nestedStructSql1.getAttributes()).thenReturn(nestedDatum2);
		
		Mockito.when(rs.getArray("CLASSIFICATIONS")).thenReturn(sqlArray0).thenReturn(sqlArray1);
		
		//MUT
		getExcelDownload.generateWorkbook(rs, sheet, 0, "deSearch", columnInfo);
		wb.close();
		//for manual testing and looking into Excel use commented code
		FileOutputStream fileOut = new FileOutputStream("testExcel.xls");
		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();
		
		//check
		HSSFSheet receivedSheet = wb.getSheetAt(0);
		//row # 0
		checkFullRow(receivedSheet, 0, 4, dataMatrixWithRsSubIdxExpected);

		//row # 1 empty between groups
		assertNull(receivedSheet.getRow(1));

		//row # 2
		checkFullRow(receivedSheet, 2, 4, dataMatrixWithRsSubIdxExpected);
	}
	@Test
	public void testPersist() throws Exception {
		//mock objects
		ResultSet rs = Mockito.mock(ResultSet.class);
        Mockito.when(rs.next()).thenReturn(true, true, false);//two main rows
		ResultSet nestedRs0 = Mockito.mock(ResultSet.class);
		ResultSet nestedRs1 = Mockito.mock(ResultSet.class);

        Mockito.when(nestedRs0.next()).thenReturn(true, true, false);
        Mockito.when(nestedRs1.next()).thenReturn(true, true, false);
		java.sql.Array sqlArray0 = Mockito.mock(java.sql.Array.class);
		java.sql.Array sqlArray1 = Mockito.mock(java.sql.Array.class);
		Struct valueStructSql0 = Mockito.mock(Struct.class);		
		Struct valueStructSql1 = Mockito.mock(Struct.class);	
		Mockito.when(sqlArray0.getResultSet()).thenReturn(nestedRs0);
		Mockito.when(sqlArray1.getResultSet()).thenReturn(nestedRs1);
		Mockito.when(nestedRs0.getObject(2)).thenReturn(valueStructSql0);
		Mockito.when(nestedRs1.getObject(2)).thenReturn(valueStructSql1);
		//data
		Object[] objArray0 = new Object[2];
		objArray0[0] = dataMatrix[0][1];
		objArray0[1] = dataMatrix[0][2];	
		Object[] objArray1 = new Object[2];
		objArray1[0] = dataMatrix[1][1];
		objArray1[1] = dataMatrix[1][2];			
		Object[] objArray2 = new Object[2];
		objArray2[0] = dataMatrix[3][1];
		objArray2[1] = dataMatrix[3][2];
		Object[] objArray3 = new Object[2];
		objArray3[0] = dataMatrix[4][1];
		objArray3[1] = dataMatrix[4][2];
		
		Object[] objArrayDe1 = new Object[2];
		objArrayDe1[0] = dataMatrix[0][3];
		objArrayDe1[1] = dataMatrix[0][4];	
		Object[] objArrayDe2 = new Object[2];
		objArrayDe2[0] = dataMatrix[1][3];
		objArrayDe2[1] = dataMatrix[1][4];
		Object[] objArrayDe3 = new Object[2];
		objArrayDe3[0] = dataMatrix[3][3];
		objArrayDe3[1] = dataMatrix[3][4];	
		Object[] objArrayDe4 = new Object[2];
		objArrayDe4[0] = dataMatrix[4][3];
		objArrayDe4[1] = dataMatrix[4][4];		
		Mockito.when(valueStructSql0.getAttributes()).thenReturn(objArray0).thenReturn(objArray1);
		Mockito.when(valueStructSql1.getAttributes()).thenReturn(objArray2).thenReturn(objArray3);
		
        Mockito.when(rs.getString("PREFERRED_NAME")).thenReturn(dataMatrix[0][0]).thenReturn(dataMatrix[3][0]);
        Mockito.when(rs.getArray("oc_concepts")).thenReturn(sqlArray0).thenReturn(sqlArray1);
        
        Struct structSqlDe1 = Mockito.mock(Struct.class);
        Object[] valueStructDe1 = new Object[1];
        java.sql.Array sqlArrayDe1 = Mockito.mock(java.sql.Array.class);
        valueStructDe1[0] = sqlArrayDe1;
        Mockito.when(structSqlDe1.getAttributes()).thenReturn(valueStructDe1);
        ResultSet nestedRsDs1 = Mockito.mock(ResultSet.class);
        Struct valueStructSqlDe1 = Mockito.mock(Struct.class);	
        Mockito.when(sqlArrayDe1.getResultSet()).thenReturn(nestedRsDs1);
        Mockito.when(nestedRsDs1.next()).thenReturn(true, true, false);
        Mockito.when(nestedRsDs1.getObject(2)).thenReturn(valueStructSqlDe1);
        Mockito.when(valueStructSqlDe1.getAttributes()).thenReturn(objArrayDe1).thenReturn(objArrayDe2);
        Struct structSqlDe2 = Mockito.mock(Struct.class);
        java.sql.Array sqlArrayDe2 = Mockito.mock(java.sql.Array.class);
        Object[] valueStructDe2 = new Object[2];
        valueStructDe2[0] = sqlArrayDe2;
        Mockito.when(structSqlDe2.getAttributes()).thenReturn(valueStructDe2);
        ResultSet nestedRsDs2 = Mockito.mock(ResultSet.class);
        Struct valueStructSqlDe2 = Mockito.mock(Struct.class);	
        Mockito.when(sqlArrayDe2.getResultSet()).thenReturn(nestedRsDs2);
        Mockito.when(nestedRsDs2.next()).thenReturn(true, true, false);
        Mockito.when(nestedRsDs2.getObject(2)).thenReturn(valueStructSqlDe2);
        Mockito.when(valueStructSqlDe2.getAttributes()).thenReturn(objArrayDe3).thenReturn(objArrayDe4);
        Mockito.when(rs.getObject("DE_DERIVATION")).thenReturn(structSqlDe1).thenReturn(structSqlDe2);
        
        DataSource dataSource = Mockito.mock(DataSource.class);
		GetExcelDownload getExcelDownload = new GetExcelDownload(dataSource);
		List<ColumnInfo> columnInfo = initColumnInfo("deSearch", getExcelDownload);
		
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		
		//MUT
		getExcelDownload.generateWorkbook(rs, sheet, 0, "deSearch", columnInfo);
		wb.close();
		
		//for manual testing and looking into Excel use commented code
		FileOutputStream fileOut = new FileOutputStream("testExcel.xls");
		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();	
		
		HSSFSheet receivedSheet = wb.getSheetAt(0);
		//row # 0
		checkFullRow(receivedSheet, 0, 4, dataMatrix);
		//row # 1
		checkExtraRow(receivedSheet, 1, 1, 4, dataMatrix);

		//row # 2 empty between groups
		HSSFRow rowReceived2 = receivedSheet.getRow(2);
		assertNull(rowReceived2);
		
		//row # 3
		checkFullRow(receivedSheet, 3, 4, dataMatrix);

		//row # 4
		checkExtraRow(receivedSheet, 4, 1, 4, dataMatrix);
	}
	private void checkFullRow(HSSFSheet receivedSheet, int i, int lastCol, String[][] dataExpected) {
		HSSFRow rowReceived0 = receivedSheet.getRow(i);
		assertNotNull(rowReceived0);
		for (int j = 0; j <= lastCol; j++) {
			HSSFCell cellReceived00 = rowReceived0.getCell(j);
			assertNotNull(cellReceived00);
			String strReceived00 = cellReceived00.getStringCellValue();
			assertEquals(dataExpected[i][j],  strReceived00);
		}
		for (int k = 1; k < 10; k++)
			assertNull(rowReceived0.getCell(lastCol+k));
	}
	private void checkExtraRow(HSSFSheet receivedSheet, int i, int startCol, int lastCol, String[][] dataExpected) {
		HSSFRow rowReceived0 = receivedSheet.getRow(i);
		assertNotNull(rowReceived0);
		for (int j = 0; j < startCol; j++) {
			assertNull(rowReceived0.getCell(j));
		}
		for (int j = startCol; j <= lastCol; j++) {
			HSSFCell cellReceived00 = rowReceived0.getCell(j);
			assertNotNull(cellReceived00);
			String strReceived00 = cellReceived00.getStringCellValue();
			assertEquals(dataExpected[i][j],  strReceived00);
		}
	}

	private List<ColumnInfo> initColumnInfo(String source, GetExcelDownload getExcelDownload) {
		List<ColumnInfo> columnInfo = new ArrayList<>();

		columnInfo.add(getExcelDownload.new ColumnInfo("PREFERRED_NAME", "Data Element Short Name", "String"));
		//test Array
		List<ColumnInfo> ocConceptInfo = new ArrayList<>();
		ocConceptInfo.add(getExcelDownload.new ColumnInfo(0, "Code"));
		ocConceptInfo.add(getExcelDownload.new ColumnInfo(1, "Name"));
		ColumnInfo ocConcepts =
				getExcelDownload.new ColumnInfo("oc_concepts", "Object Class Concept ", "Array");
		ocConcepts.nestedColumns = ocConceptInfo;
		columnInfo.add(ocConcepts);
		
		//test struct
		List<ColumnInfo> dedInfo = new ArrayList<>();
		dedInfo.add(getExcelDownload.new ColumnInfo(0, "Public ID"));
		dedInfo.add(getExcelDownload.new ColumnInfo(1, "Long Name"));
		ColumnInfo deDrivation =
				getExcelDownload.new ColumnInfo(0, "DE_DERIVATION", "DDE ", "StructArray");
		deDrivation.nestedColumns = dedInfo;
		columnInfo.add(deDrivation);
		return columnInfo;
		
	}
	private String[][] dataMatrix = {
		{"testPreferredName0", "test_OC_code01", "test_OC_name02", "test_public_id03", "test_longname_04"},
		{null					, "test_OC_code11", "test_OC_name12", "test_public_id13", "test_longname_14"},
		{},
		{"testPreferredName3", "test_OC_code31", "test_OC_name32", "test_public_id33", "test_longname_34"},
		{null					, "test_OC_code41", "test_OC_name42", "test_public_id43", "test_longname_44"},
	};
	
	private List<ColumnInfo> initColumnInfoStruct(String source, GetExcelDownload getExcelDownload) {
		List<ColumnInfo> columnInfo = new ArrayList<>();
		columnInfo.add(getExcelDownload.new ColumnInfo("LONG_NAME", "Data Element Long Name", "String"));
		
		columnInfo.add(getExcelDownload.new ColumnInfo(0, "DE_DERIVATION", "Derivation Type", "Struct"));
		columnInfo.add(getExcelDownload.new ColumnInfo(2, "DE_DERIVATION", "Derivation Method", "Struct"));
		columnInfo.add(getExcelDownload.new ColumnInfo(3, "DE_DERIVATION", "Derivation Rule", "Struct"));
		return columnInfo;
	}
	private String[][] dataMatrixStruct = {
		{"testLongName0", "test Derivation Type01", "test Derivation Method02", "test Derivation Rule03"},
		{},
		{"testLongName2", "test Derivation Type21", "test Derivation Method22", "test Derivation Rule23"},
	};
	
	private List<ColumnInfo> initColumnInfoRsSubIdx(String source, GetExcelDownload getExcelDownload) {
		List<ColumnInfo> columnInfo = new ArrayList<>();

		columnInfo.add(getExcelDownload.new ColumnInfo("OBJ_CLASS", "Object Class", "String"));
		
		List<ColumnInfo> csInfo = new ArrayList<>();
		ColumnInfo classification = getExcelDownload.new ColumnInfo("CLASSIFICATIONS", "Classification Scheme ", "Array");
		
		csInfo.add(getExcelDownload.new ColumnInfo(0, 3, "Short Name", "String"));
		//}
		csInfo.add(getExcelDownload.new ColumnInfo(0, 4, "Version","Number"));
		csInfo.add(getExcelDownload.new ColumnInfo(0, 1, "Context Name", "String"));
		csInfo.add(getExcelDownload.new ColumnInfo(0, 2, "Context Version","Number"));
		columnInfo.add(classification);
		classification.nestedColumns = csInfo;
		return columnInfo;
	}
	private Object[][] dataMatrixWithRsSubIdx = {
			{"testObjClass0", "test class Short Name01", new Integer(102), "test class Context Name03", new Integer(1004)},
			{},
			{"testObjClass2", "test class Short Name21", new Integer(202), "test class Context Name23", new Integer(2004)}
		};
	private String[][] dataMatrixWithRsSubIdxExpected = {
			{"testObjClass0", "test class Context Name03", "1004", "test class Short Name01", "102"},
			{},
			{"testObjClass2", "test class Context Name23", "2004", "test class Short Name21", "202"}
		};
}
