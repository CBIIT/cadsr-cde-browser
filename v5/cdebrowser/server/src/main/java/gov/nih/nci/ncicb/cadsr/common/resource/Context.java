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

public interface Context extends Audit
{

  public static final String CTEP="CTEP";
  public String getConteIdseq();
  public void setConteIdseq(String aConteIdseq);
  
  public String getName();
  public void setName(String aName);

  public String getLlName();
  public void setLlName(String aLlName);

  public String getPalName();
  public void setPalName(String aPalName);

  public String getDescription();
  public void setDescription(String aDescription);

  public String getLanguage();
  public void setLanguage(String aLanguage);
  
  public Object clone() throws CloneNotSupportedException ;
}