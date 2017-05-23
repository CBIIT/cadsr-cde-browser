/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.dao.TypeaheadSearchDAO;
import gov.nih.nci.cadsr.dao.operation.SearchQueryBuilder;
import gov.nih.nci.cadsr.model.SearchPreferencesServer;
import gov.nih.nci.cadsr.service.model.search.SearchCriteria;

@RestController
@RequestMapping("/typeahead")
public class TypeaheadSearchController {
	private static Logger logger = LogManager.getLogger(TypeaheadSearchController.class.getName());
	public static final int MODE_EXACT_PHRASE = 0;
	@Autowired
    protected TypeaheadSearchDAO typeaheadSearchDAO;
	
	@RequestMapping(value="/full", produces = "application/json")
	public List<String> retrieveTypeaheadSearchFull(@ModelAttribute SearchCriteria searchCriteria, BindingResult bindingResult, HttpSession httpSession)
	{
		//logger.debug("Received request retrieveTypeaheadSearchLongName searchCriteria: " + searchCriteria);
		List<String> resList;
        if (bindingResult.hasErrors())
        {
        	logger.error("Error in binding search criteria to the SearchCriteria bean." + bindingResult.getErrorCount() + bindingResult.getAllErrors());
        	return new ArrayList<>();
        }
        //we use workflow status and excluded contexts from user preferences in typeahead
        SearchPreferencesServer searchPreferencesServer = ControllerUtils.retriveSessionSearchPreferencesServer(httpSession);
		resList = typeaheadSearchDAO.buildSearchTypeaheadByNameAndDomain(searchCriteria, searchPreferencesServer);
		//logger.debug("Response from retrieveTypeaheadSearchLongName: " + resList);
		return resList;
	}
	
	//CDEBROWSER-506 AC 1: (Advanced Search) Add type ahead to the DEC Field
	@RequestMapping(value="/dec", produces = "application/json")
	public List<String> retrieveTypeaheadSearchDEC(@ModelAttribute SearchCriteria searchCriteria, BindingResult bindingResult, HttpSession httpSession)
	{
		//logger.debug("Received request retrieveTypeaheadSearchDEC searchCriteria: " + searchCriteria);
		List<String> resList;
        if (bindingResult.hasErrors())
        {
        	logger.error("Error in binding search criteria to the SearchCriteria bean." + bindingResult.getErrorCount() + bindingResult.getAllErrors());
        	return new ArrayList<>();
        }
		resList = typeaheadSearchDAO.buildSearchTypeaheadDec(searchCriteria, null);
		//logger.debug("Response from retrieveTypeaheadSearchDEC: " + resList);
		return resList;
	}
}
