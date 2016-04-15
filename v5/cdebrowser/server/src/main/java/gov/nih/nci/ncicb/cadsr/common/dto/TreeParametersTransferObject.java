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
import gov.nih.nci.ncicb.cadsr.common.resource.TreeParameters;

public class TreeParametersTransferObject implements TreeParameters  {
  private String contextName = "";
  private String conteIdseq = "";
  private String cdeTemplateName = "";
  private String classSchemeName = "";
  private String classSchemeItemName = "";
  private String nodeType = "";
  private String nodeIdseq = "";
  private String templateGrpName = "";
  private String crfLongName = "";
  private String protoLongName = "";
  private String csCsiIdseq = "";
  
  
  public TreeParametersTransferObject() {
  }

  public String getContextName() {
    return contextName;
  }

  public String getConteIdseq() {
    return conteIdseq;
  }

  public String getCDETemplateName() {
    return cdeTemplateName;
  }

  public String getClassSchemeName() {
    return classSchemeName;
  }

  public String getClassSchemeItemName() {
    return classSchemeItemName;
  }

  public String getNodeType() {
    return nodeType;
  }

  public String getNodeIdseq() {
    return nodeIdseq;
  }

  public String getTemplateGrpName() {
    return templateGrpName;
  }

  public String getCRFLongName() {
    return crfLongName;
  }

  public String getProtoLongName() {
    return protoLongName;
  }

  public String getCsCsiIdseq() {
    return csCsiIdseq;
  }

  public void setContextName(String s) {
    contextName = s;
  }

  public void setConteIdseq(String s) {
    conteIdseq = s;
  }

  public void setCDETemplateName(String s) {
    cdeTemplateName = s;
  }

  public void setClassSchemeName(String s) {
    classSchemeName = s;
  }

  public void setClassSchemeItemName(String s) {
    classSchemeItemName = s;
  }

  public void setNodeType(String s) {
    nodeType = s;
  }

  public void setNodeIdseq(String s) {
    nodeIdseq = s;
  }

  public void setTemplateGrpName(String s) {
    templateGrpName = s;
  }

  public void setCRFLongName(String s) {
    crfLongName = s;
  }

  public void setProtoLongName(String s) {
    protoLongName = s;
  }

  public void setCsCsiIdseq(String s) {
    csCsiIdseq = s;
  }
}