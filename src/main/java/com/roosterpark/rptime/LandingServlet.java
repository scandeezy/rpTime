package com.roosterpark.rptime;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@Singleton
@SuppressWarnings("serial")
public class LandingServlet extends HttpServlet {
	public static final String USER_FIELD_NAME = "user";
	public static final String USER_ENTITY_NAME = "userEntity";
	public static final String USER_BUCKET_NAME = "user";
	public static final String JSP_LOCATION = "/jsp/rptime.jsp";
	public static final String LOCATION = "/rptime";
	public static final String PAGE_REDIRECT = "page";
	public static final String IS_ADMIN = "admin";

	@Inject
	UserServlet userServlet;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		request.setAttribute(PAGE_REDIRECT, LOCATION);
		UserService userService = UserServiceFactory.getUserService();
		if (userService.isUserLoggedIn()) {
			Boolean admin = userService.isUserAdmin();
			User user = userService.getCurrentUser();

			request.setAttribute(IS_ADMIN, admin);
			request.setAttribute(USER_FIELD_NAME, user);

			if (user != null) {
				Entity userEntity = userServlet.getUser(user.getEmail());
				request.setAttribute(USER_ENTITY_NAME, userEntity);
			}
		}
		request.getRequestDispatcher(JSP_LOCATION).forward(request, response);
	}
}
