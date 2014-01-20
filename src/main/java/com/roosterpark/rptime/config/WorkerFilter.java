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

	public static final String WORKER_MODEL_ATTRIBUTE_NAME = "worker";

	private WorkerService workerService;
	private UserService userService;

	public WorkerFilter() {
		LOGGER.debug("init");
	}

	@Inject
	public void setUserService(UserService userService) {
		LOGGER.debug("Setting UserService to {}", userService);
		this.userService = userService;
	}

	@Inject
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
		try {
			if (userService.isUserLoggedIn()) {
				User user = userService.getCurrentUser();
				LOGGER.debug("current user '{}' is logged in", user);
				Worker worker = workerService.getByUser(user);
				request.setAttribute(WORKER_MODEL_ATTRIBUTE_NAME, worker);
				LOGGER.debug("set request attribute '{}' to Worker='{}'", WORKER_MODEL_ATTRIBUTE_NAME, worker);
			} else {
				LOGGER.debug("User '{}' is not logged in.", userService.getCurrentUser());
				// throw new UnsupportedOperationException("Not supported yet.");
			}
		} catch (Exception e) {
			LOGGER.warn("Suppressed error:" + e.getMessage(), e);
		} // finally {
		fc.doFilter(request, response);
		// }
	}

	@Override
	public void destroy() {
		LOGGER.debug("Destroying WorkerFilter...");
	}

}
