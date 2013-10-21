<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html ng-app="myApp">
<%@ include file="/jsp/headers.jsp"%>
<body>
<div id="userNav" ng-controller="MainCtrl as main">
<%
	User user = (User)request.getAttribute("user");
    UserService userService = UserServiceFactory.getUserService();
    if (user != null) {
      pageContext.setAttribute("user", user);
%>
	<div ng-hide="true">
		<p>Hello, ${fn:escapeXml(user.nickname)}! Admin ${fn:escapeXml(admin)} (You can
		<a href="<%= userService.createLogoutURL((String)request.getAttribute("page")) %>">sign out</a>.)</p>
		<a href="/rptime/sheet">Current Time Sheet</a></br>
		<a href="/rptime/history">History</a></br>
		<a href="/rptime/reports">Reports</a></br>
		<a href="/rptime/user">Users</a></br>
		<a href="/rptime/company">Companies</a></br>
	</div>
	
<nav class="navbar navbar-default" role="navigation">
  <!-- Brand and toggle get grouped for better mobile display -->
  <div class="navbar-header">
    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
      <span class="sr-only">Toggle navigation</span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
    </button>
	<a class="navbar-brand" href="//roosterpark.com">
		<img src="/resources/img/roosterpark-logo.png" alt="Rooster Park" height="18" width="40">
	</a>
	</div>

  <!-- Collect the nav links, forms, and other content for toggling -->
  <div class="collapse navbar-collapse navbar-ex1-collapse">
    <ul class="nav navbar-nav">
   	  	<li ng-class="{active : page == 'landing'}"><a href="#/landing">Home</a></li>
	 	<li ng-class="{active : page == 'timesheet'}" ng-hide="isAdmin"><a href="#/timesheet">Timesheet</a></li>
	 	<% if(userService.isUserAdmin()){ %>
	 	<li ng-class="{active : page == 'workers'}"><a href="#/workers">Workers</a></li>
		<li ng-class="{active : page == 'clients'}"><a href="#/clients">Clients</a></li>
		<li ng-class="{active : page == 'reports'}" class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown">Reports <b class="caret"></b></a>
			<ul class="dropdown-menu">
				<li><a href="#/reports/1">Report1</a></li>
				<li><a href="#/reports/2">Report2</a></li>
				<li><a href="#/reports/3">Report3</a></li>
			</ul>
		<li ng-class="{active : page == 'clients'}"><a href="#/history">History</a></li>
		<% } %>
    </ul>
    
    <ul class="nav navbar-nav navbar-right">
    <!-- 
		<li class="dropdown" ng-show="isAdmin"><a class="dropdown-toggle" data-toggle="dropdown">Admin <b class="caret"></b></a>
			<ul class="dropdown-menu">
				<li><a href="#">Action</a></li>
				<li><a href="#">Another action</a></li>
				<li><a href="#">Something else here</a></li>
				<li><a href="#">Separated link</a></li>
				<li><a href="#">One more separated link</a></li>
			</ul>
		</li>
 	-->
 		<li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown">${fn:escapeXml(user.nickname)} <b
				class="caret"></b></a>
			<ul class="dropdown-menu">
				<li><a href="<%=userService.createLogoutURL((String) request.getAttribute("page"))%>">Sign out</a></li>
			</ul>
		</li>
	</ul>

  </div><!-- /.navbar-collapse -->
</nav>

	<%
		if(userService.isUserAdmin()){
	%>
	<div ng-controller="AdminPageCtrl">
	<%
 	   } else { // (admin == false)
	%>
	<div ng-controller="WorkerPageCtrl">
	<%
 	   } // (admin == false)
	%>
	
	    <div class="rptime-animate-container">
    		<div ng-view class="view-rptime"><!--  --></div>
	    </div>
	    
	    <p class="text-warning" ng-show="isAdmin">
			<span class="label label-warning">Warning</span> You are signed in as an administrator.
		</p>
	    
	</div> <!-- DIV opened by ngcontroller -->
	
	
<%
    } else {
%>
   <div class="container">
      <form class="form-signin">
        <h2 class="form-signin-heading">Please sign in</h2>
        <a class="btn btn-primary" href="<%=userService.createLoginURL((String) request.getAttribute("page"))%>">Sign in</a>
      </form>
    </div>
<%
    } //end else (user==null)
%>

<%@ include file="/jsp/scripts.jsp"%>

</div>

</body>
</html>