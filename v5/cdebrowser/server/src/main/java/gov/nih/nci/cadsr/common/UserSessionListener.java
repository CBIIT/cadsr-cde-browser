package gov.nih.nci.cadsr.common;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserSessionListener implements HttpSessionListener
{
	private Logger logger = LogManager.getLogger(UserSessionListener.class.getName());	

	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent)
	{
		logger.debug("A new session was created.");
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent)
	{
		HttpSession session = sessionEvent.getSession();
		Object sessionUser = session.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME);
		session.removeAttribute(CaDSRConstants.LOGGEDIN_USER_NAME);
		if (sessionUser != null) {
			logger.debug("Session attribute " + CaDSRConstants.LOGGEDIN_USER_NAME
				+ " for username: " + sessionUser + " removed.");
		}
	}
	
}
