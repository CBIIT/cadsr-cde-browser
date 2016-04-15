/**
 * Created by lernermh on 4/17/15.
 */
angular.module("cdeLogin", []);

angular.module("cdeLogin").controller("LoginCtrl", ["$scope","$location","$route","searchFactory", "$http", function ($scope, $location, $route, searchFactory, $http) {
	searchFactory.showSearch = false; // set search area to be invisible //	
	$scope.$parent.title = "Login" // set title of page to be show on the tab //

	$scope.login = function() {


		$http({method: 'POST', url: '/cdebrowserServer/rest/login',headers: { 'Authorization':'Basic ' + btoa($scope.username+':'+$scope.credential)}}).
			success(function(response) {
				console.log("done")
			}).
			error(function(response) {
				console.log("error")
			});
	};

}]);
