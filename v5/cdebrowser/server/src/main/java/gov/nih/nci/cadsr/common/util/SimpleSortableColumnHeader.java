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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SimpleSortableColumnHeader implements SortableColumnHeader {
   public SimpleSortableColumnHeader() {
   }
   
   private String primary, secondary, tertiary;
   private boolean defaultOrder;
   private int order=ASCENDING;


   public void setPrimary(String primary) {
      this.primary = primary;
   }


   public String getPrimary() {
      return primary;
   }


   public void setSecondary(String secondary) {
      this.secondary = secondary;
   }


   public String getSecondary() {
      return secondary;
   }

  public String getTertiary()
  {
    return tertiary;
  }
  public void setTertiary(String tertiary)
  {
    this.tertiary=tertiary;
  }

   public void setOrder(int order) {
      this.order = order;
   }


   public int getOrder() {
      return order;
   }

  public boolean isDefaultOrder()
  {
    return defaultOrder;
  }

  public void setDefaultOrder(boolean defaultOrder)
  {
    this.defaultOrder = defaultOrder;
  }
  public boolean isColumnNumeric(String columnName) {
    Set<String> numericNames = new HashSet(Arrays.asList(NUMERIC_COLUMNS));
    return numericNames.contains(columnName);
  }

}