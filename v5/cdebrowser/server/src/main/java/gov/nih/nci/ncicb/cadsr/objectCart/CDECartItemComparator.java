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

package gov.nih.nci.ncicb.cadsr.objectCart;

import java.io.Serializable;

import java.util.Comparator;

public class CDECartItemComparator implements Comparator,Serializable   {
  public CDECartItemComparator() {
  }

  public int compare(Object o1, Object o2) {
    CDECartItem itemOne = (CDECartItem)o1;
    CDECartItem itemTwo = (CDECartItem)o2;
    String longNameOne = itemOne.getItem().getLongName();
    String longNameTwo = itemTwo.getItem().getLongName();
    return longNameOne.compareToIgnoreCase(longNameTwo);
  }
}