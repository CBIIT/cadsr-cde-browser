/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao.operation;

import static org.junit.Assert.*;

import org.junit.Test;

import gov.nih.nci.cadsr.service.model.search.SearchCriteria;

public class SearchQueryBuilderUtilsTest {

	@Test
	public void testBuildSearchByPublicId() {
		String publicIdFilter = "23, 345, 56*7";
		String expectedSearchSQL = " AND ((de.cde_id = 23) or (de.cde_id = 345) or (TO_CHAR(de.cde_id) like '56%7')) ";
		String received = SearchQueryBuilderUtils.buildSearchByPublicId(publicIdFilter, "de.cde_id");
		assertEquals("Expected SQL fragment for Public ID search", expectedSearchSQL, received);
	}
	@Test
	public void testBuildSearchByPublicId1() {
		String publicIdFilter = "23";
		String expectedSearchSQL = " AND ((de.cde_id = 23)) ";
		String received = SearchQueryBuilderUtils.buildSearchByPublicId(publicIdFilter, "de.cde_id");
		assertEquals("Expected SQL fragment for Public ID search", expectedSearchSQL, received);
	}
	@Test
	public void testBuildSearchByPublicId2() {
		String publicIdFilter = "23, 345";
		String expectedSearchSQL = " AND ((de.cde_id = 23) or (de.cde_id = 345)) ";
		String received = SearchQueryBuilderUtils.buildSearchByPublicId(publicIdFilter, "de.cde_id");
		assertEquals("Expected SQL fragment for Public ID search", expectedSearchSQL, received);
	}
	@Test
	public void testBuildSearchByPublicIdSpaces() {
		String publicIdFilter = "23 345 56*7";
		String expectedSearchSQL = " AND ((de.cde_id = 23) or (de.cde_id = 345) or (TO_CHAR(de.cde_id) like '56%7')) ";
		String received = SearchQueryBuilderUtils.buildSearchByPublicId(publicIdFilter, "de.cde_id");
		assertEquals("Expected SQL fragment for Public ID search", expectedSearchSQL, received);
	}
	
	@Test
	public void testBuildSearchByPublicIdWildCard() {
		String publicIdFilter = " *2*3 ";
		String expectedSearchSQL = " AND ((TO_CHAR(de.cde_id) like '%2%3')) ";
		String received = SearchQueryBuilderUtils.buildSearchByPublicId(publicIdFilter, "de.cde_id");
		assertEquals("Expected SQL fragment for Public ID search", expectedSearchSQL, received);
	}
	
	@Test
	public void testBuildSearchByPublicIdEmpty() {
		String publicIdFilter = "  ";
		String received = SearchQueryBuilderUtils.buildSearchByPublicId(publicIdFilter, "de.cde_id");
		assertEquals("Expected SQL fragment for Public ID search", " AND ((de.cde_id = -1)) ", received);
	}
	
	@Test
	public void testBuildSearchByPublicIdCommas() {
		String publicIdFilter = "  ,   ,";
		String received = SearchQueryBuilderUtils.buildSearchByPublicId(publicIdFilter, "de.cde_id");
		assertEquals("Expected SQL fragment for Public ID search", "", received);
	}
	@Test
	public void testBuildSearchByPublicIdCommasOnly() {
		String publicIdFilter = ",,";
		String received = SearchQueryBuilderUtils.buildSearchByPublicId(publicIdFilter, "de.cde_id");
		assertEquals("Expected SQL fragment for Public ID search", "", received);
	}
	@Test
	public void testBuildSearchByPublicIdNull() {
		String publicIdFilter = null;
		String received = SearchQueryBuilderUtils.buildSearchByPublicId(publicIdFilter, "de.cde_id");
		assertEquals("Expected SQL fragment for Public ID search", " AND ((de.cde_id = -1)) ", received);
	}
	@Test
	public void testBuildSearchByPublicIdAll() {
		String publicIdFilter = "*";
		String expected = " AND ((TO_CHAR(de.cde_id) like '%')) ";
		String received = SearchQueryBuilderUtils.buildSearchByPublicId(publicIdFilter, "de.cde_id");
		assertEquals("Expected SQL fragment for Public ID search", expected, received);
	}
	@Test
	public void testBuildSearchByPublicIdWrongAll() {
		String publicIdFilter = " abc def";
		String expectedSearchSQL = " AND ((de.cde_id = -1)) ";
		String received = SearchQueryBuilderUtils.buildSearchByPublicId(publicIdFilter, "de.cde_id");
		assertEquals("Expected SQL fragment for Public ID search", expectedSearchSQL, received);
	}
	
	@Test
	public void testBuildSearchByPublicIdPartlyWrong() {
		String publicIdFilter = " *2*3 2ae";
		String expectedSearchSQL = " AND ((TO_CHAR(de.cde_id) like '%2%3')) ";
		String received = SearchQueryBuilderUtils.buildSearchByPublicId(publicIdFilter, "de.cde_id");
		assertEquals("Expected SQL fragment for Public ID search", expectedSearchSQL, received);
	}
	
	@Test
	public void testBuildSearchByPublicIdAllWrong() {
		String publicIdFilter = " *2*&3 2a%e*";
		String expectedSearchSQL = " AND ((de.cde_id = -1)) ";
		String received = SearchQueryBuilderUtils.buildSearchByPublicId(publicIdFilter, "de.cde_id");
		assertEquals("Expected SQL fragment for Public ID search", expectedSearchSQL, received);
	}
	
	@Test 
	public void buildValueDomainWhere() {
		SearchCriteria sc = new SearchCriteria();
		sc.setValueDomain("test'SearchPattern*");
		sc.setVdTypeFlag("0");
		String expectedWhere = buildValueDomainStringBoth("test''SearchPattern%", sc.getVdTypeFlag());
		String receivedWhere = SearchQueryBuilderUtils.buildValueDomainWhere(sc);
		assertEquals(expectedWhere, receivedWhere);
	}

	@Test 
	public void buildValueDomainWhereVal() {
		SearchCriteria sc = new SearchCriteria();
		sc.setValueDomain("test'SearchPattern1*");
		String expectedWhere = buildValueDomainStringValue("test''SearchPattern1%");
		String receivedWhere = SearchQueryBuilderUtils.buildValueDomainWhere(sc);
		assertEquals(expectedWhere, receivedWhere);
	}
	
	@Test 
	public void buildValueDomainWhereFlag() {
		SearchCriteria sc = new SearchCriteria();
		sc.setValueDomain("test'SearchPattern2*");
		String expectedWhere = buildValueDomainStringValue("test''SearchPattern2%");
		String receivedWhere = SearchQueryBuilderUtils.buildValueDomainWhere(sc);
		assertEquals(expectedWhere, receivedWhere);
	}
	
	@Test 
	public void buildValueDomainWhereNoValues() {
		SearchCriteria sc = new SearchCriteria();
		String expectedWhere = "";
		String receivedWhere = SearchQueryBuilderUtils.buildValueDomainWhere(sc);
		assertEquals(expectedWhere, receivedWhere);
	}	
	
	@Test 
	public void buildValueDomainWhereEmpties() {
		SearchCriteria sc = new SearchCriteria();
		sc.setValueDomain("  ");
		sc.setVdTypeFlag("3");
		String expectedWhere = "";
		String receivedWhere = SearchQueryBuilderUtils.buildValueDomainWhere(sc);
		assertEquals(expectedWhere, receivedWhere);
	}
	@Test 
	public void buildValueDomainWhereEmpties1() {
		SearchCriteria sc = new SearchCriteria();
		sc.setValueDomain("  ");
		sc.setVdTypeFlag("3  ");
		String expectedWhere = "";
		String receivedWhere = SearchQueryBuilderUtils.buildValueDomainWhere(sc);
		assertEquals(expectedWhere, receivedWhere);
	}

	public static String buildValueDomainStringBoth(String expectedVd, String expectedVdFlag) {
		String vdWhere = " AND upper(vd.long_name) like upper('" + expectedVd + "')"
			+ " AND vd.vd_type_flag = '" + expectedVdFlag + "' AND vd.vd_idseq = de.vd_idseq ";
		return vdWhere;
	}
	public static String buildValueDomainStringValue(String expectedVd) {
		String vdWhere = " AND upper(vd.long_name) like upper('" + expectedVd + "')"
			+ " AND vd.vd_idseq = de.vd_idseq ";
		return vdWhere;
	}
	public static String buildValueDomainStringFlag(String expectedVdFlag) {
		String vdWhere = " AND vd.vd_type_flag = '" + expectedVdFlag + "' AND vd.vd_idseq = de.vd_idseq ";
		return vdWhere;
	}
}
