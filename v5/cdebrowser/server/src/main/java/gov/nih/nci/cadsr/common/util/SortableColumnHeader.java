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


public interface SortableColumnHeader
{
 public static int ASCENDING=1;
 public static int DESCENDING=-1;
 public static String DEFAULT_SORT_ORDER="SortableColumnHeaderDefaultSortOrder";
 static String NUMERIC_COLUMNS[] = {"cdeid"};
 public void setTertiary( String tertiary );
 public String getTertiary();
 public void setSecondary( String secondary );
 public String getSecondary();
 public void setPrimary( String primary );
 public String getPrimary();
 public int getOrder();
 public void setOrder( int order );
 public boolean isDefaultOrder();
 public void setDefaultOrder( boolean defaultOrder );
 public boolean isColumnNumeric( String columnName );

}