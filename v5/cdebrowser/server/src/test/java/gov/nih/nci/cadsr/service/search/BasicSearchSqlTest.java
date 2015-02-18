package gov.nih.nci.cadsr.service.search;

import junit.framework.TestCase;

public class BasicSearchSqlTest  extends TestCase
{

    //String goodSql = "SELECT distinct de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id) de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq) de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid from sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text' and nvl(acr.registration_status,'-1') NOT IN ( 'Retired' ) and asl.asl_name NOT IN ( 'CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' ) and conte.name NOT IN ('TEST', 'Training' ) and de.asl_name != 'RETIRED DELETED' and conte.conte_idseq = de.conte_idseq and de.latest_version_ind = 'Yes' and de.de_idseq IN ( (select de_idseq from sbr.designations_view dsn, sbr.data_elements_view de1 where de1.de_idseq = dsn.ac_idseq (+) and dsn.detl_name = 'UML Class:UML Attr' and upper (nvl(dsn.name,'%')) like upper ('tissue') ) union (select de_idseq from sbr.reference_documents_view rd1, sbr.data_elements_view de1 where de1.de_idseq = rd1.ac_idseq (+) and rd1.dctl_name (+) = 'Preferred Question Text' and (upper (de1.long_name) like upper ( 'tissue') OR upper (de1.preferred_name) like upper ( 'tissue') OR upper (nvl(rd1.doc_text,'%')) like upper ('tissue') ) union select de_idseq from sbr.reference_documents_view rd2,sbr.data_elements_view de2 where de2.de_idseq = rd2.ac_idseq (+) and rd2.dctl_name (+) = 'Alternate Question Text' and upper (nvl(rd2.doc_text,'%')) like upper ('tissue') ) ) and de.de_idseq = acr.ac_idseq (+) and acr.registration_status = rsl.registration_status (+) and de.asl_name = asl.asl_name (+) ORDER BY long_name, de_version ";
    String goodSql = "SELECT distinct de.de_idseq ,de.preferred_name de_preferred_name ,de.long_name ,rd.doc_text ,conte.name ,de.asl_name ,to_char(de.cde_id) de_cdeid ,de.version de_version ,meta_config_mgmt.get_usedby(de.de_idseq) de_usedby ,de.vd_idseq ,de.dec_idseq ,de.conte_idseq ,de.preferred_definition ,acr.registration_status ,rsl.display_order ,asl.display_order wkflow_order ,de.cde_id cdeid from sbr.data_elements_view de , sbr.reference_documents_view rd , sbr.contexts_view conte , sbr.ac_registrations_view acr , sbr.reg_status_lov_view rsl , sbr.ac_status_lov_view asl where de.de_idseq = rd.ac_idseq (+) and rd.dctl_name (+) = 'Preferred Question Text' and nvl(acr.registration_status,'-1') NOT IN ( 'Retired' ) and asl.asl_name NOT IN ( 'CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' ) and conte.name NOT IN ('TEST', 'Training' ) and de.asl_name != 'RETIRED DELETED' and conte.conte_idseq = de.conte_idseq and de.latest_version_ind = 'Yes' and de.de_idseq IN ( (select de_idseq from sbr.designations_view dsn, sbr.data_elements_view de1 where de1.de_idseq = dsn.ac_idseq (+) and dsn.detl_name = 'UML Class:UML Attr' and upper (nvl(dsn.name,'%')) like upper ('tissue') ) union (select de_idseq from sbr.reference_documents_view rd1, sbr.data_elements_view de1 where de1.de_idseq = rd1.ac_idseq (+) and rd1.dctl_name (+) = 'Preferred Question Text' and (upper (de1.long_name) like upper ( 'tissue') OR upper (de1.preferred_name) like upper ( 'tissue') OR upper (nvl(rd1.doc_text,'%')) like upper ('tissue') ) union select de_idseq from sbr.reference_documents_view rd2,sbr.data_elements_view de2 where de2.de_idseq = rd2.ac_idseq (+) and rd2.dctl_name (+) = 'Alternate Question Text' and upper (nvl(rd2.doc_text,'%')) like upper ('tissue') ) ) and de.de_idseq = acr.ac_idseq (+) and acr.registration_status = rsl.registration_status (+) and de.asl_name = asl.asl_name (+)";
    public void testSql()
    {

        TempTestParameters request = new TempTestParameters();
        //        String treeParamType = "REGCSI";
        String treeParamType = null;
        String treeParamIdSeq = null; //"99BA9DC8-2094-4E69-E034-080020C9C0E0,Standard";
        String treeConteIdSeq = null; //"99BA9DC8-2094-4E69-E034-080020C9C0E0";
        DataElementSearchBean searchBean = new DataElementSearchBean();


        DESearchQueryBuilder dESearchQueryBuilder = new DESearchQueryBuilder( request, treeParamType, treeParamIdSeq, treeConteIdSeq, searchBean, "tissue", "2" );
        String sql = dESearchQueryBuilder.getQueryStmt();
        sql = sql.replaceAll( "  *", " " );
        System.out.println("SQL: " + sql );

       // assertEquals( goodSql, sql );
    }
}
