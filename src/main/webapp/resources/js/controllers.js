'use strict';
(function() {

	var module = angular.module('myApp.controllers', ['myApp.filters']);

	module.controller('MainCtrl', [ '$log', '$scope', '$location', 'dayOfWeekArr', //
	function MainCtrlFn($log, $scope, $location, dayOfWeekArr) {
		$scope.dayOfWeekArr = dayOfWeekArr;
		$scope.debug = (($.cookie('debug') == 'true') ? true : false);
		$log.info('MainCtrl init', $location, $scope.debug);

		$scope.$watch(function locationPathPage$watchFn() {
			return $location.path();
		}, function(newLoc, oldLoc) {
			$scope.page = newLoc;
		});

		$scope.isAdmin = false;

		$scope.setAdmin = function(bool) {
			$scope.isAdmin = bool; // true if Admin, false if User
		};

		$scope.doSave = function doSaveFn(svc, obj, updateFn, arr, afterSaveFn) {
			$log.info("dosave:",[svc, obj, updateFn, arr]);
			var objJson = angular.toJson(obj);
			if(arr && arr.indexOf(obj) != null ){
				arr.push[obj];
			}
			svc.save(obj, function saveSuccessFn(data) {
				$log.info("saved data:", data);
				$log.info("do ($broadcast) update '" + updateFn + "'");
				$scope.$broadcast(updateFn);
				if(afterSaveFn){
					afterSaveFn();
				}
			});
		};

		$scope.doRemove = function doRemoveFn(svc, obj, updateFn, afterUpdateFn) {
			$log.info("deleting:", obj);
			svc.remove(obj, function successFn() {
				$log.info("do ($broadcast) update '" + updateFn + "'");
				$scope.$broadcast(updateFn);
				if(afterUpdateFn){
					afterUpdateFn();
				}
			});
		};

		$log.info('MainCtrl init complete');
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
		$scope.dows = [ 'Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat' ];

		function updateTimeSheetsFn() {
                        console.log("Updating time sheets....");
			TimeSheetService.getAll(function successFn(data) {
				$scope.timesheets = data;
                                $scope.timesheetMap = {};
                                for(var index = 0; index < data.length; index++) {
                                    var sheet = data[index];
                                    console.log("Adding sheet " + sheet + " to map...");
                                    var key = sheet.workerId + sheet.week + sheet.year;
                                    if(typeof $scope.timesheetMap[key] == 'undefined')
                                        $scope.timesheetMap[key] = [];
                                    $scope.timesheetMap[key].push(sheet);
                                    console.log("map is now size " + $scope.timesheetMap.length);
                                }
			});
		}

		$scope.set = function setFn(obj) {
			if (obj) {
                                if(obj instanceof Array)
                                    $scope.currentTimeSheets = obj;
				$scope.currentTimeSheet = obj;
			} else {
				TimeSheetService.create(function successFn(data) {
					$scope.currentTimeSheets = data;
				});
			}
			$scope.edit = true;
		}

		$scope.save = function saveFn(ts) {
                        if(ts instanceof Array) {
                            for(var index = 0; index < ts.length; index++) {
                                var sheet = ts[index];
                                $scope.doSave(TimeSheetService, sheet, 'updateTimeSheets');
                            }
                        } else {
                            $scope.doSave(TimeSheetService, ts, 'updateTimeSheets');
                        }
			$scope.edit = false;
		};

		$scope.$on('updateTimeSheets', updateTimeSheetsFn);

		updateTimeSheetsFn();

	} ]);

	module.controller('UserPageCtrl', [ '$log', '$scope', //
	function UserPageCtrlFn($log, $scope) {
		$log.info('UserPageCtrl init', $scope);
		$scope.setAdmin(false); // inherited fn from UserNavCtrl
	} ]);

})();