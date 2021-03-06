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
<%@page import="gov.nih.nci.ncicb.cadsr.common.ProcessConstants " %>
<%@page import="gov.nih.nci.ncicb.cadsr.common.lov.ClassificationsLOVBean " %>
<%@page import="org.apache.commons.lang.StringEscapeUtils" %>

<jsp:useBean id="infoBean" class="oracle.clex.process.jsp.GetInfoBean"/>
<jsp:setProperty name="infoBean" property="session" value="<%=session %>"/>

<%@include  file="/jsp/cdebrowser/cdebrowserCommon_html/SessionAuth.html"%>

<%
  TabInfoBean tib = (TabInfoBean)infoBean.getInfo("tib");
  ClassificationsLOVBean cslb = (ClassificationsLOVBean)infoBean.getInfo(ProcessConstants.CS_LOV);
  CommonLOVBean clb = cslb.getCommonLOVBean();
    
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
List of Values - Classifications
</TITLE>
</HEAD>
<BODY topmargin="0">



<SCRIPT LANGUAGE="JavaScript">
//<!--
function passback(P_ID, P_NAME) {
   opener.document.forms[0].txtClassSchemeItem.value = P_NAME;
   opener.document.forms[0].jspClassification.value = P_ID;
   opener.document.forms[0].txtClassSchemeItem.focus();
   window.close();
}

function closeOnClick() {
    close();
}

function goPage(pageInfo) {  
  document.location.href = "<%=StringEscapeUtils.escapeHtml("search?classificationsLOV=9")+"&"%>"+pageInfo+"<%=pageUrl%>";
}

var reFloat = /^((\d+(\.\d*)?)|((\d*\.)?\d+))$/
function validate() {
  var csVersion = document.forms[0].SEARCH[1].value;
  if ((csVersion != '')&&(!(reFloat.test(csVersion)))) {
     alert('Enter a valid CS Version.');
     document.forms[0].SEARCH[1].focus();
     return false;
  }
  else {
   return true;
  }
}

  
//-->
</SCRIPT>
<%@ include  file="/jsp/cdebrowser/cdebrowserCommon_html/tab_include_lov.html" %>
<center>
<p class="OraHeaderSubSub">Classifications </p>
</center>
<form method="POST" onSubmit="return validate()" ENCTYPE="application/x-www-form-urlencoded" action="<%= infoBean.getStringInfo("controller") %>">
<input type="HIDDEN" name="<%= PageConstants.PAGEID %>" value="<%= StringEscapeUtils.escapeHtml(infoBean.getPageId())%>"/>
<INPUT TYPE="HIDDEN" NAME="NOT_FIRST_DISPLAY" VALUE="<%=StringEscapeUtils.escapeHtml("1")%>"/>
<INPUT TYPE="HIDDEN" NAME="idVar" VALUE="<%= StringEscapeUtils.escapeHtml("jspClassification") %>"/>
<INPUT TYPE="HIDDEN" NAME="nameVar" VALUE="<%= StringEscapeUtils.escapeHtml("txtClassSchemeItem") %>"/>
<INPUT TYPE="HIDDEN" NAME="classificationsLOV" VALUE="<%=StringEscapeUtils.escapeHtml("9") %>"/>
<p align="left">
<font face="Arial, Helvetica, sans-serif" size="-1" color="#336699">
  Please enter the search criteria. Wildcard character is *.
</font>
</p>
<center>
<table>
<%= clb.getSearchFields() %>
<tr>
  <% 
    String chkContext = StringEscapeUtils.escapeJavaScript((String)request.getAttribute("chkContext"));    
    if((chkContext == null) || (!chkContext.equals("always"))) {
  %>
  <td class="fieldtitlebold">Restrict Search to Current Context</td>
<%
  if (clb.isFirstDisplay()) {
%>
  <td class="OraFieldText"><input type="checkbox" name="chkContext" value="<%=StringEscapeUtils.escapeHtml("yes")%>" CHECKED /></td>
<%
  }
  else {
    if (cslb.getIsContextSpecific()) {
%>
  <td class="OraFieldText"><input type="checkbox" name="chkContext" value="<%=StringEscapeUtils.escapeHtml("yes")%>" CHECKED /></td>
<%
    }
    else if (!cslb.getIsContextSpecific()) {
%>
  <td class="OraFieldText"><input type="checkbox" name="chkContext" value="<%=StringEscapeUtils.escapeHtml("yes")%>" /></td>
<%
    }
  }
} else {
%>
<INPUT type="HIDDEN" NAME="chkContext" value="<%=StringEscapeUtils.escapeJavaScript("always")%>"/>
<% } %>
</tr>

<TR>
  <TD></TD>
  <TD><input type="submit" name="submit"  value="<%=StringEscapeUtils.escapeJavaScript("Find")%>"/>&nbsp;
  <INPUT type="button" value="Close" onclick="javascript:closeOnClick()"/></TD>
</TR>
</table>

<% 
  if (clb.getTotalRecordCount() != 0) {
%>
<%= clb.getHitList() %>

<p class="OraFieldText">Total Record Count:<B> <%= clb.getTotalRecordCount() %></B></p>
<P>
<%= clb.getPageInfo() %>
<%
  }
  else {
    if (!clb.isFirstDisplay()) {
%>
  <table width="100%" align="center" cellpadding="1" cellspacing="1" border="0" class="OraBGAccentVeryDark">
  <tr class="OraTableColumnHeader">
    <th class="OraTableColumnHeader">Class Scheme Item</th>
    <th class="OraTableColumnHeader">Short Name</th>
    <th class="OraTableColumnHeader">Long Name</th>
    <th class="OraTableColumnHeader">Context</th>
    <th class="OraTableColumnHeader">Version</th>
    <th class="OraTableColumnHeader">Workflow Status</th>
    <th class="OraTableColumnHeader">Preferred Definition</th>
    
  </tr>
  <tr class="OraTabledata">
         <td colspan="7">No classification scheme items match the search criteria</td>
  </tr>
  </table>
<%
    }
  }
%>
</center>
</form>

<%@ include file="/jsp/common/common_bottom_border.jsp"%>

</BODY>
</HTML>

