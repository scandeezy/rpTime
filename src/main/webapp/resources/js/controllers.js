'use strict';
(function() {

	var module = angular.module('myApp.controllers', [ 'myApp.filters' ]);

	module.controller('AboutCtrl', [ '$log', '$scope', 'AboutService', //
	function AboutCtrlFn($log, $scope, AboutService) {
		$scope.about = AboutService.get();
	} ]);

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

			// $log.info("dosave:", [ svc, obj, updateFn, map ]);
			svc.save(obj, function saveSuccessFn(data) {
				saveOrUpdateSuccessFn(data, map, updateFn, afterFn);
			});

			function saveOrUpdateSuccessFn(data, map, updateFn, afterFn) {
				$log.info("saved data id=", data.id);
				map[data.id] = data;
				if (updateFn) {
					// $log.info("do ($broadcast) update '" + updateFn + "'");
					$scope.$broadcast(updateFn);
				} else {
					// $log.info("skipped $broadcast update");
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
					// $log.info("do ($broadcast) update '" + updateFn + "'");
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
			$location.path('/timesheet').search('id', 'current');
		}
	} ]);

	module.controller('UserPageCtrl', [ '$log', '$scope', //
	function UserPageCtrlFn($log, $scope) {
		// $log.info('UserPageCtrl init', $scope);
		$scope.setAdmin(false); // inherited fn from UserNavCtrl
	} ]);

})();