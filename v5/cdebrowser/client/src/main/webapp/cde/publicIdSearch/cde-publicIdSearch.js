angular.module("cdePublicIdSearch", []);

angular.module("cdePublicIdSearch").controller("PublicIdSearchController", ["$scope", function ($scope) {
    $scope.$watch('isNode', function () {
		$scope.publicIdSearchQuery = "";
    });
}]);
