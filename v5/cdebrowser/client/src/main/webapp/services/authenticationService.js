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
				if (redirect!='') { // only redirect if coming from another location //
					$location.path('/cdeCart').replace();
				};
			}).
			error(function(response) {
			});		
	};	

	// logout user //
	this.logout = function() {
		$http({method: 'GET', url: '/cdebrowserServer/rest/logout'}).
			success(function(response) {
				this.loggedIn = false;
			})
			.error(function(response) {
				
			});
	};

	// check user is authenticated //
	this.checkAuth = function() {
		var that = this;
		return this.loggedIn;
	};
		
});