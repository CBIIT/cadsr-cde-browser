package gov.nih.nci.cadsr.service.restControllers;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.security.Principal;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.cdecart.CdeCartUtil;
import gov.nih.nci.cadsr.common.CaDSRConstants;
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
    public SearchNode[] retrieveObjectCart(HttpSession mySession, Principal principal)
    {
		SearchNode[] results = null;
		String principalName = null;
		
		if (principal != null) {
			logger.warn("In retrieveObjectCart session for: " + principal.getName());
			principalName = principal.getName();
		}
		else{
			logger.error("........No principal received in retrieveObjectCart");
			//FIXME clean up this situation to get a user name
			//principalName = "ASAFIEVAN";
			principalName = "GUEST";
		}
		
		logger.debug("Received rest call retrieve Object Cart for user: " + principalName);

		try {
			List<SearchNode> res = cdeCartUtil.findCartNodes(mySession, principalName);
			results = res.toArray(new SearchNode[res.size()]);
		} 
		catch (Exception e) {
			return createErrorNode("Server Error:\nretrieveObjectCart: " + principalName + " failed ", e);
		}
		return results;
    }

	@RequestMapping(produces = "text/plain", consumes = "application/json", method = RequestMethod.POST)
	public ResponseEntity<String> saveCart(HttpSession mySession, Principal principal,
			RequestEntity<List<String>> request) {
		List<String> cdeIds = request.getBody();
		logger.debug("Received rest call save Object Cart, IDs: " + cdeIds);
		String principalName = null;

		if (principal != null) {
			logger.warn("In retrieveObjectCart session for: " + principal.getName());
			principalName = principal.getName();
		} 
		else {
			logger.error("........No principal received in retrieveObjectCart");
			// FIXME clean up this situation to get a user name
			//principalName = "ASAFIEVAN";
			principalName = "GUEST";
		}

		try {
			cdeCartUtil.addToCart(mySession, principalName, cdeIds);
			return new ResponseEntity<String>("Done", HttpStatus.OK);
		} 
		catch (Exception e) {
			logger.error("saveCart error: ", e.getMessage());
			e.printStackTrace();
			return new ResponseEntity<String>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
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
