/*
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.download;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class CompareExcelTables {
	//CDEBrowser_SearchResults-30.xls
	public static void main(String[] args) throws Exception {
		//expected the same
		boolean res1 = compareExcelWorkbooks("1_DEV_CDEBrowser_Excel_20160303_4_0_5.xls", "2_DEV_CDEBrowser_Excel_20160303_5_1.xls");
		System.out.println("workbooks are the same #1 : " + res1 +"\n");
		boolean res2 = compareExcelWorkbooks("3_DEV_CDEBrowser_PriorExcel_20160303_4_0_5.xls", "4_DEV_CDEBrowser_PriorExcel_20160303_5_1.xls");
		System.out.println("workbooks are the same #2 : " + res2);
		//different
//		boolean res = compareExcelWorkbooks("1_DEV_CDEBrowser_Excel_20160303_4_0_5.xls", "4_DEV_CDEBrowser_PriorExcel_20160303_5_1.xls");
//		System.out.println("workbooks are the same: " + res);
		
	}
	public static HSSFWorkbook retrieveWorkbook(String strFileName) throws Exception {
		String fileName = ClassLoader.getSystemResource(strFileName).getFile();
		FileInputStream fis = new FileInputStream(fileName);
		HSSFWorkbook wbExpected = new HSSFWorkbook(fis);
		fis.close();
		return wbExpected;
	}
	public static boolean compareExcelWorkbooks(String fileNameOld, String fileNameNew) throws Exception {

		HSSFWorkbook wbOld = retrieveWorkbook(fileNameOld);
		HSSFWorkbook wbNew = retrieveWorkbook(fileNameNew);
		HSSFSheet sheetOld = wbOld.getSheetAt(0);
		HSSFSheet sheetNew = wbNew.getSheetAt(0);
		try {
			wbOld.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			wbNew.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return compareSheets(sheetOld, sheetNew);
	}
	public static boolean compareSheets(HSSFSheet sheetOld, HSSFSheet sheetNew) {
		int count = 0;
		if (sheetNew.getLastRowNum() != sheetOld.getLastRowNum()) {
			System.out.println("different row in sheets : " + sheetNew.getLastRowNum() + ":" + sheetOld.getLastRowNum());
			return false;
		}
		else {
			System.out.println("number of rows in sheets: " + sheetNew.getLastRowNum());
		}
		
		Row headerOld = (Row)sheetOld.getRow(0);
		Row headerNew = (Row)sheetNew.getRow(0);
		
		if (headerOld.getLastCellNum() != headerNew.getLastCellNum()) {
			System.out.println("number of header columns in old: " + headerOld.getLastCellNum());
			System.out.println("number of header columns in new: " + headerNew.getLastCellNum());
			return false;
		}
		else {
			System.out.println("number of header columns: " + headerOld.getLastCellNum());
		}
		int rowIndex;
		for (rowIndex = 0; rowIndex < sheetOld.getLastRowNum(); rowIndex++){
			HSSFRow rowOld = sheetOld.getRow(rowIndex);
			HSSFRow rowNew = sheetNew.getRow(rowIndex);
			
			if (! compareRows(rowOld, rowNew, rowIndex)) {
				count++;
			}
		}
		
		System.out.println("...checked rows: " + rowIndex + ", number of different rows: " + count);
		return (count == 0);
	}
	public static boolean compareRows(Row rowOld, Row rowNew, int rowIndex) {
		if ((rowOld != null) && (rowNew == null)) {
			System.out.println("old row is null, new is not null: " + rowIndex);
			return false;
		}
		else if	((rowOld == null) && (rowNew != null)) {
			System.out.println("old row is not null, new is null: " + rowIndex);
			return false;
		}
		else if	((rowOld == null) && (rowNew == null)) {
			return true;
		}
		
		int celLastOld = rowOld.getLastCellNum();
		int celLastNew = rowOld.getLastCellNum();
		if (celLastOld != celLastNew) {
			System.out.println("different last column number in row old : " + celLastOld + ":" + celLastNew + ", rowIndex: " + rowIndex);
			return false;
		}
		for (int cellnum = 0; cellnum < celLastOld; cellnum++) {
			if (! compareCells (rowOld.getCell(cellnum), rowNew.getCell(cellnum), cellnum)) {
				System.out.println("rows are different rowIndex: " +  rowIndex);
				return false;
			}
		}
		return true;
	}
	
	private static boolean compareCells(Cell cellOld, Cell cellNew, int cellIndex) {
		if	(cellOld == null) {
			if ((cellNew != null) && (cellNew.getCellType() == Cell.CELL_TYPE_STRING) && (cellNew.getStringCellValue() != null)){
				System.out.println("old cell is null, new is not null: " + cellNew + ", cellIndex:" +  cellIndex + ", row number: " + cellNew.getRowIndex());
				return false;
			}
			else 
				return true;//both null //Null cell and cell with Null value we consider the same
		}

		if ((cellOld != null) && (cellNew == null)) {
			if ((cellOld.getCellType() == Cell.CELL_TYPE_STRING) && (cellOld.getStringCellValue() != null)) {
				System.out.println("old cell is not null " + cellOld + ", new is null, celIndex" + cellIndex + ", row number: " + cellOld.getRowIndex());
				return false;
			}
			else 
				return true;//Null cell and cell with Null value we consider the same
		}
		
		int typeOld = cellOld.getCellType();
		int typeNew = cellNew.getCellType();
		if (typeOld != typeNew) {
			System.out.println("typeOld: " + typeOld + ", typeNew " + typeNew + ", cellIndex: " + cellIndex + ", row number: " + cellNew.getRowIndex());
			return false;
		}
		if (typeOld == Cell.CELL_TYPE_NUMERIC) {//0
			double dold = cellOld.getNumericCellValue();
			double dnew = cellNew.getNumericCellValue();
			if (dold != dnew) {
				System.out.println("numeric cells are different cellIndex: " +  cellIndex + ", row number: " + cellNew.getRowIndex());
				return false;
			}
		}
		else if ((typeOld == Cell.CELL_TYPE_STRING) || (typeOld == Cell.CELL_TYPE_BLANK)) {
			String sold = cellOld.getStringCellValue();
			String snew = cellNew.getStringCellValue();
			if ((sold == null) && (snew != null)) {
				System.out.println("String type cells are different old is null, new is " + snew + ", " + " cellIndex: " +  cellIndex + ", row number: " + cellNew.getRowIndex());				
				return false;
			}
			else if((sold != null) && (snew == null)) {
				System.out.println("String type cells are different old is null " + sold + ", new is null " + " cellIndex: " +  cellIndex + ", row number: " + cellNew.getRowIndex());				
				return false;
			}
			else if ((sold == null) && (snew == null)) {
				return true;
			}
			if (!(sold.equals(snew))) {
				System.out.println("String type cells are different cellIndex: " +  cellIndex + ", row number: " + cellOld.getRowIndex() + ", sold=" + sold +", snew=" + snew);
				return false;
			} 
		}
		else {
			try {
				Date dateOld = cellOld.getDateCellValue();
				Date dateNew = cellNew.getDateCellValue();

				if (dateOld.getTime() != dateNew.getTime()) {
					System.out.println("Date type cells are different; old " +  dateOld 
							+ ", new = " + dateNew + ", cellIndex: " + cellIndex + ", row number: " + cellOld.getRowIndex());
				}
			}
			catch (Exception e) {
				System.out.println(" Exception trying to get Date type celType= " + typeOld + ", cellIndex= " + cellIndex + ", " + e);
			}
		}
		return true;
	}
}
