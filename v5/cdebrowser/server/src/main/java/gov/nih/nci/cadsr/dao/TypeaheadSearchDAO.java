/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao;

import java.util.List;

import gov.nih.nci.cadsr.service.model.search.SearchCriteria;
/**
 * This is an interface to retrieve data for Typeahead.
 * CDEBROWSER-808
 * @author asafievan
 *
 */
public interface TypeaheadSearchDAO {
	List<String> getSearchTypeaheadLongName(String pattern);
	List<String> getSearchTypeaheadLongNameFull(String pattern);
	List<String> buildSearchTypeaheadByNameAndDomain(SearchCriteria searchCriteria);
}
