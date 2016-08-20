package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.AlternateNameModel;
import gov.nih.nci.cadsr.dao.model.AlternateNameUiModel;

import java.util.List;


public interface AlternateNameDAO
{
    List<AlternateNameModel> getAlternateNamesByAcIdseq( String acIdseq );
    List<AlternateNameUiModel> getUiAlternateNamesByAcIdseq( String acIdseq );

}
