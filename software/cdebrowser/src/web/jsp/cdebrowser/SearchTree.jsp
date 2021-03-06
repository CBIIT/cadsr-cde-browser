<%--L
  Copyright SAIC-F Inc.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-cde-browser/LICENSE.txt for details.

  Portions of this source file not modified since 2008 are covered by:

  Copyright 2000-2008 Oracle, Inc.

  Distributed under the caBIG Software License.  For details see
  http://ncip.github.com/cadsr-cde-browser/LICENSE-caBIG.txt
L--%>

<%
    String skin = (String) request.getParameter("skin");    
    if (skin != null && skin.equals("null")) skin = null;
    if (skin == null) skin = "default";
    
%>

<html>
<head>
  <link rel="stylesheet" type="text/css" href="skins/<%=skin%>/SearchTree.css"/>
</head>
<body>
<table>
      <form name="search" action="/jsp/cdebrowser/SearchResults.jsp?skin=<%=skin%>" target="searchResults">
        <tr>
          <td>
            <table>
       
              <tr>
                <td>Search:</td>
                <td><input name="searchTerm" size="25"/>&nbsp;<INPUT type="submit" title="Submit Search" name="search" value="Search"/></td>
              </tr>
              <tr>              
                <td>&nbsp;</td> 
                <td>
                  <INPUT type="checkbox" name="matchwholewords" value="checked"/>&nbsp;Match Entire Search Terms Only
                </td>                   
              </tr>
            </table>
          </td>
        </tr>
      </form>  
</table>
</body>
</html>
