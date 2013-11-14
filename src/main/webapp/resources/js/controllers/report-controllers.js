'use strict';
(function() {
	// omit [] to use existing controller: http://stackoverflow.com/a/17289451/237225
	var module = angular.module('myApp.controllers');

	module.controller('ReportPageCtrl', [ '$log', '$location', '$route', '$routeParams', '$scope', //
	function ReportPageCtrlFn($log, $location, $route, $routeParams, $scope) {
		// $log.info('ReportPageCtrl init', [ '$loc', $location, '$route', $route, '$routeParams', $routeParams, $scope ]);
		$scope.selection = $routeParams.id || '';

		$scope.refresh = function refreshFn() {
			$route.reload();
		};

	} ]);

	module.controller('ReportTimeSheetsPerWorkerByWeekForClientCtrl', [ '$location', '$log', '$routeParams', '$scope', //
	'AdminClientService', 'AdminReportService', //
	function ReportTimeSheetsPerWorkerByWeekForClientCtrlFn($location, $log, $routeParams, $scope, //
	AdminClientService, AdminReportService) {
		 $log.info('ReportTimeSheetsPerWorkerByWeekForClientCtrl init', $routeParams, $scope);

		$scope.selectClient = function selectClientFn(clientId) {
			if (clientId) {
				$scope.report = AdminReportService.get({
					id : 'timesheets-per-worker-by-week-for-client',
					client : clientId
				}, function successFn(data) {
					$scope.selectedClient = clientId;
					$location.search('clientId', clientId);
				});
			} else {
				$scope.report = null;
				$location.search('clientId', null);
			}
		};

		// init:
		$scope.clientsMap = AdminClientService.getAll();
		$scope.report = null;
		if ($routeParams.clientId) {
			$scope.selectClient($routeParams.clientId);
		}

	} ]);

	module.controller('ReportTimeSheetsPerWorkerByWeekForClientWorkerCtrl', [ '$log', '$scope',//
	function ReportTimeSheetsPerWorkerByWeekForClientWorkerCtrlFn($log, $scope) {
		var workerReportMap = $scope.report.reportMap[$scope.worker.id];
		$scope.rmap = Object.keys(workerReportMap).length;

		$scope.totalDaysCount = 0;
		$scope.totalHoursSum = 0;

		angular.forEach(workerReportMap, function(hoursValue, dateKey) {
			$scope.totalHoursSum += hoursValue;
			if (hoursValue > 0) {
				$scope.totalDaysCount++;
			}
		});

		$scope.getTimeSheetLinkForDate = function getTimeSheetLinkForDateFn(workerId, date) {
			return $scope.report.workerToDateToTimeSheetMap[workerId][date];
		};
		
	} ]);

	module.controller('ReportTimeSheetsPerWorkerByRangeForClientCtrl', [ '$location', '$log', '$routeParams', '$scope', //
	'AdminClientService', 'AdminReportService', //
	function ReportTimeSheetsPerWorkerByRangeForClientCtrlFn($location, $log, $routeParams, $scope, //
	AdminClientService, AdminReportService) {
		// $log.info('ReportTimeSheetsPerWorkerByWeekForClientCtrl init', $routeParams, $scope);

		$scope.generateReport = function generateReportFn(clientId, startDate, endDate) {
			$log.info("Generating report for client " + clientId + " with start " + startDate + " and end " + endDate);
			if (clientId) {
				$scope.report = AdminReportService.get({
					id : 'hours-recorded',
					client : clientId,
					startDate : startDate,
					endDate : endDate
				}, function successFn(data) {
					$scope.report = data;
				});
			} else {
				$scope.report = null;
				$location.search('clientId', null);
			}
		};

		// init:
		$scope.clientsMap = AdminClientService.getAll();
		$scope.report = null;
		if ($routeParams.clientId) {
			$scope.selectClient($routeParams.clientId);
		}

	} ]);

	module.controller('ReportTotalHoursPerWorkerPerMonthCtrl', [ '$log', '$scope', 'AdminReportService', //
	function ReportTotalHoursPerWorkerPerMonthCtrlFn($log, $scope, AdminReportService) {
		// $log.info('ReportTotalHoursPerWorkerPerMonthCtrl init', $scope);
		$scope.report = {};
		AdminReportService.get({
			id : 'total-hours-per-worker-per-month'
		}, function successFn(data) {
			$scope.report = data;
		});

		$scope.totalHours = 0;
		$scope.$watch('report.workerIdToHoursMap', function(map) {
			var totHrs = 0;
			$log.info('foreach on ', map);
			angular.forEach(map, function(val, key) {
				$log.info(key, val);
				totHrs = totHrs + val;
			});
			$scope.totalHours = totHrs;
		}, true);
	} ]);

})();
