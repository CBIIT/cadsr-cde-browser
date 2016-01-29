package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.ConceptModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;


public class ConceptDAOImpl extends AbstractDAOOperations implements ConceptDAO
{
    private Logger logger = LogManager.getLogger( ConceptDAOImpl.class.getName() );

    private JdbcTemplate jdbcTemplate;

    public ConceptDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }

    @Override
    public List<ConceptModel> getConceptByConceptCode( String conceptCodeStr )
    {
        logger.debug( "getConceptByConceptCode(" + conceptCodeStr + ")" );
        if( conceptCodeStr == null )
        {
            return null;
        }
        // First get the list of Representation Concepts.Concept Codes
        String[] conceptCode = conceptCodeStr.split( ":" );

        String primaryConcept = conceptCode[conceptCode.length - 1];
        logger.debug( "primaryConcept[" + primaryConcept + "]" );

        StringBuilder whereClause = new StringBuilder( " where " );
        for( int f = 0; f < conceptCode.length; f++ )
        {
            if( f > 0 )
            {
                whereClause.append( " or " );
            }
            whereClause.append( " preferred_name = '" + conceptCode[f] + "'" );
        }

        String sql = "SELECT long_name AS concept_name, preferred_name AS concept_code, con_id as public_id, definition_source, evs_source FROM concepts_ext " +
                whereClause.toString() +
                " ORDER BY concept_code";


        List<ConceptModel> results = getAll( sql, ConceptModel.class );
        // The last concept code in the colon delimited list is the "Primary", others are No
        for( ConceptModel concept : results )
        {
            if( concept.getConceptCode().compareTo( primaryConcept ) == 0 )
            {
                concept.setPrimary( "Yes" );
            }
            else
            {
                concept.setPrimary( "No" );
            }

            logger.debug( "ConceptModel: " + results.toString() );
        }

        // A quick hack to sort the list by the order of the Concept Codes in conceptDerivationRule
        List<ConceptModel> results2 = new ArrayList<>();
        for( int f = 0; f < conceptCode.length; f++ )
        {
            for( ConceptModel concept : results )
            {
                if( concept.getConceptCode().compareTo( conceptCode[f] ) == 0 )
                {
                    results2.add( concept );
                    continue;
                }
            }
        }

        return results2;

    }
}
