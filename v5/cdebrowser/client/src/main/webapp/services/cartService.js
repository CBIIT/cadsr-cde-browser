angular.module("cdeBrowserApp").service('cartService', function() {
	// service to create and operate a cde cart //
	this.cartData = [];

	// delete cde's from cart //
	this.deleteCDE = function(ids) {
		this.cartData.pop();
	};

	// add cde's to cart. checkedItems is just an array of ids hence the need for searchResults //
	this.addCDE = function(checkedItems, searchResults) {
		for (var item in searchResults) {
			if (checkedItems.indexOf(searchResults[item]['deIdseq'])>=0) {
				if (this.cartData.indexOf(searchResults[item])<0) {
					this.cartData.push(searchResults[item]); 
				};
			};
		};
	};	

	// save the cart //
	this.saveCart = function() {
		console.log(this.cartData);
	};

	// retrieve the cart. Will call rest service //
	this.retrieveCart = function() {
		return this.cartData;
	};		
		
});