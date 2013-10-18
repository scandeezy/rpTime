<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.roosterpark.rptime.model.User" %>

<%
User user = (User)request.getAttribute("user");
%>
<li>
	<div id="id"><%= user.getId() %></div>
	<div id="firstName"><%= user.getFirstName() %></div>
	<div id="lastName"><%= user.getLastName() %></div>
	<div id="email"><%= user.getEmail() %></div>
	<div id="startDate"><%= user.getStart().getTime() %></div>
	<a href="/rptime/user?user=<%= user.getId() %>" >View</a>
	<a href="/rptime/user?user=<%= user.getId() %>&edit=true" >Edit</a>
</li>