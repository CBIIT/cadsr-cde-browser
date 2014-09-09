/*L
 * Copyright SAIC-F Inc.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-cde-browser/LICENSE.txt for details.
 *
 * Portions of this source file not modified since 2008 are covered by:
 *
 * Copyright 2000-2008 Oracle, Inc.
 *
 * Distributed under the caBIG Software License.  For details see
 * http://ncip.github.com/cadsr-cde-browser/LICENSE-caBIG.txt
 */

package gov.nih.nci.ncicb.cadsr.cdebrowser.process;

import gov.nih.nci.ncicb.cadsr.common.CaDSRConstants;
import gov.nih.nci.ncicb.cadsr.common.ProcessConstants;
import gov.nih.nci.ncicb.cadsr.common.base.process.BasePersistingProcess;
import gov.nih.nci.ncicb.cadsr.common.cdebrowser.CollectionUtil;
import gov.nih.nci.ncicb.cadsr.common.cdebrowser.DESearchQueryBuilder;
import gov.nih.nci.ncicb.cadsr.common.cdebrowser.DataElementSearchBean;
import gov.nih.nci.ncicb.cadsr.common.html.HTMLPageScroller;
import gov.nih.nci.ncicb.cadsr.common.resource.DataElement;
import gov.nih.nci.ncicb.cadsr.common.resource.NCIUser;
import gov.nih.nci.ncicb.cadsr.common.resource.ValidValue;
import gov.nih.nci.ncicb.cadsr.common.resource.ValueDomain;
import gov.nih.nci.ncicb.cadsr.common.resource.handler.DataElementHandler;
import gov.nih.nci.ncicb.cadsr.common.resource.handler.ValidValueHandler;
import gov.nih.nci.ncicb.cadsr.common.struts.common.BrowserFormConstants;
import gov.nih.nci.ncicb.cadsr.common.util.AppScanValidator;
import gov.nih.nci.ncicb.cadsr.common.util.BC4JPageIterator;
import gov.nih.nci.ncicb.cadsr.common.util.CDEBrowserParams;
import gov.nih.nci.ncicb.cadsr.common.util.DBUtil;
import gov.nih.nci.ncicb.cadsr.common.util.PageIterator;
import gov.nih.nci.ncicb.cadsr.common.util.SortableColumnHeader;
import gov.nih.nci.ncicb.cadsr.common.util.StringUtils;
import gov.nih.nci.ncicb.cadsr.common.util.TabInfoBean;
import gov.nih.nci.ncicb.cadsr.common.util.UserErrorMessage;
import gov.nih.nci.ncicb.cadsr.common.util.logging.Log;
import gov.nih.nci.ncicb.cadsr.common.util.logging.LogFactory;
import gov.nih.nci.ncicb.cadsr.contexttree.TreeConstants;
import gov.nih.nci.ncicb.cadsr.objectCart.CDECart;
import gov.nih.nci.ncicb.cadsr.objectCart.CDECartItem;
import gov.nih.nci.ncicb.cadsr.objectCart.CDECartItemTransferObject;
import gov.nih.nci.ncicb.cadsr.objectCart.impl.CDECartOCImpl;
import gov.nih.nci.objectCart.client.ObjectCartClient;
import gov.nih.nci.objectCart.client.ObjectCartException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import oracle.cle.persistence.HandlerFactory;
import oracle.cle.process.ProcessInfoException;
import oracle.cle.process.Service;
import oracle.cle.util.statemachine.TransitionCondition;
import oracle.cle.util.statemachine.TransitionConditionException;


/**
 * @author Ram Chilukuri
 * @version: $Id: GetDataElements.java,v 1.39 2009-02-02 18:41:36 davet Exp $
 */
public class GetDataElements extends BasePersistingProcess {
	private static Log log = LogFactory.getLog(GetDataElements.class.getName());

	String dsName = null;

	public GetDataElements() {
		this(null);

		DEBUG = false;
	}

	public GetDataElements(Service aService) {
		super(aService);

		DEBUG = false;
	}

	/**
	 * Registers all the parameters and results  (<code>ProcessInfo</code>) for
	 * this process during construction.
	 */
	public void registerInfo() {
		try {
			registerResultObject("desb");
			registerResultObject("tibSearchDE");
			registerStringParameter("P_PARAM_TYPE");
			registerStringParameter("P_IDSEQ");
			registerStringParameter("P_REGSTATUS");
			//For treeBread crumbs
			registerStringParameter(TreeConstants.TREE_BREADCRUMBS);
			registerStringResult(TreeConstants.TREE_BREADCRUMBS);
			//
			registerStringResult("P_PARAM_TYPE");
			registerStringResult("P_IDSEQ");
			registerStringResult("P_REGSTATUS");

			//
			registerStringParameter("P_CS_CSI_IDSEQ");
			registerStringResult("P_CONTEXT");
			registerStringResult("P_CONTE_IDSEQ");
			registerStringParameter("P_CONTE_IDSEQ");
			registerStringParameter("diseaseName");
			registerStringParameter("templateType");
			registerStringParameter("templateName");
			registerStringParameter("contextName");
			registerStringParameter("csName");
			registerStringResult("P_PROTOCOL");
			registerParameterObject("SEARCH");
			registerResultObject(ProcessConstants.PAGE_CONTEXT);
			registerResultObject(ProcessConstants.ALL_DATA_ELEMENTS);
			registerStringParameter("performQuery");
			registerStringResult("performQuery");
			registerParameterObject("desb");
			registerStringParameter("NOT_FIRST_DISPLAY");
			//registerStringParameter("SBREXT_DSN");
			//registerStringParameter("SBR_DSN");
			registerStringParameter("XML_DOWNLOAD_DIR");
			registerStringParameter("TREE_URL");
			//registerStringResult("SBREXT_DSN");
			//registerStringResult("SBR_DSN");
			registerStringResult("XML_DOWNLOAD_DIR");
			registerStringResult("TREE_URL");
			registerStringParameter("INITIALIZED");
			registerStringResult("INITIALIZED");
			registerParameterObject("dbUtil");
			registerResultObject("dbUtil");
			registerStringParameter("XML_PAGINATION_FLAG");
			registerStringResult("XML_PAGINATION_FLAG");
			registerStringParameter("XML_FILE_MAX_RECORDS");
			registerStringResult("XML_FILE_MAX_RECORDS");
			registerParameterObject(ProcessConstants.DE_SEARCH_PAGE_ITERATOR);
			registerResultObject(ProcessConstants.DE_SEARCH_PAGE_ITERATOR);
			registerStringParameter(ProcessConstants.DE_SEARCH_RESULTS_PAGE_NUMBER);
			registerStringResult(ProcessConstants.DE_SEARCH_RESULTS_PAGE_NUMBER);
			registerParameterObject(ProcessConstants.DE_SEARCH_QUERY_BUILDER);
			registerResultObject(ProcessConstants.DE_SEARCH_QUERY_BUILDER);
			registerResultObject(ProcessConstants.DE_SEARCH_PAGE_SCROLLER);
			registerResultObject(ProcessConstants.DE_SEARCH_TOP_PAGE_SCROLLER);
			registerResultObject("uem");
			registerParameterObject(ProcessConstants.SELECT_DE);
			registerStringResult(ProcessConstants.DOWNLOAD_LINK_WIKI);

		}
		catch (ProcessInfoException pie) {
			reportException(pie, true);
		}
		catch (Exception e) {
		}
	}

	/**
	 * persist: called by start to do all persisting work for this process.  If
	 * this method throws an exception, then the condition returned by the
	 * <code>getPersistFailureCondition()</code> is set.  Otherwise, the
	 * condition returned by <code>getPersistSuccessCondition()</code> is set.
	 */
	public void persist() throws Exception {
		HttpServletRequest myRequest = null;
		HttpSession userSession = null;
		TabInfoBean tib = null;
		String[] searchParam = null;
		DBUtil dbUtil = null;

		try {
			myRequest = (HttpServletRequest) getInfoObject("HTTPRequest");
			userSession = myRequest.getSession(false);


			log.info("GetDataElements process started successfully ");
			initialize(userSession);
			log.info(" -Performed intialization successfully");

			tib = new TabInfoBean("cdebrowser_search_tabs");

			tib.processRequest(myRequest);

			if (tib.getMainTabNum() != 0) {
				tib.setMainTabNum(0);
			}

			log.info("- Intialized TabInfoBean successfully");

			String paramType = getStringInfo("P_PARAM_TYPE");
			if (paramType!=null)
			{
				if (!AppScanValidator.validateSearchParameterType(paramType))
					throw new Exception ("Invalidate parameter type");
 
			}
			String paramIdSeq = getStringInfo("P_IDSEQ");
			if (paramIdSeq!=null)
			{
 
				if (!AppScanValidator.validateElementIdSequence(paramIdSeq))
					throw new Exception ("Invalidate element ID ");
			}
			
			String paramRegStatus = getStringInfo("P_REGSTATUS");
			//addition validation rule maybe required
			if (paramRegStatus!=null)
			{
				if (!AppScanValidator.validateSearchParameterType(paramRegStatus))
					throw new Exception ("Invalidate registration status type");
 
			}
			
			String treeConteIdseq = getStringInfo("P_CONTE_IDSEQ");
			if (treeConteIdseq!=null)
			{
 
				if (!AppScanValidator.validateElementIdSequence(treeConteIdseq))
					throw new Exception ("Invalidate ID sequence");
			}
			
			searchParam = getInfoStringArray("SEARCH");

			String performQuery = getStringInfo("performQuery");
			String conteName = getStringInfo("contextName");
			String searchScope = getStringInfo("browserSearchScope");
			String previousQuery = getStringInfo("baseQuery");			
			if (searchScope == null)
				searchScope = BrowserFormConstants.BROWSER_SEARCH_SCOPE_NEW;

			log.info("- Retrieved request parameters successfully");

			if ((paramType != null) && (paramIdSeq != null) && !("newSearch".equals(performQuery))) {       
				setResult("P_CONTEXT", conteName);
				setResult("P_CONTE_IDSEQ", treeConteIdseq);
			}

			String crumbs = getStringInfo(TreeConstants.TREE_BREADCRUMBS);

			if(crumbs!=null)
				crumbs = StringUtils.strReplace(crumbs,"*??*","'");
			setResult(TreeConstants.TREE_BREADCRUMBS, crumbs);


			DataElementSearchBean desb = null;
			List queryResults = null;
			PageIterator dePageIterator = null;
			DESearchQueryBuilder queryBuilder = null;
			String queryStmt;
			String orderBy;
			DataElementHandler dh = null;
			Object[] queryParams = null;

			dbUtil = getDBUtil();

			if (performQuery == null) {
				desb = new DataElementSearchBean(myRequest);
				dePageIterator = new BC4JPageIterator(100);
				desb.initSearchPreferences(dbUtil);
				setDownloadLink(dbUtil);
			}else if (performQuery.equals("yes")) {
				desb = new DataElementSearchBean(myRequest);
				//TODO: After creating the bean it is set with default values : GF 18442
				desb.initSearchPreferences(dbUtil);				 
				// Need to the session Preference which is per session
				setValuesFromOldSearchBean(desb);
				desb.setLOVLists(dbUtil);
				log.info("- Created DataElementSearchBean successfully");

				dePageIterator =
					(PageIterator) getInfoObject(
							ProcessConstants.DE_SEARCH_PAGE_ITERATOR);

				//dePageIterator.setCurrentPage(0);
				if (dePageIterator == null) {
					log.info("- Unable to retrieve PageIterator from session. Creating a new PageIterator..");
					dePageIterator = new BC4JPageIterator(100);
				}
				else {
					log.info("- Retrieved PageIterator from session successfully");
				}

				dePageIterator.setCurrentPage(0);

				if (paramType != null && (paramType.equalsIgnoreCase("REGCSI") ||
						paramType.equalsIgnoreCase("REGCS")))
					queryBuilder =  new DESearchQueryBuilder(
							myRequest, paramType, paramIdSeq + "," + paramRegStatus, treeConteIdseq,desb);
				else 

					queryBuilder =
						new DESearchQueryBuilder(
								myRequest, paramType, paramIdSeq, treeConteIdseq,desb);
				//TT 1511
				if((paramType!=null)&&(paramType.equals("CRF")||paramType.equals("TEMPLATE")))
				{
					myRequest.setAttribute(ProcessConstants.FORMS_IGNORE_FILTER,"Search results contain all the CDEs in the Form/Template and are not filtered by the Search preferences");
				}
				log.trace("- Created DESearchQueryBuilder successfully");

				queryStmt = queryBuilder.getSQLWithoutOrderBy();
				orderBy = queryBuilder.getOrderBy();

				if (searchScope.equalsIgnoreCase(BrowserFormConstants.BROWSER_SEARCH_SCOPE_SEARCHRESULTS)) {
					if (desb.getSimpleSearchStr()!=null) {
						queryStmt = queryStmt + " and de.de_idseq in ( " + previousQuery + " )";
						String searchCrumb = (String)userSession.getAttribute("searchCrumb") + ">>" 
						+ desb.getNameSearchMode() + " (" + desb.getBasicSearchType() + "=" +
						desb.getSimpleSearchStr() + ")";
						userSession.setAttribute("searchCrumb", searchCrumb);
					}
				}

				queryParams = queryBuilder.getQueryParams();				
				log.trace("- Query stmt is " + queryStmt);

				dh = (DataElementHandler) HandlerFactory.getHandler(DataElement.class);

				log.trace("Obtained DataElementHandler using HandlerFactory successfully");

				queryResults = dh.findDataElementsFromQueryClause(queryStmt, orderBy, getSessionId(), dePageIterator);

				log.trace("Query executed successfully");

				createPageScroller(dePageIterator, ProcessConstants.TOP_PAGE_SCROLLER, myRequest.getContextPath());
				createPageScroller(
						dePageIterator, ProcessConstants.BOTTOM_PAGE_SCROLLER, myRequest.getContextPath());
				myRequest.setAttribute(CaDSRConstants.ANCHOR, "results");
				if (previousQuery == null){
					userSession.setAttribute("baseQuery", queryBuilder.getXMLQueryStmt());					
				}
				else{
					userSession.setAttribute("baseQuery", queryBuilder.getXMLQueryStmt()+ " and de.de_idseq in ( " + previousQuery + " )");					
				}
				log.trace("Created both page scrollers successfully");
			}else if (performQuery.equals("no")) {
				desb = (DataElementSearchBean) getInfoObject("desb");

				int pageNumber =
					Integer.parseInt(
							getStringInfo(ProcessConstants.DE_SEARCH_RESULTS_PAGE_NUMBER));
				dePageIterator =
					(PageIterator) getInfoObject(
							ProcessConstants.DE_SEARCH_PAGE_ITERATOR);
				dePageIterator.setCurrentPage(pageNumber);
				queryBuilder =
					(DESearchQueryBuilder) getInfoObject(
							ProcessConstants.DE_SEARCH_QUERY_BUILDER);

				queryStmt = queryBuilder.getSQLWithoutOrderBy();
				orderBy = queryBuilder.getOrderBy();

				queryParams = queryBuilder.getQueryParams();
				dh = (DataElementHandler) HandlerFactory.getHandler(DataElement.class);

				queryResults =
					dh.findDataElementsFromQueryClause(
							queryStmt, orderBy, getSessionId(), dePageIterator);

				createPageScroller(dePageIterator, ProcessConstants.TOP_PAGE_SCROLLER,
						myRequest.getContextPath());
				createPageScroller(dePageIterator, ProcessConstants.BOTTOM_PAGE_SCROLLER,
						myRequest.getContextPath());
				myRequest.setAttribute(CaDSRConstants.ANCHOR, "results");
			}

			else if (performQuery.equals("addToCart")) {
				ValidValueHandler valueHandler =
					(ValidValueHandler) HandlerFactory.getHandler(ValidValue.class);
				desb = (DataElementSearchBean) getInfoObject("desb");
				dePageIterator =
					(PageIterator) getInfoObject(
							ProcessConstants.DE_SEARCH_PAGE_ITERATOR);
				queryBuilder =
					(DESearchQueryBuilder) getInfoObject(
							ProcessConstants.DE_SEARCH_QUERY_BUILDER);
				queryResults = (List) getInfoObject(ProcessConstants.ALL_DATA_ELEMENTS);

				CDECart cart = (CDECart)userSession.getAttribute(CaDSRConstants.CDE_CART);
				if (cart == null){
					cart = this.findCart(userSession);
				}
				String[] itemsList = getInfoStringArray(ProcessConstants.SELECT_DE);
				CDECartItem cdeItem = null;
				DataElement de = null;
				ValueDomain vd = null;

				for (int i = 0; i < itemsList.length; i++) {
					cdeItem = new CDECartItemTransferObject();
					de = CollectionUtil.locateDataElement(queryResults, itemsList[i]);
					vd = de.getValueDomain();          
					vd.setValidValues(valueHandler.getValidValues(vd.getVdIdseq().trim(), getSessionId()));
					cdeItem.setItem(de);
					cdeItem.setPersistedInd(false);
					cart.setDataElement(cdeItem);          
				}
				myRequest.setAttribute(ProcessConstants.CDE_CART_ADD_SUCCESS,(itemsList.length)+" Data Element(s) added to your CDE Cart Successfully.");
			}
			else if (performQuery.equals("newSearch")) {
				paramType = null;
				paramIdSeq = null;
				queryResults = null;
				paramRegStatus = null;
				desb = new DataElementSearchBean(myRequest);
				//TODO: After creating the bean it is set with default values : GF 18442
				desb.initSearchPreferences(dbUtil);
				// Set search preference  from old Search Bean to the new one
				setValuesFromOldSearchBean(desb);
				desb.setLOVLists(dbUtil);

				userSession.setAttribute(BrowserFormConstants.BROWSER_SEARCH_SCOPE, BrowserFormConstants.BROWSER_SEARCH_SCOPE_NEW);
				userSession.setAttribute("baseQuery", "");
				userSession.setAttribute("searchCrumb", "");

			}
			else if (performQuery.equals("sortResults")) {
				desb = (DataElementSearchBean) getInfoObject("desb");

				dePageIterator =
					(PageIterator) getInfoObject(
							ProcessConstants.DE_SEARCH_PAGE_ITERATOR);

				//dePageIterator.setCurrentPage(0);
				if (dePageIterator == null) {
					log.info("- Unable to retrieve PageIterator from session. Creating a new PageIterator..");
					dePageIterator = new BC4JPageIterator(100);
				}
				else {
					log.info("- Retrieved PageIterator from session successfully");
				}

				dePageIterator.setCurrentPage(0);

				queryBuilder =
					(DESearchQueryBuilder) getInfoObject(
							ProcessConstants.DE_SEARCH_QUERY_BUILDER);

				//update sort by column of queryBuilder
				String sortField = getStringInfo("sortField");
				if (!AppScanValidator.validateSearchParameterType(sortField))
					throw new Exception ("Invalidate parameter type");
				String sortOrderString = getStringInfo("sortOrder");
				int sortOrder = SortableColumnHeader.ASCENDING;
				if (getStringInfo("sortOrder") != null)
					sortOrder = Integer.valueOf(getStringInfo("sortOrder")).intValue();

				SortableColumnHeader sortColumnHeader = queryBuilder.getSortColumnHeader();
				//release 3.0, TT#1280, once user click on column header, the default
				// sort order will be wiped out
				if ( !sortField.equalsIgnoreCase(sortColumnHeader.getPrimary())||sortColumnHeader.isDefaultOrder()) {

					if (sortColumnHeader.isDefaultOrder()) {
						sortColumnHeader.setPrimary(sortField);
						sortColumnHeader.setSecondary(null);
						sortColumnHeader.setTertiary(null);
					} else {
						if(!sortField.equalsIgnoreCase(sortColumnHeader.getPrimary()))
						{
							String secondary = sortColumnHeader.getSecondary();
							sortColumnHeader.setSecondary(sortColumnHeader.getPrimary());
							sortColumnHeader.setPrimary(sortField);
							sortColumnHeader.setTertiary(secondary);
						}
						if(sortColumnHeader.getPrimary().equalsIgnoreCase(sortColumnHeader.getSecondary()))
						{
							sortColumnHeader.setSecondary(sortColumnHeader.getTertiary());
						}
						if (sortColumnHeader.getPrimary().equalsIgnoreCase(sortColumnHeader.getTertiary())) {
							sortColumnHeader.setTertiary(null);
						}
						else if (sortColumnHeader.getSecondary().equalsIgnoreCase(sortColumnHeader.getTertiary()))
						{
							sortColumnHeader.setTertiary(null);
						}
					}

					sortColumnHeader.setDefaultOrder(false);
				}
				sortColumnHeader.setOrder(sortOrder);

				log.trace("- Updated DESearchQueryBuilder successfully");

				queryStmt = queryBuilder.getSQLWithoutOrderBy();
				orderBy = queryBuilder.getOrderBy();

				queryParams = queryBuilder.getQueryParams();

				log.trace("- Query stmt is " + queryStmt);

				dh = (DataElementHandler) HandlerFactory.getHandler(DataElement.class);

				log.trace("Obtained DataElementHandler using HandlerFactory successfully");

				queryResults =
					dh.findDataElementsFromQueryClause(
							queryStmt, orderBy, getSessionId(), dePageIterator);

				log.trace("Query executed successfully");

				createPageScroller(dePageIterator, ProcessConstants.TOP_PAGE_SCROLLER, myRequest.getContextPath());
				createPageScroller(
						dePageIterator, ProcessConstants.BOTTOM_PAGE_SCROLLER, myRequest.getContextPath());

				myRequest.setAttribute(CaDSRConstants.ANCHOR, "results");
				log.trace("Created both page scrollers successfully");
			}
			setResult("desb", desb);
			setResult(ProcessConstants.DE_SEARCH_PAGE_ITERATOR, dePageIterator);
			setResult(ProcessConstants.DE_SEARCH_QUERY_BUILDER, queryBuilder);
			setResult(ProcessConstants.ALL_DATA_ELEMENTS, queryResults);
			setResult("tibSearchDE", tib);
			setResult("performQuery", null);
			setResult("P_PARAM_TYPE", paramType);
			setResult("P_IDSEQ", paramIdSeq);
			setResult("P_REGSTATUS", paramRegStatus);

			setCondition(SUCCESS);
		}
		catch (Exception ex) {
			UserErrorMessage uem;

			try {
				log.error("Exception caught", ex);
				tib = new TabInfoBean("cdebrowser_error_tabs");
				myRequest = (HttpServletRequest) getInfoObject("HTTPRequest");
				tib.processRequest(myRequest);

				if (tib.getMainTabNum() != 0) {
					tib.setMainTabNum(0);
				}

				uem = new UserErrorMessage();
				uem.setMsgOverview("Unexpected Application Error");
				uem.setMsgText(
				"An unexpected application error has occurred. Please re-try your search");
				uem.setMsgTechnical(
						"<b>System administrator:</b> Here is the stack " +
						"trace from the Exception.<BR><BR>" + ex.toString() + "<BR><BR>");
				setResult("tibSearchDE", tib);
				setResult("uem", uem);
				setCondition(FAILURE);
				dbUtil.returnConnection();
			}catch (TransitionConditionException tce) {
				reportException(tce, DEBUG);
			}catch (Exception e) {
				reportException(e, DEBUG);
			}
			reportException(ex, DEBUG);
			throw ex;
		}
		finally {
			if (dbUtil != null) {
				dbUtil.returnConnection();
			}
		}
	}

	protected TransitionCondition getPersistSuccessCondition() {
		return getCondition(SUCCESS);
	}

	protected TransitionCondition getPersistFailureCondition() {
		return getCondition(FAILURE);
	}

	private void initialize(HttpSession mySession) throws Exception {
		if (getStringInfo("INITIALIZED") == null) {
			CDEBrowserParams params = CDEBrowserParams.getInstance();
			//setResult("SBREXT_DSN", params.getSbrextDSN());
			//dsName = params.getSbrDSN();
			//setResult("SBR_DSN", params.getSbrDSN());
			setResult("XML_DOWNLOAD_DIR", params.getXMLDownloadDir());
			setResult("XML_PAGINATION_FLAG", params.getXMLPaginationFlag());
			setResult("XML_FILE_MAX_RECORDS", params.getXMLFileMaxRecords());
			setResult("TREE_URL", params.getTreeURL());
			setResult("INITIALIZED", "yes");
			try{
				CDECart cart = this.findCart(mySession);
				mySession.setAttribute(CaDSRConstants.CDE_CART, cart);
			}catch (Exception e){
				e.printStackTrace();
				throw e;
			}
		}
		else {
			setResult("SBREXT_DSN", getStringInfo("SBREXT_DSN"));
			setResult("SBR_DSN", getStringInfo("SBR_DSN"));
			dsName = getStringInfo("SBR_DSN");
			setResult("XML_DOWNLOAD_DIR", getStringInfo("XML_DOWNLOAD_DIR"));
			setResult("XML_PAGINATION_FLAG", getStringInfo("XML_PAGINATION_FLAG"));
			setResult("XML_FILE_MAX_RECORDS", getStringInfo("XML_FILE_MAX_RECORDS"));
			setResult("TREE_URL", getStringInfo("TREE_URL"));
			setResult("INITIALIZED", "yes");
		}
	}
	
	private void setDownloadLink(DBUtil dbUtil) {
		ResultSet rs = null;
		try {
			rs = dbUtil.executeQuery("select value from TOOL_OPTIONS_EXT where tool_name='CDEBrowser' and property='DOWNLOAD_LINK_WIKI'");
			if (rs.next()) {
				setResult(ProcessConstants.DOWNLOAD_LINK_WIKI, rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			if (rs!=null) try { rs.close(); } catch(Exception e){}
		}
	}

	public DBUtil getDBUtil() throws Exception {
		DBUtil dbUtil = null;

		if ((DBUtil) getInfoObject("dbUtil") == null) {
			try {
				dbUtil = new DBUtil();
				dbUtil.getConnectionFromContainer();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw ex;
			}
		}
		else {
			dbUtil = (DBUtil) getInfoObject("dbUtil");
			dbUtil.getConnectionFromContainer();
		}

		setResult("dbUtil", dbUtil);

		return dbUtil;
	}

	private void createPageScroller(
			PageIterator pageIterator,
			String location, String cPath) throws Exception {
		if (ProcessConstants.BOTTOM_PAGE_SCROLLER.equals(location)) {
			HTMLPageScroller dePageScroller =
				dePageScroller = new HTMLPageScroller(pageIterator, cPath);
			dePageScroller.setPageListName("dePages");
			dePageScroller.setExtraURLInfo("&performQuery=no");
			dePageScroller.generateHTML();
			setResult(ProcessConstants.DE_SEARCH_PAGE_SCROLLER, dePageScroller);
		}
		else if (ProcessConstants.TOP_PAGE_SCROLLER.equals(location)) {
			HTMLPageScroller dePageScroller =
				dePageScroller = new HTMLPageScroller(pageIterator, cPath);
			dePageScroller.setPageListName("dePagesTop");
			dePageScroller.setExtraURLInfo("&performQuery=no");
			dePageScroller.setOnChangeJSFunctionName("topListChanged");
			dePageScroller.generateHTML();
			setResult(ProcessConstants.DE_SEARCH_TOP_PAGE_SCROLLER, dePageScroller);
		}
	}

	private String getCurrentTimestamp() {
		Date timestamp = new Date();
		String ts = DateFormat.getDateTimeInstance().format(timestamp);

		return ts;
	}

	private CDECart findCart(HttpSession mySession) throws Exception{
		CDECart cart = (CDECart) mySession.getAttribute(CaDSRConstants.CDE_CART);	  
		NCIUser user = (NCIUser) mySession.getAttribute(CaDSRConstants.USER_KEY);
		String sessionId = mySession.getId();
		String uid = null ;
		try{
			if (cart == null) {
				//cart = new CDECartImpl(); 
				//ClientManager cManager = ClientManager.getInstance();	
				CDEBrowserParams params = CDEBrowserParams.getInstance();
				String ocURL = params.getObjectCartUrl();		      
				ObjectCartClient ocClient = null;

				if (!ocURL.equals(""))
					ocClient = new ObjectCartClient(ocURL);
				else
					ocClient = new ObjectCartClient();			  

				if(user!= null){
					uid = user.getUsername();	
				}	else {
					uid = "PublicUser" + sessionId;
					log.debug(" Public User Cart:  "+uid);
				}
				cart = new CDECartOCImpl(ocClient,uid,CaDSRConstants.CDE_CART);		  			  
			}
		}catch (ObjectCartException oce){
			log.error("Exception on GetDataElements", oce);
			throw oce;
		}
		return cart;
	}



	private void setValuesFromOldSearchBean(DataElementSearchBean desb) throws Exception
	{
		DataElementSearchBean oldDesb = (DataElementSearchBean) getInfoObject("desb");
		if(oldDesb!=null)
		{
			desb.setAslNameExcludeList(oldDesb.getAslNameExcludeList());
			desb.setExcludeTestContext(oldDesb.isExcludeTestContext());
			desb.setExcludeTrainingContext(oldDesb.isExcludeTrainingContext());
			desb.setRegStatusExcludeList(oldDesb.getRegStatusExcludeList());
		}else {
			log.debug("GetDataElements.java setValuesFromOldSearchBean oldDesb is null");
		}
	}
}
