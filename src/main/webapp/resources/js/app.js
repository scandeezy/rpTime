'use strict';
(function appJsFn() {
	// Declare app level module which depends on filters, and services
	angular.module('myApp', [ 'ngRoute', 'ngAnimate', 'myApp.filters', 'myApp.services', 'myApp.directives', 'myApp.controllers' ])//
	.config([ '$routeProvider', //
	function($routeProvider) {
		console.log('configuring');
		$routeProvider.when('/', {
			controller : 'LandingPageCtrl',
			templateUrl : 'resources/partials/landing.html'
		});
		$routeProvider.when('/client', {
			controller : 'AdminClientCtrl',
			templateUrl : 'resources/partials/client.html',
			reloadOnSearch : false
		});
		$routeProvider.when('/contract', {
			controller : 'AdminContractCtrl',
			templateUrl : 'resources/partials/contract.html',
			reloadOnSearch : false
		});
		$routeProvider.when('/history', {
			controller : 'HistoryPageCtrl',
			templateUrl : 'resources/partials/history.html',
			reloadOnSearch : false
		});
		$routeProvider.when('/landing', {
			controller : 'LandingPageCtrl',
			templateUrl : 'resources/partials/landing.html'
		});
		$routeProvider.when('/report/:id', {
			controller : 'ReportPageCtrl',
			templateUrl : 'resources/partials/reports/list.html',
			reloadOnSearch : false
		});
		$routeProvider.when('/timesheet', {
			controller : 'TimeSheetPageCtrl',
			templateUrl : 'resources/partials/timesheet.html',
			reloadOnSearch : false
		});
		$routeProvider.when('/worker', {
			controller : 'AdminWorkerCtrl',
			templateUrl : 'resources/partials/worker.html',
			reloadOnSearch : false
		});
		$routeProvider.otherwise({
			redirectTo : '/'
		});

	} ]);
})();