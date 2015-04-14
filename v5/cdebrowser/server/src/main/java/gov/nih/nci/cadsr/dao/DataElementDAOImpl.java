package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.*;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;


import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class DataElementDAOImpl  extends AbstractDAOOperations implements DataElementDAO
{
    private Logger logger = LogManager.getLogger( DataElementDAOImpl.class.getName() );

    private JdbcTemplate jdbcTemplate;

    //private String DataElementSql; //Spring DAOs are singletons.  Doing this whole passing sql around as member variables is totally not safe...


    @Autowired
    DataElementDAOImpl( DataSource dataSource ) {
        setDataSource(dataSource);
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public List<DataElementModel> getCdeBySearchString(String DataElementSql)
    {
        List<DataElementModel>  results;

        logger.debug( "basicSearch");
        //logger.debug( ">>>>>>> "+ sql );
        results = getAll( DataElementSql, DataElementModel.class );
        //logger.debug( sql + " <<<<<<<" );
        logger.debug( "Done basicSearch");

        return results;
    }

//    @Override
//    public DataElementModel getCdeByCdeIdseq(String CdeIdseq) {
//
//    }

    /*public String getDataElementSql()
    {
        return DataElementSql;
    }

    //@Override
    public void setDataElementSql( String dataElementSql )
    {
        this.DataElementSql = dataElementSql;
    }*/

    public final class DataElementMapper extends BeanPropertyRowMapper<DataElementModel> {

        public DataElementModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            DataElementModel dataElementModel = new DataElementModel();
            /* need to map these members:
            List<ReferenceDocModel> refDocs;
            List<DesignationModel> designationModels;
            Integer publicId;
            String idseq;
            String registrationStatus;
            ValueDomainModel valueDomainModel;
            DataElementConceptModel dec;
            ContextModel context; */
 //           dataElementModel.setContext(ContextDAO.getContextByIdseq(rs.getString("CONTE_IDSEQ")));
            return dataElementModel;
        }
    }

}
