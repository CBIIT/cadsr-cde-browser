<%--L
  Copyright SAIC-F Inc.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-cde-browser/LICENSE.txt for details.

  Portions of this source file not modified since 2008 are covered by:

  Copyright 2000-2008 Oracle, Inc.

  Distributed under the caBIG Software License.  For details see
  http://ncip.github.com/cadsr-cde-browser/LICENSE-caBIG.txt
L--%>

<%@ page import="gov.nih.nci.ncicb.cadsr.common.util.CDEBrowserParams"%>
<%
	String width = pageContext.getRequest().getParameter("width");
%>

<TABLE width="<%=width%>%" cellspacing=0 cellpadding=0 border=0>
<TR>
<TD valign=bottom width=99%><html:img page="/i/bottom_shade.gif" alt="bottom shade" height="6" width="100%" /></TD>
<TD valign=bottom width="1%" align=right><html:img page="/i/bottomblueright.gif"  alt="bottom blue right" /></TD>
</TR>
</TABLE>
<TABLE width="<%=width%>%" cellspacing=0 cellpadding=0 bgcolor="#336699" border=0>
<TR>
<TD width="20%" align="LEFT">
 &nbsp;
<FONT face="Arial" color="WHITE" size="-2">User: </FONT>
<FONT face="Arial" size="-1" color="#CCCC99">
  <logic:present name="nciUser">
    <bean:write name="nciUser" property="username"  scope="session"/>
  </logic:present>
  <logic:notPresent name="nciUser">
    Public User    
  </logic:notPresent>
</FONT>
</td>

<td width="30%" align="right">
 <FONT color="white" size=-2 face=arial>Version @cdebrowser.version@&nbsp;&nbsp;Build @cdebrowser.build@
 <%=CDEBrowserParams.mode%></FONT>
</TD>

<td td width="70%" align="right">
  <FONT color="white" size=-3 face=arial>
     Please send comments and suggestions to 
         <A href="mailto:ncicbiit@mail.nih.gov">ncicbiit@mail.nih.gov</A>
      
  </FONT>
   &nbsp; &nbsp;
</td>

</TR>
<TR>
<TD colspan=3><html:img page="/i/bottom_middle.gif" alt="bottom middle" height="6" width="100%" /></TD>
</TR>
</TABLE>