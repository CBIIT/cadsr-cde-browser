angular.module("cdeBrowserApp").service('filterService', function($resource) {
	// define variables //
	this.serverData = []; // initial data from server goes here
	this.selectedClassification = ""; // only displays after selecting a context
	this.selectedProtocolForm = ""; // only displays after selecting a context
	this.selectedProgramArea = ""; // selected program area  from drop down //
	this.selectedContext = ""; // selected context from drop down //

	// gets initial server data //
	this.getServerData = function(url) {
		var that = this;
		if (!this.serverData.length) { // check if serverData is already here. No need to make multiple server calls //
			var data = $resource(url).query();
			data.$promise.then(function(response) {
				that.serverData = response; // set server data containing program areas and contexts //
				that.selectedProgramArea = response[0]; // set initial program area to all contexts //
			});
		};		
	};

	// resets all important variables
	this.resetFilters = function() {
		this.selectedContext = "";
		this.selectedProgramArea = this.serverData[0];
		this.classifications = [];
		this.protocolForms = [];		
	};

	// returns program area number at the moment. Temp function //
	this.getProgramAreaValue = function(programArea) {
		return programArea.programArea;
	};

	// select context dropdown based on context click in left menu //
	this.selectContextByNode = function(programArea,id) {
		var contexts = this.selectedProgramArea.children;
		for (var item in contexts) {
			if (contexts[item].idSeq == id) {
				this.selectedContext = contexts[item]; 
			};
		};
	};

});