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

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.roosterpark.rptime.model.User;
import com.roosterpark.rptime.service.UserService;

@Singleton
@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {
	public static final String USER_KEY = "user";
	public static final String USERS_KEY = "users";
	public static final String FIRST_NAME_KEY = "firstName";
	public static final String LAST_NAME_KEY = "lastName";
	public static final String EMAIL_KEY = "email";
	public static final String START_DATE_KEY = "startDate";
	public static final String ERROR_STRING = "error";
	public static final String COUNT_KEY = "count";
	public static final String OFFSET_KEY = "offset";

	@Inject
	UserService userService;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String key = request.getParameter(USER_KEY);
		if (key != null) {
			User user;
			try {
				user = userService.get(key);
			} catch (EntityNotFoundException e) {
				// TODO log something here
				request.setAttribute(ERROR_STRING, e.getMessage());
				request.getRequestDispatcher("/jsp/users.jsp").forward(request, response);
				return;
			}

			request.setAttribute(USER_KEY, user);

			request.getRequestDispatcher("/jsp/user.jsp").forward(request, response);
		} else {
			Integer count = request.getParameter(COUNT_KEY) == null ? null : Integer.valueOf(request.getParameter(COUNT_KEY));
			Integer offset = request.getParameter(OFFSET_KEY) == null ? null : Integer.valueOf(request.getParameter(OFFSET_KEY));
			count = count == null ? 5 : count;
			offset = offset == null ? 0 : count;

			List<User> companies = userService.getPage(count, offset);
			request.setAttribute(USERS_KEY, companies);

			request.getRequestDispatcher("/jsp/users.jsp").forward(request, response);
		}
	}

	// Create a user
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String firstName = request.getParameter(FIRST_NAME_KEY);
		String lastName = request.getParameter(LAST_NAME_KEY);
		String email = request.getParameter(EMAIL_KEY);
		Date startDate = new Date(Long.valueOf(request.getParameter(START_DATE_KEY)));
		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEmail(email);
		user.setStart(startDate);

		userService.set(user);

		// Redirect back to the view page for this company
		response.sendRedirect("/rptime/user");
	}
}
