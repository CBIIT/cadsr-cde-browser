        cdebrowserTreeview.directive('resize', function ($window) {
            return function (scope, element, attr) {

                var w = angular.element($window);
                scope.$watch(function () {
                    return {
                        'h': window.innerHeight,
                        'w': window.innerWidth
                    };
                }, function (newValue, oldValue) {
                    scope.windowHeight = newValue.h;
                    scope.windowWidth = newValue.w;

                    scope.resizeHeightWithOffset = function (offsetH) {
                        scope.$eval(attr.notifier);
                        return {
                            'height': (newValue.h - offsetH) + 'px'
                        };
                    };
                    scope.resizeHeight = function () {
                        scope.$eval(attr.notifier);
                        return {
                            'height': (newValue.h - properties.bottomOffset) + 'px'
                        };
                    };
                }, true);

                w.bind('resize', function () {
                    scope.$apply();
                });
            }
        })