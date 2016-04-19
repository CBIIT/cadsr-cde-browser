angular.module("cdeBrowserApp").service('cartService', function($sessionStorage,$http, $location,authenticationService) {
	// service to create and operate a cde cart //
	// check session to see if cart service exists, if so set variables to the session values //
	var authService = authenticationService; // create instance of auth service //
	this.statusMessage = ''; // status message for alerting the user what is happening when user clicks on buttons //
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
		var url = '/cdebrowserServer/rest/cdeCart'; // url for server download //
		var c = 0; // keep track of index of checked cart items index //		
		var that = this;
		this.statusMessage = 'Deleting Items';
		var deleteItems = function() { // function to delete items from local cart, called on both success and failure of cdeCart service call //
			for (var i = that.cartData.length - 1; i >= 0; i--) { 
				if (that.checkedCartItems.items[that.cartData[i].deIdseq]) {
					that.cartData.splice(i,1);
				};
			};
			that.checkedCartItems.selected=false;
			that.checkedCartItems.items={};				
		};

		// create url for delete call //
		for (var i=0; i<this.cartData.length; i++) {
			if (this.checkedCartItems.items[this.cartData[i].deIdseq]) {
				if (this.cartData[i].unsavedItem==false) {
					if (c==0) {
						url+='?id='+this.cartData[i].deIdseq;
					}
					else {
						url+='&id='+this.cartData[i].deIdseq;
					};					
					c++;
				};
			};
		};	

		$http({method: 'DELETE',url:url})
			.success(function(response) { 
				deleteItems();
				that.statusMessage = '';
			})
			.error(function(response) { 
				deleteItems();
				that.statusMessage = '';				
		});
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
		var that = this;
		this.itemsForSave = [];
		this.statusMessage = 'Saving Cart';
		for (var i=0; i<this.cartData.length; i++) {
			if (this.cartData[i]['unsavedItem']==true) {
				this.itemsForSave.push(this.cartData[i].deIdseq);
			};
		};

		if (this.itemsForSave.length) { //only make call if there are unsaved items //
			$http({method: 'POST',url:'/cdebrowserServer/rest/cdeCart', data:this.itemsForSave}).success(function(response) {
				for (var i=0; i<that.cartData.length; i++) {
					that.cartData[i]['unsavedItem'] = false;
				};
				that.statusMessage = '';
			})
			.error(function(response) {
				authService.cameFrom = 'save';
		        $location.path("/login").replace(); // send user to login page //
				that.statusMessage = '';
			});			
		};
	};

	// retrieve the cart. Will call rest service //
	this.retrieveCart = function() {
		var that = this;
		this.statusMessage = 'Retrieving Cart';
		$http.get('/cdebrowserServer/rest/cdeCart')
		.success(function(response) {
			var temporaryIds = []; // array of temp ids to compare with retrieved cart items. Prevent looping through two arrays //
			for (var i=0; i<that.cartData.length;i++) {
				temporaryIds.push(that.cartData[i].deIdseq);
			};

			if (response.length) {
				for (var i=0; i<response.length;i++) {
					var index = temporaryIds.indexOf(response[i].deIdseq);
					if (index > -1) {
						that.cartData[index].unsavedItem=false;
					}
					else {
						response[i].unsavedItem = false;
						that.cartData.push(response[i])
					}
				};				
			};
			that.statusMessage = '';
		}).error(function(response) {
			authService.cameFrom = 'retrieve';
	        $location.path("/login").replace(); // send user to login page //
			that.statusMessage = '';
		});
	};		
		
});