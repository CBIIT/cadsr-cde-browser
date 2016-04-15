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

import gov.nih.nci.ncicb.cadsr.common.resource.Instruction;


public class InstructionTransferObject extends AdminComponentTransferObject
  implements Instruction {

  private int displayOrder;
  private String idseq = null;
  
  public InstructionTransferObject() {
  }

  public int getDisplayOrder() {
    return displayOrder;
  }

  public void setDisplayOrder(int order) {
    this.displayOrder = order;
  }

  public String getIdseq() {
    return idseq;
  }

  public void setIdseq(String idseq) {
    this.idseq = idseq;
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
   if(!(obj instanceof Instruction))
    return false;
   Instruction instr = (Instruction)obj;
   if(instr.getLongName().trim().equals(this.getLongName().trim()))
   {
     return true;
   }
   return false;
 }  
 
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(OBJ_SEPARATOR_START);
    sb.append(super.toString());
    sb.append(ATTR_SEPARATOR + "displayOrder=" + getDisplayOrder());
    sb.append(ATTR_SEPARATOR + "instrIdseq=" + getIdseq());
    sb.append(OBJ_SEPARATOR_END);

    return sb.toString();
  }
  
}
