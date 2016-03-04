/**
 * Created by lernermh on 4/17/15.
 */
angular.module("cdeGenericSearch", []);

angular.module("cdeGenericSearch").controller("GenericSearchController", ["$scope", function ($scope) {
    $scope.$watch('isNode', function () {
		$scope.basicSearchQuery = "";
    });
}]);
