/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.model.cdeData.dataElement;

import static org.junit.Assert.*;

import org.junit.Test;

import gov.nih.nci.cadsr.dao.model.CsCsiDeModel;

public class CsCsiTest {

	@Test
	public void testCsCsiCsCsiDeModelBasic() {
		CsCsiDeModel csCsiDeModel = prepateTestCsCsiDeModel("test");
		
		CsCsi csCsiExpected = prepareTestCsCsiBasic("test");		
		
		//MUT
		CsCsi csCsiReceived = new CsCsi(csCsiDeModel);
		
		//check
		assertEquals(csCsiExpected, csCsiReceived);
	}
	
	protected CsCsi prepareTestCsCsiBasic(String prefix) {
		CsCsi csCsiExpected = new CsCsi();
		csCsiExpected.setCsiName(prefix+"csiName");
		csCsiExpected.setCsLongName(prefix+"csLongName");
		csCsiExpected.setCsiType(prefix+"csitlName");
		csCsiExpected.setCsDefinition(prefix+"csDefinition");
		
		return csCsiExpected;
	}
	
	protected CsCsiDeModel prepateTestCsCsiDeModel(String prefix) {
		CsCsiDeModel csCsiDeModel = new CsCsiDeModel();
		csCsiDeModel.setCsDefinition("testcsDefinition");
		csCsiDeModel.setCsitlName("testcsitlName");
		csCsiDeModel.setCsLongName("testcsLongName");
		csCsiDeModel.setCsiName("testcsiName");
		return csCsiDeModel;
	}
}
