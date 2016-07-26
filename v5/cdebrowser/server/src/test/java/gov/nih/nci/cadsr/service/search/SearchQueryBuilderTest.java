package gov.nih.nci.cadsr.service.search;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import gov.nih.nci.cadsr.common.WorkflowStatusEnum;
import gov.nih.nci.cadsr.common.WorkflowStatusExcludedInitial;
import gov.nih.nci.cadsr.dao.operation.SearchQueryBuilder;
import gov.nih.nci.cadsr.model.SearchPreferencesServer;
import gov.nih.nci.cadsr.service.model.search.SearchCriteria;

public class SearchQueryBuilderTest
{
    private SearchQueryBuilder searchQueryBuilder;
    private static SearchPreferencesServer initialSearchPreferences;

    @BeforeClass
    public static void setUpClass() throws Exception
    {
        initialSearchPreferences = new SearchPreferencesServer();
        initialSearchPreferences.initPreferences();
    }

    @Before
    public void setUp() throws Exception
    {
        searchQueryBuilder = new SearchQueryBuilder();
    }

    @After
    public void tearDown() throws Exception
    {

    }

    @Test
    public void testBuildPermissibleValueWhereExactPhrase() throws Exception
    {
        String results = searchQueryBuilder.buildPermissibleValueWhere( "XXXXX ZZZZ", 0 );
        String resultsExactPrase = "and de.vd_idseq \n" +
                "IN (\n" +
                "   select vd.vd_idseq \n" +
                "   from sbr.value_domains_view vd , sbr.vd_pvs_view vp, sbr.permissible_values_view pv \n" +
                "   where  vd.vd_idseq = vp.vd_idseq  and    pv.pv_idseq = vp.pv_idseq and upper (pv.value) like upper ('XXXXX ZZZZ') \n" +
                "   )\n";
        assertEquals( cleanup( resultsExactPrase ), cleanup( results ) );
    }

    @Test
    public void testBuildPermissibleValueWhereAllWords() throws Exception
    {
        /*
         {id: 0, name: "Exact phrase"},
         {id: 1, name: "All of the words"},
         {id: 2, name: "At least one of the words"}
          */
        String results = searchQueryBuilder.buildPermissibleValueWhere( "XXXXX ZZZZ", 1 );
        String resultAllWords = "and de.vd_idseq \n" +
                "  IN (\n" +
                "         select vd.vd_idseq \n" +
                "         from sbr.value_domains_view vd , sbr.vd_pvs_view vp, sbr.permissible_values_view pv   \n" +
                "         where  vd.vd_idseq = vp.vd_idseq  and    pv.pv_idseq = vp.pv_idseq and (upper (pv.value) like upper ('% XXXXX %')  or upper (pv.value) like upper ('XXXXX %')  or upper (pv.value) like upper ('XXXXX')  or upper (pv.value) like upper ('% XXXXX') )\n" +
                "         INTERSECT \n" +
                "         select vd.vd_idseq \n" +
                "         from sbr.value_domains_view vd , sbr.vd_pvs_view vp, sbr.permissible_values_view pv   \n" +
                "         where  vd.vd_idseq = vp.vd_idseq  and    pv.pv_idseq = vp.pv_idseq and (upper (pv.value) like upper ('% ZZZZ %')  or upper (pv.value) like upper ('ZZZZ %')  or upper (pv.value) like upper ('ZZZZ')  or upper (pv.value) like upper ('% ZZZZ') )\n" +
                "   ) \n";

        assertEquals( cleanup( resultAllWords ), cleanup( results ) );
    }

    @Test
    public void testBuildPermissibleValueWhereAtLeastOne() throws Exception
    {
        /*
         {id: 0, name: "Exact phrase"},
         {id: 1, name: "All of the words"},
         {id: 2, name: "At least one of the words"}
          */
        String results = searchQueryBuilder.buildPermissibleValueWhere( "XXXXX ZZZZ", 2 );
        String resultAtLeastone = "and de.vd_idseq \n" +
                "  IN (\n" +
                "         select vd.vd_idseq \n" +
                "         from sbr.value_domains_view vd , sbr.vd_pvs_view vp, sbr.permissible_values_view pv   \n" +
                "         where  vd.vd_idseq = vp.vd_idseq  and    pv.pv_idseq = vp.pv_idseq and (upper (pv.value) like upper ('% XXXXX %')  or upper (pv.value) like upper ('XXXXX %')  or upper (pv.value) like upper ('XXXXX')  or upper (pv.value) like upper ('% XXXXX') )\n" +
                "         UNION \n" +
                "         select vd.vd_idseq \n" +
                "         from sbr.value_domains_view vd , sbr.vd_pvs_view vp, sbr.permissible_values_view pv   \n" +
                "         where  vd.vd_idseq = vp.vd_idseq  and    pv.pv_idseq = vp.pv_idseq and (upper (pv.value) like upper ('% ZZZZ %')  or upper (pv.value) like upper ('ZZZZ %')  or upper (pv.value) like upper ('ZZZZ')  or upper (pv.value) like upper ('% ZZZZ') )\n" +
                "   ) \n";

        assertEquals( cleanup( resultAtLeastone ), cleanup( results ) );
    }

    @Test
    public void testInitSearchQueryBuilder01() throws Exception
    {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setName( "" );
        searchCriteria.setPublicId( "25*" );
        searchCriteria.setSearchMode( "0" );
        searchCriteria.setProgramArea( "All" );
        searchCriteria.setContext( "Testcontext" );
        searchCriteria.setClassification( "99BA9DC8-84A5-4E69-E034-080020C9C0E0" );
        searchCriteria.setProtocol( "protocol" );
        searchCriteria.setWorkFlowStatus( "workFlowStatus" );
        searchCriteria.setRegistrationStatus( "registrationStatus" );
        searchCriteria.setConceptName( "conceptName" );
        searchCriteria.setConceptCode( "conceptCode" );
        // With workFlow
        String sqlStmt = searchQueryBuilder.initSearchQueryBuilder( searchCriteria, initialSearchPreferences );
        assertTrue( sqlStmt.contains( "asl.asl_name = 'workFlowStatus'" ) );
    }

    @Test
    public void testInitSearchQueryBuilder02() throws Exception
    {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setName( "" );
        searchCriteria.setPublicId( "25*" );
        searchCriteria.setSearchMode( "0" );
        searchCriteria.setProgramArea( "All" );
        searchCriteria.setContext( "Testcontext" );
        searchCriteria.setClassification( "99BA9DC8-84A5-4E69-E034-080020C9C0E0" );
        searchCriteria.setProtocol( "protocol" );
        searchCriteria.setWorkFlowStatus( "" );
        searchCriteria.setRegistrationStatus( "registrationStatus" );
        searchCriteria.setConceptName( "conceptName" );
        searchCriteria.setConceptCode( "conceptCode" );
        // With out workFlow, make sure workflow is empty, and exclude clause is right
        String sqlStmt = searchQueryBuilder.initSearchQueryBuilder( searchCriteria, initialSearchPreferences );
        assertTrue( sqlStmt.contains( " asl.asl_name NOT IN  ('CMTE APPROVED', 'CMTE SUBMTD', 'CMTE SUBMTD USED', 'RETIRED ARCHIVED', 'RETIRED PHASED OUT', 'RETIRED WITHDRAWN', 'RETIRED DELETED')" ) );
        assertFalse( sqlStmt.contains( "asl.asl_name =" ) );
    }


    @Test
    public void testWorkFlowStatus00() throws Exception
    {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setName( "" );
        searchCriteria.setPublicId( "25*" );
        searchCriteria.setSearchMode( "Exact phrase" );
        searchCriteria.setProgramArea( "" );
        searchCriteria.setContext( "" );
        searchCriteria.setClassification( "" );
        searchCriteria.setProtocol( "" );
        searchCriteria.setWorkFlowStatus( "APPRVD FOR TRIAL USE" );
        searchCriteria.setRegistrationStatus( "" );
        searchCriteria.setConceptName( "" );
        searchCriteria.setConceptCode( "" );
        searchCriteria.setVersionType(0);

        String sqlStmt = searchQueryBuilder.initSearchQueryBuilder( searchCriteria, initialSearchPreferences );
        assertEquals( cleanup( sqlStmt ), cleanup( sql00 ) );
    }

    @Test
    public void testWorkFlowStatus01() throws Exception
    {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setName( "" );
        searchCriteria.setPublicId( "25*" );
        searchCriteria.setSearchMode( "Exact phrase" );
        searchCriteria.setProgramArea( "" );
        searchCriteria.setContext( "" );
        searchCriteria.setClassification( "" );
        searchCriteria.setProtocol( "" );
        searchCriteria.setWorkFlowStatus( "" );
        searchCriteria.setRegistrationStatus( "" );
        searchCriteria.setConceptName( "" );
        searchCriteria.setConceptCode( "" );
        searchCriteria.setVersionType(0);

        String sqlStmt = searchQueryBuilder.initSearchQueryBuilder( searchCriteria, initialSearchPreferences );
        assertEquals( cleanup( sqlStmt ), cleanup( sql01 ) );
    }

    @Test
    public void testContext00() throws Exception
    {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setName( "" );
        searchCriteria.setPublicId( "25*" );
        searchCriteria.setSearchMode( "Exact phrase" );
        searchCriteria.setProgramArea( "" );
        searchCriteria.setContext( "99BA9DC8-2095-4E69-E034-080020C9C0E0" );
        searchCriteria.setClassification( "" );
        searchCriteria.setProtocol( "" );
        searchCriteria.setWorkFlowStatus( "" );
        searchCriteria.setRegistrationStatus( "" );
        searchCriteria.setConceptName( "" );
        searchCriteria.setConceptCode( "" );
        searchCriteria.setContextUse( 2 );
        searchCriteria.setVersionType(0);

        String sqlStmt = searchQueryBuilder.initSearchQueryBuilder( searchCriteria, initialSearchPreferences );
        assertEquals( cleanup( sqlStmt ), cleanup( sql02 ) );
    }

    @Test
    public void testClassification00() throws Exception
    {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setName( "" );
        searchCriteria.setPublicId( "258*" );
        searchCriteria.setSearchMode( "Exact phrase" );
        searchCriteria.setProgramArea( "" );
        searchCriteria.setContext( "" );
        searchCriteria.setClassification( "EB5D88AB-6077-69CB-E034-0003BA3F9857" );
        searchCriteria.setProtocol( "" );
        searchCriteria.setWorkFlowStatus( "" );
        searchCriteria.setRegistrationStatus( "" );
        searchCriteria.setConceptName( "" );
        searchCriteria.setConceptCode( "" );

        String sqlStmt = searchQueryBuilder.initSearchQueryBuilder( searchCriteria, initialSearchPreferences );
        System.out.println( cleanup( sqlStmt ) );
    }

    @Test
    public void testBuildSearchTextWhere01() throws Exception
    {
        String text = "inputText";
        String[] searchDomain = {"ALL fields"};
        String searchMode = "searchMode";
        String expected = "AND de.de_idseq IN ( (SELECT de_idseq  FROM sbr.designations_view dsn, sbr.data_elements_view de1  "+
                "WHERE  de1.de_idseq  = dsn.ac_idseq (+)  "+
                "AND dsn.detl_name = 'UML Class:UML Attr'  " +
                "AND ((UPPER (nvl(dsn.name,'%')) LIKE UPPER ('% inputText %') OR UPPER (nvl(dsn.name,'%')) LIKE UPPER ('inputText %') OR UPPER (nvl(dsn.name,'%')) LIKE UPPER ('inputText') OR UPPER (nvl(dsn.name,'%')) LIKE UPPER ('% inputText'))) ) " +
                "UNION " +
                "(SELECT de_idseq "+
                " FROM sbr.reference_documents_view rd1, sbr.data_elements_view de1  " +
                "WHERE  de1.de_idseq  = rd1.ac_idseq (+)  AND    " +
                "rd1.dctl_name (+) = 'Preferred Question Text'  " +
                "AND (((UPPER (de1.long_name) LIKE UPPER ('% inputText %')  OR UPPER (de1.long_name) LIKE UPPER ('inputText %')  OR UPPER (de1.long_name) LIKE UPPER ('inputText')  OR UPPER (de1.long_name) LIKE UPPER ('% inputText') )) " +
                "OR ((UPPER (de1.preferred_name) LIKE UPPER ('% inputText %')  OR UPPER (de1.preferred_name) LIKE UPPER ('inputText %')  OR UPPER (de1.preferred_name) LIKE UPPER ('inputText')  OR UPPER (de1.preferred_name) LIKE UPPER ('% inputText') ))" +
                " OR ((UPPER (nvl(rd1.doc_text,'%')) LIKE UPPER ('% inputText %')  OR UPPER (nvl(rd1.doc_text,'%')) LIKE UPPER ('inputText %')  OR UPPER (nvl(rd1.doc_text,'%')) LIKE UPPER ('inputText')  OR UPPER (nvl(rd1.doc_text,'%')) LIKE UPPER ('% inputText') )))  " +
                "UNION  " +
                "SELECT de_idseq  FROM sbr.reference_documents_view rd2,sbr.data_elements_view de2  WHERE  de2.de_idseq  = rd2.ac_idseq (+)  AND    rd2.dctl_name (+) = 'Alternate Question Text' " +
                " AND    ((UPPER (nvl(rd2.doc_text,'%')) LIKE UPPER ('% inputText %')  OR UPPER (nvl(rd2.doc_text,'%')) LIKE UPPER ('inputText %')  OR UPPER (nvl(rd2.doc_text,'%')) LIKE UPPER ('inputText')  OR UPPER (nvl(rd2.doc_text,'%')) LIKE UPPER ('% inputText') ))) )";
        String received = searchQueryBuilder.buildSearchTextWhere( text, searchDomain, searchMode );
        assertEquals( cleanup( expected ), cleanup( received ) );

    }

    @Test
    public void testBuildSearchTextWhere02() throws Exception
    {
        String text = "inputText";
        String[] searchDomain = {"ALL fields", "Long Name"};
        String searchMode = "searchMode";
        String expected = "AND de.de_idseq IN ( (SELECT de_idseq  FROM sbr.designations_view dsn, sbr.data_elements_view de1  "+
                "WHERE  de1.de_idseq  = dsn.ac_idseq (+)  "+
                "AND dsn.detl_name = 'UML Class:UML Attr'  " +
                "AND ((UPPER (nvl(dsn.name,'%')) LIKE UPPER ('% inputText %') OR UPPER (nvl(dsn.name,'%')) LIKE UPPER ('inputText %') OR UPPER (nvl(dsn.name,'%')) LIKE UPPER ('inputText') OR UPPER (nvl(dsn.name,'%')) LIKE UPPER ('% inputText'))) ) " +
                "UNION " +
                "(SELECT de_idseq "+
                " FROM sbr.reference_documents_view rd1, sbr.data_elements_view de1  " +
                "WHERE  de1.de_idseq  = rd1.ac_idseq (+)  AND    " +
                "rd1.dctl_name (+) = 'Preferred Question Text'  " +
                "AND (((UPPER (de1.long_name) LIKE UPPER ('% inputText %')  OR UPPER (de1.long_name) LIKE UPPER ('inputText %')  OR UPPER (de1.long_name) LIKE UPPER ('inputText')  OR UPPER (de1.long_name) LIKE UPPER ('% inputText') )) " +
                "OR ((UPPER (de1.preferred_name) LIKE UPPER ('% inputText %')  OR UPPER (de1.preferred_name) LIKE UPPER ('inputText %')  OR UPPER (de1.preferred_name) LIKE UPPER ('inputText')  OR UPPER (de1.preferred_name) LIKE UPPER ('% inputText') ))" +
                " OR ((UPPER (nvl(rd1.doc_text,'%')) LIKE UPPER ('% inputText %')  OR UPPER (nvl(rd1.doc_text,'%')) LIKE UPPER ('inputText %')  OR UPPER (nvl(rd1.doc_text,'%')) LIKE UPPER ('inputText')  OR UPPER (nvl(rd1.doc_text,'%')) LIKE UPPER ('% inputText') )))  " +
                "UNION  " +
                "SELECT de_idseq  FROM sbr.reference_documents_view rd2,sbr.data_elements_view de2  WHERE  de2.de_idseq  = rd2.ac_idseq (+)  AND    rd2.dctl_name (+) = 'Alternate Question Text' " +
                " AND    ((UPPER (nvl(rd2.doc_text,'%')) LIKE UPPER ('% inputText %')  OR UPPER (nvl(rd2.doc_text,'%')) LIKE UPPER ('inputText %')  OR UPPER (nvl(rd2.doc_text,'%')) LIKE UPPER ('inputText')  OR UPPER (nvl(rd2.doc_text,'%')) LIKE UPPER ('% inputText') ))) )";
        String received = searchQueryBuilder.buildSearchTextWhere( text, searchDomain, searchMode );
        assertEquals( cleanup( expected ), cleanup( received ) );

    }

    @Test
    public void testBuildSearchTextWhere03() throws Exception
    {
        String text = "inputText";
        String[] searchDomain = {"Long Name"};
        String searchMode = "searchMode";
        String expected = "AND DE.DE_IDSEQ IN(SELECT DE_IDSEQ FROM SBR.DATA_ELEMENTS_VIEW DE1 " +
                "WHERE((UPPER(DE1.LONG_NAME)LIKE UPPER('% INPUTTEXT %')OR UPPER(DE1.LONG_NAME)LIKE UPPER('INPUTTEXT %')OR UPPER(DE1.LONG_NAME)LIKE UPPER('INPUTTEXT')OR UPPER(DE1.LONG_NAME)LIKE UPPER('% INPUTTEXT'))))";
        String received = searchQueryBuilder.buildSearchTextWhere( text, searchDomain, searchMode );
        assertEquals( cleanup( expected ), cleanup( received ) );
    }

    @Test
    public void testBuildSearchTextWhere04() throws Exception
    {
        String text = "inputText";
        String[] searchDomain = {"Short Name"};
        String searchMode = "searchMode";
        String expected = "AND DE.DE_IDSEQ IN(SELECT DE_IDSEQ FROM SBR.DATA_ELEMENTS_VIEW DE1 " +
                "WHERE((UPPER(DE1.PREFERRED_NAME)LIKE UPPER('% INPUTTEXT %')OR UPPER(DE1.PREFERRED_NAME)LIKE UPPER('INPUTTEXT %')OR UPPER(DE1.PREFERRED_NAME)LIKE UPPER('INPUTTEXT')OR UPPER(DE1.PREFERRED_NAME)LIKE UPPER('% INPUTTEXT'))))";
        String received = searchQueryBuilder.buildSearchTextWhere( text, searchDomain, searchMode );
        assertEquals( cleanup( expected ), cleanup( received ) );
    }

    @Test
    public void testBuildSearchTextWhere05() throws Exception
    {
        String text = "inputText";
        String[] searchDomain = {"Preferred Question Text"};
        String searchMode = "searchMode";
        String expected = "AND DE.DE_IDSEQ IN(SELECT DE_IDSEQ " +
                "FROM SBR.REFERENCE_DOCUMENTS_VIEW RD1, SBR.DATA_ELEMENTS_VIEW DE1 WHERE DE1.DE_IDSEQ = RD1.AC_IDSEQ(+)" +
                "AND RD1.DCTL_NAME(+)= 'PREFERRED QUESTION TEXT' AND((UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('% INPUTTEXT %')OR UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('INPUTTEXT %')" +
                "OR UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('INPUTTEXT')OR UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('% INPUTTEXT'))))";
        String received = searchQueryBuilder.buildSearchTextWhere( text, searchDomain, searchMode );
        assertEquals( cleanup( expected ), cleanup( received ) );
    }

    @Test
    public void testBuildSearchTextWhere06() throws Exception
    {
        String text = "inputText";
        String[] searchDomain = {"Alternate Question Text"};
        String searchMode = "searchMode";
        String expected = "AND DE.DE_IDSEQ IN(SELECT DE_IDSEQ " +
                "FROM SBR.REFERENCE_DOCUMENTS_VIEW RD1, SBR.DATA_ELEMENTS_VIEW DE1 WHERE DE1.DE_IDSEQ = RD1.AC_IDSEQ(+)" +
                "AND RD1.DCTL_NAME(+)= 'Alternate QUESTION TEXT' AND((UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('% INPUTTEXT %')OR UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('INPUTTEXT %')" +
                "OR UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('INPUTTEXT')OR UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('% INPUTTEXT'))))";
        String received = searchQueryBuilder.buildSearchTextWhere( text, searchDomain, searchMode );
        assertEquals( cleanup( expected ), cleanup( received ) );
    }

    @Test
    public void testBuildSearchTextWhere07() throws Exception
    {
        String text = "inputText";
        String[] searchDomain = {"UML Class: UML Attr Alternate Name"};
        String searchMode = "searchMode";
        String expected = "AND DE.DE_IDSEQ IN(SELECT DE_IDSEQ " +
                "FROM SBR.DESIGNATIONS_VIEW DSN, SBR.DATA_ELEMENTS_VIEW DE1 WHERE DE1.DE_IDSEQ = DSN.AC_IDSEQ(+)AND DSN.DETL_NAME = 'UML CLASS:UML ATTR' AND((UPPER(NVL(DSN.NAME, '%'))LIKE UPPER('% INPUTTEXT %')" +
                "OR UPPER(NVL(DSN.NAME, '%'))LIKE UPPER('INPUTTEXT %')OR UPPER(NVL(DSN.NAME, '%'))LIKE UPPER('INPUTTEXT')OR UPPER(NVL(DSN.NAME, '%'))LIKE UPPER('% INPUTTEXT'))))";
        String received = searchQueryBuilder.buildSearchTextWhere( text, searchDomain, searchMode );
        assertEquals( cleanup( expected ), cleanup( received ) );
    }

    @Test
    public void testBuildSearchTextWhere08() throws Exception
    {
        String text = "inputText";
        String[] searchDomain = {"Long Name","Short Name"};
        String searchMode = "searchMode";
        String expected = "AND DE.DE_IDSEQ IN(SELECT DE_IDSEQ " +
                "FROM SBR.DATA_ELEMENTS_VIEW DE1 WHERE((UPPER(DE1.LONG_NAME)LIKE UPPER('% INPUTTEXT %')OR UPPER(DE1.LONG_NAME)LIKE UPPER('INPUTTEXT %')" +
                "OR UPPER(DE1.LONG_NAME)LIKE UPPER('INPUTTEXT')OR UPPER(DE1.LONG_NAME)LIKE UPPER('% INPUTTEXT')))OR((UPPER(DE1.PREFERRED_NAME)LIKE UPPER('% INPUTTEXT %')OR UPPER(DE1.PREFERRED_NAME)LIKE UPPER('INPUTTEXT %')" +
                "OR UPPER(DE1.PREFERRED_NAME)LIKE UPPER('INPUTTEXT')OR UPPER(DE1.PREFERRED_NAME)LIKE UPPER('% INPUTTEXT'))))";
        String received = searchQueryBuilder.buildSearchTextWhere( text, searchDomain, searchMode );
        assertEquals( cleanup( expected ), cleanup( received ) );
    }


    @Test
    public void testBuildSearchTextWhere09() throws Exception
    {
        String text = "inputText";
        String[] searchDomain = {"Long Name","Short Name", "Preferred Question Text"};
        String searchMode = "searchMode";
        String expected = "AND DE.DE_IDSEQ IN(SELECT DE_IDSEQ FROM SBR.REFERENCE_DOCUMENTS_VIEW RD1, SBR.DATA_ELEMENTS_VIEW DE1 " +
                "WHERE DE1.DE_IDSEQ = RD1.AC_IDSEQ(+)AND RD1.DCTL_NAME(+)= 'PREFERRED QUESTION TEXT' AND(((UPPER(DE1.LONG_NAME)LIKE UPPER('% INPUTTEXT %')" +
                "OR UPPER(DE1.LONG_NAME)LIKE UPPER('INPUTTEXT %')OR UPPER(DE1.LONG_NAME)LIKE UPPER('INPUTTEXT')OR UPPER(DE1.LONG_NAME)LIKE UPPER('% INPUTTEXT')))OR((UPPER(DE1.PREFERRED_NAME)LIKE UPPER('% INPUTTEXT %')OR UPPER(DE1.PREFERRED_NAME)LIKE UPPER('INPUTTEXT %')" +
                "OR UPPER(DE1.PREFERRED_NAME)LIKE UPPER('INPUTTEXT')OR UPPER(DE1.PREFERRED_NAME)LIKE UPPER('% INPUTTEXT')))OR((UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('% INPUTTEXT %')OR UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('INPUTTEXT %')OR UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('INPUTTEXT')" +
                "OR UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('% INPUTTEXT')))))";
        String received = searchQueryBuilder.buildSearchTextWhere( text, searchDomain, searchMode );
        assertEquals( cleanup( expected ), cleanup( received ) );
    }

    @Test
    public void testBuildSearchTextWhere10() throws Exception
    {
        String text = "inputText";
        String[] searchDomain = {"Long Name","Short Name", "Alternate Question Text"};
        String searchMode = "searchMode";
        String expected = "AND DE.DE_IDSEQ IN(SELECT DE_IDSEQ " +
                "FROM SBR.REFERENCE_DOCUMENTS_VIEW RD1, SBR.DATA_ELEMENTS_VIEW DE1 " +
                "WHERE DE1.DE_IDSEQ = RD1.AC_IDSEQ(+)AND RD1.DCTL_NAME(+)= 'ALTERNATE QUESTION TEXT' AND(((UPPER(DE1.LONG_NAME)LIKE UPPER('% INPUTTEXT %')OR UPPER(DE1.LONG_NAME)LIKE UPPER('INPUTTEXT %')" +
                "OR UPPER(DE1.LONG_NAME)LIKE UPPER('INPUTTEXT')OR UPPER(DE1.LONG_NAME)LIKE UPPER('% INPUTTEXT')))OR((UPPER(DE1.PREFERRED_NAME)LIKE UPPER('% INPUTTEXT %')" +
                "OR UPPER(DE1.PREFERRED_NAME)LIKE UPPER('INPUTTEXT %')OR UPPER(DE1.PREFERRED_NAME)LIKE UPPER('INPUTTEXT')OR UPPER(DE1.PREFERRED_NAME)LIKE UPPER('% INPUTTEXT')))OR((UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('% INPUTTEXT %')OR UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('INPUTTEXT %')" +
                "OR UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('INPUTTEXT')OR UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('% INPUTTEXT')))))";
        String received = searchQueryBuilder.buildSearchTextWhere( text, searchDomain, searchMode );
        assertEquals( cleanup( expected ), cleanup( received ) );
    }

    @Test
    public void testBuildSearchTextWhere11() throws Exception
    {
        String text = "inputText";
        String[] searchDomain = {"Long Name", "Preferred Question Text", "UML Class: UML Attr Alternate Name"};
        String searchMode = "searchMode";
        String expected = "AND DE.DE_IDSEQ IN((SELECT DE_IDSEQ " +
                "FROM SBR.DESIGNATIONS_VIEW DSN, SBR.DATA_ELEMENTS_VIEW DE1 WHERE DE1.DE_IDSEQ = DSN.AC_IDSEQ(+)AND DSN.DETL_NAME = 'UML CLASS:UML ATTR' AND((UPPER(NVL(DSN.NAME, '%'))LIKE UPPER('% INPUTTEXT %')OR UPPER(NVL(DSN.NAME, '%'))LIKE UPPER('INPUTTEXT %')" +
                "OR UPPER(NVL(DSN.NAME, '%'))LIKE UPPER('INPUTTEXT')OR UPPER(NVL(DSN.NAME, '%'))LIKE UPPER('% INPUTTEXT'))))UNION(SELECT DE_IDSEQ FROM SBR.REFERENCE_DOCUMENTS_VIEW RD1, SBR.DATA_ELEMENTS_VIEW DE1 WHERE DE1.DE_IDSEQ = RD1.AC_IDSEQ(+)" +
                "AND RD1.DCTL_NAME(+)= 'PREFERRED QUESTION TEXT' AND(((UPPER(DE1.LONG_NAME)LIKE UPPER('% INPUTTEXT %')OR UPPER(DE1.LONG_NAME)LIKE UPPER('INPUTTEXT %')OR UPPER(DE1.LONG_NAME)LIKE UPPER('INPUTTEXT')OR UPPER(DE1.LONG_NAME)LIKE UPPER('% INPUTTEXT')))" +
                "OR((UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('% INPUTTEXT %')OR UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('INPUTTEXT %')OR UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('INPUTTEXT')OR UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('% INPUTTEXT'))))))";
        String received = searchQueryBuilder.buildSearchTextWhere( text, searchDomain, searchMode );
        assertEquals( cleanup( expected ), cleanup( received ) );
    }

    @Test
    public void testBuildSearchTextWhere12() throws Exception
    {
        String text = "inputText";
        String[] searchDomain = {"Long Name", "Short Name", "Preferred Question Text", "UML Class: UML Attr Alternate Name"};
        String searchMode = "searchMode";
        String expected = "AND DE.DE_IDSEQ IN((SELECT DE_IDSEQ FROM SBR.DESIGNATIONS_VIEW DSN, SBR.DATA_ELEMENTS_VIEW DE1 " +
                "WHERE DE1.DE_IDSEQ = DSN.AC_IDSEQ(+)AND DSN.DETL_NAME = 'UML CLASS:UML ATTR' AND((UPPER(NVL(DSN.NAME, '%'))LIKE UPPER('% INPUTTEXT %')" +
                "OR UPPER(NVL(DSN.NAME, '%'))LIKE UPPER('INPUTTEXT %')OR UPPER(NVL(DSN.NAME, '%'))LIKE UPPER('INPUTTEXT')OR UPPER(NVL(DSN.NAME, '%'))LIKE UPPER('% INPUTTEXT'))))UNION(SELECT DE_IDSEQ FROM SBR.REFERENCE_DOCUMENTS_VIEW RD1, SBR.DATA_ELEMENTS_VIEW DE1 " +
                "WHERE DE1.DE_IDSEQ = RD1.AC_IDSEQ(+)AND RD1.DCTL_NAME(+)= 'PREFERRED QUESTION TEXT' AND(((UPPER(DE1.LONG_NAME)LIKE UPPER('% INPUTTEXT %')OR UPPER(DE1.LONG_NAME)LIKE UPPER('INPUTTEXT %')" +
                "OR UPPER(DE1.LONG_NAME)LIKE UPPER('INPUTTEXT')OR UPPER(DE1.LONG_NAME)LIKE UPPER('% INPUTTEXT')))OR((UPPER(DE1.PREFERRED_NAME)LIKE UPPER('% INPUTTEXT %')" +
                "OR UPPER(DE1.PREFERRED_NAME)LIKE UPPER('INPUTTEXT %')OR UPPER(DE1.PREFERRED_NAME)LIKE UPPER('INPUTTEXT')OR UPPER(DE1.PREFERRED_NAME)LIKE UPPER('% INPUTTEXT')))OR((UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('% INPUTTEXT %')OR UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('INPUTTEXT %')" +
                "OR UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('INPUTTEXT')OR UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('% INPUTTEXT'))))))\n";
        String received = searchQueryBuilder.buildSearchTextWhere( text, searchDomain, searchMode );
        assertEquals( cleanup( expected ), cleanup( received ) );
    }

    @Test
    public void testBuildSearchTextWhere13() throws Exception
    {
        String text = "inputText";
        String[] searchDomain = {"Long Name", "Alternate Question Text", "Preferred Question Text", "UML Class: UML Attr Alternate Name"};
        String searchMode = "searchMode";
        String expected = "AND DE.DE_IDSEQ IN((SELECT DE_IDSEQ FROM SBR.DESIGNATIONS_VIEW DSN, SBR.DATA_ELEMENTS_VIEW DE1 WHERE DE1.DE_IDSEQ = DSN.AC_IDSEQ(+)" +
                "AND DSN.DETL_NAME = 'UML CLASS:UML ATTR' AND((UPPER(NVL(DSN.NAME, '%'))LIKE UPPER('% INPUTTEXT %')OR UPPER(NVL(DSN.NAME, '%'))LIKE UPPER('INPUTTEXT %')OR UPPER(NVL(DSN.NAME, '%'))LIKE UPPER('INPUTTEXT')" +
                "OR UPPER(NVL(DSN.NAME, '%'))LIKE UPPER('% INPUTTEXT'))))UNION(SELECT DE_IDSEQ FROM SBR.REFERENCE_DOCUMENTS_VIEW RD1, SBR.DATA_ELEMENTS_VIEW DE1 WHERE DE1.DE_IDSEQ = RD1.AC_IDSEQ(+)" +
                "AND RD1.DCTL_NAME(+)= 'PREFERRED QUESTION TEXT' AND(((UPPER(DE1.LONG_NAME)LIKE UPPER('% INPUTTEXT %')OR UPPER(DE1.LONG_NAME)LIKE UPPER('INPUTTEXT %')OR UPPER(DE1.LONG_NAME)LIKE UPPER('INPUTTEXT')" +
                "OR UPPER(DE1.LONG_NAME)LIKE UPPER('% INPUTTEXT')))OR((UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('% INPUTTEXT %')OR UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('INPUTTEXT %')" +
                "OR UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('INPUTTEXT')OR UPPER(NVL(RD1.DOC_TEXT, '%'))LIKE UPPER('% INPUTTEXT'))))UNION SELECT DE_IDSEQ " +
                "FROM SBR.REFERENCE_DOCUMENTS_VIEW RD2, SBR.DATA_ELEMENTS_VIEW DE2 WHERE DE2.DE_IDSEQ = RD2.AC_IDSEQ(+)AND RD2.DCTL_NAME(+)= 'ALTERNATE QUESTION TEXT' AND((UPPER(NVL(RD2.DOC_TEXT, '%'))LIKE UPPER('% INPUTTEXT %')" +
                "OR UPPER(NVL(RD2.DOC_TEXT, '%'))LIKE UPPER('INPUTTEXT %')OR UPPER(NVL(RD2.DOC_TEXT, '%'))LIKE UPPER('INPUTTEXT')OR UPPER(NVL(RD2.DOC_TEXT, '%'))LIKE UPPER('% INPUTTEXT')))))";
        String received = searchQueryBuilder.buildSearchTextWhere( text, searchDomain, searchMode );
        assertEquals( cleanup( expected ), cleanup( received ) );
    }

    @Test
    public void testProtocolSearchQuery()
    {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setName( "" );
        searchCriteria.setPublicId( "" );
        searchCriteria.setSearchMode( "" );
        searchCriteria.setProgramArea( "" );
        searchCriteria.setContext( "Test" );
        searchCriteria.setClassification( "" );
        searchCriteria.setProtocol( "B40DD2C8-A047-DBE1-E040-BB89AD437202" );
        searchCriteria.setWorkFlowStatus( "" );
        searchCriteria.setRegistrationStatus( "" );
        searchCriteria.setConceptName( "" );
        searchCriteria.setConceptCode( "" );
        searchCriteria.setVersionType(0);

        String sqlStmt = searchQueryBuilder.initSearchQueryBuilder( searchCriteria, initialSearchPreferences );
        assertEquals( cleanup( sqlStmt ), cleanup( protocolSearchQuery ) );
    }

    @Test
    public void testVersionTypeAll()
    {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setName( "" );
        searchCriteria.setPublicId( "" );
        searchCriteria.setSearchMode( "" );
        searchCriteria.setProgramArea( "" );
        searchCriteria.setContext( "Test" );
        searchCriteria.setClassification( "" );
        searchCriteria.setProtocol( "B40DD2C8-A047-DBE1-E040-BB89AD437202" );
        searchCriteria.setWorkFlowStatus( "" );
        searchCriteria.setRegistrationStatus( "" );
        searchCriteria.setConceptName( "" );
        searchCriteria.setConceptCode( "" );
        searchCriteria.setVersionType(1);

        String sqlStmt = searchQueryBuilder.initSearchQueryBuilder( searchCriteria, initialSearchPreferences );
        assertEquals( cleanup( sqlStmt ), cleanup( versionTypeAllQuery ) );
    }

    private String cleanup( String s )
    {
        return s.replaceAll( "\\s\\s*", " " ).replaceAll( "\\s*,\\s*", ", " ).replaceAll( "\\s*\\)\\s*", ")" ).replaceAll( "\\s*\\(\\s*", "(" ).toUpperCase().trim();
    }

    /**
     * This method is to use in unit tests only.
     * The list of excluded workflow status is defined by Search Preferences, and is not defined by this method.
     *
     * @return The default list of Workflow Status/sbr.ac_status_lov_view.asl_name to be excluded.
     */
    public static String getExcludList()
    {
        StringBuilder sb = new StringBuilder( " asl.asl_name NOT IN ( '" );
        sb.append( WorkflowStatusEnum.CmteApproved.getWorkflowStatus() + "' , '" );
        sb.append( WorkflowStatusEnum.CmteSubmtd.getWorkflowStatus() + "' , '" );
        sb.append( WorkflowStatusEnum.CmteSubmtdUsed.getWorkflowStatus() + "' , '" );
        sb.append( WorkflowStatusEnum.RetiredArchived.getWorkflowStatus() + "' , '" );
        sb.append( WorkflowStatusEnum.RetiredPhasedOut.getWorkflowStatus() + "' , '" );
        sb.append( WorkflowStatusEnum.RetiredWithdrawn.getWorkflowStatus() + "' , '" );
        sb.append( WorkflowStatusExcludedInitial.RetiredDeleted.getWorkflowStatus() + "' ) " );
        return sb.toString();
    }

    String protocolSearchQuery = "SELECT DISTINCT de.de_idseq, de.preferred_name de_preferred_name, de.long_name , " +
            "rd.doc_text , conte.NAME ,de.asl_name , To_char(de.cde_id) de_cdeid , de.version de_version , " +
            "                meta_config_mgmt.Get_usedby(de.de_idseq) de_usedby ,de.vd_idseq , " +
            "                de.dec_idseq , de.conte_idseq , de.preferred_definition , acr.registration_status , rsl.display_order , " +
            "                asl.display_order wkflow_order , de.cde_id cdeid " +
            "FROM            sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte, " +
            "                sbrext.quest_contents_view_ext frm , sbrext.protocol_qc_ext ptfrm , sbrext.protocols_view_ext pt , " +
            "                sbrext.quest_contents_view_ext qc , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl ,  sbr.ac_status_lov_view asl " +
            "WHERE           de.de_idseq = rd.ac_idseq (+) AND rd.dctl_name (+) = 'Preferred Question Text' " +
            "AND de.latest_version_ind = 'Yes' " +
            "AND             nvl(acr.registration_status,'-1') NOT IN ( 'Retired' ) " +
            "AND             " + getExcludList() +
            "AND             conte.NAME NOT IN ( 'TEST', 'Training' ) " +
            //"AND             de.asl_name != 'RETIRED DELETED' " + //removing this status from SQL statement. This status is added to Search Preferences as of release 5.2
            "AND             conte.conte_idseq = de.conte_idseq AND             pt.proto_idseq = ptfrm.proto_idseq " +
            "AND             frm.qc_idseq = ptfrm.qc_idseq AND frm.qtl_name = 'CRF' " +
            "AND             qc.dn_crf_idseq = frm.qc_idseq AND qc.qtl_name = 'QUESTION' " +
            "AND             qc.de_idseq = de.de_idseq AND pt.proto_idseq = 'B40DD2C8-A047-DBE1-E040-BB89AD437202' " +
            "AND             de.de_idseq = acr.ac_idseq (+) AND acr.registration_status = rsl.registration_status (+) AND de.asl_name = asl.asl_name (+)";

    String versionTypeAllQuery = "SELECT DISTINCT de.de_idseq, de.preferred_name de_preferred_name, de.long_name , " +
            "rd.doc_text , conte.NAME ,de.asl_name , To_char(de.cde_id) de_cdeid , de.version de_version , " +
            "                meta_config_mgmt.Get_usedby(de.de_idseq) de_usedby ,de.vd_idseq , " +
            "                de.dec_idseq , de.conte_idseq , de.preferred_definition , acr.registration_status , rsl.display_order , " +
            "                asl.display_order wkflow_order , de.cde_id cdeid " +
            "FROM            sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte, " +
            "                sbrext.quest_contents_view_ext frm , sbrext.protocol_qc_ext ptfrm , sbrext.protocols_view_ext pt , " +
            "                sbrext.quest_contents_view_ext qc , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl ,  sbr.ac_status_lov_view asl " +
            "WHERE           de.de_idseq = rd.ac_idseq (+) AND rd.dctl_name (+) = 'Preferred Question Text' " +
            "AND             nvl(acr.registration_status,'-1') NOT IN ( 'Retired' ) " +
            "AND             " + getExcludList() +
            "AND             conte.NAME NOT IN ( 'TEST', 'Training' ) " +
            //"AND             de.asl_name != 'RETIRED DELETED' " + //removing this status from SQL statement. This status is added to Search Preferences as of release 5.2
            "AND             conte.conte_idseq = de.conte_idseq AND             pt.proto_idseq = ptfrm.proto_idseq " +
            "AND             frm.qc_idseq = ptfrm.qc_idseq AND frm.qtl_name = 'CRF' " +
            "AND             qc.dn_crf_idseq = frm.qc_idseq AND qc.qtl_name = 'QUESTION' " +
            "AND             qc.de_idseq = de.de_idseq AND pt.proto_idseq = 'B40DD2C8-A047-DBE1-E040-BB89AD437202' " +
            "AND             de.de_idseq = acr.ac_idseq (+) AND acr.registration_status = rsl.registration_status (+) AND de.asl_name = asl.asl_name (+)";

    String sql00 = "SELECT DISTINCT de.de_idseq," +
            "                de.preferred_name de_preferred_name," +
            "                de.long_name," +
            "                rd.doc_text," +
            "                conte.name," +
            "                de.asl_name," +
            "                to_char(de.cde_id) de_cdeid," +
            "                de.version de_version," +
            "                meta_config_mgmt.get_usedby(de.de_idseq) de_usedby," +
            "                de.vd_idseq," +
            "                de.dec_idseq," +
            "                de.conte_idseq," +
            "                de.preferred_definition," +
            "                acr.registration_status," +
            "                rsl.display_order," +
            "                asl.display_order wkflow_order," +
            "                de.cde_id cdeid" +
            " FROM sbr.data_elements_view de," +
            "     sbr.reference_documents_view rd," +
            "     sbr.contexts_view conte," +
            "     sbr.ac_registrations_view acr," +
            "     sbr.reg_status_lov_view rsl," +
            "     sbr.ac_status_lov_view asl" +
            " WHERE de.de_idseq = rd.ac_idseq (+)" +
            "  AND rd.dctl_name (+) = 'Preferred Question Text'" +
            " AND de.latest_version_ind = 'Yes'" +
            "  AND nvl(acr.registration_status,'-1') NOT IN ('Retired') AND " +
            getExcludList() +
            "  AND asl.asl_name = 'APPRVD FOR TRIAL USE'" +
            "  AND conte.name NOT IN ('TEST'," +
            "                         'Training')" +
            //"  AND de.asl_name != 'RETIRED DELETED'"+ //removing this status from SQL statement. This status is added to Search Preferences as of release 5.2
            "  AND conte.conte_idseq = de.conte_idseq" +
            "  AND to_char(de.cde_id) LIKE '25%'" +
            "  AND de.latest_version_ind = 'Yes'" +
            "  AND de.de_idseq = acr.ac_idseq (+)" +
            "  AND acr.registration_status = rsl.registration_status (+)" +
            "  AND de.asl_name = asl.asl_name (+)";

    String sql01 = "SELECT DISTINCT de.de_idseq," +
            "                de.preferred_name de_preferred_name," +
            "                de.long_name," +
            "                rd.doc_text," +
            "                conte.name," +
            "                de.asl_name," +
            "                to_char(de.cde_id) de_cdeid," +
            "                de.version de_version," +
            "                meta_config_mgmt.get_usedby(de.de_idseq) de_usedby," +
            "                de.vd_idseq," +
            "                de.dec_idseq," +
            "                de.conte_idseq," +
            "                de.preferred_definition," +
            "                acr.registration_status," +
            "                rsl.display_order," +
            "                asl.display_order wkflow_order," +
            "                de.cde_id cdeid" +
            " FROM sbr.data_elements_view de," +
            "     sbr.reference_documents_view rd," +
            "     sbr.contexts_view conte," +
            "     sbr.ac_registrations_view acr," +
            "     sbr.reg_status_lov_view rsl," +
            "     sbr.ac_status_lov_view asl" +
            " WHERE de.de_idseq = rd.ac_idseq (+)" +
            "  AND rd.dctl_name (+) = 'Preferred Question Text'" +
            " AND de.latest_version_ind = 'Yes'" +
            "  AND nvl(acr.registration_status,'-1') NOT IN ('Retired')" +
            "  AND " + getExcludList() +
            "  AND conte.name NOT IN ('TEST'," +
            "                         'Training')" +
            //"  AND de.asl_name != 'RETIRED DELETED'"+ //removing this status from SQL statement. This status is added to Search Preferences as of release 5.2
            "  AND conte.conte_idseq = de.conte_idseq" +
            "  AND to_char(de.cde_id) LIKE '25%'" +
            "  AND de.latest_version_ind = 'Yes'" +
            "  AND de.de_idseq = acr.ac_idseq (+)" +
            "  AND acr.registration_status = rsl.registration_status (+)" +
            "  AND de.asl_name = asl.asl_name (+)";

    String sql02 = "SELECT DISTINCT de.de_idseq," +
            "                de.preferred_name de_preferred_name," +
            "                de.long_name," +
            "                rd.doc_text," +
            "                conte.name," +
            "                de.asl_name," +
            "                to_char(de.cde_id) de_cdeid," +
            "                de.version de_version," +
            "                meta_config_mgmt.get_usedby(de.de_idseq) de_usedby," +
            "                de.vd_idseq," +
            "                de.dec_idseq," +
            "                de.conte_idseq," +
            "                de.preferred_definition," +
            "                acr.registration_status," +
            "                rsl.display_order," +
            "                asl.display_order wkflow_order," +
            "                de.cde_id cdeid" +
            " FROM sbr.data_elements_view de," +
            "     sbr.reference_documents_view rd," +
            "     sbr.contexts_view conte," +
            "     sbr.ac_registrations_view acr," +
            "     sbr.reg_status_lov_view rsl," +
            "     sbr.ac_status_lov_view asl" +
            " WHERE de.de_idseq IN (SELECT ac_idseq FROM sbr.designations_view des WHERE des.conte_idseq = '99BA9DC8-2095-4E69-E034-080020C9C0E0' " +
            " AND des.detl_name = 'USED_BY' UNION SELECT de_idseq FROM  sbr.data_elements_view de1 WHERE de1.conte_idseq = '99BA9DC8-2095-4E69-E034-080020C9C0E0') " +
            "  AND de.de_idseq = rd.ac_idseq (+)" +
            "  AND rd.dctl_name (+) = 'Preferred Question Text'" +
            " AND de.latest_version_ind = 'Yes'" +
            "  AND nvl(acr.registration_status,'-1') NOT IN ('Retired')" +
            "  AND " + getExcludList() +
            "  AND conte.name NOT IN ('TEST'," +
            "                         'Training')" +
            //"  AND de.asl_name != 'RETIRED DELETED'"+ //removing this status from SQL statement. This status is added to Search Preferences as of release 5.2
            "  AND conte.conte_idseq = de.conte_idseq" +
            "  AND to_char(de.cde_id) LIKE '25%'" +
            "  AND de.latest_version_ind = 'Yes'" +
            "  AND de.de_idseq = acr.ac_idseq (+)" +
            "  AND acr.registration_status = rsl.registration_status (+)" +
            "  AND de.asl_name = asl.asl_name (+)";


}
