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
import gov.nih.nci.ncicb.cadsr.common.resource.ObjectClass;
import gov.nih.nci.ncicb.cadsr.common.resource.ObjectClassRelationship;
import java.util.List;

public class ObjectClassRelationshipTransferObject extends AdminComponentTransferObject implements ObjectClassRelationship
{
  private List Projects = null;
  private ObjectClass sourceObjectClass= null;
  private ObjectClass targetObjectClass= null;  
  private ObjectClass sourceMultiplicity= null;
  private ObjectClass targetMultiplicity= null;  
  
  public ObjectClassRelationshipTransferObject()
  {
  }

  public void setProjects(List Projects)
  {
    this.Projects = Projects;
  }

  public List getProjects()
  {
    return Projects;
  }
}