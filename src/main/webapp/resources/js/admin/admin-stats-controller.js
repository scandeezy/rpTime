'use strict';
(function() {
	// omit [] to use existing controller: http://stackoverflow.com/a/17289451/237225
	var module = angular.module('myApp.controllers');

	module.controller('AdminStatsPageCtrl', [ '$location', '$log', '$routeParams', '$cookies', '$scope', 'AdminStatsService',//
	function AdminStatsPageCtrlFn($location, $log, $routeParams, $cookies, $scope, AdminStatsService) {
		$scope.callList = [];
        
        $scope.update = function update() {
            AdminStatsService.getServices({}, function successFn(data) {
                $scope.callList = data;
//                $location.search('listView',null).search('id', $scope.currentTimeSheet.id);
            });
        }
        
        $scope.update();
    }
    ]);
})();