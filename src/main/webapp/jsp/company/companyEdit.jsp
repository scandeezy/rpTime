<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.roosterpark.rptime.model.Company" %>

<%
	Company company = (Company)request.getAttribute("company");
	if(company == null)
		company = new Company();
%>

<div id="createCompany">
	<form method="post" action="/rptime/company">
		Name: <input type="text" name="companyName" value="<%= company.getName() %>"><br>
		Header: <input type="text" name="companyHeader" value="<%= company.getHeader() %>"><br>
		Phone: <input type="text" name="companyPhone" value="<%= company.getPhone() %>"><br>
		Start: <input type="text" name="companyStartDay" value="<%= company.getStartDayOfWeek() %>"><br>
		<input type="submit" value="Submit">
	</form>
</div>