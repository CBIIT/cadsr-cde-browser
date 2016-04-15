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
import gov.nih.nci.ncicb.cadsr.common.resource.Form;
import gov.nih.nci.ncicb.cadsr.common.resource.Instruction;
import gov.nih.nci.ncicb.cadsr.common.resource.Module;
import gov.nih.nci.ncicb.cadsr.common.resource.Question;

import gov.nih.nci.ncicb.cadsr.common.util.DebugStringBuffer;
import java.sql.Date;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;



public class ModuleTransferObject extends FormElementTransferObject
  implements Module {
  private Form crf;
  private List terms;
  private String moduleIdseq;
  private int dispOrder;
  private List instructions;
  private int numberOfRepeats;

  public ModuleTransferObject() {
    idseq = moduleIdseq;
  }

  public String getModuleIdseq() {
    return moduleIdseq;
  }

  public void setModuleIdseq(String idseq) {
    this.moduleIdseq = idseq;
    this.idseq=idseq;
  }
  public void setIdseq(String idseq) {
      this.moduleIdseq = idseq;
        this.idseq=idseq;
    }
    
  public Form getForm() {
    return crf;
  }

  public void setForm(Form crf) {
    this.crf = crf;
  }

  public List getQuestions() {
    return terms;
  }

  public void setQuestions(List terms) {
    this.terms = terms;
  }

  public int getDisplayOrder() {
    return dispOrder;
  }

  public void setDisplayOrder(int dispOrder) {
    this.dispOrder = dispOrder;
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
   * Clones the object
   * Makes a deep copy of the Questions
   * @return 
   */
  public Object clone() throws CloneNotSupportedException {
    Module copy = null;
      copy = (Module)super.clone();
      // make the copy a little deeper
     if(getQuestions()!=null)
     {
       List questionsCopy = new ArrayList();
       ListIterator it = getQuestions().listIterator();
       while(it.hasNext())
       {
         Question question = (Question)it.next();
         Question questionClone = (Question)question.clone();
         questionClone.setModule(copy);
         questionsCopy.add(questionClone);        
       }
       copy.setQuestions(questionsCopy);
       copy.setForm(null);
       copy.setQuestions(questionsCopy);
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
  public String toString()
  {
    DebugStringBuffer sb = new DebugStringBuffer();
    sb.append(OBJ_SEPARATOR_START);
    sb.append(super.toString());
    sb.append(ATTR_SEPARATOR+"moduleIdseq="+getModuleIdseq(),getModuleIdseq()); 
    sb.append(ATTR_SEPARATOR+"displayOrder="+getDisplayOrder()); 
    
    if(instructions!=null)
      sb.append(ATTR_SEPARATOR+"Instructions="+instructions);
    else
      sb.append(ATTR_SEPARATOR+"instructions=null");
      
    List questions = getQuestions();
    if(questions!=null) 
    {      
      sb.append(ATTR_SEPARATOR+"Questions="+questions);
    } 
    else
    {
      sb.append(ATTR_SEPARATOR+"Questions="+null);
    } 
    
    sb.append(OBJ_SEPARATOR_END);  
    return sb.toString();
  }  
  
  public static void main(String args[]) throws Exception
  {

    Module module = new ModuleTransferObject();
    module.setLongName("Module");
   
    
    Module clone = (Module)module.clone();
    module.setLongName("ChangedModule");  

  
    
    System.out.println(module);
    System.out.println(clone);
    
  }

    public void setNumberOfRepeats(int repeats)
    {
        this.numberOfRepeats = repeats;
    }

    public int getNumberOfRepeats()
    {
        return numberOfRepeats;
    }
}
