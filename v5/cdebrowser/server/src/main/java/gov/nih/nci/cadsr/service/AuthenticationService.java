package gov.nih.nci.cadsr.service;

public interface AuthenticationService 
{
	public boolean validateUserCredentials(String loginUsername, String credential) throws Exception;
}
