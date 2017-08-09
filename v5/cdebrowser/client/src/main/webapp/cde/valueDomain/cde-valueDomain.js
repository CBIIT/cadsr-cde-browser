
angular.module("cdeValueDomain", []);

angular.module("cdeValueDomain").controller("ValueDomainCtrl", ["$scope", "$anchorScroll", "$location", function ($scope, $anchorScroll, $location) {
	
	$scope.goToVM = function(i) {
		$scope.changeView(4, $scope.tabs[4]);
        $anchorScroll();
        $location.hash(i);
	};

	// create PV Meaning Concept Codes field // 
	$scope.createConceptCodeField = function(conceptCodeArray) {
		// return empty string if concept code is null //
		if (conceptCodeArray == null) {
			return '';
		}
		else {
			// create url for each code //
			var createUrl = function(item, index) {
				var url = '<a style="text-decoration: none;" TARGET="_blank" href="https://ncit.nci.nih.gov/ncitbrowser/ConceptReport.jsp?dictionary=NCI%20Thesaurus&code='+item+'">' + item;
				if (index!=0 && index+1==conceptCodeArray.length) {
					url+='(Primary)'
				}
				url+='</a>';
				return url;
			};

			// create empty concept string //
			var conceptString = '';

			// loop through array and create each url //
			conceptCodeArray.forEach(function(item,index) {
			  conceptString+=createUrl(item, index);

			  // add : at the end of each code unless it is the only code or last //
			  if (conceptCodeArray.length>0 && index+1!=conceptCodeArray.length) {
			    conceptString+=':';
			  };
			  
			});

			return conceptString;			
		}

	};

}]);
