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

import gov.nih.nci.ncicb.cadsr.common.CaDSRConstants;

import java.sql.Date;
import java.sql.Timestamp;


public interface Audit extends CaDSRConstants,Cloneable {
  public String getCreatedBy();

  public void setCreatedBy(String pCreatedBy);

  public Timestamp getDateCreated();

  public void setDateCreated(Timestamp pCreatedDate);

  public String getModifiedBy();

  public void setModifiedBy(String pModifiedBy);

  public Timestamp getDateModified();

  public void setDateModified(Timestamp pModifiedDate);

  
  public String toString();
}
