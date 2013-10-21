<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.roosterpark.rptime.model.Worker" %>

<%
	Worker worder = (Worker)request.getAttribute("worder");
%>
<div id="createWorker">
	<form method="post" action="/rptime/worder">
<%		if(worder == null) {	%>
		First Name: <input type="text" name="firstName"><br>
		Last Name: <input type="text" name="lastName"><br>
		Email: <input type="text" name="email"><br>
		Start Date: <input type="text" name="startDate"><br>
<% 		} else {      	%>
		First Name: <input type="text" name="firstName" value="<%= worder.getFirstName() %>"><br>
		Last Name: <input type="text" name="lastName" value="<%= worder.getLastName() %>"><br>
		Email: <input type="text" name="email" value="<%= worder.getEmail() %>"><br>
		Start Date: <input type="text" name="startDate" value="<%= worder.getStart().getTime() %>"><br>
		<input type="hidden" name="id" value="<%= worder.getId() %>">
<%		}				%>
		<input type="submit" value="Submit">
	</form>
</div>