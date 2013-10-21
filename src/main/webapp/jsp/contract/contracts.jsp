<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.roosterpark.rptime.model.Contract" %>
<%@ page import="java.util.List" %>
<html>
<%@ include file="/jsp/headers.jsp"%>
	<div id="contracts">
		<ul>
<%
	List<Contract> contracts = (List<Contract>)request.getAttribute("contracts");
	for(Contract contract : contracts)
	{
		request.setAttribute("contract", contract);
%>
<jsp:include page="/jsp/contract/contract.jsp" />
<%
	}
	request.setAttribute("contract", null);
%>
		</ul>
	</div>
<jsp:include page="/jsp/contract/contractEdit.jsp" />
<%@ include file="/jsp/scripts.jsp"%>
</html>