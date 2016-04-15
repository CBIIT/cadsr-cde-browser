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

public interface FormValidValueChange extends Serializable
{

  public String getValidValueId();
  public void setValidValueId(String vvId);
  
  public FormValidValue getUpdatedValidValue();
  public void setUpdatedValidValue(FormValidValue fvv);
  
  public InstructionChanges getInstrctionChanges();
  public void setInstrctionChanges(InstructionChanges changes);
  
  //added when valuemeaing becomes an admin components
  public String getUpdatedFormValueMeaningText();
  public void setUpdatedFormValueMeaningText(String vmText);
  public String getUpdatedFormValueMeaningDesc();
  public void setUpdatedFormValueMeaningDesc(String desc);
  
  public boolean isEmpty();
}