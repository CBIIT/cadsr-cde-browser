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

import gov.nih.nci.ncicb.cadsr.common.resource.Module;
import gov.nih.nci.ncicb.cadsr.common.resource.ModuleInstruction;


public class ModuleInstructionTransferObject extends InstructionTransferObject
  implements ModuleInstruction {

  private Module module = null;
  
  public ModuleInstructionTransferObject() {
  }

  public Module getModule() {
    return module;
  }

  public void setModule(Module mod) {
    this.module = mod;
  }
  
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append(OBJ_SEPARATOR_START);
    sb.append(super.toString());
    sb.append(OBJ_SEPARATOR_END);
    
    Module module = getModule();

    if (module != null) {
      sb.append(ATTR_SEPARATOR + "Module=" + module);
    }
    else {
      sb.append(ATTR_SEPARATOR + "Module=" + null);
    }

    sb.append(OBJ_SEPARATOR_END);

    return sb.toString();
  }
  
}
