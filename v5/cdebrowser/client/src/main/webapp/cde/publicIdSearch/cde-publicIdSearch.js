angular.module("cdePublicIdSearch", []);

angular.module("cdePublicIdSearch").controller("PublicIdSearchController", ["$scope", function ($scope) {
    $scope.$watch('isNode', function () {
		$scope.publicIdSearchQuery = "";
    });

    // clear search results and fields //
    $scope.clear = function() {
		$scope.publicIdSearchQuery='';
		$scope.publicIdName='';
		$scope.resetFilters();
    };
}]);
