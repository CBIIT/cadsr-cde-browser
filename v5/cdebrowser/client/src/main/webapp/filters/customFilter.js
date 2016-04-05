angular.module("cdeBrowserApp")
.filter('customFilter', function () {
  return function (items,searchFilter) {
    var filteredItems = [];
    if (searchFilter) {
      for (var r = 0; r < items.length; r++) { // loop through all items //
        var filterKeys = Object.keys(searchFilter); // get keys of filter as array //
        var includInFilteredItems = true; // Items are true when filtered until proven otherwise. Once a false is returned the item is not included in filtered array //
        for (var k in filterKeys) { // loop through filter and one by one get matches. //
          var key = filterKeys[k];
          var keyValue = items[r][key]
          // test if filter type is string/number or object //
          if (typeof searchFilter[key]=="object") { // filter is probably array //
            if (typeof items[r][key] != "object") { // item key being filtered on is probably a string //
              if (searchFilter[key].indexOf(items[r][key])==-1) { // is item key in the array
                includInFilteredItems = false;
                break
              }
            }
            else { // item key is an array //
              var itemHasOneValue = false; // variable to detemrine if at least one value is in filter //
              if (searchFilter[key].length>0) { // if key length > 0 loop through item values and see if one matches //
                for (var sak = 0; sak < searchFilter[key].length; sak++ ) {
                  if (searchFilter[key][sak]=='') { itemHasOneValue = true; }
                  if (items[r][key].indexOf(searchFilter[key][sak])>-1) {
                    itemHasOneValue = true;
                  };
                };                
              }
              else{ // if key length == 0, it is an all search and item matches regardless //
                itemHasOneValue = true
              };

              if (!itemHasOneValue) { // if item doesnt match anything don't include it in the search //
                includInFilteredItems = false;
              }
            };

          }
          else { // filter is not an array //
            // var regex = RegExp(searchFilter[key], "i"); // simple string match //
            if (keyValue==null) { keyValue="" }
            // if (keyValue.search(regex)==-1) { // if regex is -1 not a match //
            if (keyValue.toLowerCase()!=searchFilter[key].toLowerCase()) { // if regex is -1 not a match //
              includInFilteredItems = false;
              break;
            };
          };
        };
        if (includInFilteredItems==true) { // push item if it is to be included in results //
          filteredItems.push(items[r])
        }
      };
    }
    else { // set filteredItems to all when searchFilter hasn't been created yet //
      filteredItems = items;
    };

    return filteredItems;
  };
});