'use strict';
(function() {
	// omit [] to use existing controller: http://stackoverflow.com/a/17289451/237225
	var module = angular.module('myApp.controllers');

	module.controller('ReportPageCtrl', [ '$log', '$location', '$routeParams', '$scope', //
	function ReportPageCtrlFn($log, $location, $routeParams, $scope) {
		$log.info('ReportPageCtrl init', [$location, $routeParams, $scope]);
		$scope.selection = $routeParams.id || '';
	} ]);
	
	module.controller('ReportTotalHoursPerWorkerPerMonthCtrl', [ '$log', '$location', '$routeParams', '$scope', //
  	function ReportTotalHoursPerWorkerPerMonthCtrlFn($log, $location, $routeParams, $scope) {
	//ReportTotalHoursPerWorkerPerMonthCtrl
		$log.info('ReportTotalHoursPerWorkerPerMonthCtrl init', $location, $routeParams, $scope);

	} ]);
	
})();