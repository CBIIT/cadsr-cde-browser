package gov.nih.nci.cadsr.service.restControllers;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.dao.ToolOptionsDAO;
import gov.nih.nci.cadsr.dao.model.ToolOptionsModel;
import gov.nih.nci.cadsr.error.RestControllerException;

/**
 * Get the URL for other tools, from this tier's database
 */
@RestController
public class TierHostController
{
	private Logger logger = LogManager.getLogger(TierHostController.class.getName());
	
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
    
    @RequestMapping( value = "/getPwcsURL", produces = "text/plain")
    public String getPwcsUrl() throws RestControllerException
    {
    	String pwcsUrl = "";
        try
        {
			ToolOptionsModel cdeBrowserOptions = getToolOptionsDAO().getToolOptionsByToolNameAndProperty(CaDSRConstants.TOOL_NAME, CaDSRConstants.PWCS_PROPERTY );
			if (cdeBrowserOptions != null)
				pwcsUrl = cdeBrowserOptions.getValue();
		} catch (Exception e) {
			String errMsg = "Error in fetching PWCS URL property: ";
			logger.error(errMsg, e);
			throw new RestControllerException(errMsg + e.getMessage());
		}
        return pwcsUrl;
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
