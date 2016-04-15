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

import gov.nih.nci.ncicb.cadsr.common.resource.DataElementDerivationType;
import gov.nih.nci.ncicb.cadsr.common.resource.DerivedDataElement;
import gov.nih.nci.ncicb.cadsr.common.resource.DataElementDerivation;
import gov.nih.nci.ncicb.cadsr.common.util.DebugStringBuffer;
import java.util.Collection;



public class DerivedDataElementTransferObject extends BaseTransferObject
  implements DerivedDataElement{
  private String concatenationCharacter;
  private String ddeIdSeq;
  private String rule;
  private String methods;
  private DataElementDerivationType type;
  private Collection dataElementDerivation;

  public DerivedDataElementTransferObject() {
  }


  public boolean equals(Object obj)
  {/*
   if(obj == null)
    return false;
   if(!(obj instanceof DerivedDataElement))
    return false;
   DerivedDataElement derivedDataElement = (DerivedDataElement)obj;
   if(derivedDataElement.getDdeIdseq().equals(this.getDdeIdSeq()))
   {
     return true;
   }*/
   return false;
 }   
  public String toString() {
   return "";
  }


   public void setConcatenationCharacter(String concatenationCharacter) {
      this.concatenationCharacter = concatenationCharacter;
   }


   public String getConcatenationCharacter() {
      if (concatenationCharacter == null)
         return "";
      else
         return concatenationCharacter;
   }


   public void setDdeIdSeq(String ddeIdSeq) {
      this.ddeIdSeq = ddeIdSeq;
   }


   public String getDdeIdSeq() {
      return ddeIdSeq;
   }


   public void setRule(String rule) {
      this.rule = rule;
   }


   public String getRule() {
      if (rule == null)
        return "";
      else
        return rule;
   }


   public void setDataElementDerivation(Collection dataElementDerivation) {
      this.dataElementDerivation = dataElementDerivation;
   }


   public Collection getDataElementDerivation() {
      return dataElementDerivation;
   }


   public void setType(DataElementDerivationType type) {
      this.type = type;
   }


   public DataElementDerivationType getType() {
      return type;
   }


   public void setMethods(String methods) {
      this.methods = methods;
   }


   public String getMethods() {
      if (methods == null)
         return "";
      else 
         return methods;
   }


}
