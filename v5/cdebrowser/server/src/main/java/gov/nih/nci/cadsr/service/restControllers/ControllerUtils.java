/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.model.SearchPreferencesServer;

/**
 * 
 * @author asafievan
 *
 */
public class ControllerUtils {
	private static Logger logger = LogManager.getLogger(ControllerUtils.class.getName());
	/**
	 * This method initializes Search Preferences in HTTP Session.
	 * 
	 * @throws NullPointerException
	 * @param httpSession session to add search preferences attribute
	 */
	public static SearchPreferencesServer initSearchPreferencesServerInSession(HttpSession httpSession) {
		SearchPreferencesServer searchPreferencesServer = new SearchPreferencesServer();
		searchPreferencesServer.initPreferences();
		httpSession.setAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES, searchPreferencesServer);
		logger.debug("SearchPreferencesServer adding default to the user HTTP session: " + searchPreferencesServer);
		return searchPreferencesServer;
	}
	/**
	 * This method adds initial Search Preferences to HTTP Session if they are not in the session.
	 * 
	 * @throws NullPointerException
	 * @param httpSession session to add search preferences attribute
	 * @returns SearchPreferencesServer object of the session
	 */
	public static SearchPreferencesServer retriveSessionSearchPreferencesServer(HttpSession httpSession) {
		Object obj;
		if (((obj = httpSession.getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES)) == null) || (!(obj instanceof SearchPreferencesServer))){
			SearchPreferencesServer SearchPreferencesServer = new SearchPreferencesServer();
			SearchPreferencesServer.initPreferences();//this operation will add all excluded statuses including server exclusion always
			httpSession.setAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES, SearchPreferencesServer);
			logger.debug("SearchPreferencesServer not found in the user HTTP session, adding default: " + SearchPreferencesServer);
			return SearchPreferencesServer;
		}
		else {
			logger.debug("SearchPreferencesServer found in the user HTTP session: " + obj);
			return (SearchPreferencesServer)obj;
		}
	}	
}
