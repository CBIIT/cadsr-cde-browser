package gov.nih.nci.cadsr.dao;

import java.sql.SQLException;
import java.util.List;

public interface UserManagerDAO
{
    public boolean validUser(String s, String s1);

    //TODO: will be implemented if there is a need 
    //public abstract NCIUser getNCIUser(String s);

    public List<String> getContextsForAllRoles(String s, String s1);
    
    public int getCadsrLockoutProperties();
    
    public void getConnection(String username, String password) throws SQLException;
    
    public int incLock(String username);
    
    public int insertLock(String username);
    
    public int resetLock(String username);
    
}
