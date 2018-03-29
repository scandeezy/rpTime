package com.roosterpark.rptime.model;

import org.apache.commons.lang3.Validate;

import com.google.appengine.api.users.User;
import com.google.common.base.Objects;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class UnlinkedUser {

	@Id
	private String userId;
	private User user;
	private int workerNotFoundExceptionCount;

	public UnlinkedUser() {
		workerNotFoundExceptionCount = 0;
	}

	public UnlinkedUser(User user) {
		this();
		Validate.notNull(user);
		this.user = user;
		this.userId = user.getUserId();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getWorkerNotFoundExceptionCount() {
		return workerNotFoundExceptionCount;
	}

	public void setWorkerNotFoundExceptionCount(int workerNotFoundExceptionCount) {
		this.workerNotFoundExceptionCount = workerNotFoundExceptionCount;
	}

	public void incrementWorkerNotFoundExceptionCount() {
		this.workerNotFoundExceptionCount++;
	}

	public String toString() {
		return Objects.toStringHelper(this)//
				.add("userId", this.userId)//
				.add("user", this.user.hashCode())//
				.add("workerNotFoundExceptionCount", this.workerNotFoundExceptionCount)//
				.toString();
	}

}
