package gov.nih.nci.cadsr.service.restControllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes=LookupDataController.class)
public class LookupDataControllerTest
{
	@Autowired
    private WebApplicationContext wac;
 
    private MockMvc mockMvc;
    
    private static final String[] EXPECTED_REG_STATUS = {"Standard", "Candidate", "Proposed", "Qualified", "Superceded", "Standardized Elsewhere", "Retired", "Application", "Suspended", ""};
 
    private static final String[] EXPECTED_WRK_FLW_STATUS = {"RELEASED", "APPRVD FOR TRIAL USE", "DRAFT NEW", "CMTE APPROVED", "CMTE SUBMTD", "CMTE SUBMTD USED", "DRAFT MOD", "RETIRED ARCHIVED", "RETIRED PHASED OUT", "RETIRED WITHDRAWN", "RETIRED DELETED", "RELEASED-NON-CMPLNT"};    
    
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();		
    }
	
	@Test
	public void getRegistrationStatus() throws Exception
	{
		testRESTResponseOfList("/lookupdata/registrationstatus", EXPECTED_REG_STATUS);
	}
	
	@Test
	public void getWorkflowStatus() throws Exception
	{
		testRESTResponseOfList("/lookupdata/workflowstatus", EXPECTED_WRK_FLW_STATUS);
	}
	
	private void testRESTResponseOfList(String urlTemplate, String[] expectedResult) throws Exception
	{
		MvcResult result = mockMvc.perform(get(urlTemplate))
				//.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		Object responseObj = result.getModelAndView().getModel().get("stringList");
		Assert.notNull(responseObj);
		Assert.isTrue(responseObj.getClass() == java.util.ArrayList.class);

		List<String> resultList = (List<String>) responseObj;

		Assert.isTrue(expectedResult.length == resultList.size());

		for (int i = 0; i < expectedResult.length; i++)
			Assert.isTrue(expectedResult[i].equals(resultList.get(i)));
	}

}
