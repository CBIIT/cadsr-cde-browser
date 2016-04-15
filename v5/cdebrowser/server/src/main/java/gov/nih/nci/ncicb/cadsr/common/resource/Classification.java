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

 /**
   * Getter and setter methods for classifications
   *
   * @author Amit kochar
   * @version 1.0 (8/6/2002)
   */
package gov.nih.nci.ncicb.cadsr.common.resource;

public interface Classification 
{
   public String getDeIdseq();
   public void setDeIdseq(String aDeIdseq);
   
   public String getClassSchemeName();
   public void setClassSchemeName(String aClassSchemeName);
   
   public String getClassSchemeLongName();
   public void setClassSchemeLongName(String aClassSchemeName);

   public String getClassSchemeDefinition();
   public void setClassSchemeDefinition(String aClassSchemeDefinition);
   
   public String getClassSchemeItemName();
   public void setClassSchemeItemName(String aClassSchemeItemName);

   public String getClassSchemeItemType();
   public void setClassSchemeItemType(String aClassSchemeItemType);

   public String getClassSchemePublicId();
   public void setClassSchemePublicId(String publicId);

   public void setCsIdseq(String csIdseq);
   public String getCsIdseq();

   public void setCsiIdseq(String csiIdseq);
   public String getCsiIdseq();
   
    public Float getCsVersion();

    public void setCsVersion(Float pVersion);

    public Integer getClassSchemeItemId();
    public void setClassSchemeItemId(Integer aClassSchemeItemId);
    
    public Float getClassSchemeItemVersion();
    public void setClassSchemeItemVersion(Float aClassSchemeItemVersion);
   
}
