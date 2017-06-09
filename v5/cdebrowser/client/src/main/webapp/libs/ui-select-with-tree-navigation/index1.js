angular.module('cdeBrowserApp')

.directive('uiTreeSelect1', [
  'groupFactory1',
  '$timeout',
  '$http',
  'filterService',
  '$filter',
  '$rootScope',
  function (groupFactory1, $timeout, $http,filterService,$filter,$rootScope) {
      return {
          restrict: 'E',
          scope: { model1: '=' , contextCascade: '&' },
          link: function (scope, el) {
              scope.breadcrumbs = [{ "id": 0, "title": "CS" }];
              scope.filterService=filterService;
                  scope.loadClassifications = function(searchInput) {
       
        if(searchInput.length>=3) {
            $http.get('/cdebrowserServer/rest/lookupdata/classificationscheme',{params:{csOrCsCsi:searchInput}}).success(function(response) {
            groupFactory1.fillClassifications(response);
            scope.filterService.classifications = groupFactory1.load(0);
            });  
        }
    };

    scope.$watch('filterService.classifications', function(){
      if(scope.filterService.classifications.length==0) {
              scope.breadcrumbs = [{ "id": 0, "title": "CS" }];
      }
    });



    scope.checkChildren1=function(selectedGroup){
            return groupFactory1.isChildAvailable(selectedGroup,scope.breadcrumbs.length);
        }

        $rootScope.$on('loadClassifications',function(){
          // scope.loadChildGroupsOf1();
        })

              scope.loadChildGroupsOf1 = function (group, $select) {
                console.log(group,$select)
                  $select.search = '';
                  group.title='CSI';

                  scope.filterService.classifications=_.filter(groupFactory1.load(group.id),{'csiLevel':group.csiLevel});
                  if(scope.filterService.classifications.length==0){
                    scope.filterService.classifications=_.filter(groupFactory1.load(group.csIdSeq),{'csiLevel':group.csiLevel+1,'parentCsiIdSeq':group.csCsiIdSeq});
                  }
                  scope.breadcrumbs.push(group);

                  scope.$broadcast('uiSelectFocus');
                  
              };

              scope.navigateBackTo = function (crumb, $select) {
                  $select.search = '';
                  var index = _.findIndex(scope.breadcrumbs, { id: crumb.id });
                  scope.breadcrumbs.splice(index + 1, scope.breadcrumbs.length);
                  scope.filterService.classifications=_.filter(groupFactory1.load(_.last(scope.breadcrumbs).id),{'csiLevel':_.last(scope.breadcrumbs).csiLevel});
                  if(scope.filterService.classifications.length==0){
                    scope.filterService.classifications=_.filter(groupFactory1.load(_.last(scope.breadcrumbs).id));
                  }
                  
                  $select.open = false;
                  scope.$broadcast('uiSelectFocus');
                 
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
                $timeout(uiSelect.activate);
            });
        }
    };
})

.factory('groupFactory1', ['filterService','$filter',
  function (filterService,$filter) {
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
            // filterService.classifications = cs;
            filterService.setClassifications(cs);
            
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
            

            var sample = sampledata[0];
          },

          clearData: function() {
            sampledata = {};
          },
          getNameFromId:function(id){

            return _.find(sampledata[0],{'csIdSeq':id});
          },

          isChildAvailable: function(selectedgroup,breadcrumb_length) {
          if(breadcrumb_length==1){
              if(this.load(selectedgroup.id)==undefined && selectedgroup.csiLevel==2)
                  return false;
              return true;
          }else{
              if(selectedgroup.csiLevel==2)
                  return false;
              if($filter('filter')(angular.copy(this.load(selectedgroup.csIdSeq)),{'csiLevel':2,'parentCsiIdSeq':selectedgroup.csCsiIdSeq}).length==0){
                  return false;
              }
              return true;
          }   
          }
      }
  }
])