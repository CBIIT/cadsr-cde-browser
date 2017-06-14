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
    public DataElementDerivationModel getDataElementDerivationByCdeIdseq( String deIdseq )
    {

        String sql = "SELECT CRTL_NAME AS derivation_type, rule, METHODS AS method, CONCAT_CHAR AS concatenationCharacter, "
        		+ "DATE_MODIFIED, DATE_CREATED, CREATED_BY, MODIFIED_BY " +
                "FROM SBR.COMPLEX_DATA_ELEMENTS where P_DE_IDSEQ = ?";

        logger.debug( sql.replace("?", deIdseq) + " <<<<<<<");

        DataElementDerivationModel results = query( sql, deIdseq, DataElementDerivationModel.class );

        return results;
    }
    
    @Override
    public List<DataElementDerivationComponentModel> getDataElementDerivationComponentsByCdeIdseq( String deIdseq )
    {
        String sql = "SELECT cdr.cdr_idseq, cdr.display_order, de.long_name, ct.name AS Context, de.cde_id AS public_id, "
        		+ "de.version, de.asl_name AS workflowStatus, de.de_idseq " +
                "from sbr.contexts ct, sbr.complex_de_relationships cdr, sbr.data_elements de " +
                "where de.CONTE_IDSEQ = ct.CONTE_IDSEQ and cdr.c_de_idseq = de.de_idseq and p_de_idseq = ? " +
                "order by cdr.display_order";

        logger.debug( sql.replace( "?", deIdseq) + " <<<<<<<" );
        List<DataElementDerivationComponentModel> results = getAll( sql, deIdseq, DataElementDerivationComponentModel.class );

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
