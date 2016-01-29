package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.DefinitionModel;

import java.util.List;

public interface DefinitionDAO
{
    DefinitionModel getDefinitionByDefinIdseq( String definIdseq );

    List<DefinitionModel> getAllDefinitionsByAcIdseq( String acIdseq );
}
