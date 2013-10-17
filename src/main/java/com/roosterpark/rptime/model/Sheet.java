package com.roosterpark.rptime.model;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Sheet {

	@Id
	Long id;
	@Index
	String userId;
	@Index
	Integer week;

	public Sheet() {
	}

	public Sheet(User user, Integer week) {
		this.userId = user.getUserId();
		this.week = week;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

}
