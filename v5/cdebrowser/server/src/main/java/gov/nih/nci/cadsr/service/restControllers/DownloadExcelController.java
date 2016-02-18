/*
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.download.ExcelDownloadTypes;
import gov.nih.nci.cadsr.download.GetExcelDownload;
import gov.nih.nci.cadsr.service.ClientException;
import gov.nih.nci.cadsr.service.ServerException;
/**
 * This is a MVC RESTful controller to download data to Excel file based on provided CDE IDs.
 * 
 * @author asafievan
 *
 */

@RestController
@RequestMapping("/downloadExcel")
public class DownloadExcelController {
	private Logger logger = LogManager.getLogger(DownloadExcelController.class.getName());
	@Autowired
	GetExcelDownload getExcelDownload;

	@Value("${registrationAuthorityIdentifier}")
	String registrationAuthorityIdentifier;

	public void setGetExcelDownload(GetExcelDownload getExcelDownload) {
		this.getExcelDownload = getExcelDownload;
	}

	@RequestMapping(produces = "application/vnd.ms-excel", consumes = "application/json", method = RequestMethod.POST)
	public ResponseEntity<InputStreamResource> downloadExcel(@RequestParam("src") String source,
			@RequestBody List<String> cdeIds) throws Exception {
		logger.debug("Received rest call \"src\": " + source + ", " + cdeIds);
		if (!ExcelDownloadTypes.isDownloadTypeValid(source)) {
			throw new ClientException("Unexpected Download to Excel type: " + source);
		}
		if ((cdeIds == null) || (cdeIds.isEmpty())) {//null does not happen in Spring MCV - when there is no IDs the framework does not call this service
			throw new ClientException("Expected Download CDE IDs are not provided");
		}
		if (cdeIds.size() > 1000) {//this Exception does not happen in Spring MCV - when the 
			throw new ClientException("Download Excel allowed amount of IDs exceed 1000 limit: " + cdeIds.size());
		}
		// this is an example of Internal CDE ID we expect in here; shall come
		// from the request body
		// "B3445D55-ED6E-2584-E034-0003BA12F5E7"
		
		String excelFileName = null;
		try {
			excelFileName = getExcelDownload.persist(cdeIds, registrationAuthorityIdentifier, source);
		} catch (Exception e) {
			throw new ServerException("Download Excel: error occured in building Excel document", e);
		}
		
		if (excelFileName != null) {
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.set("Content-Disposition", "attachment; filename=CDEBrowser_SearchResults.xls");
			try {
				InputStream inputStream = getExcelFileAsInputStream(excelFileName);
				InputStreamResource isr = new InputStreamResource(inputStream);
				return new ResponseEntity<InputStreamResource>(isr, responseHeaders, HttpStatus.OK);
			} catch (Exception e) {
				throw new ServerException("Download Excel: error occured in Excel document streaming", e);
			}
		}
		else {
			throw new ServerException("Download Excel: error occured in building Excel document");			
		}
	}

	/**
	 * ExceptionHandler converts predefined exceptions to an HTTP Status code
	 */
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ClientException.class)
	public void sendBadRequest(Exception e) {
		logger.error("Sending Excel Download client error: " + e);
	}

	/**
	 * ExceptionHandler converts a predefined exception to an HTTP Status code
	 */
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ServerException.class)
	public void sendServerError(Exception e) {
		logger.error("Sending Excel Download server error: " + e);
	}

	protected BufferedInputStream getExcelFileAsInputStream(String excelFilename) throws Exception {
		BufferedInputStream bis = null;
		FileInputStream fis = new FileInputStream(excelFilename);
		bis = new BufferedInputStream(fis);

		return bis;
	}	
}
