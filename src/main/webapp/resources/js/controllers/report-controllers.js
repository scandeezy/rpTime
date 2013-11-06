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

	module.controller('ReportTotalHoursPerWorkerPerMonthCtrl', [ '$log', '$location', '$routeParams', '$scope', 'AdminReportService', //
	function ReportTotalHoursPerWorkerPerMonthCtrlFn($log, $location, $routeParams, $scope, AdminReportService) {
		$log.info('ReportTotalHoursPerWorkerPerMonthCtrl init', $location, $routeParams, $scope);
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