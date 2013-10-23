package com.roosterpark.rptime;

import java.io.IOException;
import java.util.Calendar;
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

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.roosterpark.rptime.model.TimeSheet;
import com.roosterpark.rptime.model.Worker;
import com.roosterpark.rptime.service.TimeSheetService;
import com.roosterpark.rptime.service.WorkerService;

@Singleton
@SuppressWarnings("serial")
public class TimeSheetServlet extends HttpServlet {
	public static final String TIMESHEET_BUCKET_NAME = "timesheets";
	public static final String WEEK_ATTRIBUTE_KEY = "week";
	public static final String SHEET_DATA_NAME = "sheetData";
	public static final String USER_FIELD_NAME = LandingServlet.USER_FIELD_NAME;
	public static final String KEY_SEP = ".";

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Inject
	UserService userService;

	@Inject
	WorkerService workerService;

	@Inject
	TimeSheetService sheetService;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		User user = userService.getCurrentUser();
		Worker worker = workerService.getByEmail(user.getEmail());
		// TODO externalize into filter
		if (user != null && worker != null) {
			request.setAttribute(USER_FIELD_NAME, user);
		} else {
			response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
		}

		// Pull week from request parameters if specified, default to current week.
		Integer week = Calendar.WEEK_OF_YEAR;
		if (request.getAttribute(WEEK_ATTRIBUTE_KEY) != null) {
			week = Integer.valueOf((String) request.getAttribute(WEEK_ATTRIBUTE_KEY));
		}

		List<TimeSheet> sheets = sheetService.getRecentForWorker(worker.getId(), new Date(), 5);
		LOGGER.warn("Just looked up timesheets by user and date: " + sheets);
		request.setAttribute(SHEET_DATA_NAME, sheets);
		request.setAttribute(USER_FIELD_NAME, user);

		request.getRequestDispatcher("/jsp/sheet/view.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = userService.getCurrentUser();
		// TODO externalize into filter
		if (user == null) {
			response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
		}

	}
}
