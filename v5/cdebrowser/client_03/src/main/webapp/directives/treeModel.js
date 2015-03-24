        cdeBrowserApp.directive('treeModel', ['$compile','$http', function ($compile,$http) {
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

                                //If this a "Classifications" or "ProtocolForms" folder for a Context that has not been pulled yet, this click will trigger that rest call
                                //selectedNode.href is the id of the context on the server, use this to call rest service to get this context tree.
                                // The rest call will be something like - cdebrowserServer/oneContextData?contextId=selectedNode.href
                                console.log("Click folder Icon: " + selectedNode.contextName + "   " + selectedNode.href );
                                if (selectedNode.text=='Classifications'||selectedNode.text=='ProtocolForms') {
                                    console.log(selectedNode['dataLoaded']);
                                    if (!selectedNode['dataLoaded']) {
                                        $http.get("data4.json").success(function (response) {
                                            selectedNode['dataLoaded'] = true;
                                            selectedNode.children.push(                            {
                                "text":"New Tree Node",
                                "hover":"Place Holder",
                                "childType":0,
                                "href":"",
                                "type":0,
                                "programArea":0,
                                "idSeq":"",
                                "isParent":false,
                                "isChild":true,
                                "collapsed":true,
                                "treePath":[
                                    "All",
                                    "ABTC (Adult Brain Tumor\nConsortium) ",
                                    "Classifications",
                                    "Place Holder"
                                ],
                                "children":[

                                ],
                                "palName":"",
                                "palNameDescription":"",
                                "trimText":"Place Holder"
                            }
);
                                            console.log("called");
                                        });
                                    }
                                }
                                //Collapse or Expand
                                if (selectedNode.isParent == 1) {
                                    //console.log("Folder is parent");
                                    selectedNode.collapsed = !selectedNode.collapsed;
                                }

                            };
                            //if user clicks on a plan icon
                            scope[treeId].selectNodeNorm = scope[treeId].selectNodeNorm || function (selectedNode) {
                                disp(selectedNode);
                            };

                            //if user clicks on the text
                            scope[treeId].selectNodeLabel = scope[treeId].selectNodeLabel || function (selectedNode, selectAction) {
                                console.log("* * * Click on text, action: " + selectAction);
                                // Have they clicked on a Context, if so, I need to bring back CDEs to display as search results
                                if( selectAction.indexOf("cdesByContext") > -1)
                                {
                                    scope.basicSearchServerRestCall("http://" + window.location.hostname + ":" + window.location.port + "/" + selectAction);
                                }
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
                        scope.displaySelected(selNode,selNode.treePath,selNode.text, selNode.href, selNode.hover);

                        //Update the current node with the one just selected.
                        scope[treeId].currentNode = selNode;
                    }

                }
            };
        }]);
