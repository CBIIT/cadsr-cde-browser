angular.module("cdeCompare", []);

angular.module("cdeCompare").controller("cdeCompareController",  ["$scope", "$http", "$window", "$location", "compareService", "$anchorScroll", "downloadFactory", function ($scope, $http, $window, $location, compareService, $anchorScroll, downloadFactory) {
    window.scope = $scope;
    $scope.location = $location.url();
    $scope.$parent.title = "CDE Compare"; 
    $scope.$anchorScroll = $anchorScroll;
    $scope.dataBaseData;
    $scope.compareService = compareService;
    $scope.downloadFactory = downloadFactory;
    $scope.checkedItems = [];
   // $scope.compareDataDoneLoading = false;

    //CDE details
    $scope.onClickCdeDetails = function (deIdseq) {
        $scope.getCdeDetailRestCall(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/CDEData?deIdseq=" + deIdseq);

    };


    //Multiple CDE details
    $scope.multipleCdeDetails = function () {

		// console.log(compareService.idList);
        $scope.compareDataDoneLoading = false;
        // var idList = window.location.toString().split('?cde=')[1];
        // console.log("A multipleCdeDetailsTest: " + idList);
        // $scope.compareDataDoneLoading = false;
        $scope.getCdeDetailRestCall(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/multiCDEData?deIdseq=" + compareService.idList);
    };

    $scope.multipleCdeDetailsTest = function () {
        var idList = window.location.toString().split('?cde=')[1];

        // FIXME need to error check idList
        $scope.multipleCdeDetails(idList);
    };

    // function that gets the data returned for CDE details //
    $scope.getCdeDetailRestCall = function (serverUrl) {
        $http.get(serverUrl).success(function (response) {
            $scope.cdeDetails = response;
            $scope.compareDataDoneLoading = true;
			$scope.dataBaseData = angular.copy($scope.cdeDetails); // USAGE?!
            // FIXME  Check here for errors
        });
    };

    $scope.dataLoad = function (dataSource) {
        $http.get(dataSource).success(function (response) {
            $scope.cdeDetails = response;
        });

    };

  //   $scope.dataLoad1 = function () {
  //       $scope.dataLoad("data1.json");
  //   };

  //   $scope.dataLoad2 = function () {
  //       $scope.dataLoad("data2.json");
  //   };

  //  $scope.dataLoad3 = function () {
  //       $scope.dataLoad("data3.json");
  //   };

  //  $scope.dataLoad4 = function () {
  //       $scope.dataLoad("data4.json");
  //   };

  //  $scope.dataLoad5 = function () {
  //       $scope.dataLoad("data5.json");
  //   };

  // $scope.dataLoad6 = function () {
  //       $scope.dataLoad("data6.json");
  //   };

   // $scope.dataLoad6();



   	// TOGGLE COLLAPSABLE - JUST USE NG-HIDE/NG-SHOW?! or BOOTSTRAP HACK?!

   	$scope.deleteAllCDEs = function() {
   		$scope.cdeDetails.splice(0);
   	}
   	
   	$scope.deleteCDE = function(ndx) {
   		$scope.cdeDetails.splice(ndx,1);
   	}

   	$scope.selectAll=function(){
   		console.log($scope.checkedItems);
   	}



	$scope.goTo = function(id) {
		var change = $location.hash();
		$location.hash(id);
		$anchorScroll();
		$location.hash(change);
	}

   	// go back to search screen, show search area //
	$scope.goBack = function() {
		$scope.checkboxes.items={};
		$location.path("/search");
	};

	$scope.excelDownload = function(param) {
		var items = $scope.downloadFactory.createDownloadableArray(compareService.checkedCompareItems.items); // creates simple array of ids //
		$scope.downloadFactory.excelDownload(param,items);
	};

    $scope.compareDataDoneLoading = "true";
    $scope.compareDataLoading2 = "true";

    scope.multipleCdeDetails();

/*
  $scope.multipleCdeDetails('058122C4-17DC-D98B-E050-BB89AD437492,CFCBA97B-D243-5D7B-E034-0003BA12F5E7');
*/

}]);

