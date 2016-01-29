package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ToolOptionsModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

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
    public ToolOptionsModel getToolOptionsByToolNameAndProperty( String toolName, String property )
    {
        String sql = "SELECT * FROM tool_options_ext WHERE tool_name = \'" + toolName + "\' AND property = ?";

        ToolOptionsModel toolOptionsModel = query( sql, property, ToolOptionsModel.class );

        return toolOptionsModel;

    }

    @Override
    public List<ToolOptionsModel> getToolOptionsByProperty( String property )
    {
        String sql = "SELECT * FROM tool_options_ext WHERE property = ?";

        List<ToolOptionsModel> toolOptionsModels = getAll( sql, property, ToolOptionsModel.class );

        return toolOptionsModels;

    }
}
