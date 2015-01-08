/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.dao;

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
        String sql = " select * from FB_FORMS_VIEW formview, sbr.cs_csi_view csc,sbr.ac_csi_view acs "
                + " where  csc.cs_idseq = ?"
                + " and csc.cs_csi_idseq = acs.cs_csi_idseq "
                + " and acs.AC_IDSEQ=formview.QC_IDSEQ "
                + " order by upper(protocol_long_name), upper(context_name)";

        return getAll( sql, csidSeq, ProtocolFormModel.class );
    }

    @Override
    public List<ProtocolFormModel> getProtocolFormByContextId( String ContextId )
    {
        List<ProtocolFormModel> result;
        String sql = " select * from SBREXT.FB_FORMS_VIEW"
                + " where  CONTE_IDSEQ = ? AND LATEST_VERSION_IND = 'Yes' ORDER BY LONG_NAME";
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
        String sql = " select * from SBREXT.FB_FORMS_VIEW"
                + " where LATEST_VERSION_IND = 'Yes' ORDER BY LONG_NAME";
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
        String sql = " select * from SBREXT.FB_FORMS_VIEW"
                + " where  PROTO_IDSEQ = ? AND LATEST_VERSION_IND = 'Yes' ORDER BY LONG_NAME";
        logger.debug( "getProtocolFormByProtoId" );
        //logger.debug( ">>>>>>> " + sql.replace( "?", protoIdseq ) );
        result = getAll( sql, protoIdseq, ProtocolFormModel.class );
        //logger.debug( sql.replace( "?", protoIdseq ) + " <<<<<<<" );
        logger.debug( "Done getProtocolFormByProtoId\n" );

        return result;
    }
}
