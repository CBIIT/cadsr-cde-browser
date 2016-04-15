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

package gov.nih.nci.ncicb.cadsr.common.resource;

import java.util.List;

public interface Designation  {
  public String getType();
  public String getName();
  public String getDesigIDSeq();
  public Context getContext();
  public String getLanguage();
  public List <ClassSchemeItem> getCsCsis();
  public void addCscsi (ClassSchemeItem cscsi);

}