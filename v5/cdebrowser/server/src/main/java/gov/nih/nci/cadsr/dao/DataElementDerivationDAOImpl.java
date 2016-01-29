package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.DataElementDerivationComponentModel;
import gov.nih.nci.cadsr.dao.model.DataElementDerivationModel;
import gov.nih.nci.cadsr.dao.model.PDeIdseqModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class DataElementDerivationDAOImpl extends AbstractDAOOperations implements DataElementDerivationDAO
{
    public DataElementDerivationDAOImpl()
    {
    }

    private Logger logger = LogManager.getLogger( DataElementDerivationDAOImpl.class.getName() );
    private JdbcTemplate jdbcTemplate;


    @Autowired
    DataElementDerivationDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }


    @Override
    public DataElementDerivationModel getDataElementDerivationByCdeId( int cdeId )
    {

        String sql = "SELECT complex_data_elements.crtl_name AS derivation_type, complex_data_elements.rule, complex_data_elements.methods AS method, concat_char AS concatenationCharacter" +
                " FROM complex_data_elements , data_elements " +
                " WHERE data_elements.de_idseq = complex_data_elements.p_de_idseq " +
                " AND data_elements.cde_id = ? ";

        logger.debug( sql.replace( "?", Integer.toString( cdeId ) ) + " <<<<<<<" );

        DataElementDerivationModel results = query( sql, cdeId, DataElementDerivationModel.class );

        return results;
    }

    @Override
    public List<DataElementDerivationComponentModel> getDataElementDerivationComponentsByCdeId( int cdeId )
    {
        String sql = "SELECT DISTINCT complex_data_elements.p_de_idseq " +
                " FROM complex_data_elements , data_elements , complex_de_relationships " +
                " WHERE data_elements.de_idseq = complex_data_elements.p_de_idseq " +
                " AND data_elements.cde_id = ? ";

        logger.debug( sql.replace( "?", Integer.toString( cdeId ) ) + " <<<<<<<" );

        PDeIdseqModel pDeIdseq = query( sql, cdeId, PDeIdseqModel.class );

        logger.debug( "pDeIdseq: " + pDeIdseq.getpDeIdseq() );

        sql = "SELECT display_order, data_elements.long_name, sbr.contexts.name AS Context, cde_id AS public_id, data_elements.version, asl_name AS workflowStatus, data_elements.de_idseq " +
                " FROM complex_de_relationships, data_elements, sbr.contexts " +
                " WHERE complex_de_relationships.p_de_idseq = ? " +
                " AND data_elements.de_idseq = complex_de_relationships.c_de_idseq " +
                " AND contexts.conte_idseq = data_elements.conte_idseq" +
                " ORDER BY display_order";

        logger.debug( sql.replace( "?", pDeIdseq.getpDeIdseq() ) + " <<<<<<<" );
        List<DataElementDerivationComponentModel> results = getAll( sql, pDeIdseq.getpDeIdseq(), DataElementDerivationComponentModel.class );

        return results;
    }
}
