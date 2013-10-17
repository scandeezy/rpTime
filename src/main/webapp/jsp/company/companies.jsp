<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.roosterpark.rptime.model.Company" %>
<%@ page import="java.util.List" %>
<html>
	<div id="companies">
		<ul>
<%
	List<Company> companies = (List<Company>)request.getAttribute("companies");
	for(Company company : companies)
	{
		request.setAttribute("company", company);
%>
<jsp:include page="/jsp/company/company.jsp" />
<%
	}
%>
		</ul>
	</div>
	<div id="createCompany">
		<form method="post" action="/rptime/company">
			Name: <input type="text" name="companyName"><br>
			Header: <input type="text" name="companyHeader"><br>
			Phone: <input type="text" name="companyPhone"><br>
			Start: <input type="text" name="companyStartDay"><br>
			<input type="submit" value="Submit">
		</form>
	</div>
</html>