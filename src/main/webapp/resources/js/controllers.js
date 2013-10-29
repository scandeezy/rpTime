'use strict';
(function() {

	var module = angular.module('myApp.controllers', ['myApp.filters']);

	module.controller('MainCtrl', [ '$log', '$scope', '$location', 'dayOfWeekArr', //
	function MainCtrlFn($log, $scope, $location, dayOfWeekArr) {
		$scope.dayOfWeekArr = dayOfWeekArr;
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

		$scope.doSave = function doSaveFn(svc, obj, updateFn, arr) {
			$log.info("dosave:",[svc, obj, updateFn, arr]);
			var objJson = angular.toJson(obj);
			if(arr && arr.indexOf(obj) != null ){
				arr.push[obj];
			}
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

		function updateClientsFn() {
			$log.info("updateClientsFn before:", $scope.clients.length);
			AdminClientService.getAll(function successCGetAllFn(data) {
				$log.info("inside for updateClientsFn with data size " + data.length);
				$scope.clients = data;
				$scope.clientMap = {};
				for (var index = 0; index < data.length; index++) {
					var item = data[index];
					$log.info("pushing reference to client " + item);
					$scope.clientMap[item.id] = item;
				}
				$log.info("updateClientsFn after: ", $scope.clients.length);
			});
		}

		function updateContractsFn() {
			$log.info("updateContractsFn before:", $scope.contracts.length);
			AdminContractService.getAll(function successConGetAllFn(data) {
				$scope.contracts = data;
				$log.info("updateContractsFn after:", $scope.contracts.length);
			});
		}

		function updateWorkersFn() {
			$log.info("updateWorkersFn before:", $scope.workers.length);
			AdminWorkerService.getMap({
				id : 'idmap'
			}, function successWGetAllFn(data) {
				$scope.workersMap = data;
				// $scope.workers = mapToList(data); //wtf why no worky?
			});

			AdminWorkerService.getAll(function successWGetAllFn(data) {
				$log.info("inside for updateWorkersFn with data size " + data.length);
				$scope.workers = data;
				$scope.workerMap = {};
				for (var index = 0; index < data.length; index++) {
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
		$scope.edit = false;

		$scope.save = function saveFn(obj) {
			$scope.doSave(AdminClientService, obj, 'updateClients', $scope.clients);
			$scope.edit = false;
		};

		$scope.remove = function rempveFn(obj) {
			$scope.doRemove(AdminClientService, {
				id : obj.id
			}, 'updateClients');
		};

		$scope.set = function setFn(obj) {
			$scope.currentClient =  (obj || {});
			$scope.edit = true;
		};

	} ]);

	module.controller('AdminWorkerCtrl', [ '$log', '$scope', 'AdminWorkerService', //
	function AdminWorkerCtrlFn($log, $scope, AdminWorkerService) {
		// $log.info('AdminWorkerCtrl init', $scope);
		$scope.edit = false;

		$scope.save = function saveFn(obj) {
			$scope.doSave(AdminWorkerService, obj, 'updateWorkers', $scope.workers);
			$scope.edit = false;
		};

		$scope.set = function setFn(obj) {
			$scope.edit = true;
			$scope.currentWorker =  (obj || {});
		}

	} ]);

	module.controller('AdminContractCtrl', [ '$log', '$scope', 'AdminContractService', //
	function AdminContractCtrlFn($log, $scope, AdminContractService) {
		$log.info('AdminContractCtrl init', $scope);
		$scope.edit=false;
		$scope.currentContract ={};
		
		$scope.save = function saveFn(obj) {
			$scope.doSave(AdminContractService, obj, 'updateContracts', $scope.contracts);
			$scope.edit = false;
		};
		
		$scope.set = function setFn(obj) {
			$scope.edit = true;
			$scope.currentContract = (obj || {});
		}

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

		$scope.set = function setFn(obj) {
			if (obj) {
				$scope.currentTimeSheet = obj;
			} else {
				TimeSheetService.create(function successFn(data) {
					$scope.currentTimeSheet = data;
				});
			}
			$scope.edit = true;
		}

		$scope.save = function saveFn(ts) {
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