/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.common.util.StringUtilities;
import gov.nih.nci.cadsr.dao.TypeaheadSearchDAO;

@RestController
@RequestMapping("/typeahead")
public class TypeaheadSearchController {
	private static Logger logger = LogManager.getLogger(TypeaheadSearchController.class.getName());
	
	@Autowired
    private TypeaheadSearchDAO typeaheadSearchDAO;


	@RequestMapping(value="/longname", produces = "application/json")
	public List<String> retrieveTypeaheadSearchLongName(@RequestParam("name") String searchPattern)
	{
		logger.debug("Received request retrieveTypeaheadSearchLongName searchPattern: " + searchPattern);
		String searchFilter = searchPattern.replaceAll("(\\*|\\%)", "");
		searchFilter = StringUtilities.sanitizeForSql(searchFilter);
		logger.debug("Received request retrieveTypeaheadSearchLongName after sanitize searchFilter: " + searchFilter);
		List<String> resList = typeaheadSearchDAO.getSearchTypeaheadLongName(searchFilter);
		return resList;
	}
	@RequestMapping(value="/longnamefull", produces = "application/json")
	public List<String> retrieveTypeaheadSearchLongNameFull(@RequestParam("name") String searchPattern)
	{
		logger.debug("Received request retrieveTypeaheadSearchLongName searchPattern: " + searchPattern);
		String searchFilter = StringUtilities.removeSqlWildCards(searchPattern);
		searchFilter = StringUtilities.sanitizeForSql(searchFilter);
		logger.debug("Received request retrieveTypeaheadSearchLongName after sanitize searchFilter: " + searchFilter);
		List<String> resList = typeaheadSearchDAO.getSearchTypeaheadLongNameFull(searchFilter);
		return resList;
	}
}
