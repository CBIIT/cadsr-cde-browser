package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;

import gov.nih.nci.cadsr.dao.model.SearchModel;

public interface SearchDAO
{
	public List<SearchModel> cdeOwnedAndUsedByContext( String conteId );
	
	public List<SearchModel> getAllContexts(
            String clientQuery, String clientSearchMode, int clientSearchField,
            String programArea, String context, String classification, String protocol,
            String workFlowStatus, String registrationStatus,
            String conceptName, String conceptCode );
	
	public List<SearchModel> cdeByContextClassificationSchemeItem( String classificationSchemeItemId );
	
	public List<SearchModel> cdeByProtocolForm( String id );
	
	public List<SearchModel> cdeByProtocol( String protocolId );
	
	public List<SearchModel> cdeByContextClassificationScheme( String classificationSchemeId );
}
