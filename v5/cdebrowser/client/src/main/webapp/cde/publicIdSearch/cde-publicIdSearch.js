angular.module("cdePublicIdSearch", []);

angular.module("cdePublicIdSearch").controller("PublicIdSearchController", ["$scope", "filterService", function ($scope, filterService) {
    $scope.$watch('isNode', function () {
		$scope.publicIdSearchQuery = "";
    });

    $scope.fs = filterService;

    // clear search results and fields //
    $scope.clear = function() {
		$scope.publicIdSearchQuery='';
		$scope.publicIdName='';
		$scope.resetFilters();
    };
}]);
