/**
 * The Angular module "cde", with its dependencies.
 */
angular.module("cde", [
    "ngRoute",
    "ngSanitize",
    "cdeAdminInfo",
    "cdeClassifications",
    "cdeDataElement",
    "cdeDataElementConcept",
    "cdeDataElementDerivation",
    "cdeUsage",
    "cdeValueDomain",
    "cdeGenericSearch"

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
            templateUrl: "cde/adminInfo/adminInfo-view.html"
        })
        .when("/classifications", {
            controller: "ClassificationsCtrl",
            templateUrl: "cde/classifications/classifications-view.html"
        })
        .when("/dataElement", {
            controller: "DataElementCtrl",
            templateUrl: "cde/dataElement/dataElement-view.html"
        })
        .when("/dataElementConcept", {
            controller: "DataElementConceptCtrl",
            templateUrl: "cde/dataElementConcept/dataElementConcept-view.html"
        })

        .when("/dataElementDerivation", {
            controller: "DataElementDerivationCtrl",
            templateUrl: "cde/dataElementDerivation/dataElementDerivation-view.html"
        })
        .when("/usage", {
            controller: "UsageCtrl",
            templateUrl: "cde/usage/usage-view.html"
        })
        .when("/valueDomain", {
            controller: "ValueDomainCtrl",
            templateUrl: "cde/valueDomain/valueDomain-view.html"
        });


}]);

/**
 * This Controller for the "cde" module used to contain test data, am leaving it (for now) because it may need to serve that purpose again.
 */
angular.module('cde').controller('cdeCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {

}]);
