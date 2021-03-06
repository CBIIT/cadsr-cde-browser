<%--L
  Copyright SAIC-F Inc.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-cde-browser/LICENSE.txt for details.

  Portions of this source file not modified since 2008 are covered by:

  Copyright 2000-2008 Oracle, Inc.

  Distributed under the caBIG Software License.  For details see
  http://ncip.github.com/cadsr-cde-browser/LICENSE-caBIG.txt
L--%>

<%@ taglib uri="/WEB-INF/tld/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/tld/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tld/cdebrowser.tld" prefix="cde"%>

<%@ page import="gov.nih.nci.ncicb.cadsr.common.CaDSRConstants"%>
<%@ page import="gov.nih.nci.ncicb.cadsr.common.struts.common.BrowserFormConstants"%>
<%@ page import="gov.nih.nci.ncicb.cadsr.common.struts.common.BrowserNavigationConstants"%>
<%@ page import="gov.nih.nci.ncicb.cadsr.cdebrowser.jsp.util.CDECompareJspUtils" %>
<%@page import="gov.nih.nci.ncicb.cadsr.cdebrowser.jsp.util.CDEDetailsUtils" %>
<%@page import="gov.nih.nci.ncicb.cadsr.common.util.* " %>
<%@page import="gov.nih.nci.ncicb.cadsr.common.cdebrowser.* " %>
<%@page import="oracle.clex.process.jsp.GetInfoBean " %>
<%@ page import="gov.nih.nci.ncicb.cadsr.common.html.*"%>
<jsp:useBean id="infoBean" class="oracle.clex.process.jsp.GetInfoBean"/>
<jsp:setProperty name="infoBean" property="session" value="<%=session %>"/>
<HTML>
<HEAD>
<TITLE>CDEBrowser  Search Preferences</TITLE>
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache"/>
<LINK REL=STYLESHEET TYPE="text/css" HREF="<%=request.getContextPath()%>/css/blaf.css">
<SCRIPT LANGUAGE="JavaScript1.1" SRC="<%=request.getContextPath()%>/js/newWinJS.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript1.1" SRC="<%=request.getContextPath()%>/js/checkbox.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
<!--
<%
  CDEBrowserParams params = CDEBrowserParams.getInstance();
  DataElementSearchBean desb = (DataElementSearchBean)infoBean.getInfo("desb");

%>

function save() {
  document.forms[0].<%=BrowserNavigationConstants.METHOD_PARAM%>.value="saveCDESearchPref";
  document.forms[0].submit();
}
function cancel() {
  document.forms[0].<%=BrowserNavigationConstants.METHOD_PARAM%>.value="cancelCDESearchPref";
  document.forms[0].submit();
}

function setDefaults() {
  document.forms[0].<%=BrowserNavigationConstants.METHOD_PARAM%>.value="setDefaultCDESearchPref";
  document.forms[0].submit();
}
-->

</SCRIPT>
</HEAD>
<BODY bgcolor="#ffffff" topmargin="0">
    <html:form action="/cdebrowser/cdeSearchPrefAction">
      <html:hidden value="" property="<%=BrowserNavigationConstants.METHOD_PARAM%>"/>
      <html:hidden property="src"/>
      <html:hidden property="<%=CaDSRConstants.MODULE_INDEX%>"/>
      <html:hidden property="<%=CaDSRConstants.QUESTION_INDEX%>"/>
      
      <TABLE Cellpadding=0 Cellspacing=0 border=0 >
        <TR>
          <TD valign="TOP" align="right" width="1%" colspan=1><A HREF="<%=params.getCdeBrowserHelpUrl()%>" target="_blank"><IMG SRC="<%=request.getContextPath()%>/i/icon_help.gif" alt="Task Help" border=0  width=32 height=32></A><br><font color=brown face=verdana size=1>&nbsp;Help&nbsp;</font></TD>         
        </TR>
      </TABLE>
      
      <jsp:include page="/jsp/common/tab_variable_length_inc.jsp" flush="true">
        <jsp:param name="label" value="Search&nbsp;Preferences"/>
        <jsp:param name="width" value="<%=100%>" />
      </jsp:include>
     
      <%@ include file="/jsp/cdebrowser/cdeSearchPreferences_inc.jsp"%>

 <table width="100%">
  <tr align="left">
     <td class="OraHeaderSubSub" width="50%" align="left" nowrap>CDE search preferences for this session</td>
    <logic:equal value="false" name="searchPrefForm" property="isPreferencesDefault">
      <td width="50%" align="right" nowrap>
        <a href="javascript:setDefaults()">
               Reset to default search preferences
        </a>    
      </td>
    </logic:equal>
  </tr>  
  <tr>
    <td align="center" colspan="2" ><html:img page="/i/beigedot.gif" alt="beigedot" border="0"  height="1" width="99%" align="top" /> </td>
   </tr> 
 </table> 
 
      <%@ include file="/jsp/cdebrowser/showMessages.jsp" %>     
  <table width="80%" align="center">
    <tr>
         <td valign="top" width="100%" >
          <table width="100%" cellpadding="1" cellspacing="1" class="OraBGAccentVeryDark" >      
            <tr>
              <td colspan="2" class="OraTableColumnHeaderNoBG" >
                <html:checkbox property="excludeTestContext" value="true"  >
                   <bean:message key="cadsr.cdebrowser.exclude.test.context"/>
                </html:checkbox>
              </td>
            </tr>   
            <tr>
              <td colspan="2" class="OraTableColumnHeaderNoBG" >
                <html:checkbox property="excludeTrainingContext" value="true"  >
                   <bean:message key="cadsr.cdebrowser.exclude.training.context"/>
                </html:checkbox>
              </td>
            </tr>             
          </table>
         </td>
     </tr>
  </table> 
<br>  
  <table width="80%" align="center" >
    <tr>
         <td valign="top" width="50%" >
          <table width="100%" cellpadding="1" cellspacing="1" class="OraBGAccentVeryDark" >
            <tr>
             <td width="50%" align="center" class="OraTableColumnHeaderNoBG" nowrap>Exclude Workflow Status(es) </td>
             <td width="50%" class="OraTabledata"><%=desb.getWorkflowFullList()%></td>
            </tr>          
          </table>
         </td>
     </tr>
  </table>
<br>
 <table width="80%" align="center" >
    <tr>
          <td width="100%" >
          <table width="100%" cellpadding="1" cellspacing="1" class="OraBGAccentVeryDark" >
             <tr>
              <td width="50%" align="center" class="OraTableColumnHeaderNoBG" nowrap>Exclude Registration Status(es)</td>
              <td width="50%"  class="OraTabledata"><%=desb.getRegStatusFullList()%></td>
            </tr>
          </table>
         </td>
     </tr>
  </table>  
 <table width="100%">
  <tr>
    <td align="center" ><html:img page="/i/beigedot.gif" alt="beigedot" border="0"  height="1" width="99%" align="top" /> </td>
   </tr> 
 </table>             
    <%@ include file="/jsp/cdebrowser/cdeSearchPreferences_inc.jsp"%>
       <%@ include file="/jsp/common/common_bottom_border.jsp"%>  
    </html:form>

</body>
</html>