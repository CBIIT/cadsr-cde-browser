angular.module("cdeSearchPreferences", ['dndLists']);

angular.module("cdeSearchPreferences").controller("SearchPreferencesController", ["$scope", "$http", function ($scope, $http) {
    
    $scope.searchPreferencesCheckboxModel = { };

     
     
     
     $scope.searchPreferencesSaveButton = function () {
        
        $scope.treeData = { };

        if ($scope.searchPreferencesCheckboxModel.excludeTest === true) 
          {
            $scope.treeData.showIt = false;
            
          }
        if ($scope.searchPreferencesCheckboxModel.excludeTest === false) 
          {
            
          }
        
     };



    $scope.searchPreferencesResetButton = function () {
        $scope.models = $scope.models;
     };



  // $scope.Statuses is for rest calls (workflow and registration) and $scope.Status is for variables (workflowIncluded,
  // workflowExcluded, registrationIncluded and registrationExcluded)
  
    $http.get('/cdebrowserServer/rest/searchPreferences').then(function(response) { 
        $scope.workflowStatusExcluded = response.data.workflowStatusExcluded;
        $scope.registrationStatusExcluded = response.data.registrationStatusExcluded;
        $scope.searchPreferencesCheckboxModel.excludeTest = response.data.excludeTest;
        $scope.searchPreferencesCheckboxModel.excludeTraining = response.data.excludeTraining;     
    }).then(function() { 
              $http.get('/cdebrowserServer/rest/lookupdata/workflowstatus').then(function(response) {
                $scope.workflowStatuses = response.data;
                $scope.workflowStatusIncluded = [];
                $scope.workflowStatusIncluded = $scope.workflowStatuses.filter(function(x){ return $scope.workflowStatusExcluded.indexOf(x)<0});
                angular.forEach($scope.models, function(list) {
                    switch(list.label) {
                      case "workflowStatusIncluded":
                        for (var i = 0; i <= $scope.workflowStatusIncluded.length - 1; i++) {
                          list.items.push({label: $scope.workflowStatusIncluded[i], type: "workflowStatus"});
                        }
                        break;
                      case "workflowStatusExcluded":
                        for (var i = 0; i <= $scope.workflowStatusExcluded.length - 1; i++) {
                          list.items.push({label: $scope.workflowStatusExcluded[i], type: "workflowStatus"});
                        }
                        break;
                    }  
              });
            });
    }).then(function() {
              $http.get('/cdebrowserServer/rest/lookupdata/registrationstatus').then(function(response) {
                $scope.registrationStatuses = response.data;
                $scope.registrationStatusIncluded = [];
                $scope.registrationStatusIncluded = $scope.registrationStatuses.filter(function(x){ return $scope.registrationStatusExcluded.indexOf(x)<0});
                angular.forEach($scope.models, function(list) {
                    switch(list.label) {
                      case "registrationStatusIncluded":
                        for (var i = 0; i <= $scope.registrationStatusIncluded.length - 2; i++) { // "length - 2" since the last object is empty
                          list.items.push({label: $scope.registrationStatusIncluded[i], type: "registrationStatus"});
                        }
                        break;
                      case "registrationStatusExcluded":
                        for (var i = 0; i <= $scope.registrationStatusExcluded.length - 1; i++) {
                          list.items.push({label: $scope.registrationStatusExcluded[i], type: "registrationStatus"});
                        }
                        break;
                    }  
              });
            });
    });

    $scope.models = [
        {
            label: "workflowStatusIncluded",
            allowedTypes: ['workflowStatus'],
            items: [
            ],dragging: false
        },
        {
            label: "workflowStatusExcluded",
            allowedTypes: ['workflowStatus'],
            items: [
            ],dragging: false
        },
        {
            label: "registrationStatusIncluded",
            allowedTypes: ['registrationStatus'],
            items: [
            ],dragging: false
        },
        {
            label: "registrationStatusExcluded",
            allowedTypes: ['registrationStatus'],
            items: [
            ],dragging: false
        }];

    $scope.getSelectedItemsIncluding = function(list, item) {
      item.selected = true;
      return list.items.filter(function(item) { return item.selected; });
    };

    $scope.onDragstart = function(list, event) {
       list.dragging = true;
       if (event.dataTransfer.setDragImage) {
         var img = new Image();
         img.src = '';
         event.dataTransfer.setDragImage(img, 0, 0);
       }
    };

    $scope.onDrop = function(list, items, index) {
      angular.forEach(items, function(item) { item.selected = false; });
      list.items = list.items.slice(0, index)
                  .concat(items)
                  .concat(list.items.slice(index));
      return true;
    }

    $scope.onMoved = function(list) {
      list.items = list.items.filter(function(item) { return !item.selected; });
    };

    $scope.$watch('models', function(model) {
        $scope.modelAsJson = angular.toJson(model, true);
    }, true);



    



}]);
