package com.roosterpark.rptime.exceptions;

import org.apache.commons.lang3.ObjectUtils;

import com.google.appengine.api.users.User;
import com.roosterpark.rptime.model.Worker;
import com.roosterpark.rptime.service.UnlinkedUserService;
import com.roosterpark.rptime.service.WorkerService;

/**
 * Indication that a {@link Worker} is not linked to a {@link User}. Invocations log to {@link UnlinkedUserService}.
 * 
 * @author jjzabkar
 */
public class WorkerNotFoundException extends RuntimeException implements ResolvableException {

	private static final long serialVersionUID = -2507463245207144544L;
	private static final String RESOLUTION = "To resolve, an administrator must create a Worker whose email links to User '%s'.";
	private User user;

	public WorkerNotFoundException(final String msg, final User user, final WorkerService workerService) {
		super(msg + " Worker not found for User '" + user + "'.");
		this.user = user;
		workerService.getUnlinkedUserService().logWorkerNotFoundException(user);
	}

	public WorkerNotFoundException(final String msg, final WorkerService workerService) {
		super(msg + " Worker not found for User.");
		this.user = workerService.getUserService().getCurrentUser();
		workerService.getUnlinkedUserService().logWorkerNotFoundException(this.user);
	}

	public User getUser() {
		return user;
	}

	public String getResolution() {
		return String.format(RESOLUTION, ObjectUtils.toString(user));
	}

}
