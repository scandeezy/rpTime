<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.roosterpark.rptime.model.Contract" %>

<%
	Contract contract = (Contract)request.getAttribute("contract");
%>

<div id="createContract">
	<form method="post" action="/rptime/contract">
<%		if(contract == null) {		%>
		Worker: <input type="text" name="worker"><br>
		Client: <input type="text" name="client"><br>
		Start: <input type="text" name="start"><br>
		End: <input type="text" name="end"><br>
<%		} else {					%>
		Worker: <input type="text" name="worker" value="<%= contract.getWorker() %>"><br>
		Client: <input type="text" name="client" value="<%= contract.getClient() %>"><br>
		Start: <input type="text" name="start" value="<%= contract.getStart().getTime() %>"><br>
		End: <input type="text" name="end" value="<%= contract.getEnd().getTime() %>"><br>
		<input type="hidden" name="id" value="<%= contract.getId() %>">
<%		}							%>
		<input type="submit" value="Submit">
	</form>
</div>