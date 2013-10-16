package com.roosterpark.rptime;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;

@Singleton
@SuppressWarnings("serial")
public class UserServlet extends HttpServlet {
	public static final String USER_FIELD_NAME = "user";
	public static final String USER_BUCKET_NAME = "user";
	public static final String REDIRECT_NAME = "redirect";
	public static final String EMAIL_KEY = "email";
	public static final String FIRST_NAME_KEY = "firstName";
	public static final String LAST_NAME_KEY = "lastName";

	@Inject
	DatastoreService datastore;
	@Inject
	UserService userService;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		// TODO Verify that this user has permissions to do this.
		String email;
		if (request.getAttribute(EMAIL_KEY) != null) {
			email = (String) request.getAttribute(EMAIL_KEY);
		} else {
			User user = userService.getCurrentUser();
			email = user.getEmail();
		}

		Entity entity = getUser(email);
		request.getRequestDispatcher("/jsp/rptime.jsp").forward(request, response);
	}

	// Create a user
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// TODO Verify that this user has permissions to do this.
		User user = userService.getCurrentUser();
		String email = (String) request.getAttribute(EMAIL_KEY);
		String fName = (String) request.getAttribute(FIRST_NAME_KEY);
		String lName = (String) request.getAttribute(LAST_NAME_KEY);
		createUser(email, fName, lName);
		response.sendRedirect((String) request.getAttribute(REDIRECT_NAME));
	}

	public static Entity createUser(String email, String fName, String lName) {
		Entity entity = new Entity(USER_BUCKET_NAME, email);

		if (email == null || fName == null || lName == null)
			throw new IllegalArgumentException("No arguments may be null.");

		entity.setProperty(EMAIL_KEY, email);
		entity.setProperty(FIRST_NAME_KEY, fName);
		entity.setProperty(LAST_NAME_KEY, lName);

		return entity;
	}

	public Entity getUser(String email) {
		Key userKey = KeyFactory.createKey(USER_BUCKET_NAME, email);
		Query query = new Query(USER_BUCKET_NAME, userKey);
		Entity entity = datastore.prepare(query).asSingleEntity();

		return entity;
	}
}
