'use strict';
(function() {
	
	var module = angular.module('myApp.services', [ 'ngResource' ]);

	module.factory('AdminClientService', [ '$resource',//
	function AdminClientServiceFn($resource) {
		return $resource('client/:id', {}, {
			// methods 'get', 'save', 'remove' provided by default
			getAll : {
				method : 'GET',
				isArray : true
			}
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
				},
                                isArray : true
			},
			getAll : {
				method : 'GET',
				isArray : true
			}
		});
	} ]);

	module.factory('AdminWorkerService', [ '$resource',//
	function AdminWorkerServiceFn($resource) {
		return $resource('worker/:id', {}, {
			// methods 'get', 'save', 'remove' provided by default
			getAll : {
				method : 'GET',
				isArray : true
			},
			getMap : {
				method : 'GET',
				params : {
					id : '@_id'
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
				isArray : true
			}
		});
	} ]);

})();