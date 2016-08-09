package gov.nih.nci.cadsr.service.restControllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

import gov.nih.nci.cadsr.dao.ClassificationSchemeDAO;
import gov.nih.nci.cadsr.dao.ProtocolDAO;
import gov.nih.nci.cadsr.dao.DesignationDAO;
import gov.nih.nci.cadsr.service.ClassificationSchemeService;
import gov.nih.nci.cadsr.service.ClassificationSchemeServiceImpl;
import gov.nih.nci.cadsr.service.ProtocolService;
import gov.nih.nci.cadsr.service.ProtocolServiceImpl;
import gov.nih.nci.cadsr.service.model.cdeData.Protocol;
import gov.nih.nci.cadsr.service.model.cdeData.ProtocolBuilder;
import gov.nih.nci.cadsr.service.model.cdeData.classifications.ClassificationScheme;
import gov.nih.nci.cadsr.service.model.cdeData.classifications.ClassificationSchemeBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration
public class LookupDataControllerTest
{

	@Configuration
	static class LookupDataServiceTestContextConfiguration
	{
		@Bean
		public ClassificationSchemeService classificationSchemeService() {
			return new ClassificationSchemeServiceImpl();
		}

		@Bean
		public ProtocolService protocolService() {
			return new ProtocolServiceImpl();
		}

		@Bean
		public DesignationDAO designationDAO() {
			return Mockito.mock(DesignationDAO.class);
		}

		@Bean
		public LookupDataController lookupDataController() {
			return new LookupDataController();
		}

		@Bean
		public ClassificationSchemeDAO classificationSchemeDAO() {
			return Mockito.mock(ClassificationSchemeDAO.class);
		}

		@Bean
		public ProtocolDAO protocolDAO() {
			return Mockito.mock(ProtocolDAO.class);
		}

	}

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private ClassificationSchemeService classificationSchemeService;

	@Autowired
	private ProtocolService protocolService;

	@Autowired
	private LookupDataController lookupDataController;

	@Autowired
	private ClassificationSchemeDAO classificationSchemeDAO;

	@Autowired
	private ProtocolDAO protocolDAO;

	private MockMvc mockMvc;

	private static final String[] EXPECTED_REG_STATUS = { "ALL", "Standard", "Candidate", "Proposed", "Qualified",
			"Superceded", "Standardized Elsewhere", "Retired", "Application", "Suspended", "" };

	private static final String[] EXPECTED_WRK_FLW_STATUS = {"ALL", "RELEASED", "APPRVD FOR TRIAL USE", "DRAFT NEW",
			"CMTE APPROVED", "CMTE SUBMTD", "CMTE SUBMTD USED", "DRAFT MOD", "RETIRED ARCHIVED", "RETIRED PHASED OUT",
			"RETIRED WITHDRAWN", "RELEASED-NON-CMPLNT" };

	private List<ClassificationScheme> csList = new ArrayList<ClassificationScheme>();
	private List<Protocol> protoList = new ArrayList<Protocol>();

	private String programAreaPalName = "NCI Consortium";
	private String contextIdSeq = "F6117C06-C689-F9FD-E040-BB89AD432E40";
	private String contextName = "ABTC";

	@Before
	public void setup()
	{
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

		ClassificationScheme cs1 = new ClassificationSchemeBuilder()
									.programAreaPalName(programAreaPalName)
									.contextIdSeq(contextIdSeq).contextName(contextName)
									.csIdSeq("F7BA4EB1-2D70-BA33-E040-BB89AD4355D2").csLongName("CRF CDEs")
									.csCsiIdSeq("F7BA6033-BAEA-C5EF-E040-BB89AD437201").csCsiName("Dosing")
									.csiLevel(1)
									.build();

		ClassificationScheme cs2 = new ClassificationSchemeBuilder()
									.programAreaPalName(programAreaPalName)
									.contextIdSeq(contextIdSeq).contextName(contextName)
									.csIdSeq("F7BA4EB1-2D70-BA33-E040-BB89AD4355D2").csLongName("CRF CDEs")
									.csCsiIdSeq("F7BA5589-4430-0BF5-E040-BB89AD435EB8").csCsiName("Lapatinib Dosing")
									.csiLevel(2).parentCsiIdSeq("F7BA6033-BAEA-C5EF-E040-BB89AD437201")
									.build();

		ClassificationScheme cs3 = new ClassificationSchemeBuilder()
									.programAreaPalName(programAreaPalName)
									.contextIdSeq(contextIdSeq).contextName(contextName)
									.csIdSeq("F7BA4EB1-2D70-BA33-E040-BB89AD4355D2").csLongName("CRF CDEs")
									.csCsiIdSeq("F7BA528D-C7D7-A8CF-E040-BB89AD4371BC").csCsiName("30-Day Follow-Up")
									.csiLevel(1)
									.build();

		ClassificationScheme cs4 = new ClassificationSchemeBuilder()
									.programAreaPalName(programAreaPalName)
									.contextIdSeq(contextIdSeq).contextName(contextName)
									.csIdSeq("F7BA4EB1-2D70-BA33-E040-BB89AD4355D2").csLongName("CRF CDEs")
									.csCsiIdSeq("F7BABB40-FBF3-ACC0-E040-BB89AD430920").csCsiName("Enrollment Additional Information")
									.csiLevel(1)
									.build();

		csList.add(cs1); csList.add(cs2);
		csList.add(cs3); csList.add(cs4);

		Protocol prot1 = new ProtocolBuilder()
							.programAreaPalName(programAreaPalName)
							.contextIdSeq(contextIdSeq).contextName(contextName)
							.protocolIdSeq("B40DD2C8-A047-DBE1-E040-BB89AD437202").protocolLongName("ABTC-0904")
							.formIdSeq("B2D14B67-725F-9400-E040-BB89AD4314A4").formLongName("ABTC Vital Signs").build();

		Protocol prot2 = new ProtocolBuilder()
							.programAreaPalName(programAreaPalName)
							.contextIdSeq(contextIdSeq).contextName(contextName)
							.protocolIdSeq("B40DD2C8-A047-DBE1-E040-BB89AD437202").protocolLongName("ABTC-0904")
							.formIdSeq("DAF96B53-07DB-23D6-E040-BB89AD4318A2").formLongName("ABTC TMZ Dosing").build();

		Protocol prot3 = new ProtocolBuilder()
							.programAreaPalName(programAreaPalName)
							.contextIdSeq(contextIdSeq).contextName(contextName)
							.protocolIdSeq("DAA64912-F072-6637-E040-BB89AD434736").protocolLongName("ABTC-1202")
							.formIdSeq("2D8CD1C6-F647-C967-E050-BB89AD43465C").formLongName("CT Scan Report").build();

		Protocol prot4 = new ProtocolBuilder()
							.programAreaPalName(programAreaPalName)
							.contextIdSeq(contextIdSeq).contextName(contextName)
							.protocolIdSeq("DAA64912-F072-6637-E040-BB89AD434736").protocolLongName("ABTC-1202")
							.formIdSeq("246F2368-5949-47BD-E050-BB89AD4313AA").formLongName("Fresh Tissue").build();

		protoList.add(prot1); protoList.add(prot2);
		protoList.add(prot3); protoList.add(prot4);

		Mockito.when(classificationSchemeDAO.getAllClassificationSchemeWithProgramAreaAndContext(contextIdSeq, "")).thenReturn(csList);

		Mockito.when(protocolDAO.getAllProtocolsWithProgramAreaAndContext(contextIdSeq, "")).thenReturn(protoList);
	}

/*
	@Test
	public void getRegistrationStatus() throws Exception {
		testRESTResponseOfList("/lookupdata/registrationstatus", EXPECTED_REG_STATUS);
	}

	@Test
	public void getWorkflowStatus() throws Exception {
		testRESTResponseOfList("/lookupdata/workflowstatus", EXPECTED_WRK_FLW_STATUS);
	}
*/

	@Test
	public void getClassificationScheme() throws Exception {
		MvcResult result = mockMvc.perform(get("/lookupdata/classificationscheme").param("contextIdSeq", contextIdSeq).param("csOrCsCsi", ""))
				//.andDo(print());
				.andExpect(status().isOk()).andReturn();

		Object responseObj = result.getModelAndView().getModel().get("classificationSchemeList");
		Assert.notNull(responseObj);
		Assert.isTrue(responseObj.getClass() == java.util.ArrayList.class);

		List<ClassificationScheme> resultList = (List<ClassificationScheme>) responseObj;

		Assert.isTrue(csList.size() == resultList.size());

		for (int i = 0; i < resultList.size(); i++)
		{
			Assert.isTrue(csList.get(i).getProgramAreaPalName() == resultList.get(i).getProgramAreaPalName());
			Assert.isTrue(csList.get(i).getContextIdSeq() == resultList.get(i).getContextIdSeq());
			Assert.isTrue(csList.get(i).getCsIdSeq() == resultList.get(i).getCsIdSeq());
			Assert.isTrue(csList.get(i).getCsLongName() == resultList.get(i).getCsLongName());
		}
	}

	@Test
	public void getProtocol() throws Exception
	{
		MvcResult result = mockMvc.perform(get("/lookupdata/protocol").param("contextIdSeq", contextIdSeq).param("protocolOrForm", ""))
				//.andDo(print())
				.andExpect(status().isOk()).andReturn();

		Object responseObj = result.getModelAndView().getModel().get("protocolList");
		Assert.notNull(responseObj);
		Assert.isTrue(responseObj.getClass() == java.util.ArrayList.class);

		List<Protocol> resultList = (List<Protocol>) responseObj;

		Assert.isTrue(protoList.size() == resultList.size());

		for (int i = 0; i < resultList.size(); i++)
		{
			Assert.isTrue(protoList.get(i).getProgramAreaPalName() == resultList.get(i).getProgramAreaPalName());
			Assert.isTrue(protoList.get(i).getContextIdSeq() == resultList.get(i).getContextIdSeq());
			Assert.isTrue(protoList.get(i).getProtocolIdSeq() == resultList.get(i).getProtocolIdSeq());
			Assert.isTrue(protoList.get(i).getProtocolLongName() == resultList.get(i).getProtocolLongName());
		}
	}

	private void testRESTResponseOfList(String urlTemplate, String[] expectedResult) throws Exception {
		MvcResult result = mockMvc.perform(get(urlTemplate))
				// .andDo(print())
				.andExpect(status().isOk()).andReturn();

		Object responseObj = result.getModelAndView().getModel().get("stringList");
		Assert.notNull(responseObj);
		Assert.isTrue(responseObj.getClass() == java.util.ArrayList.class);

		List<String> resultList = (List<String>) responseObj;

		Assert.isTrue(expectedResult.length == resultList.size());

		for (int i = 0; i < expectedResult.length; i++)
			Assert.isTrue(expectedResult[i].equals(resultList.get(i)));
	}

}
