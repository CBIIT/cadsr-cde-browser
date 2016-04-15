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
import gov.nih.nci.ncicb.cadsr.common.resource.ConceptDerivationRule;
import gov.nih.nci.ncicb.cadsr.common.resource.Representation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import gov.nih.nci.ncicb.cadsr.common.resource.ValueDomain;

public class ValueDomainTransferObject extends AdminComponentTransferObject
  implements ValueDomain {

  String vdIdseq;
  String dataType;
  String displayFormat;
  String unitOfMeasure;
  String maxLength;
  String minLength;
  String highValue;
  String lowValue;
  String charSet;
  String decimalPlace;
  String cdContextName;
  String cdPrefName;
  Float cdVersion; 
  int cdPublicId;
  String vdType;
  List validValues;
  Representation representation;
  ConceptDerivationRule conceptDerivationRule;
  protected String representationPrefName;
  protected String representationContextName;
  protected Float representationVersion;
  protected String representationPublicId;
  
  public ValueDomainTransferObject() {
  }

  public String getVdIdseq() {
    return vdIdseq;
  }
  
  public void setVdIdseq(String vdIdseq) {
    this.vdIdseq = vdIdseq;
  }
  
  public String getDatatype() {
    return dataType;
  }
  
  public void setDatatype(String dataType) {
    this.dataType = dataType;
  }

  public String getDisplayFormat() {
    return displayFormat;
  }
  
  public void setDisplayFormat(String displayFormat) {
    this.displayFormat = displayFormat;
  }

  public String getUnitOfMeasure() {
    return unitOfMeasure;
  }
  
  public void setUnitOfMeasure(String unitOfMeasure) {
    this.unitOfMeasure = unitOfMeasure;
    
  }

  public String getMaxLength() {
    return maxLength;
  }
  
  public void setMaxLength(String maxLength) {
    this.maxLength = maxLength;
  }

  public String getMinLength() {
    return minLength;
  }
  
  public void setMinLength(String minLength) {
    this.minLength = minLength;
  }

  public String getHighValue() {
    return highValue;
  }
  
  public void setHighValue(String highValue) {
    this.highValue = highValue;
  }

  public String getLowValue() {
    return lowValue;
  }
  
  public void setLowValue(String lowValue) {
    this.lowValue = lowValue;
  }

  public String getCharSet() {
    return charSet;
  }
  
  public void setCharSet(String charSet) {
    this.charSet = charSet;  
  }

  public String getDecimalPlace() {
    return decimalPlace;
  }
  
  public void setDecimalPlace(String decimalPlace) {
    this.decimalPlace = decimalPlace;
  }

  public String getCDContextName() {
    return cdContextName;
  }
  

  
  public String setCDContextName(String cdContextName) {
    return cdContextName;
  }
  
  public String getCDPrefName(){
    return cdPrefName;
  }
  
  public Float getCDVersion() {
    return cdVersion;
  }
  
    public String getRepresentationPrefName() {
               return representationPrefName;
       }

    public String getRepresentationContextName() {
               return representationContextName;
       }
       
    public Float getRepresentationVersion() {
            return representationVersion;
    }
    
    public String getRepresentationPublicId()
    {
      return representationPublicId;
    }


  public String getVDType() {
    return vdType;
  }
  
  public void setVDType(String vdType) {
    this.vdType = vdType;
  }
   
  public List getValidValues() {
    return validValues;
  }
  
  public void setValidValues(List validValues) {
    this.validValues = validValues;
  }

   public Representation getRepresentation()
   {
     return representation;
   }
   public void setRepresentation(Representation newRepresentation)
   {
     representation=newRepresentation;
   }
    public ConceptDerivationRule getConceptDerivationRule()
   {
     return conceptDerivationRule;
   }
   public void setConceptDerivationRule(ConceptDerivationRule rule)
   {
     conceptDerivationRule = rule;
   }  
   public int getCDPublicId()
   {
     return cdPublicId;
   }
    public void setRepresentationPublicId(String objClassPublicId)
    {
      this.representationPublicId = objClassPublicId;
    }
    
    
}