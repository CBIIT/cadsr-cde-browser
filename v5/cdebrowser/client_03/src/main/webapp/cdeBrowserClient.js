// controller
cdeBrowserApp.controller('cdeBrowserController', function ($scope, $http, $filter, ngTableParams) {

    $scope.show = [];

    console.log("$scope.show len: " + $scope.show.length);
    $scope.currentTab = '0';
    $scope.initComplete = false;
    $scope.haveSearchResults = false;
    $scope.searchResults = [];
    // Search query types - radio buttons
    $scope.searchQueryTypes = [
        {id: 0, name: "Exact phrase"},
        {id: 1, name: "All of the words"},
        {id: 2, name: "At least one of the words"}
    ];
    // Default query type
    $scope.selectedQueryType = '0'; //Starting selection

    // Query field - drop down
    $scope.searchFieldsBasic = [
        {id: 0, name: "Name"},
        {id: 1, name: "Public ID"}
    ];
    // Default query field selection
    $scope.queryField = 0; //Starting selection

    //Temporary function, when user clicks on a tree element
    $scope.displaySelected = function(text, href, hover){
        console.log("displaySelected: [" + text + "]  selNode.action(href): [" + href + "]  selNode.hover: [" + hover + "]");
        //alert( "Selected: [" + text + "]\n\nNot yet implemented");
    }


    //When a top tab is clicked, hide all trees, then show this new current one.
    $scope.onClickTab = function (tab) {
        $scope.currentTab = tab;
        $scope.hideContexts();
        $scope.show[tab] = true;
    };

    // Search button
    $scope.onClickBasicSearch = function (query, field, type) {
        $scope.basicSearchServerRestCall("http://" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/basicSearch?query=" + query + "&field=" + field + "&queryType=" + type);
    };

    $scope.basicSearchServerRestCall = function (serverUrl) {
        $scope.haveSearchResults = false;
        $scope.searchResultsMessage = "Searching";
        $scope.bigSearchResultsMessageClass = true;
        $http.get(serverUrl).success(function (response) {
            $scope.searchResults = response;

            if ($scope.searchResults.length > 0) {
                $scope.haveSearchResults = true;
                $scope.bigSearchResultsMessageClass = false;
                $scope.searchResultsMessage = "Results: " + $scope.searchResults.length;
                $scope.tableParams.reload();
            }
            else {
                $scope.searchResultsMessage = "No search results";
                $scope.haveSearchResults = false;
                $scope.bigSearchResultsMessageClass = true;
            }
        });
    };


    $scope.hideContexts = function () {
        for (var f = 0; f < $scope.show.length; f++) {
            $scope.show[f] = false;
        }
    };

    $scope.isActiveTab = function (tabUrl) {
        /*
         console.log("isActiveTab  $scope.currentTab: " + $scope.currentTab);
         console.log("isActiveTab  tabUrl: " + tabUrl);
         console.log("tab_" + tabUrl + " == " + $scope.currentTab );
         */
        return tabUrl == $scope.currentTab;
        //return true;
    };

    //Just top three levels of the tree
    $scope.dataLoad1 = function () {
        $scope.dataLoad("data1.json");
    };

    //Full Tree
    $scope.dataLoad2 = function () {
        $scope.dataLoad("data2.json");
    };

    $scope.dataLoad3 = function () {
        $scope.dataLoad("data3.json");
    };

    $scope.dataLoadFromServer = function () {
        $scope.dataLoad("http://" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/contextData");
    };

    $scope.dataLoad = function (dataSource) {
        $scope.waitMessage = "Please wait, loading Context data (" + dataSource + ").....";
        //$scope.waitMessage = "Please wait, loading Context data.....";
        $scope.bigMessageClass = true;

        $http.get(dataSource).success(function (response) {

            //console.log("Back from context_data Service:");
            //console.log( JSON.stringify( response) );
            $scope.contextListMaster = response;
            $scope.waitMessage = "caDSR Contexts:";
            $scope.bigMessageClass = false;
            //FIXME move this
            $scope.initComplete = true;
            $scope.onClickTab(0);
        });

         console.log("End dataLoad: " + dataSource);

    };

    $scope.myFilter = function (text) {
        console.log("Filter1: " + text.href);
        return true;
    };


    $scope.hideContexts();
    //$scope.dataLoadFromServer();
    $scope.dataLoad1();

    // comment this out //
    // $scope.searchResults = [{"longName":"Person Specimen External Investigator Consent Ind-2","preferredQuestionText":"Patient has given NCCTG permission to give their blood and paraffin-embedded tissue to outside researchers","ownedBy":"CTEP","publicId":"2185992","workflowStatus":"RELEASED","version":"1","usedByContext":"Alliance, DCP","registrationStatus":"Qualified","href":"cdebrowserServer/searchResultsData"},{"longName":"Specimen Related Genetic Research Consent Ind-2","preferredQuestionText":"Patient's initial consent given for specimen use for genetic research relating to the study treatment","ownedBy":"CTEP","publicId":"2556199","workflowStatus":"RELEASED","version":"1","usedByContext":"Alliance, COG, CTEP, PBTC","registrationStatus":"Qualified","href":"cdebrowserServer/searchResultsData"},{"longName":"Tissue Specimen Malignant Neoplasm Related Research Consent Ind-3","preferredQuestionText":"Consent given for specimen use?","ownedBy":"CTEP","publicId":"2428316","workflowStatus":"RELEASED","version":"3","usedByContext":"Alliance, CCR, COG","registrationStatus":null,"href":"cdebrowserServer/searchResultsData"},{"longName":"Person Household Income Alliance Category","preferredQuestionText":"What was the total combined income of your household in the past year, including income from all sources such as wages, salaries, Social Security or retirement benefits, help from relatives and so forth?","ownedBy":"Alliance","publicId":"4367861","workflowStatus":"RELEASED","version":"1","usedByContext":"Alliance","registrationStatus":null,"href":"cdebrowserServer/searchResultsData"},{"longName":"Clinical Study Protocol Institutional Review Board Approval Amendment Ind-2","preferredQuestionText":"Amendment 4 (dated August 15, 2011) IRB/REB approved","ownedBy":"CTEP","publicId":"2950096","workflowStatus":"RELEASED","version":"1","usedByContext":"COG","registrationStatus":null,"href":"cdebrowserServer/searchResultsData"},{"longName":"Tissue Specimen Research Consent Unrelated Ind-3","preferredQuestionText":"Patient's Initial Consent given for tissue specimen use for research unrelated to the patient's cancer?","ownedBy":"CTEP","publicId":"2428318","workflowStatus":"RELEASED","version":"3","usedByContext":"NRG","registrationStatus":null,"href":"cdebrowserServer/searchResultsData"},{"longName":"Person Specimen External Investigator Tissue Sample Consent Ind-2","preferredQuestionText":"Patient has given permission for the Alliance to give his/her stored tissue samples for use in future research to outside researchers","ownedBy":"CTEP","publicId":"3288373","workflowStatus":"RELEASED","version":"1","usedByContext":null,"registrationStatus":null,"href":"cdebrowserServer/searchResultsData"},{"longName":"Surgeon Total Mesorectal Excision Certification Ind-2","preferredQuestionText":"Primary surgeon is credentialed in Total Mesorectal Excision (TME) by the Alliance Surgery Committee","ownedBy":"CTEP","publicId":"3288100","workflowStatus":"RELEASED","version":"1","usedByContext":null,"registrationStatus":"Qualified","href":"cdebrowserServer/searchResultsData"},{"longName":"Diagnostic Imaging Review Assessment Received Date","preferredQuestionText":"Date mandatory imaging review received from Alliance Imaging Core Laboratory","ownedBy":"Alliance","publicId":"4155044","workflowStatus":"RELEASED","version":"1","usedByContext":"Alliance","registrationStatus":null,"href":"cdebrowserServer/searchResultsData"},{"longName":"Patient Additional Research Consent Ind-2","preferredQuestionText":"Someone from the Southwest Oncology Group may contact me in the future to ask me to take part in more research","ownedBy":"CTEP","publicId":"2186353","workflowStatus":"RELEASED","version":"1","usedByContext":"AECC, Alliance, CTEP, DCP, ECOG-ACRIN, NRG, SWOG","registrationStatus":"Qualified","href":"cdebrowserServer/searchResultsData"},{"longName":"Person Specimen Malignant Neoplasm Research Consent Ind-2","preferredQuestionText":"My specimens may be kept for use in research to learn about, prevent, treat, or cure cancer.","ownedBy":"CTEP","publicId":"2200959","workflowStatus":"RELEASED","version":"1","usedByContext":"AECC, Alliance, CTEP, ECOG-ACRIN, NRG","registrationStatus":null,"href":"cdebrowserServer/searchResultsData"},{"longName":"Patient Therapeutic Procedure Follow-up Eligibility Determination Ind-2","preferredQuestionText":"Patient accessible for treatment and follow-up.","ownedBy":"CTEP","publicId":"2002339","workflowStatus":"RELEASED","version":"3","usedByContext":"NRG","registrationStatus":null,"href":"cdebrowserServer/searchResultsData"},{"longName":"Patient Optional Consent Ind-2","preferredQuestionText":"Did the patient consent to optional core study components at the time of randomization?","ownedBy":"CTEP","publicId":"2181609","workflowStatus":"RELEASED","version":"1","usedByContext":"Alliance, DCP","registrationStatus":null,"href":"cdebrowserServer/searchResultsData"},{"longName":"Person Specimen External Investigator Blood Sample Consent Ind-2","preferredQuestionText":"Patient has given permission to the Alliance to give his/her stored blood samples for use in future research to outside researchers","ownedBy":"CTEP","publicId":"3288369","workflowStatus":"RELEASED","version":"1","usedByContext":null,"registrationStatus":null,"href":"cdebrowserServer/searchResultsData"},{"longName":"Blood Specimen Other Diseases and Disorders Research Consent Ind-3","preferredQuestionText":"Patient's Initial Consent given for blood specimen use for research unrelated to the patient's cancer?","ownedBy":"CTEP","publicId":"2428319","workflowStatus":"RELEASED","version":"3","usedByContext":"NRG","registrationStatus":null,"href":"cdebrowserServer/searchResultsData"},{"longName":"Blood Tissue Specimen Other Diseases and Disorders Research Consent Ind-3","preferredQuestionText":"Patient's Initial Consent given for specimen use for research unrelated to the patient's cancer?","ownedBy":"CTEP","publicId":"58325","workflowStatus":"RELEASED","version":"3","usedByContext":"Alliance, NRG, caBIG","registrationStatus":null,"href":"cdebrowserServer/searchResultsData"},{"longName":"Management North Central Cancer Treatment Group Therapy Ind-2","preferredQuestionText":"Treatment on this protocol must commence at the accruing membership under the supervision of an NCCTG or ABTC member physician","ownedBy":"CTEP","publicId":"3015301","workflowStatus":"RELEASED","version":"1","usedByContext":null,"registrationStatus":null,"href":"cdebrowserServer/searchResultsData"},{"longName":"Person Specimen Malignant Neoplasm Future Clinical or Research Activity Consent Ind-2","preferredQuestionText":"Patient has given permission to keep tissue sample(s) for use in future research to learn about, prevent, or treat cancer.","ownedBy":"CTEP","publicId":"3170477","workflowStatus":"RELEASED","version":"1","usedByContext":"COG","registrationStatus":null,"href":"cdebrowserServer/searchResultsData"},{"longName":"Patient Eligibility Indicator","preferredQuestionText":"Is the participant eligible for inclusion on this study","ownedBy":"CTEP","publicId":"1235","workflowStatus":"RELEASED","version":"4","usedByContext":"Alliance, CCR, CTEP, DCP, ECOG-ACRIN, LCC, NINDS, NRG, OHSU Knight, SPOREs, caBIG","registrationStatus":"Standard","href":"cdebrowserServer/searchResultsData"},{"longName":"Patient Correlative Study Consent Ind-2","preferredQuestionText":"Was consent signed for correlative biology studies?","ownedBy":"CTEP","publicId":"3304322","workflowStatus":"RELEASED","version":"1","usedByContext":"Alliance, CTEP, DCP","registrationStatus":null,"href":"cdebrowserServer/searchResultsData"},{"longName":"Laboratory Procedure Seattle Cancer Care Alliance Busulfan Pharmacokinetics Testing Performed Yes No Unknown Indicator","preferredQuestionText":"Did Seattle Cancer Care Alliance (SCCA) perform Busulfan PK testing?","ownedBy":"COG","publicId":"3646396","workflowStatus":"RELEASED","version":"1","usedByContext":null,"registrationStatus":null,"href":"cdebrowserServer/searchResultsData"},{"longName":"Blood Specimen Malignant Neoplasm Related Research Consent Ind-3","preferredQuestionText":"Patient's Initial Consent given for blood specimen use for research on the patient's cancer?","ownedBy":"CTEP","publicId":"2428315","workflowStatus":"RELEASED","version":"3","usedByContext":"CCR, NRG","registrationStatus":null,"href":"cdebrowserServer/searchResultsData"}]
    // $scope.haveSearchResults = true;
    //end comment this out //   

    // start ngTable definition //
    $scope.tableParams = new ngTableParams(
        {
            page: 1,            // show first page
            count: 20           // count per page     
        },
        {
            counts: [], // hide page counts control
            // get data and set total for pagination 
            getData: function($defer, params) {
                var data = $scope.searchResults;
                params.total(data.length);
                // use build-in angular filter
                var orderedData = params.sorting() ? 
                $filter('orderBy')(data, params.orderBy()) : data;
                $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            } 
        }); 
  
});


