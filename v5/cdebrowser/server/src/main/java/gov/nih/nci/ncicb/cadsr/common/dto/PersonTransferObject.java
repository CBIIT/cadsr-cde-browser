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

import gov.nih.nci.ncicb.cadsr.common.resource.Address;
import gov.nih.nci.ncicb.cadsr.common.resource.Contact;
import gov.nih.nci.ncicb.cadsr.common.resource.ContactCommunication;
import gov.nih.nci.ncicb.cadsr.common.resource.Organization;
import gov.nih.nci.ncicb.cadsr.common.resource.Person;

import java.util.Collection;
import java.util.Iterator;

public class PersonTransferObject  extends BaseTransferObject
  implements Person{
   public String firstName;
   public String id;
   public String lastName;
   public String middleInitial;
   public String modifiedBy;
   public String position;
   public Integer rank;
   private Collection<Address> addresses;
   private Collection<Contact> contacts; //one person can act as contact for multiple ac
   private Organization organization;
   private Collection<ContactCommunication> contactCommunications; //phone, email, fax, etc
   public PersonTransferObject() {
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getId() {
      return id;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setMiddleInitial(String middleInitial) {
      this.middleInitial = middleInitial;
   }

   public String getMiddleInitial() {
      return middleInitial;
   }

   public void setPosition(String position) {
      this.position = position;
   }

   public String getPosition() {
      return position;
   }

   public void setRank(Integer rank) {
      this.rank = rank;
   }

   public Integer getRank() {
      return rank;
   }

   public void setAddresses(Collection addresses) {
      this.addresses = addresses;
   }

   public Collection getAddresses() {
      return addresses;
   }

   public void setContacts(Collection<Contact> contacts) {
      this.contacts = contacts;
   }

   public Collection<Contact> getContacts() {
      return contacts;
   }

   public void setOrganization(Organization organization) {
      this.organization = organization;
   }

   public Organization getOrganization() {
      return organization;
   }

   public void setContactCommunications(Collection<ContactCommunication> contactCommunications) {
      this.contactCommunications = contactCommunications;
   }

   public Collection<ContactCommunication> getContactCommunications() {
      return contactCommunications;
   }
   
   public String toString() {
       String person = "Name: " + this.getFirstName() + " " + this.getLastName() + "<br>";
       if (getContactCommunications() != null) {
    	   for (Iterator iter=this.getContactCommunications().iterator(); iter.hasNext();){
               ContactCommunication cc= (ContactCommunication) iter.next();
               person += cc.getType() + ": " + cc.getValue() + "<br>";
           }
       }
       
       return person + "<br>";
   }
}
