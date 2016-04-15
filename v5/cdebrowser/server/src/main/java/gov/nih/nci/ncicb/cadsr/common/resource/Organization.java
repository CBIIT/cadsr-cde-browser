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

import java.util.Collection;

public interface Organization {
   public void setId(String id);

   public String getId();

   public void setName(String name) ;

   public String getName() ;

   public void setAddresses(Collection<Address> addresses);

   public Collection<Address> getAddresses();

   public void setPerson(Collection<Person> person) ;

   public Collection<Person> getPerson();

   public void setContacts(Collection<Contact> contacts) ;

   public Collection<Contact> getContacts() ;
   
   public void setContactCommunications(Collection<ContactCommunication> contactCommunications);

   public Collection<ContactCommunication> getContactCommunications() ;
}
