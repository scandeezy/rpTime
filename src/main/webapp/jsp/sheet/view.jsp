<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>

  <body>
	<%@include file="/jsp/userNav.jsp" %>
	<%
		List<Entity> entities = (List<Entity>)request.getAttribute("sheetData");
		
		for(Entity entity : entities)
		{
	%>
			<li>mooch</li>
	<%
		}
	%>
  </body>
</html>