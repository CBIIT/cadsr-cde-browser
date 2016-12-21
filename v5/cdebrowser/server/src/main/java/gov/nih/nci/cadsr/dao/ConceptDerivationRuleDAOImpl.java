package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ConceptDerivationRuleModel;
import gov.nih.nci.cadsr.dao.model.ContextModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ConceptDerivationRuleDAOImpl extends AbstractDAOOperations implements ConceptDerivationRuleDAO
{

    private Logger logger = LogManager.getLogger( DataElementConceptDAOImpl.class.getName() );

    private JdbcTemplate jdbcTemplate;
    private PropertyDAO propertyDAO;
    private ObjectClassDAO objectClassDAO;


    @Autowired
    ConceptDerivationRuleDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public ConceptDerivationRuleModel getCDRByIdseq( String condrIdseq ) throws EmptyResultDataAccessException
    {
        String sql = "SELECT * FROM sbrext.con_derivation_rules_ext WHERE condr_idseq = ?";
        if( condrIdseq == null)
        {
            System.err.println("ConceptDerivationRuleDAOImpl.getCDRByIdseq  condrIdseq == null ");
        }

        ConceptDerivationRuleModel conceptDerivationRuleModel = jdbcTemplate.queryForObject( sql, new Object[]{ condrIdseq }, new ConceptDerivationRuleMapper() );
        return conceptDerivationRuleModel;
    }


    @Override
    /**
     * Returns a conceptDerivationRuleModel for a Value Domain's Representation
     */
    public ConceptDerivationRuleModel getCDRByRepId( String repId )
    {
        String sql = "SELECT con_derivation_rules_ext.* FROM sbrext.representations_ext,con_derivation_rules_ext  WHERE sbrext.representations_ext.rep_id = ? AND con_derivation_rules_ext.condr_idseq = sbrext.representations_ext.condr_idseq";
        logger.debug( ">>>>>>> " + sql.replace( "?", repId ) );
        ConceptDerivationRuleModel conceptDerivationRuleModel = query( sql, repId, ConceptDerivationRuleModel.class );
        return conceptDerivationRuleModel;
    }


    public final class ConceptDerivationRuleMapper extends BeanPropertyRowMapper<ConceptDerivationRuleModel>
    {

        public ConceptDerivationRuleModel mapRow( ResultSet rs, int rowNum ) throws SQLException
        {
            ConceptDerivationRuleModel conceptDerivationRuleModel = new ConceptDerivationRuleModel();
            // don't know what needs to be mapped yet
            return conceptDerivationRuleModel;
        }
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
}
