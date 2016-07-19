
// controller
angular.module("cdeBrowserApp").controller("cdeBrowserController", function ($window, $scope, $filter, $timeout,$localStorage,$sessionStorage,$http, $location, $route, NgTableParams, searchFactory, cartService, filterService, authenticationService, downloadFactory, groupFactory, groupFactory1, compareService, $rootScope) {
    window.scope = $scope;
    $scope.searchFactory = searchFactory;
    $scope.location = $location.url();
    $scope.permissibleValueHold = "";
    $scope.dataElementConceptHOLD = "";
    $scope.valueDomainHOLD = "";
    $scope.searchAltNameHOLD = "";
    $scope.searchVersionsHOLD = 0;
    $scope.concept=[{id:"0",name:"Name"},{id:"1",name:"Code"}];
    $scope.searchContextUseHOLD = 0;
    $scope.searchObjectClassHOLD = "";
    var delimiter= ":::";


    /* Start of filter service */
    var fs = filterService // define service instance //
    $scope.filterService = fs; // set service to scope. Used to interact with view //
    $scope.fs = filterService;

    var cs = compareService;
    $scope.compareService = cs;


    $scope.$watch('contextListMaster',function(data) { // gets data for program areas and contexts //
        if (data) {
            fs.serverData = $scope.contextListMaster;
            fs.searchFilter.programArea=0;
        };
    });

    $scope.contextCascade = function(selectedInput) {
        if (selectedInput!==undefined)
            $scope.filterService.searchFilter.context = selectedInput.contextIdSeq;
    };


    $rootScope.$on('updateResult',function() {
        $scope.searchResults = [];
        $scope.breadCrumbs = [];
    });


    // check user authentication status //
    $scope.$on('$locationChangeStart', function() {
        $scope.authenticationService.checkAuth();
        if ($location.path()=='/cdeCart' || $location.path()=='/login' || $location.path()=='/cdeCompare') {
            searchFactory.showSearch = false;
        }
        else {
            searchFactory.showSearch = true;
        };
    });

    // watch for changes to dropdowns. When it changes, refilter data //
    $scope.$watch('filterService.searchFilter', function(updated,previous) {
        if (fs.isLeftTreeClick) { // check to see if left nav was clicked, if so bypass the dropdown search //
            fs.isLeftTreeClick = false;
        }
        else {
            if (Object.keys(fs.searchFilter).length) {
                if (Object.keys(fs.searchFilter).length==1) { // dont do a search because only program area is selected //
                    // console.log("NO SEARCH");
                }
                else {

                    // do search because at least one dropdown besides program area is selected //

                    var newobj = angular.copy(updated);
                    var prevobj = angular.copy(previous);
                    delete newobj.registrationStatus;
                    delete prevobj.registrationStatus;
                    delete newobj.workFlowStatus;
                    delete prevobj.workFlowStatus;

                    if(!angular.equals(newobj,prevobj)){
                        $scope.onClickBasicSearch(
                            fs.dataElementVariables.basicSearchQuery,
                            'name',
                            fs.dataElementVariables.searchDEC,
                            fs.dataElementVariables.searchPV,
                            fs.dataElementVariables.searchPVQueryType,
                            fs.dataElementVariables.selectedQueryType,
                            fs.dataElementVariables.searchVD,
                            fs.dataElementVariables.searchVDTQueryType,
                            "",
                            fs.dataElementVariables.conceptInput,
                            fs.dataElementVariables.conceptQueryType,
                            fs.dataElementVariables.searchAltName,
                            fs.dataElementVariables.searchAltNameTypes,
                            fs.dataElementVariables.searchVersions,
                            fs.dataElementVariables.searchContextUse,
                            fs.dataElementVariables.searchObjectClass);

                        $scope.breadCrumbs = fs.createBreadcrumbs();
                    }
                };
            };
        };
    },true);

    // reset filters //
    $scope.resetFilters = function() {
        fs.resetFilters();
        $scope.resetSortOrder();
        $scope.searchResults = [];
        $scope.tableParams.settings({ dataset: []});
        $scope.breadCrumbs = [];
        groupFactory.clearData();
        groupFactory1.clearData();
    };

    $scope.search=function(){
        // console.log($scope.fs);

        $rootScope.$broadcast('genericsearch', $scope.fs);

    }

    // When a context is changed, get classifications and protocol forms //
    $scope.contextSearch = function(contextId) {
        $http.get('/cdebrowserServer/rest/lookupdata/protocol',{params:{contextIdSeq:contextId.idSeq}}).success(function(response) {
            groupFactory.fillProtocols(response);
            $scope.filterService.protocols = groupFactory.load(0);
        });
        $http.get('/cdebrowserServer/rest/lookupdata/classificationscheme',{params:{contextIdSeq:contextId.idSeq}}).success(function(response) {
            groupFactory1.fillClassifications(response);
            $scope.filterService.classifications = groupFactory1.load(0);
        });
    };

    // // selects dropdown values based on search left tree click //
    $scope.selectFiltersByNode = function(searchType,id, selectedNode) {
        fs.selectFiltersByNode(searchType, id, selectedNode, $scope.currentTab);
    };
    /* End of filter service */

    $scope.$storage = $sessionStorage;
    $scope.$storage.cartService = cartService;
    $scope.cartService = cartService;
    $scope.authenticationService = authenticationService;
    $scope.downloadFactory = new downloadFactory();  // create download factory //

    $scope.show = [];
    $scope.initComplete = false;
    $scope.haveSearchResults = false;
    $scope.showCdeSearchResults = true;
    $scope.searchResults = [];
    $scope.cdeTabHasFocus = [];
    $scope.tabsDisabled = true;
    $scope.cssClasses = {"NORMAL":0,"BIG":1,"ERROR":2}
    $scope.isNode = false;
    $scope.checkedItemsForDownload = [];
    $scope.progressMessage = {"status":0,"message":"Exporting Data", "isErrorMessage":0}; // set status to 0 if message should not be displayed. Set isErrorMessage to 1 if error message //
    $scope.showSearch = true; // important variable used to show and hide search area when changing routes //

    // watch to load empty area for new route. When new route is called ex: cdeCart, cdeCart will set showSearch to false //
    // this watch makes sure when search is reloaded to load the currect tab as well as re-show the search //
    $scope.$watch('searchFactory.showSearch',function(){
        if ($scope.searchFactory.showSearch==true) {
            $scope.changeView(0,{title: 'Search Results',view: 'search'});
        };
    });

    var isInitialColumnClick = 0; // used for sort order direction override. See $scope.$watch('tableParams.sorting()' function //

    // Search query types - radio buttons
    $scope.searchQueryTypes = [
        {id: 0, name: "Exact phrase"},
        {id: 1, name: "All of the words"},
        {id: 2, name: "At least one of the words"}
    ];

    // Search versions - radio buttons
   $scope.searchVersions = [
        {id: 0, name: "Latest Version"},
        {id: 1, name: "All Versions"},
    ];

    // Search Context use options
   $scope.searchContextUseValues = [
       "Owned By",
        "Used By",
        "Owned By/Used By"
    ];

    $scope.activeSearchTab = 0;
    $scope.searchTabs = [{title:"Data Element Search",disabled:false},{title:"Public ID Search",disabled:false},{title:"Search Preferences",disabled:false}];
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
        angular.forEach($scope.searchResults, function (item) {
            if (angular.isDefined(item.deIdseq)) {
                $scope.checkboxes.items[item.deIdseq] = value;
            };
            // creates array of items to send when a user clicks download on any of the download buttons //
            var indexOfItem = $scope.checkedItemsForDownload.indexOf(item.deIdseq);
            if ($scope.checkboxes.items[item.deIdseq]==false || $scope.checkboxes.items[item.deIdseq] == undefined) {
                if (indexOfItem!=-1) {
                    if ($scope.checkboxes.checked==false) {
                        $scope.checkedItemsForDownload.splice(indexOfItem,1);
                    }
                }
            }
            else {
                if ($scope.checkedItemsForDownload.indexOf(item.deIdseq)==-1) {
                    $scope.checkedItemsForDownload.push(item.deIdseq);
                }
            }
        });
    });

    // watch for search results. reset checkboxes and checked items //
    $scope.$watch('searchResults', function () {
        $scope.checkboxes.checked=false;
        $scope.checkboxes.items = {};
        $scope.checkedItemsForDownload = [];
    });


    // watch for data checkboxes
    $scope.$watch('checkboxes.items', function (values) {

        var checked = 0, unchecked = 0,
            total = $scope.searchResults.length;
        angular.forEach($scope.searchResults, function (item) {
            checked += ($scope.checkboxes.items[item.deIdseq]) || 0;
            unchecked += (!$scope.checkboxes.items[item.deIdseq]) || 0;

            // creates array of items to send when a user clicks download on any of the download buttons //
            var indexOfItem = $scope.checkedItemsForDownload.indexOf(item.deIdseq);
            if ($scope.checkboxes.items[item.deIdseq]==false || $scope.checkboxes.items[item.deIdseq] == undefined) {
                if (indexOfItem!=-1) {
                    $scope.checkedItemsForDownload.splice(indexOfItem,1);
                }
            }
            else {
                if ($scope.checkedItemsForDownload.indexOf(item.deIdseq)==-1) {
                    $scope.checkedItemsForDownload.push(item.deIdseq);
                }
            }
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
        $scope.getCdeDetailRestCall(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/CDEData?deIdseq=" + deIdseq);

    };

    //Multiple CDE details - used for compare
    $scope.multipleCdeDetails = function (deIdseq) {
        $scope.getCdeDetailRestCall(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/multiCDEData?deIdseq=" + deIdseq);
    };


    // function that gets the data returned for CDE details //
    $scope.getCdeDetailRestCall = function (serverUrl) {
        $scope.searchResultsMessage = "Searching";
        $scope.bigSearchResultsMessageClass = true;
        $http.get(serverUrl).success(function (response) {
            $scope.tabsDisabled = false;
            // Change to "Data Element" tab
            $scope.changeView(1, $scope.tabs[1]);
            $scope.cdeDetails = response;

            $scope.searchResultsMessage = "";
            $scope.searchResultsCount = "Results: " + $scope.searchResults.length;
            $scope.bigSearchResultsMessageClass = false;
        });
    };

    $scope.isActiveCdeTab = function (cdeTabNumber) {
        return cdeTabNumber == $scope.currentCdeTab;
    };

    $scope.gotFocus = function (tabnumber) {
        $scope.cdeTabHasFocus[tabnumber] = true;
    };

    $scope.lostFocus = function (tabnumber) {
        $scope.cdeTabHasFocus[tabnumber] = false;
    };

    $scope.changeView = function (tabnumber, tab) {
        $location.path(tab.view);
        $scope.showSearch = true;
        $scope.currentCdeTab = tabnumber;

        if (tabnumber == 0) {
            $scope.showCdeSearchResults = true;
        }
        else
        {
            $scope.showCdeSearchResults = false;
        }

    };

    $scope.changeLocation = function (location) {
        // $scope.showSearch = false;
        $location.path(location).replace();
    };


    //Set to first tab - CDE Search tab
    $location.path("search");
    $scope.currentCdeTab = 0;

    // Search button
    $scope.onClickBasicSearch = function (query, field, dec, pv, pvType, type, vd, vdtType, conceptInput, conceptQueryType, publicIdName, searchAltName, searchAltNameType, filteredinput, searchVersions, searchContextUse, searchObjectClass) {
        var str = '';
        // Get searchAltNameType type field from searchAltNameType object
        for (var p in searchAltNameType) {
            if (searchAltNameType.hasOwnProperty(p)) {
                str += searchAltNameType[p].type + delimiter;
            }
        }
        searchAltNameType = str;

        // Convert searchContextUse string to index integer

        searchContextUse = $scope.searchContextUseValues.indexOf(searchContextUse.toString());
        
        // console.log("[" + searchContextUse + "]");


        $scope.currentCdeTab = 0;
        $location.path("/search").replace(); // change url to search since we are doing a search //

        var c=0; // index of searchFilter key //
        if (query!='') { // create base url. determine if query is blank //
            var url = "".concat("cdebrowserServer/rest/search?", field, "=",query); // search has a query value //

            if(typeof type != 'undefined' )
            {
                url += "&queryType="+type;
                c++;
            }

            if (publicIdName && publicIdName!='') {
                url=url.concat("&name=",publicIdName)
                c++;
            };
        }
        else {
            var url = "".concat("cdebrowserServer/rest/search");
        };

        var  connector="";

        if( dec != '') {
            connector= c==0?"?":"&";
            c++;
            url += connector + "dataElementConcept=" + dec;
        }

        if( pv != '') {
            connector= c==0?"?":"&";
            c++;
            url += connector + "permissibleValue=" + pv;
            url += "&pvQueryType=" + pvType;
        }

        if( vd != '') {
            connector= c==0?"?":"&";
            c++;
            url += connector + "valueDomain=" + vd;
            url += "&vdtQueryType=" + vdtType;
        }

        if (searchAltName != '') {
            connector = c == 0 ? "?" : "&";
            c++;
            url += connector + "altName=" + searchAltName;
            url += "&altNameType=" + searchAltNameType;
        }

        if (filteredinput != '') {
            connector = c == 0 ? "?" : "&";
            c++;
            url += connector + "filteredinput=" + filteredinput;
        }

        if (searchVersions >= 0) {
            connector = c == 0 ? "?" : "&";
            c++;
            url += connector + "versionType=" + searchVersions;
        }

        if (conceptInput != '') {
            connector = c == 0 ? "?" : "&";
            c++;
            url += connector + "conceptInput=" + conceptInput;
            url += connector + "conceptQueryType=" + conceptQueryType;
        }

        if ( searchContextUse >= 0) {
            connector = c == 0 ? "?" : "&";
            c++;
            url += connector + "contextUse=" + searchContextUse;
        }

        if( searchObjectClass != '') {
            connector= c==0?"?":"&";
            c++;
            url += connector + "objectClass=" + searchObjectClass;
        }


        for (var x in fs.searchFilter) {


            if (fs.searchFilter[x]&&field=='name') {
                connector= c==0?"?":"&";
                c++;
                if (query==''&&  typeof fs.searchFilter[x]!='object') {
                    if (x=='programArea') {
                        url+=connector+x+"="+$scope.contextListMaster[fs.searchFilter[x]].text;
                    }
                    else {
                        url+=connector+x+"="+fs.searchFilter[x];
                    };
                }
                else {

                    if (x=='programArea') {
                        url+=connector+x+"="+$scope.contextListMaster[fs.searchFilter[x]].text;
                    }
                    else {

                        if (x=='protocol') {
                            if(fs.searchFilter[x].id==fs.searchFilter[x].protocolIdSeq){
                                url+=connector+x+"="+fs.searchFilter[x].id;
                            }
                            if(fs.searchFilter[x].id==fs.searchFilter[x].formIdSeq){
                                url+=connector+"formIdSeq"+"="+fs.searchFilter[x].id;
                            }
                        }
                        if (x=='classification') {
                            if(fs.searchFilter[x].id==fs.searchFilter[x].csIdSeq){
                                url+=connector+x+"="+fs.searchFilter[x].id;
                            }
                            if(fs.searchFilter[x].id==fs.searchFilter[x].csCsiIdSeq){
                                url+=connector+"csCsiIdSeq"+"="+fs.searchFilter[x].id;
                            }
                        }else{
                            url+=connector+x+"="+fs.searchFilter[x].toString();
                        }
                    };
                };
            };
        };

        $scope.searchServerRestCall(url);
        if (field=='publicId') {
            $scope.breadCrumbs = [$scope.contextListMaster[0].text]; // only list breadcrumbs as all program areas for public id //
        }
        else {
            //$scope.breadCrumbs = [$scope.contextListMaster[fs.searchFilter.programArea].text];
            $scope.breadCrumbs = fs.createBreadcrumbs();
        }

        // Restore the view of search results table
        $scope.changeView(0,0);
        $scope.resetSortOrder();

    };

    // sets sort order for columns that should not be alphabetical //
    $scope.setSortOrder = function () {
        angular.forEach($scope.searchResults, function (item) {
            var rS = $scope.registrationSort.indexOf(item.registrationStatus);
            var wS = $scope.workflowSort.indexOf(item.workflowStatus);
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
    $scope.searchServerRestCall = function (serverUrl, searchType, id, isNode, isDropdown, selectedNode) {
        // $scope.searchServerRestCall = function (serverUrl,isNode, id, type) {
        // if clicking on a node in the left menu set the isNode variable to it's opposite, this will trigger the search box to clear //
        var url = "".concat('/',serverUrl,'?',searchType,'=',id);
        if (searchType==undefined) { // used when doing keyword search //
            var url = "".concat("/",serverUrl)
        }

        $scope.searchFactory.showSearch = true;

        // check if user clicked the left tree or used dropdown to search. If so clear out the search //
        if (isNode) {
            if (!isDropdown) // check if user selected dropdown instead of tree //
            {
                $scope.selectFiltersByNode(searchType,id, selectedNode);
            };
            $scope.isNode  = !$scope.isNode;
        }
        // reset filters if user searches using the text box //
        else {
            // $scope.resetFilters();
        }

        $scope.tabsDisabled = true;
        $scope.haveSearchResults = false;
        $scope.searchResultsMessage = "Searching";
        fs.isSearching = true;
        $scope.bigSearchResultsMessageClass = true;
        $scope.progressMessage.status=0;

        $http.get(url).success(function (response) {
            fs.isSearching = false;
            // $scope.tableParams.$params.page = 1;
            $scope.searchResults = response;
            $scope.tableParams.settings({ dataset: response });
            if ($scope.searchResults.length > 0) {

                // TODO Quick hack, make status message better when time permits.
                // check for error here .status & .longName
                if (response[0].status != 0) {
                    for (f = 0; f < $scope.breadCrumbs.length; f++) {
                        $scope.breadCrumbs[f] = "";
                    }
                    $scope.searchResultsMessage = "";
                    $scope.statusMessage = response[0].longName;
                }
                else {

                    $scope.setSortOrder();
                    $scope.haveSearchResults = true;
                    $scope.bigSearchResultsMessageClass = false;
                    $scope.searchResultsMessage = "";
                    $scope.searchResultsCount = "Results: " + $scope.searchResults.length;
                    $scope.tableParams.reload();
                }
                $scope.haveSearchResults = true;

            }
            else {
                $scope.searchResultsMessage = "";
                $scope.searchResultsCount = "No search results";
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
        return tabUrl == $scope.currentTab;
    };

    $scope.versionDisplay = function(ver, tag, scmDate, tier) {
        $window.alert('Version: ' + ver + "\n\nSMC Tag: " + tag + "\n\nSMC Date: " + scmDate + "\n\nSystem Tier: " + tier );
    }

    //Just top three levels of the tree
    // $scope.dataLoad1 = function () {
    //     $scope.dataLoad("data1.json");
    // };

    // //Full Tree
    // $scope.dataLoad2 = function () {
    //     $scope.dataLoad("data2.json");
    // };

    // $scope.dataLoad3 = function () {
    //     $scope.dataLoad("data3.json");
    // };

    // $scope.dataLoad4 = function () {
    //     $scope.dataLoad("data4.json");
    // };

    // $scope.dataLoad5 = function () {
    //     $scope.dataLoad("data5.json");
    // };

    //   $scope.dataLoad6 = function () {
    // $scope.dataLoad("data6.json");
    // };
     
    $scope.getAlternateNameTypesFromServer = function () {
        var serverUrl = "altNameType.json"
        $http.get(serverUrl)

            .success(function (response) {
                $scope.alternateNameTypes = response;
            })
            .error(function (error) {
                // console.log("Error [" + serverUrl + "]: " + error.statusText);
            });

    };


    $scope.dataLoadFromServer = function () {
        $scope.staticFilters = {};
        $scope.dataLoad(window.location.protocol + "//" +  window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/contextData");


        // load registration sort and workflow sort arrays. Will be used for sorting and filters. Put other filters here as well if needed //
        $http.get('/cdebrowserServer/rest/searchPreferences').then(function(response) {
            $scope.workflowStatusExcluded = response.data.workflowStatusExcluded;
            $scope.registrationStatusExcluded = response.data.registrationStatusExcluded;
            $scope.treeStatus = {
                test:response.data.excludeTest,
                training:response.data.excludeTraining};
        }).then(function() {
            $http.get('/cdebrowserServer/rest/lookupdata/workflowstatus').then(function(response) {
                $scope.workflowStatuses = response.data;
                $scope.workflowSort = [];
                $scope.workflowSort = $scope.workflowStatuses.filter(function(x){ return $scope.workflowStatusExcluded.indexOf(x)<0});
                angular.forEach($scope.models, function(list) {
                    switch(list.label) {
                        case "workflowStatusIncluded":
                            for (var i = 0; i <= $scope.workflowSort.length - 1; i++) {
                                list.items.push({label: $scope.workflowSort[i], type: "workflowStatus"});
                            }
                            break;
                        case "workflowStatusExcluded":
                            for (var i = 0; i <= $scope.workflowStatusExcluded.length - 1; i++) {
                                list.items.push({label: $scope.workflowStatusExcluded[i], type: "workflowStatus"});
                            }
                            break;
                    }
                });
                $scope.staticFilters.workflowStatusFilter = angular.copy($scope.workflowSort).sort();
            });
        }).then(function() {
            $http.get('/cdebrowserServer/rest/lookupdata/registrationstatus').then(function(response) {
                $scope.registrationStatuses = response.data;
                $scope.registrationSort = [];
                $scope.registrationSort = $scope.registrationStatuses.filter(function(x){ return $scope.registrationStatusExcluded.indexOf(x)<0});
                angular.forEach($scope.models, function(list) {
                    switch(list.label) {
                        case "registrationStatusIncluded":
                            for (var i = 0; i <= $scope.registrationSort.length - 2; i++) { // "length - 2" since the last object is empty
                                list.items.push({label: $scope.registrationSort[i], type: "registrationStatus"});
                            }
                            break;
                        case "registrationStatusExcluded":
                            for (var i = 0; i <= $scope.registrationStatusExcluded.length - 1; i++) {
                                list.items.push({label: $scope.registrationStatusExcluded[i], type: "registrationStatus"});
                            }
                            break;
                    }
                });
                $scope.staticFilters.registrationStatusFilter = angular.copy($scope.registrationSort).sort();
                $scope.staticFilters.registrationStatusFilter.splice(0,1); // remove empty value
            });
        }).then(function() {
            $http.get('/cdebrowserServer/rest/lookupdata/alternateType').then(function(response) {
                $scope.alternateNameTypes = response.data;
            });
        });
    };


    $scope.$on('updateTree',function(event,data) {
        var filtered_data = $filter('filter')($scope.contextListMaster[$scope.currentTab].children,function(item,index,array) {
            var search_text=item.text;
            if(search_text.indexOf('Test')!=-1) {
                angular.element(document.getElementById(item.idSeq)).parent().prop('hidden', data.test);
            }
            else if(search_text.indexOf('Training') !=-1) {
                angular.element(document.getElementById(item.idSeq)).parent().prop('hidden', data.training);
            }
            return false;
        });
        //filterService.serverData[filterService.searchFilter.programArea].children 
    });


    $scope.dataLoad = function (dataSource) {
        $scope.waitMessage = "    Loading";
        $scope.messageClass = $scope.cssClasses["BIG"];

        $http.get(dataSource).success(function (response) {

            if (response[0].status == $scope.ERROR) {
                $scope.waitMessage = response[0].text.replace(/(?:\r\n|\r|\n)/g, "\n<br>");
                $scope.messageClass = $scope.cssClasses["ERROR"];
            }
            else {
                $scope.contextListMaster = response;
                $scope.waitMessage = "";
                $scope.messageClass = $scope.cssClasses["NORMAL"];

                //FIXME move this
                $scope.initComplete = true;
                $scope.onClickTab(0);

                $scope.treeLoad();

            }
        });

    };


    $scope.treeStatus = { };

    $scope.treeLoad = function() {
        $timeout(function() {
            $scope.$broadcast('updateTree',
                {
                    test:$scope.treeStatus.test,
                    training:$scope.treeStatus.training
                })
        },100);
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
            $scope.versionPopover = response;
            $scope.versionPopover.templateUrl = 'versionPopoverTemplate.html';
            $scope.versionPopover.title2 = 'CDE Browser';
            $scope.versionPopover.shortBuildVersionORIG = response.scm_date;
            $scope.versionPopover.shortBuildVersion = response.scm_date.replace(/ .*/,"");
        });
    }


    // start ngTable definition //
    $scope.resetSortOrder = function () {
        isInitialColumnClick = 0;
        $scope.tableParams.sorting({'registrationSort': 'asc','workflowSort':'asc','longName':'asc'});

        /*  FIXME still not getting the "view" back after reset sort order  */
        //$scope.changeView(0,$scope.tabs[0]);
        $scope.currentCdeTab = 0;
        $location.path("/search").replace();


    };

    // returns array of sort order for the sort order breadcrumbs //
    // ng-repeat sends back keys in the wrong order //
    // defaults sort on single colsearchResultsMessageumn click to ascending //
    $scope.$watch('tableParams.sorting()', function() {
        // because of how ng-table is setup, sorting with multiple columns makes ng table //
        // sort the first single column clck in reverse order. Because the 3 column sort //
        // is ascending, clicking on long name, registration status or workflow status //
        // sorts those three columns in descending order (unwanted). This will override //
        if (!isInitialColumnClick) {
            isInitialColumnClick = 1;
        }
        else if (isInitialColumnClick==1) {
            var newSort = {};
            for (item in $scope.tableParams.sorting()) {
                newSort[item]="asc";
                $scope.tableParams.sorting(newSort);
                $scope.tableParams.reload();
            };
            isInitialColumnClick = 2;
        }
        // end override of column sort direction //

        var displayOrder = {"asc":"Ascending","desc":"Descending"}
        var sortOrderObject = {"sortDirection":"asc","items":[]};
        for (item in $scope.tableParams.sorting()) {
            sortOrderObject.items.push($scope.sortNames[item]);
            sortOrderObject.sortDirection = displayOrder[$scope.tableParams.sorting()[item]];
        };
        $scope.sortOrderObject = sortOrderObject;
    });

    // initialize table params //

    $scope.initTableParams = function () {

        $scope.tableParams = new NgTableParams(
            {
                count:100,
                page:1,
                filter: { },
                sorting: {
                    registrationSort: 'asc',
                    workflowSort: 'asc',
                    longName: 'asc'
                }
            },
            {
                defaultSort:"asc",
                counts:[],
                dataset:[]

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

    $scope.initTableParams(); // init table params //
    $scope.hideContexts();
    //$scope.getAlternateNameTypesFromServer();

    $scope.dataLoadFromServer();
    
    // $scope.dataLoad6();
     

    $scope.versionData();
    $scope.getToolHosts();

    // change search tab section tabs //
    $scope.changeSearchTab = function(tabIndex) {
        $scope.activeSearchTab = tabIndex;
        if (tabIndex === 0)
        {
            $http.get('/cdebrowserServer/rest/searchPreferences').then(function(response) {
                $scope.workflowStatusExcluded = response.data.workflowStatusExcluded;
                $scope.registrationStatusExcluded = response.data.registrationStatusExcluded;
            }).then(function() {
                $http.get('/cdebrowserServer/rest/lookupdata/workflowstatus').then(function(response) {
                    $scope.workflowStatuses = response.data;
                    $scope.workflowSort = [];
                    $scope.workflowSort = $scope.workflowStatuses.filter(function(x){ return $scope.workflowStatusExcluded.indexOf(x)<0});
                    angular.forEach($scope.models, function(list) {
                        switch(list.label) {
                            case "workflowStatusIncluded":
                                for (var i = 0; i <= $scope.workflowSort.length - 1; i++) {
                                    list.items.push({label: $scope.workflowSort[i], type: "workflowStatus"});
                                }
                                break;
                            case "workflowStatusExcluded":
                                for (var i = 0; i <= $scope.workflowStatusExcluded.length - 1; i++) {
                                    list.items.push({label: $scope.workflowStatusExcluded[i], type: "workflowStatus"});
                                }
                                break;
                        }
                    });
                    $scope.staticFilters.workflowStatusFilter = angular.copy($scope.workflowSort).sort();
                });
            }).then(function() {
                $http.get('/cdebrowserServer/rest/lookupdata/registrationstatus').then(function(response) {
                    $scope.registrationStatuses = response.data;
                    $scope.registrationSort = [];
                    $scope.registrationSort = $scope.registrationStatuses.filter(function(x){ return $scope.registrationStatusExcluded.indexOf(x)<0});
                    angular.forEach($scope.models, function(list) {
                        switch(list.label) {
                            case "registrationStatusIncluded":
                                for (var i = 0; i <= $scope.registrationSort.length - 2; i++) { // "length - 2" since the last object is empty
                                    list.items.push({label: $scope.registrationSort[i], type: "registrationStatus"});
                                }
                                break;
                            case "registrationStatusExcluded":
                                for (var i = 0; i <= $scope.registrationStatusExcluded.length - 1; i++) {
                                    list.items.push({label: $scope.registrationStatusExcluded[i], type: "registrationStatus"});
                                }
                                break;
                        }
                    });
                    $scope.staticFilters.registrationStatusFilter = angular.copy($scope.registrationSort).sort();
                    $scope.staticFilters.registrationStatusFilter.splice(0,1); // remove empty value
                });
            });
        }
    };

    // add items to cart //
    $scope.addCDE = function() {
        $scope.cartService.addCDE($scope.checkedItemsForDownload,$scope.searchResults);
    };

    // downloads selected search results to an excel file //
    $scope.downloadToExcel = function(param) {
        $scope.downloadFactory.downloadToExcel(param,$scope.checkedItemsForDownload);
    };

    // downloads selected search results to an excel file //
    $scope.downloadToXML = function() {
        $scope.downloadFactory.downloadToXML($scope.checkedItemsForDownload);
    };

    // log the user out //
    $scope.logout = function() {
        $scope.authenticationService.logout();
    };

    // add items to compare list //
    $scope.addToCompare = function() {
        $scope.compareService.addToCompare($scope.checkedItemsForDownload,$scope.searchResults);
    };

    // compare the items in the compare list along with any checked items //
    $scope.compareCDE = function() {
        $scope.compareService.compareCDE($scope.checkedItemsForDownload,$scope.searchResults);
        $location.path("/cdeCompare").replace();
    };

    // Start of Advance Search Options //

    $scope.more = false;


    // CHECKME - it may be better to get rid of all these hold values, and just use $scope.more to determine if they should be treated as empty as we build the search url string,
    // but that might make the URL building code less flexible...

    // We now have radio button type values which can be used by themselves, we have been indicating that we don't want to use field by making it empty (so no need for it's radio button).
    // This won't work for radio button type values which can be used by themselves, I am setting values for these radio button types to "-1" to indicate not to use in the search.
    $scope.advanceSearchShow = function() {
        $scope.more = $scope.more ? false : true;


        // If Advanced Search is hidden, clear the advanced Search fields.
        // If advanced search is shown, restore any fields that where cleared on hide.
        if( ! $scope.more) {

            $scope.dataElementConceptHOLD = fs.dataElementVariables.searchDEC;
            fs.dataElementVariables.searchDEC = "";

            $scope.valueDomainHOLD = fs.dataElementVariables.searchVD;
            fs.dataElementVariables.searchVD = "";

            $scope.permissibleValueHold = fs.dataElementVariables.searchPV;
            fs.dataElementVariables.searchPV = "";

            $scope.searchAltNameHOLD = fs.dataElementVariables.searchAltName;
            fs.dataElementVariables.searchAltName = "";

            $scope.searchVersionsHOLD = fs.dataElementVariables.searchVersions;
            fs.dataElementVariables.searchVersions = -1;

            $scope.searchContextUseHOLD = fs.dataElementVariables.searchContextUse;
            fs.dataElementVariables.searchContextUse = -1;

            $scope.searchObjectClassHOLD = fs.dataElementVariables.searchObjectClass;
            fs.dataElementVariables.searchObjectClass = "";
        }
        else {
            fs.dataElementVariables.searchDEC = $scope.dataElementConceptHOLD;
            $scope.dataElementConceptHOLD = "";

            fs.dataElementVariables.searchVD = $scope.valueDomainHOLD;
            $scope.valueDomainHOLD = "";

            fs.dataElementVariables.searchPV = $scope.permissibleValueHold;
            $scope.permissibleValueHold = "";

            fs.dataElementVariables.searchAltName = $scope.searchAltNameHOLD;
            $scope.searchAltNameHOLD = "";

            fs.dataElementVariables.searchVersions = $scope.searchVersionsHOLD;
            $scope.searchVersionsHOLD = -1;

            fs.dataElementVariables.searchContextUse = $scope.searchContextUseHOLD;
            $scope.searchContextUseHOLD = -1;

            fs.dataElementVariables.searchObjectClass = $scope.searchObjectClassHOLD;
            $scope.searchObjectClassHOLD = "";
        }
    };



});
