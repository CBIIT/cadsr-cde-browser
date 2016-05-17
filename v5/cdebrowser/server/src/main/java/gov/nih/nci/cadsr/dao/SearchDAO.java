package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;

import gov.nih.nci.cadsr.dao.model.SearchModel;
import gov.nih.nci.cadsr.service.model.search.SearchCriteria;

public interface SearchDAO
{
	public List<SearchModel> cdeOwnedAndUsedByContext( String conteId );
	
	public List<SearchModel> getAllContexts(SearchCriteria searchCriteria);
	
	public List<SearchModel> cdeByContextClassificationSchemeItem( String classificationSchemeItemId );
	
	public List<SearchModel> cdeByProtocolForm( String id );
	
	public List<SearchModel> cdeByProtocol( String protocolId );
	
	public List<SearchModel> cdeByContextClassificationScheme( String classificationSchemeId );
}
