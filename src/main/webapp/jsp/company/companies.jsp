<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.roosterpark.rptime.model.Company" %>
<%@ page import="java.util.List" %>
<html>
<%@ include file="/jsp/headers.jsp"%>
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
	request.setAttribute("company", null);
%>
		</ul>
	</div>
<jsp:include page="/jsp/company/companyEdit.jsp" />
<%@ include file="/jsp/scripts.jsp"%>
</html>