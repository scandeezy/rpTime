package com.roosterpark.rptime;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class LandingServlet extends HttpServlet {
	public static final String USER_FIELD_NAME = "user";
	public static final String USER_BUCKET_NAME = "user";
	public static final String JSP_LOCATION = "/jsp/rptime.jsp";
	public static final String LOCATION = "/rptime";
	public static final String PAGE_REDIRECT = "page";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        request.setAttribute(PAGE_REDIRECT, LOCATION);
        request.setAttribute(USER_FIELD_NAME, user);
		
        if(user != null)
        {
			Entity userEntity = UserServlet.getUser(user.getEmail());
			request.setAttribute(USER_FIELD_NAME, userEntity);
        }
        
        request.getRequestDispatcher(JSP_LOCATION).forward(request, response);
	}
}
