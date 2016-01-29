package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ProtocolFormModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class ProtocolFormDAOImpl extends AbstractDAOOperations implements ProtocolFormDAO
{
    private Logger logger = LogManager.getLogger( ProtocolFormDAOImpl.class.getName() );

    private JdbcTemplate jdbcTemplate;

    public ProtocolFormDAOImpl()
    {
    }

    @Autowired
    ProtocolFormDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public List<ProtocolFormModel> getProtocolForms( String csidSeq )
    {
        String sql = " select distinct * from FB_FORMS_VIEW FORMVIEW, SBR.CS_CSI_VIEW CSC,SBR.AC_CSI_VIEW ACS "
                + " WHERE  csc.cs_idseq = ?"
                + " AND csc.cs_csi_idseq = acs.cs_csi_idseq "
                + " AND acs.AC_IDSEQ=formview.QC_IDSEQ "
                + " ORDER BY UPPER(protocol_long_name), UPPER(context_name)";

        return getAll( sql, csidSeq, ProtocolFormModel.class );
    }

    @Override
    public List<ProtocolFormModel> getProtocolFormByContextId( String ContextId )
    {
        List<ProtocolFormModel> result;
        String sql = " SELECT DISTINCT * FROM sbrext.fb_forms_view"
                + " WHERE  conte_idseq = ? AND latest_version_ind = 'Yes' ORDER BY  UPPER(long_name)";
        logger.debug( "getProtocolFormByContextId" );
        //logger.debug( ">>>>>>> " + sql.replace( "?", ContextId ) );
        result = getAll( sql, ContextId, ProtocolFormModel.class );
        //logger.debug( sql.replace( "?", ContextId ) + " <<<<<<<" );
        logger.debug( "Done getProtocolFormByContextId\n" );

        return result;

    }

    @Override
    public List<ProtocolFormModel> getAllProtocolForm()
    {
        List<ProtocolFormModel> result;
        String sql = " select distinct * from SBREXT.FB_FORMS_VIEW"
                + " where LATEST_VERSION_IND = 'Yes' ORDER BY  upper(LONG_NAME)";
        logger.debug( "getAllProtocolForm" );
        //logger.debug( ">>>>>>> " + sql );
        result = getAll( sql, ProtocolFormModel.class );
        //logger.debug( sql + " <<<<<<<" );
        logger.debug( "Done getAllProtocolForm\n" );

        return result;

    }

    @Override
    public List<ProtocolFormModel> getProtocolFormByProtoId( String protoIdseq )
    {
        List<ProtocolFormModel> result;
        String sql = " select distinct * from SBREXT.FB_FORMS_VIEW"
                + " where  PROTO_IDSEQ = ? AND LATEST_VERSION_IND = 'Yes' ORDER BY upper(LONG_NAME)";
        logger.debug( "getProtocolFormByProtoId" );
        //logger.debug( ">>>>>>> " + sql.replace( "?", protoIdseq ) );
        result = getAll( sql, protoIdseq, ProtocolFormModel.class );
        //logger.debug( sql.replace( "?", protoIdseq ) + " <<<<<<<" );
        logger.debug( "Done getProtocolFormByProtoId\n" );

        return result;
    }
}
