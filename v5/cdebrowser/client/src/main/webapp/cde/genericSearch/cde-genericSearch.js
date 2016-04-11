angular.module("cdeGenericSearch", []);

angular.module("cdeGenericSearch").controller("GenericSearchController", function ($scope, filterService) {
    $scope.fs = filterService;
    $scope.$watch('isNode', function () {
		$scope.basicSearchQuery = "";
    });
    $scope.$watch('selectedQueryType', function () {
    	$scope.fs.dataElementVariables;
    });    
});
