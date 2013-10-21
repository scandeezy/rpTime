<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.roosterpark.rptime.model.TimeSheet" %>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
  <body>
  	<div>
		<ul>
			<%
				List<TimeSheet> entities = (List<TimeSheet>)request.getAttribute("sheetData");
				String[] days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
				for(TimeSheet sheet : entities)
				{
			%>
				<li>Time sheet id: <%= sheet.getId() %> -- Week: <%= sheet.getWeek() %>
					<ol>
					<%
						Integer dayStart = sheet.getStartDay();
						Integer dayOffset = 0;
						for(Double hours : sheet.getHours())
						{
							String day = days[(dayStart + dayOffset) % 7];
							%>
							<li name="<%= day %>"><%= hours %></li>
							<%
							dayOffset++;
						}
					%>
					</ol>
				</li>
			<%
				}
			%>
		</ul>
	
  	</div>
  </body>
</html>