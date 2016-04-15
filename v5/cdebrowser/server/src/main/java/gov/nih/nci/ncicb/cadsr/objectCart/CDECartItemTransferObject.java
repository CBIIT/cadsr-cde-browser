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

import java.io.Serializable;


public class CDECartItemTransferObject implements CDECartItem, Serializable {
  protected String id;
  protected String type;
  protected Timestamp createdDate;
  protected String createdBy;
  protected DataElement cde;
  protected AdminComponent ac;
  protected boolean deletedInd;
  protected boolean persistedInd;

  public CDECartItemTransferObject() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Timestamp getCreatedDate() {
    return createdDate;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String user) {
    createdBy = user;
  }

  public boolean equals(Object obj) {
    if (((CDECartItem)obj).getId().equals(id)) {
      return true;
    }
    else {
      return false;
    }
  }

  public int hashCode() {
    return 59878489;
  }

  /*public DataElement getDataElement() {
    return cde;
  }

  public void setDataElement(DataElement de) {
    this.cde = de;
    id = cde.getDeIdseq();
    type = "DATAELEMENT";
        
  }*/

  public AdminComponent getItem() {
    return ac;
  }

  public void setItem(AdminComponent ac) {
    this.ac = ac;
    id = ac.getIdseq().trim();
  }

  public boolean getDeletedInd() {
    return deletedInd;
  }

  public void setDeletedInd(boolean ind) {
    deletedInd = ind;
  }

  public boolean getPersistedInd() {
    return persistedInd;
  }

  public void setPersistedInd(boolean ind) {
    persistedInd = ind;
  }
}
