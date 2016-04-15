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

import gov.nih.nci.ncicb.cadsr.common.resource.FormValidValue;
import gov.nih.nci.ncicb.cadsr.common.resource.FormValidValueInstruction;


public class FormValidValueInstructionTransferObject
  extends InstructionTransferObject implements FormValidValueInstruction {

  private FormValidValue formValidValue = null;
  
  public FormValidValueInstructionTransferObject() {
  }

  public FormValidValue getFormValidValue() {
    return formValidValue;
  }

  public void setFormValidValue(FormValidValue value) {
    this.formValidValue = value;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(OBJ_SEPARATOR_START);
    sb.append(super.toString());
    sb.append(OBJ_SEPARATOR_END);
    
    FormValidValue formValidValue = getFormValidValue();

    if (formValidValue != null) {
      sb.append(ATTR_SEPARATOR + "FormValidValue=" + formValidValue);
    }
    else {
      sb.append(ATTR_SEPARATOR + "FormValidValue=" + null);
    }

    sb.append(OBJ_SEPARATOR_END);

    return sb.toString();
  }
  

}
