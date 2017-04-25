angular.module("cdeValueMeaning", []);

angular.module("cdeValueMeaning").controller("ValueMeaningCtrl", ["$scope","$location", function ($scope, $location) {
	
	$scope.currentHash = $location.$$hash; // gets hash if clicked from value domain tab //

}]);