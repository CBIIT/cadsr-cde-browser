/**
 * Created by lernermh on 4/17/15.
 */
angular.module("cdeCart", []);

angular.module("cdeCart").controller("CartCtrl", ["$scope","$location","$route","searchFactory","cartService", function ($scope, $location,$route, searchFactory, cartService) {
	searchFactory.showSearch = false; // set search area to be invisible //	
	$scope.$parent.title = "CDE Cart" // set title of page to be show on the tab //

	// define cart service //
	var cartService = cartService;
	$scope.cartService = cartService;

	// add item to cde cart //
	$scope.addCDE = function() {
		cartService.addCDE('Orange');
	};

	// retrieve items and put them in cde cart //
	$scope.retrieveCart = function() {
		// make call to server to get cart, add current items to retrieved cart //
	};	

	// delete item from cde cart //
	$scope.deleteCDEs = function() {
		cartService.deleteCDEs();
	};

	// save cart to server //
	$scope.saveCart = function() {
		cartService.saveCart();
	};

	// go back to search screen, show search area //
	$scope.goBack = function() {
		searchFactory.showSearch = true;
		$location.path("/search");
	};

	// select all or de-select all items in cart //
	$scope.selectAll = function() {
		cartService.selectAll();
	};

	// only really removes item from checked items object //
	$scope.checkCartItem = function(id) {
		cartService.checkCartItem(id);
	};

}]);
