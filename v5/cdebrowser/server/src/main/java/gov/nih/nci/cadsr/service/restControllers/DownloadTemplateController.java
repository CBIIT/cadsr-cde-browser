/*
 * Copyright (C) 2017 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.common.util.ParameterValidator;
import gov.nih.nci.cadsr.dao.ReferenceDocBlobDAO;
import gov.nih.nci.cadsr.dao.model.ReferenceDocBlobModel;
/**
 * This is a MVC RESTful controller to download Form Template based on provided Form IDSEQ.
 * 
 * @author asafievan
 *
 */

@RestController
@RequestMapping("/downloadTemplate")
public class DownloadTemplateController {
	private static Logger logger = LogManager.getLogger(DownloadTemplateController.class.getName());
	
	@Autowired
	private ReferenceDocBlobDAO referenceDocBlobDAO;
	@RequestMapping(value = "/refdocid/{acIdseq}", method = RequestMethod.GET, produces = "text/plain")
	public ResponseEntity<String> retrieveReferenceDocumentIdseq(@PathVariable("acIdseq") String acIdseq) throws Exception {
		logger.debug("Received RESTful call retrieveREferenceDocumentIdseq; acIdseq: " + acIdseq);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Type", "text/plain");
		if (ParameterValidator.validateIdSeq(acIdseq)) {
			try {
				String referenceDocIdseq = referenceDocBlobDAO.retrieveDownloadBlobIdseqByAcIdseq(acIdseq);
				if (referenceDocIdseq != null) {
					logger.debug("Sending referenceDocIdseq: " + referenceDocIdseq);
					return new ResponseEntity<String>(referenceDocIdseq, HttpStatus.OK);
				}
				else {
					logger.debug("Sending 'null' response NOT_FOUND for acIdseq: " + acIdseq);
					return new ResponseEntity<String>("null", responseHeaders, HttpStatus.NOT_FOUND);
				}
			}
			catch (Exception e) {
				String errorResult = String.format(SERVER_ERROR_RETRIEVING_REF_IDSEQ, acIdseq, e.getMessage());
				logger.error(errorResult, e);
				return new ResponseEntity<String>(errorResult, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else {
			String errorResult = String.format(CLIENT_ERROR_ID_FORMAT_WRONG, acIdseq);
			logger.error(errorResult);
			return new ResponseEntity<String>(errorResult, responseHeaders, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/doc/{rdIdseq}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> retrieveTemplateFile(@PathVariable("rdIdseq") String rdIdseq) throws Exception {
		logger.debug("Received RESTful call to Download Template; rdIdseq: " + rdIdseq);
		HttpHeaders responseHeaders = new HttpHeaders();
		if (ParameterValidator.validateIdSeq(rdIdseq)) {
			try {
				ReferenceDocBlobModel referenceDocBlobModel = referenceDocBlobDAO.retrieveReferenceDocBlobByRdIdseq(rdIdseq);
				if (referenceDocBlobModel != null) {
					responseHeaders.set("Content-Type", referenceDocBlobModel.getMimeType());
					responseHeaders.set("Content-Disposition", "attachment; filename=" + referenceDocBlobModel.getDocName());
					logger.debug("Sending Template stream for rdIdseq: " + rdIdseq);
					InputStream docStream = referenceDocBlobModel.getDocContent();
					InputStreamResource result = new InputStreamResource(docStream);
					return new ResponseEntity<InputStreamResource>(result, responseHeaders, HttpStatus.OK);
				}
				else {
					return buildErrorResponse(rdIdseq, CLIENT_ERROR_TEMPLATE_NOT_FOUND, responseHeaders, HttpStatus.NOT_FOUND);
				}
			}
			catch (Exception e) {			
				logger.error("Error in DownloadTemplateController retrieveTemplateFile rdIdseq: " + rdIdseq, e);
				return buildErrorResponse(rdIdseq, SERVER_ERROR_FORMATTED, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else {
			return buildErrorResponse(rdIdseq, CLIENT_ERROR_ID_FORMAT_WRONG, responseHeaders, HttpStatus.BAD_REQUEST);
		}
	}
	protected final static String CLIENT_ERROR_TEMPLATE_NOT_FOUND = "Client Error: Document Template not found searched by provided Reference Document IDSEQ: %s";
	protected final static String CLIENT_ERROR_ID_FORMAT_WRONG = "Client Error: Unexpected ID format: %s";
	protected final static String SERVER_ERROR_FORMATTED = "Error in DownloadTemplateController retrieving Document Template by provided Reference Document IDSEQ: %s";
	
	protected final static String SERVER_ERROR_RETRIEVING_REF_IDSEQ = "Error in DownloadTemplateController retrieving Reference Document by provided AC IDSEQ: %s, Message: %s";
	
	protected ResponseEntity<InputStreamResource> buildErrorResponse(String rdIdseq, String strMessageFormat, 
			HttpHeaders responseHeaders, HttpStatus httpStatus) {
		String errorMessage = String.format(strMessageFormat, rdIdseq);
		logger.error("Response error message: " + errorMessage);
		responseHeaders.set("Content-Type", "text/plain");
		InputStreamResource isr = new InputStreamResource(new ByteArrayInputStream(errorMessage.getBytes()));
		return new ResponseEntity<InputStreamResource>(isr, responseHeaders, httpStatus);
	}
	
	public void setReferenceDocBlobDAO(ReferenceDocBlobDAO referenceDocBlobDAO) {
		this.referenceDocBlobDAO = referenceDocBlobDAO;
	}

}
