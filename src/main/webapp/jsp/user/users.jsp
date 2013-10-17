<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.roosterpark.rptime.model.User" %>
<%@ page import="java.util.List" %>
<html>
	<div id="users">
		<ul>
<%
	List<User> users = (List<User>)request.getAttribute("users");
	for(User user : users)
	{
		request.setAttribute("user", user);
%>
<jsp:include page="/jsp/user/user.jsp" />
<%
	}
	request.setAttribute("user", null);
%>
		</ul>
	</div>
<jsp:include page="/jsp/user/userEdit.jsp" />
</html>