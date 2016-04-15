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

import java.util.List;

public interface Question extends FormElement,Orderable,Instructionable   {
  public String getQuesIdseq();
  public void setQuesIdseq(String idseq);

  public Module getModule();
  public void setModule (Module block);

  public Form getForm();
  public void setForm(Form crf);

  public List getValidValues();
  public void setValidValues(List values);

  public DataElement getDataElement();
  public void setDataElement(DataElement dataElement);

  public String getDefaultValue();
  public void setDefaultValue(String defaultValue);
  
  public FormValidValue getDefaultValidValue();
  public void setDefaultValidValue(FormValidValue vv);
  
  public List<QuestionRepitition> getQuestionRepititions();
  public void setQuestionRepitition(List<QuestionRepitition> repeats);  
  
  //added for eDCI
  public boolean isMandatory();
  public void setMandatory(boolean mandatory);  
  
  public Object clone() throws CloneNotSupportedException ;
  
}