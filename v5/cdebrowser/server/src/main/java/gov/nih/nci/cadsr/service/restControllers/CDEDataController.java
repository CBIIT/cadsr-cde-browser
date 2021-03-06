package gov.nih.nci.cadsr.service.restControllers;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gov.nih.nci.cadsr.common.CaDSRConstants;
import gov.nih.nci.cadsr.common.UsageLog;
import gov.nih.nci.cadsr.common.util.ParameterValidator;
import gov.nih.nci.cadsr.dao.ConceptDAO;
import gov.nih.nci.cadsr.dao.CsCsiDeDAO;
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
import gov.nih.nci.cadsr.dao.ValueDomainConceptDAO;
import gov.nih.nci.cadsr.dao.ValueMeaningDAO;
import gov.nih.nci.cadsr.dao.model.AcRegistrationsModel;
import gov.nih.nci.cadsr.dao.model.CSIRefDocModel;
import gov.nih.nci.cadsr.dao.model.CSRefDocModel;
import gov.nih.nci.cadsr.dao.model.ConceptModel;
import gov.nih.nci.cadsr.dao.model.CsCsiModel;
import gov.nih.nci.cadsr.dao.model.CsCsiValueMeaningModel;
import gov.nih.nci.cadsr.dao.model.CsCsiValueMeaningModelList;
import gov.nih.nci.cadsr.dao.model.DEOtherVersionsModel;
import gov.nih.nci.cadsr.dao.model.DataElementConceptModel;
import gov.nih.nci.cadsr.dao.model.DataElementDerivationComponentModel;
import gov.nih.nci.cadsr.dao.model.DataElementDerivationModel;
import gov.nih.nci.cadsr.dao.model.DataElementModel;
import gov.nih.nci.cadsr.dao.model.DefinitionModelAlt;
import gov.nih.nci.cadsr.dao.model.DesignationModelAlt;
import gov.nih.nci.cadsr.dao.model.PermissibleValuesModel;
import gov.nih.nci.cadsr.dao.model.ReferenceDocModel;
import gov.nih.nci.cadsr.dao.model.ToolOptionsModel;
import gov.nih.nci.cadsr.dao.model.UsageModel;
import gov.nih.nci.cadsr.error.RestControllerException;
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
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.AlternateDefinitionCsCsi;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.AlternateName;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.AlternateNameCsCsi;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.CsCsi;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.CsCsiForCdeDetails;
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

@RestController
public class CDEDataController
{
    private static final Logger logger = LogManager.getLogger( CDEDataController.class.getName() );
    protected static final String USED_BY = "USED_BY";

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
    private CsCsiDeDAO csCsiDeDAO;

    @Autowired
    private ValueMeaningDAO valueMeaningDAO;

    @Autowired
    private UsageLog usageLog;


    @RequestMapping( value = "/CDEData", produces = "application/json")
    @ResponseBody
    public CdeDetails retrieveDataElementDetails( @RequestParam( "deIdseq" ) String deIdseq ) throws RestControllerException
    {
        logger.debug( "Received rest call \"CDEData\": " + deIdseq );
        //We need to check IDSEQ format to comply with security scan
        verifyParameter(deIdseq);

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
        usageLog.log( "CDEData",  "deIdseq=" + deIdseq );

        return cdeDetails;
    }
    private static void verifyParameter(String deIdseq) throws RestControllerException {
    	if ((StringUtils.isEmpty(deIdseq)) || (! ParameterValidator.validateIdSeq(deIdseq))) {
    		logger.error("Unexpected parameter value provided, deIdseq: " + deIdseq);
			throw new RestControllerException("Unexpected parameter deIdseq value provided: " + deIdseq);
		}
	}
	@RequestMapping( value = "/CDELink", produces = "application/json" )
    @ResponseBody
    public CdeDetails retrieveDataElementDetailsByLink( @RequestParam( "publicId" ) String publicId, 
    		@RequestParam( "version" ) String versionNumber )
    {
        logger.debug( "Received rest call \"CDELink\" publicId: " + publicId + "v." + versionNumber );

        DataElementModel dataElementModel = null;
        CdeDetails cdeDetails = null;
        if (checkLinkParameters(publicId, versionNumber)) {
	        // Get the data model from the database
	        try
	        {
	            dataElementModel = dataElementDAO.geCdeByCdeIdAndVersion( new Integer(publicId), new Float(versionNumber));
	            if (dataElementModel != null) {
	            	cdeDetails = buildCdeDetails( dataElementModel );
	            }
	        } catch( Exception e )
	        {
	            logger.error("retrieveDataElementDetailsByLink query parameters received caused exception", e);
	        }
        }
        else {
        	logger.info("Unexpected parameter values are ignored in retrieveDataElementDetailsByLink, publicId: " + publicId + ", versionNumber: " + versionNumber);
        }
        if (cdeDetails == null) {
        	cdeDetails = new CdeDetails();
        }
        //CdeDetails cdeDetails = buildTestRecord();
        usageLog.log( "CDELink",  "publicId=" + publicId + "v." + versionNumber);

        return cdeDetails;
    }
    /**
     * Accept a comma separated list of deIdseq values.
     *
     * @param deIdseq Comma separated list of deIdseqs
     * @return Array of CdeDetails, to be used by the compare CDE feature.
     */
    @RequestMapping( value = "/multiCDEData", produces = "application/json" )
    @ResponseBody
    public CdeDetails[] multiCDEDataController( @RequestParam( "deIdseq" ) String deIdseq )
    {
        logger.debug( "multiCDEDataController: " + deIdseq );
        String[] deIdseqs = deIdseq.split( "," );
        List<String> deIdsList = new ArrayList<String>();
        for( String id : deIdseqs ) 
        {
        	if (ParameterValidator.validateIdSeq(id = id.trim())) {//This checking is to prevent unexpected IDSEQ values including given on security scan
        			deIdsList.add(id);
        		}	else {
            		logger.info("Unexpected value of IDSEQ is ignored: " + id);
            	}        	
        }
        List<CdeDetails> arrList = new ArrayList<>();    
        // CDEBROWSER-649 Improve queries for compare screen
        if (deIdsList.size()>0) {
	        List<DataElementModel> dataElementDAOList = dataElementDAO.getCdeList(deIdsList);
	        for (DataElementModel dataElementModel: dataElementDAOList)
	        {
		            arrList.add(buildCdeDetailsForCompare( dataElementModel ));
	        } 
        }
        
        CdeDetails[] cdeDetailsArray = new CdeDetails[arrList.size()];
        cdeDetailsArray = arrList.toArray(cdeDetailsArray);
        usageLog.log( "multiCDEData",  "deIdseq=" + deIdseq + " [" + cdeDetailsArray.length + " results returned]" );
        return cdeDetailsArray;
    }

    private boolean checkLinkParameters(String publicId, String versionNumber) {
    	if ((NumberUtils.isNumber(versionNumber)) && (NumberUtils.isDigits(publicId))) {
    		return true;
    	}
    	else return false;
    }
    // Build a CdeDetails to send to the client
    private CdeDetails buildCdeDetails( DataElementModel dataElementModel )
    {
        CdeDetails cdeDetails = new CdeDetails();


        // For the "Data Element" Tab
        DataElement dataElement = initDataElementTabData( dataElementModel );
        cdeDetails.setDataElement( dataElement );

        // For the "Data Element Concept" Tab
        DataElementConcept dataElementConcept = initDataElementConceptTabData( dataElementModel );
        cdeDetails.setDataElementConcept( dataElementConcept );

        // For the "Value Domain" Tab
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
     *
     * @param dataElementModel
     * @return
     */
    private CdeDetails buildCdeDetailsForCompare( DataElementModel dataElementModel )
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

    private DataElement initDataElementForCompare( DataElementModel dataElementModel )
    {        	
        DataElement dataElement = getDataElementDetails( dataElementModel );
        /////////////////////////////////////////////////////
        // "Reference Documents" of the "Data Element" Tab

        // List to populate for client side
        getDataElementReferenceDocuments( dataElementModel, dataElement );
        return dataElement;
    }
    /**********************************************************************/
    /**********************************************************************/
    /**
     * For the "Data Element" Tab
     *
     * @param dataElementModel The data model from the DataBase
     * @return The "Data Element" Tab the way the client needs it.
     */
    @SuppressWarnings("unchecked")
	private DataElement initDataElementTabData( DataElementModel dataElementModel )
    {
        DataElement dataElement = getDataElementDetails( dataElementModel );
        /////////////////////////////////////////////////////
        // "Reference Documents" of the "Data Element" Tab
       //CDEBROWSER-809 "Separate out the Documents with Document Type containing "*Question Text" "
        getDataElementReferenceDocuments( dataElementModel, dataElement, true );

        /////////////////////////////////////////////////////
        // CS/CSI data of the "Data Element" Tab
        List<CsCsi> dataElementCsCsis = new ArrayList<>();
        dataElement.setCsCsis( dataElementCsCsis );
        // add all the cscsi's and their designations and definitions except the unclassified ones
        //change for CDEBROWSER-468
        //now get the unclassified ones 
        ////////////////////////////////////////////////////
        //CDEBROWSER-809 add unclassified first
        CsCsi unclassCsCsi = ControllerUtils.populateCsCsiUnclassified( dataElementModel );
        //CDEBROWSER-809 we change the lists of alternate names
        CsCsiForCdeDetails csCsiForCdeDetails = rearrangeForCdeDetails(unclassCsCsi);
        dataElement.setCsCsisCdeDetails(csCsiForCdeDetails );
        ////////////////////////////////////////////////////
        List<CsCsi> csCsiClassifiedList = ControllerUtils.populateCsCsiModel( dataElementModel.getDeIdseq(), csCsiDeDAO );
        
        //CDEBROWSER-809 Add Alt Names and Definitions with CS/CSIs listed comma separated 
        List<AlternateNameCsCsi> altNamesWithCsCsi = buildAltNamesWithCsCsi(csCsiClassifiedList);//could be null
        List<AlternateDefinitionCsCsi> definitionsWithCsCsi = buildDefinitionsWithCsCsi(csCsiClassifiedList);//could be null
        List<AlternateNameCsCsi> altNamesWithCsCsiAll = csCsiForCdeDetails.getAlternateNames();//never null
        if (altNamesWithCsCsi != null) {
            //add classified designations to unclassified
	        for (AlternateNameCsCsi curr : altNamesWithCsCsi) {
	        	altNamesWithCsCsiAll.add(curr);
	        }
        }
        Collections.sort(altNamesWithCsCsiAll);//sort the list
        
        List<AlternateDefinitionCsCsi> altDefinitionsWithCsCsiAll = csCsiForCdeDetails.getAlternateDefinitions();//never null
        if (definitionsWithCsCsi != null) {
            //add classified definitions to unclassified
	        for (AlternateDefinitionCsCsi curr : definitionsWithCsCsi) {
	        	altDefinitionsWithCsCsiAll.add(curr);
	        }
        }
        Collections.sort(altDefinitionsWithCsCsiAll);//sort the list
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
    
    //CDEBROWSER-809
    protected List<AlternateNameCsCsi> buildAltNamesWithCsCsi(List<CsCsi> csCsiClassifiedList) {
    	if ((csCsiClassifiedList == null) || (csCsiClassifiedList.isEmpty())) {
    		return null;
    	}
    	//add ALternate names with CsCsIs
    	List<AlternateNameCsCsi> alternateNames = new ArrayList<>();
    	Map<AlternateName, Set<String>> altNamesMap = new HashMap<>();
    	for (CsCsi csCsi : csCsiClassifiedList) {
    		List<AlternateName> altNames = csCsi.getAlternateNames();
    		for (AlternateName altName : altNames) {
    			String nextCsCsi = csCsi.getCsLongName() + '/' + csCsi.getCsiName();
    			Set<String> placed = altNamesMap.get(altName);
    			if (placed == null) {
    				placed = new TreeSet<String>();
    				altNamesMap.put(altName, placed);
    			}
				placed.add(nextCsCsi);
    		}
    	}
		Set<Entry<AlternateName, Set<String>>> entries = altNamesMap.entrySet();
		for (Entry<AlternateName, Set<String>> entry : entries) {
			List<String> csCsiList = new ArrayList<>();
			for (String curr : entry.getValue()) {
				csCsiList.add(curr);
			}
			Collections.sort(csCsiList);
			AlternateNameCsCsi altName = new AlternateNameCsCsi(entry.getKey());
			altName.setCsCsi(csCsiList);
			alternateNames.add(altName);
		}
    	return alternateNames;
    }
    //CDEBROWSER-809
    protected List<AlternateDefinitionCsCsi> buildDefinitionsWithCsCsi(List<CsCsi> csCsiClassifiedList) {
    	if ((csCsiClassifiedList == null) || (csCsiClassifiedList.isEmpty())) {
    		return null;
    	}
    	//add ALternate Definitions with CsCsIs
    	List<AlternateDefinitionCsCsi> alternateDefinitions = new ArrayList<>();
    	Map<AlternateDefinition, Set<String>> altDefinitionsMap = new HashMap<>();
    	for (CsCsi csCsi : csCsiClassifiedList) {
    		String nextCsCsi = csCsi.getCsLongName() + '/' + csCsi.getCsiName();
    		List<AlternateDefinition> altNames = csCsi.getAlternateDefinitions();
    		for (AlternateDefinition altName : altNames) {
    			Set<String> placed = altDefinitionsMap.get(altName);
    			if (placed == null) {
    				placed = new TreeSet<String>();
    				altDefinitionsMap.put(altName, placed);
    			}
				placed.add(nextCsCsi);
    		}
    	}
		Set<Entry<AlternateDefinition, Set<String>>> entries = altDefinitionsMap.entrySet();
		for (Entry<AlternateDefinition, Set<String>> entry : entries) {
			List<String> csCsiList = new ArrayList<>();
			for (String curr : entry.getValue()) {
				csCsiList.add(curr);
			}
			Collections.sort(csCsiList);
			AlternateDefinitionCsCsi altDef = new AlternateDefinitionCsCsi(entry.getKey());
			altDef.setCsCsi(csCsiList);
			alternateDefinitions.add(altDef);
		}
    	return alternateDefinitions;
	}
	//CDEBROWSER-809 "Separate out the Alternate names of type = "Used_By" into their own sub-table"
    protected static CsCsiForCdeDetails rearrangeForCdeDetails(CsCsi unclassCsCsi) {
    	return rearrangeForCdeDetails(unclassCsCsi, true);
    }
    protected static CsCsiForCdeDetails rearrangeForCdeDetails(CsCsi unclassCsCsi, boolean buildUsedBy) {
    	List<AlternateName> altNameList = unclassCsCsi.getAlternateNames();
    	List<AlternateName> altNameListAll = new ArrayList<>();
    	List<AlternateName> altNameListUsedBy = new ArrayList<>();
    	if (altNameList != null) {
	    	for (AlternateName alternateName : altNameList) {
	    		if ((USED_BY.equals(alternateName.getType())) && (alternateName.getName().equals(alternateName.getContext()))
	    				&& buildUsedBy) {//CDEBROWSER-811 DEC does not have usedByAlternateNames (no DEC have USED_BY Context)
	    			altNameListUsedBy.add(alternateName);
	    		}
	    		else {
	    			altNameListAll.add(alternateName);
	    		}
	    	}
    	}
    	unclassCsCsi.setAlternateNames(altNameListAll);
    	unclassCsCsi.setUsedByAlternateNames(altNameListUsedBy);
    	CsCsiForCdeDetails csCsiForCdeDetails = new CsCsiForCdeDetails(unclassCsCsi);//will take usedByAlternateNames
    	//set up Alt Names
    	List<AlternateNameCsCsi> alternateNameCsCsiList = new ArrayList<>();
    	csCsiForCdeDetails.setAlternateNames(alternateNameCsCsiList);
    	AlternateNameCsCsi nameCsCsi;
    	for (AlternateName curr: altNameListAll) {
    		nameCsCsi = new AlternateNameCsCsi(curr);
    		alternateNameCsCsiList.add(nameCsCsi);
    	}
    	//set up Definitions
    	List<AlternateDefinitionCsCsi> alternateDefCsCsiList = new ArrayList<>();
    	csCsiForCdeDetails.setAlternateDefinitions(alternateDefCsCsiList);
    	AlternateDefinitionCsCsi defCsCsi;
    	if (unclassCsCsi.getAlternateDefinitions() != null) {
	    	for (AlternateDefinition curr: unclassCsCsi.getAlternateDefinitions()) {
	    		defCsCsi = new AlternateDefinitionCsCsi(curr);//
	    		alternateDefCsCsiList.add(defCsCsi);
	    	}
    	}
    	return csCsiForCdeDetails;
	}
    /**
     * Populates service representation object dataElement with ReferenceDocuments from DB model dataElementModel
     * @param dataElementModel contains Data from DB
     * @param dataElement to fill with data to return JSON from the service
     */
	protected void getDataElementReferenceDocuments( DataElementModel dataElementModel, DataElement dataElement )
    {
		getDataElementReferenceDocuments(dataElementModel, dataElement, false);//called from Compare service
    }
	protected void getDataElementReferenceDocuments( DataElementModel dataElementModel, DataElement dataElement, boolean toCdeDetailsView )
    {
        // List to populate for client side
        List<ReferenceDocument> referenceDocuments = new ArrayList<>();
        dataElement.setReferenceDocuments( referenceDocuments );
        //CDEBROWSER-809 Separate out the Documents with Document Type containing "*Question Text"
        TreeSet<ReferenceDocument> questionTextReferenceDocumentSet = new TreeSet<>();//using TreeSet to keep a specific order
        TreeSet<ReferenceDocument> otherReferenceDocumentSet = new TreeSet<>();
        if (dataElementModel != null) {
	        //List from database
	        List<ReferenceDocModel> dataElementModelReferenceDocumentList = dataElementModel.getRefDocs();
	        if( dataElementModelReferenceDocumentList.size() < 1 )
	        {
	            logger.debug( "No ReferenceDocuments where returned for DE: " + dataElementModel.getCdeId() + ", " + dataElementModel.getIdseq());
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
	            if (toCdeDetailsView) {
		            //CDEBROWSER-809 "Separate out the Documents with Document Type containing "*Question Text" "
		            String refDocType = referenceDoc.getDocumentType();
		            refDocType = (refDocType == null) ? "" : refDocType;
		            if (refDocType.contains("Question Text")) {
		            	questionTextReferenceDocumentSet.add(referenceDoc);
		            }
		            else {
		            	otherReferenceDocumentSet.add(referenceDoc);
		            }
	            }
	            else {
	            	referenceDocuments.add( referenceDoc );//this one contains all ref docs
	            }
	        }
	        //Question Text changes Preferred Question Text goes first
	        List<ReferenceDocument> questionTextReferenceDocuments = new ArrayList<>(questionTextReferenceDocumentSet);
	        List<ReferenceDocument> otherReferenceDocuments = new ArrayList<>(otherReferenceDocumentSet);
	        dataElement.setQuestionTextReferenceDocuments(questionTextReferenceDocuments);
	        dataElement.setOtherReferenceDocuments(otherReferenceDocuments);
        }
    }

    private DataElement getDataElementDetails( DataElementModel dataElementModel )
    {
        DataElement dataElement = new DataElement();

        /////////////////////////////////////////////////////
        // "Data Element Details" of the "Data Element" Tab
        DataElementDetails dataElementDetails = new DataElementDetails();
        dataElement.setDataElementDetails( dataElementDetails );
        if( dataElementModel != null )
        {
            if( dataElementModel.getPublicId() == null )
            {
                dataElementDetails.setPublicId( -1 );
                logger.error( " dataElementModel.getPublicId() == null" );
            }
            else
            {
                dataElementDetails.setPublicId( dataElementModel.getPublicId() );
            }

            if( dataElementModel.getVersion() == null )
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
            dataElementDetails.setId(dataElementModel.getDeIdseq());//CDEBROWSER-868
        }
        return dataElement;
    }

    private DataElementConcept initDataElementConceptForCompare( DataElementModel dataElementModel )
    {        	    	
        DataElementConcept dataElementConcept = new DataElementConcept();
        DataElementConceptDetails dataElementConceptDetails = getDataElementConceptDetails( dataElementModel );
        dataElementConcept.setDataElementConceptDetails( dataElementConceptDetails );
        
        /////////////////////////////////////////////////////
        // "Object Class" of the "Data Element Concept" Table for Compare
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
        // "Object Class Concepts" of the "Data Element Concept" Table for Compare
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
        // "Property" of the "Data Element Concept" Table for Compare
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
        // "Property Concepts" of the "Data Element Concept" Table for Compare
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

        //CDEBROWSER-811 CDEBROWSER-825 CDEBROWSER-803 add Alternate Names and Definitions to DEC DE tab
        buildDecAltNamesDefinitions(dataElementModel.getDec(), dataElementConcept);

        return dataElementConcept;
    }
    
    //CDEBROWSER-811 CDEBROWSER-825 CDEBROWSER-803 Add Alternate sections to DEC details
    protected void buildDecAltNamesDefinitions(DataElementConceptModel dataElementConceptModel, DataElementConcept dataElementConcept) {
	    // add all the cscsi's and their designations and definitions except the unclassified ones
	    //change for CDEBROWSER-468
	    //now get the unclassified ones 
	    ////////////////////////////////////////////////////
	    //add unclassified first
    	if (dataElementConceptModel == null)
    		return;
    	if (dataElementConceptModel.getCsCsiData() == null) {
    		return;
    	}
    	CsCsiForCdeDetails csCsiForCdeDetails;
	    CsCsi unclassCsCsi = ControllerUtils.populateCsCsiUnclassified(dataElementConceptModel);
	    if (unclassCsCsi == null) {
	    	csCsiForCdeDetails = new CsCsiForCdeDetails();
	    	csCsiForCdeDetails.setAlternateDefinitions(new ArrayList<>());
	    	csCsiForCdeDetails.setAlternateNames(new ArrayList<>());
	    }
	    else {
	    	//we change the lists of alternate names
	    	csCsiForCdeDetails = rearrangeForCdeDetails(unclassCsCsi, false);
	    }
	    ////////////////////////////////////////////////////
	    List<CsCsi> csCsiClassifiedList = ControllerUtils.populateCsCsiModel( dataElementConceptModel.getDecIdseq(), csCsiDeDAO );
	    
	    //Add Alt Names and Definitions with CS/CSIs listed comma separated 
	    List<AlternateNameCsCsi> altNamesWithCsCsi = buildAltNamesWithCsCsi(csCsiClassifiedList);//could be null
	    List<AlternateNameCsCsi> altNamesWithCsCsiAll = csCsiForCdeDetails.getAlternateNames();//never null
	    if (altNamesWithCsCsi != null) {
	        //add classified designations to unclassified
	        for (AlternateNameCsCsi curr : altNamesWithCsCsi) {
	        	altNamesWithCsCsiAll.add(curr);
	        }
	    }
	    if (altNamesWithCsCsiAll != null)
	    	Collections.sort(altNamesWithCsCsiAll);//sort the list
	    dataElementConcept.setAlternateNames(altNamesWithCsCsiAll);
	    
	    List<AlternateDefinitionCsCsi> altDefinitionsWithCsCsiAll = csCsiForCdeDetails.getAlternateDefinitions();//never null
	    List<AlternateDefinitionCsCsi> definitionsWithCsCsi = buildDefinitionsWithCsCsi(csCsiClassifiedList);//could be null
	    if (definitionsWithCsCsi != null) {
	        //add classified definitions to unclassified
	        for (AlternateDefinitionCsCsi curr : definitionsWithCsCsi) {
	        	altDefinitionsWithCsCsiAll.add(curr);
	        }
	    }
	    if (altDefinitionsWithCsCsiAll != null)
	    	Collections.sort(altDefinitionsWithCsCsiAll);//sort the list
	    dataElementConcept.setAlternateDefinitions(altDefinitionsWithCsCsiAll);
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
        dataElementConceptDetails.setConceptualDomainLongName( dataElementModel.getDec().getCdLongName() );//CDEBROWSER-816 used DEC Long Name
        dataElementConceptDetails.setConceptualDomainRegStatus( dataElementModel.getDec().getCdRegistrationStatus());//CDEBROWSER-816 add DEC Reg Status

        //Conceptual Domain Version
        String strVersion = dataElementModel.getDec().getCdVersion().toString();
        dataElementConceptDetails.setConceptualDomainVersion(strVersion);
        dataElementConceptDetails.setFormattedConceptualDomainVersion(strVersion);
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
            //logger.debug( "Representation Concepts of the value Domain" );
            representationConcepts = representationConceptsDAO.getRepresentationConceptByRepresentationId( dataElementModel.getValueDomainModel().getRepresentationModel().getPublicId() );
        }

        valueDomain.setRepresentationConcepts( representationConcepts );


        /////////////////////////////////////////////////////
        // "Permissible Values" of the "value Domain" Tab
        List<PermissibleValuesModel> permissibleValues = permissibleValuesDAO.getPermissibleValuesByVdIdseq( dataElementModel.getValueDomainModel().getVdIdseq() );
        valueDomain.setPermissibleValues( permissibleValues );
        logger.debug( "PermissibleValues count: " + permissibleValues.size() );
        if( logger.isTraceEnabled() )
        {
            for( PermissibleValuesModel permissibleValuesModel : permissibleValues )
            {
                logger.trace( "PermissibleValues: " + permissibleValuesModel.getValue() );
                logger.trace( "PermissibleValues: " + permissibleValuesModel.getShortMeaning() );
                logger.trace( "PermissibleValues: " + permissibleValuesModel.getMeaningDescription() );
            }
        }
        //add PV VM designations (AKA alternate names) and definitions CDEBROWSER-457
        logger.debug( "PermissibleValueExtList section start......." );

        //FIXME this requirement is changed not to use Classification information in PV and VM
        //List<PermissibleValueExt> permissibleValueExtList = buildPermissibleValueExtList(permissibleValues);
        /*  JIRA 658
        List<PermissibleValueExt> permissibleValueExtList = new ArrayList<>();
        valueDomain.setPermissibleValueExtList( permissibleValueExtList );
        logger.debug( ".........PermissibleValueExtList section done" );
        */

        /////////////////////////////////////////////////////
        // "Reference Documents" of the "value Domain" Tab
        List<ReferenceDocModel> valueDomainReferenceDocuments = referenceDocDAO.getRefDocsByAcIdseq( dataElementModel.getValueDomainModel().getVdIdseq() );
        valueDomain.setValueDomainReferenceDocuments( valueDomainReferenceDocuments );

        // Add List of valueMeaning here
        valueDomain.setValueMeaning( valueMeaningDAO.getUiValueMeanings( dataElementModel.getCdeId(), dataElementModel.getVersion() ) );

        return valueDomain;
    }

    /////////////////////////////////////////////////////
    // "Permissible Values Ext " of the "Value Domain" Tab
    protected List<PermissibleValueExt> buildPermissibleValueExtList( List<PermissibleValuesModel> permissibleValues )
    {
        List<PermissibleValueExt> permissibleValueExtList = new ArrayList<PermissibleValueExt>();
        for( PermissibleValuesModel permissibleValuesModel : permissibleValues )
        {
            PermissibleValueExt e = buildPermissibleValueExt( permissibleValuesModel );
            permissibleValueExtList.add( e );
        }
        return permissibleValueExtList;
    }

    protected PermissibleValueExt buildPermissibleValueExt( PermissibleValuesModel permissibleValueModel )
    {
        PermissibleValueExt permissibleValueExt = new PermissibleValueExt();
        //build representation from DB model class
        //every PV has one reference to VM
        permissibleValueExt.setPvIdseq( permissibleValueModel.getPvIdseq() );
        permissibleValueExt.setPvMeaning( permissibleValueModel.getMeaningDescription() );
        permissibleValueExt.setVmPublicId( permissibleValueModel.getVmId() );
        permissibleValueExt.setVmVersion( permissibleValueModel.getVmVersion() );

        //this is ID to get designations and definitions
        String vmIdseq = permissibleValueModel.getVmIdseq();
        //get classification model
        List<CsCsiValueMeaningModel> modelList = csCsiValueMeaningDAO.getCsCsisByVmId( vmIdseq );
        CsCsiValueMeaningModelList csCsiValueMeaningModelList = new CsCsiValueMeaningModelList( modelList );
        List<DesignationModelAlt> designationList = designationDAO.getDesignationModelsNoClsssification( vmIdseq );
        List<DefinitionModelAlt> definitionList = definitionDAO.getAllDefinitionsNoClassification( vmIdseq );

        List<ClassificationSchemaAlternate> classificationSchemaAlternateList = buildClassificationSchemaVmList( csCsiValueMeaningModelList, designationList, definitionList );
        permissibleValueExt.setClassificationSchemaList( classificationSchemaAlternateList );
        return permissibleValueExt;
    }

    /**
     * @param csCsiValueMeaningModelList
     * @param designationList
     * @param definitionList
     * @return list of representation classes to send to the client
     */
    protected List<ClassificationSchemaAlternate> buildClassificationSchemaVmList(
            CsCsiValueMeaningModelList csCsiValueMeaningModelList,
            List<DesignationModelAlt> designationList, List<DefinitionModelAlt> definitionList )
    {
        List<ClassificationSchemaAlternate> classificationSchemaAlternateList = new ArrayList<>();
        ClassificationSchemaAlternate unclassified = prepareUnclassified( csCsiValueMeaningModelList, designationList, definitionList );
        //add unclassified section of this PV's VM
        if( unclassified != null )
            classificationSchemaAlternateList.add( unclassified );
        //classified
        prepareClassified( classificationSchemaAlternateList, csCsiValueMeaningModelList, designationList, definitionList );
        return classificationSchemaAlternateList;
    }

    protected void prepareClassified(
            List<ClassificationSchemaAlternate> classificationSchemaAlternateList, CsCsiValueMeaningModelList csCsiValueMeaningModelList,
            List<DesignationModelAlt> designationList, List<DefinitionModelAlt> definitionList )
    {
        List<CsCsiValueMeaningModel> csCsiAttModelList = csCsiValueMeaningModelList.getModels();
        if( ( csCsiAttModelList == null ) || ( csCsiAttModelList.isEmpty() ) )
            return;

        String groupCsIdseq = csCsiAttModelList.get( 0 ).getCsIdseq();
        String currCsIdseq = groupCsIdseq;
        String groupCsiIdseq = csCsiAttModelList.get( 0 ).getCsiIdseq();
        String currCsiIdseq = groupCsiIdseq;

        ClassificationSchemaAlternate currEntity = new ClassificationSchemaAlternate( csCsiAttModelList.get( 0 ) );

        for( CsCsiValueMeaningModel csCsiAttModel : csCsiAttModelList )
        {
            currCsIdseq = csCsiAttModel.getCsIdseq();
            currCsiIdseq = csCsiAttModel.getCsiIdseq();

            if( !( groupCsIdseq.equals( currCsIdseq ) ) || ( !( groupCsiIdseq.equals( currCsiIdseq ) ) ) )
            {
                //add previous classification
                classificationSchemaAlternateList.add( currEntity );
                groupCsIdseq = currCsIdseq;
                groupCsiIdseq = currCsiIdseq;
                //Start a new Classification group
                currEntity = new ClassificationSchemaAlternate( csCsiAttModel );
            }

            for( DesignationModelAlt designationModel : designationList )
            {
                if( csCsiValueMeaningModelList.isAttClassified( designationModel.getDesigIdseq() ) )
                {
                    if( csCsiAttModel.getAttIdseq().equals( designationModel.getDesigIdseq() ) )
                    {
                        currEntity.addAlternateName( new AlternateName( designationModel ) );
                    }
                }
            }
            for( DefinitionModelAlt definitionModel : definitionList )
            {
                if( csCsiValueMeaningModelList.isAttClassified( definitionModel.getDefinIdseq() ) )
                {
                    if( csCsiAttModel.getAttIdseq().equals( definitionModel.getDefinIdseq() ) )
                    {
                        currEntity.addDefinition( new AlternateDefinition( definitionModel ) );
                    }
                }
            }
        }
        classificationSchemaAlternateList.add( currEntity );
    }

    protected ClassificationSchemaAlternate prepareUnclassified(
            CsCsiValueMeaningModelList csCsiValueMeaningModelList,
            List<DesignationModelAlt> designationList, List<DefinitionModelAlt> definitionList )
    {
        ClassificationSchemaAlternate unclassified = new ClassificationSchemaAlternate();
        unclassified.setCsLongName( "No Classification" );
        unclassified.setCsDefinition( CsCsiModel.UNCLASSIFIED );

        for( DesignationModelAlt designationModel : designationList )
        {
            if( !csCsiValueMeaningModelList.isAttClassified( designationModel.getDesigIdseq() ) )
            {
                unclassified.addAlternateName( new AlternateName( designationModel ) );
            }
        }
        for( DefinitionModelAlt definitionModel : definitionList )
        {
            if( !csCsiValueMeaningModelList.isAttClassified( definitionModel.getDefinIdseq() ) )
            {
                unclassified.addDefinition( new AlternateDefinition( definitionModel ) );
            }
        }
        if( ( unclassified.getAlternateNames().isEmpty() ) && ( unclassified.getDefinitions().isEmpty() ) )
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

        //CDEBROWSER-760 Use CD Context here, not DE Context as it was done in v.5.2.2
        valueDomainDetails.setContext(dataElementModel.getValueDomainModel().getVdContextName());

        valueDomainDetails.setDefinition( dataElementModel.getValueDomainModel().getPreferredDefinition() );
        valueDomainDetails.setWorkflowStatus( dataElementModel.getValueDomainModel().getAslName() );
        valueDomainDetails.setRegistrationStatus(dataElementModel.getRegistrationStatus()); // CDEBROWSER-832 UI Edits - Value Domain View Details - Backend
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
        valueDomainDetails.setConceptualDomainLongName( dataElementModel.getValueDomainModel().getCdLongName() ); //CDEBROWSER-798 UI Edits and Fixes - Compare Screen Matrix
        valueDomainDetails.setConceptualDomainVersion( dataElementModel.getValueDomainModel().getCdVersion() );
        valueDomainDetails.setOrigin( dataElementModel.getValueDomainModel().getOrigin() );
        AcRegistrationsModel vdRegModel = dataElementModel.getValueDomainModel().getVdRegistrationsModel();
        if (vdRegModel != null) {
        	valueDomainDetails.setVdRegistrationStatus(vdRegModel.getRegistrationStatus());
        }
        return valueDomainDetails;
    }

    private Classifications initClassificationsForCompare( DataElementModel dataElementModel )
    {
    	 		
        Classifications classifications = new Classifications();
        
        classifications.setSelectedDataElement( getSelectedDataElement( dataElementModel ) );        
        
        List<CsCsi> classificationList = new ArrayList<>();
        classifications.setClassificationList( classificationList );
        // commented to fix  CDEBROWSER-729 Classifications table on the CDE compare page is empty
        
        /*if( classificationList.size() < 1 )
        {
            logger.debug( "No ReferenceDocuments where returned" );
            classifications = null;
        }*/
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
            logger.error( "no usage models publicId: " + dataElementModel.getPublicId());
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
        //CDEBROWSER-837 Using CDE IDSEQ to search; CDE ID is shared by different CDE Versions
        DataElementDerivationModel dataElementDerivationModel = dataElementDerivationDAO.getDataElementDerivationByCdeIdseq(dataElementModel.getDeIdseq());

        // If dataElementDerivationModel == null then this Data element is not a derived data element.
        if( dataElementDerivationModel != null )
        {
            logger.debug( "dataElementDerivationModel: " + dataElementDerivationModel.toString() );
            dataElementDerivation.setDataElementDerivationDetails( dataElementDerivationModel );
           //CDEBROWSER-837 Using CDE IDSEQ to search; CDE ID is shared by different CDE Versions
            List<DataElementDerivationComponentModel> dataElementDerivationComponentModels = dataElementDerivationDAO.getDataElementDerivationComponentsByCdeIdseq(dataElementModel.getDeIdseq());
            if (dataElementDerivationComponentModels.size() > 0) {
            	dataElementDerivation.setDataElementDerivationComponentModels( dataElementDerivationComponentModels );
            } else {
            	dataElementDerivation.setDataElementDerivationComponentModels(null);
            }

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
        adminInfo.setVdCreatedBy( dataElementModel.getValueDomainModel().getCreatedBy() );
        adminInfo.setVdOwnedBy( dataElementModel.getValueDomainModel().getVdContextName() );
        adminInfo.setDecCreatedBy( dataElementModel.getDec().getCreatedBy() );
        adminInfo.setDecOwnedBy(dataElementModel.getDec().getConteName() );
        if (dataElementModel.getValueDomainModel().getDateCreated()!=null)
        	adminInfo.setVdDateCreated(new SimpleDateFormat( CaDSRConstants.DATE_FORMAT ).format(dataElementModel.getValueDomainModel().getDateCreated()));
        if (dataElementModel.getValueDomainModel().getDateModified()!=null)
        	adminInfo.setVdDateModified(new SimpleDateFormat( CaDSRConstants.DATE_FORMAT ).format(dataElementModel.getValueDomainModel().getDateModified()));
        if (dataElementModel.getDec().getDateCreated()!=null)
        	adminInfo.setDecDateCreated(new SimpleDateFormat( CaDSRConstants.DATE_FORMAT ).format(dataElementModel.getDec().getDateCreated()));
        if (dataElementModel.getDec().getDateModified()!=null)
        	adminInfo.setDecDateModified(new SimpleDateFormat( CaDSRConstants.DATE_FORMAT ).format(dataElementModel.getDec().getDateModified()));
        adminInfo.setChangeNote(dataElementModel.getChangeNote());
        
        
        // "Selected Data Element" of the "Admin Info" Tab
        adminInfo.setSelectedDataElement( getSelectedDataElement( dataElementModel ) ); // CDEBROWSER-833 UI Edits and fixes - Admin View Details - Backend
        
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

    public void setCsCsiValueMeaningDAO(CsCsiValueMeaningDAO csCsiValueMeaningDAO) {
		this.csCsiValueMeaningDAO = csCsiValueMeaningDAO;
	}
	public void setDesignationDAO(DesignationDAO designationDAO) {
		this.designationDAO = designationDAO;
	}
	public void setCsCsiDeDAO(CsCsiDeDAO csCsiDeDAO) {
		this.csCsiDeDAO = csCsiDeDAO;
	}
	public void setValueMeaningDAO(ValueMeaningDAO valueMeaningDAO) {
		this.valueMeaningDAO = valueMeaningDAO;
	}
	public void setUsageLog(UsageLog usageLog) {
		this.usageLog = usageLog;
	}
	
	public ToolOptionsDAO getToolOptionsDAO()
    {
        return toolOptionsDAO;
    }

    public void setToolOptionsDAO( ToolOptionsDAO toolOptionsDAO )
    {
        this.toolOptionsDAO = toolOptionsDAO;
    }

}
