
angular.module("cdeValueDomain", []);

angular.module("cdeValueDomain").controller("ValueDomainCtrl", ["$scope", "$anchorScroll", "$location", function ($scope, $anchorScroll, $location) {
	
	$scope.goToVM = function(i) {
		$scope.changeView(4,$scope.tabs[4]);
		console.log(i);
		var change = $location.hash();
        $location.hash(i);
        $anchorScroll();
        $location.hash(change);
	};
	
}]);
