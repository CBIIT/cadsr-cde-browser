package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ProgramAreaModel;

import java.util.List;

public interface ProgramAreaDAO
{
    public List<ProgramAreaModel> getAllProgramAreas();
}
