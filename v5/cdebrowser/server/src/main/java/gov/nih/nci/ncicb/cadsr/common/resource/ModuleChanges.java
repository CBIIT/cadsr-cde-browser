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

public interface ModuleChanges extends Serializable
{

  public String getModuleId();
  public void setModuleId(String moduleId);
  
  public InstructionChanges getInstructionChanges();
  public void setInstructionChanges(InstructionChanges changes);
  
  public List getNewQuestions();
  public void setNewQuestions(List newQuestions);
  
  public List getUpdatedQuestions();
  public void setUpdatedQuestions(List updatedQuestions);
  
  public List getDeletedQuestions();
  public void setDeletedQuestions(List deletedQuestions);  
  
  public Module getUpdatedModule();
  public void setUpdatedModule(Module updatedModule);
  
  public boolean isEmpty();
}