
describe('CDE Browser Client spec', function() {
	
	var $scope = {}, controller, httpBackend, rootscope, anchor_scroll, location, mywindow, filter, timeout, local_storage, session_storage, http, route, ng_table_params, searchfactory, cart_service, filterservice, authservice, download_factory, group_factory, group_factory1, compare_service;
	beforeEach(module("cdeBrowserApp"));


	beforeEach(inject(function($controller, $window, $filter, $timeout, $localStorage, $sessionStorage, $http, $location, $route, NgTableParams, searchFactory, cartService, filterService, authenticationService, downloadFactory, groupFactory, groupFactory1, compareService, $rootScope, $anchorScroll, $httpBackend) {
        rootscope = $rootScope.$new();
        $scope = $rootScope.$new();
        location = $location;
        mywindow = $window;
        filter = $filter, 
		timeout = $timeout, 
		local_storage = $localStorage, 
		session_storage = $sessionStorage,
		http = $http,
		route = $route, 
		ng_table_params = NgTableParams, 
		searchfactory = searchFactory,
		cart_service = cartService, 
		filterservice = filterService, 
		authservice = authenticationService, 
		download_factory = downloadFactory, 
		group_factory = groupFactory, 
		group_factory1 = groupFactory1, 
		compare_service = compareService, 
		anchor_scroll = $anchorScroll,
		httpBackend = $httpBackend,
        

        spyOn(location, 'path');
        spyOn(location, 'hash');
        spyOn(angular, 'element').and.callThrough();
        controller = $controller;

        httpBackend.when('POST', '/cdebrowserServer/rest/programAreaNames').respond({ "token": "success" });

        cdeBrowserCtrl = controller('cdeBrowserController', {
        	$window:mywindow, 
        	$scope:$scope, 
        	$filter:filter, 
        	$timeout:timeout, 
        	$localStorage:local_storage, 
        	$sessionStorage:session_storage, 
        	$http:http, 
        	$location:location,
        	$route:route, 
        	NgTableParams:ng_table_params, 
        	searchFactory:searchfactory,
        	cartService:cart_service,
        	filterService:filterservice, 
        	authenticationService:authservice,
        	downloadFactory:download_factory, 
        	groupFactory:group_factory,
        	groupFactory1:group_factory, 
        	compareService:compare_service, 
        	$rootScope:rootscope, 
        	$anchorScroll:anchor_scroll
        });

    }));

     it('first test case', function() {
        expect(true).toBe(true);
       	console.log("test_console");
     });

     it('should test goToAnchor()', function() {
     	$scope.goToAnchor("test");
     	expect(location.hash).toHaveBeenCalled();
     });

     it('should test contextCascade()', function() {
     	$scope.filterService = {
     		searchFilter: {
				context:""
     		}
     	};
     	$scope.contextCascade({contextIdSeq:"test"});
     	expect($scope.filterService.searchFilter.context).toBe("test");
     });

});
