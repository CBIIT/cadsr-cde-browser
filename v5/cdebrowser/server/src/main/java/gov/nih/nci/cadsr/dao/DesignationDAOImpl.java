package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.sql.DataSource;

import gov.nih.nci.cadsr.service.model.search.SearchCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import gov.nih.nci.cadsr.dao.model.AcAttCsCsiModel;
import gov.nih.nci.cadsr.dao.model.CsCsiModel;
import gov.nih.nci.cadsr.dao.model.DesignationModel;
import gov.nih.nci.cadsr.dao.model.DesignationModelAlt;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;

public class DesignationDAOImpl extends AbstractDAOOperations implements DesignationDAO
{
    private Logger logger = LogManager.getLogger( DesignationDAOImpl.class.getName() );

    private JdbcTemplate jdbcTemplate;
    private ContextDAO contextDAO;
    private AcAttCsCsiDAO acAttCsCsiDAO;

    @Autowired
    DesignationDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public List<DesignationModel> getDesignationModelsByAcIdseq( String acIdseq )
    {

        String sql = "SELECT * FROM sbr.designations WHERE ac_idseq = ?";
        List<DesignationModel> designationModels = jdbcTemplate.query( sql, new Object[]{ acIdseq }, new DesignationMapper( DesignationModel.class ) );
        return designationModels;
    }
    @Override
    public List<DesignationModelAlt> getDesignationModelsNoClsssification( String acIdseq )
    {

        String sql = "SELECT des.NAME des_name, des.DETL_NAME des_type, des.DESIG_IDSEQ, des.LAE_NAME lang, conte.NAME context_name " +
        		"FROM designations des inner join sbr.contexts conte  on conte.CONTE_IDSEQ = des.CONTE_IDSEQ WHERE ac_idseq = ?";
        List<DesignationModelAlt> designationModels = jdbcTemplate.query( sql, new Object[]{ acIdseq }, new DesignationMapperContext());
        return designationModels;
    }

    @Override
    public List<DesignationModel> getUsedByDesignationModels( String acIdseq )
    {

        String sql = "SELECT * FROM sbr.designations WHERE ac_idseq = ? AND detl_name = 'USED_BY'";
        List<DesignationModel> designationModels = jdbcTemplate.query( sql, new Object[]{ acIdseq }, new DesignationMapper( DesignationModel.class ) );
        return designationModels;
    }

    @Override
    public List<String> getAllDesignationModelTypes()
    {

        String sql = "SELECT distinct(DETL_NAME) FROM sbr.designations order by upper(DETL_NAME)";
        List<String> dModelTypes = (List<String>) jdbcTemplate.queryForList(sql, String.class);
        List<String> designationModelTypes = new ArrayList<String>();
        designationModelTypes.add( SearchCriteria.ALL_ALTNAME_TYPES);
        for (String dtype : dModelTypes)
        {
        	designationModelTypes.add(dtype);
        }
        return designationModelTypes;
    }

    public ContextDAO getContextDAO()
    {
        return contextDAO;
    }

    public void setContextDAO( ContextDAO contextDAO )
    {
        this.contextDAO = contextDAO;
    }

    public AcAttCsCsiDAO getAcAttCsCsiDAO()
    {
        return acAttCsCsiDAO;
    }

    public void setAcAttCsCsiDAO( AcAttCsCsiDAO acAttCsCsiDAO )
    {
        this.acAttCsCsiDAO = acAttCsCsiDAO;
    }

    public final class DesignationMapper extends BeanPropertyRowMapper<DesignationModel>
    {
        private Logger logger = LogManager.getLogger( DesignationMapper.class.getName() );

        public DesignationMapper( Class<DesignationModel> mappedClass )
        {
            super( mappedClass );
        }

        public DesignationModel mapRow( ResultSet rs, int rowNum ) throws SQLException
        {
            DesignationModel designationModel = super.mapRow( rs, rowNum );

//            designationModel.setName(rs.getString("NAME"));
            designationModel.setType( rs.getString( "DETL_NAME" ) ); // duplicate
            designationModel.setDesigIDSeq( rs.getString( "DESIG_IDSEQ" ) );
            designationModel.setLang( rs.getString( "LAE_NAME" ) );
//            designationModel.setDetlName("DETL_NAME");

            try
            {
                designationModel.setContex( getContextDAO().getContextByIdseq( rs.getString( "CONTE_IDSEQ" ) ) );
            } catch( EmptyResultDataAccessException ex )
            {
                //logger.warn( "no Context found for Definition using context idseq: " + rs.getString( "CONTE_IDSEQ" ) );
            }

            try
            {
                // getAllAcAttCsCsiByAttIdseq() is pretty inefficient.  Could be moved into this DAO and just fetch DISTINCT cs_csi.csi_idseq
                List<AcAttCsCsiModel> acAttCsCsiModels = getAcAttCsCsiDAO().getAllAcAttCsCsiByAttIdseq( rs.getString( "DESIG_IDSEQ" ) );
                if( acAttCsCsiModels != null && acAttCsCsiModels.size() > 0 )
                {
                    designationModel.setCsiIdseqs( new HashSet<String>( acAttCsCsiModels.size() ) );
                    for( AcAttCsCsiModel acAttCsCsiModel : acAttCsCsiModels )
                    {
                        if( acAttCsCsiModel.getCsCsiIdseq() != null )
                        {
                            designationModel.getCsiIdseqs().add( acAttCsCsiModel.getCsiIdseq() );
                        }
                    }
                }
                else
                {
                    // this designation is unclassified
                    designationModel.setCsiIdseqs( new HashSet<String>( 1 ) );
                    designationModel.getCsiIdseqs().add( CsCsiModel.UNCLASSIFIED );
                }
            } catch( EmptyResultDataAccessException ex )
            {
                //logger.warn( "no CSIs found for Definition: " + rs.getString( "DESIG_IDSEQ" ) );
                // this designation is unclassified
                designationModel.setCsiIdseqs( new HashSet<String>( 1 ) );
                designationModel.getCsiIdseqs().add( CsCsiModel.UNCLASSIFIED );
            }
            return designationModel;
        }
    }
    public final class DesignationMapperContext implements RowMapper<DesignationModelAlt>{

        public DesignationMapperContext( )
        {

        }

        public DesignationModelAlt mapRow( ResultSet rs, int rowNum ) throws SQLException
        {
        	DesignationModelAlt designationModel = new DesignationModelAlt();

            designationModel.setDesigIdseq(rs.getString("DESIG_IDSEQ"));
            designationModel.setName(rs.getString("des_name"));
            designationModel.setType(rs.getString("des_type"));
            designationModel.setLang(rs.getString("lang"));
            designationModel.setContextName(rs.getString("context_name"));

            return designationModel;
        }
    }
}
