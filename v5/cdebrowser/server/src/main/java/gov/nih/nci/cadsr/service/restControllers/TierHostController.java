package gov.nih.nci.cadsr.service.restControllers;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.dao.ToolOptionsDAO;
import gov.nih.nci.cadsr.dao.model.ToolOptionsModel;

/**
 * Get the URL for other tools, from this tier's database
 */
@RestController
public class TierHostController
{
    @Autowired
    private ToolOptionsDAO toolOptionsDAO;

    @RequestMapping( value = "/getToolHost" )
    @ResponseBody
    public String getToolHost( String tool )
    {
        ToolOptionsModel formBuilderOptions = getToolOptionsDAO().getToolOptionsByToolNameAndProperty( tool, "URL" );
        String host = formBuilderOptions.getValue();
        return host;
    }

    @RequestMapping( value = "/getAllToolHost" )
    @ResponseBody
    public List<ToolOptionsModel> getAllToolHost()
    {
        List<ToolOptionsModel> toolOptions = getToolOptionsDAO().getToolOptionsByProperty( "URL" );
        return toolOptions;
    }

    public ToolOptionsDAO getToolOptionsDAO()
    {
        return toolOptionsDAO;
    }

    public void setToolOptionsDAO( ToolOptionsDAO toolOptionsDAO )
    {
        this.toolOptionsDAO = toolOptionsDAO;
    }
}
