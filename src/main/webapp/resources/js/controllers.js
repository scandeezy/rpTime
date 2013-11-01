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
			$scope.page = newLoc;
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
			if (obj.id && false && 'JJZ TODO Implement UPDATE in angular services.js and server @Controllers') {
				$log.info("update id=", obj.id);
				$log.error("JJZ LOH: Implement UPDATE in @Controllers");
				// TODO JJZ LOH 2013-10-30 svc.update({id:obj.id},)

				svc.update({
					id : obj.id
				}, obj, function updateSuccessFn(data) {
					$log.info("updated data id=", data.id);
					$log.info("map before:", map);
					$log.info("map[id] before:", map[data.id]);
					map[data.id] = data;
					$log.info("map[id] after:", map[data.id]);
					$log.info("do ($broadcast) update '" + updateFn + "'");
					$scope.$broadcast(updateFn);
					if (afterFn) {
						afterFn();
					}
				});

			} else {
				svc.save(obj, function saveSuccessFn(data) {
					$log.info("saved data id=", data.id);
					$log.info("map before:", map);
					$log.info("map[id] before:", map[data.id]);
					map[data.id] = data;
					$log.info("map[id] after:", map[data.id]);
					$log.info("do ($broadcast) update '" + updateFn + "'");
					$scope.$broadcast(updateFn);
					if (afterFn) {
						afterFn();
					}
				});
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
				$log.info("do ($broadcast) update '" + updateFn + "'");
				$scope.$broadcast(updateFn);
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

	module.controller('TimeSheetPageCtrl', [ '$location', '$log', '$routeParams', '$scope', 'TimeSheetService', //
	function TimeSheetPageCtrlPageCtrlFn($location, $log, $routeParams, $scope, TimeSheetService) {
		$scope.edit = false;
		$scope.timeSheetsMap = {};
		$scope.currentTimeSheet = {};
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