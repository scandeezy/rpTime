package com.roosterpark.rptime.config;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.roosterpark.rptime.model.Worker;
import com.roosterpark.rptime.service.WorkerService;

/**
 * 
 * @author scandeezy
 */
@Named("workerFilter")
public class WorkerFilter implements Filter {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	public static final String WORKER_KEY = "worker";

	@Inject
	private WorkerService workerService;

	@Inject
	private UserService userService;

	public WorkerFilter() {
		LOGGER.debug("init");
	}

	public void setUserService(UserService userService) {
		LOGGER.debug("Setting UserService to {}", userService);
		this.userService = userService;
	}

	public void setWorkerService(WorkerService workerService) {
		LOGGER.debug("Setting WorkerService to {}", workerService);
		this.workerService = workerService;
	}

	@Override
	public void init(FilterConfig fc) throws ServletException {
		LOGGER.debug("Initializing WorkerFilter...");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException {
		if (userService.isUserLoggedIn()) {
			User user = userService.getCurrentUser();
			Worker worker = workerService.getByEmail(user.getEmail());
			request.setAttribute(WORKER_KEY, worker);

			fc.doFilter(request, response);
		} else {
			LOGGER.debug("User '{}' is not logged in.", userService.getCurrentUser());
			// throw new UnsupportedOperationException("Not supported yet.");
		}
	}

	@Override
	public void destroy() {
		LOGGER.debug("Destroying WorkerFilter...");
	}

}
