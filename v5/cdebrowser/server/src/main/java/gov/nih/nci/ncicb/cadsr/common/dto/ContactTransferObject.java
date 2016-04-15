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

import gov.nih.nci.ncicb.cadsr.common.dto.AdminComponentTransferObject;
import gov.nih.nci.ncicb.cadsr.common.resource.Contact;
import gov.nih.nci.ncicb.cadsr.common.resource.Organization;
import gov.nih.nci.ncicb.cadsr.common.resource.Person;

public class ContactTransferObject extends AdminComponentTransferObject 
implements Contact{
   public String contactRole;
    public Integer rank;
    private Person person;
    private Organization organization;
   public ContactTransferObject() {
   }

   public void setContactRole(String contactRole) {
      this.contactRole = contactRole;
   }

   public String getContactRole() {
      return contactRole;
   }

   public void setRank(Integer rank) {
      this.rank = rank;
   }

   public Integer getRank() {
      return rank;
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
   
   public String toString() {
   String contactInfo = null;
       if (this.getPerson() != null) {
           contactInfo = "Contact Person:<br>"
         + "Role: " + this.getContactRole() + "<br>"
         + person.toString();
       }
       if (this.getOrganization() != null) {
           contactInfo = "Contact Organization:<br>"
         + "Role: " + this.getContactRole() + "<br>"
         + this.getOrganization().toString();
       }
      if (contactInfo == null)      
        contactInfo = super.toString();
      return contactInfo;
   }
}
