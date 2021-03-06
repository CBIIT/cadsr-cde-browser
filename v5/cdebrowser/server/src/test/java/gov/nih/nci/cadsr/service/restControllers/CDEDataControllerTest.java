/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
/**
 * 
 */
package gov.nih.nci.cadsr.service.restControllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import gov.nih.nci.cadsr.common.UsageLog;
import gov.nih.nci.cadsr.dao.ConceptDAO;
import gov.nih.nci.cadsr.dao.CsCsiDeDAO;
import gov.nih.nci.cadsr.dao.CsCsiValueMeaningDAO;
import gov.nih.nci.cadsr.dao.DataElementDAOImpl;
import gov.nih.nci.cadsr.dao.DataElementDerivationDAO;
import gov.nih.nci.cadsr.dao.DefinitionDAO;
import gov.nih.nci.cadsr.dao.DesignationDAO;
import gov.nih.nci.cadsr.dao.ObjectClassConceptDAO;
import gov.nih.nci.cadsr.dao.PermissibleValuesDAO;
import gov.nih.nci.cadsr.dao.PropertyConceptDAO;
import gov.nih.nci.cadsr.dao.ReferenceDocDAO;
import gov.nih.nci.cadsr.dao.RepresentationConceptsDAO;
import gov.nih.nci.cadsr.dao.ToolOptionsDAO;
import gov.nih.nci.cadsr.dao.ValueDomainConceptDAO;
import gov.nih.nci.cadsr.dao.ValueDomainDAO;
import gov.nih.nci.cadsr.dao.ValueMeaningDAO;
import gov.nih.nci.cadsr.dao.model.CsCsiDeModel;
import gov.nih.nci.cadsr.dao.model.CsCsiModel;
import gov.nih.nci.cadsr.dao.model.DEOtherVersionsModel;
import gov.nih.nci.cadsr.dao.model.DataElementConceptModel;
import gov.nih.nci.cadsr.dao.model.DataElementModel;
import gov.nih.nci.cadsr.dao.model.ReferenceDocModel;
import gov.nih.nci.cadsr.dao.model.ValueDomainModel;
import gov.nih.nci.cadsr.dao.model.ValueMeaningUiModel;
import gov.nih.nci.cadsr.service.ServiceTestUtils;
import gov.nih.nci.cadsr.service.model.cdeData.CdeDetails;
import gov.nih.nci.cadsr.service.model.cdeData.adminInfo.AdminInfo;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.AlternateName;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.CsCsi;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.DataElement;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.ReferenceDocument;

/**
 * @author asafievan
 *
 */
@WebAppConfiguration
@ContextConfiguration("classpath:test-application-context.xml")
@TestExecutionListeners( {DependencyInjectionTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class CDEDataControllerTest {
	CDEDataController cdeDataController;
	@Mock
    private DataElementDAOImpl dataElementDAO = mock(DataElementDAOImpl.class);

    @Mock
    private ReferenceDocDAO referenceDocDAO = mock(ReferenceDocDAO.class);

    @Mock
    private PermissibleValuesDAO permissibleValuesDAO = mock(PermissibleValuesDAO.class);

    @Mock
    private RepresentationConceptsDAO representationConceptsDAO = mock(RepresentationConceptsDAO.class);

    @Mock
    private DataElementDerivationDAO dataElementDerivationDAO = mock(DataElementDerivationDAO.class);

    @Mock
    private ObjectClassConceptDAO objectClassConceptDAO = mock(ObjectClassConceptDAO.class);

    @Mock
    private PropertyConceptDAO propertyConceptDAO = mock(PropertyConceptDAO.class);

    @Mock
    private ValueDomainConceptDAO valueDomainConceptDAO = mock(ValueDomainConceptDAO.class);

    @Mock
    private ConceptDAO conceptDAO = mock(ConceptDAO.class);

    @Mock
    private ToolOptionsDAO toolOptionsDAO = mock(ToolOptionsDAO.class);

    @Mock
    private CsCsiValueMeaningDAO csCsiValueMeaningDAO = mock(CsCsiValueMeaningDAO.class);

    @Mock
    private DefinitionDAO definitionDAO = mock(DefinitionDAO.class);

    @Mock
    private DesignationDAO designationDAO = mock(DesignationDAO.class);

    @Mock
    private CsCsiDeDAO csCsiDeDAO = mock(CsCsiDeDAO.class);

    @Mock
    private ValueMeaningDAO valueMeaningDAO = mock(ValueMeaningDAO.class);
    
    @Mock
    private ValueDomainDAO valueDomainDAO = mock(ValueDomainDAO.class);
    
    @Mock
    private UsageLog usageLog = new UsageLog();
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		cdeDataController = new CDEDataController();
		cdeDataController.setConceptDAO(conceptDAO);
		cdeDataController.setDataElementDAO(dataElementDAO);
		dataElementDAO.setValueDomainDAO(valueDomainDAO);
		cdeDataController.setDataElementDerivationDAO(dataElementDerivationDAO);
		cdeDataController.setObjectClassConceptDAO(objectClassConceptDAO);
		cdeDataController.setPermissibleValuesDAO(permissibleValuesDAO);
		cdeDataController.setPropertyConceptDAO(propertyConceptDAO);
		cdeDataController.setReferenceDocDAO(referenceDocDAO);
		cdeDataController.setRepresentationConceptsDAO(representationConceptsDAO);
		cdeDataController.setToolOptionsDAO(toolOptionsDAO);
		cdeDataController.setValueDomainConceptDAO(valueDomainConceptDAO);
		cdeDataController.setValueMeaningDAO(valueMeaningDAO);
		cdeDataController.setCsCsiDeDAO(csCsiDeDAO);
		cdeDataController.setCsCsiValueMeaningDAO(csCsiValueMeaningDAO);
		cdeDataController.setDataElementDerivationDAO(dataElementDerivationDAO);
		cdeDataController.setUsageLog(usageLog);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		//dataElementDAO.
		Mockito.reset(dataElementDAO, csCsiDeDAO, valueMeaningDAO);
	}

	/**
	 * Test method for {@link gov.nih.nci.cadsr.service.restControllers.CDEDataController#retrieveDataElementDetails(java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public void testRetrieveDataElementDetails() throws Exception {
		String paramdeIdseq = "2678D639-5518-7AFC-E050-007F010036FF";
		String vdIdseqTest = "2678D639-5518-7AFC-E050-007F01003688";
		float deVersionToReturn = 8.4f;
		int publicIdToReturn = 123456789;
		String vdContextNameExpected = "vdContextNameTest";
		List<ValueMeaningUiModel> valueMeaningUiModelToReturn = new ArrayList<>();
		List<CsCsiDeModel> modelList = new ArrayList<>();
		//mock
		when(csCsiDeDAO.getCsCsisByAcId(paramdeIdseq)).thenReturn(modelList);
		//mock
		when(valueMeaningDAO.getUiValueMeanings(Mockito.eq(publicIdToReturn), Mockito.eq(deVersionToReturn))).thenReturn(valueMeaningUiModelToReturn);
		
		DataElementModel dataElementModelToReturn = new DataElementModel();
		dataElementModelToReturn.setVdIdseq(vdIdseqTest);
		dataElementModelToReturn.setDeIdseq(paramdeIdseq);
		dataElementModelToReturn.setPublicId(publicIdToReturn);
		dataElementModelToReturn.setCdeId(publicIdToReturn);
		dataElementModelToReturn.setVersion(deVersionToReturn);
		dataElementModelToReturn.setDateCreated(new Timestamp(System.currentTimeMillis()));
		dataElementModelToReturn.setDateModified(new Timestamp(System.currentTimeMillis()));
		ValueDomainModel valueDomainModelToReturn = new ValueDomainModel();
		valueDomainModelToReturn.setVersion(3.3f);
		valueDomainModelToReturn.setCdVersion(4.8f);
		valueDomainModelToReturn.setVdContextName(vdContextNameExpected);
		valueDomainModelToReturn.setDateCreated(new Timestamp(System.currentTimeMillis()));
		valueDomainModelToReturn.setDateModified(new Timestamp(System.currentTimeMillis()));
		assertNotNull(valueDomainModelToReturn);

		//mock how DE DAO is implemented we do not call the method below when we mock dataElementDAO
		//when(valueDomainDAO.getValueDomainByIdseq(Mockito.eq(vdIdseqTest))).thenReturn(valueDomainModelToReturn);
		
		DataElementConceptModel decToReturn = new DataElementConceptModel();
		decToReturn.setVersion(1.0f);
		decToReturn.setCdVersion(2.2f);
		decToReturn.setDateCreated(new Timestamp(System.currentTimeMillis()));
		decToReturn.setDateModified(new Timestamp(System.currentTimeMillis()));
		dataElementModelToReturn.setDec(decToReturn);
		dataElementModelToReturn.setValueDomainModel(valueDomainModelToReturn);
		List<ReferenceDocModel> refDocsToReturn = new ArrayList<>();
		dataElementModelToReturn.setRefDocs(refDocsToReturn);
		HashMap<String, CsCsiModel> hashMapToReturn = new HashMap<>();
		CsCsiModel unclassCsCsiModelToReturn = new CsCsiModel();
		hashMapToReturn.put(CsCsiModel.UNCLASSIFIED, unclassCsCsiModelToReturn);
		dataElementModelToReturn.setCsCsiData(hashMapToReturn);
		dataElementModelToReturn.setClassifications(new ArrayList());
		dataElementModelToReturn.setCsRefDocModels(new ArrayList());
		dataElementModelToReturn.setCsiRefDocModels(new ArrayList());
		HashMap<String, List<String>> csCsiDesignationsToReturn = new HashMap<>();
		dataElementModelToReturn.setCsCsiDesignations(csCsiDesignationsToReturn);
		HashMap<String, List<String>> csCsiDefinitionsToReturn = new HashMap<>();
		dataElementModelToReturn.setCsCsiDefinitions(csCsiDefinitionsToReturn);
		List<DEOtherVersionsModel> deOtherVersionsModelsToReturn = new ArrayList<>();
		dataElementModelToReturn.setDeOtherVersionsModels(deOtherVersionsModelsToReturn);
		//mock
		when(dataElementDAO.getCdeByDeIdseq(paramdeIdseq)).thenReturn(dataElementModelToReturn);
		//MUT
		CdeDetails cdedetailsReceived = cdeDataController.retrieveDataElementDetails(paramdeIdseq);
		//check
		assertNotNull(cdedetailsReceived);
		assertEquals(vdContextNameExpected, cdedetailsReceived.getValueDomain().getValueDomainDetails().getContext());
		//verify
		Mockito.verify(valueMeaningDAO).getUiValueMeanings(publicIdToReturn, deVersionToReturn);
		Mockito.verify(dataElementDAO).getCdeByDeIdseq(paramdeIdseq);
	}
	@Test
	public void testGetDataElementReferenceDocuments() {
		DataElementModel dataElementModelDB = ServiceTestUtils.buildTestRecordDataElementModel();
		dataElementModelDB.setRefDocs(ServiceTestUtils.buildTestReferenceDocModelList());
		dataElementModelDB.getRefDocs().add(ServiceTestUtils.buildTestReferenceDocModel("QuestionText1", "1-Question Text"));
		dataElementModelDB.getRefDocs().add(ServiceTestUtils.buildTestReferenceDocModel("QuestionText2", "2-Question Text"));
		DataElement dataElement = new DataElement();
		cdeDataController.getDataElementReferenceDocuments(dataElementModelDB, dataElement);
		assertNotNull(dataElement.getReferenceDocuments());
		assertEquals(dataElementModelDB.getRefDocs().size(), dataElement.getReferenceDocuments().size());
		List<ReferenceDocument> received = dataElement.getReferenceDocuments();
		List<ReferenceDocModel> source = dataElementModelDB.getRefDocs();
		int index = 0;
		for (ReferenceDocument curr : received) {
			assertTrue(ServiceTestUtils.comparaReferenceDocumentsData(curr, source.get(index++)));
		}
	}
	@Test
	public void testGetDataElementReferenceDocumentsDetails() {
		DataElementModel dataElementModelDB = ServiceTestUtils.buildTestRecordDataElementModel();
		dataElementModelDB.setRefDocs(ServiceTestUtils.buildTestReferenceDocModelList());
		dataElementModelDB.getRefDocs().add(ServiceTestUtils.buildTestReferenceDocModel("QuestionText1", "1-Question Text"));
		dataElementModelDB.getRefDocs().add(ServiceTestUtils.buildTestReferenceDocModel("QuestionText2", "2-Question Text"));
		DataElement dataElement = new DataElement();
		cdeDataController.getDataElementReferenceDocuments(dataElementModelDB, dataElement, true);
		assertNotNull(dataElement.getReferenceDocuments());
		assertEquals(dataElement.getReferenceDocuments().size(), 0);
		List<ReferenceDocument> receivedOther = dataElement.getOtherReferenceDocuments();
		List<ReferenceDocument> receivedQuestionTexts = dataElement.getQuestionTextReferenceDocuments();
		assertNotNull(receivedOther);
		assertNotNull(receivedQuestionTexts);
		assertEquals(dataElementModelDB.getRefDocs().size(), (receivedOther.size() + receivedQuestionTexts.size()));
		assertEquals(receivedQuestionTexts.size(), 2);

		List<ReferenceDocModel> source = dataElementModelDB.getRefDocs();
		for (int index = 0; index < receivedOther.size(); index++) {
			assertTrue(ServiceTestUtils.comparaReferenceDocumentsData(receivedOther.get(index), source.get(index)));
		}
		for (int index = receivedOther.size(); index < source.size(); index++) {
			assertTrue(ServiceTestUtils.comparaReferenceDocumentsData(receivedQuestionTexts.get(index - receivedOther.size()), source.get(index)));
		}
	}
	
	@Test
	public void testInitAdminInfoData() throws Exception {
		String paramdeIdseq = "903FF021-F4F5-5A41-E040-BB89AD433D33";
		String vdIdseqTest = "903FF021-F44B-5A41-E040-BB89AD433D33";
		float deVersionToReturn = 8.4f;
		int publicIdToReturn = 123456789;
		String vdContextNameExpected = "vdContextNameTest";
		List<ValueMeaningUiModel> valueMeaningUiModelToReturn = new ArrayList<>();
		List<CsCsiDeModel> modelList = new ArrayList<>();
		//mock
		when(csCsiDeDAO.getCsCsisByAcId(paramdeIdseq)).thenReturn(modelList);
		//mock
		when(valueMeaningDAO.getUiValueMeanings(Mockito.eq(publicIdToReturn), Mockito.eq(deVersionToReturn))).thenReturn(valueMeaningUiModelToReturn);
		
		DataElementModel dataElementModelToReturn = new DataElementModel();
		dataElementModelToReturn.setVdIdseq(vdIdseqTest);
		dataElementModelToReturn.setDeIdseq(paramdeIdseq);
		dataElementModelToReturn.setPublicId(publicIdToReturn);
		dataElementModelToReturn.setCdeId(publicIdToReturn);
		dataElementModelToReturn.setVersion(deVersionToReturn);
		dataElementModelToReturn.setDateCreated(null);
		dataElementModelToReturn.setDateModified(null);
		ValueDomainModel valueDomainModelToReturn = new ValueDomainModel();
		valueDomainModelToReturn.setVersion(3.3f);
		valueDomainModelToReturn.setCdVersion(4.8f);
		valueDomainModelToReturn.setVdContextName(vdContextNameExpected);
		valueDomainModelToReturn.setDateCreated(null);
		valueDomainModelToReturn.setDateModified(null);
		assertNotNull(valueDomainModelToReturn);

		//mock how DE DAO is implemented we do not call the method below when we mock dataElementDAO
		//when(valueDomainDAO.getValueDomainByIdseq(Mockito.eq(vdIdseqTest))).thenReturn(valueDomainModelToReturn);
		
		DataElementConceptModel decToReturn = new DataElementConceptModel();
		decToReturn.setVersion(1.0f);
		decToReturn.setCdVersion(2.2f);
		decToReturn.setDateCreated(null);
		decToReturn.setDateModified(null);
		dataElementModelToReturn.setDec(decToReturn);
		dataElementModelToReturn.setValueDomainModel(valueDomainModelToReturn);
		List<ReferenceDocModel> refDocsToReturn = new ArrayList<>();
		dataElementModelToReturn.setRefDocs(refDocsToReturn);
		HashMap<String, CsCsiModel> hashMapToReturn = new HashMap<>();
		CsCsiModel unclassCsCsiModelToReturn = new CsCsiModel();
		hashMapToReturn.put(CsCsiModel.UNCLASSIFIED, unclassCsCsiModelToReturn);
		dataElementModelToReturn.setCsCsiData(hashMapToReturn);
		dataElementModelToReturn.setClassifications(new ArrayList());
		dataElementModelToReturn.setCsRefDocModels(new ArrayList());
		dataElementModelToReturn.setCsiRefDocModels(new ArrayList());
		HashMap<String, List<String>> csCsiDesignationsToReturn = new HashMap<>();
		dataElementModelToReturn.setCsCsiDesignations(csCsiDesignationsToReturn);
		HashMap<String, List<String>> csCsiDefinitionsToReturn = new HashMap<>();
		dataElementModelToReturn.setCsCsiDefinitions(csCsiDefinitionsToReturn);
		List<DEOtherVersionsModel> deOtherVersionsModelsToReturn = new ArrayList<>();
		dataElementModelToReturn.setDeOtherVersionsModels(deOtherVersionsModelsToReturn);
		//mock
		when(dataElementDAO.getCdeByDeIdseq(paramdeIdseq)).thenReturn(dataElementModelToReturn);
		//MUT
		CdeDetails cdedetailsReceived = cdeDataController.retrieveDataElementDetails(paramdeIdseq);
		//check
		assertNotNull(cdedetailsReceived);
	}

}
