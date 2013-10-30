'use strict';
(function() {
	// omit [] to use existing controller: http://stackoverflow.com/a/17289451/237225
	var module = angular.module('myApp.controllers');

	module.controller('AdminPageCtrl', [ // 
	'$log', '$rootScope', '$scope', 'AdminClientService', 'AdminWorkerService', 'AdminContractService',//
	function AdminPageCtrlFn($log, $rootScope, $scope, AdminClientService, AdminWorkerService, AdminContractService) {
		// $log.info('AdminPageCtrl init', $scope);
		$scope.setAdmin(true); // inherited fn from UserNavCtrl
		$rootScope.clientsMap = {};
		$scope.contractsMap = {};
		$scope.workersMap = {};

		function updateClientsFn() {
			$log.info("updateClientsFn before:", $rootScope.clientsMap);
			AdminClientService.getAll(function successCGetAllFn(data) {
				$log.info("inside for updateClientsFn with data", data);
				$rootScope.clientsMap = data;
				$log.info("updateClientsFn after: ", $rootScope.clientsMap);
			});
		}

		function updateContractsFn() {
			AdminContractService.getAll(function successConGetAllFn(data) {
				$scope.contractsMap = data;
			});
		}

		function updateWorkersFn() {
			AdminWorkerService.getAll(function successWGetAllFn(data) {
				$scope.workersMap = data;
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

	module.controller('AdminClientCtrl', [ '$location', '$log', '$rootScope', '$routeParams', '$scope', 'AdminClientService', //
	function AdminClientCtrlFn($location, $log, $rootScope, $routeParams, $scope, AdminClientService) {
		// $log.info('AdminClientCtrl init', $scope, $location, $routeParams);
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
			$scope.doSave({
				service : AdminClientService,
				obj : obj,
				map : $rootScope.clientsMap,
				updateFnName : 'updateClients',
				afterFn : function doAfterFn() {
					$scope.edit = false;
				}
			});
		};

		$scope.remove = function removeFn(obj) {
			$scope.doRemove({
				service : AdminClientService,
				id : obj.id,
				map : $rootScope.clientsMap,
				updateFnName : 'updateClients',
				afterFn : function doAfterFn() {
					delete $rootScope.clientsMap[obj.id];
				}
			});
		};

		$scope.set = function setFn(obj) {
			$scope.currentClient = angular.copy(obj, {});
			$scope.edit = true;
			$location.search('id', $scope.currentClient.id);
			$scope.createClientForm.$setPristine();
		};

		$scope.unset = function unsetFn() {
			$scope.edit = false;
			$location.search('id', null);
		};

	} ]);

	module.controller('AdminContractCtrl', [ '$location', '$log', '$routeParams', '$scope', 'AdminContractService', //
	function AdminContractCtrlFn($location, $log, $routeParams, $scope, AdminContractService) {
		// $log.info('AdminContractCtrl init', $scope);
		$scope.edit = false;
		$scope.currentContract = {};
		if ($routeParams.id) {
			var id = $routeParams.id;
			$log.info('getting id', id);
			AdminContractService.get({
				id : id
			}, function successFn(data) {
				$scope.currentContract = data;
				$scope.edit = true;
			});
		}

		$scope.save = function saveFn(obj) {
			$scope.doSave(AdminContractService, obj, 'updateContracts', $scope.contractsMap, function doAfterSaveFn() {
				$scope.edit = false;
			});
		};

		$scope.set = function setFn(obj) {
			$scope.currentContract = angular.copy(obj, {});
			$scope.edit = true;
			$location.search('id', $scope.currentContract.id);
			$scope.createContractForm.$pristine = true;
			$scope.createContractForm.$dirty = false;
		};

		$scope.unset = function unsetFn() {
			$scope.edit = false;
			$location.search('id', null);
		};

	} ]);

	module.controller('AdminWorkerCtrl', [ '$location', '$log', '$routeParams', '$scope', 'AdminWorkerService', //
	function AdminWorkerCtrlFn($location, $log, $routeParams, $scope, AdminWorkerService) {
		// $log.info('AdminWorkerCtrl init', $scope);
		$scope.edit = false;
		if ($routeParams.id) {
			var id = $routeParams.id;
			$log.info('getting id', id);
			AdminWorkerService.get({
				id : id
			}, function successFn(data) {
				$scope.currentWorker = data;
				$scope.edit = true;
			});
		}

		$scope.remove = function removeFn(obj) {
			$scope.doRemove(AdminWorkerService, {
				id : obj.id
			}, 'updateWorkers');
		};

		$scope.save = function saveFn(obj) {
			$scope.doSave(AdminWorkerService, obj, 'updateWorkers', $scope.workersMap, function doAfterSaveFn() {
				$scope.edit = false;
			});
		};

		$scope.set = function setFn(obj) {
			$scope.currentWorker = angular.copy(obj, {});
			$scope.edit = true;
			$location.search('id', $scope.currentWorker.id);
			$scope.createWorkerForm.$pristine = true;
			$scope.createWorkerForm.$dirty = false;
		};

		$scope.unset = function unsetFn() {
			$scope.edit = false;
			$location.search('id', null);
		};

	} ]);

})();