<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.roosterpark.rptime.model.Worker" %>
<%@ page import="java.util.List" %>
<html>
<%@ include file="/jsp/headers.jsp"%>
	<div id="workers">
		<ul>
<%
	List<Worker> workers = (List<Worker>)request.getAttribute("workers");
	for(Worker worker : workers)
	{
		request.setAttribute("worker", worker);
%>
<jsp:include page="/jsp/worker/worker.jsp" />
<%
	}
	request.setAttribute("worker", null);
%>
		</ul>
	</div>
<jsp:include page="/jsp/worker/workerEdit.jsp" />
<%@ include file="/jsp/scripts.jsp"%>
</html>