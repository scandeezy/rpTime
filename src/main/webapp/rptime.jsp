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
				<% if (!userService2.isUserAdmin()) { %>
				<li ng-class="{active : page == '/timesheet'}"><a href="#/timesheet">Time Sheets</a></li>
				<% } else { %>
				<li ng-class="{active : page == '/landing'}"><a href="#/landing">Home</a></li>
				<li ng-class="{active : page == '/timesheet'}"><a href="#/timesheet">Time Sheets</a></li>
				<li ng-class="{active : page == '/worker'}"><a href="#/worker">Workers</a></li>
				<li ng-class="{active : page == '/client'}"><a href="#/client">Clients</a></li>
				<li ng-class="{active : page == '/contract'}"><a href="#/contract">Contracts</a></li>
				<li class="dropdown" ng-class="{active : page == '/report'}">
					<a class="dropdown-toggle" data-toggle="dropdown">Reports <b
						class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="#/report/total-hours-per-worker-per-month">Hours per Worker/Month, all Clients</a></li>
						<li><a href="#/report/timesheets-per-worker-by-month-for-client">Time Sheets per Worker/Month/Client</a></li>
						<li><a href="#/report/total-hours-per-worker-by-range-for-client">Time Sheets per Worker/Range/Client</a></li>
					</ul>
				<li ng-show="page == '/report'"">
					<a id="print" name="print" href="JavaScript:window.print();" title="Print"><span
						class="glyphicon glyphicon-print"></span></a>
				</li>
				<% } %>
			</ul>
	
			<ul class="nav navbar-nav navbar-right">
				<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown"><%=user2.getNickname()%><b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="<%=logoutUrl2%>">Sign out</a></li>
						<% if (userService2.isUserAdmin()) { %>
						<li class="divider"></li>
 						<li><a href="#/about">About</a></li>			
						<% } %>
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

	<p class="text-warning hidden-print" ng-show="isAdmin">
		<span class="label label-warning">Warning</span> You are signed in as an administrator.
	</p>
