package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import gov.nih.nci.cadsr.dao.model.ToolOptionsModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;

public class ToolOptionsDAOImpl extends AbstractDAOOperations implements ToolOptionsDAO
{
    private Logger logger = LogManager.getLogger( ToolOptionsDAOImpl.class.getName() );

    private JdbcTemplate jdbcTemplate;

    @Autowired
    ToolOptionsDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }


    @Override
    public ToolOptionsModel getToolOptionsByToolNameAndProperty(String toolName, String property)
    {
    	if ((StringUtils.isNotEmpty(toolName)) && (StringUtils.isNotEmpty(property))) {
	    	String sql = "SELECT * FROM tool_options_ext WHERE tool_name = ? AND property = ?";
	
	        List<ToolOptionsModel> toolOptionsModel = getAll( sql, new Object[]{toolName, property}, ToolOptionsModel.class );
	       
	        if (toolOptionsModel.size() > 0) {
	        	return toolOptionsModel.get(0);
	        }
    	}
        return new ToolOptionsModel();
    }

    @Override
    public List<ToolOptionsModel> getToolOptionsByProperty( String property )
    {
        String sql = "SELECT * FROM tool_options_ext WHERE property = ?";

        List<ToolOptionsModel> toolOptionsModels = getAll( sql, property, ToolOptionsModel.class );

        return toolOptionsModels;

    }
    
    public List<ToolOptionsModel> getHhsWarningMsg()
    {
        String sql = "SELECT * FROM tool_options_ext WHERE TOOL_NAME = 'caDSR' and PROPERTY = 'WARNING.BANNER'";

        List<ToolOptionsModel> toolOptionsModels = getAll( sql, ToolOptionsModel.class );
        return toolOptionsModels;

    }    
}
