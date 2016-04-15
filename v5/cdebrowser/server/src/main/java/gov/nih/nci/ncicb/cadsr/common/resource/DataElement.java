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


public interface DataElement extends AdminComponent{
   public String getDeIdseq();
   public void setDeIdseq(String pDeIdseq);

   public ValueDomain getValueDomain();
   public void setValueDomain(ValueDomain pValueDomain);

   public String getVdIdseq();
   public void setVdIdseq(String pVdIdseq);

   public DataElementConcept getDataElementConcept();
   public void setDataElementConcept(DataElementConcept pDataElementConcept);

   public String getDecIdseq();
   public void setDecIdseq(String pDecIdseq);

   public String getVdName();
   public String getContextName();
   public String getLongCDEName();
   public String getCDEId();
   public String getDecName();

   public void setLongCDEName (String pLongCDEName);
   public void setContextName(String pConteName);
   public void setCDEId(String pCDEId);

   public String getUsingContexts();
   public void setUsingContexts(String usingContexts);

   public DerivedDataElement getDerivedDataElement();
   public void setDerivedDataElement(DerivedDataElement dataElementDerivation);

   public List getClassifications();
   public void setClassifications(List classifications);
   
   public List getOtherVersions();
   public void setOtherVersions(List deList);   
   
   public Object clone() throws CloneNotSupportedException ;
}
