angular.module("cdeBrowserApp").service('cartService', function() {
	// service to create and operate a cde cart //
	this.cartData = [];

	this.deleteCDE = function(id) {
		this.cartData.pop();
	};

	this.addCDE = function(checkedItems, searchResults) {
		for (var item in searchResults) {
			if (checkedItems.indexOf(searchResults[item]['deIdseq'])>=0) {
				if (this.cartData.indexOf(searchResults[item])<0) {
					this.cartData.push(searchResults[item]); 
				};
			};
		};
	};	

	this.saveCart = function() {
		console.log(this.cartData);
	};

	this.retrieveCart = function() {
		return this.cartData;
	};		
		
});