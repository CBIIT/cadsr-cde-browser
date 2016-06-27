angular.module('cdeBrowserApp')

.directive('uiTreeSelect', [
  'groupFactory',
  '$timeout',
  '$http',
  'filterService',
  function (groupFactory, $timeout, $http,filterService) {
      return {
          restrict: 'E',
          scope: { model: '=' ,  contextCascade: '&' },
          link: function (scope, el) {
            scope.filterService =filterService;
              scope.breadcrumbs = [{ "id": 0, "title": "Protocols" }];
                  scope.loadProtocols=function(searchInput){
                    if(searchInput.length===3) {
        
            $http.get('/cdebrowserServer/rest/lookupdata/protocol',{params:{protocolOrForm:searchInput}}).success(function(response) {
            groupFactory.fillProtocols(response);
           scope.filterService.protocols = groupFactory.load(0);
            }); 
                  };   };

                      scope.$watch('filterService.protocols', function(){
      if(scope.filterService.protocols.length==0) {
              scope.breadcrumbs = [{ "id": 0, "title": "Protocols" }];
      }
    })

              scope.loadChildGroupsOf = function (group, $select) {
                  $select.search = '';
                  group.title='Forms';
                  scope.breadcrumbs.push(group);
                  scope.filterService.protocols = groupFactory.load(group.id);
                  scope.$broadcast('uiSelectFocus');
              };

              scope.navigateBackTo = function (crumb, $select) {
                  $select.search = '';
                  var index = _.findIndex(scope.breadcrumbs, { id: crumb.id });
                  scope.breadcrumbs.splice(index + 1, scope.breadcrumbs.length);
                  scope.filterService.protocols  = groupFactory.load(_.last(scope.breadcrumbs).id);
                  $select.open = false;
                  scope.$broadcast('uiSelectFocus');
              };

              scope.checkChildren=function(id){
            return groupFactory.isChildAvailable(id);
        }
          },
          templateUrl: '/ui-tree-select.html'
      };
  }
])

.directive('uiSelectFocuser', function ($timeout) {
    return {
        restrict: 'A',
        require: '^uiSelect',
        link: function (scope, elem, attrs, uiSelect) {
            scope.$on('uiSelectFocus', function () {
                $timeout(uiSelect.activate);
            });
        }
    };
})

.factory('groupFactory', ['filterService',
  function (filterService) {
    var sampledata = {};


      return {
          load: function (id) {
              if (id == 0) {
                  return _.uniqBy(sampledata[0],"protocolIdSeq");
              }
              else {
                  return sampledata[id];
              }
          },

          fillProtocols: function(proto) {
            filterService.protocols = proto;
            sampledata[0] = proto;

             _.forEach(sampledata[0], function (value, index) {
                sampledata[0][index].id = value.protocolIdSeq;
                sampledata[0][index].name = value.protocolLongName;
            });

            var protocols = angular.copy(_.groupBy(sampledata[0], "protocolIdSeq"));
            _.forEach(protocols, function (value, index) {
                _.forEach(value, function (obj, ndx) {
                    protocols[index][ndx].id = obj.formIdSeq;
                    protocols[index][ndx].name = obj.formLongName;
                });
            });

            _.assign(sampledata, protocols);

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

.run(['$templateCache', function ($templateCache) {
    $templateCache.put('selectize/choices.tpl.html', [

      '<div ng-show="$select.open"',
      '  class="ui-select-choices group-tree selectize-dropdown single">',
      '  <div ng-show="breadcrumbs.length > 1" class="ui-select-breadcrumbs">',
      '    <span class="ui-breadcrumb" ng-repeat="crumb in breadcrumbs"',
      '       ng-click="navigateBackTo(crumb, $select)">',
      '       {{crumb.title}}',
      '    </span>',
      '  </div>',
      '  <div class="ui-select-choices-content selectize-dropdown-content">',
      '    <div class="ui-select-choices-group optgroup">',
      '      <div ng-show="$select.isGrouped"',
      '        class="ui-select-choices-group-label optgroup-header">',
      '        {{$group.name}}',
      '      </div>',
      '      <div class="ui-select-choices-row">',
      '        <div class="option ui-select-choices-row-inner"',
      '           data-selectable="">',
      '        </div>',
      '      </div>',
      '    </div>',
      '  </div>',
      '</div>'
    ].join(''));
}])