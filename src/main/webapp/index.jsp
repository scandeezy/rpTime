<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="com.roosterpark.rptime.service.WorkerService"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html ng-app="myApp">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="rpTime">
<meta name="author" content="Rooster Park">
<title>rpTime</title>
<link rel="shortcut icon" href="/favicon.ico" />
<link rel="stylesheet" type="text/css" media="all" href="/resources/webjars/bootstrap/3.0.2/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" media="all" href="/resources/webjars/bootstrap/3.0.2/css/bootstrap-theme.min.css">
<link rel="stylesheet" type="text/css" media="all" href="/resources/css/app.css">
</head>
<body>
	<div id="mainDiv" ng-controller="MainCtrl as main">
		<%
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			String loginUrl = userService.createLoginURL(request.getRequestURI());
			String logoutUrl = userService.createLogoutURL(request.getRequestURI());
						
			if (userService.isUserLoggedIn()) {
				String nickname = user.getNickname();
		%>
		
	<div id="rpTimeWrapperDiv"  
<%
if (userService.isUserAdmin()) {
%>
ng-controller="AdminPageCtrl"
<%
} else { // (admin == false)
%>
ng-controller="UserPageCtrl"
<%
} // (admin == false)
%>
ng-init='workerExists=("<%=WorkerService.inst().getCurrent()%>"!="null");'
>

<%@ include file="/rptime.jsp"%>
		
	</div>
		<%
			} else {
		%>
		<div class="container">
			<div class="row">
				<form class="form-signin ">
					<h2 class="form-signin-heading">Please sign in</h2>					
					<a class="btn btn-primary" href="<%=loginUrl%>">Sign in</a>
				</form>
			</div>
		</div>
		<%
			}
		%>
		<div id="jsScripts">
			<!-- begin JS resources manageable by WebJars -->
			<script src="/resources/webjars/jquery/2.0.3/jquery.min.js"></script>
			<script src="/resources/webjars/jquery-cookie/1.3.1/jquery.cookie.js"></script>
			<script src="/resources/webjars/bootstrap/3.0.2/js/bootstrap.min.js"></script>
			<script src="/resources/webjars/angularjs/1.2.1/angular.js"></script>
			<script src="/resources/webjars/angularjs/1.2.1/angular-animate.js"></script>
			<script src="/resources/webjars/angularjs/1.2.1/angular-route.js"></script>
			<script src="/resources/webjars/angularjs/1.2.1/angular-resource.js"></script>
			<!-- end JS resources manageable by WebJars -->
			<script src="/resources/js/app.js"></script>
			<script src="/resources/js/services.js"></script>
			<script src="/resources/js/controllers.js"></script>
			<script src="/resources/js/controllers/timesheet-controllers.js"></script>
			<script src="/resources/js/filters.js"></script>
			<script src="/resources/js/directives.js"></script>
    
			<!-- pre-load templates into angular's $templateCache (see: http://stackoverflow.com/a/12346901/237225) -->
			<script type="text/ng-template" src="/resources/partials/about.html"></script>
			<script type="text/ng-template" src="/resources/partials/client.html"></script>
			<script type="text/ng-template" src="/resources/partials/contract.html"></script>
			<script type="text/ng-template" src="/resources/partials/history.html"></script>
			<script type="text/ng-template" src="/resources/partials/landing.html"></script>
			<script type="text/ng-template" src="/resources/partials/timesheet.html"></script>
			<script type="text/ng-template" src="/resources/partials/worker.html"></script>

			<%
	 			if (userService.isUserLoggedIn() && userService.isUserAdmin()) {
			%> 			
 			<!-- begin admin-only resources -->

			<script src="/resources/js/admin/admin-services.js"></script>
			<script src="/resources/js/admin/admin-controllers.js"></script>
			<script src="/resources/js/admin/admin-client-controllers.js"></script>
			<script src="/resources/js/admin/admin-contract-controllers.js"></script>
			<script src="/resources/js/admin/admin-report-controllers.js"></script>
			<script src="/resources/js/admin/admin-worker-controllers.js"></script>
			<script type="text/ng-template" src="/resources/partials/reports/list.html"></script>
			<script type="text/ng-template" src="/resources/partials/reports/timesheets-per-worker-by-month-for-client.html"></script>
			<script type="text/ng-template" src="/resources/partials/reports/total-hours-per-worker-by-range-for-client.html"></script>
			<script type="text/ng-template" src="/resources/partials/reports/total-hours-per-worker-per-month.html"></script>

 			<!-- end admin-only resources -->
			<%
				}
			%>
		</div>

	</div>
</body>
</html>