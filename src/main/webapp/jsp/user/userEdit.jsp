<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.roosterpark.rptime.model.User" %>

<%
	User user = (User)request.getAttribute("user");
%>
<div id="createUser">
	<form method="post" action="/rptime/user">
<%		if(user == null) {	%>
		First Name: <input type="text" name="firstName"><br>
		Last Name: <input type="text" name="lastName"><br>
		Email: <input type="text" name="email"><br>
		Start Date: <input type="text" name="startDate"><br>
<% 		} else {      	%>
		First Name: <input type="text" name="firstName" value="<%= user.getFirstName() %>"><br>
		Last Name: <input type="text" name="lastName" value="<%= user.getLastName() %>"><br>
		Email: <input type="text" name="email" value="<%= user.getEmail() %>"><br>
		Start Date: <input type="text" name="startDate" value="<%= user.getStart().getTime() %>"><br>
		<input type="hidden" name="id" value="<%= user.getId() %>">
<%		}				%>
		<input type="submit" value="Submit">
	</form>
</div>