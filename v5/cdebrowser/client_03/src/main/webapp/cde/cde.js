/**
 * Created by lernermh on 4/17/15.
 */
angular.module("cde", [
    "ngRoute",
    "cdeSearch",
    "cdeAdminInfo",
    "cdeClassifications",
    "cdeDataElement",
    "cdeDataElementConcept",
    "cdeDataElementDerivation",
    "cdeUsage",
    "cdeValueDomain"

]).config(["$routeProvider", function ($routeProvider) {

    $routeProvider
        .when("/search", {
            controller: "SearchCtrl",
            templateUrl: "cde/search/search-view.html"
        })
        .when("/adminInfo", {
            controller: "AdminInfoCtrl",
            templateUrl: "cde/adminInfo/adminInfo-view.html"
        })
        .when("/classifications", {
            controller: "ClassificationsCtrl",
            templateUrl: "cde/classifications/classifications-view.html"
        })
        .when("/dataElement", {
            controller: "DataElementCtrl",
            templateUrl: "cde/dataElement/dataElement-view.html"
        })
        .when("/dataElementConcept", {
            controller: "DataElementConceptCtrl",
            templateUrl: "cde/dataElementConcept/dataElementConcept-view.html"
        })

        .when("/dataElementDerivation", {
            controller: "DataElementDerivationCtrl",
            templateUrl: "cde/dataElementDerivation/dataElementDerivation-view.html"
        })
        .when("/usage", {
            controller: "UsageCtrl",
            templateUrl: "cde/usage/usage-view.html"
        })
        .when("/valueDomain", {
            controller: "ValueDomainCtrl",
            templateUrl: "cde/valueDomain/valueDomain-view.html"
        });


}]);

angular.module('cde').controller('cdeCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {







    // Test data
    $scope.cdeData="{\n" +
    "    \"dataElement\": {\n" +
    "        \"dataElementDetails\": {\n" +
    "            \"publicId\": 12345,\n" +
    "            \"version\": 4.1,\n" +
    "            \"longName\": \"The Long Name\",\n" +
    "            \"shortName\": \"The Short Name\",\n" +
    "            \"preferredQuestionText\": \"The preferred Question Text\",\n" +
    "            \"definition\": \"The Definition\",\n" +
    "            \"valueDomain\": \"The Value Domain\",\n" +
    "            \"dataElementConcept\": \"The Data Element Concept\",\n" +
    "            \"context\": \"The Context\",\n" +
    "            \"workflowStatus\": \"The Workflow Status\",\n" +
    "            \"origin\": \"The Origin\",\n" +
    "            \"registrationStatus\": \"The Registration Status\",\n" +
    "            \"directLink\": \"The Direct Link\"\n" +
    "        },\n" +
    "        \"referenceDocuments\": [\n" +
    "            {\n" +
    "                \"documentName\": \"The Document Name 1\",\n" +
    "                \"documentType\": \"The Document Type 1\",\n" +
    "                \"documentText\": \"The Document Text 1\",\n" +
    "                \"context\": \"The Context 1\",\n" +
    "                \"url\": \"The URL 1\"\n" +
    "            },\n" +
    "            {\n" +
    "                \"documentName\": \"The Document Name 2\",\n" +
    "                \"documentType\": \"The Document Type 2\",\n" +
    "                \"documentText\": \"The Document Text 2\",\n" +
    "                \"context\": \"The Context 2\",\n" +
    "                \"url\": \"The URL 2\"\n" +
    "            },\n" +
    "            {\n" +
    "                \"documentName\": \"The Document Name 3\",\n" +
    "                \"documentType\": \"The Document Type 3\",\n" +
    "                \"documentText\": \"The Document Text 3\",\n" +
    "                \"context\": \"The Context 3\",\n" +
    "                \"url\": \"The URL 3\"\n" +
    "            }\n" +
    "        ],\n" +
    "        \"alternateNamesAndDefinitionses\": null,\n" +
    "        \"otherVersions\": null\n" +
    "    }\n" +
    "}";

    $scope.cdeData1 = "{\"createdBy\":\"buildTestRecord1 DateCreated\",\"modifiedBy\":\"buildTestRecord1 DateModified\",\"longCDEName\":\"longCDEName\",\"contextName\":\"ContextName\",\"usingContexts\":\"UsingContexts\",\"refDocs\":[{\"createdBy\":\"buildTestReferenceDocModel DateCreated\",\"modifiedBy\":\"buildTestReferenceDocModel DateModified\",\"docName\":\"DocName_ref doc model A\",\"docType\":\"DocType_ref doc model A\",\"docIDSeq\":\"IDSeq_ref doc model A\",\"docText\":\"DocText_ref doc model A\",\"lang\":\"Lang_ref doc model A\",\"url\":\"URL_ref doc model A\",\"context\":{\"createdBy\":\"buildTestContextModel DateCreated\",\"modifiedBy\":\"buildTestContextModel DateModified\",\"conteIdseq\":\"ConteIdSeq\",\"name\":\"ContextModel Name\",\"palName\":\"ContextModel Pal Name\",\"llName\":\"ContextModel Llname\",\"description\":\"ContextModel Description\",\"preferredDefinition\":\"ContextModel PreferredDefinition\",\"lang\":\"ContextModel Lang\",\"version\":123.0,\"dateCreated\":1429212975287,\"dateModified\":1429212975287},\"dateCreated\":1429212975287,\"dateModified\":1429212975287},{\"createdBy\":\"buildTestReferenceDocModel DateCreated\",\"modifiedBy\":\"buildTestReferenceDocModel DateModified\",\"docName\":\"DocName_ref doc model B\",\"docType\":\"DocType_ref doc model B\",\"docIDSeq\":\"IDSeq_ref doc model B\",\"docText\":\"DocText_ref doc model B\",\"lang\":\"Lang_ref doc model B\",\"url\":\"URL_ref doc model B\",\"context\":{\"createdBy\":\"buildTestContextModel DateCreated\",\"modifiedBy\":\"buildTestContextModel DateModified\",\"conteIdseq\":\"ConteIdSeq\",\"name\":\"ContextModel Name\",\"palName\":\"ContextModel Pal Name\",\"llName\":\"ContextModel Llname\",\"description\":\"ContextModel Description\",\"preferredDefinition\":\"ContextModel PreferredDefinition\",\"lang\":\"ContextModel Lang\",\"version\":123.0,\"dateCreated\":1429212975287,\"dateModified\":1429212975287},\"dateCreated\":1429212975287,\"dateModified\":1429212975287},{\"createdBy\":\"buildTestReferenceDocModel DateCreated\",\"modifiedBy\":\"buildTestReferenceDocModel DateModified\",\"docName\":\"DocName_ref doc model C\",\"docType\":\"DocType_ref doc model C\",\"docIDSeq\":\"IDSeq_ref doc model C\",\"docText\":\"DocText_ref doc model C\",\"lang\":\"Lang_ref doc model C\",\"url\":\"URL_ref doc model C\",\"context\":{\"createdBy\":\"buildTestContextModel DateCreated\",\"modifiedBy\":\"buildTestContextModel DateModified\",\"conteIdseq\":\"ConteIdSeq\",\"name\":\"ContextModel Name\",\"palName\":\"ContextModel Pal Name\",\"llName\":\"ContextModel Llname\",\"description\":\"ContextModel Description\",\"preferredDefinition\":\"ContextModel PreferredDefinition\",\"lang\":\"ContextModel Lang\",\"version\":123.0,\"dateCreated\":1429212975287,\"dateModified\":1429212975287},\"dateCreated\":1429212975287,\"dateModified\":1429212975287}],\"designationModels\":[{\"createdBy\":\"buildTestDesignationModel DateCreated\",\"modifiedBy\":\"buildTestDesignationModel DateModified\",\"name\":\"designationModel name_A\",\"type\":\"designationModel type_A\",\"desigIDSeq\":\"desigIDSeq_A\",\"contex\":{\"createdBy\":\"buildTestContextModel DateCreated\",\"modifiedBy\":\"buildTestContextModel DateModified\",\"conteIdseq\":\"ConteIdSeq\",\"name\":\"ContextModel Name\",\"palName\":\"ContextModel Pal Name\",\"llName\":\"ContextModel Llname\",\"description\":\"ContextModel Description\",\"preferredDefinition\":\"ContextModel PreferredDefinition\",\"lang\":\"ContextModel Lang\",\"version\":123.0,\"dateCreated\":1429212975287,\"dateModified\":1429212975287},\"lang\":\"designationModel lang_A\",\"dateCreated\":1429212975287,\"dateModified\":1429212975287},{\"createdBy\":\"buildTestDesignationModel DateCreated\",\"modifiedBy\":\"buildTestDesignationModel DateModified\",\"name\":\"designationModel name_B\",\"type\":\"designationModel type_B\",\"desigIDSeq\":\"desigIDSeq_B\",\"contex\":{\"createdBy\":\"buildTestContextModel DateCreated\",\"modifiedBy\":\"buildTestContextModel DateModified\",\"conteIdseq\":\"ConteIdSeq\",\"name\":\"ContextModel Name\",\"palName\":\"ContextModel Pal Name\",\"llName\":\"ContextModel Llname\",\"description\":\"ContextModel Description\",\"preferredDefinition\":\"ContextModel PreferredDefinition\",\"lang\":\"ContextModel Lang\",\"version\":123.0,\"dateCreated\":1429212975287,\"dateModified\":1429212975287},\"lang\":\"designationModel lang_B\",\"dateCreated\":1429212975287,\"dateModified\":1429212975287},{\"createdBy\":\"buildTestDesignationModel DateCreated\",\"modifiedBy\":\"buildTestDesignationModel DateModified\",\"name\":\"designationModel name_C\",\"type\":\"designationModel type_C\",\"desigIDSeq\":\"desigIDSeq_C\",\"contex\":{\"createdBy\":\"buildTestContextModel DateCreated\",\"modifiedBy\":\"buildTestContextModel DateModified\",\"conteIdseq\":\"ConteIdSeq\",\"name\":\"ContextModel Name\",\"palName\":\"ContextModel Pal Name\",\"llName\":\"ContextModel Llname\",\"description\":\"ContextModel Description\",\"preferredDefinition\":\"ContextModel PreferredDefinition\",\"lang\":\"ContextModel Lang\",\"version\":123.0,\"dateCreated\":1429212975287,\"dateModified\":1429212975287},\"lang\":\"designationModel lang_C\",\"dateCreated\":1429212975287,\"dateModified\":1429212975287}],\"publicId\":2744943,\"idseq\":\"Idseq\",\"registrationStatus\":\"RegistrationStatus\",\"valueDomainModel\":{\"createdBy\":\"buildTestValueDomainModel DateCreated\",\"modifiedBy\":\"buildTestValueDomainModel DateModified\",\"preferredName\":\"valueDomainModel PreferredName\",\"preferredDefinition\":\"valueDomainModel  PreferredDefinition\",\"longName\":\"valueDomainModel LongName\",\"aslName\":\"valueDomainModel AslName\",\"version\":123.0,\"deletedInd\":\"valueDomainModel DeletedInd\",\"latestVerInd\":\"valueDomainModel LatestVerInd\",\"publicId\":321,\"origin\":\"valueDomainModel Origin\",\"idseq\":\"valueDomainModel Idseq\",\"vdIdseq\":\"valueDomainModel VdIdseq\",\"datatype\":\"valueDomainModel Datatype\",\"uom\":\"valueDomainModel Uom\",\"dispFormat\":\"valueDomainModel DispFormat\",\"maxLength\":\"valueDomainModel MaxLength\",\"minLength\":\"valueDomainModel MinLength\",\"highVal\":\"valueDomainModel ighVal\",\"lowVal\":\"valueDomainModel LowVal\",\"charSet\":\"valueDomainModel CharSet\",\"decimalPlace\":\"valueDomainModel DecimalPlace\",\"cdPrefName\":\"valueDomainModel CdPrefName\",\"cdContextName\":\"valueDomainModel CdContextName\",\"cdVersion\":123.0,\"cdPublicId\":345,\"vdType\":\"valueDomainModel VdType\",\"representationModel\":{\"createdBy\":\"buildTestRepresentationModel DateCreated\",\"modifiedBy\":\"buildTestRepresentationModel DateModified\",\"preferredName\":\"RepresentationModel PreferredName\",\"longName\":\"RepresentationModel LongName\",\"version\":23.45,\"context\":{\"createdBy\":\"buildTestContextModel DateCreated\",\"modifiedBy\":\"buildTestContextModel DateModified\",\"conteIdseq\":\"ConteIdSeq\",\"name\":\"ContextModel Name\",\"palName\":\"ContextModel Pal Name\",\"llName\":\"ContextModel Llname\",\"description\":\"ContextModel Description\",\"preferredDefinition\":\"ContextModel PreferredDefinition\",\"lang\":\"ContextModel Lang\",\"version\":123.0,\"dateCreated\":1429212975288,\"dateModified\":1429212975288},\"publicId\":4321,\"idseq\":\"RepresentationModel Idseq\",\"conceptDerivationRuleModel\":{\"createdBy\":null,\"modifiedBy\":null,\"dateCreated\":null,\"dateModified\":null},\"dateCreated\":1429212975288,\"dateModified\":1429212975288},\"conceptDerivationRuleModel\":{\"createdBy\":null,\"modifiedBy\":null,\"dateCreated\":null,\"dateModified\":null},\"dateCreated\":1429212975288,\"dateModified\":1429212975288},\"dec\":{\"createdBy\":\"buildTestDataElementConceptModel DateCreated\",\"modifiedBy\":\"buildTestDataElementConceptModel DateModified\",\"preferredName\":\"DataElementConceptModel PreferredName\",\"preferredDefinition\":\"DataElementConceptModel PreferredDefinition\",\"longName\":\"DataElementConceptModel LongName\",\"aslName\":\"DataElementConceptModel AslName\",\"version\":123.0,\"deletedInd\":\"DataElementConceptModel DeletedInd\",\"latestVerInd\":\"DataElementConceptModel LatestVerInd\",\"publicId\":543,\"origin\":\"DataElementConceptModel Origin\",\"idseq\":\"DataElementConceptModel Idseq\",\"decIdseq\":\"DataElementConceptModel DecIdseq\",\"cdIdseq\":\"DataElementConceptModel CdIdseq\",\"proplName\":\"DataElementConceptModel ProplName\",\"oclName\":\"DataElementConceptModel OclName\",\"objClassQualifier\":\"DataElementConceptModel ObjClassQualifier\",\"propertyQualifier\":\"DataElementConceptModel PropertyQualifier\",\"changeNote\":\"DataElementConceptModel ChangeNote\",\"objClassPrefName\":\"DataElementConceptModel ObjClassPrefName\",\"objClassContextName\":\"DataElementConceptModel ObjClassContextName\",\"propertyPrefName\":\"DataElementConceptModel PropertyPrefName\",\"propertyContextName\":\"DataElementConceptModel PropertyContextName\",\"propertyVersion\":987.0,\"objClassVersion\":654.0,\"conteName\":\"DataElementConceptModel ConteName\",\"cdPrefName\":\"DataElementConceptModel CdPrefName\",\"cdContextName\":\"DataElementConceptModel CdContextName\",\"cdVersion\":12.34,\"cdPublicId\":123,\"objClassPublicId\":\"DataElementConceptModel ObjClassPublicId\",\"property\":{\"createdBy\":\"buildTestPropertyModel DateCreated\",\"modifiedBy\":\"buildTestPropertyModel DateModified\",\"preferredName\":\"PropertyModel PreferredName\",\"longName\":\"PropertyModel LongName\",\"version\":1.23,\"context\":{\"createdBy\":\"buildTestContextModel DateCreated\",\"modifiedBy\":\"buildTestContextModel DateModified\",\"conteIdseq\":\"ConteIdSeq\",\"name\":\"ContextModel Name\",\"palName\":\"ContextModel Pal Name\",\"llName\":\"ContextModel Llname\",\"description\":\"ContextModel Description\",\"preferredDefinition\":\"ContextModel PreferredDefinition\",\"lang\":\"ContextModel Lang\",\"version\":123.0,\"dateCreated\":1429212975288,\"dateModified\":1429212975288},\"publicId\":123,\"name\":\"PropertyModel Name\",\"qualifier\":\"PropertyModel Qualifier\",\"dateCreated\":1429212975288,\"dateModified\":1429212975288},\"objectClassModel\":{\"createdBy\":\"buildTestObjectClassModel  DateCreated\",\"modifiedBy\":\"buildTestObjectClassModel DateModified\",\"preferredName\":\"ObjectClassModel PreferredName\",\"longName\":\"ObjectClassModel LongName\",\"version\":2.3,\"context\":{\"createdBy\":\"buildTestContextModel DateCreated\",\"modifiedBy\":\"buildTestContextModel DateModified\",\"conteIdseq\":\"ConteIdSeq\",\"name\":\"ContextModel Name\",\"palName\":\"ContextModel Pal Name\",\"llName\":\"ContextModel Llname\",\"description\":\"ContextModel Description\",\"preferredDefinition\":\"ContextModel PreferredDefinition\",\"lang\":\"ContextModel Lang\",\"version\":123.0,\"dateCreated\":1429212975288,\"dateModified\":1429212975288},\"publicId\":234,\"idseq\":\"ObjectClassModel Qualifier\",\"name\":\"ObjectClassModel Name\",\"qualifier\":\"ObjectClassModel LongName\",\"dateCreated\":1429212975288,\"dateModified\":1429212975288},\"dateCreated\":1429212975288,\"dateModified\":1429212975288},\"context\":{\"createdBy\":\"buildTestContextModel DateCreated\",\"modifiedBy\":\"buildTestContextModel DateModified\",\"conteIdseq\":\"ConteIdSeq\",\"name\":\"ContextModel Name\",\"palName\":\"ContextModel Pal Name\",\"llName\":\"ContextModel Llname\",\"description\":\"ContextModel Description\",\"preferredDefinition\":\"ContextModel PreferredDefinition\",\"lang\":\"ContextModel Lang\",\"version\":123.0,\"dateCreated\":1429212975288,\"dateModified\":1429212975288},\"deIdseq\":\"DeIdse\",\"version\":\"1.0\",\"conteIdseq\":\"ConteIdseq\",\"preferredName\":\"PreferredName\",\"vdIdseq\":\"VdIdseq\",\"decIdseq\":\"DecIdseq\",\"preferredDefinition\":\"PreferredDefinition\",\"aslName\":\"AslName\",\"longName\":\"LongName\",\"latestVerInd\":\"LatestVerInd\",\"deletedInd\":\"DeletedInd\",\"beginDate\":\"BeginDate\",\"endDate\":\"EndDate\",\"origin\":\"Origin\",\"cdeId\":\"CdeId\",\"question\":\"Question\",\"vdName\":\"VdName\",\"dateCreated\":1429212975287,\"dateModified\":1429212975287}";



}]);
