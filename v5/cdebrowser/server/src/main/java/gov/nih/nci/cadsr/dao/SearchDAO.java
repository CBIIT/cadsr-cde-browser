package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;

import gov.nih.nci.cadsr.dao.model.SearchModel;
import gov.nih.nci.cadsr.model.SearchPreferences;
import gov.nih.nci.cadsr.service.model.search.SearchCriteria;

public interface SearchDAO
{
	public List<SearchModel> cdeOwnedAndUsedByContext( String conteId, SearchPreferences searchPreferences );
	
	public List<SearchModel> getAllContexts(SearchCriteria searchCriteria, SearchPreferences searchPreferences);
	
	public List<SearchModel> cdeByContextClassificationSchemeItem( String classificationSchemeItemId, SearchPreferences searchPreferences );
	
	public List<SearchModel> cdeByProtocolForm( String id );
	
	public List<SearchModel> cdeByProtocol( String protocolId, SearchPreferences searchPreferences );
	
	public List<SearchModel> cdeByContextClassificationScheme( String classificationSchemeId, SearchPreferences searchPreferences );
}
