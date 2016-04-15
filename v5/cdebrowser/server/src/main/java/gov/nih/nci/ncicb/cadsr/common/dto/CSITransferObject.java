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
import gov.nih.nci.ncicb.cadsr.common.resource.ClassSchemeItem;
import gov.nih.nci.ncicb.cadsr.common.resource.Context;

import gov.nih.nci.ncicb.cadsr.common.resource.Protocol;

import java.io.Serializable;

public class CSITransferObject implements ClassSchemeItem, Serializable  {
  private String csiName = null;
  private String csiType = null;
  private String csiIdseq = null;
  private String csCsiIdseq = null;
  private String csiDescription = null;
  private String parentCscsiId = null;
  private String csIdseq = null;
  private String csDefinition = null;
  private String csLongName = null;
  private String csPrefName = null;
  private String csConteIdseq = null;
  private String acCsiIdseq = null;
  private String csType = null;
  private String csID = null;
  private Float csVersion = null;
  private Integer csiId = null;
  private Float csiVersion = null;
  /*private String csiID = null;
  */
  
  public CSITransferObject() {
  }

  public String getClassSchemeLongName() {
    return csLongName;
  }

  public void setClassSchemeLongName(String aClassSchemeName) {
    csLongName = aClassSchemeName;
  }

  public String getClassSchemePrefName() {
    return csPrefName;
  }

  public void setClassSchemePrefName(String aName) {
    csPrefName = aName;
  }

  public String getClassSchemeDefinition() {
    return csDefinition;
  }

  public void setClassSchemeDefinition(String aClassSchemeDefinition) {
    csDefinition = aClassSchemeDefinition;
  }

  public String getClassSchemeItemName() {
    return csiName;
  }

  public void setClassSchemeItemName(String aClassSchemeItemName) {
    csiName = aClassSchemeItemName;
  }

  public String getClassSchemeItemType() {
    return csiType;
  }

  public void setClassSchemeItemType(String aClassSchemeItemType) {
    csiType = aClassSchemeItemType;
  }

  public String getCsiIdseq() {
    return csiIdseq;
  }

  public void setCsiIdseq(String csiIdseq) {
    this.csiIdseq = csiIdseq;
  }

  public String getCsIdseq() {
    return csIdseq;
  }

  public void setCsIdseq(String csIdseq) {
    this.csIdseq = csIdseq;
  }

  public String getCsCsiIdseq() {
    return csCsiIdseq;
  }

  public void setCsCsiIdseq(String csCsiIdseq) {
    this.csCsiIdseq = csCsiIdseq;
  }

  public String getAcCsiIdseq() {
    return acCsiIdseq;
  }
  public void setAcCsiIdseq(String acCsiIdseq) {
    this.acCsiIdseq = acCsiIdseq;
  }


  public void setCsiDescription(String csiDescription)
  {
    this.csiDescription = csiDescription;
  }


  public String getCsiDescription()
  {
    return csiDescription;
  }


  public void setParentCscsiId(String parentCscsiId)
  {
    this.parentCscsiId = parentCscsiId;
  }


  public String getParentCscsiId()
  {
    return parentCscsiId;
  }


  public void setCsConteIdseq(String csConteIdseq)
  {
    this.csConteIdseq = csConteIdseq;
  }


  public String getCsConteIdseq()
  {
    return csConteIdseq;
  }
   public String getClassSchemeType() {
      return csType;
   }
   public void setClassSchemeType(String aClassSchemeType){
      csType = aClassSchemeType;
   }
   
   public Float getCsVersion() {
        return csVersion;
   }
   
   public void setCsVersion(Float csVersion){
       this.csVersion = csVersion;
   }

   public Integer getCsiId() {
       return csiId;
  }
  
  public void setCsiId(Integer csiId){
      this.csiId = csiId;
  }
  
  public Float getCsiVersion() {
      return csiVersion;
 }
 
 public void setCsiVersion(Float csiVersion){
     this.csiVersion = csiVersion;
 }
   
    
    public String getCsID() {
    	return csID;
	}
	
	public void setCsID(String csID) {
		this.csID = csID;
	}
	
	/*public String getCsiID() {
		return csiID;
	}
	
	public void setCsiID(String csiID) {
		this.csiID = csiID;
	}
	
	public Float getCsiVersion() {
		return csiVersion;
	}
	
	public void setCsiVersion(Float csiVersion) {
		this.csiVersion = csiVersion;
	}*/

	/**
     * This equals method only compares the Idseq to define equals
     * @param obj
     * @return 
     */  
    public boolean equals(Object obj)
    {
     if(obj == null)
      return false;
     if(!(obj instanceof ClassSchemeItem))
      return false;
     ClassSchemeItem csi = (ClassSchemeItem)obj;

    if(this.getAcCsiIdseq().equals(csi.getAcCsiIdseq()))
        return true;
      else
        return false;
    }   
    /**
     * Clones the ClassSchemeItem Object
     * @return 
     * @throws CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException {
      ClassSchemeItem copy = null;
      copy = (ClassSchemeItem)super.clone();
      return copy;
    }
}