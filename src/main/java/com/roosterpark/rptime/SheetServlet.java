package com.roosterpark.rptime;

import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;

@Singleton
@SuppressWarnings("serial")
public class SheetServlet extends HttpServlet {
	public static final String TIMESHEET_BUCKET_NAME = "timesheets";
	public static final String WEEK_ATTRIBUTE_KEY = "week";
	public static final String SHEET_DATA_NAME = "sheetData";
	public static final String KEY_SEP = ".";

	@Inject
	UserService userService;
	@Inject
	DatastoreService datastore;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		User user = userService.getCurrentUser();
		// TODO externalize into filter
		if (user != null) {
			request.setAttribute(LandingServlet.USER_FIELD_NAME, user);
		} else {
			response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
		}

		// Pull week from request parameters if specified.
		Integer week;
		if (request.getAttribute(WEEK_ATTRIBUTE_KEY) != null) {
			week = Integer.valueOf((String) request.getAttribute(WEEK_ATTRIBUTE_KEY));
		} else // Default to current week.
		{
			week = Calendar.WEEK_OF_YEAR;
		}
		String email = user.getEmail();
		// String companyId = "";
		Key timeSheetKey = KeyFactory.createKey(TIMESHEET_BUCKET_NAME, email);

		Filter weekFilter = new FilterPredicate(WEEK_ATTRIBUTE_KEY, FilterOperator.EQUAL, week);
		Query query = new Query(TIMESHEET_BUCKET_NAME, timeSheetKey).setFilter(weekFilter).addSort("date", Query.SortDirection.DESCENDING);
		// Setting a max of 5
		List<Entity> sheets = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(5));

		request.setAttribute(SHEET_DATA_NAME, sheets);

		request.getRequestDispatcher("/jsp/sheet/view.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		User user = userService.getCurrentUser();
		// TODO externalize into filter
		if (user == null) {
			response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
		}

	}

	private List<Entity> createTimeSheet(User user, Integer week) {
		List<Entity> entities = new LinkedList<Entity>();
		// Figure out the companies currently active in the user's profile
		datastore.put(entities);

		return entities;
	}
}
