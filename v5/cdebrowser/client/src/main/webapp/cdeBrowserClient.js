

// controller
angular.module("cdeBrowserApp").controller("cdeBrowserController", function ($window, $scope, $timeout,$localStorage,$sessionStorage,$http, $timeout,$filter, $location, $route, ngTableParams, searchFactory, cartService, filterService) {
    var fs = filterService // define service instance //
    $scope.filterService = fs; // set service to scope. Need to interact with view //
    $scope.$watch('contextListMaster',function(data) {
        if (data) {
            fs.serverData = $scope.contextListMaster;
            fs.selectedProgramArea = $scope.contextListMaster[0];
        };
    });
    window.scope = $scope;
    // fs.getServerData('/cdebrowserClient/temp_filter_data.json'); // load server data //

    // reset filters //
    $scope.resetFilters = function() {
        fs.resetFilters();
    };

    // do a search based on selected context //
    $scope.contextSearch = function() {
        $scope.basicSearchServerRestCall("cdebrowserServer/rest/cdesByContext","contextId", fs.selectedContext.idSeq, 1,1);
        fs.isAChildNodeSearch = false;
        fs.getClassificationsAndProtocolForms();
        $scope.breadCrumbs = fs.selectedContext.treePath;
    };

    // selects dropdown values based on search left tree click //
    $scope.selectFiltersByNode = function(searchType,id, selectedNode) {
        fs.isAChildNodeSearch = false;
        fs.selectedClassification = ""; fs.selectedProtocolForm = "";
        if (searchType=='contextId') {
            fs.selectContextByNode($scope.currentTab,id);
        }
        else {
            fs.isAChildNodeSearch = true;
            var currentContext = fs.getContextByName(selectedNode);
            if (searchType=='classificationSchemeItemId') {
                
                fs.selectedClassification = fs.getClassifficationOrProtocolByName(currentContext,angular.copy(selectedNode));
                // delete(fs.selectedClassification['selected'])
            }
            else if (searchType=='classificationSchemeId') {
                fs.selectedClassification = angular.copy(selectedNode);
                // delete(fs.selectedClassification['selected'])
            }
            else {
                fs.selectedProtocolForm = angular.copy(selectedNode);
                // delete(fs.selectedProtocolForm['selected'])
            };
        };
    };

    
    $scope.$storage = $sessionStorage;
    var cartService = cartService;
    $scope.$storage.cartService = cartService;
    $scope.cartService = cartService;

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
    $scope.searchFactory = searchFactory;

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

    $scope.activeSearchTab = 0;
    $scope.searchTabs = [{title:"Data Element Search",disabled:false},{title:"Public ID Search",disabled:false}];
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
        if (!$scope.records) {
            return;
        }
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

    }

    $scope.changeLocation = function (location) {
        // $scope.showSearch = false;
        $location.path(location).replace();
    }    


    //Set to first tab - CDE Search tab
    $location.path("search");
    $scope.currentCdeTab = 0;

    // Search button
    $scope.onClickBasicSearch = function (query, field, type) {
        $scope.currentCdeTab = 0;
        $location.path("/search").replace();
        $scope.basicSearchServerRestCall("".concat("cdebrowserServer/rest/basicSearchWithProgramArea?query=",query,"&field=",field,"&queryType=",type,"&programArea=",$scope.currentTab));

        $scope.breadCrumbs = [$scope.contextListMaster[$scope.currentTab].text];
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
    $scope.basicSearchServerRestCall = function (serverUrl,searchType, id, isNode, isDropdown, selectedNode) {
    // $scope.basicSearchServerRestCall = function (serverUrl,isNode, id, type) {
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
            $scope.resetFilters();
        }

        $scope.tabsDisabled = true;
        $scope.haveSearchResults = false;
        $scope.searchResultsMessage = "Searching";
        $scope.bigSearchResultsMessageClass = true;
        $scope.progressMessage.status=0;
         
        $http.get(url).success(function (response) {
            $scope.tableParams.$params.page = 1;
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
        $scope.filters = {};        
        $scope.dataLoad(window.location.protocol + "//" +  window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/contextData");
        
        // load registration sort and workflow sort arrays. Will be used for sorting and filters. Put other filters here as well if needed //
        $http.get('/cdebrowserServer/rest/lookupdata/registrationstatus').success(function (response) {
            $scope.registrationSort = response;
            $scope.filters.registrationStatusFilter = angular.copy($scope.registrationSort).sort();
        });  
        $http.get('/cdebrowserServer/rest/lookupdata/workflowstatus').success(function (response) {
            $scope.workflowSort = response;
            $scope.filters.workflowStatusFilter = angular.copy($scope.workflowSort).sort();
        });  

    };

    $scope.dataLoad = function (dataSource) {
        $scope.waitMessage = "Please wait, loading Context data\n (" + dataSource + ").....".replace(/(?:\r\n|\r|\n)/g, "\n<br>");
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

            }
        });

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
                $scope.tableParams.$params.sorting = newSort;
                $scope.tableParams.reload();
            };
            isInitialColumnClick = 2;
        }
        //end override of column sort direction //

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
        $scope.tableParams = new ngTableParams(

            {
                page: 1,            // show first page
                count: 100,           // count per page

                sorting: {
                    registrationSort: 'asc',
                    workflowSort: 'asc',
                    longName: 'asc'
                }
            },
            {
                $scope: $scope,
                defaultSort:"asc",
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

    // change search tab section tabs //
    $scope.changeSearchTab = function(tabIndex) {
        $scope.activeSearchTab = tabIndex;
    };

    // add items to cart //
    $scope.addCDE = function() {
        cartService.addCDE($scope.checkedItemsForDownload,$scope.searchResults);
    };

    // downloads selected search results to an excel file //
    $scope.downloadToExcel = function(param) {
        $scope.progressMessage = {"status":1,"message":"Exporting Data", "isErrorMessage":0}
        if (param) { // download to prior excel
          $http({method: 'POST', url: '/cdebrowserServer/rest/downloadExcel?src=deSearchPrior',data: $scope.checkedItemsForDownload}).
          success(function(data, status, headers, config) {
            window.location.href = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/downloadExcel/" + data;
            $scope.progressMessage.status=0;
          }).
          error(function(data, status, headers, config) {
            $scope.progressMessage = {"status":1,"message":data,"isErrorMessage":1};                
          });             
        }
        else { // download to excel 
          $http({method: 'POST', url: '/cdebrowserServer/rest/downloadExcel?src=deSearch',data: $scope.checkedItemsForDownload}).
          success(function(data, status, headers, config) {
            window.location.href = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/downloadExcel/" + data;
            $scope.progressMessage.status=0;
          }).
          error(function(data, status, headers, config) {
            $scope.progressMessage = {"status":1,"message":data,"isErrorMessage":1};                
          }); 
        }

    };

    // downloads selected search results to an excel file //
    $scope.downloadToXML = function() {
        $scope.progressMessage = {"status":1,"message":"Exporting Data", "isErrorMessage":0}            
        $http({method: 'POST', url: '/cdebrowserServer/rest/downloadXml?src=deSearch',data: $scope.checkedItemsForDownload}).
            success(function(data) {
                $scope.progressMessage.status=0;
                window.location.href = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/downloadXml/" + data;
        }).
        error(function(data, status, headers, config) {
            $scope.progressMessage = {"status":1,"message":data,"isErrorMessage":1};                
        });
    };

});
