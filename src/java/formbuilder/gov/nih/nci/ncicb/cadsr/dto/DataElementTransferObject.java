package gov.nih.nci.ncicb.cadsr.dto;

import gov.nih.nci.ncicb.cadsr.resource.DataElement;
import gov.nih.nci.ncicb.cadsr.resource.DataElementConcept;
import gov.nih.nci.ncicb.cadsr.resource.ValueDomain;
import gov.nih.nci.ncicb.cadsr.util.DebugStringBuffer;


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
  private String conteName;
  private String usingContexts;

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
    conteName = pConteName;
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
}
