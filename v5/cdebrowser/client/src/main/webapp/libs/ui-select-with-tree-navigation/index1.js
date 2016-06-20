angular.module('cdeBrowserApp')

.directive('uiTreeSelect1', [
  'groupFactory1',
  '$timeout',
  '$http',
  'filterService',
  function (groupFactory1, $timeout, $http,filterService) {
      return {
          restrict: 'E',
          scope: { model1: '=' },
          link: function (scope, el) {
              scope.breadcrumbs = [{ "id": 0, "title": "CS" }];
              scope.filterService=filterService;
                  scope.loadClassifications = function(searchInput) {
       
        if(searchInput.length===3) {
            $http.get('/cdebrowserServer/rest/lookupdata/classificationscheme',{params:{csOrCsCsi:searchInput}}).success(function(response) {
            groupFactory1.fillClassifications(response);
            scope.filterService.classifications = groupFactory1.load(0);
            });  
        }

       
    };

    scope.checkChildren1=function(id){
            return groupFactory1.isChildAvailable(id);
        }

              scope.loadChildGroupsOf1 = function (group, $select) {
                  $select.search = '';
                  scope.breadcrumbs.push(group);
                  scope.filterService.classifications = groupFactory1.load(group.id);
                  scope.$broadcast('uiSelectFocus');
                  angular.element(document.querySelector("#CS")).css("display","block");
                  angular.element(document.querySelector("#Protocol")).css("display","none");
              };

              scope.navigateBackTo = function (crumb, $select) {
                  console.log(crumb);
                  $select.search = '';
                  var index = _.findIndex(scope.breadcrumbs, { id: crumb.id });
                  console.log(index);
                  scope.breadcrumbs.splice(index + 1, scope.breadcrumbs.length);
                  scope.filterService.classifications  = groupFactory1.load(_.last(scope.breadcrumbs).id);
                  $select.open = false;
                  scope.$broadcast('uiSelectFocus');
                  angular.element(document.querySelector("#CS")).css("display","block");
                  angular.element(document.querySelector("#Protocol")).css("display","none");

              };
          },
          templateUrl: '/ui-tree-select1.html'
      };
  }
])

.directive('uiSelectFocuser1', function ($timeout) {
    return {
        restrict: 'A',
        require: '^uiSelect',
        link: function (scope, elem, attrs, uiSelect) {
            scope.$on('uiSelectFocus', function () {
                angular.element(document.querySelector("#CS")).css("display","block");
                angular.element(document.querySelector("#Protocol")).css("display","none");
                $timeout(uiSelect.activate);
                console.log(uiSelect);
            });
        }
    };
})

.factory('groupFactory1', ['filterService',
  function (filterService) {
    var sampledata = {};


      return {
          load: function (id) {
              if (id == 0) {
                  return _.uniqBy(sampledata[0],"csIdSeq");
              }
              else {
                  return sampledata[id];
              }
          },

          fillClassifications: function(cs) {
            filterService.classifications = cs;
            sampledata[0] = cs;

             _.forEach(sampledata[0], function (value, index) {
                sampledata[0][index].id = value.csIdSeq;
                sampledata[0][index].name = value.csLongName;
            });

            var classifications = angular.copy(_.groupBy(sampledata[0], "csIdSeq"));
            _.forEach(classifications, function (value, index) {
                _.forEach(value, function (obj, ndx) {
                    classifications[index][ndx].id = obj.csCsiIdSeq;
                    classifications[index][ndx].name = obj.csCsiName;
                });
            });

            _.assign(sampledata, classifications);
            console.log(sampledata);

            var sample = sampledata[0];
          },

          clearData: function() {
            sampledata = {};
          },

          isChildAvailable: function(id) {
              if(this.load(id)==undefined)
                return false;
              return true;
          }
      }
  }
])

// .run(['$templateCache', function ($templateCache) {
//     $templateCache.put('selectize/choices.tpl.html', [
//       '<div ng-show="$select.open"',
//       '  class="ui-select-choices group-tree selectize-dropdown single">',
//       '  <div ng-show="breadcrumbs1.length > 1" class="ui-select-breadcrumbs">',
//       '    <span class="ui-breadcrumb" ng-repeat="crumb in breadcrumbs1"',
//       '       ng-click="navigateBackTo(crumb, $select)">',
//       '       {{crumb.title}}',
//       '    </span>',
//       '  </div>',
//       '  <div class="ui-select-choices-content selectize-dropdown-content">',
//       '    <div class="ui-select-choices-group optgroup">',
//       '      <div ng-show="$select.isGrouped"',
//       '        class="ui-select-choices-group-label optgroup-header">',
//       '        {{$group.name}}',
//       '      </div>',
//       '      <div class="ui-select-choices-row">',
//       '        <div class="option ui-select-choices-row-inner"',
//       '           data-selectable="">',
//       '        </div>',
//       '      </div>',
//       '    </div>',
//       '  </div>',
//       '</div>'
//     ].join(''));
// }])