/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.cdecart;

import java.util.List;

import javax.servlet.http.HttpSession;

import gov.nih.nci.cadsr.error.AutheticationFailureException;
import gov.nih.nci.cadsr.service.model.search.SearchNode;
import gov.nih.nci.objectCart.client.ObjectCartException;

public interface CdeCartUtilInterface {

	/**
	 * This function is called form the controller to delete CDEs from the user object cart.
	 * 
	 * @param mySession
	 * @param principalName
	 * @param ids
	 * @throws Exception
	 */
	void deleteCartNodes(HttpSession mySession, String principalName, String[] ids) throws Exception;

	/**
	 * This is a method to support Controller retrieve operation.
	 * 
	 * @param mySession
	 * @param principalName
	 * @return List<SearchNode>
	 * @throws Exception
	 */
	List<SearchNode> findCartNodes(HttpSession mySession, String principalName) throws Exception;

	/**
	 * This is a method to support Controller POST operation.
	 * 
	 * @param mySession
	 * @param sessionCart
	 * @param principalName
	 * @throws ObjectCartException 
	 * @throws AutheticationFailureException 
	 */
	void addToCart(HttpSession mySession, String principalName, List<String> cdeIds)
			throws ObjectCartException, AutheticationFailureException;

}