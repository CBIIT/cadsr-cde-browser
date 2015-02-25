package gov.nih.nci.cadsr.service.search;/*L
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
import gov.nih.nci.cadsr.common.util.CDEBrowserParams;
import gov.nih.nci.cadsr.common.util.DBUtil;
import gov.nih.nci.cadsr.common.util.GenericPopListBean;
import gov.nih.nci.cadsr.common.util.StringUtils;
/*

import org.apache.commons.lang.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.*;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

*/

public class DataElementSearchBean extends Object {
  private String simpleSearchStr = "";
  private String[] strArray = null;

  //Hold only the non exluded Items
  private StringBuffer workflowList = null;
  private StringBuffer regStatusList = null;

  private StringBuffer altNameList = null;

  //Preferences
  //Holds the complete list of lov
  private StringBuffer regStatusFullList = null;
  private StringBuffer workflowFullList = null;

  private String vdPrefName = "";
  private String csiName = "";
  private String decPrefName = "";
  private String contextUse = "";
  private StringBuffer usageList = new StringBuffer("");
  private String searchText;
  private String[] aslName;
  private String[] regStatus;
  private String[] altNames;

  //Prefrences
  private String[] aslNameExcludeList=null;
  private String[] regStatusExcludeList=null;
  private boolean excludeTestContext=false;
  private boolean excludeTrainingContext=false;

  private String nameSearchMode;
  private String pvSearchMode;
  private String vdIdseq;
  private String decIdseq;
  private String cdeId;
  private String csCsiIdseq;
  private String latVersionInd;
  private StringBuffer searchInList;
  private String validValue;
  private String altName;
  private String basicSearchType="";
  private String conceptName = "";
  private String conceptCode = "";
  private String objectClass;
  private String property;
  
  private String excludeContextList;
  private String vdType;
  private String cdeType;

  
  public DataElementSearchBean() {
	super();
  }
/*

public DataElementSearchBean( HttpServletRequest request ) throws SQLException {
    strArray = request.getParameterValues("SEARCH");
    vdPrefName = StringEscapeUtils.escapeHtml( request.getParameter( "txtValueDomain" ) );
    decPrefName = StringEscapeUtils.escapeHtml(request.getParameter("txtDataElementConcept"));
    csiName = StringEscapeUtils.escapeHtml(request.getParameter("txtClassSchemeItem"));
    searchText = StringEscapeUtils.escapeHtml(request.getParameter("jspKeyword"));
    simpleSearchStr = StringEscapeUtils.escapeHtml(request.getParameter("jspSimpleKeyword"));
    aslName = request.getParameterValues("jspStatus");
    regStatus = request.getParameterValues("regStatus");
    altNames = request.getParameterValues("altName");
    vdIdseq = StringEscapeUtils.escapeHtml(request.getParameter("jspValueDomain"));
    decIdseq = StringEscapeUtils.escapeHtml(request.getParameter("jspDataElementConcept"));
    csCsiIdseq = StringEscapeUtils.escapeHtml(request.getParameter("jspClassification"));
    cdeId = StringEscapeUtils.escapeHtml(request.getParameter("jspCdeId"));
    latVersionInd = StringEscapeUtils.escapeHtml(request.getParameter("jspLatestVersion"));
    contextUse = StringEscapeUtils.escapeHtml(request.getParameter("contextUse"));
    validValue = StringEscapeUtils.escapeHtml(request.getParameter("jspValidValue"));
    altName = StringEscapeUtils.escapeHtml(request.getParameter("jspAltName"));
    basicSearchType = StringEscapeUtils.escapeHtml(request.getParameter("jspBasicSearchType"));
    conceptName = StringEscapeUtils.escapeHtml(request.getParameter("jspConceptName"));
    conceptCode = StringEscapeUtils.escapeHtml(request.getParameter("jspConceptCode"));
    objectClass = StringEscapeUtils.escapeHtml(request.getParameter("jspObjectClass"));
    property = StringEscapeUtils.escapeHtml(request.getParameter("jspProperty"));
    nameSearchMode = StringEscapeUtils.escapeHtml(request.getParameter("jspNameSearchMode"));
    pvSearchMode = StringEscapeUtils.escapeHtml(request.getParameter("jspPVSearchMode"));
    vdType = StringEscapeUtils.escapeHtml(request.getParameter("jspVDType"));
    cdeType = StringEscapeUtils.escapeHtml(request.getParameter("jspCDEType"));

    if (contextUse == null) {
      contextUse = "";
    }


    //Prefrences
    //buildContextNameFullList(contextsExcludeList, dbUtil);


    */
/*
     Moved to diffrent method to support search pref

    buildWorkflowFullList(aslNameExcludeList, dbUtil);
    buildRegStatusFullList(regStatusExcludeList,dbUtil);

    buildWorkflowList(aslName, dbUtil);
    buildRegStatusList(regStatus,dbUtil);
    buildAlternateNameList(altNames, dbUtil);
    buildContextUseList(contextUse);
    *//*

    
    searchInList = new StringBuffer("");

    String[] searchIn = request.getParameterValues("jspSearchIn");
    this.buildSearchInList(searchIn);
  }
*/

  public void setLOVLists( DBUtil dbUtil) throws Exception
  {
    buildWorkflowFullList(aslNameExcludeList, dbUtil);
    buildRegStatusFullList(regStatusExcludeList,dbUtil);

    buildWorkflowList(aslName, dbUtil);
    buildRegStatusList(regStatus,dbUtil);
    buildAlternateNameList(altNames, dbUtil);
    buildContextUseList(contextUse);
  }
  public void initSearchPreferences() throws Exception
  {
      DBUtil dbUtil = null;
      try{
        //gov.nih.nci.cadsr.common.util.CDEBrowserParams params = gov.nih.nci.cadsr.common.util.CDEBrowserParams.getInstance("cdebrowser");
        dbUtil = new DBUtil();
        dbUtil.getConnectionFromContainer();
        initSearchPreferences(dbUtil);
      }
      catch (Exception ex) {
        ex.printStackTrace();
        throw ex;
      }
     finally {
      if (dbUtil != null) {
        dbUtil.returnConnection();
      }
     }
  }
  public void initSearchPreferences( DBUtil dbUtil) throws Exception
   {
          CDEBrowserParams params = CDEBrowserParams.getInstance();
      // Initialize Search Preference Values
        boolean excludeTestContext = new Boolean(params.getExcludeTestContext()).booleanValue();
        boolean excludeTrainingContext = new Boolean(params.getExcludeTrainingContext()).booleanValue();
        setExcludeTestContext(excludeTestContext);
        setExcludeTrainingContext(excludeTrainingContext);

        String regVals = params.getExcludeRegistrationStatuses();
        if(regVals!=null&&regVals!="")
        {
          String [] regStatusExcludeList = tokenizeCSVList(regVals);
          setRegStatusExcludeList(regStatusExcludeList);
        }

        String wfVals = params.getExcludeWorkFlowStatuses();
        if(wfVals!=null&&wfVals!="")
        {
          String []  aslNameExcludeList = tokenizeCSVList(wfVals);
          setAslNameExcludeList(aslNameExcludeList);
        }
        setLOVLists(dbUtil);
   }
    public String[] tokenizeCSVList(String values) {
        String[] retVal = null;

        try {
            java.util.StringTokenizer st = new java.util.StringTokenizer(values, ",");
            int numberOfTokens = st.countTokens();
            retVal = new String[numberOfTokens];

            for (int i = 0; i < numberOfTokens; i++) {
                retVal[i] = st.nextToken();
            }
        }
        catch (Exception e) {
            return new String[0];
        }

        return retVal;
    }


    public String getSearchStr(int arrayIndex) {
    if (strArray != null) {
      return strArray[arrayIndex];
    }
    else {
      return "";
    }
  }

  public StringBuffer getWorkflowFullList() {
    return workflowFullList;
  }

  public void buildWorkflowFullList(
    String[] selectedIndex,
    DBUtil dbUtil) {
    String where = " ACTL_NAME = 'DATAELEMENT' AND ASL_NAME != 'RETIRED DELETED' ";
    workflowFullList =
      GenericPopListBean.buildList(
              "sbrext.ASL_ACTL_VIEW_EXT", "ASL_NAME", "ASL_NAME", selectedIndex,
              "jspStatus", dbUtil, where, true, 8, false, true, false, false,
              "LongLOVField" );
  }

  public StringBuffer getRegStatusFullList() {
    return regStatusFullList;
  }

  public void buildRegStatusFullList(
    String[] selectedIndex,
    DBUtil dbUtil) {
    String where = null;
    regStatusFullList =
      GenericPopListBean.buildList(
              "sbr.REG_STATUS_LOV_VIEW", "REGISTRATION_STATUS", "REGISTRATION_STATUS", selectedIndex,
              "regStatus", dbUtil, where, true, 8, false, true, false, false,
              "LongLOVField" );
  }

/**
  public StringBuffer getContextNameFullList() {
    return contextNameFullList;
  }
  public void buildContextNameFullList(
    String[] selectedIndex,
    gov.nih.nci.cadsr.common.util.DBUtil dbUtil) {
    String where = null;
    contextNameFullList =
      gov.nih.nci.cadsr.common.util.GenericPopListBean.buildList(
        "sbr.CONTEXTS", "NAME", "NAME", selectedIndex,
        "contextNames", dbUtil, where, true, 4, true, true, false, false,
        "LongLOVField");
  }
  **/

 public StringBuffer getWorkflowList() {
    return workflowList;
  }

  public void buildWorkflowList(
    String[] selectedIndex,
    DBUtil dbUtil) {
    String where = " ACTL_NAME = 'DATAELEMENT' AND ASL_NAME != 'RETIRED DELETED' ";
    if(!StringUtils.isArrayWithEmptyStrings( aslNameExcludeList ))
     {
       String exludeWhere = getExcludeWhereCluase("ASL_NAME",aslNameExcludeList);
       where = where + " and "+ exludeWhere;
     }
    workflowList =
      GenericPopListBean.buildList(
              "sbrext.ASL_ACTL_VIEW_EXT", "ASL_NAME", "ASL_NAME", selectedIndex,
              "jspStatus", dbUtil, where, false, 4, true, true, false, true,
              "LongLOVField" );
  }

  public StringBuffer getRegStatusList() {
    return regStatusList;
  }

  public void buildRegStatusList(
    String[] selectedIndex,
    DBUtil dbUtil) {
    String where = null;
    if(!StringUtils.isArrayWithEmptyStrings( aslNameExcludeList ))
     {
      where = getExcludeWhereCluase("REGISTRATION_STATUS",regStatusExcludeList);
     }
    regStatusList =
      GenericPopListBean.buildList(
              "sbr.REG_STATUS_LOV_VIEW", "REGISTRATION_STATUS", "REGISTRATION_STATUS", selectedIndex,
              "regStatus", dbUtil, where, false, 4, true, true, false, true,
              "LongLOVField" );
  }


  public StringBuffer getAltNameList() {
    return altNameList;
  }


  public void buildAlternateNameList(
    String[] selectedIndex,
    DBUtil dbUtil) {
    String where = null;
    altNameList =
      GenericPopListBean.buildList(
              "sbr.DESIGNATION_TYPES_LOV_VIEW", "DETL_NAME", "DETL_NAME", selectedIndex,
              "altName", dbUtil, where, true, 4, false, true, false, true,
              "LongLOVField" );
  }

  public String getVDPrefName() {
    return StringUtils.replaceNull( vdPrefName );
  }

  public String getDECPrefName() {
    return StringUtils.replaceNull( decPrefName );
  }

  public String getCSIName() {
    return StringUtils.replaceNull( csiName );
  }

  public String getSearchText() {
    return StringUtils.replaceNull( searchText );
  }

  public String getAslName() {
    return StringUtils.replaceNull( aslName );
  }

  public String getRegStatus() {
    return StringUtils.replaceNull( regStatus );
  }

  public String getVdIdseq() {
    return StringUtils.replaceNull( vdIdseq );
  }

  public String getDecIdseq() {
    return StringUtils.replaceNull( decIdseq );
  }

  public String getCdeId() {
    return StringUtils.replaceNull( cdeId );
  }

  public String getLatVersionInd() {
    return StringUtils.replaceNull( latVersionInd );
  }

  public String getCsCsiIdseq() {
    return StringUtils.replaceNull( csCsiIdseq );
  }

  private void buildContextUseList(String usage) {
    //if ("".equals(contextUse) || "owned_by".equals(contextUse)) {
    usageList = new StringBuffer("");
    if ("owned_by".equals(contextUse)) {
      usageList.append(
        "<select name=\"contextUse\" size=\"1\" class=\"LOVField\"> ");
      usageList.append(
        "<option selected value=\"owned_by\">Owned By</option> ");
      usageList.append("<option value=\"used_by\">Used By</option> ");
      usageList.append("<option value=\"both\">Owned By/Used By</option> ");
      usageList.append("</select> ");
    }
    else if ("used_by".equals(contextUse)) {
      usageList.append(
        "<select name=\"contextUse\" size=\"1\" class=\"LOVField\"> ");
      usageList.append("<option value=\"owned_by\">Owned By</option> ");
      usageList.append("<option selected value=\"used_by\">Used By</option> ");
      usageList.append("<option value=\"both\">Owned By/Used By</option> ");
      usageList.append("</select> ");
    }
    else if ("".equals(contextUse) || "both".equals(contextUse)) {
      usageList.append(
        "<select name=\"contextUse\" size=\"1\" class=\"LOVField\"> ");
      usageList.append("<option value=\"owned_by\">Owned By</option> ");
      usageList.append("<option value=\"used_by\">Used By</option> ");
      usageList.append(
        "<option selected value=\"both\">Owned By/Used By</option> ");
      usageList.append("</select> ");
    }
  }

  public StringBuffer getContextUseList() {
    return usageList;
  }

  public void buildSearchInList(String[] searchIn) {
    searchInList.append(
      "<select multiple name=\"jspSearchIn\" size=\"4\" class=\"LongLOVField\"> ");

    if (searchIn == null) {
      searchInList.append("<option selected value=\"ALL\">ALL</option> ");
      searchInList.append("<option value=\"Long Name\">Long Name</option> ");
      searchInList.append(
        "<option value=\"Short Name\">Short Name</option> ");
      searchInList.append("<option value=\"Doc Text\">Preferred Question Text</option> ");
      searchInList.append(
        "<option value=\"Hist\">Alternate Question Text</option> ");
       searchInList.append(
         "<option value=\"UML ALT Name\">UML Class:UML Attr Alternate Name</option> ");
        
       
    }
    else {
      if ( StringUtils.containsKey( searchIn, "ALL" )) {
        searchInList.append("<option selected value=\"ALL\">ALL</option> ");
      }
      else {
        searchInList.append("<option value=\"ALL\">ALL</option> ");
      }

      if ( StringUtils.containsKey( searchIn, "Long Name" )) {
        searchInList.append(
          "<option selected value=\"Long Name\">Long Name</option> ");
      }
      else {
        searchInList.append("<option value=\"Long Name\">Long Name</option> ");
      }

      if ( StringUtils.containsKey( searchIn, "Short Name" )) {
        searchInList.append(
          "<option selected value=\"Short Name\">Short Name</option> ");
      }
      else {
        searchInList.append(
          "<option value=\"Short Name\">Short Name</option> ");
      }

      if ( StringUtils.containsKey( searchIn, "Doc Text" )) {
        searchInList.append(
          "<option selected value=\"Doc Text\">Preferred Question Text</option> ");
      }
      else {
        searchInList.append("<option value=\"Doc Text\">Preferred Question Text</option> ");
      }

      if ( StringUtils.containsKey( searchIn, "Hist" )) {
        searchInList.append(
          "<option selected value=\"Hist\">Alternate Question Text</option> ");
      }
      else {
        searchInList.append(
          "<option value=\"Hist\">Alternate Question Text</option> ");
      }
      
       if ( StringUtils.containsKey( searchIn, "UML ALT Name" )) {
         searchInList.append(
           "<option selected value=\"UML ALT Name\">UML Class:UML Attr Alternate Name</option> ");
       }
       else {
         searchInList.append(
           "<option value=\"UML ALT Name\">UML Class:UML Attr Alternate Name</option> ");
       }

    }

    searchInList.append("</select> ");
  }

  public StringBuffer getSearchInList() {
    return searchInList;
  }

  public String getValidValue() {
    return StringUtils.replaceNull( validValue );
  }

  public String getObjectClass() {
    return StringUtils.replaceNull( objectClass );
  }

  public String getProperty() {
    return StringUtils.replaceNull( property );
  }

  public String getAltName() {
    return StringUtils.replaceNull( altName );
  }

  public String getBasicSearchType()
  {
    return StringUtils.replaceNull( basicSearchType );
  }

  public void setBasicSearchType(String jspBasicSearchType)
  {
      this.basicSearchType = jspBasicSearchType;
  }

  public String getSimpleSearchStr()
  {
    return StringUtils.replaceNull( simpleSearchStr );
  }

  public void setSimpleSearchStr(String simpleSearchStr)
  {
    this.simpleSearchStr = simpleSearchStr;
  }


  public String getConceptName() {
    return StringUtils.replaceNull( conceptName );
  }


  public String getConceptCode() {
    return StringUtils.replaceNull( conceptCode );
  }

/**
  public String[] getContextsExcludeList()
  {
    return contextsExcludeList;
  }
   public String getContextsExcludeListAsStr()
  {
    if(contextsExcludeList==null)
      return null;
    if(contextsExcludeList.length <1)
      return null;
    String str = null;
    for(int i=0;i<contextsExcludeList.length;i++)
    {
      if(str==null)
        {
          str = "'"+contextsExcludeList[i]+"'";
        }
      else
      {
        str = str +","+"'"+contextsExcludeList[i]+"'";
      }
    }
    return str;
  }

  public void setContextsExcludeList(String[] contextsExcludeList)
  {
    this.contextsExcludeList = contextsExcludeList;
  }

**/
  public String[] getRegStatusExcludeList()
  {
    return regStatusExcludeList;
  }

  public void setRegStatusExcludeList(String[] regStatusExcludeList)
  {
    this.regStatusExcludeList = regStatusExcludeList;
  }
  public String[] getAslNameExcludeList()
  {
      /*

      //FIXME - set for dev time in request
            //String[] excludeArr = {"CMTE APPROVED", "CMTE SUBMTD", "CMTE SUBMTD USED", "RETIRED ARCHIVED", "RETIRED PHASED OUT", "RETIRED WITHDRAWN"};
       */
    return aslNameExcludeList;
  }

  public void setAslNameExcludeList(String[] aslNameExcludeList)
  {
    this.aslNameExcludeList = aslNameExcludeList;
  }

  public void resetSearchCriteria() throws Exception {
      this.setSimpleSearchStr("");
      searchText = "";
      conceptName = "";
      conceptCode = "";
      cdeId = "";
      decIdseq ="";
      vdIdseq ="";
      validValue = "";
      csCsiIdseq = "";
      altName = "";
      objectClass = "";
      property = "";

  }
  public void resetLOVList() throws Exception
  {
      DBUtil dbUtil = null;
      try {
        CDEBrowserParams params = CDEBrowserParams.getInstance();
        dbUtil = new DBUtil();
        dbUtil.getConnectionFromContainer();
        buildWorkflowFullList(aslNameExcludeList, dbUtil);
        buildRegStatusFullList(regStatusExcludeList,dbUtil);
        buildWorkflowList(aslName, dbUtil);
        buildRegStatusList(regStatus,dbUtil);
      }
      catch (Exception ex) {
        ex.printStackTrace();
        throw ex;
      }
     finally {
      if (dbUtil != null) {
        dbUtil.returnConnection();
      }
     }

  }

  public String getExcludeWhereCluase(String colName, String[] excludeArr)
  {
    String whereClauseStr = null;
    if(excludeArr==null)
      return whereClauseStr;
    if(excludeArr.length <1)
      return whereClauseStr;


    for(int i=0; i<excludeArr.length;i++)
    {
      if(whereClauseStr== null)
      {
        whereClauseStr = colName+" NOT IN ('" +excludeArr[i] +"'";
      }
      else
      {
        whereClauseStr = whereClauseStr + " , '" +excludeArr[i]+"'";
      }
    }
    whereClauseStr = whereClauseStr +" ) " ;
    return whereClauseStr;
  }

  public boolean isExcludeTestContext()
  {
    return excludeTestContext;
  }

  public void setExcludeTestContext(boolean excludeTestContext)
  {
    this.excludeTestContext = excludeTestContext;
  }

  public boolean isExcludeTrainingContext()
  {
    return excludeTrainingContext;
  }

  public void setExcludeTrainingContext(boolean excludeTrainingContext)
  {
    this.excludeTrainingContext = excludeTrainingContext;
  }

    public void setNameSearchMode(String nameSearchMode) {
        this.nameSearchMode = nameSearchMode;
    }

    public String getNameSearchMode() {
        if (nameSearchMode== null)
            nameSearchMode = ProcessConstants.DE_SEARCH_MODE_EXACT;
        return nameSearchMode;
    }

    public void setPvSearchMode(String pvSearchMode) {
        this.pvSearchMode = pvSearchMode;
    }

    public String getPvSearchMode() {
        if (pvSearchMode== null)
            pvSearchMode = ProcessConstants.DE_SEARCH_MODE_EXACT;
        return pvSearchMode;
    }
    
    public String getVDType() {
        if (vdType== null)
            vdType = ProcessConstants.VD_TYPE_BOTH;
        return vdType;
    }
    
    public String getCDEType() {
        if (cdeType== null)
            cdeType = "";
        return cdeType;
    }
    
    public String getExcludeContextList()
    {
      CDEBrowserParams params = CDEBrowserParams.getInstance();  	  
      String excludeList = "";
	  if (this.isExcludeTestContext())
		  excludeList = " '" + params.getContextTest() + "'";  // " '" + CaDSRConstants.CONTEXT_TEST + "'";
	  if (this.isExcludeTrainingContext())
	  {
          if (!excludeList.equals("")) excludeList += ",";
          excludeList += " '" + params.getContextTraining() + "'";   //" '" + CaDSRConstants.CONTEXT_TRAINING + "' ";
	  }
	  return excludeList;
    }
    
/*
    public void getContextsList(Collection<Context> contexts)
    {
       String excludeList = this.getExcludeContextList();
  	   if (!excludeList.equals(""))
	   {
		  Collection<Context> exContexts = new ArrayList<Context>();
	      for (Context context: contexts) {
	          // if this context is not excluded by user preference
	          if (excludeList.contains(context.getName()))
	        	  exContexts.add(context);
	      }
	      if (!exContexts.isEmpty())
	    	  contexts.removeAll(exContexts);
	   }	  
    }
    
*/
    public void initDefaultContextPreferences()
    {
		// Initialize Search Preference Values
		final boolean excludeTestContext = new Boolean("true").booleanValue();
		final boolean excludeTrainingContext = new Boolean("true").booleanValue();
		this.setExcludeTestContext(excludeTestContext);
		this.setExcludeTrainingContext(excludeTrainingContext);
    }
    
}

