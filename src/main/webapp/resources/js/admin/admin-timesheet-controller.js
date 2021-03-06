'use strict';
(function() {
	// omit [] to use existing controller: http://stackoverflow.com/a/17289451/237225
	var module = angular.module('myApp.controllers');

	module.controller('TimeSheetAdminPageCtrl', [ '$location', '$log', '$routeParams', '$scope', 'AdminTimeSheetService', //
	function TimeSheetAdminPageCtrlFn($location, $log, $routeParams, $scope, TimeSheetService) {
		$scope.edit = false;
		$scope.adminWorkerTimeSheetMap = {};
		$scope.timeSheetsMap = {};
		$scope.timeSheetsList = [];
		$scope.currentTimeSheet = {};
		$scope.currentAvailableClientsMap = {};

		$scope.$watch('currentTimeSheet.availableClients', function(list) {
			var map = {};
			angular.forEach(list, function(obj) {
				map[obj.id] = obj;
			});
			$scope.currentAvailableClientsMap = map;
		}, true);

		$scope.errorHandlerTimeSheetGetterFn = function errorHandlerFnFn(errorObj) {
			if (errorObj && errorObj.data && errorObj.data.timestamp) {
				$scope.setError(errorObj);
				$location.path('/error');
			} else {
				$log.error("Error: not sure what to do with this object:", errorObj);
			}
		};

		// TimeSheetService will be redefined with admin paths in admin-services.js
		if ($routeParams.id) {
			var id = $routeParams.id;
			TimeSheetService.get({
				id : id
			}, function successFn(data) {
				$scope.set(data);
			}, $scope.errorHandlerTimeSheetGetterFn);
		}

		function updateTimeSheetsFn() {
			TimeSheetService.getAll(function successFn(list) {
				$scope.timeSheetsList = list;
				$scope.timeSheetsMap = {};
				$scope.adminWorkerTimeSheetMap = {};
				for ( var s in list) {
					var sheet = list[s];
					if (sheet.id != undefined) {
						$scope.timeSheetsMap[sheet.id] = sheet;
						var aWTSMList = $scope.adminWorkerTimeSheetMap[sheet.workerId];
						if (!aWTSMList) {
							aWTSMList = $scope.adminWorkerTimeSheetMap[sheet.workerId] = [];
						}
						$scope.adminWorkerTimeSheetMap[sheet.workerId].push(sheet);
					}
				}
			}, $scope.errorHandlerTimeSheetGetterFn);
		}

		$scope.isSubmittable = function isSubmittableFn(timeSheet) {
//			return timeSheet.status === 'UNSUBMITTED';
            return true;
		};
		
		$scope.newRandom = function newRandomFn() {
			$log.info('get new random timesheet');
			var timesheet = TimeSheetService.getRandom();
			$log.info('got timesheet',timesheet);
			updateTimeSheetsFn();
			$log.info('did update');
		};

		$scope.remove = function removeFn(obj) {
			$scope.doRemove({
				service : TimeSheetService,
				id : obj.id,
				map : $scope.timeSheetsMap,
				afterFn : function doAfterFn() {
					delete $scope.timeSheetsMap[obj.id];
					var aWTSMList = $scope.adminWorkerTimeSheetMap[obj.workerId];
					var index = aWTSMList.indexOf(obj) || ((aWTSMList.length) - 1);
					aWTSMList.splice(index, 1);
				}
			});
		};

		$scope.save = function saveFn(obj) {
			$scope.doSave({
				service : TimeSheetService,
				obj : obj,
				map : $scope.timeSheetsMap,
				afterFn : function doAfterFn(obj) {
					// stay in the edit view!
					// $scope.edit = false;
					$scope.currentTimeSheet.updateTimestamp = obj.updateTimestamp;
					$scope.createTimeSheetForm.$setPristine();
					updateTimeSheetsFn();
				}
			});
		};
		
		$scope.selectModalWeekDate = function selectModalWeekDateFn(date){
			$scope.setWeekOther(date);
		};

		$scope.set = function setFn(obj) {
			// $log.info("setting ", obj);
			if (!obj) {
				var o = TimeSheetService.getCurrent();
				var id = o.id;
				if (id) {
					$scope.timeSheetsMap[id] = o;
				}
				$scope.currentTimeSheet = o;
			} else {
				$scope.timeSheetsMap[obj.id] = obj;
				$scope.currentTimeSheet = angular.copy(obj, {});
			}
			$scope.edit = true;
			$location.search('id', $scope.currentTimeSheet.id);
			// $scope.createTimeSheetForm.$setPristine();
		};

		$scope.setWeekLast = function setWeekLastFn() {
			var date = new Date();
			date.setDate(date.getDate() - 7);
			// TODO FIXME: use relative RESTful URLs (lets date comp happen server side)
			TimeSheetService.get({
				id : "new",
				date : date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
			}, function successFn(data) {
				$scope.set(data);
			}, $scope.errorHandlerTimeSheetGetterFn);
		};

		$scope.setWeekNext = function setWeekNextFn() {
			var date = new Date();
			// TODO FIXME: use relative RESTful URLs (lets date comp happen server side)
			date.setDate(date.getDate() + 7);
			TimeSheetService.get({
				id : "new",
				date : date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
			}, function successFn(data) {
				$scope.set(data);
			}, $scope.errorHandlerTimeSheetGetterFn);
		};

		$scope.setWeekOther = function setWeekOtherFn(date) {
			TimeSheetService.get({
				id : "new",
				date : date
			}, function successFn(data) {
				$scope.set(data);
			}, $scope.errorHandlerTimeSheetGetterFn);
		};

		$scope.submit = function submitFn(timeSheet) {
			$log.info("submitting timeSheet.id= " + timeSheet.id);
			// make a copy since we don't want the UI-bound data to change pre-save.
			var obj = angular.copy(timeSheet);
			obj.status = 'SUBMITTED';
			$scope.doSave({
				service : TimeSheetService,
				obj : obj,
				map : $scope.timeSheetsMap,
				afterFn : function doAfterFn() {
					updateTimeSheetsFn();
					$scope.edit = false;
                    $location.search('id',null);
				}
			});
		};

		$scope.unset = function unsetFn() {
			var sheet = $scope.currentTimeSheet;
			// In the case of a new sheet having been created
			if (!$scope.timeSheetsMap[sheet.id]) {
				// Save it if it's not in the list
				$scope.timeSheetsMap[sheet.id] = sheet;
			}
			$scope.edit = false;
			$location.search('id', null);
		};

		$scope.$on('updateTimeSheets', updateTimeSheetsFn);

		updateTimeSheetsFn();

		// $log.info('TimeSheetPageCtrl init', $scope);

	} ]);
})();
