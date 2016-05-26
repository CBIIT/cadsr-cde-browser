angular.module("cdeBrowserApp").service('dataTransferService', function() {
	var arr = {};
	var setData = function(key,value){
		arr[key] = value;
	};
	var getData = function(key){
		return arr[key];
	};

	return{
		setData:setData,
		getData:getData
	};
});