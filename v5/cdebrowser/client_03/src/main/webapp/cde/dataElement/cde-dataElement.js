/**
 * Created by lernermh on 4/17/15.
 */
angular.module("cdeDataElement", []);

angular.module("cdeDataElement").controller("DataElementCtrl", ["$scope", function ($scope) {


    $scope.getCdeData = function () {


        $scope.dataElementDataElementDetails=
            [
                {
                    description: "Public ID",
                    value: JSON.parse($scope.cdeData).publicId
                },
                {
                    description: "Version",
                    value: JSON.parse($scope.cdeData).version
                },
                {
                    description: "Long Name",
                    value: JSON.parse($scope.cdeData).longName
                },
                {
                    description: "Short Name",
                    value: JSON.parse($scope.cdeData).preferredName
                },
                {
                    description: "Preferred Question Text",
                    value: JSON.parse($scope.cdeData).longCDEName
                },
                {
                    description: "Definition",
                    value: JSON.parse($scope.cdeData).preferredDefinition
                },
                {
                    description: "Value Domain",
                    value: "STILL NEED TO FIND THIS"
                },
                {
                    description: "Data Element Concept",
                    value: "STILL NEED TO FIND THIS"
                },
                {
                    description: "Context",
                    value: JSON.parse($scope.cdeData).contextName
                },
                {
                    description: "Workflow Status",
                    value:  "STILL NEED TO FIND THIS"
                },
                {
                    description: "Origin",
                    value: JSON.parse($scope.cdeData).origin
                },
                {
                    description: "Registration Status",
                    value: JSON.parse($scope.cdeData).registrationStatus
                }

            ]
    }

$scope.getCdeData();

}]);
