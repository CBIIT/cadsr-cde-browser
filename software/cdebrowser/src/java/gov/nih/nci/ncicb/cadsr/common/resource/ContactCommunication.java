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

public interface ContactCommunication {
   public void setType(String type);

   public String getType();

   public void setValue(String value);

   public String getValue() ;

   public void setId(String id) ;

   public String getId();
   
   public void setRankOrder(int rankOrder);

   public int getRankOrder() ;
   
}
