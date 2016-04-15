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

public interface ComponentConcept extends Orderable {
  /**
   * Get the Id value.
   *
   * @return the Id value.
   */
  public String getIdseq();

  public Concept getConcept();

  public void setConcept(Concept newConcept);

  /**
   * Set the Id value.
   *
   * @param newId The new Id value.
   */
  public void setIdseq(String newId);
  
  public boolean getIsPrimary();
  
  public void setIsPrimary(boolean isPrimary);

}
