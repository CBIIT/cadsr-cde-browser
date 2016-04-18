package gov.nih.nci.cadsr.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import gov.nih.nci.cadsr.common.CaDSRConstants;

@Component("authenticationInterceptor")
public class AuthenticationInterceptor extends HandlerInterceptorAdapter
{
	private Logger logger = LogManager.getLogger(AuthenticationInterceptor.class.getName());
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		boolean userLoggedIn = true;
		
		logger.info("Authentication interceptor invoked for request to a secure page: " + request.getRequestURI());
		 
		// Avoid a redirect loop for some urls
		// but not actually necessary here since interceptor is not configured for the login path
		if  (!request.getRequestURI().contains("login"))
		{
			String username = (String) request.getSession().getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME);
			if(StringUtils.isBlank(username))
			{
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "login needed");
				userLoggedIn = false;
			}
		}
		
		return userLoggedIn;
	}

}