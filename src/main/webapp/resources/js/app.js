'use strict';
(function appJsFn() {
	// Declare app level module which depends on filters, and services
	angular.module('myApp', [ 'ngRoute', 'ngAnimate', 'ngCookies', 'ngSanitize', //
	'myApp.filters', 'myApp.services', 'myApp.directives', 'myApp.controllers' ])//
	.config([ '$routeProvider', //
	function($routeProvider) {
		console.log('configuring');
		$routeProvider.when('/', {
			controller : 'LandingPageCtrl',
			templateUrl : 'resources/partials/landing.html'
		})//
		.when('/about', {
			templateUrl : 'resources/partials/about.html'
		})//
		.when('/client', {
			controller : 'AdminClientCtrl',
			templateUrl : 'resources/partials/client.html'
		})//
		.when('/contract', {
			controller : 'AdminContractCtrl',
			templateUrl : 'resources/partials/contract.html'
		})//
		.when('/error', {
			templateUrl : 'resources/partials/error.html'
		})//
		.when('/history', {
			controller : 'HistoryPageCtrl',
			templateUrl : 'resources/partials/history.html',
			reloadOnSearch : false
		})//
		.when('/landing', {
			controller : 'LandingPageCtrl',
			templateUrl : 'resources/partials/landing.html'
		})//
		.when('/report/:id', {
			controller : 'ReportPageCtrl',
			templateUrl : 'resources/partials/reports/list.html'
		})//
		.when('/timesheet', {
			controller : 'TimeSheetPageCtrl',
			templateUrl : 'resources/partials/timesheet.html'
		})//
		.when('/worker', {
			controller : 'AdminWorkerCtrl',
			templateUrl : 'resources/partials/worker.html'
		})//
		.otherwise({
			redirectTo : '/'
		});
	} ]);
})();