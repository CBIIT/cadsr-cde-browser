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

import java.sql.Timestamp;
import gov.nih.nci.ncicb.cadsr.common.resource.DataElement;
import gov.nih.nci.ncicb.cadsr.common.resource.AdminComponent;

public interface CDECartItem  {
  public String getId();
  public void setId(String id);

  public String getType();
  public void setType(String type);

  public Timestamp getCreatedDate();
  public String getCreatedBy();

  public void setCreatedBy(String user);

  /*public DataElement getDataElement();
  public void setDataElement(DataElement de);*/

  public AdminComponent getItem();
  public void setItem(AdminComponent ac);

  public boolean getDeletedInd();
  public void setDeletedInd(boolean ind);

  public boolean getPersistedInd();
  public void setPersistedInd(boolean ind);
  
}