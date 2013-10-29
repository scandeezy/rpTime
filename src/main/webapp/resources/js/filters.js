'use strict';
(function() {
	
var module = angular.module('myApp.filters', []);

module.filter('interpolate', ['version', function(version) { //
    return function interpolateFn(text) {
        return String(text).replace(/\%VERSION\%/mg, version);
      };
}]);

module.value('dayOfWeekArr',
// Preferred:
//		{ //
// 	"0" : { code: "0", description: "Sunday" }, 
// 	"1" : { code: "1", description: "Monday" },
// 	"2" : { code: "2", description: "Tuesday" },
// 	"3" : { code: "3", description: "Wednesday" },
// 	"4" : { code: "4", description: "Thursday" },
// 	"5" : { code: "5", description: "Friday" },
// 	"6" : { code: "6", description: "Saturday" }
//}
	[	
	 	{ code: 0, description: "Sunday" }, 
	 	{ code: 1, description: "Monday" },
	 	{ code: 2, description: "Tuesday" },
	 	{ code: 3, description: "Wednesday" },
	 	{ code: 4, description: "Thursday" },
	 	{ code: 5, description: "Friday" },
	 	{ code: 6, description: "Saturday" }
	]
);

})();
