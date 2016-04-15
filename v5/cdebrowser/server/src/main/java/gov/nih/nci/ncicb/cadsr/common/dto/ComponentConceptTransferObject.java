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

import gov.nih.nci.ncicb.cadsr.common.resource.ComponentConcept;
import gov.nih.nci.ncicb.cadsr.common.resource.Concept;

import java.io.Serializable;


public class ComponentConceptTransferObject implements ComponentConcept,
  Serializable {
  private String idseq = null;
  private Concept concept = null;
  private int displayOrder;
  private boolean isPrimary = false;

  public ComponentConceptTransferObject() {
  }

  /**
   * Get the Id value.
   *
   * @return the Id value.
   */
  public String getIdseq() {
    return idseq;
  }

  public void setIdseq(String newId) {
    idseq = newId;
  }

  public Concept getConcept() {
    return concept;
  }

  /**
   * Get the Order value.
   *
   * @return the Order value.
   */
  public int getDisplayOrder() {
    return displayOrder;
  }

  /**
   * Set the Order value.
   *
   * @param newOrder The new Order value.
   */
  public void setDisplayOrder(int newOrder) {
    displayOrder = newOrder;
  }

  public void setConcept(Concept newConcept) {
    concept = newConcept;
  }

  public boolean getIsPrimary()
  {
    return isPrimary;
  }

  public void setIsPrimary(boolean isPrimary)
  {
    this.isPrimary = isPrimary;
  }
}
