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

import gov.nih.nci.ncicb.cadsr.common.dto.BaseTransferObject;
import gov.nih.nci.ncicb.cadsr.common.resource.Address;
import gov.nih.nci.ncicb.cadsr.common.resource.Organization;
import gov.nih.nci.ncicb.cadsr.common.resource.Person;

public class AddressTransferObject extends BaseTransferObject
  implements Address{
   public String addressLine1;
   public String addressLine2;
   public String city;
   public String country;
   public String id;
   public String postalCode;
   public Integer rank;
   public String state;
   public String type;
   private Person person;
   private Organization organization;
   public AddressTransferObject() {
   }

   public void setAddressLine1(String addressLine1) {
      this.addressLine1 = addressLine1;
   }

   public String getAddressLine1() {
      return addressLine1;
   }

   public void setAddressLine2(String addressLine2) {
      this.addressLine2 = addressLine2;
   }

   public String getAddressLine2() {
      return addressLine2;
   }

   public void setCity(String city) {
      this.city = city;
   }

   public String getCity() {
      return city;
   }

   public void setCountry(String country) {
      this.country = country;
   }

   public String getCountry() {
      return country;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getId() {
      return id;
   }

   public void setPostalCode(String postalCode) {
      this.postalCode = postalCode;
   }

   public String getPostalCode() {
      return postalCode;
   }

   public void setRank(Integer rank) {
      this.rank = rank;
   }

   public Integer getRank() {
      return rank;
   }

   public void setState(String state) {
      this.state = state;
   }

   public String getState() {
      return state;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getType() {
      return type;
   }

   public void setPerson(Person person) {
      this.person = person;
   }

   public Person getPerson() {
      return person;
   }

   public void setOrganization(Organization organization) {
      this.organization = organization;
   }

   public Organization getOrganization() {
      return organization;
   }
}
