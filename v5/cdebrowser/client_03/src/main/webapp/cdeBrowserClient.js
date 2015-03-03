var cdeBrowserApp = angular.module('cdeBrowserApp', ['cdebrowserTreeview','ngTable']);

// controller
cdeBrowserApp.controller('cdeBrowserController', function ($scope, $http, $filter, ngTableParams) {

    $scope.show = [];

    console.log("$scope.show len: " + $scope.show.length);
    $scope.currentTab = '0';
    $scope.initComplete = false;
    $scope.haveSearchResults = false;

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
    var data = $scope.searchResults;

    $scope.tableParams = new ngTableParams(
        {
            page: 1,            // show first page
            count: 20           // count per page     
        },
        {
            counts: [], // hide page counts control

            total: data.length, // length of data
            getData: function($defer, params) {
            // use build-in angular filter
            var orderedData = params.sorting() ? 
            $filter('orderBy')(data, params.orderBy()) : data;
            $defer.resolve(orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            } 
        }); 
  
});


(function (angular) {
    'use strict';
    angular.module('cdebrowserTreeview', [])

        .directive('resize', function ($window) {
            return function (scope, element, attr) {

                var w = angular.element($window);
                scope.$watch(function () {
                    return {
                        'h': window.innerHeight,
                        'w': window.innerWidth
                    };
                }, function (newValue, oldValue) {
                    scope.windowHeight = newValue.h;
                    scope.windowWidth = newValue.w;

                    scope.resizeHeightWithOffset = function (offsetH) {
                        scope.$eval(attr.notifier);
                        return {
                            'height': (newValue.h - offsetH) + 'px'
                        };
                    };
                    scope.resizeHeight = function () {
                        scope.$eval(attr.notifier);
                        return {
                            'height': (newValue.h - properties.bottomOffset) + 'px'
                        };
                    };
                }, true);

                w.bind('resize', function () {
                    scope.$apply();
                });
            }
        })


        .directive('treeModel', ['$compile', function ($compile) {
            return {
                restrict: 'A',
                link: function (scope, element, attrs) {

                    /*
                     The name convention for the "attrs":

                     data-tree-id treeId
                     data-node-isParent nodeIsparent  <- Note: the p is not uppercase
                     data-tree-model  treeModel
                     data-node-type  nodeType
                     data-node-label  nodeLabel
                     data-node-hover  nodeHover
                     data-node-children   nodeChildren

                     nodeTypes: "Empty", "Container", "CSI", "Folder", "ProtocolFormsFolder", "CIS Folder", "Protocol";

                     */


                    //tree id
                    var treeId = attrs.treeId;

                    //tree model
                    var treeMenuModel = attrs.treeModel;

                    //node child type
                    var nodeChildType = attrs.nodeChildtype;

                    //node action
                    var nodeAction = attrs.nodeAction;

                    //node label
                    var nodeLabel = attrs.nodeLabel;

                    //Does this nod have children
                    var nodeIsParent = attrs.nodeIsparent || "1";

                    //node hover
                    var nodeHoverText = attrs.nodeHover;

                    //node type
                    var nodeType = attrs.nodeType;

                    //children
                    var nodeChildren = attrs.nodeChildren;

                    //tree template
                    var template =
                        '<ul>' +

                            //Start of the "treeModel forEach" loop
                            '<li data-ng-repeat="node in ' + treeMenuModel + '">' +

                            //Is a folder with children and is collapsed
                            '<i class="closedFolder" cl' +
                            //'title="C {{node.' + nodeHoverText + '}} Parent={{node.' + nodeIsParent + '}} Type[{{ node.' + nodeType + '}}]= {{ getNodeTypeStr(node.' + nodeType + ')}}   ChildType={{getNodeTypeStr(node.' + nodeChildType + ')}} "' +
                            'title="{{node.' + nodeHoverText + '}} "' +

                            ' data-ng-show="node.' + nodeChildren + '.length && ' +
                            ' ( node.collapsed && node.' + nodeType + ' == 3 || node.collapsed && node.' + nodeType + ' == 2 )"' +
                            ' data-ng-click="' + treeId + '.selectNodeHead(node, node.' + nodeChildType + ')" ></i>' +


                            //It is a Folder (Not protocol) Has NO children
                            '<i class="emptyFolder" cl' +
                            //'title="B {{node.' + nodeHoverText + '}} Parent={{node.' + nodeIsParent + '}} Type[{{ node.' + nodeType + '}}]= {{ getNodeTypeStr(node.' + nodeType + ')}}   ChildType={{getNodeTypeStr(node.' + nodeChildType + ')}} " ' +
                            'title="{{node.' + nodeHoverText + '}} "' +

                            ' data-ng-show="((node.' + nodeChildren + '.length == 0 && ' +
                            ' node.' + nodeType + ' != 2  && ' +
                            ' node.collapsed && node.' + nodeType + ' != 4 ) ' +
                            ' || (   node.' + nodeType + ' == 3  && node.' + nodeChildren + ' == 0) ' +
                            ' ) " ' +
                            ' data-ng-click="' + treeId + '.selectNodeHead(node, node.' + nodeChildType + ')" ></i>' +

                            //Is a Protocol folder with children and is collapsed
                            '<i class="protocolFolderClosed" cl' +
                            //'title="C {{node.' + nodeHoverText + '}} Parent={{node.' + nodeIsParent + '}} Type[{{ node.' + nodeType + '}}]= {{ getNodeTypeStr(node.' + nodeType + ')}}  ChildType={{getNodeTypeStr(node.' + nodeChildType + ')}} " ' +
                            'title="{{node.' + nodeHoverText + '}} "' +

                            ' data-ng-show="node.' + nodeChildren + '.length && node.collapsed && node.' + nodeType + ' == 4"' +
                            ' data-ng-click="' + treeId + '.selectNodeHead(node, node.' + nodeChildType + ')" ></i>' +


                            //Is a CSI folder with children and is collapsed
                            '<i class="csiFolderClosed" cl' +
                            //'title="C {{node.' + nodeHoverText + '}} Parent={{node.' + nodeIsParent + '}} Type[{{ node.' + nodeType + '}}]= {{ getNodeTypeStr(node.' + nodeType + ')}}   ChildType={{getNodeTypeStr(node.' + nodeChildType + ')}} " ' +
                            'title="{{node.' + nodeHoverText + '}} "' +

                            ' data-ng-show="node.' + nodeChildren + '.length && node.collapsed && node.' + nodeType + ' == 5"' +
                            ' data-ng-click="' + treeId + '.selectNodeHead(node, node.' + nodeChildType + ')" ></i>' +


                            //Is a Folder (Has child node(s)) and is expanded
                            '<i class="openFolder" ' +
                            //'title="D {{node.' + nodeHoverText + '}} Parent={{node.' + nodeIsParent + '}} Type[{{ node.' + nodeType + '}}]= {{ getNodeTypeStr(node.' + nodeType + ')}}   ChildType={{getNodeTypeStr(node.' + nodeChildType + ')}} " ' +
                            'title="{{node.' + nodeHoverText + '}} "' +

                            ' data-ng-show="node.' + nodeChildren + '.length && !node.collapsed && node.' + nodeType + ' != 4 && node.' + nodeType + ' != 5"' +
                            ' data-ng-click="' + treeId + '.selectNodeHead(node, node.' + nodeChildType + ')" ></i>' +

                            //Is a CSI folder and Has child node(s) and is expanded
                            '<i class="csiFolderOpen" ' +
                            //'title="E {{node.' + nodeHoverText + '}} Parent={{node.' + nodeIsParent + '}} Type[{{ node.' + nodeType + '}}]= {{ getNodeTypeStr(node.' + nodeType + ')}}   ChildType={{getNodeTypeStr(node.' + nodeChildType + ')}} " ' +
                            'title="{{node.' + nodeHoverText + '}} "' +

                            ' data-ng-show="node.' + nodeChildren + '.length && !node.collapsed && node.' + nodeType + ' == 5" ' +
                            ' data-ng-click="' + treeId + '.selectNodeHead(node, node.' + nodeChildType + ')" ></i>' +

                            //Is a Protocol folder and Has child node(s) and is expanded
                            '<i class="protocolFolderOpen" ' +
                            //'title="E {{node.' + nodeHoverText + '}} Parent={{node.' + nodeIsParent + '}} Type[{{ node.' + nodeType + '}}]= {{ getNodeTypeStr(node.' + nodeType + ')}}   ChildType={{getNodeTypeStr(node.' + nodeChildType + ')}} " ' +
                            'title="{{node.' + nodeHoverText + '}} "' +

                            ' data-ng-show="node.' + nodeChildren + '.length && !node.collapsed && node.' + nodeType + ' == 4" ' +
                            ' data-ng-click="' + treeId + '.selectNodeHead(node, node.' + nodeChildType + ')" ></i>' +


                            //End leaf (not a folder) that is NOT a Protocol form
                            '<i class="csi" ' +
                            //'title="{{node.' + nodeHoverText + '}}  Parent={{node.' + nodeIsParent + '}} Type[{{ node.' + nodeType + '}}]= {{ getNodeTypeStr(node.' + nodeType + ')}}   ChildType={{getNodeTypeStr(node.' + nodeChildType + ')}} " ' +
                            'title="{{node.' + nodeHoverText + '}} "' +

                            'data-ng-hide="node.' + nodeChildren + '.length || node.' + nodeType + ' != 2   " data-ng-click="' + treeId + '.selectNodeNorm(node, node.' + nodeAction + ' )"></i> ' +

                            //End leaf (not a folder) that IS a Protocol form
                            '<i class="protocolForm" ' +
                            //'title="protocolForm {{node.' + nodeHoverText + '}}  Parent={{node.' + nodeIsParent + '}} Type[{{ node.' + nodeType + '}}]= {{ getNodeTypeStr(node.' + nodeType + ')}}   ChildType={{getNodeTypeStr(node.' + nodeChildType + ')}} " ' +
                            'title="{{node.' + nodeHoverText + '}} "' +

                            'data-ng-hide="node.' + nodeChildren + '.length || node.' + nodeType + ' != 6"' +
                            ' data-ng-click="' + treeId + '.selectNodeNorm(node, node.' + nodeAction + ')"></i> ' +

                            //End leaf (not a folder)
                            '<span data-ng-class="node.selected" id="selectedNode" ' +
                            //'title="G {{node.' + nodeHoverText + '}} Parent={{node.' + nodeIsParent + '}} Type[{{ node.' + nodeType + '}}]= {{ getNodeTypeStr(node.' + nodeType + ')}}   ChildType={{getNodeTypeStr(node.' + nodeChildType + ')}} " ' +
                            'title="{{node.' + nodeHoverText + '}} "' +

                            'data-ng-click="' + treeId + '.selectNodeLabel(node, node.' + nodeAction + ')">{{node.' + nodeLabel + '}}</span>' +


                            '<div  data-ng-hide="node.collapsed" ' +
                            //'title="H {{node.' + nodeHoverText + '}}  *Parent={{node.' + nodeIsParent + '}} Type[{{ node.' + nodeType + '}}]= {{ getNodeTypeStr(node.' + nodeType + ')}} " ' +
                            'title="{{node.' + nodeHoverText + '}} "' +
                            'data-tree-id=' + treeId + ' ' +
                            'data-tree-model=node.' + nodeChildren + ' ' +
                            'data-node-action=' + nodeAction + ' ' +
                            'data-node-childType=' + nodeChildType + ' ' +
                            'data-node-hover=' + nodeHoverText + ' ' +
                            'data-node-isParent=' + nodeIsParent + ' ' +
                            'data-node-label=' + nodeLabel + ' ' +
                            'data-node-type=' + nodeType + ' ' +
                            'data-node-children=' + nodeChildren + ' ' +
                            '></div>' +

                            //End of the "treeModel forEach" loop
                            '</li>' +
                            '</ul>';

                    //When the user Clicks.
                    //check tree id, tree model
                    if (treeId && treeMenuModel) {
                        //console.log("Click 1");
                        //root node
                        if (attrs.cdebrowserTreeview) {

                            //create tree object if not exists
                            scope[treeId] = scope[treeId] || {};
                            //console.log("Click 2 " + scope[treeId].contextName);

                            //if user clicks on a folder icon
                            scope[treeId].selectNodeHead = scope[treeId].selectNodeHead || function (selectedNode, childType) {
                                //console.log("Click folder Icon ->[" + selectedNode.isParent +"]  Child type ->[" + childType +"]");
                                //Collapse or Expand
                                if (selectedNode.isParent == 1) {
                                    //console.log("Folder is parent");
                                    selectedNode.collapsed = !selectedNode.collapsed;
                                }

                            };
                            //if user clicks on a plan icon - acts the same as user clicking in the text
                            scope[treeId].selectNodeNorm = scope[treeId].selectNodeNorm || function (selectedNode) {
                                console.log("Click Norm Icon: " + selectedNode.contextName);
                                disp(selectedNode);
                            };

                            //if user clicks on the text
                            scope[treeId].selectNodeLabel = scope[treeId].selectNodeLabel || function (selectedNode, selectAction) {
                                console.log("Click on text, action: " + selectAction);
                                disp(selectedNode);
                            };
                        }

                        //Render template.
                        element.html('').append($compile(template)(scope));
                    }

                    // Used above (for now) when a user Clicks.
                    var disp = function (selNode) {

                        //remove highlight from previous node
                        if (scope[treeId].currentNode && scope[treeId].currentNode.selected) {
                            scope[treeId].currentNode.selected = undefined;
                        }

                        //set highlight to selected node
                        selNode.selected = 'selected';
                        console.log("selNode.text: [" + selNode.text + "]  selNode.action(href): [" + selNode.href + "]  selNode.hover: [" + selNode.hover + "]");
                        scope.displaySelected(selNode.text, selNode.href, selNode.hover);

                        //Update the current node with the one just selected.
                        scope[treeId].currentNode = selNode;
                    }

                }
            };
        }]);
})(angular);

