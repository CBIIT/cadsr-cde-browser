package gov.nih.nci.cadsr.service.search;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import gov.nih.nci.cadsr.common.WorkflowStatusEnum;
import gov.nih.nci.cadsr.dao.operation.SearchQueryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SearchQueryBuilderTest
{
    private SearchQueryBuilder searchQueryBuilder = null;
    private String queryText="25*";
    private String clientSearchMode="0";
    private int clientSearchField = 1;
    private String programArea="All";
    private String context="Testcontext";
    private String classification="99BA9DC8-84A5-4E69-E034-080020C9C0E0";
    private String protocol="protocol";
    private String workFlowStatus="workFlowStatus";
    private String registrationStatus="registrationStatus";
    private String conceptName="conceptName";
    private String conceptCode="conceptCode";




    @Before
    public void setUp() throws Exception
    {
        searchQueryBuilder = new SearchQueryBuilder();

    }

    @After
    public void tearDown() throws Exception
    {

    }


    /*
     String clientQuery, String clientSearchMode, int clientSearchField,
            String programArea,  String context, String classification, String protocol,
            String workFlowStatus, String registrationStatus,
            String conceptName, String conceptCode )
     */

    @Test
    public void testInitSeqrchQueryBuilder01() throws Exception
    {
        // With workFlow
        searchQueryBuilder.initSeqrchQueryBuilder( queryText, clientSearchMode, clientSearchField, programArea, context, classification, protocol, workFlowStatus, registrationStatus, conceptName, conceptCode);
        assertEquals( queryText, searchQueryBuilder.getClientQuery() );
        assertEquals( clientSearchMode, searchQueryBuilder.getClientSearchMode() );
        assertEquals( clientSearchField, searchQueryBuilder.getClientSearchField() );
        assertEquals( programArea, searchQueryBuilder.getProgramArea() );
        assertEquals( context, searchQueryBuilder.getContext() );
        assertEquals( classification, searchQueryBuilder.getClassification() );
        assertEquals( protocol, searchQueryBuilder.getProtocol() );
        assertEquals( workFlowStatus, searchQueryBuilder.getWorkFlowStatus() );
        assertEquals( registrationStatus, searchQueryBuilder.getRegistrationStatus() );
        assertEquals( conceptName, searchQueryBuilder.getConceptName() );
        assertEquals( conceptCode, searchQueryBuilder.getConceptCode() );

    }
    @Test
    public void testInitSeqrchQueryBuilder02() throws Exception
    {
        // With out workFlow, make sure workflow is empty, and exclude clause is right
        searchQueryBuilder.initSeqrchQueryBuilder(  queryText, clientSearchMode, clientSearchField, programArea, context, classification, protocol, "", registrationStatus, conceptName, conceptCode);
        assertEquals( queryText, searchQueryBuilder.getClientQuery() );
        assertEquals( clientSearchMode, searchQueryBuilder.getClientSearchMode() );
        assertEquals( clientSearchField, searchQueryBuilder.getClientSearchField() );
        assertEquals( programArea, searchQueryBuilder.getProgramArea() );
        assertEquals( context, searchQueryBuilder.getContext() );
        assertEquals( classification, searchQueryBuilder.getClassification() );
        assertEquals( protocol, searchQueryBuilder.getProtocol() );
        assertEquals( "", searchQueryBuilder.getWorkFlowStatus() );
        assertEquals( registrationStatus, searchQueryBuilder.getRegistrationStatus() );
        assertEquals( conceptName, searchQueryBuilder.getConceptName() );
        assertEquals( conceptCode, searchQueryBuilder.getConceptCode() );

        assertEquals( " asl.asl_name NOT IN ( 'CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' ) ", WorkflowStatusEnum.getExcludList() );

    }


    @Test
    public void testWorkFlowStatus00() throws Exception
    {
        searchQueryBuilder.initSeqrchQueryBuilder( "25*", "Exact phrase", 1, "", "", "", "", "APPRVD FOR TRIAL USE", "", "", "" );
        assertEquals( cleanup( searchQueryBuilder.getSqlStmt()), cleanup( sql00 )   );
    }

   @Test
    public void testWorkFlowStatus01() throws Exception
    {
        searchQueryBuilder.initSeqrchQueryBuilder( "25*", "Exact phrase", 1, "", "", "", "", "", "", "", "" );
        assertEquals( cleanup( searchQueryBuilder.getSqlStmt()), cleanup( sql01 )   );
    }

  @Test
    public void testContext00() throws Exception
    {
        searchQueryBuilder.initSeqrchQueryBuilder( "25*", "Exact phrase", 1, "", "99BA9DC8-2095-4E69-E034-080020C9C0E0", "", "", "", "", "", "" );
        assertEquals( cleanup( searchQueryBuilder.getSqlStmt()), cleanup( sql02 )   );
    }

    @Test
    public void testClassification00() throws Exception
    {
        searchQueryBuilder.initSeqrchQueryBuilder( "258*", "Exact phrase", 1, "", "", "EB5D88AB-6077-69CB-E034-0003BA3F9857", "", "", "", "", "" );
System.out.println( cleanup( searchQueryBuilder.getSqlStmt()));
    }

    @Test
    public void testBuildStatusWhereClause() throws Exception
    {

    }

    @Test
    public void testBuildRegStatusWhereClause() throws Exception
    {

    }


    private String cleanup( String s)
    {
        return s.replaceAll( "\\s\\s*", " " ).replaceAll( "\\s*,\\s*", ", " ).replaceAll( "\\s*\\)\\s*", ")" ).replaceAll( "\\s*\\(\\s*", "(" ).toUpperCase();
    }
    String sql00="SELECT DISTINCT de.de_idseq,"+
            "                de.preferred_name de_preferred_name,"+
            "                de.long_name,"+
            "                rd.doc_text,"+
            "                conte.name,"+
            "                de.asl_name,"+
            "                to_char(de.cde_id) de_cdeid,"+
            "                de.version de_version,"+
            "                meta_config_mgmt.get_usedby(de.de_idseq) de_usedby,"+
            "                de.vd_idseq,"+
            "                de.dec_idseq,"+
            "                de.conte_idseq,"+
            "                de.preferred_definition,"+
            "                acr.registration_status,"+
            "                rsl.display_order,"+
            "                asl.display_order wkflow_order,"+
            "                de.cde_id cdeid"+
            " FROM sbr.data_elements_view de,"+
            "     sbr.reference_documents_view rd,"+
            "     sbr.contexts_view conte,"+
            "     sbr.ac_registrations_view acr,"+
            "     sbr.reg_status_lov_view rsl,"+
            "     sbr.ac_status_lov_view asl"+
            " WHERE de.de_idseq = rd.ac_idseq (+)"+
            "  AND rd.dctl_name (+) = 'Preferred Question Text'"+
            "  AND nvl(acr.registration_status,'-1') NOT IN ('Retired')"+
            "  AND asl.asl_name = 'APPRVD FOR TRIAL USE'"+
            "  AND conte.name NOT IN ('TEST',"+
            "                         'Training')"+
            "  AND de.asl_name != 'RETIRED DELETED'"+
            "  AND conte.conte_idseq = de.conte_idseq"+
            "  AND to_char(de.cde_id) LIKE '25%'"+
            "  AND de.latest_version_ind = 'Yes'"+
            "  AND de.de_idseq = acr.ac_idseq (+)"+
            "  AND acr.registration_status = rsl.registration_status (+)"+
            "  AND de.asl_name = asl.asl_name (+)";

    String sql01="SELECT DISTINCT de.de_idseq,"+
            "                de.preferred_name de_preferred_name,"+
            "                de.long_name,"+
            "                rd.doc_text,"+
            "                conte.name,"+
            "                de.asl_name,"+
            "                to_char(de.cde_id) de_cdeid,"+
            "                de.version de_version,"+
            "                meta_config_mgmt.get_usedby(de.de_idseq) de_usedby,"+
            "                de.vd_idseq,"+
            "                de.dec_idseq,"+
            "                de.conte_idseq,"+
            "                de.preferred_definition,"+
            "                acr.registration_status,"+
            "                rsl.display_order,"+
            "                asl.display_order wkflow_order,"+
            "                de.cde_id cdeid"+
            " FROM sbr.data_elements_view de,"+
            "     sbr.reference_documents_view rd,"+
            "     sbr.contexts_view conte,"+
            "     sbr.ac_registrations_view acr,"+
            "     sbr.reg_status_lov_view rsl,"+
            "     sbr.ac_status_lov_view asl"+
            " WHERE de.de_idseq = rd.ac_idseq (+)"+
            "  AND rd.dctl_name (+) = 'Preferred Question Text'"+
            "  AND nvl(acr.registration_status,'-1') NOT IN ('Retired')"+
            "  AND asl.asl_name NOT IN ('CMTE APPROVED',"+
            "                           'CMTE SUBMTD',"+
            "                           'CMTE SUBMTD USED',"+
            "                           'RETIRED ARCHIVED',"+
            "                           'RETIRED PHASED OUT',"+
            "                           'RETIRED WITHDRAWN')"+
            "  AND conte.name NOT IN ('TEST',"+
            "                         'Training')"+
            "  AND de.asl_name != 'RETIRED DELETED'"+
            "  AND conte.conte_idseq = de.conte_idseq"+
            "  AND to_char(de.cde_id) LIKE '25%'"+
            "  AND de.latest_version_ind = 'Yes'"+
            "  AND de.de_idseq = acr.ac_idseq (+)"+
            "  AND acr.registration_status = rsl.registration_status (+)"+
            "  AND de.asl_name = asl.asl_name (+)";

    String sql02="SELECT DISTINCT de.de_idseq,"+
            "                de.preferred_name de_preferred_name,"+
            "                de.long_name,"+
            "                rd.doc_text,"+
            "                conte.name,"+
            "                de.asl_name,"+
            "                to_char(de.cde_id) de_cdeid,"+
            "                de.version de_version,"+
            "                meta_config_mgmt.get_usedby(de.de_idseq) de_usedby,"+
            "                de.vd_idseq,"+
            "                de.dec_idseq,"+
            "                de.conte_idseq,"+
            "                de.preferred_definition,"+
            "                acr.registration_status,"+
            "                rsl.display_order,"+
            "                asl.display_order wkflow_order,"+
            "                de.cde_id cdeid"+
            " FROM sbr.data_elements_view de,"+
            "     sbr.reference_documents_view rd,"+
            "     sbr.contexts_view conte,"+
            "     sbr.ac_registrations_view acr,"+
            "     sbr.reg_status_lov_view rsl,"+
            "     sbr.ac_status_lov_view asl"+
            " WHERE conte.conte_idseq = '99BA9DC8-2095-4E69-E034-080020C9C0E0'"+
            "  AND de.de_idseq = rd.ac_idseq (+)"+
            "  AND rd.dctl_name (+) = 'Preferred Question Text'"+
            "  AND nvl(acr.registration_status,'-1') NOT IN ('Retired')"+
            "  AND asl.asl_name NOT IN ('CMTE APPROVED',"+
            "                           'CMTE SUBMTD',"+
            "                           'CMTE SUBMTD USED',"+
            "                           'RETIRED ARCHIVED',"+
            "                           'RETIRED PHASED OUT',"+
            "                           'RETIRED WITHDRAWN')"+
            "  AND conte.name NOT IN ('TEST',"+
            "                         'Training')"+
            "  AND de.asl_name != 'RETIRED DELETED'"+
            "  AND conte.conte_idseq = de.conte_idseq"+
            "  AND to_char(de.cde_id) LIKE '25%'"+
            "  AND de.latest_version_ind = 'Yes'"+
            "  AND de.de_idseq = acr.ac_idseq (+)"+
            "  AND acr.registration_status = rsl.registration_status (+)"+
            "  AND de.asl_name = asl.asl_name (+)";
}
