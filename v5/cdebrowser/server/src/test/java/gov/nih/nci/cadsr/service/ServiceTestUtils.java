/**
 * Copyright (C) 2016 Leidos Biomedical Research, Inc. - All rights reserved.
 */
package gov.nih.nci.cadsr.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import gov.nih.nci.cadsr.dao.model.ConceptDerivationRuleModel;
import gov.nih.nci.cadsr.dao.model.ContextModel;
import gov.nih.nci.cadsr.dao.model.DataElementConceptModel;
import gov.nih.nci.cadsr.dao.model.DataElementModel;
import gov.nih.nci.cadsr.dao.model.DesignationModel;
import gov.nih.nci.cadsr.dao.model.ObjectClassModel;
import gov.nih.nci.cadsr.dao.model.PropertyModel;
import gov.nih.nci.cadsr.dao.model.ReferenceDocModel;
import gov.nih.nci.cadsr.dao.model.RepresentationModel;
import gov.nih.nci.cadsr.dao.model.SearchModel;
import gov.nih.nci.cadsr.dao.model.ValueDomainModel;
import gov.nih.nci.cadsr.service.model.cdeData.CdeDetails;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.AlternateName;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.DataElement;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.DataElementDetails;
import gov.nih.nci.cadsr.service.model.cdeData.dataElement.ReferenceDocument;
import gov.nih.nci.cadsr.service.model.search.SearchCriteria;
import gov.nih.nci.cadsr.service.model.search.SearchNode;
/**
 * This code is moved from CDEDataController in v. 5.3
 * @author asafievan
 *
 */
public class ServiceTestUtils {
	/////////////////////////////////////////////////////
    /////////////////////////////////////////////////////
    /////////////////////////////////////////////////////
    // Test stuff


    public static CdeDetails buildTestRecord()
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

    public static DataElementModel buildTestRecordDataElementModel()
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
        dataElementModel.setDesignationModels( buildTestDesignationModelList() );

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
    public static List<DesignationModel> buildTestDesignationModelList()
    {
        List<DesignationModel> designationModelList = new ArrayList<>();
        designationModelList.add( buildTestDesignationModel( "A" ) );
        designationModelList.add( buildTestDesignationModel( "B" ) );
        designationModelList.add( buildTestDesignationModel( "C" ) );

        return designationModelList;
    }

    public static DesignationModel buildTestDesignationModel( String text )
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

    public static ValueDomainModel buildTestValueDomainModel()
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

    public static RepresentationModel buildTestRepresentationModel()
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

    public static DataElementConceptModel buildTestDataElementConceptModel()
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

    public static ObjectClassModel buildTestObjectClassModel()
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
    public static PropertyModel buildTestPropertyModel()
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
    public static List<ReferenceDocModel> buildTestReferenceDocModelList()
    {
        List<ReferenceDocModel> referenceDocModelList = new ArrayList<>();

        referenceDocModelList.add( buildTestReferenceDocModel( "ref doc model A" ) );
        referenceDocModelList.add( buildTestReferenceDocModel( "ref doc model B" ) );
        referenceDocModelList.add( buildTestReferenceDocModel( "ref doc model C" ) );
        return referenceDocModelList;
    }

    public static ReferenceDocModel buildTestReferenceDocModel( String text )
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
    /**
     * We need particular type for some tests as ended with "Question Text".
     * 
     * @param text
     * @param typeSuffix
     * @return ReferenceDocModel
     */
    public static ReferenceDocModel buildTestReferenceDocModel( String text, String typeSuffix)
    {
        ReferenceDocModel referenceDocModel = new ReferenceDocModel();

        referenceDocModel.setCreatedBy( "buildTestReferenceDocModel DateCreated" );
        referenceDocModel.setDateCreated( getDate() );
        referenceDocModel.setModifiedBy( "buildTestReferenceDocModel DateModified" );
        referenceDocModel.setDateModified( getDate() );

        referenceDocModel.setDocName( "DocName_" + text );
        referenceDocModel.setDocType( "DocType_" + text + typeSuffix);
        referenceDocModel.setDocIDSeq( "IDSeq_" + text );
        referenceDocModel.setDocText( "DocText_" + text );
        referenceDocModel.setLang( "Lang_" + text );
        referenceDocModel.setUrl( "URL_" + text );
        referenceDocModel.setContext( buildTestContextModel() );

        return referenceDocModel;
    }

    public static ContextModel buildTestContextModel()
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

    public static Timestamp getDate()
    {
        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        return new java.sql.Timestamp( now.getTime() );
    }
    public static boolean comparaReferenceDocumentsData(ReferenceDocument refDocRepresentation, ReferenceDocModel refDocDbModel) {
    	boolean res;
    	res = (objectsEqual(refDocRepresentation.getDocumentName(), refDocDbModel.getDocName()));
    	res &= (objectsEqual(refDocRepresentation.getContext(), refDocDbModel.getContext().getName()));
    	res &= 	(objectsEqual(refDocRepresentation.getDocumentText(), refDocDbModel.getDocText()));
    	res &= 	(objectsEqual(refDocRepresentation.getUrl(), refDocDbModel.getUrl()));
    	res &= 	(objectsEqual(refDocRepresentation.getDocumentType(), refDocDbModel.getDocType()));
    	return res;
    }
    public static boolean objectsEqual(Object objectA, Object objectB) {
    	return ((objectA == null) ? (objectB == null) : objectA.equals(objectB));
    }
    /**
     * This builds representation instance.
     * 
     * @param name
     * @param altNameType
     * @return AlternateName
     */
    public static AlternateName buildTestdAlternateName (String name, String altNameType) {
    	AlternateName altName = new AlternateName();
    	altName.setContext("contextTest");
    	altName.setLanguage("en");
    	altName.setName(name);
    	altName.setType(altNameType);
    	if (!("USED_BY".equals(altNameType))){
    		altName.setName(name);
    	}
    	else {
    		altName.setName("contextTest");
    	}
    	return altName;
    }
    public static SearchCriteria buildTestSearchCriteriaAll(String suffix) {
    	SearchCriteria searchCriteria = new SearchCriteria();
    	searchCriteria.setContext("context" + suffix);
    	searchCriteria.setClassification("classification" + suffix);
    	searchCriteria.setProtocol("protocol" + suffix);
    	searchCriteria.setName("name" + suffix);
    	searchCriteria.setCsCsiIdSeq("csCsiIdSeq" + suffix);
    	searchCriteria.setFormIdSeq("formIdSeq" + suffix);
		searchCriteria.setFilteredinput(SearchCriteria.ALL_FIELDS);
		searchCriteria.setWorkFlowStatus(SearchCriteria.ALL_WORKFLOW_STATUSES);
		searchCriteria.setRegistrationStatus(SearchCriteria.ALL_REGISTRATION_STATUSES);
		searchCriteria.setAltNameType(SearchCriteria.ALL_ALTNAME_TYPES);
    	return searchCriteria;
    }
    public static SearchCriteria buildTestSearchCriteriaAll(String suffix, String filteredinput) {
    	SearchCriteria searchCriteria = buildTestSearchCriteriaAll(suffix);
		searchCriteria.setFilteredinput(filteredinput );
    	
    	return searchCriteria;
    }
    public static SearchNode buildTestSearchNode(SearchModel model)
    {
        SearchNode searchNode = new SearchNode();
        searchNode = new SearchNode();
        searchNode.setLongName( model.getLongName() );
        searchNode.setOwnedBy( model.getName() );
        searchNode.setPreferredQuestionText( model.getDocText() );
        searchNode.setPublicId( model.getDeCdeid() );
        searchNode.setWorkflowStatus( model.getAslName() );
        searchNode.setVersion( model.getDeVersion() );
        searchNode.setDeIdseq( model.getDeIdseq() );

        searchNode.setHref("cdebrowserServer/rest/CDEData");

        //This is so in the client side display table, there will be spaces to allow good line wrapping.
        if( model.getDeUsedby() != null )
        {
            searchNode.setUsedByContext( model.getDeUsedby().replace( ",", ", " ) );
        }

        searchNode.setRegistrationStatus( model.getRegistrationStatus() );
        return searchNode;
    }
    public static SearchModel buildTestSearchModel(String suffix, int publicId) {
    	SearchModel searchModel = new SearchModel();
    	String deIdseq = "29A8FB18-FFF-11D6-A42F-0010A4C1E842";
    	searchModel.setDeIdseq(deIdseq );
    	searchModel.setName("testname" + suffix);
    	searchModel.setDePreferredName("testdePreferredName" + suffix);
    	searchModel.setAslName("testAslName" + suffix);
    	searchModel.setDeCdeid(""+publicId);
		return searchModel;
    }
}
