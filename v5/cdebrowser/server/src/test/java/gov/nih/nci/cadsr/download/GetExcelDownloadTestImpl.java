/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.download;

import java.io.FileOutputStream;
import java.util.Collection;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class GetExcelDownloadTestImpl implements GetExcelDownloadInterface {
	private String excelFileName; // a value provided in Bean context

	public void setExcelFileName(String excelFileName) {
		this.excelFileName = excelFileName;
	}
	
	public String getExcelFileName() {
		String dir = System.getProperty("user.dir");
		String fileName = dir + "/src/test/resources/" + excelFileName;
		return fileName;
	}

	/**
	 * This method creates a file in project test/resources directory.
	 * Delete this file after calling this method
	 * 
	 * @return String file name with test Excel data
	 */
	@Override
	public String persist(final Collection<String> itemIds, final String RAI, final String source) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		int rowNumber = 0;

		HSSFCellStyle boldCellStyle = wb.createCellStyle();
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		boldCellStyle.setFont(font);
		boldCellStyle.setAlignment(HSSFCellStyle.ALIGN_GENERAL);


		// Create a row and put the column header in it
		HSSFRow row = sheet.createRow(rowNumber++);
		int col0 = 0;
		HSSFCell cell= row.createCell(col0);
		cell.setCellStyle(boldCellStyle);
		cell.setCellValue("Test Column Number");
		
		cell= row.createCell(1);
		cell.setCellStyle(boldCellStyle);
		cell.setCellValue("Test Column String");
		
		row = sheet.createRow(rowNumber++);
		cell = row.createCell(col0);
		cell.setCellValue(848);
		
		cell = row.createCell(1);
		cell.setCellValue("Test Column Data");
		
		//This shall be cleaned by the calling test
		String dir = System.getProperty("user.dir");
		String fileName = dir + "/src/test/resources/" + excelFileName;
		//System.out.println("fileName: " + fileName);
		FileOutputStream fileOut = new FileOutputStream(fileName);
		wb.write(fileOut);
		wb.close();
		fileOut.flush();
		fileOut.close();
		return fileName;
	}

}
