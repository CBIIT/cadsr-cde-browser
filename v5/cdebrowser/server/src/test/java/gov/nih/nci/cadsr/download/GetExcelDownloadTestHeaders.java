/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.download;

import java.io.FileInputStream;
import java.util.List;

import javax.sql.DataSource;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.mockito.Mockito;

import gov.nih.nci.cadsr.download.GetExcelDownload.ColumnInfo;

public class GetExcelDownloadTestHeaders {
	//AssertionError
	@Test(expected=AssertionError.class)
	public void testdeSearchWrong() throws Exception {	
		
		checkHeaders("CDEBrowser_deSearch_headers_old_4_x.xls", "deSearch");
	}
	@Test(expected=AssertionError.class)
	public void testdeSearchWrong2() throws Exception {	
		
		checkHeaders("CDEBrowser_deSearch_headers_old_4_x.xls", "cdeCart");
	}
	@Test(expected=AssertionError.class)
	public void testdeSearchPriorWrongName() throws Exception {	
		
		checkHeaders("CDEBrowser_deSearchPrior_headers_wrong_name.xls", "deSearchPrior");
	}
	@Test(expected=AssertionError.class)
	public void testdeSearchPriorWrongColumn() throws Exception {	
		
		checkHeaders("CDEBrowser_deSearchPrior_headers_wrong_column.xls", "deSearchPrior");
	}
	@Test
	public void testdeSearch() throws Exception {	
		
		checkHeaders("CDEBrowser_deSearch_headers.xls", "deSearch");
	}
	@Test
	public void testcdeCart() throws Exception {	
		
		checkHeaders("CDEBrowser_deSearch_headers.xls", "cdeCart");
	}
	@Test
	public void testdeSearchPrior() throws Exception {	
		
		checkHeaders("CDEBrowser_deSearchPrior_headers.xls", "deSearchPrior");
	}
	@Test
	public void testcdeCartPrior() throws Exception {	
		
		checkHeaders("CDEBrowser_deSearchPrior_headers.xls", "cdeCartPrior");
	}
	public void checkHeaders(String strFileName, String exportType) throws Exception {
		//this file is a prepared Excel file with expected headers for this export which shall stay the same as in CDE Browser 4.0
		String fileName = ClassLoader.getSystemResource(strFileName).getFile();
		FileInputStream fis = new FileInputStream(fileName);
		HSSFWorkbook wbExpected = new HSSFWorkbook(fis);
		HSSFSheet sheetExpected = wbExpected.getSheetAt(0);
		HSSFRow rowExpected = sheetExpected.getRow(0);
		wbExpected.close();
		
		DataSource dataSource = Mockito.mock(DataSource.class);
		GetExcelDownload getExcelDownload = new GetExcelDownload(dataSource);
		List<ColumnInfo> columnInfo = getExcelDownload.initColumnInfo(exportType);
		HSSFWorkbook wbhReceived = new HSSFWorkbook();
		HSSFSheet sheethReceived = wbhReceived.createSheet();
		HSSFCellStyle boldCellStyle = wbhReceived.createCellStyle();
		HSSFFont font = wbhReceived.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		boldCellStyle.setFont(font);
		boldCellStyle.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
		
		//MUT
		getExcelDownload.generateExcelHeaders(sheethReceived, boldCellStyle, exportType, columnInfo);
		HSSFRow rowReceived = sheethReceived.getRow(0);
		//check
		
		//boolean res = CompareExcelTables.compareSheets(sheetExpected, sheethReceived);
		boolean res2 = CompareExcelTables.compareRows(rowExpected, rowReceived, 0);
		wbhReceived.close();
		assertTrue("Headers rows are the same", res2);
		//assertTrue("Worksbooks are the same", res);
	}

}
