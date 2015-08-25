package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ToolOptionsModel;

import java.util.List;

/**
 * Created by lernermh on 8/21/15.
 */
public interface ToolOptionsDAO
{
    ToolOptionsModel getToolOptionsByToolNameAndProperty( String toolName, String property);
    List<ToolOptionsModel> getToolOptionsByProperty( String property);
}
