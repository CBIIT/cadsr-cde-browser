package gov.nih.nci.cadsr.service.restControllers;

import gov.nih.nci.cadsr.dao.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    @RequestMapping( value = "/CDEData" )
    @ResponseBody
    public DataElementModel CDEDataController( @RequestParam( "deIdseq" ) String deIdseq )
    {
        logger.debug( "Received rest call \"CDEData\": " + deIdseq );

        DataElementModel dataElementModel = buildTestRecord1();

        return dataElementModel;
    }

    private DataElementModel buildTestRecord1()
    {
        DataElementModel dataElementModel = new DataElementModel();
        dataElementModel.setCreatedBy( "buildTestRecord1 DateCreated" );
        dataElementModel.setDateCreated( getDate() );
        dataElementModel.setModifiedBy( "buildTestRecord1 DateModified" );
        dataElementModel.setDateModified( getDate() );

        dataElementModel.setLongCDEName( "LongCDEName" );
        dataElementModel.setContextName( "ContextName" );
        dataElementModel.setUsingContexts( "UsingContexts" );

        //List<ReferenceDocModel>
        dataElementModel.setRefDocs( buildTestReferenceDocModelList() );

        //List<DesignationModel>
        dataElementModel.setDesignationModels( buildDesignationModelList() );

        dataElementModel.setPublicId( 123 );
        dataElementModel.setIdseq( "Idseq" );
        dataElementModel.setRegistrationStatus( "RegistrationStatus" );

        dataElementModel.setValueDomainModel( buildTestValueDomainModel() );

        dataElementModel.setDec( buildTestDataElementConceptModel() );
        dataElementModel.setContext( buildTestContextModel() );
        dataElementModel.setDeIdseq( "DeIdse" );
        dataElementModel.setVersion( "Version" ); // needs to be a Float!
        dataElementModel.setConteIdseq( "ConteIdseq" );
        dataElementModel.setPreferredName( "PreferredName" );
        dataElementModel.setVdIdseq( "VdIdseq" );
        dataElementModel.setDecIdseq( "DecIdseq" );
        dataElementModel.setPreferredDefinition( "PreferredDefinition" );
        dataElementModel.setAslName( "AslName" );

        dataElementModel.setLongName( "LongName" );
        dataElementModel.setLatestVerInd( "LatestVerInd" );
        dataElementModel.setDeletedInd( "DeletedInd" );
        dataElementModel.setBeginDate( "BeginDate" );
        dataElementModel.setEndDate( "EndDate" );
        dataElementModel.setOrigin( "Origin" );
        dataElementModel.setCdeId( "CdeId" );
        dataElementModel.setQuestion( "Question" );
        dataElementModel.setVdName( "VdName" );

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
        valueDomainModel.setMaxLength( "valueDomainModel MaxLength" );
        valueDomainModel.setMinLength( "valueDomainModel MinLength" );
        valueDomainModel.setHighVal( "valueDomainModel ighVal" );
        valueDomainModel.setLowVal( "valueDomainModel LowVal" );
        valueDomainModel.setCharSet( "valueDomainModel CharSet" );
        valueDomainModel.setDecimalPlace( "valueDomainModel DecimalPlace" );
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
        contextModel.setLanguage("ContextModel Lang");
        contextModel.setVersion( (float) 123 );

        return contextModel;
    }
    /////////////////////////////////////////////////////////////////////////
    // End test ReferenceDocModel List

    private Timestamp getDate()
    {
        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        return new java.sql.Timestamp(now.getTime());
    }
}
