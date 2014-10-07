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

/**
 * CompareCDEAction
 *
 * This class is the Action class for comparing CDEs side by side
  *
 * @release 3.0
 * @author: <a href=mailto:jane.jiang@oracle.com>Shaji Kakkodi</a>
 * @date: 2/16/2005
 */

package gov.nih.nci.ncicb.cadsr.cdebrowser.struts.actions;

import gov.nih.nci.ncicb.cadsr.cdebrowser.CDECompareList;
import gov.nih.nci.ncicb.cadsr.common.cdebrowser.DataElementSearchBean;
import gov.nih.nci.ncicb.cadsr.common.ProcessConstants;

import gov.nih.nci.ncicb.cadsr.common.util.CDEBrowserParams;
import gov.nih.nci.ncicb.cadsr.common.util.AppScanValidator;




import gov.nih.nci.ncicb.cadsr.common.util.StringUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import oracle.cle.process.ProcessInfo;
import oracle.cle.process.Service;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;


public class CDESearchPrefAction
 extends BrowserBaseDispatchAction {



 /**
 *
 *
 * @param mapping The ActionMapping used to select this instance.
 * @param form The optional ActionForm bean for this request.
 * @param request The HTTP Request we are processing.
 * @param response The HTTP Response we are processing.
 *
 * @return
 *
 * @throws IOException
 * @throws ServletException
 */
 public ActionForward gotoCDESearchPref(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                          HttpServletResponse response) throws IOException, ServletException, Exception {
    DynaActionForm prefForm = (DynaActionForm)form;
    
    DataElementSearchBean searchBean  = (DataElementSearchBean)getInfoObject(request,"desb");
    
    prefForm.set("excludeTestContext",new Boolean(searchBean.isExcludeTestContext()).toString());
    prefForm.set("excludeTrainingContext",new Boolean(searchBean.isExcludeTrainingContext()).toString());
    prefForm.set("isPreferencesDefault",new Boolean(isPreferencesDefault(searchBean)).toString());

   return mapping.findForward("prefPage");
 }

 /**
  *
  *
  * @param mapping The ActionMapping used to select this instance.
  * @param form The optional ActionForm bean for this request.
  * @param request The HTTP Request we are processing.
  * @param response The HTTP Response we are processing.
  *
  * @return
  *
  * @throws IOException
  * @throws ServletException
  */
 public ActionForward saveCDESearchPref(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                               HttpServletResponse response) throws IOException, ServletException, Exception {
        DynaActionForm prefForm = (DynaActionForm)form;
        
        String[] aslNames = request.getParameterValues("jspStatus");
		//addition validation rule maybe required
        for (int i=0; i< aslNames.length; i++)
	        {
				if (aslNames[i]!=null)
					{
						if (!AppScanValidator.validateSearchParameterType(aslNames[i]))
							throw new Exception ("Invalidate jspStatus");
			
					}
	        }
        
        String[] regStatuses = request.getParameterValues("regStatus");
		//addition validation rule maybe required
        for (int i=0; i< regStatuses.length; i++)
	        {
				if (regStatuses[i]!=null)
					{
						if (!AppScanValidator.validateSearchParameterType(regStatuses[i]))
							throw new Exception ("Invalidate regStatus");
			
					}
	        }
        
        String excludeTestContext = (String)prefForm.get("excludeTestContext");
		//addition validation rule maybe required
		if (excludeTestContext!=null)
			{
				if (!AppScanValidator.validateSearchParameterType(excludeTestContext))
					throw new Exception ("Invalidate excludeTestContext");
	
			}
        
        String excludeTrainingContext = (String)prefForm.get("excludeTrainingContext");
		//addition validation rule maybe required
		if (excludeTrainingContext!=null)
			{
				if (!AppScanValidator.validateSearchParameterType(excludeTrainingContext))
					throw new Exception ("Invalidate excludeTrainingContext");
	
			}
        
        String[] emptyArr = {""};
        DataElementSearchBean searchBean  = (DataElementSearchBean)getInfoObject(request,"desb");
        if(aslNames==null)aslNames = emptyArr;
        if(regStatuses==null)regStatuses = emptyArr;
        
        
        searchBean.setAslNameExcludeList(aslNames);    
        searchBean.setRegStatusExcludeList(regStatuses);   
        
        
        if(excludeTestContext.equals("true"))
        {
            searchBean.setExcludeTestContext(true);
            prefForm.set("excludeTestContext","true");
        }
        else
        {
            searchBean.setExcludeTestContext(false);
            prefForm.set("excludeTestContext","false");
        }
        
        if(excludeTrainingContext.equals("true"))
        {
            searchBean.setExcludeTrainingContext(true);
            prefForm.set("excludeTrainingContext","true");
        }
        else
        {
            searchBean.setExcludeTrainingContext(false);
            prefForm.set("excludeTrainingContext","false");
        }
        
        prefForm.set("isPreferencesDefault",new Boolean(isPreferencesDefault(searchBean)).toString());
        
        setSessionObject(request,TREE_REFRESH_INDICATOR,YES,true);
        
        try{
            searchBean.resetLOVList();
        }
        catch(Exception exp)
        {
        //Add message later
        	saveMessage("cadsr.cdebrowser.cde.search.pref.save.error", request);
            return mapping.findForward("prefPage");
        }
        
        saveMessage("cadsr.cdebrowser.cde.search.pref.save.success", request);
        return mapping.findForward("prefPage");
 }

 /**
*
*
* @param mapping The ActionMapping used to select this instance.
* @param form The optional ActionForm bean for this request.
* @param request The HTTP Request we are processing.
* @param response The HTTP Response we are processing.
*
* @return
*
* @throws IOException
* @throws ServletException
*/
 public ActionForward cancelCDESearchPref(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                     HttpServletResponse response) throws IOException, ServletException {
                                     
  setSessionObject(request,TREE_REFRESH_INDICATOR,YES,true);
  return mapping.findForward("searchPage");
 }


 /**
*
*
* @param mapping The ActionMapping used to select this instance.
* @param form The optional ActionForm bean for this request.
* @param request The HTTP Request we are processing.
* @param response The HTTP Response we are processing.
*
* @return
*
* @throws IOException
* @throws ServletException
*/
 public ActionForward setDefaultCDESearchPref(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                     HttpServletResponse response) throws IOException, ServletException {
                                     
  DynaActionForm prefForm = (DynaActionForm)form;
  DataElementSearchBean searchBean  = (DataElementSearchBean)getInfoObject(request,"desb");
  
  try{
      searchBean.initSearchPreferences();
  }
  catch(Exception exp)
  {
	  saveMessage("cadsr.cdebrowser.cde.search.pref.default.error", request);
      return mapping.findForward("prefPage");
  }
  prefForm.set("excludeTestContext",new Boolean(searchBean.isExcludeTestContext()).toString());
  prefForm.set("excludeTrainingContext",new Boolean(searchBean.isExcludeTestContext()).toString());
  prefForm.set("isPreferencesDefault",new Boolean(isPreferencesDefault(searchBean)).toString());  
  saveMessage("cadsr.cdebrowser.cde.search.pref.default.success", request);  
  setSessionObject(request,TREE_REFRESH_INDICATOR,YES,true);
  return mapping.findForward("prefPage");
 }



    private boolean isEmptyString(String[] arr)
    {
      
      if(arr.length==1)
      {
        if(arr[0].equals(""))
          return true;
        else
          return false;
      }
      return false;
    }
    
    /**
     * Check if the user have changed from the defaults
     */
    private boolean isPreferencesDefault(DataElementSearchBean searchBean)
    {
      
        CDEBrowserParams params = CDEBrowserParams.getInstance();

        boolean excludeTestContext = new Boolean(params.getExcludeTestContext()).booleanValue();
        
        if(!searchBean.isExcludeTestContext()==excludeTestContext)
        {
          return false;
        }
        boolean excludeTrainingContext = new Boolean(params.getExcludeTrainingContext()).booleanValue();
        
        if(!searchBean.isExcludeTrainingContext()==excludeTrainingContext)
        {
          return false;
        }
        
        String regVals = params.getExcludeRegistrationStatuses();
        if(regVals!=null&&regVals!="")
        {
          String [] regStatusExcludeList = StringUtils.tokenizeCSVList(regVals);
          // Clean is done to remove the "All" Value from that array
          String[] beanRegStatusExcludeList = getCleanList(searchBean.getRegStatusExcludeList());
          if(beanRegStatusExcludeList!=null)
          {
            Arrays.sort(regStatusExcludeList);
            Arrays.sort(beanRegStatusExcludeList);
            if(!Arrays.equals(regStatusExcludeList ,beanRegStatusExcludeList))
              return false;
          }
          else
          {
            return false;
          }
        }

        String wfVals = params.getExcludeWorkFlowStatuses();
        if(wfVals!=null&&wfVals!="")
        {
          String []  aslNameExcludeList = StringUtils.tokenizeCSVList(wfVals);
          String[] beanAslNameExcludeList = getCleanList(searchBean.getAslNameExcludeList());
          if(beanAslNameExcludeList!=null)
          {
            Arrays.sort(aslNameExcludeList);
            Arrays.sort(beanAslNameExcludeList);
            if(!Arrays.equals(aslNameExcludeList ,beanAslNameExcludeList))
              return false;
          }
          else
          {
            return false;
          }            
        } 
        return true;
    }
    //Remove the first element if it equals ""
    private String[] getCleanList(String[] strArr)
    {
      if(strArr==null) return null;
      String[] cleanArr = null;
      int length = 0;
      if(strArr.length>0&&strArr[0].equals(""))
      {
        length=strArr.length-1;
        
      }
      else
      {
        return strArr;
      }
      
      cleanArr = new String[length];
      
      for(int i=0; i<strArr.length;i++)
      {
        int j=0;
        if(i==0&&strArr[i].equals(""))
        { }
        else
        {
          cleanArr[j] = strArr[i];
          j++;
        }
      }
      
      return cleanArr;
    }
}
