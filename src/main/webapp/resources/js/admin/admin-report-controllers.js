'use strict';
(function() {
	// omit [] to use existing controller: http://stackoverflow.com/a/17289451/237225
	var module = angular.module('myApp.controllers');

	module.controller('ReportPageCtrl', [ '$log', '$location', '$route', '$routeParams', '$scope', 'TimeSheetService', //
	function ReportPageCtrlFn($log, $location, $route, $routeParams, $scope, TimeSheetService) {
		// $log.info('ReportPageCtrl init', [ '$loc', $location, '$route', $route, '$routeParams', $routeParams, $scope ]);
		$scope.selection = $routeParams.id || '';

		$scope.refresh = function refreshFn() {
			$route.reload();
		};

	} ]);

	module.controller('ReportTimeSheetsPerWorkerByMonthForClientCtrl', [ '$location', '$log', '$routeParams', '$scope', //
	'AdminClientService', 'AdminReportService', //
	function ReportTimeSheetsPerWorkerByMonthForClientCtrlFn($location, $log, $routeParams, $scope, //
	AdminClientService, AdminReportService) {
		// $log.info('ReportTimeSheetsPerWorkerByMonthForClientCtrl init', $routeParams, $scope);

		$scope.selectClient = function selectClientFn(clientId) {
			var d = $routeParams.date;
			if (clientId) {
				$scope.report = AdminReportService.getTimeSheetsPerWorkerByMonthForClientReport({
					clientId : clientId,
					date : d
				}, function successFn(data) {
					$scope.selectedClient = clientId;
					$location.search('clientId', clientId).search("date", d);
				});
			} else {
				$scope.report = null;
				$location.search('clientId', null).search("date", null);
			}
		};

		// init:
		$scope.clientsMap = AdminClientService.getAll();
		$scope.report = null;
		if ($routeParams.clientId) {
			$scope.selectClient($routeParams.clientId);
		}

	} ]);

	module.controller('ReportTimeSheetsPerWorkerByMonthForClientWorkerCtrl', [ '$log', '$scope',//
	function ReportTimeSheetsPerWorkerByMonthForClientWorkerCtrlFn($log, $scope) {
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
		// $log.info('ReportTimeSheetsPerWorkerByRangeForClientCtrlFn init', $routeParams, $scope);

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

	module.controller('ReportTotalHoursPerWorkerPerMonthCtrl', [ '$log', '$routeParams', '$scope', 'AdminReportService', //
	function ReportTotalHoursPerWorkerPerMonthCtrlFn($log, $routeParams, $scope, AdminReportService) {
		// $log.info('ReportTotalHoursPerWorkerPerMonthCtrl init', $scope);
		$scope.report = {};
		var d = $routeParams.date;
		AdminReportService.getTotalHoursPerWorkerPerMonthReport({
			date: d
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
