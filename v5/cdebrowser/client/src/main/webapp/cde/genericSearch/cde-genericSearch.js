angular.module("cdeGenericSearch", []);

angular.module("cdeGenericSearch").controller("GenericSearchController", function ($scope, filterService, $rootScope, $http) {

    $scope.fs = filterService;

    $scope.$watch('isNode', function () {
		$scope.basicSearchQuery = "";
    });

    $scope.$watch('selectedQueryType', function () {
    	$scope.fs.dataElementVariables;
    });

    $scope.options = ["ALL", "Long Name", "Short Name", "Preferred Question Text", "Alternate Question Text", "UML Class: UML Attr Alternate Name"];
 	$scope.fs.dataElementVariables.searchFieldOptions.options = [];
 	$scope.fs.dataElementVariables.searchFieldOptions.options[0] = $scope.options[0];

 	$rootScope.$on('genericsearch',function(eve,fs) {
 		// console.log(fs);
		$scope.onClickBasicSearch(fs.dataElementVariables.basicSearchQuery,
			'name', fs.dataElementVariables.searchDEC, fs.dataElementVariables.searchPV,
			fs.dataElementVariables.searchPVQueryType, fs.dataElementVariables.selectedQueryType, fs.dataElementVariables.searchVD,
			fs.dataElementVariables.searchVDTQueryType, fs.dataElementVariables.conceptInput, fs.dataElementVariables.conceptQueryType, '',
			fs.dataElementVariables.searchAltName, fs.dataElementVariables.searchAltNameType, fs.dataElementVariables.searchFieldOptions.options,
			fs.dataElementVariables.searchVersions, fs.dataElementVariables.searchContextUse, fs.dataElementVariables.searchObjectClass);
 	});

 	$scope.setSelected=function() {
    var selectedlen=$scope.fs.dataElementVariables.searchFieldOptions.options;
    if(selectedlen.length>0) {
      if(selectedlen[0]=="ALL" && selectedlen.length==2) {
        $scope.fs.dataElementVariables.searchFieldOptions.options.splice(0,1);
      }else if(selectedlen.length>2 && selectedlen[0]=="ALL"){
        $scope.fs.dataElementVariables.searchFieldOptions.options = [];
        $scope.fs.dataElementVariables.searchFieldOptions.options[0] = $scope.options[0];
      }
    }
  }


	// Search Context use options
	// $scope.searchContextUseValues = [
	// 	"Owned By",
	// 	"Used By",
	// 	"Owned By/Used By"
	// ];

	// $scope.fs.dataElementVariables.searchContextUse =  $scope.searchContextUseValues[2];


	/*
       Can't do it this way, sometimes $scope.alternateNameTypes is not yet populated in time.
       $scope.fs.dataElementVariables.searchAltNameType = $scope.alternateNameTypes[0];
   */


  $scope.fs.dataElementVariables.searchAltNameType = {"type": "ALL"}; // FIX ME

});
