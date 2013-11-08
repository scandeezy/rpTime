'use strict';
(function() {
	// omit [] to use existing controller: http://stackoverflow.com/a/17289451/237225
	var module = angular.module('myApp.controllers');

	module.controller('TimeSheetPageCtrl', [ '$location', '$log', '$routeParams', '$scope', 'AdminClientService', 'TimeSheetService', //
	function TimeSheetPageCtrlPageCtrlFn($location, $log, $routeParams, $scope, AdminClientService, TimeSheetService) {
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

		$scope.addNewTimeCardLogEntry = function addNewTimeCardLogEntryFn(day) {
			var first = day.entries[0];
			day.entries.push({
				'workerId' : first.workerId,
				'clientId' : first.clientId,
				'date' : first.date,
				'startTime' : first.endTime,
				'endTime' : first.endTime
			});
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
			TimeSheetService.get(
                                {
                                        id : "new",
                                        date : date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
                                },
                                function successFn(data) {
                                        $scope.set(data);
                                }
                        );
		};

		$scope.setWeekNext = function setWeekNextFn() {
                        var date = new Date();
                        date.setDate(date.getDate() + 7);
			TimeSheetService.get(
                                {
                                        id : "new",
                                        date : date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
                                },
                                function successFn(data) {
                                        $scope.set(data);
                                }
                        );
		};

		$scope.setWeekOther = function setWeekOtherFn() {
                        var data = window.showModalDialog("datePickerModal.html");
                        $log.error(data.datePicked);
                        TimeSheetService.get({
                                        id : "new",
                                        date : data.datePicked
                                }, function successFn(data) {
                                        $scope.set(data);
                                }
                        );
		};

		$scope.unset = function unsetFn() {
			$scope.edit = false;
			$location.search('id', null);
		};

		$scope.$on('updateTimeSheets', updateTimeSheetsFn);

		updateTimeSheetsFn();

		$log.info('TimeSheetPageCtrl init', $scope);

	} ]);

})();
