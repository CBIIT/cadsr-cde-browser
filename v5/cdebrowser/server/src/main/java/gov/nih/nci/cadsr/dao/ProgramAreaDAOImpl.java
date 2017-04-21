package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ProgramAreaModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class ProgramAreaDAOImpl extends AbstractDAOOperations implements ProgramAreaDAO
{
    private Logger logger = LogManager.getLogger( ProtocolDAOImpl.class.getName() );
    private JdbcTemplate jdbcTemplate;

    @Autowired
    ProgramAreaDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }

    //this is for Unit tests
    protected ProgramAreaDAOImpl(JdbcTemplate jdbcTemplate) {
		super();
		super.setJdbcTemplate(jdbcTemplate);
	}

	@Override
    public List<ProgramAreaModel> getAllProgramAreas()
    {
        List<ProgramAreaModel> result;
        String sql = " SELECT * FROM program_areas_lov_view";
        //CDEBROWSER-768 If the Program Area list is empty instead of returning the error to the client the server tries to populate it from DB again, and return the error only if not successful again.
        try {
	        result = getAll( sql, ProgramAreaModel.class );
        }
        catch (Exception e) {
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
			}
        	result = getAll( sql, ProgramAreaModel.class );//second retry if got an error the first time; we go not catch the second exception if it happen
        }
        if (CollectionUtils.isEmpty(result)) {//another retry if returned empty
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
			}
            try {
    	        result = getAll( sql, ProgramAreaModel.class );
            }
            catch (Exception e) {
            	try {
    				Thread.sleep(1000);
    			} catch (InterruptedException e1) {
    			}
            	result = getAll( sql, ProgramAreaModel.class );
            }
        }
        //logger.debug( "Done getAllProgramAreas" );
        return result;
    }
}
