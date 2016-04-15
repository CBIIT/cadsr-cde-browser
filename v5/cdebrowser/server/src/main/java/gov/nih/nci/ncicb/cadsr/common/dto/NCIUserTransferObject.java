/*L
 * Copyright SAIC-F Inc.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cde-browser/LICENSE.txt for details.
 *
 * Portions of this source file not modified since 2008 are covered by:
 *
 * Copyright 2000-2008 Oracle, Inc.
 *
 * Distributed under the caBIG Software License.  For details see
 * http://ncip.github.com/cadsr-cde-browser/LICENSE-caBIG.txt
 */

package gov.nih.nci.ncicb.cadsr.common.dto;

import gov.nih.nci.ncicb.cadsr.common.CaDSRConstants;
import gov.nih.nci.ncicb.cadsr.common.resource.Context;
import gov.nih.nci.ncicb.cadsr.common.resource.NCIUser;

import gov.nih.nci.ncicb.cadsr.common.util.DebugStringBuffer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class NCIUserTransferObject implements NCIUser,CaDSRConstants {
  private String username = null;
  private Map contexts = new HashMap();
  //added for GF1224-lock form
  private String emailAddress;
  private String phoneNumber;

  public NCIUserTransferObject(String newUsername) {
   setUsername(newUsername);
  }

  public void setUsername(String newUsername) {
      username = newUsername;
  }
  
  public String getUsername() {
    return username;
  }

  public boolean hasRoleAccess(
    String role,
    Context context) {
    if (contexts != null) {
      Collection roleContexts = (Collection)contexts.get(role);
      if (roleContexts != null) {
        if (roleContexts.contains(context)) {
          return true;
        }
      }
      roleContexts = (Collection)contexts.get(CONTEXT_ADMIN);
      if (roleContexts != null) {
        if (roleContexts.contains(context)) {
          return true;
        }
      }      
    }
    
    return false;
  }

  public Map getContextsByRole() {
    return contexts;
  }
  
   public Collection getContextsByRoleAccess(String role) {
        Collection roleContexts = (Collection) contexts.get(role);
        
        Collection adminContexts  =(Collection) contexts.get(CONTEXT_ADMIN);
        if(roleContexts==null)
          roleContexts=adminContexts;
        if(adminContexts!=null&&roleContexts!=null)
        {
          Iterator it = adminContexts.iterator();
        
        while(it.hasNext())
        {
          Context context = (Context)it.next();
          if(!roleContexts.contains(context))
            roleContexts.add(context);           
          }
        }
        return roleContexts;
  } 
  public void setContextsByRole(Map contextsMap) {
    contexts = contextsMap;
  }
  
  public String toString()
  {
    DebugStringBuffer sb = new DebugStringBuffer();
    sb.append(OBJ_SEPARATOR_START);
    sb.append(super.toString());
    sb.append(ATTR_SEPARATOR+"username="+getUsername(),getUsername());
    sb.append(ATTR_SEPARATOR+"ContextByRole="+getContextsByRole());      
    sb.append(OBJ_SEPARATOR_END);
    return sb.toString();    
  }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
