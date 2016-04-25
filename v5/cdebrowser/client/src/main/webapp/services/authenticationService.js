angular.module("cdeBrowserApp").service('authenticationService', function($http, $location) {
	// service to authenticate user and keep track of user's location //
	this.cameFrom = ''; // was the user redirected from a particular page ? //
	this.loggedIn = false;
	this.userName = ''; // username logged into the system //
	this.errorMessage = 0; // error message status //

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
				that.errorMessage = 1;
			});
	};	

	// logout user //
	this.logout = function() {
		var that = this;
		$http({method: 'GET', url: '/cdebrowserServer/rest/logout'}).
			success(function(response) {
				that.loggedIn = false;
				that.userName = '';
			})
			.error(function(response) {
				that.loggedIn = false;				
				that.userName = '';
			});
	};

	// check user is authenticated //
	this.checkAuth = function() {
		var that = this;
		$http({method: 'GET', url: '/cdebrowserServer/rest/user'})
			.success(function(response) {
			  if (response.length>0) {
			  	that.loggedIn = true;
			  	that.userName = response;
			  }
			  else {
			  	that.loggedIn=false;
			  	that.userName = '';			  	
			  };
			})
			.error(function(response) {
			  	that.loggedIn=false;
			  	that.userName = '';			  	
			});
	};
		
});