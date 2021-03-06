<%--L
  Copyright SAIC-F Inc.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-cde-browser/LICENSE.txt for details.

  Portions of this source file not modified since 2008 are covered by:

  Copyright 2000-2008 Oracle, Inc.

  Distributed under the caBIG Software License.  For details see
  http://ncip.github.com/cadsr-cde-browser/LICENSE-caBIG.txt
L--%>

<%@ taglib uri="/WEB-INF/tld/cdebrowser.tld" prefix="cde"%>
<%@ taglib uri="/WEB-INF/tld/struts-html.tld" prefix="html"%>
<%@ page import="gov.nih.nci.ncicb.cadsr.common.util.*"%>
<%
	CDEBrowserParams params = CDEBrowserParams.getInstance();
%>
<SCRIPT LANGUAGE="JavaScript1.1" SRC='<html:rewrite page="/js/newWinJS.js"/>'></SCRIPT>
<SCRIPT LANGUAGE="JavaScript1.1" SRC='<html:rewrite page="/js/helpWinJS.js"/>'></SCRIPT>

<TABLE width=100% Cellpadding=0 Cellspacing=0 border=0>
  <tr>

    <td align="left" nowrap>

    <html:img page="/i/graphic6.gif" alt="graphic 6" border="0" />
    </td>
    <td align=right valign=top colspan=2 nowrap>
      <TABLE Cellpadding=0 Cellspacing=0 border=0 >
        <TR>
          <TD valign="TOP" align="CENTER" width="1%" colspan=1><A HREF="javascript:window.close()" TARGET="_top"><html:img page="/i/icon_return.gif" alt="Back to Search Results" border="0"  width="32" height="32" /></A><br><font color=brown face=verdana size=1>&nbsp;Back&nbsp;</font></TD>
          <TD valign="TOP" align="RIGHT" width="1%" colspan=1><A HREF="<%=params.getCdeBrowserHelpUrl()%>" target="_blank">
          <html:img page="/i/icon_help.gif" alt="Task Help" border="0"  width="32" height="32" /></A><br><font color=brown face=verdana size=1>&nbsp;Help&nbsp;</font></TD>
        </TR>
      </TABLE>
    </td>
  </tr>
  <tr>
    <td align="left" class="OraInlineInfoText" nowrap>
       <logic:present name="nciUser">
        <bean:message key="user.greet" />
    	<bean:write name="nciUser" property="username"  scope="session"/>
       </logic:present>
    </td>    
  </tr>
</TABLE>