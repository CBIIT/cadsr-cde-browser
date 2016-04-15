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

import gov.nih.nci.ncicb.cadsr.common.resource.Attachment;
import gov.nih.nci.ncicb.cadsr.common.resource.Context;
import gov.nih.nci.ncicb.cadsr.common.resource.DataElement;
import gov.nih.nci.ncicb.cadsr.common.resource.Form;
import gov.nih.nci.ncicb.cadsr.common.resource.Instruction;
import gov.nih.nci.ncicb.cadsr.common.resource.Module;
import gov.nih.nci.ncicb.cadsr.common.resource.Protocol;

import gov.nih.nci.ncicb.cadsr.common.resource.ReferenceDocument;
import gov.nih.nci.ncicb.cadsr.common.util.DebugStringBuffer;
import java.sql.Date;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class FormTransferObject extends FormElementTransferObject
  implements Form {
  private List protocols = null;
  private Protocol protocol = null;
  private String formType = null;
  private List modules;
  private String formIdseq = null;
  private String formCategory = null;
  private List instructions = null;
   private List footerInstructions = null;
   private String contextName = null;
   private String protocolLongName = null;
   private Collection classifications = null;
   
   private static final String DELIMITER = ", ";

  public FormTransferObject() {

  }

  public String getFormIdseq() {
    return formIdseq;
  }

  public void setFormIdseq(String formIdseq) {
    this.formIdseq = formIdseq;
    idseq = formIdseq;
  }

  public void setIdseq(String formIdseq) {
    this.formIdseq = formIdseq;
    idseq = formIdseq;
  }

  public String getFormType() {
    return formType;
  }

  public void setFormType(String newFormType) {
    formType = newFormType;
  }


/*      public Protocol getProtocol() {
        return protocol;
      }

      public void setProtocol(Protocol newProtocol) {
        this.protocol = newProtocol;
      }
*/

  public List getProtocols() {
    return protocols;
  }

  public void setProtocols(List newProtocols) {
    this.protocols = newProtocols;
  }

//starts
/*  public String getProtoIdseq() {

    if (protocol == null)
      return null;
    else
      return this.getProtocol().getIdseq();
  }

  public void setProtoIdseq(String p0) {
    if (protocol == null)
      protocol = new ProtocolTransferObject();

    protocol.setIdseq(p0);
  }
 */
//end

  public List getModules() {
    return modules;
  }

  public void setModules(List p0) {
    modules = p0;
  }

  public String getFormCategory() {
    return formCategory;
  }

  public void setFormCategory(String newFormCategory) {
    formCategory = newFormCategory;
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

  public Instruction getFooterInstruction()
  {
    if(footerInstructions!=null&&!footerInstructions.isEmpty())
      return (Instruction)footerInstructions.get(0);
    else
      return null;
  }
  public void setFooterInstruction(Instruction newFooterInstruction)
  {
    footerInstructions= new ArrayList();
    footerInstructions.add(newFooterInstruction);
  }

  public List getFooterInstructions()
  {
    return footerInstructions;
  }
  public void setFooterInstructions(List newFooterInstructions)
  {
    footerInstructions=newFooterInstructions;
  }
 /**
   * Make a clone of the form.
   * Protocol,Context,ModuleList reference are only
   * cloned for deep copy, rest of the refrence are set to null in the copy
   * @return
   */

  public Object clone() throws CloneNotSupportedException {
     Form copy = null;
      copy = (Form)super.clone();
      // make the copy a little deeper
     if(getModules()!=null)
     {
       List modulesCopy = new ArrayList();
       ListIterator it = getModules().listIterator();
       while(it.hasNext())
       {
         Module module = (Module)it.next();
         Module moduleClone = (Module)module.clone();
         moduleClone.setForm(copy);
         modulesCopy.add(moduleClone);
       }
       copy.setModules(modulesCopy);
     }
     
     //need to clone each protocol object in the protocols list
      List clonedProtocols = new ArrayList();
     if(this.getProtocols()!=null && !(this.getProtocols().isEmpty())){
         List oldList = this.getProtocols();
         Iterator it = oldList.iterator();
         while (it.hasNext()){
             Protocol current = (Protocol)it.next();
             Protocol p = (Protocol)(current.clone());
             clonedProtocols.add(p);
         }
     }
     //System.out.println("old this" + this.getProtocols());
      //System.out.println("old copy" + copy.getProtocols());
     copy.setProtocols(clonedProtocols);
     System.out.println("new" + copy.getProtocols());

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

     if(getFooterInstructions()!=null)
     {
       List instructionsCopy = new ArrayList();
       ListIterator it = getFooterInstructions().listIterator();
       while(it.hasNext())
       {
         Instruction instr = (Instruction)it.next();
         Instruction instrClone = (Instruction)instr.clone();
         instructionsCopy.add(instrClone);
       }
       copy.setFooterInstructions(instructionsCopy);
     }

     if(getRefereceDocs()!=null)
     {
       List refDocCopy = new ArrayList();
       ListIterator it = getRefereceDocs().listIterator();
       while(it.hasNext())
       {
         ReferenceDocument refDoc = (ReferenceDocument) it.next();
         ReferenceDocument refDocClone = (ReferenceDocument)refDoc.clone();
         if (refDoc.getAttachments() !=null)
         {
           List attachmentCopy = new ArrayList();
           ListIterator itAtt = refDoc.getAttachments().listIterator();
           while (itAtt.hasNext())
           {
             Attachment attchment = (Attachment) itAtt.next();
             Attachment attClone = (Attachment) attchment.clone();
             attachmentCopy.add(attClone);
           }
          refDocClone.setAttachments(attachmentCopy);
         }
         refDocCopy.add(refDocClone);
       }


       copy.setReferenceDocs(refDocCopy);
     }



      return copy;
  }

  public String toString()
  {
    DebugStringBuffer sb = new DebugStringBuffer();
    sb.append(OBJ_SEPARATOR_START);
    sb.append(super.toString());
    sb.append(ATTR_SEPARATOR+"formIdseq="+getFormIdseq(),getFormIdseq());
    sb.append(ATTR_SEPARATOR+"formType="+getFormType(),getFormType());
    List protocols = getProtocols();
    if(protocols!=null && !protocols.isEmpty())//TODO - verify
      sb.append(ATTR_SEPARATOR+"Protocol="+protocols.toArray().toString());
    else
      sb.append(ATTR_SEPARATOR+"Protocol=null");

    if(instructions!=null)
      sb.append(ATTR_SEPARATOR+"Instructions="+instructions);
    else
      sb.append(ATTR_SEPARATOR+"instructions=null");

    if(footerInstructions!=null)
      sb.append(ATTR_SEPARATOR+"footerInstructions="+footerInstructions);
    else
      sb.append(ATTR_SEPARATOR+"footerInstructions=null");

    List modules = getModules();
    if(modules!=null)
    {
      sb.append(ATTR_SEPARATOR+"Modules="+modules);
    }
    else
    {
      sb.append(ATTR_SEPARATOR+"Modules="+null);
    }
    sb.append(OBJ_SEPARATOR_END);
    return sb.toString();
  }

  public static void main(String args[]) throws Exception
  {
    Form form = new FormTransferObject();
    form.setLongName("Form1");
    Module module = new ModuleTransferObject();
    module.setLongName("Module");
    ArrayList list = new ArrayList();
    list.add(module);
    form.setModules(list);

    Form clone = (Form)form.clone();
    module.setLongName("ChangedModule");
    form.setLongName("ChangedFormName");


    System.out.println(form);
    System.out.println(clone);

  }


  public void setContextName(String contextName)
  {
    this.contextName = contextName;
  }


  public String getContextName()
  {
    if (contextName == null)
       this.setContextName(getContext().getName());
    return contextName;
  }


  public void setProtocolLongName(String protocolLongName)
  {
    this.protocolLongName = protocolLongName;
  }


  public String  getDelimitedProtocolLongNames(){
        List protocols = this.getProtocols();
        if (protocols==null || protocols.isEmpty()){
            return "";
        }
        
        StringBuffer sbuf = new StringBuffer();            
        String delimtedProtocolLongName = null;
        Iterator it = protocols.iterator();
        while (it.hasNext()){
            Protocol  p = (Protocol)it.next();
             sbuf.append(DELIMITER).append(p.getLongName());
        }
        //System.out.println("subString = "  + sbuf.substring(1) );
        return sbuf.substring(DELIMITER.length());
      }




  public void setClassifications(Collection classifications)
  {
    this.classifications = classifications;
  }


  public Collection getClassifications()
  {
    return classifications;
  }

  public List getCDEIdList(){

    List modules = this.getModules();
    if  (modules == null || modules.size()==0){
         return null;
    }

    List CDEList = new ArrayList();
    Iterator itm = modules.iterator();
    while (itm.hasNext()){
        ModuleTransferObject module = (ModuleTransferObject)itm.next();
        List questions = module.getQuestions();
        if (questions == null || questions.size()==0){
            continue;
        }
        Iterator itq = questions.iterator();
        while (itq.hasNext()){
            QuestionTransferObject q = (QuestionTransferObject)itq.next();
            DataElement de = q.getDataElement();
            if (de == null){
               continue; 
            }
            String cdeIdSeq = de.getDeIdseq();
            if (!CDEList.contains(cdeIdSeq)){
                CDEList.add(cdeIdSeq);  
            }    
        }
    }
    return CDEList;
    }//end of method

  }