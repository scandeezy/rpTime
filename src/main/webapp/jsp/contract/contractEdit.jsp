<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.roosterpark.rptime.model.Contract" %>
<%@ page import="com.roosterpark.rptime.model.Worker" %>
<%@ page import="com.roosterpark.rptime.model.Client" %>
<%@ page import="java.util.List" %>

<%
	Contract contract = (Contract)request.getAttribute("contract");
%>

<div id="createContract">
	<form method="post" action="/rptime/contract">
<%		if(contract == null) {		%>
		Worker: <select name="worker">
<%
			List<Worker> workers = (List<Worker>)request.getAttribute("workers");
			for(Worker worker : workers)
			{
%>
				<option value="<%= worker.getId() %>"><%= worker.getFirstName() + " " + worker.getLastName() %></option>
<%			}			%>
		</select>
		Client: <select name="client">
<%
			List<Client> clients = (List<Client>)request.getAttribute("clients");
			for(Client client : clients)
			{
%>
				<option value="<%= client.getId() %>"><%= client.getName() %></option>
<%			}			%>
		</select>
		Start: <input type="text" name="start"><br>
		End: <input type="text" name="end"><br>
<%		} else {					%>
		Worker: <select name="worker">
<%
			List<Worker> workers = (List<Worker>)request.getAttribute("workers");
			for(Worker worker : workers)
			{
				if(contract.getWorker() == worker.getId()) {
%>
				<option selected value="<%= worker.getId() %>"><%= worker.getFirstName() + " " + worker.getLastName() %></option>
<%				} else {		%>
				<option value="<%= worker.getId() %>"><%= worker.getFirstName() + " " + worker.getLastName() %></option>
<%
				}		
			}			
%>
		</select>
		Client: <select name="client">
<%
			List<Client> clients = (List<Client>)request.getAttribute("clients");
			for(Client client : clients)
			{
				if(contract.getClient() == client.getId()) {
%>
				<option selected value="<%= client.getId() %>"><%= client.getName() %></option>
<%				} else {		%>
				<option value="<%= client.getId() %>"><%= client.getName() %></option>
<%
				}
			}
%>
		</select>
		Start: <input type="text" name="start" value="<%= contract.getStart().getTime() %>"><br>
		End: <input type="text" name="end" value="<%= contract.getEnd().getTime() %>"><br>
		<input type="hidden" name="id" value="<%= contract.getId() %>">
<%		}							%>
		<input type="submit" value="Submit">
	</form>
</div>