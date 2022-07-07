package gov.nih.nci.cadsr.service.restControllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.Base64;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import gov.nih.nci.cadsr.dao.UserManagerDAO;
import gov.nih.nci.cadsr.error.ControllerErrorHandler;
import gov.nih.nci.cadsr.service.AuthenticationService;
import gov.nih.nci.cadsr.service.AuthenticationServiceImpl;

@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration
public class CdeBrowserAuthenticationControllerTest
{

	@Configuration
	@EnableWebMvc
	static class AuthenticationServiceTestContextConfiguration extends WebMvcConfigurationSupport 
	{
		@Bean
		public AuthenticationService authenticationService() {
			return new AuthenticationServiceImpl();
		}

		@Bean
		public CdeBrowserAuthenticationController cdeBrowserAuthenticationController() {
			return new CdeBrowserAuthenticationController();
		}

		@Bean
		public UserManagerDAO userManagerDAO() {
			return Mockito.mock(UserManagerDAO.class);
		}
		
		@Bean
		public ControllerErrorHandler controllerErrorHandler()
		{
			return new ControllerErrorHandler();
		}

	}

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private UserManagerDAO userManagerDAO = Mockito.mock(UserManagerDAO.class);
	
	private String username = "PURNIMAC";
	private String password = "purnimac";
	private String db_url = "";

	@Before
	public void setup() throws SQLException
	{	
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		
		Mockito.when(userManagerDAO.validUser(username, password)).thenReturn(true);
		Mockito.doReturn(1).when(userManagerDAO).insertLock(username);
		Mockito.doReturn(1).when(userManagerDAO).incLock(username);
		Mockito.doReturn(1).when(userManagerDAO).resetLock(username);
		Mockito.doReturn(2).when(userManagerDAO).getCadsrLockoutProperties();
	}
	
	@Test
	public void testLogin() throws Exception
	{		
		Mockito.doNothing().when(userManagerDAO).getConnection(username, password);
		
		mockMvc.perform(post("/login")
				.header("Authorization", "Basic " + createEncodedText(username, password)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("true"));
	}
	
	@Test
	public void testInvalidLoginWithNullPassword() throws Exception
	{		
		password = "";

		Mockito.doNothing().when(userManagerDAO).getConnection(username, password);
		
		mockMvc.perform(post("/login")
				.header("Authorization", "Basic " + createEncodedText(username, password)))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}
	
	@Test
	public void testInvalidLogin() throws Exception
	{		
		password = "abcd";
		username = "";
		Mockito.doThrow(new SQLException("Invalid username/password")).when(userManagerDAO).authenticateUser(username, password, db_url);		
		mockMvc.perform(post("/login")
				.header("Authorization", "Basic " + createEncodedText(username, password)))
				.andExpect(MockMvcResultMatchers.status().isUnauthorized());
	}
	
	private static String createEncodedText(String username, String password) {
		final String pair = username + ":" + password;
		final byte[] encodedBytes = Base64.getEncoder().encode(pair.getBytes());
		return new String(encodedBytes, Charset.forName("UTF-8"));
	}

}
