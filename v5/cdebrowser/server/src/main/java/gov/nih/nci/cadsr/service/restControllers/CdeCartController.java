package gov.nih.nci.cadsr.service.restControllers;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.cdecart.CdeCartUtilInterface;
import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.dao.DataElementDerivationDAO;
import gov.nih.nci.cadsr.error.AutheticationFailureException;
import gov.nih.nci.cadsr.service.model.search.SearchNode;

@RestController
@RequestMapping( "/cdeCart" )
public class CdeCartController
{

	private static Logger logger = LogManager.getLogger( CdeCartController.class.getName() );
	protected final static String CAUSED_BY_IN_MSG = "; caused by: ";
	protected final static String USER_IN_MSG = "; user: ";
	
	@Autowired
	CdeCartUtilInterface cdeCartUtil;
	@Autowired
	DataElementDerivationDAO dataElementDerivationDAO;
	
	
	public void setDataElementDerivationDAO(DataElementDerivationDAO dataElementDerivationDAO) {
		this.dataElementDerivationDAO = dataElementDerivationDAO;
	}

	public void setCdeCartUtil(CdeCartUtilInterface cdeCartUtil) {
		this.cdeCartUtil = cdeCartUtil;
	}

	public CdeCartController() {
	}
	//TODO remove
	@RequestMapping(method = RequestMethod.GET, value="/error", produces = "application/json")
	@ResponseBody
	public SearchNode[] retrieveObjectCartError(HttpSession mySession) throws AutheticationFailureException {
	
			return createErrorNode("Server Error:\nretrieveObjectCart: simulated error", new Exception("this is my test"));
	}
	//TODO remove
	@RequestMapping(method = RequestMethod.GET, value="/internal", produces = "test/plain")
	@ResponseBody
	public ResponseEntity retrieveObjectCartInternalError(HttpSession mySession) throws AutheticationFailureException {
	
		ResponseEntity responseEntity = new ResponseEntity("Test Internal Error", HttpStatus.INTERNAL_SERVER_ERROR);
		return responseEntity;
	}
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity retrieveObjectCartWithException(HttpSession mySession) throws AutheticationFailureException
	{
		SearchNode[] results = null;
		String principalName = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		if (mySession != null) {
			principalName = (String) mySession.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME);
			logger.warn("In retrieveObjectCart found session for: " + principalName);
		}
		
		if (principalName == null) {
			logger.error("........No user found in session in retrieveObjectCart");
			throw new AutheticationFailureException("Authenticated user not found in the session operation retrieve CDE Object Cart");
		}
		
		logger.debug("Received rest call retrieve Object Cart for user: " + principalName);
		ResponseEntity responseEntity;
		try {
			List<SearchNode> res = cdeCartUtil.findCartNodes(mySession, principalName);
			logger.debug("Sending OK response of rest call retrieve Object Cart; # of CDEs: " + res.size());
			results = res.toArray(new SearchNode[res.size()]);
			responseHeaders.set("Content-Type", "application/json");
			responseEntity = new ResponseEntity(results, responseHeaders, HttpStatus.OK);
		} 
		catch (Exception e) {
			String errorToClient = buildGetCartErrorMessage(e, principalName);
			logger.error("Sending INTERNAL_SERVER_ERROR response to rest call RETRIEVE Object Cart with errorToClient: " + errorToClient , e);
			responseHeaders.set("Content-Type", "text/plain");
			//CDEBROWSER-821 We return 500 error if we could not get user OC.
			responseEntity = new ResponseEntity(errorToClient, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
	//CDEBROWSER-821 We return 500 error if we could not get user OC.
	/**
	 * This function creates a string to send to user if OC GET error happened.
	 * 
	 * @param e original OC Exception
	 * @return String to send to CDE Browser client
	 */
	protected static String buildGetCartErrorMessage(final Exception e, String principalName) {
		StringBuilder sb = new StringBuilder();
		if (e != null) {
			sb.append(e.getMessage());
			sb.append(USER_IN_MSG).append(principalName);
			Throwable clause = e;
			while ((clause = clause.getCause()) != null) {
				sb.append(CAUSED_BY_IN_MSG).append(clause.getMessage());
			}
		}
		else {
			sb.append("Object Cart error");
			sb.append(USER_IN_MSG).append(principalName);
		}
		return sb.toString();
	}
    /**
     * This method expects only IDs which are added to the cart.
     * 
     * @param mySession
     * @param principal
     * @param request
     * @return ResponseEntity String
     * @throws AutheticationFailureException 
     */
	@RequestMapping(produces = "text/plain", consumes = "application/json", method = RequestMethod.POST)
	public ResponseEntity<String> saveCart(HttpSession mySession,
			RequestEntity<List<String>> request) throws AutheticationFailureException {
		String principalName = null;
		
		if (mySession != null) {
			principalName = (String) mySession.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME);			
			logger.warn("In saveCart found session for: " + principalName);
		}
		
		if (principalName == null) {
			logger.error("........No user found in session in saveCart");
			throw new AutheticationFailureException("Authenticated user not found in the session operation save CDE Object Cart");
		}
		
		List<String> cdeIds = request.getBody();

		if ((cdeIds == null) || (cdeIds.size() == 0)) {
			logger.debug("No ID received to add to Object Cart; returning rest call OK");
			return new ResponseEntity<String>("Done", HttpStatus.OK);
		}
		
		logger.info("ID received to save in Object Cart from user: " + principalName + ",  IDs amount: " + cdeIds.size());
		
		if (logger.isDebugEnabled()) {
			logger.debug("ID received to save in Object Cart from user: " + principalName + " in the amount: " + cdeIds.size() + cdeIds);
		}
		
		//CDEBROWSER-280 Add Derived from
		List<String> derivedFrom = dataElementDerivationDAO.getDataElementDerivationIdseqList(cdeIds);//This method never returns null
		if (!derivedFrom.isEmpty()) {
			logger.debug("...getDataElementDerivationIdseqList found CDEs: " + derivedFrom.size() + derivedFrom );
			cdeIds.addAll(derivedFrom);
			logger.info("IDs to save in Object Cart with Derived for user: " + principalName + ",  total IDs amount: " + cdeIds.size());
		}
		
		try {
			logger.info("Proceeding with REST call saveCart " + principalName);
			cdeCartUtil.addToCart(mySession, principalName, cdeIds);
			logger.info("Returning rest call saveCart: OK, user:" + principalName);
			return new ResponseEntity<String>("Done", HttpStatus.OK);
		} 
		catch (Exception e) {
			String errorToClient = buildGetCartErrorMessage(e, principalName);
			logger.error("Sending INTERNAL_SERVER_ERROR response to rest call SAVE Object Cart for " + principalName + " with errorToClient: " + errorToClient , e);
			return new ResponseEntity<String>(errorToClient, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	/**
	 * This method expects IDs to be deleted from the cart.
	 * 
	 * @param mySession
	 * @param principal
	 * @param request
	 * @return ResponseEntity String
	 * @throws AutheticationFailureException 
	 */
	@RequestMapping(value="/delete", produces = "text/plain", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> deleteFromCart(HttpSession mySession, 
			RequestEntity<List<String>> request) throws AutheticationFailureException {
		String principalName = null;

		if (mySession != null) {
			principalName = (String) mySession.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME);	
			logger.warn("In deleteFromCart found session for: " + principalName);
		}
		//take the user from session
		if (principalName == null) {
			logger.error("........No user found in session in saveCart");
			throw new AutheticationFailureException("Authenticated user not found in the session operation delete CDE Object Cart");
		}

		if (request == null) {
			logger.debug("No ID received for delete; returning rest call OK");
			return new ResponseEntity<String>("Done", HttpStatus.OK);
		}
		List<String> cdeIds = request.getBody();
		if ((cdeIds == null) || (cdeIds.size() == 0)) {
			logger.debug("No ID received for delete in request body; returning rest call OK");
			return new ResponseEntity<String>("Done", HttpStatus.OK);
		}		
		else if (logger.isDebugEnabled()) {
			logger.debug("ID received for delete from Cart in amount: " + cdeIds.size());
			logger.debug("ID received for delete from Cart: " + cdeIds);
		}
		
		try {
			//call delete from cart implementation
			String[] idItems = cdeIds.toArray(new String[cdeIds.size()]);
			cdeCartUtil.deleteCartNodes(mySession, principalName, idItems);
			logger.debug("Returning rest call deleteFromCart OK, user: " + principalName);
			return new ResponseEntity<String>("Done", HttpStatus.OK);
		} 
		catch (Exception e) {
			String errorToClient = buildGetCartErrorMessage(e, principalName);
			logger.error("Sending INTERNAL_SERVER_ERROR response to rest call DELETE Object Cart with errorToClient: " + errorToClient , e);
			return new ResponseEntity(errorToClient, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	protected String arrayToString(String[] idParams) {
		StringBuilder sb = new StringBuilder();
		if ((idParams != null) && (idParams.length != 0)) {
			for (int i = 0; i < idParams.length; i++)
				sb.append(idParams[i]).append(", ");
			String res = sb.toString();
			return res.substring(0, res.length() - 2);
		}
		return "";
	}
	public SearchNode[] createErrorNode( String text, Exception e )
    {
		logger.error( "createErrorNode exception: text" + text + ", e: " + e );
		return createErrorNode( text, e.getMessage() );
    }

    public SearchNode[] createErrorNode( String text )
    {
        return createErrorNode( text, "" );
    }

    public SearchNode[] createErrorNode( String text, String logString )
    {
        logger.error( "createErrorNode: " + text + "  " + logString );
        SearchNode[] errorNode = new SearchNode[1];
        errorNode[0] = new SearchNode();
        errorNode[0].setStatus( CaDSRConstants.ERROR );
        errorNode[0].setLongName( text );
        return errorNode;
    }

}
