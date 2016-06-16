package gov.nih.nci.cadsr.service.restControllers;
/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
import gov.nih.nci.cadsr.dao.model.DefinitionModel;
import gov.nih.nci.cadsr.dao.model.DesignationModel;
import gov.nih.nci.cadsr.dao.model.ObjectClassModel;
import gov.nih.nci.cadsr.dao.model.PermissibleValuesModel;
import gov.nih.nci.cadsr.dao.model.PropertyModel;
import gov.nih.nci.cadsr.dao.model.ReferenceDocModel;
import gov.nih.nci.cadsr.dao.model.RepresentationModel;
import gov.nih.nci.cadsr.dao.model.ToolOptionsModel;
import gov.nih.nci.cadsr.dao.model.UsageModel;
import gov.nih.nci.cadsr.dao.model.ValueDomainModel;
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

@RestController
public class CDEDataController
{
    private Logger logger = LogManager.getLogger( CDEDataController.class.getName() );

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
    
    
    @RequestMapping( value = "/CDEData" )
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
        DataElement dataElement = new DataElement();

        /////////////////////////////////////////////////////
        // "Data Element Details" of the "Data Element" Tab
        DataElementDetails dataElementDetails = new DataElementDetails();
        dataElement.setDataElementDetails( dataElementDetails );

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

        /////////////////////////////////////////////////////
        // "Reference Documents" of the "Data Element" Tab

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
                        // call the AlternateName() constructor that takes a Designation model, giving it the model found by its index
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
        DataElementConceptDetails dataElementConceptDetails = new DataElementConceptDetails();
        dataElementConcept.setDataElementConceptDetails( dataElementConceptDetails );

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
        ValueDomainDetails valueDomainDetails = new ValueDomainDetails();
        valueDomain.setValueDomainDetails( valueDomainDetails );

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
        for( PermissibleValuesModel permissibleValuesModel : permissibleValues )
        {
            logger.debug( "PermissibleValues: " + permissibleValuesModel.getValue() );
            logger.debug( "PermissibleValues: " + permissibleValuesModel.getShortMeaning() );
            logger.debug( "PermissibleValues: " + permissibleValuesModel.getMeaningDescription() );
        }
        
        //add PV VM designations (AKA alternate names) and definitions CDEBROWSER-457
        List<PermissibleValueExt> permissibleValueExtList = buildPermissibleValueExtList(permissibleValues);
        valueDomain.setPermissibleValueExtList(permissibleValueExtList);
        
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
    	logger.debug("vmIdseq: " + vmIdseq);
    	CsCsiValueMeaningModelList csCsiValueMeaningModelList = new CsCsiValueMeaningModelList(modelList);
    	List<DefinitionModel> definitionList = definitionDAO.getAllDefinitionsByAcIdseq(vmIdseq);
    	List<DesignationModel> designationList = designationDAO.getDesignationModelsByAcIdseq(vmIdseq);
    	
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
    		List<DesignationModel> designationList, List<DefinitionModel> definitionList) {
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
    		List<DesignationModel> designationList, List<DefinitionModel> definitionList) {
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
    		//an alternative is to compare based on csiIdseqs in DB model classes
        	for (DesignationModel designationModel : designationList) {
        		if (csCsiValueMeaningModelList.isAttClassified(designationModel.getDesigIDSeq())) {
        			if (csCsiAttModel.getAttIdseq().equals(designationModel.getDesigIDSeq())) {
        				currEntity.addAlternateName(new AlternateName(designationModel));
        			}
        		}
        	}
        	for (DefinitionModel definitionModel : definitionList) {
        		if (! csCsiValueMeaningModelList.isAttClassified(definitionModel.getAcIdseq())) {
        			if (csCsiAttModel.getAttIdseq().equals(definitionModel.getDefinIdseq())) {
        				currEntity.addDefinition(new AlternateDefinition(definitionModel));
        			}
        		}
        	}
    	}
		classificationSchemaAlternateList.add(currEntity);
    }
    
    protected ClassificationSchemaAlternate prepareUnclassified(CsCsiValueMeaningModelList csCsiValueMeaningModelList,
    		List<DesignationModel> designationList, List<DefinitionModel> definitionList) {
    	ClassificationSchemaAlternate unclassified = new ClassificationSchemaAlternate();
    	unclassified.setCsLongName("No Classification");
    	unclassified.setCsDefinition(CsCsiModel.UNCLASSIFIED);//TODO is this the right field?

    	for (DesignationModel designationModel : designationList) {
    		if (! csCsiValueMeaningModelList.isAttClassified(designationModel.getDesigIDSeq())) {
    			unclassified.addAlternateName(new AlternateName(designationModel));
    		}
    	}
    	for (DefinitionModel definitionModel : definitionList) {
    		if (! csCsiValueMeaningModelList.isAttClassified(definitionModel.getDefinIdseq())) {
    			unclassified.addDefinition(new AlternateDefinition(definitionModel));
    		}
    	}
    	if ((unclassified.getAlternateNames().isEmpty())&& (unclassified.getDefinitions().isEmpty()))
    		return null;
    	else 
    		return unclassified;
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

        DataElementDerivationModel dataElementDerivationModel = dataElementDerivationDAO.getDataElementDerivationByCdeId( dataElementModel.getCdeId() );

        // If dataElementDerivationModel == null then this Data element is not a derived data element.
        if( dataElementDerivationModel != null )
        {
            logger.debug( "dataElementDerivationModel: " + dataElementDerivationModel.toString() );
            dataElementDerivation.setDataElementDerivationDetails( dataElementDerivationModel );
            List<DataElementDerivationComponentModel> dataElementDerivationComponentModels = dataElementDerivationDAO.getDataElementDerivationComponentsByCdeId( dataElementModel.getCdeId() );
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
        adminInfo.setDateCreated( new SimpleDateFormat( CaDSRConstants.DATE_FORMAT ).format( dataElementModel.getDateCreated() ) );
        adminInfo.setModifiedBy( dataElementModel.getModifiedBy() );
        adminInfo.setDateModified( new SimpleDateFormat( CaDSRConstants.DATE_FORMAT ).format( dataElementModel.getDateModified() ) );
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
