angular.module("cdeBrowserApp").service('compareService', function () {

   this.checkedItemsForCompare = [];
    
    this.addToCompare = function (checkedItems, searchResults) {

        for (var i = 0; i < searchResults.length; i++) {
            if (checkedItems.indexOf(searchResults[i]['deIdseq']) >= 0) {

                if (this.checkedItemsForCompare.indexOf(searchResults[i]['deIdseq']) < 0) {
                    this.checkedItemsForCompare.push(searchResults[i]['deIdseq']);
                };

                var arrayLength = this.checkedItemsForCompare.length;
                for (var f = 0; f < arrayLength; f++) {

                };
            };
        };
    };

    this.idList = "";

    this.compareCDE = function (checkedItemsForDownload, searchResults) {
        // this.checkedItemsForCompare = [];
        
        //Add any newly checked CDEs  CHECKME, make sure this is the correct functionality.
        this.addToCompare(checkedItemsForDownload, searchResults);

        var cdeListString ="";

        var arrayLength = this.checkedItemsForCompare.length;
        for (var f = 0; f < arrayLength; f++) {
            if( f > 0)
            {
                cdeListString += ",";
            }
            cdeListString += this.checkedItemsForCompare[f];
        }

        this.idList = cdeListString;

        // var usrlString= window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + "/cdebrowserClient/cdeCompare.html?cde=" + cdeListString;
        // window.open(usrlString);
      

     this.deleteCheckedItemsforCompare=function(id,all){
        if(all){
           this.checkedItemsForCompare=[]; 
        }else{
        var len=this.checkedItemsForCompare.length;
for(var x=0;x<len;x++){
if(this.checkedItemsForCompare[x]==id){
    this.checkedItemsForCompare.splice(x,1);
    break;
}
}    
        }
        
     }   
    };

});
