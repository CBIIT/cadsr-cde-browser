package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import gov.nih.nci.cadsr.dao.model.SearchModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import gov.nih.nci.cadsr.dao.operation.SearchQueryBuilder;
import gov.nih.nci.cadsr.model.SearchPreferencesServer;
import gov.nih.nci.cadsr.service.model.search.SearchCriteria;


public class SearchDAOImpl extends AbstractDAOOperations implements SearchDAO
{
	private static Logger logger = LogManager.getLogger( SearchDAOImpl.class.getName() );
	private JdbcTemplate jdbcTemplate;

	private SearchQueryBuilder searchQueryBuilder;

	public SearchDAOImpl()
    {
    }

    @Autowired
    SearchDAOImpl( DataSource dataSource )
    {
        setDataSource( dataSource );
        jdbcTemplate = getJdbcTemplate();
    }

    /**
     *
     * @param clientName        The text the user put in the search text field in the UI.
     * @param clientSearchMode   Exact phrase, All of the words, OR At least one of the words.
     * @param clientSearchField  0 if user select Name field, 1 for Public ID.
     * @param programArea        Empty if All
     * @param workFlowStatus     de.asl_name
     *
     *                           This exclude list comes from common.WorkflowStatusEnum#getExcludList():
     *                           If empty, AND de.asl_name NOT IN ('CMTE APPROVED',
     *                           'CMTE SUBMTD',
     *                           'CMTE SUBMTD USED',
     *                           'RETIRED ARCHIVED',
     *                           'RETIRED PHASED OUT',
     *                           'RETIRED WITHDRAWN')
     * @param registrationStatus sbr.ac_registrations_view - registration_status
     * @param conceptName
     * @param conceptCode
     * @return
     */
    public List<SearchModel> getAllContexts(SearchCriteria searchCriteria, SearchPreferencesServer searchPreferences,
    		List<String> allowedWorkflowStatuses, List<String> allowedRegStatuses)
    {
        
    	String sqlStmt = searchQueryBuilder.initSearchQueryBuilder(searchCriteria, searchPreferences, 
    		allowedWorkflowStatuses, allowedRegStatuses);

        List<SearchModel> results;

        // If searchQueryBuilder.initSearchQueryBuilder returns null, the query built can not return any results.
        if( sqlStmt != null)
        {
            results = getAll( sqlStmt, SearchModel.class );
        }
        else
        {
            // Return an empty list
            results = new ArrayList<>();
        }

        return results;
    }

    public List<SearchModel> getAllContexts( String sql)
    {
        List<SearchModel> results;

        //logger.debug( "getAllContexts Search" );
        results = getAll( sql, SearchModel.class );
        //logger.debug( "Done getAllContexts Search" );

        return results;
    }
    
    //CDEBROWSER-616
    //removing joins with two tables "sbr.reg_status_lov_view rsl" and "sbr.ac_status_lov_view asl" which were used to retrieve 'display_order' values only.
    @Override
    public List<SearchModel> cdeByProtocolForm( String id )
    {
        //I do not see this query uses search preferences
    	String sql = "SELECT DISTINCT de.de_idseq ,\n" +
                "                de.preferred_name de_preferred_name ,\n" +
                "                de.long_name ,\n" +
                "                rd.doc_text ,\n" +
                "                conte.NAME ,\n" +
                "                de.asl_name ,\n" +
                "                To_char(de.cde_id)                       de_cdeid ,\n" +
                "                de.version                               de_version ,\n" +
                "                meta_config_mgmt.Get_usedby(de.de_idseq) de_usedby ,\n" +
                "                de.vd_idseq ,\n" +
                "                de.dec_idseq ,\n" +
                "                de.conte_idseq ,\n" +
                "                de.preferred_definition ,\n" +
                "                acr.registration_status ,\n" +
                "                de.cde_id         cdeid\n" +
                "FROM            sbr.data_elements_view de ,\n" +
                "                sbr.reference_documents_view rd ,\n" +
                "                sbr.contexts_view conte,\n" +
                "                sbrext.quest_contents_view_ext qc ,\n" +
                "                sbr.ac_registrations_view acr \n" +
                "WHERE           de.de_idseq = rd.ac_idseq (+)\n" +
                "AND             rd.dctl_name (+) = 'Preferred Question Text'\n" +
                "AND             de.asl_name != 'RETIRED DELETED'\n" +
                "AND             conte.conte_idseq = de.conte_idseq\n" +
                "AND             qc.dn_crf_idseq = '" + id + "'\n" +
                "AND             qc.qtl_name = 'QUESTION'\n" +
                "AND             qc.de_idseq = de.de_idseq\n" +
                "AND             de.de_idseq = acr.ac_idseq (+)\n"
                ;

        return getAllContexts( sql );
    }

    @Override
    public List<SearchModel> cdeByProtocol( String protocolId, SearchPreferencesServer searchPreferences )
    {
    	String sql = buildSqlCdeByProtocol( protocolId, searchPreferences);
        return getAllContexts( sql );
    }

    protected String buildSqlCdeByProtocol(String protocolId, SearchPreferencesServer searchPreferences) {
        String sql = "SELECT DISTINCT de.de_idseq ,\n" +
                "                de.preferred_name de_preferred_name ,\n" +
                "                de.long_name ,\n" +
                "                rd.doc_text ,\n" +
                "                conte.NAME ,\n" +
                "                de.asl_name ,\n" +
                "                To_char(de.cde_id)                       de_cdeid ,\n" +
                "                de.version                               de_version ,\n" +
                "                meta_config_mgmt.Get_usedby(de.de_idseq) de_usedby ,\n" +
                "                de.vd_idseq ,\n" +
                "                de.dec_idseq ,\n" +
                "                de.conte_idseq ,\n" +
                "                de.preferred_definition ,\n" +
                "                acr.registration_status ,\n" +
                "                de.cde_id         cdeid\n" +
                "FROM            sbr.data_elements_view de ,\n" +
                "                sbr.reference_documents_view rd ,\n" +
                "                sbr.contexts_view conte,\n" +
                "                sbrext.quest_contents_view_ext frm ,\n" +
                "                sbrext.protocol_qc_ext ptfrm ,\n" +
                "                sbrext.protocols_view_ext pt ,\n" +
                "                sbrext.quest_contents_view_ext qc ,\n" +
                "                sbr.ac_registrations_view acr \n" +
                "WHERE           de.de_idseq = rd.ac_idseq (+)\n" +
                "AND             rd.dctl_name (+) = 'Preferred Question Text'\n" +
                //add excluded Registration status
                buildRegistrationStatusExcludedSql(searchPreferences) +
                //add excluded Workflow status
                buildWorkflowStatusExcludedSql(searchPreferences) +
                //add excluded Contexts
                buildContextExcludedSql(searchPreferences) +
                //"AND             de.asl_name != 'RETIRED DELETED'\n" + //removing this status from SQL statement. This status is added to Search Preferences as of release 5.2
                "AND             conte.conte_idseq = de.conte_idseq\n" +
                "AND             pt.proto_idseq = ptfrm.proto_idseq\n" +
                "AND             frm.qc_idseq = ptfrm.qc_idseq\n" +
                "AND             frm.qtl_name = 'CRF'\n" +
                "AND             qc.dn_crf_idseq = frm.qc_idseq\n" +
                "AND             qc.qtl_name = 'QUESTION'\n" +
                "AND             qc.de_idseq = de.de_idseq\n" +
                "AND             pt.proto_idseq = '" + protocolId + "'\n" +
                "AND             de.de_idseq = acr.ac_idseq (+)\n";
        return sql;
    }
    @Override
    public List<SearchModel> cdeByContextClassificationSchemeItem(String classificationSchemeItemId, SearchPreferencesServer searchPreferences)
    {
    	String sql = buildCdeByContextClassificationSchemeItem(classificationSchemeItemId, searchPreferences);

        return getAllContexts( sql );
    }
    protected String buildCdeByContextClassificationSchemeItem(String classificationSchemeItemId, SearchPreferencesServer searchPreferences) {
        String sql = " SELECT DISTINCT de.de_idseq ,\n" +
                "                de.preferred_name de_preferred_name ,\n" +
                "                de.long_name ,\n" +
                "                rd.doc_text ,\n" +
                "                conte.NAME ,\n" +
                "                de.asl_name ,\n" +
                "                To_char(de.cde_id)                       de_cdeid ,\n" +
                "                de.version                               de_version ,\n" +
                "                meta_config_mgmt.Get_usedby(de.de_idseq) de_usedby ,\n" +
                "                de.vd_idseq ,\n" +
                "                de.dec_idseq ,\n" +
                "                de.conte_idseq ,\n" +
                "                de.preferred_definition ,\n" +
                "                acr.registration_status ,\n" +
                "                de.cde_id         cdeid\n" +
                "FROM            sbr.data_elements_view de ,\n" +
                "                sbr.reference_documents_view rd ,\n" +
                "                sbr.contexts_view conte,\n" +
                "                sbr.ac_csi_view acs ,\n" +
                "                sbr.ac_registrations_view acr \n" +
                "WHERE           de.de_idseq = rd.ac_idseq (+)\n" +
                "AND             rd.dctl_name (+) = 'Preferred Question Text'\n" +
                //add excluded Registration status
                buildRegistrationStatusExcludedSql(searchPreferences) +
                //add excluded Workflow status
                buildWorkflowStatusExcludedSql(searchPreferences) +
                //add excluded Contexts
                buildContextExcludedSql(searchPreferences) +
                //"AND             de.asl_name != 'RETIRED DELETED'\n" + //removing this status from SQL statement. This status is added to Search Preferences as of release 5.2
                "AND             conte.conte_idseq = de.conte_idseq\n" +
                "AND             acs.cs_csi_idseq = '" + classificationSchemeItemId + "'\n" +
                "AND             acs.ac_idseq = de.de_idseq\n" +
                "AND             de.de_idseq = acr.ac_idseq (+)\n"
                ;
        return sql;
    }
    @Override
    public List<SearchModel> cdeOwnedAndUsedByContext( String conteId, SearchPreferencesServer searchPreferences)
    {
        String sql = buildCdeOwnedAndUsedByContext(conteId, searchPreferences);

        return getAllContexts( sql );
    }
    protected String buildCdeOwnedAndUsedByContext( String conteId, SearchPreferencesServer searchPreferences)
    {
        String sql = " SELECT DISTINCT de.de_idseq,\n" +
                "                de.preferred_name                        de_preferred_name,\n" +
                "                de.long_name,\n" +
                "                rd.doc_text,\n" +
                "                conte.name,\n" +
                "                de.asl_name,\n" +
                "                To_char(de.cde_id)                       de_cdeid,\n" +
                "                de.version                               de_version,\n" +
                "                meta_config_mgmt.Get_usedby(de.de_idseq) de_usedby,\n" +
                "                de.vd_idseq,\n" +
                "                de.dec_idseq,\n" +
                "                de.conte_idseq,\n" +
                "                de.preferred_definition,\n" +
                "                acr.registration_status,\n" +
                "                de.cde_id                                cdeid\n" +
                "FROM   sbr.data_elements_view de,\n" +
                "       sbr.reference_documents_view rd,\n" +
                "       sbr.contexts_view conte,\n" +
                "       sbr.ac_registrations_view acr\n" +
                "WHERE  de.de_idseq = rd.ac_idseq (+)\n" +
                "       AND rd.dctl_name (+) = 'Preferred Question Text'\n" +
		                //add excluded Registration status
		                buildRegistrationStatusExcludedSql(searchPreferences) +
		                //add excluded Workflow status
		                buildWorkflowStatusExcludedSql(searchPreferences) +
		                //add excluded Contexts
		                buildContextExcludedSql(searchPreferences) +
                //"       AND de.asl_name != 'RETIRED DELETED'\n" + //removing this status from SQL statement. This status is added to Search Preferences as of release 5.2
                "       AND conte.conte_idseq = de.conte_idseq\n" +
                "       AND de.de_idseq IN (SELECT ac_idseq\n" +
                "                           FROM   sbr.designations_view des\n" +
                "                           WHERE\n" +
                "           des.conte_idseq = '" + conteId + "'\n" +
                "           AND des.detl_name = 'USED_BY'\n" +
                "                           UNION\n" +
                "                           SELECT de_idseq\n" +
                "                           FROM   sbr.data_elements_view de1\n" +
                "                           WHERE\n" +
                "           de1.conte_idseq = '" + conteId + "')\n" +
                "       AND de.de_idseq = acr.ac_idseq (+)\n"
                ;
        return sql;

    }

///////////////////////////////////////////////////////////////////////
    @Override
    public List<SearchModel> cdeByContextClassificationScheme( String classificationSchemeId, SearchPreferencesServer searchPreferences )
    {
        String sql = buildCdeByContextClassificationScheme(classificationSchemeId, searchPreferences);
        return getAllContexts( sql );
    }

    protected String buildCdeByContextClassificationScheme( String classificationSchemeId, SearchPreferencesServer searchPreferences )
    {
        String sql = " SELECT DISTINCT de.de_idseq ,\n" +
                "                de.preferred_name de_preferred_name ,\n" +
                "                de.long_name ,\n" +
                "                rd.doc_text ,\n" +
                "                conte.NAME ,\n" +
                "                de.asl_name ,\n" +
                "                To_char(de.cde_id)                       de_cdeid ,\n" +
                "                de.version                               de_version ,\n" +
                "                meta_config_mgmt.Get_usedby(de.de_idseq) de_usedby ,\n" +
                "                de.vd_idseq ,\n" +
                "                de.dec_idseq ,\n" +
                "                de.conte_idseq ,\n" +
                "                de.preferred_definition ,\n" +
                "                acr.registration_status ,\n" +
                "                de.cde_id         cdeid\n" +
                "FROM            sbr.data_elements_view de ,\n" +
                "                sbr.reference_documents_view rd ,\n" +
                "                sbr.contexts_view conte ,\n" +
                "                sbr.ac_registrations_view acr \n" +
                "WHERE           de.de_idseq = rd.ac_idseq (+)\n" +
                "AND             rd.dctl_name (+) = 'Preferred Question Text'\n" +
                //add excluded Registration status
                buildRegistrationStatusExcludedSql(searchPreferences) +
                //add excluded Workflow status
                buildWorkflowStatusExcludedSql(searchPreferences) +
                //add excluded Contexts
                buildContextExcludedSql(searchPreferences) +
                //"AND             de.asl_name != 'RETIRED DELETED'\n" + //removing this status from SQL statement. This status is added to Search Preferences as of release 5.2
                "AND             conte.conte_idseq = de.conte_idseq\n" +
                "AND             de.de_idseq = acr.ac_idseq (+)\n" +
                "AND             de.de_idseq IN\n" +
                "                (\n" +
                "                       SELECT de_idseq\n" +
                "                       FROM   sbr.data_elements_view de ,\n" +
                "                              sbr.ac_csi_view acs,\n" +
                "                              sbr.cs_csi_view csc\n" +
                "                       WHERE  csc.cs_idseq = '" + classificationSchemeId + "'\n" +
                "                       AND    csc.cs_csi_idseq = acs.cs_csi_idseq\n" +
                "                       AND    acs.ac_idseq = de_idseq )";

        return sql;
    }

	public SearchQueryBuilder getSearchQueryBuilder() {
		return searchQueryBuilder;
	}

	public void setSearchQueryBuilder(SearchQueryBuilder searchQueryBuilder) {
		this.searchQueryBuilder = searchQueryBuilder;
	}

	protected String buildWorkflowStatusExcludedSql(SearchPreferencesServer searchPreferences) {
		String strList;
		if ((searchPreferences != null) && ((strList = searchPreferences.buildExcludedWorkflowSql()) != null)) {
			return "AND de.asl_name NOT IN " + strList + "\n";
		}
		else return "";
	}
	protected String buildRegistrationStatusExcludedSql(SearchPreferencesServer searchPreferences) {
		String strList;
		if  ((searchPreferences != null) && ((strList = searchPreferences.buildExcludedRegistrationSql()) != null))
			return "AND nvl(acr.registration_status,'-1') NOT IN " + strList + "\n";
		else return "";
	}
	protected String buildContextExcludedSql(SearchPreferencesServer searchPreferences) {
		String strList;
		if  ((searchPreferences != null) && ((strList = searchPreferences.buildContextExclided()) != null))
			return "AND conte.NAME NOT IN ( " + strList + ")\n";
		else return "";
	}
    /////////////////////////////////////////////////////////////////////

}
