/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao.operation;

import static org.junit.Assert.*;

import org.junit.Test;

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
		assertEquals("Expected SQL fragment for Public ID search", "", received);
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
		assertEquals("Expected SQL fragment for Public ID search", "", received);
	}
	@Test
	public void testBuildSearchByPublicIdAll() {
		String publicIdFilter = "*";
		String expected = " AND ((TO_CHAR(de.cde_id) like '%')) ";
		String received = SearchQueryBuilderUtils.buildSearchByPublicId(publicIdFilter, "de.cde_id");
		assertEquals("Expected SQL fragment for Public ID search", expected, received);
	}
}
