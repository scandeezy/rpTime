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
		$rootScope.contractsMap = {};
		$rootScope.workersMap = {};

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
				$rootScope.contractsMap = data;
			});
		}

		function updateWorkersFn() {
			AdminWorkerService.getAll(function successWGetAllFn(data) {
				$rootScope.workersMap = data;
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

		$scope.remove = function removeFn(obj) {
			$scope.doRemove({
				service : AdminClientService,
				id : obj.id,
				map : $rootScope.clientsMap,
				afterFn : function doAfterFn() {
					delete $rootScope.clientsMap[obj.id];
				}
			});
		};

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

	module.controller('AdminContractCtrl', [ '$location', '$log', '$rootScope', '$routeParams', '$scope', 'AdminContractService', //
	function AdminContractCtrlFn($location, $log, $rootScope, $routeParams, $scope, AdminContractService) {
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

		$scope.remove = function removeFn(obj) {
			$scope.doRemove({
				service : AdminContractService,
				id : obj.id,
				map : $rootScope.contractsMap,
				afterFn : function doAfterFn() {
					delete $rootScope.contractsMap[obj.id];
				}
			});
		};

		$scope.save = function saveFn(obj) {
			$scope.doSave({
				service : AdminContractService,
				obj : obj,
				map : $rootScope.contractsMap,
				updateFnName : 'updateContracts',
				afterFn : function doAfterFn() {
					$scope.edit = false;
				}
			});
		};

		$scope.set = function setFn(obj) {
			$scope.currentContract = angular.copy(obj, {});
			$scope.edit = true;
			$location.search('id', $scope.currentContract.id);
			$scope.createContractForm.$setPristine();
		};

		$scope.unset = function unsetFn() {
			$scope.edit = false;
			$location.search('id', null);
		};

	} ]);

	module.controller('AdminWorkerCtrl', [ '$location', '$log', '$rootScope', '$routeParams', '$scope', 'AdminWorkerService', //
	function AdminWorkerCtrlFn($location, $log, $rootScope, $routeParams, $scope, AdminWorkerService) {
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
			$scope.doRemove({
				service : AdminWorkerService,
				id : obj.id,
				map : $rootScope.workersMap,
				afterFn : function doAfterFn() {
					delete $rootScope.workersMap[obj.id];
				}
			});
		};

		$scope.save = function saveFn(obj) {
			$scope.doSave({
				service : AdminWorkerService,
				obj : obj,
				map : $rootScope.workersMap,
				updateFnName : 'updateWorkers',
				afterFn : function doAfterFn() {
					$scope.edit = false;
				}
			});
		};

		$scope.set = function setFn(obj) {
			$scope.currentWorker = angular.copy(obj, {});
			$scope.edit = true;
			$location.search('id', $scope.currentWorker.id);
			$scope.createWorkerForm.$setPristine();
		};

		$scope.unset = function unsetFn() {
			$scope.edit = false;
			$location.search('id', null);
		};

	} ]);

})();