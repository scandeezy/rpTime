'use strict';
(function() {

	var module = angular.module('myApp.services', [ 'ngResource' ]);

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
		return $resource('report/:id', {}, {
		// methods 'get', 'save', 'remove' provided by default
		});
	} ]);

	module.factory('TimeSheetService', [ '$resource',//
	function TimeSheetService($resource) {
		return $resource('timesheet/:id', {}, {
			// methods 'get', 'save', 'remove' provided by default
			create : {
				method : 'GET',
				params : {
					id : 'new'
				}
			},
			getAll : {
				method : 'GET',
				params : {
					id : 'idmap'
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
			}
		});
	} ]);

})();