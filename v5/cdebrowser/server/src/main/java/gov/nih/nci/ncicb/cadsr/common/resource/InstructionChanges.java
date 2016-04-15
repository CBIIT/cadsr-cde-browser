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
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface InstructionChanges extends Serializable
{

  public String getParentId();
  public void setParentId(String parentId);
  
  public Instruction getUpdatedInstruction();
  public void setUpdatedInstruction(Instruction instructions);

  public Instruction getNewInstruction();
  public void setNewInstruction(Instruction instructions);
  
  public Instruction getDeletedInstruction();
  public void setDeletedInstruction(Instruction instructions);
  
  public boolean isEmpty();
}
