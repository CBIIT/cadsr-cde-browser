/*
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.download.ExcelDownloadTypes;
import gov.nih.nci.cadsr.download.GetExcelDownloadInterface;
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
	GetExcelDownloadInterface getExcelDownload;

	@Value("${registrationAuthorityIdentifier}")
	String registrationAuthorityIdentifier;
	@Value("${downloadDirectory}")
	String downloadDirectory;
	@Value("${downloadFileNamePrefix}")
	String fileNamePrefix;

	public void setGetExcelDownload(GetExcelDownloadInterface getExcelDownload) {
		this.getExcelDownload = getExcelDownload;
	}

	@RequestMapping(produces = "text/plain", consumes = "application/json", method = RequestMethod.POST)
	public ResponseEntity<byte[]> downloadExcel(@RequestParam("src") String source,
			RequestEntity<List<String>> request) throws Exception {
		logger.debug("Received rest call downloadExcel \"src\": " + source);
		getExcelDownload.setFileNamePrefix(fileNamePrefix);
		getExcelDownload.setLocalDownloadDirectory(downloadDirectory);
		
		URI url = request.getUrl();

		String path = String.format("%s://%s:%d%s",url.getScheme(),  url.getHost(), url.getPort(), url.getPath());

		//String path = url.getPath();
		List<String> cdeIds = request.getBody();
		logger.debug("Requested list of IDs:" + cdeIds);
		
		validateDownloadParameters(cdeIds, source);
		// this is an example of Internal CDE ID we expect in here; shall come
		// from the request body
		// "B3445D55-ED6E-2584-E034-0003BA12F5E7"
		
		String excelFileId = null;
		try {
			excelFileId = getExcelDownload.persist(cdeIds, registrationAuthorityIdentifier, source);
		} catch (Exception e) {
			throw new ServerException("Download Excel: error occured in building Excel document", e);
		}
		
		if (excelFileId != null) {
			HttpHeaders responseHeaders = new HttpHeaders();
			String location = path + '/'+ excelFileId;
			logger.debug("Location header value: " + location);	
			responseHeaders.set("Location", location);
			return new ResponseEntity<byte[]>(excelFileId.getBytes(), responseHeaders, HttpStatus.CREATED);
		}
		else {
			throw new ServerException("Download Excel: error occured in building Excel document");
		}
	}
	
	@RequestMapping(value = "/{fileId}", produces = "application/vnd.ms-excel", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> retrieveExcelFile(@PathVariable("fileId") String fileId) throws Exception {
		logger.debug("Received RESTful call to retrieve Excel file; fileId: " + fileId);
		String excelFileName = downloadDirectory + fileNamePrefix + fileId + ".xls";
		
		File file = new File (excelFileName);
		if (! file.exists()) {
			throw new ClientException("Download Excel file is not found: " + excelFileName);
		}
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Disposition", "attachment; filename=CDEBrowser_SearchResults.xls");
		try {
			InputStream inputStream = getExcelFileAsInputStream(excelFileName);
			InputStreamResource isr = new InputStreamResource(inputStream);
			logger.debug("Sending excel file:" + excelFileName);
			return new ResponseEntity<InputStreamResource>(isr, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			throw new ServerException("Download Excel: error occurred in Excel document streaming", e);
		}
	
	}
	
	protected void validateDownloadParameters(List<String> cdeIds, String source) throws ClientException {
		if (!ExcelDownloadTypes.isDownloadTypeValid(source)) {
			throw new ClientException("Unexpected Download to Excel type: " + source);
		}
		if ((cdeIds == null) || (cdeIds.isEmpty())) {//null does not happen in Spring MCV - when there is no IDs the framework does not call this service
			throw new ClientException("Expected Download CDE IDs are not provided");
		}
		if (cdeIds.size() > 1000) {//this Exception does not happen in Spring MCV - when the 
			throw new ClientException("Download Excel allowed amount of IDs exceed 1000 limit: " + cdeIds.size());
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
