<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.roosterpark.rptime.model.Client" %>

<%
	Client client = (Client)request.getAttribute("client");
%>

<div id="createCompany">
	<form method="post" action="/rptime/client">
<%		if(client == null) {		%>
		Name: <input type="text" name="clientName"><br>
		Header: <input type="text" name="clientHeader"><br>
		Phone: <input type="text" name="clientPhone"><br>
		Start: <input type="text" name="clientStartDay"><br>
<%		} else {					%>
		Name: <input type="text" name="clientName" value="<%= client.getName() %>"><br>
		Header: <input type="text" name="clientHeader" value="<%= client.getHeader() %>"><br>
		Phone: <input type="text" name="clientPhone" value="<%= client.getPhone() %>"><br>
		Start: <input type="text" name="clientStartDay" value="<%= client.getStartDayOfWeek() %>"><br>
		<input type="hidden" name="id" value="<%= client.getId() %>">
<%		}							%>
		<input type="submit" value="Submit">
	</form>
</div>