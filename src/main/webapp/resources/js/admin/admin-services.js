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
				url : 'admin/contract/client/:clientId'
			}
		});
	} ]);

	module.factory('AdminStatsService', [ '$resource',//
	function AdminStatsServiceFn($resource) {
		return $resource('admin/stats/:type/:subtype', {}, {
			// methods 'get', 'save', 'remove' provided by default
            getServices : {
                method : 'GET',
                url : 'admin/stats/service'
            }
		});
	} ]);

	module.factory('AdminReportService', [ '$resource',//
	function AdminReportServiceFn($resource) {
		return $resource('admin/report/:id/:client/:startDate/:endDate', {}, {
			// methods 'get', 'save', 'remove' provided by default
			getTimeSheetsPerWorkerByMonthForClientReport : {
				method : 'GET',
				url : 'admin/report/timesheets-per-worker-by-month-for-client/:clientId/:date'
			},
			getTotalHoursPerWorkerPerMonthReport : {
				method : 'GET',
				url : 'admin/report/total-hours-per-worker-per-month/:clientId/:date'
			}
		});
	} ]);

	console && console.info("redefining TimeSheetService for Admin...");
	module.factory('AdminTimeSheetService', [ '$resource',//
	function AdminTimeSheetService($resource) {
		return $resource('admin/timesheet/:id/:date', {}, {
			// methods 'get', 'save', 'remove' provided by default
			flag : {
				method : 'POST',
				params : {
					tid : '@id',
					flagged : '@flagged'
				},
				url : 'admin/timesheet/flag/:tid/:flagged'
			},
			getAll : {
				isArray : true,
				method : 'GET'
			},
			getAllForClient : {
				isArray : true,
				method : 'GET',
				url : 'admin/timesheet/client/:clientId'
			},
			getAllForWorker : {
				isArray : true,
				method : 'GET',
				url : 'admin/timesheet/worker/:workerId'
			},
			getForWorkerIdDate : {
				method : 'GET',
				url : 'admin/timesheet/worker/:workerId/date/:date'
			},
			getCurrent : {
				method : 'GET',
				url : 'admin/timesheet/current'
			},
			getLast : {
				method : 'GET',
				url : 'admin/timesheet/last'
			},
			getNext : {
				method : 'GET',
				url : 'admin/timesheet/next'
			},
			getNew : {
				method : 'GET',
				url : 'admin/timesheet/new'
			},
			getStatusForWorkerId : {
				isArray : true,
				method : 'GET',
				url : 'admin/timesheet/status/worker/:workerId'
			},
			getWeek : {
				method : 'GET'
			},
			submit : {
				method : 'POST',
				params : {
					tid : '@id'
				},
				url : 'admin/timesheet/submit/:tid'
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

	module.factory('AdminUnlinkedUserService', [ '$resource',//
	function AdminUnlinkedUserServiceFn($resource) {
		return $resource('admin/unlinkeduser', {}, {
			// methods 'get', 'save', 'remove' provided by default
			getAll : {
				isArray : true,
				method : 'GET'
			},
		});
	} ]);

})();