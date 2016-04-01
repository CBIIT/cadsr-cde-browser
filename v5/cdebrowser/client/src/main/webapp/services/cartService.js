angular.module("cdeBrowserApp").service('cartService', function($sessionStorage) {
	// service to create and operate a cde cart //

	if (!$sessionStorage['cartService']) {
		this.cartData = []; // all items in the cart //
		this.checkedCartItems = {"items":{}}; // stores all items that are checked for deletion //
		this.itemsForSave = [];
	}
	else {
		this.cartData = $sessionStorage.cartService.cartData;
		this.checkedCartItems = $sessionStorage.cartService.checkedCartItems;
		this.itemsForSave = $sessionStorage.cartService.itemsForSave;
	};

	// add cde's to cart. checkedItems is just an array of ids hence the need for searchResults //
	this.addCDE = function(checkedItems, searchResults) {
		for (var item in searchResults) {
			if (checkedItems.indexOf(searchResults[item]['deIdseq'])>=0) {
				if (this.cartData.indexOf(searchResults[item])<0) {
					var cartItem = searchResults[item];
					cartItem['unsavedItem'] = true;
					this.cartData.push(cartItem); 
				};
			};
		};
	};	

	// delete cde's from cart //
	this.deleteCDEs = function() {
		var arrayOfKeys = Object.keys(this.checkedCartItems.items);
		var i = 0;
		for (var i = this.cartData.length - 1; i >= 0; i--) {
			if (arrayOfKeys.indexOf(this.cartData[i].deIdseq) > -1) {
				delete(this.checkedCartItems.items[this.cartData[i].deIdseq])
				this.cartData.splice(i,1);
			};
		};
		if (!Object.keys(this.checkedCartItems.items).length) {
			this.checkedCartItems.selected = false;
		};
	};

	// selects all or de-selects all items in the cart. used for deleting only at this point //
	this.selectAll = function() {
		for (var item in this.cartData) {
			if (this.checkedCartItems.selected) {				
				this.checkedCartItems.items[this.cartData[item].deIdseq] = true;
			}
			else {
				delete(this.checkedCartItems.items[this.cartData[item].deIdseq])
			};
		};

	};

	// used when checkbox is de-selected //
	this.checkCartItem = function(id) {
		// remove item from object, makes it easier when sending to server //
		if (this.checkedCartItems.items[id]==false) {
			delete(this.checkedCartItems.items[id])
		};		

		// check if current selection makes the entire cart selected or not //
		if (Object.keys(this.checkedCartItems.items).length==this.cartData.length) {
			this.checkedCartItems.selected = true;
		}
		else {
			this.checkedCartItems.selected = false;
		};
	};

	// save the cart //
	this.saveCart = function() {
		this.itemsForSave = [];
		for (var i in this.cartData) {
			if (this.cartData[i]['unsavedItem']==true) {
				this.itemsForSave.push(this.cartData[i].deIdseq);
				this.cartData[i]['unsavedItem'] = false;
			};

		};
	};

	// retrieve the cart. Will call rest service //
	this.retrieveCart = function() {
		return this.cartData;
	};		
		
});