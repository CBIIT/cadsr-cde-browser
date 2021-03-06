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

   $scope.options = ["ALL Fields", "Long Name", "Short Name", "Preferred Question Text", "Alternate Question Text", "UML Class: UML Attr Alternate Name"];
   $scope.fs.dataElementVariables.searchFieldOptions.options = [];
   $scope.fs.dataElementVariables.searchFieldOptions.options[0] = $scope.options[0];

var previous = $scope.fs.dataElementVariables.searchFieldOptions.options;

$scope.setSelected = function() {
  var selectedlen = $scope.fs.dataElementVariables.searchFieldOptions.options;
  if(selectedlen.length>0) {
    if(selectedlen[0]=="ALL Fields" && selectedlen.length==2) {
      $scope.fs.dataElementVariables.searchFieldOptions.options.splice(0,1);
    } else if(selectedlen.length>2 && selectedlen[0]=="ALL Fields") {
      $scope.fs.dataElementVariables.searchFieldOptions.options = [];
      $scope.fs.dataElementVariables.searchFieldOptions.options[0] = $scope.options[0];
    }
  }
}

$scope.publicSearch = function() {
filterService.resetDataElementSearch();
  $scope.onClickBasicSearch($scope.publicIdSearchQuery,
                            'publicId',
                            '',
                            '',
                            0,
                            $scope.fs.dataElementVariables.selectedQueryType,
                            '',
                            2,
                            '',
                            // 0,
                            $scope.publicIdName,
                            $scope.fs.dataElementVariables.searchAltName,
                            $scope.fs.dataElementVariables.searchAltNameType,
                            $scope.fs.dataElementVariables.searchFieldOptions.options,
                            $scope.fs.dataElementVariables.searchVersions,
                            $scope.fs.dataElementVariables.publicSearchVersions,
                            $scope.fs.dataElementVariables.searchContextUse,
                            '',
                            '',
                            $scope.fs.dataElementVariables.derivedDE);
}

$scope.searchKey = function(e) {
        if (e.keyCode==13) {
            $scope.onClickBasicSearch($scope.publicIdSearchQuery,
                            'publicId',
                            '',
                            '',
                            0,
                            $scope.fs.dataElementVariables.selectedQueryType,
                            '',
                            2,
                            '',
                            // 0,
                            $scope.publicIdName,
                            $scope.fs.dataElementVariables.searchAltName,
                            $scope.fs.dataElementVariables.searchAltNameType,
                            $scope.fs.dataElementVariables.searchFieldOptions.options,
                            $scope.fs.dataElementVariables.searchVersions,
                            $scope.fs.dataElementVariables.publicSearchVersions,
                            $scope.fs.dataElementVariables.searchContextUse,
                            '',
                            '',
                            $scope.fs.dataElementVariables.derivedDE);
        }
};

}]);
