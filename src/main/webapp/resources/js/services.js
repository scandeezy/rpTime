'use strict';
(function() {

	var module = angular.module('myApp.services', [ 'ngResource' ]).value('version', '0.1');

	module.factory('AdminClientService', [ '$resource',//
	function AdminClientServiceFn($resource) {
		return $resource('client/:id', {}, {
			get : {
				method : 'GET',
				params : {
					id : '@_id'
				}
			},
			getAll : {
				method : 'GET',
				isArray : true
			},
			remove : {
				method : 'DELETE',
				params : {
					id : '@_id'
				}
			},
			save : {
				method : 'POST'
			}
		});
	} ]);

	module.factory('TimeSheetService', [ '$resource',//
	function TimeSheetService($resource) {
		return $resource('timesheet/:id', {}, {
			create : {
				method : 'GET',
				params : {
					id : 'new'
				}
			},
			get : {
				method : 'GET',
				params : {
					id : '@_id'
				}
			},
			getAll : {
				method : 'GET',
				isArray : true
			},
			remove : {
				method : 'DELETE',
				params : {
					id : '@_id'
				}
			},
			save : {
				method : 'POST'
			}
		});
	} ]);

	module.factory('AdminWorkerService', [ '$resource',//
	function AdminWorkerServiceFn($resource) {
		return $resource('worker/:id', {}, {
			get : {
				method : 'GET',
				params : {
					id : '@_id'
				}
			},
			getAll : {
				method : 'GET',
				isArray : true
			},
			getMap : {
				method : 'GET',
				params : {
					id : '@_id'
				}
			},
			remove : {
				method : 'DELETE',
				params : {
					id : '@_id'
				}
			},
			save : {
				method : 'POST'
			}
		});
	} ]);

	module.factory('AdminContractService', [ '$resource',//
	function AdminContractServiceFn($resource) {
		return $resource('contract/:id', {}, {
			get : {
				method : 'GET',
				params : {
					id : '@_id'
				}
			},
			getAll : {
				method : 'GET',
				isArray : true
			},
			remove : {
				method : 'DELETE',
				params : {
					id : '@_id'
				}
			},
			save : {
				method : 'POST'
			}
		});
	} ]);

})();