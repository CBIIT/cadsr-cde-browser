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



public class DataElementDerivationTypeTransferObject extends BaseTransferObject
  implements DataElementDerivationType{
  private String name;
  
  public DataElementDerivationTypeTransferObject() {
  }


  public boolean equals(Object obj)
  {
   if(obj == null)
    return false;
   if(!(obj instanceof DataElementDerivationType))
    return false;
   DataElementDerivationType ddet = (DataElementDerivationType)obj;
   if(ddet.getName().equals(this.getName()))
   {
     return true;
   }
   return false;
 }
  public String toString() {
   return "DerivedDataElementType: " + this.getName();
  }


   public void setName(String name) {
      this.name = name;
   }


   public String getName() {
      return name;
   }


}
