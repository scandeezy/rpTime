'use strict';
(function() {
	// omit [] to use existing controller: http://stackoverflow.com/a/17289451/237225
	var module = angular.module('myApp.controllers');

	module.controller('TimeSheetPageCtrl', [ '$location', '$log', '$routeParams', '$scope', 'AdminClientService', 'TimeSheetService', //
	function TimeSheetPageCtrlFn($location, $log, $routeParams, $scope, AdminClientService, TimeSheetService) {
		$scope.edit = false;
		$scope.timeSheetsMap = {};
		$scope.currentTimeSheet = {};
		$scope.clientsMap = AdminClientService.getAll();

		if ($routeParams.id) {
			var id = $routeParams.id;
			$log.info('getting id', id);
			TimeSheetService.get({
				id : id
			}, function successFn(data) {
				$scope.currentTimeSheet = data;
				$scope.edit = true;
			});
		}

		function updateTimeSheetsFn() {
			TimeSheetService.getAll(function successFn(data) {
				$scope.timeSheetsMap = data;
				$log.info('found ' + Object.keys($scope.timeSheetsMap).length + ' timeSheets');
			});
		}

		$scope.isSubmittable = function isSubmittableFn(timeSheet) {
			return timeSheet.status === 'UNSUBMITTED';
		}

		$scope.remove = function removeFn(obj) {
			$scope.doRemove({
				service : TimeSheetService,
				id : obj.id,
				map : $scope.timeSheetsMap,
				afterFn : function doAfterFn() {
					delete $scope.timeSheetsMap[obj.id];
				}
			});
		};

		$scope.save = function saveFn(obj) {
			$scope.doSave({
				service : TimeSheetService,
				obj : obj,
				map : $scope.timeSheetsMap,
				afterFn : function doAfterFn() {
					// stay in the edit view!
					// $scope.edit = false;
					$scope.createTimeSheetForm.$setPristine();
				}
			});
		};

		$scope.set = function setFn(obj) {
			if (!obj) {
				var o = TimeSheetService.create();
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
			TimeSheetService.get({
				id : "new",
				date : date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
			}, function successFn(data) {
				$scope.set(data);
			});
		};

		$scope.setWeekNext = function setWeekNextFn() {
			var date = new Date();
			date.setDate(date.getDate() + 7);
			TimeSheetService.get({
				id : "new",
				date : date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
			}, function successFn(data) {
				$scope.set(data);
			});
		};

		$scope.setWeekOther = function setWeekOtherFn() {
			var data = window.showModalDialog("datePickerModal.html");
			$log.error(data.datePicked);
			TimeSheetService.get({
				id : "new",
				date : data.datePicked
			}, function successFn(data) {
				$scope.set(data);
			});
		};

		$scope.submit = function submitFn(timeSheet) {
			$log.info("submitting timeSheet.id= " + timeSheet.id);
			TimeSheetService.submit({
				id : timeSheet.id
			}, function successFn() {
				timeSheet.status = 'SUBMITTED';
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

		$log.info('TimeSheetPageCtrl init', $scope);

	} ]);

	module.controller('TimeSheetDayCtrl', [ '$log', '$scope',//
	function TimeSheetDayCtrlFn($log, $scope) {

		$scope.showButtons = false;

		$scope.$watch('entry', function entry$watchFn(e) {
			$scope.showButtons = ((e.startTime || e.endTime) && !(e.startTime === e.endTime));
		}, true);

		$scope.addNewTimeCardLogEntry = function addNewTimeCardLogEntryFn(day) {
			var last = day.entries[(day.entries.length - 1)];
			day.entries.push({
				'workerId' : last.workerId,
				'clientId' : last.clientId,
				'date' : last.date,
				'startTime' : last.endTime,
				'endTime' : null
			});
			$scope.createTimeSheetForm.$setDirty();
		};

		$scope.removeTimeCardLogEntry = function removeTimeCardLogEntryFn(entry) {
			if ($scope.day && $scope.day.entries) {
				var index = $scope.day.entries.indexOf(entry) || (($scope.day.entries.length) - 1);
				$scope.day.entries.splice(index, 1);
			}
			$scope.createTimeSheetForm.$setDirty();
		};

		$scope.setEntryClientId = function setEntryClientIdFn(id) {
			$scope.entry.clientId = id;
			$scope.createTimeSheetForm.$setDirty();
		};

	} ]);

})();
