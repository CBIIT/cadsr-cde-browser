package gov.nih.nci.cadsr.service.search;

import junit.framework.TestCase;

public class BasicSearchSqlTest extends TestCase
{
    public void testNameExactPhraseSql()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "diastolic", "Exact phrase", 0 );
        String sql = cleanSql( dESearchQueryBuilder.getQueryStmt() );
        String goodSql = cleanSql( "SELECT distinct de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id) de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq) de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid from sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text' and nvl(acr.registration_status,'-1') NOT IN ( 'Retired' ) and asl.asl_name NOT IN ( 'CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' ) and conte.name NOT IN ('TEST', 'Training' ) and de.asl_name != 'RETIRED DELETED' and conte.conte_idseq = de.conte_idseq and de.latest_version_ind = 'Yes' and de.de_idseq IN ( (select de_idseq from sbr.designations_view dsn, sbr.data_elements_view de1 where de1.de_idseq = dsn.ac_idseq (+) and dsn.detl_name = 'UML Class:UML Attr' and upper (nvl(dsn.name,'%')) like upper ('diastolic') ) union (select de_idseq from sbr.reference_documents_view rd1, sbr.data_elements_view de1 where de1.de_idseq = rd1.ac_idseq (+) and rd1.dctl_name (+) = 'Preferred Question Text' and (upper (de1.long_name) like upper ('diastolic') OR upper (de1.preferred_name) like upper ('diastolic') OR upper (nvl(rd1.doc_text,'%')) like upper ('diastolic') ) union select de_idseq from sbr.reference_documents_view rd2,sbr.data_elements_view de2 where de2.de_idseq = rd2.ac_idseq (+) and rd2.dctl_name (+) = 'Alternate Question Text' and upper (nvl(rd2.doc_text,'%')) like upper ('diastolic') ) ) and de.de_idseq = acr.ac_idseq (+) and acr.registration_status = rsl.registration_status (+) and de.asl_name = asl.asl_name (+)" );
        assertEquals( goodSql, sql );
    }

    public void testNameAllOfTheWords()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "diastolic", "All of the words", 0 );
        String sql = cleanSql( dESearchQueryBuilder.getQueryStmt() );
        String goodSql = cleanSql( "SELECT distinct de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id) de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq) de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid from sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text' and nvl(acr.registration_status,'-1') NOT IN ( 'Retired' ) and asl.asl_name NOT IN ( 'CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' ) and conte.name NOT IN ('TEST', 'Training' ) and de.asl_name != 'RETIRED DELETED' and conte.conte_idseq = de.conte_idseq and de.latest_version_ind = 'Yes' and de.de_idseq IN ( (select de_idseq from sbr.designations_view dsn, sbr.data_elements_view de1 where de1.de_idseq = dsn.ac_idseq (+) and dsn.detl_name = 'UML Class:UML Attr' and ((upper (nvl(dsn.name,'%')) like upper ('% diastolic %') or upper (nvl(dsn.name,'%')) like upper ('diastolic %') or upper (nvl(dsn.name,'%')) like upper ('diastolic') or upper (nvl(dsn.name,'%')) like upper ('% diastolic'))) ) union (select de_idseq from sbr.reference_documents_view rd1, sbr.data_elements_view de1 where de1.de_idseq = rd1.ac_idseq (+) and rd1.dctl_name (+) = 'Preferred Question Text' and (((upper (de1.long_name) like upper ('% diastolic %') or upper (de1.long_name) like upper ('diastolic %') or upper (de1.long_name) like upper ('diastolic') or upper (de1.long_name) like upper ('% diastolic') )) OR ((upper (de1.preferred_name) like upper ('% diastolic %') or upper (de1.preferred_name) like upper ('diastolic %') or upper (de1.preferred_name) like upper ('diastolic') or upper (de1.preferred_name) like upper ('% diastolic') )) OR ((upper (nvl(rd1.doc_text,'%')) like upper ('% diastolic %') or upper (nvl(rd1.doc_text,'%')) like upper ('diastolic %') or upper (nvl(rd1.doc_text,'%')) like upper ('diastolic') or upper (nvl(rd1.doc_text,'%')) like upper ('% diastolic') ))) union select de_idseq from sbr.reference_documents_view rd2,sbr.data_elements_view de2 where de2.de_idseq = rd2.ac_idseq (+) and rd2.dctl_name (+) = 'Alternate Question Text' and ((upper (nvl(rd2.doc_text,'%')) like upper ('% diastolic %') or upper (nvl(rd2.doc_text,'%')) like upper ('diastolic %') or upper (nvl(rd2.doc_text,'%')) like upper ('diastolic') or upper (nvl(rd2.doc_text,'%')) like upper ('% diastolic') ))) ) and de.de_idseq = acr.ac_idseq (+) and acr.registration_status = rsl.registration_status (+) and de.asl_name = asl.asl_name (+)" );
        assertEquals( goodSql, sql );
    }

    public void testNameAtLeastOneOfTheWords()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "diastolic", " At least one of the words", 0 );
        String sql = cleanSql( dESearchQueryBuilder.getQueryStmt() );
        String goodSql = cleanSql( "SELECT distinct de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id) de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq) de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid from sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text' and nvl(acr.registration_status,'-1') NOT IN ('Retired' ) and asl.asl_name NOT IN ('CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' ) and conte.name NOT IN ( 'TEST', 'Training' ) and de.asl_name != 'RETIRED DELETED' and conte.conte_idseq = de.conte_idseq and de.latest_version_ind = 'Yes' and de.de_idseq IN ( (select de_idseq from sbr.designations_view dsn, sbr.data_elements_view de1 where de1.de_idseq = dsn.ac_idseq (+) and dsn.detl_name = 'UML Class:UML Attr' and ((upper (nvl(dsn.name,'%')) like upper ('% diastolic %') or upper (nvl(dsn.name,'%')) like upper ('diastolic %') or upper (nvl(dsn.name,'%')) like upper ('diastolic') or upper (nvl(dsn.name,'%')) like upper ('% diastolic'))) ) union (select de_idseq from sbr.reference_documents_view rd1, sbr.data_elements_view de1 where de1.de_idseq = rd1.ac_idseq (+) and rd1.dctl_name (+) = 'Preferred Question Text' and (((upper (de1.long_name) like upper ('% diastolic %') or upper (de1.long_name) like upper ('diastolic %') or upper (de1.long_name) like upper ('diastolic') or upper (de1.long_name) like upper ('% diastolic') )) OR ((upper (de1.preferred_name) like upper ('% diastolic %') or upper (de1.preferred_name) like upper ('diastolic %') or upper (de1.preferred_name) like upper ('diastolic') or upper (de1.preferred_name) like upper ('% diastolic') )) OR ((upper (nvl(rd1.doc_text,'%')) like upper ('% diastolic %') or upper (nvl(rd1.doc_text,'%')) like upper ('diastolic %') or upper (nvl(rd1.doc_text,'%')) like upper ('diastolic') or upper (nvl(rd1.doc_text,'%')) like upper ('% diastolic') ))) union select de_idseq from sbr.reference_documents_view rd2,sbr.data_elements_view de2 where de2.de_idseq = rd2.ac_idseq (+) and rd2.dctl_name (+) = 'Alternate Question Text' and ((upper (nvl(rd2.doc_text,'%')) like upper ('% diastolic %') or upper (nvl(rd2.doc_text,'%')) like upper ('diastolic %') or upper (nvl(rd2.doc_text,'%')) like upper ('diastolic') or upper (nvl(rd2.doc_text,'%')) like upper ('% diastolic') ))) ) and de.de_idseq = acr.ac_idseq (+) and acr.registration_status = rsl.registration_status (+) and de.asl_name = asl.asl_name (+)" );
        assertEquals( goodSql, sql );
    }


    public void testPublicIdExactPhraseSql()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "2183222", "Exact phrase", 1 );
        String sql = cleanSql( dESearchQueryBuilder.getQueryStmt() );
        String goodSql = cleanSql( "SELECT distinct de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id) de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq) de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid from sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text' and nvl(acr.registration_status,'-1') NOT IN ('Retired' ) and asl.asl_name NOT IN ('CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' ) and conte.name NOT IN ( 'TEST', 'Training' ) and de.asl_name != 'RETIRED DELETED' and conte.conte_idseq = de.conte_idseq and to_char(de.cde_id) like '2183222' and de.latest_version_ind = 'Yes' and de.de_idseq = acr.ac_idseq (+) and acr.registration_status = rsl.registration_status (+) and de.asl_name = asl.asl_name (+)" );
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
        assertEquals( " and de.asl_name IN ('Value0','Value1','Value2')", workFlow );

    }

    public void testBuildStatusWhereClause4()
    {
        String[] statusTest1 = { "Value0" };

        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "2183222", "Exact phrase", 1 );

        String workFlow = dESearchQueryBuilder.buildStatusWhereClause( statusTest1 );
        assertEquals( " and de.asl_name = 'Value0'", workFlow );

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
        assertEquals( " and acr.registration_status IN ('Value0','Value1','Value2')", workFlow );

    }

    public void testBuildRegStatusWhereClause5()
    {
        String[] regStatusTest1 = { "Value0" };

        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "2183222", "Exact phrase", 1 );
        String workFlow = dESearchQueryBuilder.buildRegStatusWhereClause( regStatusTest1 );
        assertEquals( " and acr.registration_status = 'Value0'", workFlow );
    }

    public void testBuildRegStatusWhereClause6()
    {
        String[] regStatusEmpty = { "" };

        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "2183222", "Exact phrase", 1 );
        String workFlow = dESearchQueryBuilder.buildRegStatusWhereClause( regStatusEmpty );
        assertTrue( "dESearchQueryBuilder.buildRegStatusWhereClause should have returned an empty String, got [" + workFlow + "]", workFlow.isEmpty() );
    }


    public void testValueDomain0()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "diastolic", "Exact phrase", 0 );
        dESearchQueryBuilder.valueDomain = "TestValueDomian";
        dESearchQueryBuilder.buildSql();

        String sql = cleanSql( dESearchQueryBuilder.getQueryStmt() );
        String goodSql = cleanSql( "SELECT distinct de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id) de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq) de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid from sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text' and nvl(acr.registration_status,'-1') NOT IN ( 'Retired' ) and asl.asl_name NOT IN ( 'CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' ) and conte.name NOT IN ('TEST', 'Training' ) and de.asl_name != 'RETIRED DELETED' and conte.conte_idseq = de.conte_idseq and de.latest_version_ind = 'Yes' and de.de_idseq IN ( (select de_idseq from sbr.designations_view dsn, sbr.data_elements_view de1 where de1.de_idseq = dsn.ac_idseq (+) and dsn.detl_name = 'UML Class:UML Attr' and upper (nvl(dsn.name,'%')) like upper ('diastolic') ) union (select de_idseq from sbr.reference_documents_view rd1, sbr.data_elements_view de1 where de1.de_idseq = rd1.ac_idseq (+) and rd1.dctl_name (+) = 'Preferred Question Text' and (upper (de1.long_name) like upper ('diastolic') OR upper (de1.preferred_name) like upper ('diastolic') OR upper (nvl(rd1.doc_text,'%')) like upper ('diastolic') ) union select de_idseq from sbr.reference_documents_view rd2,sbr.data_elements_view de2 where de2.de_idseq = rd2.ac_idseq (+) and rd2.dctl_name (+) = 'Alternate Question Text' and upper (nvl(rd2.doc_text,'%')) like upper ('diastolic') ) ) and de.de_idseq = acr.ac_idseq (+) and acr.registration_status = rsl.registration_status (+) and de.asl_name = asl.asl_name (+)" );

        //If these are the same, then changing valueDomain had no effect.
        assertTrue( "Changing valueDomain had no effect.", goodSql.compareTo( sql ) != 0 );

    }

    public void testValueDomain1()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "diastolic", "Exact phrase", 0 );
        dESearchQueryBuilder.valueDomain = "TestValueDomian";
        dESearchQueryBuilder.buildSql();

        String sql = cleanSql( dESearchQueryBuilder.getQueryStmt() );
        String goodSql = cleanSql( "SELECT distinct de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id)de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq)de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid from sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte ,sbr.value_domains_view vd , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl where de.de_idseq = rd.ac_idseq (+)and rd.dctl_name (+)= 'Preferred Question Text' and nvl(acr.registration_status,'-1')NOT IN ('Retired' )and asl.asl_name NOT IN ('CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' )and conte.name NOT IN ('TEST', 'Training' )and de.asl_name != 'RETIRED DELETED' and conte.conte_idseq = de.conte_idseq and vd.vd_idseq = 'TestValueDomian' and vd.vd_idseq = de.vd_idseq and de.latest_version_ind = 'Yes' and de.de_idseq IN ((select de_idseq from sbr.designations_view dsn, sbr.data_elements_view de1 where de1.de_idseq = dsn.ac_idseq (+)and dsn.detl_name = 'UML Class:UML Attr' and upper (nvl(dsn.name,'%'))like upper ('diastolic'))union (select de_idseq from sbr.reference_documents_view rd1, sbr.data_elements_view de1 where de1.de_idseq = rd1.ac_idseq (+)and rd1.dctl_name (+)= 'Preferred Question Text' and (upper (de1.long_name)like upper ('diastolic')OR upper (de1.preferred_name)like upper ('diastolic')OR upper (nvl(rd1.doc_text,'%'))like upper ('diastolic'))union select de_idseq from sbr.reference_documents_view rd2,sbr.data_elements_view de2 where de2.de_idseq = rd2.ac_idseq (+)and rd2.dctl_name (+)= 'Alternate Question Text' and upper (nvl(rd2.doc_text,'%'))like upper ('diastolic')))and de.de_idseq = acr.ac_idseq (+)and acr.registration_status = rsl.registration_status (+)and de.asl_name = asl.asl_name (+)" );

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
        String goodSql = cleanSql( "SELECT distinct de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id) de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq) de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid from sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text' and nvl(acr.registration_status,'-1') NOT IN ( 'Retired' ) and asl.asl_name NOT IN ( 'CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' ) and conte.name NOT IN ('TEST', 'Training' ) and de.asl_name != 'RETIRED DELETED' and conte.conte_idseq = de.conte_idseq and de.latest_version_ind = 'Yes' and de.de_idseq IN ( (select de_idseq from sbr.designations_view dsn, sbr.data_elements_view de1 where de1.de_idseq = dsn.ac_idseq (+) and dsn.detl_name = 'UML Class:UML Attr' and upper (nvl(dsn.name,'%')) like upper ('diastolic') ) union (select de_idseq from sbr.reference_documents_view rd1, sbr.data_elements_view de1 where de1.de_idseq = rd1.ac_idseq (+) and rd1.dctl_name (+) = 'Preferred Question Text' and (upper (de1.long_name) like upper ('diastolic') OR upper (de1.preferred_name) like upper ('diastolic') OR upper (nvl(rd1.doc_text,'%')) like upper ('diastolic') ) union select de_idseq from sbr.reference_documents_view rd2,sbr.data_elements_view de2 where de2.de_idseq = rd2.ac_idseq (+) and rd2.dctl_name (+) = 'Alternate Question Text' and upper (nvl(rd2.doc_text,'%')) like upper ('diastolic') ) ) and de.de_idseq = acr.ac_idseq (+) and acr.registration_status = rsl.registration_status (+) and de.asl_name = asl.asl_name (+)" );

        assertEquals( goodSql, sql );
    }

    public void testBuildSearchTextWhere1()
    {
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( "diastolic", "Exact phrase", 0 );
        dESearchQueryBuilder.searchIn[0] = "Long Name";
        dESearchQueryBuilder.buildSql();

        String sql = cleanSql( dESearchQueryBuilder.getQueryStmt() );
        String goodSql = cleanSql( "SELECT distinct de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id)de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq)de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid from sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl where de.de_idseq = rd.ac_idseq (+)and rd.dctl_name (+)= 'Preferred Question Text' and nvl(acr.registration_status,'-1')NOT IN ('Retired' )and asl.asl_name NOT IN ('CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' )and conte.name NOT IN ('TEST', 'Training' )and de.asl_name != 'RETIRED DELETED' and conte.conte_idseq = de.conte_idseq and de.latest_version_ind = 'Yes' and de.de_idseq IN (select de_idseq from sbr.data_elements_view de1 where upper (de1.long_name)like upper ('diastolic'))and de.de_idseq = acr.ac_idseq (+)and acr.registration_status = rsl.registration_status (+)and de.asl_name = asl.asl_name (+)" );

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