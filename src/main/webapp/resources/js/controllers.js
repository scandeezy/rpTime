'use strict';
(function() {
	
	console.log('loading controllers')

	var module = angular.module('myApp.controllers', []);
	
	module.controller('MainCtrl', [ '$log', '$scope', '$location',//
	function MainCtrlFn($log, $scope, $location) {
		$scope.debug = (($.cookie('debug') == 'true') ? true : false);
		$log.info('MainCtrl init', $location, $scope.debug);
		
		$scope.$watch(function() { return $location.path(); }, function(newLoc, oldLoc){
		   console.log('location changed', newLoc, oldLoc);
		});		
		
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


	module.controller('AdminPageCtrl', [ '$log', '$scope', 'AdminClientService', 'AdminWorkerService',  //
	function AdminPageCtrlFn($log, $scope, AdminClientService, AdminWorkerService) {
		$log.info('AdminPageCtrl init', $scope);
		$scope.setAdmin(true); // inherited fn from UserNavCtrl
		$scope.clients = [];
		$scope.workers = [];
		
		function updateClientsFn(){
			AdminClientService.getAll(function successCGetAllFn(data){
				$scope.clients = data;
			});
		};
		
		function updateWorkersFn(){
			AdminWorkerService.getAll(function successWGetAllFn(data){
				$scope.workers = data;
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

	module.controller('TimeSheetPageCtrl', [ '$log', '$scope',  'TimeSheetService', //
	function TimeSheetPageCtrlPageCtrlFn($log, $scope, TimeSheetService) {
		$scope.edit = false;	
		$scope.currentTimeSheet = ['abc',123];
		$log.info('TimeSheetPageCtrl init', $scope);
		$scope.dows=['Sun','Mon','Tue','Wed','Thu','Fri','Sat']
		$scope.timesheets = TimeSheetService.getAll() ||  [];
		$scope.setPage('timesheet');

	} ]);

	module.controller('UserPageCtrl', [ '$log', '$scope', //
	function UserPageCtrlFn($log, $scope) {
		$log.info('UserPageCtrl init', $scope);
 		$scope.setAdmin(false); // inherited fn from UserNavCtrl
 	} ]);

})();