angular.module("cdeSearchPreferences", ['dndLists']);

angular.module("cdeSearchPreferences").controller("SearchPreferencesController", ["$scope", "$http", "$rootScope", function ($scope, $http, $rootScope) {
    
    $scope.searchPreferencesCheckboxModel = { };
    $scope.defaultCheckboxModel = { };

     $scope.modifiedModels = { };

    $scope.$watch('authenticationService.loggedIn', function(newValue, oldValue) {
      if (newValue !== oldValue) {
        if (newValue==false) {
          $scope.searchPreferencesResetButton();
        };
      };
    });
     
     function getCategoryExcludedItems(excludeCategory) {
       var res_ctgex = [ ];
       angular.forEach(JSON.parse($scope.modifiedModels),function(obj){
          if (obj.label === excludeCategory) {
              angular.forEach(obj.items,function(value){
                res_ctgex.push(value.label);
              });
          }
       });
       return res_ctgex;
     }

     $scope.searchPreferencesSaveButton = function () {

        var saveRequestPayload = { };
        saveRequestPayload.excludeTest = $scope.searchPreferencesCheckboxModel.excludeTest;
        saveRequestPayload.excludeTraining = $scope.searchPreferencesCheckboxModel.excludeTraining;
        saveRequestPayload.workflowStatusExcluded = getCategoryExcludedItems('workflowStatusExcluded');
        saveRequestPayload.registrationStatusExcluded = getCategoryExcludedItems('registrationStatusExcluded');
        $http.post('/cdebrowserServer/rest/searchPreferences',saveRequestPayload).then(function(response){
          $rootScope.$broadcast('updateTree',
            {
              test:$scope.searchPreferencesCheckboxModel.excludeTest,
              training:$scope.searchPreferencesCheckboxModel.excludeTraining
            })
        });
            $scope.tableParams.settings({ dataset: []});
            $rootScope.$broadcast('updateResult',
            {
            })
     };
    
    $scope.searchPreferencesResetButton = function () {

        $scope.searchPreferencesCheckboxModel.excludeTest = $scope.defaultCheckboxModel.excludeTest;
        $scope.searchPreferencesCheckboxModel.excludeTraining = $scope.defaultCheckboxModel.excludeTraining;

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

          $http.get('/cdebrowserServer/rest/searchPreferences/default').then(function(response) {
              $scope.workflowStatusExcluded = response.data.workflowStatusExcluded;
              $scope.registrationStatusExcluded = response.data.registrationStatusExcluded;
              $scope.searchPreferencesCheckboxModel.excludeTest = response.data.excludeTest;
              $scope.searchPreferencesCheckboxModel.excludeTraining = response.data.excludeTraining;
              $scope.defaultWorkflowStatusExcluded = response.data.workflowStatusExcluded;
              $scope.defaultRegistrationStatusExcluded = response.data.registrationStatusExcluded;
              $scope.defaultCheckboxModel.excludeTest = response.data.excludeTest;
              $scope.defaultCheckboxModel.excludeTraining = response.data.excludeTraining;     
          }).then(function() { 
                    $http.get('/cdebrowserServer/rest/lookupdata/workflowstatus').then(function(response) {
                      $scope.workflowStatuses = response.data;
                      $scope.workflowStatusIncluded = [];
                      $scope.workflowStatusIncluded = $scope.workflowStatuses.filter(function(x){ return $scope.workflowStatusExcluded.indexOf(x)<0});
                      angular.forEach($scope.models, function(list) {
                          switch(list.label) {
                            case "workflowStatusIncluded"://we skip now the first list element which is ALL
                              for (var i = 1; i <= $scope.workflowStatusIncluded.length - 1; i++) {
                                list.items.push({label: $scope.workflowStatusIncluded[i], type: "workflowStatus"});
                              }
                              list.items.sort(function(a,b){if(a.label<b.label){return -1;}if(a.label>b.label){return 1;} return 0;});
                              break;
                            case "workflowStatusExcluded":
                              for (var i = 0; i <= $scope.workflowStatusExcluded.length - 1; i++) {
                                list.items.push({label: $scope.workflowStatusExcluded[i], type: "workflowStatus"});
                              }
                              list.items.sort(function(a,b){if(a.label<b.label){return -1;}if(a.label>b.label){return 1;} return 0;});
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
                            case "registrationStatusIncluded"://we skip now the first list element which is ALL
                              for (var i = 1; i <= $scope.registrationStatusIncluded.length - 2; i++) { // "length - 2" since the last object is empty
                                list.items.push({label: $scope.registrationStatusIncluded[i], type: "registrationStatus"});
                              }
                              list.items.sort(function(a,b){if(a.label<b.label){return -1;}if(a.label>b.label){return 1;} return 0;});
                              break;
                            case "registrationStatusExcluded":
                              for (var i = 0; i <= $scope.registrationStatusExcluded.length - 1; i++) {
                                list.items.push({label: $scope.registrationStatusExcluded[i], type: "registrationStatus"});
                              }
                              list.items.sort(function(a,b){if(a.label<b.label){return -1;}if(a.label>b.label){return 1;} return 0;});
                              break;
                          }  
                    });
                  });
          }).then(function(){
          var resetRequestPayload = { };
          resetRequestPayload.excludeTest = $scope.defaultCheckboxModel.excludeTest;
          resetRequestPayload.excludeTraining = $scope.defaultCheckboxModel.excludeTraining;
          resetRequestPayload.workflowStatusExcluded = $scope.defaultWorkflowStatusExcluded;
          resetRequestPayload.registrationStatusExcluded = $scope.defaultRegistrationStatusExcluded;
          $http.post('/cdebrowserServer/rest/searchPreferences',resetRequestPayload).then(function(response){
          $rootScope.$broadcast('updateTree',
            {
              test:$scope.searchPreferencesCheckboxModel.excludeTest,
              training:$scope.searchPreferencesCheckboxModel.excludeTraining
            })
          });
        });
            $scope.tableParams.settings({ dataset: []});
            $rootScope.$broadcast('updateResult',
            {
            })
     };

  // $scope.Statuses is for rest calls (workflow and registration) and $scope.Status is for variables (workflowIncluded,
  // workflowExcluded, registrationIncluded and registrationExcluded)
  
    $http.get('/cdebrowserServer/rest/searchPreferences').then(function(response) {
        $scope.workflowStatusExcluded = response.data.workflowStatusExcluded;
        $scope.registrationStatusExcluded = response.data.registrationStatusExcluded;
        $scope.searchPreferencesCheckboxModel.excludeTest = response.data.excludeTest;
        $scope.searchPreferencesCheckboxModel.excludeTraining = response.data.excludeTraining;
        $scope.defaultWorkflowStatusExcluded = response.data.workflowStatusExcluded;
        $scope.defaultRegistrationStatusExcluded = response.data.registrationStatusExcluded;
        $scope.defaultCheckboxModel.excludeTest = response.data.excludeTest;
        $scope.defaultCheckboxModel.excludeTraining = response.data.excludeTraining;     
    }).then(function() { 
              $http.get('/cdebrowserServer/rest/lookupdata/workflowstatus').then(function(response) {
                $scope.workflowStatuses = response.data;
                $scope.workflowStatusIncluded = [];
                $scope.workflowStatusIncluded = $scope.workflowStatuses.filter(function(x){ return $scope.workflowStatusExcluded.indexOf(x)<0});
                angular.forEach($scope.models, function(list) {
                    switch(list.label) {
                      case "workflowStatusIncluded"://we skip now the first list element which is ALL
                        for (var i = 1; i <= $scope.workflowStatusIncluded.length - 1; i++) {
                          list.items.push({label: $scope.workflowStatusIncluded[i], type: "workflowStatus"});
                        }
                        list.items.sort(function(a,b){if(a.label<b.label){return -1;}if(a.label>b.label){return 1;} return 0;});
                        break;
                      case "workflowStatusExcluded":
                        for (var i = 0; i <= $scope.workflowStatusExcluded.length - 1; i++) {
                          list.items.push({label: $scope.workflowStatusExcluded[i], type: "workflowStatus"});
                        }
                        list.items.sort(function(a,b){if(a.label<b.label){return -1;}if(a.label>b.label){return 1;} return 0;});
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
                      case "registrationStatusIncluded"://we skip now the first list element which is ALL
                        for (var i = 1; i <= $scope.registrationStatusIncluded.length - 2; i++) { // "length - 2" to remove the last empty object
                          list.items.push({label: $scope.registrationStatusIncluded[i], type: "registrationStatus"});
                        }
                        list.items.sort(function(a,b){if(a.label<b.label){return -1;}if(a.label>b.label){return 1;} return 0;});
                        break;
                      case "registrationStatusExcluded":
                        for (var i = 0; i <= $scope.registrationStatusExcluded.length - 1; i++) {
                          list.items.push({label: $scope.registrationStatusExcluded[i], type: "registrationStatus"});
                        }
                        list.items.sort(function(a,b){if(a.label<b.label){return -1;}if(a.label>b.label){return 1;} return 0;});
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

    $scope.defaultModels = $scope.models;

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
      list.items.sort(function(a,b){if(a.label<b.label){return -1;}if(a.label>b.label){return 1;} return 0;});
      return true;
    }

    $scope.onMoved = function(list) {
      list.items = list.items.filter(function(item) { return !item.selected; });
    };

    $scope.$watch('searchPreferencesCheckboxModel', function(model) {
        $scope.checkboxModelAsJson = angular.toJson(model, true);
    }, true);

    $scope.$watch('models', function(model) {
        $scope.modelsAsJson = angular.toJson(model, true);
        $scope.modifiedModels = $scope.modelsAsJson;   
    }, true);

}]);
