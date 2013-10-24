'use strict';
(function() {

	console.log('loading controllers')

	var module = angular.module('myApp.controllers', []);

	module.controller('MainCtrl', [ '$log', '$scope', '$location',//
	function MainCtrlFn($log, $scope, $location) {
		$scope.debug = (($.cookie('debug') == 'true') ? true : false);
		$log.info('MainCtrl init', $location, $scope.debug);

		$scope.$watch(function() {
			return $location.path();
		}, function(newLoc, oldLoc) {
			$scope.page = newLoc;
		});

		$scope.isAdmin = false;

		$scope.setAdmin = function(bool) {
			$scope.isAdmin = bool; // true if Admin, false if User
		};

		$scope.doSave = function doSaveFn(svc, obj, updateFn) {
			var objJson = angular.toJson(obj);
			svc.save(objJson, function saveSuccessFn(data) {
				$log.info("saved data:", data);
			});
			$log.info("do ($broadcast) update '" + updateFn + "'");
			$scope.$broadcast(updateFn);
		};
                
		$log.info('MainCtrl init complete');
	} ]);

	module.controller('AdminPageCtrl', [ '$log', '$scope', 'AdminClientService', 'AdminWorkerService',  'AdminContractService',//
	function AdminPageCtrlFn($log, $scope, AdminClientService, AdminWorkerService, AdminContractService) {
		$log.info('AdminPageCtrl init', $scope);
		$scope.setAdmin(true); // inherited fn from UserNavCtrl
		$scope.clients = [];
		$scope.workers = [];
                
                $scope.contracts = [];
		
		function updateClientsFn(){
			AdminClientService.getAll(function successCGetAllFn(data){
				$scope.clients = data;
			});
		}
		;

		function updateWorkersFn() {
			AdminWorkerService.getAll(function successWGetAllFn(data) {
				$scope.workers = data;
			});
		};
                
                function updateContractsFn() {
                        AdminContractService.getAll(function successConGetAllFn(data){
                                $scope.contracts = data;
                        });
                };
		
		$scope.$on('updateClients',updateClientsFn);
		$scope.$on('updateWorkers',updateWorkersFn);
                $scope.$on('updateContracts', updateContractsFn);
		
		updateClientsFn();
		updateWorkersFn();
                updateContractsFn();
                
		$log.info('AdminPageCtrl init complete');
	} ]);

	module.controller('AdminClientCtrl', [ '$log', '$scope', 'AdminClientService', //
	function AdminClientCtrlFn($log, $scope, AdminClientService) {
		$log.info('AdminClientCtrl init', $scope);

		$scope.save = function(obj) {
			$scope.doSave(AdminClientService, obj, 'updateClients');
		};
	} ]);

	module.controller('AdminWorkerCtrl', [ '$log', '$scope', 'AdminWorkerService', //
	function AdminWorkerCtrlFn($log, $scope, AdminWorkerService) {
		$log.info('AdminWorkerCtrl init', $scope);

		$scope.save = function(obj) {
			$scope.doSave(AdminWorkerService, obj, 'updateWorkers');
		};
	} ]);

	module.controller('AdminWorkerDetailCtrl', [ '$log', '$scope', 'AdminWorkerService', //
	function AdminWorkerDetailCtrlFn($log, $scope, AdminWorkerService) {
		$log.info('AdminWorkerDetailCtrl init', $scope);
		$scope.edit = false;

		$scope.yo = function(obj) {
			console.log(obj);
		};
  	} ]);
	
	module.controller('AdminContractCtrl', [ '$log', '$scope', 'AdminContractService', //
  	function AdminContractCtrlFn($log, $scope, AdminContractService) {
  		$log.info('AdminContractCtrl init', $scope);

		$scope.save= function(obj){
			$scope.doSave(AdminContractService, obj, 'updateContracts');
		};
  	} ]);
    
        module.controller('AdminContractDetailCtrl', [ '$log', '$scope', 'AdminContractService', //
        function AdminContractDetailCtrlFn($log, $scope, AdminWorkerService) {
                $log.info('AdminContractDetailCtrl init', $scope);
                $scope.edit = false;
                $scope.yo = function(obj) {
                console.log(obj);
                };
        } ]);

	module.controller('LandingPageCtrl', [ '$log', '$scope', //
	function LandingPageCtrlFn($log, $scope) {
		$log.info('LandingPageCtrl init', $scope);
	} ]);

	module.controller('HistoryPageCtrl', [ '$log', '$scope', //
	function HistoryPageCtrlFn($log, $scope) {
		$log.info('HistoryPageCtrl init', $scope);
	} ]);

	module.controller('TimeSheetPageCtrl', [ '$log', '$scope', 'TimeSheetService', //
	function TimeSheetPageCtrlPageCtrlFn($log, $scope, TimeSheetService) {
		$scope.edit = false;
		$scope.currentTimeSheet = {};
		$log.info('TimeSheetPageCtrl init', $scope);
		$scope.dows = [ 'Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat' ]

		function updateTimeSheetsFn() {
			TimeSheetService.getAll(function successFn(data) {
				$scope.timesheets = data;
			});
		}
		;

		$scope.create = function() {
			TimeSheetService.create(function successFn(data) {
				$scope.currentTimeSheet = data;
			});
		}
		$scope.selectTimeSheet = function(ts) {
			$scope.currentTimeSheet = ts;
		};
		$scope.save = function(ts) {
			$scope.doSave(TimeSheetService, ts, 'updateTimeSheets');
		};

		$scope.$on('updateTimeSheets', this.updateTimeSheetsFn);

		updateTimeSheetsFn();

	} ]);

	module.controller('UserPageCtrl', [ '$log', '$scope', //
	function UserPageCtrlFn($log, $scope) {
		$log.info('UserPageCtrl init', $scope);
		$scope.setAdmin(false); // inherited fn from UserNavCtrl
	} ]);

})();