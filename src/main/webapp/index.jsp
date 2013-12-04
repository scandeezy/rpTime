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
	<%
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String loginUrl = userService.createLoginURL(request.getRequestURI());
		String logoutUrl = userService.createLogoutURL(request.getRequestURI());

		if (userService.isUserLoggedIn()) {
			String nickname = user.getNickname();
	%>
	<div id="mainDiv" ng-controller="MainCtrl as main" ng-init='workerExists=("<%=WorkerService.inst().getCurrent()%>"!="null");'>

		<div id="rpTimeWrapperDiv" <%if (userService.isUserAdmin()) {%> ng-controller="AdminPageCtrl" <%} else {%> ng-controller="UserPageCtrl"
			<%}%>>

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

		<%@ include file="/scripts.jsp"%>

	</div>
</body>
</html>