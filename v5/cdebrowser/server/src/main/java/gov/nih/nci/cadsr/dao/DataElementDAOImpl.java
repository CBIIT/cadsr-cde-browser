package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import gov.nih.nci.cadsr.dao.model.AcRegistrationsModel;
import gov.nih.nci.cadsr.dao.model.CsCsiModel;
import gov.nih.nci.cadsr.dao.model.DataElementModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;


public class DataElementDAOImpl extends AbstractDAOOperations implements DataElementDAO
{
    private Logger logger = LogManager.getLogger( DataElementDAOImpl.class.getName() );

    private JdbcTemplate jdbcTemplate;

    private ContextDAO contextDAO;
    private DataElementConceptDAO dataElementConceptDAO;
    private ValueDomainDAO valueDomainDAO;
    private DesignationDAO designationDAO;
    private DefinitionDAO definitionDAO;
    private ReferenceDocDAO referenceDocDAO;
    private AcRegistrationsDAO acRegistrationsDAO;
    private CsCsiDAO csCsiDAO;
    private UsageDAO usageDAO;
    private DEOtherVersionsDAO deOtherVersionsDAO;
    private CSRefDocDAO csRefDocDAO;
    private CSIRefDocDAO csiRefDocDAO;
    
    @Autowired
    DataElementDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public List<DataElementModel> getCdeBySearchString( String dataElementSql )
    {
        List<DataElementModel> results;

        logger.debug( "getCdeBySearchString" );
        logger.debug( ">>>>>>> " + dataElementSql );
        results = getAll( dataElementSql, DataElementModel.class );
        //logger.debug( sql + " <<<<<<<" );
        logger.debug( "Done getCdeBySearchString" );

        return results;
    }

    @Override
    public DataElementModel getCdeByDeIdseq( String deIdseq ) throws EmptyResultDataAccessException
    {   
        String sql = "SELECT * FROM data_elements WHERE de_idseq = ?";
        DataElementModel dataElementModel = jdbcTemplate.queryForObject( sql, new Object[]{ deIdseq }, new DataElementMapper( DataElementModel.class ) );
        logger.debug( sql.replace( "?", deIdseq ) + " <<<<<<<" );
        return dataElementModel;
    }
    

    // CDEBROWSER-649 Improve queries for compare screen    
    @Override
    public List<DataElementModel> getCdeList(List<String> deIdseqList) throws EmptyResultDataAccessException
    { 
        List<DataElementModel> arrResult = new ArrayList<>();
    	if ((deIdseqList != null ) && (! deIdseqList.isEmpty())) {
    		String sql = "SELECT * FROM data_elements WHERE de_idseq IN (:ids)";
            Map<String, List<String>> param = Collections.singletonMap("ids", deIdseqList); 
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate().getDataSource());
            arrResult = namedParameterJdbcTemplate.query(sql, param,
            		new DataElementMapper(DataElementModel.class));
    	}
        return arrResult;
    }    

    @Override
    public List<DataElementModel> getAllCdeByCdeId( Integer cdeId )
    {
        String sql = "SELECT * FROM data_elements WHERE cde_id = ?";
        List<DataElementModel> dataElementModel = jdbcTemplate.query( sql, new Object[]{ cdeId }, new DataElementMapper( DataElementModel.class ) );
        return dataElementModel;
    }

    @Override
    public DataElementModel geCdeByCdeIdAndVersion( Integer cdeId, Number version )
    {
        String sql = "SELECT * FROM data_elements WHERE cde_id = ? AND version = ?";
        DataElementModel dataElementModel = jdbcTemplate.queryForObject( sql, new Object[]{ cdeId, version }, new DataElementMapper( DataElementModel.class ) );
        return dataElementModel;
    }

    /*public String getDataElementSql()
    {
        return DataElementSql;
    }

    //@Override
    public void setDataElementSql( String dataElementSql )
    {
        this.DataElementSql = dataElementSql;
    }*/


    public ContextDAO getContextDAO()
    {
        return contextDAO;
    }

    public void setContextDAO( ContextDAO contextDAO )
    {
        this.contextDAO = contextDAO;
    }

    public DataElementConceptDAO getDataElementConceptDAO()
    {
        return dataElementConceptDAO;
    }

    public void setDataElementConceptDAO( DataElementConceptDAO dataElementConceptDAO )
    {
        this.dataElementConceptDAO = dataElementConceptDAO;
    }

    public ValueDomainDAO getValueDomainDAO()
    {
        return valueDomainDAO;
    }

    public void setValueDomainDAO( ValueDomainDAO valueDomainDAO )
    {
        this.valueDomainDAO = valueDomainDAO;
    }

    public DesignationDAO getDesignationDAO()
    {
        return designationDAO;
    }

    public void setDesignationDAO( DesignationDAO designationDAO )
    {
        this.designationDAO = designationDAO;
    }

    public ReferenceDocDAO getReferenceDocDAO()
    {
        return referenceDocDAO;
    }

    public void setReferenceDocDAO( ReferenceDocDAO referenceDocDAO )
    {
        this.referenceDocDAO = referenceDocDAO;
    }

    public AcRegistrationsDAO getAcRegistrationsDAO()
    {
        return acRegistrationsDAO;
    }

    public void setAcRegistrationsDAO( AcRegistrationsDAO acRegistrationsDAO )
    {
        this.acRegistrationsDAO = acRegistrationsDAO;
    }

    public DefinitionDAO getDefinitionDAO()
    {
        return definitionDAO;
    }

    public void setDefinitionDAO( DefinitionDAO definitionDAO )
    {
        this.definitionDAO = definitionDAO;
    }

    public CsCsiDAO getCsCsiDAO()
    {
        return csCsiDAO;
    }

    public void setCsCsiDAO( CsCsiDAO csCsiDAO )
    {
        this.csCsiDAO = csCsiDAO;
    }

    public UsageDAO getUsageDAO()
    {
        return usageDAO;
    }

    public void setUsageDAO( UsageDAO usageDAO )
    {
        this.usageDAO = usageDAO;
    }

    public DEOtherVersionsDAO getDeOtherVersionsDAO()
    {
        return deOtherVersionsDAO;
    }

    public void setDeOtherVersionsDAO( DEOtherVersionsDAO deOtherVersionsDAO )
    {
        this.deOtherVersionsDAO = deOtherVersionsDAO;
    }

    public CSIRefDocDAO getCsiRefDocDAO()
    {
        return csiRefDocDAO;
    }

    public void setCsiRefDocDAO( CSIRefDocDAO csiRefDocDAO )
    {
        this.csiRefDocDAO = csiRefDocDAO;
    }

    public CSRefDocDAO getCsRefDocDAO()
    {
        return csRefDocDAO;
    }

    public void setCsRefDocDAO( CSRefDocDAO csRefDocDAO )
    {
        this.csRefDocDAO = csRefDocDAO;
    }

    public final class DataElementMapper extends BeanPropertyRowMapper<DataElementModel>
    {
        private Logger logger = LogManager.getLogger( DataElementMapper.class.getName() );

        public DataElementMapper( Class<DataElementModel> mappedClass )
        {
            super( mappedClass );
        }

        public DataElementModel mapRow( ResultSet rs, int rowNum ) throws SQLException
        {
            DataElementModel dataElementModel = super.mapRow( rs, rowNum );

            String deIdseq = rs.getString( "DE_IDSEQ" );
            dataElementModel.setDeIdseq( deIdseq );
            dataElementModel.setLatestVerInd( rs.getString( "LATEST_VERSION_IND" ) );
            dataElementModel.fillPreferredQuestionText();
            dataElementModel.fillUsingContexts();

            dataElementModel.setRefDocs( getReferenceDocDAO().getRefDocsByAcIdseq( deIdseq ) );

            try
            {
                dataElementModel.setDesignationModels( getDesignationDAO().getDesignationModelsByAcIdseq( deIdseq ) );
                dataElementModel.fillCsCsiDesignations();
            } catch( EmptyResultDataAccessException ex )
            {
                //logger.warn( "No Designation Models found for Data Element with idseq: " + deIdseq );
            }
            try
            {
                dataElementModel.setDefinitionModels( getDefinitionDAO().getAllDefinitionsByAcIdseq( deIdseq ) );
                dataElementModel.fillCsCsiDefinitions();
            } catch( EmptyResultDataAccessException ex )
            {
                //logger.warn( "No Definition Models found for Data Element with idseq: " + deIdseq );
            }

            try
            {
                dataElementModel.setValueDomainModel( getValueDomainDAO().getValueDomainByIdseq( rs.getString( "VD_IDSEQ" ) ) );
                //logger.debug( "valueDomainModel.getRepresentationModel: " + dataElementModel.getValueDomainModel().getRepresentationModel() );

            } catch( EmptyResultDataAccessException ex )
            {
                //logger.warn( "No Value Domain found for Data Element with idseq: " + deIdseq + "  the vdIdseq is " + rs.getString( "VD_IDSEQ" ) );
            }

            try
            {
                dataElementModel.setDec( getDataElementConceptDAO().getDecByDecIdseqWithRegStatus( rs.getString( "DEC_IDSEQ" ) ) );//CDEBROWSER-816 add DEC Reg Status
            } catch( EmptyResultDataAccessException ex )
            {
                //logger.warn( "No DataElementConcept found for Data Element with idseq: " + deIdseq + "  the DEC_IDSEQ is " + rs.getString( "DEC_IDSEQ" ) );
            }

            dataElementModel.setContext( getContextDAO().getContextByIdseq( rs.getString( "CONTE_IDSEQ" ) ) );
            if( dataElementModel.getContext() != null && dataElementModel.getContext().getName() != null )
            {
                dataElementModel.setContextName( dataElementModel.getContext().getName() );
            }
            if( dataElementModel.getContext() != null && dataElementModel.getContext().getConteIdseq() != null )
            {
                dataElementModel.setConteIdseq( dataElementModel.getContext().getConteIdseq() ); // rudely redundant
            }
            dataElementModel.setPublicId( dataElementModel.getCdeId() );

            try
            {
                AcRegistrationsModel acRegistrationsModel = getAcRegistrationsDAO().getAcRegistrationByAcIdseq( deIdseq );

                if( acRegistrationsModel != null && acRegistrationsModel.getRegistrationStatus() != null )
                {
                    dataElementModel.setRegistrationStatus( acRegistrationsModel.getRegistrationStatus() );
                }
            } catch( EmptyResultDataAccessException ex )
            {
                //logger.warn( "No AcRegistrationsModel found for Data Element with idseq: " + deIdseq );
            }

            try
            {
                List<CsCsiModel> csCsiModels = getCsCsiDAO().getAltNamesAndDefsByDataElement( deIdseq );
                if( csCsiModels != null && csCsiModels.size() > 0 )
                {
                    dataElementModel.fillCsCsiData( csCsiModels );
                }
                else
                {
                    // none found. Call fillCsCsiData() to initalize the "Unclassified" record
                    dataElementModel.fillCsCsiData( new ArrayList<CsCsiModel>( 0 ) );
                }
            } catch( EmptyResultDataAccessException ex )
            {
                //logger.warn( "No CsCsiModels found for Data Element with idseq: " + deIdseq );
                // none found. Call fillCsCsiData() to initalize the "Unclassified" record
                dataElementModel.fillCsCsiData( new ArrayList<CsCsiModel>( 0 ) );
            }
            try
            {
                dataElementModel.setUsageModels( getUsageDAO().getUsagesByDeIdseq( deIdseq ) );
            } catch( EmptyResultDataAccessException ex )
            {
                //logger.warn( "No UsageModels found for Data Element with idseq: " + deIdseq );
            }
            try
            {
                dataElementModel.setDeOtherVersionsModels( getDeOtherVersionsDAO().getOtherVersions( dataElementModel.getCdeId(), deIdseq ) );
            } catch( EmptyResultDataAccessException ex )
            {
                //logger.warn( "No Other Versions found for Data Element with idseq: " + deIdseq );
            }
            try
            {
                dataElementModel.setClassifications( getCsCsiDAO().getCsCsisByAcIdseq( deIdseq ) );
            } catch( EmptyResultDataAccessException ex )
            {
                //logger.warn( "No Other Versions found for Data Element with idseq: " + deIdseq );
            }
            try
            {
                dataElementModel.setCsRefDocModels( getCsRefDocDAO().getCSRefDocsByDEIdseq( deIdseq ) );
            } catch( EmptyResultDataAccessException ex )
            {
                //logger.info( "No Classif scheme found for Reference Docs for Data Element with idseq: " + deIdseq );
            }
            try
            {
                dataElementModel.setCsiRefDocModels( getCsiRefDocDAO().getCSIRefDocsByDEIdseq( deIdseq ) );
            } catch( EmptyResultDataAccessException ex )
            {
                //logger.warn( "No Classif scheme items found for Reference Docs for Data Element with idseq: " + deIdseq );
            }

            //logger.debug( "valueDomainModel.getRepresentationModel: " + dataElementModel.getValueDomainModel().getRepresentationModel() );
            //logger.debug( "dataElementModel.getValueDomainModel().getPublicId(): " + dataElementModel.getValueDomainModel().getPublicId() );

            return dataElementModel;
        }
    }
    //Re-implemented this method: it failed before on more than 1000 IDs
    // Limits results to a 1000 records max
	@Override
	public List<DataElementModel> getCdeByDeIdseqList(List<String> acIdseqList) throws EmptyResultDataAccessException {
        List<DataElementModel> arrResult = new ArrayList<>();
        if ((acIdseqList != null) && (!(acIdseqList.isEmpty()))) {
        	List<String> deIdseqList = cleanUpIdDuplicates(acIdseqList);
        	List<String> portionOf1000;
        	List<DataElementModel> arrOf1000;
        	Iterator<String> iter = deIdseqList.iterator();
        	while (iter.hasNext()) {
        		portionOf1000 = new ArrayList<>();
        		for (int j = 0; ((j < oracleIn1000) & iter.hasNext()); j++) {
        			portionOf1000.add(iter.next());
        		}
        		arrOf1000 = retrieve1000Ids(portionOf1000);
        		arrResult.addAll(arrOf1000);
        	}
        }
        return arrResult;
	}
	
    protected List<DataElementModel> retrieve1000Ids(List<String> deIdseqList) {
    	List<DataElementModel> dataElementModel;
    	if ((deIdseqList != null ) && (! deIdseqList.isEmpty())) {
    		String sql = "SELECT * FROM data_elements WHERE de_idseq IN (:ids)";
            //MapSqlParameterSource parameters = new MapSqlParameterSource();
            Map<String, List<String>> param = Collections.singletonMap("ids", deIdseqList); 
            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate().getDataSource());
            dataElementModel = namedParameterJdbcTemplate.query(sql, param,
            		new DataElementMapper(DataElementModel.class));
    	}
    	else {
    		dataElementModel = new ArrayList<>();
    	}
        return dataElementModel;
    }

}
