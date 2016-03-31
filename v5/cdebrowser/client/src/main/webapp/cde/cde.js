/**
 * The Angular module "cde", with its dependencies.
 */
angular.module("cde", [
    "ngRoute",
    "ngResource",
    "ngSanitize",
    "cdeAdminInfo",
    "cdeClassifications",
    "cdeDataElement",
    "cdeDataElementConcept",
    "cdeDataElementDerivation",
    "cdeUsage",
    "cdeValueDomain",
    "cdeGenericSearch",
    "cdePublicIdSearch",
    "cdeCart"

])
/**
 * This is the router for CDE details/Search results.
 * Each tab is loaded as a "View" (partial html page) the views are inserted in cdeBrowser.html:  <div class="cdeSearchTabsView" data-ng-view></div>
 *
 * The Search tab is no longer loaded as a View, when a View is loaded, it is reloaded, which caused the pager to always return back to page one.
 */
    .config(["$routeProvider", function ($routeProvider) {

    $routeProvider

 /*
        .when("/search", {
            controller: "SearchCtrl",
            templateUrl: "cde/search/search-view.html"
        })

*/
        .when("/adminInfo", {
            controller: "AdminInfoCtrl",
            templateUrl: "cde/adminInfo/adminInfo-view.html",
            title:"Admin Info"
        })     
        .when("/classifications", {
            controller: "ClassificationsCtrl",
            templateUrl: "cde/classifications/classifications-view.html",
            title: "Classifications"
        })
        .when("/dataElement", {
            controller: "DataElementCtrl",
            templateUrl: "cde/dataElement/dataElement-view.html",
            title: "Data Element"
        })
        .when("/dataElementConcept", {
            controller: "DataElementConceptCtrl",
            templateUrl: "cde/dataElementConcept/dataElementConcept-view.html",
            title: "Data Element Concept"
        })

        .when("/dataElementDerivation", {
            controller: "DataElementDerivationCtrl",
            templateUrl: "cde/dataElementDerivation/dataElementDerivation-view.html",
            title: "Data Element Derivation"
        })
        .when("/usage", {
            controller: "UsageCtrl",
            templateUrl: "cde/usage/usage-view.html",
            title: "Usage"            
        })
        .when("/valueDomain", {
            controller: "ValueDomainCtrl",
            templateUrl: "cde/valueDomain/valueDomain-view.html",
            title: "Value Domain"
        })
        .when("/cdeCart", {
            controller: "CartCtrl",
            templateUrl: "cde/cdeCart/cart-view.html",
            title: "CDE Cart"
        });        


}]);

/**
 * This Controller for the "cde" module used to contain test data, am leaving it (for now) because it may need to serve that purpose again.
 */
angular.module('cde').controller('cdeCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {

}]);
