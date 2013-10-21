<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.roosterpark.rptime.model.Worker" %>

<%
	Worker worker = (Worker)request.getAttribute("worker");
%>
<div id="createWorker">
	<form method="post" action="/rptime/worker">
<%		if(worker == null) {	%>
		First Name: <input type="text" name="firstName"><br>
		Last Name: <input type="text" name="lastName"><br>
		Email: <input type="text" name="email"><br>
		Start Date: <input type="text" name="startDate"><br>
<% 		} else {      	%>
		First Name: <input type="text" name="firstName" value="<%= worker.getFirstName() %>"><br>
		Last Name: <input type="text" name="lastName" value="<%= worker.getLastName() %>"><br>
		Email: <input type="text" name="email" value="<%= worker.getEmail() %>"><br>
		Start Date: <input type="text" name="startDate" value="<%= worker.getStart().getTime() %>"><br>
		<input type="hidden" name="id" value="<%= worker.getId() %>">
<%		}				%>
		<input type="submit" value="Submit">
	</form>
</div>