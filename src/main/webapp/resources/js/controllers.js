'use strict';
(function() {

	var module = angular.module('myApp.controllers', [ 'myApp.filters' ]);

	module.controller('AboutCtrl', [ '$log', '$scope', 'AboutService', //
	function AboutCtrlFn($log, $scope, AboutService) {
		$scope.about = AboutService.get();
	} ]);

	module.controller('ErrorPageCtrl', [ '$log', '$rootScope', function ErrorPageCtrlFn($log, $rootScope) {
		$log.info("ErrorPageCtrl");
		// uses $rootScope.rootError
	} ]);

	module.controller('MainCtrl', [ '$cookies', '$log', '$rootScope', '$scope', '$location', 'dayOfWeekArr', //
	function MainCtrlFn($cookies, $log, $rootScope, $scope, $location, dayOfWeekArr) {
		$scope.dayOfWeekArr = dayOfWeekArr;
		$scope.debug = $cookies.debug;

		$scope.$watch(function locationPathPage$watchFn() {
			return $location.path();
		}, function(newLoc, oldLoc) {
			$scope.page = "/" + (((newLoc + "/").split("/"))[1]);
		});

		$scope.isAdmin = false;

		$scope.setAdmin = function(bool) {
			$scope.isAdmin = bool; // true if Admin, false if User
		};

		$scope.setError = function setErrorFn(errorObj) {
			$rootScope.rootError = errorObj;
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
		$log.info("(Landing) $scope.workerExists", $scope.workerExists);
		// workerExists is initialized on the parent (MainCtrl) scope
		if ($scope.isAdmin) {
			// all good!
		} else if (!$scope.isAdmin && $scope.workerExists) {
			$log.info("redirecting to '/timesheet?id=current' view...");
			$location.path('/timesheet').search('id', 'current');
		} else {
			$log.error("redirecting to '/error' view...");
			//hardcode test
			$scope.setError({
				data:{
					reason : "No Worker found for your email.  To resolve, contact an administrator to create a Worker with this email to link it to a user.",
					status : 'FORBIDDEN'
				},
				status: 403
			});
			$location.path('/error');
		}
	} ]);

	module.controller('UserPageCtrl', [ '$log', '$scope', //
	function UserPageCtrlFn($log, $scope) {
		// $log.info('UserPageCtrl init', $scope);
		$scope.setAdmin(false); // inherited fn from UserNavCtrl
	} ]);

})();