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

package gov.nih.nci.ncicb.cadsr.objectCart;


import gov.nih.nci.cadsr.download.DownloadUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class CDECartTransferObject implements CDECart, Serializable {
  protected Set dataElements;
  protected Set forms;
  
  private static Logger log = LogManager.getLogger(CDECartTransferObject.class.getName());
 
  public CDECartTransferObject() {
    dataElements = new HashSet();
    forms = new HashSet();
  }

  public Collection getDataElements() {
    return dataElements;
  }

  public void setDataElements(Collection items) {
    dataElements.addAll(items);
  }

  public Collection getForms() {
    return forms;
  }

  public void setForm(CDECartItem form) {
    forms.add(form);
  }

  public void setDataElement(CDECartItem item) {
    dataElements.add(item);
  }

  public void setForms(Collection forms) {
    forms.addAll(forms);
  }

  public void mergeCart(CDECart cart) {
    Collection deColl = cart.getDataElements();
    this.dataElements.addAll(deColl);
    this.forms.addAll(cart.getForms());
  }

  public void removeDataElement(String itemId) {
  }

  public void removeDataElements(Collection items) {
  }

  public CDECartItem findDataElement(String itemId) {
    return null;
  }

  public static void main(String[] args) {
    CDECartTransferObject c = new CDECartTransferObject();
    Collection l = new ArrayList();
    CDECartItem i1 = new CDECartItemTransferObject();
    CDECartItem i2 = new CDECartItemTransferObject();

    i1.setId("2");
    i2.setId("1");
    l.add(i1);
    l.add(i2);

    c.setDataElements(l);

    log.debug("Cart size: " + c.getDataElements().size());
    log.debug("Hashcode of i1: " + i1.hashCode());
    log.debug("Hashcode of i2: " + i2.hashCode());
  }

  public void associateCart(String userId) {
	// TODO Auto-generated method stub	
  }

  public void mergeDataElements(Collection items){
	  //TODO 
  }
  
  public void expireCart(){
		//TODO  
  }
	  
  public void expireCart(Date expireDate){
		  //TODO
  }
  
}
