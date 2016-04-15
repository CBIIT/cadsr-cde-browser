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

public interface ConceptDerivationRule {
  /**
   * Get the Id value.
   *
   * @return the Id value.
   */
  public String getIdseq();

  public String getName();

  /**
   * Get the Type value.
   *
   * @return the Type value.
   */
  public String getType();

  /**
   * Set the Type value.
   *
   * @param newType The new Type value.
   */
  public void setType(String newType);

  /**
   * Set the Id value.
   *
   * @param newId The new Id value.
   */
  public void setIdseq(String newId);

  public void setName(String name);
  
  public void setComponentConcepts(List componentConcepts);

  public List getComponentConcepts();
  
  public void addComponentConcept(ComponentConcept comp);
  
  public void setRule(String rule);


  public String getRule();


  public void setMethods(String methods);


  public String getMethods();


  public void setConcatenationChar(String concatenationChar);


  public String getConcatenationChar();
  
}
