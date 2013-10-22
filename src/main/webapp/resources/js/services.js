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
			save : {
				method : 'POST',
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
			save : {
				method : 'POST',
			}
		});
	} ]);

})();