angular.module("cdeBrowserApp").service('authenticationService', function($http, $location) {
	// service to authenticate user and keep track of user's location //
	this.cameFrom = '';
	this.loggedIn = false;

	// login user //
	this.login = function(username, credential, redirect) {
		var that = this;
		$http({method: 'POST', url: '/cdebrowserServer/rest/login',headers: { 'Authorization':'Basic ' + btoa(username+':'+credential)}}).
			success(function(response) {
				that.cameFrom = redirect;
				that.loggedIn = true;
				if (redirect!='') { // only redirect if coming from another location //
					$location.path('/cdeCart').replace();
				};
			}).
			error(function(response) {
			});		
	};	

	// logout user //
	this.logout = function() {
		var that = this;
		$http({method: 'GET', url: '/cdebrowserServer/rest/logout'}).
			success(function(response) {
				that.loggedIn = false;
			})
			.error(function(response) {
				that.loggedIn = false;				
			});
	};

	// check user is authenticated //
	this.checkAuth = function() {
		var that = this;
		$http({method: 'GET', url: '/cdebrowserServer/rest/user'})
			.success(function(response) {
			  if (response.length>0) {
			  	that.loggedIn = true;
			  }
			  else {
			  	that.loggedIn=false;
			  };
			})
			.error(function(response) {
			  	that.loggedIn=false;				
			});
	};
		
});