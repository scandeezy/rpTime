'use strict';
(function appJsFn() {
	// Declare app level module which depends on filters, and services
	// angular.module('myApp', [ 'myApp.controllers' ]);
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
			templateUrl : 'resources/partials/client.html'
		});
		$routeProvider.when('/history', {
			controller : 'HistoryPageCtrl',
			templateUrl : 'resources/partials/history.html'
		});
		$routeProvider.when('/landing', {
			controller : 'LandingPageCtrl',
			templateUrl : 'resources/partials/landing.html'
		});
		$routeProvider.when('/timesheet', {
			controller : 'TimesheetPageCtrl',
			templateUrl : 'resources/partials/timesheet.html'
		});
		$routeProvider.when('/worker', {
			controller : 'AdminWorkerCtrl',
			templateUrl : 'resources/partials/worker.html'
		});
		$routeProvider.otherwise({
			redirectTo : '/'
		});

	} ]);
})();