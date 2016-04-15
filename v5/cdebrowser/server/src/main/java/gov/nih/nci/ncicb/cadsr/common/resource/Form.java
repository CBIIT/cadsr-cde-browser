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

import java.util.Collection;
import java.util.List;

public interface Form extends FormElement,Instructionable  {
  public String getFormIdseq();
  public void setFormIdseq (String idseq);

  public String getFormType();
  public void setFormType (String formType);

  public List<Protocol> getProtocols();
  public void setProtocols(List protocols);
  
  public String getDelimitedProtocolLongNames();

  //public Protocol getProtocol();
  //public void setProtocol(Protocol protocol);

  //public String getProtoIdseq();
  //public void setProtoIdseq(String idseq);

  public List getModules(); 
  public void setModules(List blocks);

  public String getFormCategory();
  public void setFormCategory (String formCategory);

  public Instruction getFooterInstruction();
  public void setFooterInstruction(Instruction instrcution);

  public List getFooterInstructions();
  public void setFooterInstructions(List instrcutions);

  public Object clone() throws CloneNotSupportedException ;

  public void setClassifications(Collection classifications);


  public Collection getClassifications();

  public List getCDEIdList();
  
  
}