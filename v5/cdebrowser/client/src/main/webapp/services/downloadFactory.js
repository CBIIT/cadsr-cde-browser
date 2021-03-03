angular.module("cdeBrowserApp").factory('downloadFactory', function($http, filterService) {
    var downloadFactory = function() {
		// service to download files //
		this.progressMessage = {"status":0,"message":"Exporting Data", "isErrorMessage":0}; // download status message //

	    // downloads selected items to an excel file //
	    this.downloadToExcel = function(param, items) {
	    	this.progressMessage = {"status":1,"message":"Exporting Data", "isErrorMessage":0};
	    	var that = this;

	        if (param) { // download to prior excel
	            $http({method: 'POST', url: '/cdebrowserServer/rest/downloadExcel?src=deSearchPrior',data: items}).
	            then(function(data, status, headers, config) {
					data=data['data'];
	            	that.progressMessage.status=0;
	                window.location.href = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/downloadExcel/" + data;
	            }).
	            catch(function(data, status, headers, config) {
	            	that.progressMessage = '';
	            });
	        }
	        else { // download to excel
	            $http({method: 'POST', url: '/cdebrowserServer/rest/downloadExcel?src=deSearch',data: items}).
	            then(function(data, status, headers, config) {
					data=data['data'];
	            	that.progressMessage.status=0;
	                window.location.href = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/downloadExcel/" + data;
	            }).
	            catch(function(data, status, headers, config) {
	            	that.progressMessage = {"status":1,"message":data,"isErrorMessage":1};
	            });
	        }

	    };

	    // excel download from compare screen //
	     this.excelDownload = function(param, items) {
	     	this.progressMessage = {"status":1,"message":"Exporting Data", "isErrorMessage":0};
	     	var that = this;

	            $http({method: 'POST', url: '/cdebrowserServer/rest/downloadCdeCompare',data: items}).
	            then(function(data, status, headers, config) {
				data=data['data'];
	           	that.progressMessage.status=0;
	                 window.location.href = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/downloadCdeCompare/" + data;
	             }).
	             catch(function(data, status, headers, config) {
	             	that.progressMessage = {"status":1,"message":data,"isErrorMessage":1};
	             });

	     };

	    // downloads selected items to an excel file //
	    this.downloadToXML = function(items) {
	    	this.progressMessage = {"status":1,"message":"Exporting Data", "isErrorMessage":0};
	    	var that = this;
	        $http({method: 'POST', url: '/cdebrowserServer/rest/downloadXml?src=deSearch',data: items}).
	        then(function(data) {
				data=data['data'];
	        	that.progressMessage.status=0;
	            window.location.href = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/downloadXml/" + data;
	        }).
	        catch(function(data, status, headers, config) {
	        	that.progressMessage = '';
	        });
	    };	

	    // creates array of deIdseq numbers for download as an array //
	    this.createDownloadableArray = function(items) {
	    	var itemArray = [];
	    	for (var x in items) {
	    		itemArray.push(x)
	    	};
	    	return itemArray;
	    };


	    // checks if template exists //
	    this.getrdIdseq = function(protocol) {
	    	var that = this;

            $http({method: 'GET', url: '/cdebrowserServer/rest/downloadTemplate/refdocid/'+protocol.formIdSeq}).
            then(function(data, status, headers, config) {
				data=data['data'];
            	that.progressMessage.status = 0;
		    	protocol['rdIdseq'] = data;
            }).
            catch(function(data, status, headers, config) {
            	if (status!=404) {
			    	that.progressMessage = {"status":1,"message":data, "isErrorMessage":1};
            	}
            	else {
	            	that.progressMessage.status=0;
            	}
            });	    	
	    };	

	    // dowmloads template for protocol form //
	    this.downloadTemplate = function(id) {
	    	this.progressMessage = {"status":1,"message":"Downloading Template", "isErrorMessage":0};
	    	var that = this;

	            $http({method: 'GET', url: '/cdebrowserServer/rest/downloadTemplate/doc/'+id}).
	            then(function(data, status, headers, config) {
					data=data['data'];
	            	that.progressMessage.status=0;
	                window.location.href = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/downloadTemplate/doc/" + id;
	            }).
	            catch(function(data, status, headers, config) {
			    	that.progressMessage = {"status":1,"message":data, "isErrorMessage":1};
	            });

	    };		        
    };
    return downloadFactory;
});


