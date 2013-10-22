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
		$routeProvider.when('/clients', {
			controller : 'ClientsPageCtrl',
			templateUrl : 'resources/partials/clients.html'
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
		$routeProvider.when('/workers', {
			controller : 'WorkersPageCtrl',
			templateUrl : 'resources/partials/workers.html'
		});
		$routeProvider.otherwise({
			redirectTo : '/'
		});

	} ]);
})();