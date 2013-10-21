<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.roosterpark.rptime.TimeSheetServlet" %>
<%@ page import="com.roosterpark.rptime.model.TimeSheet" %>
<%@ page import="javax.time.calendar.DayOfWeek" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
  <body>
  	<div>
		<ul>
			<%
				List<TimeSheet> entities = (List<TimeSheet>)request.getAttribute(TimeSheetServlet.SHEET_DATA_NAME);
				for(TimeSheet sheet : entities)
				{
			%>
				<li>Time sheet id: <%= sheet.getId() %> -- Week: <%= sheet.getWeek() %>
					<ol>
					<%
						Integer dayStart = sheet.getStartDay();
						Integer day = 0;
						for(Double hours : sheet.getHours)
						{
							DayOfWeek day = DayOfWeek.of(dayStart + day);
							%>
							<li name="<%= day.name() %>"><%= hours %></li>
							<%
							day++;
						}
					</ol>
				</li>
			<%
				}
			%>
		</ul>
	
  	</div>
  </body>
</html>