'use strict';
(function appJsFn() {
	angular.module('myApp', [ 'ngRoute', 'ngAnimate', 'ngCookies', 'ngSanitize', //
	'myApp.filters', 'myApp.services', 'myApp.directives', 'myApp.controllers' ])//
	.config([ '$routeProvider', //
	function($routeProvider) {
		console.log('configuring endpoints');
		$routeProvider
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
		.when('/timesheet', {
			controller : 'TimeSheetPageCtrl',
			templateUrl : 'resources/partials/timesheet.html'
		})//
		.otherwise({
			redirectTo : '/timesheet'
		});
	} ]);
})();