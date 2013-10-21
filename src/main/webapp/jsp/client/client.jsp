<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.roosterpark.rptime.model.Client" %>

<%
Client client = (Client)request.getAttribute("client");
%>

<li>
	<div id="id"><%= client.getId() %></div>
	<div id="clientName"><%= client.getName() %></div>
	<div id="clientHeader"><%= client.getHeader() %></div>
	<div id="clientPhone"><%= client.getPhone() %></div>
	<div id="clientStart"><%= client.getStartDayOfWeek() %></div>
	<a href="/rptime/client?client=<%= client.getId() %>" >View</a>
	<a href="/rptime/client?client=<%= client.getId() %>&edit=true" >Edit</a>
</li>