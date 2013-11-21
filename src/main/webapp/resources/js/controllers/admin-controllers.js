'use strict';
(function() {
	// omit [] to use existing controller: http://stackoverflow.com/a/17289451/237225
	var module = angular.module('myApp.controllers');

	module.controller('AdminPageCtrl', [ // 
	'$log', '$rootScope', '$scope', 'AdminClientService', 'AdminWorkerService', 'AdminContractService', 'TimeSheetService',//
	function AdminPageCtrlFn($log, $rootScope, $scope, AdminClientService, AdminWorkerService, AdminContractService, TimeSheetService) {
		// $log.info('AdminPageCtrl init', $scope);
		$scope.setAdmin(true); // inherited fn from UserNavCtrl
		$rootScope.clientsMap = {};
		$rootScope.clientsList = [];
		$rootScope.contractsMap = {};
		$rootScope.workersMap = {};
		$rootScope.workersList = []; // lists allow filters: http://stackoverflow.com/a/14789258/237225

		function updateClientsFn() {
			AdminClientService.getAll(function successCGetAllFn(map) {
				var arr = [];
				$rootScope.clientsMap = map;
				angular.forEach(map, function(val, key) {
					if (key && val.id) {
						arr.push(val);
					}
				});
				$rootScope.clientsList = arr;
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
				$log.info('get TimeSheets for client=', client);
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
				$log.info('get Contracts for client=', client);
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
				// updateFnName : 'updateContracts', //trust client-side: server side has delay.
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

		$scope.newRandom = function newRandomFn() {
			var randomNum = Math.floor(Math.random() * 900) + 100;
			var r = {
				firstName : 'first-' + randomNum,
				lastName : 'last-' + randomNum,
				email : 'email-' + randomNum + '@test.com'
			};
			$scope.set(r);
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
				// updateFnName : 'updateWorkers', //trust client-side: server side has delay.
				afterFn : function doAfterFn(savedObj) {
					$log.info("workersMap keys before=" + Object.keys($rootScope.workersMap).length);
					// $rootScope.workersMap[savedObj.id] = savedObj;
					// $log.info("workersMap keys after="+Object.keys($rootScope.workersMap).length);
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

	module.controller('AdminWorkerRelatedTimeSheetsCtrl', [ '$log', '$scope', '$timeout', 'TimeSheetService', //
	function AdminWorkerRelatedTimeSheetsCtrlFn($log, $scope, $timeout, TimeSheetService) {
		// $log.info("AdminWorkerRelatedTimeSheetsCtrl");
		$scope.myRelatedTimeSheets = [];

		$scope.updateRelatedTimeSheets = function updateRelatedTimeSheetsFn() {
			if ($scope.currentWorker && $scope.currentWorker.id) {
				var wid = $scope.currentWorker.id
				TimeSheetService.getAllForWorker({
					workerId : wid
				}, function successFn(timeSheetsList) {
					var arr = [];
					angular.forEach(timeSheetsList, function(timeSheet) {
						if (timeSheet.workerId == wid) {
							arr.push(timeSheet);
						}
					});
					$scope.myRelatedTimeSheets = arr;
				});
			} else {
				$log.info('sleeping 200ms for updateRelatedTimeSheets');
				$timeout(function my$timeoutAsyncCompensationFn() {
					$scope.updateRelatedTimeSheets();
				}, 200);
			}
		};

		$scope.$watch('currentWorker', function currentWorker$watchFn(obj) {
			if (obj) {
				$scope.updateRelatedTimeSheets();
			}
		});

	} ]);

	module.controller('AdminWorkerRelatedContractsCtrl', [ '$log', '$rootScope', '$scope', '$timeout', //
	function AdminWorkerRelatedContractsCtrl($log, $rootScope, $scope, $timeout) {
		// use Contracts from $rootScope
		// $log.info("AdminWorkerRelatedContractsCtrl, contractsMap", $scope.currentWorker, $rootScope.contractsMap);

		$scope.myRelatedContracts = [];

		$scope.updateRelatedContracts = function updateRelatedContractsFn() {
			if ($scope.currentWorker && $rootScope.contractsMap.$resolved && $rootScope.clientsMap.$resolved) {
				var wid = $scope.currentWorker.id
				var arr = [];
				angular.forEach($rootScope.contractsMap, function(contract, id) {
					if (contract.worker == wid) {
						arr.push({
							contract : contract,
							client : $rootScope.clientsMap[contract.client]
						});
					}
				});
				$scope.myRelatedContracts = arr;
			} else {
				$log.info('sleeping 200ms for updateRelatedContracts');
				$timeout(function my$timeoutAsyncCompensationFn() {
					$scope.updateRelatedContracts();
				}, 200);
			}
		};

		$scope.$watch('currentWorker', function currentWorker$watchFn(obj) {
			if (obj) {
				$scope.updateRelatedContracts();
			}
		});

	} ]);

})();