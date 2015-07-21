cdeBrowserApp.factory('cdeBrowserFactory',
    function($http,$q) {
        var obj = this;
        // function to fetch data //
        var searchResults = $q.defer();
        var dataLoadResults = $q.defer();

        obj.fetchJsonData = function(serverUrl) {
            $http.get(serverUrl).success(
                function(data) {
                searchResults.resolve(data);
                }
            );            
        };

        // function that gets the data returned for search results //
        obj.basicSearchServerRestCall = function(serverUrl) {
            obj.fetchJsonData(serverUrl);           
            var results = searchResults.promise;
            return results;
        }

        // function that gets the data returned for left tree //
        obj.dataLoad = function(serverUrl) {
            obj.fetchJsonData(serverUrl);           
            var results = dataLoadResults.promise;
            return results;
        }        
        return obj;
    }
);