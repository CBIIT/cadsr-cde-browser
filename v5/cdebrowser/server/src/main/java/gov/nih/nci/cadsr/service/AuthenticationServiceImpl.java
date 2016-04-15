package gov.nih.nci.cadsr.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.nih.nci.cadsr.dao.UserManagerDAO;

@Service("authenticationService")
public class AuthenticationServiceImpl implements AuthenticationService
{
	private Logger logger = LogManager.getLogger(AuthenticationServiceImpl.class.getName() );
	
	@Autowired
	UserManagerDAO userManagerDAO;
	
	@Override
	public boolean validateUserCredentials(String loginUsername, String credential) throws Exception 
	{
		boolean valid = false;
    	try
    	{
    		if (userManagerDAO.getCadsrLockoutProperties() == 2)
    		{
    			try {
					userManagerDAO.getConnection(loginUsername, credential);
				} catch (Exception e) {
					logger.error("Error in validating user credentials for : " + loginUsername, e);
					
					//inc lock
					int updCnt = userManagerDAO.incLock(loginUsername);
					if (updCnt == 0)
						userManagerDAO.insertLock(loginUsername);
					
					throw e;
				}
    			if(valid)
    			{
    				userManagerDAO.resetLock(loginUsername);
    				valid = true;
    			}
    		}
    	}
    	catch(Exception ex)
    	{
    		logger.error("Error in validating user credentials: ", ex);
    		throw ex;
    	}
    	
    	return valid;
	}

}
