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

public interface TreeParameters  {
  public String getContextName();
  public String getConteIdseq();
  public String getCDETemplateName();
  public String getClassSchemeName();
  public String getClassSchemeItemName();
  public String getNodeType();
  public String getNodeIdseq();
  public String getTemplateGrpName();
  public String getCRFLongName();
  public String getProtoLongName();
  public String getCsCsiIdseq();

  public void setContextName(String s);
  public void setConteIdseq(String s);
  public void setCDETemplateName(String s);
  public void setClassSchemeName(String s);
  public void setClassSchemeItemName(String s);
  public void setNodeType(String s);
  public void setNodeIdseq(String s);
  public void setTemplateGrpName(String s);
  public void setCRFLongName(String s);
  public void setProtoLongName(String s);
  public void setCsCsiIdseq(String s);
   
}