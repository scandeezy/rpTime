<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.roosterpark.rptime.model.User" %>
<%@ page import="java.util.List" %>
<html>
	<div id="companies">
		<ul>
<%
	List<User> users = (List<User>)request.getAttribute("users");
	for(User user : users)
	{
%>
			<li>
				<div id="firstName"><%= user.getFirstName() %></div>
				<div id="lastName"><%= user.getLastName() %></div>
				<div id="email"><%= user.getEmail() %></div>
				<div id="startDate"><%= user.getStart() %></div>
			</li>
<%
	}
%>
		</ul>
	</div>
	<div id="createUser">
		<form method="post" action="/rptime/user">
			First Name: <input type="text" name="firstName"><br>
			Last Name: <input type="text" name="lastName"><br>
			Email: <input type="text" name="email"><br>
			Start Date: <input type="text" name="startDate"><br>
			<input type="submit" value="Submit">
		</form>
	</div>
</html>