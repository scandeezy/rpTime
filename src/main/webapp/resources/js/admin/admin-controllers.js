'use strict';
(function() {
	// omit [] to use existing controller: http://stackoverflow.com/a/17289451/237225
	var module = angular.module('myApp.controllers');

	module.controller('AdminPageCtrl', [ // 
	'$location', '$log', '$rootScope', '$scope', 'AdminClientService',//
	'AdminContractService', 'AdminWorkerService', 'AdminUnlinkedUserService', 'TimeSheetService',//
	function AdminPageCtrlFn($location, $log, $rootScope, $scope,// 
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

		$scope.getTimeSheetForWorkerDate = function getTimeSheetForWorkerDateFn(parms, closeModalFn) {
			if (!(parms && parms.workerId && parms.date)) {
				throw new Error("required: 'workerId' and 'date'");
			}
			TimeSheetService.getForWorkerIdDate(parms, function successFn(data) {
				closeModalFn();
				$location.path('timesheet').search('id', data.id);
			});
		};

		updateClientsFn();
		updateContractsFn();
		updateWorkersFn();

		$log.info('AdminPageCtrl init complete');
	} ]);

	module.controller('WorkerWeekSelectionModalCtrl', [ //
			'$log',
			'$scope',
			'TimeSheetService',
			function WorkerWeekSelectionModalCtrlFn($log, $scope, TimeSheetService) {
				$scope.timeSheetStatusList = [];
				$scope.timeSheetStatusIsOk = false;
				var defaultStatus = 'Choose the week by date.';
				$scope.status = defaultStatus;

				$scope.$watch('currentWorker.id', function currentWorkerId$watchFn(id) {
					if (id) {
						TimeSheetService.getStatusForWorkerId({
							workerId : id
						}, function successFn(list) {
							$scope.timeSheetStatusList = list;
						});
					}
				});

				$scope.$watch('modalSelection', function modalSelection$watchFn(modalDate) {
					$scope.printableTimeSheetString = false;
					var found = null;
					if (modalDate && $scope.timeSheetStatusList) {
						var firstDate = $scope.timeSheetStatusList[0].startDate;
						angular.forEach($scope.timeSheetStatusList, function(timeSheet) {
							var tsDate = timeSheet.startDate;
							if (!found) {
								if (tsDate >= modalDate && firstDate <= modalDate) {
									found = timeSheet;
									return;
								}
							}
						});
						if (found) {
							if (angular.equals(found.status, "NOT_CREATED") && found.printableTimeSheetString) {
								$scope.status = '';
								$scope.printableTimeSheetString = found.printableTimeSheetString;
							} else {
								$scope.status = 'A time sheet for <i>' + found.printableTimeSheetString
										+ '</i> already exists.  Choose another date.';
							}
							return;
						}
					}
					$scope.status = defaultStatus;
				});

				$scope.close = function closeFn() {
					$('#workerWeekSelectionModal').modal('hide');
				};

				$scope.select = function selectFn(date) {
					if (date && $scope.printableTimeSheetString) {
						var parms = {
							workerId : $scope.currentWorker.id,
							date : date
						};
						$scope.getTimeSheetForWorkerDate(parms, $scope.close);
					}
				};

				$scope.open = function openFn() {
					$scope.modalSelection = null;
					$('#workerWeekSelectionModal').modal('show');
				};

			} ]);

})();