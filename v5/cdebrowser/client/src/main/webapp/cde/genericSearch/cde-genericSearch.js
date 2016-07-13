angular.module("cdeGenericSearch", []);

angular.module("cdeGenericSearch").controller("GenericSearchController", function ($scope, filterService) {
    
    $scope.fs = filterService;
    
    $scope.$watch('isNode', function () {
		$scope.basicSearchQuery = "";
    });
    
    
    $scope.$watch('selectedQueryType', function () {
    	$scope.fs.dataElementVariables;
    });    
    
    $scope.options = ["ALL", "Long Name", "Short Name", "Preferred Question Text", "Alternate Question Text", "UML Class: UML Attr Alternate Name"];
    $scope.searchFieldOptions = $scope.options[0];

});
