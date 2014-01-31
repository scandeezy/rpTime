'use strict';
(function() {
	// omit [] to use existing controller: http://stackoverflow.com/a/17289451/237225
	var module = angular.module('myApp.controllers');

	module.controller('AdminContractCtrl', [ '$cookies', '$location', '$log', '$rootScope', '$routeParams', '$scope',//
	'AdminContractService', 'AdminWorkerService', //
	function AdminContractCtrlFn($cookies, $location, $log, $rootScope, $routeParams, $scope, AdminContractService, AdminWorkerService) {
		$scope.showExpiredContractsModel = true && angular.equals($cookies.showExpiredContracts,'true') ;
		$scope.edit = false;
		$scope.currentContract = {};

		if ($routeParams.id) {
			var id = $routeParams.id;
			AdminContractService.get({
				id : id
			}, function successFn(data) {
				$scope.currentContract = data;
				$scope.edit = true;
			});
		}

		$scope.newForClient = function newForClientFn(client) {
			$scope.set({
				client : client.id
			});
		}
		
		$scope.newRandom = function newRandomFn() {
			var randomNum = Math.floor(Math.random() * 900) + 100;
			var worker = $rootScope.workersList[(randomNum % $rootScope.workersList.length)];
			randomNum = Math.floor(Math.random() * 900) + 100;
			var client = $rootScope.clientsList[(randomNum % $rootScope.clientsList.length)];
			
			var r = {
				worker : worker.id,
				client : client.id,
				onSite: true,
				startDate : '2013-01-01',
				startDayOfWeek : client.startDayOfWeek,
				endDate : '2015-12-31',
				lunchRequired : false,
			};
			$scope.save(r);
		}


		$scope.remove = function removeFn(obj) {
			$scope.doRemove({
				service : AdminContractService,
				id : obj.id,
				map : $rootScope.contractsMap,
				afterFn : function doAfterFn() {
					delete $rootScope.contractsMap[obj.id];
				}
			});
		};

		$scope.save = function saveFn(obj) {
			$scope.doSave({
				service : AdminContractService,
				obj : obj,
				map : $rootScope.contractsMap,
				// updateFnName : 'updateContracts', //trust client-side: server side has delay.
				afterFn : function doAfterFn() {
					$scope.edit = false;
				}
			});
		};

		$scope.set = function setFn(obj) {
			$scope.currentContract = angular.copy(obj, {});
			$scope.edit = true;
			$location.search('id', $scope.currentContract.id);
			$scope.createContractForm.$setPristine();
            
            // Scroll to top
            $('html,body').animate({
              scrollTop: 0
            }, 1000);
		};

		$scope.unset = function unsetFn() {
			$scope.edit = false;
			$location.search('id', null);
		};

		$scope.$watch('showExpiredContractsModel', function showExpiredContractsModel$watchFn(val) {
			if (!angular.equals(val, undefined)) {
				$cookies.showExpiredContracts = ""+val;
			}
		});

	} ]);

})();