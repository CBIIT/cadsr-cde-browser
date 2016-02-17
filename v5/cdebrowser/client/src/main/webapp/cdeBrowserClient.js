

// controller
angular.module("cdeBrowserApp").controller("cdeBrowserController", function ($window, $scope, $http, $filter, $location, $route, ngTableParams) {

    $scope.show = [];
    $scope.initComplete = false;
    $scope.haveSearchResults = false;
    $scope.showCdeSearchResults = true;
    $scope.searchResults = [];
    $scope.cdeTabHasFocus = [];
    $scope.tabsDisabled = true;
    $scope.NORMAL = 0;
    $scope.BIG = 1;
    $scope.ERROR = 2;



    window.scope = $scope;
    // Search query types - radio buttons
    $scope.searchQueryTypes = [
        {id: 0, name: "Exact phrase"},
        {id: 1, name: "All of the words"},
        {id: 2, name: "At least one of the words"}
    ];
    $scope.tabs = [
        {
            title: 'Search Results',
            view: 'search'
        }, {
            title: 'Data Element',
            view: 'dataElement'
        }, {
            title: 'Data Element Concept',
            view: 'dataElementConcept'
        }, {
            title: 'Value Domain',
            view: 'valueDomain'
        }, {
            title: 'Classifications',
            view: 'classifications'
        }, {
            title: 'Usage',
            view: 'usage'
        }, {
            title: 'Data Element Derivation',
            view: 'dataElementDerivation'
        }, {
            title: 'Admin Info',
            view: 'adminInfo'
        }];
    // start checkboxes for table //
    $scope.checkboxes = {'checked': false, items: {}};
    // watch for check all checkbox
    $scope.$watch('checkboxes.checked', function (value) {
        angular.forEach($scope.records, function (item) {
            if (angular.isDefined(item.publicId)) {
                $scope.checkboxes.items[item.publicId] = value;
            }
        });
    });

    // watch for data checkboxes
    $scope.$watch('checkboxes.items', function (values) {
        if (!$scope.records) {
            return;
        }
        var checked = 0, unchecked = 0,
            total = $scope.records.length;
        angular.forEach($scope.records, function (item) {
            checked += ($scope.checkboxes.items[item.publicId]) || 0;
            unchecked += (!$scope.checkboxes.items[item.publicId]) || 0;
        });
        if ((unchecked == 0) || (checked == 0)) {
            $scope.checkboxes.checked = (checked == total);
        }
        // grayed checkbox
        angular.element(document.getElementById("select_all")).prop("indeterminate", (checked != 0 && unchecked != 0));
    }, true);
    // end checkboxes for table //

    // Default query type
    $scope.selectedQueryType = '0'; //Starting selection

    // Query field - drop down
    $scope.searchFieldsBasic = [
        {id: 0, name: "Name"},
        {id: 1, name: "Public ID"}
    ];
    // Default query field selection
    $scope.queryField = 0; //Starting selection

    //When user clicks on a tree element
    $scope.displaySelected = function (node, treePath, text, href, hover) {
        console.log("displaySelected: [" + text + "]  selNode.action(href): [" + href + "]  selNode.hover: [" + hover + "] [" + treePath + "]");
        $scope.breadCrumbs = treePath;
        $scope.resetSortOrder();
        $location.path("/search").replace();

        //alert( "Selected: [" + text + "]\n\nNot yet implemented");
    }


    //When a top tab is clicked, hide all trees, then show this new current one.
    $scope.onClickTab = function (tab) {
        $scope.currentTab = tab;
        $scope.hideContexts();
        $scope.show[tab] = true;
    };

    //CDE details
    $scope.onClickCdeDetails = function (deIdseq) {
        console.log(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/CDEData?deIdseq=" + deIdseq);
        $scope.getCdeDetailRestCall(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/CDEData?deIdseq=" + deIdseq);

    };

    // function that gets the data returned for CDE details //
    $scope.getCdeDetailRestCall = function (serverUrl) {
        console.log("IN getCdeDetailRestCall: " + serverUrl);
        $scope.searchResultsMessage = "Searching";
        $scope.bigSearchResultsMessageClass = true;
        $http.get(serverUrl).success(function (response) {
            $scope.tabsDisabled = false;
            // Change to "Data Element" tab
            $scope.changeView(1, $scope.tabs[1]);
            window.scope = $scope;
            $scope.cdeDetails = response;

            $scope.searchResultsMessage = "Results: " + $scope.searchResults.length;
            $scope.bigSearchResultsMessageClass = false;
        });
    };

    $scope.isActiveCdeTab = function (cdeTabNumber) {
        /*
         console.log("isActiveTab  $scope.currentTab: " + $scope.currentTab);
         console.log("isActiveTab  tabUrl: " + tabUrl);
         console.log("tab_" + tabUrl + " == " + $scope.currentTab );
         */
        return cdeTabNumber == $scope.currentCdeTab;
        //return true;
    };

    $scope.gotFocus = function (tabnumber) {
        //$scope.changeView(tabnumber);
        $scope.cdeTabHasFocus[tabnumber] = true;
        console.log("gotFocus: " + tabnumber);
    };

    $scope.lostFocus = function (tabnumber) {
        $scope.cdeTabHasFocus[tabnumber] = false;
        console.log("lostFocus: " + tabnumber);
    };

    $scope.changeView = function (tabnumber, tab) {
        $location.path(tab.view);
        $scope.currentCdeTab = tabnumber;
        console.log("changeView View: " + tab.view);

        if (tabnumber == 0) {
            // $scope.initTableParams();
            //$scope.tableParams.reload();

            $scope.showCdeSearchResults = true;
        }
        else
        {
            $scope.showCdeSearchResults = false;

        }
        //////////////////////////////////////////
/*
        $scope.tableParams = new ngTableParams(
            {
                page: 1,            // show first page
                count: 20,           // count per page
                sorting: {
                    registrationSort: 'asc'     // initial sorting
                }
            },
            {
                $scope: $scope,
                counts: [], // hide page counts control
                // get data and set total for pagination
                getData: function ($defer, params) {
                    var data = $scope.searchResults;
                    params.total(data.length);
                    // use build-in angular filter
                    var orderedData = params.sorting() ?
                        $filter('orderBy')(data, params.orderBy()) : data;
                    $defer.resolve($scope.records = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                }
            });
*/

        //////////////////////////////////////////

    }


    //Set to first tab - CDE Search tab
    $location.path("search");
    $scope.currentCdeTab = 0;

    // Search button
    $scope.onClickBasicSearch = function (query, field, type) {
        $scope.currentCdeTab = 0;
        $location.path("/search").replace();

        $scope.basicSearchServerRestCall(window.location.protocol + "//"  + window.location.hostname + ":" + window.location.port +
        "/cdebrowserServer/rest/basicSearchWithProgramArea?query=" + query + "&field=" + field + "&queryType=" + type + "&programArea=" + $scope.currentTab);

        console.log("onClickBasicSearch:   " + window.location.protocol + "//"  + window.location.hostname + ":" + window.location.port +
        "/cdebrowserServer/rest/basicSearchWithProgramArea?query=" + query + "&field=" + field + "&queryType=" + type + "&programArea=" + $scope.currentTab);

        $scope.breadCrumbs = [$scope.contextListMaster[$scope.currentTab].text];
        // Restore the view of search results table
        $scope.changeView(0,0);
    };

    // sets sort order for columns that should not be alphabetical //
    $scope.setSortOrder = function () {
        var registrationSort = ["Standard", "Candidate", "Proposed", "Qualified", "Superseded", "Standardized Elsewhere", "Retired", "Application", "Suspended"];
        var workflowSort = ["RELEASED", "Approved for Trial Use ", "Draft New ", "Committee Approved ", "Committee Submitted ", "Committee Submitted Used ", "Draft Mod ", "Retired Archived ", "Retired Phased Out ", "Retired Withdrawn ", "Retired Deleted ", "Released-non-compliant"];
        angular.forEach($scope.searchResults, function (item) {
            var rS = registrationSort.indexOf(item.registrationStatus);
            var wS = workflowSort.indexOf(item.workflowStatus);
            if (rS > -1) {
                item['registrationSort'] = rS
            } else {
                item['registrationSort'] = 1000
            }
            if (wS > -1) {
                item['workflowSort'] = wS
            } else {
                item['workflowSort'] = 1000
            }
        });
    };

    // Basic search query to get search results //
    $scope.basicSearchServerRestCall = function (serverUrl) {
        $scope.tabsDisabled = true;

        console.log("basicSearchServerRestCall: " + serverUrl);
        $scope.haveSearchResults = false;
        $scope.searchResultsMessage = "Searching";
        $scope.bigSearchResultsMessageClass = true;
        $http.get(serverUrl).success(function (response) {
            $scope.searchResults = response;

            if ($scope.searchResults.length > 0) {

                // TODO Quick hack, make status message better when time permits.
                // check for error here .status & .longName
                if (response[0].status != 0) {
                    for (f = 0; f < $scope.breadCrumbs.length; f++) {
                        $scope.breadCrumbs[f] = "";
                    }
                    $scope.searchResultsMessage = "";
                    $scope.statusMessage = "<span  style='color: #8b0000; font-weight: bold; font-size: 18pt;'>" + response[0].longName + "</span><br>";
                }
                else {

                    $scope.setSortOrder();
                    $scope.haveSearchResults = true;
                    $scope.bigSearchResultsMessageClass = false;
                    $scope.searchResultsMessage = "Results: " + $scope.searchResults.length;
                    $scope.tableParams.reload();
                }
                $scope.haveSearchResults = true;

            }
            else {
                $scope.searchResultsMessage = "No search results";
                $scope.haveSearchResults = false;
                $scope.bigSearchResultsMessageClass = true;

                $scope.tableParams.reload();
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


    $scope.versionDisplay = function(ver, tag, scmDate, tier) {
        $window.alert('Version: ' + ver + "\n\nSMC Tag: " + tag + "\n\nSMC Date: " + scmDate + "\n\nSystem Tier: " + tier );
    }


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

    $scope.dataLoad4 = function () {
        $scope.dataLoad("data4.json");
    };

    $scope.dataLoad5 = function () {
        $scope.dataLoad("data5.json");
    };

    $scope.dataLoad6 = function () {
        $scope.dataLoad("data6.json");
    };


    $scope.dataLoadFromServer = function () {
        console.log("$scope.dataLoadFromServer");
        $scope.dataLoad(window.location.protocol + "//" +  window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/contextData");
    };

    $scope.dataLoad = function (dataSource) {
        $scope.waitMessage = "Please wait, loading Context data\n (" + dataSource + ").....".replace(/(?:\r\n|\r|\n)/g, "\n<br>");
        $scope.messageClass = $scope.BIG;

        $http.get(dataSource).success(function (response) {

            //console.log("Back from context_data Service:");
            //console.log( JSON.stringify( response) );
            console.log("STATUS [" + response[0].status + "]");

            if (response[0].status == $scope.ERROR) {
                console.log("ERROR: " + response[0].text);
                $scope.waitMessage = response[0].text.replace(/(?:\r\n|\r|\n)/g, "\n<br>");
                $scope.messageClass = $scope.ERROR;
            }
            else {
                $scope.contextListMaster = response;
                $scope.waitMessage = "";
                $scope.messageClass = $scope.NORMAL;

                //FIXME move this
                $scope.initComplete = true;
                $scope.onClickTab(0);

            }
        });

        console.log("End dataLoad: " + dataSource);
    };

    // Get the urls for the tools we link to from this page.  I got all that where in the database, although we do not use them all
    $scope.getToolHosts = function () {
        var restService = window.location.protocol + "//" +  window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/getAllToolHost"

        $http.get(restService).success(function (response) {

            for( var i = 0; i < response.length; i++)
            {
                if( response[i].toolName =="CURATION" )
                {
                    $scope.curationToolHost = response[i].value;
                }
                else if( response[i].toolName =="caDSR" )
                {
                    $scope.cadsrToolHost = response[i].value;
                }
                else if( response[i].toolName =="CADSRAPI" )
                {
                    $scope.cadsrapiToolHost = response[i].value;
                }
                else if( response[i].toolName =="EVS" )
                {
                    $scope.evsToolHost = response[i].value;
                }
                else if( response[i].toolName =="FREESTYLE" )
                {
                    $scope.freestyleToolHost = response[i].value;
                }
                else if( response[i].toolName =="SENTINEL" )
                {
                    $scope.sentinelToolHost = response[i].value;
                }
                else if( response[i].toolName =="BROWSER" )
                {
                    $scope.browserToolHost = response[i].value;
                }
                else if( response[i].toolName =="EVSAPI" )
                {
                    $scope.evsapiToolHost = response[i].value;
                }
                else if( response[i].toolName =="FormBuilder" )
                {
                    $scope.formbuilderToolHost = response[i].value;
                }
                else if( response[i].toolName =="UMLBrowser" )
                {
                    $scope.umlbrowserToolHost = response[i].value;
                }
                else if( response[i].toolName =="AdminTool" )
                {
                    $scope.admintoolToolHost = response[i].value;
                }
                else if( response[i].toolName =="CDEBrowser" )
                {
                    $scope.cdebrowserToolHost = response[i].value;
                }
                else if( response[i].toolName =="EVSBrowser" )
                {
                    $scope.evsbrowserToolHost = response[i].value;
                }
                else if( response[i].toolName =="ObjectCartAPI" )
                {
                    $scope.objectcartapiToolHost = response[i].value;
                }
                else if( response[i].toolName =="PasswordChangeStation" )
                {
                    $scope.passwordchangestationToolHost = response[i].value;
                }

            }

        });
    };


        $scope.versionData = function() {
        $http.get("version.json").success(function (response) {
            console.log("GOOD: " + response);
            $scope.versionPopover = response;
            $scope.versionPopover.templateUrl = 'versionPopoverTemplate.html';
            $scope.versionPopover.title2 = 'CDE Browser';
            $scope.versionPopover.shortBuildVersionORIG = response.scm_date;
            $scope.versionPopover.shortBuildVersion = response.scm_date.replace(/ .*/,"");
        });
    }


    // start ngTable definition //
    $scope.resetSortOrder = function () {
        $scope.tableParams.sorting({'registrationSort': 'asc'});

        /*  FIXME still not getting the "view" back after reset sort order  */
        //$scope.changeView(0,$scope.tabs[0]);
        $scope.currentCdeTab = 0;
        $location.path("/search").replace();


    };

    $scope.initTableParams = function () {
        $scope.tableParams = new ngTableParams(
            {
                page: 1,            // show first page
                count: 100,           // count per page
                sorting: {
                    registrationSort: 'asc'     // initial sorting
                }
            },
            {
                $scope: $scope,
                counts: [], // hide page counts control
                // get data and set total for pagination
                getData: function ($defer, params) {
                    var data = $scope.searchResults;
                    params.total(data.length);
                    // use build-in angular filter
                    var orderedData = params.sorting() ?
                        $filter('orderBy')(data, params.orderBy()) : data;
                    $defer.resolve($scope.records = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
                }
            });
    };

    $scope.sortNames = {
        'registrationSort': 'Registration Status',
        'longName': 'Long Name',
        'preferredQuestionText': 'Preferred Question Text',
        'ownedBy': 'Owned By',
        'usedByContext': 'Used By Context',
        'workflowSort': 'Workflow Status',
        'publicId': 'Public ID',
        'version': 'Version'
    };

    $scope.initTableParams();
    $scope.hideContexts();
    $scope.dataLoadFromServer();
    $scope.versionData();
    $scope.getToolHosts();

});
