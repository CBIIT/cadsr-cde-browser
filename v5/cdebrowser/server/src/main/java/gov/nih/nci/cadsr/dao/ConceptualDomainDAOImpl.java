package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ConceptualDomainModel;
import gov.nih.nci.cadsr.dao.model.ContextModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConceptualDomainDAOImpl extends AbstractDAOOperations implements ConceptualDomainDAO
{
    private Logger logger = LogManager.getLogger( DataElementConceptDAOImpl.class.getName() );
    private JdbcTemplate jdbcTemplate;
    private ContextDAO contextDAO;

    @Autowired
    ConceptualDomainDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public ConceptualDomainModel getConceptualDomainByIdseq( String cdIdseq )
    {
        String sql = "SELECT * FROM sbr.conceptual_domains WHERE cd_idseq = ?";
        ConceptualDomainModel conceptualDomainModel = query( sql, cdIdseq, ConceptualDomainModel.class );
        // Use the conte_idseq to get the context
        conceptualDomainModel.setContextModel( getContextDAO().getContextByIdseq( conceptualDomainModel.getConteIdseq() ) );
        return conceptualDomainModel;
    }

    public ContextDAO getContextDAO()
    {
        return contextDAO;
    }

    public void setContextDAO( ContextDAO contextDAO )
    {
        this.contextDAO = contextDAO;
    }

/*
    public final class ConceptualDomainMapper extends BeanPropertyRowMapper<ConceptualDomainModel> {

        public ConceptualDomainModel mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConceptualDomainModel conceptualDomainModel = new ConceptualDomainModel();

            conceptualDomainModel.setPreferredName(rs.getString("PREFERRED_NAME"));
            conceptualDomainModel.setVersion(rs.getFloat("VERSION"));
            conceptualDomainModel.setCdId(rs.getInt("CD_ID"));
            conceptualDomainModel.setContextModel(getContextDAO().getContextByIdseq(rs.getString("CONTE_IDSEQ")));

            return conceptualDomainModel;
        }
    }
*/
}
