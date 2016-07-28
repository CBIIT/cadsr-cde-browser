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

 	$scope.setSelectedField = function() {
    var selectedlen = $scope.fs.dataElementVariables.searchFieldOptions.options;
        if(selectedlen.length>0) {
            if(selectedlen[0]=="ALL" && selectedlen.length==2) {
                $scope.fs.dataElementVariables.searchFieldOptions.options.splice(0,1);
            }   
            else if(selectedlen.length>2 && selectedlen[0]=="ALL") {
                $scope.fs.dataElementVariables.searchFieldOptions.options = [];
                $scope.fs.dataElementVariables.searchFieldOptions.options[0] = $scope.options[0];
            }
        }
    }

    $scope.setSelectedAlt = function() {
    var selectedlen = $scope.fs.dataElementVariables.searchAltNameType;
        if(selectedlen.length>0) {
            if(selectedlen[0]=="ALL" && selectedlen.length==2) {
                $scope.fs.dataElementVariables.searchAltNameType.splice(0,1);
            }   
            else if(selectedlen.length>2 && selectedlen[0]=="ALL") {
                $scope.fs.dataElementVariables.searchAltNameType = [];
                $scope.fs.dataElementVariables.searchAltNameType[0] = $scope.options[0];
            }
        }
    }

});
