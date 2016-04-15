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

import gov.nih.nci.ncicb.cadsr.common.resource.DataElement;
import gov.nih.nci.ncicb.cadsr.common.resource.DataElementConcept;
import gov.nih.nci.ncicb.cadsr.common.resource.DataElementDerivation;
import gov.nih.nci.ncicb.cadsr.common.resource.DerivedDataElement;
import gov.nih.nci.ncicb.cadsr.common.resource.ValueDomain;
import gov.nih.nci.ncicb.cadsr.common.util.DebugStringBuffer;
import java.util.List;


public class DataElementTransferObject extends AdminComponentTransferObject
  implements DataElement {
  private String deIdseq;
  private ValueDomain valueDomain;
  private String vdIdseq;
  private DataElementConcept dataElementConcept;
  private String decIdSeq;
  private String vdName;
  private String contextName;
  private String longCDEName;
  private String cDEId;
  private String decName;
  //private String conteName;
  private String usingContexts;
  private DerivedDataElement derivedDataElement;
  private List classifications;
    private List otherVersions;

  public DataElementTransferObject() {
  }

  public String getDeIdseq() {
    return deIdseq;
  }

  public void setDeIdseq(String pDeIdseq) {
    deIdseq = pDeIdseq;
    idseq = pDeIdseq;
  }

  public ValueDomain getValueDomain() {
    return valueDomain;
  }

  public void setValueDomain(ValueDomain pValueDomain) {
    valueDomain = pValueDomain;
  }

  public String getVdIdseq() {
    return vdIdseq;
  }

  public void setVdIdseq(String pVdIdseq) {
    vdIdseq = pVdIdseq;
  }

  public DataElementConcept getDataElementConcept() {
    return dataElementConcept;
  }

  public void setDataElementConcept(DataElementConcept pDataElementConcept) {
    dataElementConcept = pDataElementConcept;
  }

  public String getDecIdseq() {
    return decIdSeq;
  }

  public void setDecIdseq(String pDecIdseq) {
    decIdSeq = pDecIdseq;
  }

  public String getVdName() {
    return vdName;
  }

  public String getContextName() {
    return contextName;
  }

  public String getLongCDEName() {
    return longCDEName;
  }

  public String getCDEId() {
    return cDEId;
  }

  public String getDecName() {
    return decName;
  }

  public void setLongCDEName(String pLongCDEName) {
    longCDEName = pLongCDEName;
  }

  public void setContextName(String pConteName) {
    contextName = pConteName;
  }

  public void setCDEId(String pCDEId) {
    cDEId = pCDEId;
  }

  public String getUsingContexts() {
    return usingContexts;
  }

  public void setUsingContexts(String usingContexts) {
    this.usingContexts = usingContexts;
  }
  /**
   * This equals method only compares the Idseq to define equals
   * @param obj
   * @return 
   */
  public boolean equals(Object obj)
  {
   if(obj == null)
    return false;
   if(!(obj instanceof DataElement))
    return false;
   DataElement dataElement = (DataElement)obj;
   if(dataElement.getDeIdseq().equals(this.getDeIdseq()))
   {
     return true;
   }
   return false;
 }   
  public String toString() {
     DebugStringBuffer sb = new DebugStringBuffer();
    sb.append(OBJ_SEPARATOR_START);
    sb.append(super.toString());
    sb.append(ATTR_SEPARATOR + "deIdseq=" + getDeIdseq(),getDeIdseq());

    /*
       ValueDomain valueDomain = getValueDomain();
       if(valueDomain!=null)
         sb.append(ATTR_SEPARATOR+"ValueDomain="+valueDomain.toString());
       else
         sb.append(ATTR_SEPARATOR+"ValueDomain=null");
     */
    sb.append(ATTR_SEPARATOR + "vdIdseq=" + getVdIdseq(),getVdIdseq());

    /*
       DataElementConcept dataElementConcept = getDataElementConcept();
       if(dataElementConcept!=null)
         sb.append(ATTR_SEPARATOR+"DataElementConcept="+dataElementConcept.toString());
       else
         sb.append(ATTR_SEPARATOR+"DataElementConcept=null");
     */
    sb.append(ATTR_SEPARATOR + "vdName=" + getVdName(),getVdName());
    sb.append(ATTR_SEPARATOR + "longCDEName=" + getLongCDEName(),getLongCDEName());
    sb.append(ATTR_SEPARATOR + "cDEId=" + getCDEId(),getCDEId());
    sb.append(ATTR_SEPARATOR + "decName=" + getDecName(),getDecName());
    sb.append(ATTR_SEPARATOR + "usingContexts=" + getUsingContexts(),getUsingContexts());
    sb.append(OBJ_SEPARATOR_END);

    return sb.toString();
  }

  public void setPublicId(int id) {
    super.setPublicId(id);
    this.cDEId = String.valueOf(id);
  }


   public void setDerivedDataElement(DerivedDataElement dataElementDerivation) {
      this.derivedDataElement = dataElementDerivation;
   }


   public DerivedDataElement getDerivedDataElement() {
      return derivedDataElement;
   }
   
   public List getClassifications()
   {
     return classifications;
   }
   public void setClassifications(List newClassifications)
   {
     classifications=newClassifications;
   }
   
    public List getOtherVersions()
    {
        return otherVersions;
    }
    public void setOtherVersions(List deList)
    {
        otherVersions=deList;
    }
    
     
}
