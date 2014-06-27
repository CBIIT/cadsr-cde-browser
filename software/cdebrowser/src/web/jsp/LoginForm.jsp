<%--L
  Copyright SAIC-F Inc.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-cde-browser/LICENSE.txt for details.

  Portions of this source file not modified since 2008 are covered by:

  Copyright 2000-2008 Oracle, Inc.

  Distributed under the caBIG Software License.  For details see
  http://ncip.github.com/cadsr-cde-browser/LICENSE-caBIG.txt
L--%>

<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="gov.nih.nci.ncicb.cadsr.common.util.*"%>

<%
  CDEBrowserParams params = CDEBrowserParams.getInstance();
%>
<html>
<head>
<title>Login</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<LINK REL=STYLESHEET TYPE="text/css" HREF="<%=request.getContextPath()%>/css/blaf.css">
<SCRIPT LANGUAGE="JavaScript">
<!--

if (parent.frames[1]) 
  parent.location.href = self.location.href; 

function submitForm() {
  document.forms[0].submit();
}

function clearForm()
{
  document.forms[0].reset();
}
-->

</SCRIPT>
</head>

<body text="#000000" topmargin="0">

		<jsp:include page="/jsp/common/common_cdebrowser_header_jsp_inc.jsp"
			flush="true">
			<jsp:param name="loginDestination"
				value="<%=StringEscapeUtils.escapeSql("formCDECartAction.do?method=displayCDECart") %>" />
			<jsp:param name="urlPrefix" value="" />
		</jsp:include>		
<br>
<br>
  
  <TABLE width=100% Cellpadding=0 Cellspacing=0 border=0>
  <TR>
  <td align=left valign=top width="1%" bgcolor="#336699"><img src="<%=request.getContextPath()%>/i/top_left.gif" alt="top left"width=4 height="25"></td>
  <td nowrap align=left valign=top width="5%" bgcolor="#336699"><b><font size="3" face="Arial" color="#FFFFFF">&nbsp; &nbsp;Please Login</font></b></td>

  <td align=left valign=top width="5%" bgcolor="#336699">&nbsp;</td>
  
  <TD align=left valign=center bgcolor="#336699" height=25 width="94%">&nbsp;</TD>
  </tr>
  </table>
  
  <table  width=100% Cellpadding=0 Cellspacing=0 border=0>
  <tr>
  <td align=right valign=top width=49 height=21 bgcolor="#336699"><img src="<%=request.getContextPath()%>/i/left_end_bottom.gif" alt="left end bottom" height=21 width=49></td>
  <TD align=right valign=top bgcolor="#FFFFFF" height=21 width="100%"><img src="<%=request.getContextPath()%>/i/bottom_middle.gif" alt="bottom middle" height=6 width=100%></TD>
  <td align="LEFT" valign=top height=21 width=5  bgcolor="#FFFFFF"><img src="<%=request.getContextPath()%>/i/right_end_bottom.gif" alt="right end bottom" height=7 width=5></td>
  </TR>
  </table>
  
  <table>
    <tr>    
      <td align="left" class="OraTipText">
        Guest users can login using username "guest" and password "Nci_gue5t".
   <br> If you require an account with curator privileges to a specific context other than Test, please contact NCICB Application Support Email: <a href='mailto:ncicbiit@mail.nih.gov'>ncicbiit@mail.nih.gov</a>
      </td>
    </tr>  
  </table> 

  <form method="post" action="<%= StringEscapeUtils.escapeHtml(response.encodeURL("j_security_check")) %>">

  <table align=center cellspacing="2" cellpadding="3" border="0" onkeypress="if(event.keyCode==13){submitForm()};">
    <%	if(request.getParameter("failed") != null && (("y").equalsIgnoreCase((String)request.getParameter("failed")))) {%>
    <tr>
      <td colspan=2 class="OraErrorText">
      <b>Could not validate the User Name and Password, please try again. You could try up to 6 attempts before being locked out, or visit Password Change Station at <a href="<%=params.getPcsUrl()%>" target="_blank" >Password Change Station&nbsp;</a> to reset your password.</b>
      </td>
    </tr>
    <% } %>    
    <tr>
        <td class="OraFieldtitlebold" nowrap>Username:</td>
        <td class="OraFieldText" nowrap>
        	<label for="loginUserName"/>
          <input type="text" id ="loginUserName" name="j_username" value="<%=StringEscapeUtils.escapeHtml("")%>" size ="20" /> 
        </td>
    </tr>
    <tr>
        <td class="OraFieldtitlebold" nowrap>Password:</td>
        <td class="OraFieldText" nowrap>
        <label for="loginPassword"/>
          <input type="password" id="loginPassword" name="j_password" value="<%=StringEscapeUtils.escapeHtml("")%>" size ="20" autocomplete="off" /> 
        </td>
    </tr>
  </table>
  <table width=100% cellspacing="2" cellpadding="3" border="0">
     <TR>
      	<td width=45%>&nbsp;</td>
      	<td width=55%>
      	  <table cellspacing="2" cellpadding="3" border="0">
		     <TR>
		        <td colspan="1" align="left" nowrap><a href="javascript:submitForm()"><img src=<%=request.getContextPath()%>/i/logon.gif border=0 alt="Logon"></a></td>
		        <td colspan="1" align="left" nowrap><a href="javascript:clearForm()"><img src=<%=request.getContextPath()%>/i/clear.gif  border=0 alt="Clear"></a></td>
		     </TR>  
		  </table>    
      	</td>
    </TR>  
    <tr>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
  </table>    
  </form>
  <SCRIPT>
    document.forms[0].elements[0].focus();
  </SCRIPT>
<%@ include file="/jsp/common/common_bottom_border.jsp"%>
</body>
</html>