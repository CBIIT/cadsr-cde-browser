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

   $scope.options = ["ALL", "Long Name", "Short Name", "Preferred Question Text", "Alternate Question Text", "UML Class: UML Attr Alternate Name"];
   $scope.fs.dataElementVariables.searchFieldOptions.options = [];
   $scope.fs.dataElementVariables.searchFieldOptions.options[0] = $scope.options[0];

   $scope.mysearch=function(){
  filterService.resetDataElementSearch();
    $scope.onClickBasicSearch($scope.publicIdSearchQuery, 'publicId', '', ' ', 0, $scope.fs.dataElementVariables.selectedQueryType, 
      $scope.publicIdName, $scope.fs.dataElementVariables.searchAltName, $scope.fs.dataElementVariables.searchAltNameType, '', '', '', '', 
      $scope.fs.dataElementVariables.searchFieldOptions.options);
   }

}]);
