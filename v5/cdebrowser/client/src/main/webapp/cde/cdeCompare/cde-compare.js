// angular.module("cdeCompare", []);

// angular.module("cdeCompare").controller("CompareCtrl", ["$scope", ]) {

// }

/**
 * Created by lernermh on 6/1/16.
 */
angular.module("cdeCompare", []);

angular.module("cdeCompare").controller("cdeCompareController",  ["$scope", "$http", "$window", "$location", "compareService", function ( $scope, $http, $window, $location, compareService) {
    window.scope = $scope;
    $scope.location = $location.url();
   // $scope.compareDataDoneLoading = false;

    //CDE details
    $scope.onClickCdeDetails = function (deIdseq) {
        $scope.getCdeDetailRestCall(window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/CDEData?deIdseq=" + deIdseq);

    };


    //Multiple CDE details
    $scope.multipleCdeDetails = function () {

		console.log(compareService.idList);
        
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

            // FIXME  Check here for errors
        });
    };

    $scope.dataLoad = function (dataSource) {
        $http.get(dataSource).success(function (response) {
            $scope.cdeDetails = response;
        });

    };

    $scope.dataLoad1 = function () {
        $scope.dataLoad("data1.json");
    };

    $scope.dataLoad2 = function () {
        $scope.dataLoad("data2.json");
    };

   $scope.dataLoad3 = function () {
        $scope.dataLoad("data3.json");
    };

   $scope.dataLoad4 = function () {
        $scope.dataLoad("data4.json");
    };

   $scope.dataLoad5 = function () {
        $scope.dataLoad("data5.json");
    };

  $scope.dataLoad6 = function () {
        $scope.dataLoad("data6.json");
    };

   // $scope.dataLoad6();

    $scope.compareDataDoneLoading = "true";
    $scope.compareDataLoading2 = "true";


    scope.multipleCdeDetails();

/*
  $scope.multipleCdeDetails('058122C4-17DC-D98B-E050-BB89AD437492,CFCBA97B-D243-5D7B-E034-0003BA12F5E7');
*/

}]);

