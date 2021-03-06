<%--L
  Copyright SAIC-F Inc.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-cde-browser/LICENSE.txt for details.

  Portions of this source file not modified since 2008 are covered by:

  Copyright 2000-2008 Oracle, Inc.

  Distributed under the caBIG Software License.  For details see
  http://ncip.github.com/cadsr-cde-browser/LICENSE-caBIG.txt
L--%>

<table align="center" width=15% Cellpadding=0 Cellspacing=4 border=0>
    <tr>
        <TD valign="TOP" align="CENTER"  colspan=1>
          <a href="javascript:done()">
             <img src="<%=request.getContextPath()%>/i/backButton.gif" border=0 alt="Done">
          </a>            
       <td align="left">
          <a href="javascript:removeFromCompareList()">
             <img src="<%=request.getContextPath()%>/i/remove_from_cde_comparelist.gif" border=0 alt="Remove CDEs from the compare list">
          </a>              
        </td> 
       <td align="left">
          <a href="javascript:changeDisplayOrder()">
             <img src="<%=request.getContextPath()%>/i/changeCompareOrder.gif" border=0 alt="Change the order in which the CDEs are compared">
          </a>          
        </td>
        <TD valign="TOP" align="CENTER"  colspan=1>
		  <html:link action='<%="/cdebrowser/CDECompareExcelDownload.do?"+BrowserNavigationConstants.METHOD_PARAM+"=downloadToExcel"%>'>
		    <img src="<%=request.getContextPath()%>/i/excelDownload.gif" alt="excel download" border=0>
		  </html:link>          
        </TD>        
    </tr>
</table>