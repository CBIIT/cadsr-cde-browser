angular.module("cdeBrowserApp").service('filterService', function($resource,$injector,$filter) {
    // define variables //
    this.serverData = []; // initial data from server goes here //
    this.lookupData = {}; // stores master list of classifications and protocol forms //

    this.dataElementVariables = {selectedQueryType:"2",basicSearchQuery:"",searchDEC:"",searchPV:"",searchPVQueryType:"0",searchVD:"",searchVDTQueryType:"2", searchFieldOptions:{options:["ALL Fields"]}, conceptInput:"", conceptQueryType:"0",
        searchAltName:"", searchAltNameType:[], searchVersions:"0", publicSearchVersions:"0", searchContextUse:"2", searchObjectClass:"", searchProperty:"", derivedDE:"false"}

    this.searchFilter = {programArea:0};
    this.isAChildNodeSearch = false;
    this.isLeftTreeClick = false; // temporarily set to true when left nav is hit so the watch function doesn't search //
    this.classifications = []; // classification array for context //
    this.protocols = []; // protocol form array for context //
    this.currentContext = []; // current selected context and its children //
    this.showClassificationsProtocolForms = 0; // when 1 show the classification and protocol form dropdowns //
    this.isSearching = false; // is true if search is in progress. Disable all input fields //

    // resets all important variables
    this.resetFilters = function() {
        this.isAChildNodeSearch = false;
        this.resetElementsInCommon();
        this.dataElementVariables = {selectedQueryType:"2",basicSearchQuery:"",searchDEC:"",searchPV:"",searchPVQueryType:"0",searchVD:"",searchVDTQueryType:"2", searchFieldOptions:{options:["ALL Fields"]}, conceptInput:"", conceptQueryType:"0",
            searchAltName:"", searchAltNameType:["ALL Alternate Name Types"], searchVersions:"0", publicSearchVersions:"0", searchContextUse:"2", searchObjectClass:"", searchProperty:"", derivedDE:"false"}
        // this.classifications = [];
        // this.protocols = [];
        // this.showClassificationsProtocolForms = 0; // hide protocol forms and classification dropdowns //
    };

    this.resetDataElementSearch = function() {
        this.resetElementsInCommon();

    }

    this.resetElementsInCommon = function() {
        this.searchFilter = {programArea:0,workFlowStatus:["ALL Workflow Statuses"],registrationStatus:["ALL Registration Statuses"]}// reset search filter to just having program area selected to all //
        // this.classifications = [];
        // this.protocols = [];
        this.resetClassificationAndProtocol();
        this.showClassificationsProtocolForms = 0;
    }

    this.resetClassificationAndProtocol = function() {
        this.classifications = [];
        this.protocols = [];

        // CHECKME MHL  JIRA 737 & 682
        delete this.searchFilter.classification;
        delete this.searchFilter.protocol;
    }

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

    // select context dropdown based on context click in left menu //
    this.selectContextByNode = function(programArea,id) {
        this.searchFilter = {programArea:programArea,workFlowStatus:["ALL Workflow Statuses"],registrationStatus:["ALL Registration Statuses"]}; // user clicked the left menu. set program area //
        this.searchFilter.context = id;
        // this.getClassificationsAndProtocolForms(); // get classifications and protocol forms //
    };

    this.setClassifications = function(cs){
        this.myclassifications = cs;
        // console.log(this.myclassifications);
    }

    // selects dropdown values based on search left tree click //
    this.selectFiltersByNode = function(searchType,id, selectedNode, programArea) {
        this.isAChildNodeSearch = false;

        this.dataElementVariables = {selectedQueryType:"2",basicSearchQuery:"",searchDEC:"",searchPV:"",searchPVQueryType:"0",searchVD:"",searchVDTQueryType:"2", searchFieldOptions:{options:["ALL Fields"]}, conceptInput:"", conceptQueryType:"0",
            searchAltName:"", searchAltNameType:["ALL Alternate Name Types"], searchVersions:"0", publicSearchVersions:"0", searchContextUse:"2", searchObjectClass:"", searchProperty:"", derivedDE:"false"};

        this.searchFilter.programArea = programArea;
        this.isLeftTreeClick = true;
        if (searchType=='contextId') {
            this.selectContextByNode(this.searchFilter.programArea,id);
        }
        else {
            this.selectContextByNode(this.searchFilter.programArea,selectedNode.contextIdSeq);
            this.isAChildNodeSearch = true;
            if (searchType=='protocolId'||searchType=='id') { // if protocol set protocol for dropdown //
                this.searchFilter['protocol']=selectedNode;
            }
            else { // if classification scheme item or classification scheme item id set classification for dropdown //
                this.searchFilter['classification'] = selectedNode;
            };
        };
    };

    // creates breadcrumbs when using dropdowns //
    this.createBreadcrumbs = function() {
        var programArea = this.serverData[this.searchFilter.programArea];
        var breadcrumbs = programArea.treePath;
        if (this.searchFilter.context && this.searchFilter.context!="") {
            var contexts = programArea.children;
            for (var context=0; context<contexts.length;context++) {
                if (this.searchFilter.context==contexts[context].idSeq) {
                    breadcrumbs = angular.copy(contexts[context].treePath);
                };
            };
        };
        if (this.searchFilter.classification) {

            breadcrumbs.push("Classification");
            if(this.searchFilter.classification.name==this.searchFilter.classification.csLongName)
                breadcrumbs.push(this.searchFilter.classification.csLongName);
            else if(this.searchFilter.classification.name==this.searchFilter.classification.csCsiName){
                breadcrumbs.push(this.searchFilter.classification.csLongName);

                if(this.searchFilter.classification.csiLevel==2) {
                    var data=$filter('filter')(this.myclassifications,{"csCsiIdSeq":this.searchFilter.classification.parentCsiIdSeq,
                        csiLevel:1});
                    breadcrumbs.push(data[0].csCsiName);
                }
                breadcrumbs.push(this.searchFilter.classification.csCsiName);
            }else if(this.searchFilter.classification.csiLevel==2) {
                breadcrumbs.push(this.searchFilter.classification.csLongName);
                breadcrumbs.push(this.searchFilter.classification.name);
            }
        }

        // else
        if (this.searchFilter.protocol) {
            breadcrumbs.push("ProtocolForms");
            if(this.searchFilter.protocol.name==this.searchFilter.protocol.protocolLongName)
                breadcrumbs.push(this.searchFilter.protocol.protocolLongName);
            else if(this.searchFilter.protocol.name==this.searchFilter.protocol.formLongName){
                breadcrumbs.push(this.searchFilter.protocol.protocolLongName);
                breadcrumbs.push(this.searchFilter.protocol.name);
            }
        };
        return breadcrumbs
    };  

    // sets search filter variables based on url parameters //
    this.setVariablesFromURLParameters = function(parameters) {
        var keys = Object.keys(parameters);

        if (keys.indexOf('contextId') > -1) {
            this.searchFilter.context = parameters.contextId;

            if (keys.indexOf('programArea') > -1) {
                var parsedProgramArea = parseInt(parameters.programArea);
                if (parsedProgramArea) {
                    var programArea = $filter('filter')(this.serverData,parameters.contextId).pop().programArea
                    this.searchFilter.programArea = programArea;
                };
            };            
        };    

        if (keys.indexOf('classificationSchemeId') > -1) {
            this.searchFilter.classification = this.selectClassificationById(parameters.classificationSchemeId);
        };  

        if (keys.indexOf('classificationSchemeItemId') > -1) {
            this.searchFilter.classification = this.selectClassificationById(parameters.classificationSchemeItemId);
        };  
    };

    // gets actual context object using id. useful for parameters //
    this.selectContextNodeById = function(id) {
        var contexts = this.serverData[this.searchFilter.programArea].children;
        for (var x = 0; x < contexts.length; x++) {
            if (contexts[x].idSeq == id) {
                return contexts[x];
            };
        };
    };

    // gets actual classification object using id. useful for parameters //
    this.selectClassificationById = function(id) {
        for (var x = 0; x < this.classifications.length; x++) {
          if (id == this.classifications[x].id) {
            return this.classifications[x];
          };
        };     
    };


});
