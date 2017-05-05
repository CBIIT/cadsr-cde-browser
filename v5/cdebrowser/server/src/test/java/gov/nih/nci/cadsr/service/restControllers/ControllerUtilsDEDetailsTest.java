/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import gov.nih.nci.cadsr.dao.CsCsiDeDAO;
import gov.nih.nci.cadsr.dao.model.CsCsiDeModel;
import gov.nih.nci.cadsr.dao.model.DefinitionModelAlt;
import gov.nih.nci.cadsr.dao.model.DesignationModelAlt;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.AlternateDefinition;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.AlternateName;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.CsCsi;


public class ControllerUtilsDEDetailsTest {
//FIXME fix commented tests
	@Mock
	private CsCsiDeDAO csCsiDeDAO;
	private static String deIdseq = "Seq";
	private static final String ALT_NAME = "altName";
	private static final String DEFIN = "defin";
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	@After 
	public void cleanUp() {
		Mockito.reset(csCsiDeDAO);
	}
	@Test
	public void testPopulateCsCsiDeModelNoDesigNorDefs() throws Exception{
		List<CsCsiDeModel> csCsiDeModelList = prepareDeCsCsiTestList(deIdseq, 3);
		Mockito.when(csCsiDeDAO.getCsCsisByDeId(deIdseq)).thenReturn(csCsiDeModelList);
		csCsiDeDAO.getCsCsisByDeId(deIdseq);
		List<CsCsi> csCsilList = ControllerUtils.populateCsCsiDeModel(deIdseq, csCsiDeDAO);
		assertEquals(0, csCsilList.size());
	}
	//TODO check the data returned by MUT
	public List<CsCsi> prepareCsCsiListExpected (int numCsCsiModel, int numCsCsiModelToUse, int numAltNames, int numDefs) {
		List<CsCsiDeModel> csCsiDeModelList = prepareDeCsCsiTestList(deIdseq, numCsCsiModel);
		List<CsCsi> csCsiExpected = new ArrayList<>();
		List<AlternateName> altNames;
		List<AlternateDefinition> altDefins;
		for (int i = 0; ((i < csCsiDeModelList.size()) && (i < numCsCsiModelToUse)); i++) {
			CsCsi csCsi = new CsCsi(csCsiDeModelList.get(i));
			altNames = prepareAltNamesExpected(ALT_NAME, i, numAltNames);
			csCsi.setAlternateNames(altNames);
			altDefins = prepareDefinsExpected(DEFIN, i, numDefs);
			csCsi.setAlternateDefinitions(altDefins);
			csCsiExpected.add(csCsi);
		}
		return csCsiExpected;
	}
	//@Test
	public void testPopulateCsCsiDeModelBoth3All() throws Exception{
		//MUT
		List<CsCsi> csCsilList = testPopulateCsCsiModel(3, 3, 2, 2);
	}
	
	@Test
	public void testPopulateCsCsiDeModelNames2All() throws Exception{
		List<CsCsi> csCsilList = testPopulateCsCsiModel(2, 2, 2, 0);
		assertEquals(2, csCsilList.size());
	}

	//@Test
	public void testPopulateCsCsiDeModelDefins3All() throws Exception{
		List<CsCsi> csCsilList = testPopulateCsCsiModel(4, 4, 0, 3);
		assertEquals(4, csCsilList.size());
	}
	
	//@Test
	public void testPopulateCsCsiDeModelSubBoth2() throws Exception{
		List<CsCsi> csCsilList = testPopulateCsCsiModel(3, 2, 2, 2);
		System.out.println("csCsilList:" + csCsilList.toString());
		assertEquals(2, csCsilList.size());
	}
	
	@Test
	public void testPopulateCsCsiDeModelSubNames2() throws Exception{
		List<CsCsi> csCsilList = testPopulateCsCsiModel(2, 1, 2, 0);
		assertEquals(1, csCsilList.size());
	}

	//@Test
	public void testPopulateCsCsiDeModelSubDefins3() throws Exception{
		List<CsCsi> csCsilList = testPopulateCsCsiModel(4, 3, 0, 3);
		assertEquals(3, csCsilList.size());
	}

	//@Test
	public void testPopulateCsCsiDeModel1Both3All() throws Exception{
		List<CsCsi> csCsilList = testPopulateCsCsiModel(1, 1, 2, 2);
		assertEquals(1, csCsilList.size());
	}
	
	@Test
	public void testPopulateCsCsiDeModel1Names2All() throws Exception{
		List<CsCsi> csCsilList = testPopulateCsCsiModel(1, 1, 2, 0);
		assertEquals(1, csCsilList.size());
	}

	//@Test
	public void testPopulateCsCsiDeModel1Defins3() throws Exception{
		List<CsCsi> csCsilList = testPopulateCsCsiModel(4, 1, 0, 3);
		assertEquals(1, csCsilList.size());
	}
	/**
	 * Expected in assert csCsiNumToUse <= numCsCsi.
	 * The method is simplified to add the same amount of Names or Definitions to each CsCsi which shal have either of them.
	 * 
	 * @param numCsCsi CsCsi Model amount
	 * @param csCsiNumToUse CsCsi which will have either Alt Names or Definitions or both
	 * @param numAltNames Amount of names to add to each CsCsiModel
	 * @param numDefins Amount of definitions to add to each CsCsiModel
	 * @return List<CsCsi>
	 */
	public List<CsCsi> testPopulateCsCsiModel(int numCsCsi, int csCsiNumToUse, int numAltNames, int numDefins) {
		List<CsCsiDeModel> csCsiDeModelList = prepareDeCsCsiTestList(deIdseq, numCsCsi);
		//build expected
		List<CsCsi> csCsiExpected = prepareCsCsiListExpected(csCsiDeModelList.size(), csCsiNumToUse, numAltNames, numDefins);
		List<DesignationModelAlt> deCsCsiAltNames=null;
		List<DefinitionModelAlt> deCsCsiDefins=null;

		deCsCsiAltNames = prepareAltNames(csCsiDeModelList, ALT_NAME, csCsiNumToUse, numAltNames);

		deCsCsiDefins = prepareDefins(csCsiDeModelList, DEFIN, csCsiNumToUse, numDefins);

		Mockito.when(csCsiDeDAO.getCsCsisByDeId(deIdseq)).thenReturn(csCsiDeModelList);
		Mockito.when(csCsiDeDAO.getCsCsiDeAltNamesById(deIdseq, csCsiDeModelList)).thenReturn(deCsCsiAltNames);
		Mockito.when(csCsiDeDAO.getCsCsiDeDefinitionsById(deIdseq, csCsiDeModelList)).thenReturn(deCsCsiDefins);

		//MUT
		List<CsCsi> csCsilList = ControllerUtils.populateCsCsiDeModel(deIdseq, csCsiDeDAO);
		
		Mockito.verify(csCsiDeDAO).getCsCsisByDeId(deIdseq);
		Mockito.verify(csCsiDeDAO).getCsCsiDeAltNamesById(deIdseq, csCsiDeModelList);
		Mockito.verify(csCsiDeDAO).getCsCsiDeDefinitionsById(deIdseq, csCsiDeModelList);
		
		assertNotNull(csCsilList);
		assertEquals(csCsiNumToUse, csCsilList.size());
		assertEquals(csCsiExpected, csCsilList);
		
		return csCsilList;
	}
	
	public static List<CsCsiDeModel> prepareDeCsCsiTestList(Object prefix, int amount) {
		List<CsCsiDeModel> deCsCsiList = new ArrayList<>();
		for (int i = 0; i < amount; i++) {
			deCsCsiList.add(prepareCsCsiModel(prefix.toString()+i));
		}
		
		return deCsCsiList;
	}
	
	public static List<CsCsi> prepareDeCsCsiExpectedList(Object prefix, int amount) {
		List<CsCsi> csCsiList = new ArrayList<>();
		for (int i = 0; i < amount; i++) {
			csCsiList.add(prepareCsCsiExpected(prefix.toString()+i));
		}
		
		return csCsiList;
	}
	public List<DesignationModelAlt> prepareAltNames(List<CsCsiDeModel> csCsiDeModelList, Object prefix, int amount) {
		return prepareAltNames(csCsiDeModelList, prefix, csCsiDeModelList.size(), amount);
	}

	public List<DesignationModelAlt> prepareAltNames(List<CsCsiDeModel> csCsiDeModelList, Object prefix, int csCsiToTake, int amount) {
		List<DesignationModelAlt> deCsCsiList = new ArrayList<>();
		for (int i = 0; ((i < csCsiDeModelList.size()) && (i < csCsiToTake)); i++) {
			CsCsiDeModel csCsiDeModel = csCsiDeModelList.get(i);		
			for (int j = 0; j < amount; j++) {
				deCsCsiList.add(prepareCsCsiAltName((prefix.toString()+i)+j, csCsiDeModel.getCsCsiIdseq()));
			}
		}
		return deCsCsiList;
	}
	
	public List<DefinitionModelAlt> prepareDefins(List<CsCsiDeModel> csCsiDeModelList, Object prefix, int amount) {
		return prepareDefins(csCsiDeModelList, prefix, csCsiDeModelList.size(), amount);
	}
	
	public List<DefinitionModelAlt> prepareDefins(List<CsCsiDeModel> csCsiDeModelList, Object prefix, int csCsiToTake, int amount) {
		List<DefinitionModelAlt> deCsCsiList = new ArrayList<>();
		for (int i = 0; ((i < csCsiDeModelList.size()) && (i < csCsiToTake)); i++) {
			CsCsiDeModel csCsiDeModel = csCsiDeModelList.get(i);
			for (int j = 0; j < amount; j++) {
				deCsCsiList.add(prepareCsCsiDefin((prefix.toString()+i) + j, csCsiDeModel.getCsCsiIdseq()));
			}
		}
		return deCsCsiList;
	}
	
	public static CsCsiDeModel prepareCsCsiModel(Object prefix) {
		CsCsiDeModel csCsiModel = new CsCsiDeModel();
		csCsiModel.setCsCsiIdseq("testIdseq" + prefix);
		csCsiModel.setCsiName("testName"+ prefix);
		csCsiModel.setCsitlName("testType"+ prefix);
		csCsiModel.setCsLongName("testLongName"+ prefix);
		csCsiModel.setCsDefinition("testDefinition"+ prefix);
		return csCsiModel;
	}
	
	public static DesignationModelAlt prepareCsCsiAltName(Object prefix, String csCsiIdseq) {
		DesignationModelAlt model = new DesignationModelAlt();
		model.setDesigIdseq(csCsiIdseq);
		model.setName("testName"+ prefix);
		model.setType("testType"+ prefix);
		model.setLang("testLang"+ prefix);
		model.setContextName("testContext" + prefix);
		return model;
	}
	
	public static DefinitionModelAlt prepareCsCsiDefin(Object prefix, String csCsiIdseq) {
		DefinitionModelAlt model = new DefinitionModelAlt();
		model.setDefinIdseq(csCsiIdseq);
		model.setDefinition("testName"+ prefix);
		model.setType("testType"+ prefix);
		model.setLang("testLang"+ prefix);
		model.setContextName("testContext" + prefix);
		return model;
	}
	
	public static CsCsi prepareCsCsiExpected(Object prefix) {
		CsCsi csCsi = new CsCsi();
		csCsi.setCsiName("testName"+ prefix);
		csCsi.setCsiType("testType"+ prefix);
		csCsi.setCsLongName("testLongName"+ prefix);
		csCsi.setCsDefinition("testDefinition"+ prefix);
		return csCsi;
	}

	public List<AlternateName> prepareAltNamesExpected(Object prefix, int csCsiOrderNum, int amount) {
		List<AlternateName> list = new ArrayList<>();
	
		for (int j = 0; j < amount; j++) {
			list.add(prepareAltNameExpected((prefix.toString()+csCsiOrderNum)+j));
		}
		
		return list;
	}
	
	public static AlternateName prepareAltNameExpected(Object prefix) {
		AlternateName model = new AlternateName();
		model.setName("testName"+ prefix);
		model.setType("testType"+ prefix);
		model.setLanguage("testLang"+ prefix);
		model.setContext("testContext" + prefix);
		return model;
	}
	
	public List<AlternateDefinition> prepareDefinsExpected(Object prefix, int csCsiOrderNum, int amount) {
		List<AlternateDefinition> list = new ArrayList<>();

		for (int j = 0; j < amount; j++) {
			list.add(prepareDefinitionExpected((prefix.toString()+csCsiOrderNum) + j));
		}
	
		return list;
	}
	
	public static AlternateDefinition prepareDefinitionExpected(Object prefix) {
		AlternateDefinition model = new AlternateDefinition();
		model.setName("testName"+ prefix);
		model.setType("testType"+ prefix);
		model.setContext("testContext" + prefix);
		return model;
	}
}
