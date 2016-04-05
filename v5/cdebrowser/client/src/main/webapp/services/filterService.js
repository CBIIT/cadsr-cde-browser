angular.module("cdeBrowserApp").service('filterService', function($resource) {
	// define variables //
	this.serverData = []; // initial data from server goes here
	this.selectedClassification = ""; // only displays after selecting a context
	this.selectedProtocolForm = ""; // only displays after selecting a context
	this.isAChildNodeSearch = false;
	this.selectedProgramArea = undefined; // selected program area  from drop down //
	this.selectedContext = ""; // selected context from drop down //
	this.searchFilter = {};
	this.classifications = []; // classification array for context //
	this.protocolForms = []; // protocol form array for context //	
	this.currentContext = []; // temp method, delete after rest services are fixec //
	this.reset = 0; // filters have been reset when 0 //

	// resets all important variables
	this.resetFilters = function() {
		this.selectedContext = "";
		this.isAChildNodeSearch = false;	
		this.selectedProgramArea = this.serverData[0];
		this.classifications = [];
		this.protocolForms = [];
		this.selectedProtocolForm = "";
		this.selectedClassification	= "";
		this.searchFilter = {};
		this.reset = 0; // hide protocol forms and classification dropdowns //
	};

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

	// returns classificiations and protocol forms //
	this.getClassificationsAndProtocolForms = function() {
		this.classifications = []; this.protocolForms = [];
		this.reset=1; // show classifications and protocolforms dropdowns //
		var that = this;
		var classifications = $resource('/cdebrowserServer/rest/oneContextData?contextId='.concat(this.selectedContext.idSeq,"&programArea=",this.selectedProgramArea.programArea,"&folderType=0")).query();
		var protocolForms = $resource('/cdebrowserServer/rest/oneContextData?contextId='.concat(this.selectedContext.idSeq,"&programArea=",this.selectedProgramArea.programArea,"&folderType=1")).query();
		classifications.$promise.then(function(response) {
			that.classifications = response;
		});
		protocolForms.$promise.then(function(response) {
			that.protocolForms = response;
		});
	};	

	// select context dropdown based on context click in left menu //
	this.selectContextByNode = function(programArea,id) {
		this.selectedProgramArea = this.serverData[programArea]; // user clicked the left menu. set program area //
		var programAreaContexts = this.selectedProgramArea.children;
		for (var item in programAreaContexts) {
			if (programAreaContexts[item].idSeq == id) {
				this.selectedContext = programAreaContexts[item]; 
				this.getClassificationsAndProtocolForms(); // get classifications and protocol forms //
			};
		};
	};

	// get selected context via tree pruning //
	// delete me when context is added to items //
	// temporary method delete after rest services are fixed //
	this.getContextByName = function(node) {
		var currentProgramAreaChildren = this.serverData[node.programArea].children;
		for (var x in currentProgramAreaChildren) {
			if (currentProgramAreaChildren[x].text==node.treePath[1]) {
				this.selectContextByNode(node.programArea, currentProgramAreaChildren[x].idSeq)
				this.currentContext = currentProgramAreaChildren[x];
				break
			};
		};
		return this.currentContext;
	};

	// gets classificationSchemeId when selecting a classificationSchemeItemId //
	// temporary method delete after rest services are fixed //
	this.getClassifficationOrProtocolByName = function(currentContext,node) {
		var parentText = node.treePath[node.treePath.length-2];
		var children = currentContext.children;
		for (var child in children) {
			var grandchildren = children[child].children;
			for (var grandchild in grandchildren) {
				var currentText = grandchildren[grandchild].text;
				if (currentText==parentText) {
					return grandchildren[grandchild];
					break
				};
			};
		};
	};

	// this.findIndexOfNode = function(node) {
	// 	var children = this.protocolForms[0]['children'];
	// 	for (var item in children) {
	// 		if (node.href.split(',')[1]==children[item].href.split(',')[1]) {
	// 			this.selectedProtocolForm = children[item]
	// 		};
	// 	};
	// };

});