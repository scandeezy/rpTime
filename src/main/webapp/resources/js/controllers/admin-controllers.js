'use strict';
(function() {
	// omit [] to use existing controller: http://stackoverflow.com/a/17289451/237225
	var module = angular.module('myApp.controllers');

	module.controller('AdminPageCtrl', [ '$log', '$scope', 'AdminClientService', 'AdminWorkerService', 'AdminContractService',//
	function AdminPageCtrlFn($log, $scope, AdminClientService, AdminWorkerService, AdminContractService) {
		$log.info('AdminPageCtrl init', $scope);
		$scope.setAdmin(true); // inherited fn from UserNavCtrl
		$scope.clients = [];
		$scope.clientsMap = {};
		$scope.workers = [];
		$scope.workersMap = {};
		$scope.workers = [];
		$scope.workerMap = {};

		$scope.contracts = [];

		function updateClientsFn() {
			$log.info("updateClientsFn before:", $scope.clients.length);
			AdminClientService.getAll(function successCGetAllFn(data) {
				$log.info("inside for updateClientsFn with data size " + data.length);
				$scope.clients = data;
				$scope.clientsMap = {};
				for (var index = 0; index < data.length; index++) {
					var item = data[index];
					$log.info("pushing reference to client " + item);
					$scope.clientsMap[item.id] = item;
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

	module.controller('AdminClientCtrl', [ '$location', '$log', '$routeParams', '$scope', 'AdminClientService', //
	function AdminClientCtrlFn($location, $log, $routeParams, $scope, AdminClientService) {
		$log.info('AdminClientCtrl init', $scope, $location, $routeParams);
		$scope.edit = false;
		if ($routeParams.id) {
			var id = $routeParams.id;
			$log.info('getting id', id);
			AdminClientService.get({
				id : id
			}, function successFn(data) {
				$scope.currentClient = data;
				$scope.edit = true;
			});
		}

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
			$scope.currentClient = angular.copy(obj, {});
			$scope.edit = true;
			$location.search('id', $scope.currentClient.id);
			$scope.createClientForm.$pristine = true;
			$scope.createClientForm.$dirty = false;
		};

		$scope.unset = function unsetFn() {
			$scope.edit = false;
			$location.search('id', null);
		};

	} ]);

	module.controller('AdminContractCtrl', [ '$log', '$scope', 'AdminContractService', //
	function AdminContractCtrlFn($log, $scope, AdminContractService) {
		$log.info('AdminContractCtrl init', $scope);
		$scope.edit = false;
		$scope.currentContract = {};

		$scope.save = function saveFn(obj) {
			$scope.doSave(AdminContractService, obj, 'updateContracts', $scope.contracts);
			$scope.edit = false;
		};

		$scope.set = function setFn(obj) {
			$scope.edit = true;
			$scope.currentContract = (obj || {});
		}

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
			$scope.currentWorker = angular.copy(obj, {});
			$scope.edit = true;
			$location.search('id', $scope.currentWorker.id);
			$scope.createWorkerForm.$pristine = true;
			$scope.createWorkerForm.$dirty = false;
		}

		$scope.unset = function unsetFn() {
			$scope.edit = false;
			$location.search('id', null);
		};

	} ]);

})();