package gov.nih.nci.ncicb.cadsr.dto;

import gov.nih.nci.ncicb.cadsr.resource.Context;
import gov.nih.nci.ncicb.cadsr.resource.DataElement;
import gov.nih.nci.ncicb.cadsr.resource.Form;
import gov.nih.nci.ncicb.cadsr.resource.FormValidValue;
import gov.nih.nci.ncicb.cadsr.resource.Instruction;
import gov.nih.nci.ncicb.cadsr.resource.Module;
import gov.nih.nci.ncicb.cadsr.resource.Question;

import gov.nih.nci.ncicb.cadsr.util.DebugStringBuffer;
import java.sql.Date;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;


public class QuestionTransferObject extends AdminComponentTransferObject
  implements Question {
  protected Form crf;
  protected Module module;
  protected List validValues;
  protected String quesIdseq;
  protected int displayOrder;
  protected String deIdseq;
  protected DataElement dataElement;
  protected List instructions = null;

  public QuestionTransferObject() {
    idseq = quesIdseq;
  }

  public String getQuesIdseq() {
    return quesIdseq;
  }

  public void setQuesIdseq(String idseq) {
    this.quesIdseq = idseq;
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
