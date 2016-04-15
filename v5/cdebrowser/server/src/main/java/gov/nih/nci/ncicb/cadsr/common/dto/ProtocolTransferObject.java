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

package gov.nih.nci.ncicb.cadsr.common.dto;

import gov.nih.nci.ncicb.cadsr.common.resource.Protocol;
import gov.nih.nci.ncicb.cadsr.common.util.DebugStringBuffer;
import java.sql.Date;


public class ProtocolTransferObject extends AdminComponentTransferObject
  implements Protocol {
  String protoIdseq = null;
  //String longName = null;
  String leadOrg = null;
  String type = null;
  String phase = null;
  Date endDate = null;
  Date beginDate = null;
  String protocolId = null;

  public ProtocolTransferObject() {
  }
  public ProtocolTransferObject(String longName) {
    setLongName(longName);
  }

  public String getProtoIdseq() {
    return protoIdseq;
    
  }

  public void setIdseq(String idseq) {
        this.protoIdseq = idseq;
        this.idseq=idseq;
      }
  public void setProtoIdseq(String protocolIdseq) {
    this.protoIdseq = protocolIdseq;
      this.idseq=protocolIdseq;    
  }

  public String getLeadOrg() {
    return leadOrg;
  }

  public void setLeadOrg(String leadOrg) {
    this.leadOrg = leadOrg;
  }

  public String getType() {
    return type;
  }

  public void setType(String protocolType) {
    this.type = protocolType;
  }

  public String getPhase() {
    return phase;
  }

  public void setPhase(String protocolPhase) {
    this.phase = protocolPhase;
  }

  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  /**
   * This equals method only compares the Idseq to define equals
   * @param obj
   * @return 
   */  
 public boolean equals(Object obj)
 {
   if(obj == null)
    return false;
   if(!(obj instanceof Protocol))
    return false;
   Protocol protocol = (Protocol)obj;

  if(this.getProtoIdseq().equals(protocol.getProtoIdseq()))
      return true;
    else
      return false;
 }
  public String toString()
  {
    DebugStringBuffer sb = new DebugStringBuffer();
    sb.append(OBJ_SEPARATOR_START);
    sb.append(super.toString());
    sb.append(ATTR_SEPARATOR+"longName="+getLongName(),getLongName());
    sb.append(ATTR_SEPARATOR+"protocolIdseq="+getProtoIdseq(),getProtoIdseq());

    sb.append(OBJ_SEPARATOR_END);

    return sb.toString();
  }

    public void setProtocolId(String protocolId) {
        this.protocolId = protocolId;
    }

    public String getProtocolId() {
        return protocolId;
    }
}
