/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

angular.module("cdeDataElement", []);
//this code creates a direct link to a DE details view by DE Public ID and Version.
angular.module("cdeDataElement").controller("DataElementCtrl", ["$scope", "$location", "$http", function ($scope, $location, $http) {
    $scope.getCdeData = function () {
    	$scope.dataElementLink = window.location.protocol + "//" + window.location.host + "/cdebrowserClient/cdeBrowser.html#/search?publicId=" + $scope.cdeDetails.dataElement.dataElementDetails.publicId +"&version=" 
    				+ $scope.cdeDetails.dataElement.dataElementDetails.formattedVersion;
    };
    if ($scope.cdeDetails.dataElement != null) {
    	$scope.getCdeData();
	}

	$scope.getContextsUsingCDE = function(usedByAlternateNames) {
		var contextString = "";
		if (usedByAlternateNames) {
			for (var x=0; x<usedByAlternateNames.length; x++) {
				if (usedByAlternateNames[x].name==usedByAlternateNames[x].context) {
					if (x>0) {
						contextString+=', ';
					};
					contextString+=usedByAlternateNames[x].name;
				};
			};			
		};
		if (!contextString.length) {
			contextString = "None";
		};
		return contextString;
	};
}]);
