'use strict';
(function() {
	
	console.log('loading controllers')

	var module = angular.module('myApp.controllers', []);

	module.controller('AdminPageCtrl', [ '$log', '$scope', 'AdminClientService', 'AdminWorkerService', //
	function AdminPageCtrlFn($log, $scope, AdminClientService, AdminWorkerService) {
		$log.info('AdminPageCtrl init', $scope);
		$scope.setAdmin(true); // inherited fn from UserNavCtrl
		$scope.clients = [];
		$scope.workers = [];
		
		function updateClientsFn(){
			$log.info("updating collection");
			AdminClientService.getAll(function successCGetAllFn(data){
				$scope.clients = data;
				$log.info("success",data.length);
			});
		};
		
		function updateWorkersFn(){
			$log.info("updating collection");
			AdminWorkerService.getAll(function successWGetAllFn(data){
				$scope.workers = data;
				$log.info("success",data.length);
			});
		};
		
		$scope.$on('updateClients',updateClientsFn);
		$scope.$on('updateWorkers',updateWorkersFn);
		
		updateClientsFn(); 
		updateWorkersFn();
	} ]);

	module.controller('AdminClientCtrl', [ '$log', '$scope', 'AdminClientService', //
	function AdminClientCtrlFn($log, $scope, AdminClientService) {
		$scope.setPage('client');
		$log.info('AdminClientCtrl init', $scope);

		$scope.save = function(obj){
			$scope.doSave(AdminClientService, obj, 'updateClients');
		};
 	} ]);                              	
	
	module.controller('AdminWorkerCtrl', [ '$log', '$scope', 'AdminWorkerService', //
  	function AdminWorkerCtrlFn($log, $scope, AdminWorkerService) {
		$scope.setPage('worker');
  		$log.info('AdminWorkerCtrl init', $scope);

		$scope.save= function(obj){
			$scope.doSave(AdminWorkerService, obj, 'updateWorkers');
		};
  	} ]);

	module.controller('LandingPageCtrl', [ '$log', '$scope', //
	function LandingPageCtrlFn($log, $scope) {
		$log.info('LandingPageCtrl init', $scope);
		$scope.setPage('landing');
	} ]);
	
	module.controller('HistoryPageCtrl', [ '$log', '$scope', //
	function HistoryPageCtrlFn($log, $scope) {
		$scope.setPage('history');
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
		$log.info('MainCtrl init', $location, $scope.debug);
		

		// $scope.$on('$locationChangeStart', function (event, newLoc, oldLoc){
		// console.log('changing to: ' + newLoc);
		// });
		//
		// $scope.$on('$locationChangeSuccess', function (event, newLoc, oldLoc){
		// console.log('changed to: ' + newLoc);
		// });
		
		$scope.isAdmin = false;
		
		$scope.setAdmin = function(bool) {
			$scope.isAdmin = bool;  // true if Admin, false if User
		};
		
		$scope.setPage = function(page) {
			$scope.page = page;
		};
		
		$scope.doSave = function doSaveFn(svc, obj, updateFn){
			var objJson = angular.toJson(obj);
			svc.save(objJson,function saveSuccessFn(data){
				$log.info("saved data:", data);
			});
			$log.info("do ($broadcast) update '"+updateFn+"'");
			$scope.$broadcast(updateFn);
		};
	} ]);

	module.controller('UserPageCtrl', [ '$log', '$scope', //
	function UserPageCtrlFn($log, $scope) {
		$log.info('UserPageCtrl init', $scope);
 		$scope.setAdmin(false); // inherited fn from UserNavCtrl
	} ]);

})();