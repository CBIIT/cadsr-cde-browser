angular.module("cdeBrowserApp").service('filterService', function($resource) {
	// define variables //
	this.serverData = []; // initial data from server goes here //
	this.lookupData = {}; // stores master list of classifications and protocol forms //
	this.dataElementVariables = {selectedQueryType:"0",basicSearchQuery:""}
	this.searchFilter = {};
	this.isAChildNodeSearch = false;
	this.isLeftTreeClick = false; // temporarily set to true when left nav is hit so the watch function doesn't search //
	this.classifications = []; // classification array for context //
	this.protocols = []; // protocol form array for context //	
	this.currentContext = []; // current selected context and its children //
	this.showClassificationsProtocolForms = 0; // when 1 show the classification and protocol form dropdowns //
	this.isSearching = false; // is true if search is in progress. Disable all input fields //

	// resets all important variables
	this.resetFilters = function() {
        this.searchFilter = {programArea:0} // reset search filter to just having program area selected to all //
		this.isAChildNodeSearch = false; 
		this.dataElementVariables = {selectedQueryType:"0",basicSearchQuery:""}
		this.classifications = [];
		this.protocols = [];
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

	// returns classificiations and protocol forms //
	this.getClassificationsAndProtocolForms = function() {
		this.classifications = []; this.protocols = []; // reset classifications and protocol dropdowns //
		delete(this.searchFilter.classification);  delete(this.searchFilter.protocol); // remove any values from searchFilter //
		if (this.searchFilter.context) { // get classifications and protocols for selected context //
			for (var classification in this.lookupData.classifications) { // get classifications for context //
			  if (this.lookupData.classifications[classification].contextIdSeq==this.searchFilter.context) {
			  	this.classifications.push(this.lookupData.classifications[classification])
			  };
			};

			for (var protocol in this.lookupData.protocols) { // get protocols for context //
			  if (this.lookupData.protocols[protocol].contextIdSeq==this.searchFilter.context) {
			  	this.protocols.push(this.lookupData.protocols[protocol])
			  };
			};			
		};
	};	

	// select context dropdown based on context click in left menu //
	this.selectContextByNode = function(programArea,id) {
		this.searchFilter = {programArea:programArea}; // user clicked the left menu. set program area //
		var programAreaContexts = this.serverData[this.searchFilter.programArea].children;
		for (var item in programAreaContexts) {
			if (programAreaContexts[item].idSeq == id) {
				this.searchFilter.context = id; 
				this.getClassificationsAndProtocolForms(); // get classifications and protocol forms //
			};
		};
	};

	// get selected context by contextId //
	this.getContextByContextId = function(node) {
		var currentProgramAreaChildren = this.serverData[node.programArea].children;
		for (var x in currentProgramAreaChildren) {
			if (currentProgramAreaChildren[x].idSeq==node.contextId) {
				this.selectContextByNode(node.programArea, node.contextId)
				this.currentContext = currentProgramAreaChildren[x];
				break
			};
		};
		return this.currentContext;
	};

	// gets classificationSchemeId when selecting a classificationSchemeItemId //
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

    // selects dropdown values based on search left tree click //
    this.selectFiltersByNode = function(searchType,id, selectedNode) {
        this.isAChildNodeSearch = false;
        this.isLeftTreeClick = true;
        if (searchType=='contextId') {
            this.selectContextByNode(this.searchFilter.programArea,id);
        }
        else {
            this.isAChildNodeSearch = true;
            var currentContext = this.getContextByContextId(selectedNode);
            if (searchType=='classificationSchemeItemId') {
                this.selectedClassification = this.getClassifficationOrProtocolByName(currentContext,angular.copy(selectedNode));
            }
            else if (searchType=='classificationSchemeId') {
                this.selectedClassification = angular.copy(selectedNode);
            }
            else {
                this.selectedProtocolForm = angular.copy(selectedNode);
            };
        };
    };

});