<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.roosterpark.rptime.model.Company" %>

<%
	Company company = (Company)request.getAttribute("company");
%>

<div id="createCompany">
	<form method="post" action="/rptime/company">
<%		if(company == null) {		%>
		Name: <input type="text" name="companyName"><br>
		Header: <input type="text" name="companyHeader"><br>
		Phone: <input type="text" name="companyPhone"><br>
		Start: <input type="text" name="companyStartDay"><br>
<%		} else {					%>
		Name: <input type="text" name="companyName" value="<%= company.getName() %>"><br>
		Header: <input type="text" name="companyHeader" value="<%= company.getHeader() %>"><br>
		Phone: <input type="text" name="companyPhone" value="<%= company.getPhone() %>"><br>
		Start: <input type="text" name="companyStartDay" value="<%= company.getStartDayOfWeek() %>"><br>
		<input type="hidden" name="id" value="<%= company.getId() %>">
<%		}							%>
		<input type="submit" value="Submit">
	</form>
</div>