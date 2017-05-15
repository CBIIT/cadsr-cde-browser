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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.common.util.StringReplace;
import gov.nih.nci.cadsr.common.util.StringUtilities;
import gov.nih.nci.cadsr.dao.TypeaheadSearchDAO;
import gov.nih.nci.cadsr.dao.operation.SearchQueryBuilder;
import gov.nih.nci.cadsr.model.SearchPreferencesServer;
import gov.nih.nci.cadsr.service.model.search.SearchCriteria;
import gov.nih.nci.cadsr.service.search.ProcessConstants;

@RestController
@RequestMapping("/typeahead")
public class TypeaheadSearchController {
	private static Logger logger = LogManager.getLogger(TypeaheadSearchController.class.getName());
	public static final int MODE_EXACT_PHRASE = 0;
	@Autowired
    private TypeaheadSearchDAO typeaheadSearchDAO;
	@Autowired
	private SearchQueryBuilder searchQueryBuilder;
	//TODO remove this method initial attempt
//	@RequestMapping(value="/longname", produces = "application/json")
//	public List<String> retrieveTypeaheadSearchLongName(@RequestParam("name") String searchPattern)
//	{
//		logger.debug("Received request retrieveTypeaheadSearchLongName searchPattern: " + searchPattern);
//		String searchFilter = searchPattern.replaceAll("(\\*|\\%)", "");
//		searchFilter = StringUtilities.sanitizeForSql(searchFilter);
//		logger.debug("Received request retrieveTypeaheadSearchLongName after sanitize searchFilter: " + searchFilter);
//		List<String> resList = typeaheadSearchDAO.getSearchTypeaheadLongName(searchFilter);
//		return resList;
//	}
	//TODO remove this method
//	@RequestMapping(value="/longnamefull", produces = "application/json")
//	public List<String> retrieveTypeaheadSearchLongNameFull(@RequestParam("name") String searchPattern)
//	{
//		logger.debug("Received request retrieveTypeaheadSearchLongName searchPattern: " + searchPattern);
//		String searchFilter = StringUtilities.removeSqlWildCards(searchPattern);
//		searchFilter = StringUtilities.sanitizeForSql(searchFilter);
//		logger.debug("Received request retrieveTypeaheadSearchLongName after sanitize searchFilter: " + searchFilter);
//		List<String> resList = typeaheadSearchDAO.getSearchTypeaheadLongNameFull(searchFilter);
//		return resList;
//	}
	@RequestMapping(value="/full", produces = "application/json")
	public List<String> retrieveTypeaheadSearchFull(@ModelAttribute SearchCriteria searchCriteria, BindingResult bindingResult, HttpSession httpSession)
	{
		logger.debug("Received request retrieveTypeaheadSearchLongName searchCriteria: " + searchCriteria);
		List<String> resList;
        if (bindingResult.hasErrors())
        {
        	logger.error("Error in binding search criteria to the SearchCriteria bean." + bindingResult.getErrorCount() + bindingResult.getAllErrors().get(0));
        	return new ArrayList<>();
        }
        //we use workflow status and excluded contexts from user preferences in typeahead
        SearchPreferencesServer searchPreferencesServer = ControllerUtils.retriveSessionSearchPreferencesServer(httpSession);
		resList = typeaheadSearchDAO.buildSearchTypeaheadByNameAndDomain(searchCriteria, searchPreferencesServer);
		//logger.debug("Response from retrieveTypeaheadSearchLongName: " + resList);
		return resList;
	}

}
