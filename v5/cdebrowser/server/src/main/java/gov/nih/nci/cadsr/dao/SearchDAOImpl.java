package gov.nih.nci.cadsr.dao;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.dao.model.SearchModel;
import gov.nih.nci.cadsr.dao.operation.AbstractDAOOperations;
import gov.nih.nci.cadsr.dao.operation.SearchQueryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;


public class SearchDAOImpl extends AbstractDAOOperations implements SearchDAO
{
    private Logger logger = LogManager.getLogger( SearchDAOImpl.class.getName() );
    private JdbcTemplate jdbcTemplate;


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
     * @param clientQuery        The text the user put in the search text field in the UI.
     * @param clientSearchMode   Exact phrase, All of the words, OR At least one of the words.
     * @param clientSearchField  0 if user select Name field, 1 for Public ID.
     * @param programArea        Empty if All
     * @param workFlowStatus     sbr.ac_status_lov_view - asl_name
     *
     *                           This exclude list comes from common.WorkflowStatusEnum#getExcludList():
     *                           If empty, AND asl.asl_name NOT IN ('CMTE APPROVED',
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
    public List<SearchModel> getAllContexts(
            String clientQuery, String clientSearchMode, int clientSearchField,
            String programArea, String context, String classification, String protocol,
            String workFlowStatus, String registrationStatus,
            String conceptName, String conceptCode )
    {
        SearchQueryBuilder searchQueryBuilder = new SearchQueryBuilder( clientQuery, clientSearchMode, clientSearchField, programArea,
                context, classification, protocol,
                workFlowStatus, registrationStatus,
                conceptName, conceptCode );

        List<SearchModel> results;

        //logger.debug( "Start Search: " + searchQueryBuilder.getSqlStmt() );
        results = getAll( searchQueryBuilder.getSqlStmt(), SearchModel.class );
        //logger.debug( "Done Search" );

        return results;
    }

    public List<SearchModel> getAllContexts(
            String clientQuery, String clientSearchMode, int clientSearchField,
            String programArea)
    {
        return getAllContexts(clientQuery, clientSearchMode, clientSearchField, programArea,"","","","","","","" );
    }

    public List<SearchModel> getAllContexts( String sql )
    {
        List<SearchModel> results;

        //logger.debug( "getAllContexts Search" );
        results = getAll( sql, SearchModel.class );
        //logger.debug( "Done getAllContexts Search" );

        return results;
    }



    public List<SearchModel> cdeByProtocolForm( String id )
    {
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
                "                rsl.display_order ,\n" +
                "                asl.display_order wkflow_order ,\n" +
                "                de.cde_id         cdeid\n" +
                "FROM            sbr.data_elements_view de ,\n" +
                "                sbr.reference_documents_view rd ,\n" +
                "                sbr.contexts_view conte,\n" +
                "                sbrext.quest_contents_view_ext qc ,\n" +
                "                sbr.ac_registrations_view acr ,\n" +
                "                sbr.reg_status_lov_view rsl ,\n" +
                "                sbr.ac_status_lov_view asl\n" +
                "WHERE           de.de_idseq = rd.ac_idseq (+)\n" +
                "AND             rd.dctl_name (+) = 'Preferred Question Text'\n" +
                "AND             de.asl_name != 'RETIRED DELETED'\n" +
                "AND             conte.conte_idseq = de.conte_idseq\n" +
                "AND             qc.dn_crf_idseq = '" + id + "'\n" +
                "AND             qc.qtl_name = 'QUESTION'\n" +
                "AND             qc.de_idseq = de.de_idseq\n" +
                "AND             de.de_idseq = acr.ac_idseq (+)\n" +
                "AND             acr.registration_status = rsl.registration_status (+)\n" +
                "AND             de.asl_name = asl.asl_name (+)";

        return getAllContexts( sql );
    }


    public List<SearchModel> cdeByProtocol( String protocolId )
    {
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
                "                rsl.display_order ,\n" +
                "                asl.display_order wkflow_order ,\n" +
                "                de.cde_id         cdeid\n" +
                "FROM            sbr.data_elements_view de ,\n" +
                "                sbr.reference_documents_view rd ,\n" +
                "                sbr.contexts_view conte,\n" +
                "                sbrext.quest_contents_view_ext frm ,\n" +
                "                sbrext.protocol_qc_ext ptfrm ,\n" +
                "                sbrext.protocols_view_ext pt ,\n" +
                "                sbrext.quest_contents_view_ext qc ,\n" +
                "                sbr.ac_registrations_view acr ,\n" +
                "                sbr.reg_status_lov_view rsl ,\n" +
                "                sbr.ac_status_lov_view asl\n" +
                "WHERE           de.de_idseq = rd.ac_idseq (+)\n" +
                "AND             rd.dctl_name (+) = 'Preferred Question Text'\n" +
                "AND             nvl(acr.registration_status,'-1') NOT IN ( 'Retired' )\n" +
                "AND             asl.asl_name NOT                      IN ( 'CMTE APPROVED' ,\n" +
                "                                                          'CMTE SUBMTD' ,\n" +
                "                                                          'CMTE SUBMTD USED' ,\n" +
                "                                                          'RETIRED ARCHIVED' ,\n" +
                "                                                          'RETIRED PHASED OUT' ,\n" +
                "                                                          'RETIRED WITHDRAWN' )\n" +
                "AND             conte.NAME NOT IN ( 'TEST',\n" +
                "                                   'Training' )\n" +
                "AND             de.asl_name != 'RETIRED DELETED'\n" +
                "AND             conte.conte_idseq = de.conte_idseq\n" +
                "AND             pt.proto_idseq = ptfrm.proto_idseq\n" +
                "AND             frm.qc_idseq = ptfrm.qc_idseq\n" +
                "AND             frm.latest_version_ind = 'Yes'\n" +
                "AND             frm.qtl_name = 'CRF'\n" +
                "AND             qc.dn_crf_idseq = frm.qc_idseq\n" +
                "AND             qc.qtl_name = 'QUESTION'\n" +
                "AND             qc.de_idseq = de.de_idseq\n" +
                "AND             pt.proto_idseq = '" + protocolId + "'\n" +
                "AND             de.de_idseq = acr.ac_idseq (+)\n" +
                "AND             acr.registration_status = rsl.registration_status (+)\n" +
                "AND             de.asl_name = asl.asl_name (+)";

        return getAllContexts( sql );
    }


    public List<SearchModel> cdeByContextClassificationSchemeItem( String classificationSchemeItemId )
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
                "                rsl.display_order ,\n" +
                "                asl.display_order wkflow_order ,\n" +
                "                de.cde_id         cdeid\n" +
                "FROM            sbr.data_elements_view de ,\n" +
                "                sbr.reference_documents_view rd ,\n" +
                "                sbr.contexts_view conte,\n" +
                "                sbr.ac_csi_view acs ,\n" +
                "                sbr.ac_registrations_view acr ,\n" +
                "                sbr.reg_status_lov_view rsl ,\n" +
                "                sbr.ac_status_lov_view asl\n" +
                "WHERE           de.de_idseq = rd.ac_idseq (+)\n" +
                "AND             rd.dctl_name (+) = 'Preferred Question Text'\n" +
                "AND             nvl(acr.registration_status,'-1') NOT IN ( 'Retired' )\n" +
                "AND             asl.asl_name NOT                      IN ( 'CMTE APPROVED' ,\n" +
                "                                                          'CMTE SUBMTD' ,\n" +
                "                                                          'CMTE SUBMTD USED' ,\n" +
                "                                                          'RETIRED ARCHIVED' ,\n" +
                "                                                          'RETIRED PHASED OUT' ,\n" +
                "                                                          'RETIRED WITHDRAWN' )\n" +
                "AND             conte.NAME NOT IN ( 'TEST',\n" +
                "                                   'Training' )\n" +
                "AND             de.asl_name != 'RETIRED DELETED'\n" +
                "AND             conte.conte_idseq = de.conte_idseq\n" +
                "AND             acs.cs_csi_idseq = '" + classificationSchemeItemId + "'\n" +
                "AND             acs.ac_idseq = de.de_idseq\n" +
                "AND             de.de_idseq = acr.ac_idseq (+)\n" +
                "AND             acr.registration_status = rsl.registration_status (+)\n" +
                "AND             de.asl_name = asl.asl_name (+)";

        return getAllContexts( sql );
    }

    public List<SearchModel> cdeOwnedAndUsedByContext( String conteId )
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
                "                rsl.display_order,\n" +
                "                asl.display_order                        wkflow_order,\n" +
                "                de.cde_id                                cdeid\n" +
                "FROM   sbr.data_elements_view de,\n" +
                "       sbr.reference_documents_view rd,\n" +
                "       sbr.contexts_view conte,\n" +
                "       sbr.ac_registrations_view acr,\n" +
                "       sbr.reg_status_lov_view rsl,\n" +
                "       sbr.ac_status_lov_view asl\n" +
                "WHERE  de.de_idseq = rd.ac_idseq (+)\n" +
                "       AND rd.dctl_name (+) = 'Preferred Question Text'\n" +
                "       AND Nvl(acr.registration_status, '-1') NOT IN ( 'Retired' )\n" +
                "       AND asl.asl_name NOT IN ( 'CMTE APPROVED', 'CMTE SUBMTD',\n" +
                "                                 'CMTE SUBMTD USED',\n" +
                "                                 'RETIRED ARCHIVED',\n" +
                "                                 'RETIRED PHASED OUT', 'RETIRED WITHDRAWN' )\n" +
                "       AND conte.name NOT IN ( 'TEST', 'Training' )\n" +
                "       AND de.asl_name != 'RETIRED DELETED'\n" +
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
                "       AND de.de_idseq = acr.ac_idseq (+)\n" +
                "       AND acr.registration_status = rsl.registration_status (+)\n" +
                "       AND de.asl_name = asl.asl_name (+)  ";

        return getAllContexts( sql );
    }

///////////////////////////////////////////////////////////////////////

    public List<SearchModel> cdeByContextClassificationScheme( String classificationSchemeId )
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
                "                rsl.display_order ,\n" +
                "                asl.display_order wkflow_order ,\n" +
                "                de.cde_id         cdeid\n" +
                "FROM            sbr.data_elements_view de ,\n" +
                "                sbr.reference_documents_view rd ,\n" +
                "                sbr.contexts_view conte ,\n" +
                "                sbr.ac_registrations_view acr ,\n" +
                "                sbr.reg_status_lov_view rsl ,\n" +
                "                sbr.ac_status_lov_view asl\n" +
                "WHERE           de.de_idseq = rd.ac_idseq (+)\n" +
                "AND             rd.dctl_name (+) = 'Preferred Question Text'\n" +
                "AND             nvl(acr.registration_status,'-1') NOT IN ( 'Retired' )\n" +
                "AND             asl.asl_name NOT                      IN ( 'CMTE APPROVED' ,\n" +
                "                                                          'CMTE SUBMTD' ,\n" +
                "                                                          'CMTE SUBMTD USED' ,\n" +
                "                                                          'RETIRED ARCHIVED' ,\n" +
                "                                                          'RETIRED PHASED OUT' ,\n" +
                "                                                          'RETIRED WITHDRAWN' )\n" +
                "AND             conte.NAME NOT IN ( 'TEST',\n" +
                "                                   'Training' )\n" +
                "AND             de.asl_name != 'RETIRED DELETED'\n" +
                "AND             conte.conte_idseq = de.conte_idseq\n" +
                "AND             de.de_idseq = acr.ac_idseq (+)\n" +
                "AND             acr.registration_status = rsl.registration_status (+)\n" +
                "AND             de.asl_name = asl.asl_name (+)\n" +
                "AND             de.de_idseq IN\n" +
                "                (\n" +
                "                       SELECT de_idseq\n" +
                "                       FROM   sbr.data_elements_view de ,\n" +
                "                              sbr.ac_csi_view acs,\n" +
                "                              sbr.cs_csi_view csc\n" +
                "                       WHERE  csc.cs_idseq = '" + classificationSchemeId + "'\n" +
                "                       AND    csc.cs_csi_idseq = acs.cs_csi_idseq\n" +
                "                       AND    acs.ac_idseq = de_idseq )";

        return getAllContexts( sql );
    }


    /////////////////////////////////////////////////////////////////////

}