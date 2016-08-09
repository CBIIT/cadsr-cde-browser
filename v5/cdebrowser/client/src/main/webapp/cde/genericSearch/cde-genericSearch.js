angular.module("cdeGenericSearch", []);

angular.module("cdeGenericSearch").controller("GenericSearchController", function ($scope, filterService, $rootScope, $http) {

    $scope.fs = filterService;

    $scope.$watch('isNode', function () {
		$scope.basicSearchQuery = "";
    });

    $scope.$watch('selectedQueryType', function () {
    	$scope.fs.dataElementVariables;
    });

    $scope.options = ["ALL Fields", "Long Name", "Short Name", "Preferred Question Text", "Alternate Question Text", "UML Class: UML Attr Alternate Name"];
 	$scope.fs.dataElementVariables.searchFieldOptions.options = [];
 	$scope.fs.dataElementVariables.searchFieldOptions.options[0] = $scope.options[0];

 	$scope.$on('genericsearch',function(eve,fs) {
		$scope.onClickBasicSearch(fs.dataElementVariables.basicSearchQuery,
			'name', fs.dataElementVariables.searchDEC, fs.dataElementVariables.searchPV,
			fs.dataElementVariables.searchPVQueryType, fs.dataElementVariables.selectedQueryType, fs.dataElementVariables.searchVD,
			fs.dataElementVariables.searchVDTQueryType, fs.dataElementVariables.conceptInput, '',
			fs.dataElementVariables.searchAltName, fs.dataElementVariables.searchAltNameType, fs.dataElementVariables.searchFieldOptions.options,
			fs.dataElementVariables.searchVersions, fs.dataElementVariables.searchContextUse, fs.dataElementVariables.searchObjectClass);
 	});

 	$scope.setSelectedField = function() {
    var selectedlen = $scope.fs.dataElementVariables.searchFieldOptions.options;
        if(selectedlen.length>0) {
            if(selectedlen[0]=="ALL Fields" && selectedlen.length==2) {
                $scope.fs.dataElementVariables.searchFieldOptions.options.splice(0,1);
            }   
            else if(selectedlen.length>2 && selectedlen[0]=="ALL Fields") {
                $scope.fs.dataElementVariables.searchFieldOptions.options = [];
                $scope.fs.dataElementVariables.searchFieldOptions.options[0] = $scope.options[0];
            }
        }
    }

    $scope.setSelectedAlt = function() {
    var selectedlen = $scope.fs.dataElementVariables.searchAltNameType;
        if(selectedlen.length>0) {
            if(selectedlen[0]=="ALL Alternate Name Types" && selectedlen.length==2) {
                $scope.fs.dataElementVariables.searchAltNameType.splice(0,1);
            }   
            else if(selectedlen.length>2 && selectedlen[0]=="ALL Alternate Name Types") {
                $scope.fs.dataElementVariables.searchAltNameType = [];
                $scope.fs.dataElementVariables.searchAltNameType[0] = $scope.options[0];
            }
        }
    }

});
