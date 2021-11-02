/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.download;

import java.io.FileOutputStream;

import java.util.Collection;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import gov.nih.nci.cadsr.service.restControllers.DownloadExcelController;
/**
 * 
 * @author asafievan
 *
 */
public class GetExcelDownloadTestImpl implements GetExcelDownloadInterface {
	private String localDownloadDirectory;  //"/local/content/cdebrowser/output/" a value provided by service controller
	private String fileNamePrefix; // a value provided by service controller

	public void setLocalDownloadDirectory(String localDownloadDirectory) {
		this.localDownloadDirectory = localDownloadDirectory;
	}

	public void setFileNamePrefix(String excelFileNamePrefix) {
		this.fileNamePrefix = excelFileNamePrefix;
	}
	private String fileId = "007";

	public String getFileId() {
		return fileId;
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
		Sheet sheet = wb.createSheet();
		int rowNumber = 0;

		CellStyle boldCellStyle = wb.createCellStyle();
		Font font = wb.createFont();
		font.setBold(true); // Commented by Vikram S //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		boldCellStyle.setFont(font);
		boldCellStyle.setAlignment(HorizontalAlignment.GENERAL);// Commented by Vikram S //boldCellStyle.setAlignment(HSSFCellStyle.ALIGN_GENERAL);


		// Create a row and put the column header in it
		Row row = sheet.createRow(rowNumber++);
		int col0 = 0;
		Cell cell= row.createCell(col0);
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
		String fileName = buildDownloadAbsoluteFileName(fileId);
		//System.out.println("fileName: " + fileName);
		FileOutputStream fileOut = new FileOutputStream(fileName);
		wb.write(fileOut);
		wb.close();
		fileOut.flush();
		fileOut.close();
		return fileId;
	}
	public String buildDownloadAbsoluteFileName(String excelFileSuffix) {
		String excelFilename = localDownloadDirectory +  fileNamePrefix + excelFileSuffix  + DownloadExcelController.fileExtension;
		return excelFilename;
	}

	@Override
	public String generateExcelFileId() throws Exception {
		return "1234567";
	}
}
