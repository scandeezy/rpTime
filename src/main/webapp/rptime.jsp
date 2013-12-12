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
		<a class="navbar-brand" href="//roosterpark.com" tabindex="-1"> <img src="/resources/img/roosterpark-logo.png" alt="Rooster Park"
			height="18" width="40">
		</a>
	</div>

	<!-- Collect the nav links, forms, and other content for toggling -->
	<div class="collapse navbar-collapse navbar-ex1-collapse">
		<ul class="nav navbar-nav">
			<%
				if (!userService2.isUserAdmin()) {
			%>
			<li ng-show="workerExists" ng-class="{active : page == '/timesheet'}"><a href="#/timesheet" tabindex="-1">Time Sheets</a></li>
			<%
				} else {
			%>
			<li ng-class="{active : page == '/landing'}"><a href="#/landing" tabindex="-1">Home</a></li>
			<li ng-class="{active : page == '/timesheet'}"><a href="#/timesheet" tabindex="-1">Time Sheets</a></li>
			<li ng-class="{active : page == '/worker'}"><a href="#/worker" tabindex="-1">Workers</a></li>
			<li ng-class="{active : page == '/client'}"><a href="#/client" tabindex="-1">Clients</a></li>
			<li ng-class="{active : page == '/contract'}"><a href="#/contract" tabindex="-1">Contracts</a></li>
			<li class="dropdown" ng-class="{active : page == '/report'}"><a class="dropdown-toggle" data-toggle="dropdown">Reports <b
					class="caret"></b></a>
				<ul class="dropdown-menu" ng-controller="AdminReportLinkCtrl">
					<li ng-repeat="report in reportLinks">
						<a ng-href="#/report/{{report.link}}" tabindex="-1">{{report.name}}</a>
					</li>
				</ul>
			<li ng-show="page == '/report'"><a id="print" name="print" href="JavaScript:window.print();" title="Print" tabindex="-1"><span
					class="glyphicon glyphicon-print"></span></a></li>
			<%
				}
			%>
			<li class="dropdown" ng-show="debug"><a class="dropdown-toggle debug" data-toggle="dropdown">Debug <b class="caret"></b></a>
				<ul class="dropdown-menu debug">
					<!-- isAdmin is set on MainCtrl's scope -->
					<li><pre class="debug">isAdmin={{isAdmin}}</pre></li>
					<li class="divider"></li>
					<!-- workerExists is inherited from index.jsp -->
					<li><pre class="debug">workerExists={{workerExists}}</pre></li>
				</ul></li>
		</ul>

		<ul class="nav navbar-nav navbar-right">
			<li class="dropdown"><a id="signOut" class="dropdown-toggle" data-toggle="dropdown"><%=user2.getNickname()%><b class="caret"></b></a>
				<ul class="dropdown-menu">
					<%
						if (userService2.isUserAdmin()) {
					%>
					<li><a href="#/about">About</a></li>
					<li class="divider"></li>
					<%
						}
					%>
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

<p class="text-warning hidden-print" ng-show="isAdmin">
	<span class="label label-warning">Warning</span> You are signed in as an administrator.
</p>
