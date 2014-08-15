/*L
 * Copyright SAIC-F Inc.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cde-browser/LICENSE.txt for details.
 *
 * Portions of this source file not modified since 2008 are covered by:
 *
 * Copyright 2000-2008 Oracle, Inc.
 *
 * Distributed under the caBIG Software License.  For details see
 * http://ncip.github.com/cadsr-cde-browser/LICENSE-caBIG.txt
 */

package gov.nih.nci.ncicb.cadsr.common.downloads.impl;

import gov.nih.nci.ncicb.cadsr.cdebrowser.process.GetExcelDownload.ColumnInfo;
import gov.nih.nci.ncicb.cadsr.common.downloads.GetExcelDownload;
import gov.nih.nci.ncicb.cadsr.common.util.CDEBrowserParams;
import gov.nih.nci.ncicb.cadsr.common.util.ConnectionHelper;
import gov.nih.nci.ncicb.cadsr.common.util.DBUtil;
import gov.nih.nci.ncicb.cadsr.common.util.logging.Log;
import gov.nih.nci.ncicb.cadsr.common.util.logging.LogFactory;
import gov.nih.nci.ncicb.cadsr.objectCart.CDECart;
import gov.nih.nci.ncicb.cadsr.objectCart.CDECartItem;

import java.io.FileOutputStream;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class GetExcelDownloadImpl implements GetExcelDownload {
	private static Log log = LogFactory.getLog(GetExcelDownloadImpl.class.getName());
	private String jndiName = null;
	private String source;
	private String where;
	private String fileName = "";
	
	public GetExcelDownloadImpl() {
		super();
		
	}

	public void generateExcelForCDECart(CDECart cart, String src, String _jndiName) throws Exception 
	{
		if (_jndiName == null) {
			throw new Exception("JNDI name cannot be null");
		}
		
		Collection items = cart.getDataElements();
		CDECartItem item = null;
		boolean firstOne = true;
		StringBuffer whereBuffer = new StringBuffer("");
		Iterator itemsIt = items.iterator();

		while (itemsIt.hasNext()) {
			item = (CDECartItem) itemsIt.next();

			if (firstOne) {
				whereBuffer.append("'" + item.getId() + "'");

				firstOne = false;
			}
			else
			{
				whereBuffer.append(",'" + item.getId() + "'");
			}
		}

		where = whereBuffer.toString();
		source = src;
		jndiName = _jndiName;		
		generateExcelFile();
	}
	
	public void generateExcelForDESearch(String sWhere, String src, String _jndiName) throws Exception 
	{
		if (_jndiName == null) {
			throw new Exception("JNDI name cannot be null");
		}
		
		where = sWhere;
		source = src;
		jndiName = _jndiName;			
		generateExcelFile();
	}
	
	@SuppressWarnings("static-access")
	public String getFileName()
	{
		Connection cn = null;
		
		try {
			if (fileName.equals(""))
			{
				ConnectionHelper connHelper = new ConnectionHelper(jndiName);
				cn = connHelper.getConnection();
			      
				if (cn == null) {
					throw new Exception("Cannot get the connection for the JNDI name ["+jndiName+"]");
				}
				
				DBUtil dbutil = new DBUtil();
				String excelFileSuffix = dbutil.getUniqueId(cn, "SBREXT.XML_FILE_SEQ.NEXTVAL");
				String downLoadDir = CDEBrowserParams.getInstance().getXMLDownloadDir();
				fileName =	downLoadDir + "DataElements" + "_" + excelFileSuffix + ".xls";
			}
		} catch (Exception e) {
			log.error("Error trying to get the download file name", e);
		} finally {
			try {if (cn != null) cn.close();} catch (Exception e) {}
		}
		return fileName;
	}
	
	public void setFileName(String sfile)
	{
		fileName = sfile;
	}
	
	//generate the workbook and format it and crate the header
	private void generateExcelFile() throws Exception
	{
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
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			boldCellStyle.setFont(font);
			boldCellStyle.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
	
			// Create a row and put the column header in it
			HSSFRow row = sheet.createRow(rowNumber++);
			short col = 0;
			List colInfo = this.initColumnInfo(source);
	
			for (int i = 0; i < colInfo.size(); i++) {
				ColumnInfo currCol = (ColumnInfo) colInfo.get(i);
	
				if (currCol.type.indexOf("Array") >= 0) {
					for (int nestedI = 0; nestedI < currCol.nestedColumns.size();
					nestedI++) {
						ColumnInfo nestedCol =
							(ColumnInfo) currCol.nestedColumns.get(nestedI);
	
						HSSFCell cell = row.createCell(col++);
						cell.setCellValue(currCol.displayName + nestedCol.displayName);
						cell.setCellStyle(boldCellStyle);
					}
				}
				else {
					HSSFCell cell = row.createCell(col++);
	
					cell.setCellValue(currCol.displayName);
					cell.setCellStyle(boldCellStyle);
				}
			}
			
			String sqlStmt =
				"SELECT * FROM DE_CDE_EXCEL_GENERATOR_VIEW " + "WHERE DE_IDSEQ IN " +
				" ( " + where + " )  ";
			
			ConnectionHelper connHelper = new ConnectionHelper(jndiName);
			cn = connHelper.getConnection();
		      
			if (cn == null) {
				throw new Exception("Cannot get the connection for the JNDI name ["+jndiName+"]");
			}
		      
			st = cn.createStatement();
			rs = st.executeQuery(sqlStmt);
			generateDataRow(rowNumber, sheet, colInfo, rs);
			String filename = getFileName();
			fileOut = new FileOutputStream(filename);
			wb.write(fileOut);
			
		} catch (SQLException e) {
			log.warn("Database error ", e);
		} catch (Exception ex) {
			log.error("Exception caught in Generate Excel File", ex);			
			throw ex;
		}
		finally {
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
				if (fileOut != null) {
					fileOut.close();
				}
			}
			catch (Exception e) {
				log.debug("Unable to perform clean up due to the following error ", e);
			}
		}

	}
	
	//create teh rows on the sheet from the result set.
	private void generateDataRow(int rowNumber, HSSFSheet sheet, List colInfo, ResultSet rs)
	{
		try {
			int maxRowNumber = 0;
			HSSFRow row; 
			while (rs.next()) {
				row = sheet.createRow(rowNumber);
				short col = 0;

				for (int i = 0; i < colInfo.size(); i++) {
					ColumnInfo currCol = (ColumnInfo) colInfo.get(i);

					if (currCol.type.indexOf("Array") >= 0) {
						Array array = null;

						if (currCol.type.equalsIgnoreCase("Array")) {
							array = rs.getArray(currCol.rsColumnName);
						}
						else if (currCol.type.equalsIgnoreCase("StructArray")) {
							Struct struct =
								(Struct)rs.getObject(currCol.rsColumnName);
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
								for (
										short nestedI = 0; nestedI < currCol.nestedColumns.size();
										nestedI++) {
									ColumnInfo nestedCol =
										(ColumnInfo) currCol.nestedColumns.get(nestedI);

									HSSFCell cell = row.createCell((short) (col + nestedI));

									if (nestedCol.rsSubIndex < 0) {
										if (valueDatum[nestedCol.rsIndex] != null) {
											if (nestedCol.type.equalsIgnoreCase("Number")) {
												cell.setCellValue(
														((Number) valueDatum[nestedCol.rsIndex]).floatValue());
											}else if (nestedCol.type.equalsIgnoreCase("Date")){  
												cell.setCellValue(
														((Date) valueDatum[nestedCol.rsIndex]).toString());                    	  
											}else {  
												cell.setCellValue(
														(String)valueDatum[nestedCol.rsIndex]);
											}
										}
									}
									else {
										Struct nestedStruct =
											(Struct) valueDatum[nestedCol.rsIndex];

										Object[] nestedDatum = nestedStruct.getAttributes();

										if (nestedCol.type.equalsIgnoreCase("Number")) {
											//changed the conversion from stringValue from floatValue 07/11/2007 to fix GF7664 Prerna
											cell.setCellValue(
													((Number) nestedDatum[nestedCol.rsSubIndex]).doubleValue());
										}
										else if (nestedCol.type.equalsIgnoreCase("String")) {
											cell.setCellValue(
													(nestedDatum[nestedCol.rsSubIndex]).toString());
										}
									}
								}

								nestedRowNumber++;
							}
						}

						col += currCol.nestedColumns.size();
					}
					else if (currCol.type.equalsIgnoreCase("Struct")) {
						Struct struct =
							(Struct)rs.getObject(currCol.rsColumnName);

						Object[] valueStruct = struct.getAttributes();
						HSSFCell cell = row.createCell(col++);
						cell.setCellValue((String) valueStruct[currCol.rsIndex]);
					}
					else {
						row = sheet.getRow(rowNumber);
						HSSFCell cell = row.createCell(col++);
						// Changed the way date is displayed in Excel in 4.0
						String columnName = ((ColumnInfo) colInfo.get(i)).rsColumnName;											
						if(currCol.type.equalsIgnoreCase("Date")){
							cell.setCellValue((rs.getDate(columnName) != null)?(rs.getDate(columnName)).toString():"");
						}else{						
							cell.setCellValue(rs.getString(columnName));
						}
					}
				}
				if (maxRowNumber > rowNumber)
					rowNumber = maxRowNumber + 2;
				else 
					rowNumber += 2;
			}
		} catch (Exception e) {
			log.error("unable to generate excel data ", e);
		}		
	}

	//initiate the column information as per the source
	private List initColumnInfo(String source) {
		List<ColumnInfo> columnInfo = new ArrayList<ColumnInfo>();

		columnInfo.add(
				new ColumnInfo("PREFERRED_NAME", "Data Element Short Name", "String"));
		columnInfo.add(
				new ColumnInfo("LONG_NAME", "Data Element Long Name", "String"));
		columnInfo.add(
				new ColumnInfo("DOC_TEXT", "Data Element Preferred Question Text", "String"));
		columnInfo.add(
				new ColumnInfo(
						"PREFERRED_DEFINITION", "Data Element Preferred Definition", "String"));
		columnInfo.add(new ColumnInfo("VERSION", "Data Element Version", "String"));
		columnInfo.add(
				new ColumnInfo("DE_CONTE_NAME", "Data Element Context Name", "String"));
		columnInfo.add(
				new ColumnInfo(
						"DE_CONTE_VERSION", "Data Element Context Version", "Number"));
		columnInfo.add(
				new ColumnInfo("CDE_ID", "Data Element Public ID", "Number"));    
		////The deSearch condition is added for the new version of excel files
		if ("deSearch".equals(source) || "cdeCart".equals(source)){ 
			columnInfo.add(
					new ColumnInfo("DE_WK_FLOW_STATUS", "Data Element Workflow Status", "String"));
			columnInfo.add(
					new ColumnInfo("REGISTRATION_STATUS", "Data Element Registration Status", "Number"));
			columnInfo.add(new ColumnInfo("BEGIN_DATE", "Data Element Begin Date", "Date"));
			columnInfo.add(new ColumnInfo("ORIGIN", "Data Element Source", "String"));
		}else {
			columnInfo.add(
					new ColumnInfo("DE_WK_FLOW_STATUS", "Workflow Status", "String"));
			columnInfo.add(
					new ColumnInfo("REGISTRATION_STATUS", "Registration Status", "Number"));
			columnInfo.add(new ColumnInfo("BEGIN_DATE", "Begin Date", "Date"));
			columnInfo.add(new ColumnInfo("ORIGIN", "Source", "String"));
		}

		//data element concept
		columnInfo.add(
				new ColumnInfo("DEC_ID", "Data Element Concept Public ID", "Number"));
		columnInfo.add(
				new ColumnInfo(
						"DEC_PREFERRED_NAME", "Data Element Concept Short Name", "String"));
		columnInfo.add(
				new ColumnInfo(
						"DEC_LONG_NAME", "Data Element Concept Long Name", "String"));
		columnInfo.add(
				new ColumnInfo("DEC_VERSION", "Data Element Concept Version", "Number"));
		columnInfo.add(
				new ColumnInfo(
						"DEC_CONTE_NAME", "Data Element Concept Context Name", "String"));
		columnInfo.add(
				new ColumnInfo(
						"DEC_CONTE_VERSION", "Data Element Concept Context Version", "Number"));

		//object class concept
		columnInfo.add(new ColumnInfo("OC_ID", "Object Class Public ID", "String"));
		columnInfo.add(
				new ColumnInfo("OC_LONG_NAME", "Object Class Long Name", "String"));
		columnInfo.add(
				new ColumnInfo(
						"OC_PREFERRED_NAME", "Object Class Short Name", "String"));
		columnInfo.add(
				new ColumnInfo("OC_CONTE_NAME", "Object Class Context Name", "String"));
		columnInfo.add(
				new ColumnInfo("OC_VERSION", "Object Class Version", "String"));

		List<ColumnInfo> ocConceptInfo = new ArrayList<ColumnInfo>();
		ocConceptInfo.add(new ColumnInfo(1, "Name"));
		ocConceptInfo.add(new ColumnInfo(0, "Code"));
		ocConceptInfo.add(new ColumnInfo(2, "Public ID", "Number"));
		ocConceptInfo.add(new ColumnInfo(3, "Definition Source"));    
		ocConceptInfo.add(new ColumnInfo(5, "EVS Source"));
		ocConceptInfo.add(new ColumnInfo(6, "Primary Flag"));

		ColumnInfo ocConcepts =
			new ColumnInfo("oc_concepts", "Object Class Concept ", "Array");
		ocConcepts.nestedColumns = ocConceptInfo;
		columnInfo.add(ocConcepts);

		//property concept
		columnInfo.add(new ColumnInfo("PROP_ID", "Property Public ID", "String"));
		columnInfo.add(
				new ColumnInfo("PROP_LONG_NAME", "Property Long Name", "String"));
		columnInfo.add(
				new ColumnInfo(
						"PROP_PREFERRED_NAME", "Property Short Name", "String"));
		columnInfo.add(
				new ColumnInfo("PROP_CONTE_NAME", "Property Context Name", "String"));
		columnInfo.add(
				new ColumnInfo("PROP_VERSION", "Property Version", "String"));

		List<ColumnInfo> propConceptInfo = new ArrayList<ColumnInfo>();
		propConceptInfo.add(new ColumnInfo(1, "Name"));
		propConceptInfo.add(new ColumnInfo(0, "Code"));
		propConceptInfo.add(new ColumnInfo(2, "Public ID", "Number"));
		propConceptInfo.add(new ColumnInfo(3, "Definition Source"));
		propConceptInfo.add(new ColumnInfo(5, "EVS Source"));
		propConceptInfo.add(new ColumnInfo(6, "Primary Flag"));

		ColumnInfo propConcepts =
			new ColumnInfo("prop_concepts", "Property Concept ", "Array");
		propConcepts.nestedColumns = propConceptInfo;
		columnInfo.add(propConcepts);

		//value domain
		columnInfo.add(new ColumnInfo("VD_ID", "Value Domain Public ID", "Number"));
		columnInfo.add(
				new ColumnInfo(
						"VD_PREFERRED_NAME", "Value Domain Short Name", "String"));
		columnInfo.add(
				new ColumnInfo("VD_LONG_NAME", "Value Domain Long Name", "String"));
		columnInfo.add(
				new ColumnInfo("VD_VERSION", "Value Domain Version", "Number"));
		columnInfo.add(
				new ColumnInfo("VD_CONTE_NAME", "Value Domain Context Name", "String"));
		columnInfo.add(
				new ColumnInfo(
						"VD_CONTE_VERSION", "Value Domain Context Version", "Number"));
		columnInfo.add(new ColumnInfo("VD_TYPE", "Value Domain Type", "String"));
		columnInfo.add(
				new ColumnInfo("DTL_NAME", "Value Domain Datatype", "String"));
		columnInfo.add(
				new ColumnInfo("MIN_LENGTH_NUM", "Value Domain Min Length", "Number"));
		columnInfo.add(
				new ColumnInfo("MAX_LENGTH_NUM", "Value Domain Max Length", "Number"));
		columnInfo.add(
				new ColumnInfo("LOW_VALUE_NUM", "Value Domain Min Value", "Number"));
		columnInfo.add(
				new ColumnInfo("HIGH_VALUE_NUM", "Value Domain Max Value", "Number"));
		columnInfo.add(
				new ColumnInfo("DECIMAL_PLACE", "Value Domain Decimal Place", "Number"));
		columnInfo.add(
				new ColumnInfo("FORML_NAME", "Value Domain Format", "String"));

		//Value Domain Concept
		List<ColumnInfo> vdConceptInfo = new ArrayList<ColumnInfo>();
		vdConceptInfo.add(new ColumnInfo(1, "Name"));
		vdConceptInfo.add(new ColumnInfo(0, "Code"));
		vdConceptInfo.add(new ColumnInfo(2, "Public ID", "Number"));
		vdConceptInfo.add(new ColumnInfo(3, "Definition Source"));
		vdConceptInfo.add(new ColumnInfo(5, "EVS Source"));
		vdConceptInfo.add(new ColumnInfo(6, "Primary Flag"));

		ColumnInfo vdConcepts =
			new ColumnInfo("vd_concepts", "Value Domain Concept ", "Array");
		vdConcepts.nestedColumns = vdConceptInfo;
		columnInfo.add(vdConcepts);    
		//representation concept
		//The deSearch condition is added to support both the old and the new version of excel files
		if ("deSearch".equals(source)|| "cdeCart".equals(source)){    	
			columnInfo.add(new ColumnInfo("REP_ID", "Representation Public ID", "String"));
			columnInfo.add(
					new ColumnInfo("REP_LONG_NAME", "Representation Long Name", "String"));
			columnInfo.add(
					new ColumnInfo(
							"REP_PREFERRED_NAME", "Representation Short Name", "String"));
			columnInfo.add(
					new ColumnInfo("REP_CONTE_NAME", "Representation Context Name", "String"));
			columnInfo.add(
					new ColumnInfo("REP_VERSION", "Representation Version", "String"));

			List<ColumnInfo> repConceptInfo = new ArrayList<ColumnInfo>();
			repConceptInfo.add(new ColumnInfo(1, "Name"));
			repConceptInfo.add(new ColumnInfo(0, "Code"));
			repConceptInfo.add(new ColumnInfo(2, "Public ID", "Number"));
			repConceptInfo.add(new ColumnInfo(3, "Definition Source"));
			repConceptInfo.add(new ColumnInfo(5, "EVS Source"));
			repConceptInfo.add(new ColumnInfo(6, "Primary Flag"));

			ColumnInfo repConcepts =
				new ColumnInfo("rep_concepts", "Representation Concept ", "Array");
			repConcepts.nestedColumns = repConceptInfo;
			columnInfo.add(repConcepts);
		}    

		//Valid Value
		List<ColumnInfo> validValueInfo = new ArrayList<ColumnInfo>();
		validValueInfo.add(new ColumnInfo(0, "Valid Values"));
		//The deSearch condition is added to support both the (3.2.0.1) old and the (3.2.0.2)new version of excel files
		if ("deSearch".equals(source)|| "cdeCart".equals(source)){
			validValueInfo.add(new ColumnInfo(1, "Value Meaning Name"));
			validValueInfo.add(new ColumnInfo(2, "Value Meaning Description"));
			validValueInfo.add(new ColumnInfo(3, "Value Meaning Concepts"));
			//*	Added for 4.0	
			validValueInfo.add(new ColumnInfo(4, "PVBEGINDATE","PV Begin Date", "Date"));
			validValueInfo.add(new ColumnInfo(5, "PVENDDATE","PV End Date", "Date"));
			validValueInfo.add(new ColumnInfo(6, "VMPUBLICID", "Value Meaning PublicID", "Number"));
			validValueInfo.add(new ColumnInfo(7, "VMVERSION", "Value Meaning Version", "Number"));
			//	Added for 4.0	*/
			//validValueInfo.add(new ColumnInfo(8, "VMALTERNATEDEFINITIONS", "Value Meaning Alternate Definitions", "String"));
		}else {
			validValueInfo.add(new ColumnInfo(1, "Value Meaning"));
		}
		ColumnInfo validValue = new ColumnInfo("VALID_VALUES", "", "Array");
		validValue.nestedColumns = validValueInfo;
		columnInfo.add(validValue);

		//Classification Scheme
		List<ColumnInfo> csInfo = new ArrayList<ColumnInfo>();
		/*if ("deSearch".equals(source)|| "cdeCart".equals(source)){
    	csInfo.add(new ColumnInfo(0, 3, "Preferred Name", "String"));
    }else{*/
		csInfo.add(new ColumnInfo(0, 3, "Short Name", "String"));
		//}
		csInfo.add(new ColumnInfo(0, 4, "Version","Number"));
		csInfo.add(new ColumnInfo(0, 1, "Context Name", "String"));
		csInfo.add(new ColumnInfo(0, 2, "Context Version","Number"));
		csInfo.add(new ColumnInfo(1, "Item Name"));
		csInfo.add(new ColumnInfo(2, "Item Type Name"));
		//	Added for 4.0 
		csInfo.add(new ColumnInfo(3, "CsiPublicId","Item Public Id", "Number"));
		csInfo.add(new ColumnInfo(4, "CsiVersion","Item Version", "Number"));
		//	Added for 4.0	
		ColumnInfo classification =
			new ColumnInfo("CLASSIFICATIONS", "Classification Scheme ", "Array");
		classification.nestedColumns = csInfo;
		columnInfo.add(classification);

		//Alternate name
		List<ColumnInfo> altNameInfo = new ArrayList<ColumnInfo>();
		altNameInfo.add(new ColumnInfo(0, "Context Name"));
		altNameInfo.add(new ColumnInfo(1, "Context Version", "Number"));
		altNameInfo.add(new ColumnInfo(2, ""));
		altNameInfo.add(new ColumnInfo(3, "Type"));
		ColumnInfo altNames;
		if("deSearch".equals(source)|| "cdeCart".equals(source)){
			altNames = new ColumnInfo("designations", "Data Element Alternate Name ", "Array");
		}else {
			altNames = new ColumnInfo("designations", "Alternate Name ", "Array");
		}
		altNames.nestedColumns = altNameInfo;
		columnInfo.add(altNames);

		//Reference Document
		List<ColumnInfo> refDocInfo = new ArrayList<ColumnInfo>();
		refDocInfo.add(new ColumnInfo(3, ""));
		refDocInfo.add(new ColumnInfo(0, "Name"));
		refDocInfo.add(new ColumnInfo(2, "Type"));

		ColumnInfo refDoc = new ColumnInfo("reference_docs", "Document ", "Array");
		refDoc.nestedColumns = refDocInfo;
		columnInfo.add(refDoc);

		//Derived data elements
		columnInfo.add(
				new ColumnInfo(0, "DE_DERIVATION", "Derivation Type", "Struct"));
		columnInfo.add(
				new ColumnInfo(2, "DE_DERIVATION", "Derivation Method", "Struct"));
		columnInfo.add(
				new ColumnInfo(3, "DE_DERIVATION", "Derivation Rule", "Struct"));
		columnInfo.add(
				new ColumnInfo(4, "DE_DERIVATION", "Concatenation Character", "Struct"));

		List<ColumnInfo> dedInfo = new ArrayList<ColumnInfo>();
		dedInfo.add(new ColumnInfo(0, "Public ID", "Number"));
		dedInfo.add(new ColumnInfo(1, "Long Name"));
		dedInfo.add(new ColumnInfo(4, "Version", "Number"));
		dedInfo.add(new ColumnInfo(5, "Workflow Status"));
		dedInfo.add(new ColumnInfo(6, "Context"));
		dedInfo.add(new ColumnInfo(7, "Display Order", "Number"));

		ColumnInfo deDrivation =
			new ColumnInfo(5, "DE_DERIVATION", "DDE ", "StructArray");
		deDrivation.nestedColumns = dedInfo;
		columnInfo.add(deDrivation);    

		return columnInfo;
	}

	//various column formats
	private class ColumnInfo {
		String rsColumnName;
		int rsIndex;
		int rsSubIndex = -1;
		String displayName;
		String type;
		List nestedColumns;

		/**
		 * Constructor for a regular column that maps to one result set column
		 */
		ColumnInfo(
				String rsColName,
				String excelColName,
				String colType) {
			super();

			rsColumnName = rsColName;
			displayName = excelColName;
			type = colType;
		}

		/**
		 * Constructor for a column that maps to one result set object column,
		 * e.g., the Derived Data Element columns
		 */
		ColumnInfo(
				int colIdx,
				String rsColName,
				String excelColName,
				String colType) {
			super();

			rsIndex = colIdx;
			rsColumnName = rsColName;
			displayName = excelColName;
			type = colType;
		}

		/**
		 * Constructor for a regular column that maps to one column inside an Aarry
		 * of type String
		 */
		ColumnInfo(
				int rsIdx,
				String excelColName) {
			super();

			rsIndex = rsIdx;
			displayName = excelColName;
			type = "String";
		}

		/**
		 * Constructor for a regular column that maps to one column inside an Aarry
		 */
		ColumnInfo(
				int rsIdx,
				String excelColName,
				String colClass) {
			super();

			rsIndex = rsIdx;
			displayName = excelColName;
			type = colClass;
		}

		/**
		 * Constructor for a regular column that maps to one column inside an
		 * Object of the Aarry type.  E.g., the classification scheme information
		 */
		ColumnInfo(
				int rsIdx,
				int rsSubIdx,
				String excelColName,
				String colType) {
			super();

			rsIndex = rsIdx;
			rsSubIndex = rsSubIdx;
			displayName = excelColName;
			type = colType;
		}
	}

		/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
