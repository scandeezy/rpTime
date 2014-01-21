'use strict';
(function() {
	// omit [] to use existing controller: http://stackoverflow.com/a/17289451/237225
	var module = angular.module('myApp.controllers');

	module.controller('AdminWorkerCtrl', [ '$location', '$log', '$rootScope', '$routeParams', '$scope', 'AdminWorkerService', //
	function AdminWorkerCtrlFn($location, $log, $rootScope, $routeParams, $scope, AdminWorkerService) {
		$scope.edit = false;
		if ($routeParams.id) {
			var id = $routeParams.id;
			AdminWorkerService.get({
				id : id
			}, function successFn(data) {
				$scope.currentWorker = data;
				$scope.edit = true;
			});
		}
		
		$scope.createFromUnlinked = function createFromUnlinkedFn(user){
			if(user && user.email){
				$scope.set({email : user.email});
			}
		};

		$scope.newRandom = function newRandomFn() {
			var randomNum = Math.floor(Math.random() * 900) + 100;
			var r = {
				firstName : 'first-' + randomNum,
				lastName : 'last-' + randomNum,
				email : 'email-' + randomNum + '@test.com'
			};
			$scope.set(r);
		}

		$scope.remove = function removeFn(obj) {
			$scope.doRemove({
				service : AdminWorkerService,
				id : obj.id,
				map : $rootScope.workersMap,
				afterFn : function doAfterFn() {
					delete $rootScope.workersMap[obj.id];
				}
			});
		};

		$scope.save = function saveFn(obj) {
			$scope.doSave({
				service : AdminWorkerService,
				obj : obj,
				map : $rootScope.workersMap,
				// updateFnName : 'updateWorkers', //trust client-side: server side has delay.
				afterFn : function doAfterFn(savedObj) {
					// $rootScope.workersMap[savedObj.id] = savedObj;
					$scope.edit = false;
				}
			});
		};

		$scope.set = function setFn(obj) {
			$scope.currentWorker = angular.copy(obj, {});
			$scope.edit = true;
			$location.search('id', $scope.currentWorker.id);
			$scope.createWorkerForm.$setPristine();
		};

		$scope.unset = function unsetFn() {
			$scope.edit = false;
			$location.search('id', null);
		};

	} ]);

	module.controller('AdminWorkerRelatedTimeSheetsCtrl', [ '$log', '$scope', '$timeout', 'AdminTimeSheetService', //
	function AdminWorkerRelatedTimeSheetsCtrlFn($log, $scope, $timeout, AdminTimeSheetService) {
		$scope.myRelatedTimeSheets = [];

		var tryXtimes = 20;

		$scope.updateRelatedTimeSheets = function updateRelatedTimeSheetsFn() {
			tryXtimes = tryXtimes - 1;
			if ($scope.currentWorker && $scope.currentWorker.id) {
				var wid = $scope.currentWorker.id;
                $log.debug("Getting all for worker ", wid);
				AdminTimeSheetService.getAllForWorker({
					workerId : wid
				}, function successFn(timeSheetsList) {
					var arr = [];
					angular.forEach(timeSheetsList, function(timeSheet) {
						if (timeSheet.workerId == wid) {
							arr.push(timeSheet);
						}
					});
					$scope.myRelatedTimeSheets = arr;
				});
			} else {
				if (tryXtimes > 0) {
					$timeout(function my$timeoutAsyncCompensationFn() {
						$scope.updateRelatedTimeSheets();
					}, 200);
				}
			}
		};

		$scope.$watch('currentWorker', function currentWorker$watchFn(obj) {
			if (obj) {
				$scope.updateRelatedTimeSheets();
			}
		});

	} ]);

	module.controller('AdminWorkerRelatedContractsCtrl', [ '$log', '$rootScope', '$scope', '$timeout', //
	function AdminWorkerRelatedContractsCtrl($log, $rootScope, $scope, $timeout) {

		$scope.myRelatedContracts = [];
        $scope.hasActiveContract = false;

		$scope.updateRelatedContracts = function updateRelatedContractsFn() {
			if ($scope.currentWorker && $rootScope.contractsMap.$resolved && $rootScope.clientsMap.$resolved) {
				var wid = $scope.currentWorker.id
				var arr = [];
				angular.forEach($rootScope.contractsMap, function(contract, id) {
					if (contract.worker == wid) {
						arr.push({
							contract : contract,
							client : $rootScope.clientsMap[contract.client]
						});
                        
                        if(!contract.expired) {
                            $scope.hasActiveContract = true;
                        }
					}
				});
				$scope.myRelatedContracts = arr;
			} else {
				$log.info('sleeping 200ms for updateRelatedContracts');
				$timeout(function my$timeoutAsyncCompensationFn() {
					$scope.updateRelatedContracts();
				}, 200);
			}
		};

		$scope.$watch('currentWorker', function currentWorker$watchFn(obj) {
			if (obj) {
				$scope.updateRelatedContracts();
			}
		});

	} ]);

})();