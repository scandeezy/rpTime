<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.roosterpark.rptime.model.Contract" %>

<%
Contract contract = (Contract)request.getAttribute("contract");
%>

<li>
	<div id="id"><%= contract.getId() %></div>
	<div id="worker"><%= contract.getWorker() %></div>
	<div id="client"><%= contract.getClient() %></div>
	<div id="start"><%= contract.getStart().getTime() %></div>
	<div id="end"><%= contract.getEnd().getTime() %></div>
	<a href="/rptime/contract?contract=<%= contract.getId() %>" >View</a>
	<a href="/rptime/contract?contract=<%= contract.getId() %>&edit=true" >Edit</a>
</li>