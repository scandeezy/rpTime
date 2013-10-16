package com.roosterpark.rptime;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;

@Singleton
@SuppressWarnings("serial")
public class HistoryServlet extends HttpServlet {

	@Inject
	UserService userService;

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		User user = userService.getCurrentUser();

		if (user != null) {
			resp.setContentType("text/plain");
			resp.getWriter().println("Hello, " + user.getNickname());
		} else {
			resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
		}
		// resp.setContentType("text/plain");
		// resp.getWriter().println("Hello, world");
	}

}
