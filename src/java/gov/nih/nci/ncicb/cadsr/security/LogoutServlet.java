package gov.nih.nci.ncicb.cadsr.security;
import gov.nih.nci.ncicb.cadsr.CaDSRConstants;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import gov.nih.nci.ncicb.cadsr.util.SessionUtils;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.servlet.http.HttpSession;
import gov.nih.nci.ncicb.cadsr.formbuilder.common.FormBuilderConstants;

public class LogoutServlet extends HttpServlet 
{
  
  private static String LOGOUT_JSP="logout.jsp";
  private String[] logoutKeys = {CaDSRConstants.USER_KEY,CaDSRConstants.USER_CONTEXTS};
  public LogoutServlet()  
  {
  }

  protected void doGet(HttpServletRequest p0, HttpServletResponse p1) throws ServletException, IOException
  {
    // TODO:  Override this javax.servlet.http.HttpServlet method
    doPost(p0, p1);    
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    synchronized(SessionUtils.sessionObjectCache)
    {
      HttpSession session = request.getSession();
      String forwardUrl = "/"+LOGOUT_JSP;

      if(session!=null&&isLoggedIn(request))
      {      
          for(int i=0;i<logoutKeys.length;i++)
          {
            session.removeAttribute(logoutKeys[i]);
          }      
        //remove formbuilder specific objects
        //TODO has to be moved to an action
        Collection keys = (Collection)session.getAttribute(FormBuilderConstants.CLEAR_SESSION_KEYS);
        if(keys!=null)
        {
          Iterator it  = keys.iterator();
          while(it.hasNext())
          {
            session.removeAttribute((String)it.next());
          }
        }          
          HashMap allMap = new HashMap();
          allMap.put(CaDSRConstants.GLOBAL_SESSION_KEYS,copyAllsessionKeys(session));
          allMap.put(CaDSRConstants.GLOBAL_SESSION_MAP,copyAllsessionObjects(session));
          SessionUtils.addToSessionCache(session.getId(),allMap);        
          forwardUrl=forwardUrl+"?"+CaDSRConstants.PREVIOUS_SESSION_ID+"="+session.getId();
          session.invalidate();

      }
            
      RequestDispatcher dispacher = request.getRequestDispatcher(forwardUrl);
      dispacher.forward(request,response);           
    }
  }
 private Map copyAllsessionObjects(HttpSession session)
 {
    HashMap map = new HashMap();
    Enumeration  keys = session.getAttributeNames();
    for (; keys.hasMoreElements() ;) {
         String key = (String)keys.nextElement();
         map.put(key,session.getAttribute(key));
     }
     return map;
 }
 
  private Set copyAllsessionKeys(HttpSession session)
 {
    HashSet set = new HashSet();
    Enumeration  keys = session.getAttributeNames();
    for (; keys.hasMoreElements() ;) {
         String key = (String)keys.nextElement();
         set.add(key);
     }
     return set;
 }
 
 private boolean  isLoggedIn(HttpServletRequest request)
 {
   String user = request.getRemoteUser();
   if(user==null)
    return false;
   if("".equals(user))
    return false;
  return true;
 }
}