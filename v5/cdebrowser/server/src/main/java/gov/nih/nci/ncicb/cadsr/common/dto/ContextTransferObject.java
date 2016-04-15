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

import gov.nih.nci.ncicb.cadsr.common.resource.Context;
import gov.nih.nci.ncicb.cadsr.common.util.DebugStringBuffer;


public class ContextTransferObject extends BaseTransferObject implements Context {
  private String conteIdSeq = null;
  private String name = null;
  private String description = null;
  public ContextTransferObject() {
  }

  public ContextTransferObject(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getConteIdseq() {
    return conteIdSeq;
  }

  public void setConteIdseq(String newConteIdseq) {
    conteIdSeq = newConteIdseq;
  }

  public String getLlName() {
    return null;
  }

  public void setLlName(String p0) {
  }

  public String getPalName() {
    return null;
  }

  public void setPalName(String p0) {
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String desc) {
    description = desc;
  }

  public String getLanguage() {
    return null;
  }

  public void setLanguage(String p0) {
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
   if(!(obj instanceof Context))
    return false;
   Context context = (Context)obj;

  if(this.getConteIdseq().equals(context.getConteIdseq()))
      return true;
    else
      return false;
 }  
  /**
   * Clones the Context Object
   * @return 
   * @throws CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException {
    Context copy = null;
    copy = (Context)super.clone();
    return copy;
  }
  public String toString() {
    DebugStringBuffer sb = new DebugStringBuffer();
    sb.append(OBJ_SEPARATOR_START);
    sb.append(ATTR_SEPARATOR + "name=" + getName(),getName());
    sb.append(ATTR_SEPARATOR + "conteIdSeq=" + getConteIdseq(),getConteIdseq());
    sb.append(super.toString());
    sb.append(OBJ_SEPARATOR_END);
    sb.toString();

    return sb.toString();
  }
}
