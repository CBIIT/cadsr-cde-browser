package gov.nih.nci.cadsr.service.search;

import junit.framework.TestCase;

public class DESearchQueryBuilderTest extends TestCase
{
    public void testNameExactPhraseSql()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "diastolic", "Exact phrase", 0 );
        String sql = cleanSql( dESearchQueryBuilder.getQueryStmt() );
        String goodSql = cleanSql( "SELECT DISTINCT de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id) de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq) de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid FROM sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl WHERE de.de_idseq = rd.ac_idseq (+) AND rd.dctl_name (+) = 'Preferred Question Text' AND nvl(acr.registration_status,'-1') NOT IN ( 'Retired' ) AND asl.asl_name NOT IN ( 'CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' ) AND conte.name NOT IN ('TEST', 'Training' ) AND de.asl_name != 'RETIRED DELETED' AND conte.conte_idseq = de.conte_idseq AND de.latest_version_ind = 'Yes' AND de.de_idseq IN ( (SELECT de_idseq FROM sbr.designations_view dsn, sbr.data_elements_view de1 WHERE de1.de_idseq = dsn.ac_idseq (+) AND dsn.detl_name = 'UML Class:UML Attr' AND UPPER (nvl(dsn.name,'%')) LIKE UPPER ('diastolic') ) UNION (SELECT de_idseq FROM sbr.reference_documents_view rd1, sbr.data_elements_view de1 WHERE de1.de_idseq = rd1.ac_idseq (+) AND rd1.dctl_name (+) = 'Preferred Question Text' AND (UPPER (de1.long_name) LIKE UPPER ('diastolic') OR UPPER (de1.preferred_name) LIKE UPPER ('diastolic') OR UPPER (nvl(rd1.doc_text,'%')) LIKE UPPER ('diastolic') ) UNION SELECT de_idseq FROM sbr.reference_documents_view rd2,sbr.data_elements_view de2 WHERE de2.de_idseq = rd2.ac_idseq (+) AND rd2.dctl_name (+) = 'Alternate Question Text' AND UPPER (nvl(rd2.doc_text,'%')) LIKE UPPER ('diastolic') ) ) AND de.de_idseq = acr.ac_idseq (+) AND acr.registration_status = rsl.registration_status (+) AND de.asl_name = asl.asl_name (+)" );
        assertEquals( goodSql, sql );
    }

    public void testNameAllOfTheWords()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "diastolic", "All of the words", 0 );
        String sql = cleanSql( dESearchQueryBuilder.getQueryStmt() );
        String goodSql = cleanSql( "SELECT DISTINCT de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id) de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq) de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid FROM sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl WHERE de.de_idseq = rd.ac_idseq (+) AND rd.dctl_name (+) = 'Preferred Question Text' AND nvl(acr.registration_status,'-1') NOT IN ( 'Retired' ) AND asl.asl_name NOT IN ( 'CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' ) AND conte.name NOT IN ('TEST', 'Training' ) AND de.asl_name != 'RETIRED DELETED' AND conte.conte_idseq = de.conte_idseq AND de.latest_version_ind = 'Yes' AND de.de_idseq IN ( (SELECT de_idseq FROM sbr.designations_view dsn, sbr.data_elements_view de1 WHERE de1.de_idseq = dsn.ac_idseq (+) AND dsn.detl_name = 'UML Class:UML Attr' AND ((UPPER (nvl(dsn.name,'%')) LIKE UPPER ('% diastolic %') OR UPPER (nvl(dsn.name,'%')) LIKE UPPER ('diastolic %') OR UPPER (nvl(dsn.name,'%')) LIKE UPPER ('diastolic') OR UPPER (nvl(dsn.name,'%')) LIKE UPPER ('% diastolic'))) ) UNION (SELECT de_idseq FROM sbr.reference_documents_view rd1, sbr.data_elements_view de1 WHERE de1.de_idseq = rd1.ac_idseq (+) AND rd1.dctl_name (+) = 'Preferred Question Text' AND (((UPPER (de1.long_name) LIKE UPPER ('% diastolic %') OR UPPER (de1.long_name) LIKE UPPER ('diastolic %') OR UPPER (de1.long_name) LIKE UPPER ('diastolic') OR UPPER (de1.long_name) LIKE UPPER ('% diastolic') )) OR ((UPPER (de1.preferred_name) LIKE UPPER ('% diastolic %') OR UPPER (de1.preferred_name) LIKE UPPER ('diastolic %') OR UPPER (de1.preferred_name) LIKE UPPER ('diastolic') OR UPPER (de1.preferred_name) LIKE UPPER ('% diastolic') )) OR ((UPPER (nvl(rd1.doc_text,'%')) LIKE UPPER ('% diastolic %') OR UPPER (nvl(rd1.doc_text,'%')) LIKE UPPER ('diastolic %') OR UPPER (nvl(rd1.doc_text,'%')) LIKE UPPER ('diastolic') OR UPPER (nvl(rd1.doc_text,'%')) LIKE UPPER ('% diastolic') ))) UNION SELECT de_idseq FROM sbr.reference_documents_view rd2,sbr.data_elements_view de2 WHERE de2.de_idseq = rd2.ac_idseq (+) AND rd2.dctl_name (+) = 'Alternate Question Text' AND ((UPPER (nvl(rd2.doc_text,'%')) LIKE UPPER ('% diastolic %') OR UPPER (nvl(rd2.doc_text,'%')) LIKE UPPER ('diastolic %') OR UPPER (nvl(rd2.doc_text,'%')) LIKE UPPER ('diastolic') OR UPPER (nvl(rd2.doc_text,'%')) LIKE UPPER ('% diastolic') ))) ) AND de.de_idseq = acr.ac_idseq (+) AND acr.registration_status = rsl.registration_status (+) AND de.asl_name = asl.asl_name (+)" );
        assertEquals( goodSql, sql );
    }

    public void testNameAtLeastOneOfTheWords()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "diastolic", " At least one of the words", 0 );
        String sql = cleanSql( dESearchQueryBuilder.getQueryStmt() );
        String goodSql = cleanSql( "SELECT DISTINCT de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id) de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq) de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid FROM sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl WHERE de.de_idseq = rd.ac_idseq (+) AND rd.dctl_name (+) = 'Preferred Question Text' AND nvl(acr.registration_status,'-1') NOT IN ('Retired' ) AND asl.asl_name NOT IN ('CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' ) AND conte.name NOT IN ( 'TEST', 'Training' ) AND de.asl_name != 'RETIRED DELETED' AND conte.conte_idseq = de.conte_idseq AND de.latest_version_ind = 'Yes' AND de.de_idseq IN ( (SELECT de_idseq FROM sbr.designations_view dsn, sbr.data_elements_view de1 WHERE de1.de_idseq = dsn.ac_idseq (+) AND dsn.detl_name = 'UML Class:UML Attr' AND ((UPPER (nvl(dsn.name,'%')) LIKE UPPER ('% diastolic %') OR UPPER (nvl(dsn.name,'%')) LIKE UPPER ('diastolic %') OR UPPER (nvl(dsn.name,'%')) LIKE UPPER ('diastolic') OR UPPER (nvl(dsn.name,'%')) LIKE UPPER ('% diastolic'))) ) UNION (SELECT de_idseq FROM sbr.reference_documents_view rd1, sbr.data_elements_view de1 WHERE de1.de_idseq = rd1.ac_idseq (+) AND rd1.dctl_name (+) = 'Preferred Question Text' AND (((UPPER (de1.long_name) LIKE UPPER ('% diastolic %') OR UPPER (de1.long_name) LIKE UPPER ('diastolic %') OR UPPER (de1.long_name) LIKE UPPER ('diastolic') OR UPPER (de1.long_name) LIKE UPPER ('% diastolic') )) OR ((UPPER (de1.preferred_name) LIKE UPPER ('% diastolic %') OR UPPER (de1.preferred_name) LIKE UPPER ('diastolic %') OR UPPER (de1.preferred_name) LIKE UPPER ('diastolic') OR UPPER (de1.preferred_name) LIKE UPPER ('% diastolic') )) OR ((UPPER (nvl(rd1.doc_text,'%')) LIKE UPPER ('% diastolic %') OR UPPER (nvl(rd1.doc_text,'%')) LIKE UPPER ('diastolic %') OR UPPER (nvl(rd1.doc_text,'%')) LIKE UPPER ('diastolic') OR UPPER (nvl(rd1.doc_text,'%')) LIKE UPPER ('% diastolic') ))) UNION SELECT de_idseq FROM sbr.reference_documents_view rd2,sbr.data_elements_view de2 WHERE de2.de_idseq = rd2.ac_idseq (+) AND rd2.dctl_name (+) = 'Alternate Question Text' AND ((UPPER (nvl(rd2.doc_text,'%')) LIKE UPPER ('% diastolic %') OR UPPER (nvl(rd2.doc_text,'%')) LIKE UPPER ('diastolic %') OR UPPER (nvl(rd2.doc_text,'%')) LIKE UPPER ('diastolic') OR UPPER (nvl(rd2.doc_text,'%')) LIKE UPPER ('% diastolic') ))) ) AND de.de_idseq = acr.ac_idseq (+) AND acr.registration_status = rsl.registration_status (+) AND de.asl_name = asl.asl_name (+)" );
        assertEquals( goodSql, sql );
    }


    public void testPublicIdExactPhraseSql()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "2183222", "Exact phrase", 1 );
        String sql = cleanSql( dESearchQueryBuilder.getQueryStmt() );
        String goodSql = cleanSql( "SELECT DISTINCT de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id) de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq) de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid FROM sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl WHERE de.de_idseq = rd.ac_idseq (+) AND rd.dctl_name (+) = 'Preferred Question Text' AND nvl(acr.registration_status,'-1') NOT IN ('Retired' ) AND asl.asl_name NOT IN ('CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' ) AND conte.name NOT IN ( 'TEST', 'Training' ) AND de.asl_name != 'RETIRED DELETED' AND conte.conte_idseq = de.conte_idseq AND to_char(de.cde_id) LIKE '2183222' AND de.latest_version_ind = 'Yes' AND de.de_idseq = acr.ac_idseq (+) AND acr.registration_status = rsl.registration_status (+) AND de.asl_name = asl.asl_name (+)" );
        assertEquals( goodSql, sql );
    }


    public void testBuildStatusWhereClause0()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "2183222", "Exact phrase", 1 );
        String workFlow = dESearchQueryBuilder.buildStatusWhereClause( dESearchQueryBuilder.statusWhere );
        assertTrue( "dESearchQueryBuilder.buildStatusWhereClause should have returned an empty String, got [" + workFlow + "]", workFlow.isEmpty() );
    }

    public void testBuildStatusWhereClause1()
    {
        String[] statusAll = { "ALL" };

        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "2183222", "Exact phrase", 1 );
        String workFlow = dESearchQueryBuilder.buildStatusWhereClause( statusAll );
        assertTrue( "dESearchQueryBuilder.buildStatusWhereClause should have returned an empty String, got [" + workFlow + "]", workFlow.isEmpty() );
    }

    public void testBuildStatusWhereClause2()
    {
        String[] statusNull = null;

        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "2183222", "Exact phrase", 1 );

        String workFlow = dESearchQueryBuilder.buildStatusWhereClause( statusNull );
        assertTrue( "dESearchQueryBuilder.buildStatusWhereClause should have returned an empty String, got [" + workFlow + "]", workFlow.isEmpty() );
    }

    public void testBuildStatusWhereClause3()
    {
        String[] statusTest1 = { "Value0", "Value1", "Value2" };

        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "2183222", "Exact phrase", 1 );

        String workFlow = dESearchQueryBuilder.buildStatusWhereClause( statusTest1 );
        assertEquals( " AND de.asl_name IN ('Value0','Value1','Value2')", workFlow );

    }

    public void testBuildStatusWhereClause4()
    {
        String[] statusTest1 = { "Value0" };

        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "2183222", "Exact phrase", 1 );

        String workFlow = dESearchQueryBuilder.buildStatusWhereClause( statusTest1 );
        assertEquals( " AND de.asl_name = 'Value0'", workFlow );

    }

    public void testBuildStatusWhereClause5()
    {
        String[] statusEmpty = { "" };

        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "2183222", "Exact phrase", 1 );

        String workFlow = dESearchQueryBuilder.buildStatusWhereClause( statusEmpty );
        assertTrue( "dESearchQueryBuilder.buildStatusWhereClause should have returned an empty String, got [" + workFlow + "]", workFlow.isEmpty() );
    }

    public void testBuildRegStatusWhereClause0()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "2183222", "Exact phrase", 1 );

        String workFlow = dESearchQueryBuilder.buildRegStatusWhereClause( dESearchQueryBuilder.regStatusesWhere );
        assertTrue( "dESearchQueryBuilder.buildRegStatusWhereClause should have returned an empty String, got [" + workFlow + "]", workFlow.isEmpty() );
    }

    public void testBuildRegStatusWhereClause1()
    {
        String[] regStatusAll = { "ALL" };

        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "2183222", "Exact phrase", 1 );
        String workFlow = dESearchQueryBuilder.buildRegStatusWhereClause( regStatusAll );
        assertTrue( "dESearchQueryBuilder.buildRegStatusWhereClause should have returned an empty String, got [" + workFlow + "]", workFlow.isEmpty() );
    }

    public void testBuildRegStatusWhereClause2()
    {
        String[] regStatusNull = null;

        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "2183222", "Exact phrase", 1 );

        String workFlow = dESearchQueryBuilder.buildRegStatusWhereClause( regStatusNull );
        assertTrue( "dESearchQueryBuilder.buildRegStatusWhereClause should have returned an empty String, got [" + workFlow + "]", workFlow.isEmpty() );
    }

    public void testBuildRegStatusWhereClause3()
    {
        String[] regStatusNull = null;

        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "2183222", "Exact phrase", 1 );
        String workFlow = dESearchQueryBuilder.buildRegStatusWhereClause( regStatusNull );
        assertTrue( "dESearchQueryBuilder.buildRegStatusWhereClause should have returned an empty String, got [" + workFlow + "]", workFlow.isEmpty() );
    }

    public void testBuildRegStatusWhereClause4()
    {
        String[] regStatusTest1 = { "Value0", "Value1", "Value2" };

        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "2183222", "Exact phrase", 1 );
        String workFlow = dESearchQueryBuilder.buildRegStatusWhereClause( regStatusTest1 );
        assertEquals( " AND acr.registration_status IN ('Value0','Value1','Value2')", workFlow );

    }

    public void testBuildRegStatusWhereClause5()
    {
        String[] regStatusTest1 = { "Value0" };

        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "2183222", "Exact phrase", 1 );
        String workFlow = dESearchQueryBuilder.buildRegStatusWhereClause( regStatusTest1 );
        assertEquals( " AND acr.registration_status = 'Value0'", workFlow );
    }

    public void testBuildRegStatusWhereClause6()
    {
        String[] regStatusEmpty = { "" };

        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "2183222", "Exact phrase", 1 );
        String workFlow = dESearchQueryBuilder.buildRegStatusWhereClause( regStatusEmpty );
        assertTrue( "dESearchQueryBuilder.buildRegStatusWhereClause should have returned an empty String, got [" + workFlow + "]", workFlow.isEmpty() );
    }

    public void testProgramArea0()
    {
        String goodSql = cleanSql( "SELECT DISTINCT de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id)de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq)de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid FROM sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl WHERE de.de_idseq = rd.ac_idseq (+)AND rd.dctl_name (+)= 'Preferred Question Text' AND nvl(acr.registration_status,'-1')NOT IN ('Retired' )AND asl.asl_name NOT IN ('CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' )AND conte.name NOT IN ('TEST', 'Training' )AND de.asl_name != 'RETIRED DELETED' AND conte.conte_idseq = de.conte_idseq AND de.latest_version_ind = 'Yes' AND de.de_idseq IN ((SELECT de_idseq FROM sbr.designations_view dsn, sbr.data_elements_view de1 WHERE de1.de_idseq = dsn.ac_idseq (+)AND dsn.detl_name = 'UML Class:UML Attr' AND UPPER (nvl(dsn.name,'%'))LIKE UPPER ('diastolic'))UNION (SELECT de_idseq FROM sbr.reference_documents_view rd1, sbr.data_elements_view de1 WHERE de1.de_idseq = rd1.ac_idseq (+)AND rd1.dctl_name (+)= 'Preferred Question Text' AND (UPPER (de1.long_name)LIKE UPPER ('diastolic')OR UPPER (de1.preferred_name)LIKE UPPER ('diastolic')OR UPPER (nvl(rd1.doc_text,'%'))LIKE UPPER ('diastolic'))UNION SELECT de_idseq FROM sbr.reference_documents_view rd2,sbr.data_elements_view de2 WHERE de2.de_idseq = rd2.ac_idseq (+)AND rd2.dctl_name (+)= 'Alternate Question Text' AND UPPER (nvl(rd2.doc_text,'%'))LIKE UPPER ('diastolic')))AND de.de_idseq = acr.ac_idseq (+)AND acr.registration_status = rsl.registration_status (+)AND de.asl_name = asl.asl_name (+)" );
        String goodSqlWithProgramArea = cleanSql( "SELECT DISTINCT de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id)de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq)de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid FROM sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl WHERE conte.pal_name = '3' AND de.de_idseq = rd.ac_idseq (+)AND rd.dctl_name (+)= 'Preferred Question Text' AND nvl(acr.registration_status,'-1')NOT IN ('Retired' )AND asl.asl_name NOT IN ('CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' )AND conte.name NOT IN ('TEST', 'Training' )AND de.asl_name != 'RETIRED DELETED' AND conte.conte_idseq = de.conte_idseq AND de.latest_version_ind = 'Yes' AND de.de_idseq IN ((SELECT de_idseq FROM sbr.designations_view dsn, sbr.data_elements_view de1 WHERE de1.de_idseq = dsn.ac_idseq (+)AND dsn.detl_name = 'UML Class:UML Attr' AND UPPER (nvl(dsn.name,'%'))LIKE UPPER ('diastolic'))UNION (SELECT de_idseq FROM sbr.reference_documents_view rd1, sbr.data_elements_view de1 WHERE de1.de_idseq = rd1.ac_idseq (+)AND rd1.dctl_name (+)= 'Preferred Question Text' AND (UPPER (de1.long_name)LIKE UPPER ('diastolic')OR UPPER (de1.preferred_name)LIKE UPPER ('diastolic')OR UPPER (nvl(rd1.doc_text,'%'))LIKE UPPER ('diastolic'))UNION SELECT de_idseq FROM sbr.reference_documents_view rd2,sbr.data_elements_view de2 WHERE de2.de_idseq = rd2.ac_idseq (+)AND rd2.dctl_name (+)= 'Alternate Question Text' AND UPPER (nvl(rd2.doc_text,'%'))LIKE UPPER ('diastolic')))AND de.de_idseq = acr.ac_idseq (+)AND acr.registration_status = rsl.registration_status (+)AND de.asl_name = asl.asl_name (+)");

        // Adding 3 as the Program Area in the constructOR
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "diastolic", "Exact phrase", 0, "3" );
        dESearchQueryBuilder.buildSql();
        String sql = cleanSql( dESearchQueryBuilder.getQueryStmt() );

        // Did adding Program area change the sql String at all
        assertTrue( "Setting ProgramArea in constructor had no effect.", goodSql.compareTo( sql ) != 0 );
        // Did adding Program area change the sql String correctly
        assertEquals( goodSqlWithProgramArea, sql );

        // Adding 3 as the Program Area with setter
        dESearchQueryBuilder  = new DESearchQueryBuilder( "diastolic", "Exact phrase", 0 );
        dESearchQueryBuilder.setProgramArea( "3" );
        dESearchQueryBuilder.buildSql();
        sql = cleanSql( dESearchQueryBuilder.getQueryStmt() );

        // Did adding Program area change the sql String at all
        assertTrue( "Setting ProgramArea with setter had no effect.", goodSql.compareTo( sql ) != 0 );
        // Did adding Program area change the sql String correctly
        assertEquals( goodSqlWithProgramArea, sql );


    }

    public void testValueDomain0()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "diastolic", "Exact phrase", 0 );
        dESearchQueryBuilder.valueDomain = "TestValueDomian";
        dESearchQueryBuilder.buildSql();

        String sql = cleanSql( dESearchQueryBuilder.getQueryStmt() );
        String goodSql = cleanSql( "SELECT DISTINCT de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id) de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq) de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid FROM sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl WHERE de.de_idseq = rd.ac_idseq (+) AND rd.dctl_name (+) = 'Preferred Question Text' AND nvl(acr.registration_status,'-1') NOT IN ( 'Retired' ) AND asl.asl_name NOT IN ( 'CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' ) AND conte.name NOT IN ('TEST', 'Training' ) AND de.asl_name != 'RETIRED DELETED' AND conte.conte_idseq = de.conte_idseq AND de.latest_version_ind = 'Yes' AND de.de_idseq IN ( (SELECT de_idseq FROM sbr.designations_view dsn, sbr.data_elements_view de1 WHERE de1.de_idseq = dsn.ac_idseq (+) AND dsn.detl_name = 'UML Class:UML Attr' AND UPPER (nvl(dsn.name,'%')) LIKE UPPER ('diastolic') ) UNION (SELECT de_idseq FROM sbr.reference_documents_view rd1, sbr.data_elements_view de1 WHERE de1.de_idseq = rd1.ac_idseq (+) AND rd1.dctl_name (+) = 'Preferred Question Text' AND (UPPER (de1.long_name) LIKE UPPER ('diastolic') OR UPPER (de1.preferred_name) LIKE UPPER ('diastolic') OR UPPER (nvl(rd1.doc_text,'%')) LIKE UPPER ('diastolic') ) UNION SELECT de_idseq FROM sbr.reference_documents_view rd2,sbr.data_elements_view de2 WHERE de2.de_idseq = rd2.ac_idseq (+) AND rd2.dctl_name (+) = 'Alternate Question Text' AND UPPER (nvl(rd2.doc_text,'%')) LIKE UPPER ('diastolic') ) ) AND de.de_idseq = acr.ac_idseq (+) AND acr.registration_status = rsl.registration_status (+) AND de.asl_name = asl.asl_name (+)" );

        //If these are the same, then changing valueDomain had no effect.
        assertTrue( "Changing valueDomain had no effect.", goodSql.compareTo( sql ) != 0 );

    }

    public void testValueDomain1()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "diastolic", "Exact phrase", 0 );
        dESearchQueryBuilder.valueDomain = "TestValueDomian";
        dESearchQueryBuilder.buildSql();

        String sql = cleanSql( dESearchQueryBuilder.getQueryStmt() );
        String goodSql = cleanSql( "SELECT DISTINCT de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id)de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq)de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid FROM sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte ,sbr.value_domains_view vd , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl WHERE de.de_idseq = rd.ac_idseq (+)AND rd.dctl_name (+)= 'Preferred Question Text' AND nvl(acr.registration_status,'-1')NOT IN ('Retired' )AND asl.asl_name NOT IN ('CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' )AND conte.name NOT IN ('TEST', 'Training' )AND de.asl_name != 'RETIRED DELETED' AND conte.conte_idseq = de.conte_idseq AND vd.vd_idseq = 'TestValueDomian' AND vd.vd_idseq = de.vd_idseq AND de.latest_version_ind = 'Yes' AND de.de_idseq IN ((SELECT de_idseq FROM sbr.designations_view dsn, sbr.data_elements_view de1 WHERE de1.de_idseq = dsn.ac_idseq (+)AND dsn.detl_name = 'UML Class:UML Attr' AND UPPER (nvl(dsn.name,'%'))LIKE UPPER ('diastolic'))UNION (SELECT de_idseq FROM sbr.reference_documents_view rd1, sbr.data_elements_view de1 WHERE de1.de_idseq = rd1.ac_idseq (+)AND rd1.dctl_name (+)= 'Preferred Question Text' AND (UPPER (de1.long_name)LIKE UPPER ('diastolic')OR UPPER (de1.preferred_name)LIKE UPPER ('diastolic')OR UPPER (nvl(rd1.doc_text,'%'))LIKE UPPER ('diastolic'))UNION SELECT de_idseq FROM sbr.reference_documents_view rd2,sbr.data_elements_view de2 WHERE de2.de_idseq = rd2.ac_idseq (+)AND rd2.dctl_name (+)= 'Alternate Question Text' AND UPPER (nvl(rd2.doc_text,'%'))LIKE UPPER ('diastolic')))AND de.de_idseq = acr.ac_idseq (+)AND acr.registration_status = rsl.registration_status (+)AND de.asl_name = asl.asl_name (+)" );

        assertEquals( goodSql, sql );
    }

    public void testEmptyQuery()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "", "Exact phrase", 0 );
        assertNull( "An empty query did not give us a null getQueryStmt.", dESearchQueryBuilder.getQueryStmt() );
    }

    public void testBadSearchField()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "diastolic", "Exact phrase", 20 );
        assertNull( "An bad search field value did not give us a null getQueryStmt.", dESearchQueryBuilder.getQueryStmt() );
    }

    public void testBuildSearchTextWhere0()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "diastolic", "Exact phrase", 0 );
        dESearchQueryBuilder.searchIn[0] = "ALL";
        dESearchQueryBuilder.buildSql();

        String sql = cleanSql( dESearchQueryBuilder.getQueryStmt() );
        String goodSql = cleanSql( "SELECT DISTINCT de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id) de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq) de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid FROM sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl WHERE de.de_idseq = rd.ac_idseq (+) AND rd.dctl_name (+) = 'Preferred Question Text' AND nvl(acr.registration_status,'-1') NOT IN ( 'Retired' ) AND asl.asl_name NOT IN ( 'CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' ) AND conte.name NOT IN ('TEST', 'Training' ) AND de.asl_name != 'RETIRED DELETED' AND conte.conte_idseq = de.conte_idseq AND de.latest_version_ind = 'Yes' AND de.de_idseq IN ( (SELECT de_idseq FROM sbr.designations_view dsn, sbr.data_elements_view de1 WHERE de1.de_idseq = dsn.ac_idseq (+) AND dsn.detl_name = 'UML Class:UML Attr' AND UPPER (nvl(dsn.name,'%')) LIKE UPPER ('diastolic') ) UNION (SELECT de_idseq FROM sbr.reference_documents_view rd1, sbr.data_elements_view de1 WHERE de1.de_idseq = rd1.ac_idseq (+) AND rd1.dctl_name (+) = 'Preferred Question Text' AND (UPPER (de1.long_name) LIKE UPPER ('diastolic') OR UPPER (de1.preferred_name) LIKE UPPER ('diastolic') OR UPPER (nvl(rd1.doc_text,'%')) LIKE UPPER ('diastolic') ) UNION SELECT de_idseq FROM sbr.reference_documents_view rd2,sbr.data_elements_view de2 WHERE de2.de_idseq = rd2.ac_idseq (+) AND rd2.dctl_name (+) = 'Alternate Question Text' AND UPPER (nvl(rd2.doc_text,'%')) LIKE UPPER ('diastolic') ) ) AND de.de_idseq = acr.ac_idseq (+) AND acr.registration_status = rsl.registration_status (+) AND de.asl_name = asl.asl_name (+)" );

        assertEquals( goodSql, sql );
    }

    public void testBuildSearchTextWhere1()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "diastolic", "Exact phrase", 0 );
        dESearchQueryBuilder.searchIn[0] = "Long Name";
        dESearchQueryBuilder.buildSql();

        String sql = cleanSql( dESearchQueryBuilder.getQueryStmt() );
        String goodSql = cleanSql( "SELECT DISTINCT de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id)de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq)de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid FROM sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl WHERE de.de_idseq = rd.ac_idseq (+)AND rd.dctl_name (+)= 'Preferred Question Text' AND nvl(acr.registration_status,'-1')NOT IN ('Retired' )AND asl.asl_name NOT IN ('CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' )AND conte.name NOT IN ('TEST', 'Training' )AND de.asl_name != 'RETIRED DELETED' AND conte.conte_idseq = de.conte_idseq AND de.latest_version_ind = 'Yes' AND de.de_idseq IN (SELECT de_idseq FROM sbr.data_elements_view de1 WHERE UPPER (de1.long_name)LIKE UPPER ('diastolic'))AND de.de_idseq = acr.ac_idseq (+)AND acr.registration_status = rsl.registration_status (+)AND de.asl_name = asl.asl_name (+)" );

        assertEquals( goodSql, sql );
    }


    public void testGetExcludeWhereClause0()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "diastolic", "Exact phrase", 0 );
        String columnName = "asl.asl_name";
        String[] excludeStrings = { "exclude_0", "exclude_1", "exclude_2" };

        String excludeWhereClause = cleanSql( dESearchQueryBuilder.getExcludeWhereClause( columnName, excludeStrings ) );
        String goodExcludeWhereClause = cleanSql( " asl.asl_name NOT IN ('exclude_0'   , 'exclude_1' , 'exclude_2' ) " );
        assertEquals( goodExcludeWhereClause, excludeWhereClause );
    }

    public void testGetExcludeWhereClause1()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "diastolic", "Exact phrase", 0 );
        String columnName = "asl.asl_name";
        String[] excludeStrings = { "exclude_0" };

        String excludeWhereClause = cleanSql( dESearchQueryBuilder.getExcludeWhereClause( columnName, excludeStrings ) );
        String goodExcludeWhereClause = cleanSql( " asl.asl_name NOT IN ('exclude_0' ) " );

        assertEquals( goodExcludeWhereClause, excludeWhereClause );
    }

    public void testGetExcludeWhereClause2()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "diastolic", "Exact phrase", 0 );
        String columnName = "asl.asl_name";
        String[] excludeStrings = null;
        assertNull( dESearchQueryBuilder.getExcludeWhereClause( columnName, excludeStrings ) );
    }

    public void testGetExcludeWhereClause3()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "diastolic", "Exact phrase", 0 );
        String columnName = "asl.asl_name";
        String[] excludeStrings = { };
        assertNull( dESearchQueryBuilder.getExcludeWhereClause( columnName, excludeStrings ) );
    }

    private String cleanSql( String sql )
    {
        sql = sql.replaceAll( "  *", " " ).replaceAll( "\\(  *", "(" ).replaceAll( "\\)  *", ")" );
        return sql;
    }
}
