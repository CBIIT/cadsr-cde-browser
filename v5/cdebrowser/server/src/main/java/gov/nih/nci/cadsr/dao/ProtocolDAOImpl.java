package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ProtocolModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import gov.nih.nci.cadsr.service.model.cdeData.Protocol;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class ProtocolDAOImpl extends AbstractDAOOperations implements ProtocolDAO
{
    private Logger logger = LogManager.getLogger( ProtocolDAOImpl.class.getName() );
    private JdbcTemplate jdbcTemplate;

    public ProtocolDAOImpl()
    {
    }

    @Autowired
    ProtocolDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public List<ProtocolModel> getProtocolsByContext( String conteId )
    {
        List<ProtocolModel> result;

        String sql = " SELECT DISTINCT * FROM SBREXT.PROTOCOLS_VIEW_EXT "
                + " WHERE  CONTE_IDSEQ = ? order by upper(long_name)";
        result = getAll( sql, conteId, ProtocolModel.class );
        return result;
    }

    public boolean haveProtocolsByContext( String conteId )
    {
        List<ProtocolModel> result;

        String sql = " SELECT  DISTINCT proto_idseq FROM sbrext.protocols_view_ext "
                + " WHERE  conte_idseq = ?";
        result = getAll( sql, conteId, ProtocolModel.class );
        return ( !result.isEmpty() );
    }

    @Override
    public List<ProtocolModel> getAllProtocols()
    {
        List<ProtocolModel> result;

        String sql = " SELECT DISTINCT * FROM sbrext.protocols_view_ext "
                + " order by upper(long_name)";
        result = getAll( sql, ProtocolModel.class );

        return result;
    }
    
    @Override
	public List<Protocol> getAllProtocolsWithProgramAreaAndContext()
	{
		String sql = "SELECT DISTINCT c.pal_name programAreaPalName, c.conte_idseq contextIdSeq, pve.proto_idseq protocolIdSeq, pve.long_name protocolLongName " 
				   + "FROM SBREXT.PROTOCOLS_VIEW_EXT pve, sbr.contexts c WHERE pve.conte_idseq = c.conte_idseq "
				   + "ORDER BY c.pal_name, c.conte_idseq, pve.long_name";
        
		List<Protocol> result = getAll(sql, Protocol.class);

        return result;
	}

}
