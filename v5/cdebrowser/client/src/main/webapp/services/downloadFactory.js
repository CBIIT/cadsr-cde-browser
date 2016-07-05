angular.module("cdeBrowserApp").factory('downloadFactory', function($http) {
    var downloadFactory = function() {
		// service to download files //
		this.progressMessage = {"status":0,"message":"Exporting Data", "isErrorMessage":0}; // download status message //

	    // downloads selected items to an excel file //
	    this.downloadToExcel = function(param, items) {
	    	this.progressMessage = {"status":1,"message":"Exporting Data", "isErrorMessage":0};
	    	var that = this;

	        if (param) { // download to prior excel
	            $http({method: 'POST', url: '/cdebrowserServer/rest/downloadExcel?src=deSearchPrior',data: items}).
	            success(function(data, status, headers, config) {
	            	that.progressMessage.status=0;
	                window.location.href = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/downloadExcel/" + data;
	            }).
	            error(function(data, status, headers, config) {
	            	that.progressMessage = '';
	            });
	        }
	        else { // download to excel
	            $http({method: 'POST', url: '/cdebrowserServer/rest/downloadExcel?src=deSearch',data: items}).
	            success(function(data, status, headers, config) {
	            	that.progressMessage.status=0;
	                window.location.href = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/downloadExcel/" + data;
	            }).
	            error(function(data, status, headers, config) {
	            	that.progressMessage = {"status":1,"message":data,"isErrorMessage":1};
	            });
	        }

	    };



	    // download to excel from compare screen
	     this.excelDownload = function(param, items) {
	     	this.progressMessage = {"status":1,"message":"Exporting Data", "isErrorMessage":0};
	     	var that = this;

	            $http({method: 'POST', url: '/cdebrowserServer/rest/downloadExcel?src=deSearch',data: items}).
	            success(function(data, status, headers, config) {
	           	that.progressMessage.status=0;
	                 window.location.href = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/downloadExcel/" + data;
	             }).
	             error(function(data, status, headers, config) {
	             	that.progressMessage = {"status":1,"message":data,"isErrorMessage":1};
	             });

	     };



	    // downloads selected items to an excel file //
	    this.downloadToXML = function(items) {
	    	this.progressMessage = {"status":1,"message":"Exporting Data", "isErrorMessage":0};
	    	var that = this;
	        $http({method: 'POST', url: '/cdebrowserServer/rest/downloadXml?src=deSearch',data: items}).
	        success(function(data) {
	        	that.progressMessage.status=0;
	            window.location.href = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/cdebrowserServer/rest/downloadXml/" + data;
	        }).
	        error(function(data, status, headers, config) {
	        	that.progressMessage = '';
	        });
	    };	

	    // creates array of deIdseq numbers for download as an array //
	    this.createDownloadableArray = function(items) {
	    	debugger;
	    	console.log(items);
	    	var itemArray = [];
	    	for (var x in items) {
	    		itemArray.push(x)
	    	};
	    	return itemArray;
	    };
    };
    return downloadFactory;
});


