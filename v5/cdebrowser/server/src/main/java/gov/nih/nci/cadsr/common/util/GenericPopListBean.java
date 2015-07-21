package gov.nih.nci.cadsr.common.util;/*L
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

// Copyright (c) 2000 Oracle Corporation

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * gov.nih.nci.cadsr.common.util.GenericPopListBean will be used in JSP's to create a Poplist Item.
 * @author Ram Chilukuri
 */
public class GenericPopListBean extends Object {

  /**
   * Constructor
   */
  public GenericPopListBean() {
  }

  /**
   * Generates an HTML SELECT list. Includes parameter to modify the WHERE clause.
   * <p>
   * Example:<br>
   *   gov.nih.nci.cadsr.common.util.GenericPopListBean.buildList("ROLES","NAME","ID","234","P_ROLE_ID",myDBLAccess);
   * <p>
   * <pre>
   *   &lt;SELECT NAME=P_ROLE_ID &gt;
   *   &lt;OPTION&gt;&lt;/OPTION&gt;
   *   &lt;OPTION VALUE="12"&gt;CCTADMIN&lt;/OPTION&gt;
   *   &lt;OPTION VALUE="13"&gt;ORGADMIN&lt;/OPTION&gt;&lt;OPTION SELECTED VALUE="14"&gt;DEPTUSER&lt;/OPTION&gt;
   *   &lt;/SELECT&gt;
   * </pre>
   * @param tableName corresponds to name of the table.
   * @param displayColumn indicates the column value from the specified table that will]
   *        be displayed as the value of each select list OPTION.
   * @param valueColumn indicates the column value from the specified table that will
   *        be used to populate the VALUE attribute of each select list OPTION.
   * @param selectedID corresponds to the selected value of the item.
   *        Must be a non-null string. If no value currently selected, pass the
   *        empty string ("").  If non-empty, this value is matched against the retrieved ID values (see valueColumn)
   *        and the option with a match gets the SELECTED attribute set.
   * @param itemName corresponds to the NAME attribute of the SELECT LIST.
   * param dblAccess is the database connection details
   * @param additionalWhere is an additional where clause that should be used in constructing the SQL query.  Use NULL when no where clause modifications are necessary.
   *        For example: " OGN_ID = 999 "
   * @param showEmptyEntry Use TRUE if an empty entry should be included as the first element in the list, FALSE otherwise.
   * @param size If 0 then generates a DROP DOWN list, otherwise generates a multi-row list with number of rows based on this parameter value.
   * @param selectFirstOption If TRUE and selectedID is the empty string (""), first non-empty item in list is selected.
   * @param multiple If TRUE, sets the MULTIPLE attribute of the select list.
   * @param selectAll If TRUE, selects ALL items in the list.  Only valid if multiple == true.
   * @return StringBuffer containing HTML to generate the Poplist with appropriate values.
   */
  public static StringBuffer buildList (String tableName
                                       ,String displayColumn
                                       ,String valueColumn
                                       ,String selectedID
                                       ,String itemName
                                       ,DBUtil dbUtil
                                       ,String additionalWhere
                                       ,boolean showEmptyEntry
                                       ,int size
                                       ,boolean selectFirstOption
                                       ,boolean multiple
                                       ,boolean selectAll
                                       ,boolean createAllEntry
                                       ,String cssClassName) {

    StringBuffer genericList = new StringBuffer();
    Vector rsVector = null;
    Vector dataVector = null;

    String id = "";
    String value = "";
    try {
      //DBBroker myDBBroker = dblAccess.getDBBroker();
      String[] columns = {valueColumn,displayColumn};
      String where = null;
      if (additionalWhere != null) {
        where = " WHERE " + additionalWhere + " ";
      }
      else {
        where = " WHERE 1=1 ";
      }

      rsVector = dbUtil.retrieveMultipleRecordsDB(tableName,columns,where);

      if (size <= 0) {
        genericList.append("<SELECT NAME="+itemName);
      }
      else {
        genericList.append("<SELECT NAME="+itemName+" SIZE=\"" + size + "\"");
      }

      if (multiple) {
        genericList.append(" MULTIPLE ");
      }
      if (cssClassName != null) {
        genericList.append(" CLASS=\""+cssClassName+"\"");
      }

      genericList.append(">");

      if (showEmptyEntry) {
        genericList.append("<OPTION></OPTION>");
      }
      if (createAllEntry) {
        if (!selectFirstOption)
          genericList.append("<OPTION VALUE=\"ALL\">ALL</OPTION>");
        else
          genericList.append("<OPTION SELECTED VALUE=\"ALL\">ALL</OPTION>");
      }

      for (int i = 0; i  < rsVector.size(); i++) {
        dataVector = (Vector)rsVector.elementAt(i);
        id = (String)dataVector.elementAt(0);
        value = (String)dataVector.elementAt(1);
        if ((multiple && selectAll) ||
            id.equals(selectedID.toString()) ||
            (selectedID.equals("") && selectFirstOption && (i == 0)&&!createAllEntry)) {
          genericList.append("<OPTION SELECTED VALUE=\"" + id + "\">" + value + "</OPTION>");
        }
        else {
          genericList.append("<OPTION VALUE=\"" + id + "\">" + value + "</OPTION>");
        }
      }
      genericList.append("</SELECT>");
      
    }
    catch (SQLException sqle) {

    }
  return genericList;
  }

  public static StringBuffer buildList (String tableName
                                       ,String displayColumn
                                       ,String valueColumn
                                       ,String [] selIds
                                       ,String itemName
                                       ,DBUtil dbUtil
                                       ,String additionalWhere
                                       ,boolean showEmptyEntry
                                       ,int size
                                       ,boolean selectFirstOption
                                       ,boolean multiple
                                       ,boolean selectAll
                                       ,boolean createAllEntry
                                       ,String cssClassName) {

    StringBuffer genericList = new StringBuffer();
    Vector rsVector = null;
    Vector dataVector = null;

    String id = "";
    String value = "";
    try {
      //DBBroker myDBBroker = dblAccess.getDBBroker();
      String[] columns = {valueColumn,displayColumn};
      String where = null;
      if (additionalWhere != null) {
        where = " WHERE " + additionalWhere + " ";
      }
      else {
        where = " WHERE 1=1 ";
      }

      rsVector = dbUtil.retrieveMultipleRecordsDB(tableName,columns,where);

      if (size <= 0) {
        genericList.append("<SELECT NAME="+itemName);
      }
      else {
        genericList.append("<SELECT NAME="+itemName+" SIZE=\"" + size + "\"");
      }

      if (multiple) {
        genericList.append(" MULTIPLE ");
      }
      if (cssClassName != null) {
        genericList.append(" CLASS=\""+cssClassName+"\"");
      }

      genericList.append(">");

      if (showEmptyEntry) {
        genericList.append("<OPTION></OPTION>");
      }
      if (createAllEntry) {
        if (selIds == null) {
          if (!selectFirstOption)
            genericList.append("<OPTION VALUE=\"ALL\">ALL</OPTION>");
          else
            genericList.append("<OPTION SELECTED VALUE=\"ALL\">ALL</OPTION>");
        }
        else {
          if (StringUtils.containsKey(selIds,"ALL"))
            genericList.append("<OPTION SELECTED VALUE=\"ALL\">ALL</OPTION>");
          else
            genericList.append("<OPTION VALUE=\"ALL\">ALL</OPTION>");
        }
      }

      for (int i = 0; i  < rsVector.size(); i++) {
        dataVector = (Vector)rsVector.elementAt(i);
        id = (String)dataVector.elementAt(0);
        value = (String)dataVector.elementAt(1);
        if ((multiple && selectAll) ||
            StringUtils.containsKey( selIds, id ) ||
            ((selIds == null) && selectFirstOption && (i == 0)&&!createAllEntry)) {
          genericList.append("<OPTION SELECTED VALUE=\"" + id + "\">" + value + "</OPTION>");
        }
        else {
          genericList.append("<OPTION VALUE=\"" + id + "\">" + value + "</OPTION>");
        }
      }
      genericList.append("</SELECT>");
      
    }
    catch (SQLException sqle) {

    }
  return genericList;
  }


  /**
   * Generates an HTML SELECT list. Includes an empty entry as the first element.
   * <p>
   * Example:<br>
   *   gov.nih.nci.cadsr.common.util.GenericPopListBean.buildList("ROLES","NAME","ID","234","P_ROLE_ID",myDBLAccess);
   * <p>
   * <pre>
   *   &lt;SELECT NAME=P_ROLE_ID &gt;
   *   &lt;OPTION&gt;&lt;/OPTION&gt;
   *   &lt;OPTION VALUE="12"&gt;CCTADMIN&lt;/OPTION&gt;
   *   &lt;OPTION VALUE="13"&gt;ORGADMIN&lt;/OPTION&gt;&lt;OPTION SELECTED VALUE="14"&gt;DEPTUSER&lt;/OPTION&gt;
   *   &lt;/SELECT&gt;
   * </pre>
   * @param tableName corresponds to name of the table.
   * @param displayColumn indicates the column value from the specified table that will]
   *        be displayed as the value of each select list OPTION.
   * @param valueColumn indicates the column value from the specified table that will
   *        be used to populate the VALUE attribute of each select list OPTION.
   * @param selectedID corresponds to the selected value of the item.
   *        Must be a non-null string. If no value currently selected, pass the
   *        empty string ("").  If non-empty, this value is matched against the retrieved ID values (see valueColumn)
   *        and the option with a match gets the SELECTED attribute set.
   * @param itemName corresponds to the NAME attribute of the SELECT LIST.
   * param dblAccess is the database connection details
   * @return StringBuffer containing HTML to generate the Poplist with appropriate values.
   */
  public static StringBuffer buildList (String tableName
                                       ,String displayColumn
                                       ,String valueColumn
                                       ,String selectedID
                                       ,String itemName
                                       ,DBUtil dbUtil) {

    return buildList(tableName, displayColumn, valueColumn,
      selectedID, itemName, dbUtil, null, true, 0, false, false, false,false,null);
  }

  /**
   * Generates an HTML SELECT list. Includes parameter to modify the WHERE clause.
   * <p>
   * Example:<br>
   *   gov.nih.nci.cadsr.common.util.GenericPopListBean.buildList("ROLES","NAME","ID","234","P_ROLE_ID",myDBLAccess);
   * <p>
   * <pre>
   *   &lt;SELECT NAME=P_ROLE_ID &gt;
   *   &lt;OPTION&gt;&lt;/OPTION&gt;
   *   &lt;OPTION VALUE="12"&gt;CCTADMIN&lt;/OPTION&gt;
   *   &lt;OPTION VALUE="13"&gt;ORGADMIN&lt;/OPTION&gt;&lt;OPTION SELECTED VALUE="14"&gt;DEPTUSER&lt;/OPTION&gt;
   *   &lt;/SELECT&gt;
   * </pre>
   * @param tableName corresponds to name of the table.
   * @param displayColumn indicates the column value from the specified table that will]
   *        be displayed as the value of each select list OPTION.
   * @param valueColumn indicates the column value from the specified table that will
   *        be used to populate the VALUE attribute of each select list OPTION.
   * @param selectedID corresponds to the selected value of the item.
   *        Must be a non-null string. If no value currently selected, pass the
   *        empty string ("").  If non-empty, this value is matched against the retrieved ID values (see valueColumn)
   *        and the option with a match gets the SELECTED attribute set.
   * @param itemName corresponds to the NAME attribute of the SELECT LIST.
   * param dblAccess is the database connection details
   * @param additionalWhere is an additional where clause that should be used in constructing the SQL query.  Use NULL when no where clause modifications are necessary.
   *        For example: " OGN_ID = 999 "
   * @param showEmptyEntry Use TRUE if an empty entry should be included as the first element in the list, FALSE otherwise.
   * @param size If 0 then generates a DROP DOWN list, otherwise generates a multi-row list with number of rows based on this parameter value.
   * @param selectFirstOption If TRUE and selectedID is the empty string (""), first non-empty item in list is selected.
   * @return StringBuffer containing HTML to generate the Poplist with appropriate values.
   */
  public static StringBuffer buildList (
    String tableName,String displayColumn,String valueColumn,
    String selectedID,String itemName,DBUtil dbUtil,
    String additionalWhere, boolean showEmptyEntry,
    int size, boolean selectFirstOption) {

    return buildList (
      tableName, displayColumn, valueColumn,
      selectedID, itemName, dbUtil,
      additionalWhere, showEmptyEntry,
      size, selectFirstOption, false, false,false,null);
  }

  /**
   * Used to determine of the select list is empty has no options or one "empty" option.
   * @param theList  is a StringBuffer containing an HTML SELECT list.
   * @return boolean True if the list contains no occurances of the string "<OPTION>", false otherwise.
   */
  public static boolean isEmpty(String theList) {
    boolean result = true;
    if (theList.indexOf("<OPTION ") >= 0) {
      result = false;
    }
    return result;
  }

  public static StringBuffer buildList (List pageList
                                       ,String itemName
                                       ,String selectedID
                                       ,boolean showEmptyEntry
                                       ,int size
                                       ,boolean selectFirstOption
                                       ,boolean multiple
                                       ,boolean selectAll
                                       ,boolean createAllEntry
                                       ,String cssClassName
                                       ,boolean createJS
                                       ,String jsFunctionName
                                       ,String extraURLInfo) throws Exception{

    StringBuffer genericList = new StringBuffer();
    if (pageList == null) {
      throw new Exception("Error occured in creating HTML list. List is null");
    }

    if (pageList.size() == 0) {
      throw new Exception("Error occured in creating HTML list. List is empty");
    }
    
    if (size <= 0) {
      genericList.append("<SELECT NAME="+itemName);
    }
    else {
      genericList.append("<SELECT NAME="+itemName+" SIZE=\"" + size + "\"");
    }

    if (multiple) {
      genericList.append(" MULTIPLE ");
    }
    if (cssClassName != null) {
      genericList.append(" CLASS=\""+cssClassName+"\"");
    }
    if (createJS) {
      if (jsFunctionName == null){
        throw new Exception("Error occured in building HTML list item. Please "+
                            "provide a valid Javascript function name");
                      
      }
      else {
        String urlInfo = extraURLInfo;
        if (urlInfo == null) urlInfo = "";
        genericList.append(" onChange = \""+jsFunctionName+"('"+urlInfo+"');\"");
      }
    }
    genericList.append(">");

    if (showEmptyEntry) {
      genericList.append("<OPTION></OPTION>");
    }
    if (createAllEntry) {
      genericList.append("<OPTION VALUE=\"ALL\">ALL</OPTION>");
    }
    String listEntry;
    int id=0;
    for (Iterator it=pageList.iterator(); it.hasNext(); ) {
      listEntry = (String)it.next();
      if ((multiple && selectAll) ||
            new Integer(id).toString().equals(selectedID.toString()) ||
            (selectedID.equals("") && selectFirstOption && (id == 0))) {
        genericList.append("<OPTION SELECTED VALUE=\"" + id + "\">" + listEntry + "</OPTION>");
      }
      else {
        genericList.append("<OPTION VALUE=\"" + id + "\">" + listEntry + "</OPTION>");
      }
      id++;
    }

    genericList.append("</SELECT>");
  return genericList;
  }
}


