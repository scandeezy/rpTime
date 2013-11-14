'use strict';
(function() {

	var module = angular.module('myApp.controllers', [ 'myApp.filters' ]);

	module.controller('MainCtrl', [ '$log', '$scope', '$location', 'dayOfWeekArr', //
	function MainCtrlFn($log, $scope, $location, dayOfWeekArr) {
		$scope.dayOfWeekArr = dayOfWeekArr;
		$scope.debug = (($.cookie('debug') == 'true') ? true : false);

		$scope.$watch(function locationPathPage$watchFn() {
			return $location.path();
		}, function(newLoc, oldLoc) {
			$scope.page = "/" + (((newLoc + "/").split("/"))[1]);
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

	module.controller('LandingPageCtrl', [ '$location', '$log', '$scope', //
	function LandingPageCtrlFn($location, $log, $scope) {

		// auto-redirect to current timesheet IF not admin
		if (!$scope.isAdmin) {
			$location.path('/timesheet').search('id','current');
		}

	} ]);

	module.controller('HistoryPageCtrl', [ '$log', '$scope', //
	function HistoryPageCtrlFn($log, $scope) {
		// $log.info('HistoryPageCtrl init', $scope);
	} ]);

	module.controller('UserPageCtrl', [ '$log', '$scope', //
	function UserPageCtrlFn($log, $scope) {
		// $log.info('UserPageCtrl init', $scope);
		$scope.setAdmin(false); // inherited fn from UserNavCtrl
	} ]);

})();