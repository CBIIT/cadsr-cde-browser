package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ConceptModel;

import java.util.List;

public interface ObjectClassConceptDAO
{
    List<ConceptModel> getObjectClassConceptByDecIdseq( String decIdseq );
}
