'use strict';
(function() {

	var module = angular.module('myApp.services', [ 'ngResource' ]);

	module.factory('AboutService', [ '$resource',//
	function AboutServiceFn($resource) {
		return $resource('about', {}, {});
	} ]);

	module.factory('AdminClientService', [ '$resource',//
	function AdminClientServiceFn($resource) {
		return $resource('client/:id', {}, {
			// methods 'get', 'save', 'remove' provided by default
			getAll : {
				method : 'GET',
				params : {
					id : 'idmap'
				}
			}
		});
	} ]);

	module.factory('AdminContractService', [ '$resource',//
	function AdminContractServiceFn($resource) {
		return $resource('contract/:id', {}, {
			// methods 'get', 'save', 'remove' provided by default
			getAll : {
				method : 'GET',
				params : {
					id : 'idmap'
				}
			}
		});
	} ]);

	module.factory('AdminWorkerService', [ '$resource',//
	function AdminWorkerServiceFn($resource) {
		return $resource('worker/:id', {}, {
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
		return $resource('report/:id/:client/:startDate/:endDate', {}, {
		// methods 'get', 'save', 'remove' provided by default
		});
	} ]);

	module.factory('TimeSheetService', [ '$resource',//
	function TimeSheetService($resource) {
		return $resource('timesheet/:id/:date', {}, {
			// methods 'get', 'save', 'remove' provided by default
			create : {
				method : 'GET',
				params : {
					id : 'new'
				}
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
			getCurrent : {
				method : 'GET',
				params : {
					id : 'current'
				}
			},
			getLast : {
				method : 'GET',
				params : {
					id : 'last'
				}
			},
			getNext : {
				method : 'GET',
				params : {
					id : 'next'
				}
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