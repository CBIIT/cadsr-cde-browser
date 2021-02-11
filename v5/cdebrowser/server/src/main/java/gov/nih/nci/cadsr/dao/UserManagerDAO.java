package gov.nih.nci.cadsr.dao;

import java.sql.SQLException;
import java.util.List;

public interface UserManagerDAO
{
    public boolean validUser(String userName, String password);

    public List<String> getContextsForAllRoles(String username, String acType);
    
    public int getCadsrLockoutProperties();
    
    public void getConnection(String username, String password) throws SQLException;
    
    public void authenticateUser(String username, String password, String db_url) throws SQLException;
    
    public int incLock(String username);
    
    public int insertLock(String username);
    
    public int resetLock(String username);
    
    public String getOrganization(String username);
    
}
