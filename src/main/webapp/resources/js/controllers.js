'use strict';
(function() {

	var module = angular.module('myApp.controllers', []);

	module.controller('MyPageCtrl', [ '$log', '$scope', function MyPageCtrlFn($log, $scope) {
		$log.info('StatsPageCtrl init', $scope);
	} ]);
	
	module.controller('SheetCtrl', [ '$log', '$scope', function SheetCtrlFn($log, $scope) {
		$log.info('SheetCtrl init', $scope);
	} ]);

})();