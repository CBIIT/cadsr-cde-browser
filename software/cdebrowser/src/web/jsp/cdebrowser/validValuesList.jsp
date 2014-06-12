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
<%@page import="gov.nih.nci.ncicb.cadsr.common.html.* " %>
<%@page import="oracle.clex.process.jsp.GetInfoBean " %>
<%@page import="oracle.clex.process.PageConstants " %>
<%@page import="gov.nih.nci.ncicb.cadsr.common.resource.* " %>
<%@page import="gov.nih.nci.ncicb.cadsr.common.ProcessConstants " %>
<%@page import="java.util.List " %>
<%@page import="gov.nih.nci.ncicb.cadsr.cdebrowser.jsp.util.CDEDetailsUtils" %>
<%@page import="gov.nih.nci.ncicb.cadsr.common.util.StringUtils" %>
<%@page import="java.net.URLEncoder" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>

<jsp:useBean id="infoBean" class="oracle.clex.process.jsp.GetInfoBean"/>
<jsp:setProperty name="infoBean" property="session" value="<%=session %>"/>

<%@include  file="/jsp/cdebrowser/cdebrowserCommon_html/SessionAuth.html"%>

<%
  DataElement de = (DataElement)infoBean.getInfo("de");
  pageContext.setAttribute("de",de);  
  TabInfoBean tib = (TabInfoBean)infoBean.getInfo("tib");
  ValueDomain vd = de.getValueDomain();

  
  String pageId = StringEscapeUtils.escapeJavaScript(infoBean.getPageId());
  String pageName = StringEscapeUtils.escapeJavaScript(PageConstants.PAGEID);
  String pageUrl = StringEscapeUtils.escapeJavaScript("&"+pageName+"="+pageId);
  HTMLPageScroller scroller = (HTMLPageScroller)
                infoBean.getInfo(ProcessConstants.VALID_VALUES_PAGE_SCROLLER);
  CDEBrowserParams params = CDEBrowserParams.getInstance();

%>

<HTML>
<HEAD>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=WINDOWS-1252">
<LINK REL=STYLESHEET TYPE="text/css" HREF="<%=request.getContextPath()%>/css/blaf.css">
<TITLE>
Value Domain
</TITLE>
</HEAD>
<BODY topmargin="0">



<SCRIPT LANGUAGE="JavaScript">
<!--
function redirect1(detailReqType, linkParms ){
  document.location.href="search?dataElementDetails=" + linkParms;
}
function goPage(pgNum,urlInfo) {
  document.location.href= "search?listValidValuesForDataElements=9&tabClicked=2&vvPageNumber="+pgNum+"<%= pageUrl %>"+urlInfo;
}
function listChanged(urlInfo) {
  var pgNum = document.forms[0].vv_pages.options[document.forms[0].vv_pages.selectedIndex].value
  document.location.href= "search?listValidValuesForDataElements=9&tabClicked=2&performQuery=no&vvPageNumber="+pgNum+"<%= pageUrl %>"+urlInfo;
}

function valueMeaningDetails(vmId)
{
  //var urlString="<%=request.getContextPath()%>/search?dataElementDetails=9" + linkParms + "<%= pageUrl %>"+"&queryDE=yes";
  var urlString="<%=request.getContextPath()%>/valueMeaningAlternates.do?method=showValueMeaningAlternates&id="+escape(vmId);
  newWin(urlString,'valueMeaningDetails',800,600)
  
}
  
//-->
</SCRIPT>
<%@ include  file="cdebrowserCommon_html/tab_include.html" %>
<form method="POST" ENCTYPE="application/x-www-form-urlencoded" action="<%= StringEscapeUtils.escapeJavaScript(infoBean.getStringInfo("controller")) %>">
<input type="HIDDEN" name="<%= PageConstants.PAGEID %>" value="<%= StringEscapeUtils.escapeJavaScript(infoBean.getPageId())%>"/>

<table cellpadding="0" cellspacing="0" width="80%" align="center">
  <tr>
    <td class="OraHeaderSubSub" width="100%">Selected Data Element</td>
  </tr>
  <tr>
    <td width="100%"><img height=1 src="i/beigedot.gif" alt="beigedot" width="99%" align=top border=0> </td>
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
    <td class="OraHeaderSubSub" width="100%">Value Domain Details</td>
  </tr>
  <tr>
    <td width="100%"><img height=1 src="i/beigedot.gif" alt="beigedot" width="99%" align=top border=0> </td>
  </tr>
</table>

<table width="80%" align="center" cellpadding="1" cellspacing="1" class="OraBGAccentVeryDark">

  <tr class="OraTabledata">
    <td class="TableRowPromptText">Public ID:</td>
    <td class="OraFieldText"><%=vd.getPublicId()%></td>
 </tr>

 <tr class="OraTabledata">
    <td class="TableRowPromptText">Version:</td>
    <td class="OraFieldText"><%=vd.getVersion()%> </td>
 </tr>
 
  <tr class="OraTabledata">
    <td class="TableRowPromptText" width="20%">Long Name:</td>
    <td class="OraFieldText"><%=vd.getLongName()%></td>
 </tr>
 
 <tr class="OraTabledata">
    <td class="TableRowPromptText">Short Name:</td>
    <td class="OraFieldText"><%=vd.getPreferredName()%></td>
 </tr>
 
  <tr class="OraTabledata">
     <td class="TableRowPromptText">Context Name:</td>
     <td class="OraFieldText"><%=vd.getContext().getName()%></td>
 </tr>

 <tr class="OraTabledata">
    <td class="TableRowPromptText">Definition:</td>
    <td class="OraFieldText"><%=vd.getPreferredDefinition()%> </td>
 </tr>
 <tr class="OraTabledata">
    <td class="TableRowPromptText">Workflow Status:</td>
    <td class="OraFieldText"><%=vd.getAslName()%> </td>
 </tr>

 <tr class="OraTabledata">
    <td class="TableRowPromptText">Datatype:</td>
    <td class="OraFieldText"><%=vd.getDatatype()%></td>
 </tr>
 <tr class="OraTabledata">
    <td class="TableRowPromptText">Unit of Measure:</td>
    <td class="OraFieldText"><%=vd.getUnitOfMeasure()%></td>
 </tr>
 <tr class="OraTabledata">
    <td class="TableRowPromptText">Display Format:</td>
    <td class="OraFieldText"><%=vd.getDisplayFormat()%></td>
 </tr>
 <tr class="OraTabledata">
    <td class="TableRowPromptText">Maximum Length:</td>
    <td class="OraFieldText"><%=vd.getMaxLength()%></td>
 </tr>
 <tr class="OraTabledata">
    <td class="TableRowPromptText">Minimum Length:</td>
    <td class="OraFieldText"><%=vd.getMinLength()%></td>
 </tr>
 <tr class="OraTabledata">
    <td class="TableRowPromptText">Decimal Place:</td>
    <td class="OraFieldText"><%=vd.getDecimalPlace()%></td>
 </tr>
 <tr class="OraTabledata">
    <td class="TableRowPromptText">High Value:</td>
    <td class="OraFieldText"><%=vd.getHighValue()%></td>
 </tr>
 <tr class="OraTabledata">
    <td class="TableRowPromptText">Low Value:</td>
    <td class="OraFieldText"><%=vd.getLowValue()%></td>
 </tr>
 <tr class="OraTabledata">
    <td class="TableRowPromptText">Value Domain Type:</td>
    <td class="OraFieldText"><%=vd.getVDType()%></td>
 </tr>
 
  <tr class="OraTabledata">
    <td class="TableRowPromptText">Conceptual Domain Public ID:</td>
    <td class="OraFieldText"><%=vd.getCDPublicId()%> </td>
 </tr>
 <tr class="OraTabledata">
    <td class="TableRowPromptText">Conceptual Domain Short Name:</td>
    <td class="OraFieldText"><%=vd.getCDPrefName()%> </td>
 </tr>
 <tr class="OraTabledata">
    <td class="TableRowPromptText">Conceptual Domain Context Name:</td>
    <td class="OraFieldText"><%=vd.getCDContextName()%> </td>
 </tr>
 <tr class="OraTabledata">
    <td class="TableRowPromptText">Conceptual Domain Version:</td>
    <td class="OraFieldText"><%=vd.getCDVersion()%> </td>
 </tr>
 
 <tr class="OraTabledata">
    <td class="TableRowPromptText">Origin:</td>
    <td class="OraFieldText"><%=vd.getOrigin()%> </td>
 </tr>
</table>
        <logic:present name="de" property = "valueDomain.conceptDerivationRule"> 
            <% ConceptDerivationRule ocdr = vd.getConceptDerivationRule(); %>
            <!-- Taken out at Denises request TT#1240
               <br>
              <table valign="bottom" cellpadding="0" cellspacing="0" width="80%" align="center">
                <tr  valign="bottom" >
                  <td class="OraHeaderSubSubSub" width="100%">Value Domain Concept Derivation Rule</td>
                </tr>
             </table>
             <table width="80%" align="center" cellpadding="4" cellspacing="1" class="OraBGAccentVeryDark">
               <tr class="OraTabledata">
                  <td class="TableRowPromptText"  width="20%" >Name:</td>
                  <td class="OraFieldText"><%=ocdr.getName()%> </td>
               </tr>
               <tr class="OraTabledata">
                  <td class="TableRowPromptText"  width="20%" >Type:</td>
                  <td class="OraFieldText"><%=ocdr.getType()%> </td>
               </tr>
               <tr class="OraTabledata">
                  <td class="TableRowPromptText"  width="20%">Rule:</td>
                  <td class="OraFieldText"><%=ocdr.getRule()%> </td>
               </tr>  
               <tr class="OraTabledata">
                  <td class="TableRowPromptText"  width="20%" >Methods:</td>
                  <td class="OraFieldText"><%=ocdr.getMethods()%> </td>
               </tr>  
               <tr class="OraTabledata">
                  <td class="TableRowPromptText"  width="20%" >Concatenation Character:</td>
                  <td class="OraFieldText"><%=ocdr.getConcatenationChar()%> </td>
               </tr>                 
             </table>
            --->
              <logic:present name="de" property = "valueDomain.conceptDerivationRule.componentConcepts">                    
                   <br>
                  <table valign="bottom" cellpadding="0" cellspacing="0" width="80%" align="center">
                        <tr  valign="bottom" >
                          <td class="OraHeaderSubSubSub" width="100%">Value Domain Concepts</td>
                        </tr>
                     </table>
                    <table width="80%" align="center" cellpadding="4" cellspacing="1" class="OraBGAccentVeryDark">
                        <tr class="OraTabledata">
                          <td class="OraTableColumnHeader">Concept Name</td>
                          <td class="OraTableColumnHeader">Concept Code</td>
                          <td class="OraTableColumnHeader">Public ID</td>                          
                          <td class="OraTableColumnHeader">Definition Source</td>
                          <td class="OraTableColumnHeader">EVS Source</td>
                          <td class="OraTableColumnHeader">Primary</td>
                        </tr>   
                       <logic:iterate id="comp" name="de" type="gov.nih.nci.ncicb.cadsr.common.resource.ComponentConcept" property="valueDomain.conceptDerivationRule.componentConcepts" indexId="ccIndex" >                                 
                        <tr class="OraTabledata">
                           <td class="OraFieldText"><%=comp.getConcept().getLongName()%> </td>
                           <td class="OraFieldText">
                                <%=CDEDetailsUtils.getConceptCodeUrl(comp.getConcept(),params,"link",",")%>
                           </td>
                           <td class="OraFieldText"><%=comp.getConcept().getPublicId()%> </td> 
                           <td class="OraFieldText"><%=comp.getConcept().getDefinitionSource()%> </td> 
                           <td class="OraFieldText"><%=comp.getConcept().getEvsSource()%> </td>
                          <td class="OraFieldText"><%=StringUtils.booleanToStr(comp.getIsPrimary())%> </td>
                        </tr>
                       </logic:iterate>
                    </table>                      
             </logic:present>
              <logic:notPresent name="de" property = "valueDomain.conceptDerivationRule.componentConcepts">
                    <br>
                      <table valign="bottom" cellpadding="0" cellspacing="0" width="80%" align="center">
                        <tr  valign="bottom" >
                          <td class="OraHeaderSubSubSub" width="100%">Value Domain Concepts</td>
                        </tr>
                     </table>
                     <table width="80%" align="center" cellpadding="4" cellspacing="1" class="OraBGAccentVeryDark">
                       <tr class="OraTabledata">
                          <td   width="20%" >Value Domain does not have any Concepts.</td>
                       </tr>
                       </tr>                 
                     </table>      
              </logic:notPresent>             
      </logic:present>
      <logic:notPresent name="de" property = "valueDomain.conceptDerivationRule">
            <br>
              <table valign="bottom" cellpadding="0" cellspacing="0" width="80%" align="center">
                <tr  valign="bottom" >
                  <td class="OraHeaderSubSubSub" width="100%">Value Domain Concepts</td>
                </tr>
             </table>
             <table width="80%" align="center" cellpadding="4" cellspacing="1" class="OraBGAccentVeryDark">
               <tr class="OraTabledata">
                  <td   width="20%" >Value Domain does not have any Concepts.</td>
               </tr>
               </tr>                 
             </table>      
      </logic:notPresent>      
      
 <logic:present name="de" property = "valueDomain.representation">    
    <% Representation rep = vd.getRepresentation(); %>
    <%
      String contextPath = request.getContextPath();
      String repId = rep.getIdseq();
    
    %>
         <br>
         <table valign="bottom" cellpadding="0" cellspacing="0" width="80%" align="center">
          <tr  valign="bottom" >
           <td class="OraHeaderSubSubSub" width="80%">Representation</td>
          </tr>
         </table>
         <table valign="top"  width="80%" align="center" cellpadding="4" cellspacing="1" class="OraBGAccentVeryDark">
         <tr class="OraTabledata">
            <td class="TableRowPromptText"  width="20%" >Public ID:</td>
            <td class="OraFieldText"><%=rep.getPublicId()%></td>
         </tr>
         <tr class="OraTabledata">
            <td class="TableRowPromptText"  width="20%" >Version:</td>
            <td class="OraFieldText"><%=rep.getVersion()%> </td>
         </tr>
         
         <tr class="OraTabledata"> 
            <td class="TableRowPromptText"  width="20%" >Long Name:</td>
            <td class="OraFieldText"><%=rep.getLongName()%> </td>
         </tr>         
         <tr class="OraTabledata"> 
            <td class="TableRowPromptText"  width="20%" >Short Name:</td>
            <td class="OraFieldText"><%=rep.getPreferredName()%> </td>
         </tr>
         <tr class="OraTabledata">
            <td class="TableRowPromptText"  width="20%" >Context:</td>
            <td class="OraFieldText"><%=rep.getContext().getName()%></td>
         </tr>

 
         </table>
         
        <logic:present name="de" property = "valueDomain.representation.conceptDerivationRule"> 
            <% ConceptDerivationRule repdr = rep.getConceptDerivationRule(); %>
              <logic:present name="de" property = "valueDomain.representation.conceptDerivationRule.componentConcepts">                    
                  <table valign="bottom" cellpadding="0" cellspacing="0" width="80%" align="center">
                        <tr  valign="bottom" >
                          <td class="OraHeaderSubSubSub" width="100%">Representation Concepts</td>
                        </tr>
                     </table>
                    <table width="80%" align="center" cellpadding="4" cellspacing="1" class="OraBGAccentVeryDark">
                        <tr class="OraTabledata">
                          <td class="OraTableColumnHeader">Concept Name</td>
                          <td class="OraTableColumnHeader">Concept Code</td>
                          <td class="OraTableColumnHeader">Public ID</td>                          
                          <td class="OraTableColumnHeader">Definition Source</td>
                          <td class="OraTableColumnHeader">EVS Source</td>   
                          <td class="OraTableColumnHeader">Primary</td>
                        </tr>   
                       <logic:iterate id="comp" name="de" type="gov.nih.nci.ncicb.cadsr.common.resource.ComponentConcept" property="valueDomain.representation.conceptDerivationRule.componentConcepts" indexId="ccIndex" >                                 
                        <tr class="OraTabledata">
                           <td class="OraFieldText"><%=comp.getConcept().getLongName()%> </td>
                           <td class="OraFieldText">
                                <%=CDEDetailsUtils.getConceptCodeUrl(comp.getConcept(),params,"link",",")%>                                  
                           </td>
                           <td class="OraFieldText"><%=comp.getConcept().getPublicId()%> </td> 
                           <td class="OraFieldText"><%=comp.getConcept().getDefinitionSource()%> </td> 
                           <td class="OraFieldText"><%=comp.getConcept().getEvsSource()%> </td>
                           <td class="OraFieldText"><%=StringUtils.booleanToStr(comp.getIsPrimary())%> </td>
                        </tr>
                       </logic:iterate>
                    </table>                      
             </logic:present>
            <logic:notPresent name="de" property = "valueDomain.representation.conceptDerivationRule.componentConcepts"> 
                                            
                       <br>
                       <table valign="bottom" cellpadding="0" cellspacing="0" width="80%" align="center">
                        <tr  valign="bottom" >
                         <td class="OraHeaderSubSubSub" width="100%">Representaiton Concepts</td>
                        </tr>
                       </table>
                       <table width="80%" align="center" cellpadding="4" cellspacing="1" class="OraBGAccentVeryDark">
                         <tr class="OraTabledata">
                            <td  width="20%" >Representation does not have any Concepts.</td>
                         </tr>
                  
                       </table>    
              </logic:notPresent >
  
        </logic:present>
        
        <logic:notPresent name="de" property = "valueDomain.representation.conceptDerivationRule">       
            <br>
            <table valign="bottom" cellpadding="0" cellspacing="0" width="80%" align="center">
                <tr  valign="bottom" >
                <td class="OraHeaderSubSubSub" width="100%">Reprsentation Concepts</td>
                </tr>
            </table>
            <table width="80%" align="center" cellpadding="4" cellspacing="1" class="OraBGAccentVeryDark">
                <tr class="OraTabledata">
                <td  width="20%" >Representation does not have any Concepts.</td>
               </tr>               
            </table>    
        </logic:notPresent >
 </logic:present> 
  <logic:notPresent name="de" property = "valueDomain.representation"> 
         <br>
         <table valign="bottom" cellpadding="0" cellspacing="0" width="80%" align="center">
          <tr  valign="bottom" >
           <td class="OraHeaderSubSubSub" width="100%">Representation</td>
          </tr>
         </table>
         <table width="80%" align="center" cellpadding="4" cellspacing="1" class="OraBGAccentVeryDark">
           <tr class="OraTabledata">
              <td   width="20%" >Value Domain does not have a Representation.</td>
           </tr>
           </tr>                 
         </table>    
 </logic:notPresent> 

<br>
<table cellpadding="0" cellspacing="0" width="80%" align="center" >
  <tr>
    <td class="OraHeaderSubSub" width="100%">Permissible Values</td>
  </tr>
  <tr>
    <td width="100%"><img height=1 src="i/beigedot.gif" alt="beigedot" width="99%" align=top border=0> </td>
  </tr>
</table>

<%
  if ("Enumerated".equals(vd.getVDType())) {
    List vv = (List)infoBean.getInfo(ProcessConstants.VALID_VALUES_LIST);
    int numberOfValidValues = vv.size();
    if (numberOfValidValues > 0) {
      /*StringBuffer vvPageList = (StringBuffer)
                      infoBean.getInfo(ProcessConstants.VALID_VALUES_PAGE_LIST);*/
        String vvPageList = scroller.getScrollerHTML();
%>

<table width="80%" align="center" cellpadding="1" cellspacing="1" border="0">
  <tr><td align="right"><%=vvPageList%></td></tr>
</table>
<%
  }
%>
<table width="80%" align="center" cellpadding="1" cellspacing="1" border="0" class="OraBGAccentVeryDark">
  <tr class="OraTableColumnHeader">
    <th class="OraTableColumnHeader">PV</th>
    <th class="OraTableColumnHeader">PV Meaning</th>
    <th class="OraTableColumnHeader">PV Meaning Concept Codes</th>
    <th class="OraTableColumnHeader">PV Meaning Description</th>
    <th class="OraTableColumnHeader">PV Begin Date</th>
    <th class="OraTableColumnHeader">PV End   Date</th>
    <th class="OraTableColumnHeader">VM Public ID</th>
    <th class="OraTableColumnHeader">VM Version</th>
  </tr>
<%
  ValidValue validValue;
  if (numberOfValidValues > 0) {
    for (int i=0;i<numberOfValidValues; i++) {
      validValue = (ValidValue)vv.get(i);
%>
      <tr class="OraTabledata">
        <td class="OraFieldText"><%=validValue.getShortMeaningValue()%> </td>
        <td class="OraFieldText">
        <% String encoded = "";
        	try{
        		encoded = URLEncoder.encode(validValue.getShortMeaning(), "UTF-8");
       	   }catch(Exception e){
       	   	e.printStackTrace();
       	   }
        %>
        
          <a href="javascript:valueMeaningDetails('<%=validValue.getVmId()%>')" >
            <%=validValue.getShortMeaning()%> 
          </a>                   
        </td>
       <td class="OraFieldText">
          <%=CDEDetailsUtils.getConceptCodesUrl(validValue.getConceptDerivationRule(),params,"link",",")%>
       </td>
        <td class="OraFieldText"><%=validValue.getDescription()%> </td>
        <td class="OraFieldText"><%=validValue.getBeginDate()%></td>
        <td class="OraFieldText"><%=validValue.getEndDate()%></td>
        <td class="OraFieldText"><%=validValue.getVmId()%></td>
        <td class="OraFieldText"><%=validValue.getVmVersion()%></td>        
      </tr>
<%
    }
  }
  else {
%>
       <tr class="OraTabledata">
         <td colspan="4">There are no permissible values for the selected CDE.</td>
       </tr>
<%
  }
%>
</table>
<%
  }
  else {
%>
<center>
  <p class="OraHeaderSubSub">This Value Domain is Non Enumerated</p>
</center>

<%
  }
%>


<br>
<table cellpadding="0" cellspacing="0" width="80%" align="center" >
  <tr>
    <td class="OraHeaderSubSub" width="100%">Reference Documents</td>
  </tr>
  <tr>
    <td width="100%"><img height=1 src="i/beigedot.gif" alt="beigedot" width="99%" align=top border=0> </td>
  </tr>
</table>
<table width="80%" align="center" cellpadding="1" cellspacing="1" border="0" class="OraBGAccentVeryDark">
  <tr class="OraTableColumnHeader">
    <th class="OraTableColumnHeader">Document Name</th>
    <th class="OraTableColumnHeader">Document Type</th>
    <th class="OraTableColumnHeader">Document Text</th>
    <th class="OraTableColumnHeader">Context</th>
    <th class="OraTableColumnHeader">URL</th>
  </tr>
<%
  ReferenceDocument rd;
  List refDocs = de.getValueDomain().getRefereceDocs();
  int numberOfDocs;
  if ((refDocs != null)&& (refDocs.size() > 0)) {
    numberOfDocs = refDocs.size();
    for (int i=0;i<numberOfDocs; i++) {
      rd = (ReferenceDocument)refDocs.get(i);
%>
      <tr class="OraTabledata">
        <td class="OraFieldText"><%=rd.getDocName()%> </td>
        <td class="OraFieldText"><%=rd.getDocType()%> </td>
        <td class="OraFieldText"><%=rd.getDocText()%> </td>
         <td class="OraFieldText">
         <% if (rd.getContext() != null) {
         %><%=rd.getContext().getName()%> <% } %></td>
        <td class="OraFieldText">
         <% if (rd.getUrl() != null) {
         %>  <a href="<%=rd.getUrl()%>" target="AuxWindow"> <%=rd.getUrl()%> </a>
         <% } %>
         </td>
      </tr>
<%
    }
  }
  else {
%>
       <tr class="OraTabledata">
         <td colspan="5">There are no reference documents for the value domain.</td>
       </tr>
<%
  }
%>
</table>


</form>

<%@ include file="/jsp/common/common_bottom_border.jsp"%>

</BODY>
</HTML>

