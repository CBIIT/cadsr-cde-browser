angular.module("cdeBrowserApp").service('filterService', function($resource,$injector) {
	//var groupFactory1=$injector.get("groupFactory1");
	// define variables //
	this.serverData = []; // initial data from server goes here //
	this.lookupData = {}; // stores master list of classifications and protocol forms //
	this.dataElementVariables = {selectedQueryType:"0",basicSearchQuery:"",searchDEC:"",searchPV:"",searchPVQueryType:"0",searchAltName:"", searchAltNameTypes:"0"}
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
		this.dataElementVariables = {selectedQueryType:"0",basicSearchQuery:"",searchDEC:"",searchPV:"",searchPVQueryType:"0",searchAltName:"", searchAltNameTypes:"0"}
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
	// this.getClassificationsAndProtocolForms = function() {
	// 	this.classifications = []; this.protocols = []; // reset classifications and protocol dropdowns //
	// 	delete(this.searchFilter.classification);  delete(this.searchFilter.protocol); // remove any values from searchFilter //
	// 	if (this.searchFilter.context) { // get classifications and protocols for selected context //
	// 		for (var classification=0; classification<this.lookupData.classifications.length; classification++) { // get classifications for context //
	// 		  if (this.lookupData.classifications[classification].contextIdSeq==this.searchFilter.context) {
	// 		  	this.classifications.push(this.lookupData.classifications[classification])
	// 		  };
	// 		};

	// 		for (var protocol=0; protocol< this.lookupData.protocols.length; protocol++) { // get protocols for context //
	// 		  if (this.lookupData.protocols[protocol].contextIdSeq==this.searchFilter.context) {
	// 		  	this.protocols.push(this.lookupData.protocols[protocol])
	// 		  };
	// 		};
	// 	};
	// };

	// select context dropdown based on context click in left menu //
	this.selectContextByNode = function(programArea,id) {
		this.searchFilter = {programArea:programArea}; // user clicked the left menu. set program area //
		this.searchFilter.context = id;
		// this.getClassificationsAndProtocolForms(); // get classifications and protocol forms //
	};

    // selects dropdown values based on search left tree click //
    this.selectFiltersByNode = function(searchType,id, selectedNode, programArea) {
        this.isAChildNodeSearch = false;
        this.dataElementVariables = {selectedQueryType:"0",basicSearchQuery:"",searchDEC:"",searchPV:"",searchPVQueryType:"0",searchAltName:"", searchAltNameTypes:"0"};
        this.searchFilter.programArea = programArea;
        this.isLeftTreeClick = true;
        if (searchType=='contextId') {
            this.selectContextByNode(this.searchFilter.programArea,id);
        }
        else {
            this.selectContextByNode(this.searchFilter.programArea,selectedNode.contextId);
            this.isAChildNodeSearch = true;
            if (searchType=='protocolId'||searchType=='id') { // if protocol set protocol for dropdown //
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
		 for (var context=0; context<contexts.length;context++) {
		   if (this.searchFilter.context==contexts[context].idSeq) {
			breadcrumbs = angular.copy(contexts[context].treePath);
		   };
		 };
		};

		if (this.searchFilter.classification) {
			// for (var i=0; i<this.classifications.length;i++) {
				// if (this.classifications[i].csIdSeq==this.searchFilter.classification) {
				// if (this.searchFilter.classification) {
					if(this.searchFilter.classification.name==this.searchFilter.classification.csLongName)
					breadcrumbs.push(this.searchFilter.classification.csLongName);
				else if(this.searchFilter.classification.name==this.searchFilter.classification.csCsiName){
					breadcrumbs.push(this.searchFilter.classification.csLongName);
					breadcrumbs.push(this.searchFilter.classification.csCsiName);
				}else if(this.searchFilter.classification.csiLevel==2){
						breadcrumbs.push(this.searchFilter.classification.csLongName);
						//console.log(_.find(groupFactory1.getNameFromId(this.searchFilter.classification.csIdSeq)));
						//breadcrumbs.push( _.find(groupfactory1.getNameFromId(this.searchFilter.classification.csIdSeq))

						// breadcrumbs.push(this.searchFilter.classification.csCsiName);
						// breadcrumbs.push(this.searchFilter.classification.parentCsiIdSeq);

						breadcrumbs.push(this.searchFilter.classification.name);

				}

					// if (this.searchFilter.classification.csLongName) {
					// 	breadcrumbs.push(this.searchFilter.classification.csLongName.csCsiName)
					// };

				// };
			// };
		}

		// else
		if (this.searchFilter.protocol) {
			if(this.searchFilter.protocol.name==this.searchFilter.protocol.protocolLongName)
					breadcrumbs.push(this.searchFilter.protocol.protocolLongName);
				else if(this.searchFilter.protocol.name==this.searchFilter.protocol.formLongName){
					breadcrumbs.push(this.searchFilter.protocol.protocolLongName);
					breadcrumbs.push(this.searchFilter.protocol.name);
				}
			// for (var i=0; i<this.protocols.length;i++) {
				// if (this.protocols[i].protocolIdSeq==this.searchFilter.protocol) {
					//breadcrumbs.push(this.searchFilter.protocol.protocolLongName)
				// };
			// };
		};

		// if (this.searchFilter.classification.csLongName) {
		// 			breadcrumbs.push(this.searchFilter.classification.csCsiName)
		// }

		// else if (this.searchFilter.protocol.protocolLongName) {
		// 			breadcrumbs.push(this.searchFilter.protocol.formLongName)
		// };

		// console.log(breadcrumbs);
		return breadcrumbs
    };


});
