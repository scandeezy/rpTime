'use strict';
(function() {

	var module = angular.module('myApp.services', [ 'ngResource' ]);

	module.factory('AboutService', [ '$resource',//
	function AboutServiceFn($resource) {
		return $resource('about', {}, {});
	} ]);

	module.factory('ClientService', [ '$resource',//
	function ClientServiceFn($resource) {
		// methods 'get', 'save', 'remove' provided by default
		return $resource('client/:id', {}, {
			getAll : {
				method : 'GET'
			}
		});
	} ]);

	module.factory('TimeSheetService', [ '$resource',//
	function TimeSheetService($resource) {
		return $resource('timesheet/:id/:date', {}, {
			// methods 'get', 'save', 'remove' provided by default
			create : {
				method : 'GET',
				url : 'timesheet/new'
			},
			flag : {
				method : 'POST',
				params : {
					tid : '@id',
					flagged : '@flagged'
				},
				url : 'timesheet/flag/:tid/:flagged'
			},
			getAll : {
				isArray : true,
				method : 'GET'
			},
			getAllForClient : {
				isArray : true,
				method : 'GET',
				url : 'timesheet/client/:clientId'
			},
			getAllForWorker : {
				isArray : true,
				method : 'GET',
				url : 'timesheet/worker/:workerId'
			},
			getCurrent : {
				method : 'GET',
				url : 'timesheet/current'
			},
			getLast : {
				method : 'GET',
				url : 'timesheet/last'
			},
			getNext : {
				method : 'GET',
				url : 'timesheet/next'
			},
			getWeek : {
				method : 'GET'
			},
			submit : {
				method : 'POST',
				params : {
					tid : '@id'
				},
				url : 'timesheet/submit/:tid'
			}
		});
	} ]);

})();