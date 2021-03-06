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
<%@page import="javax.servlet.http.* " %>
<%@page import="javax.servlet.* " %>
<%//@page import="gov.nih.nci.ncicb.cadsr.cdebrowser.* " %>
<%@page import="gov.nih.nci.ncicb.cadsr.common.util.* " %>
<%@page import="oracle.clex.process.jsp.GetInfoBean " %>
<%@page import="oracle.clex.process.PageConstants " %>
<%@page import="gov.nih.nci.ncicb.cadsr.common.resource.* " %>
<%@page import="gov.nih.nci.ncicb.cadsr.common.html.* " %>
<%@page import="gov.nih.nci.ncicb.cadsr.common.ProcessConstants " %>
<%@page import="java.util.Iterator" %>
<jsp:useBean id="infoBean" class="oracle.clex.process.jsp.GetInfoBean"/>
<jsp:setProperty name="infoBean" property="session" value="<%=session %>"/>
<%@page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@include  file="/jsp/cdebrowser/cdebrowserCommon_html/SessionAuth.html"%>

<%
  DataElement de = (DataElement)infoBean.getInfo("de");
  DerivedDataElement dde = (DerivedDataElement)infoBean.getInfo("derivedDe");
  TabInfoBean tib = (TabInfoBean)infoBean.getInfo("tib");
  String pageId = StringEscapeUtils.escapeHtml(infoBean.getPageId());
  String pageName = StringEscapeUtils.escapeHtml(PageConstants.PAGEID);
  String pageUrl = "&"+StringEscapeUtils.escapeHtml(pageName)+"="+StringEscapeUtils.escapeHtml(pageId);
  CDEBrowserParams params = CDEBrowserParams.getInstance();

%>

<HTML>
<HEAD>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=WINDOWS-1252">
<LINK REL=STYLESHEET TYPE="text/css" HREF="<%=request.getContextPath()%>/css/blaf.css">
<TITLE>
Data Element Concept
</TITLE>
</HEAD>
<BODY topmargin="0">



<SCRIPT LANGUAGE="JavaScript">
<!--
function redirect1(detailReqType, linkParms )
{
  document.location.href="search?dataElementDetails=" + linkParms;
  
}
function goPage(pageInfo) {
  document.location.href = "<%=StringEscapeUtils.escapeJavaScript("search?searchDataElements=&")%>"+pageInfo;
  
}
  
//-->
</SCRIPT>
<%@ include  file="cdebrowserCommon_html/tab_include.html" %>
<form method="POST" ENCTYPE="application/x-www-form-urlencoded" action="<%= infoBean.getStringInfo("controller") %>">
<input type="HIDDEN" name="<%= PageConstants.PAGEID %>" value="<%= StringEscapeUtils.escapeJavaScript(infoBean.getPageId())%>"/>
<table cellpadding="0" cellspacing="0" width="80%" align="center">
  <tr>
    <td class="OraHeaderSubSub" width="100%">Selected Data Element</td>
  </tr>
  <tr>
    <td width="100%"><img height=1 src="i/beigedot.gif" alt="beige dot" width="99%" align=top border=0> </td>
  </tr>
</table>

<table width="80%" align="center" cellpadding="1" cellspacing="1" class="OraBGAccentVeryDark">

 <tr class="OraTabledata">
    <td class="TableRowPromptText">Public ID:</td>
    <td class="OraFieldText"><%=de.getCDEId()%></td>
 </tr>

 <tr class="OraTabledata">
    <td class="TableRowPromptText">Version:</td>
    <td class="OraFieldText"><%=de.getVersion()%> </td>
 </tr>
 
 <tr class="OraTabledata">
    <td class="TableRowPromptText" width="20%">Long Name:</td>
    <td class="OraFieldText"><%=de.getLongName()%></td>
 </tr>
 
 <tr class="OraTabledata">
    <td class="TableRowPromptText" width="20%">Short Name:</td>
    <td class="OraFieldText"><%=de.getPreferredName()%></td>
 </tr>
 


 <tr class="OraTabledata">
    <td class="TableRowPromptText">Preferred Question Text:</td>
    <td class="OraFieldText"><%=de.getLongCDEName()%></td>
 </tr>
 
 <tr class="OraTabledata">
    <td class="TableRowPromptText">Definition:</td>
    <td class="OraFieldText"><%=de.getPreferredDefinition()%> </td>
 </tr>
 <tr class="OraTabledata">
    <td class="TableRowPromptText">Workflow Status:</td>
    <td class="OraFieldText"><%=de.getAslName()%> </td>
 </tr>


 
</table>
<br>

<table cellpadding="0" cellspacing="0" width="80%" align="center">
  <tr>
    <td class="OraHeaderSubSub" width="100%">Data Element Derivation Details</td>
  </tr>
  <tr>
    <td width="100%"><img height=1 src="i/beigedot.gif" alt="beige dot" width="99%" align=top border=0> </td>
  </tr>
</table>

<table width="80%" align="center" cellpadding="4" cellspacing="1" class="OraBGAccentVeryDark">
<%
 if (dde != null) {
 %>
  <tr class="OraTabledata">
    <td class="TableRowPromptText">Derivation Type</td>
    <td class="OraFieldText"><%=dde.getType().getName()%></td>
 </tr>
 
 <tr class="OraTabledata">
    <td class="TableRowPromptText" width="20%">Rule:</td>
    <td class="OraFieldText"><%=dde.getRule()%></td>
 </tr>
 
 <tr class="OraTabledata">
    <td class="TableRowPromptText" width="20%">Method:</td>
    <td class="OraFieldText"><%=dde.getMethods()%></td>
 </tr>

 <tr class="OraTabledata">
    <td class="TableRowPromptText">Concatenation Character:</td>
    <td class="OraFieldText"><%=dde.getConcatenationCharacter()%> </td>
 </tr>
 <% } else {%>
       <tr class="OraTabledata">
         <td colspan="4">Selected CDE is not a derived data element.</td>
       </tr>
<%
  }
%>
 
</table>
<br>

<%
  DataElementDerivation deDerivation;
  DataElement derivedDe;
  
  if (( dde !=null) && (dde.getDataElementDerivation().size()> 0 )) {
    Iterator iter = dde.getDataElementDerivation().iterator(); %>
    
   <table cellpadding="0" cellspacing="0" width="80%" align="center">
  <tr>
    <td class="OraHeaderSubSub" width="100%">Component Data Elements</td>
  </tr>
  <tr>
    <td><img height=1 src="i/beigedot.gif" alt="beige dot" width="99%" align=top border=0> </td>
  </tr>
</table>

<table width="80%" align="center" cellpadding="1" cellspacing="1" bgcolor="#999966">
  <tr class="OraTableColumnHeader">
    <th>Display Order</th>
    <th>Long Name</th>
    <th>Context</th>
    <th>Workflow Status</th>
    <th>Public ID</th>
    <th>Version</th>
  </tr> 
  <%
  while (iter.hasNext()) {
      deDerivation = (DataElementDerivation)iter.next();
      derivedDe = deDerivation.getDerivedDataElement();
      %>
      <tr class="OraTabledata">
        <td class="OraFieldText"><%=deDerivation.getDisplayOrder()%> </td>
        <td class="OraFieldText"><a href="search?dataElementDetails=9&p_de_idseq=<%=derivedDe.getDeIdseq()%>&PageId=DataElementsGroup&queryDE=yes" target="_blank"><%=derivedDe.getLongName()%></a> </td>
        <td class="OraFieldText"><%=derivedDe.getContextName()%> </td>
        <td class="OraFieldText"><%=derivedDe.getAslName()%> </td>
        <td class="OraFieldText"><%=derivedDe.getCDEId()%> </td>
        <td class="OraFieldText"><%=derivedDe.getVersion()%> </td>
      </tr>
<%    } %>
</table>
<br>
<%
    }
%>
</form>

<%@ include file="/jsp/common/common_bottom_border.jsp"%>

</BODY>
</HTML>

