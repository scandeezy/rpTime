<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.roosterpark.rptime.model.Client" %>
<%@ page import="java.util.List" %>
<html>
<%@ include file="/jsp/headers.jsp"%>
	<div id="clients">
		<ul>
<%
	List<Client> clients = (List<Client>)request.getAttribute("clients");
	for(Client client : clients)
	{
		request.setAttribute("client", client);
%>
<jsp:include page="/jsp/client/client.jsp" />
<%
	}
	request.setAttribute("client", null);
%>
		</ul>
	</div>
<jsp:include page="/jsp/client/clientEdit.jsp" />
<%@ include file="/jsp/scripts.jsp"%>
</html>