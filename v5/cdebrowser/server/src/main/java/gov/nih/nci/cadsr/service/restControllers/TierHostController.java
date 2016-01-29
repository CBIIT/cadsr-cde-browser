package gov.nih.nci.cadsr.service.restControllers;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.ToolOptionsDAOImpl;
import gov.nih.nci.cadsr.dao.model.ToolOptionsModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Get the URL for other tools, from this tier's database
 */
@RestController
public class TierHostController
{
    private ToolOptionsDAOImpl toolOptionsDAO;

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

    public ToolOptionsDAOImpl getToolOptionsDAO()
    {
        return toolOptionsDAO;
    }

    public void setToolOptionsDAO( ToolOptionsDAOImpl toolOptionsDAO )
    {
        this.toolOptionsDAO = toolOptionsDAO;
    }
}
