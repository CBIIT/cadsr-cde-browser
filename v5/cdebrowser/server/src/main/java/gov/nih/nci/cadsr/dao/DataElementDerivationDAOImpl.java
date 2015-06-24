package gov.nih.nci.cadsr.dao;

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

/**
 * Created by lernermh on 6/17/15.
 */
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

        String sql = "select COMPLEX_DATA_ELEMENTS.CRTL_NAME as derivation_type, COMPLEX_DATA_ELEMENTS.RULE, COMPLEX_DATA_ELEMENTS.METHODS as method, CONCAT_CHAR as concatenationCharacter" +
                " from COMPLEX_DATA_ELEMENTS , DATA_ELEMENTS " +
                " WHERE DATA_ELEMENTS.DE_IDSEQ = COMPLEX_DATA_ELEMENTS.P_DE_IDSEQ " +
                " and DATA_ELEMENTS.CDE_ID = ? ";

        logger.debug( sql.replace( "?", Integer.toString( cdeId ) ) + " <<<<<<<" );

        DataElementDerivationModel results = query( sql, cdeId, DataElementDerivationModel.class );

        return results;
    }

    @Override
    public List<DataElementDerivationComponentModel> getDataElementDerivationComponentsByCdeId( int cdeId )
    {
        String sql = "select distinct COMPLEX_DATA_ELEMENTS.P_DE_IDSEQ " +
                " from COMPLEX_DATA_ELEMENTS , DATA_ELEMENTS , COMPLEX_DE_RELATIONSHIPS " +
                " WHERE DATA_ELEMENTS.DE_IDSEQ = COMPLEX_DATA_ELEMENTS.P_DE_IDSEQ " +
                " and DATA_ELEMENTS.CDE_ID = ? ";

        logger.debug( sql.replace( "?", Integer.toString( cdeId ) ) + " <<<<<<<" );

               PDeIdseqModel pDeIdseq = query( sql, cdeId, PDeIdseqModel.class );

        logger.debug( "pDeIdseq: " + pDeIdseq.getpDeIdseq() );

        sql = "select DISPLAY_ORDER, DATA_ELEMENTS.LONG_NAME, SBR.CONTEXTS.NAME as Context, CDE_ID as public_id, DATA_ELEMENTS.VERSION, ASL_NAME as workflowStatus, DATA_ELEMENTS.DE_IDSEQ " +
        " from COMPLEX_DE_RELATIONSHIPS, DATA_ELEMENTS, SBR.CONTEXTS " +
        " where COMPLEX_DE_RELATIONSHIPS.P_DE_IDSEQ = ? " +
        " and DATA_ELEMENTS.DE_IDSEQ = COMPLEX_DE_RELATIONSHIPS.C_DE_IDSEQ " +
        " and CONTEXTS.CONTE_IDSEQ = DATA_ELEMENTS.CONTE_IDSEQ";

        logger.debug( sql.replace( "?", pDeIdseq.getpDeIdseq()) + " <<<<<<<" );
        List<DataElementDerivationComponentModel> results = getAll( sql, pDeIdseq.getpDeIdseq(), DataElementDerivationComponentModel.class );

        return results;
    }
}
