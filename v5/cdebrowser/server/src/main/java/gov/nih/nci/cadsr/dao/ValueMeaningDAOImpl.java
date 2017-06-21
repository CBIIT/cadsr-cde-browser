package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */


import gov.nih.nci.cadsr.dao.model.ValueMeaningModel;
import gov.nih.nci.cadsr.dao.model.ValueMeaningUiModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class ValueMeaningDAOImpl extends AbstractDAOOperations implements ValueMeaningDAO
{
    private static final Logger logger = LogManager.getLogger( ValueMeaningDAOImpl.class.getName() );
    private JdbcTemplate jdbcTemplate;

    private AlternateDefinitionDAOImpl alternateDefinitionDAO;
    private AlternateNameDAOImpl alternateNameDAO;

    @Autowired
    ValueMeaningDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }


    @Override
    public List<ValueMeaningModel> getValueMeaningsByPvIdseq( String pvIdseq )
    {
        String sql = pvIdseqQueryBuilder(  pvIdseq );
        List<ValueMeaningModel>  valueMeaningModelList = getAll( sql, ValueMeaningModel.class );

        // Add the AlternateNames and AlternateDefinitions
        for( ValueMeaningModel valueMeaningModel: valueMeaningModelList)
        {
            valueMeaningModel.setAlternateNames( alternateNameDAO.getAlternateNamesByAcIdseq( valueMeaningModel.getVmIdseq() ) );
            valueMeaningModel.setAlternateDefinitions( alternateDefinitionDAO.getAlternateDefinitionsByAcIdseq( valueMeaningModel.getVmIdseq() ) );
        }

        return valueMeaningModelList;
    }

    protected String pvIdseqQueryBuilder( String pvIdseq )
    {
        String sql = "SELECT DISTINCT  " +
                "value_meanings.* " +
                "FROM " +
                "sbr.permissible_values," +
                "sbr.vd_pvs,sbr.value_meanings  " +
                "WHERE sbr.permissible_values.pv_idseq = sbr.vd_pvs.pv_idseq " +
                "AND sbr.vd_pvs.vd_idseq = " + pvIdseq + " " +
                "AND sbr.value_meanings.vm_idseq = sbr.permissible_values.vm_idseq ";

        //logger.debug( sql + " <<<<<<<" );
        return sql;
    }


    @Override
    public List<ValueMeaningModel> getValueMeaningsByCdeIdAndVersion( String cdeId, String version )
    {
        String sql = cdeIdAndVersionQueryBuilder(  cdeId,  version );
        List<ValueMeaningModel>  valueMeaningModelList = getAll( sql, cdeId, ValueMeaningModel.class );

        // Add the AlternateNames and AlternateDefinitions
        for( ValueMeaningModel valueMeaningModel: valueMeaningModelList)
        {
            valueMeaningModel.setAlternateNames( alternateNameDAO.getAlternateNamesByAcIdseq( valueMeaningModel.getVmIdseq() ) );
            valueMeaningModel.setAlternateDefinitions( alternateDefinitionDAO.getAlternateDefinitionsByAcIdseq( valueMeaningModel.getVmIdseq() ) );
        }

        return valueMeaningModelList;
    }

    protected String cdeIdAndVersionQueryBuilder( String cdeId, String version )
    {
        String sql = "SELECT DISTINCT  " +
                "value_meanings.* " +
                "FROM " +
                "sbr.permissible_values," +
                "sbr.vd_pvs,sbr.value_meanings,  " +
                "sbr.data_elements " +
                "WHERE sbr.permissible_values.pv_idseq = sbr.vd_pvs.pv_idseq " +
                "AND sbr.data_elements.cde_id = " + cdeId + " " +
                "AND sbr.data_elements.version = "+ version + " " +
                "AND sbr.vd_pvs.vd_idseq = sbr.data_elements.vd_idseq " +
                "AND sbr.value_meanings.vm_idseq = sbr.permissible_values.vm_idseq ";

        //logger.debug( sql.replace( "?", cdeId ) + " <<<<<<<" );
        return sql;
    }

    @Override
    // Number parameters
    public List<ValueMeaningModel> getValueMeaningsByCdeIdAndVersion( int cdeId, float version )
    {
        return getValueMeaningsByCdeIdAndVersion(  Integer.toString( cdeId ) ,  Float.toString(version));
    }

    @Override
    public List<ValueMeaningModel> getValueMeaningsByIdAndVersion( String id, String version )
    {
        String sql = "SELECT * from  sbr.value_meanings " +
                "WHERE ( vm_id = ? AND version = " + version + " )";

        //logger.debug( sql.replace( "?", id ) + " <<<<<<<" );

        List<ValueMeaningModel>  valueMeaningModelList = getAll( sql, id, ValueMeaningModel.class );

        // Add the AlternateNames and AlternateDefinitions
        for( ValueMeaningModel valueMeaningModel: valueMeaningModelList)
        {
            valueMeaningModel.setAlternateNames( alternateNameDAO.getAlternateNamesByAcIdseq( valueMeaningModel.getVmIdseq() ) );
            valueMeaningModel.setAlternateDefinitions( alternateDefinitionDAO.getAlternateDefinitionsByAcIdseq( valueMeaningModel.getVmIdseq() ) );
        }
        return valueMeaningModelList;
    }

    /**
     * @param cdeId
     * @param version
     * @return
     */
    @Override
    public List<ValueMeaningUiModel> getUiValueMeanings( int cdeId, float version )
    {
        List<ValueMeaningUiModel>  valueMeaningUiModelList = getUiValueMeaningsByCdeIdAndVersion(  cdeId,  version );

        // Add the AlternateNames and AlternateDefinitions
        return addAltNamesAndDefinitions(valueMeaningUiModelList);
    }

    protected List<ValueMeaningUiModel> addAltNamesAndDefinitions(  List<ValueMeaningUiModel>  valueMeaningUiModelList)
    {
        // Add the AlternateNames and AlternateDefinitions
        for( ValueMeaningUiModel valueMeaningUiModel: valueMeaningUiModelList)
        {
            valueMeaningUiModel.setAlternateNames( alternateNameDAO.getUiAlternateNamesByAcIdseq( valueMeaningUiModel.getVmIdseq() ) );
            valueMeaningUiModel.setAlternateDefinitions( alternateDefinitionDAO.getUiAlternateDefinitionsByAcIdseq( valueMeaningUiModel.getVmIdseq() ) );
        }
        return valueMeaningUiModelList;
    }

    protected List<ValueMeaningUiModel> getUiValueMeaningsByCdeIdAndVersion( int cdeId, float version )
    {
        String sql = uiCdeIdAndVersionQueryBuilder( cdeId, version );
        List<ValueMeaningUiModel>  valueMeaningUiModelList = getAll( sql, ValueMeaningUiModel.class );
        return valueMeaningUiModelList;
    }

    protected List<ValueMeaningUiModel> getUiValueMeaningsByCdeIdAndVersion( String cdeId, String version )
    {
        return getUiValueMeaningsByCdeIdAndVersion(Integer.parseInt(cdeId), Float.parseFloat( version )  );
    }

    /**
     * This query only get's the fields that are needed for the UI and renames them, it populates ValueMeaningUiModel rather than  ValueMeaningModel
     * @param cdeId
     * @param version
     * @return
     */
    protected String uiCdeIdAndVersionQueryBuilder(int cdeId, float version )
    {
        String sql = "SELECT sbr.vd_pvs.pv_idseq AS pvIdseq, " +
                "sbr.value_meanings.long_name AS pvMeaning, " +
                "sbr.value_meanings.vm_id AS vmPublicId, " +
                "sbr.value_meanings.version AS vmVersion, " +
                "sbr.value_meanings.vm_idseq AS vmIdseq " +
                "FROM sbr.permissible_values," +
                " sbr.vd_pvs," +
                " sbr.value_meanings," +
                " sbr.data_elements " +
                "WHERE sbr.permissible_values.pv_idseq = sbr.vd_pvs.pv_idseq " +
                "AND sbr.data_elements.cde_id = " + cdeId + " " +
                "AND sbr.data_elements.version = "+ version + " " +
                "AND sbr.vd_pvs.vd_idseq = sbr.data_elements.vd_idseq " +
                "AND sbr.value_meanings.vm_idseq = sbr.permissible_values.vm_idseq ORDER BY UPPER(sbr.permissible_values.value)";

        //logger.debug( sql.replace( "?", Integer.toString( cdeId ) ) + " <<<<<<<" );
        return sql;
    }



        public AlternateDefinitionDAOImpl getAlternateDefinitionDAO()
    {
        return alternateDefinitionDAO;
    }

    public void setAlternateDefinitionDAO( AlternateDefinitionDAOImpl alternateDefinitionDAO )
    {
        this.alternateDefinitionDAO = alternateDefinitionDAO;
    }

    public AlternateNameDAOImpl getAlternateNameDAO()
    {
        return alternateNameDAO;
    }

    public void setAlternateNameDAO( AlternateNameDAOImpl alternateNameDAO )
    {
        this.alternateNameDAO = alternateNameDAO;
    }
}
