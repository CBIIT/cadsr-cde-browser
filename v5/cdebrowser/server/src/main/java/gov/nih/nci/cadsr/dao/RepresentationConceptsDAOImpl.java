package gov.nih.nci.cadsr.dao;

import gov.nih.nci.cadsr.dao.model.RepresentationConceptModel;
import gov.nih.nci.cadsr.dao.model.RepresentationModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lernermh on 6/9/15.
 */
public class RepresentationConceptsDAOImpl extends AbstractDAOOperations implements RepresentationConceptsDAO
{
    private RepresentationDAO representationDAO;
    private ConceptDerivationRuleDAOImpl conceptDerivationRuleDAO;

    private Logger logger = LogManager.getLogger( RepresentationConceptsDAOImpl.class.getName() );
    private JdbcTemplate jdbcTemplate;

    @Autowired
    RepresentationConceptsDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }


    @Override
    public List<RepresentationConceptModel> getRepresentationConceptByRepresentationId( String representationId )
    {
        logger.debug( "representationModel(" + representationId + ")" );

        //select CON_DERIVATION_RULES_EXT.NAME   from sbrext.REPRESENTATIONS_EXT,CON_DERIVATION_RULES_EXT  where sbrext.REPRESENTATIONS_EXT.REP_ID = '3125303' and CON_DERIVATION_RULES_EXT.condr_idseq = sbrext.REPRESENTATIONS_EXT.CONDR_IDSEQ;

        // First get the list of Representation Concepts.Concept Codes
        String[] conceptCode = conceptDerivationRuleDAO.getCDRByByRepId( representationId ).getName().split(":");
        String primaryConcept = conceptCode[conceptCode.length - 1];
        logger.debug( "primaryConcept[" + primaryConcept + "]");

        StringBuilder whereClause = new StringBuilder( " where ");
        for( int f = 0; f < conceptCode.length; f++)
        {
            if( f > 0 )
            {
                whereClause.append( " or " );
            }
            whereClause.append( " preferred_name = '" + conceptCode[f] + "'" );
        }

        String sql = "select long_name as concept_name, preferred_name as concept_code, con_id as public_id, definition_source, evs_source from CONCEPTS_EXT " +
                whereClause.toString() +
                " order by concept_code";


        logger.debug( "representationModel(" + representationId + "): \n" + sql );

        List<RepresentationConceptModel> results = getAll( sql, RepresentationConceptModel.class );
        // The last concept code in the colon delimited list is the "Primary", others are No
        for( RepresentationConceptModel concept: results)
        {
            if( concept.getConceptCode().compareTo( primaryConcept ) == 0)
            {
                concept.setPrimary("Yes");
            }
            else
            {
                concept.setPrimary("No");
            }

            logger.debug( "RepresentationConceptModel: " + results.toString() );
        }

        // A quick hack to sort the list by the order of the Concept Codes in conceptDerivationRule
        List<RepresentationConceptModel> results2 = new ArrayList<>(  );
        for( int f = 0; f < conceptCode.length; f++)
        {
            for( RepresentationConceptModel concept: results)
            {
                if( concept.getConceptCode().compareTo(conceptCode[f]  ) == 0)
                {
                    results2.add( concept );
                    continue;
                }
            }
        }

        return results2;

    }

    @Override
    public List<RepresentationConceptModel> getRepresentationConceptByRepresentationId( int representationId )
    {
        return getRepresentationConceptByRepresentationId( new Integer( representationId ).toString() );
    }

    public ConceptDerivationRuleDAOImpl getConceptDerivationRuleDAO()
    {
        return conceptDerivationRuleDAO;
    }

    public void setConceptDerivationRuleDAO( ConceptDerivationRuleDAOImpl conceptDerivationRuleDAO )
    {
        this.conceptDerivationRuleDAO = conceptDerivationRuleDAO;
    }

    public RepresentationDAO getRepresentationDAO()
    {
        return representationDAO;
    }

    public void setRepresentationDAO( RepresentationDAO representationDAO )
    {
        this.representationDAO = representationDAO;
    }


}
