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

public interface Address {
   public void setAddressLine1(String addressLine1);

   public String getAddressLine1() ;

   public void setAddressLine2(String addressLine2);

   public String getAddressLine2() ;

   public void setCity(String city) ;

   public String getCity() ;

   public void setCountry(String country);

   public String getCountry() ;

   public void setId(String id);

   public String getId() ;

   public void setPostalCode(String postalCode) ;

   public String getPostalCode() ;

   public void setRank(Integer rank) ;

   public Integer getRank() ;

   public void setState(String state) ;

   public String getState() ;

   public void setType(String type) ;

   public String getType() ;

   public void setPerson(Person person) ;

   public Person getPerson() ;

   public void setOrganization(Organization organization) ;

   public Organization getOrganization() ;
}
