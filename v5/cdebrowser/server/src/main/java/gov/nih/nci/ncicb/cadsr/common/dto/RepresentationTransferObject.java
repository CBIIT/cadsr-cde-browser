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

import gov.nih.nci.ncicb.cadsr.common.resource.ConceptDerivationRule;
import gov.nih.nci.ncicb.cadsr.common.resource.Representation;

public class RepresentationTransferObject extends AdminComponentTransferObject implements Representation
{
    private String name=null;
    private ConceptDerivationRule conceptDerivationRule;
    
    public RepresentationTransferObject()
    {
    }
    public String getName()
    {
      return name;
    }
    public void setName(String newName)
    {
      name=newName;
    }
    
    
    public ConceptDerivationRule getConceptDerivationRule()
    {
      return conceptDerivationRule;
    }
    public void setConceptDerivationRule(ConceptDerivationRule newConceptDerivationRule)
    {
      conceptDerivationRule=newConceptDerivationRule;
    }
}