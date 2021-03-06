<%--L
  Copyright SAIC-F Inc.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-cde-browser/LICENSE.txt for details.

  Portions of this source file not modified since 2008 are covered by:

  Copyright 2000-2008 Oracle, Inc.

  Distributed under the caBIG Software License.  For details see
  http://ncip.github.com/cadsr-cde-browser/LICENSE-caBIG.txt
L--%>

<SCRIPT LANGUAGE="JavaScript1.1" SRC='<html:rewrite page="/js/helpWinJS.js"/>'></SCRIPT>
<SCRIPT LANGUAGE="JavaScript1.1" SRC='<html:rewrite page="/js/newWinJS.js"/>'></SCRIPT>
<%@ include  file="/jsp/common/topHeader.jsp" %>
<%@ page import="gov.nih.nci.ncicb.cadsr.common.CaDSRConstants"%>
<%
	String preSessionId = (String)request.getParameter(CaDSRConstants.PREVIOUS_SESSION_ID);
    String forwardPage = request.getContextPath()+"/jsp/cdeBrowse.jsp";
  	if(preSessionId!=null)
	  forwardPage=forwardPage+"?PageId=DataElementsGroup&"+CaDSRConstants.PREVIOUS_SESSION_ID+"="+preSessionId;
  	else
	  forwardPage=forwardPage+"?PageId=DataElementsGroup";  
%>
<TABLE width=100% Cellpadding=0 Cellspacing=0 border=0>
  <tr>
    <td align="left" nowrap>

    <img src="<%=request.getContextPath()%>/i/cde_browser_banner_full.gif" alt="cde browser banner" border=0>
    </td>
    <td align=right valign=top nowrap>
      <TABLE Cellpadding=0 Cellspacing=0 border=0>
        <TR>
          <TD valign="TOP" align="center" width="1%" colspan=1><A HREF="<%=forwardPage%>" TARGET="_top"><IMG SRC="<%=request.getContextPath()%>/i/icon_cdebrowser.gif" alt="CDE Browser" border=0  width=32 height=32></A></TD>
          <TD valign="TOP" align="left" width="1%" colspan=1><A HREF="<%=params.getCdeBrowserHelpUrl()%>" target="_blank"><IMG SRC="<%=request.getContextPath()%>/i/icon_help.gif" alt="Task Help" border=0  width=32 height=32></A></TD>
        </TR>
        <TR>
          <TD valign="TOP" align="center" colspan=1><font color=brown face=verdana size=1>&nbsp;CDE Browser&nbsp;</font></TD>
          <TD valign="TOP" align="left" colspan=1><font color=brown face=verdana size=1>&nbsp;Help&nbsp;</font></TD>
        </TR>
      </TABLE>
    </td>
  </tr>
</TABLE>