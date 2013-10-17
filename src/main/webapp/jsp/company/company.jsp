<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.roosterpark.rptime.model.Company" %>

<%
Company company = (Company)request.getAttribute("company");
%>

<li>
	<div id="companyName"><%= company.getName() %></div>
	<div id="companyHeader"><%= company.getHeader() %></div>
	<div id="companyPhone"><%= company.getPhone() %></div>
	<div id="companyStart"><%= company.getStartDayOfWeek() %></div>
	<a href="/rptime/company?company=<%= company.getName() %>" >View</a>
	<a href="/rptime/company?company=<%= company.getName() %>&edit=true" >Edit</a>
</li>