'use strict';

var app = angular.module('App', []);


app.controller('NavCtrl', function ($scope, $http) {
    // $scope.breadcrumbs = [];
    $scope.hideMenu = 1;


    $scope.dataLoad1 = function () {
        $scope.waitMessage = "Please wait, loading menu data.....";
        $scope.hideMenu = 1;
        console.log("Load data 1");
        $http.get("data1.json").success(function (response) {
            $scope.waitMessage = "Please wait, Init menu data.....";
            console.log( JSON.stringify( response) );

            $scope.menu = response;
            $scope.waitMessage = "";
            $scope.contextHeading = "caDSR Contexts:";
            $scope.hideMenu = 0;
        });
    };

    $scope.dataLoad2 = function () {
        $scope.waitMessage = "Please wait, loading menu data.....";
        $scope.hideMenu = 1;
        console.log("Load data 2");
        console.log($scope.subsetCount);
        $http.get("data2.json").success(function (response) {
            $scope.waitMessage = "Please wait, Init menu data.....";
            //console.log( JSON.stringify( response) );

            $scope.menu = response;
            $scope.waitMessage = "";
            $scope.contextHeading = "caDSR Contexts:";
            $scope.hideMenu = 0;
        });
    };

    $scope.dataLoad3 = function () {
        $scope.waitMessage = "Please wait, loading menu data.....";
        $scope.hideMenu = 1;
        console.log("dataLoad3 = function ()");
        //$http.get("http://" + window.location.hostname + ":"+ window.location.port + "/cdebrowserServer/contextData?uiType=" + $scope.subsetCount ).success(function (response) {
        $http.get("http://" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/contextData?uiType=5").success(function (response) {
            //console.log( JSON.stringify( response) );
            $scope.waitMessage = "Please wait, Init menu data.....";
            $scope.menu = response;
            $scope.waitMessage = "";
            $scope.contextHeading = "caDSR Contexts:";
            $scope.hideMenu = 0;
        });
    };

    $scope.dataLoad3();
 //   $scope.waitMessage = "";

});


/* Directives */


app.directive('navMenu', ['$parse', '$compile', function ($parse, $compile) {
        return {
            restrict: 'C', //Element
            scope: true,
            link: function (scope, element, attrs) {
                scope.selectedNode = null;

                scope.$watch(attrs.menuData, function (val) {

                    var template = angular.element('<ul id="parentTreeNavigation"><li ng-repeat="node in ' + attrs.menuData + '" ng-class="{active:node.active && node.active==true, \'has-dropdown\': !!node.children && node.children.length}"><a ng-href="{{node.href}}" ng-click="{{node.click}}" target="{{node.target}}" >{{node.text}}</a><sub-navigation-tree></sub-navigation-tree></li></ul>');

                    var linkFunction = $compile(template);
                    linkFunction(scope);
                    element.html(null).append(template);

                }, true);
            }
        };
    }])


    .directive('subNavigationTree', ['$compile', function ($compile) {
        return {
            restrict: 'E', //Element
            scope: true,
            link: function (scope, element, attrs) {
                scope.tree = scope.node;

                if (scope.tree != null && scope.tree.children && scope.tree.children.length) {
                    var template = angular.element('<ul class="dropdown "><li ng-repeat="node in tree.children" node-id={{node.' + attrs.nodeId + '}}  ng-class="{active:node.active && node.active==true, \'has-dropdown\': !!node.children && node.children.length}"><a ng-href="{{node.href}}" ng-click="{{node.click}}" target="{{node.target}}" ng-bind-html-unsafe="node.text"></a><sub-navigation-tree tree="node"></sub-navigation-tree></li></ul>');

                    var linkFunction = $compile(template);
                    linkFunction(scope);
                    element.replaceWith(template);
                }
                else {
                    element.remove();
                }
            }
        };
    }]);


