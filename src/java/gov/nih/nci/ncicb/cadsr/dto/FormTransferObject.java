package gov.nih.nci.ncicb.cadsr.dto;

import gov.nih.nci.ncicb.cadsr.resource.Context;
import gov.nih.nci.ncicb.cadsr.resource.Form;
import gov.nih.nci.ncicb.cadsr.resource.Instruction;
import gov.nih.nci.ncicb.cadsr.resource.Module;
import gov.nih.nci.ncicb.cadsr.resource.Protocol;

import gov.nih.nci.ncicb.cadsr.util.DebugStringBuffer;
import java.sql.Date;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class FormTransferObject extends AdminComponentTransferObject
  implements Form {
  private Protocol protocol = null;
  private String formType = null;
  private List modules;
  private String formIdseq = null;
  private String formCategory = null;
  private List instructions = null;
   private List footerInstructions = null;
   private String contextName = null;
   private String protocolLongName = null;

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

  public Protocol getProtocol() {
    return protocol;
  }

  public void setProtocol(Protocol newProtocol) {
    this.protocol = newProtocol;
  }

  public String getProtoIdseq() {
    return null;
  }

  public void setProtoIdseq(String p0) {
  }

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
     if(this.getProtocol()!=null)
      copy.setProtocol((Protocol)getProtocol().clone());

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
                
      return copy;
  }

  public String toString()
  {
    DebugStringBuffer sb = new DebugStringBuffer();
    sb.append(OBJ_SEPARATOR_START);
    sb.append(super.toString());
    sb.append(ATTR_SEPARATOR+"formIdseq="+getFormIdseq(),getFormIdseq());
    sb.append(ATTR_SEPARATOR+"formType="+getFormType(),getFormType());
    Protocol protocol = getProtocol();
    if(protocol!=null)
      sb.append(ATTR_SEPARATOR+"Protocol="+protocol.toString());
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


  public String getProtocolLongName()
  {
    if (protocolLongName == null)  
       if (getProtocol() == null)
          setProtocolLongName(this.getProtocol().getLongName());
       else 
          setProtocolLongName("");
    return protocolLongName;
  }
}
