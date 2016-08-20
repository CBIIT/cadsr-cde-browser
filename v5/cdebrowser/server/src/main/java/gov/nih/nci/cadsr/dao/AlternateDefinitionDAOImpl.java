package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.AlternateDefinitionModel;
import gov.nih.nci.cadsr.dao.model.AlternateDefinitionUiModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class AlternateDefinitionDAOImpl extends AbstractDAOOperations implements AlternateDefinitionDAO
{
    private Logger logger = LogManager.getLogger( AlternateDefinitionDAOImpl.class.getName() );
    private JdbcTemplate jdbcTemplate;
    private ContextDAO contextDAO;

    @Autowired
    AlternateDefinitionDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public List<AlternateDefinitionModel> getAlternateDefinitionsByAcIdseq( String acIdseq )
    {
        String sql = "SELECT * FROM cabio31_definitions_view alternateDefinitions WHERE ac_idseq = ?";
        return getAll( sql, acIdseq, AlternateDefinitionModel.class );
    }

    /**
     * This query only get's the fields that are needed for the UI, it populates AlternateDefinitionUiModel (the UI only, version of AlternateDefinitionModel)
     * @param acIdseq
     * @return
     */
    @Override
    public List<AlternateDefinitionUiModel> getUiAlternateDefinitionsByAcIdseq( String acIdseq )
    {
        String sql = "SELECT definition AS name,lae_name AS language, defl_name AS type, conte_idseq FROM cabio31_definitions_view WHERE ac_idseq = ?";
        //logger.debug( sql.replace( "?", acIdseq) + " <<<<<<<" );

        List<AlternateDefinitionUiModel> alternateDefinitionUiModels = getAll( sql, acIdseq, AlternateDefinitionUiModel.class );
        // Add Context
        for( AlternateDefinitionUiModel alternateDefinitionUiModel : alternateDefinitionUiModels)
        {
            // Get Context name with the CONTE_IDSEQ from the alternateDefinitionUiModel.
            alternateDefinitionUiModel.setContext( contextDAO.getContextByIdseq( alternateDefinitionUiModel.getConteIdseq() ).getName());
        }
        return alternateDefinitionUiModels;

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
