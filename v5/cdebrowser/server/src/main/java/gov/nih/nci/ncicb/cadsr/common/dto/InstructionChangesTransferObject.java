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
import gov.nih.nci.ncicb.cadsr.common.resource.InstructionChanges;
import java.util.List;
import java.util.Map;

public class InstructionChangesTransferObject implements InstructionChanges 
{
  private Instruction newInstruction = null;
  private Instruction updatedInstruction = null;
  private Instruction deletedInstruction = null;
  private String parentId = null;
  
  public InstructionChangesTransferObject()
  {
  }
  public String getParentId()
  {
    return parentId;
  }
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
  
  public Instruction getUpdatedInstruction()
  {
    return updatedInstruction;
  }

  public void setUpdatedInstruction(Instruction instruction)
  {
    updatedInstruction=instruction;
  }

  public Instruction getNewInstruction()
  {
    return newInstruction;
  }

  public void setNewInstruction(Instruction instruction)
  {
    this.newInstruction=instruction;
  }

  public Instruction getDeletedInstruction()
  {
    return deletedInstruction;
  }

  public void setDeletedInstruction(Instruction instruction)
  {
    this.deletedInstruction=instruction;
  }

  public boolean isEmpty()
  {
    if(deletedInstruction==null&&updatedInstruction==null&&newInstruction==null)
    {
      return true;
    }
    boolean result = true;
    if(deletedInstruction!=null)
      result =false;
    if(updatedInstruction!=null)
      result =false;
    if(newInstruction!=null)
      result =false;   
    return result;
  }

}