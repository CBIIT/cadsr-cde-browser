/**
 * Created by lernermh on 4/17/15.
 */
angular.module("cdeCart", []);

angular.module("cdeCart").controller("CartCtrl", ["$scope","$location","$route","searchFactory","cartService", function ($scope, $location,$route, searchFactory, cartService) {
	// define cart service //
	var cartService = cartService;
	$scope.cartData = cartService.cartData;

	// set title of page to be show on the tab //
	$scope.$parent.title = "CDE Cart"

	// add item to cde cart //
	$scope.addCDE = function() {
		cartService.addCDE('Orange');
		// $scope.retrieveCart();
	};
	// retrieve items and put them in cde cart //
	$scope.retrieveCart = function() {
		$scope.cartData = cartService.retrieveCart();
	};	

	// delete item from cde cart //
	$scope.deleteCDE = function() {
		cartService.deleteCDE();
	};

	// set search area to be invisible //	
	searchFactory.showSearch = false;

	



	// go back to search screen, show search area //
	$scope.goBack = function() {
		searchFactory.showSearch = true;
		$location.path("/search");
	};

}]);
