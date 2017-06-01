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
	@RequestMapping(value="/thdec", produces = "application/json")
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
	
	//CDEBROWSER-506 AC 2: (Advanced Search) Add type ahead to the VD Field
	@RequestMapping(value="/thvaluedomain", produces = "application/json")
	public List<String> retrieveTypeaheadSearchValueDomain(@ModelAttribute SearchCriteria searchCriteria, BindingResult bindingResult, HttpSession httpSession)
	{
		//logger.debug("Received request retrieveTypeaheadSearchValueDomain searchCriteria: " + searchCriteria);
		List<String> resList;
        if (bindingResult.hasErrors())
        {
        	logger.error("Error in binding search criteria to the SearchCriteria bean." + bindingResult.getErrorCount() + bindingResult.getAllErrors());
        	return new ArrayList<>();
        }
		resList = typeaheadSearchDAO.buildSearchTypeaheadValueDomain(searchCriteria, null);
		//logger.debug("Response from retrieveTypeaheadSearchValueDomain: " + resList);
		return resList;
	}
	//CDEBROWSER-506 AC 3: (Advanced Search) Add type ahead to the PV (name) Field
	@RequestMapping(value="/thpermissiblevalue", produces = "application/json")
	public List<String> retrieveTypeaheadSearchPermissibleValue(@ModelAttribute SearchCriteria searchCriteria, BindingResult bindingResult, HttpSession httpSession)
	{
		//logger.debug("Received request retrieveTypeaheadSearchPermissibleValue searchCriteria: " + searchCriteria);
		List<String> resList;
        if (bindingResult.hasErrors())
        {
        	logger.error("Error in binding search criteria to the SearchCriteria bean." + bindingResult.getErrorCount() + bindingResult.getAllErrors());
        	return new ArrayList<>();
        }
		resList = typeaheadSearchDAO.buildSearchTypeaheadPermissibleValue(searchCriteria, null);
		//logger.debug("Response from retrieveTypeaheadSearchPermissibleValue: " + resList);
		return resList;
	}
	
	//CDEBROWSER-506 AC 5: (Advanced Search) Add type ahead to the OC Field
	@RequestMapping(value="/thobjectclass", produces = "application/json")
	public List<String> retrieveTypeaheadSearchObjectClass(@ModelAttribute SearchCriteria searchCriteria, BindingResult bindingResult, HttpSession httpSession)
	{
		//logger.debug("Received request buildSearchTypeaheadObjectClass searchCriteria: " + searchCriteria);
		List<String> resList;
        if (bindingResult.hasErrors())
        {
        	logger.error("Error in binding search criteria to the SearchCriteria bean." + bindingResult.getErrorCount() + bindingResult.getAllErrors());
        	return new ArrayList<>();
        }
		resList = typeaheadSearchDAO.buildSearchTypeaheadObjectClass(searchCriteria, null);
		//logger.debug("Response from buildSearchTypeaheadObjectClass: " + resList);
		return resList;
	}
	//CDEBROWSER-506 AC 6: (Advanced Search) Add type ahead to the Property Field
	@RequestMapping(value="/thproperty", produces = "application/json")
	public List<String> retrieveTypeaheadSearchProperty(@ModelAttribute SearchCriteria searchCriteria, BindingResult bindingResult, HttpSession httpSession)
	{
		//logger.debug("Received request retrieveTypeaheadSearchProperty searchCriteria: " + searchCriteria);
		List<String> resList;
        if (bindingResult.hasErrors())
        {
        	logger.error("Error in binding search criteria to the SearchCriteria bean." + bindingResult.getErrorCount() + bindingResult.getAllErrors());
        	return new ArrayList<>();
        }
		resList = typeaheadSearchDAO.buildSearchTypeaheadProperty(searchCriteria, null);
		//logger.debug("Response from retrieveTypeaheadSearchProperty: " + resList);
		return resList;
	}
	//CDEBROWSER-506 AC 4: (Advanced Search) Add type ahead to the Alternate Name Field
	@RequestMapping(value="/thdesignation", produces = "application/json")
	public List<String> retrieveTypeaheadSearchDesignation(@ModelAttribute SearchCriteria searchCriteria, BindingResult bindingResult, HttpSession httpSession)
	{
		//logger.debug("Received request retrieveTypeaheadSearchDesignation searchCriteria: " + searchCriteria);
		List<String> resList;
        if (bindingResult.hasErrors())
        {
        	logger.error("Error in binding search criteria to the SearchCriteria bean." + bindingResult.getErrorCount() + bindingResult.getAllErrors());
        	return new ArrayList<>();
        }
		resList = typeaheadSearchDAO.buildSearchTypeaheadDesignation(searchCriteria, null);
		//logger.debug("Response from retrieveTypeaheadSearchDesignation: " + resList);
		return resList;
	}
}
