/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

angular.module("cdeDataElement", []);
//this code creates a direct link to a DE details view by DE Public ID and Version.
angular.module("cdeDataElement").controller("DataElementCtrl", ["$scope", "$location", "$http", function ($scope, $location, $http) {
    $scope.getCdeData = function () {
    	if ($scope.cdeDetails != null && $scope.cdeDetails.dataElementDetails != null) {
    		$scope.dataElementLink = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port 
    			+ "/cdebrowserClient/cdeBrowser.html#/search?publicId=" + $scope.cdeDetails.dataElement.dataElementDetails.publicId +"&version=" 
    				+ $scope.cdeDetails.dataElement.dataElementDetails.formattedVersion;
    	}
    };

    $scope.getCdeData();

}]);
