package gov.nih.nci.cadsr.service.restControllers;

import java.nio.charset.Charset;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.error.AutheticationFailureException;
import gov.nih.nci.cadsr.service.AuthenticationService;

@RestController
public class CdeBrowserAuthenticationController 
{
	private Logger logger = LogManager.getLogger(CdeBrowserAuthenticationController.class.getName());
	
	@Autowired
	AuthenticationService authenticationService;
	
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public String login(@RequestHeader("Authorization") String authorization,
						 HttpServletRequest request) throws AutheticationFailureException
	{
		logger.debug("Received request to authenticate user.");
		Boolean login = Boolean.TRUE;
		
		final String[] credentials = decodeAuthorizationHeader(authorization);
		
		logger.debug("Processing login request for user: " + credentials[0]);
		
		if (credentials == null || credentials.length != 2 || StringUtils.isBlank(credentials[0]) || StringUtils.isBlank(credentials[1]))
			throw new AutheticationFailureException("Authentication failed for user because username or password is null:" + credentials[0]);
		else
		{
			try 
			{
				authenticationService.validateUserCredentials(credentials[0], credentials[1]);
				HttpSession session = request.getSession(false);
				if (session != null)
				{
					String currUser = (String) session.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME);
					// do not invalidate if the same user is trying to login again.
					if (!StringUtils.equals(currUser, credentials[0]))
					{
						logger.debug("Invalidating current session since a new user is loggin in.");
						session.invalidate();
					}
				}
				
				logger.debug("Setting user in a new session after successful login:" + credentials[0]);
				request.getSession(true).setAttribute(CaDSRConstants.LOGGEDIN_USER_NAME, credentials[0]);
			} 
			catch (Exception e)
			{
				logger.error("Error in validating user credentials, username: " + credentials[0], e);
				throw new AutheticationFailureException("Authentication failed for user because of invalid credentials:" + credentials[0]);
			}
		}
		
		return login.toString();
	}
	
	@RequestMapping(value="/logout")
	public void logout(HttpSession session)
	{
		if (session != null)
		{
			String currUser = (String) session.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME);
			logger.debug("Received request to logout user: " + currUser);
			session.invalidate();
		}	
	}
	
	@RequestMapping(value="/user", method = RequestMethod.GET, produces = "text/plain")
	public String getUser(HttpSession session)
	{
		String username = (session != null) ? (String) session.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME) : "";
		logger.debug("Request to fetch logged in username.");
		return username;
	}
	
	private String[] decodeAuthorizationHeader(String authorization)
	{
		String[] unpwd = new String[2];
		
		if (authorization != null && authorization.startsWith("Basic"))
		{
			// Authorization: Basic base64credentials
			String base64Credentials = authorization.substring("Basic".length()).trim();
			String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
			
			// credentials = username:password
			unpwd = credentials.split(":", 2);
		}
		return unpwd;
	}
	
}
