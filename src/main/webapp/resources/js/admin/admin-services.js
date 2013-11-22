'use strict';
(function() {
	// omit [] to use existing module: http://stackoverflow.com/a/17289451/237225
	var module = angular.module('myApp.services');

	module.factory('AdminClientService', [ '$resource',//
	function AdminClientServiceFn($resource) {
		return $resource('admin/client/:id', {}, {
			// methods 'get', 'save', 'remove' provided by default
			getAll : {
				method : 'GET'
			}
		});
	} ]);

	module.factory('AdminContractService', [ '$resource',//
	function AdminContractServiceFn($resource) {
		return $resource('admin/contract/:id', {}, {
			// methods 'get', 'save', 'remove' provided by default
			getAll : {
				method : 'GET',
				params : {
					id : 'idmap'
				}
			},
			getAllForClient : {
				isArray : true,
				method : 'GET',
				url : 'contract/client/:clientId'
			}
		});
	} ]);

	module.factory('AdminWorkerService', [ '$resource',//
	function AdminWorkerServiceFn($resource) {
		return $resource('admin/worker/:id', {}, {
			// methods 'get', 'save', 'remove' provided by default
			getAll : {
				method : 'GET',
				params : {
					id : 'idmap'
				}
			}
		});
	} ]);

	module.factory('AdminReportService', [ '$resource',//
	function AdminReportServiceFn($resource) {
		return $resource('admin/report/:id/:client/:startDate/:endDate', {}, {
		// methods 'get', 'save', 'remove' provided by default
		});
	} ]);

	module.factory('AdminTimeSheetService', [ '$resource',//
	function AdminTimeSheetService($resource) {
		return $resource('admin/timesheet/:id/:date', {}, {
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