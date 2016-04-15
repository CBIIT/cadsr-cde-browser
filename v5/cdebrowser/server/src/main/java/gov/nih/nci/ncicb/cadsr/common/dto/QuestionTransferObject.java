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
import gov.nih.nci.ncicb.cadsr.common.resource.DataElement;
import gov.nih.nci.ncicb.cadsr.common.resource.Form;
import gov.nih.nci.ncicb.cadsr.common.resource.FormValidValue;
import gov.nih.nci.ncicb.cadsr.common.resource.Instruction;
import gov.nih.nci.ncicb.cadsr.common.resource.Module;
import gov.nih.nci.ncicb.cadsr.common.resource.Question;

import gov.nih.nci.ncicb.cadsr.common.resource.QuestionRepitition;
import gov.nih.nci.ncicb.cadsr.common.util.DebugStringBuffer;
import java.sql.Date;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;


public class QuestionTransferObject extends FormElementTransferObject
  implements Question {
  protected Form crf;
  protected Module module;
  protected List validValues;
  protected String quesIdseq;
  protected int displayOrder;
  protected String deIdseq;
  protected DataElement dataElement;
  protected List instructions = null;
  protected String defaultValue =null;
  protected FormValidValue defaultValidValue =null;
  protected List<QuestionRepitition> questionRepititions;
  protected boolean mandatory;
  
  public QuestionTransferObject() {
    idseq = quesIdseq;
  }

  public String getQuesIdseq() {
    return quesIdseq;
  }

  public void setQuesIdseq(String idseq) {
    this.quesIdseq = idseq;
      this.idseq=idseq;
  }
  public void setIdseq(String idseq) {
        this.quesIdseq = idseq;
          this.idseq=idseq;
      }
  public Module getModule() {
    return module;
  }

  public void setModule(Module module) {
    this.module = module;
  }

  public Form getForm() {
    return crf;
  }

  public void setForm(Form crf) {
    this.crf = crf;
  }

  public List getValidValues() {
    return validValues;
  }

  public void setValidValues(List validValues) {
    this.validValues = validValues;
  }

  public int getDisplayOrder() {
    return displayOrder;
  }

  public void setDisplayOrder(int dispOrder) {
    this.displayOrder = dispOrder;
  }

  public DataElement getDataElement() {
    return dataElement;
  }

  public void setDataElement(DataElement dataElement) {
    this.dataElement = dataElement;
  }
  
  public Instruction getInstruction()
  {
    if(instructions!=null&&!instructions.isEmpty())
      return (Instruction)instructions.get(0);
    else
      return null;
  }
  public void setInstruction(Instruction newInstruction)
  {
    if(newInstruction!=null)
    {
    instructions= new ArrayList();
    instructions.add(newInstruction);      
    }
    else
    {
      instructions=null;
    }
  }
  
  public List getInstructions()
  {
    return instructions;
  }
  public void setInstructions(List newInstructions)
  {
    instructions=newInstructions;
  }
  
   public List<QuestionRepitition> getQuestionRepititions()
   {
        return questionRepititions;
   }
    public void setQuestionRepitition(List<QuestionRepitition> repeats)
    {
        questionRepititions=repeats;
    }
    
    
    //added for eDCI
    public boolean isMandatory(){
        return mandatory;
    }
    public void setMandatory(boolean mandatory){
        this.mandatory = mandatory;
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
   if(!(obj instanceof Question))
    return false;
   Question question = (Question)obj;
   if(question.getQuesIdseq().equals(this.getQuesIdseq()))
   {
     return true;
   }
   return false;
 }
  /**
   * Clones the object
   * Makes a deep copy of the Valivalues
   * form references are set to null;
   * @return 
   */
  public Object clone() throws CloneNotSupportedException {
     Question copy = null;
      copy = (Question)super.clone();
      // make the copy a little deeper
     if(getValidValues()!=null)
     {
       List validValuesCopy = new ArrayList();
       ListIterator it = getValidValues().listIterator();
       while(it.hasNext())
       {
         FormValidValue validValue = (FormValidValue)it.next();
         FormValidValue clonedValidValue = (FormValidValue)validValue.clone();
         clonedValidValue.setQuestion(copy);
         validValuesCopy.add(clonedValidValue);        
       }
       copy.setValidValues(validValuesCopy);
       copy.setForm(null);
       copy.setModule(null);
     }  
     
     if(getInstructions()!=null)
     {
       List instructionsCopy = new ArrayList();
       ListIterator it = getInstructions().listIterator();
       while(it.hasNext())
       {
         Instruction instr = (Instruction)it.next();
         Instruction instrClone = (Instruction)instr.clone(); 
         instructionsCopy.add(instrClone);
       }
       copy.setInstructions(instructionsCopy);
     } 
     
      return copy;
  }
  
    public String getDefaultValue()
    {
        return defaultValue;
    }
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue=defaultValue;
    }
    
    
    public FormValidValue getDefaultValidValue() {
        return defaultValidValue;
    }
    
    public void setDefaultValidValue(FormValidValue vv){
        defaultValidValue = vv;        
    }


  public String toString() {
    DebugStringBuffer sb = new DebugStringBuffer();
    sb.append(OBJ_SEPARATOR_START);
    sb.append(super.toString());
    sb.append(ATTR_SEPARATOR + "quesIdseq=" + getQuesIdseq(),getQuesIdseq());
    sb.append(ATTR_SEPARATOR + "displayOrder=" + getDisplayOrder());

    if(instructions!=null)
      sb.append(ATTR_SEPARATOR+"Instructions="+instructions);
    else
      sb.append(ATTR_SEPARATOR+"instructions=null");
         
    List validValues = getValidValues();

    if (validValues != null) {
      sb.append(ATTR_SEPARATOR + "ValidValues=" + validValues);
    }
    else {
      sb.append(ATTR_SEPARATOR + "ValidValues=" + null);
    }

    sb.append(OBJ_SEPARATOR_END);

    DataElement dataElement = getDataElement();

    if (dataElement != null) {
      sb.append(ATTR_SEPARATOR + "DataElement=" + dataElement);
    }
    else {
      sb.append(ATTR_SEPARATOR + "DataElement=" + null);
    }

    sb.append(OBJ_SEPARATOR_END);

    return sb.toString();
  }
}
