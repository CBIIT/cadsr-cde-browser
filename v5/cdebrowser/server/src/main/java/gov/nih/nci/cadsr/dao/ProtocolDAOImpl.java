package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ProtocolModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import gov.nih.nci.cadsr.dao.operation.LookupDataQueryBuilder;
import gov.nih.nci.cadsr.service.model.cdeData.Protocol;

import org.apache.commons.lang3.StringUtils;
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
    private LookupDataQueryBuilder lookupDataQueryBuilder;

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

        String sql = " SELECT DISTINCT * FROM SBREXT.PROTOCOLS_VIEW_EXT WHERE  CONTE_IDSEQ = ? order by upper(long_name)";
        result = getAll( sql, conteId, ProtocolModel.class );
        return result;
    }

    public boolean haveProtocolsByContext( String conteId )
    {
        List<ProtocolModel> result;

        String sql = " SELECT DISTINCT proto_idseq FROM sbrext.protocols_view_ext WHERE  conte_idseq = ?";
        result = getAll( sql, conteId, ProtocolModel.class );
        return ( !result.isEmpty() );
    }

    @Override
    public List<ProtocolModel> getAllProtocols()
    {
        List<ProtocolModel> result;

        String sql = " SELECT DISTINCT * FROM sbrext.protocols_view_ext order by upper(long_name)";
        result = getAll( sql, ProtocolModel.class );

        return result;
    }
    
    @Override
	public List<Protocol> getAllProtocolsWithProgramAreaAndContext(String contexIdSeq, String protocolOrForm)
	{
    	String sql = lookupDataQueryBuilder.buildProtocolLookupQuery(contexIdSeq, protocolOrForm);
		
    	List<Protocol> result = getAll(sql, Protocol.class);

        return result;
	}

	public LookupDataQueryBuilder getLookupDataQueryBuilder() {
		return lookupDataQueryBuilder;
	}

	public void setLookupDataQueryBuilder(LookupDataQueryBuilder lookupDataQueryBuilder) {
		this.lookupDataQueryBuilder = lookupDataQueryBuilder;
	}

}
