'use strict';
(function() {
	// omit [] to use existing controller: http://stackoverflow.com/a/17289451/237225
	var module = angular.module('myApp.controllers');

	module.controller('AdminPageCtrl', [ // 
	'$log', '$rootScope', '$scope', 'AdminClientService',//
	'AdminContractService', 'AdminWorkerService', 'AdminUnlinkedUserService', 'TimeSheetService',//
	function AdminPageCtrlFn($log, $rootScope, $scope,// 
	AdminClientService, AdminContractService, AdminWorkerService, AdminUnlinkedUserService, TimeSheetService) {
		// $log.info('AdminPageCtrl init', $scope);
		$scope.setAdmin(true); // inherited fn from UserNavCtrl
		$rootScope.clientsMap = {};
		$rootScope.clientsList = [];
		$rootScope.contractsMap = {};
		$rootScope.contractsList = [];
		$rootScope.workersMap = {};
		$rootScope.workersList = []; // lists allow filters: http://stackoverflow.com/a/14789258/237225
		$rootScope.unlinkedUsersList = [];

		function updateClientsFn() {
			AdminClientService.getAll(function successCGetAllFn(map) {
				$rootScope.clientsMap = map;
			});
		}

		function updateContractsFn() {
			AdminContractService.getAll(function successConGetAllFn(map) {
				$rootScope.contractsMap = map;
			});
		}

		function updateWorkersFn() {
			AdminWorkerService.getAll(function successWGetAllFn(data) {
				$rootScope.workersMap = data;
			});
			AdminUnlinkedUserService.getAll(function successFn(arr) {
				$rootScope.unlinkedUsersList = arr;
			});
		}

		$scope.$on('updateClients', updateClientsFn);
		$scope.$on('updateWorkers', updateWorkersFn);
		$scope.$on('updateContracts', updateContractsFn);

		$rootScope.$watch('clientsMap', function clientsMap$watchListSyncFn(map) {
			var arr = [];
			angular.forEach(map, function(val, key) {
				if (key && val.id) {
					arr.push(val);
				}
			});
			$rootScope.clientsList = arr;
		}, true);

		$rootScope.$watch('contractsMap', function contractsMap$watchListSyncFn(map) {
			var arr = [];
			angular.forEach(map, function(val, key) {
				if (key && val.id) {
					arr.push(val);
				}
			});
			$rootScope.contractsList = arr;
		}, true);

		$scope.$watch('workersMap', function workersMap$watchFn(map) {
			var arr = [];
			angular.forEach(map, function(val, key) {
				if (key && val.id) {
					arr.push(val);
				}
			});
			$rootScope.workersList = arr;
		}, true);

		$scope.flag = function flagFn(id, flagged) {
			TimeSheetService.flag({
				id : id,
				flagged : flagged
			}, function successFn() {
				$log.info('timesheet id/flagged', id, flagged);
			});
		};

		updateClientsFn();
		updateContractsFn();
		updateWorkersFn();

		$log.info('AdminPageCtrl init complete');
	} ]);

	module.controller('ModalDemoCtrl', [ '$log', '$scope',//
	function ModalDemoCtrlFn($log, $scope) {
		$scope.items = [ 'item1', 'item2', 'item3' ];
		$scope.jqueryModalElementHandle = $('#myModal');

		$scope.close = function closeFn() {
			$scope.closeModal = true;
		};

		$scope.open = function openFn() {
			$log.info('ModalDemoCtrl [myModal] open()...');
			$scope.closeModal = false;
			$scope.jqueryModalElementHandle
			// $('#myModal')
			.modal({
				show : true
			})
		};

		$log.info('ModalDemoCtrl init complete');

	} ]);

})();