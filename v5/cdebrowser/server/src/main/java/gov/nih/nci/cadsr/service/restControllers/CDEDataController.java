package gov.nih.nci.cadsr.service.restControllers;

import gov.nih.nci.cadsr.dao.DataElementDAOImpl;
import gov.nih.nci.cadsr.dao.model.*;
import gov.nih.nci.cadsr.service.model.cdeData.CdeDetails;
import gov.nih.nci.cadsr.service.model.cdeData.DataElementConcept.*;
import gov.nih.nci.cadsr.service.model.cdeData.SelectedDataElement;
import gov.nih.nci.cadsr.service.model.cdeData.classifications.Classification;
import gov.nih.nci.cadsr.service.model.cdeData.classifications.Classifications;
import gov.nih.nci.cadsr.service.model.cdeData.classifications.ClassificationsSchemeItemReferenceDocument;
import gov.nih.nci.cadsr.service.model.cdeData.classifications.ClassificationsScheneRefernceDocument;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.*;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.ReferenceDocument;
import gov.nih.nci.cadsr.service.model.cdeData.dataElementDerivation.DataElementDerivation;
import gov.nih.nci.cadsr.service.model.cdeData.usage.FormUsage;
import gov.nih.nci.cadsr.service.model.cdeData.usage.Usage;
import gov.nih.nci.cadsr.service.model.cdeData.valueDomain.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
public class CDEDataController
{
    private Logger logger = LogManager.getLogger( CDEDataController.class.getName() );
    private DataElementDAOImpl dataElementDAO;

    @RequestMapping( value = "/CDEData" )
    @ResponseBody
    public CdeDetails CDEDataController( @RequestParam( "deIdseq" ) String deIdseq )
    {
        logger.debug( "Received rest call \"CDEData\": " + deIdseq );
        logger.debug( "Received rest call \"CDEData\":  dataElementDAO.getCdeByDeIdseq( " + deIdseq + ")" );

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
        } else
        {
            dataElementDetails.setPublicId( dataElementModel.getPublicId() );
        }

        if( dataElementModel.getVersion() == null )
        {
            dataElementDetails.setVersion( -1 );
            logger.error( " dataElementModel.getVersion() == null" );
        } else
        {
            dataElementDetails.setVersion( dataElementModel.getVersion() );
        }

        dataElementDetails.setLongName( dataElementModel.getLongName() );
        dataElementDetails.setShortName( dataElementModel.getPreferredName() );
        dataElementDetails.setPreferredQuestionText( dataElementModel.getPreferredQuestionText() );
        dataElementDetails.setDefinition( dataElementModel.getPreferredDefinition() );
        dataElementDetails.setValueDomain( dataElementModel.getValueDomainModel().getLongName() );
        dataElementDetails.setDataElementConcept( "STILL NEED TO TRACK DOWN Data Element Concept" );
        dataElementDetails.setContext( dataElementModel.getContextName() );
        dataElementDetails.setWorkflowStatus( dataElementModel.getAslName() );
        dataElementDetails.setOrigin( dataElementModel.getOrigin() );
        dataElementDetails.setRegistrationStatus( dataElementModel.getRegistrationStatus() );
        //dataElementDetails.setDirectLink( "STILL NEED TO TRACK DOWN Direct Link" );

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
            logger.debug( referenceDocModel.getDocName() );

            referenceDoc.setDocumentType( referenceDocModel.getDocType() );
            logger.debug( referenceDocModel.getDocType() );

            referenceDoc.setDocumentText( referenceDocModel.getDocText() );
            logger.debug( referenceDocModel.getDocText() );

            referenceDoc.setContext( referenceDocModel.getContext().getName() );
            logger.debug( referenceDocModel.getContext().getName() );

            referenceDoc.setUrl( referenceDocModel.getUrl() );
            logger.debug( referenceDocModel.getUrl() );

            referenceDocuments.add( referenceDoc );
        }

        /////////////////////////////////////////////////////
        // "Alternate Names" of the "Data Element" Tab

        // List to populate for client side
        List<AlternateName> alternateNames = new ArrayList<>();
        dataElement.setAlternateNames( alternateNames );
        List<DesignationModel> designationModels = dataElementModel.getDesignationModels();
        for( DesignationModel designationModel : designationModels )
        {
            AlternateName alternateName = new AlternateName();
            alternateName.setName( designationModel.getName() );
            alternateName.setType( designationModel.getType() );
            alternateName.setContext( designationModel.getContex().getName() );
            alternateName.setLanguage( designationModel.getContex().getLanguage() );
            alternateNames.add( alternateName );
        }

        /////////////////////////////////////////////////////
        // "Alternate Definitions" of the "Data Element" Tab
        List<AlternateDefinition> alternateDefinitions = new ArrayList<>();
        dataElement.setAlternateDefinitions( alternateDefinitions );
        // FIXME - Need to find out where to get list of alternateDefinitions from dataElementModel


        /////////////////////////////////////////////////////
        // "Other Versions" of the "Data Element" Tab
        List<OtherVersion> otherVersions = new ArrayList<>();
        dataElement.setOtherVersions( otherVersions );

        // FIXME - Need to find out where to get Data otherVersions from dataElementModel


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

        // FIXME - Need to find out where to get Data Element Concept Details from dataElementModel
        // dataElementConceptDetails.setPublicId( dataElementModel. );


        /////////////////////////////////////////////////////
        // "Object Class" of the "Data Element Concept" Tab
        ObjectClass objectClass = new ObjectClass();
        dataElementConcept.setObjectClass( objectClass );

        // FIXME - Need to find out where to get Object Class from dataElementModel
        //objectClass.setPublicId( dataElementModel.);


        /////////////////////////////////////////////////////
        // "Object Class Concepts" of the "Data Element Concept" Tab
        // This is a list of ObjectClassConcept

        List<ObjectClassConcept> objectClassConcepts = new ArrayList<>();
        dataElementConcept.setObjectClassConcepts( objectClassConcepts );

        // FIXME - Need to find out where to get List of Object Class Concepts from dataElementModel


        /////////////////////////////////////////////////////
        // "Property" of the "Data Element Concept" Tab
        Property property = new Property();
        dataElementConcept.setProperty( property );

        // FIXME - Need to find out where to get property from dataElementModel
        //property.setPublicId( dataElementModel. );


        /////////////////////////////////////////////////////
        // "Property Concepts" of the "Data Element Concept" Tab
        // This is a list of PropertyConcept

        List<PropertyConcept> propertyConcepts = new ArrayList<>();
        dataElementConcept.setPropertyConcepts( propertyConcepts );

        // FIXME - Need to find out where to get list of Property Concept from dataElementModel


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
        valueDomainDetails.setContext( dataElementModel.getValueDomainModel().getCdContextName() );
        valueDomainDetails.setWorkflowStatus( dataElementModel.getValueDomainModel().getAslName() );
        valueDomainDetails.setDataType( dataElementModel.getValueDomainModel().getDatatype() );
        valueDomainDetails.setUnitOfMeasure( dataElementModel.getValueDomainModel().getUom() );
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

        /////////////////////////////////////////////////////
        // "value Domain Concepts" of the "value Domain" Tab
        //Just a string
        valueDomain.setValueDomainConcepts( "STILL NEED TO TRACK DOWN ValueDomainConcepts" );


        /////////////////////////////////////////////////////
        // "Representation" of the "value Domain" Tab
        Representation representation = new Representation();
        valueDomain.setRepresentation( representation );

        representation.setPublicId( dataElementModel.getValueDomainModel().getPublicId() );
        if( dataElementModel.getValueDomainModel().getVersion() != null )
        {
            representation.setVersion( dataElementModel.getValueDomainModel().getVersion() );
        }
        representation.setLongName( dataElementModel.getValueDomainModel().getLongName() );
        representation.setShortName( dataElementModel.getValueDomainModel().getPreferredName() );
        representation.setContext( dataElementModel.getValueDomainModel().getCdContextName() );


        /////////////////////////////////////////////////////
        // "Representation Concepts" of the "value Domain" Tab
        List<RepresentationConcept> representationConcepts = new ArrayList<>();
        valueDomain.setRepresentationConcepts( representationConcepts );
        // FIXME - Need to find out where to get Representation Concepts from dataElementModel
        //dataElementModel.getValueDomainModel().getConceptDerivationRuleModel().

        /////////////////////////////////////////////////////
        // "Permissible Values" of the "value Domain" Tab
        List<PermissibleValue> permissibleValues = new ArrayList<>();
        valueDomain.setPermissibleValues( permissibleValues );
        // FIXME - Need to find out where to get Permissible Value List from dataElementModel

        /////////////////////////////////////////////////////
        // "Reference Documents" of the "value Domain" Tab
        List<ValueDomainReferenceDocument> valueDomainReferenceDocuments = new ArrayList<>();
        valueDomain.setValueDomainReferenceDocuments( valueDomainReferenceDocuments );

        // FIXME - Need to find out where to get Reference Documents List from dataElementModel
        //dataElementModel.getValueDomainModel().

        return valueDomain;
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
        List<Classification> classificationList = new ArrayList<>();
        classifications.setClassificationList( classificationList );
        // FIXME - Need to find out where to get classifications List from dataElementModel

        /////////////////////////////////////////////////////
        // "Classifications" section of the "Classifications" tab
        List<ClassificationsScheneRefernceDocument> classificationsScheneRefernceDocuments = new ArrayList<>();
        classifications.setClassificationsScheneRefernceDocuments( classificationsScheneRefernceDocuments );
        // FIXME - Need to find out where to get classificationsScheneRefernceDocuments List from dataElementModel

        /////////////////////////////////////////////////////
        // "Classification Scheme Item Reference Document
        ClassificationsSchemeItemReferenceDocument classificationsSchemeItemReferenceDocument = new ClassificationsSchemeItemReferenceDocument();
        classifications.setClassificationsSchemeItemReferenceDocument( classificationsSchemeItemReferenceDocument );
        // FIXME - Need to find out where to get classificationsSchemeItemReferenceDocument List from dataElementModel


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
        // FIXME - Need to find out where to get FormUsage List from dataElementModel
        //dataElementModel.getUsingContexts()

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

        // "Selected Data Element" of the "Data Elements Derivation" Tab
        dataElementDerivation.setSelectedDataElement( getSelectedDataElement( dataElementModel ) );


        /////////////////////////////////////////////////////
        // FIXME - Need to find out where to get dataElementDerivationDetails from dataElementModel

        return dataElementDerivation;
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
        } else
        {
            selectedDataElement.setPublicId( dataElementModel.getPublicId() );
        }

        if( dataElementModel.getVersion() == null )
        {
            selectedDataElement.setVersion( -1 );
            logger.error( " dataElementModel.getVersion() == null" );
        } else
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


    public void setDataElementDAO( DataElementDAOImpl dataElementDAO )
    {
        this.dataElementDAO = dataElementDAO;
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
        dataElementConceptModel.setObjClassPublicId( "DataElementConceptModel ObjClassPublicId" );
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
