<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html ng-app="myApp">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="rpTime">
<meta name="author" content="Rooster Park">
<title>rpTime</title>
<link rel="shortcut icon" href="/favicon.ico" />
<link rel="stylesheet" href="/resources/webjars/bootstrap/3.0.0/css/bootstrap.min.css">
<link rel="stylesheet" href="/resources/webjars/bootstrap/3.0.0/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="/resources/css/app.css" />
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
>
		
		<%@ include file="/jsp/rptime.jsp"%>
		
	</div>
		<%
			} else {
		%>
		<div class="container">
			<form class="form-signin">
				<h2 class="form-signin-heading">Please sign in</h2>
				<a class="btn btn-primary" href="<%=loginUrl%>">Sign in</a>
			</form>
		</div>
		<%
			}
		%>

		<div ng-cloak ng-show="debug">
			<pre>user = <%=user%>
userService = <%=userService%>
userService.isUserLoggedIn = <%=userService.isUserLoggedIn()%>
pageContext = <%=pageContext%>
request = <%=request%></pre>
		</div>

		<div id="jsScripts">
			<!-- begin JS resources manageable by WebJars -->
			<script src="/resources/webjars/jquery/2.0.3/jquery.min.js"></script>
			<script src="/resources/webjars/jquery-cookie/1.3.1/jquery.cookie.js"></script>
			<script src="/resources/webjars/bootstrap/3.0.0/js/bootstrap.min.js"></script>
			<script src="/resources/webjars/angularjs/1.2.0-rc.3/angular.js"></script>
			<script src="/resources/webjars/angularjs/1.2.0-rc.3/angular-animate.js"></script>
			<script src="/resources/webjars/angularjs/1.2.0-rc.3/angular-route.js"></script>
			<script src="/resources/webjars/angularjs/1.2.0-rc.3/angular-resource.js"></script>
			<!-- end JS resources manageable by WebJars -->
			<script src="/resources/js/app.js"></script>
			<script src="/resources/js/services.js"></script>
			<script src="/resources/js/controllers.js"></script>
			<script src="/resources/js/filters.js"></script>
			<script src="/resources/js/directives.js"></script>
			<!-- pre-load templates into angular's $templateCache (see: http://stackoverflow.com/a/12346901/237225) -->
			<script type="text/ng-template" src="/resources/partials/client.html"></script>
			<script type="text/ng-template" src="/resources/partials/history.html"></script>
			<script type="text/ng-template" src="/resources/partials/landing.html"></script>
			<script type="text/ng-template" src="/resources/partials/timesheet.html"></script>
			<script type="text/ng-template" src="/resources/partials/worker.html"></script>
			<!-- end pre-load templates --> 
		</div>

	</div>
</body>
</html>