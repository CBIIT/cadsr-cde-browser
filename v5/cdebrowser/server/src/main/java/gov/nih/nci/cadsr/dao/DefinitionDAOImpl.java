package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.AcAttCsCsiModel;
import gov.nih.nci.cadsr.dao.model.ContextModel;
import gov.nih.nci.cadsr.dao.model.CsCsiModel;
import gov.nih.nci.cadsr.dao.model.DefinitionModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DefinitionDAOImpl extends AbstractDAOOperations implements DefinitionDAO
{

    private Logger logger = LogManager.getLogger( DefinitionDAOImpl.class.getName() );

    private JdbcTemplate jdbcTemplate;
    private ContextDAO contextDAO;
    private AcAttCsCsiDAO acAttCsCsiDAO;

    @Autowired
    DefinitionDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }


    public ContextDAO getContextDAO()
    {
        return contextDAO;
    }

    public void setContextDAO( ContextDAO contextDAO )
    {
        this.contextDAO = contextDAO;
    }

    public AcAttCsCsiDAO getAcAttCsCsiDAO()
    {
        return acAttCsCsiDAO;
    }

    public void setAcAttCsCsiDAO( AcAttCsCsiDAO acAttCsCsiDAO )
    {
        this.acAttCsCsiDAO = acAttCsCsiDAO;
    }

    @Override
    public DefinitionModel getDefinitionByDefinIdseq( String definIdseq )
    {
        String sql = "SELECT * FROM sbr.definitions WHERE defin_idseq = ?";
        DefinitionModel definitionModel = jdbcTemplate.queryForObject( sql, new Object[]{ definIdseq }, new DefinitionMapper( DefinitionModel.class ) );
        return definitionModel;
    }

    @Override
    public List<DefinitionModel> getAllDefinitionsByAcIdseq( String acIdseq )
    {
        String sql = "SELECT * FROM sbr.definitions WHERE ac_idseq = ?";
        List<DefinitionModel> definitionModels = jdbcTemplate.query( sql, new Object[]{ acIdseq }, new DefinitionMapper( DefinitionModel.class ) );
        return definitionModels;
    }

    public final class DefinitionMapper extends BeanPropertyRowMapper<DefinitionModel>
    {
        private Logger logger = LogManager.getLogger( DefinitionMapper.class.getName() );

        public DefinitionMapper( Class<DefinitionModel> mappedClass )
        {
            super( mappedClass );
        }

        public DefinitionModel mapRow( ResultSet rs, int rowNum ) throws SQLException
        {
            DefinitionModel definitionModel = super.mapRow( rs, rowNum );
            try
            {
                ContextModel contextModel = getContextDAO().getContextByIdseq( rs.getString( "CONTE_IDSEQ" ) );
                if( contextModel != null )
                {
                    definitionModel.setContext( contextModel );
                }
            } catch( EmptyResultDataAccessException ex )
            {
                logger.warn( "no contextModel found for CONTE_IDSEQ: " + rs.getString( "CONTE_IDSEQ" ) );
            }

            try
            {
                // getAllAcAttCsCsiByAttIdseq() is pretty inefficient.  Could be moved into this DAO and just fetch DISTINCT cs_csi.csi_idseq
                List<AcAttCsCsiModel> acAttCsCsiModels = getAcAttCsCsiDAO().getAllAcAttCsCsiByAttIdseq( rs.getString( "DEFIN_IDSEQ" ) );
                if( acAttCsCsiModels != null && acAttCsCsiModels.size() > 0 )
                {
                    definitionModel.setCsiIdseqs( new HashSet<String>( acAttCsCsiModels.size() ) );
                    for( AcAttCsCsiModel acAttCsCsiModel : acAttCsCsiModels )
                    {
                        if( acAttCsCsiModel.getCsCsiIdseq() != null )
                        {
                            definitionModel.getCsiIdseqs().add( acAttCsCsiModel.getCsiIdseq() );
                        }
                    }
                }
                else
                {
                    // this Definition is unclassified
                    definitionModel.setCsiIdseqs( new HashSet<String>( 1 ) );
                    definitionModel.getCsiIdseqs().add( CsCsiModel.UNCLASSIFIED );
                }
            } catch( EmptyResultDataAccessException ex )
            {
                logger.warn( "no CSIs found for Definition: " + rs.getString( "DEFIN_IDSEQ" ) );
                // this Definition is unclassified
                definitionModel.setCsiIdseqs( new HashSet<String>( 1 ) );
                definitionModel.getCsiIdseqs().add( CsCsiModel.UNCLASSIFIED );
            }

            return definitionModel;
        }
    }
}
