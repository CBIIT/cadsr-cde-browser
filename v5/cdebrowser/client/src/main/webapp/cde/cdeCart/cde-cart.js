/**
 * Created by lernermh on 4/17/15.
 */
angular.module("cdeCart", []);

angular.module("cdeCart").controller("CartCtrl", ["$scope","$location","$route","searchFactory", function ($scope, $location,$route, searchFactory) {

	// set search area to be invisible //	
	searchFactory.showSearch = false;

	// log area //

	// set title of page to be show on the tab //
	$scope.$parent.title = "CDE Cart"

	// go back to search screen, show search area //
	$scope.goBack = function() {
		searchFactory.showSearch = true;
		$location.path("/search");
	};

}]);
