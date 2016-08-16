angular.module("cdeCompare", []);

angular.module("cdeCompare").controller("cdeCompareController",  ["$scope", "$http", "$window", "$location", "compareService", "$anchorScroll", "downloadFactory", "$filter", function ($scope, $http, $window, $location, compareService, $anchorScroll, downloadFactory, $filter) {
    window.scope = $scope;
    $scope.location = $location.url();
    $scope.$parent.title = "CDE Compare"; 
    $scope.$anchorScroll = $anchorScroll;
    $scope.dataBaseData;
    $scope.compareService = compareService;
    $scope.downloadFactory = new downloadFactory();
    $scope.checkedItems = [];

    //CDE details
    $scope.onClickCdeDetails = function (deIdseq) {
        $scope.getCdeDetailRestCall(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/CDEData?deIdseq=" + deIdseq);

    };

    //Multiple CDE details
    $scope.multipleCdeDetails = function () {

        $scope.compareDataDoneLoading = false;
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
         var len = compareService.checkedItemsForCompare.length;
         $scope.cdeDetails = response;
          for(var i=0;i<len;i++){
            $scope.cdeDetails[i].id=compareService.checkedItemsForCompare[i];
          }
            $scope.compareDataDoneLoading = true;
            // FIXME  Check here for errors
        });
    };

    $scope.dataLoad = function (dataSource) {
        $http.get(dataSource).success(function (response) {
            $scope.cdeDetails = response;
        });

    };

  //							           //
  //	FOR TESTING PURPOSE		 //
  //  							         //

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
	
  //							           //
  //	END TESTING PURPOSE		 //
  //  							         //

   	$scope.deleteAllCDEs = function() {
   		$scope.cdeDetails.splice(0);
      compareService.deleteCheckedItemsforCompare('',true);
   	}
   	
   	$scope.deleteCDE = function(id,ndx) {
   		$scope.cdeDetails.splice(ndx,1);
      compareService.deleteCheckedItemsforCompare(id,false);
   	}

	$scope.goTo = function(id) {
		var change = $location.hash();
		$location.hash(id);
		$anchorScroll();
		$location.hash(change);
	}

  // go back to search screen, show search area //
	$scope.goBack = function() {
		$scope.checkboxes.items = {};
		$location.path("/search");
	};

	$scope.excelDownload = function() {
		var items = [];
		items = compareService.checkedItemsForCompare;
    console.log(compareService.checkedItemsForCompare);
    console.log(items);
		var param = false;
		$scope.downloadFactory.excelDownload(param,items);
	};

    $scope.compareDataDoneLoading = "true";
    $scope.compareDataLoading2 = "true";

    scope.multipleCdeDetails();

}]);

