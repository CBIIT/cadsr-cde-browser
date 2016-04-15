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

public interface QuestionChange extends Serializable
{

  public String getQuestionId();
  public void setQuestionId(String questionId);
  
  public Question getUpdatedQuestion();
  public void setUpdatedQuestion(Question question);
  
  public InstructionChanges getInstrctionChanges();
  public void setInstrctionChanges(InstructionChanges changes);
  
  public FormValidValueChanges getFormValidValueChanges();
  public void setFormValidValueChanges(FormValidValueChanges changes);
 
  public void setDefaultValidValue(FormValidValue defaultValidValue) ;
  public FormValidValue getDefaultValidValue() ;
  
  public void setDefaultValue(String  defaultValue) ;
  public String getDefaultValue() ;
  
  public boolean isMandatory();
  public void setMandatory(boolean mandatory);
    
  public void setQuestAttrChange(boolean change);
  public boolean isQuestAttrChange();
  
  
  public boolean isEmpty();  
}