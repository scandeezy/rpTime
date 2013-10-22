'use strict';
(function() {
	
	console.log('loading controllers')

	var module = angular.module('myApp.controllers', []);

	module.controller('AdminPageCtrl', [ '$log', '$scope', //
	function AdminPageCtrlFn($log, $scope) {
		$log.info('AdminPageCtrl init', $scope);
		$scope.setAdmin(true); // inherited fn from UserNavCtrl
	} ]);

	module.controller('AdminClientCtrl', [ '$log', '$scope', 'AdminClientService', //
	function AdminClientCtrlFn($log, $scope, AdminClientService) {
		$log.info('AdminClientCtrl init', $scope);
		$scope.clients = AdminClientService.getAll() || [];
		$scope.save = function(obj){
			$scope.doSave(obj, AdminClientService, $scope.clients);
		};
 	} ]);                              	
	
	module.controller('AdminWorkerCtrl', [ '$log', '$scope', 'AdminWorkerService', //
  	function AdminWorkerCtrlFn($log, $scope, AdminWorkerService) {
  		$log.info('AdminWorkerCtrl init', $scope);
		$scope.workers = AdminWorkerService.getAll() || [];
		$scope.save= function(obj){
			$scope.doSave(obj, AdminWorkerService, $scope.workers);
		};
  	} ]);

	module.controller('LandingPageCtrl', [ '$log', '$scope', //
	function LandingPageCtrlFn($log, $scope) {
		$log.info('LandingPageCtrl init', $scope);
		$scope.setPage('landing');
	} ]);
	
	module.controller('HistoryPageCtrl', [ '$log', '$scope', //
	function HistoryPageCtrlFn($log, $scope) {
		$log.info('HistoryPageCtrl init', $scope);
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
		$scope.debug = (($.cookie('debug') == 'true') ? true : false);
		$log.info('MainCtrl init', $location,$scope.debug);
		
		$scope.isAdmin = false;
		$scope.setAdmin = function(bool) {
			$scope.isAdmin = bool;  // true if Admin, false if User
		};
		$scope.setPage = function(page) {
			$scope.page = page;
		};
		$scope.doSave = function(svc, obj, arr){
			var objJson = angular.toJson(obj);
			svc.save(objJson,function saveSuccessFn(data){
				$log.info("saved data:", data);
				arr.push[data];
			});
		};
	} ]);

	module.controller('UserPageCtrl', [ '$log', '$scope', //
	function UserPageCtrlFn($log, $scope) {
		$log.info('UserPageCtrl init', $scope);
 		$scope.setAdmin(false); // inherited fn from UserNavCtrl
	} ]);

})();