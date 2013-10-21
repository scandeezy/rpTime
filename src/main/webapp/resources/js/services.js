'use strict';
(function() {

	console.log('loading services');

	// Demonstrate how to register services
	// In this case it is a simple value service.
	var module = angular.module('myApp.services', [ 'ngResource' ]).value('version', '0.1');

	// module.factory('RestService', [ '$resource',//
	// function RestServiceFn($resource) {
	// return $resource(serviceCatalog.myAppUrlRoot + 'answer/new', {}, {
	// create : {
	// method : 'GET'
	// }
	// });
	// } ]);

	module.factory('ClientService', [ '$resource',//
	function ClientServiceFn($resource) {
		 return $resource('clients', {}, {
		 create : {
		 method : 'GET'
		 }
		 });
	} ]);

})();