package gov.nih.nci.cadsr.service.search;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gov.nih.nci.cadsr.common.WorkflowStatusEnum;
import gov.nih.nci.cadsr.dao.operation.SearchQueryBuilder;

public class SearchQueryBuilderTest
{
	private SearchQueryBuilder searchQueryBuilder = null;
	private String queryText="";
	private String publicId = "25*";
	private String clientSearchMode="0";
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

	@Test
	public void testInitSeqrchQueryBuilder01() throws Exception
	{
		// With workFlow
		searchQueryBuilder.initSeqrchQueryBuilder( queryText, clientSearchMode, publicId, programArea, context, classification, protocol, workFlowStatus, registrationStatus, conceptName, conceptCode);
	}

	@Test
	public void testInitSeqrchQueryBuilder02() throws Exception
	{
		// With out workFlow, make sure workflow is empty, and exclude clause is right
		searchQueryBuilder.initSeqrchQueryBuilder(  queryText, clientSearchMode, publicId, programArea, context, classification, protocol, "", registrationStatus, conceptName, conceptCode);

		assertEquals( " asl.asl_name NOT IN ( 'CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' ) ", WorkflowStatusEnum.getExcludList() );
	}


	@Test
	public void testWorkFlowStatus00() throws Exception
	{
		searchQueryBuilder.initSeqrchQueryBuilder( "", "Exact phrase", publicId, "", "", "", "", "APPRVD FOR TRIAL USE", "", "", "" );
		assertEquals( cleanup( searchQueryBuilder.getSqlStmt()), cleanup( sql00 )   );
	}

	@Test
	public void testWorkFlowStatus01() throws Exception
	{
		searchQueryBuilder.initSeqrchQueryBuilder( "", "Exact phrase", publicId, "", "", "", "", "", "", "", "" );
		assertEquals( cleanup( searchQueryBuilder.getSqlStmt()), cleanup( sql01 )   );
	}

	@Test
	public void testContext00() throws Exception
	{
		searchQueryBuilder.initSeqrchQueryBuilder( "", "Exact phrase", publicId, "", "99BA9DC8-2095-4E69-E034-080020C9C0E0", "", "", "", "", "", "" );
		assertEquals( cleanup( searchQueryBuilder.getSqlStmt()), cleanup( sql02 )   );
	}

	@Test
	public void testClassification00() throws Exception
	{
		searchQueryBuilder.initSeqrchQueryBuilder( "", "Exact phrase", "258*", "", "", "EB5D88AB-6077-69CB-E034-0003BA3F9857", "", "", "", "", "" );
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

	@Test
	public void testProtocolSearchQuery()
	{
		searchQueryBuilder.initSeqrchQueryBuilder( "", "", "", "", "Test", "", "B40DD2C8-A047-DBE1-E040-BB89AD437202", "", "", "", "" );
		assertEquals(cleanup(searchQueryBuilder.getSqlStmt()), cleanup(protocolSearchQuery));
	}

	private String cleanup( String s)
	{
		return s.replaceAll( "\\s\\s*", " " ).replaceAll( "\\s*,\\s*", ", " ).replaceAll( "\\s*\\)\\s*", ")" ).replaceAll( "\\s*\\(\\s*", "(" ).toUpperCase();
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
			"AND             nvl(acr.registration_status,'-1') NOT IN ( 'Retired' ) " +
			"AND             asl.asl_name NOT IN ( 'CMTE APPROVED' , 'CMTE SUBMTD' , 'CMTE SUBMTD USED' , 'RETIRED ARCHIVED' , 'RETIRED PHASED OUT' , 'RETIRED WITHDRAWN' ) " +
			"AND             conte.NAME NOT IN ( 'TEST', 'Training' ) " +
			"AND             de.asl_name != 'RETIRED DELETED' " +
			"AND             conte.conte_idseq = de.conte_idseq AND             pt.proto_idseq = ptfrm.proto_idseq " +
			"AND             frm.qc_idseq = ptfrm.qc_idseq AND frm.qtl_name = 'CRF' " +
			"AND             qc.dn_crf_idseq = frm.qc_idseq AND qc.qtl_name = 'QUESTION' " +
			"AND             qc.de_idseq = de.de_idseq AND pt.proto_idseq = 'B40DD2C8-A047-DBE1-E040-BB89AD437202' " +
			"AND             de.de_idseq = acr.ac_idseq (+) AND acr.registration_status = rsl.registration_status (+) AND de.asl_name = asl.asl_name (+)";

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
			" WHERE de.de_idseq IN (SELECT ac_idseq FROM sbr.designations_view des WHERE des.conte_idseq = '99BA9DC8-2095-4E69-E034-080020C9C0E0' " +
			" AND des.detl_name = 'USED_BY' UNION SELECT de_idseq FROM  sbr.data_elements_view de1 WHERE de1.conte_idseq = '99BA9DC8-2095-4E69-E034-080020C9C0E0') " +
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
