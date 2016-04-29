/**
 * Created by lernermh on 4/17/15.
 */
angular.module("cdeCart", []);

angular.module("cdeCart").controller("CartCtrl", ["$scope","$location","$localStorage","$sessionStorage","$route","searchFactory","cartService", "authenticationService","downloadFactory", function ($scope, $location,$localStorage, $sessionStorage, $route, searchFactory, cartService, authenticationService, downloadFactory) {
	$scope.$parent.title = "CDE Cart" // set title of page to be show on the tab //
	$scope.downloadFactory = new downloadFactory(); // define download factory //
	var authService = authenticationService; // define authentication service //
	var redirect = angular.copy(authService.cameFrom); // set if coming from cart. Determines what was clicked on //
	authService.cameFrom = ''; // set came from back to empty string //
	// define cart service //
	var cartService = cartService;
	$scope.$storage = $sessionStorage;
	$scope.$storage.cartService = cartService;

	if (redirect=='retrieve') { cartService.retrieveCart(); } // checks if user has been redirected after logging in //
	else if(redirect == 'save') { cartService.saveCart(); }	 // checks if user has been redirected after logging in //

	// retrieve cart everytime controller is loaded. //
	// Doesnt have to force login. if authentication fails simply ignore authentication failure // 
	// Only force login on retrieve cart button //
	cartService.retrieveCart();

	// retrieve items and put them in cde cart //
	$scope.retrieveCart = function() {
		cartService.retrieveCart('click'); 
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
		// this needs to be properly moved into a service along with the entire search //
			$scope.checkboxes.items={};
		// end this needs to be properly moved into a service along with the entire search //
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

	// downloads excel file with checked cart items //
	$scope.downloadToExcel = function(param) {
		var items = $scope.downloadFactory.createDownloadableArray(cartService.checkedCartItems.items); // creates simple array of ids //
		$scope.downloadFactory.downloadToExcel(param,items);
	};

	// downloads xml file with checked cart items //
	$scope.downloadToXML = function() {
		var items = $scope.downloadFactory.createDownloadableArray(cartService.checkedCartItems.items);  // creates simple array of ids //
		$scope.downloadFactory.downloadToXML(items);
	};		

}]);
