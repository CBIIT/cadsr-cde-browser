<%--L
  Copyright Oracle Inc, SAIC-F Inc.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-cde-browser/LICENSE.txt for details.
L--%>

<%
	String urlPrefix = request.getContextPath();
	String label = pageContext.getRequest().getParameter("label");
	String width = pageContext.getRequest().getParameter("width");
%>

<TABLE width="<%=width%>%" Cellpadding=0 Cellspacing=0 border=0>
  <tr>
    <td width=98%>&nbsp;</td>
    <td valign=bottom align=right>
      <table border=0 cellpadding=0 cellspacing=0>
        <tr>


<TD bgcolor="#336699" width="1%" align=LEFT valign=TOP><IMG SRC="<%=urlPrefix%>/i/ctab_open.gif"alt="c tab open" height=21 width=18 border=0></TD>
<TD width=1% bgcolor="#336699"><b><font size="-1" face="Arial" color="#FFFFFF"><%=label%></font></b></TD>
<TD bgcolor="#336699" width="1%" align=RIGHT valign=TOP><IMG SRC="<%=urlPrefix%>/i/ctab_close.gif" alt="c tab close" height=21 width=12 border=0></TD>


</table>
</td>
</TR>
</TABLE>

<TABLE width="<%=width%>%" Cellpadding=0 Cellspacing=0 border=0>
<TR>
<td align=left valign=top width="1%" bgcolor="#336699"><img src="<%=urlPrefix%>/i/top_left.gif" alt="top left" width=4 height="25"></td>
<td align=left valign=top width="5%" bgcolor="#336699">&nbsp;</td>


<td align=left valign=top width="5%" bgcolor="#336699">&nbsp;</td>

<!-- add here --->

<TD align=left valign=center bgcolor="#336699" height=25 width="94%">&nbsp;</TD>
</tr>
</table>

<table  width="<%=width%>%" Cellpadding=0 Cellspacing=0 border=0>
<tr>
<td align=right valign=top width=49 height=21 bgcolor="#336699"><img src="<%=urlPrefix%>/i/left_end_bottom.gif" alt="left end bottom" height=21 width=49></td>
<TD align=right valign=top bgcolor="#FFFFFF" height=21 width="100%"><img src="<%=urlPrefix%>/i/bottom_middle.gif" alt="bottom middle" height=6 width=100%></TD>
<td align="LEFT" valign=top height=21 width=5  bgcolor="#FFFFFF"><img src="<%=urlPrefix%>/i/right_end_bottom.gif" alt="right end bottom" height=7 width=5></td>
</TR>
</TABLE>

