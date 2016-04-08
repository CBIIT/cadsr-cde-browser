angular.module("cdeBrowserApp").service('filterService', function($resource) {
	// define variables //
	this.serverData = []; // initial data from server goes here
	this.searchFilter = {};
	this.isAChildNodeSearch = false;
	this.isLeftTreeClick = false; // temporarily set to true when left nav is hit so the watch function doesn't search //
	this.classifications = []; // classification array for context //
	this.protocolForms = []; // protocol form array for context //	
	this.currentContext = []; // temp method, delete after rest services are fixec //
	this.showClassificationsProtocolForms = 0; // when 1 show the classification and protocol form dropdowns //

	// resets all important variables
	this.resetFilters = function() {
        this.searchFilter = {programArea:0}
		this.isAChildNodeSearch = false;	
		this.classifications = [];
		this.protocolForms = [];
		this.showClassificationsProtocolForms = 0; // hide protocol forms and classification dropdowns //
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

	this.resetContext = function() {
		if (this.searchFilter.programArea!=0) {
			if (Object.keys(this.searchFilter).indexOf('contextId')>-1) {
				delete(this.searchFilter.contextId)
			}
		}
	};

	// returns classificiations and protocol forms //
	this.getClassificationsAndProtocolForms = function() {
		this.classifications = []; this.protocolForms = [];
		this.showClassificationsProtocolForms=1; // show classifications and protocolforms dropdowns //
		var that = this;
		var classifications = $resource('/cdebrowserServer/rest/oneContextData?contextId='.concat(this.searchFilter.contextId,"&programArea=",this.searchFilter.programArea,"&folderType=0")).query();
		var protocolForms = $resource('/cdebrowserServer/rest/oneContextData?contextId='.concat(this.searchFilter.contextId,"&programArea=",this.searchFilter.programArea,"&folderType=1")).query();
		classifications.$promise.then(function(response) {
			that.classifications = response;
		});
		protocolForms.$promise.then(function(response) {
			that.protocolForms = response;
		});
	};	

	// select context dropdown based on context click in left menu //
	this.selectContextByNode = function(programArea,id) {
		this.searchFilter.programArea = programArea; // user clicked the left menu. set program area //
		var programAreaContexts = this.serverData[this.searchFilter.programArea].children;
		for (var item in programAreaContexts) {
			if (programAreaContexts[item].idSeq == id) {
				this.searchFilter.contextId = id; 
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
				this.selectConte3xtByNode(node.programArea, currentProgramAreaChildren[x].idSeq)
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

});