package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2017 Leidos Biomedical Research, Inc.
 */



import gov.nih.nci.cadsr.dao.model.RegistrationStatusModel;
import gov.nih.nci.cadsr.dao.model.WorkflowStatusModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorkflowStatusDAOImpl extends AbstractDAOOperations implements WorkflowStatusDAO
{

    private Logger logger = LogManager.getLogger( WorkflowStatusDAOImpl.class.getName() );

    private JdbcTemplate jdbcTemplate;

    @Autowired
    WorkflowStatusDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }
    /**
     * This is to use in unit tests only
     * @param jdbcTemplate
     */
    protected WorkflowStatusDAOImpl(JdbcTemplate jdbcTemplate)
    {
    	this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public List<WorkflowStatusModel> getAllWorkflowStatuses( )
    {
        String sql = "SELECT * from SBR.AC_STATUS_LOV where display_order IS NOT NULL and asl_name <> 'RETIRED DELETED'";

        List<WorkflowStatusModel> workflowStatusModelList = jdbcTemplate.query( sql, new Object[]{ }, new WorkflowStatusMapper(WorkflowStatusModel.class) );
        return workflowStatusModelList;
    }
    
    
    public List<String> getWorkflowStatusesAsList( )
    {
    	List<WorkflowStatusModel> workflowStatusModelList = getAllWorkflowStatuses();
    	
    	Collections.sort(workflowStatusModelList, (o1, o2) -> o1.getDisplayOrder() - o2.getDisplayOrder());    	
    	
        List<String> workflowStatusesList = new ArrayList<String>();
        for (WorkflowStatusModel wsm : workflowStatusModelList) {
        	workflowStatusesList.add(wsm.getAslName());
		}
        return workflowStatusesList;
    }        


	public final class WorkflowStatusMapper extends BeanPropertyRowMapper<WorkflowStatusModel>
    {
        private Logger logger = LogManager.getLogger( WorkflowStatusMapper.class.getName() );
        
        public WorkflowStatusMapper( Class<WorkflowStatusModel> mappedClass )
        {
            super( mappedClass );
        }        

        public WorkflowStatusModel mapRow( ResultSet rs, int rowNum ) throws SQLException
        {
        	WorkflowStatusModel workflowStatusModel = super.mapRow( rs, rowNum );
        	        	
        	workflowStatusModel.setAslName( rs.getString( "ASL_NAME" ) );
        	workflowStatusModel.setDesc( rs.getString( "DESCRIPTION" ) );
        	workflowStatusModel.setComments( rs.getString( "COMMENTS" ) );
        	workflowStatusModel.setDisplayOrder( rs.getInt( "DISPLAY_ORDER" ) );
            workflowStatusModel.setCreatedBy( rs.getString( "CREATED_BY" ) );
            workflowStatusModel.setDateCreated( rs.getTimestamp( "DATE_CREATED" ) );             
            workflowStatusModel.setDateModified( rs.getTimestamp( "DATE_MODIFIED" ) );
            return workflowStatusModel;
        }
    }
}
