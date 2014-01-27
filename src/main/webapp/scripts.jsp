<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%
	UserService userServiceScr = UserServiceFactory.getUserService();
	boolean showAdminOnly = userServiceScr.isUserLoggedIn() && userServiceScr.isUserAdmin();
%>

<div id="jsScripts">
	<!-- begin JS resources manageable by WebJars -->
	<script src="/resources/webjars/jquery/2.0.3/jquery.min.js"></script>
	<script src="/resources/webjars/bootstrap/3.0.2/js/bootstrap.min.js"></script>
	<script src="/resources/webjars/angularjs/1.2.3/angular.js"></script>
	<!-- <script src="/resources/webjars/angularjs/1.2.3/angular.min.js"></script> -->
	<script src="/resources/webjars/angularjs/1.2.3/angular-animate.min.js"></script>
	<script src="/resources/webjars/angularjs/1.2.3/angular-cookies.min.js"></script>
	<script src="/resources/webjars/angularjs/1.2.3/angular-route.min.js"></script>
	<script src="/resources/webjars/angularjs/1.2.3/angular-resource.js"></script>
	<!-- <script src="/resources/webjars/angularjs/1.2.3/angular-resource.min.js"></script> -->
	<script src="/resources/webjars/angularjs/1.2.3/angular-sanitize.min.js"></script>
	<!-- end JS resources manageable by WebJars -->
	<script src="/resources/js/app.js"></script>
	<script src="/resources/js/services.js"></script>
	<script src="/resources/js/controllers.js"></script>
	<script src="/resources/js/controllers/timesheet-controllers.js"></script>
	<script src="/resources/js/filters.js"></script>
	<script src="/resources/js/directives.js"></script>

	<%
		if (showAdminOnly) {
	%>
	<!-- begin admin-only resources -->

    <script src="/resources/js/admin/admin-app.js"></script>
	<script src="/resources/js/admin/admin-services.js"></script>
	<script src="/resources/js/admin/admin-controllers.js"></script>
	<script src="/resources/js/admin/admin-client-controllers.js"></script>
	<script src="/resources/js/admin/admin-contract-controllers.js"></script>
	<script src="/resources/js/admin/admin-report-controllers.js"></script>
    <script src="/resources/js/admin/admin-timesheet-controller.js"></script>
	<script src="/resources/js/admin/admin-worker-controllers.js"></script>
	<script src="/resources/js/admin/admin-stats-controller.js"></script>

	<!-- end admin-only resources -->
	<%
		}
	%>
</div>