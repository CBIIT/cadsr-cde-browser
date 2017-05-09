package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.*;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DataElementConceptDAOImpl extends AbstractDAOOperations implements DataElementConceptDAO
{

    private Logger logger = LogManager.getLogger( DataElementConceptDAOImpl.class.getName() );

    private JdbcTemplate jdbcTemplate;

    private PropertyDAO propertyDAO;
    private ObjectClassDAO objectClassDAO;
    private ContextDAO contextDAO;
    public ConceptualDomainDAO conceptualDomainDAO;


    @Autowired
    DataElementConceptDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public DataElementConceptModel getDecByDecIdseq( String decIdseq ) throws EmptyResultDataAccessException
    {
        String sql = "SELECT * FROM sbr.data_element_concepts WHERE dec_idseq = ?";
        DataElementConceptModel dataElementConceptModel = jdbcTemplate.queryForObject( sql, new Object[]{ decIdseq }, new DataElementConceptMapper( DataElementConceptModel.class ) );
        logger.debug( "dataElementConceptModel: " + dataElementConceptModel.toString() );
        return dataElementConceptModel;
    }
    @Override
    public DataElementConceptModel getDecByDecIdseqWithRegStatus( String decIdseq ) throws EmptyResultDataAccessException
    {
        //CDEBROWSER-816 add DEC Reg Status
    	String sql = "SELECT dc.*, reg.REGISTRATION_STATUS cd_registration_status FROM sbr.data_element_concepts dc left join sbr.AC_REGISTRATIONS reg on dc.dec_idseq = reg.ac_idseq WHERE dc.dec_idseq = ?";
        DataElementConceptModel dataElementConceptModel = jdbcTemplate.queryForObject( sql, new Object[]{ decIdseq }, new DataElementConceptMapper( DataElementConceptModel.class ) );
        logger.debug( "dataElementConceptModel: " + dataElementConceptModel.toString() );
        return dataElementConceptModel;
    }
    
   @Override
    public List<DataElementConceptModel> getDecByLongNameWildCard( String lName ) throws EmptyResultDataAccessException
    {
        // Change wildcard *  to Oracle %
        lName = lName.replaceAll( "\\*", "%" );
        String sql = "SELECT * FROM sbr.data_element_concepts WHERE upper(long_name) like upper('"+ lName + "')";

        return getAll( sql,  DataElementConceptModel.class );
    }


    public PropertyDAO getPropertyDAO()
    {
        return propertyDAO;
    }

    public void setPropertyDAO( PropertyDAO propertyDAO )
    {
        this.propertyDAO = propertyDAO;
    }

    public ObjectClassDAO getObjectClassDAO()
    {
        return objectClassDAO;
    }

    public void setObjectClassDAO( ObjectClassDAO objectClassDAO )
    {
        this.objectClassDAO = objectClassDAO;
    }

    public ContextDAO getContextDAO()
    {
        return contextDAO;
    }

    public void setContextDAO( ContextDAO contextDAO )
    {
        this.contextDAO = contextDAO;
    }

    public ConceptualDomainDAO getConceptualDomainDAO()
    {
        return conceptualDomainDAO;
    }

    public void setConceptualDomainDAO( ConceptualDomainDAO conceptualDomainDAO )
    {
        this.conceptualDomainDAO = conceptualDomainDAO;
    }

    public final class DataElementConceptMapper extends BeanPropertyRowMapper<DataElementConceptModel>
    {
        private Logger logger = LogManager.getLogger( DataElementConceptMapper.class.getName() );

        public DataElementConceptMapper( Class<DataElementConceptModel> mappedClass )
        {
            super( mappedClass );
        }

        public DataElementConceptModel mapRow( ResultSet rs, int rowNum ) throws SQLException
        {
//            DataElementConceptModel dataElementConceptModel = new DataElementConceptModel();
            DataElementConceptModel dataElementConceptModel = super.mapRow( rs, rowNum );

//            dataElementConceptModel.setPreferredName(rs.getString("PREFERRED_NAME"));
//            dataElementConceptModel.setPreferredDefinition(rs.getString("PREFERRED_DEFINITION"));
//            dataElementConceptModel.setLongName(rs.getString("LONG_NAME"));
//            dataElementConceptModel.setAslName(rs.getString("ASL_NAME"));
//            dataElementConceptModel.setVersion(rs.getFloat("VERSION"));
//            dataElementConceptModel.setDeletedInd(rs.getString("DELETED_IND"));
            dataElementConceptModel.setLatestVerInd( rs.getString( "LATEST_VERSION_IND" ) );
            dataElementConceptModel.setPublicId( rs.getInt( "DEC_ID" ) );
            dataElementConceptModel.setIdseq( rs.getString( "DEC_IDSEQ" ) );
//            dataElementConceptModel.setDecIdseq(rs.getString("DEC_IDSEQ"));
//            dataElementConceptModel.setCdIdseq(rs.getString("CD_IDSEQ"));
//            dataElementConceptModel.setProplName(rs.getString("PROPL_NAME"));
//            dataElementConceptModel.setOclName(rs.getString("OCL_NAME"));
//            dataElementConceptModel.setObjClassQualifier(rs.getString("OBJ_CLASS_QUALIFIER"));
//            dataElementConceptModel.setPropertyQualifier(rs.getString("PROPERTY_QUALIFIER"));
//            dataElementConceptModel.setChangeNote(rs.getString("CHANGE_NOTE"));
            dataElementConceptModel.setCreatedBy(rs.getString("CREATED_BY"));

            try
            {
                PropertyModel propertyModel = getPropertyDAO().getPropertyByIdseq( rs.getString( "PROP_IDSEQ" ) );
                if( propertyModel != null )
                {
                    dataElementConceptModel.setProperty( propertyModel );
                    if( propertyModel.getPreferredName() != null )
                    {
                        dataElementConceptModel.setPropertyPrefName( propertyModel.getPreferredName() );
                    }
                    if( propertyModel.getContext() != null && propertyModel.getContext().getName() != null )
                    {
                        dataElementConceptModel.setPropertyContextName( propertyModel.getContext().getName() );
                    }
                    if( propertyModel.getVersion() != null )
                    {
                        dataElementConceptModel.setPropertyVersion( propertyModel.getVersion() );
                    }
                }
            } catch( EmptyResultDataAccessException ex )
            {
                logger.warn( "no Property found for propIdseq: " + rs.getString( "PROP_IDSEQ" ) );
            }

            try
            {
                ObjectClassModel objectClassModel = getObjectClassDAO().getObjectClassByIdseq( rs.getString( "OC_IDSEQ" ) );
                if( objectClassModel != null )
                {
                    dataElementConceptModel.setObjectClassModel( objectClassModel );
                    dataElementConceptModel.setObjClassPublicId( objectClassModel.getPublicId() );
                    if( objectClassModel.getPreferredName() != null )
                    {
                        dataElementConceptModel.setObjClassPrefName( objectClassModel.getPreferredName() );
                    }
                    if( objectClassModel.getContext() != null && objectClassModel.getContext().getName() != null )
                    {
                        dataElementConceptModel.setObjClassContextName( objectClassModel.getContext().getName() );
                    }
                    if( objectClassModel.getVersion() != null )
                    {
                        dataElementConceptModel.setObjClassVersion( objectClassModel.getVersion() );
                    }
                }
            } catch( EmptyResultDataAccessException ex )
            {
                logger.warn( "no ObjectClassModel found for OC_IDSEQ: " + rs.getString( "OC_IDSEQ" ) );
            }
            try
            {
                ContextModel contextModel = getContextDAO().getContextByIdseq( rs.getString( "CONTE_IDSEQ" ) );
                if( contextModel != null && contextModel.getName() != null )
                {
                    dataElementConceptModel.setConteName( contextModel.getName() );
                }
            } catch( EmptyResultDataAccessException ex )
            {
                logger.warn( "no contextModel found for CONTE_IDSEQ: " + rs.getString( "CONTE_IDSEQ" ) );
            }
            try
            {
                ConceptualDomainModel conceptualDomainModel = getConceptualDomainDAO().getConceptualDomainByIdseq( rs.getString( "CD_IDSEQ" ) );
                if( conceptualDomainModel != null )
                {
                    logger.debug( "conceptualDomainModel.getVersion(): " + conceptualDomainModel.getVersion() );
                    dataElementConceptModel.setCdIdseq( rs.getString( "CD_IDSEQ" ) );
                    dataElementConceptModel.setCdPublicId( conceptualDomainModel.getCdId() );
                    if( conceptualDomainModel.getPreferredName() != null )
                    {
                        dataElementConceptModel.setCdPrefName( conceptualDomainModel.getPreferredName() );
                    }
                    if( conceptualDomainModel.getLongName() != null )//CDEBROWSER-816 Use CD Long name not Preferred name od CDE Details page
                    {
                        dataElementConceptModel.setCdLongName(conceptualDomainModel.getLongName());
                    }
                    if( conceptualDomainModel.getVersion() != null )
                    {
                        dataElementConceptModel.setCdVersion( conceptualDomainModel.getVersion() );
                    }
                    if( conceptualDomainModel.getContextModel() != null && conceptualDomainModel.getContextModel().getName() != null )
                    {
                        dataElementConceptModel.setCdContextName( conceptualDomainModel.getContextModel().getName() );
                    }
                    logger.debug( "dataElementConceptModel: " + dataElementConceptModel.toString() );
                }
            } catch( EmptyResultDataAccessException ex )
            {
                logger.warn( "no dataElementConceptModel found for CD_IDSEQ: " + rs.getString( "CD_IDSEQ" ) );
            }

            return dataElementConceptModel;
        }
    }

}
