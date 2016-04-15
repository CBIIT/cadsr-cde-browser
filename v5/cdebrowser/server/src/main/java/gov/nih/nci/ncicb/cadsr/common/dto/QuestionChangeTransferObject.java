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

//import gov.nih.nci.ncicb.cadsr.common.persistence.dao.FormValidValueDAO;
import gov.nih.nci.ncicb.cadsr.common.resource.FormValidValue;
import gov.nih.nci.ncicb.cadsr.common.resource.QuestionChange;
import gov.nih.nci.ncicb.cadsr.common.resource.Question;
import gov.nih.nci.ncicb.cadsr.common.resource.InstructionChanges;
import gov.nih.nci.ncicb.cadsr.common.resource.FormValidValueChanges;

public class QuestionChangeTransferObject implements QuestionChange 
{
  private Question updatedQuestion = null;
  private InstructionChanges instructionChanges = null;
  private FormValidValueChanges fvvChanges = null;
  private String questionId;
  private String defaultValue;
  private FormValidValue  defaultValidValue;
  private boolean questAttrChange = false;
  private boolean mandatory;
  
  public QuestionChangeTransferObject()
  {
  }

  public String getQuestionId()
  {
    return questionId;
  }
  public void setQuestionId(String questionId)
  {
    this.questionId = questionId;
  }
  
  public Question getUpdatedQuestion()
  {
    return updatedQuestion;
  }

  public void setUpdatedQuestion(Question question)
  {
    this.updatedQuestion=question;
  }

  public InstructionChanges getInstrctionChanges()
  {
    return instructionChanges;
  }

  public void setInstrctionChanges(InstructionChanges changes)
  {
    this.instructionChanges=changes;
  }

  public FormValidValueChanges getFormValidValueChanges()
  {
    return fvvChanges;
  }

  public void setFormValidValueChanges(FormValidValueChanges changes)
  {
    fvvChanges=changes;
  }

  public boolean isEmpty()
  { //TODO refactor
    if(updatedQuestion==null&&instructionChanges==null&&fvvChanges==null && !questAttrChange)
      return true;
    boolean result = true;
    if(updatedQuestion!=null)
      result = false;
    if(instructionChanges!=null&&!instructionChanges.isEmpty())
      result = false;
    if(fvvChanges!=null&&!fvvChanges.isEmpty())
      result = false;
    if (questAttrChange)  {
        result = false;
    }
    return result;
  }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValidValue(FormValidValue defaultValidValue) {
        this.defaultValidValue = defaultValidValue;
    }

    public FormValidValue getDefaultValidValue() {
        return defaultValidValue;
    }

    public void setInstructionChanges(InstructionChanges instructionChanges) {
        this.instructionChanges = instructionChanges;
    }

    public InstructionChanges getInstructionChanges() {
        return instructionChanges;
    }

    public void setQuestAttrChange(boolean defaultValueChange) {
        this.questAttrChange = defaultValueChange;
    }

    public boolean isQuestAttrChange() {
        return questAttrChange;
    }
    
    public boolean isMandatory(){
        return mandatory;
    }
    
    public void setMandatory(boolean mandatory){
        this.mandatory = mandatory;
    }    
}
