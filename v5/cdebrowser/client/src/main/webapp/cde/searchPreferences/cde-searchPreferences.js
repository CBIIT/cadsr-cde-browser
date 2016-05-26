angular.module("cdeSearchPreferences", ['dndLists']);//, 'cdeBrowserApp'

angular.module("cdeSearchPreferences").controller("SearchPreferencesController", ["$scope", "$http", "dataTransferService", function ($scope, $http, dataTransferService) {
    
    $scope.searchPreferencesCheckboxModel = { };
    $scope.defaultCheckboxModel = { };

     $scope.modifiedModels = { };
     
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
          console.log(response.statusText);
          // $scope.$emit('basicSearchEvent', query, field, type, publicIdName);
        });



        //dataTransferService.setData("workflowStatusIncluded", $scope.workflowStatusIncluded);
        //dataTransferService.setData("registrationStatusIncluded", $scope.registrationStatusIncluded);


        // console.log(angular.element('span#selectedNode').text());
        // $http.get('/cdebrowserServer/rest/contextData').then(function(response){
        //   console.log(response);
        //   $scope.contextData = response.data;
        // });
        // $scope.treeData = { };

        // if ($scope.searchPreferencesCheckboxModel.excludeTest === true) 
        //   {
        //     $scope.treeData.showIt = false;
        //     // console.log("Hidden");
        //   }
        // if ($scope.searchPreferencesCheckboxModel.excludeTest === false) 
        //   {
        //     // var obj = angular.element("div#cssFade ul li span");
        //     // console.log(obj);
        //   }
        // // if ($scope.searchPreferencesCheckboxModel.excludeTraining === true) 
        // //   {
        // //     $scope.treeData.showIt = false;
        // //   }
        // // if ($scope.searchPreferencesCheckboxModel.excludeTraining === false) 
        // //   {
        // //     $scope.treeData.showIt = true;
        // //   }



      // $scope.onClickBasicSearch = function (query, field, type, publicIdName) {
      //     $scope.currentCdeTab = 0;
      //     $location.path("/search").replace(); // change url to search since we are doing a search //
      //     if (query!='') { // create base url. determine if query is blank //
      //             var url = "".concat("cdebrowserServer/rest/search?", field, "=",query,"&queryType=",type); // search has a query value // 
      //             if (publicIdName && publicIdName!='') {
      //                 url=url.concat("&name=",publicIdName)
      //             };
      //     }
      //     else {
      //         var url = "".concat("cdebrowserServer/rest/search");
      //     };
      //     c=0; // index of searchFilter key //
      //     for (var x in fs.searchFilter) {

      //         if (fs.searchFilter[x]&&field=='name') {
      //             if (c==0&&query=='') {
      //                 if (x=='programArea') {
      //                     url+="?"+x+"="+$scope.contextListMaster[fs.searchFilter[x]].text;
      //                 }
      //                 else {
      //                     url+="?"+x+"="+fs.searchFilter[x];
      //                 };
      //             }
      //             else {
      //                 if (x=='programArea') {
      //                     url+="&"+x+"="+$scope.contextListMaster[fs.searchFilter[x]].text;
      //                 }
      //                 else {
      //                     url+="&"+x+"="+fs.searchFilter[x];
      //                 };                    
      //             };
      //             c++;
      //         };
      //     };






     };
    


    $scope.searchPreferencesResetButton = function () {

        $scope.searchPreferencesCheckboxModel.excludeTest = $scope.defaultCheckboxModel.excludeTest;
        $scope.searchPreferencesCheckboxModel.excludeTraining = $scope.defaultCheckboxModel.excludeTraining;
        // $scope.searchPreferencesCheckboxModel = $scope.defaultCheckboxModel;
        $scope.models = $scope.defaultModels;

        var resetRequestPayload = { };
        resetRequestPayload.excludeTest = $scope.defaultCheckboxModel.excludeTest;
        resetRequestPayload.excludeTraining = $scope.defaultCheckboxModel.excludeTraining;
        resetRequestPayload.workflowStatusExcluded = $scope.defaultWorkflowStatusExcluded;
        resetRequestPayload.registrationStatusExcluded = $scope.defaultRegistrationStatusExcluded;
        $http.post('/cdebrowserServer/rest/searchPreferences',resetRequestPayload).then(function(response){
          console.log(response.statusText);
          // $scope.$emit('basicSearchEvent', query, field, type, publicIdName);
        });

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

    // $scope.defaultModels = angular.toJson($scope.models);
    // $scope.defaultCheckboxModel = angular.copy($scope.searchPreferencesCheckboxModel);




   // $scope.setSortOrder = function () {
   //      angular.forEach($scope.searchResults, function (item) {
   //          var rS = $scope.registrationSort.indexOf(item.registrationStatus);
   //          var wS = $scope.workflowSort.indexOf(item.workflowStatus);
   //          if (rS > -1) {
   //              item['registrationSort'] = rS
   //          } else {
   //              item['registrationSort'] = 1000
   //          }
   //          if (wS > -1) {
   //              item['workflowSort'] = wS
   //          } else {
   //              item['workflowSort'] = 1000
   //          }
   //      });

   //  };

   //  $http.get('/cdebrowserServer/rest/lookupdata/registrationstatus').success(function (response) {
   //      $scope.registrationSort = response;
   //      $scope.staticFilters.registrationStatusFilter = angular.copy($scope.registrationSort).sort();
   //  });
   //  $http.get('/cdebrowserServer/rest/lookupdata/workflowstatus').success(function (response) {
   //      $scope.workflowSort = response;
   //      $scope.staticFilters.workflowStatusFilter = angular.copy($scope.workflowSort).sort();
   //  });

   //  $scope.resetSortOrder = function () {
   //      isInitialColumnClick = 0;
   //      $scope.tableParams.sorting({'registrationSort': 'asc','workflowSort':'asc','longName':'asc'});

   //      /*  FIXME still not getting the "view" back after reset sort order  */
   //      //$scope.changeView(0,$scope.tabs[0]);
   //      $scope.currentCdeTab = 0;
   //      $location.path("/search").replace();


   //  };

   //  $scope.initTableParams = function () {

   //      $scope.tableParams = new NgTableParams(
   //        {
   //          count:100,
   //          page:1,
   //          filter: { },
   //          sorting: {
   //              registrationSort: 'asc',
   //              workflowSort: 'asc',
   //              longName: 'asc'
   //          }      
   //        },
   //        {
   //          defaultSort:"asc",
   //          counts:[],
   //          dataset:[]
            
   //        });  
   //  };



}]);
