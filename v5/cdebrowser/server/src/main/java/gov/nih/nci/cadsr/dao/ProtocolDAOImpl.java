/*
 * Copyright 2015 Leidos Biomedical Research, Inc.
 */

package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.ProtocolModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class ProtocolDAOImpl   extends AbstractDAOOperations implements ProtocolDAO
{
    private Logger logger = LogManager.getLogger( ProtocolDAOImpl.class.getName() );
    private JdbcTemplate jdbcTemplate;

    public ProtocolDAOImpl()
    {
    }

    @Autowired
    ProtocolDAOImpl(DataSource dataSource) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public List<ProtocolModel> getProtocolsByContext( String conteId )
    {
        List<ProtocolModel> result;

        String sql = " select * from SBREXT.PROTOCOLS_VIEW_EXT "
                + " where  CONTE_IDSEQ = ? order by LONG_NAME";
        result = getAll( sql, conteId , ProtocolModel.class );
        return result;
    }

    @Override
    public List<ProtocolModel> getAllProtocols()
    {
        List<ProtocolModel> result;

        String sql = " select * from SBREXT.PROTOCOLS_VIEW_EXT "
                + " order by LONG_NAME";
        logger.debug( "getAllProtocols" );
        //logger.debug( ">>>>>>> "+ sql + "\n" );
        result = getAll( sql , ProtocolModel.class );
        //logger.debug( sql + " <<<<<<<\n" );
        logger.debug( "Done getAllProtocols" );

        return result;
    }

}
