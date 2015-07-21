/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ProgramAreaModel;

import java.util.List;

public interface ProgramAreaDAO
{
    public List<ProgramAreaModel> getAllProgramAreas();
}
