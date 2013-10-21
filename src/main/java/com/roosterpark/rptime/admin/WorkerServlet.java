package com.roosterpark.rptime.admin;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.ObjectifyService;
import com.roosterpark.rptime.model.Worker;
import com.roosterpark.rptime.service.WorkerService;

@Singleton
@SuppressWarnings("serial")
public class WorkerServlet extends HttpServlet {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	public static final String WORKERS_KEY = "workers";

	public static final String WORKER_KEY = "worker";
	public static final String ERROR_STRING = "error";
	public static final String COUNT_KEY = "count";
	public static final String OFFSET_KEY = "offset";
	public static final String EDIT_PARAM = "edit";

	// Post Field Names
	public static final String ID_KEY = "id";
	public static final String FIRST_NAME_KEY = "firstName";
	public static final String LAST_NAME_KEY = "lastName";
	public static final String EMAIL_KEY = "email";
	public static final String START_DATE_KEY = "startDate";
	
	// JSP Paths
	public static final String WORKERS_JSP = "/jsp/worker/workers.jsp";
	public static final String WORKER_JSP = "/jsp/worker/worker.jsp";
	public static final String WORKER_EDIT_JSP = "/jsp/worker/workerEdit.jsp";
	
	// Servlet Path
	public static final String ROUTE_PATH = "/rptime/worker";
	
	@Inject
	WorkerService workerService;

	public WorkerServlet() {
		LOGGER.debug("init UserServlet");
		LOGGER.trace("registering User class with ObjectifyService");
		ObjectifyService.register(Worker.class);
		LOGGER.trace("registered User");
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String key = request.getParameter(WORKER_KEY);
		if (key != null) {
			Long lKey = Long.valueOf(key);
			Worker worker = workerService.getById(lKey);
			
			if(worker == null) {
				// TODO log something here
				request.setAttribute(ERROR_STRING, "Worker not found.");
				request.getRequestDispatcher(WORKERS_JSP).forward(request, response);
				return;
			}

			request.setAttribute(WORKER_KEY, worker);
			String edit = request.getParameter(EDIT_PARAM);

			if(edit != null)
				request.getRequestDispatcher(WORKER_EDIT_JSP).forward(request, response);
			else
				request.getRequestDispatcher(WORKER_JSP).forward(request, response);
		} else {
			Integer count = request.getParameter(COUNT_KEY) == null ? null : Integer.valueOf(request.getParameter(COUNT_KEY));
			Integer offset = request.getParameter(OFFSET_KEY) == null ? null : Integer.valueOf(request.getParameter(OFFSET_KEY));
			count = count == null ? 5 : count;
			offset = offset == null ? 0 : count;

			List<Worker> companies = workerService.getPage(count, offset);
			request.setAttribute(WORKERS_KEY, companies);

			request.getRequestDispatcher(WORKERS_JSP).forward(request, response);
		}
	}

	// Create a worker
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String idString = request.getParameter(ID_KEY);
		String firstName = request.getParameter(FIRST_NAME_KEY);
		String lastName = request.getParameter(LAST_NAME_KEY);
		String email = request.getParameter(EMAIL_KEY);
		Date startDate = new Date(Long.valueOf(request.getParameter(START_DATE_KEY)));
		Worker worker = new Worker();
		if(idString != null && idString.length() > 0)
		{
			worker.setId(Long.valueOf(idString));
		}
		worker.setFirstName(firstName);
		worker.setLastName(lastName);
		worker.setEmail(email);
		worker.setStart(startDate);

		workerService.set(worker);

		// Redirect back to the view page for this worker
		response.sendRedirect(ROUTE_PATH);
	}
}
