package gov.nih.nci.ncicb.cadsr.cdebrowser.process;


// java imports
//CDE Browser Application Imports
import gov.nih.nci.ncicb.cadsr.CaDSRConstants;
import gov.nih.nci.ncicb.cadsr.base.process.BasePersistingProcess;
import gov.nih.nci.ncicb.cadsr.cdebrowser.DESearchQueryBuilder;
import gov.nih.nci.ncicb.cadsr.cdebrowser.DataElementSearchBean;
import gov.nih.nci.ncicb.cadsr.resource.CDECart;
import gov.nih.nci.ncicb.cadsr.resource.CDECartItem;
import gov.nih.nci.ncicb.cadsr.resource.DataElement;
import gov.nih.nci.ncicb.cadsr.resource.handler.DataElementHandler;
import gov.nih.nci.ncicb.cadsr.util.ApplicationParameters;
import gov.nih.nci.ncicb.cadsr.util.DBUtil;
import gov.nih.nci.ncicb.cadsr.util.logging.Log;
import gov.nih.nci.ncicb.cadsr.util.logging.LogFactory;

import oracle.cle.persistence.HandlerFactory;

import oracle.cle.process.ProcessInfoException;
import oracle.cle.process.Service;

import oracle.cle.util.statemachine.TransitionCondition;
import oracle.cle.util.statemachine.TransitionConditionException;

import oracle.jdbc.OracleResultSet;

import oracle.sql.ARRAY;
import oracle.sql.CHAR;
import oracle.sql.Datum;
import oracle.sql.NUMBER;
import oracle.sql.STRUCT;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;
import java.io.PrintWriter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * @author Ram Chilukuri
 * @version: $Id: GetExcelDownload.java,v 1.13 2006-03-31 13:38:54 jiangj Exp $
 */
public class GetExcelDownload extends BasePersistingProcess {
  private static Log log = LogFactory.getLog(GetExcelDownload.class.getName());
  private static final int NUMBER_OF_DE_COLUMNS = 32;

  public GetExcelDownload() {
    this(null);

    DEBUG = false;
  }

  public GetExcelDownload(Service aService) {
    super(aService);

    DEBUG = false;
  }

  /**
   * Registers all the parameters and results (<code>ProcessInfo</code>) for
   * this process during construction.
   */
  public void registerInfo() {
    try {
      registerParameterObject(ProcessConstants.ALL_DATA_ELEMENTS);

      registerStringParameter("SBREXT_DSN");
      registerStringParameter("src");
      registerStringParameter("XML_DOWNLOAD_DIR");
      registerStringResult("EXCEL_FILE_NAME");
      registerParameterObject("dbUtil");
      registerStringParameter("SBR_DSN");
      registerParameterObject("desb");
      registerParameterObject(ProcessConstants.DE_SEARCH_QUERY_BUILDER);
    }
    catch (ProcessInfoException pie) {
      reportException(pie, true);
    }
    catch (Exception e) {
    }
  }

  /**
   * persist: called by start to do all persisting work for this process.  If
   * this method throws an exception, then the condition returned by the
   * <code>getPersistFailureCondition()</code> is set.  Otherwise, the
   * condition returned by <code>getPersistSuccessCondition()</code> is set.
   */
  public void persist() throws Exception {
    DBUtil dbUtil = null;

    String excelFileSuffix = "";
    String excelFilename = "";

    try {
      dbUtil = (DBUtil) getInfoObject("dbUtil");

      //String dsName = getStringInfo("SBREXT_DSN");
      dbUtil.getConnectionFromContainer();

      excelFileSuffix = dbUtil.getUniqueId("SBREXT.XML_FILE_SEQ.NEXTVAL");
      excelFilename =
        getStringInfo("XML_DOWNLOAD_DIR") + "DataElements" + "_" +
        excelFileSuffix + ".xls";

      //writeToExcelFile(resultsVector,excelFilename);
      generateExcelFile(excelFilename, dbUtil);
      setResult("EXCEL_FILE_NAME", excelFilename);

      setCondition(SUCCESS);
    }
    catch (Exception ex) {
      try {
        setCondition(FAILURE);
      }
      catch (TransitionConditionException tce) {
        reportException(tce, DEBUG);
      }
      log.error("Error generating excel file", ex);
      reportException(ex, DEBUG);
    }
    finally {
      try {
        dbUtil.returnConnection();
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  protected TransitionCondition getPersistSuccessCondition() {
    return getCondition(SUCCESS);
  }

  protected TransitionCondition getPersistFailureCondition() {
    return getCondition(FAILURE);
  }

  public void generateExcelFile(
    String filename,
    DBUtil dbUtil) throws Exception {
    Connection cn = null;

    Statement st = null;
    ResultSet rs = null;
    PrintWriter pw = null;
    String where = "";
    DataElementSearchBean desb = null;
    DESearchQueryBuilder deSearch = null;
    String source = null;
    HSSFWorkbook wb = null;
    FileOutputStream fileOut = null;
    source = getStringInfo("src");

    try {
      //String dataSource = getStringInfo("SBREXT_DSN");
      //cn = dbUtil.getConnection(); -- Commented for JBoss deployment
      //ApplicationParameters ap = ApplicationParameters.getInstance("cdebrowser");
      dbUtil.getOracleConnectionFromContainer();
      cn = dbUtil.getConnection();
      st = cn.createStatement();

      if ("deSearch".equals(source)) {

        desb = (DataElementSearchBean) getInfoObject("desb");

        deSearch =
          (DESearchQueryBuilder) getInfoObject(
            ProcessConstants.DE_SEARCH_QUERY_BUILDER);
        where = deSearch.getXMLQueryStmt();
      }
      else if ("cdeCart".equals(source)) {
        HttpServletRequest myRequest =
          (HttpServletRequest) getInfoObject("HTTPRequest");

        HttpSession userSession = myRequest.getSession(false);
        CDECart cart =
          (CDECart) userSession.getAttribute(CaDSRConstants.CDE_CART);
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
      }
      else {
        throw new Exception("No result set to download");
      }

      String sqlStmt =
        "SELECT * FROM DE_EXCEL_GENERATOR_VIEW " + "WHERE DE_IDSEQ IN " +
        " ( " + where + " )  ";

      //+" ORDER BY PREFERRED_NAME ";
      rs = st.executeQuery(sqlStmt);

      List colInfo = this.initColumnInfo();
      wb = new HSSFWorkbook();

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

      int maxRowNumber = 0;

      while (rs.next()) {
        row = sheet.createRow(rowNumber++);

        col = 0;

        for (int i = 0; i < colInfo.size(); i++) {
          ColumnInfo currCol = (ColumnInfo) colInfo.get(i);

          if (currCol.type.indexOf("Array") >= 0) {
            ARRAY array = null;

            if (currCol.type.equalsIgnoreCase("Array")) {
              array = ((OracleResultSet) rs).getARRAY(currCol.rsColumnName);
            }
            else if (currCol.type.equalsIgnoreCase("StructArray")) {
              STRUCT struct =
                ((OracleResultSet) rs).getSTRUCT(currCol.rsColumnName);
              Object[] valueStruct = struct.getAttributes();
              array = (ARRAY) valueStruct[currCol.rsIndex];
            }

            if ((array != null) && (array.length()!=0)) {
              ResultSet nestedRs = array.getResultSet();

              int nestedRowNumber = 0;

              while (nestedRs.next()) {
                row = sheet.getRow(rowNumber + nestedRowNumber);

                if (row == null) {
                  row = sheet.createRow(rowNumber + nestedRowNumber);

                  maxRowNumber = rowNumber + nestedRowNumber;
                }

                STRUCT valueStruct = (STRUCT) nestedRs.getObject(2);
                Datum[] valueDatum = valueStruct.getOracleAttributes();

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
                          ((NUMBER) valueDatum[nestedCol.rsIndex]).floatValue());
                      }
                      else
                      {
                        cell.setCellValue(
                          ((CHAR) valueDatum[nestedCol.rsIndex]).stringValue());
                      }
                    }
                  }
                  else {
                    STRUCT nestedStruct =
                      (STRUCT) valueDatum[nestedCol.rsIndex];

                    Datum[] nestedDatum = nestedStruct.getOracleAttributes();

                    if (nestedCol.type.equalsIgnoreCase("Number")) {
                      cell.setCellValue(
                        ((NUMBER) nestedDatum[nestedCol.rsSubIndex]).floatValue());
                    }
                    else if (nestedCol.type.equalsIgnoreCase("String")) {
                      cell.setCellValue(
                        ((CHAR) nestedDatum[nestedCol.rsSubIndex]).toString());
                    }
                  }
                }

                nestedRowNumber++;
              }
            }

            col += currCol.nestedColumns.size();
          }
          else if (currCol.type.equalsIgnoreCase("Struct")) {
            STRUCT struct =
              ((OracleResultSet) rs).getSTRUCT(currCol.rsColumnName);

            Object[] valueStruct = struct.getAttributes();
            HSSFCell cell = row.createCell(col++);
            cell.setCellValue((String) valueStruct[currCol.rsIndex]);
          }
          else {
            HSSFCell cell = row.createCell(col++);

            String columnName = ((ColumnInfo) colInfo.get(i)).rsColumnName;
            cell.setCellValue(rs.getString(columnName));
          }
        }

        rowNumber = maxRowNumber + 1;
      }

      fileOut = new FileOutputStream(filename);
      wb.write(fileOut);
    }
    catch (Exception ex) {
      log.error("Exception caught", ex);

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
          cn.close(); // Uncommented for JBoss deployment
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

  private List initColumnInfo() {
    List columnInfo = new ArrayList();

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
    columnInfo.add(
      new ColumnInfo("DE_WK_FLOW_STATUS", "Workflow Status", "String"));
    columnInfo.add(
      new ColumnInfo("REGISTRATION_STATUS", "Registration Status", "Number"));
    columnInfo.add(new ColumnInfo("BEGIN_DATE", "Begin Date", "Number"));
    columnInfo.add(new ColumnInfo("ORIGIN", "Source", "String"));

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

    List ocConceptInfo = new ArrayList();
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

    List propConceptInfo = new ArrayList();
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
    List vdConceptInfo = new ArrayList();
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

    //Valid Value
    List validValueInfo = new ArrayList();
    validValueInfo.add(new ColumnInfo(0, "Valid Values"));
    validValueInfo.add(new ColumnInfo(1, "Value Meaning"));

    ColumnInfo validValue = new ColumnInfo("VALID_VALUES", "", "Array");
    validValue.nestedColumns = validValueInfo;
    columnInfo.add(validValue);

    //Classification Scheme
    List csInfo = new ArrayList();

    csInfo.add(new ColumnInfo(0, 3, "Short Name", "String"));
    csInfo.add(new ColumnInfo(0, 2, "Version", "Number"));
    csInfo.add(new ColumnInfo(0, 1, "Context Name", "String"));
    csInfo.add(new ColumnInfo(0, 4, "Context Version", "Number"));
    csInfo.add(new ColumnInfo(1, "Item Name"));
    csInfo.add(new ColumnInfo(2, "Item Type Name"));

    ColumnInfo classification =
      new ColumnInfo("CLASSIFICATIONS", "Classification Scheme ", "Array");
    classification.nestedColumns = csInfo;
    columnInfo.add(classification);

    //Alternate name
    List altNameInfo = new ArrayList();
    altNameInfo.add(new ColumnInfo(0, "Context Name"));
    altNameInfo.add(new ColumnInfo(1, "Context Version", "Number"));
    altNameInfo.add(new ColumnInfo(2, ""));
    altNameInfo.add(new ColumnInfo(3, "Type"));

    ColumnInfo altNames =
      new ColumnInfo("designations", "Alternate Name ", "Array");
    altNames.nestedColumns = altNameInfo;
    columnInfo.add(altNames);

    //Reference Document
    List refDocInfo = new ArrayList();
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

    List dedInfo = new ArrayList();
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
}
