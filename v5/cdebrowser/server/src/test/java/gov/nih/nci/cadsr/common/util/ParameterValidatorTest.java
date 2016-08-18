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
	@Test
	public void testValidateIdSeqOK() {
		assertTrue(ParameterValidator.validateIdSeq("D6C8D723-02FA-6501-E034-0003BA12F5E7"));
	}
	@Test
	public void testValidateIdSeqShort() {
		assertFalse(ParameterValidator.validateIdSeq("D6C8D723-02FA-6501-E034-0003BA12F5E"));
	}
	@Test
	public void testValidateIdSeqLong() {
		assertFalse(ParameterValidator.validateIdSeq("D6C8D723-02FA-6501-E034-0003BA12F5E71"));
	}
	@Test
	public void testValidateIdSeqWrongFormat1() {
		assertFalse(ParameterValidator.validateIdSeq("1D6C8D72302FA-6501-E034-0003BA12F5E7"));
	}
	@Test
	public void testValidateIdSeqWrongFormat2() {
		assertFalse(ParameterValidator.validateIdSeq("2D6C8D723-02FA6501-E034-0003BA12F5E7"));
	}
	@Test
	public void testValidateIdSeqWrongFormat3() {
		assertFalse(ParameterValidator.validateIdSeq("3D6C8D723-02FA-6501E034-0003BA12F5E7"));
	}
	@Test
	public void testValidateIdSeqWrong4() {
		assertFalse(ParameterValidator.validateIdSeq("D6C-D723-02FA-6501-E034-000-BA12F5E7"));
	}
}
