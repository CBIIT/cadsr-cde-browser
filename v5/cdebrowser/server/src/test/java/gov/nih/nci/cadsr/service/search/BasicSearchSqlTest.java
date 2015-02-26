package gov.nih.nci.cadsr.service.search;

import junit.framework.TestCase;

public class BasicSearchSqlTest  extends TestCase
{
    TempTestParameters request = new TempTestParameters();

    public void testNameExactPhraseSql()
    {
        DataElementSearchBean searchBean = new DataElementSearchBean();
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request, searchBean, "diastolic", "Exact phrase", 0 );
        String sql = dESearchQueryBuilder.getQueryStmt();
        sql = sql.replaceAll( "  *", " " );
        sql = sql.replaceAll( "\\(  *", "(" );
        sql = sql.replaceAll( "\\)  *", ")" );
        String goodSql = "SELECT distinct de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id) de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq) de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid from sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text' and nvl(acr.registration_status,'-1') NOT IN ( 'Retired' ) and asl.asl_name NOT IN ( 'CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' ) and conte.name NOT IN ('TEST', 'Training' ) and de.asl_name != 'RETIRED DELETED' and conte.conte_idseq = de.conte_idseq and de.latest_version_ind = 'Yes' and de.de_idseq IN ( (select de_idseq from sbr.designations_view dsn, sbr.data_elements_view de1 where de1.de_idseq = dsn.ac_idseq (+) and dsn.detl_name = 'UML Class:UML Attr' and upper (nvl(dsn.name,'%')) like upper ('diastolic') ) union (select de_idseq from sbr.reference_documents_view rd1, sbr.data_elements_view de1 where de1.de_idseq = rd1.ac_idseq (+) and rd1.dctl_name (+) = 'Preferred Question Text' and (upper (de1.long_name) like upper ('diastolic') OR upper (de1.preferred_name) like upper ('diastolic') OR upper (nvl(rd1.doc_text,'%')) like upper ('diastolic') ) union select de_idseq from sbr.reference_documents_view rd2,sbr.data_elements_view de2 where de2.de_idseq = rd2.ac_idseq (+) and rd2.dctl_name (+) = 'Alternate Question Text' and upper (nvl(rd2.doc_text,'%')) like upper ('diastolic') ) ) and de.de_idseq = acr.ac_idseq (+) and acr.registration_status = rsl.registration_status (+) and de.asl_name = asl.asl_name (+)";
        goodSql = goodSql.replaceAll( "  *", " " );
        goodSql = goodSql.replaceAll( "\\(  *", "(" );
        goodSql = goodSql.replaceAll( "\\)  *", ")" );

        assertEquals( goodSql, sql );
    }

    public void testNameAllOfTheWords()
    {
        DataElementSearchBean searchBean = new DataElementSearchBean();
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request, searchBean, "diastolic", "All of the words", 0 );
        String sql = dESearchQueryBuilder.getQueryStmt();
        sql = sql.replaceAll( "  *", " " );
        sql = sql.replaceAll( "\\(  *", "(" );
        sql = sql.replaceAll( "\\)  *", ")" );
        String goodSql = "SELECT distinct de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id) de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq) de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid from sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text' and nvl(acr.registration_status,'-1') NOT IN ( 'Retired' ) and asl.asl_name NOT IN ( 'CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' ) and conte.name NOT IN ('TEST', 'Training' ) and de.asl_name != 'RETIRED DELETED' and conte.conte_idseq = de.conte_idseq and de.latest_version_ind = 'Yes' and de.de_idseq IN ( (select de_idseq from sbr.designations_view dsn, sbr.data_elements_view de1 where de1.de_idseq = dsn.ac_idseq (+) and dsn.detl_name = 'UML Class:UML Attr' and ((upper (nvl(dsn.name,'%')) like upper ('% diastolic %') or upper (nvl(dsn.name,'%')) like upper ('diastolic %') or upper (nvl(dsn.name,'%')) like upper ('diastolic') or upper (nvl(dsn.name,'%')) like upper ('% diastolic'))) ) union (select de_idseq from sbr.reference_documents_view rd1, sbr.data_elements_view de1 where de1.de_idseq = rd1.ac_idseq (+) and rd1.dctl_name (+) = 'Preferred Question Text' and (((upper (de1.long_name) like upper ('% diastolic %') or upper (de1.long_name) like upper ('diastolic %') or upper (de1.long_name) like upper ('diastolic') or upper (de1.long_name) like upper ('% diastolic') )) OR ((upper (de1.preferred_name) like upper ('% diastolic %') or upper (de1.preferred_name) like upper ('diastolic %') or upper (de1.preferred_name) like upper ('diastolic') or upper (de1.preferred_name) like upper ('% diastolic') )) OR ((upper (nvl(rd1.doc_text,'%')) like upper ('% diastolic %') or upper (nvl(rd1.doc_text,'%')) like upper ('diastolic %') or upper (nvl(rd1.doc_text,'%')) like upper ('diastolic') or upper (nvl(rd1.doc_text,'%')) like upper ('% diastolic') ))) union select de_idseq from sbr.reference_documents_view rd2,sbr.data_elements_view de2 where de2.de_idseq = rd2.ac_idseq (+) and rd2.dctl_name (+) = 'Alternate Question Text' and ((upper (nvl(rd2.doc_text,'%')) like upper ('% diastolic %') or upper (nvl(rd2.doc_text,'%')) like upper ('diastolic %') or upper (nvl(rd2.doc_text,'%')) like upper ('diastolic') or upper (nvl(rd2.doc_text,'%')) like upper ('% diastolic') ))) ) and de.de_idseq = acr.ac_idseq (+) and acr.registration_status = rsl.registration_status (+) and de.asl_name = asl.asl_name (+)";
        goodSql = goodSql.replaceAll( "  *", " " );
        goodSql = goodSql.replaceAll( "\\(  *", "(" );
        goodSql = goodSql.replaceAll( "\\)  *", ")" );

        assertEquals( goodSql, sql );
    }

    public void testNameAtLeastOneOfTheWords()
    {
        DataElementSearchBean searchBean = new DataElementSearchBean();
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request, searchBean, "diastolic", " At least one of the words", 0 );
        String sql = dESearchQueryBuilder.getQueryStmt();
        sql = sql.replaceAll( "  *", " " );
        sql = sql.replaceAll( "\\(  *", "(" );
        sql = sql.replaceAll( "\\)  *", ")" );
        String goodSql = "SELECT distinct de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id) de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq) de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid from sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text' and nvl(acr.registration_status,'-1') NOT IN ('Retired' ) and asl.asl_name NOT IN ('CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' ) and conte.name NOT IN ( 'TEST', 'Training' ) and de.asl_name != 'RETIRED DELETED' and conte.conte_idseq = de.conte_idseq and de.latest_version_ind = 'Yes' and de.de_idseq IN ( (select de_idseq from sbr.designations_view dsn, sbr.data_elements_view de1 where de1.de_idseq = dsn.ac_idseq (+) and dsn.detl_name = 'UML Class:UML Attr' and ((upper (nvl(dsn.name,'%')) like upper ('% diastolic %') or upper (nvl(dsn.name,'%')) like upper ('diastolic %') or upper (nvl(dsn.name,'%')) like upper ('diastolic') or upper (nvl(dsn.name,'%')) like upper ('% diastolic'))) ) union (select de_idseq from sbr.reference_documents_view rd1, sbr.data_elements_view de1 where de1.de_idseq = rd1.ac_idseq (+) and rd1.dctl_name (+) = 'Preferred Question Text' and (((upper (de1.long_name) like upper ('% diastolic %') or upper (de1.long_name) like upper ('diastolic %') or upper (de1.long_name) like upper ('diastolic') or upper (de1.long_name) like upper ('% diastolic') )) OR ((upper (de1.preferred_name) like upper ('% diastolic %') or upper (de1.preferred_name) like upper ('diastolic %') or upper (de1.preferred_name) like upper ('diastolic') or upper (de1.preferred_name) like upper ('% diastolic') )) OR ((upper (nvl(rd1.doc_text,'%')) like upper ('% diastolic %') or upper (nvl(rd1.doc_text,'%')) like upper ('diastolic %') or upper (nvl(rd1.doc_text,'%')) like upper ('diastolic') or upper (nvl(rd1.doc_text,'%')) like upper ('% diastolic') ))) union select de_idseq from sbr.reference_documents_view rd2,sbr.data_elements_view de2 where de2.de_idseq = rd2.ac_idseq (+) and rd2.dctl_name (+) = 'Alternate Question Text' and ((upper (nvl(rd2.doc_text,'%')) like upper ('% diastolic %') or upper (nvl(rd2.doc_text,'%')) like upper ('diastolic %') or upper (nvl(rd2.doc_text,'%')) like upper ('diastolic') or upper (nvl(rd2.doc_text,'%')) like upper ('% diastolic') ))) ) and de.de_idseq = acr.ac_idseq (+) and acr.registration_status = rsl.registration_status (+) and de.asl_name = asl.asl_name (+)";
        goodSql = goodSql.replaceAll( "  *", " " );
        goodSql = goodSql.replaceAll( "\\(  *", "(" );
        goodSql = goodSql.replaceAll( "\\)  *", ")" );

        assertEquals( goodSql, sql );

    }


    public void testPublicIdExactPhraseSql()
    {
        DataElementSearchBean searchBean = new DataElementSearchBean();
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request, searchBean, "2183222", "Exact phrase", 1 );
        String sql = dESearchQueryBuilder.getQueryStmt();
        sql = sql.replaceAll( "  *", " " );
        sql = sql.replaceAll( " *, *", "," );
        sql = sql.replaceAll( "\\(  *", "(" );
        sql = sql.replaceAll( "\\)  *", ")" );
        String goodSql = "SELECT distinct de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id) de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq) de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid from sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text' and nvl(acr.registration_status,'-1') NOT IN ('Retired' ) and asl.asl_name NOT IN ('CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' ) and conte.name NOT IN ( 'TEST', 'Training' ) and de.asl_name != 'RETIRED DELETED' and conte.conte_idseq = de.conte_idseq and to_char(de.cde_id) like '2183222' and de.latest_version_ind = 'Yes' and de.de_idseq = acr.ac_idseq (+) and acr.registration_status = rsl.registration_status (+) and de.asl_name = asl.asl_name (+)";
        goodSql = goodSql.replaceAll( "  *", " " );
        goodSql = goodSql.replaceAll(  " *, *", "," );
        goodSql = goodSql.replaceAll( "\\(  *", "(" );
        goodSql = goodSql.replaceAll( "\\)  *", ")" );

        assertEquals( goodSql, sql );
    }

    public void testBuildStatusWhereClause0()
    {
        DataElementSearchBean searchBean = new DataElementSearchBean();
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request, searchBean, "2183222", "Exact phrase", 1 );
        String workFlow = dESearchQueryBuilder.buildStatusWhereClause( dESearchQueryBuilder.statusWhere );
        assertTrue(  "dESearchQueryBuilder.buildStatusWhereClause should have returned an empty String, got [" +  workFlow + "]",  workFlow.isEmpty());
    }

    public void testBuildStatusWhereClause1()
    {
        String[] statusAll = {"ALL"};

        DataElementSearchBean searchBean = new DataElementSearchBean();
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request, searchBean, "2183222", "Exact phrase", 1 );
        String workFlow = dESearchQueryBuilder.buildStatusWhereClause( statusAll );
        assertTrue(  "dESearchQueryBuilder.buildStatusWhereClause should have returned an empty String, got [" +  workFlow + "]",  workFlow.isEmpty());
    }

    public void testBuildStatusWhereClause2()
    {
        String[] statusNull = null;

        DataElementSearchBean searchBean = new DataElementSearchBean();
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request, searchBean, "2183222", "Exact phrase", 1 );

        String workFlow = dESearchQueryBuilder.buildStatusWhereClause( statusNull );
        assertTrue(  "dESearchQueryBuilder.buildStatusWhereClause should have returned an empty String, got [" +  workFlow + "]",  workFlow.isEmpty());
    }

    public void testBuildStatusWhereClause3()
    {
        String[] statusTest1 = {"Value0", "Value1", "Value2"};

        DataElementSearchBean searchBean = new DataElementSearchBean();
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request, searchBean, "2183222", "Exact phrase", 1 );

        String workFlow = dESearchQueryBuilder.buildStatusWhereClause( statusTest1 );
        assertEquals( " and de.asl_name IN ('Value0','Value1','Value2')", workFlow );

    }

    public void testBuildStatusWhereClause4()
    {
        String[] statusEmpty = {""};

        DataElementSearchBean searchBean = new DataElementSearchBean();
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request, searchBean, "2183222", "Exact phrase", 1 );

        String  workFlow = dESearchQueryBuilder.buildStatusWhereClause( statusEmpty );
        assertTrue(  "dESearchQueryBuilder.buildStatusWhereClause should have returned an empty String, got [" +  workFlow + "]",  workFlow.isEmpty());
    }

    public void testBuildRegStatusWhereClause0()
    {
        DataElementSearchBean searchBean = new DataElementSearchBean();
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request, searchBean, "2183222", "Exact phrase", 1 );

        String workFlow = dESearchQueryBuilder.buildRegStatusWhereClause( dESearchQueryBuilder.regStatusesWhere );
        assertTrue(  "dESearchQueryBuilder.buildRegStatusWhereClause should have returned an empty String, got [" +  workFlow + "]",  workFlow.isEmpty());
    }
    public void testBuildRegStatusWhereClause1()
    {
        String[] regStatusAll = {"ALL"};

        DataElementSearchBean searchBean = new DataElementSearchBean();
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request, searchBean, "2183222", "Exact phrase", 1 );
        String workFlow = dESearchQueryBuilder.buildRegStatusWhereClause( regStatusAll );
        assertTrue(  "dESearchQueryBuilder.buildRegStatusWhereClause should have returned an empty String, got [" +  workFlow + "]",  workFlow.isEmpty());
    }

    public void testBuildRegStatusWhereClause2()
    {
        String[] regStatusNull = null;

        DataElementSearchBean searchBean = new DataElementSearchBean();
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request, searchBean, "2183222", "Exact phrase", 1 );

        String workFlow = dESearchQueryBuilder.buildRegStatusWhereClause( regStatusNull );
        assertTrue(  "dESearchQueryBuilder.buildRegStatusWhereClause should have returned an empty String, got [" +  workFlow + "]",  workFlow.isEmpty());
    }
    public void testBuildRegStatusWhereClause3()
    {
        String[] regStatusNull = null;

        DataElementSearchBean searchBean = new DataElementSearchBean();
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request, searchBean, "2183222", "Exact phrase", 1 );
        String workFlow = dESearchQueryBuilder.buildRegStatusWhereClause( regStatusNull );
        assertTrue(  "dESearchQueryBuilder.buildRegStatusWhereClause should have returned an empty String, got [" +  workFlow + "]",  workFlow.isEmpty());
    }

    public void testBuildRegStatusWhereClause4()
    {
        String[] regStatusTest1 = {"Value0", "Value1", "Value2"};

        DataElementSearchBean searchBean = new DataElementSearchBean();
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request, searchBean, "2183222", "Exact phrase", 1 );
        String workFlow = dESearchQueryBuilder.buildRegStatusWhereClause( regStatusTest1 );
        assertEquals( " and acr.registration_status IN ('Value0','Value1','Value2')", workFlow );

    }
    public void testBuildRegStatusWhereClause5()
    {
        String[] regStatusEmpty = {""};

        DataElementSearchBean searchBean = new DataElementSearchBean();
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request, searchBean, "2183222", "Exact phrase", 1 );
        String workFlow = dESearchQueryBuilder.buildRegStatusWhereClause( regStatusEmpty );
        assertTrue(  "dESearchQueryBuilder.buildRegStatusWhereClause should have returned an empty String, got [" +  workFlow + "]",  workFlow.isEmpty());
    }


    public void testValueDomain0()
    {
        DataElementSearchBean searchBean = new DataElementSearchBean();
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request, searchBean, "diastolic", "Exact phrase", 0 );
        dESearchQueryBuilder.valueDomain = "TestValueDomian";
        dESearchQueryBuilder.buildSql();

        String sql = dESearchQueryBuilder.getQueryStmt();
        sql = sql.replaceAll( "  *", " " );
        sql = sql.replaceAll( "\\(  *", "(" );
        sql = sql.replaceAll( "\\)  *", ")" );
        String goodSql = "SELECT distinct de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id) de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq) de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid from sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text' and nvl(acr.registration_status,'-1') NOT IN ( 'Retired' ) and asl.asl_name NOT IN ( 'CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' ) and conte.name NOT IN ('TEST', 'Training' ) and de.asl_name != 'RETIRED DELETED' and conte.conte_idseq = de.conte_idseq and de.latest_version_ind = 'Yes' and de.de_idseq IN ( (select de_idseq from sbr.designations_view dsn, sbr.data_elements_view de1 where de1.de_idseq = dsn.ac_idseq (+) and dsn.detl_name = 'UML Class:UML Attr' and upper (nvl(dsn.name,'%')) like upper ('diastolic') ) union (select de_idseq from sbr.reference_documents_view rd1, sbr.data_elements_view de1 where de1.de_idseq = rd1.ac_idseq (+) and rd1.dctl_name (+) = 'Preferred Question Text' and (upper (de1.long_name) like upper ('diastolic') OR upper (de1.preferred_name) like upper ('diastolic') OR upper (nvl(rd1.doc_text,'%')) like upper ('diastolic') ) union select de_idseq from sbr.reference_documents_view rd2,sbr.data_elements_view de2 where de2.de_idseq = rd2.ac_idseq (+) and rd2.dctl_name (+) = 'Alternate Question Text' and upper (nvl(rd2.doc_text,'%')) like upper ('diastolic') ) ) and de.de_idseq = acr.ac_idseq (+) and acr.registration_status = rsl.registration_status (+) and de.asl_name = asl.asl_name (+)";
        goodSql = goodSql.replaceAll( "  *", " " );
        goodSql = goodSql.replaceAll( "\\(  *", "(" );
        goodSql = goodSql.replaceAll( "\\)  *", ")" );
        //If these are the same, then changing valueDomain had no effect.
        assertTrue( "Changing valueDomain had no effect.", goodSql.compareTo( sql ) != 0);


    }
    public void testValueDomain1()
    {
        DataElementSearchBean searchBean = new DataElementSearchBean();
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request, searchBean, "diastolic", "Exact phrase", 0 );
        dESearchQueryBuilder.valueDomain = "TestValueDomian";
        dESearchQueryBuilder.buildSql();

        String sql = dESearchQueryBuilder.getQueryStmt();
        sql = sql.replaceAll( "  *", " " );
        sql = sql.replaceAll( "\\(  *", "(" );
        sql = sql.replaceAll( "\\)  *", ")" );
        String goodSql = "SELECT distinct de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id)de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq)de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid from sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte ,sbr.value_domains_view vd , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl where de.de_idseq = rd.ac_idseq (+)and rd.dctl_name (+)= 'Preferred Question Text' and nvl(acr.registration_status,'-1')NOT IN ('Retired' )and asl.asl_name NOT IN ('CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' )and conte.name NOT IN ('TEST', 'Training' )and de.asl_name != 'RETIRED DELETED' and conte.conte_idseq = de.conte_idseq and vd.vd_idseq = 'TestValueDomian' and vd.vd_idseq = de.vd_idseq and de.latest_version_ind = 'Yes' and de.de_idseq IN ((select de_idseq from sbr.designations_view dsn, sbr.data_elements_view de1 where de1.de_idseq = dsn.ac_idseq (+)and dsn.detl_name = 'UML Class:UML Attr' and upper (nvl(dsn.name,'%'))like upper ('diastolic'))union (select de_idseq from sbr.reference_documents_view rd1, sbr.data_elements_view de1 where de1.de_idseq = rd1.ac_idseq (+)and rd1.dctl_name (+)= 'Preferred Question Text' and (upper (de1.long_name)like upper ('diastolic')OR upper (de1.preferred_name)like upper ('diastolic')OR upper (nvl(rd1.doc_text,'%'))like upper ('diastolic'))union select de_idseq from sbr.reference_documents_view rd2,sbr.data_elements_view de2 where de2.de_idseq = rd2.ac_idseq (+)and rd2.dctl_name (+)= 'Alternate Question Text' and upper (nvl(rd2.doc_text,'%'))like upper ('diastolic')))and de.de_idseq = acr.ac_idseq (+)and acr.registration_status = rsl.registration_status (+)and de.asl_name = asl.asl_name (+)";
        goodSql = goodSql.replaceAll( "  *", " " );
        goodSql = goodSql.replaceAll( "\\(  *", "(" );
        goodSql = goodSql.replaceAll( "\\)  *", ")" );
        assertEquals( goodSql, sql );
    }

    public void testEmptyQuery()
    {
        DataElementSearchBean searchBean = new DataElementSearchBean();
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request, searchBean, "", "Exact phrase", 0 );
        dESearchQueryBuilder.buildSql();
        assertNull( "An empty query did not give us a null getQueryStmt.", dESearchQueryBuilder.getQueryStmt() );
    }

    public void testBadSearchField()
    {
        DataElementSearchBean searchBean = new DataElementSearchBean();
        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request, searchBean, "diastolic", "Exact phrase", 20 );
        dESearchQueryBuilder.buildSql();
        assertNull( "An bad search field value did not give us a null getQueryStmt.", dESearchQueryBuilder.getQueryStmt() );
    }
}