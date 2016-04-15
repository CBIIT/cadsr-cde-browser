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
import gov.nih.nci.ncicb.cadsr.common.resource.ContactCommunication;


public class ContactCommunicationTransferObject extends BaseTransferObject
  implements ContactCommunication{
   private String type;
   private String value;
   private String id;
   private int rankOrder;
   public ContactCommunicationTransferObject() {
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getType() {
      return type;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public String getValue() {
      return value;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getId() {
      return id;
   }

   public void setRankOrder(int rankOrder) {
      this.rankOrder = rankOrder;
   }

   public int getRankOrder() {
      return rankOrder;
   }
}
