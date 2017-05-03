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

    // returns list of suggestions based on typed text in name field //
    $scope.getLocation = function(val) {
        if (val.indexOf("*")==-1) {
            return $http.get('/cdebrowserServer/rest/typeahead/longnamefull', {
              params: {
                name: val,
                filteredinput: $scope.fs.dataElementVariables.searchFieldOptions.options.join(","),
                queryType: $scope.selectedQueryType
              }
            }).then(function(response){
                $scope.vals = response.data;
              return response.data;
            });            
        }
        else {
            return [];
        };

    };

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
        else {
                $scope.fs.dataElementVariables.searchFieldOptions.options = [];
                $scope.fs.dataElementVariables.searchFieldOptions.options[0] = $scope.options[0];
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

    $scope.searchKey = function(e) {
        if (e.keyCode==13) {
            $scope.onClickBasicSearch($scope.fs.dataElementVariables.basicSearchQuery,
            'name', $scope.fs.dataElementVariables.searchDEC, $scope.fs.dataElementVariables.searchPV,
            $scope.fs.dataElementVariables.searchPVQueryType, $scope.fs.dataElementVariables.selectedQueryType, $scope.fs.dataElementVariables.searchVD,
            $scope.fs.dataElementVariables.searchVDTQueryType, $scope.fs.dataElementVariables.conceptInput, '',
            $scope.fs.dataElementVariables.searchAltName, $scope.fs.dataElementVariables.searchAltNameType, $scope.fs.dataElementVariables.searchFieldOptions.options,
            $scope.fs.dataElementVariables.searchVersions, $scope.fs.dataElementVariables.publicSearchVersions, $scope.fs.dataElementVariables.searchContextUse, $scope.fs.dataElementVariables.searchObjectClass, $scope.fs.dataElementVariables.searchProperty, $scope.fs.dataElementVariables.derivedDE);
        }
    };



});
