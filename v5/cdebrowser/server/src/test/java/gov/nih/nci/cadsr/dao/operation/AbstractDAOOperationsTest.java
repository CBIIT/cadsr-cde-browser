/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.dao.operation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AbstractDAOOperationsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCleanUpIdDuplicates() {
		List<String> listGiven = new ArrayList<>();
		List<String> listExpected = new ArrayList<>();		
		listGiven.add("1");
		listGiven.add("2");	
		listGiven.add("1");	
		listGiven.add("2");	
		listGiven.add("3");	
		listGiven.add("4");	
		
		listExpected.add("1");
		listExpected.add("2");
		listExpected.add("3");
		listExpected.add("4");
		//MUT
		List<String> listReceived = AbstractDAOOperations.cleanUpIdDuplicates(listGiven);
		assertEquals(4, listReceived.size());
		assertEquals(listExpected, listReceived);
	}
	
	@Test
	public void testCleanUpIdDuplicatesOne() {
		List<String> listGiven = new ArrayList<>();
		List<String> listExpected = new ArrayList<>();		
		listGiven.add("1");
		listGiven.add("1");	
		listGiven.add("1");	
		listGiven.add("1");	
		listGiven.add("1");	
		listGiven.add("1");	
		
		listExpected.add("1");
		//MUT
		List<String> listReceived = AbstractDAOOperations.cleanUpIdDuplicates(listGiven);
		assertEquals(1, listReceived.size());
		assertEquals(listExpected, listReceived);
	}
}
