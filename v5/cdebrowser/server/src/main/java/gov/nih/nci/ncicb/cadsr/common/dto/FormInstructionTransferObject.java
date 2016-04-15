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

import gov.nih.nci.ncicb.cadsr.common.resource.Form;
import gov.nih.nci.ncicb.cadsr.common.resource.FormInstruction;


public class FormInstructionTransferObject extends InstructionTransferObject
  implements FormInstruction {

  private Form form;
  
  public FormInstructionTransferObject() {
  }

  public Form getForm() {
    return form;
  }

  public void setForm(Form frm) {
    this.form = frm;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(OBJ_SEPARATOR_START);
    sb.append(super.toString());
    sb.append(OBJ_SEPARATOR_END);
    
    Form form = getForm();

    if (form != null) {
      sb.append(ATTR_SEPARATOR + "Form=" + form);
    }
    else {
      sb.append(ATTR_SEPARATOR + "Form=" + null);
    }

    sb.append(OBJ_SEPARATOR_END);

    return sb.toString();
  }
  

}
