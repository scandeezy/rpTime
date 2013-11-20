package com.roosterpark.rptime.model;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import com.google.common.base.Objects;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Worker {
	public static final String EMAIL_KEY = "email";

	@Id
	private Long id;
	@Index
	private String email;
	@Index
	private String firstName;
	@Index
	private String lastName;
	@Index
	private Boolean active;
	@Index
	private LocalDate start;
	@Index
	private Boolean hourly;

	public Worker() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = StringUtils.lowerCase(email);
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getHourly() {
		return this.hourly;
	}

	public void setHourly(Boolean hourly) {
		this.hourly = hourly;
	}

	public String toString() {
		return Objects.toStringHelper(this)//
				.add("id", this.id)//
				.add("firstName", this.firstName)//
				.add("lastName", this.lastName)//
				.add("email", this.email)//
				.add("start", this.start)//
				.toString();
	}
}
