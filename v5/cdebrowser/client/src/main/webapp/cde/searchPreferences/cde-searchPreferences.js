angular.module("cdeSearchPreferences", ['dndLists']);

angular.module("cdeSearchPreferences").controller("SearchPreferencesController", ["$scope", "$http", function ($scope, $http) {
    $scope.searchPreferencesCheckboxModel = {
       test : true,
       training : true
     };

    $scope.models = [
        {
            label: "workflowStatusIncluded",
            allowedTypes: ['workflowStatus'],
            items: [
                {label: "APPRVD FOR TRIAL USE", type: "workflowStatus"},
                {label: "DRAFT MOD", type: "workflowStatus"},
                {label: "ADRAFT NEW", type: "workflowStatus"},
                {label: "RELEASED", type: "workflowStatus"},
                {label: "RELEASED-NON-CMPLNT", type: "workflowStatus"}
            ],dragging: false
        },
        {
            label: "workflowStatusExcluded",
            allowedTypes: ['workflowStatus'],
            items: [
                {label: "CMTE APPROVED", type: "workflowStatus"},
                {label: "CMTE SUBMTD", type: "workflowStatus"},
                {label: "CMTE SUBMTD USED", type: "workflowStatus"},
                {label: "RETIRED ARCHIVED", type: "workflowStatus"},
                {label: "RETIRED PHASED OUT", type: "workflowStatus"},
                {label: "RETIRED WITHDRAWN", type: "workflowStatus"}
            ],dragging: false
        },
        {
            label: "registrationStatusIncluded",
            allowedTypes: ['registrationStatus'],
            items: [
                {label: "Application", type: "registrationStatus"},
                {label: "Candidate", type: "registrationStatus"},
                {label: "Proposed", type: "registrationStatus"},
                {label: "Qualified", type: "registrationStatus"},
                {label: "Standard", type: "registrationStatus"},
                {label: "Standardized Elsewhere", type: "registrationStatus"},
                {label: "Superceded", type: "registrationStatus"},
                {label: "Suspended", type: "registrationStatus"}
            ],dragging: false
        },
        {
            label: "registrationStatusExcluded",
            allowedTypes: ['registrationStatus'],
            items: [
                {label: "Retired", type: "registrationStatus"}
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
         img.src = 'framework/vendor/ic_content_copy_black_24dp_2x.png';
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
