package com.roosterpark.rptime.admin;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.roosterpark.rptime.model.Client;
import com.roosterpark.rptime.service.ClientService;

@Singleton
@SuppressWarnings("serial")
public class ClientServlet extends HttpServlet {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	public static final String CLIENTS_KEY = "clients";

	public static final String CLIENT_KEY = "client";
	public static final String COUNT_KEY = "count";
	public static final String OFFSET_KEY = "offset";
	public static final String EDIT_PARAM = "edit";
	public static final String ERROR_STRING = "error";

	// Post Field Names
	public static final String ID_KEY = "id";
	public static final String CLIENT_NAME_KEY = "clientName";
	public static final String CLIENT_HEADER_KEY = "clientHeader";
	public static final String CLIENT_PHONE_KEY = "clientPhone";
	public static final String CLIENT_START_KEY = "clientStartDay";

	// JSP Paths
	public static final String CLIENTS_JSP = "/jsp/client/clients.jsp";
	public static final String CLIENT_JSP = "/jsp/client/client.jsp";
	public static final String CLIENT_EDIT_JSP = "/jsp/client/clientEdit.jsp";

	// Servlet Path
	public static final String ROUTE_PATH = "/rptime/client";

	@Inject
	ClientService clientService;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String key = request.getParameter(CLIENT_KEY);
		if (key != null) {
			Long lKey = Long.valueOf(key);
			Client client = clientService.getById(lKey);
			if (client == null) {
				LOGGER.warn("Unable to retrieve record for client " + key);
				// TODO log something here
				request.setAttribute(ERROR_STRING, "Client not found.");
				request.getRequestDispatcher(CLIENTS_JSP).forward(request, response);
				return;
			}

			request.setAttribute(CLIENT_KEY, client);
			String edit = request.getParameter(EDIT_PARAM);
			if (edit != null)
				request.getRequestDispatcher(CLIENT_EDIT_JSP).forward(request, response);
			else
				request.getRequestDispatcher(CLIENT_JSP).forward(request, response);
		} else {
			Integer count = request.getParameter(COUNT_KEY) == null ? null : Integer.valueOf(request.getParameter(COUNT_KEY));
			Integer offset = request.getParameter(OFFSET_KEY) == null ? null : Integer.valueOf(request.getParameter(OFFSET_KEY));
			count = count == null ? 5 : count;
			offset = offset == null ? 0 : count;

			List<Client> companies = clientService.getPage(count, offset);
			request.setAttribute(CLIENTS_KEY, companies);

			request.getRequestDispatcher(CLIENTS_JSP).forward(request, response);
		}
	}

	// Create a user
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String idString = request.getParameter(ID_KEY);
		String clientName = request.getParameter(CLIENT_NAME_KEY);
		String clientHeader = request.getParameter(CLIENT_HEADER_KEY);
		String clientPhone = request.getParameter(CLIENT_PHONE_KEY);
		Integer startDayOfWeek = Integer.valueOf(request.getParameter(CLIENT_START_KEY));
		Client client = new Client();
		if (idString != null && idString.length() > 0)
			client.setId(Long.valueOf(idString));
		client.setName(clientName);
		client.setHeader(clientHeader);
		client.setPhone(clientPhone);
		client.setStartDayOfWeek(startDayOfWeek);

		clientService.set(client);

		// Redirect back to the view page for this client
		response.sendRedirect(ROUTE_PATH);
	}
}
