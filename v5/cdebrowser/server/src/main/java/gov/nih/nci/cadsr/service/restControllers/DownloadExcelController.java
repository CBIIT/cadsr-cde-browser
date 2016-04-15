/*
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ByteArrayInputStream;
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
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.download.ExcelDownloadTypes;
import gov.nih.nci.cadsr.download.GetExcelDownloadInterface;
import gov.nih.nci.cadsr.service.ClientException;
/**
 * This is a MVC RESTful controller to download data to Excel file based on provided CDE IDs.
 * 
 * @author asafievan
 *
 */

@RestController
@RequestMapping("/downloadExcel")
public class DownloadExcelController {
	private static Logger logger = LogManager.getLogger(DownloadExcelController.class.getName());
	@Autowired
	GetExcelDownloadInterface getExcelDownload;

	@Value("${registrationAuthorityIdentifier}")
	String registrationAuthorityIdentifier;
	@Value("${downloadDirectory}")
	String downloadDirectory;
	@Value("${downloadFileNamePrefix}")
	String fileNamePrefix;
	public static final String fileExtension = ".xls";
	
	//Client Error Texts
	public static final String clientErrorMessageFileNotFound = "Please contact the support group. Expected Excel file is not found on the server: '%s'.";
	public static final String clientErrorMessageWrongParam = "The expected ‘src’ parameter value is not correct. Please correct the value and try again. Received: '%s'.";
	public static final String clientErrorMessageNoIDs = "Please select CDEs in the search results and download again. Expected CDE IDs were not provided.";
	
	//Server Error Text
	public static final String serverErrorMessage = "Please contact the support group for the following Java error message: %s.";
	public static final String serverErrorMessageStreaming = "Please contact the support group. An error occurred in downloaded document streaming '%s'. Java error message: %s.";
	public static final String serverErrorBuildingDocument = "unable to build Excel document file.";
	

	public void setGetExcelDownload(GetExcelDownloadInterface getExcelDownload) {
		this.getExcelDownload = getExcelDownload;
	}
	
	public void setDownloadDirectory(String downloadDirectory) {
		this.downloadDirectory = downloadDirectory;
	}

	public void setFileNamePrefix(String fileNamePrefix) {
		this.fileNamePrefix = fileNamePrefix;
	}

	@RequestMapping(produces = "text/plain", consumes = "application/json", method = RequestMethod.POST)
	public ResponseEntity<String> downloadExcel(@RequestParam("src") String source,
			RequestEntity<List<String>> request) {
		logger.debug("Received rest call downloadExcel \"src\": " + source);
		
		URI url = request.getUrl();

		String path = String.format("%s://%s:%d%s",url.getScheme(),  url.getHost(), url.getPort(), url.getPath());

		//String path = url.getPath();
		List<String> cdeIds = request.getBody();
		
		String excelFileId = null;
		try {
			validateDownloadParameters(cdeIds, source);
			
			excelFileId = getExcelDownload.persist(cdeIds, registrationAuthorityIdentifier, source);
		
			if (excelFileId != null) {
				HttpHeaders responseHeaders = new HttpHeaders();
				String location = path + '/'+ excelFileId;
				logger.debug("Location header value: " + location);	
				responseHeaders.set("Location", location);
				//cdebrowser client expects file ID in the response body or an error message
				return new ResponseEntity<String>(excelFileId, responseHeaders, HttpStatus.CREATED);
			}
			else {
				String str = String.format(serverErrorMessage, serverErrorBuildingDocument);
				logger.error(str);
				return new ResponseEntity<String>(str , HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} 
		catch (ClientException e) {
			logger.error("Sending client error: " + e);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			logger.error(String.format(serverErrorMessage, e.toString()));
			return new ResponseEntity<String>(String.format(serverErrorMessage, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/{fileId}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> retrieveExcelFile(@PathVariable("fileId") String fileId) throws Exception {
		logger.debug("Received RESTful call to retrieve Excel file; fileId: " + fileId);
		String excelFileName = downloadDirectory + fileNamePrefix + fileId + fileExtension;
		HttpHeaders responseHeaders = new HttpHeaders();
		
		File file = new File (excelFileName);
		if (file.exists()) {
			responseHeaders.set("Content-Disposition", "attachment; filename=CDEBrowser_SearchResults" + fileExtension);
			
			try {
				InputStream inputStream = getExcelFileAsInputStream(excelFileName);
				InputStreamResource isr = new InputStreamResource(inputStream);
				responseHeaders.set("Content-Type", "application/vnd.ms-excel");
				logger.debug("Sending Excel file:" + excelFileName);
				return new ResponseEntity<InputStreamResource>(isr, responseHeaders, HttpStatus.OK);
			}
			catch (Exception e) {
				String strMessage = String.format(serverErrorMessageStreaming, excelFileName, e.getMessage());
				logger.error(strMessage + ' ' + e);
				responseHeaders.set("Content-Type", "text/plain");
				InputStreamResource isr = new InputStreamResource(new ByteArrayInputStream(strMessage.getBytes()));
				return new ResponseEntity<InputStreamResource>(isr, responseHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else {
			String strMessage = String.format(clientErrorMessageFileNotFound, fileId);
			logger.error(strMessage);
			responseHeaders.set("Content-Type", "text/plain");
			InputStreamResource isr = new InputStreamResource(new ByteArrayInputStream(strMessage.getBytes()));
			return new ResponseEntity<InputStreamResource>(isr, responseHeaders, HttpStatus.BAD_REQUEST);
		}
	}

	protected void validateDownloadParameters(List<String> cdeIds, String source) throws ClientException {
		if (!ExcelDownloadTypes.isDownloadTypeValid(source)) {
			throw new ClientException(String.format(clientErrorMessageWrongParam, source));
		}
		if ((cdeIds == null) || (cdeIds.isEmpty())) {//null does not happen in Spring MCV - when there is no IDs the framework does not call this service
			throw new ClientException(clientErrorMessageNoIDs);
		}
		// this is an example of Internal CDE ID we expect in here; shall come
		// from the request body
		// "B3445D55-ED6E-2584-E034-0003BA12F5E7"
		if (logger.isTraceEnabled())
			logger.trace("Requested list of IDs: " + cdeIds);
		
		logger.debug("Received number of IDs downloadXml: " + cdeIds.size());
	}

	protected BufferedInputStream getExcelFileAsInputStream(String excelFilename) throws Exception {
		BufferedInputStream bis = null;
		FileInputStream fis = new FileInputStream(excelFilename);
		bis = new BufferedInputStream(fis);

		return bis;
	}	
}
