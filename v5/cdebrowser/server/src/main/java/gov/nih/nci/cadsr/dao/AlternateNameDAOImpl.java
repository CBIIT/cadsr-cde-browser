package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.AlternateNameModel;
import gov.nih.nci.cadsr.dao.model.AlternateNameUiModel;
import gov.nih.nci.cadsr.dao.model.ConceptModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class AlternateNameDAOImpl  extends AbstractDAOOperations implements AlternateNameDAO
{
    private Logger logger = LogManager.getLogger( AlternateNameDAOImpl.class.getName() );
    private JdbcTemplate jdbcTemplate;
    private ContextDAO contextDAO;

    @Autowired
    AlternateNameDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public List<AlternateNameModel> getAlternateNamesByAcIdseq( String acIdseq )
    {
        String sql = "select * from CABIO31_DESIGNATIONS_VIEW alternateDefinitions where AC_IDSEQ = ?";
        //logger.debug( sql.replace( "?", acIdseq) + " <<<<<<<" );

        return getAll( sql, acIdseq, AlternateNameModel.class );
    }


    /**
     * This query only get's the fields that are needed for the UI, it populates AlternateNameUiModel (the UI only, version of AlternateNameModel)
     * @param acIdseq
     * @return
     */
    @Override
    public List<AlternateNameUiModel> getUiAlternateNamesByAcIdseq( String acIdseq )
    {
        String sql = "SELECT name, lae_name AS language, detl_name AS type, conte_idseq FROM cabio31_designations_view alternateDefinitions WHERE ac_idseq = ?";
        //logger.debug( sql.replace( "?", acIdseq) + " <<<<<<<" );

        List<AlternateNameUiModel> alternateNameUiModels = getAll( sql, acIdseq, AlternateNameUiModel.class );
        // Add Context
        for( AlternateNameUiModel alternateNameUiModel : alternateNameUiModels)
        {
            // Get Context name with the CONTE_IDSEQ from the alternateNameUiModel.
            alternateNameUiModel.setContext( contextDAO.getContextByIdseq( alternateNameUiModel.getConteIdseq() ).getName());
        }
        return alternateNameUiModels;
    }

    public ContextDAO getContextDAO()
    {
        return contextDAO;
    }

    public void setContextDAO( ContextDAO contextDAO )
    {
        this.contextDAO = contextDAO;
    }
}
