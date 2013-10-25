'use strict';
(function() {

	var module = angular.module('myApp.controllers', []);

	module.controller('MainCtrl', [ '$log', '$scope', '$location',//
	function MainCtrlFn($log, $scope, $location) {
		$scope.debug = (($.cookie('debug') == 'true') ? true : false);
		$log.info('MainCtrl init', $location, $scope.debug);

		$scope.$watch(function locationPathPage$watchFn() {
			return $location.path();
		}, function(newLoc, oldLoc) {
			$scope.page = newLoc;
		});

		$scope.isAdmin = false;

		$scope.setAdmin = function(bool) {
			$scope.isAdmin = bool; // true if Admin, false if User
		};

		$scope.doSave = function doSaveFn(svc, obj, updateFn) {
			var objJson = angular.toJson(obj);
			svc.save(objJson, function saveSuccessFn(data) {
				$log.info("saved data:", data);
				$log.info("do ($broadcast) update '" + updateFn + "'");
				$scope.$broadcast(updateFn);
			});
		};

		$scope.doRemove = function doRemoveFn(svc, obj, updateFn) {
			$log.info("deleting:", obj);
			svc.remove(obj, function successFn() {
				$log.info("do ($broadcast) update '" + updateFn + "'");
				$scope.$broadcast(updateFn);
			});
		};
		
		$log.info('MainCtrl init complete');
	} ]);

	module.controller('AdminPageCtrl', [ '$log', '$scope', 'AdminClientService', 'AdminWorkerService', 'AdminContractService',//
	function AdminPageCtrlFn($log, $scope, AdminClientService, AdminWorkerService, AdminContractService) {
		$log.info('AdminPageCtrl init', $scope);
		$scope.setAdmin(true); // inherited fn from UserNavCtrl
		$scope.clients = [];
		$scope.clientsMap = {};
		$scope.workers = [];
		$scope.workersMap = {};
                $scope.clientMap = {};
		$scope.workers = [];
                $scope.workerMap = {};

		$scope.contracts = [];
		
		function mapToList(map){
			$log.info("map2list",map);
			var list = [];
			angular.forEach(map, function (obj,key){
				$log.info("map2list: pushed obj",obj);
				list.push[obj];
			});
			return list;
		}

		function updateClientsFn() {
			$log.info("updateClientsFn before:", $scope.clients.length);
			AdminClientService.getAll(function successCGetAllFn(data) {
                                $log.info("inside for updateClientsFn with data size " + data.length);
				$scope.clients = data;
                                $scope.clientMap = {};
                                for(var index = 0; index < data.length; index++) {
                                    var item = data[index];
                                    $log.info("pushing reference to client " + item);
                                    $scope.clientMap[item.id] = item;
                                }
				$log.info("updateClientsFn after: ", $scope.clients.length);
			});
		}

		function updateContractsFn() {
			AdminContractService.getAll(function successConGetAllFn(data) {
				$scope.contracts = data;
			});
		}

		function updateWorkersFn() {
			$log.info("updateWorkersFn before:", $scope.workers.length);
			AdminWorkerService.getMap({id:'idmap'},function successWGetAllFn(data) {
				$scope.workersMap = data;
				//$scope.workers = mapToList(data); //wtf why no worky?
                        });
                        
			AdminWorkerService.getAll(function successWGetAllFn(data) {
                                $log.info("inside for updateWorkersFn with data size " + data.length);
				$scope.workers = data;
                                $scope.workerMap = {};
                                for(var index = 0; index < data.length; index++) {
                                    var item = data[index];
                                    $log.info("pushing reference to worker " + item);
                                    $scope.workerMap[item.id] = item;
                                }
				$log.info("updateWorkersFn after:", $scope.workers.length);
			});
		}

		$scope.$on('updateClients', updateClientsFn);
		$scope.$on('updateWorkers', updateWorkersFn);
		$scope.$on('updateContracts', updateContractsFn);

		updateClientsFn();
		updateContractsFn();
		updateWorkersFn();

		$log.info('AdminPageCtrl init complete');
	} ]);

	module.controller('AdminClientCtrl', [ '$log', '$scope', 'AdminClientService', //
	function AdminClientCtrlFn($log, $scope, AdminClientService) {
		$log.info('AdminClientCtrl init', $scope);

		$scope.save = function(obj) {
			$scope.doSave(AdminClientService, obj, 'updateClients');
		};

		$scope.remove = function(obj) {
			$scope.doRemove(AdminClientService, {
				id : obj.id
			}, 'updateClients');
		};

	} ]);

	module.controller('AdminWorkerCtrl', [ '$log', '$scope', 'AdminWorkerService', //
	function AdminWorkerCtrlFn($log, $scope, AdminWorkerService) {
		// $log.info('AdminWorkerCtrl init', $scope);
		$scope.edit = false;

		$scope.save = function(obj) {
			$scope.doSave(AdminWorkerService, obj, 'updateWorkers');
			$scope.edit = false;
		};
		
		$scope.modify = function(obj){
			$scope.edit = true;
		 	$scope.currentWorker = obj;
		}

	} ]);

	module.controller('AdminWorkerDetailCtrl', [ '$log', '$scope', 'AdminWorkerService', //
	function AdminWorkerDetailCtrlFn($log, $scope, AdminWorkerService) {
		// $log.info('AdminWorkerDetailCtrl init', $scope);
	} ]);

	module.controller('AdminContractCtrl', [ '$log', '$scope', 'AdminContractService', //
	function AdminContractCtrlFn($log, $scope, AdminContractService) {
		$log.info('AdminContractCtrl init', $scope);

		$scope.save = function(obj) {
			$scope.doSave(AdminContractService, obj, 'updateContracts');
		};
	} ]);

	module.controller('AdminContractDetailCtrl', [ '$log', '$scope', 'AdminContractService', //
	function AdminContractDetailCtrlFn($log, $scope, AdminWorkerService) {
		// $log.info('AdminContractDetailCtrl init', $scope);
		$scope.edit = false;
	} ]);

	module.controller('LandingPageCtrl', [ '$log', '$scope', //
	function LandingPageCtrlFn($log, $scope) {
		$log.info('LandingPageCtrl init', $scope);
	} ]);

	module.controller('HistoryPageCtrl', [ '$log', '$scope', //
	function HistoryPageCtrlFn($log, $scope) {
		$log.info('HistoryPageCtrl init', $scope);
	} ]);

	module.controller('TimeSheetPageCtrl', [ '$log', '$scope', 'TimeSheetService', //
	function TimeSheetPageCtrlPageCtrlFn($log, $scope, TimeSheetService) {
		$scope.edit = false;
		$scope.currentTimeSheet = {};
		$log.info('TimeSheetPageCtrl init', $scope);
		$scope.dows = [ 'Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat' ];

		function updateTimeSheetsFn() {
			TimeSheetService.getAll(function successFn(data) {
				$scope.timesheets = data;
			});
		}

		$scope.create = function() {
			TimeSheetService.create(function successFn(data) {
				$scope.currentTimeSheet = data;
				$scope.edit = true;
			});
		}

		$scope.selectTimeSheet = function(ts) {
			$scope.currentTimeSheet = ts;
			$scope.edit = true;
		};

		$scope.save = function(ts) {
			$scope.doSave(TimeSheetService, ts, 'updateTimeSheets');
			$scope.edit = false;
		};

		$scope.$on('updateTimeSheets', this.updateTimeSheetsFn);

		updateTimeSheetsFn();

	} ]);

	module.controller('UserPageCtrl', [ '$log', '$scope', //
	function UserPageCtrlFn($log, $scope) {
		$log.info('UserPageCtrl init', $scope);
		$scope.setAdmin(false); // inherited fn from UserNavCtrl
	} ]);

})();