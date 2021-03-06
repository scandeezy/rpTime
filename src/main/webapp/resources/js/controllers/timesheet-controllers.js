'use strict';
(function() {
	// omit [] to use existing controller: http://stackoverflow.com/a/17289451/237225
	var module = angular.module('myApp.controllers');

	module.controller('TimeSheetPageCtrl', [ '$location', '$log', '$routeParams', '$cookies', '$scope', 'TimeSheetService', 'ContractService',//
	function TimeSheetPageCtrlFn($location, $log, $routeParams, $cookies, $scope, TimeSheetService, ContractService) {
        var id = $location.search().id;
		$scope.edit = id ? true : false;
		$scope.timeSheetsMap = {};
		$scope.timeSheetsList = [];
		$scope.currentTimeSheet = {};
		$scope.currentAvailableClientsMap = {};
        
        // If there are no search params...
        if(Object.keys($location.search()).length == 0) {
            $location.path('/timesheet').search('id', 'current');
        }

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

		if ($routeParams.id) {
			var id = $routeParams.id;
			TimeSheetService.get({
				id : id
			}, function successFn(data) {
				$scope.set(data);
			}, $scope.errorHandlerTimeSheetGetterFn);
		}

        function setContractsFn() {
//            $log.info("Getting contracts for current user.");
            ContractService.getCurrent(function successFn(list) {
//                $log.info("Setting contracts to ", list);
                $scope.contractsList = list;
                $scope.createSheet = list.length == 0 ? false : true;
            });
//            $log.info("Contracts set.");
        }

		function updateTimeSheetsFn() {
			TimeSheetService.getAll(function successFn(list) {
				$scope.timeSheetsList = list;
				$scope.timeSheetsMap = {};
				for ( var s in list) {
					var sheet = list[s];
					if (sheet.id != undefined) {
						$scope.timeSheetsMap[sheet.id] = sheet;
					}
				}
			}, $scope.errorHandlerTimeSheetGetterFn);
		}

		$scope.isSubmittable = function isSubmittableFn(timeSheet) {
//			return timeSheet.status === 'UNSUBMITTED';
            // Hardcoding since we've commented out save.
            return true;
		};

		$scope.remove = function removeFn(obj) {
			$scope.doRemove({
				service : TimeSheetService,
				id : obj.id,
				map : $scope.timeSheetsMap,
				afterFn : function doAfterFn() {
					delete $scope.timeSheetsMap[obj.id];
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
                    $scope.success = true;
                    // Smooth scroll up
                    $('html,body').animate({
                      scrollTop: 0
                    }, 1000);
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
				TimeSheetService.getCurrent({}, function successFn(data) {
                    $scope.currentTimeSheet = data;
                    var id = data.id;
                    $scope.timeSheetsMap[id] = data;
                    $location.search('listView',null).search('id', $scope.currentTimeSheet.id);
                });
			} else {
				$scope.timeSheetsMap[obj.id] = obj;
				$scope.currentTimeSheet = angular.copy(obj, {});
                $location.search('listView',null).search('id', $scope.currentTimeSheet.id);
			}
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

		$scope.setWeekNext = function setWeekNextFn(date) {
//            $log.debug("date is ", date);
            if(!date) {
                date = new Date();
            } else {
                date = new Date(date);
            }
            
//            $log.debug("date is now ", date);
			// TODO FIXME: use relative RESTful URLs (lets date comp happen server side)
			date.setDate(date.getDate() + 8);
//            $log.debug("date sent is ", date);
			TimeSheetService.get({
				id : "new",
				date : date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate()
			}, function successFn(data) {
				$scope.set(data);
			}, $scope.errorHandlerTimeSheetGetterFn);
		};

		$scope.setWeekOther = function setWeekOtherFn(date) {
//			//TODO FIXME: replace with $modal service
//			var data = window.showModalDialog("resources/partials/datePickerModal.html");
//			
//			$('#weekSelectionModal').modal('show');
//			
//			$log.error(data.datePicked);
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
                    $location.search('id', null).search('listView');
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
        setContractsFn();
	} ]);

	module.controller('TimeSheetDayCtrl', [ '$log', '$scope',//
	function TimeSheetDayCtrlFn($log, $scope) {

		$scope.showButtons = false;

		$scope.$watch('entry', function entry$watchFn(e) {
			// suppress "hh:mm:ss.nnnn" time formatting
			if (e.startTime && e.startTime.length > 5) {
				e.startTime = e.startTime.substr(0, 5);
			}
			if (e.endTime && e.endTime.length > 5) {
				e.endTime = e.endTime.substr(0, 5);
			}
			if (e.endTime === e.startTime) {
				e.endTime = e.startTime = undefined;
			}
			$scope.showButtons = ((e.startTime || e.endTime) && !(e.startTime === e.endTime));
		}, true);

		$scope.addNewTimeSheetLogEntry = function addNewTimeSheetLogEntryFn(day) {
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

		$scope.removeTimeSheetLogEntry = function removeTimeSheetLogEntryFn(entry) {
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
