'use strict';
(function() {

	var module = angular.module('myApp.controllers', [ 'myApp.filters' ]);

	module.controller('MainCtrl', [ '$log', '$scope', '$location', 'dayOfWeekArr', //
	function MainCtrlFn($log, $scope, $location, dayOfWeekArr) {
		$scope.dayOfWeekArr = dayOfWeekArr;
		$log.info("dayOfWeekArr=", $scope.dayOfWeekArr);

		$scope.debug = (($.cookie('debug') == 'true') ? true : false);
		// $log.info('MainCtrl init', $location, $scope.debug);

		$scope.$watch(function locationPathPage$watchFn() {
			return $location.path();
		}, function(newLoc, oldLoc) {
			$scope.page = "/"+(((newLoc + "/").split("/"))[1]);
		});

		$scope.isAdmin = false;

		$scope.setAdmin = function(bool) {
			$scope.isAdmin = bool; // true if Admin, false if User
		};

		$scope.doSave = function doSaveFn(args) {
			var svc = args.service; // the service to invoke
			var obj = args.obj; // the entity to save
			var map = args.map; // the $rootScope's map to update
			var updateFn = args.updateFnName; // optional function NAME
			var afterFn = args.afterFn; // optional function
			if (!(svc && obj && map)) {
				$log.error("missing required args: ", args);
				throw new Error("missing required args");
			}

			$log.info("dosave:", [ svc, obj, updateFn, map ]);
			if (obj.id // JJZ LOH FIXME
					&& false && 'JJZ TODO Implement UPDATE in angular services.js and server @Controllers') {
				svc.update({
					id : obj.id
				}, obj, function updateSuccessFn(data) {
					saveOrUpdateSuccessFn(data, map, updateFn, afterFn);
				});

			} else {
				if (obj.id) {
					$log.error("JJZ LOH FIXME: Implement UPDATE in @Controllers. FIXME.");
					// TODO JJZ LOH FIXME 2013-10-30 svc.update({id:obj.id},)
				}
				svc.save(obj, function saveSuccessFn(data) {
					saveOrUpdateSuccessFn(data, map, updateFn, afterFn);
				});
			}

			function saveOrUpdateSuccessFn(data, map, updateFn, afterFn) {
				$log.info("saved data id=", data.id);
				map[data.id] = data;
				if (updateFn) {
					$log.info("do ($broadcast) update '" + updateFn + "'");
					$scope.$broadcast(updateFn);
				} else {
					$log.info("skipped $broadcast update");
				}
				if (afterFn) {
					afterFn(data);
				}
			}

		};

		$scope.doRemove = function doRemoveFn(args) {
			var svc = args.service; // the service to invoke
			var id = args.id; // the id of the entity to delete
			var map = args.map; // the $rootScope's map to update
			var updateFn = args.updateFnName; // optional function NAME
			var afterFn = args.afterFn; // optional function
			if (!(svc && id && map)) {
				$log.error("missing required args: ", args);
				throw new Error("missing required args");
			}

			$log.info("deleting id:", id, "map", map);
			if (map && id) {
				delete map[id];
			}
			svc.remove({
				id : id
			}, function successFn() {
				if (updateFn) {
					$log.info("do ($broadcast) update '" + updateFn + "'");
					$scope.$broadcast(updateFn);
				}
				if (afterFn) {
					afterFn();
				}
			});
		};

		$log.info('MainCtrl init complete');
	} ]);

	module.controller('LandingPageCtrl', [ '$log', '$scope', //
	function LandingPageCtrlFn($log, $scope) {
		// $log.info('LandingPageCtrl init', $scope);
	} ]);

	module.controller('HistoryPageCtrl', [ '$log', '$scope', //
	function HistoryPageCtrlFn($log, $scope) {
		$log.info('HistoryPageCtrl init', $scope);
	} ]);

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

		$log.info('TimeSheetPageCtrl init', $scope);

		function updateTimeSheetsFn() {
			$log.info("Updating time sheets map....");
			TimeSheetService.getAll(function successFn(data) {
				$scope.timeSheetsMap = data;
				$log.info('found ' + Object.keys($scope.timeSheetsMap).length + ' timeSheets');
				$log.info('timeSheetsMap', $scope.timeSheetsMap);
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
				// updateFnName : 'updateTimeSheets',
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
				$scope.timeSheetsMap[o.id] = o;
				$scope.currentTimeSheet = o;
			} else {
				$scope.currentTimeSheet = angular.copy(obj, {});
			}
			$log.info('$scope.currentTimeSheet=', $scope.currentTimeSheet);
			$scope.edit = true;
			$location.search('id', $scope.currentTimeSheet.id);
			// $scope.createTimeSheetForm.$setPristine();
		};

		$scope.setWeekLast = function setWeekLastFn() {
			TimeSheetService.getLast(function successFn(data) {
				$scope.set(data);
			});
		};

		$scope.setWeekNext = function setWeekNextFn() {
			TimeSheetService.getNext(function successFn(data) {
				$scope.set(data);
			});
		};

		$scope.setWeekOther = function setWeekOtherFn() {
			$log.error("TODO: Placeholder for setWeekXxxxFn");
		};

		$scope.unset = function unsetFn() {
			$scope.edit = false;
			$location.search('id', null);
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