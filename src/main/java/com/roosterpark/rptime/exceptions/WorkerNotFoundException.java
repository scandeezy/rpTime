package com.roosterpark.rptime.exceptions;

import com.google.appengine.api.users.User;

public class WorkerNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -2507463245207144533L;
	private User user;

	public WorkerNotFoundException(final String msg, User user) {
		super(msg);
		this.user = user;
	}

	public User getUser() {
		return user;
	}

}
