/**
 * Created by lernermh on 4/17/15.
 */
angular.module("cdeLogin", []);

angular.module("cdeLogin").controller("LoginCtrl", ["$scope","$location","$route","searchFactory","authenticationService", "$http", function ($scope, $location, $route, searchFactory, authenticationService, $http) {
	searchFactory.showSearch = false; // set search area to be invisible //	
	$scope.$parent.title = "Login" // set title of page to be show on the tab //

	var authService = authenticationService; // define authentication service //
	var redirect = angular.copy(authService.cameFrom); // set if coming from cart. Determines what was clicked on //
	authService.cameFrom = ''; // set came from back to empty string //

	$scope.login = function() {
		authService.login($scope.username,$scope.credential, redirect);
	};

}]);
