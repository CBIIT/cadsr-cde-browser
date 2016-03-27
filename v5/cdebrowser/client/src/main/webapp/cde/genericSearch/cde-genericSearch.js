angular.module("cdeGenericSearch", []);

angular.module("cdeGenericSearch").controller("GenericSearchController", ["$scope", function ($scope) {
    $scope.$watch('isNode', function () {
		$scope.basicSearchQuery = "";
    });
}]);
