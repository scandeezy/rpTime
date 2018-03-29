'use strict';
(function() {

	var module = angular.module('myApp.filters', []);

	module.filter('interpolate', [ 'version', function(version) { //
		return function interpolateFn(text) {
			return String(text).replace(/\%VERSION\%/mg, version);
		};
	} ]);

	module.filter('truncatedtimestamp', function truncatedtimestampFilterFn() { //
		return function truncatedtimestampFilterFnFn(text) {
			if (text && text.length == 23) {
				return String(text).substr(0, 16);
			}
			return text;
		};
	} );

	module.value('dayOfWeekArr', [ {
		code : 0,
		description : "Sunday"
	}, {
		code : 1,
		description : "Monday"
	}, {
		code : 2,
		description : "Tuesday"
	}, {
		code : 3,
		description : "Wednesday"
	}, {
		code : 4,
		description : "Thursday"
	}, {
		code : 5,
		description : "Friday"
	}, {
		code : 6,
		description : "Saturday"
	} ]);

})();
