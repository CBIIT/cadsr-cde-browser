/**
 * Created by lernermh on 4/17/15.
 */
angular.module("cdeLogin", []);

angular.module("cdeLogin").controller("LoginCtrl", ["$scope","authenticationService","searchFactory","cartService", function ($scope, authenticationService, searchFactory, cartService) {
	searchFactory.showSearch = false; // set search area to be invisible //	
	$scope.$parent.title = "Login" // set title of page to be show on the tab //

	var authService = authenticationService; // define authentication service //
	var cartService = cartService; // define cart service //
	var redirect = angular.copy(authService.cameFrom); // set if coming from cart. Determines what was clicked on //
	authService.cameFrom = ''; // set came from back to empty string //

	// logs the user in //
	$scope.login = function() {
		cartService.resetUnsavedStatus(); // reset all items in cart to have unsaved status of true //
		authService.login($scope.username,$scope.credential, redirect);
	};

	// resets username and credential to blank //
	$scope.resetFields = function() {
		$scope.username = '';
		$scope.credential = '';
	};

}]);
