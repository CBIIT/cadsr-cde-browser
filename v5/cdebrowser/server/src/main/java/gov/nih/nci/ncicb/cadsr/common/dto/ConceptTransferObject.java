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

import gov.nih.nci.ncicb.cadsr.common.resource.Concept;


public class ConceptTransferObject extends AdminComponentTransferObject
  implements Concept {
  private String definitionSource = null;
  private String evsSource = null;
  private String code = null;

  public ConceptTransferObject() {
  }

  /**
   * Get the DefinitionSource value.
   *
   * @return the DefinitionSource value.
   */
  public String getDefinitionSource() {
      if (definitionSource == null)
         return "";
      else 
         return definitionSource;    
  }

  /**
   * Set the DefinitionSource value.
   *
   * @param newDefinitionSource The new DefinitionSource value.
   */
  public void setDefinitionSource(String newDefinitionSource) {
    definitionSource = newDefinitionSource;
  }

  public void setEvsSource(String evsSource) {
    this.evsSource = evsSource;
  }

  public String getEvsSource() {
      if (evsSource == null)
         return "";
      else 
         return evsSource;        
  }


  public void setCode(String code)
  {
    this.code = code;
  }


  public String getCode()
  {
      if (code == null)
         return "";
      else 
         return code;        
  }
}
