package com.roosterpark.rptime.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.roosterpark.rptime.model.Worker;
import com.roosterpark.rptime.service.WorkerService;

/**
 * 
 * @author scandeezy
 */
@Component("workerFilter")
public class WorkerFilter extends GenericFilterBean {// OncePerRequestFilter implements Filter {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	public static final String WORKER_KEY = "worker";

	// @Autowired
	private static WorkerService workerService;
	// @Autowired
	private static UserService userService;

	public WorkerFilter() {
		LOGGER.debug("init 3, this={}, context={}", this, getServletContext());

	}

	// @Autowired
	// public WorkerFilter(WorkerService workerService, UserService userService) {
	// LOGGER.debug("init 4 for this={}, workerService={}, userService={}", new Object[] { this, workerService, userService });
	// this.workerService = workerService;
	// this.userService = userService;
	// }
	// @Inject
	// public WorkerFilter(WebApplicationContext webApplicationContext) {
	// LOGGER.debug("init.  wac={}", webApplicationContext);
	// }

	// @Inject
	@Autowired
	public void setUserService(UserService svc) {
		if (userService == null) {
			LOGGER.debug("Setting UserService to {} for this={}", svc, this);
			userService = svc;
		}
	}

	// @Inject
	@Autowired
	public void setWorkerService(WorkerService svc) {
		if (workerService == null) {
			LOGGER.debug("Setting WorkerService to {} for this={}", svc, this);
			workerService = svc;
		}
	}

	// @Override
	// public void init(FilterConfig fc) throws ServletException {
	// LOGGER.debug("Initializing WorkerFilter.  fc={},  for this={}", fc, this);
	//
	// }

	// @Override
	// public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException {
	// LOGGER.debug("doing filter  for this={}", this);
	// if (userService != null) {
	// if (userService.isUserLoggedIn()) {
	// User user = userService.getCurrentUser();
	// Worker worker = workerService.getByEmail(user.getEmail());
	// request.setAttribute(WORKER_KEY, worker);
	//
	// } else {
	// LOGGER.debug("User not logged in...");
	// throw new UnsupportedOperationException("Not supported yet.");
	// }
	// } else {
	// LOGGER.error("userService is null");
	// }
	// fc.doFilter(request, response);
	// }

	@Override
	public void destroy() {
		LOGGER.debug("Destroying WorkerFilter");
	}

	@Override
	// protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	// throws ServletException, IOException {
	// public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException,
	// IOException {
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// super.doFilter(request, response, filterChain);
		LOGGER.debug("doing filter  for this={}, workerService={}, userService={}", this, workerService, userService);
		if (userService != null) {
			if (userService.isUserLoggedIn()) {
				User user = userService.getCurrentUser();
				Worker worker = workerService.getByEmail(user.getEmail());
				request.setAttribute(WORKER_KEY, worker);

			} else {
				LOGGER.debug("User not logged in...");
				throw new UnsupportedOperationException("Not supported yet.");
			}
		} else {
			LOGGER.error("userService is null");
		}
		filterChain.doFilter(request, response);
	}

}
