/*
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */

package gov.nih.nci.cadsr.download;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import gov.nih.nci.cadsr.common.util.StringUtils;
import gov.nih.nci.cadsr.service.ClientException;

/**
 * This is an auxiliary class to create Excel file returned by DownloadExcelController.
 * 
 * @author asafievan
 *
 */
public class GetExcelDownload extends JdbcDaoSupport implements GetExcelDownloadInterface {
    private Logger logger = LogManager.getLogger(GetExcelDownload.class.getName());

	//This value will be provided by the service taken from component properties file
	//public static final String DEFAULT_RAI = "2.16.840.1.113883.3.26.2";

	//this is parameterized 
	private String localDownloadDirectory;  //"/local/content/cdebrowser/output/" is a value provided in Bean context
	private String fileNamePrefix; // a value provided in Bean context

	public void setLocalDownloadDirectory(String localDownloadDirectory) {
		this.localDownloadDirectory = localDownloadDirectory;
	}

	public void setFileNamePrefix(String excelFileNamePrefix) {
		this.fileNamePrefix = excelFileNamePrefix;
	}
	@Autowired
	public GetExcelDownload(DataSource dataSource) {
		setDataSource(dataSource);
	}

    private String generateExcelFileId() {
		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		String fileId = jdbcTemplate.queryForObject("SELECT SBREXT.XML_FILE_SEQ.NEXTVAL from dual", new Object[]{}, String.class);
		return fileId;
    }
    /* (non-Javadoc)
	 * @see gov.nih.nci.cadsr.download.GetExcelDownloadInterface#persist(java.util.Collection, java.lang.String, java.lang.String)
	 */
	@Override
	public String persist(Collection<String> itemIds, String RAI, String source) throws Exception {

		String excelFileSuffix;
		String excelFilename;
		//this is currently taken from DB sequence
		excelFileSuffix = generateExcelFileId() ;
		//the Bean properties set up in the Spring context
		excelFilename = localDownloadDirectory +  fileNamePrefix + excelFileSuffix + ".xls";
		//prepare List of ColumnInfo objects
		List<ColumnInfo> colInfo = this.initColumnInfo(source);
		//this function saves the file on the local drive
		generateExcelFile(excelFilename, itemIds, RAI, source, colInfo);
		//the file name returned to the RESTful service to build the response
		return excelFileSuffix;


	}
	protected String buildSqlInCondition(final Collection<String> itemIds) throws Exception {
		if ((itemIds != null) && (!(itemIds.isEmpty())) && (itemIds.size() <= 1000)) {
			
			StringBuilder sb = new StringBuilder();
			for (String item : itemIds) {
				sb.append("'" + item + "', ");
			}
			int len = sb.length();
			return sb.toString().substring(0, len - 2);
		}
		else if ((itemIds == null) || (!(itemIds.isEmpty()))) {
			throw new ClientException("No item ID to download");
		}
		else {
			throw new ClientException("Excel download is restricted to 1,000 item IDs");
		}
		
	}
	/**
	 * This method generates Excel file with the given file name.
	 * 
	 * @param filename absolute file name
	 * @param itemIds internal IDs
	 * @param RAI
	 * @param source @see gov.nih.nci.cadsr.download.ExcelDownloadTypes
	 * @param colInfo List of ColumnInfo objects
	 * @throws Exception
	 */
	protected void generateExcelFile(
			final String filename,
			final Collection<String> itemIds,
			String RAI,
			final String source, List<ColumnInfo> colInfo) throws Exception {
		Connection cn = null;

		Statement st = null;
		ResultSet rs = null;
		String where = "";
		HSSFWorkbook wb = null;
		FileOutputStream fileOut = null;
		
		try {
			//source tells what type of download do we do: "deSearch", "deCart", "deSearchPrior"
			wb = new HSSFWorkbook();

			HSSFSheet sheet = wb.createSheet();
			int rowNumber = 0;//NA main row number; we leave one empty row between main rows

			HSSFCellStyle boldCellStyle = wb.createCellStyle();
			HSSFFont font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			boldCellStyle.setFont(font);
			boldCellStyle.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
			//create sheet headers
			generateExcelHeaders(sheet, boldCellStyle, source, colInfo);
			
			rowNumber++; //this is the row where we want to start data
			
			cn = getConnection();// we either get a connection, or an Exception is thrown; no null is returned
			st = cn.createStatement();
			
			where = buildSqlInCondition(itemIds);//SQL IN construct
			//RAI value is taken not from DB, but from configuration; all the same for different columns
			
			String sqlStmt =
				"SELECT DE_CDE_EXCEL_GENERATOR_VIEW.*, '" + RAI + "' as \"RAI\" FROM DE_CDE_EXCEL_GENERATOR_VIEW " + "WHERE DE_IDSEQ IN " +
				" ( " + where + " )  ";

			//+" ORDER BY PREFERRED_NAME ";
			rs = st.executeQuery(sqlStmt);
			
			//this method will populate Excel data while looping though the result set
			generateWorkbook(rs, sheet, rowNumber, source, colInfo);
			
			fileOut = new FileOutputStream(filename);
			wb.write(fileOut);
		}
		catch (Exception ex) {
			logger.error("Exception caught in Generate Excel File", ex);			
			throw ex;
		}
		finally {
			if (wb != null) {
				try {
					wb.close();//NA does nothing but to avoid warning
				}
				catch (IOException e) {
					logger.debug("Unable to close Excel Workbook due to the following error ", e);
				}
			}
			if (rs != null) {
				try {						
						rs.close();
					}
				catch (Exception e) {
					logger.debug("Unable to close DB Recordset due to the following error ", e);
				}
			}
			if (st != null) {
				try {	
					st.close();
				}				
				catch (Exception e) {
					logger.debug("Unable to close DB Statement due to the following error ", e);
				}
			}
			
			if (cn != null) {
					releaseConnection(cn);
			}
			
			if (fileOut != null) {
				try{
					fileOut.flush();
					fileOut.close();
				}
				catch (Exception e) {
					logger.debug("Unable to close temporarily file due to the following error ", e);
				}
			}
		}//finally
	}
	/**
	 * This method generates headers in the first row based on list of ColumnInfo.
	 * 
	 * @param sheet
	 * @param boldCellStyle
	 * @param source
	 * @param colInfo
	 */
	protected void generateExcelHeaders(HSSFSheet sheet, final HSSFCellStyle boldCellStyle, 
			final String source, final List<ColumnInfo> colInfo) {
		// Create the first row and put the column header in it
		HSSFRow row = sheet.createRow(0);
		int col = 0;

		for (int i = 0; i < colInfo.size(); i++) {
			ColumnInfo currCol = (ColumnInfo) colInfo.get(i);

			if (currCol.type.indexOf("Array") >= 0) {//StructArray or Array
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
	}
	
	/**
	 * This method populates provided HSSFSheet.
	 * It loops through the provided ResultSet, but it does not close it. This is a job of the calling method.
	 * 
	 * @param rs ResultSet to retrieve DB CDE data for Excel
	 * @param sheet HSSFSheet created by the calling method which will be populated with DB data
	 * @param rowNumber the row number in Excel table which shall be the first one created by this method
	 * @param source
	 * @param colInfo
	 * @throws Exception
	 */
	protected void generateWorkbook(ResultSet rs, 
		HSSFSheet sheet, int rowNumber, 
		final String source, final List<ColumnInfo> colInfo) throws Exception 
	{
		int maxRowNumber = 0;
		
		while (rs.next()) {
			HSSFRow row = sheet.createRow(rowNumber);//NA new main row
			int col = 0;
			//in ColumnInfo Array type has column name; nested ColumnInfo has a rs index inside the nested rs
			for (int i = 0; i < colInfo.size(); i++) {
				ColumnInfo currCol = (ColumnInfo) colInfo.get(i);

				if (currCol.type.indexOf("Array") >= 0) {
					Array sqlArray = null;//NA java.sql types variable names are prefixed with 'sql' as sqlArray in here
					if (currCol.type.equalsIgnoreCase("Array")) {
						sqlArray = rs.getArray(currCol.rsColumnName);
					}
					else if (currCol.type.equalsIgnoreCase("StructArray")) {
						Struct structSql = (Struct)rs.getObject(currCol.rsColumnName);
						Object[] valueStruct = structSql.getAttributes();
						sqlArray = (Array)valueStruct[currCol.rsIndex];
					}

					if (sqlArray != null) {
						ResultSet nestedRs = sqlArray.getResultSet();

						int nestedRowNumber = 0;  

						while (nestedRs.next()) {
							row = sheet.getRow(rowNumber + nestedRowNumber);

							if (row == null) {
								row = sheet.createRow(rowNumber + nestedRowNumber);//NA creating a dependent row

								maxRowNumber = rowNumber + nestedRowNumber;
							}
							Struct valueStructSql = null;
							try {									
								valueStructSql = (Struct) nestedRs.getObject(2);
//								The second column stores the element value; the first column stores the index into the array for 
//								that element (with the first array element being at index 1).
							}
							catch (SQLException sqlEx) {										
								logger.info("SQLException when getting STRUCT from nester ResultSet: " + sqlEx);											
							}
							
							if ( valueStructSql != null ) {//NA adding multi-row columns values related to Oracle type arrays
								Object[] valueDatum = valueStructSql.getAttributes();
								for (short nestedI = 0; nestedI < currCol.nestedColumns.size(); nestedI++) 
								{
									ColumnInfo nestedCol =
										(ColumnInfo) currCol.nestedColumns.get(nestedI);

									HSSFCell cell = row.createCell((col + nestedI));

									if (nestedCol.rsSubIndex < 0) {
										if (valueDatum[nestedCol.rsIndex] != null) {
											if (nestedCol.type.equalsIgnoreCase("Number")) {
												cell.setCellValue(((BigDecimal)valueDatum[nestedCol.rsIndex]).floatValue());
											}
											else if (nestedCol.type.equalsIgnoreCase("Date")){  
												Object obj = valueDatum[nestedCol.rsIndex];
												if (obj instanceof java.util.Date) {//both java.sqlTimestamp and java.sql.Date extend java.util.Date
													java.sql.Date dateVal = new java.sql.Date(((java.util.Date)obj).getTime());//we use here java.sql.Date to drop the Time part of Timestamp
													cell.setCellValue(dateVal.toString());		
												}
												else {
													logger.debug("!!! valueDatum[nestedCol.rsIndex] unexpected class: " + obj.getClass().getName());
													cell.setCellValue(obj.toString());
												}
											}
											else {  
												String stringCellValue=(String) valueDatum[nestedCol.rsIndex];
												cell.setCellValue(StringUtils.updateDataForSpecialCharacters(stringCellValue));
											}
										}
									}//if (nestedCol.rsSubIndex < 0
									else {
										Struct nestedStructSql = (Struct)valueDatum[nestedCol.rsIndex];
										Object[] nestedDatum = nestedStructSql.getAttributes();
										if (nestedCol.type.equalsIgnoreCase("Number")) {
											//changed the conversion from stringValue from floatValue 07/11/2007 to fix GF7664 Prerna
											cell.setCellValue(nestedDatum[nestedCol.rsSubIndex].toString());
										}
										else if (nestedCol.type.equalsIgnoreCase("String")) {
											String stringCellValue=(String) nestedDatum[nestedCol.rsSubIndex];
											cell.setCellValue(StringUtils.updateDataForSpecialCharacters(stringCellValue));
										}
										else {
											logger.info("!!! nestedCol.type unexpected type: " + nestedCol);
										}
									}
								}//end of for
							}//end of ( valueStructSql != null )

							nestedRowNumber++;
						}//end of while (nestedRs.next())
					}//end of (sqlArray != null)

					col += currCol.nestedColumns.size();
				}//end of (currCol.type.indexOf("Array") >= 0)
				else if (currCol.type.equalsIgnoreCase("Struct")) {
					Struct struct =
							(Struct)rs.getObject(currCol.rsColumnName);
					
					Object[] valueStruct = struct.getAttributes();
					HSSFCell cell = row.createCell(col++);
					cell.setCellValue(StringUtils.updateDataForSpecialCharacters((String) valueStruct[currCol.rsIndex]));
				}
				else {
					row = sheet.getRow(rowNumber);
					HSSFCell cell = row.createCell(col++);
					// Changed the way date is displayed in Excel in 4.0
					String columnName = ((ColumnInfo) colInfo.get(i)).rsColumnName;											
					if(currCol.type.equalsIgnoreCase("Date")){
						cell.setCellValue((rs.getDate(columnName) != null)?(rs.getDate(columnName)).toString():"");
					}else{
						cell.setCellValue(StringUtils.updateDataForSpecialCharacters(rs.getString(columnName)));
					}
				}
			}
			if (maxRowNumber > rowNumber)
				rowNumber = maxRowNumber + 2;
			else 
				rowNumber += 2;
		}//end of while
	}
	protected List<ColumnInfo> initColumnInfo(String source) {
		List<ColumnInfo> columnInfo = new ArrayList<>();

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

		List<ColumnInfo> ocConceptInfo = new ArrayList<>();
		ocConceptInfo.add(new ColumnInfo(1, "Name"));
		ocConceptInfo.add(new ColumnInfo(0, "Code"));
		ocConceptInfo.add(new ColumnInfo(2, "Public ID", "Number"));
		ocConceptInfo.add(new ColumnInfo(3, "Definition Source"));    
		ocConceptInfo.add(new ColumnInfo(5, "EVS Source"));
		ocConceptInfo.add(new ColumnInfo(6, "Primary Flag"));

		ColumnInfo ocConcepts =
			new ColumnInfo("oc_concepts", "Object Class Concept ", "Array");// column X - column AC
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

		List<ColumnInfo> propConceptInfo = new ArrayList<>();
		propConceptInfo.add(new ColumnInfo(1, "Name"));
		propConceptInfo.add(new ColumnInfo(0, "Code"));
		propConceptInfo.add(new ColumnInfo(2, "Public ID", "Number"));
		propConceptInfo.add(new ColumnInfo(3, "Definition Source"));
		propConceptInfo.add(new ColumnInfo(5, "EVS Source"));
		propConceptInfo.add(new ColumnInfo(6, "Primary Flag"));

		ColumnInfo propConcepts =
			new ColumnInfo("prop_concepts", "Property Concept ", "Array");//columns AI - AN
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
		List<ColumnInfo> vdConceptInfo = new ArrayList<>();
		vdConceptInfo.add(new ColumnInfo(1, "Name"));
		vdConceptInfo.add(new ColumnInfo(0, "Code"));
		vdConceptInfo.add(new ColumnInfo(2, "Public ID", "Number"));
		vdConceptInfo.add(new ColumnInfo(3, "Definition Source"));
		vdConceptInfo.add(new ColumnInfo(5, "EVS Source"));
		vdConceptInfo.add(new ColumnInfo(6, "Primary Flag"));

		ColumnInfo vdConcepts =
			new ColumnInfo("vd_concepts", "Value Domain Concept ", "Array");//columns BC - BH
		vdConcepts.nestedColumns = vdConceptInfo;
		columnInfo.add(vdConcepts);    
		//representation concept
		//The deSearch condition is added to support both the old and the new version of excel files
		if ("deSearch".equals(source)|| "cdeCart".equals(source)){    	
			columnInfo.add(new ColumnInfo("REP_ID", "Representation Public ID", "String"));//new column BI
			columnInfo.add(
					new ColumnInfo("REP_LONG_NAME", "Representation Long Name", "String"));
			columnInfo.add(
					new ColumnInfo(
							"REP_PREFERRED_NAME", "Representation Short Name", "String"));
			columnInfo.add(
					new ColumnInfo("REP_CONTE_NAME", "Representation Context Name", "String"));
			columnInfo.add(
					new ColumnInfo("REP_VERSION", "Representation Version", "String"));

			List<ColumnInfo> repConceptInfo = new ArrayList<>();//columns BN-BS
			repConceptInfo.add(new ColumnInfo(1, "Name"));
			repConceptInfo.add(new ColumnInfo(0, "Code"));
			repConceptInfo.add(new ColumnInfo(2, "Public ID", "Number"));
			repConceptInfo.add(new ColumnInfo(3, "Definition Source"));
			repConceptInfo.add(new ColumnInfo(5, "EVS Source"));
			repConceptInfo.add(new ColumnInfo(6, "Primary Flag"));

			ColumnInfo repConcepts =
				new ColumnInfo("rep_concepts", "Representation Concept ", "Array");//new download only BN - BS
			repConcepts.nestedColumns = repConceptInfo;
			columnInfo.add(repConcepts);
		}    

		//Valid Value
		List<ColumnInfo> validValueInfo = new ArrayList<>();
		validValueInfo.add(new ColumnInfo(0, "Valid Values"));//new download BT
		//The deSearch condition is added to support both the (3.2.0.1) old and the (3.2.0.2)new version of excel files
		if ("deSearch".equals(source)|| "cdeCart".equals(source)){
			validValueInfo.add(new ColumnInfo(1, "Value Meaning Name"));
			validValueInfo.add(new ColumnInfo(2, "Value Meaning Description"));
			validValueInfo.add(new ColumnInfo(3, "Value Meaning Concepts"));
			//*	Added for 4.0	
			validValueInfo.add(new ColumnInfo(4, "PVBEGINDATE","PV Begin Date", "Date"));//new BX
			validValueInfo.add(new ColumnInfo(5, "PVENDDATE","PV End Date", "Date"));
			validValueInfo.add(new ColumnInfo(6, "VMPUBLICID", "Value Meaning PublicID", "Number"));
			validValueInfo.add(new ColumnInfo(7, "VMVERSION", "Value Meaning Version", "Number"));
			//	Added for 4.0	*/
			//validValueInfo.add(new ColumnInfo(8, "VMALTERNATEDEFINITIONS", "Value Meaning Alternate Definitions", "String"));
		}else {
			validValueInfo.add(new ColumnInfo(1, "Value Meaning"));//old column BJ
		}
		ColumnInfo validValue = new ColumnInfo("VALID_VALUES", "", "Array"); //new download columns BU - BZ
		validValue.nestedColumns = validValueInfo;
		columnInfo.add(validValue);

		//Classification Scheme
		List<ColumnInfo> csInfo = new ArrayList<>();
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
		if ("deSearch".equals(source)|| "cdeCart".equals(source)){
			csInfo.add(new ColumnInfo(3, "CsiPublicId","Item Public Id", "Number"));//new CH
			csInfo.add(new ColumnInfo(4, "CsiVersion","Item Version", "Number"));//new CI
		}
		//	Added for 4.0	
		ColumnInfo classification =
			new ColumnInfo("CLASSIFICATIONS", "Classification Scheme ", "Array");
		classification.nestedColumns = csInfo;
		columnInfo.add(classification);

		//Alternate name
		List<ColumnInfo> altNameInfo = new ArrayList<>();
		altNameInfo.add(new ColumnInfo(0, "Context Name"));//new CJ
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
		List<ColumnInfo> refDocInfo = new ArrayList<>();
		refDocInfo.add(new ColumnInfo(3, ""));//new CN
		refDocInfo.add(new ColumnInfo(0, "Name"));//new CO
		refDocInfo.add(new ColumnInfo(2, "Type"));//new CP

		ColumnInfo refDoc = new ColumnInfo("reference_docs", "Document ", "Array");//new CN start
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

		List<ColumnInfo> dedInfo = new ArrayList<>();//new CU start
		dedInfo.add(new ColumnInfo(0, "Public ID", "Number"));
		dedInfo.add(new ColumnInfo(1, "Long Name"));
		dedInfo.add(new ColumnInfo(4, "Version", "Number"));
		dedInfo.add(new ColumnInfo(5, "Workflow Status"));
		dedInfo.add(new ColumnInfo(6, "Context"));
		dedInfo.add(new ColumnInfo(7, "Display Order", "Number"));//new CZ

		ColumnInfo deDrivation =
			new ColumnInfo(5, "DE_DERIVATION", "DDE ", "StructArray");
		deDrivation.nestedColumns = dedInfo;
		columnInfo.add(deDrivation);  
		//RAI below take the same value for all columns
		columnInfo.add(
				new ColumnInfo("RAI", "Data Element RAI", "String"));//new DA
		columnInfo.add(
				new ColumnInfo("RAI", "Object Class RAI", "String"));
		columnInfo.add(
				new ColumnInfo("RAI", "Property RAI", "String"));
		columnInfo.add(
				new ColumnInfo("RAI", "Value Domain RAI", "String"));
		columnInfo.add(
				new ColumnInfo("RAI", "Representation RAI", "String"));//new DE old CL last column

		return columnInfo;
	}

	public class ColumnInfo {
		String rsColumnName;
		int rsIndex;
		int rsSubIndex = -1;
		String displayName;
		String type;
		List<ColumnInfo> nestedColumns;

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

		@Override
		public String toString() {
			return "ColumnInfo [rsColumnName=" + rsColumnName + ", rsIndex=" + rsIndex + ", rsSubIndex=" + rsSubIndex
					+ ", displayName=" + displayName + ", type=" + type + ", nestedColumns=" + nestedColumns + "]";
		}
		
	}
}
