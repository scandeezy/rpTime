<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%
UserService userService2 = UserServiceFactory.getUserService();
User user2 = userService2.getCurrentUser();
String loginUrl2 = userService2.createLoginURL(request.getRequestURI());
String logoutUrl2 = userService2.createLogoutURL(request.getRequestURI());
%>
	<nav class="navbar navbar-default" role="navigation">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
				<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="//roosterpark.com"> <img src="/resources/img/roosterpark-logo.png" alt="Rooster Park" height="18"
				width="40">
			</a>
		</div>
	
		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse navbar-ex1-collapse">
			<ul class="nav navbar-nav">
				<li ng-class="{active : page == '/landing'}"><a href="#/landing">Home</a></li>
				<li ng-class="{active : page == '/timesheet'}" ng-hide="isAdmin && !debug"><a href="#/timesheet">Timesheets</a></li>
				<%
					if (userService2.isUserAdmin()) {
				%>
				<li ng-class="{active : page == '/worker'}"><a href="#/worker">Workers</a></li>
				<li ng-class="{active : page == '/client'}"><a href="#/client">Clients</a></li>
				<li ng-class="{active : page == '/contract'}"><a href="#/contract">Contracts</a></li>
				<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown">Reports <b
						class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="#/report/total-hours-per-worker-per-month">Hours per Worker/Month</a></li>
						<li><a href="#/report/timesheets-per-worker-by-week-for-client">Time Sheets per Worker/Week/Client</a></li>
					</ul>
				<%
					}
				%>
				<li ng-class="{active : page == '/history'}"><a href="#/history">History</a></li>
			</ul>
	
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown"><%=user2.getNickname()%><b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="<%=logoutUrl2%>">Sign out</a></li>
					</ul></li>
			</ul>
	
		</div>
		<!-- /.navbar-collapse -->
	</nav>

	<div class="rptime-animate-container">
		<div ng-view class="view-rptime">
			<!--  -->
		</div>
	</div>

	<p class="text-warning" ng-show="isAdmin">
		<span class="label label-warning">Warning</span> You are signed in as an administrator.
	</p>
