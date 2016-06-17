package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;

import gov.nih.nci.cadsr.dao.model.DefinitionModel;
import gov.nih.nci.cadsr.dao.model.DefinitionModelAlt;

public interface DefinitionDAO
{
    DefinitionModel getDefinitionByDefinIdseq( String definIdseq );

    List<DefinitionModel> getAllDefinitionsByAcIdseq( String acIdseq );
    List<DefinitionModelAlt> getAllDefinitionsNoClassification( String acIdseq );
}
