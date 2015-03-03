var cdeBrowserApp = angular.module('cdeBrowserApp', ['cdebrowserTreeview']);

// controller
cdeBrowserApp.controller('cdeBrowserController', function ($scope, $http) {

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
        console.log("Clicked " + tab);
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

    $scope.dataLoad1 = function () {
        $scope.dataLoad("data1.json");
    };

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

            console.log("Back from context_data Service:");
            console.log( JSON.stringify( response) );
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
    $scope.dataLoadFromServer();
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

