<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.roosterpark.rptime.SheetServlet" %>
<%@ page import="com.roosterpark.rptime.model.Sheet" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
  <body>
  	<div>
  		Time sheets for user <b><%= request.getAttribute(SheetServlet.USER_FIELD_NAME) %></b>

		<ul>
			<%
				List<Sheet> entities = (List<Sheet>)request.getAttribute(SheetServlet.SHEET_DATA_NAME);
				for(Sheet sheet : entities)
				{
			%>
				<li>Time sheet id: <%= sheet.getId() %> -- Week: <%= sheet.getWeek() %></li>
			<%
				}
			%>
		</ul>
	
  	</div>
  </body>
</html>