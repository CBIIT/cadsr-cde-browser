package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ReferenceDocModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ReferenceDocDAOImpl extends AbstractDAOOperations implements ReferenceDocDAO
{
    private Logger logger = LogManager.getLogger( DataElementDAOImpl.class.getName() );

    private JdbcTemplate jdbcTemplate;

    private ContextDAO contextDAO;

    @Autowired
    ReferenceDocDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }


    @Override
    public List<ReferenceDocModel> getRefDocsByRdIdseq( String rdIdseq )
    {
        String sql = "SELECT * FROM sbr.reference_documents WHERE rd_idseq = ?";
        List<ReferenceDocModel> referenceDocModel = jdbcTemplate.query( sql, new Object[]{ rdIdseq }, new ReferenceDocMapper() );
        return referenceDocModel;
    }


    @Override
    public List<ReferenceDocModel> getRefDocsByAcIdseq( String acIdseq )
    {

        String sql = "SELECT * FROM sbr.reference_documents WHERE ac_idseq = ?";
        logger.debug( sql.replace( "?", acIdseq ) + " <<<<<<<" );

        List<ReferenceDocModel> referenceDocModel = jdbcTemplate.query( sql, new Object[]{ acIdseq }, new ReferenceDocMapper() );
        return referenceDocModel;
    }


    public ContextDAO getContextDAO()
    {
        return contextDAO;
    }

    public void setContextDAO( ContextDAO contextDAO )
    {
        this.contextDAO = contextDAO;
    }

    public final class ReferenceDocMapper extends BeanPropertyRowMapper<ReferenceDocModel>
    {

        public ReferenceDocModel mapRow( ResultSet rs, int rowNum ) throws SQLException
        {
            ReferenceDocModel referenceDocModel = new ReferenceDocModel();

            referenceDocModel.setDocName( rs.getString( "NAME" ) );// LAE_NAME(language name)  RDTL_NAME
            referenceDocModel.setDocType( rs.getString( "DCTL_NAME" ) );  // duplicate
            referenceDocModel.setDocIDSeq( rs.getString( "RD_IDSEQ" ) );
            referenceDocModel.setDocText( rs.getString( "DOC_TEXT" ) );
            referenceDocModel.setLang( rs.getString( "LAE_NAME" ) );
            referenceDocModel.setUrl( rs.getString( "URL" ) );
            referenceDocModel.setDctlName( rs.getString( "DCTL_NAME" ) );

            referenceDocModel.setContext( getContextDAO().getContextByIdseq( rs.getString( "CONTE_IDSEQ" ) ) );
            return referenceDocModel;
        }
    }
}
