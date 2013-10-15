<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div id="userNav"
<%
	User user = (User)request.getAttribute("user");
    UserService userService = UserServiceFactory.getUserService();
    if (user != null) {
      pageContext.setAttribute("user", user);
%>
<p>Hello, ${fn:escapeXml(user.nickname)}! Admin ${fn:escapeXml(admin)} (You can
<a href="<%= userService.createLogoutURL((String)request.getAttribute("page")) %>">sign out</a>.)</p>
	<a href="/rptime/sheet">Current Time Sheet</a></br>
	<a href="/rptime/history">History</a></br>
	<a href="/rptime/reports">Reports</a></br>
<%
    } else {
%>
<p>Hello!
<a href="<%= userService.createLoginURL((String)request.getAttribute("page")) %>">Sign in</a>
to include your name with greetings you post.</p>
<%
    }
%>
</div>
