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

	@RequestMapping(value = "/{formId}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> retrieveTemplateFile(@PathVariable("formId") String formId) throws Exception {
		logger.debug("Received RESTful call to Download Template; formId: " + formId);
		HttpHeaders responseHeaders = new HttpHeaders();
		if (ParameterValidator.validateIdSeq(formId)) {
			try {
				ReferenceDocBlobModel referenceDocBlobModel = referenceDocBlobDAO.getReferenceDocBlobByAcIdseq(formId);
				if (referenceDocBlobModel != null) {
					responseHeaders.set("Content-Type", referenceDocBlobModel.getMimeType());
					responseHeaders.set("Content-Disposition", "attachment; filename=" + referenceDocBlobModel.getDocName());
					logger.debug("Sending Template stream for Form ID: " + formId);
					InputStream docStream = referenceDocBlobModel.getDocContent();
					InputStreamResource result = new InputStreamResource(docStream);
					return new ResponseEntity<InputStreamResource>(result, responseHeaders, HttpStatus.OK);
				}
				else {
					return buildErrorResponse(formId, CLIENT_ERROR_TEMPLATE_NOT_FOUND, responseHeaders, HttpStatus.NOT_FOUND);
				}
			}
			catch (Exception e) {
				logger.error("Error in DownloadTemplateController " + formId + ' ' + e);
				return buildErrorResponse(formId, SERVER_ERROR_FORMATTED, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else {
			return buildErrorResponse(formId, CLIENT_ERROR_ID_FORMAT_WRONG, responseHeaders, HttpStatus.BAD_REQUEST);
		}
	}
	protected final static String CLIENT_ERROR_TEMPLATE_NOT_FOUND = "Client Error: Template not found using provided Form ID: %s";
	protected final static String CLIENT_ERROR_ID_FORMAT_WRONG = "Client Error: Unexpected Form ID format: %s";
	protected final static String SERVER_ERROR_FORMATTED = "Error in DownloadTemplateController on provided Form ID: %s";

	protected ResponseEntity<InputStreamResource> buildErrorResponse(String formId, String strMessageFormat, 
			HttpHeaders responseHeaders, HttpStatus httpStatus) {
		String errorMessage = String.format(strMessageFormat, formId);
		logger.error("Response error message: " + errorMessage);
		responseHeaders.set("Content-Type", "text/plain");
		InputStreamResource isr = new InputStreamResource(new ByteArrayInputStream(errorMessage.getBytes()));
		return new ResponseEntity<InputStreamResource>(isr, responseHeaders, httpStatus);
	}
	
	public void setReferenceDocBlobDAO(ReferenceDocBlobDAO referenceDocBlobDAO) {
		this.referenceDocBlobDAO = referenceDocBlobDAO;
	}

}
