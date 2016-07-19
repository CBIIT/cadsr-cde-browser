package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;

import gov.nih.nci.cadsr.dao.model.DesignationModel;
import gov.nih.nci.cadsr.dao.model.DesignationModelAlt;

public interface DesignationDAO
{
    List<DesignationModel> getDesignationModelsByAcIdseq( String acIdseq );
    List<DesignationModelAlt> getDesignationModelsNoClsssification( String acIdseq );
    List<DesignationModel> getUsedByDesignationModels( String acIdseq );
    List<String> getAllDesignationModelTypes();
}
