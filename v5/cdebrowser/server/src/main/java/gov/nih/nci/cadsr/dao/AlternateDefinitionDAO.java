package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.AlternateDefinitionModel;
import gov.nih.nci.cadsr.dao.model.AlternateDefinitionUiModel;

import java.util.List;

public interface AlternateDefinitionDAO
{
    List<AlternateDefinitionModel> getAlternateDefinitionsByAcIdseq( String acIdseq );
    List<AlternateDefinitionUiModel> getUiAlternateDefinitionsByAcIdseq( String acIdseq );
}
