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
<%@ page contentType="text/html;charset=windows-1252"%>

<%@page import="gov.nih.nci.ncicb.cadsr.common.util.*"%>
<%@page import="gov.nih.nci.ncicb.cadsr.common.html.*"%>
<%@page import="oracle.clex.process.PageConstants"%>
<%@page import="gov.nih.nci.ncicb.cadsr.common.resource.*"%>
<%@page import="java.util.*"%>
<%@page import="gov.nih.nci.ncicb.cadsr.common.CaDSRConstants"%>
<%@page import="gov.nih.nci.ncicb.cadsr.common.ProcessConstants"%>
<%@ page import="gov.nih.nci.ncicb.cadsr.common.struts.common.BrowserNavigationConstants"%>
<%@page import="gov.nih.nci.cadsr.domain.ReferenceDocument"%>
<%@page import="gov.nih.nci.cadsr.domain.ReferenceDocumentAttachment"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>


<jsp:useBean id="infoBean" class="oracle.clex.process.jsp.GetInfoBean" />
<jsp:setProperty name="infoBean" property="session"
	value="<%=session%>" />

<%@include file="/jsp/cdebrowser/cdebrowserCommon_html/SessionAuth.html"%>

<%
	List classificationVector = (List) infoBean.getInfo(ProcessConstants.CLASSIFICATION_VECTOR);
	DataElement de = (DataElement) infoBean.getInfo("de");
	TabInfoBean tib = (TabInfoBean) infoBean.getInfo("tib");
	Map csRefDocs = (Map) infoBean.getInfo("csRefDocs");
	Map csiRefDocs = (Map) infoBean.getInfo("csiRefDocs");
	Map csContacts = (Map) infoBean.getInfo("csContacts");
	String pageId = StringEscapeUtils.escapeJavaScript(infoBean.getPageId());
	String pageName = StringEscapeUtils.escapeJavaScript(PageConstants.PAGEID);
	String pageUrl = "&"+StringEscapeUtils.escapeJavaScript(pageName)+ "=" + StringEscapeUtils.escapeJavaScript(pageId);
	HTMLPageScroller scroller = (HTMLPageScroller) infoBean.getInfo(ProcessConstants.DE_CS_PAGE_SCROLLER);
	String scrollerHTML = scroller.getScrollerHTML();
	CDEBrowserParams params = CDEBrowserParams.getInstance();
%>


<HTML>
	<HEAD>
		<META HTTP-EQUIV="Content-Type"
			CONTENT="text/html; charset=windows-1252">
		<LINK REL=STYLESHEET TYPE="text/css"
			HREF="<%=request.getContextPath()%>/css/blaf.css">
		<TITLE>Classifications</TITLE>
	</HEAD>
	<BODY topmargin="0">
		<%
			String csId = "";
			String contactInfo = "";
			Iterator csConIter = csContacts.keySet().iterator();
			while (csConIter.hasNext()) {
				csId = (String) csConIter.next();
				contactInfo = (String) csContacts.get(csId);
				if (contactInfo != null && contactInfo.trim().length() > 0)
					contactInfo = "Contact Information<br>" + contactInfo;
				else
					contactInfo = "No contact information available";
		%>
		<div id="<%=csId%>" class="tip"><%=contactInfo%></div>
		<%
			}
		%>
		<script type="text/javascript" src="/js/tooltip.js"></script>
		<link rel="stylesheet" href="/CDEBrowser/css/tooltip.css"
			type="text/css" />
		<SCRIPT LANGUAGE="JavaScript">
<!--
function goPage(pgNum,urlInfo) {
  document.location.href= "search?classificationsForDataElements=9&tabClicked=3&pageNum="+pgNum+"<%=pageUrl%>"+urlInfo;
}
function listChanged(urlInfo) {
  var pgNum = document.forms[0].cs_pages.options[document.forms[0].cs_pages.selectedIndex].value
  document.location.href= "search?classificationsForDataElements=9&tabClicked=3&newSearch=no&pageNum="+pgNum+"<%=pageUrl%>"+urlInfo;
}
  
//-->
</SCRIPT>
		<%@ include file="cdebrowserCommon_html/tab_include.html"%>
		<form method="POST" ENCTYPE="application/x-www-form-urlencoded"
			action="<%=infoBean.getStringInfo("controller")%>">
			<input type="HIDDEN" name="<%=StringEscapeUtils.escapeJavaScript(PageConstants.PAGEID)%>"
				value="<%=StringEscapeUtils.escapeJavaScript(infoBean.getPageId())%>" />
			<table cellpadding="0" cellspacing="0" width="80%" align="center">
				<tr>
					<td class="OraHeaderSubSub" width="100%">
						Selected Data Element
					</td>
				</tr>
				<tr>
					<td width="100%">
						<img height=1 src="i/beigedot.gif" alt="beigedot" width="99%" align=top border=0>
					</td>
				</tr>
			</table>

			<table width="80%" align="center" cellpadding="1" cellspacing="1"
				bgcolor="#999966">

				<tr class="OraTabledata">
					<td class="TableRowPromptText">
						Public ID:
					</td>
					<td class="OraFieldText"><%=de.getCDEId()%></td>
				</tr>

				<tr class="OraTabledata">
					<td class="TableRowPromptText">
						Version:
					</td>
					<td class="OraFieldText"><%=de.getVersion()%>
					</td>
				</tr>

				<tr class="OraTabledata">
					<td class="TableRowPromptText" width="20%">
						Long Name:
					</td>
					<td class="OraFieldText"><%=de.getLongName()%></td>
				</tr>

				<tr class="OraTabledata">
					<td class="TableRowPromptText" width="20%">
						Short Name:
					</td>
					<td class="OraFieldText"><%=de.getPreferredName()%></td>
				</tr>



				<tr class="OraTabledata">
					<td class="TableRowPromptText">
						Preferred Question Text:
					</td>
					<td class="OraFieldText"><%=de.getLongCDEName()%></td>
				</tr>

				<tr class="OraTabledata">
					<td class="TableRowPromptText">
						Definition:
					</td>
					<td class="OraFieldText"><%=de.getPreferredDefinition()%>
					</td>
				</tr>
				<tr class="OraTabledata">
					<td class="TableRowPromptText">
						Workflow Status:
					</td>
					<td class="OraFieldText"><%=de.getAslName()%>
					</td>
				</tr>



			</table>
			<br>


			<table cellpadding="0" cellspacing="0" width="80%" align="center">
				<tr>
					<td class="OraHeaderSubSub" width="100%">
						Classifications
					</td>
				</tr>
				<tr>
					<td>
						<img height=1 src="i/beigedot.gif" alt="beigedot" width="99%" align=top border=0>
					</td>
				</tr>
				<tr>
					<td>
						<font size="-2" color="#336699">*CS:Classification
							Scheme&nbsp;&nbsp; CSI:Classification Scheme Item</font>
					</td>
				</tr>
			</table>

			<table width="80%" align="center" cellpadding="1" cellspacing="1"
				border="0">
				<tr>
					<td align="right"><%=scrollerHTML%></td>
				</tr>
			</table>

			<table width="80%" align="center" cellpadding="1" cellspacing="1"
				bgcolor="#999966">
				<tr class="OraTableColumnHeader">
					<th>
						CS* Long Name
					</th>
					<th>
						CS* Definition
					</th>
					<th>
						CS* Public ID/Version
					</th>
					<%-- <th>CS* Version</th> changed for 4.0--%>
					<th>
						CSI* Name
					</th>
					<th>
						CSI* Type
					</th>
					<th>
						CSI* Public ID/Version
					</th>
				</tr>
				<%
					Classification classification;
					int classificationCount = classificationVector.size();
					if (classificationCount > 0) {
						for (int i = 0; i < classificationCount; i++) {
							classification = (Classification) classificationVector
									.get(i);
				%>
				<tr class="OraTabledata">
					<td class="OraFieldText"
						onmouseout="popUp(event,'<%=classification.getCsIdseq()%>')"
						onmouseover="popUp(event,'<%=classification.getCsIdseq()%>')"
						onclick="return false"><%=classification.getClassSchemeLongName()%>
					</td>
					<td class="OraFieldText"><%=classification.getClassSchemeDefinition()%>
					</td>
					<td class="OraFieldText">
						<%=classification.getClassSchemePublicId()
							+ "v" + classification.getCsVersion()%>
					</td>
					<%-- <td class="OraFieldText"><%=classification.getCsVersion()%> </td> changed for 4.0--%>
					<td class="OraFieldText"><%=classification.getClassSchemeItemName()%>
					</td>
					<td class="OraFieldText"><%=classification.getClassSchemeItemType()%>
					</td>
					<td class="OraFieldText"><%=classification.getClassSchemeItemId() + "v"
							+ classification.getClassSchemeItemVersion()%>
					</td>
				</tr>
				<%
					}
					} else {
				%>
				<tr class="OraTabledata">
					<td colspan="6">
						There are no classifications for the selected CDE.
					</td>
				</tr>
				<%
					}
				%>
			</table>
			<br>
			<table cellpadding="0" cellspacing="0" width="80%" align="center">
				<tr>
					<td class="OraHeaderSubSub" width="100%">
						Classifications Scheme Reference Documents
					</td>
				</tr>
				<tr>
					<td>
						<img height=1 src="i/beigedot.gif" alt="beigedot" width="99%" align=top border=0>
					</td>
				</tr>
				<tr>
					<td>
						<font size="-2" color="#336699">*CS:Classification
							Scheme&nbsp;&nbsp; </font>
					</td>
				</tr>
			</table>

			<table width="80%" align="center" cellpadding="1" cellspacing="1"
				bgcolor="#999966">
				<tr class="OraTableColumnHeader">
					<th>
						CS* Long Name
					</th>
					<th>
						CS* Version
					</th>
					<th>
						Document Name
					</th>
					<th>
						Document Type
					</th>
					<th>
						Document Text
					</th>
					<th>
						URL
					</th>
					<th>
						Attachments
					</th>
				</tr>
				<%
					if (csRefDocs != null && csRefDocs.keySet().size() > 0) {
						Iterator csIter = csRefDocs.keySet().iterator();
						while (csIter.hasNext()) {
							Classification cs = (Classification) csIter.next();
							List refDocs = (List) csRefDocs.get(cs);
							for (int i = 0; i < refDocs.size(); i++) {
								ReferenceDocument refDoc = (ReferenceDocument) refDocs
										.get(i);
				%>
				<tr class="OraTabledata">
					<td class="OraFieldText"><%=cs.getClassSchemeLongName()%></td>
					<td class="OraFieldText"><%=cs.getCsVersion()%></td>
					<td class="OraFieldText"><%=refDoc.getName()%>
					</td>
					<td class="OraFieldText"><%=refDoc.getType()%>
					</td>
					<td class="OraFieldText">
						<%
							if (refDoc.getDoctext() != null) {
						%>
						<%=refDoc.getDoctext()%>
						<%
							}
						%>
					</td>
					<td class="OraFieldText">
						<%
							if (refDoc.getURL() != null) {
						%>
						<a href="<%=refDoc.getURL()%>" target="AuxWindow"> <%=refDoc.getURL()%>
						</a>
						<%
							}
						%>
					</td>
					<td>

						<%
							Iterator attIter = refDoc.getAttachments().iterator();
										while (attIter.hasNext()) {
											ReferenceDocumentAttachment refDocAtt = (ReferenceDocumentAttachment) attIter
													.next();
						%>
						<a
							href='<%=request.getContextPath()
											+ "/ocbrowser/viewRefDocAttchment.do?"
											+ BrowserNavigationConstants.METHOD_PARAM
											+ "=viewReferenceDocAttchment&"
											+ CaDSRConstants.REFERENCE_DOC_ATTACHMENT_NAME
											+ "=" + refDocAtt.getName()%>'
							target="_blank"> <%=refDocAtt.getName()%> </a>
						<br>
						<%
							}
						%>
					</td>
				</tr>
				<%
					}
						}
					} else {
				%>

				<tr class="OraTabledata">
					<td colspan="7">
						There are no reference documents for the classifications.
					</td>
				</tr>

				<%
					}
				%>
			</table>

			<br>

			<table cellpadding="0" cellspacing="0" width="80%" align="center">
				<tr>
					<td class="OraHeaderSubSub" width="100%">
						Classification Scheme Item Reference Document
					</td>
				</tr>
				<tr>
					<td>
						<img height=1 src="i/beigedot.gif" alt="beigedot" width="99%" align=top border=0>
					</td>
				</tr>
				<tr>
					<td>
						<font size="-2" color="#336699">CSI:Classification Scheme
							Item</font>
					</td>
				</tr>
			</table>

			<table width="80%" align="center" cellpadding="1" cellspacing="1"
				bgcolor="#999966">
				<tr class="OraTableColumnHeader">
					<th>
						CSI* Name
					</th>
					<th>
						Document Name
					</th>
					<th>
						Document Type
					</th>
					<th>
						Document Text
					</th>
					<th>
						URL
					</th>
					<th>
						Attachments
					</th>
				</tr>
				<%
					if (csiRefDocs != null && csiRefDocs.keySet().size() > 0) {
						Iterator csiIter = csiRefDocs.keySet().iterator();
						while (csiIter.hasNext()) {
							String csiName = (String) csiIter.next();
							List refDocs = (List) csiRefDocs.get(csiName);
							for (int i = 0; i < refDocs.size(); i++) {
								ReferenceDocument refDoc = (ReferenceDocument) refDocs
										.get(i);
				%>
				<tr class="OraTabledata">
					<td class="OraFieldText"><%=csiName%></td>
					<td class="OraFieldText"><%=refDoc.getName()%>
					</td>
					<td class="OraFieldText"><%=refDoc.getType()%>
					</td>
					<td class="OraFieldText">
						<%
							if (refDoc.getDoctext() != null) {
						%>
						<%=refDoc.getDoctext()%>
						<%
							}
						%>
					
					<td class="OraFieldText">
					</td>
					<%
						if (refDoc.getURL() != null) {
					%>
					<a href="<%=refDoc.getURL()%>" target="AuxWindow"> <%=refDoc.getURL()%>
					</a>
					<%
						}
					%>
					</td>
					<td>

						<%
							Iterator attIter = refDoc.getAttachments().iterator();
										while (attIter.hasNext()) {
											ReferenceDocumentAttachment refDocAtt = (ReferenceDocumentAttachment) attIter
													.next();
						%>
						<a
							href='<%=request.getContextPath()
											+ "/ocbrowser/viewRefDocAttchment.do?"
											+ BrowserNavigationConstants.METHOD_PARAM
											+ "=viewReferenceDocAttchment&"
											+ CaDSRConstants.REFERENCE_DOC_ATTACHMENT_NAME
											+ "=" + refDocAtt.getName()%>'
							target="_blank"> <%=refDocAtt.getName()%> </a>
						<br>
						<%
							}
						%>
					</td>
				</tr>
				<%
					}
						}
					} else {
				%>

				<tr class="OraTabledata">
					<td colspan="6">
						There are no reference documents for the classifications.
					</td>
				</tr>

				<%
					}
				%>
			</table>


		</form>

		<%@ include file="/jsp/common/common_bottom_border.jsp"%>

	</BODY>
</HTML>
