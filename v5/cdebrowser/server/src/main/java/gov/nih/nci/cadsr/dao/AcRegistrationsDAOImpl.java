package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.AcRegistrationsModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class AcRegistrationsDAOImpl extends AbstractDAOOperations implements AcRegistrationsDAO
{
    private Logger logger = LogManager.getLogger( AcRegistrationsDAOImpl.class.getName() );

    private JdbcTemplate jdbcTemplate;

    @Autowired
    AcRegistrationsDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }

    /**
     * Since we only need the registration status string out of this object, don't bother with a rowmapper
     *
     * @param acIdseq the id of the Administered Component
     * @return
     */
    @Override
    public AcRegistrationsModel getAcRegistrationByAcIdseq( String acIdseq )
    {
        String sql = "SELECT * FROM sbr.ac_registrations WHERE ac_idseq = ?";
        AcRegistrationsModel acRegistrationsModel = jdbcTemplate.queryForObject( sql, new Object[]{ acIdseq }, new BeanPropertyRowMapper<>( AcRegistrationsModel.class ) );
        return acRegistrationsModel;
    }
}
