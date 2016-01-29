package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.DesignationModel;

import java.util.List;

public interface DesignationDAO
{
    List<DesignationModel> getDesignationModelsByAcIdseq( String acIdseq );
    List<DesignationModel> getUsedByDesignationModels( String acIdseq );
}
