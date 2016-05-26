package gov.nih.nci.cadsr.service.restControllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

import gov.nih.nci.cadsr.dao.ClassificationSchemeDAO;
import gov.nih.nci.cadsr.dao.ProtocolDAO;
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

	private static final String[] EXPECTED_REG_STATUS = { "Standard", "Candidate", "Proposed", "Qualified",
			"Superceded", "Standardized Elsewhere", "Retired", "Application", "Suspended", "" };

	private static final String[] EXPECTED_WRK_FLW_STATUS = { "RELEASED", "APPRVD FOR TRIAL USE", "DRAFT NEW",
			"CMTE APPROVED", "CMTE SUBMTD", "CMTE SUBMTD USED", "DRAFT MOD", "RETIRED ARCHIVED", "RETIRED PHASED OUT",
			"RETIRED WITHDRAWN", "RELEASED-NON-CMPLNT" };
	
	List<ClassificationScheme> csList = new ArrayList<ClassificationScheme>();
	List<Protocol> protoList = new ArrayList<Protocol>();

	@Before
	public void setup()
	{	
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		
		ClassificationScheme cs1 = new ClassificationSchemeBuilder()
									.programAreaPalName("Cancer Centers")
									.contextIdSeq("D8D849BC-68CF-10AA-E040-BB89AD430348")
									.csIdSeq("D8EC8CDC-6298-6CD0-E040-BB89AD432C99")
									.csLongName("NCI Standard Template CDEs").build();
		
		ClassificationScheme cs2 = new ClassificationSchemeBuilder()
									.programAreaPalName("NCI")
									.contextIdSeq("99BA9DC8-2095-4E69-E034-080020C9C0E0")
									.csIdSeq("2AB29302-4615-24BE-E044-0003BA3F9857")
									.csLongName("Submission and Reporting").build();
		
		ClassificationScheme cs3 = new ClassificationSchemeBuilder()
									.programAreaPalName("UNASSIGNED")
									.contextIdSeq("29A8FB18-0AB1-11D6-A42F-0010A4C1E842")
									.csIdSeq("27CC431E-E589-0162-E044-0003BA3F9857")
									.csLongName("My Test Container").build();
		
		ClassificationScheme cs4 = new ClassificationSchemeBuilder()
									.programAreaPalName("NIH Institutes")
									.contextIdSeq("EDA90DE9-80D9-1E28-E034-0003BA3F9857")
									.csIdSeq("1B8A6942-75CA-2403-E044-0003BA3F9857")
									.csLongName("Potential CDEs for Reuse").build();
		
		csList.add(cs1);
		csList.add(cs2);
		csList.add(cs3);
		csList.add(cs4);
		
		Protocol prot1 = new ProtocolBuilder()
							.programAreaPalName("NIH Institutes")
							.contextIdSeq("EDA90DE9-80D9-1E28-E034-0003BA3F9857")
							.protocolIdSeq("2E50EEA2-C01F-17CF-E044-0003BA3F9857")
							.protocolLongName("Hepatitis Serology Pre-HSCT Disease Insert (Form 2047)").build();
		
		Protocol prot2 = new ProtocolBuilder()
							.programAreaPalName("NIH Institutes")
							.contextIdSeq("EDA90DE9-80D9-1E28-E034-0003BA3F9857")
							.protocolIdSeq("2E51ECCA-ABAE-29FB-E044-0003BA3F9857")
							.protocolLongName("Human Immunodeficiency Virus Post-HSCT Disease Insert (Form 2148)").build();
		
		Protocol prot3 = new ProtocolBuilder()
							.programAreaPalName("NIH Institutes")
							.contextIdSeq("F5E94686-5C79-2E5A-E034-0003BA3F9857")
							.protocolIdSeq("48CD8F6F-538E-54FC-E044-0003BA3F9857")
							.protocolLongName("PRL0707").build();
		
		protoList.add(prot1);
		protoList.add(prot2);
		protoList.add(prot3);
		
		Mockito.when(classificationSchemeDAO.getAllClassificationSchemeWithProgramAreaAndContext()).thenReturn(csList);
		
		Mockito.when(protocolDAO.getAllProtocolsWithProgramAreaAndContext()).thenReturn(protoList);
	}

	@Test
	public void getRegistrationStatus() throws Exception {
		testRESTResponseOfList("/lookupdata/registrationstatus", EXPECTED_REG_STATUS);
	}

	@Test
	public void getWorkflowStatus() throws Exception {
		testRESTResponseOfList("/lookupdata/workflowstatus", EXPECTED_WRK_FLW_STATUS);
	}
	
	@Test
	public void getClassificationScheme() throws Exception {
		MvcResult result = mockMvc.perform(get("/lookupdata/classificationscheme"))
				// .andDo(print())
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
		MvcResult result = mockMvc.perform(get("/lookupdata/protocol"))
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