package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ClassificationSchemeModel;
import gov.nih.nci.cadsr.service.model.cdeData.classifications.ClassificationScheme;

import java.util.List;

public interface ClassificationSchemeDAO
{
    public List<ClassificationSchemeModel> getAllClassificationSchemes();
    public List<ClassificationSchemeModel> getClassificationSchemes( String conteId );
    public List<ClassificationSchemeModel> getChildrenClassificationSchemesByCsId( String csId );
    public ClassificationSchemeModel getClassificationSchemeById( String contextId );
    
    public List<ClassificationScheme> getAllClassificationSchemeWithProgramAreaAndContext(String contexIdSeq, String csOrCsCsi);
    
    public boolean haveClassificationSchemes( String conteId );
}
