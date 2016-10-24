/*
 * Copyright 2016 Leidos Biomedical Research, Inc.
 */

angular.module("cdeDataElement", []);
//TODO this code tries to create a direct link to a DE details view by DE Public ID and Version. It is a test implementation, it does not really work
angular.module("cdeDataElement").controller("DataElementCtrl", ["$scope", "$location", "$http", function ($scope, $location, $http) {
    $scope.getCdeDetailByLink = function (serverUrl) {
        $scope.bigSearchResultsMessageClass = true;
        $http.get(serverUrl).success(function (response) {
            $scope.tabsDisabled = false;
            // Change to "Data Element" tab
            $scope.changeView(1, $scope.tabs[1]);
            $scope.cdeDetails = response;
            $scope.searchResultsMessage = "";
            $scope.searchResultsCount = "Results: " + $scope.searchResults.length;
            $scope.bigSearchResultsMessageClass = false;
        });
    };
    $scope.getCdeData = function () {
    	$scope.publicId = $location;
    	var searchObject = $location.search();
    	console.log("cde-dataElement.js debug log searchObject.publicId: " + searchObject.publicId + ", searchObject.version: " + searchObject.version );
    	if (($location.search().hasOwnProperty('publicId')) && ($location.search().hasOwnProperty('version'))) {
    		var dataElementServerLink = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port 
        		+ "/cdebrowserServer/rest/CDELink?publicId=" + searchObject.publicId+"&version=" + searchObject.version;
    		console.log("cde-dataElement.js debug log dataElementServerLink: " + dataElementServerLink);
    		$scope.dataElementLink = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port 
			+ "/cdebrowserClient/cdeBrowser.html#/dataElement?publicId=" + searchObject.publicId +"&version=" 
				+ searchObject.version;
    		console.log("cde-dataElement.js debug log $scope.dataElementLink: " + $scope.dataElementLink);
            $scope.getCdeDetailByLink(dataElementServerLink);
    	}
    	else {
    		$scope.dataElementLink = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port 
    			+ "/cdebrowserClient/cdeBrowser.html#/dataElement?publicId=" + $scope.cdeDetails.dataElement.dataElementDetails.publicId +"&version=" 
    				+ $scope.cdeDetails.dataElement.dataElementDetails.formattedVersion;
    		console.log("cde-dataElement.js dataElementLink: " + $scope.dataElementLink);
    	}
    };

    $scope.getCdeData();

}]);
