package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ClassificationSchemeModel;

import java.util.List;

public interface ClassificationSchemeDAO
{
    public List<ClassificationSchemeModel> getAllClassificationSchemes();
    public List<ClassificationSchemeModel> getClassificationSchemes( String conteId );
    public List<ClassificationSchemeModel> getChildrenClassificationSchemesByCsId( String csId );
    public ClassificationSchemeModel getClassificationSchemeById( String contextId );
}
