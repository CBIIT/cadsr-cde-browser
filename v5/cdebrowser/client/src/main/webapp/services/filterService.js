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
		this.searchFilter.context = id; 
		this.getClassificationsAndProtocolForms(); // get classifications and protocol forms //
	};

    // selects dropdown values based on search left tree click //
    this.selectFiltersByNode = function(searchType,id, selectedNode) {
        this.isAChildNodeSearch = false;
        this.isLeftTreeClick = true;
        if (searchType=='contextId') {
            this.selectContextByNode(this.searchFilter.programArea,id);
        }
        else {
            this.selectContextByNode(this.searchFilter.programArea,selectedNode.contextId);        	
            this.isAChildNodeSearch = true;
            if (searchType=='protocolId') { // if protocol set protocol for dropdown //
            	this.searchFilter['protocol']=selectedNode.parentId;
            }
            else { // if classification scheme item or classification scheme item id set classification for dropdown //
            	this.searchFilter['classification']=selectedNode.parentId;
            };
        };
    };

    // creates breadcrumbs when using dropdowns //
    this.createBreadcrumbs = function() {
    	var programArea = this.serverData[this.searchFilter.programArea];
		var breadcrumbs = programArea.treePath;
		if (this.searchFilter.context) {
		 var contexts = programArea.children;
		 for (var context in contexts) {
		   if (this.searchFilter.context==contexts[context].idSeq) {
			breadcrumbs = angular.copy(contexts[context].treePath);
		   };
		 };
		};

		if (this.searchFilter.classification) {
			for (var i in this.classifications) {
				if (this.classifications[i].csIdSeq==this.searchFilter.classification) {
					breadcrumbs.push(this.classifications[i].csLongName)
				};
			};
		};

		if (this.searchFilter.protocol) {
			for (var i in this.protocols) {
				if (this.protocols[i].protocolIdSeq==this.searchFilter.protocol) {
					breadcrumbs.push(this.protocols[i].protocolLongName)
				};
			};
		};
		return breadcrumbs
    };


});