/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.common.RegistrationStatusExcludedInitial;
import gov.nih.nci.cadsr.common.WorkflowStatusExcludedInitial;
import gov.nih.nci.cadsr.model.SearchPreferences;
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
		ControllerUtils.initSearchPreferencesInSession(httpSession);
		logger.debug("User search initial preferences assigned to HTTP session, returning : " + searchPreferences);
		return searchPreferences;
	}

	@RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public SearchPreferences saveSearchPreferences(HttpServletRequest request, 
			@RequestBody(required=false) SearchPreferences searchPreferences) {
		logger.debug("Received request to save search preferences: " + searchPreferences);
		HttpSession httpSession = request.getSession(true);
		if (searchPreferences != null) {//FIXME validate the values received
			cleanUpSearchPreferences(searchPreferences);
			httpSession.setAttribute(CaDSRConstants.USER_SEARCH_PREFERENCES, searchPreferences);
			logger.debug("User session search preferences are updated to: " + searchPreferences);
		}
		else {
			ControllerUtils.initSearchPreferencesInSession(httpSession);
			logger.debug("User search initial preferences assigned to HTTP session: " + searchPreferences);			
		}
		return searchPreferences;
	}

	private void cleanUpSearchPreferences(SearchPreferences searchPreferences) {
		searchPreferences.setWorkflowStatusExcluded(
			WorkflowStatusExcludedInitial.buildValidStatusList(searchPreferences.getWorkflowStatusExcluded()));
		searchPreferences.setRegistrationStatusExcluded(
				RegistrationStatusExcludedInitial.buildValidStatusList(searchPreferences.getRegistrationStatusExcluded()));
	}
	
}