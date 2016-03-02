package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ConceptModel;

import java.util.List;

interface RepresentationConceptsDAO
{
    public List<ConceptModel> getRepresentationConceptByRepresentationId( String representationId );
    public List<ConceptModel> getRepresentationConceptByRepresentationId( int representationId );
}