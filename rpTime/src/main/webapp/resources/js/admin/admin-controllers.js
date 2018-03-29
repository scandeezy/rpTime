'use strict';
(function() {
	// omit [] to use existing controller: http://stackoverflow.com/a/17289451/237225
	var module = angular.module('myApp.controllers');

	module.controller('AdminPageCtrl', [ // 
	'$location', '$log', '$rootScope', '$scope', 'AdminClientService',//
	'AdminContractService', 'AdminWorkerService', 'AdminUnlinkedUserService', 'AdminTimeSheetService',//
	function AdminPageCtrlFn($location, $log, $rootScope, $scope,// 
	AdminClientService, AdminContractService, AdminWorkerService, AdminUnlinkedUserService, AdminTimeSheetService) {
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
			AdminTimeSheetService.flag({
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
			AdminTimeSheetService.getForWorkerIdDate(parms, function successFn(data) {
				closeModalFn();
				$location.path('timesheetadmin').search('id', data.id);
			});
		};

		updateClientsFn();
		updateContractsFn();
		updateWorkersFn();

		// $log.info('AdminPageCtrl init complete');
	} ]);

	module.controller('WorkerWeekSelectionModalCtrl', [ //
			'$log',
			'$scope',
			'AdminTimeSheetService',
			function WorkerWeekSelectionModalCtrlFn($log, $scope, AdminTimeSheetService) {
				$scope.timeSheetStatusList = [];
				$scope.timeSheetStatusIsOk = false;
				var defaultStatus = 'Choose the week by date.';
				$scope.status = defaultStatus;

				$scope.$watch('currentWorker.id', function currentWorkerId$watchFn(id) {
					if (id) {
                        $log.debug("About to update statuses for id ", id);
						AdminTimeSheetService.getStatusForWorkerId({
							workerId : id
						}, function successFn(list) {
                            $log.info("Successfully pulled down latest timesheetstatuslist. ", list.length);
							$scope.timeSheetStatusList = list;
						});
					}
				});

				$scope.$watch('modalSelection', function modalSelection$watchFn(modalDate) {
					$scope.printableTimeSheetString = false;
					var found = null;
					if (modalDate && $scope.timeSheetStatusList) {
                        $log.info("element 0 of timeSheetStatusList is ", $scope.timeSheetStatusList[0]);
						var firstDate = $scope.timeSheetStatusList[0].startDate;
                        var searchDate = formatDate(modalDate);
						angular.forEach($scope.timeSheetStatusList, function(timeSheet) {
							var tsDate = timeSheet.startDate;
							if (!found) {
								if (tsDate >= searchDate && firstDate <= searchDate) {
                                    $log.debug("tsDate is ", tsDate, ", modalDate is ", searchDate, ", firstDate is ", firstDate);
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
                
                function formatDate(modalDate) {
                    $log.debug("Pre date is ", modalDate);
                    modalDate = new Date(modalDate);
                    var dateMonth = modalDate.getDate();
                    var dateWeek = modalDate.getDay();
                    var setDate = dateMonth - dateWeek;
                    modalDate.setUTCDate(setDate);
                    $log.debug("Date after set ", modalDate);
                    var searchDate = modalDate.getFullYear() + "-";
                    if(modalDate.getMonth() > 9) {
                        searchDate = searchDate + (modalDate.getMonth() + 1) + "-";
                    } else {
                        searchDate = searchDate + "0" + (modalDate.getMonth() + 1) + "-";
                    }
                    if(modalDate.getDate() > 9) {
                        searchDate = searchDate + modalDate.getDate();
                    } else {
                        searchDate = searchDate + "0" + modalDate.getDate();
                    }
                    
                    return searchDate;
                }

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