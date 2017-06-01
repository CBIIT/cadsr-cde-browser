/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao;

import java.util.List;

import gov.nih.nci.cadsr.model.SearchPreferencesServer;
import gov.nih.nci.cadsr.service.model.search.SearchCriteria;
/**
 * This is an interface to retrieve data for Typeahead.
 * CDEBROWSER-808 CDEBROWSER-506
 * @author asafievan
 *
 */
public interface TypeaheadSearchDAO {
	List<String> buildSearchTypeaheadByNameAndDomain(SearchCriteria searchCriteria, SearchPreferencesServer searchPreferencesServer);
	List<String> buildSearchTypeaheadDec(SearchCriteria searchCriteria, SearchPreferencesServer searchPreferencesServer);
	List<String> buildSearchTypeaheadValueDomain(SearchCriteria searchCriteria, SearchPreferencesServer searchPreferencesServer);
	List<String> buildSearchTypeaheadPermissibleValue(SearchCriteria searchCriteria, SearchPreferencesServer searchPreferencesServer);
	List<String> buildSearchTypeaheadObjectClass(SearchCriteria searchCriteria, SearchPreferencesServer searchPreferencesServer);
	List<String> buildSearchTypeaheadProperty(SearchCriteria searchCriteria, SearchPreferencesServer searchPreferencesServer);
	List<String> buildSearchTypeaheadDesignation(SearchCriteria searchCriteria, SearchPreferencesServer searchPreferencesServer);
}
