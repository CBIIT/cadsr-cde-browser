angular.module("cdeBrowserApp").service('cartService', function($sessionStorage,$http, $location,authenticationService) {
	// service to create and operate a cde cart //
	// check session to see if cart service exists, if so set variables to the session values //
	var authService = authenticationService; // create instance of auth service //

	if (!$sessionStorage['cartService']) {
		this.cartData = []; // all items in the cart //
		this.checkedCartItems = {"items":{}}; // stores all items that are checked for deletion //
		this.itemsForSave = [];
		this.savedData = [];
	}
	else {
		this.cartData = $sessionStorage.cartService.cartData;
		this.checkedCartItems = $sessionStorage.cartService.checkedCartItems;
		this.itemsForSave = $sessionStorage.cartService.itemsForSave;
	};

	// add cde's to cart. checkedItems is just an array of ids hence the need for searchResults //
	this.addCDE = function(checkedItems, searchResults) {
		for (var i=0; i<searchResults.length; i++) {
			if (checkedItems.indexOf(searchResults[i]['deIdseq'])>=0) {
				if (this.cartData.indexOf(searchResults[i])<0) {
					var cartItem = searchResults[i];
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
		for (var i=0; i<this.cartData.length; i++) {
			if (this.checkedCartItems.selected) {				
				this.checkedCartItems.items[this.cartData[i].deIdseq] = true;
			}
			else {
				delete(this.checkedCartItems.items[this.cartData[i].deIdseq])
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
		$http({method: 'POST',url:'/cdebrowserServer/rest/cdeCart', data:this.itemsForSave}).success(function(response) {
			for (var i=0; i<this.cartData.length; i++) {
				if (this.cartData[i]['unsavedItem']==true) {
					this.itemsForSave.push(this.cartData[i].deIdseq);
					this.cartData[i]['unsavedItem'] = false;
				};
			};
		})
		.error(function(response) {
			authService.cameFrom = 'save';
	        $location.path("/login").replace(); // send user to login page //
		});
	};

	// retrieve the cart. Will call rest service //
	this.retrieveCart = function() {
		var that = this;
		$http.get('/cdebrowserServer/rest/cdeCart').success(function(response) {
			var temporaryIds = []; // array of temp ids to compare with retrieved cart items. Prevent looping through two arrays //
			for (var i=0; i<that.cartData.length;i++) {
				temporaryIds.push(that.cartData[i].publicId);
			};

			if (response.length) {
				for (var i=0; i<response.length;i++) {
					var index = temporaryIds.indexOf(response[i].publicId);
					if (index > -1) {
						that.cartData[index].unsavedItem=false;
					}
					else {
						response[i].unsavedItem = false;
						that.cartData.push(response[i])
					}
				};				
			};
		}).error(function(response) {
			authService.cameFrom = 'retrieve';
	        $location.path("/login").replace(); // send user to login page //

		});

	
	};		
		
});