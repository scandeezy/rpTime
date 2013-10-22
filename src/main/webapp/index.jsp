<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html ng-app="myApp">

<%@ include file="/jsp/headers.jsp"%>

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
		<%@ include file="/jsp/rptime.jsp"%>
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

		<%@ include file="/jsp/scripts.jsp"%>

	</div>
</body>
</html>