<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.List"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<%@ include file="/jsp/headers.jsp"%>
<body>
	<h1>Hello App Engine!</h1>

	<table>
		<tr>
			<td colspan="2" style="font-weight: bold;">Available Servlets:</td>
		</tr>
		<tr>
			<td><a href="rptime">Rptime</a></td>
		</tr>
	</table>
	
	<hr/>
	
	<%@ include file="/jsp/scripts.jsp"%>
	
</body>
</html>
