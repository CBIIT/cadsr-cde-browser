/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.model.SearchPreferences;
/**
 * This is a class to handle user session search preferences
 * @author asafievan
 *
 */
@RestController
@RequestMapping(value="/searchPreferences", produces = "application/json")
public class SearchPreferencesController {
	private static Logger logger = LogManager.getLogger(SearchPreferencesController.class.getName());
	@RequestMapping(method = RequestMethod.GET)
	public SearchPreferences retrieveSearchPreferences(HttpServletRequest request) {
		logger.debug("Received request to retrieve search preferences");
		SearchPreferences searchPreferences = null;
		HttpSession httpSession = request.getSession(false);
		if (httpSession != null) {
			Object obj = httpSession.getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES);
			if ((obj != null) && (obj instanceof SearchPreferences)) {
				logger.debug("User search preferences found in HTTP session; returning: " + obj);
				return (SearchPreferences) obj;
			}
		}
		else {
			logger.debug("User HTTP sessionis was not found, creating");
			httpSession = request.getSession(true);
		}
		searchPreferences = new SearchPreferences();
		logger.debug("User search initial preferences assigned to HTTP session, returning : " + searchPreferences);
		httpSession.setAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES, searchPreferences);
		return searchPreferences;
	}
}
