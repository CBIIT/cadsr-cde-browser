/*
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service.restControllers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.common.AppConfig;
import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.dao.ConceptDAO;
import gov.nih.nci.cadsr.dao.CsCsiValueMeaningDAO;
import gov.nih.nci.cadsr.dao.DataElementDAO;
import gov.nih.nci.cadsr.dao.DataElementDerivationDAO;
import gov.nih.nci.cadsr.dao.DefinitionDAO;
import gov.nih.nci.cadsr.dao.DesignationDAO;
import gov.nih.nci.cadsr.dao.ObjectClassConceptDAO;
import gov.nih.nci.cadsr.dao.PermissibleValuesDAO;
import gov.nih.nci.cadsr.dao.PropertyConceptDAO;
import gov.nih.nci.cadsr.dao.ReferenceDocDAO;
import gov.nih.nci.cadsr.dao.RepresentationConceptsDAO;
import gov.nih.nci.cadsr.dao.ToolOptionsDAO;
import gov.nih.nci.cadsr.dao.UserManagerDAO;
import gov.nih.nci.cadsr.dao.ValueDomainConceptDAO;
import gov.nih.nci.cadsr.dao.model.CSIRefDocModel;
import gov.nih.nci.cadsr.dao.model.CSRefDocModel;
import gov.nih.nci.cadsr.dao.model.ConceptDerivationRuleModel;
import gov.nih.nci.cadsr.dao.model.ConceptModel;
import gov.nih.nci.cadsr.dao.model.ContextModel;
import gov.nih.nci.cadsr.dao.model.CsCsiModel;
import gov.nih.nci.cadsr.dao.model.CsCsiValueMeaningModel;
import gov.nih.nci.cadsr.dao.model.CsCsiValueMeaningModelList;
import gov.nih.nci.cadsr.dao.model.DEOtherVersionsModel;
import gov.nih.nci.cadsr.dao.model.DataElementConceptModel;
import gov.nih.nci.cadsr.dao.model.DataElementDerivationComponentModel;
import gov.nih.nci.cadsr.dao.model.DataElementDerivationModel;
import gov.nih.nci.cadsr.dao.model.DataElementModel;
import gov.nih.nci.cadsr.dao.model.DefinitionModelAlt;
import gov.nih.nci.cadsr.dao.model.DesignationModel;
import gov.nih.nci.cadsr.dao.model.DesignationModelAlt;
import gov.nih.nci.cadsr.dao.model.ObjectClassModel;
import gov.nih.nci.cadsr.dao.model.PermissibleValuesModel;
import gov.nih.nci.cadsr.dao.model.PropertyModel;
import gov.nih.nci.cadsr.dao.model.ReferenceDocModel;
import gov.nih.nci.cadsr.dao.model.RepresentationModel;
import gov.nih.nci.cadsr.dao.model.ToolOptionsModel;
import gov.nih.nci.cadsr.dao.model.UsageModel;
import gov.nih.nci.cadsr.dao.model.ValueDomainModel;
import gov.nih.nci.cadsr.download.DownloadUtils;
import gov.nih.nci.cadsr.download.ExcelDownloadTypes;
import gov.nih.nci.cadsr.download.GetExcelDownloadInterface;
import gov.nih.nci.cadsr.download.GetExcelDownload.ColumnInfo;
import gov.nih.nci.cadsr.service.ClientException;
import gov.nih.nci.cadsr.service.model.cdeData.CdeDetails;
import gov.nih.nci.cadsr.service.model.cdeData.SelectedDataElement;
import gov.nih.nci.cadsr.service.model.cdeData.DataElementConcept.DataElementConcept;
import gov.nih.nci.cadsr.service.model.cdeData.DataElementConcept.DataElementConceptDetails;
import gov.nih.nci.cadsr.service.model.cdeData.DataElementConcept.ObjectClass;
import gov.nih.nci.cadsr.service.model.cdeData.DataElementConcept.Property;
import gov.nih.nci.cadsr.service.model.cdeData.adminInfo.AdminInfo;
import gov.nih.nci.cadsr.service.model.cdeData.classifications.Classifications;
import gov.nih.nci.cadsr.service.model.cdeData.classifications.ClassificationsSchemeItemReferenceDocument;
import gov.nih.nci.cadsr.service.model.cdeData.classifications.ClassificationsSchemeReferenceDocument;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.AlternateDefinition;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.AlternateName;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.CsCsi;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.DataElement;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.DataElementDetails;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.OtherVersion;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.ReferenceDocument;
import gov.nih.nci.cadsr.service.model.cdeData.dataElementDerivation.DataElementDerivation;
import gov.nih.nci.cadsr.service.model.cdeData.usage.FormUsage;
import gov.nih.nci.cadsr.service.model.cdeData.usage.Usage;
import gov.nih.nci.cadsr.service.model.cdeData.valueDomain.ClassificationSchemaAlternate;
import gov.nih.nci.cadsr.service.model.cdeData.valueDomain.PermissibleValueExt;
import gov.nih.nci.cadsr.service.model.cdeData.valueDomain.Representation;
import gov.nih.nci.cadsr.service.model.cdeData.valueDomain.ValueDomain;
import gov.nih.nci.cadsr.service.model.cdeData.valueDomain.ValueDomainDetails;
/**
 * RESTful controller to download CDE data to Excel file based on provided CDE IDs.
 *
 * @author santhanamv
 *
 */

@RestController
@RequestMapping("/downloadCdeCompare")
public class DownloadCompareExcelController {
	private static Logger logger = LogManager.getLogger(DownloadCompareExcelController.class.getName());
	@Autowired
	GetExcelDownloadInterface getExcelDownload;

	@Autowired
	AppConfig appConfig;

    @Autowired
    private DataElementDAO dataElementDAO;

    @Autowired
    private ReferenceDocDAO referenceDocDAO;

    @Autowired
    private PermissibleValuesDAO permissibleValuesDAO;

    @Autowired
    private RepresentationConceptsDAO representationConceptsDAO;

    @Autowired
    private DataElementDerivationDAO dataElementDerivationDAO;

    @Autowired
    private ObjectClassConceptDAO objectClassConceptDAO;

    @Autowired
    private PropertyConceptDAO propertyConceptDAO;

    @Autowired
    private ValueDomainConceptDAO valueDomainConceptDAO;

    @Autowired
    private ConceptDAO conceptDAO;

    @Autowired
    private ToolOptionsDAO toolOptionsDAO;

    @Autowired
    private CsCsiValueMeaningDAO csCsiValueMeaningDAO;

    @Autowired
    private DefinitionDAO definitionDAO;


    @Autowired
    private DesignationDAO designationDAO;

    @Autowired
    private UserManagerDAO userManagerDAO;

	/*@Value("${registrationAuthorityIdentifier}")
	String registrationAuthorityIdentifier;
	@Value("${downloadDirectory}")
	String downloadDirectory;
	@Value("${downloadFileNamePrefix}")
	String fileNamePrefix;*/
	public static final String fileExtension = ".xls";
	private static final int COLUMN_PER_CDE = 8;

//	@Value("${downloadDirectory}")
	String localDownloadDirectory = "/local/content/cdebrowser/output/";
//	@Value("${downloadFileNamePrefix}")
	String fileNamePrefix = "DataElements_";


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

/*	public void setLocalDownloadDirectory(String localDownloadDirectory) {
		this.localDownloadDirectory = localDownloadDirectory;
	}

	public void setFileNamePrefix(String excelFileNamePrefix) {
		this.fileNamePrefix = excelFileNamePrefix;
	}*/

	@RequestMapping(produces = "text/plain", consumes = "application/json", method = RequestMethod.POST)
	public ResponseEntity<String> downloadExcel(RequestEntity<List<String>> request) {
		logger.debug("Received rest call downloadCompareExcel");

		URI url = request.getUrl();

		String path = String.format("%s://%s:%d%s",url.getScheme(),  url.getHost(), url.getPort(), url.getPath());

		//String path = url.getPath();
		List<String> cdeIds = request.getBody();

		String excelFileId = null;
		try {
/*
		for (int i=0; i < cdeIds.size(); i++) {
			if (deIdSeq.equals("")) {
				deIdSeq = cdeIds.get(i);
					} else {
				deIdSeq = deIdSeq +  "," + cdeIds.get(i);
				}
			}*/
			CdeDetails[] cdeDetailsArray = downloadCdeCompareExcel(cdeIds);
			logger.debug("CDE details Array: " + cdeDetailsArray.length);
			excelFileId = persist(cdeDetailsArray);

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
		String excelFileName = appConfig.getDownloadDirectory() + appConfig.getFileNamePrefix() + fileId + fileExtension;
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
			logger.error("Excel file is not found: " + excelFileName);
			responseHeaders.set("Content-Type", "text/plain");
			InputStreamResource isr = new InputStreamResource(new ByteArrayInputStream(strMessage.getBytes()));
			return new ResponseEntity<InputStreamResource>(isr, responseHeaders, HttpStatus.BAD_REQUEST);
		}
	}


    /* (non-Javadoc)
	 * @see gov.nih.nci.cadsr.download.GetExcelDownloadInterface#persist(java.util.Collection)
	 */
	private String persist(CdeDetails[] cdeDetails) throws Exception {

		String excelFileSuffix;
		String excelFilename;
		excelFileSuffix = getExcelDownload.generateExcelFileId();
		//the Bean properties set up in the Spring context
		excelFilename = buildDownloadAbsoluteFileName(excelFileSuffix);
		//this function saves the file on the local drive
		generateExcelFile(excelFilename, cdeDetails);
		//the file name returned to the RESTful service to build the response
		return excelFileSuffix;
	}



	public String buildDownloadAbsoluteFileName(String excelFileSuffix) {
		String excelFilename = localDownloadDirectory + fileNamePrefix + excelFileSuffix + DownloadCompareExcelController.fileExtension;
		return excelFilename;
	}


	/**
	 * This method generates Excel file with the given file name.
	 *
	 * @param filename absolute file name
	 * @param itemIds internal IDs
	 * @param RAI
	 * @param source @see gov.nih.nci.cadsr.download.ExcelDownloadTypes
	 * @param colInfo List of ColumnInfo objects
	 * @throws Exception
	 */
	protected void generateExcelFile(
			final String filename,
			CdeDetails[] cdeDetails) throws Exception {

		HSSFWorkbook wb = null;
		FileOutputStream fileOut = null;

		try {
			//source tells what type of download do we do: "deSearch", "deCart", "deSearchPrior"
			wb = new HSSFWorkbook();

			HSSFSheet sheet = wb.createSheet();
			int rowNumber = 0;//NA main row number; we leave one empty row between main rows

			HSSFCellStyle boldCellStyle = wb.createCellStyle();
			HSSFFont font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			boldCellStyle.setFont(font);
			boldCellStyle.setAlignment(HSSFCellStyle.ALIGN_GENERAL);

			rowNumber++; //this is the row where we want to start data
			rowNumber = generateWorkbookCdeCompare(cdeDetails, sheet, rowNumber, cdeDetails.length, boldCellStyle);
			logger.error("Final File name:"+ filename);
			fileOut = new FileOutputStream(filename);

			wb.write(fileOut);

			fileOut.flush();
		}
		catch (Exception ex) {
			logger.error("Exception caught in Generate Excel File", ex);
			throw ex;
		}
		finally {
			if (wb != null) {
				try {
					wb.close();//NA does nothing but to avoid warning
				}
				catch (IOException e) {
					logger.debug("Unable to close Excel Workbook due to the following error ", e);
				}
			}

			if (fileOut != null) {
				try{
					fileOut.flush();
					fileOut.close();
				}
				catch (Exception e) {
					logger.debug("Unable to close temporarily file due to the following error ", e);
				}
			}
		}//finally
	}



	protected BufferedInputStream getExcelFileAsInputStream(String excelFilename) throws Exception {
		BufferedInputStream bis = null;
		FileInputStream fis = new FileInputStream(excelFilename);
		bis = new BufferedInputStream(fis);

		return bis;
	}

	public AppConfig getAppConfig() {
		return appConfig;
	}

	public void setAppConfig(AppConfig appConfig) {
		this.appConfig = appConfig;
	}

    @RequestMapping( value = "/CDEData1" )
    @ResponseBody
    public CdeDetails CDEDataController( @RequestParam( "deIdseq" ) String deIdseq )
    {
        logger.debug( "Received rest call \"CDEData\": " + deIdseq );

        DataElementModel dataElementModel = null;

        // Get the data model from the database
        try
        {
            dataElementModel = dataElementDAO.getCdeByDeIdseq( deIdseq );
        } catch( EmptyResultDataAccessException e )
        {
            e.printStackTrace();
        }

        CdeDetails cdeDetails = buildCdeDetails( dataElementModel );
        //CdeDetails cdeDetails = buildTestRecord();

        return cdeDetails;
    }

    /**
     * Accept a comma separated list of deIdseq values.
     * @param deIdseq Comma separated list of deIdseqs
     * @return Array of CdeDetails, to be used by the compare CDE feature.
     */
    //@RequestMapping( value = "/downloadCdeCompare" )
    @ResponseBody
    public CdeDetails[] downloadCdeCompareExcel( List<String> cdeIds )
    {
        int i = 0;
        DataElementModel dataElementModel;
        CdeDetails[] cdeDetailsArray = new CdeDetails[cdeIds.size()];
        if (dataElementDAO!=null) {
        	logger.debug("Not null");
        } else {
        	logger.debug("null");
        }
        for(String id: cdeIds)
        {
            dataElementModel = dataElementDAO.getCdeByDeIdseq( id.trim() );
            logger.debug("DataElement Model "+i+ " : "+ dataElementModel.toString() );
            cdeDetailsArray[i] = buildCdeDetailsForCompare( dataElementModel );
            i++;
        }
        return cdeDetailsArray;
    }

    /**
     * Accept a comma separated list of deIdseq values.
     * @param deIdseq Comma separated list of deIdseqs
     * @return Array of CdeDetails, to be used by the compare CDE feature.
     */
    public CdeDetails[] cdeCompareDownloadData(String deIdseq)
    {
        logger.debug( "cdeCompareDownloadData: " + deIdseq );
        String[] deIdseqs = deIdseq.split( "," );
        int i = 0;
        DataElementModel dataElementModel;
        CdeDetails[] cdeDetailsArray = new CdeDetails[deIdseqs.length];
        for(String id: deIdseqs)
        {
        	if (this.dataElementDAO!=null) {
        		dataElementModel = this.dataElementDAO.getCdeByDeIdseq( id.trim() );
                logger.debug( dataElementModel.toString() );
                cdeDetailsArray[i] = buildCdeDetailsForCompare( dataElementModel );
                i++;
        	}
        }
        return cdeDetailsArray;
    }


    // Build a CdeDetails to send to the client
    private CdeDetails buildCdeDetails( DataElementModel dataElementModel )
    {
        logger.debug( "buildCdeDetails" );
        CdeDetails cdeDetails = new CdeDetails();


        // For the "Data Element" Tab
        logger.debug( "\"Data Element\" Tab" );

        DataElement dataElement = initDataElementTabData( dataElementModel );
        cdeDetails.setDataElement( dataElement );

        // For the "Data Element Concept" Tab
        logger.debug( "\"Data Element Concept\" Tab" );

        DataElementConcept dataElementConcept = initDataElementConceptTabData( dataElementModel );
        cdeDetails.setDataElementConcept( dataElementConcept );

        // For the "Value Domain" Tab
        logger.debug( "\"Value Domain\" Tab" );
        ValueDomain valueDomain = initValueDomainTabData( dataElementModel );
        cdeDetails.setValueDomain( valueDomain );

        // For the "Classifications" Tab
        Classifications classifications = initClassificationsTabData( dataElementModel );
        cdeDetails.setClassifications( classifications );

        // For the "Usage" tab
        Usage usage = initUsageTabData( dataElementModel );
        cdeDetails.setUsage( usage );

        // For the "Data Elements Derivation" tab
        DataElementDerivation dataElementDerivation = initDataElementDerivationTabData( dataElementModel );
        cdeDetails.setDataElementDerivation( dataElementDerivation );

        // For the "Admin Info" tab
        AdminInfo adminInfo = initAdminInfoTabData( dataElementModel );
        cdeDetails.setAdminInfo( adminInfo );

        return cdeDetails;
    }

    /**
     * Gather data needed for CDE compare
     * This is a sub set of the data we return for All the tabs in the "CDE Details
     * @param dataElementModel
     * @return
     */
    private CdeDetails buildCdeDetailsForCompare ( DataElementModel dataElementModel )
    {
        CdeDetails cdeDetails = new CdeDetails();

        // For the "Data Element" section
        DataElement dataElement = initDataElementForCompare( dataElementModel );
        cdeDetails.setDataElement( dataElement );

        // For the "Data Element Concept" section
        DataElementConcept dataElementConcept = initDataElementConceptForCompare( dataElementModel );
        cdeDetails.setDataElementConcept( dataElementConcept );

        // For the "Value Domain" section
        ValueDomain valueDomain = initValueDomainForCompare( dataElementModel );
        cdeDetails.setValueDomain( valueDomain );

        // For the "Classifications" section
        Classifications classifications = initClassificationsForCompare( dataElementModel );
        cdeDetails.setClassifications( classifications );

        // For the "Data Elements Derivation" tab
        DataElementDerivation dataElementDerivation = initDataElementDerivationTabData( dataElementModel );
        cdeDetails.setDataElementDerivation( dataElementDerivation );

        return cdeDetails;
    }

    private DataElement initDataElementForCompare(DataElementModel dataElementModel)
    {
        DataElement dataElement = getDataElementDetails( dataElementModel );
        /////////////////////////////////////////////////////
        // "Reference Documents" of the "Data Element" Tab

        // List to populate for client side
        getDataElementReferenceDocuments( dataElementModel, dataElement );
        return dataElement;
    }


	/**
	 * This method populates provided HSSFSheet.
	 * It loops through the provided ResultSet, but it does not close it. This is a job of the calling method.
	 *
	 * @param rs ResultSet to retrieve DB CDE Compare data for Excel
	 * @param sheet HSSFSheet created by the calling method which will be populated with DB data
	 * @param rowNumber the row number in Excel table which shall be the first one created by this method
	 * @param source
	 * @param colInfo
	 * @throws Exception
	 */
	protected int generateWorkbookCdeCompare(CdeDetails[] cdeDetails,
		HSSFSheet sheet, int rowNumber,
		int amountOfIds, HSSFCellStyle boldCellStyle) throws Exception
	{
		// Create a row and put some cells in it. Rows are 0 based.
		HSSFRow row = sheet.createRow(rowNumber++);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue("Data Element");
		cell.setCellStyle(boldCellStyle);

		List cdeList = new ArrayList();

		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElement().getDataElementDetails().getPublicId());
		}
		addNewRow(sheet, rowNumber++, "Public ID", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElement().getDataElementDetails().getVersion());
		}
		addNewRow(sheet, rowNumber++, "Version", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElement().getDataElementDetails().getLongName());
		}
		addNewRow(sheet, rowNumber++, "Long Name", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElement().getDataElementDetails().getShortName());
		}
		addNewRow(sheet, rowNumber++, "Short Name", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElement().getDataElementDetails().getPreferredQuestionText());
		}
		addNewRow(sheet, rowNumber++, "Preferred Question Text", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElement().getDataElementDetails().getDefinition());
		}
		addNewRow(sheet, rowNumber++, "Definition", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElement().getDataElementDetails().getValueDomain().toString());
		}
		addNewRow(sheet, rowNumber++, "Value Domain", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElement().getDataElementDetails().getDataElementConcept().toString());
		}
		addNewRow(sheet, rowNumber++, "Data Element Concept", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElement().getDataElementDetails().getContext());
		}
		addNewRow(sheet, rowNumber++, "Context", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElement().getDataElementDetails().getWorkflowStatus());
		}
		addNewRow(sheet, rowNumber++, "Workflow Status", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElement().getDataElementDetails().getOrigin());
		}
		addNewRow(sheet, rowNumber++, "Origin", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElement().getDataElementDetails().getRegistrationStatus());
		}
		addNewRow(sheet, rowNumber++, "Registration Status", boldCellStyle, cdeList);
		cdeList = new ArrayList();



		row = sheet.createRow(rowNumber++);
		row = sheet.createRow(rowNumber++);
		cell = row.createCell(0);
		cell.setCellValue("Data Element Concept");
		cell.setCellStyle(boldCellStyle);


		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElementConcept().getDataElementConceptDetails().getPublicId());
		}
		addNewRow(sheet, rowNumber++, "Public Id", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElementConcept().getDataElementConceptDetails().getVersion());
		}
		addNewRow(sheet, rowNumber++, "Version", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElementConcept().getDataElementConceptDetails().getLongName());
		}
		addNewRow(sheet, rowNumber++, "Long Name", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElementConcept().getDataElementConceptDetails().getShortName());
		}
		addNewRow(sheet, rowNumber++, "Short Name", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElementConcept().getDataElementConceptDetails().getDefinition());
		}
		addNewRow(sheet, rowNumber++, "Definition", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElementConcept().getDataElementConceptDetails().getContext());
		}
		addNewRow(sheet, rowNumber++, "Context", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElementConcept().getDataElementConceptDetails().getWorkflowStatus());
		}
		addNewRow(sheet, rowNumber++, "Workflow Status", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElementConcept().getDataElementConceptDetails().getConceptualDomainPublicId());
		}
		addNewRow(sheet, rowNumber++, "Conceptual Domain Public Id", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElementConcept().getDataElementConceptDetails().getConceptualDomainShortName());
		}
		addNewRow(sheet, rowNumber++, "Conceptual Domain Short Name", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElementConcept().getDataElementConceptDetails().getConceptualDomainContextName());
		}
		addNewRow(sheet, rowNumber++, "Conceptual Domain Context Name", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElementConcept().getDataElementConceptDetails().getConceptualDomainVersion());
		}
		addNewRow(sheet, rowNumber++, "Conceptual Domain Version", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getDataElementConcept().getDataElementConceptDetails().getOrigin());
		}
		addNewRow(sheet, rowNumber++, "Origin", boldCellStyle, cdeList);
		cdeList = new ArrayList();


		row = sheet.createRow(rowNumber++);
		row = sheet.createRow(rowNumber++);


		List refDocPropertyTitles = new ArrayList();
		refDocPropertyTitles.add(0, "Document Name");
		refDocPropertyTitles.add(1, "Document Type");
		refDocPropertyTitles.add(2, "Document Text");
		List refDocProperties = new ArrayList();
		refDocProperties.add(0, "docName");
		refDocProperties.add(1, "docType");
		refDocProperties.add(2, "docText");

		rowNumber += this.exportObjects(sheet,   rowNumber,        "Reference Document(s)", "referenceDocs", boldCellStyle,
				cdeDetails, refDocProperties, refDocPropertyTitles);

		row = sheet.createRow(rowNumber++);
		row = sheet.createRow(rowNumber++);
		cell = row.createCell(0);
		cell.setCellValue("Value Domain");
		cell.setCellStyle(boldCellStyle);

		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getValueDomain().getValueDomainDetails().getPublicId());
		}
		addNewRow(sheet, rowNumber++, "Public ID", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getValueDomain().getValueDomainDetails().getVersion());
		}
		addNewRow(sheet, rowNumber++, "Version", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getValueDomain().getValueDomainDetails().getLongName());
		}
		addNewRow(sheet, rowNumber++, "Long Name", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getValueDomain().getValueDomainDetails().getShortName());
		}
		addNewRow(sheet, rowNumber++, "Short Name", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getValueDomain().getValueDomainDetails().getContext());
		}
		addNewRow(sheet, rowNumber++, "Context", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getValueDomain().getValueDomainDetails().getDefinition());
		}
		addNewRow(sheet, rowNumber++, "Definition", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getValueDomain().getValueDomainDetails().getWorkflowStatus());
		}
		addNewRow(sheet, rowNumber++, "Workflow Status", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getValueDomain().getValueDomainDetails().getDataType());
		}
		addNewRow(sheet, rowNumber++, "Data Type", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getValueDomain().getValueDomainDetails().getUnitOfMeasure());
		}
		addNewRow(sheet, rowNumber++, "Unit of Measure", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getValueDomain().getValueDomainDetails().getDisplayFormat());
		}
		addNewRow(sheet, rowNumber++, "Display Format", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getValueDomain().getValueDomainDetails().getMaximumLength());
		}
		addNewRow(sheet, rowNumber++, "Maximum Length", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getValueDomain().getValueDomainDetails().getMinimumLength());
		}
		addNewRow(sheet, rowNumber++, "Minimum Length", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getValueDomain().getValueDomainDetails().getDecimalPlace());
		}
		addNewRow(sheet, rowNumber++, "Decimal Place", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getValueDomain().getValueDomainDetails().getHighValue());
		}
		addNewRow(sheet, rowNumber++, "High Value", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getValueDomain().getValueDomainDetails().getLowValue());
		}
		addNewRow(sheet, rowNumber++, "Low Value", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getValueDomain().getValueDomainDetails().getValueDomainType());
		}
		addNewRow(sheet, rowNumber++, "Value Domain Type", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getValueDomain().getValueDomainDetails().getConceptualDomainShortName());
		}
		addNewRow(sheet, rowNumber++, "Conceptual Domain Short Name", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getValueDomain().getValueDomainDetails().getConceptualDomainContextName());
		}
		addNewRow(sheet, rowNumber++, "Conceptual Domain Context Name", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getValueDomain().getValueDomainDetails().getConceptualDomainVersion());
		}
		addNewRow(sheet, rowNumber++, "Conceptual Domain Version", boldCellStyle, cdeList);
		cdeList = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			cdeList.add(cdeDetails[i].getValueDomain().getValueDomainDetails().getOrigin());
		}
		addNewRow(sheet, rowNumber++, "Origin", boldCellStyle, cdeList);


		row = sheet.createRow(rowNumber++);
		row = sheet.createRow(rowNumber++);

		List pvTitles = new ArrayList();
		pvTitles.add(0, "PV");
		pvTitles.add(1, "PV Meaning");
		pvTitles.add(2, "PV Meaning Concept Codes");
		pvTitles.add(3, "PV Meaning Description");
		pvTitles.add(4, "PV Begin Date");
		pvTitles.add(5, "PV End Date");
		pvTitles.add(6, "VM Public ID");
		pvTitles.add(7, "VM Version");
		List pvProperties = new ArrayList();
		pvProperties.add(0, "PV");
		pvProperties.add(1, "PVMeaning");
		pvProperties.add(2, "PVMeaningConceptCodes");
		pvProperties.add(3, "PVMeaningDescription");
		pvProperties.add(4, "PVBeginDate");
		pvProperties.add(5, "PVEndDate");
		pvProperties.add(6, "VMPublicID");
		pvProperties.add(7, "VMVersion");

		rowNumber += this.exportObjects(sheet, rowNumber, "Permissible Value(s)",
		"permissible-values", boldCellStyle,
		cdeDetails, pvProperties, pvTitles);


		List csPropertyTitles = new ArrayList();
		csPropertyTitles.add(0, "CS* Short Name");
		csPropertyTitles.add(1, "CS* Definition");
		csPropertyTitles.add(2, "CSI* Name");
		csPropertyTitles.add(3, "CSI* Type");
		List csProperties = new ArrayList();
		csProperties.add(0, "classSchemeName");
		csProperties.add(1, "classSchemeDefinition");
		csProperties.add(2, "classSchemeItemName");
		csProperties.add(3, "classSchemeItemType");

		rowNumber += this.exportObjects(sheet, rowNumber, "Classification(s)", "classifications", boldCellStyle,
				cdeDetails, csProperties, csPropertyTitles);

		row = sheet.createRow(rowNumber++);
		row = sheet.createRow(rowNumber++);
		cell = row.createCell(0);
		cell.setCellValue("Data Element Derivation - Derivation Details");
		cell.setCellStyle(boldCellStyle);

		List cde1List = new ArrayList();
		List cde2List = new ArrayList();
		List cde3List = new ArrayList();
		List cde4List = new ArrayList();
		for (int i = 0; i < cdeDetails.length; i++) {
			if (cdeDetails[i].getDataElementDerivation().getDataElementDerivationDetails()!=null) {
				cde1List.add(cdeDetails[i].getDataElementDerivation().getDataElementDerivationDetails().getDerivationType());
				cde2List.add(cdeDetails[i].getDataElementDerivation().getDataElementDerivationDetails().getRule());
				cde3List.add(cdeDetails[i].getDataElementDerivation().getDataElementDerivationDetails().getMethod());
				cde4List.add(cdeDetails[i].getDataElementDerivation().getDataElementDerivationDetails().getConcatenationCharacter());
			}

		}
		addNewRow(sheet, rowNumber++, "Derivation Type", boldCellStyle, cde1List);
		addNewRow(sheet, rowNumber++, "Rule", boldCellStyle, cde2List);
		addNewRow(sheet, rowNumber++, "Method", boldCellStyle, cde3List);
		addNewRow(sheet, rowNumber++, "Concatenation Character", boldCellStyle, cde4List);


		List dedPropertyTitles = new ArrayList();
		dedPropertyTitles.add(0, "Display Order");
		dedPropertyTitles.add(1, "Long Name");
		dedPropertyTitles.add(2, "Context");
		dedPropertyTitles.add(3, "Workflow Status");
		dedPropertyTitles.add(4, "Public ID");
		dedPropertyTitles.add(5, "Version");
		List dedProperties = new ArrayList();
		dedProperties.add(0, "displayOrder");
		dedProperties.add(1, "longName");
		dedProperties.add(2, "contextName");
		dedProperties.add(3, "CDEId");
		dedProperties.add(4, "workflowStatus");
		dedProperties.add(5, "version");

		cell = row.createCell(0);
		rowNumber += this.exportObjects(sheet, rowNumber, "Data Element Derivation - Component Data Elements",
		"de-cde", boldCellStyle,
		cdeDetails, dedProperties, dedPropertyTitles);
		return rowNumber;
	}

private void addNewRow(HSSFSheet sheet, int rowNumber, String title, HSSFCellStyle titleStyle, List cdeList) {
		HSSFRow row = sheet.createRow(rowNumber++);

		int colNumber = 0;
	HSSFCell cell = row.createCell(colNumber++);
	cell.setCellValue(title);
	cell.setCellStyle(titleStyle);
	logger.debug("cdeList size : "+cdeList.size());
	try {
			for (int i = 0; i < cdeList.size(); i++) {
				cell = row.createCell(colNumber);
				if (cdeList.get(i)!=null) {
					logger.debug("cdeList item "+i+" : "+(cdeList.get(i)).toString());
					cell.setCellValue((cdeList.get(i)).toString());
				} else {
					cell.setCellValue("");
				}
				colNumber += COLUMN_PER_CDE;
			}
		} catch (Exception ie) {
			cell.setCellValue("");
		}
}

private int exportObjects(HSSFSheet sheet,          int rowNumber, String title,      String propertyName,
        HSSFCellStyle titleStyle, CdeDetails[] cdeDetails,    List propertyList, List titleList) {
//this row contains the number of valid values for each data element value domain
HSSFRow row = sheet.createRow(rowNumber++);

int colNumber = 1;
int maxValueNumber = 0;
HSSFCell cell;

row = sheet.createRow(rowNumber++);
row = sheet.createRow(rowNumber++);
cell = row.createCell(0);
cell.setCellValue(title);
cell.setCellStyle(titleStyle);

for (int i = 0; i < cdeDetails.length; i++) {
cell = row.createCell(colNumber);

try {
int validValueSize = 0;
if (propertyName.equalsIgnoreCase("referenceDocs")) {
	if (cdeDetails[i].getDataElement().getReferenceDocuments() != null) {
		validValueSize = ((List)cdeDetails[i].getDataElement().getReferenceDocuments()).size();
	}
} else if (propertyName.equalsIgnoreCase("permissible-values")) {
	if (cdeDetails[i].getValueDomain().getPermissibleValues() != null) {
		validValueSize = ((List)cdeDetails[i].getValueDomain().getPermissibleValues()).size();
	}
} else if (propertyName.equalsIgnoreCase("classifications")) {
	if (cdeDetails[i].getClassifications().getClassificationList() != null) {
		validValueSize = ((List)cdeDetails[i].getClassifications().getClassificationList()).size();
	}
} else if (propertyName.equalsIgnoreCase("de-cde")) {
	if (cdeDetails[i].getDataElementDerivation().getDataElementDerivationComponentModels() != null) {
		validValueSize = ((List)cdeDetails[i].getDataElementDerivation().getDataElementDerivationComponentModels()).size();
	}
}

if (validValueSize > maxValueNumber)
maxValueNumber = validValueSize;

	cell.setCellValue(validValueSize + " " + title);
} catch (Exception e) {
//cell.setCellValue("");
}

colNumber += COLUMN_PER_CDE;
}

if (maxValueNumber > 0) {
//this row contains valid value properties
row = sheet.createRow(rowNumber++);

colNumber = 1;

for (int i = 0; i < cdeDetails.length; i++) {
colNumber = i * COLUMN_PER_CDE + 1;

for (int titleIdx = 0; titleIdx < titleList.size(); titleIdx++) {
cell = row.createCell(colNumber++);

cell.setCellValue(titleList.get(titleIdx).toString());
cell.setCellStyle(titleStyle);
}
}

for (int i = 0; i < maxValueNumber; i++) {
colNumber = 1;

row = sheet.createRow(rowNumber++);

for (int j = 0; j < cdeDetails.length; j++) {
List valueList = null;

try {

	if (propertyName.equalsIgnoreCase("referenceDocs")) {
		valueList = (List)cdeDetails[j].getDataElement().getReferenceDocuments();
	} else if (propertyName.equalsIgnoreCase("permissible-values")) {
		valueList = (List)cdeDetails[j].getValueDomain().getPermissibleValues();
	} else if (propertyName.equalsIgnoreCase("classifications")) {
		valueList = (List)cdeDetails[j].getClassifications().getClassificationList();
	} else if (propertyName.equalsIgnoreCase("de-cde")) {
		valueList = (List)cdeDetails[j].getDataElementDerivation().getDataElementDerivationComponentModels();
	}
} catch (Exception e) { }

if (valueList != null && valueList.size() > i) {
colNumber = j * COLUMN_PER_CDE + 1;

for (int pIdx = 0; pIdx < propertyList.size(); pIdx++) {
cell = row.createCell(colNumber++);

try {
	List cdeObjList = new ArrayList();
	cdeObjList.add(valueList.get(i));
	String cellValue = getProperty(propertyName ,(String)propertyList.get(pIdx), cdeObjList);
cell.setCellValue(cellValue);
} catch (Exception e) {
logger.debug("Exception in excel exportRow"+e.getMessage());
e.printStackTrace();
cell.setCellValue("");
}
} //end of writing one object
}  //end if object not null
}   //end of one row
}    //if there is any row
}     // end of all rows

return (4 + maxValueNumber);
}

private String getProperty (String propertyName, String property, List cdeObjList) {
	String cellValue = "";
	if (propertyName.equalsIgnoreCase("referenceDocs")) {
		ReferenceDocument referenceDoc = (ReferenceDocument)cdeObjList.get(0);
		if (property.equalsIgnoreCase("docName")) {
			cellValue = referenceDoc.getDocumentName();
		} else if (property.equalsIgnoreCase("docType")) {
			cellValue = referenceDoc.getDocumentType();
		} else if (property.equalsIgnoreCase("docText")) {
			cellValue = referenceDoc.getDocumentText();
		}
	} else if (propertyName.equalsIgnoreCase("permissible-values")) {
		PermissibleValuesModel pvModel = (PermissibleValuesModel)cdeObjList.get(0);
		if (property.equalsIgnoreCase("PV")) {
			cellValue = pvModel.getValue();
		} else if (property.equalsIgnoreCase("PVMeaning")) {
			cellValue = pvModel.getShortMeaning();
		} else if (property.equalsIgnoreCase("PVMeaningConceptCodes")) {
			cellValue = pvModel.getConceptCode();
		} else if (property.equalsIgnoreCase("PVMeaningDescription")) {
			cellValue = pvModel.getVmDescription();
		} else if (property.equalsIgnoreCase("PVBeginDate")) {
			cellValue = pvModel.getBeginDateString();
		} else if (property.equalsIgnoreCase("PVEndDate")) {
			cellValue = pvModel.getEndDateString();
		} else if (property.equalsIgnoreCase("VMPublicID")) {
			cellValue = pvModel.getVmId();
		} else if (property.equalsIgnoreCase("VMVersion")) {
			cellValue = pvModel.getVmVersion();
		}
	} else if (propertyName.equalsIgnoreCase("classifications")) {
		CsCsi csCsi = (CsCsi)cdeObjList.get(0);
		if (property.equalsIgnoreCase("classSchemeName")) {
			cellValue = csCsi.getCsLongName();
		} else if (property.equalsIgnoreCase("classSchemeDefinition")) {
			cellValue = csCsi.getCsDefinition();
		} else if (property.equalsIgnoreCase("classSchemeItemName")) {
			cellValue = csCsi.getCsiName();
		} else if (property.equalsIgnoreCase("classSchemeItemType")) {
			cellValue = csCsi.getCsiType();
		}
	} else if (propertyName.equalsIgnoreCase("de-cde")) {
		DataElementDerivationComponentModel deDerivedModel = (DataElementDerivationComponentModel)cdeObjList.get(0);
		if (property.equalsIgnoreCase("displayOrder")) {
			cellValue = deDerivedModel.getDisplayOrder();
		} else  if (property.equalsIgnoreCase("longName")) {
			cellValue = deDerivedModel.getLongName();
		} else  if (property.equalsIgnoreCase("contextName")) {
			cellValue = deDerivedModel.getContext();
		} else  if (property.equalsIgnoreCase("workflowStatus")) {
			cellValue = deDerivedModel.getWorkflowStatus();
		} else  if (property.equalsIgnoreCase("CDEId")) {
			cellValue = deDerivedModel.getPublicId();
		} else  if (property.equalsIgnoreCase("version")) {
			cellValue = deDerivedModel.getVersion();
		}
	}
	return cellValue;
}

    /**********************************************************************/
    /**********************************************************************/
    /**
     * For the "Data Element" Tab
     *
     * @param dataElementModel The data model from the DataBase
     * @return The "Data Element" Tab the way the client needs it.
     */
    private DataElement initDataElementTabData( DataElementModel dataElementModel )
    {
        DataElement dataElement = getDataElementDetails( dataElementModel );
        /////////////////////////////////////////////////////
        // "Reference Documents" of the "Data Element" Tab
        getDataElementReferenceDocuments( dataElementModel, dataElement );


        /////////////////////////////////////////////////////
        // CS/CSI data of the "Data Element" Tab
        List<CsCsi> dataElementCsCsis = new ArrayList<>();
        dataElement.setCsCsis( dataElementCsCsis );
        // add all the cscsi's and their designations and definitions except the unclassified ones
        for( CsCsiModel csCsiModel : dataElementModel.getCsCsiData().values() )
        {
            if( !csCsiModel.getCsiIdseq().equals( csCsiModel.UNCLASSIFIED ) )
            {
                CsCsi csCsi = new CsCsi( csCsiModel );
                ArrayList<AlternateName> alternateNames = new ArrayList<>();
                if( dataElementModel.getCsCsiDesignations().get( csCsiModel.getCsiIdseq() ) != null )
                { // if this CsCsiModel is only for definitions, might be null in designations
                    // get the list of DesignationIdseq from DataElementModel.csCsiDesignations, a hashmap of Lists of designationIdseq's indexed by csCsiIdseq
                    for( String designationIdseq : dataElementModel.getCsCsiDesignations().get( csCsiModel.getCsiIdseq() ) )
                    {
                        // call the AlternateNameDAO() constructor that takes a Designation model, giving it the model found by its index
                        alternateNames.add( new AlternateName( dataElementModel.getDesignationModels().get( designationIdseq ) ) );
                    }
                }
                csCsi.setAlternateNames( alternateNames );
                ArrayList<AlternateDefinition> alternateDefinitions = new ArrayList<>();
                if( dataElementModel.getCsCsiDefinitions().get( csCsiModel.getCsiIdseq() ) != null )
                { // if this CsCsiModel is only for designations, might be null in definitions
                    // get the list of DefinitionIdseqs from DataElementModel.csCsiDefinitions, a hashmap of Lists of definitionIdseq's indexed by csCsiIdseq
                    for( String definitionIdseq : dataElementModel.getCsCsiDefinitions().get( csCsiModel.getCsiIdseq() ) )
                    {
                        // call the AlternateDefinition constructor that takes a DefinitionModel, giving it the model found by its index
                        alternateDefinitions.add( new AlternateDefinition( dataElementModel.getDefinitionModels().get( definitionIdseq ) ) );
                    }
                }
                csCsi.setAlternateDefinitions( alternateDefinitions );
                dataElementCsCsis.add( csCsi );
            }
        }
        // now get the unclassified ones

        CsCsi unclassCsCsi = new CsCsi( dataElementModel.getCsCsiData().get( CsCsiModel.UNCLASSIFIED ) );
        ArrayList<AlternateName> unclassAlternateNames = new ArrayList<>();
        if( dataElementModel.getCsCsiDesignations().get( CsCsiModel.UNCLASSIFIED ) != null )
        {
            for( String designationIdseq : dataElementModel.getCsCsiDesignations().get( CsCsiModel.UNCLASSIFIED ) )
            {
                unclassAlternateNames.add( new AlternateName( dataElementModel.getDesignationModels().get( designationIdseq ) ) );
            }
        }
        unclassCsCsi.setAlternateNames( unclassAlternateNames );
        ArrayList<AlternateDefinition> unclassAlternateDefinitions = new ArrayList<>();
        if( dataElementModel.getCsCsiDefinitions().get( CsCsiModel.UNCLASSIFIED ) != null )
        {
            for( String definitionIdseq : dataElementModel.getCsCsiDefinitions().get( CsCsiModel.UNCLASSIFIED ) )
            {
                unclassAlternateDefinitions.add( new AlternateDefinition( dataElementModel.getDefinitionModels().get( definitionIdseq ) ) );
            }
        }
        unclassCsCsi.setAlternateDefinitions( unclassAlternateDefinitions );
        dataElementCsCsis.add( unclassCsCsi );


        /////////////////////////////////////////////////////
        // "Other Versions" of the "Data Element" Tab
        List<OtherVersion> otherVersions = new ArrayList<>();
        dataElement.setOtherVersions( otherVersions );
        for( DEOtherVersionsModel deOtherVersionsModel : dataElementModel.getDeOtherVersionsModels() )
        {
            OtherVersion otherVersion = new OtherVersion( deOtherVersionsModel );
            otherVersions.add( otherVersion );
        }

        return dataElement;
    }

    private void getDataElementReferenceDocuments( DataElementModel dataElementModel, DataElement dataElement )
    {
        // List to populate for client side
        List<ReferenceDocument> referenceDocuments = new ArrayList<>();
        dataElement.setReferenceDocuments( referenceDocuments );

        //List from database
        List<ReferenceDocModel> dataElementModelReferenceDocumentList = dataElementModel.getRefDocs();
        if( dataElementModelReferenceDocumentList.size() < 1 )
        {
            logger.debug( "No ReferenceDocuments where returned" );
        }
        for( ReferenceDocModel referenceDocModel : dataElementModelReferenceDocumentList )
        {
            ReferenceDocument referenceDoc = new ReferenceDocument();
            referenceDoc.setDocumentName( referenceDocModel.getDocName() );
            //logger.debug( referenceDocModel.getDocName() );

            referenceDoc.setDocumentType( referenceDocModel.getDocType() );
            //logger.debug( referenceDocModel.getDocType() );

            referenceDoc.setDocumentText( referenceDocModel.getDocText() );
            //logger.debug( referenceDocModel.getDocText() );

            referenceDoc.setContext( referenceDocModel.getContext().getName() );
            //logger.debug( referenceDocModel.getContext().getName() );

            referenceDoc.setUrl( referenceDocModel.getUrl() );
            //logger.debug( referenceDocModel.getUrl() );

            referenceDocuments.add( referenceDoc );
        }
    }

    private DataElement getDataElementDetails( DataElementModel dataElementModel )
    {
        DataElement dataElement = new DataElement();

        /////////////////////////////////////////////////////
        // "Data Element Details" of the "Data Element" Tab
        DataElementDetails dataElementDetails = new DataElementDetails();
        dataElement.setDataElementDetails( dataElementDetails );
        if( dataElementModel != null)
        {
            if(  dataElementModel.getPublicId() == null )
            {
                dataElementDetails.setPublicId( -1 );
                logger.error( " dataElementModel.getPublicId() == null" );
            }
            else
            {
                dataElementDetails.setPublicId( dataElementModel.getPublicId() );
            }

            if(  dataElementModel.getVersion() == null )
            {
                dataElementDetails.setVersion( -1 );
                logger.error( " dataElementModel.getVersion() == null" );
            }
            else
            {
                dataElementDetails.setVersion( dataElementModel.getVersion() );
            }

            dataElementDetails.setLongName( dataElementModel.getLongName() );
            dataElementDetails.setShortName( dataElementModel.getPreferredName() );
            dataElementDetails.setPreferredQuestionText( dataElementModel.getPreferredQuestionText() );
            dataElementDetails.setDefinition( dataElementModel.getPreferredDefinition() );
            dataElementDetails.setValueDomain( dataElementModel.getValueDomainModel().getLongName() );
            dataElementDetails.setDataElementConcept( dataElementModel.getDec().getLongName() );
            dataElementDetails.setContext( dataElementModel.getContextName() );
            dataElementDetails.setWorkflowStatus( dataElementModel.getAslName() );
            dataElementDetails.setOrigin( dataElementModel.getOrigin() );
            dataElementDetails.setRegistrationStatus( dataElementModel.getRegistrationStatus() );
            dataElementDetails.setDirectLink( "STILL NEED TO Create rest service and Link" );
        }
        return dataElement;
    }

    private DataElementConcept initDataElementConceptForCompare( DataElementModel dataElementModel )
    {
        DataElementConcept dataElementConcept = new DataElementConcept();
        DataElementConceptDetails dataElementConceptDetails = getDataElementConceptDetails( dataElementModel );
        dataElementConcept.setDataElementConceptDetails( dataElementConceptDetails );
        return dataElementConcept;
    }

    /**********************************************************************/
    /**********************************************************************/
    /**
     * Initialize the Data Element Concept tab
     *
     * @param dataElementModel data model from the database
     * @return Data model for the UI client.
     */
    private DataElementConcept initDataElementConceptTabData( DataElementModel dataElementModel )
    {
        DataElementConcept dataElementConcept = new DataElementConcept();

        // "Selected Data Element" of the "Data Element Concept" Tab
        dataElementConcept.setSelectedDataElement( getSelectedDataElement( dataElementModel ) );

        /////////////////////////////////////////////////////
        // "Data Element Concept Details" of the "Data Element Concept" Tab
        DataElementConceptDetails dataElementConceptDetails = getDataElementConceptDetails( dataElementModel );
        dataElementConcept.setDataElementConceptDetails( dataElementConceptDetails );

        /////////////////////////////////////////////////////
        // "Object Class" of the "Data Element Concept" Tab
        ObjectClass objectClass = new ObjectClass();
        dataElementConcept.setObjectClass( objectClass );

        objectClass.setPublicId( dataElementModel.getDec().getObjClassPublicId() );

        if( dataElementModel.getDec() == null )
        {
            logger.debug( "dataElementModel.getDec() == null" );
        }
        else
        {
            logger.debug( "dataElementModel.getDec() != null" );
        }

        if( dataElementModel.getDec().getObjClassVersion() == null )
        {
            logger.debug( "dataElementModel.getDec().getObjClassVersion() == null" );
        }
        else
        {
            logger.debug( "dataElementModel.getDec().getObjClassVersion() != null" );
        }

        if( dataElementModel.getDec().getObjectClassModel() == null )
        {
            logger.debug( "dataElementModel.getDec().getObjectClassModel() == null" );
        }
        else
        {
            logger.debug( "dataElementModel.getDec().getObjectClassModel() != null" );
        }

        if( dataElementModel.getDec().getObjClassVersion() != null )
        {
            // objectClass.setVersion will choke on null.
            objectClass.setVersion( dataElementModel.getDec().getObjClassVersion() );
        }

        if( dataElementModel.getDec().getObjectClassModel() != null )
        {
            objectClass.setLongName( dataElementModel.getDec().getObjectClassModel().getLongName() );
        }
        objectClass.setShortName( dataElementModel.getDec().getObjClassPrefName() );
        objectClass.setContext( dataElementModel.getDec().getObjClassContextName() );
        objectClass.setQualifier( dataElementModel.getDec().getObjClassQualifier() );

        /////////////////////////////////////////////////////
        // "Object Class Concepts" of the "Data Element Concept" Tab
        // This is a list of ObjectClassConcept
        if( dataElementModel.getDec().getDecIdseq() == null )
        {
            logger.debug( "dataElementModel.getDec().getDecIdseq() == null" );
        }
        else
        {
            logger.debug( "dataElementModel.getDec().getDecIdseq() != null" );
        }

        List<ConceptModel> objectClassConcepts = objectClassConceptDAO.getObjectClassConceptByDecIdseq( dataElementModel.getDec().getDecIdseq() );
        dataElementConcept.setObjectClassConcepts( objectClassConcepts );

        /////////////////////////////////////////////////////
        // "Property" of the "Data Element Concept" Tab
        Property property = new Property();
        dataElementConcept.setProperty( property );

        if( dataElementModel.getDec().getProperty() != null )
        {
            logger.debug( "dataElementModel.getDec().getProperty() != null" );
            property.setPublicId( dataElementModel.getDec().getProperty().getPublicId() );
            property.setVersion( dataElementModel.getDec().getProperty().getVersion() );
            property.setLongName( dataElementModel.getDec().getProperty().getLongName() );
            property.setShortName( dataElementModel.getDec().getProperty().getPreferredName() );
            property.setContext( dataElementModel.getDec().getProperty().getContext().getName() );
            property.setQualifier( dataElementModel.getDec().getProperty().getQualifier() );

        }
        else
        {
            logger.debug( "dataElementModel.getDec().getProperty() == null" );

        }


        /////////////////////////////////////////////////////
        // "Property Concepts" of the "Data Element Concept" Tab
        // This is a list of PropertyConcepts
        if( dataElementModel.getDec().getDecIdseq() == null )
        {
            logger.debug( "dataElementModel.getDec().getDecIdseq() == null" );
        }
        else
        {
            logger.debug( "dataElementModel.getDec().getDecIdseq() != null: " + dataElementModel.getDec().getDecIdseq() );
        }
        List<ConceptModel> propertyConcepts = propertyConceptDAO.getPropertyConceptByDecIdseq( dataElementModel.getDec().getDecIdseq() );
        dataElementConcept.setPropertyConcepts( propertyConcepts );

        return dataElementConcept;
    }

    private DataElementConceptDetails getDataElementConceptDetails( DataElementModel dataElementModel )
    {
        DataElementConceptDetails dataElementConceptDetails = new DataElementConceptDetails();


        dataElementConceptDetails.setPublicId( dataElementModel.getDec().getPublicId() );
        dataElementConceptDetails.setVersion( dataElementModel.getDec().getVersion() );
        dataElementConceptDetails.setLongName( dataElementModel.getDec().getLongName() );
        dataElementConceptDetails.setShortName( dataElementModel.getDec().getPreferredName() );
        dataElementConceptDetails.setDefinition( dataElementModel.getDec().getPreferredDefinition() );

        dataElementConceptDetails.setContext( dataElementModel.getDec().getConteName() );
        dataElementConceptDetails.setConceptualDomainContextName( dataElementModel.getDec().getCdContextName() );
        dataElementConceptDetails.setWorkflowStatus( dataElementModel.getDec().getAslName() );
        dataElementConceptDetails.setConceptualDomainPublicId( dataElementModel.getDec().getCdPublicId() );
        dataElementConceptDetails.setConceptualDomainShortName( dataElementModel.getDec().getCdPrefName() );

        //Conceptual Domain Version

        dataElementConceptDetails.setFormattedConceptualDomainVersion( dataElementModel.getDec().getCdVersion().toString() );
        dataElementConceptDetails.setOrigin( dataElementModel.getDec().getOrigin() );
        return dataElementConceptDetails;
    }

    private ValueDomain initValueDomainForCompare( DataElementModel dataElementModel )
    {
        ValueDomain valueDomain = new ValueDomain();
        ValueDomainDetails valueDomainDetails = getValueDomainDetails( dataElementModel );
        valueDomain.setValueDomainDetails( valueDomainDetails );
        List<PermissibleValuesModel> permissibleValues = permissibleValuesDAO.getPermissibleValuesByVdIdseq( dataElementModel.getValueDomainModel().getVdIdseq() );
        if (permissibleValues.size() > 0) {
        	valueDomain.setPermissibleValues( permissibleValues );	
        } else {
        	valueDomain.setPermissibleValues( null );
        }        
        logger.debug( "PermissibleValues count: " + permissibleValues.size() );        
        return valueDomain;
    }

    /**********************************************************************/
    /**********************************************************************/
    /**
     * Initialize the Value Domain tab
     *
     * @param dataElementModel data model from the database
     * @return Data model for the UI client.
     */
    private ValueDomain initValueDomainTabData( DataElementModel dataElementModel )
    {
        logger.debug( "initValueDomainTabData" );
        ValueDomain valueDomain = new ValueDomain();

        // "Selected Data Element" of the "Value Domain" Tab
        valueDomain.setSelectedDataElement( getSelectedDataElement( dataElementModel ) );

        /////////////////////////////////////////////////////
        // "value Domain Details" of the "value Domain" Tab
        ValueDomainDetails valueDomainDetails = getValueDomainDetails( dataElementModel );
        valueDomain.setValueDomainDetails( valueDomainDetails );

        /////////////////////////////////////////////////////
        // "value Domain Concepts" of the "value Domain" Tab
        //Just a string

        logger.debug( "value Domain Concepts of the value Domain" );
        List<ConceptModel> valueDomainConcepts = valueDomainConceptDAO.getValueDomainConceptByVdIdseq( dataElementModel.getValueDomainModel().getVdIdseq() );
        valueDomain.setValueDomainConcepts( valueDomainConcepts );


        /////////////////////////////////////////////////////
        // "Representation" of the "value Domain" Tab
        Representation representation = new Representation();
        valueDomain.setRepresentation( representation );

        List<ConceptModel> representationConcepts = new ArrayList<>();
        if( dataElementModel.getValueDomainModel().getRepresentationModel() != null )
        {

            representation.setPublicId( dataElementModel.getValueDomainModel().getRepresentationModel().getPublicId() );
            if( dataElementModel.getValueDomainModel().getRepresentationModel().getVersion() != null )
            {
                representation.setVersion( dataElementModel.getValueDomainModel().getRepresentationModel().getVersion() );
            }
            representation.setLongName( dataElementModel.getValueDomainModel().getRepresentationModel().getLongName() );
            representation.setShortName( dataElementModel.getValueDomainModel().getRepresentationModel().getPreferredName() );
            representation.setContext( dataElementModel.getValueDomainModel().getRepresentationModel().getContext().getName() );


            /////////////////////////////////////////////////////
            // "Representation Concepts" of the "value Domain" Tab
            logger.debug( "Representation Concepts of the value Domain" );
            representationConcepts = representationConceptsDAO.getRepresentationConceptByRepresentationId( dataElementModel.getValueDomainModel().getRepresentationModel().getPublicId() );
        }

        valueDomain.setRepresentationConcepts( representationConcepts );


        /////////////////////////////////////////////////////
        // "Permissible Values" of the "value Domain" Tab
        List<PermissibleValuesModel> permissibleValues = permissibleValuesDAO.getPermissibleValuesByVdIdseq( dataElementModel.getValueDomainModel().getVdIdseq() );
        valueDomain.setPermissibleValues( permissibleValues );
        logger.debug( "PermissibleValues count: " + permissibleValues.size() );
        if (logger.isTraceEnabled()) {
	        for( PermissibleValuesModel permissibleValuesModel : permissibleValues )
	        {
	            logger.trace( "PermissibleValues: " + permissibleValuesModel.getValue() );
	            logger.trace( "PermissibleValues: " + permissibleValuesModel.getShortMeaning() );
	            logger.trace( "PermissibleValues: " + permissibleValuesModel.getMeaningDescription() );
	        }
        }
        //add PV VM designations (AKA alternate names) and definitions CDEBROWSER-457
        logger.debug( "PermissibleValueExtList section start.......");

        //FIXME this requirement is changed not to use Classification information in PV and VM
        //List<PermissibleValueExt> permissibleValueExtList = buildPermissibleValueExtList(permissibleValues);
        List<PermissibleValueExt> permissibleValueExtList = new ArrayList<>();
        valueDomain.setPermissibleValueExtList(permissibleValueExtList);

        logger.debug( ".........PermissibleValueExtList section done");

        /////////////////////////////////////////////////////
        // "Reference Documents" of the "value Domain" Tab
        List<ReferenceDocModel> valueDomainReferenceDocuments = referenceDocDAO.getRefDocsByAcIdseq( dataElementModel.getValueDomainModel().getVdIdseq() );
        logger.debug( "valueDomainReferenceDocuments count: " + valueDomainReferenceDocuments.size() );
        valueDomain.setValueDomainReferenceDocuments( valueDomainReferenceDocuments );

        return valueDomain;
    }
    /////////////////////////////////////////////////////
    // "Permissible Values Ext " of the "Value Domain" Tab
    protected List<PermissibleValueExt> buildPermissibleValueExtList(List<PermissibleValuesModel> permissibleValues) {
    	List<PermissibleValueExt> permissibleValueExtList = new ArrayList<PermissibleValueExt>();
    	for (PermissibleValuesModel permissibleValuesModel : permissibleValues) {
    		PermissibleValueExt e = buildPermissibleValueExt(permissibleValuesModel);
    		permissibleValueExtList.add(e);
    	}
    	return permissibleValueExtList;
    }

    protected PermissibleValueExt buildPermissibleValueExt(PermissibleValuesModel permissibleValueModel) {
    	PermissibleValueExt permissibleValueExt = new PermissibleValueExt();
    	//build representation from DB model class
    	//every PV has one reference to VM
     	permissibleValueExt.setPvIdseq(permissibleValueModel.getPvIdseq());
    	permissibleValueExt.setPvMeaning(permissibleValueModel.getMeaningDescription());
    	permissibleValueExt.setVmPublicId(permissibleValueModel.getVmId());
    	permissibleValueExt.setVmVersion(permissibleValueModel.getVmVersion());

    	//this is ID to get designations and definitions
       	String vmIdseq = permissibleValueModel.getVmIdseq();
    	//get classification model
    	List<CsCsiValueMeaningModel> modelList = csCsiValueMeaningDAO.getCsCsisByVmId(vmIdseq);
    	CsCsiValueMeaningModelList csCsiValueMeaningModelList = new CsCsiValueMeaningModelList(modelList);
    	List<DesignationModelAlt> designationList = designationDAO.getDesignationModelsNoClsssification(vmIdseq);
    	List<DefinitionModelAlt> definitionList = definitionDAO.getAllDefinitionsNoClassification(vmIdseq);

    	List<ClassificationSchemaAlternate> classificationSchemaAlternateList = buildClassificationSchemaVmList(csCsiValueMeaningModelList, designationList, definitionList);
    	permissibleValueExt.setClassificationSchemaList(classificationSchemaAlternateList);
    	return permissibleValueExt;
    }

    /**
     *
     * @param csCsiValueMeaningModelList
     * @param designationList
     * @param definitionList
     * @return list of representation classes to send to the client
     */
    protected List<ClassificationSchemaAlternate> buildClassificationSchemaVmList(CsCsiValueMeaningModelList csCsiValueMeaningModelList,
    		List<DesignationModelAlt> designationList, List<DefinitionModelAlt> definitionList) {
    	List<ClassificationSchemaAlternate> classificationSchemaAlternateList = new ArrayList<>();
    	ClassificationSchemaAlternate unclassified = prepareUnclassified(csCsiValueMeaningModelList, designationList, definitionList);
    	//add unclassified section of this PV's VM
    	if (unclassified != null)
    		classificationSchemaAlternateList.add(unclassified);
    	//classified
    	prepareClassified(classificationSchemaAlternateList, csCsiValueMeaningModelList, designationList, definitionList);
    	return classificationSchemaAlternateList;
    }
    protected void prepareClassified(List<ClassificationSchemaAlternate> classificationSchemaAlternateList, CsCsiValueMeaningModelList csCsiValueMeaningModelList,
    		List<DesignationModelAlt> designationList, List<DefinitionModelAlt> definitionList) {
    	List<CsCsiValueMeaningModel> csCsiAttModelList = csCsiValueMeaningModelList.getModels();
    	if ((csCsiAttModelList == null) || (csCsiAttModelList.isEmpty()))
    			return;

    	String groupCsIdseq = csCsiAttModelList.get(0).getCsIdseq();
    	String currCsIdseq = groupCsIdseq;
    	String groupCsiIdseq = csCsiAttModelList.get(0).getCsiIdseq();
    	String currCsiIdseq = groupCsiIdseq;

    	ClassificationSchemaAlternate currEntity = new ClassificationSchemaAlternate(csCsiAttModelList.get(0));

    	for (CsCsiValueMeaningModel csCsiAttModel : csCsiAttModelList) {
    		currCsIdseq = csCsiAttModel.getCsIdseq();
    		currCsiIdseq = csCsiAttModel.getCsiIdseq();

    		if (!(groupCsIdseq.equals(currCsIdseq)) || (!(groupCsiIdseq.equals(currCsiIdseq)))){
    			//add previous classification
    			classificationSchemaAlternateList.add(currEntity);
    			groupCsIdseq = currCsIdseq;
    			groupCsiIdseq = currCsiIdseq;
    			//Start a new Classification group
    			currEntity = new ClassificationSchemaAlternate(csCsiAttModel);
    		}

        	for (DesignationModelAlt designationModel : designationList) {
        		if (csCsiValueMeaningModelList.isAttClassified(designationModel.getDesigIdseq())) {
        			if (csCsiAttModel.getAttIdseq().equals(designationModel.getDesigIdseq())) {
        				currEntity.addAlternateName(new AlternateName(designationModel));
        			}
        		}
        	}
        	for (DefinitionModelAlt definitionModel : definitionList) {
        		if (csCsiValueMeaningModelList.isAttClassified(definitionModel.getDefinIdseq())) {
        			if (csCsiAttModel.getAttIdseq().equals(definitionModel.getDefinIdseq())) {
        				currEntity.addDefinition(new AlternateDefinition(definitionModel));
        			}
        		}
        	}
    	}
		classificationSchemaAlternateList.add(currEntity);
    }

    protected ClassificationSchemaAlternate prepareUnclassified(CsCsiValueMeaningModelList csCsiValueMeaningModelList,
    		List<DesignationModelAlt> designationList, List<DefinitionModelAlt> definitionList) {
    	ClassificationSchemaAlternate unclassified = new ClassificationSchemaAlternate();
    	unclassified.setCsLongName("No Classification");
    	unclassified.setCsDefinition(CsCsiModel.UNCLASSIFIED);

    	for (DesignationModelAlt designationModel : designationList) {
    		if (! csCsiValueMeaningModelList.isAttClassified(designationModel.getDesigIdseq())) {
    			unclassified.addAlternateName(new AlternateName(designationModel));
    		}
    	}
    	for (DefinitionModelAlt definitionModel : definitionList) {
    		if (! csCsiValueMeaningModelList.isAttClassified(definitionModel.getDefinIdseq())) {
    			unclassified.addDefinition(new AlternateDefinition(definitionModel));
    		}
    	}
    	if ((unclassified.getAlternateNames().isEmpty())&& (unclassified.getDefinitions().isEmpty()))
    		return null;
    	else
    		return unclassified;
    }

    private ValueDomainDetails getValueDomainDetails( DataElementModel dataElementModel )
    {
        ValueDomainDetails valueDomainDetails = new ValueDomainDetails();

        valueDomainDetails.setPublicId( dataElementModel.getValueDomainModel().getPublicId() );
        if( dataElementModel.getValueDomainModel().getVersion() != null )
        {
            valueDomainDetails.setVersion( dataElementModel.getValueDomainModel().getVersion() );
        }
        valueDomainDetails.setLongName( dataElementModel.getValueDomainModel().getLongName() );
        valueDomainDetails.setShortName( dataElementModel.getValueDomainModel().getPreferredName() );

        // CHECKME - need to see where this is getting the wrong value.
        //valueDomainDetails.setContext( dataElementModel.getValueDomainModel().getCdContextName() );
        valueDomainDetails.setContext( dataElementModel.getContextName() );// CHECKME

        valueDomainDetails.setDefinition( dataElementModel.getValueDomainModel().getPreferredDefinition() );
        valueDomainDetails.setWorkflowStatus( dataElementModel.getValueDomainModel().getAslName() );
        valueDomainDetails.setDataType( dataElementModel.getValueDomainModel().getDatatype() );
        valueDomainDetails.setUnitOfMeasure( dataElementModel.getValueDomainModel().getUom() );
        valueDomainDetails.setDisplayFormat( dataElementModel.getValueDomainModel().getDispFormat() );
        valueDomainDetails.setMaximumLength( dataElementModel.getValueDomainModel().getMaxLength() );
        valueDomainDetails.setMinimumLength( dataElementModel.getValueDomainModel().getMinLength() );
        valueDomainDetails.setDecimalPlace( dataElementModel.getValueDomainModel().getDecimalPlace() );
        valueDomainDetails.setHighValue( dataElementModel.getValueDomainModel().getHighVal() );
        valueDomainDetails.setLowValue( dataElementModel.getValueDomainModel().getLowVal() );
        valueDomainDetails.setValueDomainType( dataElementModel.getValueDomainModel().getVdType() );
        valueDomainDetails.setConceptualDomainPublicId( dataElementModel.getValueDomainModel().getCdPublicId() );
        valueDomainDetails.setConceptualDomainShortName( dataElementModel.getValueDomainModel().getCdPrefName() );
        valueDomainDetails.setConceptualDomainContextName( dataElementModel.getValueDomainModel().getCdContextName() );
        valueDomainDetails.setConceptualDomainVersion( dataElementModel.getValueDomainModel().getCdVersion() );
        valueDomainDetails.setOrigin( dataElementModel.getValueDomainModel().getOrigin() );
        return valueDomainDetails;
    }

    private Classifications initClassificationsForCompare( DataElementModel dataElementModel )
    {
        Classifications classifications = new Classifications();
        List<CsCsi> classificationList = new ArrayList<>();
        classifications.setClassificationList( classificationList );
        for( CsCsiModel csCsiModel : dataElementModel.getClassifications() )
        {
            CsCsi csCsi = new CsCsi( csCsiModel );
            classificationList.add( csCsi );
        }
        return classifications;
    }


    /**********************************************************************/
    /**********************************************************************/
    /**
     * Initialize the Classifications tab
     *
     * @param dataElementModel data model from the database
     * @return Data model for the UI client.
     */
    private Classifications initClassificationsTabData( DataElementModel dataElementModel )
    {
        Classifications classifications = new Classifications();

        // "Selected Data Element" of the "Value Domain" Tab
        classifications.setSelectedDataElement( getSelectedDataElement( dataElementModel ) );

        /////////////////////////////////////////////////////
        // "Classifications" section of the "Classifications" tab
        List<CsCsi> classificationList = new ArrayList<>();
        classifications.setClassificationList( classificationList );
        for( CsCsiModel csCsiModel : dataElementModel.getClassifications() )
        {
            CsCsi csCsi = new CsCsi( csCsiModel );
            classificationList.add( csCsi );
        }

        /////////////////////////////////////////////////////
        // "Classifications Scheme Reference Documents" section of the "Classifications" tab
        List<ClassificationsSchemeReferenceDocument> classificationsSchemeReferenceDocuments = new ArrayList<>();
        classifications.setClassificationsSchemeReferenceDocuments( classificationsSchemeReferenceDocuments );
        for( CSRefDocModel csRefDocModel : dataElementModel.getCsRefDocModels() )
        {
            classificationsSchemeReferenceDocuments.add( new ClassificationsSchemeReferenceDocument( csRefDocModel ) );
        }


        /////////////////////////////////////////////////////
        // "Classification Scheme Item Reference Document
        List<ClassificationsSchemeItemReferenceDocument> classificationsSchemeItemReferenceDocuments = new ArrayList<>();
        classifications.setClassificationsSchemeItemReferenceDocuments( classificationsSchemeItemReferenceDocuments );
        for( CSIRefDocModel csiRefDocModel : dataElementModel.getCsiRefDocModels() )
        {
            classificationsSchemeItemReferenceDocuments.add( new ClassificationsSchemeItemReferenceDocument( csiRefDocModel ) );
        }

        return classifications;
    }


    /**********************************************************************/
    /**********************************************************************/
    /**
     * Initialize the Usage tab
     *
     * @param dataElementModel data model from the database
     * @return Data model for the UI client.
     */
    private Usage initUsageTabData( DataElementModel dataElementModel )
    {
        Usage usage = new Usage();

        // "Selected Data Element" of the "Value Domain" Tab
        usage.setSelectedDataElement( getSelectedDataElement( dataElementModel ) );

        /////////////////////////////////////////////////////
        // "Classifications" section of the "Classifications" tab
        List<FormUsage> formUsages = new ArrayList<>();
        usage.setFormUsages( formUsages );

        // Get FormBuilder host for links in "Public Id" column
        ToolOptionsModel formBuilderOptions = getToolOptionsDAO().getToolOptionsByToolNameAndProperty( "FormBuilder", "URL" );

        if( dataElementModel.getUsageModels() != null && dataElementModel.getUsageModels().size() > 0 )
        {
            for( UsageModel usageModel : dataElementModel.getUsageModels() )
            {
                formUsages.add( new FormUsage( usageModel, formBuilderOptions ) );
            }
        }
        else
        {
            logger.error( "no usage models" );
        }
        return usage;
    }


    /**********************************************************************/
    /**********************************************************************/
    /**
     * Initialize the Data Elements Derivation
     *
     * @param dataElementModel data model from the database
     * @return Data model for the UI client.
     */
    private DataElementDerivation initDataElementDerivationTabData( DataElementModel dataElementModel )
    {
        DataElementDerivation dataElementDerivation = new DataElementDerivation();
        dataElementDerivation.setSelectedDataElement( getSelectedDataElement( dataElementModel ) );

        DataElementDerivationModel dataElementDerivationModel = dataElementDerivationDAO.getDataElementDerivationByCdeIdseq(dataElementModel.getDeIdseq());

        // If dataElementDerivationModel == null then this Data element is not a derived data element.
        if( dataElementDerivationModel != null )
        {
            logger.debug( "dataElementDerivationModel: " + dataElementDerivationModel.toString() );
            dataElementDerivation.setDataElementDerivationDetails( dataElementDerivationModel );
            //CDEBROWSER-837 Using CDE IDSEQ to search; CDE ID is shared by different CDE Versions
            List<DataElementDerivationComponentModel> dataElementDerivationComponentModels = dataElementDerivationDAO.getDataElementDerivationComponentsByCdeIdseq(dataElementModel.getDeIdseq());
            dataElementDerivation.setDataElementDerivationComponentModels( dataElementDerivationComponentModels );
        }
        return dataElementDerivation;
    }

    /**********************************************************************/
    /**********************************************************************/
    /**
     * Initialize the Admin Info tab
     *
     * @param dataElementModel data model from the database
     * @return Data model for the UI client.
     */
    private AdminInfo initAdminInfoTabData( DataElementModel dataElementModel )
    {
        AdminInfo adminInfo = new AdminInfo();
        adminInfo.setCreatedBy( dataElementModel.getCreatedBy() );
        if (dataElementModel.getDateCreated()!=null)
        	adminInfo.setDateCreated( new SimpleDateFormat( CaDSRConstants.DATE_FORMAT ).format( dataElementModel.getDateCreated() ) );
        adminInfo.setModifiedBy( dataElementModel.getModifiedBy() );
        if (dataElementModel.getDateModified()!=null)
        	adminInfo.setDateModified( new SimpleDateFormat( CaDSRConstants.DATE_FORMAT ).format( dataElementModel.getDateModified() ) );
        adminInfo.setVdCreatedBy(dataElementModel.getValueDomainModel().getCreatedBy());
        adminInfo.setVdOwnedBy(dataElementModel.getValueDomainModel().getCdContextName());
        adminInfo.setDecCreatedBy(dataElementModel.getDec().getCreatedBy());
        adminInfo.setDecOwnedBy(dataElementModel.getDec().getCdContextName());
        adminInfo.setOrganization(userManagerDAO.getOrganization(dataElementModel.getCreatedBy()));
        return adminInfo;
    }

    /***************************************************************/
    /**
     * Most of the tabs start with the same "Selected Data Element" section
     *
     * @param dataElementModel The data model from the DataBase
     * @return The "Selected Data Element" section, used by most of the tabs (at the top)
     */
    public SelectedDataElement getSelectedDataElement( DataElementModel dataElementModel )
    {
        /////////////////////////////////////////////////////
        // "Selected Data Element" of the "Data Element Concept" Tab
        SelectedDataElement selectedDataElement = new SelectedDataElement();

        if( dataElementModel.getPublicId() == null )
        {
            selectedDataElement.setPublicId( -1 );
            logger.error( " dataElementModel.getPublicId() == null" );
        }
        else
        {
            selectedDataElement.setPublicId( dataElementModel.getPublicId() );
        }

        if( dataElementModel.getVersion() == null )
        {
            selectedDataElement.setVersion( -1 );
            logger.error( " dataElementModel.getVersion() == null" );
        }
        else
        {
            selectedDataElement.setVersion( dataElementModel.getVersion() );
        }

        selectedDataElement.setLongName( dataElementModel.getLongName() );
        selectedDataElement.setShortName( dataElementModel.getPreferredName() );
        selectedDataElement.setPreferredQuestionText( dataElementModel.getPreferredQuestionText() );
        selectedDataElement.setDefinition( dataElementModel.getPreferredDefinition() );
        selectedDataElement.setWorkflowStatus( dataElementModel.getAslName() );

        return selectedDataElement;
    }


    public void setDataElementDAO( DataElementDAO dataElementDAO )
    {
        this.dataElementDAO = dataElementDAO;
    }

    public void setReferenceDocDAO( ReferenceDocDAO referenceDocDAO )
    {
        this.referenceDocDAO = referenceDocDAO;
    }

    public void setPermissibleValuesDAO( PermissibleValuesDAO permissibleValuesDAO )
    {
        this.permissibleValuesDAO = permissibleValuesDAO;
    }

    public void setRepresentationConceptsDAO( RepresentationConceptsDAO representationConceptsDAO )
    {
        this.representationConceptsDAO = representationConceptsDAO;
    }

    public void setDataElementDerivationDAO( DataElementDerivationDAO dataElementDerivationDAO )
    {
        this.dataElementDerivationDAO = dataElementDerivationDAO;
    }

    public void setObjectClassConceptDAO( ObjectClassConceptDAO objectClassConceptDAO )
    {
        this.objectClassConceptDAO = objectClassConceptDAO;
    }

    public ConceptDAO getConceptDAO()
    {
        return conceptDAO;
    }

    public void setConceptDAO( ConceptDAO conceptDAO )
    {
        this.conceptDAO = conceptDAO;
    }

    public PropertyConceptDAO getPropertyConceptDAO()
    {
        return propertyConceptDAO;
    }

    public void setPropertyConceptDAO( PropertyConceptDAO propertyConceptDAO )
    {
        this.propertyConceptDAO = propertyConceptDAO;
    }

    public ValueDomainConceptDAO getValueDomainConceptDAO()
    {
        return valueDomainConceptDAO;
    }

    public void setValueDomainConceptDAO( ValueDomainConceptDAO valueDomainConceptDAO )
    {
        this.valueDomainConceptDAO = valueDomainConceptDAO;
    }

    public ToolOptionsDAO getToolOptionsDAO()
    {
        return toolOptionsDAO;
    }

    public void setToolOptionsDAO( ToolOptionsDAO toolOptionsDAO )
    {
        this.toolOptionsDAO = toolOptionsDAO;
    }

    /////////////////////////////////////////////////////
    /////////////////////////////////////////////////////
    /////////////////////////////////////////////////////
    // Test stuff


    private CdeDetails buildTestRecord()
    {
        CdeDetails cdeDetails = new CdeDetails();
        DataElement dataElement = new DataElement();
        cdeDetails.setDataElement( dataElement );

        DataElementDetails dataElementDetails = new DataElementDetails();
        dataElement.setDataElementDetails( dataElementDetails );

        dataElementDetails.setPublicId( 912345 );
        dataElementDetails.setVersion( 4.1F );
        dataElementDetails.setLongName( "The Long Name" );
        dataElementDetails.setShortName( "The Short Name" );
        dataElementDetails.setPreferredQuestionText( "The preferred Question Text" );
        dataElementDetails.setDefinition( "The Definition" );
        dataElementDetails.setValueDomain( "The Value Domain" );
        dataElementDetails.setDataElementConcept( "The Data Element Concept" );
        dataElementDetails.setContext( "The Context" );
        dataElementDetails.setWorkflowStatus( "The Workflow Status" );
        dataElementDetails.setOrigin( "The Origin" );
        dataElementDetails.setRegistrationStatus( "The Registration Status" );
        dataElementDetails.setDirectLink( "The Direct Link" );


        List<ReferenceDocument> referenceDocuments = new ArrayList<>();
        dataElement.setReferenceDocuments( referenceDocuments );
        // Add two sample referenceDocuments
        ReferenceDocument referenceDocument1 = new ReferenceDocument();
        referenceDocument1.setDocumentName( "The Document Name 1" );
        referenceDocument1.setDocumentType( "The Document Type 1" );
        referenceDocument1.setDocumentText( "The Document Text 1" );
        referenceDocument1.setContext( "The Context 1" );
        referenceDocument1.setUrl( "The URL 1" );
        referenceDocuments.add( referenceDocument1 );

        ReferenceDocument referenceDocument2 = new ReferenceDocument();
        referenceDocument2.setDocumentName( "The Document Name 2" );
        referenceDocument2.setDocumentType( "The Document Type 2" );
        referenceDocument2.setDocumentText( "The Document Text 2" );
        referenceDocument2.setContext( "The Context 2" );
        referenceDocument2.setUrl( "The URL 2" );
        referenceDocuments.add( referenceDocument2 );

        ReferenceDocument referenceDocument3 = new ReferenceDocument();
        referenceDocument3.setDocumentName( "The Document Name 3" );
        referenceDocument3.setDocumentType( "The Document Type 3" );
        referenceDocument3.setDocumentText( "The Document Text 3" );
        referenceDocument3.setContext( "The Context 3" );
        referenceDocument3.setUrl( "The URL 3" );
        referenceDocuments.add( referenceDocument3 );

        return cdeDetails;
    }

    private DataElementModel buildTestRecord1()
    {
        DataElementModel dataElementModel = new DataElementModel();
        dataElementModel.setCreatedBy( "buildTestRecord1 DateCreated" );
        dataElementModel.setDateCreated( getDate() );
        dataElementModel.setModifiedBy( "buildTestRecord1 DateModified" );
        dataElementModel.setDateModified( getDate() );

        dataElementModel.setPreferredQuestionText( "LongCDEName" );
        dataElementModel.setContextName( "ContextName" );
        dataElementModel.setUsingContexts( "UsingContexts" );

        //List<ReferenceDocModel>
        dataElementModel.setRefDocs( buildTestReferenceDocModelList() );

        //List<DesignationModel>
        dataElementModel.setDesignationModels( buildDesignationModelList() );

        dataElementModel.setPublicId( 12345 );
        dataElementModel.setIdseq( "Idseq" );
        dataElementModel.setRegistrationStatus( "RegistrationStatus" );

        dataElementModel.setValueDomainModel( buildTestValueDomainModel() );

        dataElementModel.setDec( buildTestDataElementConceptModel() );
        dataElementModel.setContext( buildTestContextModel() );
        dataElementModel.setDeIdseq( "DeIdse" );
        dataElementModel.setVersion( 4.1F ); // needs to be a Float!
        dataElementModel.setConteIdseq( "ConteIdseq" );
        dataElementModel.setPreferredName( "PreferredName" );
        dataElementModel.setVdIdseq( "VdIdseq" );
        dataElementModel.setDecIdseq( "DecIdseq" );
        dataElementModel.setPreferredDefinition( "PreferredDefinition" );
        dataElementModel.setAslName( "AslName" );

        dataElementModel.setLongName( "LongName" );
        dataElementModel.setLatestVerInd( "LatestVerInd" );
        dataElementModel.setDeletedInd( "DeletedInd" );
        dataElementModel.setBeginDate( getDate() );
        dataElementModel.setEndDate( getDate() );
        dataElementModel.setOrigin( "Origin" );
        dataElementModel.setCdeId( 12345 );
        dataElementModel.setQuestion( "Question" );

        return dataElementModel;
    }

    /////////////////////////////////////////////////////////////////////////
    // Test DesignationModel List
    private List<DesignationModel> buildDesignationModelList()
    {
        List<DesignationModel> designationModelList = new ArrayList<>();
        designationModelList.add( buildTestDesignationModel( "A" ) );
        designationModelList.add( buildTestDesignationModel( "B" ) );
        designationModelList.add( buildTestDesignationModel( "C" ) );

        return designationModelList;
    }

    private DesignationModel buildTestDesignationModel( String text )
    {
        DesignationModel designationModel = new DesignationModel();

        designationModel.setCreatedBy( "buildTestDesignationModel DateCreated" );
        designationModel.setDateCreated( getDate() );
        designationModel.setModifiedBy( "buildTestDesignationModel DateModified" );
        designationModel.setDateModified( getDate() );

        designationModel.setName( "designationModel name_" + text );
        designationModel.setDesigIDSeq( "desigIDSeq_" + text );
        designationModel.setType( "designationModel type_" + text );
        designationModel.setContex( buildTestContextModel() );
        designationModel.setLang( "designationModel lang_" + text );

        return designationModel;
    }

    private ValueDomainModel buildTestValueDomainModel()
    {
        ValueDomainModel valueDomainModel = new ValueDomainModel();

        valueDomainModel.setCreatedBy( "buildTestValueDomainModel DateCreated" );
        valueDomainModel.setDateCreated( getDate() );
        valueDomainModel.setModifiedBy( "buildTestValueDomainModel DateModified" );
        valueDomainModel.setDateModified( getDate() );

        valueDomainModel.setPreferredName( "valueDomainModel PreferredName" );
        valueDomainModel.setPreferredDefinition( "valueDomainModel  PreferredDefinition" );
        valueDomainModel.setLongName( "valueDomainModel LongName" );
        valueDomainModel.setAslName( "valueDomainModel AslName" );
        valueDomainModel.setVersion( new Float( 123 ) );
        valueDomainModel.setDeletedInd( "valueDomainModel DeletedInd" );
        valueDomainModel.setLatestVerInd( "valueDomainModel LatestVerInd" );
        valueDomainModel.setPublicId( 321 );
        valueDomainModel.setOrigin( "valueDomainModel Origin" );
        valueDomainModel.setIdseq( "valueDomainModel Idseq" );
        valueDomainModel.setVdIdseq( "valueDomainModel VdIdseq" );
        valueDomainModel.setDatatype( "valueDomainModel Datatype" );
        valueDomainModel.setUom( "valueDomainModel Uom" );
        valueDomainModel.setDispFormat( "valueDomainModel DispFormat" );
        valueDomainModel.setMaxLength( 40 );
        valueDomainModel.setMinLength( 2 );
        valueDomainModel.setHighVal( "valueDomainModel ighVal" );
        valueDomainModel.setLowVal( "valueDomainModel LowVal" );
        valueDomainModel.setCharSet( "valueDomainModel CharSet" );
        valueDomainModel.setDecimalPlace( 2 );
        valueDomainModel.setCdPrefName( "valueDomainModel CdPrefName" );
        valueDomainModel.setCdContextName( "valueDomainModel CdContextName" );
        valueDomainModel.setCdVersion( new Float( 123 ) );
        valueDomainModel.setCdPublicId( 345 );
        valueDomainModel.setVdType( "valueDomainModel VdType" );
        valueDomainModel.setRepresentationModel( buildTestRepresentationModel() );

        // ConceptDerivationRuleModel is still being worked on, so far it's just an empty class
        valueDomainModel.setConceptDerivationRuleModel( new ConceptDerivationRuleModel() );

        return valueDomainModel;
    }

    private RepresentationModel buildTestRepresentationModel()
    {
        RepresentationModel representationModel = new RepresentationModel();

        representationModel.setCreatedBy( "buildTestRepresentationModel DateCreated" );
        representationModel.setDateCreated( getDate() );
        representationModel.setModifiedBy( "buildTestRepresentationModel DateModified" );
        representationModel.setDateModified( getDate() );

        representationModel.setPreferredName( "RepresentationModel PreferredName" );
        representationModel.setLongName( "RepresentationModel LongName" );
        representationModel.setVersion( ( float ) 23.45 );
        representationModel.setContext( buildTestContextModel() );
        representationModel.setPublicId( 4321 );
        representationModel.setIdseq( "RepresentationModel Idseq" );
        // ConceptDerivationRuleModel is still being worked on, so far it's just an empty class
        representationModel.setConceptDerivationRuleModel( new ConceptDerivationRuleModel() );

        return representationModel;
    }

    private DataElementConceptModel buildTestDataElementConceptModel()
    {
        DataElementConceptModel dataElementConceptModel = new DataElementConceptModel();

        dataElementConceptModel.setCreatedBy( "buildTestDataElementConceptModel DateCreated" );
        dataElementConceptModel.setDateCreated( getDate() );
        dataElementConceptModel.setModifiedBy( "buildTestDataElementConceptModel DateModified" );
        dataElementConceptModel.setDateModified( getDate() );

        dataElementConceptModel.setPreferredName( "DataElementConceptModel PreferredName" );
        dataElementConceptModel.setPreferredDefinition( "DataElementConceptModel PreferredDefinition" );
        dataElementConceptModel.setLongName( "DataElementConceptModel LongName" );
        dataElementConceptModel.setAslName( "DataElementConceptModel AslName" );
        dataElementConceptModel.setVersion( ( float ) 123 );

        dataElementConceptModel.setDeletedInd( "DataElementConceptModel DeletedInd" );
        dataElementConceptModel.setLatestVerInd( "DataElementConceptModel LatestVerInd" );
        dataElementConceptModel.setPublicId( 543 );

        dataElementConceptModel.setOrigin( "DataElementConceptModel Origin" );
        dataElementConceptModel.setIdseq( "DataElementConceptModel Idseq" );
        dataElementConceptModel.setDecIdseq( "DataElementConceptModel DecIdseq" );
        dataElementConceptModel.setCdIdseq( "DataElementConceptModel CdIdseq" );
        dataElementConceptModel.setProplName( "DataElementConceptModel ProplName" );
        dataElementConceptModel.setOclName( "DataElementConceptModel OclName" );
        dataElementConceptModel.setObjClassQualifier( "DataElementConceptModel ObjClassQualifier" );
        dataElementConceptModel.setPropertyQualifier( "DataElementConceptModel PropertyQualifier" );
        dataElementConceptModel.setChangeNote( "DataElementConceptModel ChangeNote" );
        dataElementConceptModel.setObjClassPrefName( "DataElementConceptModel ObjClassPrefName" );
        dataElementConceptModel.setObjClassContextName( "DataElementConceptModel ObjClassContextName" );
        dataElementConceptModel.setPropertyPrefName( "DataElementConceptModel PropertyPrefName" );
        dataElementConceptModel.setPropertyContextName( "DataElementConceptModel PropertyContextName" );

        dataElementConceptModel.setPropertyVersion( ( float ) 987 );

        dataElementConceptModel.setObjClassVersion( ( float ) 654 );

        dataElementConceptModel.setConteName( "DataElementConceptModel ConteName" );
        dataElementConceptModel.setCdPrefName( "DataElementConceptModel CdPrefName" );
        dataElementConceptModel.setCdContextName( "DataElementConceptModel CdContextName" );
        dataElementConceptModel.setCdVersion( ( float ) 12.34 );
        dataElementConceptModel.setCdPublicId( 123 );
        dataElementConceptModel.setObjClassPublicId( 123 );
        dataElementConceptModel.setProperty( buildTestPropertyModel() );
        dataElementConceptModel.setObjectClassModel( buildTestObjectClassModel() );

        return dataElementConceptModel;
    }

    private ObjectClassModel buildTestObjectClassModel()
    {
        ObjectClassModel objectClassModel = new ObjectClassModel();

        objectClassModel.setCreatedBy( "buildTestObjectClassModel  DateCreated" );
        objectClassModel.setDateCreated( getDate() );
        objectClassModel.setModifiedBy( "buildTestObjectClassModel DateModified" );
        objectClassModel.setDateModified( getDate() );

        objectClassModel.setPreferredName( "ObjectClassModel PreferredName" );
        objectClassModel.setLongName( "ObjectClassModel LongName" );
        objectClassModel.setVersion( ( float ) 2.3 );
        objectClassModel.setContext( buildTestContextModel() );
        objectClassModel.setPublicId( 234 );

        objectClassModel.setQualifier( "ObjectClassModel LongName" );

        objectClassModel.setIdseq( "ObjectClassModel Idseq" );
        objectClassModel.setName( "ObjectClassModel Name" );
        objectClassModel.setIdseq( "ObjectClassModel Qualifier" );
        return objectClassModel;
    }

    /*

     */
    private PropertyModel buildTestPropertyModel()
    {
        PropertyModel propertyModel = new PropertyModel();

        propertyModel.setCreatedBy( "buildTestPropertyModel DateCreated" );
        propertyModel.setDateCreated( getDate() );
        propertyModel.setModifiedBy( "buildTestPropertyModel DateModified" );
        propertyModel.setDateModified( getDate() );

        propertyModel.setPreferredName( "PropertyModel PreferredName" );
        propertyModel.setLongName( "PropertyModel LongName" );
        propertyModel.setVersion( ( float ) 1.23 );
        propertyModel.setContext( buildTestContextModel() );
        propertyModel.setPublicId( 123 );
        propertyModel.setName( "PropertyModel Name" );
        propertyModel.setQualifier( "PropertyModel Qualifier" );

        return propertyModel;
    }

    /////////////////////////////////////////////////////////////////////////
// Test ReferenceDocModel List
    private List<ReferenceDocModel> buildTestReferenceDocModelList()
    {
        List<ReferenceDocModel> referenceDocModelList = new ArrayList<>();

        referenceDocModelList.add( buildTestReferenceDocModel( "ref doc model A" ) );
        referenceDocModelList.add( buildTestReferenceDocModel( "ref doc model B" ) );
        referenceDocModelList.add( buildTestReferenceDocModel( "ref doc model C" ) );
        return referenceDocModelList;
    }

    private ReferenceDocModel buildTestReferenceDocModel( String text )
    {
        ReferenceDocModel referenceDocModel = new ReferenceDocModel();

        referenceDocModel.setCreatedBy( "buildTestReferenceDocModel DateCreated" );
        referenceDocModel.setDateCreated( getDate() );
        referenceDocModel.setModifiedBy( "buildTestReferenceDocModel DateModified" );
        referenceDocModel.setDateModified( getDate() );

        referenceDocModel.setDocName( "DocName_" + text );
        referenceDocModel.setDocType( "DocType_" + text );
        referenceDocModel.setDocIDSeq( "IDSeq_" + text );
        referenceDocModel.setDocText( "DocText_" + text );
        referenceDocModel.setLang( "Lang_" + text );
        referenceDocModel.setUrl( "URL_" + text );
        referenceDocModel.setContext( buildTestContextModel() );

        return referenceDocModel;
    }

    private ContextModel buildTestContextModel()
    {
        ContextModel contextModel = new ContextModel();
        contextModel.setCreatedBy( "buildTestContextModel DateCreated" );
        contextModel.setDateCreated( getDate() );
        contextModel.setModifiedBy( "buildTestContextModel DateModified" );
        contextModel.setDateModified( getDate() );

        contextModel.setConteIdseq( "ConteIdSeq" );
        contextModel.setName( "ContextModel Name" );
        contextModel.setPalName( "ContextModel Pal Name" );
        contextModel.setLlName( "ContextModel Llname" );
        contextModel.setDescription( "ContextModel Description" );
        contextModel.setPreferredDefinition( "ContextModel PreferredDefinition" );
        contextModel.setLanguage( "ContextModel Lang" );
        contextModel.setVersion( ( float ) 123 );

        return contextModel;
    }
    /////////////////////////////////////////////////////////////////////////
    // End test ReferenceDocModel List

    private Timestamp getDate()
    {
        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        return new java.sql.Timestamp( now.getTime() );
    }

}
