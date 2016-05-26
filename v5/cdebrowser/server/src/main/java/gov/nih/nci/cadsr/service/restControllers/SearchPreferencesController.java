/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.model.SearchPreferences;
import gov.nih.nci.cadsr.model.SearchPreferencesServer;
/**
 * This is a class to handle user session search preferences
 * @author asafievan
 *
 */
@RestController
@RequestMapping("/searchPreferences")
public class SearchPreferencesController {
	private static Logger logger = LogManager.getLogger(SearchPreferencesController.class.getName());
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public SearchPreferences retrieveSearchPreferences(HttpServletRequest request) {
		logger.debug("Received request to retrieve search preferences");
		HttpSession httpSession = request.getSession(false);
		Object obj = null;
		if (httpSession != null) {
			obj = httpSession.getAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES);
			if ((obj != null) && (obj instanceof SearchPreferencesServer)) {
				SearchPreferencesServer searchPreferencesServer = (SearchPreferencesServer)obj;
				SearchPreferences searchPreferencesClient = searchPreferencesServer.buildClientSearchPreferences();
				logger.debug("User search preferences found in HTTP session; returning: " + obj);
				return searchPreferencesClient;
			}
		}
		else {
			logger.debug("User HTTP sessionis was not found, creating");
			httpSession = request.getSession(true);
		}
		SearchPreferencesServer searchPreferencesServer = ControllerUtils.initSearchPreferencesServerInSession(httpSession);
		SearchPreferences searchPreferencesToView = searchPreferencesServer.buildClientSearchPreferences();
		logger.debug("User search initial preferences assigned to HTTP session, returning : " + searchPreferencesToView);
		return searchPreferencesToView;
	}
	/**
	 * This method saves user search preferences for search operations.
	 * It there is no request body this is considered to be reset to initial request.
	 * If there is any JSON body even "{}" this is considered as a new search preferences object to use.
	 * 
	 * @param request
	 * @param searchPreferencesClient
	 * @return SearchPreferences
	 */
	@RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public SearchPreferences saveSearchPreferences(HttpServletRequest request, 
			@RequestBody(required=false) SearchPreferences searchPreferencesClient) {
		logger.debug("Received request to save search preferences: " + searchPreferencesClient);
		HttpSession httpSession = request.getSession(true);
		if (searchPreferencesClient != null) {//validate the values received
			searchPreferencesClient.cleanUpClientSearchPreferences();
			SearchPreferencesServer searchPreferencesServer = new SearchPreferencesServer(searchPreferencesClient);
			httpSession.setAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES, searchPreferencesServer);
			logger.debug("User session search preferences are updated to: " + searchPreferencesClient);
		}
		else {
			SearchPreferencesServer searchPreferencesServer = ControllerUtils.initSearchPreferencesServerInSession(httpSession);
			searchPreferencesClient = searchPreferencesServer.buildClientSearchPreferences();
			logger.debug("User search initial preferences sent: " + searchPreferencesClient);	
		}
		return searchPreferencesClient;
	}
	@RequestMapping(value = "/default", method = RequestMethod.GET, produces = "application/json")
	public SearchPreferences retrieveSearchPreferencesDefault() {
		logger.debug("Received request to retrieve default search preferences");
		SearchPreferences searchPreferences = new SearchPreferences();
		searchPreferences.initPreferences();
		logger.debug("User search initial preferences assigned to HTTP session, returning : " + searchPreferences);
		return searchPreferences;
	}
	@RequestMapping(value = "/reset", method = RequestMethod.POST, produces = "application/json")
	public SearchPreferences resetSearchPreferencesToDefault(HttpSession httpSession) {
		logger.debug("Received request to reset search preferences to default");
		SearchPreferencesServer searchPreferencesServer = ControllerUtils.initSearchPreferencesServerInSession(httpSession);
		SearchPreferences searchPreferencesClient = searchPreferencesServer.buildClientSearchPreferences();
		logger.debug("User search preferences reset to: " + searchPreferencesClient);	
		return searchPreferencesClient;
	}
}
