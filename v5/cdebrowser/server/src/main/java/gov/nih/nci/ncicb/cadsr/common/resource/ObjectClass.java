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

public interface ObjectClass extends AdminComponent
{
  public String getName();
  public void setName(String newName);
  
  public String getQualifier();
  public void setQualifier(String newQualifier);

  public ConceptDerivationRule getConceptDerivationRule();
  public void setConceptDerivationRule(ConceptDerivationRule newConceptDerivationRule);  
  
}