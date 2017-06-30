package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2017 Leidos Biomedical Research, Inc.
 */



import gov.nih.nci.cadsr.dao.model.RegistrationStatusModel;
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

public class RegistrationStatusDAOImpl extends AbstractDAOOperations implements RegistrationStatusDAO
{

    private Logger logger = LogManager.getLogger( RegistrationStatusDAOImpl.class.getName() );

    private JdbcTemplate jdbcTemplate;

    @Autowired
    RegistrationStatusDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }
    /**
     * This is to use in unit tests only
     * @param jdbcTemplate
     */
    protected RegistrationStatusDAOImpl(JdbcTemplate jdbcTemplate)
    {
    	this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public List<RegistrationStatusModel> getAllRegnStatuses( )
    {
        String sql = "SELECT * from SBR.REG_STATUS_LOV";

        List<RegistrationStatusModel> registrationStatusModelList = jdbcTemplate.query( sql, new Object[]{ }, new RegistrationStatusMapper(RegistrationStatusModel.class) );
        return registrationStatusModelList;
    }
    
    public List<String> getRegnStatusesAsList( )
    {
    	List<RegistrationStatusModel> registrationStatusModelList = getAllRegnStatuses();
    	
    	Collections.sort(registrationStatusModelList, (o1, o2) -> o1.getDisplayOrder() - o2.getDisplayOrder());    	
    	
        List<String> registrationStatusesList = new ArrayList<String>();
        for (RegistrationStatusModel rsm : registrationStatusModelList) {
        	registrationStatusesList.add(rsm.getRegistrationStatusName());
		}
        registrationStatusesList.add("");
        return registrationStatusesList;
    }    
    


	public final class RegistrationStatusMapper extends BeanPropertyRowMapper<RegistrationStatusModel>
    {
        private Logger logger = LogManager.getLogger( RegistrationStatusMapper.class.getName() );
        
        public RegistrationStatusMapper( Class<RegistrationStatusModel> mappedClass )
        {
            super( mappedClass );
        }        

        public RegistrationStatusModel mapRow( ResultSet rs, int rowNum ) throws SQLException
        {
        	RegistrationStatusModel registrationStatusModel = super.mapRow( rs, rowNum );
        	        	
        	registrationStatusModel.setRegistrationStatusName( rs.getString( "REGISTRATION_STATUS" ) );
        	registrationStatusModel.setDesc( rs.getString( "DESCRIPTION" ) );
        	registrationStatusModel.setComments( rs.getString( "COMMENTS" ) );
        	registrationStatusModel.setDisplayOrder( rs.getInt( "DISPLAY_ORDER" ) );
            registrationStatusModel.setCreatedBy( rs.getString( "CREATED_BY" ) );
            registrationStatusModel.setDateCreated( rs.getTimestamp( "DATE_CREATED" ) );             
            registrationStatusModel.setDateModified( rs.getTimestamp( "DATE_MODIFIED" ) );
            return registrationStatusModel;
        }
    }
}
