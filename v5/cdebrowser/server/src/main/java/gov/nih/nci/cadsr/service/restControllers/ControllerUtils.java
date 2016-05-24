/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.model.SearchPreferences;

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
	public static SearchPreferences initSearchPreferencesInSession(HttpSession httpSession) {
		SearchPreferences searchPreferences = new SearchPreferences();
		searchPreferences.initPreferences();
		httpSession.setAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES, searchPreferences);
		logger.debug("SearchPreferences adding default to the user HTTP session: " + searchPreferences);
		return searchPreferences;
	}
	/**
	 * This method adds initial Search Preferences to HTTP Session if they are not in the session.
	 * 
	 * @throws NullPointerException
	 * @param httpSession session to add search preferences attribute
	 * @returns SearchPreferences object of the session
	 */
	public static SearchPreferences retriveSessionSearchPreferences(HttpSession httpSession) {
		Object obj;
		if (((obj = httpSession.getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES)) == null) || (!(obj instanceof SearchPreferences))){
			SearchPreferences searchPreferences = new SearchPreferences();
			searchPreferences.initPreferences();
			httpSession.setAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES, searchPreferences);
			logger.debug("SearchPreferences not found in the user HTTP session, adding default: " + searchPreferences);
			return searchPreferences;
		}
		else {
			logger.debug("SearchPreferences found in the user HTTPO session: " + obj);
			return (SearchPreferences)obj;
		}
	}	
}
