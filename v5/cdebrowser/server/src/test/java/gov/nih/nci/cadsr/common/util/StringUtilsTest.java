/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.common.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void testBuildArrayFromParameterNoSeparator() {
		String source = "abc test";
		String[] received = StringUtilities.buildArrayFromParameter(source);
		assertEquals(1, received.length);
		assertEquals("abc test", received[0]);
	}
	@Test
	public void testBuildArrayFromParameterNull() {
		String source = null;
		String[] received = StringUtilities.buildArrayFromParameter(source);
		assertNull(received);
	}
	@Test
	public void testBuildArrayFromParameterSeparator2() {
		String source = "abc test:::mnb test:::";
		String[] received = StringUtilities.buildArrayFromParameter(source);
		assertEquals(2, received.length);
		assertEquals("abc test", received[0]);
		assertEquals("mnb test", received[1]);
	}
	@Test
	public void testBuildArrayFromParameterSeparator1() {
		String source = "abc test:::mnb test";
		String[] received = StringUtilities.buildArrayFromParameter(source);
		assertEquals(2, received.length);
		assertEquals("abc test", received[0]);
		assertEquals("mnb test", received[1]);
	}
	@Test
	public void testBuildArrayFromParameterSeparator3() {
		String source = "abc test:::mnb test:::poi test";
		String[] received = StringUtilities.buildArrayFromParameter(source);
		assertEquals(3, received.length);
		assertEquals("abc test", received[0]);
		assertEquals("mnb test", received[1]);
		assertEquals("poi test", received[2]);
	}
	@Test
	public void testContainsKeyLoopTrue() {
		String[] source = {"ALL", "test val", null};
		boolean received = StringUtilities.containsKeyLoop(source, "ALL");
		assertTrue(received);
	}
	@Test
	public void testContainsKeyLoopTrue1() {
		String[] source = {"ALL", "test val", null};
		boolean received = StringUtilities.containsKeyLoop(source, "test val");
		assertTrue(received);
	}
	@Test
	public void testContainsKeyLoopTrue2() {
		String[] source = {"ALL"};
		boolean received = StringUtilities.containsKeyLoop(source, "ALL");
		assertTrue(received);
	}
	@Test
	public void testContainsKeyLoopWithNull() {
		String[] source = {"ALL", "test val", null};
		boolean received = StringUtilities.containsKeyLoop(source, null);
		assertTrue(received);
	}
	@Test
	public void testContainsKeyLoopFalse() {
		String[] source = {"ALL", "test val", null};
		boolean received = StringUtilities.containsKeyLoop(source, "mnb test");
		assertFalse(received);
	}
	@Test
	public void testContainsKeyLoopFalse1() {
		String[] source = {"test val"};
		boolean received = StringUtilities.containsKeyLoop(source, "mnb test");
		assertFalse(received);
	}
	@Test
	public void testContainsKeyLoopNull() {
		boolean received = StringUtilities.containsKeyLoop(null, null);
		assertFalse(received);
	}
	@Test
	public void testContainsKeyLoopNull1() {
		boolean received = StringUtilities.containsKeyLoop(null, "abc");
		assertFalse(received);
	}
}
