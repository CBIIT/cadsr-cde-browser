package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ToolOptionsModel;

import java.util.List;

public interface ToolOptionsDAO
{
    ToolOptionsModel getToolOptionsByToolNameAndProperty( String toolName, String property );
    List<ToolOptionsModel> getToolOptionsByProperty( String property );
    public List<ToolOptionsModel> getHhsWarningMsg();
}
