/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ClassificationSchemeModel;

import java.util.List;

public interface ClassificationSchemeDAO
{
    public List<ClassificationSchemeModel> getAllClassificationSchemes();
    public List<ClassificationSchemeModel> getClassificationSchemes( String conteId );
    public List<ClassificationSchemeModel> getChildrenClassificationSchemesByCsId( String csId );
    public ClassificationSchemeModel getClassificationSchemeById( String contextId );
}
