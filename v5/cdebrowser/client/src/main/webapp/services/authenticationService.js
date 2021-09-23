angular.module("cdeBrowserApp").service('authenticationService', function($http, $location, $q) {
	// service to authenticate user and keep track of user's location //
	this.cameFrom = ''; // was the user redirected from a particular page ? //
	this.loggedIn = false;
	this.userName = ''; // username logged into the system //
	this.errorMessage = 0; // error message status //
	this.passwordChangeStationLink = '';

	// login user //
	this.login = function(username, credential, redirect) {
		var that = this;
		$http({method: 'POST', url: '/cdebrowserServer/rest/login',headers: { 'Authorization':'Basic ' + btoa(username+':'+credential)}}).
			then(function(response) {
				response=response['data'];
				that.cameFrom = redirect;
				that.loggedIn = true;
				if (redirect!='') { // only redirect if coming from another location //
					$location.path('/cdeCart').replace();
				};
			}).
			catch(function(response) {
				that.errorMessage = 1;
			});
	};	

	// logout user //
	this.logout = function() {
		var that = this;
		$http({method: 'GET', url: '/cdebrowserServer/rest/logout'}).
			then(function(response) {
				response=response['data'];
				that.loggedIn = false;
				that.userName = '';
			})
			.catch(function(response) {
				that.loggedIn = false;				
				that.userName = '';
			});
	};

	// check user is authenticated //
	this.checkAuth = function() {
		var that = this;
		$http({method: 'GET', url: '/cdebrowserServer/rest/user'})
			.then(function(response) {
				response=response['data'];
			  if (response.length>0) {
			  	that.loggedIn = true;
			  	that.userName = response;
			  }
			  else {
			  	that.loggedIn=false;
			  	that.userName = '';			  	
			  };
			})
			.catch(function(response) {
			  	that.loggedIn=false;
			  	that.userName = '';			  	
			});
	};

	// get password change station link //
	this.getPasswordChangeStationLink = function() {
		var that = this;
		$http({method: 'GET', url: '/cdebrowserServer/rest/getPwcsURL'})
			.then(function(response) {
				response=response['data'];
				that.passwordChangeStationLink = response;
			})
			.catch(function(response) {
		  	
			})
	};
	
		
});