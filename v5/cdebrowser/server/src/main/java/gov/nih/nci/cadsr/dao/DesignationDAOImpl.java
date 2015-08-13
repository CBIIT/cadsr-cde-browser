package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.AcAttCsCsiModel;
import gov.nih.nci.cadsr.dao.model.CsCsiModel;
import gov.nih.nci.cadsr.dao.model.DesignationModel;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
    public List<DesignationModel> getUsedByDesignationModels( String acIdseq )
    {

        String sql = "SELECT * FROM sbr.designations WHERE ac_idseq = ? AND detl_name = 'USED_BY'";
        List<DesignationModel> designationModels = jdbcTemplate.query( sql, new Object[]{ acIdseq }, new DesignationMapper( DesignationModel.class ) );
        return designationModels;
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
            }
            catch( EmptyResultDataAccessException ex )
            {
                logger.warn( "no Context found for Definition using context idseq: " + rs.getString( "CONTE_IDSEQ" ) );
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
            }
            catch( EmptyResultDataAccessException ex )
            {
                logger.warn( "no CSIs found for Definition: " + rs.getString( "DESIG_IDSEQ" ) );
                // this designation is unclassified
                designationModel.setCsiIdseqs( new HashSet<String>( 1 ) );
                designationModel.getCsiIdseqs().add( CsCsiModel.UNCLASSIFIED );
            }
            return designationModel;
        }
    }
}
