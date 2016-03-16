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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.download.XmlDownloadTypes;
import gov.nih.nci.cadsr.download.GetXmlDownloadInterface;
import gov.nih.nci.cadsr.service.ClientException;
import gov.nih.nci.cadsr.service.ServerException;
/**
 * This is a MVC RESTful controller to download data to XML file based on provided CDE IDs.
 * 
 * @author asafievan
 *
 */

@RestController
@RequestMapping("/downloadXml")
public class DownloadXmlController {
	private Logger logger = LogManager.getLogger(DownloadXmlController.class.getName());
	@Autowired
	GetXmlDownloadInterface getXmlDownload;

	@Value("${downloadDirectory}")
	String downloadDirectory;
	@Value("${downloadFileNamePrefix}")
	String fileNamePrefix;
	@Value("${registrationAuthorityIdentifier}")
	String registrationAuthorityIdentifier;
	
	public void setGetXmlDownload(GetXmlDownloadInterface getXmlDownload) {
		this.getXmlDownload = getXmlDownload;
	}
	
	public void setDownloadDirectory(String downloadDirectory) {
		this.downloadDirectory = downloadDirectory;
	}

	public void setFileNamePrefix(String fileNamePrefix) {
		this.fileNamePrefix = fileNamePrefix;
	}

	@RequestMapping(produces = "text/plain", consumes = "application/json", method = RequestMethod.POST)
	public ResponseEntity<byte[]> downloadXml(@RequestParam("src") String source,
			RequestEntity<List<String>> request) throws Exception {
		logger.debug("Received rest call downloadXml \"src\": " + source);
		
		URI url = request.getUrl();

		String path = String.format("%s://%s:%d%s",url.getScheme(),  url.getHost(), url.getPort(), url.getPath());

		//String path = url.getPath();
		List<String> cdeIds = request.getBody();
		if (logger.isTraceEnabled())
			logger.trace("Requested list of IDs:" + cdeIds);
		
		validateDownloadParameters(cdeIds, source);
		// this is an example of Internal CDE ID we expect in here; shall come
		// from the request body
		// "B3445D55-ED6E-2584-E034-0003BA12F5E7"
		
		logger.debug("Received number of IDs downloadXml: " + cdeIds.size());

		String fileId = null;
		try {
			fileId = getXmlDownload.persist(cdeIds, registrationAuthorityIdentifier, source);
		} 
		catch (ClientException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ServerException("Download XML: error occured in building XML document", e);
		}
		
		if (fileId != null) {
			HttpHeaders responseHeaders = new HttpHeaders();
			String location = path + '/'+ fileId;
			logger.debug("Location header value: " + location);	
			responseHeaders.set("Location", location);
			return new ResponseEntity<byte[]>(fileId.getBytes(), responseHeaders, HttpStatus.CREATED);
		}
		else {
			throw new ServerException("Download XML: error occured in building XML document");
		}
	}
	
	@RequestMapping(value = "/{fileId}", produces = "application/vnd.cadsr+xml", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> retrieveXmlFile(@PathVariable("fileId") String fileId) throws Exception {
		logger.debug("Received RESTful call to retrieve XML file; fileId: " + fileId);
		String fileName = downloadDirectory + fileNamePrefix + fileId + ".xml";
		
		File file = new File (fileName);
		if (! file.exists()) {
			throw new ClientException("Download XML file is not found: " + fileName);
		}
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Content-Disposition", "attachment; filename=CDEBrowser_SearchResults.xml");
		try {
			InputStream inputStream = getFileAsInputStream(fileName);
			InputStreamResource isr = new InputStreamResource(inputStream);
			logger.debug("Sending XML file:" + fileName);
			return new ResponseEntity<InputStreamResource>(isr, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			throw new ServerException("Download XML: error occurred in XML document streaming", e);
		}
	
	}
	/**
	 * ExceptionHandler converts predefined exceptions to an HTTP Status code
	 */
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ClientException.class)
	public void sendBadRequest(Exception e) {
		logger.error("Sending XML Download client error: " + e);
	}

	/**
	 * ExceptionHandler converts a predefined exception to an HTTP Status code
	 */
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ServerException.class)
	public void sendServerError(Exception e) {
		logger.error("Sending XML Download server error: " + e);
	}

	/**
	 * ExceptionHandler converts a predefined exception to an HTTP Status code
	 */
	@ResponseStatus(value = HttpStatus.PAYLOAD_TOO_LARGE)
	@ExceptionHandler(OutOfMemoryError.class)
	public void sendOOMError(Exception e) {
		logger.error("Sending OOM XML Download server error");
	}
	
	protected BufferedInputStream getFileAsInputStream(String filename) throws Exception {
		BufferedInputStream bis = null;
		FileInputStream fis = new FileInputStream(filename);
		bis = new BufferedInputStream(fis);

		return bis;
	}
	
	protected void validateDownloadParameters(List<String> cdeIds, String source) throws ClientException {
		if (!XmlDownloadTypes.isDownloadTypeValid(source)) {
			throw new ClientException("Unexpected Download to XML type: " + source);
		}
		if ((cdeIds == null) || (cdeIds.isEmpty())) {//null does not happen in Spring MCV - when there is no IDs the framework does not call this service
			throw new ClientException("Expected Download CDE IDs are not provided");
		}
		//this requirement is deferred
//		if (cdeIds.size() > 1000) {//this Exception does not happen in Spring MCV - when the 
//			throw new ClientException("Download XML allowed amount of IDs exceed 1000 limit: " + cdeIds.size());
//		}
	}
}
