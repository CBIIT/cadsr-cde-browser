package gov.nih.nci.cadsr.service.restControllers;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.cdecart.CdeCartUtil;
import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.error.AutheticationFailureException;
import gov.nih.nci.cadsr.service.model.search.SearchNode;

@RestController
@RequestMapping( "/cdeCart" )
public class CdeCartController
{

	private static Logger logger = LogManager.getLogger( CdeCartController.class.getName() );

	@Value("${cdeDataRestService}")
	String cdeDataRestServiceName;

	@Autowired
	CdeCartUtil cdeCartUtil;

	public void setCdeCartUti(CdeCartUtil cdeCartUtil) {
		this.cdeCartUtil = cdeCartUtil;
	}

	public CdeCartController() {
	}

	@RequestMapping( method = RequestMethod.GET )
	@ResponseBody
	public SearchNode[] retrieveObjectCart(HttpSession mySession) throws AutheticationFailureException
	{
		SearchNode[] results = null;
		String principalName = null;
		
		if (mySession != null) {
			principalName = (String) mySession.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME);
			logger.warn("In retrieveObjectCart found session for: " + principalName);
		}
		
		if (principalName == null) {
			logger.error("........No user found in session in retrieveObjectCart");
			throw new AutheticationFailureException("Authenticated user not found in the session operation retrieve CDE Cart");
		}
		
		logger.debug("Received rest call retrieve Object Cart for user: " + principalName);

		try {
			List<SearchNode> res = cdeCartUtil.findCartNodes(mySession, principalName);
			logger.debug("Sending OK response of rest call retrieve Object Cart; # of CDEs: " + res.size());			
			results = res.toArray(new SearchNode[res.size()]);
		} 
		catch (Exception e) {
			return createErrorNode("Server Error:\nretrieveObjectCart: " + principalName + " failed ", e);
		}
		return results;
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
		List<String> cdeIds = request.getBody();
		logger.debug("Received rest call save Object Cart, IDs: " + cdeIds);
		String principalName = null;

		if (mySession != null) {
			principalName = (String) mySession.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME);			
			logger.warn("In saveCart found session for: " + principalName);
		}
		
		if (principalName == null) {
			logger.error("........No user found in session in saveCart");
			throw new AutheticationFailureException("Authenticated user not found in the session operation save CDE Cart");
		}

		try {
			cdeCartUtil.addToCart(mySession, principalName, cdeIds);
			logger.debug("Returning rest call saveCart: OK");
			return new ResponseEntity<String>("Done", HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("saveCart error: ", e);
			return new ResponseEntity<String>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
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
	@RequestMapping(produces = "text/plain", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseEntity<String> deleteFromCart(HttpSession mySession, 
			@RequestParam("id") String[] idParams) throws AutheticationFailureException {
		String principalName = null;

		if (mySession != null) {
			principalName = (String) mySession.getAttribute(CaDSRConstants.LOGGEDIN_USER_NAME);	
			logger.warn("In deleteFromCart found session for: " + principalName);
		}
		//take the user from session
		if (principalName == null) {
			logger.error("........No user found in session in saveCart");
			throw new AutheticationFailureException("Authenticated user not found in the session operation save CDE Cart");
		}

		if ((idParams == null) || (idParams.length == 0)) {
			logger.debug("No ID received for delete returning rest call OK");
			return new ResponseEntity<String>("Done", HttpStatus.OK);
		}
		else if (logger.isDebugEnabled()) {
			logger.debug("ID received for delete from Cart: " + arrayToString(idParams));
		}
		
		try {
			//call delete from cart implementation
			cdeCartUtil.deleteFromCart(mySession, principalName, idParams);
			logger.debug("Returning rest call deleteFromCart OK");
			return new ResponseEntity<String>("Done", HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("Returning rest call deleteFromCart error: ", e);
			return new ResponseEntity<String>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
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
