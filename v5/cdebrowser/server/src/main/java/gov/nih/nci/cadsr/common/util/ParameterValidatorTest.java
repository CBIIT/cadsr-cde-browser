/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.common.util;

import static org.junit.Assert.*;

import org.junit.Test;
/**
 * 
 * @author asafievan
 *
 */
public class ParameterValidatorTest {

	@Test
	public void testJustNumbers() {
		assertTrue(ParameterValidator.validatePublicIdWIthStar("123"));
	}
	@Test
	public void testWithStar() {
		assertTrue(ParameterValidator.validatePublicIdWIthStar("123*"));
	}
	@Test
	public void testEmpty() {
		assertFalse(ParameterValidator.validatePublicIdWIthStar(""));
	}
	@Test
	public void testNull() {
		assertFalse(ParameterValidator.validatePublicIdWIthStar(null));
	}
	@Test
	public void testStartBeginning() {
		assertTrue(ParameterValidator.validatePublicIdWIthStar("*123"));
	}
	@Test
	public void tesStartOnEnds() {
		assertTrue(ParameterValidator.validatePublicIdWIthStar("*123*"));
	}
	@Test
	public void testManyStarts() {
		assertTrue(ParameterValidator.validatePublicIdWIthStar("*1*2*3*"));
	}
	@Test
	public void testStarInMiddle() {
		assertTrue(ParameterValidator.validatePublicIdWIthStar("1*23*45"));
	}
	@Test
	public void testNotNumber() {
		assertFalse(ParameterValidator.validatePublicIdWIthStar("abc%v"));
	}
	@Test
	public void testNotNumberLetters1() {
		assertFalse(ParameterValidator.validatePublicIdWIthStar("aBcv"));
	}
	@Test
	public void testNotNumberLetters() {
		assertFalse(ParameterValidator.validatePublicIdWIthStar("aB*cv"));
	}
}
