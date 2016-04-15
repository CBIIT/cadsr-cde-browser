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

public interface Contact extends AdminComponent{
   public void setContactRole(String contactRole);

   public String getContactRole();

   public void setRank(Integer rank) ;

   public Integer getRank() ;

   public void setPerson(Person person) ;

   public Person getPerson();

   public void setOrganization(Organization organization);

   public Organization getOrganization() ;
}
