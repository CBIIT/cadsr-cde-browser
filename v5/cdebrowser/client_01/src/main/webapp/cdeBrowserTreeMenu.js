//angular module
var cdeBrowserApp = angular.module('cdeBrowserApp', ['cdebrowserTreeview']);


//controller
cdeBrowserApp.controller('cdeBrowserController', function ($scope, $http) {

    $scope.displaySelected = function (text, action, hover) {
        console.log(text + ", " + action + ", " + hover);
        $scope.selectedText = text;
        $scope.selectedAction = action;
        $scope.selectedHover = hover;
    };


    $scope.getHeights = function () {
        $scope.heights3 = angular.element(document.getElementById("treeMenu"))[0].offsetHeight;

    };

    $scope.dataLoad0 = function () {

        $http.get("data6.json").success(function (response) {
            $scope.contextList = response;
        });
        //$scope.waitMessage = "Loaded";
    };

    //This is used in tests to return the text version of a node type
    $scope.getNodeTypeStr = function (nodeType) {
        //console.log("nodeTypes[" + nodeType + "]: " + nodeTypes[nodeType]);
        return nodeTypes[nodeType];
    };


    $scope.dataLoad1 = function () {
        $scope.dataLoad("data1.json");
    };

    $scope.dataLoad2 = function () {
        $scope.dataLoad("data2.json");
    };

    $scope.dataLoad2b = function () {
        $scope.dataLoad("data3.json");
    };

    $scope.dataLoad3 = function () {
        $scope.dataLoad("http://" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/contextData?uiType=5");
    };

    $scope.dataLoad = function (dataSource) {
        console.log(dataSource);
        $scope.waitMessage = "Please wait, loading menu data.....";
        $scope.hideTree=1;
        $http.get(dataSource).success(function (response) {
            //console.log("From context_data Service:");
            //console.log( JSON.stringify( response) );
            $scope.contextList = response;
            $scope.waitMessage = "caDSR Contexts:";
            $scope.hideTree = 0;
        });
    };

    $scope.notifyServiceOnChange = function (scope, elem, attrs) {
        //console.log("Size: " + $scope.windowHeight);
    };


    /*
     $scope.dataLoad3();
     */

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

