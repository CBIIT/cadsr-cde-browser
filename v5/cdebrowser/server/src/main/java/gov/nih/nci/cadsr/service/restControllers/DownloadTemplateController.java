/*
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

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

import gov.nih.nci.cadsr.common.AppConfig;
import gov.nih.nci.cadsr.common.util.ParameterValidator;
import gov.nih.nci.cadsr.download.XmlDownloadTypes;
import gov.nih.nci.cadsr.service.ClientException;
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
	private AppConfig appConfig;

	/*@Value("${downloadDirectory}")
	String downloadDirectory;
	@Value("${downloadFileNamePrefix}")
	String fileNamePrefix;
	@Value("${registrationAuthorityIdentifier}")
	String registrationAuthorityIdentifier;*/
	//FIXME This is a placeholder implementation
	@RequestMapping(value = "/{formId}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> retrieveTemplateFile(@PathVariable("formId") String formId) throws Exception {
		logger.debug("Received RESTful call to Download Template; formId: " + formId);
		HttpHeaders responseHeaders = new HttpHeaders();
		if (ParameterValidator.validateIdSeq(formId)) {
			
			try {
				String testXml = "<?xml version=\"1.\0\" encoding=\"UTF-8\"?><test> this is DownloadTemplateController test</test>";
				InputStream inputStream = new ByteArrayInputStream(testXml.getBytes(Charset.defaultCharset()));
				InputStreamResource isr = new InputStreamResource(inputStream);
				responseHeaders.set("Content-Type", "text/xml");
				logger.debug("Sending Template stream for Form ID:" + formId);
				return new ResponseEntity<InputStreamResource>(isr, responseHeaders, HttpStatus.OK);
			}
			catch (Exception e) {
				String strMessage = "Error in DownloadTemplateController";
				logger.error(strMessage + ' ' + e);
				responseHeaders.set("Content-Type", "text/plain");
				InputStreamResource isr = new InputStreamResource(new ByteArrayInputStream(strMessage.getBytes()));
				return new ResponseEntity<InputStreamResource>(isr, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		
		}
		else {
			String strMessage = String.format("Client Error providing Form ID: %s", formId);
			logger.error(strMessage);
			logger.error("Template ID is not valid: " + formId);
			responseHeaders.set("Content-Type", "text/plain");
			InputStreamResource isr = new InputStreamResource(new ByteArrayInputStream(strMessage.getBytes()));
			return new ResponseEntity<InputStreamResource>(isr, responseHeaders, HttpStatus.BAD_REQUEST);
		}
	}

	public AppConfig getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(AppConfig appConfig) {
		this.appConfig = appConfig;
	}

}
