<%--L
  Copyright Oracle Inc, SAIC-F Inc.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-cde-browser/LICENSE.txt for details.
L--%>

<%@ page import="gov.nih.nci.ncicb.cadsr.common.util.CDEBrowserParams"%>
<TABLE width=100% cellspacing=0 cellpadding=0 border=0>
<TR>
<TD valign=bottom width=99%><html:img page="/i/bottom_shade.gif" alt="bottom shade" height="6" width="100%" /></TD>
<TD valign=bottom width="1%" align=right><html:img page="/i/bottomblueright.gif" alt="bottom blue right" /></TD>
</TR>
</TABLE>
<TABLE width=100% cellspacing=0 cellpadding=0 bgcolor="#336699" border=0>
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
<td td width="15%" align="right">
  <FONT color="white" size=-2 face=arial>
  	<A href="<%=params.getPrivacyURL()%>" target="_blank">Privacy Notice</A>      
  </FONT>
   &nbsp; &nbsp;
</td>
<td width="30%" align="right">
 <FONT color="white" size=-2 face=arial>Version 4.0.3&nbsp;&nbsp;Build 1
 <%=CDEBrowserParams.mode%></FONT>
</td>
<td td width="70%" align="right">
  <FONT color="white" size=-3 face=arial>
     Please send comments and suggestions to 
         <A href="mailto:ncicb@pop.nci.nih.gov">ncicb@pop.nci.nih.gov</A>
      
  </FONT>
   &nbsp; &nbsp;
</td>
</TR>
<TR>
<TD colspan=3><html:img page="/i/bottom_middle.gif" alt="bottom middle" height="6" width="100%" /></TD>
</TR>
</TABLE>