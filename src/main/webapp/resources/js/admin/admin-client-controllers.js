'use strict';
(function() {
	// omit [] to use existing controller: http://stackoverflow.com/a/17289451/237225
	var module = angular.module('myApp.controllers');

	module.controller('AdminClientCtrl', [ '$location', '$log', '$rootScope', '$routeParams', '$scope', 'AdminClientService', //
	function AdminClientCtrlFn($location, $log, $rootScope, $routeParams, $scope, AdminClientService) {
		$scope.edit = false;
		if ($routeParams.id) {
			var id = $routeParams.id;
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
				// updateFnName : 'updateClients', //trust client-side: server side has delay.
				afterFn : function doAfterFn() {
					$scope.edit = false;
				}
			});
		};

		$scope.set = function setFn(obj) {
			$scope.currentClient = angular.copy(obj, {});
			$scope.currentClient.editable = true;
			$scope.edit = true;
			$location.search('id', $scope.currentClient.id);
			$scope.createClientForm.$setPristine();
		};

		$scope.unset = function unsetFn() {
			$scope.edit = false;
			$location.search('id', null);
		};

	} ]);

	module.controller('AdminClientRelatedTimeSheetsCtrl', [ '$log', '$rootScope', '$routeParams', '$scope', 'TimeSheetService', //
	function AdminClientRelatedTimeSheetsCtrlFn($log, $rootScope, $routeParams, $scope, TimeSheetService) {

		$scope.showWorker = {};
		$scope.myRelatedWorkerIdToTimeSheetsMap = {}; // workerId to timeSheet obj

		$scope.$watch('currentClient', function currentClient$watchFn(client) {
			if (client && client.id) {
				// $log.info('get TimeSheets for client=', client);
				TimeSheetService.getAllForClient({
					clientId : client.id,
				}, function successFn(list) {
					var workersMap = {};
					var map = {};
					angular.forEach(list, function(timeSheet) {
						var wid = timeSheet.workerId;
						workersMap[wid] = true;
						if (!map[wid]) {
							map[wid] = [];
						}
						map[wid].push(timeSheet);
					});
					$scope.showWorker = workersMap;
					$scope.myRelatedWorkerIdToTimeSheetsMap = map;
				});
			}
		});

	} ]);

	module.controller('AdminClientRelatedContractsCtrl', [ '$log', '$rootScope', '$routeParams', '$scope', 'AdminContractService', //
	function AdminClientRelatedContractsCtrlFn($log, $rootScope, $routeParams, $scope, AdminContractService) {

		$scope.showWorker = {};
		$scope.myRelatedWorkerIdToContractsMap = {}; // workerId to timeSheet obj

		$scope.$watch('currentClient', function currentClient$watchFn(client) {
			if (client && client.id) {
				// $log.info('get Contracts for client=', client);
				AdminContractService.getAllForClient({
					clientId : client.id,
				}, function successFn(list) {
					var workersMap = {};
					var map = {};
					angular.forEach(list, function(contract) {
						var wid = contract.worker;
						workersMap[wid] = true;
						if (!map[wid]) {
							map[wid] = [];
						}
						map[wid].push(contract);
					});
					$scope.showWorker = workersMap;
					$scope.myRelatedWorkerIdToContractsMap = map;
				});
			}
		});

	} ]);

})();