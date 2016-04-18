/**
 * Created by lernermh on 4/17/15.
 */
angular.module("cdeCart", []);

angular.module("cdeCart").controller("CartCtrl", ["$scope","$location","$localStorage","$sessionStorage","$route","searchFactory","cartService", "authenticationService", function ($scope, $location,$localStorage, $sessionStorage, $route, searchFactory, cartService, authenticationService) {
	searchFactory.showSearch = false; // set search area to be invisible //	
	$scope.$parent.title = "CDE Cart" // set title of page to be show on the tab //

	var authService = authenticationService; // define authentication service //
	var redirect = angular.copy(authService.cameFrom); // set if coming from cart. Determines what was clicked on //
	authService.cameFrom = ''; // set came from back to empty string //

	// define cart service //
	var cartService = cartService;
	$scope.$storage = $sessionStorage;
	$scope.$storage.cartService = cartService;

	if (redirect=='retrieve') { cartService.retrieveCart(); } // checks if user has been redirected after logging in //
	else if(redirect == 'save') { cartService.saveCart(); }	 // checks if user has been redirected after logging in //

	// add item to cde cart //
	$scope.addCDE = function() {
		cartService.addCDE('Orange');
	};

	// retrieve items and put them in cde cart //
	$scope.retrieveCart = function() {
		console.log("????")
		// make call to server to get cart, add current items to retrieved cart //
		cartService.retrieveCart(); 
	};	

	// delete item from cde cart //
	$scope.deleteCDEs = function() {
		cartService.deleteCDEs();
		console.log("delete if logged in and check unsaved status to delete locally or from server. If authorized error, ignore the unsaved status and just delete locally.")
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
