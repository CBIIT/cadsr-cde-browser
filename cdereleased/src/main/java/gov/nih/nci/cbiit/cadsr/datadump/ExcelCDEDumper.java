package gov.nih.nci.cbiit.cadsr.datadump;
/*
 * Copyright (C) 2018 Leidos Biomedical Research, Inc. - All rights reserved.
 */
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelCDEDumper extends AbstractCDEDumper {
	private static int MAX_FLUSH_NUMBER = 10000;

	private static final Log log = LogFactory.getLog(ExcelCDEDumper.class);

	public void doDump() {
		Connection cn = null;
		Statement st = null;
		ResultSet rs = null;
		FileOutputStream fileOut = null;
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet();
			int rowNumber = 0;

			HSSFCellStyle boldCellStyle = wb.createCellStyle();
			HSSFFont font = wb.createFont();
			font.setBoldweight((short)700);
			boldCellStyle.setFont(font);
			boldCellStyle.setAlignment((short)0);

			HSSFRow row = sheet.createRow(rowNumber++);
			short col = 0;
			List colInfo = initColumnInfo("deSearch");

			for (int i = 0; i < colInfo.size(); ++i) {
				ColumnInfo currCol = (ColumnInfo) colInfo.get(i);
				int nestedI;
				if (currCol.type.indexOf("Array") >= 0) {
					for (nestedI = 0; nestedI < currCol.nestedColumns.size();) {
						ColumnInfo nestedCol = (ColumnInfo) currCol.nestedColumns.get(nestedI);

						col = (short) (col + 1);
						HSSFCell cell = row.createCell(col);
						cell.setCellValue(currCol.displayName + nestedCol.displayName);
						cell.setCellStyle(boldCellStyle);

						++nestedI;
					}

				} else {
					col = (short) (col + 1);
					HSSFCell cell = row.createCell(col);

					cell.setCellValue(currCol.displayName);
					cell.setCellStyle(boldCellStyle);
				}
			}

			String sqlStmt = "SELECT * FROM DE_EXCEL_GENERATOR_VIEW where DE_WK_FLOW_STATUS not like ('%RETIRED%') and upper(DE_CONTE_NAME) not in ('TEST', 'TRAINING')";

			cn = createConnection();

			st = cn.createStatement();
			rs = st.executeQuery(sqlStmt);
			generateDataRow(rowNumber, wb, colInfo, rs);
		} catch (SQLException e) {
			System.out.println("Database error ");
			e.printStackTrace();
		} catch (Exception ex) {
			System.out.println("Exception caught in Generate Excel File");
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (cn != null) {
					cn.close();
				}
				if (fileOut != null)
					fileOut.close();
			} catch (Exception e) {
				System.out.println("Unable to perform clean up due to the following error ");
				e.printStackTrace();
			}
		}
	}

	private void generateDataRow(int rowNumber, HSSFWorkbook wb, List colInfo, ResultSet rs) {
		try {
			int maxRowNumber = 0;
			int rowCnt = 0;
			int wbNo = 1;
			HSSFSheet sheet = wb.getSheetAt(0);

			while (rs.next()) {
				++rowCnt;
				HSSFRow row = sheet.createRow(rowNumber);
				short col = 0;

				for (int i = 0; i < colInfo.size(); ++i) {
					ColumnInfo currCol = (ColumnInfo) colInfo.get(i);

					if (currCol.type.indexOf("Array") >= 0) {
						Array array = null;

						if (currCol.type.equalsIgnoreCase("Array")) {
							array = rs.getArray(currCol.rsColumnName);
						} else if (currCol.type.equalsIgnoreCase("StructArray")) {
							Struct struct = (Struct) rs.getObject(currCol.rsColumnName);

							Object[] valueStruct = struct.getAttributes();
							array = (Array) valueStruct[currCol.rsIndex];
						}

						if (array != null) {
							ResultSet nestedRs = array.getResultSet();

							int nestedRowNumber = 0;

							while (nestedRs.next()) {
								row = sheet.getRow(rowNumber + nestedRowNumber);

								if (row == null) {
									row = sheet.createRow(rowNumber + nestedRowNumber);

									maxRowNumber = rowNumber + nestedRowNumber;
								}

								Struct valueStruct = (Struct) nestedRs.getObject(2);
								Object[] valueDatum = valueStruct.getAttributes();

								for (short nestedI = 0; nestedI < currCol.nestedColumns.size();) {
									ColumnInfo nestedCol = (ColumnInfo) currCol.nestedColumns.get(nestedI);

									HSSFCell cell = row.createCell((short) (col + nestedI));

									if (nestedCol.rsSubIndex < 0) {
										if (valueDatum[nestedCol.rsIndex] != null) {
											if (nestedCol.type.equalsIgnoreCase("Number")) {
												cell.setCellValue(
														((Number) valueDatum[nestedCol.rsIndex]).floatValue());
											} else if (nestedCol.type.equalsIgnoreCase("Date")) {
												cell.setCellValue(
														((java.util.Date) valueDatum[nestedCol.rsIndex]).toString());
											} else {
												cell.setCellValue((String) valueDatum[nestedCol.rsIndex]);
											}
										}
									} else {
										Struct nestedStruct = (Struct) valueDatum[nestedCol.rsIndex];

										Object[] nestedDatum = nestedStruct.getAttributes();

										if (nestedCol.type.equalsIgnoreCase("Number")) {
											cell.setCellValue(
													((Number) nestedDatum[nestedCol.rsSubIndex]).doubleValue());
										} else if (nestedCol.type.equalsIgnoreCase("String"))
											cell.setCellValue(nestedDatum[nestedCol.rsSubIndex].toString());
									}
									nestedI = (short) (nestedI + 1);
								}

								++nestedRowNumber;
							}
						}

						col = (short) (col + currCol.nestedColumns.size());
					} else if (currCol.type.equalsIgnoreCase("Struct")) {
						Struct struct = (Struct) rs.getObject(currCol.rsColumnName);

						Object[] valueStruct = struct.getAttributes();
						col = (short) (col + 1);
						HSSFCell cell = row.createCell(col);
						cell.setCellValue((String) valueStruct[currCol.rsIndex]);
					} else {
						row = sheet.getRow(rowNumber);
						col = (short) (col + 1);
						HSSFCell cell = row.createCell(col);

						String columnName = ((ColumnInfo) colInfo.get(i)).rsColumnName;
						if (currCol.type.equalsIgnoreCase("Date"))
							cell.setCellValue(
									(rs.getDate(columnName) != null) ? rs.getDate(columnName).toString() : "");
						else {
							cell.setCellValue(rs.getString(columnName));
						}
					}
				}
				if (maxRowNumber > rowNumber)
					rowNumber = maxRowNumber + 2;
				else {
					rowNumber += 2;
				}
				if (rowNumber > 60000) {
					doFlush(wb, getFileNamePrefix() + "_" + wbNo + ".xls");

					wb = new HSSFWorkbook();
					sheet = wb.createSheet();
					rowNumber = 0;
					++wbNo;

					Thread.yield();
				}
			}

			doFlush(wb, getFileNamePrefix() + "_" + wbNo + ".xls");
		} catch (Exception e) {
			log.error("unable to generate excel data ", e);
		}
	}

	private void doFlush(HSSFWorkbook wb, String fileName) {
		OutputStream fileOut = null;
		try {
			log.debug("Flushing to file [" + fileName + "]...");
			fileOut = new FileOutputStream(fileName, true);
			wb.write(fileOut);
		} catch (Exception e) {
			log.error("Error while writing to file [" + fileName + "]", e);
		} finally {
			if (fileOut != null)
				try {
					fileOut.flush();
					fileOut.close();
				} catch (Exception e) {
				}
		}
	}

	private List initColumnInfo(String source) {
		List columnInfo = new ArrayList();

		columnInfo.add(new ColumnInfo("PREFERRED_NAME", "Data Element Short Name", "String"));

		columnInfo.add(new ColumnInfo("LONG_NAME", "Data Element Long Name", "String"));

		columnInfo.add(new ColumnInfo("DOC_TEXT", "Data Element Preferred Question Text", "String"));

		columnInfo.add(new ColumnInfo("PREFERRED_DEFINITION", "Data Element Preferred Definition", "String"));

		columnInfo.add(new ColumnInfo("VERSION", "Data Element Version", "String"));
		columnInfo.add(new ColumnInfo("DE_CONTE_NAME", "Data Element Context Name", "String"));

		columnInfo.add(new ColumnInfo("DE_CONTE_VERSION", "Data Element Context Version", "Number"));

		columnInfo.add(new ColumnInfo("CDE_ID", "Data Element Public ID", "Number"));

		if (("deSearch".equals(source)) || ("cdeCart".equals(source))) {
			columnInfo.add(new ColumnInfo("DE_WK_FLOW_STATUS", "Data Element Workflow Status", "String"));

			columnInfo.add(new ColumnInfo("REGISTRATION_STATUS", "Data Element Registration Status", "Number"));

			columnInfo.add(new ColumnInfo("BEGIN_DATE", "Data Element Begin Date", "Date"));
			columnInfo.add(new ColumnInfo("ORIGIN", "Data Element Source", "String"));
		} else {
			columnInfo.add(new ColumnInfo("DE_WK_FLOW_STATUS", "Workflow Status", "String"));

			columnInfo.add(new ColumnInfo("REGISTRATION_STATUS", "Registration Status", "Number"));

			columnInfo.add(new ColumnInfo("BEGIN_DATE", "Begin Date", "Date"));
			columnInfo.add(new ColumnInfo("ORIGIN", "Source", "String"));
		}

		columnInfo.add(new ColumnInfo("DEC_ID", "Data Element Concept Public ID", "Number"));

		columnInfo.add(new ColumnInfo("DEC_PREFERRED_NAME", "Data Element Concept Short Name", "String"));

		columnInfo.add(new ColumnInfo("DEC_LONG_NAME", "Data Element Concept Long Name", "String"));

		columnInfo.add(new ColumnInfo("DEC_VERSION", "Data Element Concept Version", "Number"));

		columnInfo.add(new ColumnInfo("DEC_CONTE_NAME", "Data Element Concept Context Name", "String"));

		columnInfo.add(new ColumnInfo("DEC_CONTE_VERSION", "Data Element Concept Context Version", "Number"));

		columnInfo.add(new ColumnInfo("OC_ID", "Object Class Public ID", "String"));
		columnInfo.add(new ColumnInfo("OC_LONG_NAME", "Object Class Long Name", "String"));

		columnInfo.add(new ColumnInfo("OC_PREFERRED_NAME", "Object Class Short Name", "String"));

		columnInfo.add(new ColumnInfo("OC_CONTE_NAME", "Object Class Context Name", "String"));

		columnInfo.add(new ColumnInfo("OC_VERSION", "Object Class Version", "String"));

		List ocConceptInfo = new ArrayList();
		ocConceptInfo.add(new ColumnInfo(1, "Name"));
		ocConceptInfo.add(new ColumnInfo(0, "Code"));
		ocConceptInfo.add(new ColumnInfo(2, "Public ID", "Number"));
		ocConceptInfo.add(new ColumnInfo(3, "Definition Source"));
		ocConceptInfo.add(new ColumnInfo(5, "EVS Source"));
		ocConceptInfo.add(new ColumnInfo(6, "Primary Flag"));

		ColumnInfo ocConcepts = new ColumnInfo("oc_concepts", "Object Class Concept ", "Array");

		ocConcepts.nestedColumns = ocConceptInfo;
		columnInfo.add(ocConcepts);

		columnInfo.add(new ColumnInfo("PROP_ID", "Property Public ID", "String"));
		columnInfo.add(new ColumnInfo("PROP_LONG_NAME", "Property Long Name", "String"));

		columnInfo.add(new ColumnInfo("PROP_PREFERRED_NAME", "Property Short Name", "String"));

		columnInfo.add(new ColumnInfo("PROP_CONTE_NAME", "Property Context Name", "String"));

		columnInfo.add(new ColumnInfo("PROP_VERSION", "Property Version", "String"));

		List propConceptInfo = new ArrayList();
		propConceptInfo.add(new ColumnInfo(1, "Name"));
		propConceptInfo.add(new ColumnInfo(0, "Code"));
		propConceptInfo.add(new ColumnInfo(2, "Public ID", "Number"));
		propConceptInfo.add(new ColumnInfo(3, "Definition Source"));
		propConceptInfo.add(new ColumnInfo(5, "EVS Source"));
		propConceptInfo.add(new ColumnInfo(6, "Primary Flag"));

		ColumnInfo propConcepts = new ColumnInfo("prop_concepts", "Property Concept ", "Array");

		propConcepts.nestedColumns = propConceptInfo;
		columnInfo.add(propConcepts);

		columnInfo.add(new ColumnInfo("VD_ID", "Value Domain Public ID", "Number"));
		columnInfo.add(new ColumnInfo("VD_PREFERRED_NAME", "Value Domain Short Name", "String"));

		columnInfo.add(new ColumnInfo("VD_LONG_NAME", "Value Domain Long Name", "String"));

		columnInfo.add(new ColumnInfo("VD_VERSION", "Value Domain Version", "Number"));

		columnInfo.add(new ColumnInfo("VD_CONTE_NAME", "Value Domain Context Name", "String"));

		columnInfo.add(new ColumnInfo("VD_CONTE_VERSION", "Value Domain Context Version", "Number"));

		columnInfo.add(new ColumnInfo("VD_TYPE", "Value Domain Type", "String"));
		columnInfo.add(new ColumnInfo("DTL_NAME", "Value Domain Datatype", "String"));

		columnInfo.add(new ColumnInfo("MIN_LENGTH_NUM", "Value Domain Min Length", "Number"));

		columnInfo.add(new ColumnInfo("MAX_LENGTH_NUM", "Value Domain Max Length", "Number"));

		columnInfo.add(new ColumnInfo("LOW_VALUE_NUM", "Value Domain Min Value", "Number"));

		columnInfo.add(new ColumnInfo("HIGH_VALUE_NUM", "Value Domain Max Value", "Number"));

		columnInfo.add(new ColumnInfo("DECIMAL_PLACE", "Value Domain Decimal Place", "Number"));

		columnInfo.add(new ColumnInfo("FORML_NAME", "Value Domain Format", "String"));

		List vdConceptInfo = new ArrayList();
		vdConceptInfo.add(new ColumnInfo(1, "Name"));
		vdConceptInfo.add(new ColumnInfo(0, "Code"));
		vdConceptInfo.add(new ColumnInfo(2, "Public ID", "Number"));
		vdConceptInfo.add(new ColumnInfo(3, "Definition Source"));
		vdConceptInfo.add(new ColumnInfo(5, "EVS Source"));
		vdConceptInfo.add(new ColumnInfo(6, "Primary Flag"));

		ColumnInfo vdConcepts = new ColumnInfo("vd_concepts", "Value Domain Concept ", "Array");

		vdConcepts.nestedColumns = vdConceptInfo;
		columnInfo.add(vdConcepts);

		if (("deSearch".equals(source)) || ("cdeCart".equals(source))) {
			columnInfo.add(new ColumnInfo("REP_ID", "Representation Public ID", "String"));
			columnInfo.add(new ColumnInfo("REP_LONG_NAME", "Representation Long Name", "String"));

			columnInfo.add(new ColumnInfo("REP_PREFERRED_NAME", "Representation Short Name", "String"));

			columnInfo.add(new ColumnInfo("REP_CONTE_NAME", "Representation Context Name", "String"));

			columnInfo.add(new ColumnInfo("REP_VERSION", "Representation Version", "String"));

			List repConceptInfo = new ArrayList();
			repConceptInfo.add(new ColumnInfo(1, "Name"));
			repConceptInfo.add(new ColumnInfo(0, "Code"));
			repConceptInfo.add(new ColumnInfo(2, "Public ID", "Number"));
			repConceptInfo.add(new ColumnInfo(3, "Definition Source"));
			repConceptInfo.add(new ColumnInfo(5, "EVS Source"));
			repConceptInfo.add(new ColumnInfo(6, "Primary Flag"));

			ColumnInfo repConcepts = new ColumnInfo("rep_concepts", "Representation Concept ", "Array");

			repConcepts.nestedColumns = repConceptInfo;
			columnInfo.add(repConcepts);
		}

		List validValueInfo = new ArrayList();
		validValueInfo.add(new ColumnInfo(0, "Valid Values"));

		if (("deSearch".equals(source)) || ("cdeCart".equals(source))) {
			validValueInfo.add(new ColumnInfo(1, "Value Meaning Name"));
			validValueInfo.add(new ColumnInfo(2, "Value Meaning Description"));
			validValueInfo.add(new ColumnInfo(3, "Value Meaning Concepts"));

			validValueInfo.add(new ColumnInfo(4, "PVBEGINDATE", "PV Begin Date", "Date"));
			validValueInfo.add(new ColumnInfo(5, "PVENDDATE", "PV End Date", "Date"));
			validValueInfo.add(new ColumnInfo(6, "VMPUBLICID", "Value Meaning PublicID", "Number"));
			validValueInfo.add(new ColumnInfo(7, "VMVERSION", "Value Meaning Version", "Number"));
		} else {
			validValueInfo.add(new ColumnInfo(1, "Value Meaning"));
		}
		ColumnInfo validValue = new ColumnInfo("VALID_VALUES", "", "Array");
		validValue.nestedColumns = validValueInfo;
		columnInfo.add(validValue);

		List csInfo = new ArrayList();

		csInfo.add(new ColumnInfo(0, 3, "Short Name", "String"));

		csInfo.add(new ColumnInfo(0, 4, "Version", "Number"));
		csInfo.add(new ColumnInfo(0, 1, "Context Name", "String"));
		csInfo.add(new ColumnInfo(0, 2, "Context Version", "Number"));
		csInfo.add(new ColumnInfo(1, "Item Name"));
		csInfo.add(new ColumnInfo(2, "Item Type Name"));

		csInfo.add(new ColumnInfo(3, "CsiPublicId", "Item Public Id", "Number"));
		csInfo.add(new ColumnInfo(4, "CsiVersion", "Item Version", "Number"));

		ColumnInfo classification = new ColumnInfo("CLASSIFICATIONS", "Classification Scheme ", "Array");

		classification.nestedColumns = csInfo;
		columnInfo.add(classification);

		List altNameInfo = new ArrayList();
		altNameInfo.add(new ColumnInfo(0, "Context Name"));
		altNameInfo.add(new ColumnInfo(1, "Context Version", "Number"));
		altNameInfo.add(new ColumnInfo(2, ""));
		altNameInfo.add(new ColumnInfo(3, "Type"));
		ColumnInfo altNames;
		if (("deSearch".equals(source)) || ("cdeCart".equals(source)))
			altNames = new ColumnInfo("designations", "Data Element Alternate Name ", "Array");
		else {
			altNames = new ColumnInfo("designations", "Alternate Name ", "Array");
		}
		altNames.nestedColumns = altNameInfo;
		columnInfo.add(altNames);

		List refDocInfo = new ArrayList();
		refDocInfo.add(new ColumnInfo(3, ""));
		refDocInfo.add(new ColumnInfo(0, "Name"));
		refDocInfo.add(new ColumnInfo(2, "Type"));

		ColumnInfo refDoc = new ColumnInfo("reference_docs", "Document ", "Array");
		refDoc.nestedColumns = refDocInfo;
		columnInfo.add(refDoc);

		columnInfo.add(new ColumnInfo(0, "DE_DERIVATION", "Derivation Type", "Struct"));

		columnInfo.add(new ColumnInfo(2, "DE_DERIVATION", "Derivation Method", "Struct"));

		columnInfo.add(new ColumnInfo(3, "DE_DERIVATION", "Derivation Rule", "Struct"));

		columnInfo.add(new ColumnInfo(4, "DE_DERIVATION", "Concatenation Character", "Struct"));

		List dedInfo = new ArrayList();
		dedInfo.add(new ColumnInfo(0, "Public ID", "Number"));
		dedInfo.add(new ColumnInfo(1, "Long Name"));
		dedInfo.add(new ColumnInfo(4, "Version", "Number"));
		dedInfo.add(new ColumnInfo(5, "Workflow Status"));
		dedInfo.add(new ColumnInfo(6, "Context"));
		dedInfo.add(new ColumnInfo(7, "Display Order", "Number"));

		ColumnInfo deDrivation = new ColumnInfo(5, "DE_DERIVATION", "DDE ", "StructArray");

		deDrivation.nestedColumns = dedInfo;
		columnInfo.add(deDrivation);

		return columnInfo;
	}

	private class ColumnInfo {
		String rsColumnName;
		int rsIndex;
		int rsSubIndex = -1;
		String displayName;
		String type;
		List nestedColumns;

		ColumnInfo(String paramString1, String paramString2, String paramString3) {
			this.rsColumnName = paramString1;
			this.displayName = paramString2;
			this.type = paramString3;
		}

		ColumnInfo(int paramInt, String paramString1, String paramString2, String paramString3) {
			this.rsIndex = paramInt;
			this.rsColumnName = paramString1;
			this.displayName = paramString2;
			this.type = paramString3;
		}

		ColumnInfo(int paramInt, String paramString) {
			this.rsIndex = paramInt;
			this.displayName = paramString;
			this.type = "String";
		}

		ColumnInfo(int paramInt, String paramString1, String paramString2) {
			this.rsIndex = paramInt;
			this.displayName = paramString1;
			this.type = paramString2;
		}

		ColumnInfo(int paramInt1, int paramInt2, String paramString1, String paramString2) {
			this.rsIndex = paramInt1;
			this.rsSubIndex = paramInt2;
			this.displayName = paramString1;
			this.type = paramString2;
		}
	}
}