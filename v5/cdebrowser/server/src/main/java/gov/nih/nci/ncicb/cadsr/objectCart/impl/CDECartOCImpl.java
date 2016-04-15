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

package gov.nih.nci.ncicb.cadsr.objectCart.impl;

import gov.nih.nci.ncicb.cadsr.objectCart.CDECart;
import gov.nih.nci.ncicb.cadsr.objectCart.CDECartItem;
import gov.nih.nci.ncicb.cadsr.objectCart.CDECartItemComparator;
import gov.nih.nci.ncicb.cadsr.objectCart.CDECartItemTransferObject;
import gov.nih.nci.objectCart.client.ObjectCartClient;
import gov.nih.nci.objectCart.client.ObjectCartException;
import gov.nih.nci.objectCart.domain.Cart;
import gov.nih.nci.objectCart.domain.CartObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CDECartOCImpl implements CDECart, Serializable  {

	private Cart oCart;
	private CDECartItemComparator itemComparator;
	private ObjectCartClient cartClient;
	private String userId;
	private String cartName;
	private Class CDECartObjectType;

	public CDECartOCImpl(ObjectCartClient client, String uid, String cName) {
		oCart = new Cart();
		itemComparator = new CDECartItemComparator();
		userId = uid;
		cartName = cName;
		cartClient = client;
		CDECartObjectType = CDECartItemTransferObject.class;
		
		try {
			oCart = cartClient.createCart(userId, cartName);
		} catch (ObjectCartException oce) {
			throw new RuntimeException("Constructor: Error creating the Object Cart ", oce);
		}
	}  
	
	public Collection getDataElements() {
		return getElements();
	}	

	public Collection getForms() {
		return getElements();
	}

	private Collection getElements() {
		try {
			Collection cartElements = cartClient.getObjectsByType(oCart, CDECartObjectType);
			if (cartElements != null){
				Collection items = ObjectCartClient.getPOJOCollection(CDECartObjectType, cartElements);
				List itemList = new ArrayList(items);
				Collections.sort(itemList,itemComparator);
				return itemList;
			} else 
				return new ArrayList();
		} catch (ObjectCartException oce) {
			oce.printStackTrace();
			throw new RuntimeException("getElements: Error restoring the POJO Collection", oce);
		}
	}
	
	public void setDataElement(CDECartItem item) {
		setElement(item);
	}
	
	public void setForm(CDECartItem form) {
		setElement(form);
	}
	
	private void setElement(CDECartItem item) {
		CartObject co = this.getNativeObject(item.getId());
		if (co == null){
			try {
				oCart = cartClient.storePOJO(oCart, CDECartItem.class, item.getItem().getLongName(), item.getId(), item);
			} catch (ObjectCartException oce) {
				throw new RuntimeException("getDataElements: Error storing the POJO ", oce);
			}
		}
	}
	
	public void setDataElements(Collection items) {
		setElements(items);
	}
	
	public void setForms(Collection forms) {
		setElements(forms);
	}
	
	
	private void setElements(Collection items) {
	
		Map<String, String> objectDisplayNames = new HashMap<String, String> ();
		Map<String, Object>  objects = new HashMap<String, Object>();

		for(Object o: items) {
			CDECartItem item = (CDECartItem) o;
			if(getNativeObject(item.getId()) == null){
				objectDisplayNames.put(item.getId(), item.getItem().getLongName());
				objects.put(item.getId(), item);
			}			
		}
		try {
			oCart = cartClient.storePOJOCollection(oCart, CDECartObjectType, objectDisplayNames, objects);
		} catch (ObjectCartException oce) {
			throw new RuntimeException("getDataElements: Error restoring the POJO Collection", oce);
		}
	}	

	public void mergeCart(CDECart cart) {			
		Collection deColl = cart.getDataElements();						
		mergeDataElements(deColl);	    
	}
	
	public void mergeDataElements(Collection items) {
		Map<String, String> objectDisplayNames = new HashMap<String, String> ();
		Map<String, Object>  objects = new HashMap<String, Object>();
		HashSet<CartObject> forRemoval = new HashSet<CartObject>();
		
		for(Object o: items) {
			CDECartItem item = (CDECartItem) o;
			CartObject co = getNativeObject(item.getId());
			
			objectDisplayNames.put(item.getId(), item.getItem().getLongName());
			objects.put(item.getId(), item);
			
			if(co != null)
				forRemoval.add(co);
		}
		try {
			oCart = cartClient.removeObjectCollection(oCart, forRemoval);
			oCart = cartClient.storePOJOCollection(oCart, CDECartObjectType, objectDisplayNames, objects);
		} catch (ObjectCartException oce) {
			throw new RuntimeException("getDataElements: Error restoring the POJO Collection", oce);
		}
	}
	
	public void removeDataElement(String itemId) {
		CartObject co = getNativeObject(itemId);
		if (co != null) {
			try {	
				oCart = cartClient.removeObject(oCart, co);			
			} catch (ObjectCartException oce) {
				throw new RuntimeException("removeDataElement: Error removing object with native ID:"+itemId, oce);
			}			
		}		
	}

	public void removeDataElements(Collection items) {
		List<CartObject> cList = getNativeObjects(items);
		if (!cList.isEmpty()) {
			try {	
				oCart = cartClient.removeObjectCollection(oCart, cList);
			} catch (ObjectCartException oce) {
				throw new RuntimeException("removeDataElements: Error removing collection of objects", oce);
			}	
		}
	}

	public CDECartItem findDataElement(String itemId) {
		CartObject item = getId(oCart, itemId);
		if ( item != null) {
			try {
				return (CDECartItem)cartClient.getPOJO(CDECartObjectType, item);
			} catch (ObjectCartException oce) {
				throw new RuntimeException("findDataElement: Error finding objects with native Id:"+itemId, oce);
			}
		}

		return null; 
	}

	private CartObject getNativeObject(String id) {
		if (oCart.getCartObjectCollection() == null)
			return null;
		
		for(CartObject co: oCart.getCartObjectCollection()){
			if (co.getNativeId().equals(id))
				return co;
		}
		return null;
	}
	
	private List<CartObject> getNativeObjects(Collection ids) {
		List<CartObject> list = new ArrayList<CartObject>();
		if (oCart.getCartObjectCollection() == null)
			return list;
		for(CartObject co: oCart.getCartObjectCollection()){
			if (ids.contains(co.getNativeId()))
				list.add(co);
		}
		return list;
	}

	private CartObject getId(Cart cart, String id) {
		for(CartObject co: cart.getCartObjectCollection()){
			if (co.getNativeId().equals(id))
				return co;
		}
		return null;
	}

	public void associateCart(String userId) {	
		try {			
			 oCart = cartClient.associateCart(oCart, userId);
		} catch (ObjectCartException oce) {
			throw new RuntimeException("associateCart: Error associating cart ("+oCart.getUserId()+") with new User ID "+userId, oce);
		}		
	}

	public void expireCart(){
		try{
			oCart = cartClient.setDefaultExpiration(oCart);
		}catch(ObjectCartException oce){
			throw new RuntimeException("expireCart: Error in setting Cart for default Expiration("+oCart.getUserId()+")");
		}		
	}
	
	public void expireCart(Date expirationDate){
		try{
			oCart = cartClient.setCartExpiration(oCart, expirationDate);
		}catch(ObjectCartException oce){
			throw new RuntimeException("expireCart: Error in setting Cart Expiration by date("+oCart.getUserId()+")");
		}		
	}
	
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the cartName
	 */
	public String getCartName() {
		return cartName;
	}

	/**
	 * @param cartName the cartName to set
	 */
	public void setCartName(String cartName) {
		this.cartName = cartName;
	}	
	
	public String getCartId() {
		return oCart.getId().toString();	
	}
}
