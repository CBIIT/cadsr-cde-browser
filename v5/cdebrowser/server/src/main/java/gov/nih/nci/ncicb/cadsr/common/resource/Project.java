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

public interface Project 
{
  public String getName();
  public void setName(String name);

  public String getId();
  public void setId(String name);  
  
  public List getChildren();
  public void setChildren(List subProjects);
  public void addChild(Project subProject);
  
  public void setPackages(List packages);
  public List getPackages();
  public void addPackage(OCRPackage ocrPackage);

}