'use strict';
(function() {

	var module = angular.module('myApp.controllers', []);

	module.controller('AdminPageCtrl', [ '$log', '$scope', //
	function AdminPageCtrlFn($log, $scope) {
		$log.info('AdminPageCtrl init', $scope);
		$scope.setAdmin(true); // inherited fn from UserNavCtrl
	} ]);

	module.controller('LandingPageCtrl', [ '$log', '$scope', //
	function LandingPageCtrlFn($log, $scope) {
		$log.info('LandingPageCtrl init', $scope);
		$scope.setPage('landing');
	} ]);
	
	
		module.controller('TimesheetPageCtrl', [ '$log', '$scope', //
	function TimesheetPageCtrlPageCtrlFn($log, $scope) {
		$scope.edit = false;	
		$log.info('TimesheetPageCtrl init', $scope);
		$scope.dows=['Sun','Mon','Tue','Wed','Thu','Fri','Sat']
		$scope.setPage('timesheet');
		var monthDays = [ // 
		{ date: "1", hours: "10", type: "abc"}, // 
		{ date: "2", hours: "20", type: "dabc"}, // 
		{ date: "3", hours: "30", type: "eabc"}, // 
		{ date: "4", hours: "40", type: "fabc"}, // 
		{ date: "5", hours: "50", type: "gabc"}, // 
		{ date: "6", hours: "60", type: "habc"}, // 
		{ date: "7", hours: "70", type: "iabc"}, // 
		{ date: "8", hours: "80", type: "jabc"} // 
		                ];
		
	    var dates = [];
	    for (var i = 0; i < monthDays.length; i++ ) {
	        if (i % 7 == 0) dates.push([]);
	        dates[dates.length-1].push(monthDays[i]);
	    }
	    $scope.dates = dates;

	} ]);

	module.controller('MainCtrl', [ '$log', '$scope', '$location',//
	function MainCtrlFn($log, $scope, $location) {
		$log.info('MainCtrl init', $location);
		$scope.isAdmin = null;
		$scope.setAdmin = function(bool) {
			$scope.isAdmin = bool;
		};
		$scope.setPage = function(page) {
			$scope.page = page;
		};
	} ]);

	module.controller('WorkerPageCtrl', [ '$log', '$scope', //
	function WorkerPageCtrlFn($log, $scope) {
		$log.info('WorkerPageCtrl init', $scope);
		$scope.setAdmin(false); // inherited fn from UserNavCtrl
	} ]);

})();