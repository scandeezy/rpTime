<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.roosterpark.rptime.model.Worker" %>

<%
Worker worker = (Worker)request.getAttribute("worker");
%>
<li>
	<div id="id"><%= worker.getId() %></div>
	<div id="firstName"><%= worker.getFirstName() %></div>
	<div id="lastName"><%= worker.getLastName() %></div>
	<div id="email"><%= worker.getEmail() %></div>
	<div id="startDate"><%= worker.getStart().getTime() %></div>
	<a href="/rptime/worker?worker=<%= worker.getId() %>" >View</a>
	<a href="/rptime/worker?worker=<%= worker.getId() %>&edit=true" >Edit</a>
</li>