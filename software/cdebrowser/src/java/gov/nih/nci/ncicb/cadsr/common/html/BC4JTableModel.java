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

package gov.nih.nci.ncicb.cadsr.common.html;

import javax.swing.table.AbstractTableModel;
import oracle.jbo.Row;

public abstract class BC4JTableModel extends AbstractTableModel  {
  protected Row[] tableData;
  protected int rowCount;
  protected int columnCount;
  
  public BC4JTableModel(Row[] tableData) {
    this.tableData = tableData;
    rowCount = tableData.length;
    
  }
  public int getRowCount() {
    return rowCount;
  }
  public int getColumnCount() {
    Row firstRow = tableData[0];
    return firstRow.getAttributeCount();
  }
  public String getColumnName (int column) {
    Row firstRow = tableData[0];
    String [] columnNames = firstRow.getAttributeNames();
    return columnNames[column];
  }
  public Class getColumnClass(int c) {
    return getValueAt(0, c).getClass();
  }

  //public abstract Object getValueAt(int row, int column); 
  /*{
    return tableData[row].getAttribute(column);
  }*/
}