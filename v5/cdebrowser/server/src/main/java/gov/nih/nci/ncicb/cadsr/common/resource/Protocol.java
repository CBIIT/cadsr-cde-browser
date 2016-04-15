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
import java.sql.Date;

public interface Protocol extends AdminComponent {
  public String getProtoIdseq();
  public void setProtoIdseq(String sProtoIdseq);

  public String getLeadOrg();
  public void setLeadOrg(String sLeadOrg);

  public String getType();
  public void setType(String sType);

  public String getPhase();
  public void setPhase(String sPhase);

  public Date getBeginDate();
  public void setBeginDate(Date dBeginDate);

  public Date getEndDate();
  public void setEndDate(Date dEndDate);
  
  public String getProtocolId();
  public void setProtocolId(String  id);
  
  public Object clone() throws CloneNotSupportedException ;
}